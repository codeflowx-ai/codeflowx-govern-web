"use client";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Shield,
  Plus,
  Search,
  Filter,
  X,
  Edit,
  Trash2,
  Eye,
  TrendingUp,
  AlertTriangle,
  CheckCircle,
} from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
interface GovernancePolicy {
  idxpolicy: number;
  policyname: string;
  policytype: string;
  enforcementlevel: string;
  version: number;
  status: string;
  totalviolations: number;
  avgcompliancescore: number;
}
interface GovernanceMetrics {
  totalPolicies: number;
  activePolicies: number;
  totalViolations: number;
  complianceScore: number;
}
export default function GovernanceOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [policies, setPolicies] = useState<GovernancePolicy[]>([]);
  const [metrics, setMetrics] = useState<GovernanceMetrics>({
    totalPolicies: 0,
    activePolicies: 0,
    totalViolations: 0,
    complianceScore: 0,
  });
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [enforcementFilter, setEnforcementFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  useEffect(() => {
    loadData();
  }, [currentPage, searchTerm, typeFilter, statusFilter, enforcementFilter]);
  const loadData = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams({
        page: currentPage.toString(),
        size: "20",
        ...(searchTerm && { search: searchTerm }),
        ...(typeFilter !== "ALL" && { type: typeFilter }),
        ...(statusFilter !== "ALL" && { status: statusFilter }),
        ...(enforcementFilter !== "ALL" && { enforcement: enforcementFilter }),
      });
      const response = await fetch(`/api/governance/policies?${params}`);
      const data = await response.json();
      if (data.success) {
        setPolicies(data.policies || []);
        setMetrics(data.metrics || metrics);
        setTotalPages(data.totalPages || 1);
      }
    } catch (error) {
      console.error("Error loading policies:", error);
    } finally {
      setLoading(false);
    }
  };
  const handleViewDetails = (policyId: number) => {
    router.push(`/governance/detail/${policyId}`);
  };
  const handleCreatePolicy = () => {
    router.push("/governance/detail/new");
  };
  const handleDeletePolicy = async (policyId: number) => {
    if (!confirm(t("governance.overview.confirmDelete", "¿Eliminar esta política?"))) {
      return;
    }
    try {
      const response = await fetch(`/api/governance/policies/${policyId}`, {
        method: "DELETE",
      });
      if (response.ok) {
        loadData();
      }
    } catch (error) {
      console.error("Error deleting policy:", error);
    }
  };
  const clearFilters = () => {
    setSearchTerm("");
    setTypeFilter("ALL");
    setStatusFilter("ALL");
    setEnforcementFilter("ALL");
    setCurrentPage(1);
  };
  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "bg-green-100 text-green-800",
      INACTIVE: "bg-gray-100 text-gray-800",
      DRAFT: "bg-yellow-100 text-yellow-800",
      UNDER_REVIEW: "bg-blue-100 text-blue-800",
      ARCHIVED: "bg-gray-100 text-gray-800",
    };
    return variants[status] || "bg-gray-100 text-gray-800";
  };
  const getTypeBadge = (type: string) => {
    const variants: Record<string, string> = {
      SECURITY: "bg-red-100 text-red-800",
      COMPLIANCE: "bg-blue-100 text-blue-800",
      ETHICS: "bg-purple-100 text-purple-800",
      PRIVACY: "bg-indigo-100 text-indigo-800",
      OPERATIONAL: "bg-orange-100 text-orange-800",
    };
    return variants[type] || "bg-gray-100 text-gray-800";
  };
  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Shield className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("governance.overview.title", "Vista General de Políticas")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.overview.subtitle", "Gestión y configuración de políticas de gobierno")}
          </p>
        </div>
        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-1">
                    {t("governance.overview.metrics.total", "Total Políticas")}
                  </p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-cyan-500 bg-clip-text text-transparent">
                    {metrics.totalPolicies}
                  </p>
                </div>
                <Shield className="w-10 h-10 text-blue-500 opacity-70" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-1">
                    {t("governance.overview.metrics.active", "Políticas Activas")}
                  </p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-green-500 to-emerald-500 bg-clip-text text-transparent">
                    {metrics.activePolicies}
                  </p>
                </div>
                <CheckCircle className="w-10 h-10 text-green-500 opacity-70" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-1">
                    {t("governance.overview.metrics.violations", "Violaciones")}
                  </p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-yellow-500 to-orange-500 bg-clip-text text-transparent">
                    {metrics.totalViolations}
                  </p>
                </div>
                <AlertTriangle className="w-10 h-10 text-yellow-500 opacity-70" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardBody className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground mb-1">
                    {t("governance.overview.metrics.compliance", "Score Cumplimiento")}
                  </p>
                  <p className="text-3xl font-bold bg-gradient-to-r from-pink-500 to-rose-500 bg-clip-text text-transparent">
                    {metrics.complianceScore.toFixed(1)}%
                  </p>
                </div>
                <TrendingUp className="w-10 h-10 text-pink-500 opacity-70" />
              </div>
            </CardBody>
          </Card>
        </div>
        {/* Filtros y Búsqueda */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Filter className="w-5 h-5" />
                {t("governance.overview.filters.title", "Filtros")}
              </CardTitle>
              <Button
                variant="ghost"
                size="sm"
                onClick={clearFilters}
                className="flex items-center gap-2"
              >
                <X className="w-4 h-4" />
                {t("governance.overview.filters.clear", "Limpiar")}
              </Button>
            </div>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div className="md:col-span-2">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <Input
                    placeholder={t("governance.overview.filters.search.placeholder", "Buscar políticas...")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="px-3 py-2 border border-border rounded-md bg-background"
              >
                <option value="ALL">{t("governance.overview.filters.type", "Todos los Tipos")}</option>
                <option value="SECURITY">{t("governance.overview.filters.type.security", "Seguridad")}</option>
                <option value="COMPLIANCE">{t("governance.overview.filters.type.compliance", "Cumplimiento")}</option>
                <option value="ETHICS">{t("governance.overview.filters.type.ethics", "Ética")}</option>
                <option value="PRIVACY">{t("governance.overview.filters.type.privacy", "Privacidad")}</option>
              </select>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-3 py-2 border border-border rounded-md bg-background"
              >
                <option value="ALL">{t("governance.overview.filters.status", "Todos los Estados")}</option>
                <option value="ACTIVE">{t("governance.overview.filters.status.active", "Activo")}</option>
                <option value="INACTIVE">{t("governance.overview.filters.status.inactive", "Inactivo")}</option>
                <option value="DRAFT">{t("governance.overview.filters.status.draft", "Borrador")}</option>
              </select>
              <select
                value={enforcementFilter}
                onChange={(e) => setEnforcementFilter(e.target.value)}
                className="px-3 py-2 border border-border rounded-md bg-background"
              >
                <option value="ALL">{t("governance.overview.filters.enforcement", "Todos los Niveles")}</option>
                <option value="MANDATORY">{t("governance.overview.filters.enforcement.mandatory", "Obligatorio")}</option>
                <option value="RECOMMENDED">{t("governance.overview.filters.enforcement.recommended", "Recomendado")}</option>
                <option value="OPTIONAL">{t("governance.overview.filters.enforcement.optional", "Opcional")}</option>
              </select>
            </div>
          </CardBody>
        </Card>
        {/* Tabla de Políticas */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>
                {t("governance.overview.table.title", "Políticas")}
              </CardTitle>
              <Button onClick={handleCreatePolicy} className="flex items-center gap-2">
                <Plus className="w-4 h-4" />
                {t("governance.overview.button.create", "Crear Política")}
              </Button>
            </div>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="flex items-center justify-center py-12">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary" />
              </div>
            ) : policies.length === 0 ? (
              <div className="text-center py-12 text-muted-foreground">
                <Shield className="w-16 h-16 mx-auto mb-4 opacity-50" />
                <p>{t("governance.overview.table.empty", "No se encontraron políticas")}</p>
              </div>
            ) : (
              <>
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="border-b border-border">
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.id", "ID")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.name", "Nombre")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.type", "Tipo")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.enforcement", "Nivel")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.version", "Versión")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.status", "Estado")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.violations", "Violaciones")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.score", "Score")}
                        </th>
                        <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                          {t("governance.overview.table.actions", "Acciones")}
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {policies.map((policy) => (
                        <tr
                          key={policy.idxpolicy}
                          className="border-b border-border/50 hover:bg-background/50 transition-colors cursor-pointer"
                          onClick={() => handleViewDetails(policy.idxpolicy)}
                        >
                          <td className="p-3">{policy.idxpolicy}</td>
                          <td className="p-3 font-medium">{policy.policyname}</td>
                          <td className="p-3">
                            <Badge className={getTypeBadge(policy.policytype)}>
                              {policy.policytype}
                            </Badge>
                          </td>
                          <td className="p-3">
                            <Badge variant="outline">{policy.enforcementlevel}</Badge>
                          </td>
                          <td className="p-3">{policy.version}</td>
                          <td className="p-3">
                            <Badge className={getStatusBadge(policy.status)}>
                              {policy.status}
                            </Badge>
                          </td>
                          <td className="p-3">
                            <Badge
                              className={
                                policy.totalviolations > 0
                                  ? "bg-red-100 text-red-800"
                                  : "bg-green-100 text-green-800"
                              }
                            >
                              {policy.totalviolations}
                            </Badge>
                          </td>
                          <td className="p-3">
                            <Badge
                              className={
                                policy.avgcompliancescore >= 80
                                  ? "bg-green-100 text-green-800"
                                  : policy.avgcompliancescore >= 60
                                  ? "bg-yellow-100 text-yellow-800"
                                  : "bg-red-100 text-red-800"
                              }
                            >
                              {policy.avgcompliancescore.toFixed(1)}%
                            </Badge>
                          </td>
                          <td className="p-3">
                            <div className="flex items-center gap-2" onClick={(e) => e.stopPropagation()}>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => handleViewDetails(policy.idxpolicy)}
                                className="h-8 w-8 p-0"
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => handleDeletePolicy(policy.idxpolicy)}
                                className="h-8 w-8 p-0 text-destructive hover:text-destructive"
                              >
                                <Trash2 className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                {/* Paginación */}
                {totalPages > 1 && (
                  <div className="flex items-center justify-between mt-4">
                    <Button
                      variant="outline"
                      onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                      disabled={currentPage === 1}
                    >
                      {t("common.previous", "Anterior")}
                    </Button>
                    <span className="text-sm text-muted-foreground">
                      {t("common.of", "de")} {currentPage} / {totalPages}
                    </span>
                    <Button
                      variant="outline"
                      onClick={() => setCurrentPage((p) => Math.min(totalPages, p + 1))}
                      disabled={currentPage === totalPages}
                    >
                      {t("common.next", "Siguiente")}
                    </Button>
                  </div>
                )}
              </>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
