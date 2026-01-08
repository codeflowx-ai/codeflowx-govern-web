"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Globe,
  Plus,
  Edit,
  Eye,
  CheckCircle,
  Clock,
  XCircle,
  Search,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";

interface EuRegistration {
  registrationId: number;
  projectId: number;
  projectName: string;
  status: "DRAFT" | "PENDING" | "SUBMITTED" | "REGISTERED" | "REJECTED";
  registrationType: "STANDARD" | "SENSITIVE" | "NATIONAL";
  euRegistrationId?: string;
  submittedAt?: string;
  registeredAt?: string;
}

interface ProjectWithRegistrations {
  projectId: number;
  projectName: string;
  registrations: EuRegistration[];
  totalRegistrations: number;
  latestRegistration?: EuRegistration;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true" || true;

// Mock data
const mockProjects: ProjectWithRegistrations[] = [
  {
    projectId: 1001,
    projectName: "AI Credit Scoring System",
    totalRegistrations: 2,
    registrations: [
      {
        registrationId: 2001,
        projectId: 1001,
        projectName: "AI Credit Scoring System",
        status: "DRAFT",
        registrationType: "STANDARD",
      },
      {
        registrationId: 2002,
        projectId: 1001,
        projectName: "AI Credit Scoring System",
        status: "REGISTERED",
        registrationType: "STANDARD",
        euRegistrationId: "EU-2024-001234",
        registeredAt: "2024-01-15T10:30:00Z",
      },
    ],
    latestRegistration: {
      registrationId: 2002,
      projectId: 1001,
      projectName: "AI Credit Scoring System",
      status: "REGISTERED",
      registrationType: "STANDARD",
      euRegistrationId: "EU-2024-001234",
      registeredAt: "2024-01-15T10:30:00Z",
    },
  },
  {
    projectId: 1002,
    projectName: "Facial Recognition System",
    totalRegistrations: 1,
    registrations: [
      {
        registrationId: 2003,
        projectId: 1002,
        projectName: "Facial Recognition System",
        status: "SUBMITTED",
        registrationType: "SENSITIVE",
        submittedAt: "2024-01-20T14:20:00Z",
      },
    ],
    latestRegistration: {
      registrationId: 2003,
      projectId: 1002,
      projectName: "Facial Recognition System",
      status: "SUBMITTED",
      registrationType: "SENSITIVE",
      submittedAt: "2024-01-20T14:20:00Z",
    },
  },
  {
    projectId: 1003,
    projectName: "HR Recruitment AI",
    totalRegistrations: 0,
    registrations: [],
  },
  {
    projectId: 1004,
    projectName: "Medical Diagnosis Assistant",
    totalRegistrations: 1,
    registrations: [
      {
        registrationId: 2004,
        projectId: 1004,
        projectName: "Medical Diagnosis Assistant",
        status: "PENDING",
        registrationType: "STANDARD",
      },
    ],
    latestRegistration: {
      registrationId: 2004,
      projectId: 1004,
      projectName: "Medical Diagnosis Assistant",
      status: "PENDING",
      registrationType: "STANDARD",
    },
  },
];

export default function EuRegistrationPage() {
  const { t, language, mounted } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  const [projects, setProjects] = useState<ProjectWithRegistrations[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [typeFilter, setTypeFilter] = useState<string>("");

  useEffect(() => {
    loadProjects();
  }, [statusFilter, typeFilter]);

  // Forzar re-render cuando cambie el idioma
  useEffect(() => {
    // Este efecto asegura que el componente se re-renderice cuando cambia el idioma
  }, [language, mounted]);

  const loadProjects = async () => {
    try {
      setLoading(true);

      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 500));
        let filteredProjects = [...mockProjects];

        if (projectIdFromUrl) {
          filteredProjects = filteredProjects.filter(
            (p) => p.projectId.toString() === projectIdFromUrl
          );
        }

        setProjects(filteredProjects);
        return;
      }

      // Llamada real a API
      const params = new URLSearchParams();
      if (projectIdFromUrl) params.append("projectId", projectIdFromUrl);
      if (statusFilter) params.append("status", statusFilter);
      if (typeFilter) params.append("type", typeFilter);

      const response = await fetch(
        `${API_BASE_URL}/api/v1/compliance/eu-registration/status?${params.toString()}`
      );
      if (response.ok) {
        const registrations: EuRegistration[] = await response.json();
        // Agrupar por proyecto
        const projectsMap = new Map<number, ProjectWithRegistrations>();
        registrations.forEach((reg) => {
          if (!projectsMap.has(reg.projectId)) {
            projectsMap.set(reg.projectId, {
              projectId: reg.projectId,
              projectName: reg.projectName,
              registrations: [],
              totalRegistrations: 0,
            });
          }
          const project = projectsMap.get(reg.projectId)!;
          project.registrations.push(reg);
          project.totalRegistrations++;
          if (!project.latestRegistration || (reg.submittedAt && (!project.latestRegistration.submittedAt || reg.submittedAt > project.latestRegistration.submittedAt))) {
            project.latestRegistration = reg;
          }
        });
        setProjects(Array.from(projectsMap.values()));
      }
    } catch (error) {
      console.error("Error loading projects:", error);
      if (USE_MOCK_DATA) {
        setProjects(mockProjects);
      }
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

  const getTypeLabel = (type: string) => {
    return t(
      `governance.compliance.euRegistration.registrationTypeLabels.${type}`,
      type
    );
  };

  const filteredProjects = projects.filter((project) => {
    const matchesSearch =
      !searchQuery ||
      project.projectName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      project.registrations.some(
        (reg) =>
          reg.registrationId.toString().includes(searchQuery) ||
          (reg.euRegistrationId &&
            reg.euRegistrationId.toLowerCase().includes(searchQuery.toLowerCase()))
      );

    const matchesStatus =
      !statusFilter ||
      project.latestRegistration?.status === statusFilter ||
      project.registrations.some((reg) => reg.status === statusFilter);

    const matchesType =
      !typeFilter ||
      project.latestRegistration?.registrationType === typeFilter ||
      project.registrations.some((reg) => reg.registrationType === typeFilter);

    return matchesSearch && matchesStatus && matchesType;
  });

  if (loading || !mounted) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Globe className="w-8 h-8 text-blue-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
              {t("governance.compliance.euRegistration.title", "EU Database Registration")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "governance.compliance.euRegistration.subtitle",
              "Register your AI system in the EU Database according to Art. 49 and Annex VIII"
            )}
          </p>
        </div>

