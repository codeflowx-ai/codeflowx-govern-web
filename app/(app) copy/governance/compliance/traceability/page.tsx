"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Network,
  Download,
  Filter,
  CheckCircle,
  AlertTriangle,
  Clock,
  Link2,
  FileText,
  TrendingUp,
  Search,
  Calendar,
  User,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  mockTraceability,
  type TraceabilityLog,
  type HitlDecision,
  type ModelOutput,
  type IntegrityVerification,
} from "../../data/mockTraceability";

interface TraceabilityEvidence {
  id: number;
  entityType: string;
  entityId: number;
  entityName: string;
  eventType: string;
  timestamp: string;
  userId: string;
  relatedEntities: {
    type: string;
    id: number;
    name: string;
  }[];
  hash: string;
  integrityVerified: boolean;
}

interface TraceabilityMetrics {
  totalEntities: number;
  totalLogs: number;
  verifiedLogs: number;
  integrityScore: number;
  totalDecisions: number;
  totalOutputs: number;
}

export default function TraceabilityPage() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [filterEntity, setFilterEntity] = useState<string>("ALL");
  const [searchTerm, setSearchTerm] = useState("");
  const [dateFilter, setDateFilter] = useState<string>("");
  const [userFilter, setUserFilter] = useState<string>("");
  const [selectedEntity, setSelectedEntity] = useState<string>("Model");

  const data = mockTraceability;

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));
    } catch (error) {
      console.error("Error loading traceability data:", error);
    } finally {
      setLoading(false);
    }
  };

  // Convertir logs a evidencias para visualización
  const evidences: TraceabilityEvidence[] = data.logs.map((log, idx) => ({
    id: log.id,
    entityType: log.entityType,
    entityId: log.entityId,
    entityName: log.entityType === "Model" ? data.model.name : data.dataset.name,
    eventType: log.type,
    timestamp: log.timestamp,
    userId: log.userId,
    relatedEntities: [],
    hash: log.hash.substring(0, 20) + "...",
    integrityVerified: log.integrityVerified,
  }));

  const filteredEvidences = evidences.filter((ev) => {
    const matchesEntity = filterEntity === "ALL" || ev.entityType === filterEntity;
    const matchesSearch = !searchTerm || ev.entityName.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDate = !dateFilter || ev.timestamp.startsWith(dateFilter);
    const matchesUser = !userFilter || ev.userId.toLowerCase().includes(userFilter.toLowerCase());
    return matchesEntity && matchesSearch && matchesDate && matchesUser;
  });

  const metrics: TraceabilityMetrics = {
    totalEntities: 3,
    totalLogs: data.logs.length,
    verifiedLogs: data.logs.filter((l) => l.integrityVerified).length,
    integrityScore: data.integrityVerification.score,
    totalDecisions: data.decisions.length,
    totalOutputs: data.outputs.length,
  };

  const handleViewDetails = (entityType: string, entityId: number) => {
    window.location.href = `/governance/compliance/traceability/${entityType}/${entityId}`;
  };

  const handleExportEvidence = (format: "JSON" | "PDF") => {
    // TODO: Implementar exportación real
    console.log(`Exporting evidence as ${format}`);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-pink-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-pink-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-blue-400/30 rounded-full animate-pulse delay-300" />
        <div className="absolute bottom-1/4 left-1/2 w-1 h-1 bg-purple-400/30 rounded-full animate-pulse delay-700" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-left space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Network className="w-8 h-8 text-pink-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-pink-500 to-pink-700 bg-clip-text text-transparent">
              {t("governance.compliance.traceability.title", "Traceability & Evidence")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.traceability.subtitle", "Complete model-dataset-output traceability according to Art. 12 and 19 EU AI Act")}
          </p>
        </div>

        {/* Métricas de Trazabilidad */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">{t("governance.compliance.traceability.totalEntities", "Total Entities")}</p>
                  <p className="text-2xl font-bold">{metrics.totalEntities}</p>
                </div>
                <Network className="w-8 h-8 text-pink-500/50" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">{t("governance.compliance.traceability.verifiedLogs", "Verified Logs")}</p>
                  <p className="text-2xl font-bold">{metrics.verifiedLogs}/{metrics.totalLogs}</p>
                </div>
                <CheckCircle className="w-8 h-8 text-green-500/50" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">{t("governance.compliance.traceability.integrityScoreLabel", "Integrity Score")}</p>
                  <p className="text-2xl font-bold">{(metrics.integrityScore * 100).toFixed(0)}%</p>
                </div>
                <TrendingUp className="w-8 h-8 text-blue-500/50" />
              </div>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardBody className="p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">{t("governance.compliance.traceability.hitlDecisionsLabel", "HITL Decisions")}</p>
                  <p className="text-2xl font-bold">{metrics.totalDecisions}</p>
                </div>
                <FileText className="w-8 h-8 text-purple-500/50" />
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Filtros y Búsqueda */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="w-5 h-5 text-pink-500" />
              {t("governance.compliance.traceability.filtersAndSearch", "Filters and Search")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.compliance.traceability.searchEntity", "Search entity...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <div className="relative">
                <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <select
                  value={filterEntity}
                  onChange={(e) => setFilterEntity(e.target.value)}
                  className="w-full px-3 py-2 pl-10 border rounded-md text-sm bg-background"
                >
                  <option value="ALL">{t("governance.compliance.traceability.allEntities", "All Entities")}</option>
                  <option value="Model">{t("governance.compliance.traceability.entityTypes.Model", "Model")}</option>
                  <option value="Project">{t("governance.compliance.traceability.entityTypes.Project", "Project")}</option>
                  <option value="Agent">{t("governance.compliance.traceability.entityTypes.Agent", "Agent")}</option>
                  <option value="Dataset">{t("governance.compliance.traceability.entityTypes.Dataset", "Dataset")}</option>
                  <option value="Output">{t("governance.compliance.traceability.entityTypes.Output", "Output")}</option>
                </select>
              </div>
              <div className="relative">
                <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  type="date"
                  placeholder={t("governance.compliance.traceability.filterByDate", "Filter by date...")}
                  value={dateFilter}
                  onChange={(e) => setDateFilter(e.target.value)}
                  className="pl-10"
                />
              </div>
              <div className="relative">
                <User className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.compliance.traceability.filterByUser", "Filter by user...")}
                  value={userFilter}
                  onChange={(e) => setUserFilter(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Evidencias de Trazabilidad */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <FileText className="w-5 h-5 text-pink-500" />
                {t("governance.compliance.traceability.evidences", "Traceability Evidences")}
              </CardTitle>
              <Badge variant="outline">
                {filteredEvidences.length} {t("governance.compliance.traceability.evidences", "evidences")}
              </Badge>
            </div>
          </CardHeader>
          <CardBody>
            {filteredEvidences.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("governance.compliance.traceability.noEvidencesFound", "No evidences found with applied filters")}
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {filteredEvidences.map((evidence) => (
                  <Card
                    key={evidence.id}
                    className="backdrop-blur-md bg-background/60 border-border/50 hover:bg-background/70 transition-colors"
                  >
                    <CardBody className="p-4 space-y-3">
                      <div className="flex items-start justify-between gap-2">
                        <div className="flex-1 min-w-0">
                          <div className="flex items-center gap-2 mb-2 flex-wrap">
                            <Badge variant="outline" className="text-xs">{evidence.entityType}</Badge>
                            <Badge variant="secondary" className="text-xs">{evidence.eventType}</Badge>
                            {evidence.integrityVerified ? (
                              <Badge className="bg-green-500 text-xs">
                                <CheckCircle className="h-3 w-3 mr-1" />
                                {t("governance.compliance.traceability.verified", "Verified")}
                              </Badge>
                            ) : (
                              <Badge className="bg-yellow-500 text-xs">
                                <AlertTriangle className="h-3 w-3 mr-1" />
                                {t("governance.compliance.traceability.pending", "Pending")}
                              </Badge>
                            )}
                          </div>
                          <h3 className="font-semibold text-base mb-2 truncate" title={evidence.entityName}>
                            {evidence.entityName}
                          </h3>
                        </div>
                      </div>
                      <div className="flex items-center gap-4 text-xs text-muted-foreground">
                        <div className="flex items-center gap-1">
                          <User className="w-3 h-3" />
                          <span className="truncate">{evidence.userId}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <Clock className="w-3 h-3" />
                          <span className="truncate">{new Date(evidence.timestamp).toLocaleDateString()}</span>
                        </div>
                      </div>
                      {evidence.relatedEntities.length > 0 && (
                        <div>
                          <div className="text-xs font-medium mb-1">
                            {t("governance.compliance.traceability.relatedEntities", "Related Entities")}:
                          </div>
                          <div className="flex flex-wrap gap-1">
                            {evidence.relatedEntities.slice(0, 2).map((rel, idx) => (
                              <Badge key={idx} variant="outline" className="text-xs">
                                {rel.type}: #{rel.id}
                              </Badge>
                            ))}
                            {evidence.relatedEntities.length > 2 && (
                              <Badge variant="outline" className="text-xs">
                                +{evidence.relatedEntities.length - 2}
                              </Badge>
                            )}
                          </div>
                        </div>
                      )}
                      <div className="text-xs">
                        <div className="text-muted-foreground mb-1">
                          {t("governance.compliance.traceability.hash", "Hash")}:
                        </div>
                        <div className="font-mono bg-background/50 p-2 rounded text-xs truncate" title={evidence.hash}>
                          {evidence.hash}
                        </div>
                      </div>
                      <Button
                        variant="outline"
                        size="sm"
                        className="w-full mt-2"
                        onClick={() => handleViewDetails(evidence.entityType, evidence.entityId)}
                      >
                        {t("governance.compliance.traceability.viewDetails", "View Details")}
                      </Button>
                    </CardBody>
                  </Card>
                ))}
              </div>
            )}
          </CardBody>
        </Card>

        {/* Ejemplo de Cadena Modelo-Dataset-Output (Resumen) */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Link2 className="w-5 h-5 text-pink-500" />
                {t("governance.compliance.traceability.modelChain", "Model-Dataset-Output Chain")}
                <Badge variant="outline" className="ml-2 text-xs">
                  {t("governance.compliance.traceability.example", "Example")}
                </Badge>
              </CardTitle>
              <Badge className={data.integrityVerification.status === "INTEGRITY_OK" ? "bg-green-500" : "bg-yellow-500"}>
                <CheckCircle className="h-3 w-3 mr-1" />
                {t(`governance.compliance.traceability.integrityStatus.${data.integrityVerification.status}`, data.integrityVerification.status)}
              </Badge>
            </div>
          </CardHeader>
          <CardBody>
            <p className="text-sm text-muted-foreground mb-4">
              {t("governance.compliance.traceability.modelChainDescription", "Example of a complete traceability chain showing how a model is linked to its training dataset and generated outputs.")}
            </p>
            <div className="flex flex-col md:flex-row items-center justify-between gap-6">
              <div className="flex flex-col md:flex-row items-center gap-4 flex-1">
                <div className="text-center p-4 rounded-lg bg-pink-500/10 border border-pink-500/20 min-w-[200px]">
                  <div className="text-sm text-muted-foreground mb-2">{t("governance.compliance.traceability.model", "Model")}</div>
                  <div className="font-semibold text-lg mb-2">{data.model.name}</div>
                  <Badge variant="outline" className="mt-1">
                    ID: {data.model.id} | v{data.model.version}
                  </Badge>
                </div>
                <div className="text-2xl text-pink-500">→</div>
                <div className="text-center p-4 rounded-lg bg-blue-500/10 border border-blue-500/20 min-w-[200px]">
                  <div className="text-sm text-muted-foreground mb-2">{t("governance.compliance.traceability.dataset", "Dataset")}</div>
                  <div className="font-semibold text-lg mb-2">{data.dataset.name}</div>
                  <Badge variant="outline" className="mt-1">
                    ID: {data.dataset.id} | v{data.dataset.version}
                  </Badge>
                </div>
                <div className="text-2xl text-pink-500">→</div>
                <div className="text-center p-4 rounded-lg bg-purple-500/10 border border-purple-500/20 min-w-[200px]">
                  <div className="text-sm text-muted-foreground mb-2">{t("governance.compliance.traceability.output", "Output")}</div>
                  <div className="font-semibold text-lg mb-2">{data.outputs.length} {t("governance.compliance.traceability.outputs", "Outputs")}</div>
                  <Badge variant="outline" className="mt-1">
                    {t("governance.compliance.traceability.generated", "Generated")}
                  </Badge>
                </div>
              </div>
              <div className="flex flex-col gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleViewDetails("Model", data.model.id)}
                >
                  {t("governance.compliance.traceability.viewDetails", "View Details")}
                </Button>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleExportEvidence("JSON")}
                  >
                    <Download className="h-4 w-4 mr-2" />
                    JSON
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleExportEvidence("PDF")}
                  >
                    <FileText className="h-4 w-4 mr-2" />
                    PDF
                  </Button>
                </div>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}


