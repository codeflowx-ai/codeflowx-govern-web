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
  Menu,
  Plus,
  Search,
  Trash2,
  Edit,
} from "lucide-react";
import { useState, useEffect } from "react";

interface MenuItem {
  id: number;
  idxmenu: number;
  parentid?: number;
  createdat: string;
  updatedat: string;
  menuname: string;
  label: string;
}

export default function MenusPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [menus, setMenus] = useState<MenuItem[]>([]);
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
      const mockData: MenuItem[] = [
        {
          id: 1,
          idxmenu: 1,
          parentid: undefined,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          menuname: "dashboard",
          label: "Dashboard",
        },
        {
          id: 2,
          idxmenu: 2,
          parentid: 1,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          menuname: "admin",
          label: "Administración",
        },
        {
          id: 3,
          idxmenu: 3,
          parentid: undefined,
          createdat: "2024-01-15T10:00:00Z",
          updatedat: "2024-01-20T14:30:00Z",
          menuname: "settings",
          label: "Configuración",
        },
      ];

      const filtered = searchTerm
        ? mockData.filter(
            (menu) =>
              menu.menuname.toLowerCase().includes(searchTerm.toLowerCase()) ||
              menu.label.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setActiveItems(filtered.length);
      setInactiveItems(0);
      setPendingApproval(0);

      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setMenus(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading menus:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm("¿Está seguro de eliminar este menú?")) {
      setMenus(menus.filter((m) => m.id !== id));
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
            <Menu className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.menus.title", "Gestión de Menús")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("core.menus.description", "Gestiona los menús del sistema")}
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
                onClick={() => window.location.href = "/admin/menus/new"}
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("core.menus.register", "Registrar Menú")}
              </Button>
            </div>
          </CardHeader>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 shadow-xl">
          <CardHeader>
            <CardTitle>{t("core.menus.list", "Lista de Menús")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : menus.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron menús
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
                        Etiqueta
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Padre
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Creado
                      </th>
                      <th className="text-right p-3 text-sm font-medium text-muted-foreground">
                        {t("common.actions", "Acciones")}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {menus.map((menu) => (
                      <tr
                        key={menu.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{menu.idxmenu}</td>
                        <td className="p-3 font-medium">{menu.menuname}</td>
                        <td className="p-3 text-sm">{menu.label}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {menu.parentid || "-"}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(menu.createdat)}
                        </td>
                        <td className="p-3">
                          <div className="flex items-center justify-end gap-2">
                            <Button 
                              variant="ghost" 
                              size="sm"
                              onClick={() => window.location.href = `/admin/menus/${menu.idxmenu}`}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDelete(menu.id)}
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
                    Mostrando {menus.length} de {totalItems} menús
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


