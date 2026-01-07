"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Search,
  RefreshCw,
  Filter,
  X,
  Eye,
  Calendar,
  Database,
  AlertCircle,
  CheckCircle,
  XCircle,
  ArrowLeft,
} from "lucide-react";
import { telemetryService } from "../services/telemetryService";
import type { TelemetryEventDto } from "../types/telemetry";
import { Badge } from "@/components/ui/badge";

interface SearchCriteria {
  query?: string;
  componentUuid?: string;
  agentExternalId?: string;
  eventType?: string;
  severity?: string;
  startDate?: string;
  endDate?: string;
  biasChecked?: boolean;
  toxicityChecked?: boolean;
  piiDetected?: boolean;
  secretDetected?: boolean;
  complianceStatus?: string;
  riskLevel?: string;
  complianceCategory?: string;
  issueTag?: string;
  page: number;
  size: number;
}

export default function TelemetrySearchPage() {
  const { t } = useTranslation();
  const [criteria, setCriteria] = useState<SearchCriteria>({
    page: 0,
    size: 20,
  });
  const [events, setEvents] = useState<TelemetryEventDto[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
    // Ejecutar búsqueda automática al cargar la página
    const autoSearch = async () => {
      try {
        setLoading(true);
        // Hacer una búsqueda sin query para mostrar todos los eventos recientes
        const response = await telemetryService.searchByContent(
          "",
          0,
          20,
          undefined,
          undefined
        );
        if (response.success && response.data) {
          setEvents(response.data.events);
          setTotal(response.data.total);
        }
      } catch (error) {
        console.error("Error in auto search:", error);
      } finally {
        setLoading(false);
      }
    };
    autoSearch();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const filterEvents = (events: TelemetryEventDto[]): TelemetryEventDto[] => {
    return events.filter((event) => {
      // Filtrar por severity
      if (criteria.severity && event.severity !== criteria.severity) {
        return false;
      }

      // Filtrar por componentUuid
      if (criteria.componentUuid && event.componentUuid !== criteria.componentUuid) {
        return false;
      }

      // Filtrar por agentExternalId
      if (criteria.agentExternalId && event.agentExternalId !== criteria.agentExternalId) {
        return false;
      }

      // Filtrar por eventType
      if (criteria.eventType && event.eventType !== criteria.eventType) {
        return false;
      }

      // Filtrar por flags booleanos
      if (criteria.biasChecked !== undefined && event.biasChecked !== criteria.biasChecked) {
        return false;
      }
      if (criteria.toxicityChecked !== undefined && event.toxicityChecked !== criteria.toxicityChecked) {
        return false;
      }
      if (criteria.piiDetected !== undefined && event.piiDetected !== criteria.piiDetected) {
        return false;
      }
      if (criteria.secretDetected !== undefined && event.secretDetected !== criteria.secretDetected) {
        return false;
      }

      // Filtrar por complianceStatus
      if (criteria.complianceStatus && event.complianceStatus !== criteria.complianceStatus) {
        return false;
      }

      // Filtrar por riskLevel
      if (criteria.riskLevel && event.riskLevel !== criteria.riskLevel) {
        return false;
      }

      // Filtrar por complianceCategory
      if (criteria.complianceCategory && event.complianceCategory !== criteria.complianceCategory) {
        return false;
      }

      // Filtrar por issueTag
      if (criteria.issueTag && (!event.issueTags || !event.issueTags.includes(criteria.issueTag))) {
        return false;
      }

      return true;
    });
  };

  const performSearch = async () => {
    try {
      setLoading(true);

      // Obtener eventos (con query o sin query)
      let allEvents: TelemetryEventDto[] = [];
      let totalEvents = 0;

      if (criteria.query && criteria.query.trim() !== "") {
        const response = await telemetryService.searchByContent(
          criteria.query,
          0, // Obtener todos para filtrar después
          1000, // Límite alto para obtener todos los eventos
          criteria.startDate ? new Date(criteria.startDate).toISOString() : undefined,
          criteria.endDate ? new Date(criteria.endDate + 'T23:59:59').toISOString() : undefined
        );
        if (response.success && response.data) {
          allEvents = response.data.events || [];
          totalEvents = response.data.total || 0;
        }
      } else if (criteria.componentUuid) {
        const response = await telemetryService.getEventsByComponent(
          criteria.componentUuid,
          0,
          1000,
          criteria.startDate ? new Date(criteria.startDate).toISOString() : undefined,
          criteria.endDate ? new Date(criteria.endDate + 'T23:59:59').toISOString() : undefined
        );
        if (response.success && response.data) {
          allEvents = response.data.events || [];
          totalEvents = response.data.total || 0;
        }
      } else {
        const response = await telemetryService.searchByContent(
          "",
          0,
          1000,
          criteria.startDate ? new Date(criteria.startDate).toISOString() : undefined,
          criteria.endDate ? new Date(criteria.endDate + 'T23:59:59').toISOString() : undefined
        );
        if (response.success && response.data) {
          allEvents = response.data.events || [];
          totalEvents = response.data.total || 0;
        }
      }

      // Aplicar filtros adicionales
      const filteredEvents = filterEvents(allEvents);

      // Aplicar paginación
      const start = criteria.page * criteria.size;
      const end = start + criteria.size;
      const paginatedEvents = filteredEvents.slice(start, end);

      setEvents(paginatedEvents);
      setTotal(filteredEvents.length);
    } catch (error) {
      console.error("Error searching events:", error);
      setEvents([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  const updateCriteria = (updates: Partial<SearchCriteria>) => {
    setCriteria((prev) => ({ ...prev, ...updates, page: 0 }));
  };

  const clearFilters = () => {
    setCriteria({
      page: 0,
      size: 20,
    });
    setEvents([]);
    setTotal(0);
  };

  const getSeverityBadge = (severity: string) => {
    const severityConfig = {
      INFO: { label: "Info", className: "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300" },
      WARN: { label: "Warning", className: "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300" },
      ERROR: { label: "Error", className: "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300" },
      CRITICAL: { label: "Critical", className: "bg-red-200 text-red-900 dark:bg-red-900/40 dark:text-red-200" },
      DEBUG: { label: "Debug", className: "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300" },
    };
    const config = severityConfig[severity as keyof typeof severityConfig] || severityConfig.INFO;
    return <Badge className={config.className}>{config.label}</Badge>;
  };

  const getComplianceStatusBadge = (status?: string) => {
    if (!status) return null;
    const statusConfig = {
      PASS: { label: "PASS", className: "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300" },
      WARNING: { label: "WARNING", className: "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300" },
      REVIEW_REQUIRED: { label: "REVIEW", className: "bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-300" },
      VIOLATION: { label: "VIOLATION", className: "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300" },
      CRITICAL_VIOLATION: { label: "CRITICAL", className: "bg-red-200 text-red-900 dark:bg-red-900/40 dark:text-red-200" },
    };
    const config = statusConfig[status as keyof typeof statusConfig];
    if (!config) return null;
    return <Badge className={config.className}>{config.label}</Badge>;
  };

  const getRiskLevelBadge = (riskLevel?: string) => {
    if (!riskLevel) return null;
    const riskConfig = {
      LOW: { label: "LOW", className: "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300" },
      MEDIUM: { label: "MEDIUM", className: "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300" },
      HIGH: { label: "HIGH", className: "bg-orange-100 text-orange-800 dark:bg-orange-900/20 dark:text-orange-300" },
      CRITICAL: { label: "CRITICAL", className: "bg-red-200 text-red-900 dark:bg-red-900/40 dark:text-red-200" },
    };
    const config = riskConfig[riskLevel as keyof typeof riskConfig];
    if (!config) return null;
    return <Badge className={config.className}>{config.label}</Badge>;
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString();
  };

  const parseJsonSafely = (jsonString?: string) => {
    if (!jsonString) return null;
    try {
      return JSON.parse(jsonString);
    } catch {
      return null;
    }
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Search className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.telemetrySearch.title", "Búsqueda de Eventos de Telemetría")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.telemetrySearch.subtitle", "Busca y filtra eventos de telemetría con múltiples criterios")}
          </p>
        </div>
        <Button
          onClick={() => {
            if (typeof window !== 'undefined') {
              window.location.href = '/governance/telemetry/dashboard';
            }
          }}
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
        >
          <ArrowLeft className="h-4 w-4" />
          {t("governance.telemetrySearch.back", "Volver")}
        </Button>
      </div>

      {/* Filtros de Búsqueda */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Filter className="w-5 h-5" />
              {t("governance.telemetrySearch.filters", "Criterios de Búsqueda")}
            </CardTitle>
            <Button variant="outline" size="sm" onClick={clearFilters}>
              <X className="w-4 h-4 mr-2" />
              {t("governance.telemetrySearch.clearFilters", "Limpiar")}
            </Button>
          </div>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {/* Búsqueda por contenido */}
            <div>
              <Label htmlFor="query">
                {t("governance.telemetrySearch.query", "Búsqueda por contenido")}
              </Label>
              <Input
                id="query"
                type="text"
                placeholder={t("governance.telemetrySearch.queryPlaceholder", "Buscar en payload, métricas...")}
                value={criteria.query || ""}
                onChange={(e) => updateCriteria({ query: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Component UUID */}
            <div>
              <Label htmlFor="componentUuid">
                {t("governance.telemetrySearch.componentUuid", "UUID del Componente")}
              </Label>
              <Input
                id="componentUuid"
                type="text"
                placeholder="550e8400-e29b-41d4-a716-446655440001"
                value={criteria.componentUuid || ""}
                onChange={(e) => updateCriteria({ componentUuid: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Agent External ID */}
            <div>
              <Label htmlFor="agentExternalId">
                {t("governance.telemetrySearch.agentExternalId", "ID del Agente")}
              </Label>
              <Input
                id="agentExternalId"
                type="text"
                placeholder="agent-123"
                value={criteria.agentExternalId || ""}
                onChange={(e) => updateCriteria({ agentExternalId: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Event Type */}
            <div>
              <Label htmlFor="eventType">
                {t("governance.telemetrySearch.eventType", "Tipo de Evento")}
              </Label>
              <Input
                id="eventType"
                type="text"
                placeholder="INTERACTION_COMPLETED"
                value={criteria.eventType || ""}
                onChange={(e) => updateCriteria({ eventType: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Severity */}
            <div>
              <Label htmlFor="severity">
                {t("governance.telemetrySearch.severity", "Severidad")}
              </Label>
              <select
                id="severity"
                value={criteria.severity || ""}
                onChange={(e) => updateCriteria({ severity: e.target.value || undefined })}
                className="w-full mt-1 px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.telemetrySearch.all", "Todas")}</option>
                <option value="INFO">INFO</option>
                <option value="WARN">WARN</option>
                <option value="ERROR">ERROR</option>
                <option value="CRITICAL">CRITICAL</option>
                <option value="DEBUG">DEBUG</option>
              </select>
            </div>

            {/* Fecha Inicio */}
            <div>
              <Label htmlFor="startDate">
                {t("governance.telemetrySearch.startDate", "Fecha Inicio")}
              </Label>
              <Input
                id="startDate"
                type="date"
                value={criteria.startDate || ""}
                onChange={(e) => updateCriteria({ startDate: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Fecha Fin */}
            <div>
              <Label htmlFor="endDate">
                {t("governance.telemetrySearch.endDate", "Fecha Fin")}
              </Label>
              <Input
                id="endDate"
                type="date"
                value={criteria.endDate || ""}
                onChange={(e) => updateCriteria({ endDate: e.target.value || undefined })}
                className="mt-1"
              />
            </div>

            {/* Filtros booleanos */}
            <div className="space-y-2">
              <Label>{t("governance.telemetrySearch.governanceChecks", "Verificaciones de Gobernanza")}</Label>
              <div className="grid grid-cols-2 gap-2">
                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="biasChecked"
                    checked={criteria.biasChecked || false}
                    onChange={(e) => updateCriteria({ biasChecked: e.target.checked || undefined })}
                    className="rounded"
                  />
                  <Label htmlFor="biasChecked" className="text-sm">
                    {t("governance.telemetrySearch.biasChecked", "Bias")}
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="toxicityChecked"
                    checked={criteria.toxicityChecked || false}
                    onChange={(e) => updateCriteria({ toxicityChecked: e.target.checked || undefined })}
                    className="rounded"
                  />
                  <Label htmlFor="toxicityChecked" className="text-sm">
                    {t("governance.telemetrySearch.toxicityChecked", "Toxicidad")}
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="piiDetected"
                    checked={criteria.piiDetected || false}
                    onChange={(e) => updateCriteria({ piiDetected: e.target.checked || undefined })}
                    className="rounded"
                  />
                  <Label htmlFor="piiDetected" className="text-sm">
                    {t("governance.telemetrySearch.piiDetected", "PII")}
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="secretDetected"
                    checked={criteria.secretDetected || false}
                    onChange={(e) => updateCriteria({ secretDetected: e.target.checked || undefined })}
                    className="rounded"
                  />
                  <Label htmlFor="secretDetected" className="text-sm">
                    {t("governance.telemetrySearch.secretDetected", "Secretos")}
                  </Label>
                </div>
              </div>
            </div>

            {/* Compliance Status */}
            <div>
              <Label htmlFor="complianceStatus">
                {t("governance.telemetrySearch.complianceStatus", "Estado de Cumplimiento")}
              </Label>
              <select
                id="complianceStatus"
                value={criteria.complianceStatus || ""}
                onChange={(e) => updateCriteria({ complianceStatus: e.target.value || undefined })}
                className="w-full mt-1 px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.telemetrySearch.all", "Todos")}</option>
                <option value="PASS">PASS - Cumple</option>
                <option value="WARNING">WARNING - Advertencia</option>
                <option value="REVIEW_REQUIRED">REVIEW_REQUIRED - Requiere Revisión</option>
                <option value="VIOLATION">VIOLATION - Incumplimiento</option>
                <option value="CRITICAL_VIOLATION">CRITICAL_VIOLATION - Violación Crítica</option>
              </select>
            </div>

            {/* Risk Level */}
            <div>
              <Label htmlFor="riskLevel">
                {t("governance.telemetrySearch.riskLevel", "Nivel de Riesgo")}
              </Label>
              <select
                id="riskLevel"
                value={criteria.riskLevel || ""}
                onChange={(e) => updateCriteria({ riskLevel: e.target.value || undefined })}
                className="w-full mt-1 px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.telemetrySearch.all", "Todos")}</option>
                <option value="LOW">LOW - Bajo</option>
                <option value="MEDIUM">MEDIUM - Medio</option>
                <option value="HIGH">HIGH - Alto</option>
                <option value="CRITICAL">CRITICAL - Crítico</option>
              </select>
            </div>

            {/* Compliance Category */}
            <div>
              <Label htmlFor="complianceCategory">
                {t("governance.telemetrySearch.complianceCategory", "Categoría de Cumplimiento")}
              </Label>
              <select
                id="complianceCategory"
                value={criteria.complianceCategory || ""}
                onChange={(e) => updateCriteria({ complianceCategory: e.target.value || undefined })}
                className="w-full mt-1 px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.telemetrySearch.all", "Todas")}</option>
                <option value="GDPR">GDPR</option>
                <option value="CCPA">CCPA</option>
                <option value="HIPAA">HIPAA</option>
                <option value="OWASP">OWASP</option>
                <option value="PCI_DSS">PCI DSS</option>
                <option value="SOC2">SOC 2</option>
                <option value="SECURITY">SECURITY - Seguridad</option>
                <option value="LEGAL">LEGAL - Legal</option>
              </select>
            </div>

            {/* Issue Tag */}
            <div>
              <Label htmlFor="issueTag">
                {t("governance.telemetrySearch.issueTag", "Tag de Problema")}
              </Label>
              <select
                id="issueTag"
                value={criteria.issueTag || ""}
                onChange={(e) => updateCriteria({ issueTag: e.target.value || undefined })}
                className="w-full mt-1 px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("governance.telemetrySearch.all", "Todos")}</option>
                <option value="pii_exposure">PII Exposure</option>
                <option value="pii_leak">PII Leak</option>
                <option value="secret_leak">Secret Leak</option>
                <option value="hardcoded_secret">Hardcoded Secret</option>
                <option value="api_key_exposure">API Key Exposure</option>
                <option value="sql_injection">SQL Injection</option>
                <option value="xss">XSS</option>
                <option value="cve_detected">CVE Detected</option>
                <option value="gdpr_violation">GDPR Violation</option>
                <option value="ccpa_violation">CCPA Violation</option>
                <option value="hipaa_violation">HIPAA Violation</option>
                <option value="owasp_violation">OWASP Violation</option>
                <option value="pci_dss_violation">PCI DSS Violation</option>
                <option value="soc2_violation">SOC 2 Violation</option>
                <option value="consent_missing">Consent Missing</option>
                <option value="data_minimization_violation">Data Minimization Violation</option>
                <option value="critical_vulnerability">Critical Vulnerability</option>
                <option value="security_warning">Security Warning</option>
              </select>
            </div>
          </div>

          <div className="mt-4 flex justify-end">
            <Button onClick={performSearch} disabled={loading} className="flex items-center gap-2">
              <Search className="w-4 h-4" />
              {loading ? (
                <>
                  <RefreshCw className="w-4 h-4 animate-spin" />
                  {t("governance.telemetrySearch.searching", "Buscando...")}
                </>
              ) : (
                t("governance.telemetrySearch.search", "Buscar")
              )}
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Resultados */}
      {events.length > 0 && (
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center justify-between">
              <span>
                {t("governance.telemetrySearch.results", "Resultados")} ({total})
              </span>
              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => updateCriteria({ page: Math.max(0, criteria.page - 1) })}
                  disabled={criteria.page === 0}
                >
                  {t("governance.telemetrySearch.previous", "Anterior")}
                </Button>
                <span className="text-sm">
                  {t("governance.telemetrySearch.page", "Página")} {criteria.page + 1} / {Math.ceil(total / criteria.size)}
                </span>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => updateCriteria({ page: criteria.page + 1 })}
                  disabled={(criteria.page + 1) * criteria.size >= total}
                >
                  {t("governance.telemetrySearch.next", "Siguiente")}
                </Button>
              </div>
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {events.map((event) => {
                const metrics = parseJsonSafely(event.metrics);
                return (
                  <Card key={event.id} className="border-l-4 border-l-primary">
                    <CardBody className="p-4">
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <div>
                          <div className="flex items-center gap-2 mb-2">
                            <Calendar className="w-4 h-4 text-muted-foreground" />
                            <span className="text-sm font-medium">
                              {formatDate(event.timestamp)}
                            </span>
                          </div>
                          <div className="flex items-center gap-2 mb-2 flex-wrap">
                            {getSeverityBadge(event.severity)}
                            <Badge variant="outline">{event.eventType}</Badge>
                          </div>
                          {event.sourceTool && (
                            <Badge variant="outline" className="text-xs mb-2">
                              {event.sourceTool}
                            </Badge>
                          )}
                          <div className="flex items-center gap-2 mb-2 flex-wrap">
                            {getComplianceStatusBadge(event.complianceStatus)}
                            {getRiskLevelBadge(event.riskLevel)}
                            {event.complianceCategory && (
                              <Badge variant="outline" className="text-xs">
                                {event.complianceCategory}
                              </Badge>
                            )}
                          </div>
                          {event.issueTags && event.issueTags.length > 0 && (
                            <div className="flex flex-wrap gap-1 mb-2">
                              {event.issueTags.slice(0, 3).map((tag) => (
                                <Badge key={tag} variant="outline" className="text-xs">
                                  {tag}
                                </Badge>
                              ))}
                              {event.issueTags.length > 3 && (
                                <Badge variant="outline" className="text-xs">
                                  +{event.issueTags.length - 3}
                                </Badge>
                              )}
                            </div>
                          )}
                        </div>
                        <div>
                          <div className="space-y-1 text-sm">
                            <div>
                              <span className="font-medium">Component:</span>{" "}
                              <span className="font-mono text-xs">{event.componentUuid.substring(0, 36)}...</span>
                            </div>
                            {event.agentExternalId && (
                              <div>
                                <span className="font-medium">Agent:</span> {event.agentExternalId}
                              </div>
                            )}
                            {event.traceId && (
                              <div>
                                <span className="font-medium">Trace:</span> {event.traceId}
                              </div>
                            )}
                            {event.runId && (
                              <div>
                                <span className="font-medium">Run:</span> {event.runId}
                              </div>
                            )}
                          </div>
                        </div>
                        <div>
                          <div className="flex flex-wrap gap-2 mb-2">
                            {event.biasChecked && (
                              <Badge className="bg-purple-100 text-purple-800">
                                <CheckCircle className="w-3 h-3 mr-1" />
                                Bias
                              </Badge>
                            )}
                            {event.toxicityChecked && (
                              <Badge className="bg-orange-100 text-orange-800">
                                <CheckCircle className="w-3 h-3 mr-1" />
                                Toxicity
                              </Badge>
                            )}
                            {event.piiDetected && (
                              <Badge className="bg-red-100 text-red-800">
                                <AlertCircle className="w-3 h-3 mr-1" />
                                PII
                              </Badge>
                            )}
                            {event.secretDetected && (
                              <Badge className="bg-red-100 text-red-800">
                                <AlertCircle className="w-3 h-3 mr-1" />
                                Secret
                              </Badge>
                            )}
                          </div>
                          {metrics && (
                            <div className="text-xs space-y-1">
                              {metrics.latency_ms && (
                                <div>
                                  <span className="font-medium">Latency:</span> {metrics.latency_ms} ms
                                </div>
                              )}
                              {metrics.tokens_used && (
                                <div>
                                  <span className="font-medium">Tokens:</span> {metrics.tokens_used}
                                </div>
                              )}
                              {metrics.cost_usd && (
                                <div>
                                  <span className="font-medium">Cost:</span> ${metrics.cost_usd}
                                </div>
                              )}
                            </div>
                          )}
                        </div>
                      </div>
                      <div className="mt-4 flex justify-end">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => {
                            const eventData = encodeURIComponent(JSON.stringify(event));
                            window.location.href = `/governance/telemetry/events/${event.id}?data=${eventData}`;
                          }}
                          className="flex items-center gap-2"
                        >
                          <Database className="w-4 h-4" />
                          {t("governance.telemetrySearch.viewDetails", "Ver Detalle Completo")}
                        </Button>
                      </div>
                    </CardBody>
                  </Card>
                );
              })}
            </div>
          </CardBody>
        </Card>
      )}

      {!loading && events.length === 0 && mounted && (
        <Card>
          <CardBody className="text-center py-12">
            <Search className="w-12 h-12 mx-auto mb-4 text-muted-foreground" />
            <p className="text-muted-foreground">
              {t("governance.telemetrySearch.noResults", "No hay resultados. Aplica filtros y haz una búsqueda.")}
            </p>
          </CardBody>
        </Card>
      )}
    </div>
  );
}
