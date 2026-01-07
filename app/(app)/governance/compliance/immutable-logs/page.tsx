"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Lock,
  Search,
  Download,
  CheckCircle,
  AlertTriangle,
  Filter,
  ChevronLeft,
  ChevronRight,
  Eye,
  FileText,
  FileJson,
  Link as LinkIcon,
  Calendar,
  X,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  mockImmutableLogs,
  LOG_TYPES,
  ENTITY_TYPES,
  type ImmutableLog,
  type IntegrityVerificationResult
} from "@/app/(app)/governance/data/mockImmutableLogs";

interface SearchCriteria {
  logType?: string;
  entityType?: string;
  entityId?: string;
  userId?: string;
  startDate?: string;
  endDate?: string;
  hash?: string;
  searchText?: string;
  page?: number;
  pageSize?: number;
  sortBy?: "timestamp" | "logType" | "entityType";
  sortOrder?: "asc" | "desc";
}

interface SearchResponse {
  logs: ImmutableLog[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
}

export default function ImmutableLogsPage() {
  const { t, language } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [searchResults, setSearchResults] = useState<SearchResponse | null>(null);
  const [integrityResult, setIntegrityResult] = useState<IntegrityVerificationResult | null>(null);
  const [showFilters, setShowFilters] = useState(false);
  const [verifying, setVerifying] = useState(false);

  // Filtros
  const [criteria, setCriteria] = useState<SearchCriteria>({
    page: 1,
    pageSize: 20,
    sortBy: "timestamp",
    sortOrder: "desc"
  });

  useEffect(() => {
    performSearch();
  }, [criteria]);

  const performSearch = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/compliance/immutable-logs/search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(criteria),
      });

      if (response.ok) {
        const data = await response.json();
        setSearchResults(data);
      }
    } catch (error) {
      console.error("Error searching logs:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyIntegrity = async () => {
    if (!searchResults || searchResults.total === 0) return;

    try {
      setVerifying(true);
      // Obtener todos los resultados de la búsqueda (sin paginación)
      const allResultsCriteria = { ...criteria, page: 1, pageSize: searchResults.total };
      const response = await fetch("/api/compliance/immutable-logs/search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(allResultsCriteria),
      });

      if (response.ok) {
        const allResults = await response.json();
        if (allResults.logs && allResults.logs.length > 0) {
          const logIds = allResults.logs.map((log: ImmutableLog) => log.id);
          const startId = Math.min(...logIds);
          const endId = Math.max(...logIds);

          const verifyResponse = await fetch("/api/compliance/immutable-logs/verify-integrity", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ startId, endId }),
          });

          if (verifyResponse.ok) {
            const data = await verifyResponse.json();
            setIntegrityResult(data);
          }
        }
      }
    } catch (error) {
      console.error("Error verifying integrity:", error);
    } finally {
      setVerifying(false);
    }
  };

  const handleExportCSV = () => {
    if (!searchResults) return;

    const headers = ["ID", "Tipo", "Entidad", "ID Entidad", "Usuario", "Fecha", "Hash", "Hash Anterior"];
    const rows = searchResults.logs.map(log => [
      log.id,
      log.logType,
      log.entityType,
      log.entityId,
      log.userId,
      new Date(log.timestamp).toISOString(),
      log.hash,
      log.previousHash
    ]);

    const csvContent = [headers, ...rows]
      .map(row => row.map(cell => `"${cell}"`).join(","))
      .join("\n");

    const blob = new Blob([csvContent], { type: "text/csv" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `immutable-logs-${new Date().toISOString()}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleExportJSON = () => {
    if (!searchResults) return;

    const jsonContent = JSON.stringify(searchResults.logs, null, 2);
    const blob = new Blob([jsonContent], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `immutable-logs-${new Date().toISOString()}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleExportHashChain = async () => {
    if (!searchResults || searchResults.total === 0) return;

    try {
      setLoading(true);
      // Obtener todos los resultados de la búsqueda (sin paginación)
      const allResultsCriteria = { ...criteria, page: 1, pageSize: searchResults.total };
      const response = await fetch("/api/compliance/immutable-logs/search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(allResultsCriteria),
      });

      if (response.ok) {
        const allResults = await response.json();
        if (allResults.logs && allResults.logs.length > 0) {
          // Ordenar por ID para mantener la secuencia de la cadena
          const sortedLogs = [...allResults.logs].sort((a, b) => a.id - b.id);

          const hashChain = sortedLogs.map(log => ({
            id: log.id,
            hash: log.hash,
            previousHash: log.previousHash,
            timestamp: log.timestamp
          }));

          const jsonContent = JSON.stringify({
            exportedAt: new Date().toISOString(),
            totalLogs: hashChain.length,
            searchCriteria: {
              logType: criteria.logType,
              entityType: criteria.entityType,
              startDate: criteria.startDate,
              endDate: criteria.endDate
            },
            chain: hashChain
          }, null, 2);

          const blob = new Blob([jsonContent], { type: "application/json" });
          const url = URL.createObjectURL(blob);
          const a = document.createElement("a");
          a.href = url;
          a.download = `hash-chain-${new Date().toISOString()}.json`;
          a.click();
          URL.revokeObjectURL(url);
        }
      }
    } catch (error) {
      console.error("Error exporting hash chain:", error);
    } finally {
      setLoading(false);
    }
  };

  const updateCriteria = (updates: Partial<SearchCriteria>) => {
    setCriteria(prev => ({ ...prev, ...updates, page: 1 }));
  };

  const clearFilters = () => {
    setCriteria({
      page: 1,
      pageSize: 20,
      sortBy: "timestamp",
      sortOrder: "desc"
    });
  };

  const getIntegrityStatusColor = (status?: string) => {
    switch (status) {
      case "INTEGRITY_OK":
        return "text-green-600";
      case "INTEGRITY_PARTIAL":
        return "text-yellow-600";
      case "INTEGRITY_BROKEN":
        return "text-red-600";
      default:
        return "text-gray-600";
    }
  };

  const getIntegrityStatusIcon = (status?: string) => {
    switch (status) {
      case "INTEGRITY_OK":
        return <CheckCircle className="h-5 w-5 text-green-600" />;
      case "INTEGRITY_PARTIAL":
        return <AlertTriangle className="h-5 w-5 text-yellow-600" />;
      case "INTEGRITY_BROKEN":
        return <AlertTriangle className="h-5 w-5 text-red-600" />;
      default:
        return null;
    }
  };

  return (
    <div key={language} className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-purple-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-blue-400/30 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-left space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Lock className="w-8 h-8 text-purple-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-purple-500 to-purple-700 bg-clip-text text-transparent">
              {t("governance.compliance.immutableLogs.title", "Logs Inmutables")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.immutableLogs.subtitle", "Búsqueda y verificación de logs inmutables según Art. 19 EU AI Act")}
          </p>
        </div>

        {/* Estadísticas rápidas */}
        {searchResults && (
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <Card>
              <CardBody>
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.immutableLogs.totalLogs", "Total de Logs")}
                </div>
                <div className="text-2xl font-bold">{searchResults.total}</div>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.immutableLogs.currentPage", "Página Actual")}
                </div>
                <div className="text-2xl font-bold">
                  {searchResults.page} / {searchResults.totalPages}
                </div>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.immutableLogs.integrityStatus", "Estado de Integridad")}
                </div>
                <div className="flex items-center gap-2 mt-1">
                  {integrityResult ? (
                    <>
                      {getIntegrityStatusIcon(integrityResult.status)}
                      <span className={`font-semibold ${getIntegrityStatusColor(integrityResult.status)}`}>
                        {integrityResult.integrityScore.toFixed(2)}
                      </span>
                    </>
                  ) : (
                    <span className="text-muted-foreground">
                      {t("governance.compliance.immutableLogs.notVerified", "No verificado")}
                    </span>
                  )}
                </div>
              </CardBody>
            </Card>
            <Card>
              <CardBody>
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.immutableLogs.verifiedLogs", "Logs Verificados")}
                </div>
                <div className="text-2xl font-bold text-green-600">
                  {integrityResult?.verifiedLogs || searchResults.logs.filter(l => l.integrityVerified).length}
                </div>
              </CardBody>
            </Card>
          </div>
        )}

        {/* Búsqueda y Filtros */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Search className="h-5 w-5" />
                {t("governance.compliance.immutableLogs.search.title", "Búsqueda Avanzada")}
              </CardTitle>
              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setShowFilters(!showFilters)}
                >
                  <Filter className="h-4 w-4 mr-2" />
                  {t("governance.compliance.immutableLogs.filters", "Filtros")}
                </Button>
                {(criteria.logType || criteria.entityType || criteria.userId || criteria.searchText) && (
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={clearFilters}
                  >
                    <X className="h-4 w-4 mr-2" />
                    {t("governance.compliance.immutableLogs.clearFilters", "Limpiar")}
                  </Button>
                )}
              </div>
            </div>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {/* Búsqueda de texto libre */}
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("governance.compliance.immutableLogs.search.placeholder", "Buscar en logs...")}
                  value={criteria.searchText || ""}
                  onChange={(e) => updateCriteria({ searchText: e.target.value })}
                  className="pl-10"
                />
              </div>

              {/* Filtros avanzados */}
              {showFilters && (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 pt-4 border-t">
                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.logType", "Tipo de Log")}</Label>
                    <select
                      value={criteria.logType || ""}
                      onChange={(e) => updateCriteria({ logType: e.target.value || undefined })}
                      className="w-full px-3 py-2 border rounded-md"
                    >
                      <option value="">{t("governance.compliance.immutableLogs.allTypes", "Todos")}</option>
                      {LOG_TYPES.map(type => (
                        <option key={type} value={type}>{type}</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.entityType", "Tipo de Entidad")}</Label>
                    <select
                      value={criteria.entityType || ""}
                      onChange={(e) => updateCriteria({ entityType: e.target.value || undefined })}
                      className="w-full px-3 py-2 border rounded-md"
                    >
                      <option value="">{t("governance.compliance.immutableLogs.allTypes", "Todos")}</option>
                      {ENTITY_TYPES.map(type => (
                        <option key={type} value={type}>{type}</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.entityId", "ID de Entidad")}</Label>
                    <Input
                      type="number"
                      value={criteria.entityId || ""}
                      onChange={(e) => updateCriteria({ entityId: e.target.value || undefined })}
                      placeholder="123"
                    />
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.userId", "Usuario")}</Label>
                    <Input
                      value={criteria.userId || ""}
                      onChange={(e) => updateCriteria({ userId: e.target.value || undefined })}
                      placeholder="user@example.com"
                    />
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.startDate", "Fecha Inicio")}</Label>
                    <Input
                      type="date"
                      value={criteria.startDate || ""}
                      onChange={(e) => updateCriteria({ startDate: e.target.value || undefined })}
                    />
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.endDate", "Fecha Fin")}</Label>
                    <Input
                      type="date"
                      value={criteria.endDate || ""}
                      onChange={(e) => updateCriteria({ endDate: e.target.value || undefined })}
                    />
                  </div>

                  <div>
                    <Label>{t("governance.compliance.immutableLogs.filters.hash", "Hash")}</Label>
                    <Input
                      value={criteria.hash || ""}
                      onChange={(e) => updateCriteria({ hash: e.target.value || undefined })}
                      placeholder="a1b2c3d4..."
                    />
                  </div>
                </div>
              )}

              {/* Ordenamiento */}
              <div className="flex items-center gap-4 pt-4 border-t">
                <Label>{t("governance.compliance.immutableLogs.sortBy", "Ordenar por")}</Label>
                <select
                  value={criteria.sortBy || "timestamp"}
                  onChange={(e) => updateCriteria({ sortBy: e.target.value as any })}
                  className="px-3 py-2 border rounded-md"
                >
                  <option value="timestamp">{t("governance.compliance.immutableLogs.sortByTimestamp", "Fecha")}</option>
                  <option value="logType">{t("governance.compliance.immutableLogs.sortByLogType", "Tipo de Log")}</option>
                  <option value="entityType">{t("governance.compliance.immutableLogs.sortByEntityType", "Tipo de Entidad")}</option>
                </select>
                <select
                  value={criteria.sortOrder || "desc"}
                  onChange={(e) => updateCriteria({ sortOrder: e.target.value as any })}
                  className="px-3 py-2 border rounded-md"
                >
                  <option value="desc">{t("governance.compliance.immutableLogs.sortDesc", "Descendente")}</option>
                  <option value="asc">{t("governance.compliance.immutableLogs.sortAsc", "Ascendente")}</option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Resultado de Verificación de Integridad */}
        {integrityResult && (
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                {getIntegrityStatusIcon(integrityResult.status)}
                {t("governance.compliance.immutableLogs.integrity.title", "Verificación de Integridad")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div>
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.immutableLogs.integrity.score", "Score de Integridad")}
                  </div>
                  <div className={`text-2xl font-bold ${getIntegrityStatusColor(integrityResult.status)}`}>
                    {(integrityResult.integrityScore * 100).toFixed(2)}%
                  </div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.immutableLogs.integrity.verifiedLogs", "Logs Verificados")}
                  </div>
                  <div className="text-2xl font-bold">
                    {integrityResult.verifiedLogs} / {integrityResult.totalLogs}
                  </div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.immutableLogs.integrity.statusLabel", "Estado")}
                  </div>
                  <div className="text-lg font-semibold">
                    {t(`governance.compliance.immutableLogs.integrity.status.${integrityResult.status}`, integrityResult.status)}
                  </div>
                </div>
                <div>
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.immutableLogs.integrity.range", "Rango")}
                  </div>
                  <div className="text-lg">
                    {integrityResult.startId} - {integrityResult.endId}
                  </div>
                </div>
              </div>
              {integrityResult.brokenChains && integrityResult.brokenChains.length > 0 && (
                <div className="mt-4 p-4 bg-red-50 rounded-lg">
                  <div className="text-sm font-semibold text-red-800 mb-2">
                    {t("governance.compliance.immutableLogs.integrity.brokenChains", "Cadenas Rotas")}
                  </div>
                  <ul className="list-disc list-inside space-y-1">
                    {integrityResult.brokenChains.map((broken, idx) => (
                      <li key={idx} className="text-sm text-red-700">
                        Log ID {broken.logId}: Hash esperado {broken.expectedHash.substring(0, 16)}...
                        vs Hash actual {broken.actualHash.substring(0, 16)}...
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </CardBody>
          </Card>
        )}

        {/* Acciones */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              onClick={handleVerifyIntegrity}
              disabled={!searchResults || searchResults.logs.length === 0 || verifying}
            >
              <CheckCircle className="h-4 w-4 mr-2" />
              {verifying
                ? t("governance.compliance.immutableLogs.verifying", "Verificando...")
                : t("governance.compliance.immutableLogs.verifyIntegrity", "Verificar Integridad")
              }
            </Button>
          </div>
          <div className="flex items-center gap-2">
            <Button variant="outline" onClick={handleExportCSV}>
              <FileText className="h-4 w-4 mr-2" />
              {t("governance.compliance.immutableLogs.exportCSV", "Exportar CSV")}
            </Button>
            <Button variant="outline" onClick={handleExportJSON}>
              <FileJson className="h-4 w-4 mr-2" />
              {t("governance.compliance.immutableLogs.exportJSON", "Exportar JSON")}
            </Button>
            <Button variant="outline" onClick={handleExportHashChain}>
              <LinkIcon className="h-4 w-4 mr-2" />
              {t("governance.compliance.immutableLogs.exportHashChain", "Exportar Hash Chain")}
            </Button>
          </div>
        </div>

        {/* Tabla de Logs */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.immutableLogs.results", "Resultados")}
              {searchResults && ` (${searchResults.total})`}
            </CardTitle>
          </CardHeader>
          <CardBody>
            {loading ? (
              <div className="flex items-center justify-center py-12">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
              </div>
            ) : searchResults && searchResults.logs.length > 0 ? (
              <>
                <div className="overflow-x-auto">
                  <table className="w-full border-collapse">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.id", "ID")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.type", "Tipo")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.entity", "Entidad")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.user", "Usuario")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.timestamp", "Fecha")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.hash", "Hash")}</th>
                        <th className="text-left p-2">{t("governance.compliance.immutableLogs.table.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {searchResults.logs.map((log) => (
                        <tr key={log.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{log.id}</td>
                          <td className="p-2">
                            <Badge variant="outline">{log.logType}</Badge>
                          </td>
                          <td className="p-2">
                            <div>
                              <div className="font-medium">{log.entityType}</div>
                              <div className="text-sm text-muted-foreground">
                                #{log.entityId} {log.entityName && `- ${log.entityName}`}
                              </div>
                            </div>
                          </td>
                          <td className="p-2">{log.userId}</td>
                          <td className="p-2">
                            {new Date(log.timestamp).toLocaleString()}
                          </td>
                          <td className="p-2">
                            <div className="font-mono text-xs max-w-xs truncate" title={log.hash}>
                              {log.hash.substring(0, 16)}...
                            </div>
                          </td>
                          <td className="p-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={(e) => {
                                e.preventDefault();
                                e.stopPropagation();
                                window.location.href = `/governance/compliance/immutable-logs/${log.id}`;
                              }}
                            >
                              <Eye className="h-4 w-4 mr-1" />
                              {t("governance.compliance.immutableLogs.view", "Ver")}
                            </Button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                {/* Paginación */}
                {searchResults.totalPages > 1 && (
                  <div className="flex items-center justify-between mt-4 pt-4 border-t">
                    <div className="text-sm text-muted-foreground">
                      {t("governance.compliance.immutableLogs.showing", "Mostrando")} {(searchResults.page - 1) * searchResults.pageSize + 1} - {Math.min(searchResults.page * searchResults.pageSize, searchResults.total)} {t("governance.compliance.immutableLogs.of", "de")} {searchResults.total}
                    </div>
                    <div className="flex items-center gap-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => updateCriteria({ page: (criteria.page || 1) - 1 })}
                        disabled={searchResults.page === 1}
                      >
                        <ChevronLeft className="h-4 w-4" />
                      </Button>
                      <span className="text-sm">
                        {t("governance.compliance.immutableLogs.page", "Página")} {searchResults.page} / {searchResults.totalPages}
                      </span>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => updateCriteria({ page: (criteria.page || 1) + 1 })}
                        disabled={searchResults.page >= searchResults.totalPages}
                      >
                        <ChevronRight className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                )}
              </>
            ) : (
              <div className="text-center py-12 text-muted-foreground">
                {t("governance.compliance.immutableLogs.noResults", "No se encontraron logs")}
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
