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
  LogIn,
  Search,
  CheckCircle,
  XCircle,
} from "lucide-react";
import { useState, useEffect } from "react";

interface LoginAttempt {
  id: number;
  idxloginattempt: number;
  createdat: string;
  username?: string;
  success?: boolean;
  ipaddress?: string;
  useragent?: string;
}

export default function LoginAttemptsPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [attempts, setAttempts] = useState<LoginAttempt[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [successItems, setSuccessItems] = useState(0);
  const [failedItems, setFailedItems] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    loadData();
  }, [currentPage, searchTerm]);

  const loadData = async () => {
    setLoading(true);
    try {
      const mockData: LoginAttempt[] = [
        {
          id: 1,
          idxloginattempt: 1,
          createdat: "2024-01-22T10:00:00Z",
          username: "jdoe",
          success: true,
          ipaddress: "192.168.1.100",
          useragent: "Mozilla/5.0",
        },
        {
          id: 2,
          idxloginattempt: 2,
          createdat: "2024-01-22T09:30:00Z",
          username: "jsmith",
          success: false,
          ipaddress: "192.168.1.101",
          useragent: "Mozilla/5.0",
        },
        {
          id: 3,
          idxloginattempt: 3,
          createdat: "2024-01-22T08:15:00Z",
          username: "bjohnson",
          success: true,
          ipaddress: "192.168.1.102",
          useragent: "Mozilla/5.0",
        },
      ];

      const filtered = searchTerm
        ? mockData.filter(
            (attempt) =>
              attempt.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              attempt.ipaddress?.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setSuccessItems(filtered.filter((a) => a.success).length);
      setFailedItems(filtered.filter((a) => !a.success).length);

      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setAttempts(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading login attempts:", error);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="min-h-screen bg-background w-full">
      <div className="w-full px-6 py-6 space-y-6">
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <LogIn className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.loginAttempts.title", "Intentos de Login")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("core.loginAttempts.description", "Historial de intentos de acceso al sistema")}
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
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
              <div className="text-sm text-muted-foreground">Exitosos</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{successItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Fallidos</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{failedItems}</div>
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
            </div>
          </CardHeader>
        </Card>

        <Card className="bg-card border border-border shadow-sm">
          <CardHeader>
            <CardTitle>{t("core.loginAttempts.list", "Lista de Intentos")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : attempts.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron intentos de login
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
                        IP
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Fecha
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Estado
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {attempts.map((attempt) => (
                      <tr
                        key={attempt.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{attempt.idxloginattempt}</td>
                        <td className="p-3 font-medium">{attempt.username || "-"}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {attempt.ipaddress || "-"}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(attempt.createdat)}
                        </td>
                        <td className="p-3">
                          <Badge
                            variant={attempt.success ? "primary" : "danger"}
                            className={
                              attempt.success
                                ? "bg-green-500/10 text-green-700 dark:text-green-400 border-green-500/20"
                                : "bg-red-500/10 text-red-700 dark:text-red-400 border-red-500/20"
                            }
                          >
                            {attempt.success ? (
                              <>
                                <CheckCircle className="w-3 h-3 mr-1" />
                                Exitoso
                              </>
                            ) : (
                              <>
                                <XCircle className="w-3 h-3 mr-1" />
                                Fallido
                              </>
                            )}
                          </Badge>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>

                <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
                  <div className="text-sm text-muted-foreground">
                    Mostrando {attempts.length} de {totalItems} intentos
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


