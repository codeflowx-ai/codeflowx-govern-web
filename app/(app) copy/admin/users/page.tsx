"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Users,
  Plus,
  Search,
  Trash2,
  Edit,
  CheckCircle,
  AlertCircle,
  Lock,
} from "lucide-react";
import { useState, useEffect } from "react";

interface User {
  id: number;
  idxuser: number;
  passwordhash: string;
  firstname?: string;
  lastname?: string;
  lastlogin?: string;
  createdat: string;
  updatedat: string;
  username?: string;
  email?: string;
  phone?: string;
  isactive?: boolean;
  islocked?: boolean;
  failedlogins?: number;
  departmentid?: number;
}

export default function UsersPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [users, setUsers] = useState<User[]>([]);
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
      const mockData: User[] = [
        {
          id: 1,
          idxuser: 1,
          passwordhash: "***",
          firstname: "John",
          lastname: "Doe",
          lastlogin: "2024-01-22T10:00:00Z",
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          username: "jdoe",
          email: "john.doe@example.com",
          phone: "+1234567890",
          isactive: true,
          islocked: false,
          failedlogins: 0,
          departmentid: 1,
        },
        {
          id: 2,
          idxuser: 2,
          passwordhash: "***",
          firstname: "Jane",
          lastname: "Smith",
          lastlogin: "2024-01-21T15:30:00Z",
          createdat: "2024-01-10T09:00:00Z",
          updatedat: "2024-01-18T16:00:00Z",
          username: "jsmith",
          email: "jane.smith@example.com",
          phone: "+1234567891",
          isactive: true,
          islocked: false,
          failedlogins: 0,
          departmentid: 2,
        },
        {
          id: 3,
          idxuser: 3,
          passwordhash: "***",
          firstname: "Bob",
          lastname: "Johnson",
          lastlogin: "2024-01-20T08:00:00Z",
          createdat: "2024-01-05T08:00:00Z",
          updatedat: "2024-01-15T12:00:00Z",
          username: "bjohnson",
          email: "bob.johnson@example.com",
          phone: "+1234567892",
          isactive: false,
          islocked: true,
          failedlogins: 5,
          departmentid: 3,
        },
      ];

      const filtered = searchTerm
        ? mockData.filter(
            (user) =>
              user.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              user.firstname?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              user.lastname?.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setActiveItems(filtered.filter((u) => u.isactive).length);
      setInactiveItems(filtered.filter((u) => !u.isactive).length);
      setPendingApproval(0);

      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setUsers(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading users:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este usuario?")) {
      setUsers(users.filter((u) => u.id !== id));
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
            <Users className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.users.title", "Gestión de Usuarios")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("core.users.description", "Gestiona usuarios del sistema")}
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

        <Card className="bg-card border border-border shadow-sm">
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
                onClick={() => window.location.href = "/admin/users/new"}
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("core.users.register", "Registrar Usuario")}
              </Button>
            </div>
          </CardHeader>
        </Card>

        <Card className="bg-card border border-border shadow-sm">
          <CardHeader>
            <CardTitle>{t("core.users.list", "Lista de Usuarios")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : users.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron usuarios
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
                        Usuario
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Nombre
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Email
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Último Login
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Estado
                      </th>
                      <th className="text-right p-3 text-sm font-medium text-muted-foreground">
                        {t("common.actions", "Acciones")}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr
                        key={user.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{user.idxuser}</td>
                        <td className="p-3">
                          <div className="font-medium">{user.username}</div>
                        </td>
                        <td className="p-3">
                          <div className="text-sm">
                            {user.firstname} {user.lastname}
                          </div>
                        </td>
                        <td className="p-3 text-sm">{user.email}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {user.lastlogin ? formatDate(user.lastlogin) : "Nunca"}
                        </td>
                        <td className="p-3">
                          <div className="flex gap-2">
                            <Badge
                              variant={user.isactive ? "primary" : "secondary"}
                              className={
                                user.isactive
                                  ? "bg-muted text-foreground"
                                  : "bg-muted/50 text-muted-foreground"
                              }
                            >
                              {user.isactive ? (
                                <>
                                  <CheckCircle className="w-3 h-3 mr-1" />
                                  Activo
                                </>
                              ) : (
                                <>
                                  <AlertCircle className="w-3 h-3 mr-1" />
                                  Inactivo
                                </>
                              )}
                            </Badge>
                            {user.islocked && (
                              <Badge variant="secondary" className="bg-muted/50 text-muted-foreground">
                                <Lock className="w-3 h-3 mr-1" />
                                Bloqueado
                              </Badge>
                            )}
                          </div>
                        </td>
                        <td className="p-3">
                          <div className="flex items-center justify-end gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => window.location.href = `/admin/users/${user.idxuser}`}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDelete(user.id)}
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
                    Mostrando {users.length} de {totalItems} usuarios
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


