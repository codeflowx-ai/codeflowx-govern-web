"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  FileText,
  ChevronLeft,
  ChevronRight,
  CheckCircle,
  AlertTriangle,
  Send,
  Plus,
  X,
  Trash2,
  ArrowLeft,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";

// Tipos según especificaciones del prompt
interface FriaRisk {
  id: number;
  type: string;
  severity: "low" | "medium" | "high" | "critical";
  probability: number; // 0.0 - 1.0
  impact: "low" | "medium" | "high";
  description: string;
}

interface MitigationMeasure {
  id: number;
  type: "PREVENTIVE" | "DETECTIVE" | "CORRECTIVE";
  description: string;
  effectiveness: number; // 0.0 - 1.0
  associatedRiskId: number | null;
}

interface FriaData {
  friaId: number | null;
  projectId: number;
  projectName: string;
  currentStep: number;
  completenessScore: number;
  finalRisk: number | null;
  riskLevel: "low" | "medium" | "high" | "critical" | null;
  status: "DRAFT" | "COMPLETED" | "NOTIFIED";
  steps: {
    step1: {
      processDescription: string;
    };
    step2: {
      usagePeriodStart: string;
      usagePeriodEnd: string;
      usageFrequency: string;
    };
    step3: {
      affectedCategories: string[];
      vulnerableGroups: string;
      vulnerableGroupsIncluded: boolean;
    };
    step4: {
      risks: FriaRisk[];
    };
    step5: {
      humanOversight: string;
      hitlEnabled: boolean;
    };
    step6: {
      mitigationMeasures: MitigationMeasure[];
    };
  };
}

const mockData: FriaData = {
  friaId: null,
  projectId: 1001,
  projectName: "AI Credit Scoring System",
  currentStep: 1,
  completenessScore: 0.0,
  finalRisk: null,
  riskLevel: null,
  status: "DRAFT",
  steps: {
    step1: {
      processDescription: "",
    },
    step2: {
      usagePeriodStart: "",
      usagePeriodEnd: "",
      usageFrequency: "",
    },
    step3: {
      affectedCategories: [],
      vulnerableGroups: "",
      vulnerableGroupsIncluded: false,
    },
    step4: {
      risks: [],
    },
    step5: {
      humanOversight: "",
      hitlEnabled: false,
    },
    step6: {
      mitigationMeasures: [],
    },
  },
};

const riskTypes = [
  "Discrimination",
  "Privacy",
  "Transparency",
  "Autonomy",
  "Dignity",
  "Other",
];

