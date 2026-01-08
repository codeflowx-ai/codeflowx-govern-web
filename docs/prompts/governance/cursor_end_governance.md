# END_GOVERNANCE
_Exported on 12/8/2025 at 19:42:27 GMT+1 from Cursor (2.0.77)_

---

**User**

@MIGRACION_GOVERNANCE_DETAILS.md @MIGRACION_GOVERNANCE_OVERVIEWS.md realiza la migracion de estos prompts

---

**Cursor**

Revisando los archivos ZUL originales y los ViewModels asociados para entender la funcionalidad.


Revisando los ViewModels completos y verificando si las páginas ya existen:


Revisando la página de policies existente para entender su estructura:


Creando las páginas de migración. Empezando por la página de overview:


Creando las páginas de migración. Empezando por la página de overview:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
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
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
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

```

Creando la página de detalle con formulario y tabs:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Shield,
  ArrowLeft,
  Save,
  CheckCircle,
  AlertTriangle,
  Calculator,
  FileCheck,
  List,
  ClipboardList,
  AlertCircle,
  BarChart3,
  Info,
} from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";

interface Policy {
  idxpolicy?: number;
  name: string;
  version: number;
  policytype: string;
  category: string;
  enforcementlevel: string;
  description: string;
  effectivedate: string;
  expirationdate: string;
  status: string;
  metadata: string;
  createdby?: number;
  createdat?: string;
  updatedat?: string;
}

interface PolicyRule {
  idxpolicyrule: number;
  name: string;
  conditiontext: string;
  actiontext: string;
  priority: number;
  isactive: boolean;
}

interface PolicyEvaluation {
  idxpolicyevaluation: number;
  evaluationdate: string;
  result: string;
  compliancescore: number;
  evaluatedby: string;
}

interface PolicyViolation {
  idxpolicyviolation: number;
  violationtype: string;
  description: string;
  severity: string;
  detectedat: string;
  status: string;
}

interface ComplianceAssessment {
  idxcomplianceassessment: number;
  assessmentdate: string;
  compliancearea: string;
  compliancescore: number;
  status: string;
  totalfindings: number;
}

interface PolicyStatistics {
  totalRules: number;
  totalEvaluations: number;
  totalViolations: number;
  currentPolicyScore: number;
  averageComplianceScore: number;
  lastEvaluationStatus: string;
  lastEvaluationDate: string;
}

export default function GovernanceDetailPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const policyId = params.id as string;

  const [policy, setPolicy] = useState<Policy>({
    name: "",
    version: 1,
    policytype: "COMPLIANCE",
    category: "",
    enforcementlevel: "RECOMMENDED",
    description: "",
    effectivedate: "",
    expirationdate: "",
    status: "DRAFT",
    metadata: "",
  });

  const [rules, setRules] = useState<PolicyRule[]>([]);
  const [evaluations, setEvaluations] = useState<PolicyEvaluation[]>([]);
  const [violations, setViolations] = useState<PolicyViolation[]>([]);
  const [assessments, setAssessments] = useState<ComplianceAssessment[]>([]);
  const [statistics, setStatistics] = useState<PolicyStatistics>({
    totalRules: 0,
    totalEvaluations: 0,
    totalViolations: 0,
    currentPolicyScore: 0,
    averageComplianceScore: 0,
    lastEvaluationStatus: "NOT_EVALUATED",
    lastEvaluationDate: "",
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [activeTab, setActiveTab] = useState("rules");
  const isEditing = policyId !== "new" && policy.idxpolicy;

  useEffect(() => {
    if (policyId === "new") {
      setLoading(false);
    } else {
      loadPolicy();
    }
  }, [policyId]);

  const loadPolicy = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/governance/policies/${policyId}`);
      const data = await response.json();

      if (data.success) {
        setPolicy(data.policy);
        setRules(data.rules || []);
        setEvaluations(data.evaluations || []);
        setViolations(data.violations || []);
        setAssessments(data.assessments || []);
        setStatistics(data.statistics || statistics);
      }
    } catch (error) {
      console.error("Error loading policy:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      const method = isEditing ? "PUT" : "POST";
      const url = isEditing
        ? `/api/governance/policies/${policy.idxpolicy}`
        : "/api/governance/policies";

      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(policy),
      });

      const data = await response.json();
      if (data.success) {
        router.push("/governance/overview");
      }
    } catch (error) {
      console.error("Error saving policy:", error);
    } finally {
      setSaving(false);
    }
  };

  const handleEvaluate = async () => {
    try {
      const response = await fetch(`/api/governance/policies/${policy.idxpolicy}/evaluate`, {
        method: "POST",
      });
      const data = await response.json();
      if (data.success) {
        alert(t("governance.detail.evaluation.success", "Evaluación completada"));
        loadPolicy();
      }
    } catch (error) {
      console.error("Error evaluating policy:", error);
    }
  };

  const handleComplianceCheck = async () => {
    try {
      const response = await fetch(`/api/governance/policies/${policy.idxpolicy}/compliance-check`, {
        method: "POST",
      });
      const data = await response.json();
      if (data.success) {
        alert(t("governance.detail.compliance.success", "Verificación de cumplimiento completada"));
        loadPolicy();
      }
    } catch (error) {
      console.error("Error running compliance check:", error);
    }
  };

  const handleCalculateScore = async () => {
    try {
      const response = await fetch(`/api/governance/policies/${policy.idxpolicy}/calculate-score`, {
        method: "POST",
      });
      const data = await response.json();
      if (data.success) {
        alert(t("governance.detail.score.success", "Score calculado: {score}", { score: data.score }));
        loadPolicy();
      }
    } catch (error) {
      console.error("Error calculating score:", error);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => router.push("/governance/overview")}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="w-4 h-4" />
              {t("common.back", "Volver")}
            </Button>
            <div className="flex items-center gap-3">
              <Shield className="w-8 h-8 text-primary" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {isEditing
                  ? t("governance.detail.title.edit", "Editar Política")
                  : t("governance.detail.title.create", "Nueva Política")}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            {isEditing && (
              <>
                <Button
                  variant="outline"
                  onClick={handleEvaluate}
                  className="flex items-center gap-2"
                >
                  <CheckCircle className="w-4 h-4" />
                  {t("governance.detail.button.evaluate", "Evaluar")}
                </Button>
                <Button
                  variant="outline"
                  onClick={handleComplianceCheck}
                  className="flex items-center gap-2"
                >
                  <FileCheck className="w-4 h-4" />
                  {t("governance.detail.button.compliance", "Verificar Cumplimiento")}
                </Button>
                <Button
                  variant="outline"
                  onClick={handleCalculateScore}
                  className="flex items-center gap-2"
                >
                  <Calculator className="w-4 h-4" />
                  {t("governance.detail.button.score", "Calcular Score")}
                </Button>
              </>
            )}
            <Button onClick={handleSave} disabled={saving} className="flex items-center gap-2">
              <Save className="w-4 h-4" />
              {saving ? t("common.saving", "Guardando...") : t("common.save", "Guardar")}
            </Button>
          </div>
        </div>

        {/* Formulario Principal */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.detail.form.title", "Información de la Política")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="md:col-span-2">
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.name", "Nombre de la Política")}
                </label>
                <Input
                  value={policy.name}
                  onChange={(e) => setPolicy({ ...policy, name: e.target.value })}
                  maxLength={100}
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.version", "Versión")}
                </label>
                <Input
                  type="number"
                  value={policy.version}
                  onChange={(e) => setPolicy({ ...policy, version: parseInt(e.target.value) || 1 })}
                  readOnly
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.type", "Tipo")}
                </label>
                <select
                  value={policy.policytype}
                  onChange={(e) => setPolicy({ ...policy, policytype: e.target.value })}
                  className="w-full px-3 py-2 border border-border rounded-md bg-background"
                  readOnly
                >
                  <option value="SECURITY">{t("governance.detail.form.type.security", "Seguridad")}</option>
                  <option value="COMPLIANCE">{t("governance.detail.form.type.compliance", "Cumplimiento")}</option>
                  <option value="ETHICS">{t("governance.detail.form.type.ethics", "Ética")}</option>
                  <option value="PRIVACY">{t("governance.detail.form.type.privacy", "Privacidad")}</option>
                  <option value="OPERATIONAL">{t("governance.detail.form.type.operational", "Operacional")}</option>
                </select>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.category", "Categoría")}
                </label>
                <Input
                  value={policy.category}
                  onChange={(e) => setPolicy({ ...policy, category: e.target.value })}
                  maxLength={100}
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.enforcement", "Nivel de Enforcement")}
                </label>
                <select
                  value={policy.enforcementlevel}
                  onChange={(e) => setPolicy({ ...policy, enforcementlevel: e.target.value })}
                  className="w-full px-3 py-2 border border-border rounded-md bg-background"
                  readOnly
                >
                  <option value="MANDATORY">{t("governance.detail.form.enforcement.mandatory", "Obligatorio")}</option>
                  <option value="RECOMMENDED">{t("governance.detail.form.enforcement.recommended", "Recomendado")}</option>
                  <option value="OPTIONAL">{t("governance.detail.form.enforcement.optional", "Opcional")}</option>
                  <option value="INFORMATIONAL">{t("governance.detail.form.enforcement.informational", "Información")}</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">
                {t("governance.detail.form.description", "Descripción")}
              </label>
              <Textarea
                value={policy.description}
                onChange={(e) => setPolicy({ ...policy, description: e.target.value })}
                rows={3}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.effectiveDate", "Fecha Efectiva")}
                </label>
                <Input
                  type="date"
                  value={policy.effectivedate}
                  onChange={(e) => setPolicy({ ...policy, effectivedate: e.target.value })}
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.expirationDate", "Fecha de Expiración")}
                </label>
                <Input
                  type="date"
                  value={policy.expirationdate}
                  onChange={(e) => setPolicy({ ...policy, expirationdate: e.target.value })}
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.detail.form.status", "Estado")}
                </label>
                <select
                  value={policy.status}
                  onChange={(e) => setPolicy({ ...policy, status: e.target.value })}
                  className="w-full px-3 py-2 border border-border rounded-md bg-background"
                  readOnly
                >
                  <option value="ACTIVE">{t("governance.detail.form.status.active", "Activo")}</option>
                  <option value="INACTIVE">{t("governance.detail.form.status.inactive", "Inactivo")}</option>
                  <option value="DRAFT">{t("governance.detail.form.status.draft", "Borrador")}</option>
                  <option value="UNDER_REVIEW">{t("governance.detail.form.status.review", "Revisión")}</option>
                  <option value="ARCHIVED">{t("governance.detail.form.status.archived", "Archivado")}</option>
                </select>
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-2">
                {t("governance.detail.form.metadata", "Metadata (JSON)")}
              </label>
              <Textarea
                value={policy.metadata}
                onChange={(e) => setPolicy({ ...policy, metadata: e.target.value })}
                rows={4}
                placeholder='{"key": "value"}'
              />
            </div>
          </CardBody>
        </Card>

        {/* Tabs de Información Descendente */}
        {isEditing && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <div className="flex items-center gap-2 border-b border-border">
                <button
                  onClick={() => setActiveTab("rules")}
                  className={`px-4 py-2 font-medium transition-colors ${
                    activeTab === "rules"
                      ? "border-b-2 border-primary text-primary"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <List className="w-4 h-4 inline mr-2" />
                  {t("governance.detail.tabs.rules", "Reglas")} ({statistics.totalRules})
                </button>
                <button
                  onClick={() => setActiveTab("evaluations")}
                  className={`px-4 py-2 font-medium transition-colors ${
                    activeTab === "evaluations"
                      ? "border-b-2 border-primary text-primary"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <ClipboardList className="w-4 h-4 inline mr-2" />
                  {t("governance.detail.tabs.evaluations", "Evaluaciones")} ({statistics.totalEvaluations})
                </button>
                <button
                  onClick={() => setActiveTab("violations")}
                  className={`px-4 py-2 font-medium transition-colors ${
                    activeTab === "violations"
                      ? "border-b-2 border-primary text-primary"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <AlertCircle className="w-4 h-4 inline mr-2" />
                  {t("governance.detail.tabs.violations", "Violaciones")} ({statistics.totalViolations})
                </button>
                <button
                  onClick={() => setActiveTab("compliance")}
                  className={`px-4 py-2 font-medium transition-colors ${
                    activeTab === "compliance"
                      ? "border-b-2 border-primary text-primary"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <CheckCircle className="w-4 h-4 inline mr-2" />
                  {t("governance.detail.tabs.compliance", "Cumplimiento")}
                </button>
                <button
                  onClick={() => setActiveTab("statistics")}
                  className={`px-4 py-2 font-medium transition-colors ${
                    activeTab === "statistics"
                      ? "border-b-2 border-primary text-primary"
                      : "text-muted-foreground hover:text-foreground"
                  }`}
                >
                  <BarChart3 className="w-4 h-4 inline mr-2" />
                  {t("governance.detail.tabs.statistics", "Estadísticas")}
                </button>
              </div>
            </CardHeader>
            <CardBody>
              {/* Tab: Reglas */}
              {activeTab === "rules" && (
                <div className="space-y-4">
                  {rules.length === 0 ? (
                    <div className="text-center py-12 text-muted-foreground">
                      <List className="w-16 h-16 mx-auto mb-4 opacity-50" />
                      <p>{t("governance.detail.tabs.rules.empty", "No hay reglas definidas")}</p>
                    </div>
                  ) : (
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead>
                          <tr className="border-b border-border">
                            <th className="text-left p-3 text-sm font-medium">ID</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.rules.name", "Nombre")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.rules.condition", "Condición")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.rules.action", "Acción")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.rules.priority", "Prioridad")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.rules.active", "Activa")}</th>
                          </tr>
                        </thead>
                        <tbody>
                          {rules.map((rule) => (
                            <tr key={rule.idxpolicyrule} className="border-b border-border/50">
                              <td className="p-3">{rule.idxpolicyrule}</td>
                              <td className="p-3">{rule.name}</td>
                              <td className="p-3 text-sm text-muted-foreground">{rule.conditiontext.substring(0, 50)}...</td>
                              <td className="p-3 text-sm text-muted-foreground">{rule.actiontext.substring(0, 50)}...</td>
                              <td className="p-3">
                                <Badge variant="outline">{rule.priority}</Badge>
                              </td>
                              <td className="p-3">
                                <Badge className={rule.isactive ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800"}>
                                  {rule.isactive ? t("common.yes", "Sí") : t("common.no", "No")}
                                </Badge>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              )}

              {/* Tab: Evaluaciones */}
              {activeTab === "evaluations" && (
                <div className="space-y-4">
                  {evaluations.length === 0 ? (
                    <div className="text-center py-12 text-muted-foreground">
                      <ClipboardList className="w-16 h-16 mx-auto mb-4 opacity-50" />
                      <p>{t("governance.detail.tabs.evaluations.empty", "No hay evaluaciones registradas")}</p>
                    </div>
                  ) : (
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead>
                          <tr className="border-b border-border">
                            <th className="text-left p-3 text-sm font-medium">ID</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.evaluations.date", "Fecha")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.evaluations.result", "Resultado")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.evaluations.score", "Puntuación")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.evaluations.evaluator", "Evaluador")}</th>
                          </tr>
                        </thead>
                        <tbody>
                          {evaluations.map((eval) => (
                            <tr key={eval.idxpolicyevaluation} className="border-b border-border/50">
                              <td className="p-3">{eval.idxpolicyevaluation}</td>
                              <td className="p-3">{new Date(eval.evaluationdate).toLocaleDateString()}</td>
                              <td className="p-3">
                                <Badge className={eval.result === "PASS" ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"}>
                                  {eval.result}
                                </Badge>
                              </td>
                              <td className="p-3">
                                <Badge variant="outline">{eval.compliancescore}</Badge>
                              </td>
                              <td className="p-3">{eval.evaluatedby}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              )}

              {/* Tab: Violaciones */}
              {activeTab === "violations" && (
                <div className="space-y-4">
                  {violations.length === 0 ? (
                    <div className="text-center py-12 text-muted-foreground">
                      <AlertCircle className="w-16 h-16 mx-auto mb-4 opacity-50" />
                      <p>{t("governance.detail.tabs.violations.empty", "No hay violaciones registradas")}</p>
                    </div>
                  ) : (
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead>
                          <tr className="border-b border-border">
                            <th className="text-left p-3 text-sm font-medium">ID</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.violations.type", "Tipo")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.violations.description", "Descripción")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.violations.severity", "Severidad")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.violations.date", "Fecha")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.violations.status", "Estado")}</th>
                          </tr>
                        </thead>
                        <tbody>
                          {violations.map((violation) => (
                            <tr key={violation.idxpolicyviolation} className="border-b border-border/50">
                              <td className="p-3">{violation.idxpolicyviolation}</td>
                              <td className="p-3">
                                <Badge variant="outline">{violation.violationtype}</Badge>
                              </td>
                              <td className="p-3 text-sm">{violation.description.substring(0, 60)}...</td>
                              <td className="p-3">
                                <Badge
                                  className={
                                    violation.severity === "CRITICAL"
                                      ? "bg-red-100 text-red-800"
                                      : violation.severity === "HIGH"
                                      ? "bg-yellow-100 text-yellow-800"
                                      : "bg-blue-100 text-blue-800"
                                  }
                                >
                                  {violation.severity}
                                </Badge>
                              </td>
                              <td className="p-3">{new Date(violation.detectedat).toLocaleDateString()}</td>
                              <td className="p-3">
                                <Badge className={violation.status === "RESOLVED" ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"}>
                                  {violation.status}
                                </Badge>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              )}

              {/* Tab: Cumplimiento */}
              {activeTab === "compliance" && (
                <div className="space-y-4">
                  <div className="flex items-center justify-between mb-4">
                    <p className="text-sm text-muted-foreground">
                      {t("governance.detail.tabs.compliance.average", "Score Promedio")}:{" "}
                      <span className="font-bold">{statistics.averageComplianceScore.toFixed(1)}%</span>
                    </p>
                  </div>
                  {assessments.length === 0 ? (
                    <div className="text-center py-12 text-muted-foreground">
                      <CheckCircle className="w-16 h-16 mx-auto mb-4 opacity-50" />
                      <p>{t("governance.detail.tabs.compliance.empty", "No hay evaluaciones de cumplimiento")}</p>
                    </div>
                  ) : (
                    <div className="overflow-x-auto">
                      <table className="w-full">
                        <thead>
                          <tr className="border-b border-border">
                            <th className="text-left p-3 text-sm font-medium">ID</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.compliance.date", "Fecha")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.compliance.area", "Área")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.compliance.score", "Score")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.compliance.status", "Estado")}</th>
                            <th className="text-left p-3 text-sm font-medium">{t("governance.detail.tabs.compliance.findings", "Hallazgos")}</th>
                          </tr>
                        </thead>
                        <tbody>
                          {assessments.map((assessment) => (
                            <tr key={assessment.idxcomplianceassessment} className="border-b border-border/50">
                              <td className="p-3">{assessment.idxcomplianceassessment}</td>
                              <td className="p-3">{new Date(assessment.assessmentdate).toLocaleDateString()}</td>
                              <td className="p-3">{assessment.compliancearea}</td>
                              <td className="p-3">
                                <Badge
                                  className={
                                    assessment.compliancescore >= 80
                                      ? "bg-green-100 text-green-800"
                                      : assessment.compliancescore >= 60
                                      ? "bg-yellow-100 text-yellow-800"
                                      : "bg-red-100 text-red-800"
                                  }
                                >
                                  {assessment.compliancescore}%
                                </Badge>
                              </td>
                              <td className="p-3">
                                <Badge className={assessment.status === "PASSED" ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"}>
                                  {assessment.status}
                                </Badge>
                              </td>
                              <td className="p-3">{assessment.totalfindings}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              )}

              {/* Tab: Estadísticas */}
              {activeTab === "statistics" && (
                <div className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <Card className="backdrop-blur-md bg-gradient-to-br from-purple-500/10 to-purple-600/10 border-purple-500/20">
                      <CardBody className="p-6 text-center">
                        <List className="w-10 h-10 mx-auto mb-3 text-purple-500" />
                        <p className="text-3xl font-bold mb-1">{statistics.totalRules}</p>
                        <p className="text-sm text-muted-foreground">{t("governance.detail.tabs.statistics.rules", "Reglas")}</p>
                      </CardBody>
                    </Card>
                    <Card className="backdrop-blur-md bg-gradient-to-br from-pink-500/10 to-pink-600/10 border-pink-500/20">
                      <CardBody className="p-6 text-center">
                        <ClipboardList className="w-10 h-10 mx-auto mb-3 text-pink-500" />
                        <p className="text-3xl font-bold mb-1">{statistics.totalEvaluations}</p>
                        <p className="text-sm text-muted-foreground">{t("governance.detail.tabs.statistics.evaluations", "Evaluaciones")}</p>
                      </CardBody>
                    </Card>
                    <Card className="backdrop-blur-md bg-gradient-to-br from-cyan-500/10 to-cyan-600/10 border-cyan-500/20">
                      <CardBody className="p-6 text-center">
                        <AlertCircle className="w-10 h-10 mx-auto mb-3 text-cyan-500" />
                        <p className="text-3xl font-bold mb-1">{statistics.totalViolations}</p>
                        <p className="text-sm text-muted-foreground">{t("governance.detail.tabs.statistics.violations", "Violaciones")}</p>
                      </CardBody>
                    </Card>
                    <Card className="backdrop-blur-md bg-gradient-to-br from-emerald-500/10 to-emerald-600/10 border-emerald-500/20">
                      <CardBody className="p-6 text-center">
                        <BarChart3 className="w-10 h-10 mx-auto mb-3 text-emerald-500" />
                        <p className="text-3xl font-bold mb-1">{statistics.currentPolicyScore.toFixed(1)}</p>
                        <p className="text-sm text-muted-foreground">{t("governance.detail.tabs.statistics.score", "Score Actual")}</p>
                      </CardBody>
                    </Card>
                  </div>

                  <Card className="backdrop-blur-md bg-background/60 border-border/50">
                    <CardHeader>
                      <CardTitle className="flex items-center gap-2">
                        <Info className="w-5 h-5" />
                        {t("governance.detail.tabs.statistics.info", "Información de la Política")}
                      </CardTitle>
                    </CardHeader>
                    <CardBody>
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.createdBy", "Creado por")}:
                          </p>
                          <p className="font-medium">{policy.createdby || "N/A"}</p>
                        </div>
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.createdAt", "Fecha de creación")}:
                          </p>
                          <p className="font-medium">
                            {policy.createdat ? new Date(policy.createdat).toLocaleDateString() : "N/A"}
                          </p>
                        </div>
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.lastEvaluation", "Última evaluación")}:
                          </p>
                          <p className="font-medium">
                            {statistics.lastEvaluationDate || "N/A"}
                          </p>
                        </div>
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.lastUpdated", "Última actualización")}:
                          </p>
                          <p className="font-medium">
                            {policy.updatedat ? new Date(policy.updatedat).toLocaleDateString() : "N/A"}
                          </p>
                        </div>
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.evaluationStatus", "Estado evaluación")}:
                          </p>
                          <Badge
                            className={
                              statistics.lastEvaluationStatus === "PASS"
                                ? "bg-green-100 text-green-800"
                                : "bg-yellow-100 text-yellow-800"
                            }
                          >
                            {statistics.lastEvaluationStatus}
                          </Badge>
                        </div>
                        <div>
                          <p className="text-sm text-muted-foreground mb-1">
                            {t("governance.detail.tabs.statistics.complianceScore", "Score cumplimiento")}:
                          </p>
                          <p className="font-medium">{statistics.averageComplianceScore.toFixed(1)}%</p>
                        </div>
                      </div>
                    </CardBody>
                  </Card>
                </div>
              )}
            </CardBody>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Creando las API routes con mock data. Empezando por la ruta de listado:


```typescript
import { NextRequest, NextResponse } from "next/server";

// Mock data para políticas
const mockPolicies = [
  {
    idxpolicy: 1,
    policyname: "Política de Seguridad de Datos",
    policytype: "SECURITY",
    enforcementlevel: "MANDATORY",
    version: 1,
    status: "ACTIVE",
    totalviolations: 2,
    avgcompliancescore: 85.5,
  },
  {
    idxpolicy: 2,
    policyname: "Política de Cumplimiento GDPR",
    policytype: "COMPLIANCE",
    enforcementlevel: "MANDATORY",
    version: 2,
    status: "ACTIVE",
    totalviolations: 0,
    avgcompliancescore: 92.3,
  },
  {
    idxpolicy: 3,
    policyname: "Política de Ética en IA",
    policytype: "ETHICS",
    enforcementlevel: "RECOMMENDED",
    version: 1,
    status: "DRAFT",
    totalviolations: 5,
    avgcompliancescore: 78.2,
  },
  {
    idxpolicy: 4,
    policyname: "Política de Privacidad de Usuarios",
    policytype: "PRIVACY",
    enforcementlevel: "MANDATORY",
    version: 1,
    status: "ACTIVE",
    totalviolations: 1,
    avgcompliancescore: 88.7,
  },
  {
    idxpolicy: 5,
    policyname: "Política Operacional de Monitoreo",
    policytype: "OPERATIONAL",
    enforcementlevel: "RECOMMENDED",
    version: 1,
    status: "ACTIVE",
    totalviolations: 0,
    avgcompliancescore: 95.0,
  },
];

const mockMetrics = {
  totalPolicies: 5,
  activePolicies: 4,
  totalViolations: 8,
  complianceScore: 87.9,
};

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const page = parseInt(searchParams.get("page") || "1");
    const size = parseInt(searchParams.get("size") || "20");
    const search = searchParams.get("search") || "";
    const type = searchParams.get("type");
    const status = searchParams.get("status");
    const enforcement = searchParams.get("enforcement");

    // Filtrar políticas
    let filtered = [...mockPolicies];

    if (search) {
      filtered = filtered.filter((p) =>
        p.policyname.toLowerCase().includes(search.toLowerCase())
      );
    }

    if (type && type !== "ALL") {
      filtered = filtered.filter((p) => p.policytype === type);
    }

    if (status && status !== "ALL") {
      filtered = filtered.filter((p) => p.status === status);
    }

    if (enforcement && enforcement !== "ALL") {
      filtered = filtered.filter((p) => p.enforcementlevel === enforcement);
    }

    // Paginación
    const start = (page - 1) * size;
    const end = start + size;
    const paginated = filtered.slice(start, end);
    const totalPages = Math.ceil(filtered.length / size);

    return NextResponse.json({
      success: true,
      policies: paginated,
      metrics: mockMetrics,
      totalPages,
      currentPage: page,
      totalItems: filtered.length,
    });
  } catch (error) {
    console.error("Error in GET /api/governance/policies:", error);
    return NextResponse.json(
      { success: false, error: "Error loading policies" },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Simular creación de política
    const newPolicy = {
      idxpolicy: mockPolicies.length + 1,
      ...body,
      version: 1,
      status: body.status || "DRAFT",
      totalviolations: 0,
      avgcompliancescore: 0,
    };

    return NextResponse.json({
      success: true,
      policy: newPolicy,
    });
  } catch (error) {
    console.error("Error in POST /api/governance/policies:", error);
    return NextResponse.json(
      { success: false, error: "Error creating policy" },
      { status: 500 }
    );
  }
}

```

```typescript
import { NextRequest, NextResponse } from "next/server";

// Mock data para una política completa
const mockPolicy = {
  idxpolicy: 1,
  name: "Política de Seguridad de Datos",
  version: 1,
  policytype: "SECURITY",
  category: "Seguridad de Información",
  enforcementlevel: "MANDATORY",
  description: "Política para garantizar la seguridad de los datos sensibles en el sistema.",
  effectivedate: "2025-01-01",
  expirationdate: "2026-01-01",
  status: "ACTIVE",
  metadata: '{"framework": "ISO 27001", "owner": "IT Security"}',
  createdby: 1,
  createdat: "2024-12-01T10:00:00Z",
  updatedat: "2025-01-15T14:30:00Z",
};

const mockRules = [
  {
    idxpolicyrule: 1,
    name: "Regla de Encriptación",
    conditiontext: "IF data.sensitivity == 'HIGH' THEN encrypt",
    actiontext: "ENCRYPT data",
    priority: 1,
    isactive: true,
  },
  {
    idxpolicyrule: 2,
    name: "Regla de Acceso",
    conditiontext: "IF user.role != 'ADMIN' THEN deny",
    actiontext: "DENY access",
    priority: 2,
    isactive: true,
  },
];

const mockEvaluations = [
  {
    idxpolicyevaluation: 1,
    evaluationdate: "2025-01-10T10:00:00Z",
    result: "PASS",
    compliancescore: 85,
    evaluatedby: "admin@example.com",
  },
  {
    idxpolicyevaluation: 2,
    evaluationdate: "2025-01-15T14:00:00Z",
    result: "PASS",
    compliancescore: 90,
    evaluatedby: "admin@example.com",
  },
];

const mockViolations = [
  {
    idxpolicyviolation: 1,
    violationtype: "ACCESS_VIOLATION",
    description: "Intento de acceso no autorizado a datos sensibles",
    severity: "HIGH",
    detectedat: "2025-01-12T08:30:00Z",
    status: "RESOLVED",
  },
  {
    idxpolicyviolation: 2,
    violationtype: "ENCRYPTION_VIOLATION",
    description: "Datos sensibles almacenados sin encriptación",
    severity: "CRITICAL",
    detectedat: "2025-01-13T11:20:00Z",
    status: "PENDING",
  },
];

const mockAssessments = [
  {
    idxcomplianceassessment: 1,
    assessmentdate: "2025-01-10T10:00:00Z",
    compliancearea: "Data Protection",
    compliancescore: 85,
    status: "PASSED",
    totalfindings: 2,
  },
  {
    idxcomplianceassessment: 2,
    assessmentdate: "2025-01-15T14:00:00Z",
    compliancearea: "Access Control",
    compliancescore: 90,
    status: "PASSED",
    totalfindings: 1,
  },
];

const mockStatistics = {
  totalRules: 2,
  totalEvaluations: 2,
  totalViolations: 2,
  currentPolicyScore: 87.5,
  averageComplianceScore: 87.5,
  lastEvaluationStatus: "PASS",
  lastEvaluationDate: "2025-01-15",
};

export async function GET(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;

    if (id === "new") {
      return NextResponse.json({
        success: true,
        policy: {
          name: "",
          version: 1,
          policytype: "COMPLIANCE",
          category: "",
          enforcementlevel: "RECOMMENDED",
          description: "",
          effectivedate: "",
          expirationdate: "",
          status: "DRAFT",
          metadata: "",
        },
        rules: [],
        evaluations: [],
        violations: [],
        assessments: [],
        statistics: {
          totalRules: 0,
          totalEvaluations: 0,
          totalViolations: 0,
          currentPolicyScore: 0,
          averageComplianceScore: 0,
          lastEvaluationStatus: "NOT_EVALUATED",
          lastEvaluationDate: "",
        },
      });
    }

    return NextResponse.json({
      success: true,
      policy: mockPolicy,
      rules: mockRules,
      evaluations: mockEvaluations,
      violations: mockViolations,
      assessments: mockAssessments,
      statistics: mockStatistics,
    });
  } catch (error) {
    console.error("Error in GET /api/governance/policies/[id]:", error);
    return NextResponse.json(
      { success: false, error: "Error loading policy" },
      { status: 500 }
    );
  }
}

export async function PUT(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;
    const body = await request.json();

    // Simular actualización
    const updatedPolicy = {
      ...mockPolicy,
      ...body,
      idxpolicy: parseInt(id),
      updatedat: new Date().toISOString(),
    };

    return NextResponse.json({
      success: true,
      policy: updatedPolicy,
    });
  } catch (error) {
    console.error("Error in PUT /api/governance/policies/[id]:", error);
    return NextResponse.json(
      { success: false, error: "Error updating policy" },
      { status: 500 }
    );
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;

    // Simular eliminación
    return NextResponse.json({
      success: true,
      message: "Policy deleted successfully",
    });
  } catch (error) {
    console.error("Error in DELETE /api/governance/policies/[id]:", error);
    return NextResponse.json(
      { success: false, error: "Error deleting policy" },
      { status: 500 }
    );
  }
}

```

```typescript
import { NextRequest, NextResponse } from "next/server";

export async function POST(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;

    // Simular evaluación de política
    const evaluationResult = {
      success: true,
      score: 87.5,
      level: "SUCCESS",
      checks: {
        has_name: true,
        has_rules: true,
        has_valid_status: true,
        has_effective_date: true,
      },
    };

    return NextResponse.json(evaluationResult);
  } catch (error) {
    console.error("Error in POST /api/governance/policies/[id]/evaluate:", error);
    return NextResponse.json(
      { success: false, error: "Error evaluating policy" },
      { status: 500 }
    );
  }
}

```

```typescript
import { NextRequest, NextResponse } from "next/server";

export async function POST(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;

    // Simular verificación de cumplimiento
    const complianceResult = {
      success: true,
      assessmentId: `assessment_${Date.now()}`,
      message: "Compliance check completed successfully",
    };

    return NextResponse.json(complianceResult);
  } catch (error) {
    console.error("Error in POST /api/governance/policies/[id]/compliance-check:", error);
    return NextResponse.json(
      { success: false, error: "Error running compliance check" },
      { status: 500 }
    );
  }
}

```

```typescript
import { NextRequest, NextResponse } from "next/server";

export async function POST(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const { id } = params;

    // Simular cálculo de score
    const scoreResult = {
      success: true,
      score: 87.5,
      level: "GOOD",
      metrics: {
        total_rules: 2,
        active_rules: 2,
        total_evaluations: 2,
        avg_confidence: 87.5,
      },
    };

    return NextResponse.json(scoreResult);
  } catch (error) {
    console.error("Error in POST /api/governance/policies/[id]/calculate-score:", error);
    return NextResponse.json(
      { success: false, error: "Error calculating score" },
      { status: 500 }
    );
  }
}

```

Revisando el archivo de módulos para añadir las entradas al menú:


Buscando la sección de governance en el menú:


Actualizando el menú de governance para añadir las nuevas entradas:


```typescript
    case "Governance":
      return [
        {
          name: "Overview",
          href: `/governance/overview`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Policies",
          href: `/governance/policies`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
```

Corrigiendo la actualización del menú:


```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
    defaultPath: "/agents/monitoring/dashboard",
    description: "Gestión completa de agentes AI",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
  {
    name: "Core",
    icon: "Settings",
    roles: ["admin", "developer"],
    defaultPath: "/core/admin-dashboard",
    description: "Gestión del sistema core: usuarios, departamentos, roles, permisos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
    agents: "Agents",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Proyectos",
          href: `/projects`,
          icon: "FileText",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Overview",
          href: `/governance/overview`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Policies",
          href: `/governance/policies`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
          isDefault: true,
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Provider Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect"],
        },
      ];
    case "Core":
      return [
        {
          name: "Admin Dashboard",
          href: `/core/admin-dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer"],
          isDefault: true,
        },
        {
          name: "Departamentos",
          href: `/core/departments`,
          icon: "Building2",
          roles: ["admin", "developer"],
        },
        {
          name: "Usuarios",
          href: `/core/users`,
          icon: "Users",
          roles: ["admin", "developer"],
        },
        {
          name: "Roles",
          href: `/core/roles`,
          icon: "Shield",
          roles: ["admin", "developer"],
        },
        {
          name: "Permisos",
          href: `/core/permissions`,
          icon: "Key",
          roles: ["admin", "developer"],
        },
        {
          name: "Menús",
          href: `/core/menus`,
          icon: "Menu",
          roles: ["admin", "developer"],
        },
        {
          name: "Sesiones de Usuario",
          href: `/core/user-sessions`,
          icon: "Activity",
          roles: ["admin", "developer"],
        },
        {
          name: "Intentos de Login",
          href: `/core/login-attempts`,
          icon: "LogIn",
          roles: ["admin", "developer"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
    case "Agents":
      return [
        {
          name: "Dashboard",
          href: `/agents/monitoring/dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
          isDefault: true,
        },
        {
          name: "Alerts",
          href: `/agents/alerts/overview`,
          icon: "AlertTriangle",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Approval",
          href: `/agents/approval/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Bias Detection",
          href: `/agents/bias-detection/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Compliance",
          href: `/agents/compliance/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Decisions",
          href: `/agents/decisions/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Deployment",
          href: `/agents/deployment/overview`,
          icon: "Rocket",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Ethics",
          href: `/agents/ethics/overview`,
          icon: "Heart",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Governance",
          href: `/agents/governance/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Interactions",
          href: `/agents/interactions/overview`,
          icon: "Users",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Learning",
          href: `/agents/learning/overview`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Monitoring",
          href: `/agents/monitoring/overview`,
          icon: "Activity",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Registry",
          href: `/agents/registry/domain-overview`,
          icon: "Database",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Rollback",
          href: `/agents/rollback/overview`,
          icon: "RotateCcw",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Tools",
          href: `/agents/tools/overview`,
          icon: "Wrench",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Transparency",
          href: `/agents/transparency/overview`,
          icon: "Eye",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Versioning",
          href: `/agents/versioning/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Workflow",
          href: `/agents/workflow/execution-overview`,
          icon: "Workflow",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
      ];
  }
};

```

Añadiendo las traducciones. Revisando el archivo de i18n:


Añadiendo las traducciones necesarias. Buscando la sección de governance:


Añadiendo las traducciones necesarias:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Verificando si existe el componente Textarea:


Verificando cómo se importa Textarea en otros archivos:


Migración completada. Resumen:

## Migración completada

### 1. Páginas creadas

- **Overview** (`app/(app)/governance/overview/page.tsx`):
  - Listado de políticas con paginación
  - Filtros (búsqueda, tipo, estado, enforcement)
  - Métricas (total, activas, violaciones, score de cumplimiento)
  - Estilos "Wow Factor" con gradientes y animaciones

- **Detail** (`app/(app)/governance/detail/[id]/page.tsx`):
  - Formulario para crear/editar políticas
  - Tabs con información descendente (Reglas, Evaluaciones, Violaciones, Cumplimiento, Estadísticas)
  - Acciones: Evaluar, Verificar Cumplimiento, Calcular Score
  - Estilos "Wow Factor"

### 2. API routes con mock data

- `app/api/governance/policies/route.ts` - GET (listado) y POST (crear)
- `app/api/governance/policies/[id]/route.ts` - GET, PUT, DELETE
- `app/api/governance/policies/[id]/evaluate/route.ts` - Evaluar política
- `app/api/governance/policies/[id]/compliance-check/route.ts` - Verificar cumplimiento
- `app/api/governance/policies/[id]/calculate-score/route.ts` - Calcular score

### 3. Menú actualizado

- Añadida entrada "Overview" como página por defecto en el módulo Governance
- Mantenidas las entradas existentes (Policies, Security, Monitoring)

### 4. Traducciones añadidas

- Traducciones en español e inglés para:
  - Overview (métricas, filtros, tabla)
  - Detail (formulario, tabs, acciones)
  - Mensajes de éxito/error

### Características implementadas

- Estilos "Wow Factor" con gradientes, glassmorphism y animaciones
- Mock data para desarrollo y testing
- Estructura lista para integración con backend (solo cambiar variable de entorno)
- Responsive design
- Validaciones y manejo de errores
- Navegación entre páginas

Las páginas están listas para usar. Para desactivar el mock cuando el backend esté disponible, cambiar la variable de entorno `NEXT_PUBLIC_USE_MOCK=false` o actualizar las llamadas API en los servicios.

