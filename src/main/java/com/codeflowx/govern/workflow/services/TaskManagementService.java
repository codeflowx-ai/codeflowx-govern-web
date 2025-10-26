package com.codeflowx.govern.workflow.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeflowx.admin.Ssorol;
import com.codeflowx.admin.Ssoruserol;
import com.codeflowx.admin.Ssousuario;
import com.codeflowx.bpmn.Bpmmonitor;

import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;

/**
 * Service: Task Management
 * 
 * Integra Flowable BPMN con sistema de usuarios/roles existente:
 * - Ssousuario, Ssorol, Ssoruserol
 * - Bpmmonitor (monitorización)
 * 
 * Funcionalidades:
 * - Obtener tareas pendientes por usuario/rol
 * - Asignar tarea a usuario específico
 * - Claim (reclamar) tarea
 * - Completar tarea
 * - Historial de tareas
 */
@Slf4j
@Service
public class TaskManagementService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private BusinessService businessService;

    /**
     * Obtener tareas pendientes para un usuario.
     * 
     * Incluye:
     * - Tareas asignadas directamente al usuario
     * - Tareas asignadas a candidateGroups (roles del usuario)
     */
    public List<TaskDTO> getPendingTasksForUser(String username) {
        try {
            // 1. Obtener usuario
            Ssousuario user = getUserByUsername(username);
            if (user == null) {
                log.warn("⚠️ Usuario no encontrado: {}", username);
                return Collections.emptyList();
            }

            // 2. Obtener roles del usuario
            List<String> userRoles = getUserRoles(user.getIdxssousuario());
            log.info("📋 Usuario: {} tiene roles: {}", username, userRoles);

            // 3. Tareas asignadas directamente
            List<Task> assignedTasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc()
                .list();

            // 4. Tareas de candidateGroups (roles)
            List<Task> groupTasks = new ArrayList<>();
            for (String role : userRoles) {
                List<Task> tasks = taskService.createTaskQuery()
                    .taskCandidateGroup(role)
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
                groupTasks.addAll(tasks);
            }

            // 5. Combinar y convertir a DTO
            Set<Task> allTasks = new HashSet<>();
            allTasks.addAll(assignedTasks);
            allTasks.addAll(groupTasks);

            return allTasks.stream()
                .map(this::convertToDTO)
                .sorted(Comparator.comparing(TaskDTO::getPriority).reversed()
                    .thenComparing(TaskDTO::getCreateTime).reversed())
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("❌ Error obteniendo tareas para usuario {}: {}", username, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Obtener todas las tareas pendientes (para admin)
     */
    public List<TaskDTO> getAllPendingTasks() {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                .orderByTaskPriority()
                .desc()
                .list();

            return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("❌ Error obteniendo todas las tareas: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Asignar tarea a usuario específico
     */
    public boolean assignTaskToUser(String taskId, String username) {
        try {
            // Verificar que usuario existe
            Ssousuario user = getUserByUsername(username);
            if (user == null) {
                log.error("❌ Usuario no encontrado: {}", username);
                return false;
            }

            // Asignar en Flowable
            taskService.setAssignee(taskId, username);
            
            // Registrar en Bpmmonitor
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                registerTaskAssignment(task, username);
            }

            log.info("✅ Tarea {} asignada a {}", taskId, username);
            return true;

        } catch (Exception e) {
            log.error("❌ Error asignando tarea {}: {}", taskId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Claim (reclamar) tarea de candidateGroup
     */
    public boolean claimTask(String taskId, String username) {
        try {
            taskService.claim(taskId, username);
            
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                registerTaskAssignment(task, username);
            }

            log.info("✅ Tarea {} reclamada por {}", taskId, username);
            return true;

        } catch (Exception e) {
            log.error("❌ Error reclamando tarea {}: {}", taskId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Liberar tarea (unclaim)
     */
    public boolean releaseTask(String taskId) {
        try {
            taskService.unclaim(taskId);
            log.info("✅ Tarea {} liberada", taskId);
            return true;

        } catch (Exception e) {
            log.error("❌ Error liberando tarea {}: {}", taskId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Obtener usuarios que pueden ejecutar una tarea
     * (basándose en candidateGroups)
     */
    public List<UserDTO> getUsersForTask(String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                return Collections.emptyList();
            }

            // Obtener candidateGroups de la tarea
            List<String> candidateGroups = taskService.getIdentityLinksForTask(taskId).stream()
                .filter(link -> "candidate".equals(link.getType()))
                .filter(link -> link.getGroupId() != null)
                .map(org.flowable.identitylink.api.IdentityLink::getGroupId)
                .collect(Collectors.toList());

            if (candidateGroups.isEmpty()) {
                return Collections.emptyList();
            }

            log.info("📋 Tarea {} tiene candidateGroups: {}", taskId, candidateGroups);

            // Obtener usuarios de esos roles
            List<UserDTO> users = new ArrayList<>();
            for (String roleGroupName : candidateGroups) {
                users.addAll(getUsersByRoleName(roleGroupName));
            }

            return users.stream()
                .distinct()
                .sorted(Comparator.comparing(UserDTO::getFullname))
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("❌ Error obteniendo usuarios para tarea {}: {}", taskId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    // ===============================================
    // PRIVATE HELPERS
    // ===============================================

    private Ssousuario getUserByUsername(String username) {
        try {
            String sql = "SELECT * FROM SSOUSUARIO WHERE USUARIO = :username";
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("username", username);
            List<Ssousuario> users = businessService.findByParams(Ssousuario.class, sql, params);
            return users != null && !users.isEmpty() ? users.get(0) : null;
        } catch (Exception e) {
            log.error("Error buscando usuario: {}", username, e);
            return null;
        }
    }

    private List<String> getUserRoles(Long userId) {
        try {
            String sql = "SELECT * FROM SSORUSEROL WHERE URLIDXUSER = :userId";
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("userId", userId);
            List<Ssoruserol> userRoles = businessService.findByParams(Ssoruserol.class, sql, params);
            
            if (userRoles == null || userRoles.isEmpty()) {
                return Collections.emptyList();
            }
            
            return userRoles.stream()
                .map(ur -> ur.getIdssorol())
                .filter(Objects::nonNull)
                .map(Ssorol::getRol)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error obteniendo roles de usuario: {}", userId, e);
            return Collections.emptyList();
        }
    }

    private List<UserDTO> getUsersByRoleName(String roleName) {
        try {
            // 1. Buscar rol por nombre
            String sqlRole = "SELECT * FROM SSOROL WHERE ROL = :roleName";
            java.util.Map<String, Object> paramsRole = new java.util.HashMap<>();
            paramsRole.put("roleName", roleName);
            List<Ssorol> roles = businessService.findByParams(Ssorol.class, sqlRole, paramsRole);
            
            if (roles == null || roles.isEmpty()) {
                return Collections.emptyList();
            }

            Ssorol role = roles.get(0);
            
            // 2. Buscar usuarios con ese rol
            String sqlUserRole = "SELECT * FROM SSORUSEROL WHERE URLIDXROLE = :roleId";
            java.util.Map<String, Object> paramsUserRole = new java.util.HashMap<>();
            paramsUserRole.put("roleId", role.getIdxssorol());
            List<Ssoruserol> userRoles = businessService.findByParams(Ssoruserol.class, sqlUserRole, paramsUserRole);

            if (userRoles == null || userRoles.isEmpty()) {
                return Collections.emptyList();
            }

            return userRoles.stream()
                .map(ur -> ur.getIdssousuario())
                .filter(Objects::nonNull)
                .filter(u -> !u.isLocked())
                .map(u -> new UserDTO(
                    u.getIdxssousuario(),
                    u.getUsuario(),
                    u.getEmail(),
                    u.getNombre() + " " + (u.getApellidos() != null ? u.getApellidos() : "")
                ))
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error obteniendo usuarios por rol: {}", roleName, e);
            return Collections.emptyList();
        }
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setAssignee(task.getAssignee());
        dto.setOwner(task.getOwner());
        dto.setPriority(task.getPriority());
        dto.setCreateTime(task.getCreateTime());
        dto.setDueDate(task.getDueDate());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setFormKey(task.getFormKey());

        // Obtener candidateGroups
        List<String> candidateGroups = taskService.getIdentityLinksForTask(task.getId()).stream()
            .filter(link -> "candidate".equals(link.getType()))
            .filter(link -> link.getGroupId() != null)
            .map(org.flowable.identitylink.api.IdentityLink::getGroupId)
            .collect(Collectors.toList());
        dto.setCandidateGroups(candidateGroups);

        return dto;
    }

    private void registerTaskAssignment(Task task, String username) {
        try {
            Bpmmonitor monitor = new Bpmmonitor();
            monitor.setUsername(username);
            monitor.setProcesskey(task.getProcessDefinitionId());
            monitor.setInstanceprocess(task.getProcessInstanceId());
            monitor.setInstancetask(task.getId());
            monitor.setNametask(task.getName());
            monitor.setTaskey(task.getTaskDefinitionKey());
            monitor.setStart(new Timestamp(System.currentTimeMillis()));
            monitor.setAlta(new Timestamp(System.currentTimeMillis()));

            businessService.save(monitor);
            log.info("✅ Registro en Bpmmonitor creado para tarea {} → usuario {}", task.getId(), username);

        } catch (Exception e) {
            log.error("❌ Error registrando en Bpmmonitor: {}", e.getMessage(), e);
        }
    }

    // ===============================================
    // DTOs
    // ===============================================

    public static class TaskDTO {
        private String id;
        private String name;
        private String description;
        private String assignee;
        private String owner;
        private String priority;
        private Date createTime;
        private Date dueDate;
        private String processInstanceId;
        private String processDefinitionId;
        private String formKey;
        private List<String> candidateGroups;
        private Map<String, Object> processVariables;
        // Getters y Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAssignee() { return assignee; }
        public void setAssignee(String assignee) { this.assignee = assignee; }

        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }

        public String getPriority() { return priority; }
        public void setPriority(String priority2) { this.priority = priority2; }

        public Date getCreateTime() { return createTime; }
        public void setCreateTime(Date createTime) { this.createTime = createTime; }

        public Date getDueDate() { return dueDate; }
        public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

        public String getProcessInstanceId() { return processInstanceId; }
        public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }

        public String getProcessDefinitionId() { return processDefinitionId; }
        public void setProcessDefinitionId(String processDefinitionId) { this.processDefinitionId = processDefinitionId; }

        public String getFormKey() { return formKey; }
        public void setFormKey(String formKey) { this.formKey = formKey; }

        public List<String> getCandidateGroups() { return candidateGroups; }
        public void setCandidateGroups(List<String> candidateGroups) { this.candidateGroups = candidateGroups; }
		public void setProcessVariables(Map<String, Object> vars) {
			this.processVariables=vars;
			
		}
		public Map getProcessVariables() {
			return processVariables;
		}
    }

    public static class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String fullname;

        public UserDTO(Long id, String username, String email, String fullname) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.fullname = fullname;
        }

        // Getters
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getFullname() { return fullname; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserDTO userDTO = (UserDTO) o;
            return Objects.equals(id, userDTO.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}

