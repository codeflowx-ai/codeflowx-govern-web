"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Send,
  FileText,
  CheckCircle,
  Clock,
  XCircle,
  Globe,
  Save,
  ChevronRight,
  ChevronLeft,
  ArrowLeft,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

interface EuRegistrationData {
  registrationId: number;
  projectId: number;
  projectName: string;
  status: "DRAFT" | "PENDING" | "SUBMITTED" | "REGISTERED" | "REJECTED";
  registrationType: "STANDARD" | "SENSITIVE" | "NATIONAL";
  sectionA: {
    providerName: string;
    providerAddress: string;
    providerCountry: string;
    taxId: string;
    contactEmail: string;
    contactPhone: string;
    website?: string;
    legalRepresentative?: string;
    registrationNumber?: string;
    vatNumber?: string;
    establishmentDate?: string;
    mainActivity?: string;
  };
  sectionB: {
    systemName: string;
    intendedPurpose: string;
    highRiskCategory: string;
    technicalDescription: string;
    deploymentDate?: string;
    systemVersion?: string;
    aiTechniques?: string;
    trainingData?: string;
    performanceMetrics?: string;
  };
  sectionC: {
    conformityBasis: string;
    notifiedBody?: string | null;
    certificateId?: string | null;
    certificateDate?: string | null;
    conformityAssessment?: string | null;
    additionalInfo?: string | null;
  };
}

const mockData: EuRegistrationData = {
  registrationId: 2001,
  projectId: 1001,
  projectName: "AI Credit Scoring System",
  status: "DRAFT",
  registrationType: "STANDARD",
  sectionA: {
    providerName: "ACME Corporation",
    providerAddress: "123 Main St, Madrid, Spain",
    providerCountry: "ES",
    taxId: "B12345678",
    contactEmail: "contact@acme.com",
    contactPhone: "+34 123 456 789",
    website: "https://www.acme.com",
    legalRepresentative: "John Doe",
    registrationNumber: "REG-12345",
    vatNumber: "ESB12345678",
    establishmentDate: "2020-01-15",
    mainActivity: "Financial Services",
  },
  sectionB: {
    systemName: "AI Credit Scoring System",
    intendedPurpose: "Automated credit scoring for loan applications",
    highRiskCategory: "A3_4",
    technicalDescription:
      "Machine learning model for credit risk assessment using gradient boosting algorithms",
    deploymentDate: "2024-01-01",
    systemVersion: "2.1.0",
    aiTechniques: "Gradient Boosting, Neural Networks",
    trainingData: "Historical loan data from 2015-2023",
    performanceMetrics: "Accuracy: 87%, AUC: 0.92",
  },
  sectionC: {
    conformityBasis: "Internal control procedure",
    notifiedBody: null,
    certificateId: null,
    certificateDate: null,
    conformityAssessment: "Self-assessment completed",
    additionalInfo: "System complies with GDPR and EU AI Act requirements",
  },
};

const API_BASE_URL =
  process.env.NEXT_PUBLIC_BFF_BASE_URL ||
  process.env.NEXT_PUBLIC_API_BASE_URL ||
  "http://localhost:8080";
const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true" || true;

