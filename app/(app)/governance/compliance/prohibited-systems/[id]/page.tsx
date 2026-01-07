"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Ban,
  ArrowLeft,
  Shield,
  AlertTriangle,
  CheckCircle,
  XCircle,
  Mail,
  FileText,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

interface DetectedSystem {
  id: number;
  name: string;
  category: string;
  detectedAt: string;
  confidence: number;
}

interface Project {
  id: number;
  name: string;
  description: string;
  status: string;
  models: {
    id: number;
    name: string;
    type: string;
  }[];
}

interface DetectionDetail {
  id: number;
  project: Project;
  detectedSystem: DetectedSystem;
  matchedKeywords: string[];
  evidence: string;
  blocked: boolean;
  falsePositive: boolean;
}

const mockDetection: DetectionDetail = {
  id: 1,
  project: {
    id: 5001,
    name: "AI Credit Scoring System",
    description: "System for evaluating creditworthiness using AI",
    status: "ACTIVE",
    models: [
      { id: 1, name: "Credit Score Model v2.1", type: "Classification" },
      { id: 2, name: "Risk Assessment Model", type: "Regression" },
    ],
  },
  detectedSystem: {
    id: 1,
    name: "Social Scoring by Public Authorities",
    category: "Art. 5.1.c",
    detectedAt: "2025-12-01T10:30:00Z",
    confidence: 0.95,
  },
  matchedKeywords: ["social scoring", "public authority", "citizen rating"],
  evidence:
    "Project description and model names contain keywords matching prohibited system patterns. The system appears to evaluate citizens' behavior for general purpose scoring.",
  blocked: true,
  falsePositive: false,
};

export default function ProhibitedSystemDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const detectionId = params?.id ? parseInt(params.id as string) : 1;

  const [detection, setDetection] = useState<DetectionDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDetection();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [params?.id]);

  const loadDetection = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 300));
      setDetection({ ...mockDetection, id: detectionId });
    } catch (error) {
      console.error("Error loading detection:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleBlockDeployment = async () => {
    if (detection) setDetection({ ...detection, blocked: true });
  };

  const handleMarkFalsePositive = async () => {
    if (detection) setDetection({ ...detection, falsePositive: true, blocked: false });
  };

  if (loading || !detection) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-red-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-red-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-red-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Button
              variant="outline"
              onClick={() => {
                if (typeof window !== "undefined") {
                  window.location.href = "/governance/compliance/prohibited-systems";
                }
              }}
              className="backdrop-blur-md bg-background/60"
            >
              <ArrowLeft className="h-4 w-4 mr-2" />
              {t("governance.prohibitedSystems.back", "Back")}
            </Button>
            <div className="flex items-center gap-3">
              <Ban className="w-8 h-8 text-red-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-red-500 to-red-700 bg-clip-text text-transparent">
                {t("governance.prohibitedSystems.detectionDetail", "Detection Detail")}
              </h1>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileText className="h-5 w-5" />
                {t("governance.prohibitedSystems.projectInformation", "Project Information")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.projectName", "Project Name")}
                  </div>
                  <div className="text-lg">{detection.project.name}</div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.projectDescription", "Project Description")}
                  </div>
                  <div className="text-muted-foreground">{detection.project.description}</div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-2">
                    {t("governance.prohibitedSystems.associatedModels", "Associated Models")}
                  </div>
                  <div className="space-y-2">
                    {detection.project.models.map((model) => (
                      <div key={model.id} className="flex items-center gap-2">
                        <Badge variant="outline">{model.type}</Badge>
                        <span>{model.name}</span>
                      </div>
                    ))}
                  </div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.projectStatus", "Project Status")}
                  </div>
                  <Badge className={detection.project.status === "ACTIVE" ? "bg-green-500" : "bg-gray-500"}>
                    {detection.project.status}
                  </Badge>
                </div>
              </div>
            </CardBody>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-red-500/20">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertTriangle className="h-5 w-5 text-red-500" />
                {t("governance.prohibitedSystems.detectionDetails", "Detection Details")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.detectedSystem", "Detected System")}
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="danger">{detection.detectedSystem.category}</Badge>
                    <span className="text-lg font-semibold">{detection.detectedSystem.name}</span>
                  </div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.confidenceLevel", "Confidence Level")}
                  </div>
                  <div className="flex items-center gap-2">
                    <div className="flex-1 bg-gray-200 rounded-full h-2.5 dark:bg-gray-700">
                      <div
                        className="bg-red-600 h-2.5 rounded-full"
                        style={{ width: `${detection.detectedSystem.confidence * 100}%` }}
                      ></div>
                    </div>
                    <span className="text-sm font-semibold">
                      {Math.round(detection.detectedSystem.confidence * 100)}%
                    </span>
                  </div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.matchedKeywords", "Matched Keywords")}
                  </div>
                  <div className="flex flex-wrap gap-2">
                    {detection.matchedKeywords.map((keyword, idx) => (
                      <Badge
                        key={idx}
                        className="bg-red-100 text-red-800 border-red-300 dark:bg-red-900/30 dark:text-red-200 dark:border-red-700"
                      >
                        {keyword}
                      </Badge>
                    ))}
                  </div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.evidence", "Evidence")}
                  </div>
                  <div className="p-3 bg-muted rounded-lg text-sm">{detection.evidence}</div>
                </div>
                <div>
                  <div className="text-sm font-semibold mb-1">
                    {t("governance.prohibitedSystems.detectedAt", "Detected At")}
                  </div>
                  <div className="text-muted-foreground">
                    {new Date(detection.detectedSystem.detectedAt).toLocaleString()}
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.prohibitedSystems.statusAndActions", "Status and Actions")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center gap-4">
                <div className="text-sm font-semibold">
                  {t("governance.prohibitedSystems.currentStatus", "Current Status")}:
                </div>
                {detection.blocked ? (
                  <Badge variant="danger" className="flex items-center gap-1">
                    <XCircle className="h-3 w-3" />
                    {t("governance.prohibitedSystems.deploymentBlocked", "Deployment Blocked")}
                  </Badge>
                ) : (
                  <Badge className="bg-green-500 flex items-center gap-1">
                    <CheckCircle className="h-3 w-3" />
                    {t("governance.prohibitedSystems.deploymentAllowed", "Deployment Allowed")}
                  </Badge>
                )}
                {detection.falsePositive && (
                  <Badge variant="outline" className="flex items-center gap-1">
                    <CheckCircle className="h-3 w-3" />
                    {t("governance.prohibitedSystems.markedFalsePositive", "Marked as False Positive")}
                  </Badge>
                )}
              </div>
              <div className="flex flex-wrap gap-2">
                {!detection.blocked && (
                  <Button
                    variant="danger"
                    onClick={handleBlockDeployment}
                    className="backdrop-blur-md bg-background/60"
                  >
                    <Shield className="h-4 w-4 mr-2" />
                    {t("governance.prohibitedSystems.blockDeployment", "Block Deployment")}
                  </Button>
                )}
                {!detection.falsePositive && (
                  <Button
                    variant="outline"
                    onClick={handleMarkFalsePositive}
                    className="backdrop-blur-md bg-background/60"
                  >
                    <CheckCircle className="h-4 w-4 mr-2" />
                    {t("governance.prohibitedSystems.markFalsePositive", "Mark False Positive")}
                  </Button>
                )}
                <Button variant="outline" className="backdrop-blur-md bg-background/60">
                  <Mail className="h-4 w-4 mr-2" />
                  {t("governance.prohibitedSystems.contactProjectOwner", "Contact Project Owner")}
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