export default function FriaAssessmentPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const [data, setData] = useState<FriaData>(mockData);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // Leer parámetros de la URL
      const friaIdParam = searchParams.get("friaId");
      const projectIdParam = searchParams.get("projectId");
      const baseFriaIdParam = searchParams.get("baseFriaId");

      if (friaIdParam) {
        // Cargar FRIA existente para editar
        const friaId = parseInt(friaIdParam, 10);
        // TODO: Reemplazar con llamada real a API
        await new Promise((resolve) => setTimeout(resolve, 500));
        setData((prev) => ({ ...prev, friaId }));
      } else if (projectIdParam) {
        // Nueva FRIA para un proyecto específico
        const projectId = parseInt(projectIdParam, 10);
        setData((prev) => ({ ...prev, projectId }));

        // Si hay baseFriaId, cargar los datos de la FRIA base para crear una versión
        if (baseFriaIdParam) {
          const baseFriaId = parseInt(baseFriaIdParam, 10);
          // TODO: Reemplazar con llamada real a API para cargar datos de la FRIA base
          await new Promise((resolve) => setTimeout(resolve, 500));
          // Aquí se deberían cargar los datos de la FRIA base y copiarlos al estado
          // Por ahora, solo marcamos que es una versión basada en otra
          console.log("Creating new version based on FRIA:", baseFriaId);
        }
      }
      // TODO: Reemplazar con llamada real a API para cargar datos completos
      await new Promise((resolve) => setTimeout(resolve, 500));
    } catch (error) {
      console.error("Error loading FRIA data:", error);
    } finally {
      setLoading(false);
    }
  };

  const saveStepData = async (stepNumber: number) => {
    if (!data.friaId) {
      // Crear FRIA si no existe
      const response = await fetch("/api/compliance/fria/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ projectId: data.projectId }),
      });
      const result = await response.json();
      if (result.success) {
        setData({ ...data, friaId: result.friaId });
      }
    }

    if (data.friaId) {
      setSaving(true);
      try {
        const response = await fetch(
          `/api/compliance/fria/${data.friaId}/step/${stepNumber}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ data: data.steps }),
          }
        );
        const result = await response.json();
        if (result.success) {
          setData({
            ...data,
            completenessScore: result.completenessScore || 0,
          });
        }
      } catch (error) {
        console.error("Error saving step:", error);
      } finally {
        setSaving(false);
      }
    }
  };

  const nextStep = async () => {
    if (data.currentStep < 6) {
      await saveStepData(data.currentStep);
      setData({ ...data, currentStep: data.currentStep + 1 });
    }
  };

  const previousStep = () => {
    if (data.currentStep > 1) {
      setData({ ...data, currentStep: data.currentStep - 1 });
    }
  };

  const calculateCompleteness = (): number => {
    let score = 0;

    // Step 1: Validar longitud mínima 100 caracteres (según auditoría)
    if (data.steps.step1.processDescription.length >= 100) {
      score += 1/6;
    } else if (data.steps.step1.processDescription.length >= 50) {
      score += 0.5/6; // Parcial
    }

    // Step 2: Validar ambos campos
    if (
      data.steps.step2.usagePeriodStart &&
      data.steps.step2.usagePeriodEnd &&
      data.steps.step2.usageFrequency
    ) {
      score += 1/6;
    }

    // Step 3: Validar categorías + flag vulnerable groups
    if (
      data.steps.step3.affectedCategories.length > 0 &&
      data.steps.step3.vulnerableGroupsIncluded !== undefined
    ) {
      score += 1/6;
    }

    // Step 4: Validar al menos 1 riesgo completo
    if (
      data.steps.step4.risks.length > 0 &&
      data.steps.step4.risks.every(
        (r) =>
          r.description &&
          r.severity &&
          r.probability !== undefined &&
          r.impact
      )
    ) {
      score += 1/6;
    }

    // Step 5: Validar descripción + flag HITL
    if (
      data.steps.step5.humanOversight.length >= 50 &&
      data.steps.step5.hitlEnabled !== undefined
    ) {
      score += 1/6;
    }

    // Step 6: Validar medidas con efectividad
    if (
      data.steps.step6.mitigationMeasures.length > 0 &&
      data.steps.step6.mitigationMeasures.every(
        (m) => m.description && m.effectiveness !== undefined && m.type
      )
    ) {
      score += 1/6;
    }

    return score;
  };

  const calculateFinalRisk = async () => {
    if (!data.friaId) return;

    try {
      const response = await fetch(
        `/api/compliance/fria/${data.friaId}/calculate-risk`,
        {
          method: "POST",
        }
      );
      const result = await response.json();
      if (result.success) {
        setData({
          ...data,
          finalRisk: result.finalRisk,
          riskLevel: result.riskLevel,
          status: "COMPLETED",
        });
      }
    } catch (error) {
      console.error("Error calculating risk:", error);
    }
  };

  const notifyAuthority = async () => {
    if (!data.friaId) return;

    try {
      const response = await fetch(
        `/api/compliance/fria/${data.friaId}/notify-authority`,
        {
          method: "POST",
        }
      );
      const result = await response.json();
      if (result.success) {
        setData({ ...data, status: "NOTIFIED" });
      }
    } catch (error) {
      console.error("Error notifying authority:", error);
    }
  };

  const [crossValidation, setCrossValidation] = useState<{
    consistencyScore: number | null;
    inconsistencies: Array<{
      type: string;
      severity: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
      description: string;
    }>;
    loading: boolean;
  }>({
    consistencyScore: null,
    inconsistencies: [],
    loading: false,
  });

  const validateAgainstTechnicalMetrics = async () => {
    if (!data.friaId) return;

    setCrossValidation({ ...crossValidation, loading: true });
    try {
      const response = await fetch(
        `/api/compliance/fria/${data.friaId}/cross-validate`,
        {
          method: "POST",
        }
      );
      const result = await response.json();
      if (result.success) {
        setCrossValidation({
          consistencyScore: result.consistencyScore,
          inconsistencies: result.inconsistencies || [],
          loading: false,
        });

        // Si score < 0.70, mostrar alerta crítica
        if (result.consistencyScore < 0.70) {
          alert(
            `CRÍTICO: FRIA no coincide con métricas técnicas reales.\n` +
              `Score de consistencia: ${(result.consistencyScore * 100).toFixed(0)}%\n` +
              `Mínimo requerido: 70%\n\n` +
              `Se requiere justificación para continuar.`
          );
        }
      }
    } catch (error) {
      console.error("Error validating against technical metrics:", error);
      setCrossValidation({ ...crossValidation, loading: false });
    }
  };

  const addRisk = () => {
    const newRisk: FriaRisk = {
      id: Date.now(),
      type: "Discrimination",
      severity: "medium",
      probability: 0.5,
      impact: "medium",
      description: "",
    };
    setData({
      ...data,
      steps: {
        ...data.steps,
        step4: {
          risks: [...data.steps.step4.risks, newRisk],
        },
      },
    });
  };

  const removeRisk = (riskId: number) => {
    setData({
      ...data,
      steps: {
        ...data.steps,
        step4: {
          risks: data.steps.step4.risks.filter((r) => r.id !== riskId),
        },
      },
    });
  };

  const updateRisk = (riskId: number, field: keyof FriaRisk, value: any) => {
    setData({
      ...data,
      steps: {
        ...data.steps,
        step4: {
          risks: data.steps.step4.risks.map((r) =>
            r.id === riskId ? { ...r, [field]: value } : r
          ),
        },
      },
    });
  };

  const addMitigationMeasure = () => {
    const newMeasure: MitigationMeasure = {
      id: Date.now(),
      type: "PREVENTIVE",
      description: "",
      effectiveness: 0.5,
      associatedRiskId: null,
    };
    setData({
      ...data,
      steps: {
        ...data.steps,
        step6: {
          mitigationMeasures: [
            ...data.steps.step6.mitigationMeasures,
            newMeasure,
          ],
        },
      },
    });
  };

  const removeMitigationMeasure = (measureId: number) => {
    setData({
      ...data,
      steps: {
        ...data.steps,
        step6: {
          mitigationMeasures: data.steps.step6.mitigationMeasures.filter(
            (m) => m.id !== measureId
          ),
        },
      },
    });
  };

  const updateMitigationMeasure = (
    measureId: number,
    field: keyof MitigationMeasure,
    value: any
  ) => {
    setData({
      ...data,
      steps: {
        ...data.steps,
        step6: {
          mitigationMeasures: data.steps.step6.mitigationMeasures.map((m) =>
            m.id === measureId ? { ...m, [field]: value } : m
          ),
        },
      },
    });
  };

  const renderStepContent = () => {
    switch (data.currentStep) {
      case 1:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step1.title")} (Art. 27.1.a)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step1.description")}
              </p>
              <Textarea
                value={data.steps.step1.processDescription}
                onChange={(e) =>
                  setData({
                    ...data,
                    steps: {
                      ...data.steps,
                      step1: { processDescription: e.target.value },
                    },
                  })
                }
                rows={8}
                placeholder={t("governance.compliance.fria.steps.step1.placeholder")}
                minLength={50}
              />
              {data.steps.step1.processDescription.length > 0 &&
                data.steps.step1.processDescription.length < 50 && (
                  <p className="text-sm text-yellow-600 mt-1">
                    {t("governance.compliance.fria.minCharactersRequired", undefined, { count: "50" })}
                  </p>
                )}
            </div>
          </div>
        );

      case 2:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step2.title")} (Art. 27.1.b)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step2.description")}
              </p>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <Label>{t("governance.compliance.fria.steps.step2.usagePeriod")} - {t("governance.compliance.fria.start")}</Label>
                <Input
                  type="date"
                  value={data.steps.step2.usagePeriodStart}
                  onChange={(e) =>
                    setData({
                      ...data,
                      steps: {
                        ...data.steps,
                        step2: {
                          ...data.steps.step2,
                          usagePeriodStart: e.target.value,
                        },
                      },
                    })
                  }
                />
              </div>
              <div>
                <Label>{t("governance.compliance.fria.steps.step2.usagePeriod")} - {t("governance.compliance.fria.end")}</Label>
                <Input
                  type="date"
                  value={data.steps.step2.usagePeriodEnd}
                  onChange={(e) =>
                    setData({
                      ...data,
                      steps: {
                        ...data.steps,
                        step2: {
                          ...data.steps.step2,
                          usagePeriodEnd: e.target.value,
                        },
                      },
                    })
                  }
                />
              </div>
            </div>
            <div>
              <Label>{t("governance.compliance.fria.steps.step2.usageFrequency")}</Label>
              <Select
                value={data.steps.step2.usageFrequency}
                onValueChange={(value) =>
                  setData({
                    ...data,
                    steps: {
                      ...data.steps,
                      step2: {
                        ...data.steps.step2,
                        usageFrequency: value,
                      },
                    },
                  })
                }
              >
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.compliance.fria.selectFrequency")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="daily">
                    {t("governance.compliance.fria.steps.step2.frequencyOptions.daily")}
                  </SelectItem>
                  <SelectItem value="weekly">
                    {t("governance.compliance.fria.steps.step2.frequencyOptions.weekly")}
                  </SelectItem>
                  <SelectItem value="monthly">
                    {t("governance.compliance.fria.steps.step2.frequencyOptions.monthly")}
                  </SelectItem>
                  <SelectItem value="continuous">
                    {t(
                      "governance.compliance.fria.steps.step2.frequencyOptions.continuous"
                    )}
                  </SelectItem>
                  <SelectItem value="other">
                    {t("governance.compliance.fria.steps.step2.frequencyOptions.other")}
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        );

      case 3:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step3.title")} (Art. 27.1.c)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step3.description")}
              </p>
            </div>
            <div>
              <Label>{t("governance.compliance.fria.steps.step3.affectedCategories")}</Label>
              <div className="space-y-2 mt-2">
                {data.steps.step3.affectedCategories.map((category, index) => (
                  <div
                    key={index}
                    className="flex items-center gap-2 p-2 border rounded"
                  >
                    <Input
                      value={category}
                      onChange={(e) => {
                        const updated = [...data.steps.step3.affectedCategories];
                        updated[index] = e.target.value;
                        setData({
                          ...data,
                          steps: {
                            ...data.steps,
                            step3: { ...data.steps.step3, affectedCategories: updated },
                          },
                        });
                      }}
                    />
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => {
                        const updated = data.steps.step3.affectedCategories.filter(
                          (_, i) => i !== index
                        );
                        setData({
                          ...data,
                          steps: {
                            ...data.steps,
                            step3: { ...data.steps.step3, affectedCategories: updated },
                          },
                        });
                      }}
                    >
                      <X className="h-4 w-4" />
                    </Button>
                  </div>
                ))}
                <Button
                  variant="outline"
                  onClick={() => {
                    setData({
                      ...data,
                      steps: {
                        ...data.steps,
                        step3: {
                          ...data.steps.step3,
                          affectedCategories: [
                            ...data.steps.step3.affectedCategories,
                            "",
                          ],
                        },
                      },
                    });
                  }}
                >
                  <Plus className="h-4 w-4 mr-2" />
                  {t("governance.compliance.fria.steps.step3.addCategory")}
                </Button>
              </div>
            </div>
            <div>
              <div className="flex items-center gap-2 mb-2">
                <input
                  type="checkbox"
                  id="vulnerableGroupsIncluded"
                  checked={data.steps.step3.vulnerableGroupsIncluded}
                  onChange={(e) =>
                    setData({
                      ...data,
                      steps: {
                        ...data.steps,
                        step3: {
                          ...data.steps.step3,
                          vulnerableGroupsIncluded: e.target.checked,
                        },
                      },
                    })
                  }
                  className="w-4 h-4"
                />
                <Label htmlFor="vulnerableGroupsIncluded">
                  {t("governance.compliance.fria.vulnerableGroupsAffected")}
                </Label>
              </div>
              {data.steps.step3.vulnerableGroupsIncluded && (
                <div>
                  <Label>{t("governance.compliance.fria.steps.step3.vulnerableGroups")}</Label>
                  <Textarea
                    value={data.steps.step3.vulnerableGroups}
                    onChange={(e) =>
                      setData({
                        ...data,
                        steps: {
                          ...data.steps,
                          step3: {
                            ...data.steps.step3,
                            vulnerableGroups: e.target.value,
                          },
                        },
                      })
                    }
                    rows={3}
                    placeholder={t("governance.compliance.fria.vulnerableGroupsExample")}
                  />
                </div>
              )}
            </div>
          </div>
        );

      case 4:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step4.title")} (Art. 27.1.d)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step4.description")}
              </p>
            </div>
            <div className="space-y-4">
              {data.steps.step4.risks.map((risk) => (
                <Card key={risk.id} className="p-4">
                  <div className="space-y-4">
                    <div className="flex justify-between items-center">
                      <h4 className="font-semibold">{t("governance.compliance.fria.riskNumber", undefined, { id: String(risk.id) })}</h4>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => removeRisk(risk.id)}
                      >
                        <Trash2 className="h-4 w-4 text-red-500" />
                      </Button>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step4.riskType")}</Label>
                        <Select
                          value={risk.type}
                          onValueChange={(value) =>
                            updateRisk(risk.id, "type", value)
                          }
                        >
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            {riskTypes.map((type) => (
                              <SelectItem key={type} value={type}>
                                {type}
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      </div>
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step4.severity")}</Label>
                        <Select
                          value={risk.severity}
                          onValueChange={(value: any) =>
                            updateRisk(risk.id, "severity", value)
                          }
                        >
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="low">
                              {t("governance.compliance.fria.steps.step4.severityOptions.low")}
                            </SelectItem>
                            <SelectItem value="medium">
                              {t("governance.compliance.fria.steps.step4.severityOptions.medium")}
                            </SelectItem>
                            <SelectItem value="high">
                              {t("governance.compliance.fria.steps.step4.severityOptions.high")}
                            </SelectItem>
                            <SelectItem value="critical">
                              {t(
                                "governance.compliance.fria.steps.step4.severityOptions.critical"
                              )}
                            </SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step4.probability")}</Label>
                        <Input
                          type="number"
                          min="0"
                          max="1"
                          step="0.01"
                          value={risk.probability}
                          onChange={(e) =>
                            updateRisk(risk.id, "probability", parseFloat(e.target.value))
                          }
                        />
                      </div>
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step4.impact")}</Label>
                        <Select
                          value={risk.impact}
                          onValueChange={(value: any) =>
                            updateRisk(risk.id, "impact", value)
                          }
                        >
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="low">
                              {t("governance.compliance.fria.steps.step4.impactOptions.low")}
                            </SelectItem>
                            <SelectItem value="medium">
                              {t("governance.compliance.fria.steps.step4.impactOptions.medium")}
                            </SelectItem>
                            <SelectItem value="high">
                              {t("governance.compliance.fria.steps.step4.impactOptions.high")}
                            </SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                    <div>
                      <Label>{t("governance.compliance.fria.steps.step4.description")}</Label>
                      <Textarea
                        value={risk.description}
                        onChange={(e) =>
                          updateRisk(risk.id, "description", e.target.value)
                        }
                        rows={3}
                      />
                    </div>
                  </div>
                </Card>
              ))}
              <Button variant="outline" onClick={addRisk}>
                <Plus className="h-4 w-4 mr-2" />
                {t("governance.compliance.fria.steps.step4.addRisk")}
              </Button>
            </div>
          </div>
        );

      case 5:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step5.title")} (Art. 27.1.e)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step5.description")}
              </p>
              <div className="flex items-center gap-2 mb-4">
                <input
                  type="checkbox"
                  id="hitlEnabled"
                  checked={data.steps.step5.hitlEnabled}
                  onChange={(e) =>
                    setData({
                      ...data,
                      steps: {
                        ...data.steps,
                        step5: {
                          ...data.steps.step5,
                          hitlEnabled: e.target.checked,
                        },
                      },
                    })
                  }
                  className="w-4 h-4"
                />
                <Label htmlFor="hitlEnabled">
                  {t("governance.compliance.fria.hitlEnabled")}
                </Label>
              </div>
              <Textarea
                value={data.steps.step5.humanOversight}
                onChange={(e) =>
                  setData({
                    ...data,
                    steps: {
                      ...data.steps,
                      step5: { ...data.steps.step5, humanOversight: e.target.value },
                    },
                  })
                }
                rows={8}
                placeholder={t("governance.compliance.fria.steps.step5.placeholder")}
                minLength={50}
              />
              {data.steps.step5.humanOversight.length > 0 &&
                data.steps.step5.humanOversight.length < 50 && (
                  <p className="text-sm text-yellow-600 mt-1">
                    {t("governance.compliance.fria.minCharactersRequired", undefined, { count: "50" })}
                  </p>
                )}
            </div>
          </div>
        );

      case 6:
        return (
          <div className="space-y-4">
            <div>
              <Label>
                {t("governance.compliance.fria.steps.step6.title")} (Art. 27.1.f)
              </Label>
              <p className="text-sm text-muted-foreground mb-2">
                {t("governance.compliance.fria.steps.step6.description")}
              </p>
            </div>
            <div className="space-y-4">
              {data.steps.step6.mitigationMeasures.map((measure) => (
                <Card key={measure.id} className="p-4">
                  <div className="space-y-4">
                    <div className="flex justify-between items-center">
                      <h4 className="font-semibold">{t("governance.compliance.fria.measureNumber", undefined, { id: String(measure.id) })}</h4>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => removeMitigationMeasure(measure.id)}
                      >
                        <Trash2 className="h-4 w-4 text-red-500" />
                      </Button>
                    </div>
                    <div>
                      <Label>{t("governance.compliance.fria.steps.step6.description")}</Label>
                      <Textarea
                        value={measure.description}
                        onChange={(e) =>
                          updateMitigationMeasure(measure.id, "description", e.target.value)
                        }
                        rows={3}
                      />
                    </div>
                    <div className="grid grid-cols-3 gap-4">
                      <div>
                        <Label>{t("governance.compliance.fria.measureType")}</Label>
                        <Select
                          value={measure.type}
                          onValueChange={(value: any) =>
                            updateMitigationMeasure(measure.id, "type", value)
                          }
                        >
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="PREVENTIVE">{t("governance.compliance.fria.measureTypePreventive")}</SelectItem>
                            <SelectItem value="DETECTIVE">{t("governance.compliance.fria.measureTypeDetective")}</SelectItem>
                            <SelectItem value="CORRECTIVE">{t("governance.compliance.fria.measureTypeCorrective")}</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step6.effectiveness")}</Label>
                        <Input
                          type="number"
                          min="0"
                          max="1"
                          step="0.01"
                          value={measure.effectiveness}
                          onChange={(e) =>
                            updateMitigationMeasure(
                              measure.id,
                              "effectiveness",
                              parseFloat(e.target.value)
                            )
                          }
                        />
                      </div>
                      <div>
                        <Label>{t("governance.compliance.fria.steps.step6.associatedRisk")}</Label>
                        <Select
                          value={measure.associatedRiskId?.toString() || ""}
                          onValueChange={(value) =>
                            updateMitigationMeasure(
                              measure.id,
                              "associatedRiskId",
                              value ? parseInt(value) : null
                            )
                          }
                        >
                          <SelectTrigger>
                            <SelectValue placeholder={t("governance.compliance.fria.selectRisk")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="">{t("governance.compliance.fria.none")}</SelectItem>
                            {data.steps.step4.risks.map((risk) => (
                              <SelectItem key={risk.id} value={risk.id.toString()}>
                                {risk.type} - {risk.severity}
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                  </div>
                </Card>
              ))}
              <Button variant="outline" onClick={addMitigationMeasure}>
                <Plus className="h-4 w-4 mr-2" />
                {t("governance.compliance.fria.steps.step6.addMeasure")}
              </Button>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  const completenessScore = calculateCompleteness();
  const progressPercentage = (data.currentStep / 6) * 100;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-1/3 right-1/4 w-1.5 h-1.5 bg-blue-300/20 rounded-full animate-pulse delay-75" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => {
              window.location.href = "/governance/compliance/fria/projects";
            }}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("governance.compliance.fria.back")}
          </Button>
          <div className="text-center space-y-2 flex-1">
            <div className="flex items-center justify-center gap-3 mb-4">
              <FileText className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                {t("governance.compliance.fria.title")}
              </h1>
            </div>
            <p className="text-muted-foreground">{t("governance.compliance.fria.subtitle")}</p>
          </div>
          <div className="w-[120px]"></div> {/* Spacer para centrar */}
        </div>

        {/* Progress Bar */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody>
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium">
                {t("governance.compliance.fria.stepOf", undefined, { current: String(data.currentStep), total: "6" })}
              </span>
              <span className="text-sm text-muted-foreground">
                {t("governance.compliance.fria.completeness")}:{" "}
                {(completenessScore * 100).toFixed(0)}%
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-blue-500 h-2 rounded-full transition-all duration-300"
                style={{ width: `${progressPercentage}%` }}
              />
            </div>
          </CardBody>
        </Card>

        {/* Project Information */}
        <Card>
          <CardHeader>
            <CardTitle>{t("governance.compliance.fria.projectInformation")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="flex items-center gap-4">
              <div>
                <Label>{t("governance.compliance.fria.projectName")}</Label>
                <div className="font-semibold">{data.projectName}</div>
              </div>
              <div>
                <Label>{t("governance.compliance.fria.projectId")}</Label>
                <div className="font-semibold">{data.projectId}</div>
              </div>
              {data.finalRisk !== null && (
                <div>
                  <Label>{t("governance.compliance.fria.risk")}</Label>
                  <Badge
                    variant={
                      data.riskLevel === "critical" || data.riskLevel === "high"
                        ? "danger"
                        : "primary"
                    }
                    className="text-lg px-4 py-2"
                  >
                    {(data.finalRisk * 100).toFixed(0)}% -{" "}
                    {data.riskLevel &&
                      t(`governance.compliance.fria.riskLevels.${data.riskLevel}`)}
                  </Badge>
                </div>
              )}
            </div>
          </CardBody>
        </Card>

        {/* Step Indicators */}
        <div className="flex items-center justify-center gap-2 flex-wrap">
          {[1, 2, 3, 4, 5, 6].map((step) => (
            <div
              key={step}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg transition-all ${
                data.currentStep === step
                  ? "bg-primary text-primary-foreground"
                  : data.currentStep > step
                  ? "bg-primary/20"
                  : "bg-muted"
              }`}
            >
              {data.currentStep > step && <CheckCircle className="h-4 w-4" />}
              <span className="text-sm font-medium">{step}</span>
            </div>
          ))}
        </div>

        {/* Wizard Content */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("governance.compliance.fria.step")} {data.currentStep}:{" "}
              {t(`governance.compliance.fria.steps.step${data.currentStep}.title`)}
            </CardTitle>
          </CardHeader>
          <CardBody>{renderStepContent()}</CardBody>
        </Card>

        {/* Validación Cruzada con Métricas Técnicas (INC-007) */}
        {data.currentStep === 6 && crossValidation.consistencyScore !== null && (
          <Card className={crossValidation.consistencyScore < 0.70 ? "border-red-500" : "border-green-500"}>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertTriangle
                  className={
                    crossValidation.consistencyScore < 0.70
                      ? "h-5 w-5 text-red-500"
                      : "h-5 w-5 text-green-500"
                  }
                />
                {t("governance.compliance.fria.crossValidation")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div>
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm font-medium">{t("governance.compliance.fria.consistencyScore")}</span>
                    <Badge
                      variant={
                        crossValidation.consistencyScore < 0.70
                          ? "danger"
                          : "primary"
                      }
                      className="text-lg"
                    >
                      {(crossValidation.consistencyScore * 100).toFixed(0)}%
                    </Badge>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div
                      className={`h-2 rounded-full transition-all ${
                        crossValidation.consistencyScore < 0.70
                          ? "bg-red-500"
                          : "bg-green-500"
                      }`}
                      style={{
                        width: `${crossValidation.consistencyScore * 100}%`,
                      }}
                    />
                  </div>
                  {crossValidation.consistencyScore < 0.70 && (
                    <p className="text-sm text-red-600 mt-2">
                      {t("governance.compliance.fria.criticalScoreBelow")}
                    </p>
                  )}
                </div>
                {crossValidation.inconsistencies.length > 0 && (
                  <div>
                    <Label className="mb-2">{t("governance.compliance.fria.inconsistenciesDetected")}</Label>
                    <div className="space-y-2">
                      {crossValidation.inconsistencies.map((inc, index) => (
                        <div
                          key={index}
                          className={`p-3 rounded border ${
                            inc.severity === "CRITICAL" || inc.severity === "HIGH"
                              ? "border-red-500 bg-red-50"
                              : "border-yellow-500 bg-yellow-50"
                          }`}
                        >
                          <div className="flex items-center gap-2 mb-1">
                            <Badge
                              variant={
                                inc.severity === "CRITICAL" || inc.severity === "HIGH"
                                  ? "danger"
                                  : "secondary"
                              }
                            >
                              {inc.severity}
                            </Badge>
                            <span className="font-medium">{inc.type}</span>
                          </div>
                          <p className="text-sm">{inc.description}</p>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        )}

        {/* Navigation */}
        <div className="flex justify-between">
          <Button
            variant="outline"
            onClick={previousStep}
            disabled={data.currentStep === 1}
          >
            <ChevronLeft className="h-4 w-4 mr-2" />
            {t("governance.compliance.fria.previous")}
          </Button>
          <div className="flex gap-2">
            {data.currentStep === 6 ? (
              <>
                <Button variant="outline" onClick={calculateFinalRisk}>
                  {t("governance.compliance.fria.calculateRisk")}
                </Button>
                <Button
                  variant="outline"
                  onClick={validateAgainstTechnicalMetrics}
                  disabled={crossValidation.loading || !data.friaId}
                >
                  {crossValidation.loading
                    ? t("governance.compliance.fria.validating")
                    : t("governance.compliance.fria.validateTechnicalMetrics")}
                </Button>
                {data.finalRisk !== null && data.finalRisk >= 0.75 && (
                  <Button variant="outline" onClick={notifyAuthority}>
                    <Send className="h-4 w-4 mr-2" />
                    {t("governance.compliance.fria.notifyAuthority")}
                  </Button>
                )}
                <Button onClick={async () => await saveStepData(6)}>
                  {t("governance.compliance.fria.completeFRIA")}
                </Button>
              </>
            ) : (
              <Button onClick={nextStep} disabled={saving}>
                {t("governance.compliance.fria.next")}
                <ChevronRight className="h-4 w-4 ml-2" />
              </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
