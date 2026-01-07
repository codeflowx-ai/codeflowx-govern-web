"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Network,
  Download,
  CheckCircle,
  AlertTriangle,
  Link2,
  FileText,
  ArrowLeft,
  Shield,
  Activity,
  Users,
  Database,
  Eye,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import {
  mockEntityTraceability,
  mockProjectTraceability,
  mockAgentTraceability,
  type EntityTraceability,
  type IntegrityVerification,
} from "@/app/(app)/governance/data/mockTraceability";

export default function EntityTraceabilityPage() {
  const { t } = useTranslation();
  const params = useParams();
  const entityType = params?.entityType as string;
  const entityId = params?.id as string;

  const [loading, setLoading] = useState(true);
  const [data, setData] = useState<EntityTraceability | null>(null);
  const [integrityVerification, setIntegrityVerification] = useState<IntegrityVerification | null>(
    null
  );

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [entityType, entityId]);

  const loadData = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 300));

      let traceabilityData: EntityTraceability | null = null;
      const id = parseInt(entityId || "0");

      switch (entityType?.toUpperCase()) {
        case "MODEL":
          traceabilityData = { ...mockEntityTraceability, entityId: id };
          break;
        case "PROJECT":
          traceabilityData = { ...mockProjectTraceability, entityId: id };
          break;
        case "AGENT":
          traceabilityData = { ...mockAgentTraceability, entityId: id };
          break;
        default:
          traceabilityData = { ...mockEntityTraceability, entityId: id };
      }

      setData(traceabilityData);
      setIntegrityVerification(traceabilityData.traceability.integrityVerification);
    } catch (error) {
      console.error("Error loading entity traceability:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleExportEvidence = (format: "JSON" | "PDF") => {
    console.log(`Exporting evidence as ${format} for ${entityType} ${entityId}`);
    alert(`Exportando evidencias como ${format}...`);
  };

  const getIntegrityStatusColor = (status: string) => {
    switch (status) {
      case "INTEGRITY_OK":
        return "bg-green-500";
      case "INTEGRITY_WARNING":
        return "bg-yellow-500";
      case "INTEGRITY_ERROR":
        return "bg-red-500";
      default:
        return "bg-gray-500";
    }
  };

  const getEntityIcon = (type: string) => {
    switch (type?.toUpperCase()) {
      case "MODEL":
        return <Activity className="w-6 h-6 text-pink-500" />;
      case "PROJECT":
        return <Database className="w-6 h-6 text-blue-500" />;
      case "AGENT":
        return <Users className="w-6 h-6 text-purple-500" />;
      default:
        return <Network className="w-6 h-6 text-gray-500" />;
    }
  };

  if (loading || !data) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-pink-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-pink-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-blue-400/30 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10 w-full px-4 py-4 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Button
              variant="outline"
              size="sm"
              onClick={() => (window.location.href = "/governance/compliance/traceability")}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("governance.compliance.traceability.back", "Back")}
            </Button>
            <div className="flex items-center gap-2">
              {getEntityIcon(entityType)}
              <div>
                <h1 className="text-2xl font-bold bg-gradient-to-r from-pink-500 to-pink-700 bg-clip-text text-transparent">
                  {data.entityName}
                </h1>
                <p className="text-sm text-muted-foreground">
                  {t(`governance.compliance.traceability.entityTypes.${entityType}`, entityType)} #
                  {data.entityId}
                </p>
              </div>
            </div>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => handleExportEvidence("JSON")}>
              <Download className="w-4 h-4 mr-2" />
              JSON
            </Button>
            <Button variant="outline" size="sm" onClick={() => handleExportEvidence("PDF")}>
              <FileText className="w-4 h-4 mr-2" />
              PDF
            </Button>
          </div>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
              <div>
                <p className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.traceability.entityType", "Entity Type")}
                </p>
                <Badge variant="outline" className="text-sm">
                  {t(`governance.compliance.traceability.entityTypes.${data.entityType}`, data.entityType)}
                </Badge>
              </div>
              {integrityVerification && (
                <>
                  <div>
                    <p className="text-xs text-muted-foreground mb-1">
                      {t("governance.compliance.traceability.integrityScore", "Integrity Score")}
                    </p>
                    <div className="flex items-center gap-1">
                      <span className="text-xl font-bold">
                        {(integrityVerification.score * 100).toFixed(0)}%
                      </span>
                      <Badge className={getIntegrityStatusColor(integrityVerification.status)} size="sm">
                        {t(
                          `governance.compliance.traceability.integrityStatus.${integrityVerification.status}`,
                          integrityVerification.status
                        )}
                      </Badge>
                    </div>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground mb-1">
                      {t("governance.compliance.traceability.verifiedLogs", "Verified Logs")}
                    </p>
                    <p className="text-lg font-bold">
                      {integrityVerification.verifiedLogs}/{integrityVerification.totalLogs}
                    </p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground mb-1">
                      {t("governance.compliance.traceability.totalLogs", "Total Logs")}
                    </p>
                    <p className="text-lg font-bold">{integrityVerification.totalLogs}</p>
                  </div>
                </>
              )}
              <div>
                <p className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.traceability.hitlDecisions", "HITL Decisions")}
                </p>
                <p className="text-lg font-bold">{data.traceability.decisions.length}</p>
              </div>
              <div>
                <p className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.traceability.outputsGenerated", "Outputs")}
                </p>
                <p className="text-lg font-bold">{data.traceability.outputs.length}</p>
              </div>
            </div>

            {integrityVerification?.issues && integrityVerification.issues.length > 0 && (
              <div className="mt-3 p-3 bg-yellow-500/10 border border-yellow-500/20 rounded-lg">
                <div className="flex items-center gap-2 mb-1">
                  <AlertTriangle className="w-4 h-4 text-yellow-500" />
                  <p className="text-sm font-semibold">Problemas de Integridad Detectados</p>
                </div>
                <ul className="list-disc list-inside space-y-0.5">
                  {integrityVerification.issues.map((issue, idx) => (
                    <li key={idx} className="text-xs">
                      {issue}
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </CardBody>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base flex items-center gap-2">
                <Shield className="w-4 h-4 text-pink-500" />
                {t("governance.compliance.traceability.immutableLogs", "Immutable Logs")}
                <Badge variant="outline" className="ml-auto">
                  {data.traceability.logs.length}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardBody className="p-3 space-y-2 max-h-[600px] overflow-y-auto">
              {data.traceability.logs.map((log) => (
                <div key={log.id} className="p-3 border rounded-lg hover:bg-background/50 transition-colors">
                  <div className="flex items-center gap-2 mb-1 flex-wrap">
                    <Badge variant="outline" className="text-xs">
                      {log.type}
                    </Badge>
                    {log.integrityVerified ? (
                      <CheckCircle className="w-3 h-3 text-green-500" />
                    ) : (
                      <AlertTriangle className="w-3 h-3 text-yellow-500" />
                    )}
                    <span className="text-xs text-muted-foreground">
                      {new Date(log.timestamp).toLocaleDateString()}
                    </span>
                  </div>
                  <p className="text-xs mb-1">{log.description}</p>
                  <div className="flex items-center gap-2 text-xs text-muted-foreground">
                    <span className="truncate">{log.userId}</span>
                    <span className="font-mono">{log.hash.substring(0, 12)}...</span>
                  </div>
                </div>
              ))}
            </CardBody>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base flex items-center gap-2">
                <Users className="w-4 h-4 text-pink-500" />
                {t("governance.compliance.traceability.hitlDecisions", "HITL Decisions")}
                <Badge variant="outline" className="ml-auto">
                  {data.traceability.decisions.length}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardBody className="p-3 space-y-2 max-h-[600px] overflow-y-auto">
              {data.traceability.decisions.length > 0 ? (
                data.traceability.decisions.map((decision) => (
                  <div key={decision.id} className="p-3 border rounded-lg hover:bg-background/50 transition-colors">
                    <div className="flex items-center gap-2 mb-1 flex-wrap">
                      <Badge variant="outline" className="text-xs">
                        {decision.type}
                      </Badge>
                      <Badge
                        className={
                          decision.decision === "APPROVED"
                            ? "bg-green-500 text-xs"
                            : decision.decision === "REJECTED"
                            ? "bg-red-500 text-xs"
                            : "bg-yellow-500 text-xs"
                        }
                      >
                        {decision.decision}
                      </Badge>
                      <span className="text-xs text-muted-foreground">
                        {new Date(decision.timestamp).toLocaleDateString()}
                      </span>
                    </div>
                    <p className="text-xs mb-1">{decision.userId}</p>
                    {decision.notes && <p className="text-xs text-muted-foreground">{decision.notes}</p>}
                  </div>
                ))
              ) : (
                <p className="text-xs text-muted-foreground text-center py-4">No hay decisiones HITL</p>
              )}
            </CardBody>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base flex items-center gap-2">
                <Activity className="w-4 h-4 text-pink-500" />
                {t("governance.compliance.traceability.outputsGenerated", "Generated Outputs")}
                <Badge variant="outline" className="ml-auto">
                  {data.traceability.outputs.length}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardBody className="p-3 space-y-2 max-h-[600px] overflow-y-auto">
              {data.traceability.outputs.length > 0 ? (
                data.traceability.outputs.map((output) => (
                  <div key={output.id} className="p-3 border rounded-lg hover:bg-background/50 transition-colors">
                    <div className="flex items-center justify-between mb-2">
                      <Badge variant="outline" className="text-xs">
                        Output #{output.id}
                      </Badge>
                      <span className="text-xs text-muted-foreground">
                        {(output.confidence * 100).toFixed(0)}%
                      </span>
                    </div>
                    <div className="space-y-1">
                      <div>
                        <p className="text-xs text-muted-foreground mb-0.5">Input:</p>
                        <p
                          className="text-xs font-mono bg-background/50 p-1.5 rounded truncate"
                          title={output.input}
                        >
                          {output.input.substring(0, 60)}...
                        </p>
                      </div>
                      <div>
                        <p className="text-xs text-muted-foreground mb-0.5">Output:</p>
                        <p className="text-xs font-mono bg-background/50 p-1.5 rounded">{output.output}</p>
                      </div>
                    </div>
                    <p className="text-xs text-muted-foreground mt-1">
                      {new Date(output.timestamp).toLocaleDateString()}
                    </p>
                  </div>
                ))
              ) : (
                <p className="text-xs text-muted-foreground text-center py-4">No hay outputs generados</p>
              )}
            </CardBody>
          </Card>
        </div>

        {data.relatedEntities.length > 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base flex items-center gap-2">
                <Network className="w-4 h-4 text-pink-500" />
                {t("governance.compliance.traceability.relationships", "Relationships")}
                <Badge variant="outline" className="ml-auto">
                  {data.relatedEntities.length}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardBody className="p-3">
              <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
                {data.relatedEntities.map((entity, idx) => (
                  <div
                    key={idx}
                    className="p-3 border rounded-lg hover:bg-background/50 transition-colors flex flex-col"
                  >
                    <div className="flex items-center justify-between mb-1 flex-wrap gap-1">
                      <Badge variant="outline" className="text-xs">
                        {entity.type}
                      </Badge>
                      <Badge variant="secondary" className="text-xs">
                        {t(
                          `governance.compliance.traceability.relationships.${entity.relationship}`,
                          entity.relationship
                        )}
                      </Badge>
                    </div>
                    <p className="font-semibold text-sm mb-0.5 truncate" title={entity.name}>
                      {entity.name}
                    </p>
                    <p className="text-xs text-muted-foreground mb-2">#{entity.id}</p>
                    <Button
                      variant="outline"
                      size="sm"
                      className="w-full mt-auto"
                      onClick={() =>
                        (window.location.href = `/governance/compliance/traceability/${entity.type}/${entity.id}`)
                      }
                    >
                      <Eye className="w-3 h-3 mr-1" />
                      {t("governance.compliance.traceability.viewDetails", "Ver Detalles")}
                    </Button>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>
        )}
      </div>
    </div>
  );
}
