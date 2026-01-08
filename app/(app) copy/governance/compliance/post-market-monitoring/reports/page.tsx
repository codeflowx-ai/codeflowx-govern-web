"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  FileText,
  Download,
  Plus,
  CheckCircle,
  Clock,
  Archive,
  Filter,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { mockProjects } from "@/app/(app)/governance/data/mockPMM";

interface SurveillanceReport {
  idxpmsreport: number;
  iduuid: string;
  idxproject: number;
  projectName?: string;
  idxmodel?: number;
  modelName?: string;
  pmsreporttype: string;
  pmsreportdate: string;
  pmsstatus: string;
  pmscreatedat: string;
  pmsupdatedat?: string;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true" || true; // Por defecto usar mock para demo

export default function PMMReportsPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [reports, setReports] = useState<SurveillanceReport[]>([]);
  const [loading, setLoading] = useState(true);
  const [projectFilter, setProjectFilter] = useState<string>(projectIdFromUrl || "");
  const [typeFilter, setTypeFilter] = useState<string>("");
  const [availableProjects, setAvailableProjects] = useState<Array<{ id: number; name: string }>>([]);

  // Mock data para demo (definido fuera del componente para persistencia)
  const [mockReportsState, setMockReportsState] = useState<SurveillanceReport[]>([
    {
      idxpmsreport: 1,
      iduuid: "550e8400-e29b-41d4-a716-446655440001",
      idxproject: 1,
      projectName: "AI Credit Scoring System",
      pmsreporttype: "WEEKLY",
      pmsreportdate: new Date().toISOString(),
      pmsstatus: "GENERATED",
      pmscreatedat: new Date().toISOString(),
    },
    {
      idxpmsreport: 2,
      iduuid: "550e8400-e29b-41d4-a716-446655440002",
      idxproject: 2,
      projectName: "Facial Recognition System",
      pmsreporttype: "MONTHLY",
      pmsreportdate: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
      pmsstatus: "APPROVED",
      pmscreatedat: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
    },
    {
      idxpmsreport: 3,
      iduuid: "550e8400-e29b-41d4-a716-446655440003",
      idxproject: 3,
      projectName: "Healthcare Diagnostics AI System",
      pmsreporttype: "DAILY",
      pmsreportdate: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
      pmsstatus: "DRAFT",
      pmscreatedat: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
    },
  ]);

  useEffect(() => {
    if (projectIdFromUrl) {
      setProjectFilter(projectIdFromUrl);
    }
    loadReports();
    loadProjects();
  }, [projectFilter, typeFilter, projectIdFromUrl]);

