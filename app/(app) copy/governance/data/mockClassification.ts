// Mock data para Classification Module
// Interfaces alineadas con DTOs Java

/**
 * Interface alineada con AnnexIIICategoryDto.java
 * Tabla: ANNANNEXIIICATEGORIES
 */
export interface AnnexIIICategory {
  idxannexiiicategory?: number;
  iduuid?: string;
  anncategorycode: string; // III.1, III.2, ..., III.8
  anncategoryname: string;
  anncategorydescription?: string;
  annsubcategorycode?: string; // NULL si es categoría padre
  annsubcategoryname?: string;
  annsubcategorydescription?: string;
  annparentcategory?: number;
  annislevel1?: boolean; // TRUE para 8 categorías principales
  annislevel2?: boolean; // TRUE para subcategorías
  annannexiiisection?: string;
  annarticlereference?: string;
  annkeywords?: string; // JSON array
  annactive?: boolean;
  anndisplayorder?: number;
  anncreatedat?: string;
  // Helper para UI: subcategorías agrupadas
  subcategories?: AnnexIIISubcategory[];
}

/**
 * Interface para subcategorías (derivada de AnnexIIICategory)
 */
export interface AnnexIIISubcategory {
  annsubcategorycode: string;
  annsubcategoryname: string;
  annsubcategorydescription?: string;
}

export interface AISuggestion {
  suggestedCategory: string;
  suggestedSubcategories: string[];
  confidence: number;
  reasoning: string;
}

// Helper function para parsear JSONB de categorías
export function parseAnnexIIICategories(jsonb: string | null | undefined): { category?: string; subcategories?: string[] } {
  if (!jsonb) return {};
  try {
    return JSON.parse(jsonb);
  } catch {
    return {};
  }
}

// Helper function para crear JSONB de categorías
export function createAnnexIIICategoriesJSONB(category: string, subcategories: string[]): string {
  return JSON.stringify({ category, subcategories });
}