        {/* Actions and Filters */}
        <div className="flex items-center justify-end flex-wrap gap-4">
          <div className="flex items-center gap-4 flex-wrap flex-1">
            <div className="relative flex-1 max-w-md">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder={t("governance.compliance.euRegistration.statusPage.searchPlaceholder", "Search by project or ID...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 w-full"
              />
            </div>

            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-3 py-2 border rounded-md bg-background"
            >
              <option value="">
                {t("governance.compliance.euRegistration.statusPage.all", "All Statuses")}
              </option>
              <option value="DRAFT">
                {t("governance.compliance.euRegistration.statusLabels.DRAFT", "Draft")}
              </option>
              <option value="PENDING">
                {t("governance.compliance.euRegistration.statusLabels.PENDING", "Pending")}
              </option>
              <option value="SUBMITTED">
                {t("governance.compliance.euRegistration.statusLabels.SUBMITTED", "Submitted")}
              </option>
              <option value="REGISTERED">
                {t("governance.compliance.euRegistration.statusLabels.REGISTERED", "Registered")}
              </option>
              <option value="REJECTED">
                {t("governance.compliance.euRegistration.statusLabels.REJECTED", "Rejected")}
              </option>
            </select>

            <select
              value={typeFilter}
              onChange={(e) => setTypeFilter(e.target.value)}
              className="px-3 py-2 border rounded-md bg-background"
            >
              <option value="">
                {t("governance.compliance.euRegistration.statusPage.all", "All Types")}
              </option>
              <option value="STANDARD">
                {t("governance.compliance.euRegistration.registrationTypeLabels.STANDARD", "Standard")}
              </option>
              <option value="SENSITIVE">
                {t("governance.compliance.euRegistration.registrationTypeLabels.SENSITIVE", "Sensitive")}
              </option>
              <option value="NATIONAL">
                {t("governance.compliance.euRegistration.registrationTypeLabels.NATIONAL", "National")}
              </option>
            </select>
          </div>
        </div>

        {/* Projects List */}
        {filteredProjects.length === 0 ? (
          <Card>
            <CardBody className="text-center py-8 text-muted-foreground">
              {t("governance.compliance.euRegistration.noProjects", "No projects found")}
            </CardBody>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {filteredProjects.map((project) => (
              <Card key={project.projectId} className="relative">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <CardTitle className="text-xl">{project.projectName}</CardTitle>
                      <Badge variant="outline">
                        {project.totalRegistrations}{" "}
                        {project.totalRegistrations === 1
                          ? t("governance.compliance.euRegistration.registration", "registration")
                          : t("governance.compliance.euRegistration.registrations", "registrations")}
                      </Badge>
                      {project.latestRegistration && getStatusBadge(project.latestRegistration.status)}
                    </div>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        window.location.href = `/governance/compliance/eu-registration/new?projectId=${project.projectId}`;
                      }}
                    >
                      <Plus className="w-4 h-4 mr-2" />
                      {t("governance.compliance.euRegistration.newRegistration", "Nuevo Registro")}
                    </Button>
                  </div>
                </CardHeader>
                <CardBody>
                  {project.registrations.length === 0 ? (
                    <div className="text-center py-4 text-muted-foreground">
                      <p className="mb-4">
                        {t(
                          "governance.compliance.euRegistration.noRegistrationsForProject",
                          "No registrations for this project yet"
                        )}
                      </p>
                      <Button
                        variant="outline"
                        onClick={() => {
                          window.location.href = `/governance/compliance/eu-registration/new?projectId=${project.projectId}`;
                        }}
                      >
                        <Plus className="w-4 h-4 mr-2" />
                        {t("governance.compliance.euRegistration.createFirst", "Create First Registration")}
                      </Button>
                    </div>
                  ) : (
                    <div className="space-y-3">
                      {project.registrations.map((registration) => (
                        <div
                          key={registration.registrationId}
                          className="flex items-center justify-between p-4 border rounded-lg hover:bg-muted/50 transition-colors"
                        >
                          <div className="flex items-center gap-4 flex-1">
                            <div className="flex-1">
                              <div className="flex items-center gap-2 mb-1">
                                <span className="font-semibold">
                                  {t("governance.compliance.euRegistration.registrationId", "Registration ID")}:{" "}
                                  {registration.registrationId}
                                </span>
                                {getStatusBadge(registration.status)}
                                <Badge variant="outline">
                                  {getTypeLabel(registration.registrationType)}
                                </Badge>
                              </div>
                              {registration.euRegistrationId && (
                                <p className="text-sm text-muted-foreground">
                                  {t("governance.compliance.euRegistration.statusPage.euRegistrationId", "EU Registration ID")}:{" "}
                                  {registration.euRegistrationId}
                                </p>
                              )}
                              {registration.submittedAt && (
                                <p className="text-sm text-muted-foreground">
                                  {t("governance.compliance.euRegistration.statusPage.submittedAt", "Submitted At")}:{" "}
                                  {new Date(registration.submittedAt).toLocaleString()}
                                </p>
                              )}
                            </div>
                          </div>
                          <div className="flex items-center gap-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => {
                                window.location.href = `/governance/compliance/eu-registration/${registration.registrationId}`;
                              }}
                            >
                              <Edit className="w-4 h-4 mr-2" />
                              {t("governance.compliance.euRegistration.edit", "Edit")}
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => {
                                window.location.href = `/governance/compliance/eu-registration/${registration.registrationId}`;
                              }}
                            >
                              <Eye className="w-4 h-4 mr-2" />
                              {t("governance.compliance.euRegistration.view", "View")}
                            </Button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </CardBody>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
