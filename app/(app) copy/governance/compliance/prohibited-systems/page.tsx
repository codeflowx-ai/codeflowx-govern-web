"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { XCircle, AlertTriangle, Shield, CheckCircle, Search, Ban, RefreshCw, ExternalLink } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface ProhibitedSystem {
  id: number;
  systemName: string;
  systemType: string;
  prohibitionReason: string;
  article: string;
  detectedAt: string;
  status: "CLEAN" | "PROHIBITED" | "WARNING" | "BLOCKED" | "RESOLVED";
  projectId: number;
  projectName: string;
  confidence?: number;
  detectedSystems?: {
    id: number;
    name: string;
    category: string;
    detectedAt: string;
    confidence: number;
  }[];
  blocked?: boolean;
}

interface ProhibitedSystemsData {
  activeProhibited: number;
  blockedDeployments: number;
  totalDetections: number;
  systems: ProhibitedSystem[];
  catalog: {
    article: string;
    description: string;
    examples: string[];
  }[];
}

const mockSystems: ProhibitedSystem[] = [
  {
    id: 1,
    systemName: "AI Credit Scoring System",
    systemType: "AI System",
    prohibitionReason: "Social scoring for general purpose (Art. 5.1.c)",
    article: "Art. 5.1.c",
    detectedAt: "2025-12-01T10:30:00Z",
    status: "PROHIBITED",
    projectId: 5001,
    projectName: "Citizen Rating Project",
    confidence: 0.95,
    detectedSystems: [
      {
        id: 1,
        name: "Social Scoring by Public Authorities",
        category: "Art. 5.1.c",
        detectedAt: "2025-12-01T10:30:00Z",
        confidence: 0.95,
      },
    ],
    blocked: true,
  },
  {
    id: 2,
    systemName: "Facial Recognition System",
    systemType: "Biometric System",
    prohibitionReason: "Real-time remote biometric identification in public spaces (Art. 5.1.d)",
    article: "Art. 5.1.d",
    detectedAt: "2025-12-01T11:00:00Z",
    status: "WARNING",
    projectId: 5002,
    projectName: "Security Monitoring System",
    confidence: 0.75,
    detectedSystems: [
      {
        id: 2,
        name: "Real-time Remote Biometric Identification",
        category: "Art. 5.1.d",
        detectedAt: "2025-12-01T11:00:00Z",
        confidence: 0.75,
      },
    ],
    blocked: false,
  },
  {
    id: 3,
    systemName: "Customer Analytics Platform",
    systemType: "AI System",
    prohibitionReason: "No prohibited systems detected",
    article: "N/A",
    detectedAt: "2025-12-01T09:00:00Z",
    status: "CLEAN",
    projectId: 5003,
    projectName: "Customer Analytics Platform",
    confidence: 1.0,
    blocked: false,
  },
];

const mockData: ProhibitedSystemsData = {
  activeProhibited: 1,
  blockedDeployments: 2,
  totalDetections: 3,
  systems: mockSystems,
  catalog: [
    {
      article: "Art. 5.1.a",
      description: "AI systems using subliminal techniques to distort behavior",
      examples: ["Manipulative advertising", "Coercive interfaces"],
    },
    {
      article: "Art. 5.1.b",
      description: "Real-time remote biometric identification in public spaces",
      examples: ["Facial recognition in streets", "Gait recognition systems"],
    },
    {
      article: "Art. 5.1.c",
      description: "Social scoring for general purpose",
      examples: ["Citizen rating systems", "General purpose credit scoring"],
    },
    {
      article: "Art. 5.1.d",
      description: "Emotion recognition in workplace and education",
      examples: ["Employee monitoring", "Student assessment"],
    },
  ],
};