export const mockAnnexIIICategories: AnnexIIICategory[] = [
  {
    idxannexiiicategory: 1,
    iduuid: "550e8400-e29b-41d4-a716-446655440001",
    anncategorycode: "III.1",
    anncategoryname: "Biometría y categorización biométrica",
    anncategorydescription: "Sistemas de IA utilizados para identificación biométrica y categorización de personas naturales",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 1,
    annarticlereference: "Art. 6.2 + Anexo III punto 1",
    subcategories: [
      {
        annsubcategorycode: "III.1.a",
        annsubcategoryname: "Sistemas de identificación biométrica remota",
        annsubcategorydescription: "Identificación biométrica remota en tiempo real o posterior",
      },
      {
        annsubcategorycode: "III.1.b",
        annsubcategoryname: "Categorización biométrica",
        annsubcategorydescription: "Categorización de personas naturales basada en atributos o características sensibles",
      },
      {
        annsubcategorycode: "III.1.c",
        annsubcategoryname: "Reconocimiento de emociones",
        annsubcategorydescription: "Sistemas de IA destinados a reconocimiento de emociones",
      },
    ],
  },
  {
    idxannexiiicategory: 2,
    iduuid: "550e8400-e29b-41d4-a716-446655440002",
    anncategorycode: "III.2",
    anncategoryname: "Gestión y operación de infraestructuras críticas",
    anncategorydescription: "Sistemas de IA utilizados como componentes de seguridad en la gestión y operación de infraestructuras críticas",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 2,
    annarticlereference: "Art. 6.2 + Anexo III punto 2",
    subcategories: [
      {
        annsubcategorycode: "III.2.1",
        annsubcategoryname: "Infraestructuras digitales críticas",
        annsubcategorydescription: "Sistemas de gestión de infraestructuras digitales críticas",
      },
      {
        annsubcategorycode: "III.2.2",
        annsubcategoryname: "Tráfico rodado",
        annsubcategorydescription: "Sistemas de gestión de tráfico rodado",
      },
      {
        annsubcategorycode: "III.2.3",
        annsubcategoryname: "Suministro de agua, gas, calefacción o electricidad",
        annsubcategorydescription: "Sistemas de gestión de suministro de servicios esenciales",
      },
    ],
  },
  {
    idxannexiiicategory: 3,
    iduuid: "550e8400-e29b-41d4-a716-446655440003",
    anncategorycode: "III.3",
    anncategoryname: "Educación y formación profesional",
    anncategorydescription: "Sistemas de IA para determinar acceso o asignar personas a instituciones educativas y formación profesional",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 3,
    annarticlereference: "Art. 6.2 + Anexo III punto 3",
    subcategories: [
      {
        annsubcategorycode: "III.3.a",
        annsubcategoryname: "Determinación de acceso o admisión",
        annsubcategorydescription: "Sistemas para determinar acceso o admisión a centros educativos y de formación profesional",
      },
      {
        annsubcategorycode: "III.3.b",
        annsubcategoryname: "Evaluación de resultados del aprendizaje",
        annsubcategorydescription: "Sistemas para evaluar resultados del aprendizaje y orientar el proceso",
      },
      {
        annsubcategorycode: "III.3.c",
        annsubcategoryname: "Evaluación del nivel de educación",
        annsubcategorydescription: "Sistemas para evaluar el nivel de educación adecuado",
      },
      {
        annsubcategorycode: "III.3.d",
        annsubcategoryname: "Seguimiento y detección de comportamientos prohibidos",
        annsubcategorydescription: "Sistemas para seguimiento y detección de comportamientos prohibidos durante exámenes",
      },
    ],
  },
  {
    idxannexiiicategory: 4,
    iduuid: "550e8400-e29b-41d4-a716-446655440004",
    anncategorycode: "III.4",
    anncategoryname: "Empleo, gestión de trabajadores y acceso al autoempleo",
    anncategorydescription: "Sistemas de IA utilizados en empleo, gestión de trabajadores y acceso al autoempleo",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 4,
    annarticlereference: "Art. 6.2 + Anexo III punto 4",
    subcategories: [
      {
        annsubcategorycode: "III.4.a",
        annsubcategoryname: "Contratación o selección de personas",
        annsubcategorydescription: "Sistemas para publicar anuncios, analizar solicitudes y evaluar candidatos",
      },
      {
        annsubcategorycode: "III.4.b",
        annsubcategoryname: "Decisiones que afectan condiciones laborales",
        annsubcategorydescription: "Sistemas para tomar decisiones sobre condiciones, promoción o rescisión de relaciones laborales",
      },
    ],
  },
  {
    idxannexiiicategory: 5,
    iduuid: "550e8400-e29b-41d4-a716-446655440005",
    anncategorycode: "III.5",
    anncategoryname: "Acceso a servicios privados esenciales y a servicios y prestaciones públicos esenciales",
    anncategorydescription: "Sistemas de IA utilizados para evaluar la solvencia crediticia de personas naturales o establecer su calificación crediticia",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 5,
    annarticlereference: "Art. 6.2 + Anexo III punto 5",
    subcategories: [
      {
        annsubcategorycode: "III.5.a",
        annsubcategoryname: "Evaluación de admisibilidad para servicios y prestaciones públicas",
        annsubcategorydescription: "Sistemas para evaluar admisibilidad para servicios y prestaciones esenciales de asistencia pública",
      },
      {
        annsubcategorycode: "III.5.b",
        annsubcategoryname: "Evaluación de solvencia y calificación crediticia",
        annsubcategorydescription: "Sistemas para evaluar solvencia de personas físicas o establecer calificación crediticia",
      },
      {
        annsubcategorycode: "III.5.c",
        annsubcategoryname: "Evaluación de riesgos y fijación de precios en seguros",
        annsubcategorydescription: "Sistemas para evaluación de riesgos y fijación de precios en seguros de vida y salud",
      },
      {
        annsubcategorycode: "III.5.d",
        annsubcategoryname: "Evaluación y clasificación de llamadas de emergencia",
        annsubcategorydescription: "Sistemas para evaluación y clasificación de llamadas de emergencia y triaje de pacientes",
      },
    ],
  },
  {
    idxannexiiicategory: 6,
    iduuid: "550e8400-e29b-41d4-a716-446655440006",
    anncategorycode: "III.6",
    anncategoryname: "Garantía del cumplimiento del Derecho",
    anncategorydescription: "Sistemas de IA utilizados por autoridades policiales para garantía del cumplimiento del Derecho",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 6,
    annarticlereference: "Art. 6.2 + Anexo III punto 6",
    subcategories: [
      {
        annsubcategorycode: "III.6.a",
        annsubcategoryname: "Evaluación del riesgo de ser víctima de delitos",
        annsubcategorydescription: "Sistemas para evaluar el riesgo de que una persona física sea víctima de delitos",
      },
      {
        annsubcategorycode: "III.6.b",
        annsubcategoryname: "Polígrafos o herramientas similares",
        annsubcategorydescription: "Sistemas utilizados como polígrafos o herramientas similares",
      },
      {
        annsubcategorycode: "III.6.c",
        annsubcategoryname: "Evaluación de la fiabilidad de las pruebas",
        annsubcategorydescription: "Sistemas para evaluar la fiabilidad de las pruebas durante investigación o enjuiciamiento",
      },
      {
        annsubcategorycode: "III.6.d",
        annsubcategoryname: "Evaluación del riesgo de cometer delitos",
        annsubcategorydescription: "Sistemas para evaluar el riesgo de que una persona física cometa un delito o reincida",
      },
      {
        annsubcategorycode: "III.6.e",
        annsubcategoryname: "Elaboración de perfiles de personas físicas",
        annsubcategorydescription: "Sistemas para elaborar perfiles de personas físicas durante detección, investigación o enjuiciamiento",
      },
    ],
  },
  {
    idxannexiiicategory: 7,
    iduuid: "550e8400-e29b-41d4-a716-446655440007",
    anncategorycode: "III.7",
    anncategoryname: "Migración, asilo y gestión del control fronterizo",
    anncategorydescription: "Sistemas de IA utilizados en migración, asilo y gestión del control fronterizo",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 7,
    annarticlereference: "Art. 6.2 + Anexo III punto 7",
    subcategories: [
      {
        annsubcategorycode: "III.7.a",
        annsubcategoryname: "Polígrafos o herramientas similares",
        annsubcategorydescription: "Sistemas utilizados como polígrafos o herramientas similares en migración y asilo",
      },
      {
        annsubcategorycode: "III.7.b",
        annsubcategoryname: "Evaluación de riesgos de seguridad, salud o migración irregular",
        annsubcategorydescription: "Sistemas para evaluar riesgos que plantee una persona física que tenga intención de entrar en territorio",
      },
      {
        annsubcategorycode: "III.7.c",
        annsubcategoryname: "Examen de solicitudes de asilo, visado o permiso de residencia",
        annsubcategorydescription: "Sistemas para ayudar a examinar solicitudes de asilo, visado o permiso de residencia",
      },
      {
        annsubcategorycode: "III.7.d",
        annsubcategoryname: "Detección, reconocimiento o identificación de personas físicas",
        annsubcategorydescription: "Sistemas para detectar, reconocer o identificar personas físicas en contexto de migración",
      },
    ],
  },
  {
    idxannexiiicategory: 8,
    iduuid: "550e8400-e29b-41d4-a716-446655440008",
    anncategorycode: "III.8",
    anncategoryname: "Administración de justicia y procesos democráticos",
    anncategorydescription: "Sistemas de IA utilizados en la administración de justicia y procesos democráticos",
    annislevel1: true,
    annislevel2: false,
    annactive: true,
    anndisplayorder: 8,
    annarticlereference: "Art. 6.2 + Anexo III punto 8",
    subcategories: [
      {
        annsubcategorycode: "III.8.a",
        annsubcategoryname: "Ayuda a autoridad judicial",
        annsubcategorydescription: "Sistemas para ayudar a una autoridad judicial en investigación e interpretación de hechos y ley",
      },
      {
        annsubcategorycode: "III.8.b",
        annsubcategoryname: "Influencia en resultado de elecciones o referendos",
        annsubcategorydescription: "Sistemas para influir en el resultado de una elección o referéndum o en el comportamiento electoral",
      },
    ],
  },
];

