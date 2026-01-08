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
  Activity,
  Search,
  CheckCircle,
  XCircle,
} from "lucide-react";
import { useState, useEffect } from "react";

interface UserSession {
  id: number;
  idxusersession: number;
  createdat: string;
  updatedat?: string;
  userid?: number;
  username?: string;
  isactive?: boolean;
  lastactivity?: string;
  ipaddress?: string;
}

export default function UserSessionsPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [sessions, setSessions] = useState<UserSession[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [inactiveItems, setInactiveItems] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    loadData();
  }, [currentPage, searchTerm]);

  const loadData = async () => {
    setLoading(true);
    try {
      const mockData: UserSession[] = [
        {
          id: 1,
          idxusersession: 1,
          createdat: "2024-01-22T08:00:00Z",
          updatedat: "2024-01-22T10:30:00Z",
          userid: 1,
          username: "jdoe",
          isactive: true,
          lastactivity: "2024-01-22T10:30:00Z",
          ipaddress: "192.168.1.100",
        },
        {
          id: 2,
          idxusersession: 2,
          createdat: "2024-01-22T09:00:00Z",
          updatedat: "2024-01-22T09:45:00Z",
          userid: 2,
          username: "jsmith",
          isactive: true,
          lastactivity: "2024-01-22T09:45:00Z",
          ipaddress: "192.168.1.101",
        },
        {
          id: 3,
          idxusersession: 3,
          createdat: "2024-01-21T14:00:00Z",
          updatedat: "2024-01-21T16:00:00Z",
          userid: 3,
          username: "bjohnson",
          isactive: false,
          lastactivity: "2024-01-21T16:00:00Z",
          ipaddress: "192.168.1.102",
        },
      ];

      const filtered = searchTerm
        ? mockData.filter(
            (session) =>
              session.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              session.ipaddress?.toLowerCase().includes(searchTerm.toLowerCase())
          )
        : mockData;

      setTotalItems(filtered.length);
      setActiveItems(filtered.filter((s) => s.isactive).length);
      setInactiveItems(filtered.filter((s) => !s.isactive).length);

      const start = (currentPage - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      setSessions(filtered.slice(start, end));
    } catch (error) {
      console.error("Error loading user sessions:", error);
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
            <Activity className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.userSessions.title", "Sesiones de Usuario")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("core.userSessions.description", "Gestiona las sesiones activas de usuarios")}
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
              <div className="text-sm text-muted-foreground">Activas</div>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-semibold text-foreground">{activeItems}</div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader className="pb-2">
              <div className="text-sm text-muted-foreground">Inactivas</div>
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
            </div>
          </CardHeader>
        </Card>

        <Card className="bg-card border border-border shadow-sm">
          <CardHeader>
            <CardTitle>{t("core.userSessions.list", "Lista de Sesiones")}</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : sessions.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                No se encontraron sesiones
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
                        Creada
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Última Actividad
                      </th>
                      <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                        Estado
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {sessions.map((session) => (
                      <tr
                        key={session.id}
                        className="border-b border-border/50 hover:bg-muted/50 transition-colors"
                      >
                        <td className="p-3 text-sm">{session.idxusersession}</td>
                        <td className="p-3 font-medium">{session.username || "-"}</td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {session.ipaddress || "-"}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {formatDate(session.createdat)}
                        </td>
                        <td className="p-3 text-sm text-muted-foreground">
                          {session.lastactivity ? formatDate(session.lastactivity) : "-"}
                        </td>
                        <td className="p-3">
                          <Badge
                            variant={session.isactive ? "primary" : "secondary"}
                            className={
                              session.isactive
                                ? "bg-green-500/10 text-green-700 dark:text-green-400 border-green-500/20"
                                : "bg-gray-500/10 text-gray-700 dark:text-gray-400 border-gray-500/20"
                            }
                          >
                            {session.isactive ? (
                              <>
                                <CheckCircle className="w-3 h-3 mr-1" />
                                Activa
                              </>
                            ) : (
                              <>
                                <XCircle className="w-3 h-3 mr-1" />
                                Inactiva
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
                    Mostrando {sessions.length} de {totalItems} sesiones
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


