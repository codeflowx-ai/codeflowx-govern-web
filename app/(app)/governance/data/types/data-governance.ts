/**
 * Tipos TypeScript para Data Governance
 */

export interface DataGovernanceDataset {
  idxdataset: number;
  iduuid: string;
  dtgname: string;
  dtgdescription?: string;
  dtgtype: "TRAINING" | "VALIDATION" | "TEST" | "PRODUCTION" | "RAG";
  dtgcategory?: "STRUCTURED" | "UNSTRUCTURED" | "SEMI_STRUCTURED";
  dtgformat?: string;
  dtgorigintype: "INTERNAL" | "EXTERNAL" | "SYNTHETIC" | "PUBLIC";
  idxorigin?: number;
  dtgsourceurl?: string;
  dtgsyncmethod?: "MANUAL" | "SCHEDULED" | "REAL_TIME" | "API";
  idxproject?: number;
  idxragsystem?: number;
  idxtrainingexecution?: number;
  dtgschema?: string;
  dtgmetadata?: string;
  dtgstatistics?: string;
  dtgqualityscore?: number;
  dtgqualitymetrics?: string;
  dtgbiasscore?: number;
  dtgbiasanalysis?: string;
  dtgbiasanalyzed?: boolean;
  dtgbiasanalyzedat?: string;
  dtgrepresentativityscore?: number;
  dtgrepresentativitymetrics?: string;
  dtggenderdistribution?: string;
  dtggenderbalancescore?: number;
  dtggenderbalanced?: boolean;
  dtggenderanalyzed?: boolean;
  dtgdataclass?: "PUBLIC" | "INTERNAL" | "CONFIDENTIAL" | "RESTRICTED";
  dtgpiidetected?: boolean;
  dtgpiianalysis?: string;
  dtgencrypted?: boolean;
  dtgencryptionmethod?: string;
  dtgstandardized?: boolean;
  dtgstandardizedat?: string;
  dtgstandardizedformat?: string;
  dtgstandardizedpath?: string;
  dtgoriginalformat?: string;
  dtgoriginalpath?: string;
  dtgversion: string;
  dtgversionhash?: string;
  idxparentdataset?: number;
  dtgstatus: "DRAFT" | "VALIDATED" | "APPROVED" | "ACTIVE" | "ARCHIVED";
  dtgapproved?: boolean;
  dtgapprovedby?: number;
  dtgapprovedat?: string;
  dtgstoragetype?: "S3" | "MINIO" | "LOCAL" | "DATABASE";
  dtgstoragepath?: string;
  dtgsize?: number;
  dtgrecordcount?: number;
  dtglastsynced?: string;
  dtglastsyncstatus?: "SUCCESS" | "FAILED" | "PENDING";
  dtglastsyncerror?: string;
  dtgnextsync?: string;
  dtgcreatedat: string;
  dtgupdatedat?: string;
  dtgcreatedby?: number;
  dtgupdatedby?: number;
  dtgarchivedat?: string;
  dtgarchivedby?: number;
}

export interface DataGovernanceOrigin {
  idxorigin: number;
  iduuid: string;
  dtgorname: string;
  dtgordescription?: string;
  dtgortype: "INTERNAL" | "EXTERNAL";
  dtgorcategory: "DATABASE" | "API" | "FILE_SYSTEM" | "CLOUD_STORAGE" | "MARKETPLACE" | "WEB";
  dtgorconnectiontype?: string;
  dtgorconnectionconfig: string;
  dtgorcredentialsid?: number;
  dtgorurl?: string;
  dtgorpath?: string;
  dtgorhost?: string;
  dtgorport?: number;
  dtgordatabase?: string;
  dtgorschema?: string;
  dtgortable?: string;
  dtgorcollection?: string;
  dtgorauthmethod?: "NONE" | "BASIC" | "OAUTH2" | "API_KEY" | "CERTIFICATE";
  dtgorauthconfig?: string;
  dtgorsyncmethod?: "MANUAL" | "SCHEDULED" | "REAL_TIME" | "WEBHOOK";
  dtgorsyncschedule?: string;
  dtgorsyncfrequency?: "HOURLY" | "DAILY" | "WEEKLY" | "MONTHLY" | "ON_DEMAND";
  dtgorlastsynced?: string;
  dtgorlastsyncstatus?: "SUCCESS" | "FAILED" | "PENDING";
  dtgorlastsyncerror?: string;
  dtgornextsync?: string;
  dtgorvalidated?: boolean;
  dtgorvalidationrules?: string;
  dtgorqualityscore?: number;
  dtgoraccesslevel?: "PUBLIC" | "INTERNAL" | "PRIVATE" | "RESTRICTED";
  dtgorpermissions?: string;
  dtgorowner?: number;
  idxorganization?: number;
  idxproject?: number;
  dtgorstatus: "ACTIVE" | "INACTIVE" | "DEPRECATED" | "ERROR";
  dtgorenabled?: boolean;
  dtgormetadata?: string;
  dtgorprovider?: string;
  dtgorlicense?: string;
  dtgorterms?: string;
  dtgorcreatedat: string;
  dtgorupdatedat?: string;
  dtgorcreatedby?: number;
  dtgorupdatedby?: number;
}

