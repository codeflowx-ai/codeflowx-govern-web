"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Activity, Eye, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
interface ModelUsage {
  id: number;
  requestId: string;
  userId: string;
  projectId: string;
  agentId: string;
  promptId: string;
}
export default function UsageOverviewPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState<ModelUsage[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [requestTypeFilter, setRequestTypeFilter] = useState<string>("ALL");
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    pending: 0,
    inactive: 0,
  });
  useEffect(() => {
    loadData();
  }, []);
  const loadData = async () => {
    try {
      setLoading(true);
      const mockData: ModelUsage[] = [
        {
          id: 1,
          requestId: "req-001",
          userId: "user1",
          projectId: "proj1",
          agentId: "agent1",
          promptId: "prompt1",
        },
      ];
      setItems(mockData);
      setStats({
        total: mockData.length,
        active: 1,
        pending: 0,
        inactive: 0,
      });
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };
  const filteredItems = items.filter((item) =>
    item.requestId.toLowerCase().includes(searchQuery.toLowerCase())
  );
  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Activity className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.usageOverview.title", "Uso de Modelos")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t("governance.models.usageOverview.subtitle", "Gestión de uso y consumo de modelos")}
        </p>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.usageOverview.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.usageOverview.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.active}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.usageOverview.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{stats.pending}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.usageOverview.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.inactive}</h2>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <Card className="border-2">
        <CardContent className="p-4">
          <div className="flex gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder={t("governance.models.usageOverview.filters.search", "Buscar...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={requestTypeFilter} onValueChange={setRequestTypeFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.usageOverview.filters.requestType", "Filtrar por Requesttype")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.usageOverview.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="TYPE1">{t("governance.models.usageOverview.filters.type1", "TIPO 1")}</SelectItem>
                <SelectItem value="TYPE2">{t("governance.models.usageOverview.filters.type2", "TIPO 2")}</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>
      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.usageOverview.table.title", "Listado")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.usageOverview.table.noResults", "No hay datos")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.id", "ID")}</th>
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.requestId", "Requestid")}</th>
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.userId", "Userid")}</th>
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.projectId", "Projectid")}</th>
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.agentId", "Agent Id")}</th>
                    <th className="text-left p-2">{t("governance.models.usageOverview.table.promptId", "Prompt Id")}</th>
                    <th className="text-center p-2">{t("governance.models.usageOverview.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.requestId}</td>
                      <td className="p-2">{item.userId}</td>
                      <td className="p-2">{item.projectId}</td>
                      <td className="p-2">{item.agentId}</td>
                      <td className="p-2">{item.promptId}</td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button variant="ghost" size="sm" title={t("common.view", "Ver")}>
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm" className="text-red-400 hover:text-red-300" title={t("common.delete", "Eliminar")}>
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}

