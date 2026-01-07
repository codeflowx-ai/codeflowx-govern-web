"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Eye, GitBranch, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
interface ModelVersion {
  id: number;
  version: string;
  description: string;
  performanceMetrics: string;
  trainingConfig: string;
  status: string;
}
export default function VersioningPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState<ModelVersion[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
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
      const mockData: ModelVersion[] = [
        {
          id: 1,
          version: "1.0.0",
          description: "Initial release",
          performanceMetrics: "Accuracy: 95%",
          trainingConfig: "epochs=100, lr=0.001",
          status: "ACTIVE",
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
    item.version.toLowerCase().includes(searchQuery.toLowerCase())
  );
  return (
    <div className="min-h-screen w-full bg-background">
      <div className="relative z-10">
      </div>
      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <GitBranch className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("modelManagement.versioning.title", "Versionado")}
            </h1>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-gradient-to-br from-blue-500/20 to-cyan-500/20 border-blue-500/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Total</p>
              <h2 className="text-4xl font-bold text-white">{stats.total}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-green-500/20 to-cyan-500/20 border-green-500/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Activos</p>
              <h2 className="text-4xl font-bold text-white">{stats.active}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-pink-500/20 to-yellow-500/20 border-pink-500/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Pendientes</p>
              <h2 className="text-4xl font-bold text-white">{stats.pending}</h2>
            </CardContent>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-purple-500/20 to-red-500/20 border-purple-500/50">
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Inactivos</p>
              <h2 className="text-4xl font-bold text-white">{stats.inactive}</h2>
            </CardContent>
          </Card>
        </div>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="p-4">
            <div className="flex gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder="Buscar..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-48">
                  <SelectValue placeholder="Filtrar por Status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>Listado</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-center py-8">Cargando...</div>
            ) : filteredItems.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">No hay datos</div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-border/50">
                      <th className="text-left p-4">ID</th>
                      <th className="text-left p-4">Version</th>
                      <th className="text-left p-4">Description</th>
                      <th className="text-left p-4">Performancemetrics</th>
                      <th className="text-left p-4">Training Config</th>
                      <th className="text-left p-4">Status</th>
                      <th className="text-center p-4">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredItems.map((item) => (
                      <tr
                        key={item.id}
                        className="border-b border-border/30 hover:bg-background/50 transition-colors"
                      >
                        <td className="p-4">{item.id}</td>
                        <td className="p-4 font-mono">{item.version}</td>
                        <td className="p-4">{item.description}</td>
                        <td className="p-4">{item.performanceMetrics}</td>
                        <td className="p-4">{item.trainingConfig}</td>
                        <td className="p-4">
                          <Badge
                            className={
                              item.status === "ACTIVE"
                                ? "bg-green-500/20 text-green-400 border-green-500/50"
                                : "bg-gray-500/20 text-gray-400 border-gray-500/50"
                            }
                          >
                            {item.status}
                          </Badge>
                        </td>
                        <td className="p-4">
                          <div className="flex items-center justify-center gap-2">
                            <Button variant="ghost" size="sm">
                              <Eye className="w-4 h-4" />
                            </Button>
                            <Button variant="ghost" size="sm" className="text-red-400 hover:text-red-300">
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
    </div>
  );
}