export interface ProjectDetails {
  id: number;
  name: string;
  code: string;
  description: string;
  type: string;
  status: string;
  owner: string;
  ownerEmail: string;
  client?: string;
  startDate?: string;
  endDate?: string;
  createdAt: string;
  updatedAt: string;
  models: Array<{
    id: number;
    name: string;
    version: string;
    type: string;
    isGPAI: boolean;
    flopsTraining?: number;
    systemicRisk?: boolean;
  }>;
  currentClassification: ClassificationDetails | null;
}

/**
 * Interface alineada con campos de ProjectDto.java relacionados con clasificación
 * Campos EU AI Act Compliance - Art. 5, 6, 49, Anexos I, II, III
 */
export interface ClassificationDetails {
  // Campos principales según ProjectDto.java
  prjishighrisk?: boolean; // PRJISHIGHRISK
  prjannexiiicategories?: string; // PRJANNEXIIICATEGORIES (JSONB) - formato: {"category":"III.5","subcategories":["III.5.b","III.5.c"]}
  prjclassificationdate?: string; // PRJCLASSIFICATIONDATE (ISO 8601)
  prjclassificationauthor?: string; // PRJCLASSIFICATIONAUTHOR (max 100)

  // Anexo I - Sector Regulado
  prjregulatedsector?: boolean; // PRJREGULATEDSECTOR
  prjannexilegislation?: string; // PRJANNEXILEGISLATION (JSONB) - formato: ["Reglamento (UE) 2017/745"]

