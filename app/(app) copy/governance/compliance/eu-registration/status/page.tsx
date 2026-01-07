"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Database,
  CheckCircle,
  Clock,
  XCircle,
  Eye,
  RefreshCw,
  Download,
  FileText,
  Filter,
  Search,
} from "lucide-react";
import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

interface EuRegistrationStatus {
  id: number;
  projectId: number;
  projectName: string;
  registrationType: "STANDARD" | "SENSITIVE" | "NATIONAL";
  status: "DRAFT" | "PENDING" | "SUBMITTED" | "REGISTERED" | "REJECTED";
  submittedAt: string | null;
  euRegistrationId: string | null;
  response: {
    success: boolean;
    registrationId?: string;
    registeredAt?: string;
    error?: string;
  } | null;
}

const mockStatusData: EuRegistrationStatus[] = [
  {
    id: 1,
    projectId: 1001,
    projectName: "AI Credit Scoring System",
    registrationType: "STANDARD",
    status: "REGISTERED",
    submittedAt: "2025-12-01T10:00:00Z",
    euRegistrationId: "EU-REG-2025-001234",
    response: {
      success: true,
      registrationId: "EU-REG-2025-001234",
      registeredAt: "2025-12-01T10:05:00Z",
    },
  },
  {
    id: 2,
    projectId: 1002,
    projectName: "Facial Recognition System",
    registrationType: "SENSITIVE",
    status: "PENDING",
    submittedAt: null,
    euRegistrationId: null,
    response: null,
  },
  {
    id: 3,
    projectId: 1003,
    projectName: "HR Recruitment AI",
    registrationType: "STANDARD",
    status: "SUBMITTED",
    submittedAt: "2025-12-05T14:30:00Z",
    euRegistrationId: null,
    response: null,
  },
  {
    id: 4,
    projectId: 1004,
    projectName: "Medical Diagnosis Assistant",
    registrationType: "SENSITIVE",
    status: "REJECTED",
    submittedAt: "2025-11-20T09:15:00Z",
    euRegistrationId: null,
    response: {
      success: false,
      error: "Missing required documentation",
    },
  },
];

