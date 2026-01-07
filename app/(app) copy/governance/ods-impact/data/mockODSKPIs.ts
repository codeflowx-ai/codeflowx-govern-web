// ⚠️ MOCK DATA PARA DESARROLLO ⚠️
//
// IMPORTANTE: Estos datos son inventados para desarrollo del frontend.
//
// 📊 FACTIBILIDAD DE KPIs (DESPUÉS DE FASE 1 Y FASE 2):
// - ✅ 29 KPIs (85%) son TOTALMENTE FACTIBLES con tablas existentes
// - ⚠️ 1 KPI (3%) es PARCIALMENTE FACTIBLE (requiere ajustes)
// - ❌ 4 KPIs (12%) NO son factibles (requieren Fase 3 - Marketplace)
//
// 📋 TABLAS REALES DISPONIBLES:
// - IMLIMMUTABLELOGS, PRJPROJECTS, GOVQUALITYMANAGEMENTSYSTEMS
// - COMCOMPLIANCEASSESSMENTS, REGEUREGISTRATIONS
// - GOVHITLDECISIONS, GOVHITLSUPERVISIONS
// - FRIAFUNDAMENTALRIGHTSASSESSMENTS, GOVAIAACTTECHNICALDOCS
// - GOVPROHIBITEDSYSTEMS, PRMPROMPTS, GOVINCIDENTS
// - DSDDATASETS (Fase 1), TELRESOURCETELEMETRY (Fase 2)
// - PRJAGENTS (Fase 2), ORGORGANIZATIONS (Fase 2), RAGPIPELINES (Fase 2)
//
// 📋 TABLAS PENDIENTES (Fase 3 - Marketplace):
// - Marketplace de agentes y recursos compartidos
//
// Ver documentación completa:
// - docs/prompts/compliance/ODS_IMPACT_SQL_QUERIES.md (queries SQL)
// - docs/prompts/compliance/ODS_IMPACT_FEASIBILITY_ANALYSIS.md (análisis de factibilidad)
//
// Mock data para KPIs ODS

export interface ODSKPI {
  code: string;
  name: string;
  value: number;
  unit: string;
  meta: number;
  criticalThreshold: number;
  status: "good" | "warning" | "critical";
  description: string;
  trend?: "up" | "down" | "stable";
  trendValue?: number;
  frequency: "realtime" | "daily" | "weekly" | "monthly" | "quarterly";
  sources: string[];
}

export interface ODSImpact {
  odsNumber: number;
  odsName: string;
  score: number; // 0-100
  trend: "up" | "down" | "stable";
  modulesCount: number;
  status: "good" | "warning" | "critical";
  kpis: ODSKPI[];
}

export interface CompositeKPI {
  name: string;
  value: number;
  meta: number;
  status: "good" | "warning" | "critical";
  components: {
    name: string;
    weight: number;
    value: number;
  }[];
}

export interface ODSImpactDashboard {
  compositeKPIs: {
    overallImpact: CompositeKPI;
    complianceRate: CompositeKPI;
  };
  odsImpacts: ODSImpact[];
  topCriticalKPIs: ODSKPI[];
  evolution: {
    date: string;
    overallScore: number;
    complianceRate: number;
  }[];
}

