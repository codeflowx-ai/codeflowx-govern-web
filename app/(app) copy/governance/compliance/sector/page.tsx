"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Building2,
  AlertTriangle,
  FileText,
  CheckCircle,
  Globe,
} from "lucide-react";
import { useEffect, useState } from "react";

interface Sector {
  label: string;
  code: string;
  riskProfile: string;
  geoScope: string;
  requirementsDoc?: string;
  regulationRefs: string[];
  standardRefs: string[];
  subsectorLabels: string[];
  prompts: string[];
  hasSubsectors: () => boolean;
  hasPrompts: () => boolean;
}

interface Framework {
  configKey: string;
  version: string;
  geoScope: string;
  metadata?: {
    prompts?: string;
  };
}

interface Policy {
  policyCode: string;
  riskProfile: string;
  activationConditions?: {
    regulationRefs?: string;
  };
}

interface SectorDashboardData {
  sectors: Sector[];
  selectedSector: Sector | null;
  selectedFrameworks: Framework[];
  selectedPolicies: Policy[];
}

const mockSectors: Sector[] = [
  {
    label: "Healthcare - Clinical",
    code: "HLT_CLN",
    riskProfile: "HIGH",
    geoScope: "EU",
    requirementsDoc: "EU MDR 2017/745",
    regulationRefs: ["EU MDR 2017/745", "GDPR"],
    standardRefs: ["ISO 13485", "IEC 62304"],
    subsectorLabels: ["Diagnostics", "Therapeutics", "Monitoring"],
    prompts: ["Clinical Safety Prompt", "Medical Device Classification"],
    hasSubsectors: () => true,
    hasPrompts: () => true,
  },
  {
    label: "Finance - Banking",
    code: "FIN_BNK",
    riskProfile: "HIGH",
    geoScope: "EU",
    regulationRefs: ["PSD2", "GDPR", "MiFID II"],
    standardRefs: ["ISO 27001", "PCI DSS"],
    subsectorLabels: ["Retail Banking", "Investment Banking"],
    prompts: ["Fraud Detection Prompt", "Risk Assessment Prompt"],
    hasSubsectors: () => true,
    hasPrompts: () => true,
  },
  {
    label: "Manufacturing - Automotive",
    code: "MFG_AUT",
    riskProfile: "MEDIUM",
    geoScope: "GLOBAL",
    regulationRefs: ["UN R157", "GDPR"],
    standardRefs: ["ISO 26262", "IEC 61508"],
    subsectorLabels: [],
    prompts: [],
    hasSubsectors: () => false,
    hasPrompts: () => false,
  },
];

const mockFrameworks: Framework[] = [
  {
    configKey: "EU_AI_ACT_V1",
    version: "1.0",
    geoScope: "EU",
    metadata: {
      prompts: "EU AI Act Compliance Prompt",
    },
  },
  {
    configKey: "GDPR_COMPLIANCE_V2",
    version: "2.1",
    geoScope: "EU",
    metadata: {
      prompts: "GDPR Data Protection Prompt",
    },
  },
];

const mockPolicies: Policy[] = [
  {
    policyCode: "POL_HIGH_RISK_001",
    riskProfile: "HIGH",
    activationConditions: {
      regulationRefs: "EU AI Act Art. 6",
    },
  },
  {
    policyCode: "POL_MEDICAL_DEVICE_002",
    riskProfile: "HIGH",
    activationConditions: {
      regulationRefs: "EU MDR 2017/745",
    },
  },
];