export default function ProhibitedSystemsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [data, setData] = useState<ProhibitedSystemsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [verifying, setVerifying] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));
      setData(mockData);
    } catch (error) {
      console.error("Error loading prohibited systems data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyProject = async (projectId: number) => {
    try {
      setVerifying(true);
      // Simular verificación
      await new Promise((resolve) => setTimeout(resolve, 1500));
      // Actualizar estado del proyecto
      if (data) {
        const updatedSystems = data.systems.map((system) =>
          system.projectId === projectId
            ? { ...system, status: "PROHIBITED" as const, blocked: true }
            : system
        );
        setData({ ...data, systems: updatedSystems });
      }
    } catch (error) {
      console.error("Error verifying project:", error);
    } finally {
      setVerifying(false);
    }
  };

  const handleBlockDeployment = async (projectId: number) => {
    try {
      // Simular bloqueo
      if (data) {
        const updatedSystems = data.systems.map((system) =>
          system.projectId === projectId
            ? { ...system, status: "BLOCKED" as const, blocked: true }
            : system
        );
        setData({ ...data, systems: updatedSystems });
      }
    } catch (error) {
      console.error("Error blocking deployment:", error);
    }
  };

  const filteredSystems = data?.systems.filter((system) => {
    const matchesSearch =
      system.systemName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      system.prohibitionReason.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === "ALL" || system.status === filterStatus;
    return matchesSearch && matchesStatus;
  });

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "BLOCKED":
        return (
          <Badge variant="danger">
            <XCircle className="h-3 w-3 mr-1" />
            {t("governance.prohibitedSystems.blocked")}
          </Badge>
        );
      case "PROHIBITED":
        return (
          <Badge variant="danger">
            <Ban className="h-3 w-3 mr-1" />
            {t("governance.prohibitedSystems.prohibited")}
          </Badge>
        );
      case "WARNING":
        return (
          <Badge className="bg-yellow-500">
            <AlertTriangle className="h-3 w-3 mr-1" />
            {t("governance.prohibitedSystems.warning")}
          </Badge>
        );
      case "CLEAN":
        return (
          <Badge className="bg-green-500">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t("governance.prohibitedSystems.clean")}
          </Badge>
        );
      case "RESOLVED":
        return (
          <Badge className="bg-green-500">
            <CheckCircle className="h-3 w-3 mr-1" />
            {t("governance.prohibitedSystems.resolved")}
          </Badge>
        );
      default:
        return <Badge variant="outline">{status}</Badge>;
    }
  };

  if (loading || !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-red-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-red-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-red-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          {/* Título y descripción a la izquierda */}
          <div className="flex-1 space-y-2">
            <div className="flex items-center gap-3">
              <Ban className="w-8 h-8 text-red-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-red-500 to-red-700 bg-clip-text text-transparent">
                {t("governance.prohibitedSystems.title")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.prohibitedSystems.subtitle")}
            </p>
          </div>
          {/* Botones de acción a la derecha */}
          <div className="flex items-center gap-4">
            <Button
              onClick={() => {
                if (typeof window !== "undefined") {
                  window.location.href = "/governance/compliance/prohibited-systems/catalog";
                }
              }}
              variant="outline"
              className="backdrop-blur-md bg-background/60"
            >
              <Shield className="h-4 w-4 mr-2" />
              {t("governance.prohibitedSystems.viewCatalog")}
            </Button>
            <Button
              onClick={() => handleVerifyProject(5001)}
              disabled={verifying}
              className="backdrop-blur-md bg-background/60"
            >
              <RefreshCw className={`h-4 w-4 mr-2 ${verifying ? "animate-spin" : ""}`} />
              {t("governance.prohibitedSystems.verifyAllProjects")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="backdrop-blur-md bg-background/60 border-red-500/20">
          <CardBody>
            <div className="text-sm text-muted-foreground">{t("governance.prohibitedSystems.activeProhibited")}</div>
            <div className="text-2xl font-bold text-red-600">
              {data.activeProhibited}
            </div>
          </CardBody>
        </Card>
        <Card className="backdrop-blur-md bg-background/60 border-orange-500/20">
          <CardBody>
            <div className="text-sm text-muted-foreground">{t("governance.prohibitedSystems.blockedDeployments")}</div>
            <div className="text-2xl font-bold text-orange-600">
              {data.blockedDeployments}
            </div>
          </CardBody>
        </Card>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody>
            <div className="text-sm text-muted-foreground">{t("governance.prohibitedSystems.totalDetections")}</div>
            <div className="text-2xl font-bold">{data.totalDetections}</div>
          </CardBody>
        </Card>
      </div>

      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <CardTitle>{t("governance.prohibitedSystems.catalogTitle")}</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {data.catalog.map((item, idx) => (
              <div key={idx} className="p-4 border rounded-lg backdrop-blur-md bg-background/40 hover:bg-background/60 transition-all">
                <div className="flex items-center gap-2 mb-2">
                  <Badge variant="danger">{item.article}</Badge>
                </div>
                <div className="text-sm font-medium mb-2">{item.description}</div>
                <div className="text-xs text-muted-foreground">
                  <div className="font-medium mb-1">{t("governance.prohibitedSystems.examples")}</div>
                  <ul className="list-disc list-inside space-y-1">
                    {item.examples.map((example, i) => (
                      <li key={i}>{example}</li>
                    ))}
                  </ul>
                </div>
              </div>
            ))}
          </div>
        </CardBody>
      </Card>

      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>{t("governance.prohibitedSystems.detectedSystems")}</CardTitle>
            <div className="flex items-center gap-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("governance.prohibitedSystems.search")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10 pr-3 py-2 border rounded-md text-sm backdrop-blur-md bg-background/60"
                />
              </div>
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="px-3 py-2 border rounded-md text-sm backdrop-blur-md bg-background/60"
              >
                <option value="ALL">{t("governance.prohibitedSystems.allStatus")}</option>
                <option value="PROHIBITED">{t("governance.prohibitedSystems.prohibited")}</option>
                <option value="WARNING">{t("governance.prohibitedSystems.warning")}</option>
                <option value="CLEAN">{t("governance.prohibitedSystems.clean")}</option>
                <option value="BLOCKED">{t("governance.prohibitedSystems.blocked")}</option>
                <option value="RESOLVED">{t("governance.prohibitedSystems.resolved")}</option>
              </select>
            </div>
          </div>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {filteredSystems?.map((system) => (
              <Card
                key={system.id}
                className="backdrop-blur-md bg-background/40 hover:bg-background/60 transition-all border-border/50"
              >
                <CardBody className="space-y-3">
                  <div className="space-y-2">
                    <div className="flex items-start justify-between gap-2">
                      <span className="font-semibold text-base line-clamp-2">{system.projectName}</span>
                      {getStatusBadge(system.status)}
                    </div>
                    {system.confidence && (
                      <Badge variant="outline" className="text-xs">
                        {Math.round(system.confidence * 100)}% {t("governance.prohibitedSystems.confidence")}
                      </Badge>
                    )}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    <div>{t("governance.prohibitedSystems.type")}: {system.systemType}</div>
                  </div>
                  {system.detectedSystems && system.detectedSystems.length > 0 && (
                    <div>
                      <div className="text-xs font-semibold mb-1">{t("governance.prohibitedSystems.detectedSystems")}:</div>
                      <div className="space-y-1">
                        {system.detectedSystems.map((detected, idx) => (
                          <div key={idx} className="flex items-center gap-1 flex-wrap">
                            <Badge variant="danger" className="text-xs">{detected.category}</Badge>
                            <span className="text-xs line-clamp-1">{detected.name}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                  <div className="space-y-1">
                    <div className="flex items-center gap-1 flex-wrap">
                      <Badge variant="outline" className="text-xs">{system.article}</Badge>
                    </div>
                    <p className="text-xs font-medium line-clamp-2">
                      {system.prohibitionReason}
                    </p>
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {t("governance.prohibitedSystems.detected")}: {new Date(system.detectedAt).toLocaleString()}
                  </div>
                  {(system.status === "PROHIBITED" || system.status === "WARNING") && (
                    <div className="flex flex-col gap-2 pt-2 border-t">
                      <Button
                        size="sm"
                        variant="danger"
                        className="w-full"
                        onClick={() => handleBlockDeployment(system.projectId)}
                      >
                        {t("governance.prohibitedSystems.blockDeployment")}
                      </Button>
                      <div className="flex gap-2">
                        <Button
                          size="sm"
                          variant="outline"
                          className="flex-1"
                          onClick={() => {
                            if (typeof window !== "undefined") {
                              window.location.href = `/governance/compliance/prohibited-systems/${system.id}`;
                            }
                          }}
                        >
                          <ExternalLink className="h-3 w-3 mr-1" />
                          {t("governance.prohibitedSystems.viewDetails")}
                        </Button>
                        <Button size="sm" variant="outline" className="flex-1">
                          {t("governance.prohibitedSystems.markFalsePositive")}
                        </Button>
                      </div>
                    </div>
                  )}
                </CardBody>
              </Card>
            ))}
          </div>
        </CardBody>
      </Card>
      </div>
    </div>
  );
}
