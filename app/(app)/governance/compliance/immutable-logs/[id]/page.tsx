"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Lock,
  ArrowLeft,
  ChevronLeft,
  ChevronRight,
  CheckCircle,
  AlertTriangle,
  Download,
  Link as LinkIcon,
  Copy,
  Check,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import type { ImmutableLog } from "@/app/(app)/governance/data/mockImmutableLogs";

interface LogDetailResponse {
  log: ImmutableLog;
  chain: ImmutableLog[];
  previousLog: ImmutableLog | null;
  nextLog: ImmutableLog | null;
  chainLength: number;
  currentPosition: number;
}

export default function ImmutableLogDetailPage() {
  const { t, language } = useTranslation();
  const params = useParams();
  const [loading, setLoading] = useState(true);
  const [logDetail, setLogDetail] = useState<LogDetailResponse | null>(null);
  const [copiedHash, setCopiedHash] = useState<string | null>(null);

  useEffect(() => {
    const logId = Array.isArray(params.id) ? params.id[0] : params.id;
    if (logId) loadLogDetail(logId);
  }, [params.id]);

  const loadLogDetail = async (logId: string | string[] | undefined) => {
    if (!logId) return;
    const id = Array.isArray(logId) ? logId[0] : logId;
    if (!id) return;

    try {
      setLoading(true);
      const response = await fetch(`/api/compliance/immutable-logs/${id}`);
      if (response.ok) {
        const data = await response.json();
        setLogDetail(data);
      } else {
        console.error("Error loading log detail");
      }
    } catch (error) {
      console.error("Error loading log detail:", error);
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = (text: string, type: string) => {
    navigator.clipboard.writeText(text);
    setCopiedHash(type);
    setTimeout(() => setCopiedHash(null), 2000);
  };

  const handleExportLog = () => {
    if (!logDetail) return;
    const exportData = { log: logDetail.log, chain: logDetail.chain, exportedAt: new Date().toISOString() };
    const jsonContent = JSON.stringify(exportData, null, 2);
    const blob = new Blob([jsonContent], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `immutable-log-${logDetail.log.id}-${new Date().toISOString()}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!logDetail) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
        <div className="relative z-10 w-full px-4 py-6">
          <Card>
            <CardBody className="text-center py-12">
              <p className="text-muted-foreground">
                {t("governance.compliance.immutableLogs.detail.notFound", "Log no encontrado")}
              </p>
              <Button
                variant="outline"
                className="mt-4"
                onClick={() => (window.location.href = "/governance/compliance/immutable-logs")}
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                {t("governance.compliance.immutableLogs.back", "Volver")}
              </Button>
            </CardBody>
          </Card>
        </div>
      </div>
    );
  }

  const { log, chain, previousLog, nextLog, chainLength, currentPosition } = logDetail;

  return (
    <div
      key={language}
      className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden"
    >
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-purple-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Button variant="outline" onClick={() => (window.location.href = "/governance/compliance/immutable-logs")}>
              <ArrowLeft className="h-4 w-4 mr-2" />
              {t("governance.compliance.immutableLogs.back", "Volver")}
            </Button>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-500 to-purple-700 bg-clip-text text-transparent flex items-center gap-3">
                <Lock className="w-8 h-8 text-purple-500" />
                {t("governance.compliance.immutableLogs.detail.title", "Detalle de Log Inmutable")}
              </h1>
              <p className="text-muted-foreground mt-1">
                {t("governance.compliance.immutableLogs.detail.subtitle", "ID")}: {log.id}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Button variant="outline" onClick={handleExportLog}>
              <Download className="h-4 w-4 mr-2" />
              {t("governance.compliance.immutableLogs.detail.export", "Exportar")}
            </Button>
          </div>
        </div>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <Button
                variant="outline"
                onClick={() =>
                  previousLog && (window.location.href = `/governance/compliance/immutable-logs/${previousLog.id}`)
                }
                disabled={!previousLog}
              >
                <ChevronLeft className="h-4 w-4 mr-2" />
                {t("governance.compliance.immutableLogs.detail.previous", "Log Anterior")}
              </Button>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.immutableLogs.detail.position", "Posición en la cadena")}
                </div>
                <div className="text-lg font-semibold">
                  {currentPosition} / {chainLength}
                </div>
              </div>
              <Button
                variant="outline"
                onClick={() =>
                  nextLog && (window.location.href = `/governance/compliance/immutable-logs/${nextLog.id}`)
                }
                disabled={!nextLog}
              >
                {t("governance.compliance.immutableLogs.detail.next", "Log Siguiente")}
                <ChevronRight className="h-4 w-4 ml-2" />
              </Button>
            </div>
          </CardBody>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-base">
                {t("governance.compliance.immutableLogs.detail.generalInfo", "Información General")}
              </CardTitle>
            </CardHeader>
            <CardBody className="space-y-2">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <div className="text-xs text-muted-foreground mb-0.5">
                    {t("governance.compliance.immutableLogs.detail.logType", "Tipo de Log")}
                  </div>
                  <Badge variant="outline" className="text-xs">
                    {log.logType}
                  </Badge>
                </div>
                <div>
                  <div className="text-xs text-muted-foreground mb-0.5">
                    {t("governance.compliance.immutableLogs.detail.entityType", "Tipo de Entidad")}
                  </div>
                  <div className="font-medium text-sm">{log.entityType}</div>
                </div>
                <div>
                  <div className="text-xs text-muted-foreground mb-0.5">
                    {t("governance.compliance.immutableLogs.detail.entityId", "ID de Entidad")}
                  </div>
                  <div className="font-medium text-sm">#{log.entityId}</div>
                  {log.entityName && (
                    <div className="text-xs text-muted-foreground mt-0.5">{log.entityName}</div>
                  )}
                </div>
                <div>
                  <div className="text-xs text-muted-foreground mb-0.5">
                    {t("governance.compliance.immutableLogs.detail.userId", "Usuario")}
                  </div>
                  <div className="font-medium text-sm truncate">{log.userId}</div>
                </div>
              </div>
              <div className="pt-2 border-t">
                <div className="text-xs text-muted-foreground mb-0.5">
                  {t("governance.compliance.immutableLogs.detail.timestamp", "Fecha y Hora")}
                </div>
                <div className="font-medium text-sm">{new Date(log.timestamp).toLocaleString()}</div>
                <div className="text-xs text-muted-foreground mt-0.5">{new Date(log.timestamp).toISOString()}</div>
              </div>
              <div className="pt-2 border-t">
                <div className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.immutableLogs.detail.integrity", "Integridad")}
                </div>
                <div className="flex items-center gap-1.5">
                  {log.integrityVerified ? (
                    <>
                      <CheckCircle className="h-4 w-4 text-green-600" />
                      <span className="text-green-600 font-semibold text-sm">
                        {t("governance.compliance.immutableLogs.detail.verified", "Verificado")}
                      </span>
                    </>
                  ) : (
                    <>
                      <AlertTriangle className="h-4 w-4 text-red-600" />
                      <span className="text-red-600 font-semibold text-sm">
                        {t("governance.compliance.immutableLogs.detail.notVerified", "No Verificado")}
                      </span>
                    </>
                  )}
                </div>
              </div>
            </CardBody>
          </Card>

          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-base flex items-center gap-2">
                <LinkIcon className="h-4 w-4" />
                {t("governance.compliance.immutableLogs.detail.hashChain", "Hash Chain")}
              </CardTitle>
            </CardHeader>
            <CardBody className="space-y-3">
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.immutableLogs.detail.currentHash", "Hash Actual (SHA-256)")}
                </div>
                <div className="flex items-center gap-1.5">
                  <div className="flex-1 font-mono text-xs bg-muted p-2 rounded break-all">{log.hash}</div>
                  <Button
                    variant="ghost"
                    size="sm"
                    className="h-8 w-8 p-0"
                    onClick={() => copyToClipboard(log.hash, "hash")}
                  >
                    {copiedHash === "hash" ? (
                      <Check className="h-3.5 w-3.5 text-green-600" />
                    ) : (
                      <Copy className="h-3.5 w-3.5" />
                    )}
                  </Button>
                </div>
              </div>
              <div>
                <div className="text-xs text-muted-foreground mb-1">
                  {t("governance.compliance.immutableLogs.detail.previousHash", "Hash Anterior")}
                </div>
                <div className="flex items-center gap-1.5">
                  <div className="flex-1 font-mono text-xs bg-muted p-2 rounded break-all">
                    {log.previousHash === "0"
                      ? t("governance.compliance.immutableLogs.detail.genesis", "Genesis (Primer log)")
                      : log.previousHash}
                  </div>
                  {log.previousHash !== "0" && (
                    <Button
                      variant="ghost"
                      size="sm"
                      className="h-8 w-8 p-0"
                      onClick={() => copyToClipboard(log.previousHash, "previousHash")}
                    >
                      {copiedHash === "previousHash" ? (
                        <Check className="h-3.5 w-3.5 text-green-600" />
                      ) : (
                        <Copy className="h-3.5 w-3.5" />
                      )}
                    </Button>
                  )}
                </div>
              </div>
              <div className="pt-2 border-t">
                <div className="grid grid-cols-2 gap-3 text-xs">
                  <div>
                    <span className="text-muted-foreground">
                      {t("governance.compliance.immutableLogs.detail.chainLength", "Longitud")}:
                    </span>
                    <span className="font-medium ml-1">{chainLength}</span>
                  </div>
                  <div>
                    <span className="text-muted-foreground">
                      {t("governance.compliance.immutableLogs.detail.currentPosition", "Posición")}:
                    </span>
                    <span className="font-medium ml-1">{currentPosition}</span>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-base">
                {t("governance.compliance.immutableLogs.detail.logData", "Datos del Log")}
              </CardTitle>
            </CardHeader>
            <CardBody className="py-3">
              <div className="bg-muted p-3 rounded-lg overflow-auto max-h-96">
                <pre className="text-xs font-mono whitespace-pre-wrap">
                  {JSON.stringify(log.logData, null, 2)}
                </pre>
              </div>
            </CardBody>
          </Card>

          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-base">
                {t("governance.compliance.immutableLogs.detail.chainVisualization", "Visualización de la Cadena")}
              </CardTitle>
            </CardHeader>
            <CardBody className="py-3">
              <div className="space-y-1.5 max-h-96 overflow-y-auto">
                {chain.map((chainLog, index) => (
                  <div
                    key={chainLog.id}
                    className={`p-2.5 rounded-lg border-2 transition-all ${
                      chainLog.id === log.id
                        ? "border-purple-500 bg-purple-100 dark:bg-purple-950/30 shadow-sm"
                        : "border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 hover:border-purple-300 dark:hover:border-purple-600"
                    }`}
                  >
                    <div className="flex items-center justify-between gap-3">
                      <div className="flex items-center gap-2 flex-1 min-w-0">
                        <div
                          className={`w-6 h-6 rounded-full flex items-center justify-center font-semibold text-xs flex-shrink-0 ${
                            chainLog.id === log.id
                              ? "bg-purple-500 text-white"
                              : "bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300"
                          }`}
                        >
                          {index + 1}
                        </div>
                        <div className="min-w-0 flex-1">
                          <div
                            className={`font-medium text-sm truncate ${
                              chainLog.id === log.id ? "text-purple-700 dark:text-purple-300" : ""
                            }`}
                          >
                            {chainLog.logType}
                          </div>
                          <div
                            className={`text-xs truncate ${
                              chainLog.id === log.id ? "text-purple-600 dark:text-purple-400" : ""
                            }`}
                          >
                            {chainLog.entityType} #{chainLog.entityId}
                          </div>
                        </div>
                      </div>
                      <div className="flex items-center gap-2 flex-shrink-0">
                        {chainLog.id === log.id ? (
                          <Badge className="bg-purple-500 text-white text-xs">Actual</Badge>
                        ) : (
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-7 text-xs dark:text-gray-300 hover:bg-purple-100 dark:hover:bg-purple-900/20"
                            onClick={() => (window.location.href = `/governance/compliance/immutable-logs/${chainLog.id}`)}
                          >
                            {t("governance.compliance.immutableLogs.view", "Ver")}
                          </Button>
                        )}
                      </div>
                    </div>
                    {index < chain.length - 1 && (
                      <div className="flex justify-center mt-1.5">
                        <div className="w-0.5 h-3 bg-purple-300 dark:bg-purple-600"></div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}