  // Art. 5 - Sistemas Prohibidos
  prjprohibitedusechecked?: boolean; // PRJPROHIBITEDUSECHECKED
  prjprohibitedusejustification?: string; // PRJPROHIBITEDUSEJUSTIFICATION (CLOB)

  // Art. 49 - Registro Base Datos UE
  prjeuregistrationid?: string; // PRJEUREGISTRATIONID (max 100)
  prjeuregistrationdate?: string; // PRJEUREGISTRATIONDATE (ISO 8601)
  prjeuregistrationstatus?: string; // PRJEUREGISTRATIONSTATUS (max 20) - DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED, ERROR

  // Campo de justificación (mapea a PRJCLASSIFICATIONJUSTIFICATION)
  prjclassificationjustification?: string; // Justificación (CLOB) - mínimo 100 caracteres

  // Campos helper para UI (derivados de prjannexiiicategories)
  category?: string; // Extraído de prjannexiiicategories
  subcategories?: string[]; // Extraído de prjannexiiicategories
  justification?: string; // Helper: mapea a prjclassificationjustification

  // Campos adicionales (no en Project.java, pero útiles para UI)
  nextReviewDate?: string;
  confidence?: number;
  internalNotes?: string;
  complianceOfficer?: string;
  workflowInstanceId?: string;
  workflowStatus?: string;
  previousClassifications?: Array<{
    category: string;
    date: string;
    by: string;
    reason: string;
  }>;
}

/**
 * Interface alineada con ClassificationRequestDto.java
 */
export interface ClassificationRequest {
  projectId: number;
  category: string; // III.1 a III.8
  subcategories: string[]; // Lista de códigos de subcategorías
  justification: string; // Mínimo 100 caracteres
  prohibitedUseChecked?: boolean;
  prohibitedUseJustification?: string;
  regulatedSector?: boolean;
  annexILegislation?: string[];
}

/**
 * Interface alineada con ClassificationResponseDto.java
 */