  const loadReports = async () => {
    try {
      setLoading(true);

      if (USE_MOCK_DATA) {
        // Usar datos mock para demo
        await new Promise((resolve) => setTimeout(resolve, 500));
        let filteredReports = [...mockReportsState];

        if (projectFilter) {
          filteredReports = filteredReports.filter(r => r.idxproject.toString() === projectFilter);
        }
        if (typeFilter) {
          filteredReports = filteredReports.filter(r => r.pmsreporttype === typeFilter);
        }

        setReports(filteredReports);
        return;
      }

      // Llamada real a API
      const params = new URLSearchParams();
      if (projectFilter) params.append("projectId", projectFilter);
      if (typeFilter) params.append("reportType", typeFilter);

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/reports?${params.toString()}`);
      if (response.ok) {
        const data = await response.json();
        setReports(data);
      }
    } catch (error) {
      console.error("Error loading reports:", error);
      // Fallback a mock data en caso de error
      if (USE_MOCK_DATA) {
        setReports(mockReportsState);
      }
    } finally {
      setLoading(false);
    }
  };

  const loadProjects = async () => {
    try {
      if (USE_MOCK_DATA) {
        // Usar proyectos mock sincronizados
        setAvailableProjects(mockProjects);
        return;
      }
      // Llamada real a API
      const response = await fetch(`${API_BASE_URL}/api/v1/projects`);
      if (response.ok) {
        const data = await response.json();
        setAvailableProjects(data);
      }
    } catch (error) {
      console.error("Error loading projects:", error);
      // Fallback a mock data
      if (USE_MOCK_DATA) {
        setAvailableProjects(mockProjects);
      }
    }
  };

  const handleGenerateReport = async (projectId: number, reportType: string) => {
    try {
      if (USE_MOCK_DATA) {
        // Simular generación con mock data
        await new Promise((resolve) => setTimeout(resolve, 1000));
        const newReport: SurveillanceReport = {
          idxpmsreport: mockReportsState.length + 1,
          iduuid: `550e8400-e29b-41d4-a716-44665544001${mockReportsState.length + 1}`,
          idxproject: projectId,
          projectName: availableProjects.find(p => p.id === projectId)?.name,
          pmsreporttype: reportType,
          pmsreportdate: new Date().toISOString(),
          pmsstatus: "GENERATED",
          pmscreatedat: new Date().toISOString(),
        };
        setMockReportsState([newReport, ...mockReportsState]);
        loadReports();
        return;
      }

      // Llamada real a API
      const response = await fetch(
        `${API_BASE_URL}/api/v1/pmm/reports/generate?projectId=${projectId}&reportType=${reportType}`,
        { method: "POST" }
      );

      if (response.ok) {
        loadReports();
      }
    } catch (error) {
      console.error("Error generating report:", error);
    }
  };

  const handleDownloadPdf = async (reportId: number) => {
    try {
      if (USE_MOCK_DATA) {
        // Simular descarga de PDF
        alert(t("governance.compliance.pmm.reports.pdfDownloaded", "PDF descargado (simulado para demo)"));
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/reports/${reportId}/pdf`);
      if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `report-${reportId}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      }
    } catch (error) {
      console.error("Error downloading PDF:", error);
    }
  };

  const handleApproveReport = async (reportId: number) => {
    try {
      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        const report = reports.find(r => r.idxpmsreport === reportId);
        if (report) {
          report.pmsstatus = "APPROVED";
          setReports([...reports]);
        }
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/pmm/reports/${reportId}/approve`, {
        method: "POST",
      });

