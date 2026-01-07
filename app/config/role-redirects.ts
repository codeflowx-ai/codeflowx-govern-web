// Configuración de redirecciones específicas por rol después del login
// Esta configuración puede ser modificada desde la interfaz de administración

export interface RoleConfig {
  name: string;
  description: string;
  defaultRedirect: string;
  isActive: boolean;
  color: string;
}

export const roleConfigs: Record<string, RoleConfig> = {
  // Roles de negocio (nuevos)
  agencia: {
    name: "Agencia",
    description: "Acceso a proyectos, generación, conocimiento, evaluación básica y portales",
    defaultRedirect: "/projects",
    isActive: true,
    color: "bg-emerald-100 text-emerald-700",
  },
  it: {
    name: "IT",
    description: "Acceso completo técnico: proyectos, conocimiento, dominios, stacks, modelos, serving, evaluación, MLflow, infraestructura",
    defaultRedirect: "/projects",
    isActive: true,
    color: "bg-blue-100 text-blue-700",
  },
  oem: {
    name: "OEM",
    description: "Acceso a proyectos, portales, serving, SDK y evaluación resumen",
    defaultRedirect: "/projects",
    isActive: true,
    color: "bg-purple-100 text-purple-700",
  },
  business_admin: {
    name: "Business Admin",
    description: "Acceso completo con selector de vista por rol tras login",
    defaultRedirect: "/admin",
    isActive: true,
    color: "bg-red-100 text-red-700",
  },
  
  // Roles técnicos (existentes - mantenidos para compatibilidad)
  admin: {
    name: "Administrador",
    description: "Acceso completo al sistema con capacidades de administración",
    defaultRedirect: "/admin",
    isActive: true,
    color: "bg-red-100 text-red-700",
  },
  developer: {
    name: "Desarrollador",
    description: "Desarrollo de código y funcionalidades técnicas",
    defaultRedirect: "/code-playground",
    isActive: true,
    color: "bg-blue-100 text-blue-700",
  },
  viewer: {
    name: "Visualizador",
    description: "Solo visualización de datos y reportes",
    defaultRedirect: "/plugins",
    isActive: true,
    color: "bg-gray-100 text-gray-700",
  },
  business_analytics: {
    name: "Analista de Negocio",
    description: "Análisis de datos de negocio y métricas",
    defaultRedirect: "/technology-management",
    isActive: true,
    color: "bg-purple-100 text-purple-700",
  },
  ai_analytics: {
    name: "Analista de IA",
    description: "Análisis de modelos de IA y machine learning",
    defaultRedirect: "/training-center",
    isActive: true,
    color: "bg-indigo-100 text-indigo-700",
  },
  ai_developer: {
    name: "Desarrollador de IA",
    description: "Desarrollo de modelos de IA y machine learning",
    defaultRedirect: "/playground/chat",
    isActive: true,
    color: "bg-cyan-100 text-cyan-700",
  },
  devops: {
    name: "DevOps",
    description: "Operaciones de desarrollo e infraestructura",
    defaultRedirect: "/infrastructure",
    isActive: true,
    color: "bg-orange-100 text-orange-700",
  },
  project_manager: {
    name: "Gerente de Proyecto",
    description: "Gestión de proyectos y equipos",
    defaultRedirect: "/projects",
    isActive: true,
    color: "bg-green-100 text-green-700",
  },
  architect: {
    name: "Arquitecto",
    description: "Diseño de arquitecturas y soluciones técnicas",
    defaultRedirect: "/technology-management",
    isActive: true,
    color: "bg-emerald-100 text-emerald-700",
  },
  ai_architect: {
    name: "Arquitecto de IA",
    description: "Diseño de arquitecturas de IA y machine learning",
    defaultRedirect: "/model-management",
    isActive: true,
    color: "bg-pink-100 text-pink-700",
  },
};

export const roleRedirects: Record<string, string> = Object.fromEntries(
  Object.entries(roleConfigs).map(([role, config]) => [
    role,
    config.defaultRedirect,
  ])
);

// Función para obtener la redirección por rol
export const getRedirectByRole = (roles: string[]): string => {
  // Si el usuario tiene múltiples roles, usar el primero
  const primaryRole = roles[0];
  return roleConfigs[primaryRole]?.defaultRedirect || "/dashboard";
};

// Función para obtener la redirección por rol específico
export const getRedirectBySpecificRole = (role: string): string => {
  return roleConfigs[role]?.defaultRedirect || "/dashboard";
};

// Función para obtener todos los roles activos
export const getActiveRoles = (): string[] => {
  return Object.entries(roleConfigs)
    .filter(([_, config]) => config.isActive)
    .map(([role, _]) => role);
};

// Función para obtener configuración de un rol
export const getRoleConfig = (role: string): RoleConfig | undefined => {
  return roleConfigs[role];
};