export interface ClassificationResponse {
  success: boolean;
  message: string;
  data?: {
    projectId: number;
    isHighRisk: boolean;
    category: string;
    categoryName: string;
    subcategories: string[];
    classificationDate: string;
    classifiedBy: string;
    workflowTriggered?: boolean;
    workflowInstanceId?: string;
    euRegistrationRequired?: boolean;
    nextSteps?: string[];
  };
}

export const mockProject: ProjectDetails = {
  id: 1001,
  name: "AI Credit Scoring System",
  code: "PRJ-2025-001",
  description: "Sistema de scoring crediticio basado en IA para evaluación de préstamos y acceso a servicios financieros. El sistema utiliza machine learning para analizar el historial crediticio, ingresos, gastos y otros factores para determinar la solvencia de solicitantes de préstamos.",
  type: "AI_SYSTEM",
  status: "IN_PROGRESS",
  owner: "María González",
  ownerEmail: "maria.gonzalez@company.com",
  client: "Banco Nacional",
  startDate: "2024-06-01",
  endDate: "2025-12-31",
  createdAt: "2024-05-15T10:00:00Z",
  updatedAt: "2025-01-15T10:30:00Z",
  models: [
    {
      id: 1,
      name: "Credit Risk Model v1.0",
      version: "1.0.0",
      type: "CLASSIFIER",
      isGPAI: false,
    },
    {
      id: 2,
      name: "Creditworthiness Assessment v2.1",
      version: "2.1.3",
      type: "REGRESSION",
      isGPAI: false,
    },
  ],
  currentClassification: null,
};

export const mockAISuggestion: AISuggestion = {
  suggestedCategory: "III.5",
  suggestedSubcategories: ["III.5.b", "III.5.c"],
  confidence: 0.92,
  reasoning: "El sistema evalúa la solvencia crediticia de personas naturales para préstamos y servicios financieros, lo cual está cubierto por la categoría III.5 del Anexo III (Acceso a servicios privados esenciales). Las subcategorías III.5.b (Evaluación de solvencia y calificación crediticia) y III.5.c (Evaluación de riesgos y fijación de precios en seguros) son relevantes dado que el sistema procesa datos financieros personales y toma decisiones automatizadas que afectan significativamente el acceso a crédito.",
};

export const mockAnnexIIICatalog = {
  categories: mockAnnexIIICategories,
  examples: {
    "III.1": [
      "Facial recognition systems in airports",
      "Biometric access control systems",
      "Fingerprint identification for law enforcement",
    ],
    "III.2": [
      "Sistemas de gestión de tráfico inteligente",
      "Sistemas de control de red eléctrica",
      "Automatización de plantas de tratamiento de agua",
    ],
    "III.3": [
      "Sistemas automatizados de admisión estudiantil",
      "Sistemas de calificación basados en IA",
      "Plataformas de aprendizaje personalizado",
    ],
    "III.4": [
      "Sistemas de selección de CV basados en IA",
      "Sistemas automatizados de emparejamiento de trabajos",
      "Monitoreo de rendimiento de empleados",
    ],
    "III.5": [
      "Algoritmos de scoring crediticio",
      "Sistemas de aprobación de préstamos",
      "Cálculo de primas de seguros",
    ],
    "III.6": [
      "Sistemas de policía predictiva",
      "Herramientas de análisis de evidencia",
      "Evaluación de riesgo criminal",
    ],
    "III.7": [
      "Visa application processing",
      "Border surveillance systems",
      "Asylum decision support",
    ],
    "III.8": [
      "Sistemas de predicción de casos legales",
      "Herramientas de análisis de decisiones judiciales",
      "Predicción de resultados electorales",
    ],
  },
};

export interface ClassifiedProject {
  id: number;
  name: string;
  description: string;
  category: string;
  categoryName: string;
  subcategories: string[];
  justification: string;
  classifiedDate: string;
  classifiedBy: string;
  status: "active" | "archived" | "pending_review";
  modelsCount: number;
}