export default function EuRegistrationDetailPage() {
  const { t, language, mounted } = useTranslation();
  const params = useParams();
  const registrationId = params?.id ? parseInt(params.id as string) : null;

  const [data, setData] = useState<EuRegistrationData | null>(null);
  const [loading, setLoading] = useState(true);
  const [activeSection, setActiveSection] = useState<"A" | "B" | "C">("A");
  const [saving, setSaving] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (registrationId) loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [registrationId]);

  // Forzar re-render cuando cambie el idioma
  useEffect(() => {}, [language, mounted]);

  const loadData = async () => {
    if (!registrationId) return;
    try {
      setLoading(true);

      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        setData({ ...mockData, registrationId });
        return;
      }

      const response = await fetch(
        `${API_BASE_URL}/api/v1/eu-registrations/${registrationId}`
      );
      if (response.ok) {
        const registrationData = await response.json();
        setData({
          registrationId: registrationData.idxeuregistration,
          projectId: registrationData.idxproject,
          projectName: registrationData.projectName || "Unknown Project",
          status: registrationData.regstatus,
          registrationType: registrationData.regregistrationtype,
          sectionA: registrationData.regsubmissiondata?.sectionA || mockData.sectionA,
          sectionB: registrationData.regsubmissiondata?.sectionB || mockData.sectionB,
          sectionC: registrationData.regsubmissiondata?.sectionC || mockData.sectionC,
        });
      } else if (response.status === 404) {
        setData({ ...mockData, registrationId });
      } else {
        throw new Error(`Failed to load registration: ${response.status}`);
      }
    } catch (error) {
      console.error("Error loading EU registration data:", error);
      setData({ ...mockData, registrationId });
    } finally {
      setLoading(false);
    }
  };

  const saveSection = async (section: "A" | "B" | "C") => {
    if (!data) return;
    try {
      setSaving(true);

      if (USE_MOCK_DATA) {
        await new Promise((resolve) => setTimeout(resolve, 300));
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/v1/eu-registrations/section`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          registrationId: data.registrationId,
          projectId: data.projectId,
          section,
          data:
            section === "A"
              ? data.sectionA
              : section === "B"
              ? data.sectionB
              : data.sectionC,
        }),
      });
      if (response.ok) {
        const updated = await response.json();
        setData({
          ...data,
          registrationId: updated.idxeuregistration,
          status: updated.regstatus,
        });
      }
    } catch (error) {
      console.error("Error saving section:", error);
    } finally {
      setSaving(false);
    }
  };

  const handleSectionChange = async (newSection: "A" | "B" | "C") => {
    if (data && activeSection !== newSection) {
      await saveSection(activeSection);
      setActiveSection(newSection);
    }
  };

  const handleSubmit = async () => {
    if (!data) return;
    try {
      setSubmitting(true);
      const response = await fetch("/api/compliance/eu-registration/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ registrationId: data.registrationId }),
      });
      if (response.ok) {
        const result = await response.json();
        setData({ ...data, status: result.status, registrationId: result.registrationId });
      }
    } catch (error) {
      console.error("Error submitting registration:", error);
    } finally {
      setSubmitting(false);
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
        return <Badge variant="outline">{t(statusKey, "DRAFT")}</Badge>;
    }
  };

  const validateSection = (section: "A" | "B" | "C"): boolean => {
    if (!data) return false;
    if (section === "A") {
      return !!(
        data.sectionA.providerName &&
        data.sectionA.providerAddress &&
        data.sectionA.providerCountry &&
        data.sectionA.taxId &&
        data.sectionA.contactEmail
      );
    }
    if (section === "B") {
      return !!(
        data.sectionB.systemName &&
        data.sectionB.intendedPurpose &&
        data.sectionB.highRiskCategory &&
        data.sectionB.technicalDescription
      );
    }
    if (section === "C") return !!data.sectionC.conformityBasis;
    return false;
  };

  if (loading || !data || !mounted) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Back Button */}
        <div className="mb-4">
          <Button
            variant="outline"
            onClick={() => {
              window.location.href = "/governance/compliance/eu-registration";
            }}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("governance.compliance.euRegistration.back", "Volver")}
          </Button>
        </div>

        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="text-center space-y-2">
            <div className="flex items-center justify-center gap-3 mb-4">
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
          <div className="flex items-center gap-4">
            {getStatusBadge(data.status)}
            <Button
              variant="outline"
              onClick={() => {
                const json = JSON.stringify(data, null, 2);
                const blob = new Blob([json], { type: "application/json" });
                const url = URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = url;
                a.download = `eu-registration-${data.registrationId}.json`;
                a.click();
              }}
            >
              <FileText className="h-4 w-4 mr-2" />
              {t("governance.compliance.euRegistration.viewJSON", "View JSON")}
            </Button>
          </div>
        </div>

        {/* Registration Information Card */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50">
          <CardHeader>
            <CardTitle>
              {t(
                "governance.compliance.euRegistration.registrationInformation",
                "Registration Information"
              )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <Label className="text-muted-foreground">
                  {t("governance.compliance.euRegistration.registrationId", "Registration ID")}
                </Label>
                <div className="font-semibold text-lg">{data.registrationId}</div>
              </div>
              <div>
                <Label className="text-muted-foreground">
                  {t("governance.compliance.euRegistration.projectName", "Project Name")}
                </Label>
                <div className="font-semibold text-lg">{data.projectName}</div>
              </div>
              <div>
                <Label className="text-muted-foreground">
                  {t("governance.compliance.euRegistration.registrationType", "Registration Type")}
                </Label>
                <div className="font-semibold text-lg">
                  {t(
                    `governance.compliance.euRegistration.registrationTypeLabels.${data.registrationType}`,
                    data.registrationType
                  )}
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Section Navigation */}
        <div className="flex gap-2 justify-center">
          <Button
            variant={activeSection === "A" ? "primary" : "outline"}
            onClick={() => handleSectionChange("A")}
            className="relative"
          >
            {t("governance.compliance.euRegistration.sections.sectionA.title", "Section A")}
            {validateSection("A") && <CheckCircle className="h-4 w-4 ml-2 text-green-500" />}
          </Button>
          <Button
            variant={activeSection === "B" ? "primary" : "outline"}
            onClick={() => handleSectionChange("B")}
            className="relative"
          >
            {t("governance.compliance.euRegistration.sections.sectionB.title", "Section B")}
            {validateSection("B") && <CheckCircle className="h-4 w-4 ml-2 text-green-500" />}
          </Button>
          <Button
            variant={activeSection === "C" ? "primary" : "outline"}
            onClick={() => handleSectionChange("C")}
            className="relative"
          >
            {t("governance.compliance.euRegistration.sections.sectionC.title", "Section C")}
            {validateSection("C") && <CheckCircle className="h-4 w-4 ml-2 text-green-500" />}
          </Button>
        </div>

        {/* Section Content */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50">
          <CardHeader>
            <CardTitle>
              {activeSection === "A"
                ? t(
                    "governance.compliance.euRegistration.sections.sectionA.title",
                    "Section A: Provider Information"
                  )
                : activeSection === "B"
                ? t(
                    "governance.compliance.euRegistration.sections.sectionB.title",
                    "Section B: AI System Information"
                  )
                : t(
                    "governance.compliance.euRegistration.sections.sectionC.title",
                    "Section C: Conformity Information"
                  )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            {activeSection === "A" && (
              <div className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.providerName", "Provider Name")}{" "}
                      *
                    </Label>
                    <Input
                      value={data.sectionA.providerName}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, providerName: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.providerCountry", "Country")}{" "}
                      *
                    </Label>
                    <Input
                      value={data.sectionA.providerCountry}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, providerCountry: e.target.value },
                        })
                      }
                      placeholder={t(
                        "governance.compliance.euRegistration.sections.sectionA.providerCountryPlaceholder",
                        "ES, FR, DE..."
                      )}
                    />
                  </div>
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionA.providerAddress", "Address")}{" "}
                    *
                  </Label>
                  <Textarea
                    value={data.sectionA.providerAddress}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionA: { ...data.sectionA, providerAddress: e.target.value },
                      })
                    }
                    rows={2}
                  />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.taxId", "Tax ID")} *
                    </Label>
                    <Input
                      value={data.sectionA.taxId}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, taxId: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.vatNumber", "VAT Number")}
                    </Label>
                    <Input
                      value={data.sectionA.vatNumber || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, vatNumber: e.target.value },
                        })
                      }
                    />
                  </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.contactEmail", "Contact Email")}{" "}
                      *
                    </Label>
                    <Input
                      type="email"
                      value={data.sectionA.contactEmail}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, contactEmail: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionA.contactPhone", "Contact Phone")}{" "}
                      *
                    </Label>
                    <Input
                      value={data.sectionA.contactPhone}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, contactPhone: e.target.value },
                        })
                      }
                    />
                  </div>
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionA.website", "Website")}
                  </Label>
                  <Input
                    type="url"
                    value={data.sectionA.website || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionA: { ...data.sectionA, website: e.target.value },
                      })
                    }
                  />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t(
                        "governance.compliance.euRegistration.sections.sectionA.legalRepresentative",
                        "Legal Representative"
                      )}
                    </Label>
                    <Input
                      value={data.sectionA.legalRepresentative || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, legalRepresentative: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t(
                        "governance.compliance.euRegistration.sections.sectionA.establishmentDate",
                        "Establishment Date"
                      )}
                    </Label>
                    <Input
                      type="date"
                      value={data.sectionA.establishmentDate || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionA: { ...data.sectionA, establishmentDate: e.target.value },
                        })
                      }
                    />
                  </div>
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionA.mainActivity", "Main Activity")}
                  </Label>
                  <Input
                    value={data.sectionA.mainActivity || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionA: { ...data.sectionA, mainActivity: e.target.value },
                      })
                    }
                  />
                </div>
              </div>
            )}

            {activeSection === "B" && (
              <div className="space-y-4">
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionB.systemName", "System Name")} *
                  </Label>
                  <Input
                    value={data.sectionB.systemName}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, systemName: e.target.value },
                      })
                    }
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionB.intendedPurpose",
                      "Intended Purpose"
                    )}{" "}
                    *
                  </Label>
                  <Textarea
                    value={data.sectionB.intendedPurpose}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, intendedPurpose: e.target.value },
                      })
                    }
                    rows={3}
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionB.highRiskCategory",
                      "High Risk Category (Annex III)"
                    )}{" "}
                    *
                  </Label>
                  <Input
                    value={data.sectionB.highRiskCategory}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, highRiskCategory: e.target.value },
                      })
                    }
                    placeholder={t(
                      "governance.compliance.euRegistration.sections.sectionB.highRiskCategoryPlaceholder",
                      "A3_4, A5_1, etc."
                    )}
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionB.technicalDescription",
                      "Technical Description"
                    )}{" "}
                    *
                  </Label>
                  <Textarea
                    value={data.sectionB.technicalDescription}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, technicalDescription: e.target.value },
                      })
                    }
                    rows={4}
                  />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t(
                        "governance.compliance.euRegistration.sections.sectionB.deploymentDate",
                        "Deployment Date"
                      )}
                    </Label>
                    <Input
                      type="date"
                      value={data.sectionB.deploymentDate || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionB: { ...data.sectionB, deploymentDate: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t(
                        "governance.compliance.euRegistration.sections.sectionB.systemVersion",
                        "System Version"
                      )}
                    </Label>
                    <Input
                      value={data.sectionB.systemVersion || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionB: { ...data.sectionB, systemVersion: e.target.value },
                        })
                      }
                    />
                  </div>
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionB.aiTechniques", "AI Techniques")}
                  </Label>
                  <Textarea
                    value={data.sectionB.aiTechniques || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, aiTechniques: e.target.value },
                      })
                    }
                    rows={2}
                  />
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionB.trainingData", "Training Data")}
                  </Label>
                  <Textarea
                    value={data.sectionB.trainingData || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, trainingData: e.target.value },
                      })
                    }
                    rows={2}
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionB.performanceMetrics",
                      "Performance Metrics"
                    )}
                  </Label>
                  <Textarea
                    value={data.sectionB.performanceMetrics || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionB: { ...data.sectionB, performanceMetrics: e.target.value },
                      })
                    }
                    rows={2}
                  />
                </div>
              </div>
            )}

            {activeSection === "C" && (
              <div className="space-y-4">
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionC.conformityBasis",
                      "Conformity Basis"
                    )}{" "}
                    *
                  </Label>
                  <Textarea
                    value={data.sectionC.conformityBasis}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionC: { ...data.sectionC, conformityBasis: e.target.value },
                      })
                    }
                    rows={3}
                    placeholder={t(
                      "governance.compliance.euRegistration.sections.sectionC.conformityBasisPlaceholder",
                      "e.g., Internal control procedure, Conformity assessment by notified body..."
                    )}
                  />
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionC.notifiedBody", "Notified Body")}
                    </Label>
                    <Input
                      value={data.sectionC.notifiedBody || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionC: { ...data.sectionC, notifiedBody: e.target.value },
                        })
                      }
                    />
                  </div>
                  <div>
                    <Label>
                      {t("governance.compliance.euRegistration.sections.sectionC.certificateId", "Certificate ID")}
                    </Label>
                    <Input
                      value={data.sectionC.certificateId || ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          sectionC: { ...data.sectionC, certificateId: e.target.value },
                        })
                      }
                    />
                  </div>
                </div>
                <div>
                  <Label>
                    {t("governance.compliance.euRegistration.sections.sectionC.certificateDate", "Certificate Date")}
                  </Label>
                  <Input
                    type="date"
                    value={data.sectionC.certificateDate || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionC: { ...data.sectionC, certificateDate: e.target.value },
                      })
                    }
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionC.conformityAssessment",
                      "Conformity Assessment"
                    )}
                  </Label>
                  <Textarea
                    value={data.sectionC.conformityAssessment || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionC: { ...data.sectionC, conformityAssessment: e.target.value },
                      })
                    }
                    rows={3}
                  />
                </div>
                <div>
                  <Label>
                    {t(
                      "governance.compliance.euRegistration.sections.sectionC.additionalInfo",
                      "Additional Information"
                    )}
                  </Label>
                  <Textarea
                    value={data.sectionC.additionalInfo || ""}
                    onChange={(e) =>
                      setData({
                        ...data,
                        sectionC: { ...data.sectionC, additionalInfo: e.target.value },
                      })
                    }
                    rows={4}
                    placeholder={t(
                      "governance.compliance.euRegistration.sections.sectionC.additionalInfoPlaceholder",
                      "Any additional information relevant to the registration..."
                    )}
                  />
                </div>
              </div>
            )}
          </CardBody>
        </Card>

        {/* Action Buttons */}
        <div className="flex justify-between items-center">
          <Button variant="outline" onClick={() => saveSection(activeSection)} disabled={saving}>
            <Save className="h-4 w-4 mr-2" />
            {saving
              ? t("governance.compliance.euRegistration.saving", "Saving...")
              : t("governance.compliance.euRegistration.saveDraft", "Save Draft")}
          </Button>
          <div className="flex gap-2">
            {activeSection !== "A" && (
              <Button
                variant="outline"
                onClick={() => handleSectionChange(activeSection === "B" ? "A" : "B")}
              >
                <ChevronLeft className="h-4 w-4 mr-2" />
                {t("governance.compliance.euRegistration.previous", "Previous")}
              </Button>
            )}
            {activeSection !== "C" && (
              <Button onClick={() => handleSectionChange(activeSection === "A" ? "B" : "C")} disabled={!validateSection(activeSection)}>
                {t("governance.compliance.euRegistration.next", "Next")}
                <ChevronRight className="h-4 w-4 ml-2" />
              </Button>
            )}
            {activeSection === "C" && (
              <Button
                onClick={handleSubmit}
                disabled={
                  data.status === "SUBMITTED" ||
                  data.status === "REGISTERED" ||
                  submitting ||
                  !validateSection("C")
                }
                className="bg-blue-600 hover:bg-blue-700"
              >
                {submitting ? (
                  <>
                    <Clock className="h-4 w-4 mr-2 animate-spin" />
                    {t("governance.compliance.euRegistration.submitting", "Submitting...")}
                  </>
                ) : (
                  <>
                    <Send className="h-4 w-4 mr-2" />
                    {t(
                      "governance.compliance.euRegistration.submitToEuDatabase",
                      "Submit to EU Database"
                    )}
                  </>
                )}
              </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
