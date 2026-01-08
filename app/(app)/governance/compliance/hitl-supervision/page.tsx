"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  UserCheck,
  Clock,
  CheckCircle,
  AlertTriangle,
  Filter,
  Settings,
  TrendingUp,
  AlertCircle,
  Eye,
  X,
  Edit,
  Save
} from "lucide-react";
import { useEffect, useState } from "react";
import { mockHitlData, type HitlIntervention, type HitlDecision, type HitlMetrics, type HitlSupervisionConfig } from "@/app/(app)/governance/data/mockHitl";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { RichTextEditor } from "@/components/ui/rich-text-editor";

export default function HitlSupervisionPage() {
  const { t } = useTranslation();
  const [data, setData] = useState(mockHitlData);
  const [loading, setLoading] = useState(false);
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [filterType, setFilterType] = useState<string>("ALL");
  const [filterDecisionType, setFilterDecisionType] = useState<string>("ALL");
  const [configDialogOpen, setConfigDialogOpen] = useState(false);
  const [selectedConfig, setSelectedConfig] = useState<HitlSupervisionConfig | null>(null);
  const [decisionDialogOpen, setDecisionDialogOpen] = useState(false);
  const [selectedIntervention, setSelectedIntervention] = useState<HitlIntervention | null>(null);
  const [decisionReason, setDecisionReason] = useState<string>("");
  const [decisionType, setDecisionType] = useState<string>("APPROVED");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/compliance/hitl/dashboard");
      if (response.ok) {
        const result = await response.json();
        if (result.success) {
          setData(result.data);
        }
      }
    } catch (error) {
      console.error("Error loading HITL data:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredInterventions = data?.pendingInterventions.filter((intervention) => {
    const matchesStatus = filterStatus === "ALL" || intervention.status === filterStatus;
    const matchesType = filterType === "ALL" || intervention.entityType === filterType;
    return matchesStatus && matchesType;
  });

  const filteredDecisions = data?.recentDecisions.filter((decision) => {
    const matchesType = filterDecisionType === "ALL" || decision.entityType === filterDecisionType;
    return matchesType;
  });

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "APPROVED":
        return (
          <Badge className="bg-green-500">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t("governance.hitlSupervision.approved")}
          </Badge>
        );
      case "REJECTED":
        return (
          <Badge variant="danger">
            <AlertTriangle className="h-3 w-3 mr-1" />
            {t("governance.hitlSupervision.rejected")}
          </Badge>
        );
      case "MODIFIED":
        return (
          <Badge className="bg-blue-500">
            <Edit className="h-3 w-3 mr-1" />
            {t("compliance.hitl.decisions.MODIFIED", "Modificado")}
          </Badge>
        );
      case "IN_REVIEW":
        return (
          <Badge className="bg-blue-500">
            <Clock className="h-3 w-3 mr-1" />
            {t("governance.hitlSupervision.inReview")}
          </Badge>
        );
      default:
        return (
          <Badge className="bg-yellow-500">
            <Clock className="h-3 w-3 mr-1" />
            {t("governance.hitlSupervision.pending")}
          </Badge>
        );
    }
  };

  const getUrgencyBadge = (urgency?: string) => {
    switch (urgency) {
      case "CRITICAL":
        return <Badge variant="danger">CRITICAL</Badge>;
      case "HIGH":
        return <Badge className="bg-orange-500">HIGH</Badge>;
      case "MEDIUM":
        return <Badge className="bg-yellow-500">MEDIUM</Badge>;
      default:
        return <Badge variant="outline">LOW</Badge>;
    }
  };

  const handleRecordDecision = async (interventionId: number, decision: string, reason: string) => {
    try {
      const response = await fetch("/api/compliance/hitl/interventions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          interventionId,
          decision,
          reason,
          userId: "current-user",
        }),
      });

      if (response.ok) {
        await loadData();
        setDecisionDialogOpen(false);
        setSelectedIntervention(null);
      }
    } catch (error) {
      console.error("Error recording decision:", error);
    }
  };

  const handleUpdateConfig = async (config: HitlSupervisionConfig) => {
    try {
      const response = await fetch("/api/compliance/hitl/config", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(config),
      });

      if (response.ok) {
        await loadData();
        setConfigDialogOpen(false);
        setSelectedConfig(null);
      }
    } catch (error) {
      console.error("Error updating config:", error);
    }
  };

  if (loading && !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="w-full min-h-full bg-gradient-to-br from-background via-background to-background/80 relative overflow-x-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-cyan-400/30 rounded-full animate-pulse" />
        <div className="absolute top-1/3 right-1/4 w-1.5 h-1.5 bg-cyan-400/20 rounded-full animate-pulse delay-300" />
        <div className="absolute bottom-1/4 left-1/3 w-2.5 h-2.5 bg-cyan-400/25 rounded-full animate-pulse delay-700" />
      </div>

      <div className="relative z-10 w-full space-y-6">
        {/* Header - HITL Supervision Dashboard */}
        <div className="flex items-center justify-between">
          <div className="text-center space-y-2">
            <div className="flex items-center justify-center gap-3 mb-4">
              <UserCheck className="w-8 h-8 text-cyan-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-cyan-500 to-cyan-700 bg-clip-text text-transparent">
                {t("governance.hitlSupervision.title")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.hitlSupervision.subtitle")}
            </p>
          </div>
          <Dialog open={configDialogOpen} onOpenChange={setConfigDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" onClick={() => setConfigDialogOpen(true)}>
                <Settings className="h-4 w-4 mr-2" />
                {t("common.configure", "Configurar")}
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-2xl">
              <DialogHeader>
                <DialogTitle>{t("compliance.hitl.configuration", "Configuración de Supervisión")}</DialogTitle>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setConfigDialogOpen(false)}
                  aria-label="Close"
                  style={{ backgroundColor: 'transparent', border: 'none', fontSize: '1.5rem', opacity: 0.5 }}
                ></button>
              </DialogHeader>
              <div className="modal-body" style={{ maxHeight: '70vh', overflowY: 'auto' }}>
                <div className="space-y-4">
                  {data.supervisionConfig.map((config) => (
                    <Card key={config.type}>
                      <CardHeader>
                        <CardTitle className="text-lg">
                          {t(`compliance.hitl.supervisionTypes.${config.type}`, config.type)}
                        </CardTitle>
                      </CardHeader>
                      <CardContent className="space-y-4">
                        <div className="flex items-center justify-between">
                          <Label>{t("compliance.hitl.enabled", "Habilitado")}</Label>
                          <Switch
                            checked={config.enabled}
                            onChange={(checked) => {
                              const updated = { ...config, enabled: checked };
                              handleUpdateConfig(updated);
                            }}
                          />
                        </div>
                        <div>
                          <Label>{t("compliance.hitl.slaHours", "SLA (horas)")}</Label>
                          <Input
                            type="number"
                            value={config.slaHours}
                            onChange={(e) => {
                              const updated = { ...config, slaHours: parseInt(e.target.value) };
                              handleUpdateConfig(updated);
                            }}
                          />
                        </div>
                        <div>
                          <Label>{t("compliance.hitl.autoEscalation", "Escalación Automática")}</Label>
                          <Switch
                            checked={config.autoEscalation}
                            onChange={(checked) => {
                              const updated = { ...config, autoEscalation: checked };
                              handleUpdateConfig(updated);
                            }}
                          />
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>
              <div className="modal-footer" style={{ borderTop: '1px solid var(--theme-border)', padding: '1rem' }}>
                <Button variant="outline" onClick={() => setConfigDialogOpen(false)}>
                  {t("common.close", "Cerrar")}
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        {/* Métricas Principales - SOLO 3 CARDS */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Card 1: Tiempo promedio de respuesta */}
          <Card className="group hover:scale-[1.02] transition-all duration-300 bg-card/80 backdrop-blur-sm border-border shadow-lg">
            <CardBody>
              <div className="flex items-center justify-between mb-2">
                <Clock className="h-5 w-5 text-cyan-500" />
                <TrendingUp className="h-4 w-4 text-green-500" />
              </div>
              <div className="text-sm text-muted-foreground">{t("governance.hitlSupervision.averageResponseTime")}</div>
              <div className="text-2xl font-bold">{data.metrics.averageResponseTime.toFixed(1)}h</div>
            </CardBody>
          </Card>
          {/* Card 2: Tasa de aprobación */}
          <Card className="group hover:scale-[1.02] transition-all duration-300 bg-card/80 backdrop-blur-sm border-border shadow-lg">
            <CardBody>
              <div className="flex items-center justify-between mb-2">
                <CheckCircle className="h-5 w-5 text-green-500" />
                <TrendingUp className="h-4 w-4 text-green-500" />
              </div>
              <div className="text-sm text-muted-foreground">{t("governance.hitlSupervision.approvalRate")}</div>
              <div className="text-2xl font-bold">
                {(data.metrics.approvalRate * 100).toFixed(0)}%
              </div>
            </CardBody>
          </Card>
          {/* Card 3: Tasa de cumplimiento SLA */}
          <Card className="group hover:scale-[1.02] transition-all duration-300 bg-card/80 backdrop-blur-sm border-border shadow-lg">
            <CardBody>
              <div className="flex items-center justify-between mb-2">
                <AlertCircle className="h-5 w-5 text-green-500" />
                <TrendingUp className="h-4 w-4 text-green-500" />
              </div>
              <div className="text-sm text-muted-foreground">{t("governance.hitlSupervision.slaComplianceRate")}</div>
              <div className="text-2xl font-bold text-green-600">
                {(data.metrics.slaCompliance * 100).toFixed(0)}%
              </div>
            </CardBody>
          </Card>
        </div>

        {/* Tabs para Intervenciones y Decisiones */}
        <Tabs defaultValue="interventions" className="space-y-4">
          <TabsList>
            <TabsTrigger value="interventions">
              {t("compliance.hitl.pendingInterventions", "Intervenciones Pendientes")} ({data.metrics.pendingInterventions})
            </TabsTrigger>
            <TabsTrigger value="decisions">
              {t("compliance.hitl.recentDecisions", "Decisiones Recientes")} ({data.recentDecisions.length})
            </TabsTrigger>
          </TabsList>

          <TabsContent value="interventions" className="space-y-4">
            <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>{t("compliance.hitl.pendingInterventions", "Intervenciones Pendientes")}</CardTitle>
                  <div className="flex items-center gap-2">
                    <Filter className="h-4 w-4 text-muted-foreground" />
                    <Select value={filterStatus} onValueChange={setFilterStatus}>
                      <SelectTrigger className="w-[150px]">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ALL">{t("governance.hitlSupervision.allStatus")}</SelectItem>
                        <SelectItem value="PENDING">{t("governance.hitlSupervision.pending")}</SelectItem>
                        <SelectItem value="IN_REVIEW">{t("governance.hitlSupervision.inReview")}</SelectItem>
                      </SelectContent>
                    </Select>
                    <Select value={filterType} onValueChange={setFilterType}>
                      <SelectTrigger className="w-[150px]">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ALL">{t("governance.hitlSupervision.allTypes")}</SelectItem>
                        <SelectItem value="Agent">{t("governance.hitlSupervision.agent")}</SelectItem>
                        <SelectItem value="Model">{t("governance.hitlSupervision.model")}</SelectItem>
                        <SelectItem value="Prompt">{t("governance.hitlSupervision.prompt")}</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                {filteredInterventions?.length === 0 ? (
                  <div className="text-center py-8 text-muted-foreground">
                    {t("compliance.hitl.noPendingInterventions", "No hay intervenciones pendientes")}
                  </div>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {filteredInterventions?.map((intervention) => {
                      const isSlaUrgent = intervention.timeRemaining < 1;
                      const isSlaWarning = intervention.timeRemaining < 2 && intervention.timeRemaining >= 1;

                      return (
                        <Card
                          key={intervention.id}
                          className="transition-all hover:shadow-lg"
                          style={{
                            backgroundColor: isSlaUrgent
                              ? "var(--theme-error)"
                              : isSlaWarning
                              ? "var(--theme-warning)"
                              : "var(--theme-card)",
                            borderColor: isSlaUrgent
                              ? "var(--theme-error)"
                              : isSlaWarning
                              ? "var(--theme-warning)"
                              : "var(--theme-border)",
                          }}
                        >
                          <CardContent className="p-4">
                            <div className="flex items-start gap-4">
                              {/* Contenido principal */}
                              <div className="flex-1 space-y-3">
                                <div className="flex items-center gap-2 mb-2 flex-wrap">
                                  <Badge variant="outline">{intervention.entityType}</Badge>
                                  <span className="font-semibold text-sm">{intervention.entityName}</span>
                                </div>
                                <div className="flex items-center gap-2 mb-3 flex-wrap">
                                  {getStatusBadge(intervention.status)}
                                  {getUrgencyBadge(intervention.urgency)}
                                  {isSlaUrgent && (
                                    <Badge variant="danger" className="text-xs">
                                      <AlertCircle className="h-3 w-3 mr-1" />
                                      SLA Urgente
                                    </Badge>
                                  )}
                                  {isSlaWarning && (
                                    <Badge className="bg-orange-500 text-xs">
                                      <Clock className="h-3 w-3 mr-1" />
                                      SLA Próximo
                                    </Badge>
                                  )}
                                </div>
                                <div className="text-sm text-muted-foreground space-y-1">
                                  <div>
                                    <strong>{t("compliance.hitl.type", "Tipo")}:</strong> {intervention.type}
                                  </div>
                                  <div>
                                    <strong>{t("compliance.hitl.createdAt", "Creado")}:</strong>{" "}
                                    {new Date(intervention.createdAt).toLocaleString()}
                                  </div>
                                  <div>
                                    <strong>{t("compliance.hitl.slaDeadline", "SLA Deadline")}:</strong>{" "}
                                    {new Date(intervention.slaDeadline).toLocaleString()}
                                  </div>
                                  <div>
                                    <strong>{t("compliance.hitl.timeRemaining", "Tiempo Restante")}:</strong>{" "}
                                    <span className={isSlaUrgent ? "text-red-600 font-bold" : isSlaWarning ? "text-orange-600 font-semibold" : ""}>
                                      {intervention.timeRemaining.toFixed(1)}h
                                    </span>
                                  </div>
                                </div>
                              </div>

                              {/* Botones de acción en el lateral derecho */}
                              {intervention.status === "PENDING" && (
                                <div className="flex flex-col gap-2 min-w-[120px]">
                                  <Button
                                    size="sm"
                                    variant="outline"
                                    className="w-full"
                                    onClick={() => {
                                      setSelectedIntervention(intervention);
                                      setDecisionDialogOpen(true);
                                    }}
                                  >
                                    <Eye className="h-4 w-4 mr-1" />
                                    {t("common.review", "Revisar")}
                                  </Button>
                                  <Button
                                    size="sm"
                                    className="w-full"
                                    onClick={() => {
                                      setSelectedIntervention(intervention);
                                      setDecisionType("APPROVED");
                                      setDecisionReason("");
                                      setDecisionDialogOpen(true);
                                    }}
                                  >
                                    <CheckCircle className="h-4 w-4 mr-1" />
                                    {t("common.approve", "Aprobar")}
                                  </Button>
                                  <Button
                                    size="sm"
                                    variant="danger"
                                    className="w-full"
                                    onClick={() => {
                                      setSelectedIntervention(intervention);
                                      setDecisionType("REJECTED");
                                      setDecisionReason("");
                                      setDecisionDialogOpen(true);
                                    }}
                                  >
                                    <X className="h-4 w-4 mr-1" />
                                    {t("common.reject", "Rechazar")}
                                  </Button>
                                </div>
                              )}
                            </div>
                          </CardContent>
                        </Card>
                      );
                    })}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value="decisions" className="space-y-4">
            <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>{t("compliance.hitl.recentDecisions", "Decisiones Recientes")}</CardTitle>
                  <div className="flex items-center gap-2">
                    <Filter className="h-4 w-4 text-muted-foreground" />
                    <Select value={filterDecisionType} onValueChange={setFilterDecisionType}>
                      <SelectTrigger className="w-[150px]">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ALL">{t("governance.hitlSupervision.allTypes")}</SelectItem>
                        <SelectItem value="Agent">{t("governance.hitlSupervision.agent")}</SelectItem>
                        <SelectItem value="Model">{t("governance.hitlSupervision.model")}</SelectItem>
                        <SelectItem value="Prompt">{t("governance.hitlSupervision.prompt")}</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                {filteredDecisions?.length === 0 ? (
                  <div className="text-center py-8 text-muted-foreground">
                    {t("compliance.hitl.noRecentDecisions", "No hay decisiones recientes")}
                  </div>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {filteredDecisions?.map((decision) => (
                      <Card
                        key={decision.id}
                        className="transition-all hover:shadow-lg bg-card"
                      >
                        <CardContent className="p-4 space-y-3">
                          <div className="flex items-center gap-2 mb-2 flex-wrap">
                            <Badge variant="outline">{decision.entityType}</Badge>
                            <span className="font-semibold text-sm">{decision.entityName}</span>
                          </div>
                          <div className="flex items-center gap-2 mb-3">
                            {getStatusBadge(decision.decision)}
                          </div>
                          <div className="text-sm text-muted-foreground space-y-1">
                            <div>
                              <strong>{t("compliance.hitl.decisionReason", "Razón")}:</strong>{" "}
                              <span className="text-foreground">{decision.decisionReason}</span>
                            </div>
                            <div>
                              <strong>{t("compliance.hitl.responseTime", "Tiempo de Respuesta")}:</strong>{" "}
                              {decision.responseTime.toFixed(1)}h
                            </div>
                            <div>
                              <strong>{t("compliance.hitl.decisionDate", "Fecha")}:</strong>{" "}
                              {new Date(decision.decisionDate).toLocaleString()}
                            </div>
                            <div>
                              <strong>{t("compliance.hitl.userId", "Usuario")}:</strong>{" "}
                              <span className="text-foreground">{decision.userId}</span>
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>
        </Tabs>
      </div>

      {/* Dialog para registrar decisión */}
      <Dialog open={decisionDialogOpen} onOpenChange={(open) => {
        setDecisionDialogOpen(open);
        if (!open) {
          setDecisionReason("");
          setDecisionType("APPROVED");
        }
      }}>
        <DialogContent maxWidth="700px">
          <DialogHeader>
            <DialogTitle>{t("compliance.hitl.recordDecision", "Registrar Decisión")}</DialogTitle>
            <button
              type="button"
              className="btn-close"
              onClick={() => setDecisionDialogOpen(false)}
              aria-label="Close"
              style={{ backgroundColor: 'transparent', border: 'none', fontSize: '1.5rem', opacity: 0.5 }}
            ></button>
          </DialogHeader>
          {selectedIntervention && (
            <div className="modal-body" style={{ minHeight: '550px', maxHeight: '80vh', overflowY: 'auto' }}>
              <div className="space-y-4">
                <div>
                  <Label>{t("compliance.hitl.entity", "Entidad")}</Label>
                  <Input value={selectedIntervention.entityName} readOnly />
                </div>
                <div>
                  <Label>{t("compliance.hitl.decision", "Decisión")}</Label>
                  <Select value={decisionType} onValueChange={setDecisionType}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="APPROVED">{t("compliance.hitl.decisions.APPROVED", "Aprobado")}</SelectItem>
                      <SelectItem value="REJECTED">{t("compliance.hitl.decisions.REJECTED", "Rechazado")}</SelectItem>
                      <SelectItem value="MODIFIED">{t("compliance.hitl.decisions.MODIFIED", "Modificado")}</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label className="mb-2 block">{t("compliance.hitl.decisionReason", "Razón de la Decisión")}</Label>
                  <RichTextEditor
                    value={decisionReason}
                    onChange={setDecisionReason}
                    placeholder={t("compliance.hitl.decisionReasonPlaceholder", "Explica la razón de la decisión...")}
                    minHeight="350px"
                  />
                </div>
              </div>
            </div>
          )}
          <div className="modal-footer" style={{ borderTop: '1px solid var(--theme-border)', padding: '1rem' }}>
            <div className="flex justify-end gap-2">
              <Button variant="outline" onClick={() => {
                setDecisionDialogOpen(false);
                setDecisionReason("");
                setDecisionType("APPROVED");
              }}>
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button onClick={() => {
                handleRecordDecision(selectedIntervention!.id, decisionType, decisionReason);
                setDecisionReason("");
                setDecisionType("APPROVED");
              }}>
                <Save className="h-4 w-4 mr-2" />
                {t("common.save", "Guardar")}
              </Button>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}
