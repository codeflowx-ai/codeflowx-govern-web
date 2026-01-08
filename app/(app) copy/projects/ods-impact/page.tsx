"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Leaf,
  Target,
  TrendingUp,
  CheckCircle2,
  AlertCircle,
} from "lucide-react";

interface ODSImpact {
  odsNumber: number;
  odsName: string;
  impactScore: number;
  projectsCount: number;
  status: "on-track" | "at-risk" | "behind";
  kpis: Array<{
    name: string;
    value: number;
    target: number;
    unit: string;
  }>;
}

const mockODSImpact: ODSImpact[] = [
  {
    odsNumber: 16,
    odsName: "Paz, Justicia e Instituciones Sólidas",
    impactScore: 0.92,
    projectsCount: 8,
    status: "on-track",
    kpis: [
      { name: "Tasa de Trazabilidad Completa", value: 95, target: 95, unit: "%" },
      { name: "Tasa de Certificación", value: 85, target: 80, unit: "%" },
      { name: "Score Promedio de Compliance", value: 0.88, target: 0.85, unit: "" },
    ],
  },
  {
    odsNumber: 9,
    odsName: "Industria, Innovación e Infraestructura",
    impactScore: 0.78,
    projectsCount: 12,
    status: "on-track",
    kpis: [
      { name: "Eficiencia de Recursos", value: 82, target: 80, unit: "%" },
      { name: "Reducción de Costos", value: 15, target: 10, unit: "%" },
    ],
  },
  {
    odsNumber: 12,
    odsName: "Producción y Consumo Responsables",
    impactScore: 0.65,
    projectsCount: 5,
    status: "at-risk",
    kpis: [
      { name: "Reducción de Desperdicios", value: 45, target: 50, unit: "%" },
      { name: "Uso de Recursos Renovables", value: 60, target: 70, unit: "%" },
    ],
  },
];

const getStatusColor = (status: string) => {
  switch (status) {
    case "on-track":
      return "primary";
    case "at-risk":
      return "secondary";
    case "behind":
      return "danger";
    default:
      return "outline";
  }
};

export default function ODSImpactPage() {
  const { t } = useTranslation();

  const totalProjects = mockODSImpact.reduce((sum, ods) => sum + ods.projectsCount, 0);
  const avgImpactScore = mockODSImpact.reduce((sum, ods) => sum + ods.impactScore, 0) / mockODSImpact.length;
  const onTrackODS = mockODSImpact.filter((ods) => ods.status === "on-track").length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Leaf className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.odsImpact.title", "Impacto en Objetivos de Desarrollo Sostenible")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.odsImpact.totalProjects", "Total Proyectos")}
              </CardTitle>
              <Target className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.odsImpact.contributing", "contribuyendo a ODS")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.odsImpact.avgImpactScore", "Score de Impacto Promedio")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{(avgImpactScore * 100).toFixed(1)}%</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.odsImpact.acrossAllODS", "en todos los ODS")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.odsImpact.onTrackODS", "ODS en Camino")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-500">{onTrackODS}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.odsImpact.ofTotal", "de {total}", { total: String(mockODSImpact.length) })}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockODSImpact.map((ods) => (
            <Card
              key={ods.odsNumber}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">
                      ODS {ods.odsNumber}: {ods.odsName}
                    </CardTitle>
                    <div className="flex items-center gap-2 flex-wrap">
                      <Badge variant={getStatusColor(ods.status) as any}>
                        {t(`projects.odsImpact.status.${ods.status}`, ods.status)}
                      </Badge>
                      <Badge variant="outline">
                        {ods.projectsCount} {t("projects.odsImpact.projects", "proyectos")}
                      </Badge>
                      <Badge variant="outline">
                        {(ods.impactScore * 100).toFixed(0)}% {t("projects.odsImpact.impact", "impacto")}
                      </Badge>
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-muted-foreground">
                      {t("projects.odsImpact.impactScore", "Score de Impacto")}
                    </span>
                    <span className="font-medium">{(ods.impactScore * 100).toFixed(1)}%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${ods.impactScore * 100}%` }}
                    />
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {ods.kpis.map((kpi, index) => (
                    <div key={index} className="space-y-2 p-3 rounded-lg bg-muted/50">
                      <div className="flex items-center justify-between">
                        <p className="text-sm font-medium">{kpi.name}</p>
                        {kpi.value >= kpi.target ? (
                          <CheckCircle2 className="w-4 h-4 text-green-500" />
                        ) : (
                          <AlertCircle className="w-4 h-4 text-yellow-500" />
                        )}
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-lg font-bold">
                          {kpi.value}
                          {kpi.unit}
                        </span>
                        <span className="text-sm text-muted-foreground">
                          {t("projects.odsImpact.target", "Meta")}: {kpi.target}
                          {kpi.unit}
                        </span>
                      </div>
                      <div className="w-full bg-muted rounded-full h-1.5">
                        <div
                          className={`h-1.5 rounded-full transition-all duration-500 ${
                            kpi.value >= kpi.target ? "bg-green-500" : "bg-yellow-500"
                          }`}
                          style={{ width: `${Math.min((kpi.value / kpi.target) * 100, 100)}%` }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}