      if (response.ok) {
        loadReports();
      }
    } catch (error) {
      console.error("Error approving report:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<string, { label: string; variant: "primary" | "secondary" | "danger" | "outline" }> = {
      DRAFT: { label: t("governance.compliance.pmm.reports.status.DRAFT", "Borrador"), variant: "outline" },
      GENERATED: { label: t("governance.compliance.pmm.reports.status.GENERATED", "Generado"), variant: "primary" },
      APPROVED: { label: t("governance.compliance.pmm.reports.status.APPROVED", "Aprobado"), variant: "primary" },
      ARCHIVED: { label: t("governance.compliance.pmm.reports.status.ARCHIVED", "Archivado"), variant: "outline" },
    };

    const statusInfo = statusMap[status] || { label: status, variant: "outline" as const };
    return <Badge variant={statusInfo.variant}>{statusInfo.label}</Badge>;
  };

  const getTypeLabel = (type: string) => {
    const typeMap: Record<string, string> = {
      DAILY: t("governance.compliance.pmm.reports.type.DAILY", "Diario"),
      WEEKLY: t("governance.compliance.pmm.reports.type.WEEKLY", "Semanal"),
      MONTHLY: t("governance.compliance.pmm.reports.type.MONTHLY", "Mensual"),
      AD_HOC: t("governance.compliance.pmm.reports.type.AD_HOC", "Ad-hoc"),
    };
    return typeMap[type] || type;
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <FileText className="w-8 h-8 text-orange-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-orange-500 to-orange-700 bg-clip-text text-transparent">
              {t("governance.compliance.pmm.reports.title", "Reportes de Vigilancia Post-Mercado")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.pmm.reports.subtitle",
              "Generación y gestión de reportes de vigilancia según EU AI Act Art. 72"
            )}
          </p>
        </div>

        {/* Actions and Filters */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              onClick={() => {
                const projectId = projectFilter || availableProjects[0]?.id;
                if (projectId) {
                  handleGenerateReport(parseInt(String(projectId)), "WEEKLY");
                }
              }}
            >
              <Plus className="w-4 h-4 mr-2" />
              {t("governance.compliance.pmm.reports.generateWeekly", "Generar Reporte Semanal")}
            </Button>
            <Button
              variant="outline"
              onClick={() => {
                const projectId = projectFilter || availableProjects[0]?.id;
                if (projectId) {
                  handleGenerateReport(parseInt(String(projectId)), "MONTHLY");
                }
              }}
            >
              <Plus className="w-4 h-4 mr-2" />
              {t("governance.compliance.pmm.reports.generateMonthly", "Generar Reporte Mensual")}
            </Button>
          </div>

          {/* Filters */}
          <div className="flex items-center gap-4">
            {projectIdFromUrl ? (
              <div className="px-3 py-2 border rounded-md bg-muted text-muted-foreground">
                {availableProjects.find((p) => p.id.toString() === projectIdFromUrl)?.name || `Proyecto ${projectIdFromUrl}`}
              </div>
            ) : (
              <select
                value={projectFilter}
                onChange={(e) => setProjectFilter(e.target.value)}
                className="px-3 py-2 border rounded-md bg-background"
              >
                <option value="">{t("common.allProjects", "Todos los proyectos")}</option>
                {availableProjects.map((project) => (
                  <option key={project.id} value={project.id.toString()}>
                    {project.name}
                  </option>
                ))}
              </select>
            )}

            <select
              value={typeFilter}
              onChange={(e) => setTypeFilter(e.target.value)}
              className="px-3 py-2 border rounded-md bg-background"
            >
              <option value="">{t("common.allTypes", "Todos los tipos")}</option>
              <option value="DAILY">{t("governance.compliance.pmm.reports.type.DAILY", "Diario")}</option>
              <option value="WEEKLY">{t("governance.compliance.pmm.reports.type.WEEKLY", "Semanal")}</option>
              <option value="MONTHLY">{t("governance.compliance.pmm.reports.type.MONTHLY", "Mensual")}</option>
              <option value="AD_HOC">{t("governance.compliance.pmm.reports.type.AD_HOC", "Ad-hoc")}</option>
            </select>
          </div>
        </div>

        {/* Reports List */}
        {loading ? (
          <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
        ) : (
          <div className="grid gap-4">
            {reports.map((report) => (
              <Card key={report.idxpmsreport} className="relative">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <CardTitle className="text-xl">
                        {getTypeLabel(report.pmsreporttype)} - {new Date(report.pmsreportdate).toLocaleDateString()}
                      </CardTitle>
                      {getStatusBadge(report.pmsstatus)}
                    </div>
                    <div className="flex items-center gap-2">
                      {report.pmsstatus === "GENERATED" && (
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleApproveReport(report.idxpmsreport)}
                        >
                          <CheckCircle className="w-4 h-4 mr-1" />
                          {t("governance.compliance.pmm.reports.approve", "Aprobar")}
                        </Button>
                      )}
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleDownloadPdf(report.idxpmsreport)}
                      >
                        <Download className="w-4 h-4 mr-1" />
                        {t("common.download", "Descargar PDF")}
                      </Button>
                    </div>
                  </div>
                </CardHeader>
                <CardBody>
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.reports.project", "Proyecto")}:
                      </span>
                      <p className="font-medium">{report.projectName || `#${report.idxproject}`}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.reports.type", "Tipo")}:
                      </span>
                      <p className="font-medium">{getTypeLabel(report.pmsreporttype)}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.reports.date", "Fecha")}:
                      </span>
                      <p className="font-medium">
                        {new Date(report.pmsreportdate).toLocaleDateString()}
                      </p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">
                        {t("governance.compliance.pmm.reports.createdAt", "Creado")}:
                      </span>
                      <p className="font-medium">
                        {new Date(report.pmscreatedat).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                </CardBody>
              </Card>
            ))}

            {reports.length === 0 && (
              <Card>
                <CardBody className="text-center py-8 text-muted-foreground">
                  {t("governance.compliance.pmm.reports.noReports", "No hay reportes disponibles")}
                </CardBody>
              </Card>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