// Mock data completo
export const mockODSImpactDashboard: ODSImpactDashboard = {
  compositeKPIs: {
    overallImpact: {
      name: "Score General de Impacto ODS",
      value: 0.82,
      meta: 0.85,
      status: "warning",
      components: [
        { name: "ODS 16", weight: 0.30, value: 0.88 },
        { name: "ODS 9", weight: 0.25, value: 0.85 },
        { name: "ODS 10", weight: 0.20, value: 0.78 },
        { name: "ODS 5", weight: 0.10, value: 0.80 },
        { name: "Otros ODS", weight: 0.15, value: 0.75 },
      ],
    },
    complianceRate: {
      name: "Tasa de Cumplimiento Normativo",
      value: 0.87,
      meta: 0.90,
      status: "warning",
      components: [
        { name: "Trazabilidad", weight: 0.15, value: 0.95 },
        { name: "QMS", weight: 0.20, value: 0.85 },
        { name: "Clasificación", weight: 0.10, value: 0.90 },
        { name: "HITL", weight: 0.10, value: 0.98 },
        { name: "FRIA", weight: 0.15, value: 0.85 },
        { name: "Conformidad", weight: 0.15, value: 0.80 },
        { name: "Registro UE", weight: 0.10, value: 0.95 },
        { name: "Sistemas Prohibidos", weight: 0.05, value: 1.0 },
      ],
    },
  },
  odsImpacts: [
    {
      odsNumber: 16,
      odsName: "Paz, Justicia e Instituciones Sólidas",
      score: 88.5,
      trend: "up",
      modulesCount: 18,
      status: "good",
      kpis: [
        {
          code: "KPI 16.1",
          name: "Tasa de Trazabilidad Completa",
          value: 92.5,
          unit: "%",
          meta: 95,
          criticalThreshold: 80,
          status: "good",
          description: "Porcentaje de sistemas/activos con trazabilidad completa",
          trend: "up",
          trendValue: 2.5,
          frequency: "daily",
          sources: ["IMLIMMUTABLELOGS", "PRJPROJECTS", "MODMODELS", "AGTAGENTS"],
        },
        {
          code: "KPI 16.2",
          name: "Tasa de Certificación de Sistemas",
          value: 78.0,
          unit: "%",
          meta: 80,
          criticalThreshold: 60,
          status: "warning",
          description: "Porcentaje de sistemas certificados según EU AI Act",
          trend: "up",
          trendValue: 3.0,
          frequency: "weekly",
          sources: ["COMCOMPLIANCEASSESSMENTS", "PRJPROJECTS"],
        },
        {
          code: "KPI 16.3",
          name: "Score Promedio de Compliance",
          value: 0.85,
          unit: "",
          meta: 0.85,
          criticalThreshold: 0.70,
          status: "good",
          description: "Score promedio de compliance QMS (0.00 - 1.00)",
          trend: "stable",
          frequency: "daily",
          sources: ["GOVQUALITYMANAGEMENTSYSTEMS"],
        },
        {
          code: "KPI 16.4",
          name: "Tasa de Registro en BD UE",
          value: 95.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 90,
          status: "good",
          description: "Porcentaje de sistemas registrados en Base de Datos UE",
          trend: "up",
          trendValue: 5.0,
          frequency: "weekly",
          sources: ["REGEUREGISTRATIONS", "PRJPROJECTS"],
        },
        {
          code: "KPI 16.5",
          name: "Tasa de Aprobación Humana (HITL)",
          value: 98.5,
          unit: "%",
          meta: 100,
          criticalThreshold: 95,
          status: "good",
          description: "Porcentaje de decisiones críticas con supervisión humana",
          trend: "up",
          trendValue: 1.5,
          frequency: "daily",
          sources: ["GOVHITLDECISIONS", "GOVHITLSUPERVISIONS"],
        },
        {
          code: "KPI 16.6",
          name: "Tiempo Promedio de Auditoría",
          value: 18.5,
          unit: "horas",
          meta: 24,
          criticalThreshold: 72,
          status: "good",
          description: "Tiempo promedio para completar auditoría de decisiones",
          trend: "down",
          trendValue: -2.5,
          frequency: "daily",
          sources: ["IMLIMMUTABLELOGS"],
        },
      ],
    },
    {
      odsNumber: 9,
      odsName: "Industria, Innovación e Infraestructura",
      score: 85.0,
      trend: "up",
      modulesCount: 13,
      status: "good",
      kpis: [
        {
          code: "KPI 9.1",
          name: "Score QMS Promedio",
          value: 0.85,
          unit: "",
          meta: 0.85,
          criticalThreshold: 0.70,
          status: "good",
          description: "Score promedio del Sistema de Gestión de Calidad",
          trend: "stable",
          frequency: "daily",
          sources: ["GOVQUALITYMANAGEMENTSYSTEMS"],
        },
        {
          code: "KPI 9.2",
          name: "Tasa de Documentación Completa",
          value: 88.0,
          unit: "%",
          meta: 90,
          criticalThreshold: 75,
          status: "good",
          description: "Porcentaje de sistemas con documentación técnica completa",
          trend: "up",
          trendValue: 3.0,
          frequency: "weekly",
          sources: ["GOVAIAACTTECHNICALDOCS", "MODMODELS"],
        },
        {
          code: "KPI 9.3",
          name: "Tiempo Promedio de Certificación",
          value: 28.0,
          unit: "días",
          meta: 30,
          criticalThreshold: 60,
          status: "good",
          description: "Tiempo promedio desde inicio hasta certificación",
          trend: "down",
          trendValue: -2.0,
          frequency: "weekly",
          sources: ["COMCOMPLIANCEASSESSMENTS"],
        },
        {
          code: "KPI 9.4",
          name: "Tasa de Reutilización de Agentes",
          value: 65.0,
          unit: "%",
          meta: 60,
          criticalThreshold: 40,
          status: "good",
          description: "Porcentaje de agentes reutilizados vs. nuevos",
          trend: "up",
          trendValue: 5.0,
          frequency: "monthly",
          sources: ["AGTAGENTS", "AIOCOMPONENTS"],
        },
        {
          code: "KPI 9.5",
          name: "Tasa de Reutilización de Prompts",
          value: 72.0,
          unit: "%",
          meta: 70,
          criticalThreshold: 50,
          status: "good",
          description: "Porcentaje de prompts reutilizados vs. nuevos",
          trend: "up",
          trendValue: 4.0,
          frequency: "monthly",
          sources: ["PRMPROMPTS"],
        },
        {
          code: "KPI 9.6",
          name: "Tiempo Promedio de Ciclo MLOps",
          value: 6.5,
          unit: "días",
          meta: 7,
          criticalThreshold: 14,
          status: "good",
          description: "Tiempo promedio de entrenamiento-despliegue",
          trend: "down",
          trendValue: -0.5,
          frequency: "weekly",
          sources: ["MODMODELS", "AIOCOMPONENTS"],
        },
        {
          code: "KPI 9.7",
          name: "Tasa de Adopción de LLMs Open Source",
          value: 42.0,
          unit: "%",
          meta: 40,
          criticalThreshold: 20,
          status: "good",
          description: "Porcentaje de uso de LLMs open source vs. propietarios",
          trend: "up",
          trendValue: 5.0,
          frequency: "monthly",
          sources: ["AIOCOMPONENTS", "Telemetría"],
        },
      ],
    },
    {
      odsNumber: 10,
      odsName: "Reducción de las Desigualdades",
      score: 78.0,
      trend: "up",
      modulesCount: 8,
      status: "warning",
      kpis: [
        {
          code: "KPI 10.1",
          name: "Tasa de FRIA Completada",
          value: 95.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 90,
          status: "warning",
          description: "Porcentaje de sistemas de alto riesgo con FRIA completada",
          trend: "up",
          trendValue: 2.0,
          frequency: "weekly",
          sources: ["FRIAFUNDAMENTALRIGHTSASSESSMENTS", "PRJPROJECTS"],
        },
        {
          code: "KPI 10.2",
          name: "Riesgo Promedio en FRIA",
          value: 0.48,
          unit: "",
          meta: 0.50,
          criticalThreshold: 0.75,
          status: "good",
          description: "Riesgo promedio calculado en evaluaciones FRIA",
          trend: "down",
          trendValue: -0.02,
          frequency: "weekly",
          sources: ["FRIAFUNDAMENTALRIGHTSASSESSMENTS"],
        },
        {
          code: "KPI 10.3",
          name: "Tasa de Detección de Sesgos en Datasets",
          value: 88.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 80,
          status: "warning",
          description: "Porcentaje de datasets con análisis de sesgos completado",
          trend: "up",
          trendValue: 3.0,
          frequency: "monthly",
          sources: ["DSDDATASETS", "Análisis de sesgos"],
        },
        {
          code: "KPI 10.4",
          name: "Score de Representatividad de Datasets",
          value: 0.78,
          unit: "",
          meta: 0.80,
          criticalThreshold: 0.60,
          status: "warning",
          description: "Score promedio de representatividad de grupos en datasets",
          trend: "up",
          trendValue: 0.02,
          frequency: "monthly",
          sources: ["DSDDATASETS"],
        },
        {
          code: "KPI 10.5",
          name: "Tasa de Sistemas Prohibidos Detectados",
          value: 0,
          unit: "",
          meta: 0,
          criticalThreshold: 0,
          status: "good",
          description: "Número de sistemas prohibidos detectados (meta: 0)",
          trend: "stable",
          frequency: "daily",
          sources: ["GOVPROHIBITEDSYSTEMS", "PRJPROJECTS"],
        },
        {
          code: "KPI 10.6",
          name: "Tasa de Prompts Validados por Sesgos",
          value: 92.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 80,
          status: "good",
          description: "Porcentaje de prompts validados para detectar sesgos",
          trend: "up",
          trendValue: 2.0,
          frequency: "weekly",
          sources: ["PRMPROMPTS"],
        },
      ],
    },
    {
      odsNumber: 5,
      odsName: "Igualdad de Género",
      score: 80.0,
      trend: "up",
      modulesCount: 6,
      status: "good",
      kpis: [
        {
          code: "KPI 5.1",
          name: "Tasa de Datasets Balanceados por Género",
          value: 88.0,
          unit: "%",
          meta: 90,
          criticalThreshold: 70,
          status: "good",
          description: "Porcentaje de datasets con balance de género adecuado",
          trend: "up",
          trendValue: 2.0,
          frequency: "monthly",
          sources: ["DSDDATASETS"],
        },
        {
          code: "KPI 5.2",
          name: "Score de Representación de Género",
          value: 0.86,
          unit: "",
          meta: 0.85,
          criticalThreshold: 0.70,
          status: "good",
          description: "Score promedio de representación de género en datasets",
          trend: "up",
          trendValue: 0.01,
          frequency: "monthly",
          sources: ["DSDDATASETS"],
        },
        {
          code: "KPI 5.3",
          name: "Tasa de Sistemas con Evaluación de Sesgos de Género",
          value: 85.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 80,
          status: "good",
          description: "Porcentaje de sistemas evaluados por sesgos de género",
          trend: "up",
          trendValue: 3.0,
          frequency: "monthly",
          sources: ["FRIAFUNDAMENTALRIGHTSASSESSMENTS", "Análisis de sesgos"],
        },
      ],
    },
    {
      odsNumber: 8,
      odsName: "Trabajo Decente y Crecimiento Económico",
      score: 95.0,
      trend: "up",
      modulesCount: 2,
      status: "good",
      kpis: [
        {
          code: "KPI 8.1",
          name: "Tasa de Supervisión Humana (HITL)",
          value: 98.5,
          unit: "%",
          meta: 100,
          criticalThreshold: 95,
          status: "good",
          description: "Porcentaje de decisiones críticas con supervisión humana",
          trend: "up",
          trendValue: 0.5,
          frequency: "daily",
          sources: ["GOVHITLDECISIONS"],
        },
        {
          code: "KPI 8.2",
          name: "Tiempo Promedio de Respuesta HITL",
          value: 3.2,
          unit: "horas",
          meta: 4,
          criticalThreshold: 8,
          status: "good",
          description: "Tiempo promedio de respuesta humana en decisiones HITL",
          trend: "down",
          trendValue: -0.3,
          frequency: "daily",
          sources: ["GOVHITLDECISIONS"],
        },
      ],
    },
    {
      odsNumber: 12,
      odsName: "Producción y Consumo Responsables",
      score: 72.0,
      trend: "up",
      modulesCount: 3,
      status: "warning",
      kpis: [
        {
          code: "KPI 12.1",
          name: "Tasa de Reutilización de Modelos",
          value: 48.0,
          unit: "%",
          meta: 50,
          criticalThreshold: 30,
          status: "warning",
          description: "Porcentaje de modelos reutilizados vs. nuevos",
          trend: "up",
          trendValue: 3.0,
          frequency: "monthly",
          sources: ["MODMODELS", "AIOCOMPONENTS"],
        },
        {
          code: "KPI 12.2",
          name: "Reducción de Consumo Energético",
          value: 18.0,
          unit: "%",
          meta: 20,
          criticalThreshold: 10,
          status: "warning",
          description: "Porcentaje de reducción de consumo energético por optimización",
          trend: "up",
          trendValue: 2.0,
          frequency: "monthly",
          sources: ["Telemetría de recursos", "AIOCOMPONENTS"],
        },
        {
          code: "KPI 12.3",
          name: "Tasa de Uso de Modelos Eficientes",
          value: 58.0,
          unit: "%",
          meta: 60,
          criticalThreshold: 40,
          status: "warning",
          description: "Porcentaje de uso de modelos optimizados para eficiencia",
          trend: "up",
          trendValue: 2.0,
          frequency: "monthly",
          sources: ["Telemetría", "MODMODELS"],
        },
      ],
    },
    {
      odsNumber: 3,
      odsName: "Salud y Bienestar",
      score: 92.0,
      trend: "up",
      modulesCount: 2,
      status: "good",
      kpis: [
        {
          code: "KPI 3.1",
          name: "Tasa de Detección de Incidentes en Sistemas de Salud",
          value: 98.0,
          unit: "%",
          meta: 100,
          criticalThreshold: 90,
          status: "good",
          description: "Porcentaje de incidentes detectados en sistemas IA de salud",
          trend: "up",
          trendValue: 1.0,
          frequency: "daily",
          sources: ["GOVINCIDENTS", "PRJPROJECTS"],
        },
        {
          code: "KPI 3.2",
          name: "Tiempo Promedio de Detección de Incidentes",
          value: 0.8,
          unit: "horas",
          meta: 1,
          criticalThreshold: 4,
          status: "good",
          description: "Tiempo promedio desde ocurrencia hasta detección",
          trend: "down",
          trendValue: -0.1,
          frequency: "daily",
          sources: ["GOVINCIDENTS"],
        },
      ],
    },
    {
      odsNumber: 17,
      odsName: "Alianzas para lograr los Objetivos",
      score: 65.0,
      trend: "up",
      modulesCount: 2,
      status: "warning",
      kpis: [
        {
          code: "KPI 17.1",
          name: "Tasa de Compartir Agentes en Marketplace",
          value: 28.0,
          unit: "%",
          meta: 30,
          criticalThreshold: 15,
          status: "warning",
          description: "Porcentaje de agentes compartidos en marketplace",
          trend: "up",
          trendValue: 2.0,
          frequency: "monthly",
          sources: ["AGTAGENTS", "Marketplace"],
        },
        {
          code: "KPI 17.2",
          name: "Tasa de Adopción de Recursos Compartidos",
          value: 38.0,
          unit: "%",
          meta: 40,
          criticalThreshold: 20,
          status: "warning",
          description: "Porcentaje de uso de recursos compartidos",
          trend: "up",
          trendValue: 3.0,
          frequency: "monthly",
          sources: ["Telemetría", "Marketplace"],
        },
      ],
    },
    {
      odsNumber: 4,
      odsName: "Educación de Calidad",
      score: 55.0,
      trend: "up",
      modulesCount: 2,
      status: "warning",
      kpis: [
        {
          code: "KPI 4.1",
          name: "Tasa de Uso Educativo de LLMs Open Source",
          value: 42.0,
          unit: "%",
          meta: 50,
          criticalThreshold: 30,
          status: "warning",
          description: "Porcentaje de instituciones educativas usando LLMs open source",
          trend: "up",
          trendValue: 5.0,
          frequency: "quarterly",
          sources: ["AIOCOMPONENTS", "Telemetría"],
        },
        {
          code: "KPI 4.2",
          name: "Número de Bases de Conocimiento Educativas",
          value: 75,
          unit: "",
          meta: 100,
          criticalThreshold: 50,
          status: "good",
          description: "Cantidad de bases de conocimiento RAG para educación",
          trend: "up",
          trendValue: 5,
          frequency: "monthly",
          sources: ["RAG pipelines"],
        },
      ],
    },
    {
      odsNumber: 7,
      odsName: "Energía Asequible y No Contaminante",
      score: 75.0,
      trend: "up",
      modulesCount: 1,
      status: "good",
      kpis: [
        {
          code: "KPI 7.1",
          name: "Reducción de Consumo Energético por Optimización",
          value: 22.0,
          unit: "%",
          meta: 25,
          criticalThreshold: 10,
          status: "good",
          description: "Porcentaje de reducción de consumo energético",
          trend: "up",
          trendValue: 2.0,
          frequency: "monthly",
          sources: ["Telemetría de recursos", "Métricas de infraestructura"],
        },
      ],
    },
  ],
  topCriticalKPIs: [
    {
      code: "KPI 10.1",
      name: "Tasa de FRIA Completada",
      value: 95.0,
      unit: "%",
      meta: 100,
      criticalThreshold: 90,
      status: "warning",
      description: "Porcentaje de sistemas de alto riesgo con FRIA completada",
      trend: "up",
      trendValue: 2.0,
      frequency: "weekly",
      sources: ["FRIAFUNDAMENTALRIGHTSASSESSMENTS", "PRJPROJECTS"],
    },
    {
      code: "KPI 10.3",
      name: "Tasa de Detección de Sesgos en Datasets",
      value: 88.0,
      unit: "%",
      meta: 100,
      criticalThreshold: 80,
      status: "warning",
      description: "Porcentaje de datasets con análisis de sesgos completado",
      trend: "up",
      trendValue: 3.0,
      frequency: "monthly",
      sources: ["DSDDATASETS", "Análisis de sesgos"],
    },
    {
      code: "KPI 12.1",
      name: "Tasa de Reutilización de Modelos",
      value: 48.0,
      unit: "%",
      meta: 50,
      criticalThreshold: 30,
      status: "warning",
      description: "Porcentaje de modelos reutilizados vs. nuevos",
      trend: "up",
      trendValue: 3.0,
      frequency: "monthly",
      sources: ["MODMODELS", "AIOCOMPONENTS"],
    },
    {
      code: "KPI 17.1",
      name: "Tasa de Compartir Agentes en Marketplace",
      value: 28.0,
      unit: "%",
      meta: 30,
      criticalThreshold: 15,
      status: "warning",
      description: "Porcentaje de agentes compartidos en marketplace",
      trend: "up",
      trendValue: 2.0,
      frequency: "monthly",
      sources: ["AGTAGENTS", "Marketplace"],
    },
    {
      code: "KPI 4.1",
      name: "Tasa de Uso Educativo de LLMs Open Source",
      value: 42.0,
      unit: "%",
      meta: 50,
      criticalThreshold: 30,
      status: "warning",
      description: "Porcentaje de instituciones educativas usando LLMs open source",
      trend: "up",
      trendValue: 5.0,
      frequency: "quarterly",
      sources: ["AIOCOMPONENTS", "Telemetría"],
    },
  ],
  evolution: [
    { date: "2024-01", overallScore: 0.75, complianceRate: 0.82 },
    { date: "2024-02", overallScore: 0.77, complianceRate: 0.83 },
    { date: "2024-03", overallScore: 0.78, complianceRate: 0.84 },
    { date: "2024-04", overallScore: 0.79, complianceRate: 0.85 },
    { date: "2024-05", overallScore: 0.80, complianceRate: 0.85 },
    { date: "2024-06", overallScore: 0.80, complianceRate: 0.86 },
    { date: "2024-07", overallScore: 0.81, complianceRate: 0.86 },
    { date: "2024-08", overallScore: 0.81, complianceRate: 0.87 },
    { date: "2024-09", overallScore: 0.82, complianceRate: 0.87 },
    { date: "2024-10", overallScore: 0.82, complianceRate: 0.87 },
    { date: "2024-11", overallScore: 0.82, complianceRate: 0.87 },
    { date: "2024-12", overallScore: 0.82, complianceRate: 0.87 },
  ],
};

// Helper para obtener datos de un ODS específico
export function getODSData(odsNumber: number): ODSImpact | undefined {
  return mockODSImpactDashboard.odsImpacts.find(
    (ods) => ods.odsNumber === odsNumber
  );
}

// Helper para obtener KPI específico
export function getKPIData(odsNumber: number, kpiCode: string): ODSKPI | undefined {
  const odsData = getODSData(odsNumber);
  return odsData?.kpis.find((kpi) => kpi.code === kpiCode);
}
