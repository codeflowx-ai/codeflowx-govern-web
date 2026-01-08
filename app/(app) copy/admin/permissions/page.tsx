"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Key,
  Plus,
  Search,
  Trash2,
  Edit,
} from "lucide-react";
import { useState, useEffect } from "react";

interface Permission {
  id: number;
  idxpermission: number;
  createdat: string;
  updatedat: string;
  permissionname: string;
  resource?: string;
}

export default function PermissionsPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [inactiveItems, setInactiveItems] = useState(0);
  const [pendingApproval, setPendingApproval] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    loadData();
  }, [currentPage, searchTerm]);

  const loadData = async () => {
    setLoading(true);
    try {
      const mockData: Permission[] = [
        {
          id: 1,
          idxpermission: 1,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          permissionname: "read:users",
          resource: "/api/users",
        },
        {
          id: 2,
          idxpermission: 2,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          permissionname: "write:users",
          resource: "/api/users",
        },
        {
          id: 3,
          idxpermission: 3,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          permissionname: "delete:users",
          resource: "/api/users",
        },
        {
          id: 4,
          idxpermission: 4,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          permissionname: "read:departments",
          resource: "/api/departments",
        },
      ];

      const filtered = searchTerm
        ? mockData.filter(
            (perm) =>
              perm.permissionname
                .toLowerCase()
                .includes(searchTerm.toLowerCase()) ||
              perm.resource?.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setActiveItems(filtered.length);
      setInactiveItems(0);
      setPendingApproval(0);

      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setPermissions(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading permissions:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este permiso?")) {
      setPermissions(permissions.filter((p) => p.id !== id));
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6">
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Key className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.permissions.title", "Gestión de Permisos")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("core.permissions.description", "Gestiona los permisos del sistema")}
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Total</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{totalItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Activos</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{activeItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Pendientes</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{pendingApproval}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Inactivos</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{inactiveItems}</div>
            </CardContent>
          </Card>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
          <CardHeader>
            <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
              <div className="flex-1 w-full md:w-auto">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                  <Input
                    placeholder={t("common.search", "Buscar...")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              <Button 
                className="w-full md:w-auto"
                onClick={() => window.location.href = "/admin/permissions/new"}
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("core.permissions.register", "Registrar Permiso")}
              </Button>
            </div>
          </CardHeader>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
          <CardHeader>
            <CardTitle>{t("core.permissions.list", "Lista de Permisos")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : permissions.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron permisos
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border">
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        ID
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Nombre
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Recurso
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Creado
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Actualizado
                      </th>
                      <th className="text-right p-3 text-sm font-medium text-muted-foreground">
                        {t("common.actions", "Acciones")}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {permissions.map((perm) => (
                      <tr
                        key={perm.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{perm.idxpermission}</td>
                        <td className="p-3 font-medium">{perm.permissionname}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {perm.resource || "-"}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(perm.createdat)}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(perm.updatedat)}
                        </td>
                        <td className="p-3">
                          <div className="flex items-center justify-end gap-2">
                            <Button 
                              variant="ghost" 
                              size="sm"
                              onClick={() => window.location.href = `/admin/permissions/${perm.idxpermission}`}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDelete(perm.id)}
                              className="text-destructive hover:text-destructive"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>

                <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
                  <div className="text-sm text-muted-foreground">
                    Mostrando {permissions.length} de {totalItems} permisos
                  </div>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                      disabled={currentPage === 1}
                    >
                      Anterior
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() =>
                        setCurrentPage(
                          Math.min(
                            Math.ceil(totalItems / itemsPerPage),
                            currentPage + 1
                          )
                        )
                      }
                      disabled={
                        currentPage >= Math.ceil(totalItems / itemsPerPage)
                      }
                    >
                      Siguiente
                    </Button>
                  </div>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


