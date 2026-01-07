"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  FileText,
  Eye,
  Edit,
  Send,
  Download,
  Search,
  Filter,
  Plus,
} from "lucide-react";
import { useEffect, useState } from "react";
import Link from "next/link";

interface FriaAssessment {
  id: number;
  projectId: number;
  projectName: string;
  createdAt: string;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED";
  completenessScore: number;
  finalRisk: number | null;
  riskLevel: "low" | "medium" | "high" | "critical" | null;
}

const mockAssessments: FriaAssessment[] = [
  {
    id: 1,
    projectId: 1001,
    projectName: "AI Credit Scoring System",
    createdAt: "2025-12-01T10:00:00Z",
    status: "COMPLETED",
    completenessScore: 0.95,
    finalRisk: 0.42,
    riskLevel: "medium",
  },
  {
    id: 2,
    projectId: 1002,
    projectName: "Facial Recognition System",
    createdAt: "2025-12-02T14:30:00Z",
    status: "DRAFT",
    completenessScore: 0.60,
    finalRisk: null,
    riskLevel: null,
  },
  {
    id: 3,
    projectId: 1003,
    projectName: "Automated Hiring System",
    createdAt: "2025-12-03T09:15:00Z",
    status: "NOTIFIED",
    completenessScore: 1.0,
    finalRisk: 0.85,
    riskLevel: "high",
  },
];

export default function FriaAssessmentsPage() {
  const { t } = useTranslation();
  const [assessments, setAssessments] = useState<FriaAssessment[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");

  useEffect(() => {
    loadAssessments();
  }, []);

  const loadAssessments = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada real a API
      await new Promise((resolve) => setTimeout(resolve, 500));
      setAssessments(mockAssessments);
    } catch (error) {
      console.error("Error loading assessments:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredAssessments = assessments.filter((assessment) => {
    const matchesSearch =
      assessment.projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      assessment.projectId.toString().includes(searchTerm);
    const matchesStatus =
      statusFilter === "all" || assessment.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const getStatusBadge = (status: string) => {
    const variants: Record<string, "primary" | "secondary" | "danger"> = {
      DRAFT: "secondary",
      COMPLETED: "primary",
      NOTIFIED: "danger",
    };
    return (
      <Badge variant={variants[status] || "primary"}>
        {t(`governance.compliance.fria.status.${status.toLowerCase()}`)}
      </Badge>
    );
  };

  const getRiskBadge = (risk: number | null, level: string | null) => {
    if (risk === null) return <span className="text-muted-foreground">-</span>;
    const variants: Record<string, "primary" | "secondary" | "danger"> =
      {
        low: "primary",
        medium: "primary",
        high: "danger",
        critical: "danger",
      };
    return (
      <Badge variant={variants[level || "low"] || "primary"}>
        {(risk * 100).toFixed(0)}% -{" "}
        {level && t(`governance.compliance.fria.riskLevels.${level}`)}
      </Badge>
    );
  };

  const notifyAuthority = async (friaId: number) => {
    try {
      const response = await fetch(
        `/api/compliance/fria/${friaId}/notify-authority`,
        {
          method: "POST",
        }
      );
      const result = await response.json();
      if (result.success) {
        loadAssessments();
      }
    } catch (error) {
      console.error("Error notifying authority:", error);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <FileText className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                {t("governance.compliance.fria.assessments")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.compliance.fria.assessmentsList")}
            </p>
          </div>
          <Link href="/governance/compliance/fria">
            <Button>
              <Plus className="h-4 w-4 mr-2" />
              {t("governance.compliance.fria.newAssessment")}
            </Button>
          </Link>
        </div>

        {/* Filters */}
        <Card>
          <CardBody>
            <div className="flex gap-4">
              <div className="flex-1">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder={t("governance.compliance.fria.searchByProject")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              <div className="w-48">
                <select
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                  className="w-full px-3 py-2 border rounded-md"
                >
                  <option value="all">{t("governance.compliance.fria.allStatuses")}</option>
                  <option value="DRAFT">{t("governance.compliance.fria.status.draft")}</option>
                  <option value="COMPLETED">{t("governance.compliance.fria.status.completed")}</option>
                  <option value="NOTIFIED">{t("governance.compliance.fria.status.notified")}</option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Table */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.fria.assessmentsCount", undefined, { count: String(filteredAssessments.length) })}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <Table responsive>
              <TableHeader>
                <TableRow>
                  <TableCell header scope="col">{t("governance.compliance.fria.project")}</TableCell>
                  <TableCell header scope="col">{t("governance.compliance.fria.creationDate")}</TableCell>
                  <TableCell header scope="col">{t("governance.compliance.fria.state")}</TableCell>
                  <TableCell header scope="col">{t("governance.compliance.fria.completeness")}</TableCell>
                  <TableCell header scope="col">{t("governance.compliance.fria.finalRisk")}</TableCell>
                  <TableCell header scope="col">{t("governance.compliance.fria.actions")}</TableCell>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredAssessments.length === 0 ? (
                  <TableRow>
                    <TableCell>
                      <div className="text-center py-8">
                        <p className="text-muted-foreground">
                          {t("governance.compliance.fria.noAssessmentsFound")}
                        </p>
                      </div>
                    </TableCell>
                  </TableRow>
                ) : (
                  filteredAssessments.map((assessment) => (
                    <TableRow key={assessment.id}>
                      <TableCell className="font-medium">
                        {assessment.projectName}
                      </TableCell>
                      <TableCell>
                        {new Date(assessment.createdAt).toLocaleDateString()}
                      </TableCell>
                      <TableCell>{getStatusBadge(assessment.status)}</TableCell>
                      <TableCell>
                        {(assessment.completenessScore * 100).toFixed(0)}%
                      </TableCell>
                      <TableCell>
                        {getRiskBadge(assessment.finalRisk, assessment.riskLevel)}
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Link href={`/governance/compliance/fria/${assessment.id}`}>
                            <Button variant="ghost" size="sm">
                              <Eye className="h-4 w-4" />
                            </Button>
                          </Link>
                          {assessment.status === "DRAFT" && (
                            <Link href={`/governance/compliance/fria?friaId=${assessment.id}`}>
                              <Button variant="ghost" size="sm">
                                <Edit className="h-4 w-4" />
                              </Button>
                            </Link>
                          )}
                          {assessment.status === "COMPLETED" &&
                            assessment.finalRisk !== null &&
                            assessment.finalRisk >= 0.75 && (
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => notifyAuthority(assessment.id)}
                              >
                                <Send className="h-4 w-4" />
                              </Button>
                            )}
                          <Button variant="ghost" size="sm">
                            <Download className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