export default function SectorDashboardPage() {
  const { t } = useTranslation();
  const [data, setData] = useState<SectorDashboardData>({
    sectors: [],
    selectedSector: null,
    selectedFrameworks: [],
    selectedPolicies: [],
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada real a API
      await new Promise((resolve) => setTimeout(resolve, 500));
      setData({
        sectors: mockSectors,
        selectedSector: null,
        selectedFrameworks: [],
        selectedPolicies: [],
      });
    } catch (error) {
      console.error("Error loading dashboard:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSectorSelect = (sector: Sector) => {
    // Simular carga de frameworks y políticas para el sector seleccionado
    setData({
      ...data,
      selectedSector: sector,
      selectedFrameworks: mockFrameworks,
      selectedPolicies: mockPolicies,
    });
  };

  const getRiskColor = (risk: string) => {
    switch (risk?.toUpperCase()) {
      case "HIGH":
        return "bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-300";
      case "MEDIUM":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300";
      case "LOW":
        return "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300";
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Building2 className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("governance.sector.title")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.sector.subtitle")}
          </p>
        </div>

        {/* Main Content */}
        <Card className="border-primary backdrop-blur-md bg-background/60">
          <CardHeader className="bg-primary/10">
            <CardTitle>{t("governance.sector.title")}</CardTitle>
            <p className="text-sm text-muted-foreground mt-2">
              {t("governance.sector.subtitle")}
            </p>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              {/* Lista de Sectores */}
              <div className="lg:col-span-1">
                <Card className="backdrop-blur-md bg-background/60 border-border/50">
                  <CardHeader>
                    <CardTitle className="text-lg">{t("governance.sector.title")}</CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="space-y-2 max-h-[600px] overflow-y-auto">
                      {data.sectors.map((sector, idx) => (
                        <div
                          key={idx}
                          onClick={() => handleSectorSelect(sector)}
                          className={`p-3 rounded-lg border cursor-pointer transition-all duration-300 ${
                            data.selectedSector?.code === sector.code
                              ? "border-primary bg-primary/10"
                              : "border-border hover:border-primary/50 hover:bg-muted/50"
                          }`}
                        >
                          <div className="flex items-center justify-between mb-2">
                            <span className="font-semibold">{sector.label}</span>
                            <Badge className={getRiskColor(sector.riskProfile)}>
                              {sector.riskProfile}
                            </Badge>
                          </div>
                          <div className="flex items-center gap-2 text-xs text-muted-foreground">
                            <Globe className="w-3 h-3" />
                            <span>{sector.geoScope}</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </CardBody>
                </Card>
              </div>

              {/* Detalle del Sector Seleccionado */}
              <div className="lg:col-span-2 space-y-6">
                {/* Información del Sector */}
                <Card className="backdrop-blur-md bg-background/60 border-border/50">
                  <CardBody>
                    <h4 className="text-xl font-bold mb-4">
                      {data.selectedSector?.label || t("governance.sector.selectSector")}
                    </h4>
                    {data.selectedSector && (
                      <>
                        <div className="space-y-3 mb-4">
                          <div className="grid grid-cols-2 gap-4 text-sm">
                            <div>
                              <span className="font-semibold">{t("common.code")}:</span>
                              <p className="text-muted-foreground">{data.selectedSector.code}</p>
                            </div>
                            <div>
                              <span className="font-semibold">{t("governance.sector.riskProfile")}:</span>
                              <p>
                                <Badge className={getRiskColor(data.selectedSector.riskProfile)}>
                                  {data.selectedSector.riskProfile}
                                </Badge>
                              </p>
                            </div>
                            <div>
                              <span className="font-semibold">{t("governance.sector.geoScope")}:</span>
                              <p className="text-muted-foreground">
                                {data.selectedSector.geoScope}
                              </p>
                            </div>
                            {data.selectedSector.requirementsDoc && (
                              <div>
                                <span className="font-semibold">{t("governance.sector.requirementsDoc")}:</span>
                                <p className="text-muted-foreground">
                                  {data.selectedSector.requirementsDoc}
                                </p>
                              </div>
                            )}
                          </div>
                        </div>

                        <div className="space-y-4">
                          <div>
                            <h5 className="font-semibold mb-2 flex items-center gap-2">
                              <FileText className="w-4 h-4" />
                              {t("governance.sector.regulationRefs")}
                            </h5>
                            <div className="space-y-1">
                              {data.selectedSector.regulationRefs.map((reg, idx) => (
                                <Badge key={idx} variant="outline" className="mr-2">
                                  {reg}
                                </Badge>
                              ))}
                            </div>
                          </div>

                          <div>
                            <h5 className="font-semibold mb-2 flex items-center gap-2">
                              <CheckCircle className="w-4 h-4" />
                              {t("governance.sector.standardRefs")}
                            </h5>
                            <div className="space-y-1">
                              {data.selectedSector.standardRefs.map((std, idx) => (
                                <Badge key={idx} variant="outline" className="mr-2">
                                  {std}
                                </Badge>
                              ))}
                            </div>
                          </div>

                          {data.selectedSector.hasSubsectors() && (
                            <div>
                              <h5 className="font-semibold mb-2">{t("governance.sector.subsectors")}</h5>
                              <div className="space-y-1">
                                {data.selectedSector.subsectorLabels.map((label, idx) => (
                                  <Badge key={idx} variant="outline" className="mr-2">
                                    {label}
                                  </Badge>
                                ))}
                              </div>
                            </div>
                          )}

                          {data.selectedSector.hasPrompts() && (
                            <div>
                              <h5 className="font-semibold mb-2">{t("governance.sector.prompts")}</h5>
                              <div className="space-y-1">
                                {data.selectedSector.prompts.map((prompt, idx) => (
                                  <Badge key={idx} variant="outline" className="mr-2">
                                    {prompt}
                                  </Badge>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                      </>
                    )}
                  </CardBody>
                </Card>

                {/* Frameworks FaaS vinculados */}
                {data.selectedFrameworks.length > 0 && (
                  <Card className="backdrop-blur-md bg-background/60 border-border/50">
                    <CardHeader>
                      <CardTitle>{t("governance.sector.frameworks")}</CardTitle>
                    </CardHeader>
                    <CardBody>
                      <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                          <thead>
                            <tr className="border-b">
                              <th className="text-left p-2">{t("governance.sector.frameworks")}</th>
                              <th className="text-left p-2">{t("common.version")}</th>
                              <th className="text-left p-2">{t("governance.sector.geoScope")}</th>
                              <th className="text-left p-2">{t("governance.sector.prompts")}</th>
                            </tr>
                          </thead>
                          <tbody>
                            {data.selectedFrameworks.map((framework, idx) => (
                              <tr key={idx} className="border-b hover:bg-muted/50">
                                <td className="p-2">{framework.configKey}</td>
                                <td className="p-2">{framework.version}</td>
                                <td className="p-2">{framework.geoScope}</td>
                                <td className="p-2">
                                  {framework.metadata?.prompts || "N/A"}
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    </CardBody>
                  </Card>
                )}

                {/* Políticas sectoriales */}
                {data.selectedPolicies.length > 0 && (
                  <Card className="backdrop-blur-md bg-background/60 border-border/50">
                    <CardHeader>
                      <CardTitle>{t("governance.sector.policies")}</CardTitle>
                    </CardHeader>
                    <CardBody>
                      <div className="overflow-x-auto">
                        <table className="w-full text-sm">
                          <thead>
                            <tr className="border-b">
                              <th className="text-left p-2">{t("common.code")}</th>
                              <th className="text-left p-2">{t("governance.sector.riskProfile")}</th>
                              <th className="text-left p-2">{t("governance.sector.regulationRefs")}</th>
                            </tr>
                          </thead>
                          <tbody>
                            {data.selectedPolicies.map((policy, idx) => (
                              <tr key={idx} className="border-b hover:bg-muted/50">
                                <td className="p-2">{policy.policyCode}</td>
                                <td className="p-2">
                                  <Badge className={getRiskColor(policy.riskProfile)}>
                                    {policy.riskProfile}
                                  </Badge>
                                </td>
                                <td className="p-2">
                                  {policy.activationConditions?.regulationRefs || "N/A"}
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                      </div>
                    </CardBody>
                  </Card>
                )}
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}


