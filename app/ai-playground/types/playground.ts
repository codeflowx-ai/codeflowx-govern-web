// app/ai-playground/types/playground.ts
// Tipos mínimos para el módulo AI Playground usados por hooks y componentes.
// Mantener KISS: solo lo necesario para compilar y tipar correctamente las llamadas.

export type JsonValue =
  | string
  | number
  | boolean
  | null
  | { [key: string]: JsonValue }
  | JsonValue[];

export interface InferenceRequest {
  modelId: string;
  prompt?: string;
  input?: string;
  parameters?: Record<string, JsonValue>;
}

export interface ComparisonRequest extends Omit<InferenceRequest, "modelId"> {
  modelIds: string[];
}

export interface ModelResponse {
  id: string;
  modelId: string;
  content: string;
  tokens?: number;
  cost?: number;
  responseTime?: number;
  raw?: JsonValue;
}

export interface RAGRequest {
  modelId: string;
  query?: string;
  context?: JsonValue;
  topK?: number;
  parameters?: Record<string, JsonValue>;
}

export interface RAGSource {
  title: string;
  snippet: string;
  score?: number;
  url?: string;
}

export interface RAGResponse {
  id: string;
  content: string;
  sources: RAGSource[];
  modelId: string;
  responseTime?: number;
  raw?: JsonValue;
}

export interface TrainingRequest {
  datasetId?: string;
  modelId?: string;
  parameters?: Record<string, JsonValue>;
}

export type TrainingStatus = "pending" | "running" | "completed" | "failed";

export interface TrainingResponse {
  id: string;
  status: TrainingStatus;
  progress?: number;
  message?: string;
  raw?: JsonValue;
}
