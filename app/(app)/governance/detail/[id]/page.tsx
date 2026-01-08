"use client";

import { useTranslation } from "@/app/config/i18n";
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      const url = isEditing ? `/api/governance/policies/${policy.idxpolicy}` : "/api/governance/policies";

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
        alert(
          t("governance.detail.compliance.success", "Verificación de cumplimiento completada")
        );
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
        alert(
          t("governance.detail.score.success", "Score calculado: {score}", { score: data.score })
        );
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

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
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
                <Button variant="outline" onClick={handleEvaluate} className="flex items-center gap-2">
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
                >
                  <option value="SECURITY">{t("governance.detail.form.type.security", "Seguridad")}</option>
                  <option value="COMPLIANCE">
                    {t("governance.detail.form.type.compliance", "Cumplimiento")}
                  </option>
                  <option value="ETHICS">{t("governance.detail.form.type.ethics", "Ética")}</option>
                  <option value="PRIVACY">
                    {t("governance.detail.form.type.privacy", "Privacidad")}
                  </option>
                  <option value="OPERATIONAL">
                    {t("governance.detail.form.type.operational", "Operacional")}
                  </option>
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
                >
                  <option value="MANDATORY">
                    {t("governance.detail.form.enforcement.mandatory", "Obligatorio")}
                  </option>
                  <option value="RECOMMENDED">
                    {t("governance.detail.form.enforcement.recommended", "Recomendado")}
                  </option>
                  <option value="OPTIONAL">
                    {t("governance.detail.form.enforcement.optional", "Opcional")}
                  </option>
                  <option value="INFORMATIONAL">
                    {t("governance.detail.form.enforcement.informational", "Información")}
                  </option>
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
                >
                  <option value="ACTIVE">{t("governance.detail.form.status.active", "Activo")}</option>
                  <option value="INACTIVE">{t("governance.detail.form.status.inactive", "Inactivo")}</option>
                  <option value="DRAFT">{t("governance.detail.form.status.draft", "Borrador")}</option>
                  <option value="UNDER_REVIEW">
                    {t("governance.detail.form.status.review", "Revisión")}
                  </option>
                  <option value="ARCHIVED">
                    {t("governance.detail.form.status.archived", "Archivado")}
                  </option>
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
                            <th className="text-left p-3 text-sm font-medium">
                              {t("governance.detail.tabs.rules.name", "Nombre")}
                            </th>
                            <th className="text-left p-3 text-sm font-medium">
                              {t("governance.detail.tabs.rules.condition", "Condición")}
                            </th>
                            <th className="text-left p-3 text-sm font-medium">
                              {t("governance.detail.tabs.rules.action", "Acción")}
                            </th>
                            <th className="text-left p-3 text-sm font-medium">
                              {t("governance.detail.tabs.rules.priority", "Prioridad")}
                            </th>
                            <th className="text-left p-3 text-sm font-medium">
                              {t("governance.detail.tabs.rules.active", "Activa")}
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          {rules.map((rule) => (
                            <tr key={rule.idxpolicyrule} className="border-b border-border/50">
                              <td className="p-3">{rule.idxpolicyrule}</td>
                              <td className="p-3">{rule.name}</td>
                              <td className="p-3 text-sm text-muted-foreground">
                                {rule.conditiontext.substring(0, 50)}...
                              </td>
                              <td className="p-3 text-sm text-muted-foreground">
                                {rule.actiontext.substring(0, 50)}...
                              </td>
                              <td className="p-3">
                                <Badge variant="outline">{rule.priority}</Badge>
                              </td>
                              <td className="p-3">
                                <Badge
                                  className={
                                    rule.isactive
                                      ? "bg-green-100 text-green-800"
                                      : "bg-gray-100 text-gray-800"
                                  }
                                >
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

              {activeTab === "statistics" && (
                <div className="space-y-6">
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
                            {t("governance.detail.tabs.statistics.lastUpdated", "Última actualización")}:
                          </p>
                          <p className="font-medium">
                            {policy.updatedat ? new Date(policy.updatedat).toLocaleDateString() : "N/A"}
                          </p>
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