export const mockClassifiedProjects: ClassifiedProject[] = [
  {
    id: 1001,
    name: "AI Credit Scoring System",
    description: "Sistema de scoring crediticio basado en IA para evaluación de préstamos y acceso a servicios financieros",
    category: "III.5",
    categoryName: "Acceso a servicios privados esenciales y a servicios y prestaciones públicos esenciales",
    subcategories: ["III.5.b", "III.5.c"],
    justification: "Este sistema evalúa la solvencia crediticia de personas naturales para préstamos y servicios financieros, lo cual está cubierto por la categoría III.5 del Anexo III. El sistema procesa datos financieros personales y toma decisiones automatizadas que afectan significativamente el acceso a crédito, representando un alto riesgo para los derechos fundamentales de los usuarios.",
    classifiedDate: "2025-01-15T10:30:00Z",
    classifiedBy: "María González",
    status: "active",
    modelsCount: 2,
  },
  {
    id: 1002,
    name: "Biometric Access Control",
    description: "Sistema de control de acceso biométrico para instalaciones críticas",
    category: "III.1",
    categoryName: "Biometría y categorización biométrica",
    subcategories: ["III.1.a", "III.1.b"],
    justification: "Este sistema utiliza identificación biométrica en tiempo real para control de acceso en instalaciones críticas. La identificación biométrica de personas naturales en espacios públicos representa un alto riesgo para la privacidad y los derechos fundamentales, especialmente cuando se utiliza para identificación remota en tiempo real.",
    classifiedDate: "2025-01-10T14:20:00Z",
    classifiedBy: "Juan Pérez",
    status: "active",
    modelsCount: 1,
  },
  {
    id: 1003,
    name: "AI Resume Screening System",
    description: "Sistema de selección de candidatos basado en IA para procesos de reclutamiento",
    category: "III.4",
    categoryName: "Empleo, gestión de trabajadores y acceso al autoempleo",
    subcategories: ["III.4.a"],
    justification: "Este sistema evalúa y selecciona candidatos para empleo utilizando algoritmos de IA. Los sistemas de IA utilizados en empleo y gestión de trabajadores representan un alto riesgo ya que pueden afectar significativamente las oportunidades de empleo y la vida profesional de las personas, especialmente cuando se utilizan para decisiones automatizadas de contratación.",
    classifiedDate: "2025-01-08T09:15:00Z",
    classifiedBy: "Ana Martínez",
    status: "active",
    modelsCount: 3,
  },
  {
    id: 1004,
    name: "Smart Traffic Management",
    description: "Sistema de gestión inteligente de tráfico para infraestructura crítica",
    category: "III.2",
    categoryName: "Gestión y operación de infraestructuras críticas",
    subcategories: ["III.2.2"],
    justification: "Este sistema gestiona la infraestructura crítica de tráfico utilizando componentes de seguridad basados en IA. Los sistemas de IA utilizados como componentes de seguridad en la gestión de infraestructura crítica representan un alto riesgo ya que pueden afectar la seguridad pública y la continuidad de servicios esenciales.",
    classifiedDate: "2025-01-05T16:45:00Z",
    classifiedBy: "Carlos Rodríguez",
    status: "active",
    modelsCount: 1,
  },
  {
    id: 1005,
    name: "Predictive Policing System",
    description: "Sistema de predicción de delitos para autoridades policiales",
    category: "III.6",
    categoryName: "Garantía del cumplimiento del Derecho",
    subcategories: ["III.6.a", "III.6.d"],
    justification: "Este sistema es utilizado por autoridades policiales para evaluación de riesgos y prevención de delitos. Los sistemas de IA utilizados por autoridades policiales representan un alto riesgo crítico ya que pueden afectar significativamente los derechos fundamentales, incluyendo la libertad personal, la privacidad y la presunción de inocencia.",
    classifiedDate: "2024-12-20T11:30:00Z",
    classifiedBy: "Laura Fernández",
    status: "pending_review",
    modelsCount: 2,
  },
];