export interface DataGovernanceDatasetListResponse {
  datasets: DataGovernanceDataset[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}

export interface DatasetFilters {
  projectId?: number;
  type?: string;
  originType?: string;
  status?: string;
  standardized?: boolean;
  search?: string;
}

export interface OriginFilters {
  type?: string;
  category?: string;
  provider?: string;
  projectId?: number;
  enabled?: boolean;
}

// ============================================================================
// MEJORA 1: Gestión de Riesgos de Datos
// ============================================================================

export interface DataGovernanceDatasetRisk {
  idxrisk: number;
  idxdataset: number;
  dtgrisktype: "QUALITY" | "BIAS" | "SECURITY" | "PRIVACITY" | "COMPLIANCE" | "LEGAL";
  dtgriskname: string;
  dtgriskdescription?: string;
  dtgriskprobability: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
  dtgriskimpact: "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
  dtgriskscore?: number; // Calculado: probability * impact
  dtgriskstatus: "IDENTIFIED" | "ASSESSED" | "TREATED" | "MONITORED" | "CLOSED";
  dtgmitigationplan?: string;
  dtgmitigationstatus?: "NOT_STARTED" | "IN_PROGRESS" | "COMPLETED";
  dtgresponsible?: number;
  dtgtargetdate?: string;
  dtgcreatedat: string;
  dtgcreatedby?: number;
}

// ============================================================================
// MEJORA 2: Métricas de Calidad Detalladas
// ============================================================================

export interface DataGovernanceQualityMetric {
  idxmetric: number;
  idxdataset: number;
  dtgqualitydimension: "COMPLETENESS" | "ACCURACY" | "CONSISTENCY" | "VALIDITY" | "TIMELINESS" | "UNIQUENESS";
  dtgqualityscore: number; // 0.00 - 1.00
  dtgqualitythreshold?: number; // Umbral mínimo aceptable
  dtgqualitystatus: "PASS" | "WARNING" | "FAIL";
  dtgqualitydetails?: Record<string, any>; // JSONB details
  dtgmetricdate: string;
  dtgmetricversion?: string;
}

// ============================================================================
// MEJORA 3: Gestión de Privacidad y GDPR
// ============================================================================

export interface DataGovernanceDatasetPrivacy {
  idxprivacy: number;
  idxdataset: number;
  dtgpiipresent: boolean;
  dtgpiitypes?: string; // Comma-separated: EMAIL, PHONE, SSN, etc.
  dtgpiianalysis?: Record<string, any>; // JSONB analysis
  dtglegalbasis?: "CONSENT" | "CONTRACT" | "LEGAL_OBLIGATION" | "VITAL_INTERESTS" | "PUBLIC_TASK" | "LEGITIMATE_INTERESTS";
  dtgconsentrequired: boolean;
  dtgconsentobtained?: boolean;
  dtgconsentdate?: string;
  dtgconsentmethod?: "EXPLICIT" | "IMPLICIT" | "OPT_IN" | "OPT_OUT";
  dtgretentionperiod?: number; // días
  dtgretentionpolicy?: string;
  dtgretentionstartdate?: string;
  dtgretentionenddate?: string;
  dtgautodelete?: boolean;
  dtgdpiacompleted?: boolean;
  dtgdpiadate?: string;
  dtgdpiaresult?: "LOW_RISK" | "MEDIUM_RISK" | "HIGH_RISK";
  dtgdpiarecommendations?: string;
  dtgrightsmanagement?: Record<string, any>; // JSONB derechos del interesado
  dtgropaentryid?: string;
  dtgropaupdatedat?: string;
  dtgcreatedat: string;
  dtgcreatedby?: number;
}

// ============================================================================
// MEJORA 4: Línea de Base (Data Lineage)
// ============================================================================

export interface DataGovernanceLineage {
  idxlineage: number;
  idxdataset: number;
  idxsourcedataset?: number;
  dtglineagetype?: "TRANSFORMATION" | "AGGREGATION" | "FILTER" | "JOIN" | "SPLIT" | "MERGE";
  dtglineagedetails?: Record<string, any>; // JSONB details
  dtglineagetimestamp?: string;
  dtglineageparameters?: Record<string, any>; // JSONB parameters
  dtglineageuser?: number;
  dtgcreatedat: string;
}

// ============================================================================
// MEJORA 5: Documentación y Trazabilidad de Decisiones
// ============================================================================

export interface DataGovernanceDatasetDocumentation {
  idxdoc: number;
  idxdataset: number;
  dtgdocumenttype: "DECISION" | "TRANSFORMATION" | "APPROVAL" | "REJECTION" | "CHANGE" | "INCIDENT";
  dtgdocumenttitle: string;
  dtgdocumentcontent: string;
  dtgdocumentauthor?: number;
  dtgdocumentdate: string;
  dtgdocumentversion?: string;
  dtgdocumentattachments?: string[]; // URLs de archivos
  dtgdocumenttags?: string; // Comma-separated tags
  dtgdocumentstatus?: "DRAFT" | "PUBLISHED" | "ARCHIVED";
}
