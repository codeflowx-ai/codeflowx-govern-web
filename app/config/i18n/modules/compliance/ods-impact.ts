// Traducciones para ODS Impact Dashboard
import { TranslationModule } from "../../types";

export const odsImpactTranslations: TranslationModule = {
  es: {
    compliance: {
      odsImpact: {
        title: "Impacto ODS",
        subtitle:
          "Medición del impacto de la plataforma en los Objetivos de Desarrollo Sostenible",
        odsImpact: "Impacto por ODS",
        topCriticalKPIs: "Top 5 KPIs Críticos",
        evolution: "Evolución Temporal (Últimos 12 meses)",
        overallImpactScore: "Score General de Impacto",
        complianceRate: "Tasa de Cumplimiento",
        scoreGeneral: "Score General",
        compliance: "Cumplimiento",
        components: "Componentes",
        modules: "Módulos",
        back: "Volver",
        // ODS 16
        ods16: {
          description:
            "Paz, Justicia e Instituciones Sólidas - 6 KPIs principales",
        },
        traceability: "Trazabilidad",
        traceabilityEvolution: "Evolución de Tasa de Trazabilidad",
        certification: "Certificación",
        certifiedSystems: "Sistemas Certificados vs. Pendientes",
        systemsInProcess: "Sistemas en Proceso de Certificación",
        daysRemaining: "días restantes",
        qms: "Sistema de Gestión de Calidad (QMS)",
        qmsModuleScores: "Score QMS por Módulo (13 módulos)",
        gapsDetected: "Gaps Detectados",
        currentScore: "Score actual",
        target: "Meta",
        gap: "Gap",
        hitl: "Supervisión Humana (HITL)",
        hitlDecisions: "Decisiones con/sin HITL",
        hitlResponseTime: "Tiempo Promedio de Respuesta HITL",
        pendingDecisions: "Decisiones Pendientes de Supervisión Humana",
        timeRemaining: "h restantes",
        pending: "Pendiente",
        // ODS 9
        ods9: {
          description:
            "Industria, Innovación e Infraestructura - 7 KPIs principales",
        },
        reuse: "Reutilización",
        reuseRate: "Tasa de Reutilización (%)",
        resourceReuse: "Reutilización de Recursos",
        mlops: "MLOps",
        mlopsCycleTime: "Tiempo Promedio (días)",
        llms: "LLMs Open Source",
        llmAdoption: "Adopción LLMs Open Source (%)",
        documentation: "Documentación",
        documentationScore: "Score de Documentación (%)",
        completeness: "Completitud (%)",
        // ODS 10
        ods10: {
          description:
            "Reducción de las Desigualdades - 6 KPIs principales",
        },
        fria: "Evaluación de Impacto en Derechos Fundamentales (FRIA)",
        friaCompleted: "FRIAs Completadas vs. Pendientes",
        averageRisk: "Riesgo Promedio en FRIA",
        highRiskWithoutFria: "Sistemas de Alto Riesgo sin FRIA",
        pendingSince: "Pendiente desde hace",
        days: "días",
        riskLevel: "Riesgo",
        prohibited: "Sistemas Prohibidos",
        noProhibitedSystems: "Estado: Sin Sistemas Prohibidos Detectados",
        allSystemsCompliant: "Todos los sistemas cumplen con las restricciones del EU AI Act",
        prohibitedByCategory: "Sistemas Prohibidos Detectados por Categoría",
        blockedSystems: "Sistemas Bloqueados (Histórico)",
        noBlockedSystems: "No hay sistemas bloqueados en el historial.",
        datasetsBias: "Análisis de Sesgos en Datasets",
        biasAnalysisCompleted: "Datasets con Análisis de Sesgos Completado",
        promptsValidated: "Prompts Validados por Sesgos",
        representativityEvolution: "Evolución del Score de Representatividad",
        bias: "Sesgos en Datasets",
        prompts: "Validación de Prompts",
        // ODS 5
        ods5: {
          description: "Igualdad de Género - 3 KPIs principales",
        },
        genderBalance: "Balance de Género en Datasets",
        genderDatasets: "Análisis de Género en Datasets",
        genderDistribution: "Distribución de Género en Datasets",
        balancedVsUnbalanced: "Datasets Balanceados vs. No Balanceados",
        genderRepresentationEvolution: "Evolución del Score de Representación de Género",
        biasEvaluation: "Evaluación de Sesgos",
        systemsEvaluated: "Sistemas Evaluados vs. Pendientes",
        mitigationMeasures: "Medidas de Mitigación Implementadas",
        genderBiasDetected: "Sistemas con Sesgos de Género Detectados",
        type: "Tipo",
        mitigation: "Mitigación",
        severity: "Severidad",
        high: "Alto",
        medium: "Medio",
        low: "Bajo",
        // ODS 8
        ods8: {
        },
        withHitl: "Con HITL",
        withoutHitl: "Sin HITL",
        decisions: "Decisiones",
        averageTime: "Tiempo Promedio (horas)",
        // ODS 12
        ods12: {
        },
        modelReuse: "Reutilización de Modelos",
        reuseEfficiency: "Reutilización y Eficiencia",
        energyEfficiency: "Eficiencia Energética",
        energyReduction: "Reducción de Consumo Energético (%)",
        efficientModels: "Modelos Eficientes",
        nonOptimizedModels: "Modelos No Optimizados",
        usage: "Uso (%)",
        // ODS 3
        ods3: {
        },
        incidentDetection: "Detección de Incidentes en Sistemas de Salud",
        incidents: "Detección de Incidentes",
        detected: "Detectados",
        notDetected: "No Detectados",
        averageDetectionTime: "Tiempo Promedio (horas)",
        // ODS 17
        ods17: {
        },
        agentSharing: "Compartir Agentes en Marketplace",
        sharedResources: "Adopción de Recursos Compartidos",
        sharedAgents: "Tasa de Compartir Agentes (%)",
        sharedResourcesAdoption: "Tasa de Adopción de Recursos Compartidos (%)",
        shared: "Compartidos",
        notShared: "No Compartidos",
        agents: "Agentes",
        // ODS 4
        ods4: {
        },
        educationalLLMs: "Uso Educativo de LLMs Open Source",
        educationalUsage: "Uso Educativo de IA",
        educationalKnowledgeBases: "Bases de Conocimiento Educativas",
        educationalInstitutions: "Uso en Instituciones Educativas (%)",
        openSource: "Open Source",
        proprietary: "Propietarios",
        educationalLLMUsage: "Tasa de Uso Educativo de LLMs Open Source (%)",
        educationalKnowledgeBasesCount: "Número de Bases de Conocimiento Educativas",
        // ODS 7
        ods7: {
        },
        energyReductionTitle: "Reducción de Consumo Energético por Optimización",
        // Meses
        months: {
          jan: "Ene",
          feb: "Feb",
          mar: "Mar",
          apr: "Abr",
          may: "May",
          jun: "Jun",
          jul: "Jul",
          aug: "Ago",
          sep: "Sep",
          oct: "Oct",
          nov: "Nov",
          dec: "Dic",
        },
        // KPICard
        kpiCard: {
          fulfilled: "Cumplido",
          attention: "Atención",
          critical: "Crítico",
          ofTarget: "de meta",
          criticalThreshold: "Umbral crítico",
        },
        // ODSCard
        odsCard: {
          good: "Bueno",
          score: "Score",
        },
        // Common chart labels
        systems: "Sistemas",
        datasets: "Datasets",
        projects: "Proyectos",
        // Calculated from
        calculatedFrom: "Calculado desde",
        // Chart labels
        chartLabels: {
          distribution: "Distribución (%)",
          averageTimeDays: "Tiempo Promedio (días)",
          usageEducational: "Uso en Instituciones Educativas (%)",
          agentSharingRate: "Tasa de Compartir Agentes (%)",
          sharedResourcesAdoptionRate: "Tasa de Adopción de Recursos Compartidos (%)",
          genderRepresentationScore: "Score de Representación de Género",
          traceabilityRate: "Tasa de Trazabilidad (%)",
          time: "Tiempo (horas)",
        },
        // Status labels
        statusLabels: {
          balanced: "Balanceados",
          notBalanced: "No Balanceados",
          male: "Masculino",
          female: "Femenino",
          nonBinary: "No binario",
          other: "Otros",
          efficient: "Modelos Eficientes",
          nonOptimized: "Modelos No Optimizados",
        },
        // KPI names
        kpiNames: {
          friaCompletedRate: "Tasa de FRIA Completada",
          biasDetectionRate: "Tasa de Detección de Sesgos en Datasets",
          highRiskSystemsWithFria: "Porcentaje de sistemas de alto riesgo con FRIA completada",
          modelReuseRate: "Tasa de Reutilización de Modelos",
          agentSharingMarketplace: "Tasa de Compartir Agentes en Marketplace",
          educationalLLMUsageRate: "Tasa de Uso Educativo de LLMs Open Source",
        },
        // ODS Names
        odsNames: {
          ods16: "Paz, Justicia e Instituciones Sólidas",
          ods9: "Industria, Innovación e Infraestructura",
          ods10: "Reducción de las Desigualdades",
          ods5: "Igualdad de Género",
          ods8: "Trabajo Decente y Crecimiento Económico",
          ods12: "Producción y Consumo Responsables",
          ods3: "Salud y Bienestar",
          ods17: "Alianzas para lograr los Objetivos",
          ods4: "Educación de Calidad",
          ods7: "Energía Asequible y No Contaminante",
        },
      },
    },
  },
  en: {
    compliance: {
      odsImpact: {
        subtitle:
          "Measurement of the platform's impact on Sustainable Development Goals",
        ods16: {
          description:
            "Peace, Justice and Strong Institutions - 6 main KPIs",
        },
        ods9: {
          description:
            "Industry, Innovation and Infrastructure - 7 main KPIs",
        },
        ods10: {
          description:
            "Reduced Inequalities - 6 main KPIs",
        },
        ods5: {
        },
        ods8: {
        },
        ods12: {
        },
        ods3: {
        },
        ods17: {
        },
        ods4: {
        },
        ods7: {
        },
        months: {
        },
        kpiCard: {
        },
        odsCard: {
        },
        chartLabels: {
        },
        statusLabels: {
        },
        // Section titles
        friaCompletedVsPending: "Completed vs. Pending FRIAs",
        averageRiskInFria: "Average Risk in FRIA",
        highRiskSystemsWithoutFria: "High Risk Systems without FRIA",
        certifiedSystemsVsPending: "Certified Systems vs. Pending",
        systemsInCertificationProcess: "Systems in Certification Process",
        pendingHumanSupervisionDecisions: "Pending Human Supervision Decisions",
        reuseRateByType: "Reuse Rate by Type",
        reuseEvolution: "Reuse Evolution",
        energyReductionEvolution: "Energy Consumption Reduction Evolution",
        llmAdoptionEvolution: "Open Source LLM Adoption Evolution",
        educationalKnowledgeBasesEvolution: "Educational Knowledge Bases Evolution",
        modelReuseEvolution: "Model Reuse Evolution",
        genderRepresentationScoreEvolution: "Gender Representation Score Evolution",
        systemsEvaluatedVsPending: "Evaluated vs. Pending Systems",
        systemsWithGenderBiasDetected: "Systems with Detected Gender Bias",
        pendingSupervisionDecisions: "Pending Supervision Decisions",
        incidentsDetectedVsNotDetected: "Detected vs. Not Detected Incidents",
        prohibitedSystemsDetectedByCategory: "Prohibited Systems Detected by Category",
        blockedSystemsHistory: "Blocked Systems (History)",
        promptsValidatedForBias: "Prompts Validated for Bias",
        representativityScoreEvolution: "Representativity Score Evolution",
        // KPI names
        kpiNames: {
        },
        // ODS Names
        odsNames: {
        },
      },
    },
  },
  fr: {
    compliance: {
      odsImpact: {
        subtitle:
          "Mesure de l'impact de la plateforme sur les Objectifs de Développement Durable",
        ods16: {
          description:
            "Paix, Justice et Institutions Solides - 6 KPI principaux",
        },
        ods9: {
          description:
            "Industrie, Innovation et Infrastructure - 7 KPI principaux",
        },
        ods10: {
          description:
            "Réduction des Inégalités - 6 KPI principaux",
        },
        ods5: {
        },
        ods8: {
        },
        ods12: {
        },
        ods3: {
        },
        ods17: {
        },
        ods4: {
        },
        ods7: {
        },
        months: {
        },
        kpiCard: {
        },
        odsCard: {
        },
        chartLabels: {
        },
        statusLabels: {
        },
        // Section titles
        // KPI names
        kpiNames: {
        },
        // ODS Names
        odsNames: {
        },
      },
    },
  },
  de: {
    compliance: {
      odsImpact: {
        subtitle:
          "Messung der Auswirkungen der Plattform auf die Ziele für nachhaltige Entwicklung",
        ods16: {
          description:
            "Frieden, Gerechtigkeit und Starke Institutionen - 6 Haupt-KPIs",
        },
        ods9: {
          description:
            "Industrie, Innovation und Infrastruktur - 7 Haupt-KPIs",
        },
        ods10: {
          description:
            "Weniger Ungleichheiten - 6 Haupt-KPIs",
        },
        ods5: {
        },
        ods8: {
        },
        ods12: {
        },
        ods3: {
        },
        ods17: {
        },
        ods4: {
        },
        ods7: {
        },
        months: {
        },
        kpiCard: {
        },
        odsCard: {
        },
        chartLabels: {
        },
        statusLabels: {
        },
        // Section titles
        // KPI names
        kpiNames: {
        },
        // ODS Names
        odsNames: {
        },
      },
    },
  },
  it: {
    compliance: {
      odsImpact: {
        subtitle:
          "Misurazione dell'impatto della piattaforma sugli Obiettivi di Sviluppo Sostenibile",
        ods16: {
          description:
            "Pace, Giustizia e Istituzioni Solide - 6 KPI principali",
        },
        ods9: {
          description:
            "Industria, Innovazione e Infrastruttura - 7 KPI principali",
        },
        ods10: {
          description:
            "Riduzione delle Disuguaglianze - 6 KPI principali",
        },
        ods5: {
        },
        ods8: {
        },
        ods12: {
        },
        ods3: {
        },
        ods17: {
        },
        ods4: {
        },
        ods7: {
        },
        months: {
        },
        kpiCard: {
        },
        odsCard: {
        },
        chartLabels: {
        },
        statusLabels: {
        },
        // Section titles
        // KPI names
        kpiNames: {
        },
        // ODS Names
        odsNames: {
        },
      },
    },
  },
  pt: {
    compliance: {
      odsImpact: {
        subtitle:
          "Medição do impacto da plataforma nos Objetivos de Desenvolvimento Sustentável",
        ods16: {
          description:
            "Paz, Justiça e Instituições Fortes - 6 KPIs principais",
        },
        ods9: {
          description:
            "Indústria, Inovação e Infraestrutura - 7 KPIs principais",
        },
        ods10: {
          description:
            "Redução das Desigualdades - 6 KPIs principais",
        },
        ods5: {
        },
        ods8: {
        },
        ods12: {
        },
        ods3: {
        },
        ods17: {
        },
        ods4: {
        },
        ods7: {
        },
        months: {
        },
        kpiCard: {
        },
        odsCard: {
        },
        chartLabels: {
        },
        statusLabels: {
        },
        // Section titles
        // KPI names
        kpiNames: {
        },
        // ODS Names
        odsNames: {
        },
      },
    },
  },
};
