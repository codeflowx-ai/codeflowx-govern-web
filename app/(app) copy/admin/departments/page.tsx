"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Building2,
  Plus,
  Search,
  Trash2,
  Edit,
  X,
  CheckCircle,
  AlertCircle,
} from "lucide-react";
import { useState, useEffect } from "react";

interface Department {
  id: number;
  idxdepartment: number;
  createdat: string;
  updatedat: string;
  corname: string;
  code: string;
  description?: string;
  managerid?: number;
  parentid?: number;
  isactive?: boolean;
  subcorusers?: number;
}

export default function DepartmentsPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [departments, setDepartments] = useState<Department[]>([]);
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
      // Mock data - En producción esto vendría de la API
      const mockData: Department[] = [
        {
          id: 1,
          idxdepartment: 1,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          corname: "AI Development",
          code: "AIDEV",
          description: "Equipo de desarrollo de inteligencia artificial",
          managerid: 1,
          parentid: undefined,
          isactive: true,
          subcorusers: 24,
        },
        {
          id: 2,
          idxdepartment: 2,
          createdat: "2024-01-10T09:00:00Z",
          updatedat: "2024-01-18T16:00:00Z",
          corname: "Data Science",
          code: "DATASCI",
          description: "Análisis de datos y machine learning",
          managerid: 2,
          parentid: undefined,
          isactive: true,
          subcorusers: 18,
        },
        {
          id: 3,
          idxdepartment: 3,
          createdat: "2024-01-05T08:00:00Z",
          updatedat: "2024-01-15T12:00:00Z",
          corname: "DevOps & Infrastructure",
          code: "DEVOPS",
          description: "Operaciones y infraestructura cloud",
          managerid: 3,
          parentid: undefined,
          isactive: true,
          subcorusers: 15,
        },
        {
          id: 4,
          idxdepartment: 4,
          createdat: "2024-01-20T11:00:00Z",
          updatedat: "2024-01-22T10:00:00Z",
          corname: "Quality Assurance",
          code: "QA",
          description: "Control de calidad y testing",
          managerid: 4,
          parentid: undefined,
          isactive: false,
          subcorusers: 8,
        },
      ];

      // Filtrar por término de búsqueda
      const filtered = searchTerm
        ? mockData.filter(
            (dept) =>
              dept.corname.toLowerCase().includes(searchTerm.toLowerCase()) ||
              dept.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
              dept.description?.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setActiveItems(filtered.filter((d) => d.isactive).length);
      setInactiveItems(filtered.filter((d) => !d.isactive).length);
      setPendingApproval(0);

      // Paginación
      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setDepartments(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading departments:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este departamento?")) {
      // En producción, llamar a la API
      setDepartments(departments.filter((d) => d.id !== id));
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
      {/* Partículas flotantes animadas */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Building2 className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.departments.title", "Gestión de Departamentos")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "core.departments.description",
              "Gestiona departamentos, equipos y recursos organizacionales"
            )}
          </p>
        </div>

        {/* Métricas Resumen */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <CardDescription className="text-sm text-muted-foreground">
                Total
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{totalItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <CardDescription className="text-sm text-muted-foreground">
                Activos
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{activeItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <CardDescription className="text-sm text-muted-foreground">
                Pendientes
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{pendingApproval}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <CardDescription className="text-sm text-muted-foreground">
                Inactivos
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{inactiveItems}</div>
            </CardContent>
          </Card>
        </div>

        {/* Barra de búsqueda y acciones */}
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
                onClick={() => window.location.href = "/admin/departments/new"}
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("core.departments.register", "Registrar Departamento")}
              </Button>
            </div>
          </CardHeader>
        </Card>

        {/* Tabla de departamentos */}
        <Card className="bg-card border border-border shadow-sm">
          <CardHeader>
            <CardTitle>{t("core.departments.list", "Lista de Departamentos")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : departments.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron departamentos
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
                        Código
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Creado
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Actualizado
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
                    {departments.map((dept) => (
                      <tr
                        key={dept.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{dept.idxdepartment}</td>
                        <td className="p-3">
                          <div className="font-medium">{dept.corname}</div>
                          {dept.description && (
                            <div className="text-xs text-muted-foreground">
                              {dept.description}
                            </div>
                          )}
                        </td>
                        <td className="p-3 text-sm">{dept.code}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(dept.createdat)}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(dept.updatedat)}
                        </td>
                        <td className="p-3">
                          <Badge
                            variant={dept.isactive ? "primary" : "secondary"}
                            className={
                              dept.isactive
                                ? "bg-muted text-foreground"
                                : "bg-muted/50 text-muted-foreground"
                            }
                          >
                            {dept.isactive ? (
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
                        </td>
                        <td className="p-3">
                          <div className="flex items-center justify-end gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => window.location.href = `/admin/departments/${dept.idxdepartment}`}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDelete(dept.id)}
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

                {/* Paginación */}
                <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
                  <div className="text-sm text-muted-foreground">
                    Mostrando {departments.length} de {totalItems} departamentos
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


