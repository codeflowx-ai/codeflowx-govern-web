"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Users, Plus, Eye, Edit, Trash2, X } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

type RoleType = "OWNER" | "STEWARD" | "COMPLIANCE_OFFICER" | "DATA_SCIENTIST" | "REVIEWER" | "APPROVER";
type RoleStatus = "ACTIVE" | "INACTIVE";

interface Role {
  roleId?: number;
  userId: number;
  userName: string;
  roleType: RoleType;
  status: RoleStatus;
}

interface DatasetRoles {
  datasetId: number;
  datasetName: string;
  roles: Role[];
}

export default function RolesOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [roles, setRoles] = useState<DatasetRoles[]>([
    {
      datasetId: 1,
      datasetName: "Customer Dataset v1.0",
      roles: [
        { roleId: 1, userId: 1, userName: "Juan Pérez", roleType: "OWNER", status: "ACTIVE" },
        { roleId: 2, userId: 2, userName: "María García", roleType: "STEWARD", status: "ACTIVE" },
        { roleId: 3, userId: 3, userName: "Carlos López", roleType: "COMPLIANCE_OFFICER", status: "ACTIVE" },
      ],
    },
  ]);

  const [datasets, setDatasets] = useState([
    { id: 1, name: "Customer Dataset v1.0" },
    { id: 2, name: "Product Dataset v2.1" },
    { id: 3, name: "Sales Dataset v1.5" },
  ]);

  const [users, setUsers] = useState([
    { id: 1, name: "Juan Pérez", email: "juan.perez@example.com" },
    { id: 2, name: "María García", email: "maria.garcia@example.com" },
    { id: 3, name: "Carlos López", email: "carlos.lopez@example.com" },
    { id: 4, name: "Ana Martínez", email: "ana.martinez@example.com" },
    { id: 5, name: "Pedro Sánchez", email: "pedro.sanchez@example.com" },
  ]);

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingRole, setEditingRole] = useState<{ datasetId: number; role: Role } | null>(null);
  const [formData, setFormData] = useState({
    datasetId: "",
    userId: "",
    roleType: "" as RoleType | "",
    status: "ACTIVE" as RoleStatus,
  });

  const roleNames: Record<RoleType, string> = {
    OWNER: "Propietario",
    STEWARD: "Data Steward",
    COMPLIANCE_OFFICER: "Compliance Officer",
    DATA_SCIENTIST: "Data Scientist",
    REVIEWER: "Revisor",
    APPROVER: "Aprobador",
  };

  useEffect(() => {
    loadRoles();
    loadDatasets();
    loadUsers();
  }, []);

  const loadRoles = async () => {
    try {
      // TODO: Llamar al backend real
      // const response = await fetch("/api/v1/governance/data/roles");
      // const data = await response.json();
      // setRoles(data);
    } catch (error) {
      console.error("Error loading roles:", error);
    }
  };

  const loadDatasets = async () => {
    try {
      // TODO: Llamar al backend real
      // const response = await fetch("/api/v1/governance/data/datasets");
      // const data = await response.json();
      // setDatasets(data);
    } catch (error) {
      console.error("Error loading datasets:", error);
    }
  };

  const loadUsers = async () => {
    try {
      // TODO: Llamar al backend real
      // const response = await fetch("/api/v1/users");
      // const data = await response.json();
      // setUsers(data);
    } catch (error) {
      console.error("Error loading users:", error);
    }
  };

  const handleOpenDialog = (datasetId?: number, role?: Role) => {
    if (datasetId && role) {
      setEditingRole({ datasetId, role });
      setFormData({
        datasetId: datasetId.toString(),
        userId: role.userId.toString(),
        roleType: role.roleType,
        status: role.status,
      });
    } else {
      setEditingRole(null);
      setFormData({
        datasetId: datasetId?.toString() || "",
        userId: "",
        roleType: "" as RoleType | "",
        status: "ACTIVE",
      });
    }
    setIsDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setIsDialogOpen(false);
    setEditingRole(null);
    setFormData({
      datasetId: "",
      userId: "",
      roleType: "" as RoleType | "",
      status: "ACTIVE",
    });
  };

  const handleSaveRole = async () => {
    if (!formData.datasetId || !formData.userId || !formData.roleType) {
      alert("Por favor complete todos los campos requeridos");
      return;
    }

    try {
      const datasetId = parseInt(formData.datasetId);
      const userId = parseInt(formData.userId);
      const user = users.find((u) => u.id === userId);

      if (editingRole) {
        // Actualizar rol existente
        setRoles((prev) =>
          prev.map((item) => {
            if (item.datasetId === datasetId) {
              return {
                ...item,
                roles: item.roles.map((r) =>
                  r.roleId === editingRole.role.roleId
                    ? {
                        ...r,
                        userId,
                        userName: user?.name || "",
                        roleType: formData.roleType as RoleType,
                        status: formData.status,
                      }
                    : r
                ),
              };
            }
            return item;
          })
        );
      } else {
        // Crear nuevo rol
        const newRole: Role = {
          roleId: Date.now(), // Temporal, en producción vendría del backend
          userId,
          userName: user?.name || "",
          roleType: formData.roleType as RoleType,
          status: formData.status,
        };

        setRoles((prev) => {
          const existing = prev.find((item) => item.datasetId === datasetId);
          if (existing) {
            return prev.map((item) =>
              item.datasetId === datasetId
                ? { ...item, roles: [...item.roles, newRole] }
                : item
            );
          } else {
            const dataset = datasets.find((d) => d.id === datasetId);
            return [...prev, { datasetId, datasetName: dataset?.name || "", roles: [newRole] }];
          }
        });
      }

      // TODO: Llamar al backend real
      // await fetch("/api/v1/governance/data/roles", {
      //   method: editingRole ? "PUT" : "POST",
      //   headers: { "Content-Type": "application/json" },
      //   body: JSON.stringify(formData),
      // });

      handleCloseDialog();
    } catch (error) {
      console.error("Error saving role:", error);
      alert("Error al guardar el rol");
    }
  };

  const handleDeleteRole = async (datasetId: number, roleId: number) => {
    if (!confirm("¿Está seguro de eliminar este rol?")) {
      return;
    }

    try {
      setRoles((prev) =>
        prev.map((item) => {
          if (item.datasetId === datasetId) {
            return {
              ...item,
              roles: item.roles.filter((r) => r.roleId !== roleId),
            };
          }
          return item;
        })
      );

      // TODO: Llamar al backend real
      // await fetch(`/api/v1/governance/data/roles/${roleId}`, {
      //   method: "DELETE",
      // });
    } catch (error) {
      console.error("Error deleting role:", error);
      alert("Error al eliminar el rol");
    }
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.roles.title", "Gestión Centralizada de Roles")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.roles.description", "Vista centralizada de todos los roles asignados en todos los datasets. Para gestionar roles de un dataset específico, vaya al detalle del dataset.")}
          </p>
        </div>
        <div className="flex gap-2">
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button onClick={() => handleOpenDialog()}>
                <Plus className="h-4 w-4 mr-2" />
                Asignar Rol
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-md">
              <DialogHeader>
                <DialogTitle>
                  {editingRole ? "Editar Rol" : "Asignar Nuevo Rol"}
                </DialogTitle>
              </DialogHeader>
              <div className="space-y-4 mt-4">
                <div>
                  <Label htmlFor="dataset">Dataset *</Label>
                  <Select
                    value={formData.datasetId}
                    onValueChange={(value) => setFormData({ ...formData, datasetId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar dataset" />
                    </SelectTrigger>
                    <SelectContent>
                      {datasets.map((dataset) => (
                        <SelectItem key={dataset.id} value={dataset.id.toString()}>
                          {dataset.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="user">Usuario *</Label>
                  <Select
                    value={formData.userId}
                    onValueChange={(value) => setFormData({ ...formData, userId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar usuario" />
                    </SelectTrigger>
                    <SelectContent>
                      {users.map((user) => (
                        <SelectItem key={user.id} value={user.id.toString()}>
                          {user.name} ({user.email})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="roleType">Tipo de Rol *</Label>
                  <Select
                    value={formData.roleType}
                    onValueChange={(value) => setFormData({ ...formData, roleType: value as RoleType })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Seleccionar tipo de rol" />
                    </SelectTrigger>
                    <SelectContent>
                      {Object.entries(roleNames).map(([key, name]) => (
                        <SelectItem key={key} value={key}>
                          {name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="status">Estado</Label>
                  <Select
                    value={formData.status}
                    onValueChange={(value) => setFormData({ ...formData, status: value as RoleStatus })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="ACTIVE">Activo</SelectItem>
                      <SelectItem value="INACTIVE">Inactivo</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="flex justify-end gap-2 pt-4">
                  <Button variant="outline" onClick={handleCloseDialog}>
                    Cancelar
                  </Button>
                  <Button onClick={handleSaveRole}>
                    {editingRole ? "Actualizar" : "Asignar"}
                  </Button>
                </div>
              </div>
            </DialogContent>
          </Dialog>
          <Button variant="outline" onClick={() => router.push("/governance/data/dashboard")}>
            Ver Dashboard
          </Button>
        </div>
      </div>

      <div className="space-y-4">
        {roles.length === 0 ? (
          <Card>
            <CardBody className="text-center py-8 text-muted-foreground">
              No hay roles asignados. Haga clic en "Asignar Rol" para comenzar.
            </CardBody>
          </Card>
        ) : (
          roles.map((item) => (
            <Card key={item.datasetId}>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>{item.datasetName}</CardTitle>
                  <div className="flex gap-2">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleOpenDialog(item.datasetId)}
                    >
                      <Plus className="h-4 w-4 mr-2" />
                      Asignar Rol
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => router.push(`/governance/data/datasets/${item.datasetId}`)}
                    >
                      <Eye className="h-4 w-4 mr-2" />
                      Ver Dataset
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardBody>
                {item.roles.length === 0 ? (
                  <div className="text-center py-4 text-muted-foreground">
                    No hay roles asignados para este dataset.
                  </div>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    {item.roles.map((role) => (
                      <div key={role.roleId} className="p-3 border rounded-lg relative group">
                        <div className="flex items-center justify-between mb-2">
                          <span className="font-medium">{role.userName}</span>
                          <Badge variant="outline">{roleNames[role.roleType] || role.roleType}</Badge>
                        </div>
                        <p className="text-xs text-muted-foreground mb-2">
                          Estado:{" "}
                          <Badge
                            className={`text-xs ${
                              role.status === "ACTIVE"
                                ? "bg-green-100 dark:bg-green-900/40 text-green-800 dark:text-green-200"
                                : "bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-200"
                            }`}
                          >
                            {role.status}
                          </Badge>
                        </p>
                        <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity flex gap-1">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-6 w-6 p-0"
                            onClick={() => handleOpenDialog(item.datasetId, role)}
                            title="Editar rol"
                          >
                            <Edit className="h-3 w-3" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-6 w-6 p-0 text-red-600 hover:text-red-700"
                            onClick={() => handleDeleteRole(item.datasetId, role.roleId!)}
                            title="Eliminar rol"
                          >
                            <Trash2 className="h-3 w-3" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardBody>
            </Card>
          ))
        )}
      </div>
    </div>
  );
}