export default function EuRegistrationStatusPage() {
  const { t } = useTranslation();
  const [registrations, setRegistrations] = useState<EuRegistrationStatus[]>([]);
  const [loading, setLoading] = useState(true);
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [filterType, setFilterType] = useState<string>("ALL");
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/compliance/eu-registration/status");
      if (response.ok) {
        const result = await response.json();
        if (result.success) {
          setRegistrations(result.data || []);
        }
      } else {
        // Fallback to mock data if API fails
        setRegistrations(mockStatusData);
      }
    } catch (error) {
      console.error("Error loading EU registration status:", error);
      // Fallback to mock data on error
      setRegistrations(mockStatusData);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusKey = `governance.compliance.euRegistration.statusLabels.${status}`;
    switch (status) {
      case "REGISTERED":
        return (
          <Badge className="bg-green-500 text-white">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t(statusKey, status)}
          </Badge>
        );
      case "SUBMITTED":
        return (
          <Badge className="bg-blue-500 text-white">
            <Clock className="h-3 w-3 mr-1" />
            {t(statusKey, status)}
          </Badge>
        );
      case "REJECTED":
        return (
          <Badge variant="danger">
            <XCircle className="h-3 w-3 mr-1" />
            {t(statusKey, status)}
          </Badge>
        );
      case "PENDING":
        return (
          <Badge className="bg-yellow-500 text-white">
            <Clock className="h-3 w-3 mr-1" />
            {t(statusKey, status)}
          </Badge>
        );
      default:
        return (
          <Badge variant="outline">
            {t(statusKey, "DRAFT")}
          </Badge>
        );
    }
  };

  const filteredRegistrations = registrations.filter((reg) => {
    const matchesStatus = filterStatus === "ALL" || reg.status === filterStatus;
    const matchesType = filterType === "ALL" || reg.registrationType === filterType;
    const matchesSearch =
      searchQuery === "" ||
      reg.projectName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (reg.euRegistrationId && reg.euRegistrationId.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesStatus && matchesType && matchesSearch;
  });

  const handleResubmit = async (id: number) => {
    try {
      const response = await fetch(`/api/compliance/eu-registration/${id}/resubmit`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        const result = await response.json();
        if (result.success) {
          await loadData(); // Reload data after successful resubmission
        }
      }
    } catch (error) {
      console.error("Error resubmitting registration:", error);
    }
  };

  const handleDownloadJSON = (registration: EuRegistrationStatus) => {
    const json = JSON.stringify(registration, null, 2);
    const blob = new Blob([json], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `eu-registration-${registration.id}.json`;
    a.click();
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-blue-500/20 rounded-full animate-pulse delay-300" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="text-center space-y-2">
            <div className="flex items-center justify-center gap-3 mb-4">
              <Database className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                {t("governance.compliance.euRegistration.statusPage.title", "EU Registration Status")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.compliance.euRegistration.statusPage.subtitle", "Monitor and manage EU Database registrations")}
            </p>
          </div>
          <Button onClick={loadData} variant="outline">
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("governance.compliance.euRegistration.statusPage.refresh", "Refresh")}
          </Button>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="h-5 w-5" />
              {t("governance.compliance.euRegistration.statusPage.filters", "Filters")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div>
                  <Label>{t("governance.compliance.euRegistration.statusPage.search", "Search")}</Label>
                  <div className="relative">
                    <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                    <Input
                      placeholder={t("governance.compliance.euRegistration.statusPage.searchPlaceholder", "Search by project or ID...")}
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="pl-8"
                  />
                </div>
              </div>
              <div>
                <Label>{t("governance.compliance.euRegistration.statusPage.filterByStatus", "Status")}</Label>
                <select
                  className="w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value)}
                >
                  <option value="ALL">{t("governance.compliance.euRegistration.statusPage.all", "All")}</option>
                  <option value="DRAFT">{t("governance.compliance.euRegistration.statusLabels.DRAFT", "Draft")}</option>
                  <option value="PENDING">{t("governance.compliance.euRegistration.statusLabels.PENDING", "Pending")}</option>
                  <option value="SUBMITTED">{t("governance.compliance.euRegistration.statusLabels.SUBMITTED", "Submitted")}</option>
                  <option value="REGISTERED">{t("governance.compliance.euRegistration.statusLabels.REGISTERED", "Registered")}</option>
                  <option value="REJECTED">{t("governance.compliance.euRegistration.statusLabels.REJECTED", "Rejected")}</option>
                </select>
              </div>
              <div>
                <Label>{t("governance.compliance.euRegistration.statusPage.filterByType", "Type")}</Label>
                <select
                  className="w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  value={filterType}
                  onChange={(e) => setFilterType(e.target.value)}
                >
                  <option value="ALL">{t("governance.compliance.euRegistration.statusPage.all", "All")}</option>
                  <option value="STANDARD">{t("governance.compliance.euRegistration.statusPage.standard", "Standard")}</option>
                  <option value="SENSITIVE">{t("governance.compliance.euRegistration.statusPage.sensitive", "Sensitive")}</option>
                  <option value="NATIONAL">{t("governance.compliance.euRegistration.statusPage.national", "National")}</option>
                </select>
              </div>
              <div className="flex items-end">
                <Button
                  variant="outline"
                  onClick={() => {
                    setFilterStatus("ALL");
                    setFilterType("ALL");
                    setSearchQuery("");
                  }}
                  className="w-full"
                >
                  {t("governance.compliance.euRegistration.statusPage.clearFilters", "Clear Filters")}
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Registrations Table */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.euRegistration.statusPage.registrations", "Registrations")} ({filteredRegistrations.length})
            </CardTitle>
          </CardHeader>
          <CardBody>
            {filteredRegistrations.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                {t("governance.compliance.euRegistration.statusPage.noRegistrations", "No registrations found")}
              </div>
            ) : (
              <div className="overflow-x-auto">
                <Table responsive hover>
                  <TableHeader>
                    <TableRow>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.project", "Project")}</TableCell>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.type", "Type")}</TableCell>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.status", "Status")}</TableCell>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.submittedAt", "Submitted At")}</TableCell>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.euRegistrationId", "EU Registration ID")}</TableCell>
                      <TableCell header scope="col">{t("governance.compliance.euRegistration.statusPage.actions", "Actions")}</TableCell>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredRegistrations.map((registration) => (
                      <TableRow key={registration.id}>
                        <TableCell><span className="font-medium">{registration.projectName}</span></TableCell>
                        <TableCell>
                          <Badge variant="outline">{registration.registrationType}</Badge>
                        </TableCell>
                        <TableCell>{getStatusBadge(registration.status)}</TableCell>
                        <TableCell>
                          {registration.submittedAt
                            ? new Date(registration.submittedAt).toLocaleDateString()
                            : "-"}
                        </TableCell>
                        <TableCell>
                          {registration.euRegistrationId ? (
                            <span className="font-mono text-sm">{registration.euRegistrationId}</span>
                          ) : (
                            "-"
                          )}
                        </TableCell>
                        <TableCell>
                          <div className="flex gap-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDownloadJSON(registration)}
                              title={t("governance.compliance.euRegistration.statusPage.viewDetails", "View Details")}
                            >
                              <Eye className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDownloadJSON(registration)}
                              title={t("governance.compliance.euRegistration.statusPage.downloadJSON", "Download JSON")}
                            >
                              <Download className="h-4 w-4" />
                            </Button>
                            {registration.status === "REJECTED" && (
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => handleResubmit(registration.id)}
                                title={t("governance.compliance.euRegistration.statusPage.resubmit", "Resubmit")}
                              >
                                <RefreshCw className="h-4 w-4" />
                              </Button>
                            )}
                          </div>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
