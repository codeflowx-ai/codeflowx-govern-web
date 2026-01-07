"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import {
  ArrowLeft,
  BarChart3,
  BookOpen,
  Cloud,
  Edit,
  Eye,
  Plus,
  Save,
  Server,
  Tag,
  TrendingUp,
  Trash2,
} from "lucide-react";
import { useParams } from "next/navigation";
import Link from "next/link";
import { useEffect, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface Model {
  id: number;
  modelName: string;
  modelId: string;
  displayName: string;
  description: string;
  version: string;
  type: string;
  framework: string;
  status: string;
  approvalStatus?: string;
  approvedBy?: string;
  approvedAt?: string;
  providerId?: number;
  providerName?: string;
  currentStage?: string;
  lifecycleStage?: string;
  tags?: string[];
  versions?: ModelVersion[];
  associatedProjects?: AssociatedProject[];
  metrics?: ModelMetrics;
  mlopsIntegration?: string; // Plataforma MLOps asociada (databricks, sagemaker, etc.)
  mlopsConfig?: { // Configuración específica del modelo en la plataforma MLOps
    workspace?: string;
    modelPath?: string;
    modelName?: string;
    modelId?: string;
    endpointName?: string;
    endpointId?: string;
    version?: string;
    modelUri?: string;
    deploymentName?: string;
    runId?: string;
    runPath?: string;
  };
  // Precios de referencia (Pricing Reference)
  inputCostPer1M?: number; // Coste por millón de tokens de input (USD)
  outputCostPer1M?: number; // Coste por millón de tokens de output (USD)
  costPerImage?: number; // Coste por imagen (USD)
  costPerMinuteAudio?: number; // Coste por minuto de audio (USD)
  costPerMinuteVideo?: number; // Coste por minuto de video (USD)
  pricingCurrency?: string; // Moneda de precios (ISO 4217)
  pricingLastUpdate?: string; // Última actualización de precios
}

interface ModelVersion {
  id: number;
  version: string;
  description?: string;
  status: string;
  createdAt: string;
  createdBy: string;
  currentStage?: string;
  lifecycleStage?: string;
}

interface AssociatedProject {
  id: number;
  name: string;
  lastUsedAt?: string;
  usageCount: number;
}

interface ModelMetrics {
  totalRequests: number;
  successRate: number;
  averageLatency: number;
  accuracy: number;
  lastDeployedAt?: string;
  trends?: Array<{
    date: string;
    requests: number;
    successRate: number;
    latency: number;
    accuracy: number;
  }>;
}

interface ModelCosts {
  totalTokens: number;
  totalCost: number;
  costPerToken: number;
  monthlyTokens: number;
  monthlyCost: number;
  dailyTokens: number;
  dailyCost: number;
  byVersion: Array<{
    version: string;
    tokens: number;
    cost: number;
  }>;
  trends: Array<{
    date: string;
    tokens: number;
    cost: number;
  }>;
}

interface Provider {
  id: number;
  name: string;
  displayName: string;
}

interface ModelDeployment {
  id: number;
  name: string;
  status: string;
  endpoint?: string;
  namespace?: string;
  cluster?: string;
  provider?: string;
  baseUrl?: string;
  entrypointPath?: string;
  active: boolean;
  createdAt?: string;
  updatedAt?: string;
  config?: string;
}

interface ProviderInfo {
  id: number;
  name: string;
  displayName: string;
  baseUrl?: string;
  regions?: string[];
  healthCheckUrl?: string;
  documentationUrl?: string;
  type?: string; // EXTERNAL | INTERNAL
}

export default function ModelDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const id = params.id as string;
  const isNew = id === "new";

  // Verificar si hay parámetro edit en la URL
  const [isEditMode, setIsEditMode] = useState(false);
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const searchParams = new URLSearchParams(window.location.search);
      setIsEditMode(searchParams.get('edit') === 'true');
    }
  }, []);

  const [loading, setLoading] = useState(!isNew);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState(isNew || isEditMode);
  const [model, setModel] = useState<Model>({
    id: 0,
    modelName: "",
    modelId: "", // UUID generado automáticamente
    displayName: "",
    description: "",
    version: "", // Versión semántica automática
    type: "",
    framework: "",
    status: "",
    versions: [],
    associatedProjects: [],
    metrics: {
      totalRequests: 0,
      successRate: 0,
      averageLatency: 0,
      accuracy: 0,
    },
  });
  const [costs, setCosts] = useState<ModelCosts>({
    totalTokens: 0,
    totalCost: 0,
    costPerToken: 0,
    monthlyTokens: 0,
    monthlyCost: 0,
    dailyTokens: 0,
    dailyCost: 0,
    byVersion: [],
    trends: [
      { date: "2024-01-01", tokens: 0, cost: 0 },
    ],
  });
  const [providers, setProviders] = useState<Provider[]>([]);
  const [newVersion, setNewVersion] = useState({
    version: "",
    description: "",
    status: "",
  });
  const [showVersionForm, setShowVersionForm] = useState(false);
  const [editingVersion, setEditingVersion] = useState<ModelVersion | null>(null);
  const [selectedProject, setSelectedProject] = useState<AssociatedProject | null>(null);
  const [deployments, setDeployments] = useState<ModelDeployment[]>([]);
  const [providerInfo, setProviderInfo] = useState<ProviderInfo | null>(null);
  const [loadingDeployments, setLoadingDeployments] = useState(false);

  // Función helper para generar UUID v4
  const generateUUID = (): string => {
    // Intentar usar crypto.randomUUID si está disponible
    if (typeof crypto !== 'undefined' && crypto.randomUUID) {
      return crypto.randomUUID();
    }
    // Fallback: generar UUID v4 manualmente
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      const r = (Math.random() * 16) | 0;
      const v = c === 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  };

  useEffect(() => {
    if (!isNew) {
      loadModel();
      loadCosts();
    } else {
      // Generar UUID automáticamente para nuevo modelo
      const uuid = generateUUID();
      setModel(prev => ({ ...prev, modelId: uuid }));
      // Generar versión inicial automática
      setModel(prev => ({ ...prev, version: "1.0.0" }));
    }
    loadProviders();
  }, [id]);

  // Cargar deployments cuando el modelo esté cargado
  useEffect(() => {
    if (!isNew && model.id > 0) {
      loadDeployments();
    }
  }, [model.id, model.providerId]);

  const loadDeployments = async () => {
    if (isNew) return;

    try {
      setLoadingDeployments(true);

      // Cargar deployments internos
      try {
        // TODO: Implementar llamada API real
        // const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || ''}/api/v1/models/${id}/deployments`);
        // if (response.ok) {
        //   const data = await response.json();
        //   setDeployments(data);
        // }

        // Mock data para deployments internos
        const mockDeployments: ModelDeployment[] = [
          {
            id: 1,
            name: "production-deployment",
            status: "ACTIVE",
            endpoint: "https://models.example.com/gpt-4/v1",
            baseUrl: "https://models.example.com",
            entrypointPath: "/gpt-4/v1",
            namespace: "production",
            cluster: "k8s-cluster-1",
            provider: "AWS",
            active: true,
            createdAt: "2024-01-15T10:00:00",
            updatedAt: "2024-01-20T14:30:00",
          },
        ];
        setDeployments(mockDeployments);
      } catch (error) {
        console.error("Error loading deployments:", error);
        setDeployments([]);
      }

      // Si el modelo tiene provider, cargar información del provider (para modelos externos)
      if (model.providerId) {
        try {
          // TODO: Implementar llamada API real
          // const providerResponse = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || ''}/api/v1/providers/${model.providerId}`);
          // if (providerResponse.ok) {
          //   const providerData = await providerResponse.json();
          //   setProviderInfo({
          //     id: providerData.idxmodelprovider,
          //     name: providerData.modname,
          //     displayName: providerData.moddisplayname || providerData.modname,
          //     baseUrl: providerData.modbaseurl,
          //     regions: providerData.modavailableregions ? JSON.parse(providerData.modavailableregions) : [],
          //     healthCheckUrl: providerData.modhealthcheckurl,
          //     documentationUrl: providerData.moddocumentationurl,
          //     type: providerData.modprovidertype,
          //   });
          // }

        const mockProviderInfo: ProviderInfo = {
          id: model.providerId,
          name: model.providerName || "unknown",
          displayName: model.providerName || "Unknown Provider",
            baseUrl: "https://api.openai.com",
            regions: ["us-east-1", "eu-west-1", "asia-pacific-1"],
            healthCheckUrl: "https://status.openai.com",
            documentationUrl: "https://platform.openai.com/docs",
            type: "EXTERNAL",
          };
          setProviderInfo(mockProviderInfo);
        } catch (error) {
          console.error("Error loading provider info:", error);
        }
      }
    } catch (error) {
      console.error("Error loading deployments:", error);
    } finally {
      setLoadingDeployments(false);
    }
  };

  const loadProviders = async () => {
    try {
      // TODO: Implementar llamada API
      const mockProviders: Provider[] = [
        { id: 1, name: "openai", displayName: "OpenAI" },
        { id: 2, name: "anthropic", displayName: "Anthropic" },
        { id: 3, name: "google", displayName: "Google" },
      ];
      setProviders(mockProviders);
    } catch (error) {
      console.error("Error loading providers:", error);
    }
  };

  const loadModel = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/bff/compliance/models/${id}`);

      if (response.ok) {
        const data: Model = await response.json();
        setModel(data);
        return;
      }

      // Fallback a mock data si la API falla
      const modelIdNum = parseInt(id);
      let mockModel: Model;

      if (modelIdNum === 2) {
        // CodeFlowX Code Adapter
        mockModel = {
          id: modelIdNum,
          modelName: "CodeFlowX-CodeAdapter",
          modelId: "codeflowx-code-adapter-v1",
          displayName: "CodeFlowX Code Adapter",
          description: "Modelo propio con adapter especializado para generación y análisis de código",
          version: "2.1.0",
          type: "CODE",
          framework: "CodeFlowX",
          status: "ACTIVE",
          approvalStatus: "APPROVED",
          approvedBy: "admin",
          approvedAt: "2024-01-10T10:00:00",
          providerId: undefined,
          providerName: undefined,
          currentStage: "PRODUCTION",
          lifecycleStage: "ACTIVE",
          tags: ["code", "adapter", "internal"],
          versions: [
            { id: 1, version: "1.0.0", description: "Versión inicial", status: "ACTIVE", createdAt: "2023-12-01T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
            { id: 2, version: "2.0.0", description: "Mejoras de rendimiento", status: "ACTIVE", createdAt: "2024-01-01T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
            { id: 3, version: "2.1.0", description: "Nuevas funcionalidades de código", status: "ACTIVE", createdAt: "2024-01-10T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
          ],
          associatedProjects: [
            { id: 3, name: "Proyecto DevTools", lastUsedAt: "2024-01-20T14:30:00", usageCount: 3420 },
            { id: 4, name: "Proyecto CodeReview", lastUsedAt: "2024-01-19T09:15:00", usageCount: 2150 },
          ],
          metrics: {
            totalRequests: 87500,
            successRate: 97.8,
            averageLatency: 320,
            accuracy: 92.5,
            lastDeployedAt: "2024-01-10T10:00:00",
            trends: [
              { date: "2024-01-01", requests: 6200, successRate: 96.5, latency: 380, accuracy: 90.2 },
              { date: "2024-01-05", requests: 7800, successRate: 97.0, latency: 350, accuracy: 91.0 },
              { date: "2024-01-10", requests: 9200, successRate: 97.5, latency: 330, accuracy: 91.8 },
              { date: "2024-01-15", requests: 10500, successRate: 97.7, latency: 325, accuracy: 92.2 },
              { date: "2024-01-20", requests: 11200, successRate: 97.8, latency: 320, accuracy: 92.5 },
              { date: "2024-01-25", requests: 11800, successRate: 97.9, latency: 318, accuracy: 92.7 },
              { date: "2024-01-30", requests: 12400, successRate: 98.0, latency: 315, accuracy: 92.9 },
            ],
          },
          // Precios de referencia (modelo interno - sin costes)
          inputCostPer1M: undefined,
          outputCostPer1M: undefined,
          costPerImage: undefined,
          costPerMinuteAudio: undefined,
          costPerMinuteVideo: undefined,
          pricingCurrency: "USD",
          pricingLastUpdate: "2024-01-10T10:00:00",
        };
      } else if (modelIdNum === 3) {
        // Mistral Large
        mockModel = {
          id: modelIdNum,
          modelName: "Mistral-Large",
          modelId: "mistral-large",
          displayName: "Mistral Large",
          description: "Modelo de asistencia avanzada con capacidades de razonamiento",
          version: "1.0.0",
          type: "LLM",
          framework: "Mistral",
          status: "ACTIVE",
          approvalStatus: "APPROVED",
          approvedBy: "admin",
          approvedAt: "2024-01-12T10:00:00",
          providerId: 2,
          providerName: "Mistral AI",
          currentStage: "PRODUCTION",
          lifecycleStage: "ACTIVE",
          tags: ["llm", "assistance", "reasoning"],
          versions: [
            { id: 1, version: "1.0.0", description: "Versión inicial", status: "ACTIVE", createdAt: "2024-01-12T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
          ],
          associatedProjects: [
            { id: 5, name: "Proyecto Asistente", lastUsedAt: "2024-01-20T16:45:00", usageCount: 1800 },
          ],
          metrics: {
            totalRequests: 54200,
            successRate: 98.5,
            averageLatency: 380,
            accuracy: 94.8,
            lastDeployedAt: "2024-01-12T10:00:00",
            trends: [
              { date: "2024-01-12", requests: 4500, successRate: 98.0, latency: 420, accuracy: 93.5 },
              { date: "2024-01-15", requests: 6800, successRate: 98.3, latency: 400, accuracy: 94.0 },
              { date: "2024-01-18", requests: 7900, successRate: 98.4, latency: 390, accuracy: 94.5 },
              { date: "2024-01-20", requests: 8200, successRate: 98.5, latency: 380, accuracy: 94.8 },
            ],
          },
          // Precios de referencia Mistral
          inputCostPer1M: 2.70,
          outputCostPer1M: 8.10,
          costPerImage: undefined,
          costPerMinuteAudio: undefined,
          costPerMinuteVideo: undefined,
          pricingCurrency: "USD",
          pricingLastUpdate: "2024-01-12T10:00:00",
        };
      } else if (modelIdNum === 4) {
        // DeepSeek Coder
        mockModel = {
          id: modelIdNum,
          modelName: "DeepSeek-Coder",
          modelId: "deepseek-coder",
          displayName: "DeepSeek Coder",
          description: "Modelo especializado en programación y asistencia técnica",
          version: "1.5.2",
          type: "CODE",
          framework: "DeepSeek",
          status: "ACTIVE",
          approvalStatus: "APPROVED",
          approvedBy: "admin",
          approvedAt: "2024-01-08T10:00:00",
          providerId: 3,
          providerName: "DeepSeek",
          currentStage: "PRODUCTION",
          lifecycleStage: "ACTIVE",
          tags: ["code", "programming", "technical"],
          versions: [
            { id: 1, version: "1.0.0", description: "Versión inicial", status: "ACTIVE", createdAt: "2024-01-01T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
            { id: 2, version: "1.5.0", description: "Mejoras de código", status: "ACTIVE", createdAt: "2024-01-05T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
            { id: 3, version: "1.5.2", description: "Bug fixes", status: "ACTIVE", createdAt: "2024-01-08T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
          ],
          associatedProjects: [
            { id: 6, name: "Proyecto Programación", lastUsedAt: "2024-01-19T11:20:00", usageCount: 2890 },
          ],
          metrics: {
            totalRequests: 67200,
            successRate: 96.2,
            averageLatency: 290,
            accuracy: 91.3,
            lastDeployedAt: "2024-01-08T10:00:00",
            trends: [
              { date: "2024-01-01", requests: 4800, successRate: 95.0, latency: 320, accuracy: 89.5 },
              { date: "2024-01-05", requests: 6800, successRate: 95.8, latency: 305, accuracy: 90.5 },
              { date: "2024-01-08", requests: 8200, successRate: 96.2, latency: 290, accuracy: 91.3 },
              { date: "2024-01-12", requests: 9500, successRate: 96.3, latency: 285, accuracy: 91.5 },
              { date: "2024-01-15", requests: 10200, successRate: 96.4, latency: 280, accuracy: 91.7 },
              { date: "2024-01-18", requests: 10800, successRate: 96.5, latency: 275, accuracy: 91.9 },
              { date: "2024-01-20", requests: 11400, successRate: 96.6, latency: 270, accuracy: 92.1 },
            ],
          },
          // Precios de referencia DeepSeek
          inputCostPer1M: 0.14,
          outputCostPer1M: 0.28,
          costPerImage: undefined,
          costPerMinuteAudio: undefined,
          costPerMinuteVideo: undefined,
          pricingCurrency: "USD",
          pricingLastUpdate: "2024-01-08T10:00:00",
        };
      } else {
        // Modelo por defecto: GPT-4
        mockModel = {
          id: modelIdNum,
          modelName: "GPT-4",
          modelId: "550e8400-e29b-41d4-a716-446655440000",
          displayName: "GPT-4",
          description: "Large language model",
          version: "1.2.3",
          type: "LLM",
          framework: "OpenAI",
          status: "ACTIVE",
          approvalStatus: "APPROVED",
          approvedBy: "admin",
          approvedAt: "2024-01-15T10:30:00",
          providerId: 1,
          providerName: "OpenAI",
          currentStage: "PRODUCTION",
          lifecycleStage: "ACTIVE",
          tags: ["nlp", "llm", "generative"],
          versions: [
            { id: 1, version: "1.0.0", description: "Initial release", status: "ACTIVE", createdAt: "2024-01-01T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
            { id: 2, version: "1.1.0", description: "Performance improvements", status: "ACTIVE", createdAt: "2024-01-10T00:00:00", createdBy: "admin", currentStage: "STAGING", lifecycleStage: "ACTIVE" },
            { id: 3, version: "1.2.3", description: "Bug fixes and optimizations", status: "ACTIVE", createdAt: "2024-01-15T00:00:00", createdBy: "admin", currentStage: "PRODUCTION", lifecycleStage: "ACTIVE" },
          ],
          associatedProjects: [
            { id: 1, name: "Proyecto Demo", lastUsedAt: "2024-01-15T10:30:00", usageCount: 1250 },
            { id: 2, name: "Proyecto RAG", lastUsedAt: "2024-01-14T15:20:00", usageCount: 890 },
          ],
          metrics: {
            totalRequests: 125000,
            successRate: 99.5,
            averageLatency: 450,
            accuracy: 95.2,
            lastDeployedAt: "2024-01-15T10:30:00",
            trends: [
              { date: "2024-01-01", requests: 8500, successRate: 98.2, latency: 520, accuracy: 93.5 },
              { date: "2024-01-05", requests: 10200, successRate: 98.8, latency: 480, accuracy: 94.1 },
              { date: "2024-01-10", requests: 11500, successRate: 99.1, latency: 465, accuracy: 94.8 },
              { date: "2024-01-15", requests: 12500, successRate: 99.5, latency: 450, accuracy: 95.2 },
              { date: "2024-01-20", requests: 13200, successRate: 99.6, latency: 445, accuracy: 95.5 },
              { date: "2024-01-25", requests: 14000, successRate: 99.7, latency: 440, accuracy: 95.8 },
              { date: "2024-01-30", requests: 14800, successRate: 99.8, latency: 435, accuracy: 96.0 },
            ],
          },
          // Precios de referencia GPT-4
          inputCostPer1M: 30.00,
          outputCostPer1M: 60.00,
          costPerImage: 0.040, // GPT-4 Vision
          costPerMinuteAudio: undefined,
          costPerMinuteVideo: undefined,
          pricingCurrency: "USD",
          pricingLastUpdate: "2024-01-15T10:30:00",
        };
      }

      setModel(mockModel);
    } catch (error) {
      console.error("Error loading model:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadCosts = async () => {
    try {
      const response = await fetch(`/api/v1/bff/compliance/models/${id}/costs`);

      if (response.ok) {
        const data: ModelCosts = await response.json();
        setCosts(data);
        return;
      }

      // Fallback a mock data si la API falla
      const mockCosts: ModelCosts = {
        totalTokens: 12500000,
        totalCost: 1250.50,
        costPerToken: 0.0001,
        monthlyTokens: 5000000,
        monthlyCost: 500.00,
        dailyTokens: 166666,
        dailyCost: 16.67,
        byVersion: [
          { version: "1.2.3", tokens: 3000000, cost: 300.00 },
          { version: "1.1.0", tokens: 1500000, cost: 150.00 },
          { version: "1.0.0", tokens: 500000, cost: 50.00 },
        ],
        trends: [
          { date: "2024-01-01", tokens: 120000, cost: 12.00 },
          { date: "2024-01-05", tokens: 145000, cost: 14.50 },
          { date: "2024-01-10", tokens: 162000, cost: 16.20 },
          { date: "2024-01-15", tokens: 178000, cost: 17.80 },
          { date: "2024-01-20", tokens: 195000, cost: 19.50 },
          { date: "2024-01-25", tokens: 210000, cost: 21.00 },
          { date: "2024-01-30", tokens: 225000, cost: 22.50 },
        ],
      };
      setCosts(mockCosts);
    } catch (error) {
      console.error("Error loading costs:", error);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      // TODO: Implementar llamada API
      if (isNew) {
        // Crear nuevo modelo
        window.location.href = "/models/registry";
      } else {
        // Actualizar modelo existente
        setEditing(false);
      }
    } catch (error) {
      console.error("Error saving model:", error);
    } finally {
      setSaving(false);
    }
  };

  const calculateNextVersion = (currentVersion: string, versionType: 'major' | 'minor' | 'patch' = 'patch'): string => {
    const parts = currentVersion.split('.').map(Number);
    if (parts.length !== 3) return "1.0.0";

    if (versionType === 'major') {
      return `${parts[0] + 1}.0.0`;
    } else if (versionType === 'minor') {
      return `${parts[0]}.${parts[1] + 1}.0`;
    } else {
      return `${parts[0]}.${parts[1]}.${parts[2] + 1}`;
    }
  };

  const handleCreateVersion = async () => {
    if (!newVersion.status) {
      alert(t("governance.models.registry.detail.versions.validationError", "Por favor, complete todos los campos obligatorios"));
      return;
    }

    try {
      // TODO: Implementar llamada API
      if (editingVersion) {
        // Actualizar versión existente (solo descripción y estado, no la versión)
        const updatedVersion: ModelVersion = {
          ...editingVersion,
          description: newVersion.description,
          status: newVersion.status,
        };

        setModel({
          ...model,
          versions: model.versions?.map(v => v.id === editingVersion.id ? updatedVersion : v) || [],
        });
        setEditingVersion(null);
      } else {
        // Crear nueva versión - calcular automáticamente
        const latestVersion = model.versions && model.versions.length > 0
          ? model.versions[model.versions.length - 1].version
          : model.version || "1.0.0";
        const nextVersion = calculateNextVersion(latestVersion, 'patch');

        const newVer: ModelVersion = {
          id: (model.versions?.length || 0) + 1,
          version: nextVersion,
          description: newVersion.description,
          status: newVersion.status,
          createdAt: new Date().toISOString(),
          createdBy: "current_user", // TODO: Obtener del contexto
        };

        setModel({
          ...model,
          versions: [...(model.versions || []), newVer],
          version: nextVersion, // Actualizar versión actual del modelo
        });
      }

      setNewVersion({
        version: "",
        description: "",
        status: "",
      });
      setShowVersionForm(false);
    } catch (error) {
      console.error("Error saving version:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    if (status === "ACTIVE") {
      return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">ACTIVE</Badge>;
    } else if (status === "INACTIVE") {
      return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">INACTIVE</Badge>;
    } else if (status === "PENDING") {
      return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">PENDING</Badge>;
    }
    return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">{status}</Badge>;
  };

  if (loading) {
    return (
      <div className="space-y-4 p-6">
        <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
      </div>
    );
  }

  return (
    <div className="space-y-4 p-6">
      {/* Header - Título y Subtítulo */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <BookOpen className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {isNew
              ? t("governance.models.registry.detail.createTitle", "Nuevo Modelo")
              : model.displayName || model.modelName}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {isNew
            ? t("governance.models.registry.detail.createSubtitle", "Crear un nuevo modelo")
            : t("governance.models.registry.detail.subtitle", "Detalles y gestión del modelo")}
        </p>
      </div>

      {/* Botones de Acción - Volver a la izquierda, Acciones a la derecha */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => { window.location.href = "/models/registry"; }}
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex items-center gap-2">
          {!isNew && !editing && (
            <Button
              variant="outline"
              onClick={() => setEditing(true)}
            >
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Editar")}
            </Button>
          )}
          {(editing || isNew) && (
            <>
              <Button
                variant="outline"
                onClick={() => {
                  if (isNew) {
                    window.location.href = "/models/registry";
                  } else {
                    setEditing(false);
                    loadModel();
                  }
                }}
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleSave}
                disabled={saving}
                className="bg-primary hover:bg-primary/90"
              >
                <Save className="w-4 h-4 mr-2" />
                {saving ? t("common.saving", "Guardando...") : t("common.save", "Guardar")}
              </Button>
            </>
          )}
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="general" className="space-y-6">
        <TabsList className="w-full">
          <TabsTrigger value="general" className="flex-1">
            {t("governance.models.registry.detail.tabs.general", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="versions" className="flex-1">
            {t("governance.models.registry.detail.tabs.versions", "Versiones")}
          </TabsTrigger>
          <TabsTrigger value="projects" className="flex-1">
            {t("governance.models.registry.detail.tabs.projects", "Proyectos")}
          </TabsTrigger>
          <TabsTrigger value="costs" className="flex-1">
            {t("governance.models.registry.detail.tabs.costs", "Costes y Consumo")}
          </TabsTrigger>
          <TabsTrigger value="metrics" className="flex-1">
            {t("governance.models.registry.detail.tabs.metrics", "Métricas")}
          </TabsTrigger>
          <TabsTrigger value="deployments" className="flex-1">
            {t("governance.models.registry.detail.tabs.deployments", "Despliegues")}
          </TabsTrigger>
          <TabsTrigger value="integrations" className="flex-1">
            {t("governance.models.registry.detail.tabs.integrations", "Integraciones")}
          </TabsTrigger>
          <TabsTrigger value="testing" className="flex-1">
            {t("governance.models.registry.detail.tabs.testing", "Testing")}
          </TabsTrigger>
        </TabsList>

        {/* Tab: Información General */}
        <TabsContent value="general" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.models.registry.detail.general.title", "Información del Modelo")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.modal.create.modelName", "Nombre del Modelo")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={model.modelName}
                      onChange={(e) => setModel({ ...model, modelName: e.target.value })}
                      placeholder={t("governance.models.registry.modal.create.modelNamePlaceholder", "Ingrese el nombre del modelo")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{model.modelName}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.modal.create.modelId", "ID del Modelo (UUID)")}
                  </label>
                  <p className="text-sm text-foreground font-mono text-muted-foreground">
                    {model.modelId || t("governance.models.registry.detail.general.uuidAutoGenerated", "Se generará automáticamente")}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {t("governance.models.registry.detail.general.uuidInfo", "UUID generado automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.modal.create.displayName", "Nombre de Visualización")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={model.displayName}
                      onChange={(e) => setModel({ ...model, displayName: e.target.value })}
                      placeholder={t("governance.models.registry.modal.create.displayNamePlaceholder", "Ingrese el nombre de visualización")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{model.displayName}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.modal.create.version", "Versión")}
                  </label>
                  <p className="text-sm text-foreground font-mono">
                    {model.version || "1.0.0"}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {t("governance.models.registry.detail.general.versionAutoGenerated", "Versión semántica generada automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.provider.name", "Proveedor")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={model.providerId?.toString() || ""}
                      onValueChange={(value) => {
                        const provider = providers.find(p => p.id === parseInt(value));
                        setModel({
                          ...model,
                          providerId: provider?.id,
                          providerName: provider?.displayName,
                        });
                      }}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.registry.detail.provider.placeholder", "Seleccione un proveedor")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="">{t("governance.models.registry.detail.provider.none", "Sin proveedor")}</SelectItem>
                        {providers.map((provider) => (
                          <SelectItem key={provider.id} value={provider.id.toString()}>
                            {provider.displayName}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">
                        {model.providerName || <span className="text-muted-foreground">-</span>}
                      </p>
                      {model.providerId && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => { window.location.href = `/models/providers/${model.providerId}`; }}
                          title={t("governance.models.registry.detail.provider.viewProvider", "Ver Detalles del Proveedor")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.general.type", "Tipo")} *
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={model.type}
                      onValueChange={(value) => setModel({ ...model, type: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.registry.detail.general.typePlaceholder", "Seleccione el tipo")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="LLM">LLM</SelectItem>
                        <SelectItem value="EMBEDDING">EMBEDDING</SelectItem>
                        <SelectItem value="CLASSIFICATION">CLASSIFICATION</SelectItem>
                        <SelectItem value="REGRESSION">REGRESSION</SelectItem>
                        <SelectItem value="OTHER">OTHER</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <p className="text-sm text-foreground">{model.type}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.general.framework", "Framework")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={model.framework}
                      onChange={(e) => setModel({ ...model, framework: e.target.value })}
                      placeholder={t("governance.models.registry.detail.general.frameworkPlaceholder", "Ingrese el framework")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{model.framework || "-"}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.general.status", "Estado")} *
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={model.status}
                      onValueChange={(value) => setModel({ ...model, status: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.registry.detail.general.statusPlaceholder", "Seleccione el estado")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                        <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                        <SelectItem value="PENDING">PENDING</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <div>{getStatusBadge(model.status)}</div>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.general.currentStage", "Etapa Actual")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={model.currentStage || ""}
                      onValueChange={(value) => setModel({ ...model, currentStage: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.registry.detail.general.currentStagePlaceholder", "Seleccione la etapa")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="DEVELOPMENT">DEVELOPMENT</SelectItem>
                        <SelectItem value="STAGING">STAGING</SelectItem>
                        <SelectItem value="PRODUCTION">PRODUCTION</SelectItem>
                        <SelectItem value="ARCHIVED">ARCHIVED</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <p className="text-sm text-foreground">{model.currentStage || "-"}</p>
                  )}
                </div>
                <div className="space-y-2 md:col-span-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.modal.create.description", "Descripción")}
                  </label>
                  {editing || isNew ? (
                    <Textarea
                      value={model.description}
                      onChange={(e) => setModel({ ...model, description: e.target.value })}
                      placeholder={t("governance.models.registry.modal.create.descriptionPlaceholder", "Ingrese una descripción del modelo")}
                      rows={3}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{model.description || "-"}</p>
                  )}
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Precios de Referencia */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.models.registry.detail.pricing.title", "Precios de Referencia")}</CardTitle>
              <p className="text-sm text-muted-foreground mt-1">
                {t("governance.models.registry.detail.pricing.subtitle", "Precios del proveedor como referencia (no es control presupuestario exhaustivo)")}
              </p>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.inputCostPer1M", "Coste por Millón Tokens Input (USD)")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      type="number"
                      step="0.01"
                      value={model.inputCostPer1M?.toString() || ""}
                      onChange={(e) => setModel({ ...model, inputCostPer1M: e.target.value ? parseFloat(e.target.value) : undefined })}
                      placeholder="2.50"
                    />
                  ) : (
                    <p className="text-sm text-foreground">
                      {model.inputCostPer1M !== undefined ? `$${model.inputCostPer1M.toFixed(2)}` : "-"}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.outputCostPer1M", "Coste por Millón Tokens Output (USD)")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      type="number"
                      step="0.01"
                      value={model.outputCostPer1M?.toString() || ""}
                      onChange={(e) => setModel({ ...model, outputCostPer1M: e.target.value ? parseFloat(e.target.value) : undefined })}
                      placeholder="10.00"
                    />
                  ) : (
                    <p className="text-sm text-foreground">
                      {model.outputCostPer1M !== undefined ? `$${model.outputCostPer1M.toFixed(2)}` : "-"}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.costPerImage", "Coste por Imagen (USD)")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      type="number"
                      step="0.001"
                      value={model.costPerImage?.toString() || ""}
                      onChange={(e) => setModel({ ...model, costPerImage: e.target.value ? parseFloat(e.target.value) : undefined })}
                      placeholder="0.040"
                    />
                  ) : (
                    <p className="text-sm text-foreground">
                      {model.costPerImage !== undefined ? `$${model.costPerImage.toFixed(3)}` : "-"}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.costPerMinuteAudio", "Coste por Minuto Audio (USD)")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      type="number"
                      step="0.01"
                      value={model.costPerMinuteAudio?.toString() || ""}
                      onChange={(e) => setModel({ ...model, costPerMinuteAudio: e.target.value ? parseFloat(e.target.value) : undefined })}
                      placeholder="0.15"
                    />
                  ) : (
                    <p className="text-sm text-foreground">
                      {model.costPerMinuteAudio !== undefined ? `$${model.costPerMinuteAudio.toFixed(2)}` : "-"}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.costPerMinuteVideo", "Coste por Minuto Video (USD)")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      type="number"
                      step="0.01"
                      value={model.costPerMinuteVideo?.toString() || ""}
                      onChange={(e) => setModel({ ...model, costPerMinuteVideo: e.target.value ? parseFloat(e.target.value) : undefined })}
                      placeholder="0.30"
                    />
                  ) : (
                    <p className="text-sm text-foreground">
                      {model.costPerMinuteVideo !== undefined ? `$${model.costPerMinuteVideo.toFixed(2)}` : "-"}
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.pricing.currency", "Moneda")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={model.pricingCurrency || "USD"}
                      onValueChange={(value) => setModel({ ...model, pricingCurrency: value })}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="USD">USD</SelectItem>
                        <SelectItem value="EUR">EUR</SelectItem>
                        <SelectItem value="GBP">GBP</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <p className="text-sm text-foreground">{model.pricingCurrency || "USD"}</p>
                  )}
                </div>
                {model.pricingLastUpdate && (
                  <div className="space-y-2">
                    <label className="text-sm font-medium">
                      {t("governance.models.registry.detail.pricing.lastUpdate", "Última Actualización")}
                    </label>
                    <p className="text-sm text-muted-foreground">
                      {new Date(model.pricingLastUpdate).toLocaleString()}
                    </p>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Versiones */}
        <TabsContent value="versions" className="space-y-6">
          <Card className="border-2">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Tag className="w-5 h-5" />
                {t("governance.models.registry.detail.tabs.versions", "Versiones")}
              </CardTitle>
              <Button
                onClick={() => {
                  setEditingVersion(null);
                  setNewVersion({
                    version: "",
                    description: "",
                    status: "",
                  });
                  setShowVersionForm(!showVersionForm);
                }}
                className="bg-primary hover:bg-primary/90"
                size="sm"
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("governance.models.registry.detail.versions.addButton", "Agregar Versión")}
              </Button>
            </CardHeader>
            <CardBody>
              {showVersionForm && (
                <Card className="border-2 mb-6">
                  <CardHeader>
                    <CardTitle className="text-lg">
                      {editingVersion
                        ? t("governance.models.registry.detail.versions.editTitle", "Editar Versión")
                        : t("governance.models.registry.detail.versions.createTitle", "Agregar Versión")}
                    </CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.registry.modal.create.version", "Versión")}
                        </label>
                        <p className="text-sm text-foreground font-mono bg-muted p-2 rounded">
                          {editingVersion
                            ? editingVersion.version
                            : model.versions && model.versions.length > 0
                              ? calculateNextVersion(model.versions[model.versions.length - 1].version)
                              : calculateNextVersion(model.version || "1.0.0")}
                        </p>
                        <p className="text-xs text-muted-foreground">
                          {t("governance.models.registry.detail.versions.autoGenerated", "Generada automáticamente")}
                        </p>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.registry.detail.general.status", "Estado")} *
                        </label>
                        <Select value={newVersion.status} onValueChange={(value) => setNewVersion({ ...newVersion, status: value })}>
                          <SelectTrigger>
                            <SelectValue placeholder={t("governance.models.registry.detail.general.statusPlaceholder", "Seleccione el estado")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                            <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                            <SelectItem value="PENDING">PENDING</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2 md:col-span-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.registry.modal.create.description", "Descripción")}
                        </label>
                        <Textarea
                          placeholder={t("governance.models.registry.modal.create.descriptionPlaceholder", "Ingrese una descripción")}
                          value={newVersion.description}
                          onChange={(e) => setNewVersion({ ...newVersion, description: e.target.value })}
                          rows={3}
                        />
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4">
                      <Button
                        variant="outline"
                        onClick={() => {
                          setShowVersionForm(false);
                          setEditingVersion(null);
                          setNewVersion({
                            version: "",
                            description: "",
                            status: "",
                          });
                        }}
                      >
                        {t("common.cancel", "Cancelar")}
                      </Button>
                      <Button onClick={handleCreateVersion} className="bg-primary hover:bg-primary/90">
                        {editingVersion
                          ? t("common.save", "Guardar")
                          : t("governance.models.registry.detail.versions.addButton", "Agregar Versión")}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              )}

              {model.versions && model.versions.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.registry.modal.create.version", "Versión")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.modal.create.description", "Descripción")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.detail.general.status", "Estado")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.detail.versions.createdAt", "Creado")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.detail.versions.createdBy", "Creado Por")}</th>
                        <th className="text-center p-2">{t("governance.models.registry.detail.versions.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {model.versions.map((version) => (
                        <tr key={version.id} className="border-b hover:bg-muted/50">
                          <td className="p-2 font-mono">{version.version}</td>
                          <td className="p-2">{version.description || <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{getStatusBadge(version.status)}</td>
                          <td className="p-2">{new Date(version.createdAt).toLocaleString()}</td>
                          <td className="p-2">{version.createdBy}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.edit", "Editar")}
                                onClick={() => {
                                  setEditingVersion(version);
                                  setNewVersion({
                                    version: version.version,
                                    description: version.description || "",
                                    status: version.status,
                                  });
                                  setShowVersionForm(true);
                                }}
                              >
                                <Edit className="w-4 h-4" />
                              </Button>
                              <Button
                                variant="ghost"
                                size="sm"
                                className="text-red-400 hover:text-red-300"
                                title={t("common.delete", "Eliminar")}
                                onClick={() => {
                                  if (confirm(t("common.confirmDelete", "¿Está seguro de eliminar esta versión?"))) {
                                    setModel({
                                      ...model,
                                      versions: model.versions?.filter(v => v.id !== version.id) || [],
                                    });
                                  }
                                }}
                              >
                                <Trash2 className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.models.registry.detail.versions.noVersions", "No hay versiones registradas")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Costes y Consumo */}
        <TabsContent value="costs" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.costs.totalTokens", "Total Tokens")}
                </p>
                <h2 className="text-2xl font-bold">{costs.totalTokens.toLocaleString()}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.costs.totalCost", "Coste Total")}
                </p>
                <h2 className="text-2xl font-bold">${costs.totalCost.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.costs.monthlyCost", "Coste Mensual")}
                </p>
                <h2 className="text-2xl font-bold">${costs.monthlyCost.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.costs.costPerToken", "Coste por Token")}
                </p>
                <h2 className="text-2xl font-bold">${costs.costPerToken.toFixed(6)}</h2>
              </CardBody>
            </Card>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card className="border-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <BarChart3 className="w-5 h-5" />
                  {t("governance.models.registry.detail.costs.byVersion", "Consumo por Versión")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                {costs.byVersion && costs.byVersion.length > 0 ? (
                  <div className="space-y-2">
                    {costs.byVersion.map((item, index) => (
                      <div key={index} className="flex justify-between items-center p-2 border rounded">
                        <div>
                          <span className="text-sm font-semibold">{item.version}</span>
                          <p className="text-xs text-muted-foreground">{item.tokens.toLocaleString()} tokens</p>
                        </div>
                        <span className="text-sm font-semibold">${item.cost.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="text-center py-8 text-muted-foreground">
                    {t("governance.models.registry.detail.costs.noData", "No hay datos de consumo")}
                  </div>
                )}
              </CardBody>
            </Card>

            <Card className="border-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="w-5 h-5" />
                  {t("governance.models.registry.detail.costs.trends", "Tendencias")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                {costs.trends && costs.trends.length > 0 ? (
                  <div className="h-80">
                    <Line
                      data={{
                        labels: costs.trends.map(t => {
                          const date = new Date(t.date);
                          return date.toLocaleDateString('es-ES', { month: 'short', day: 'numeric' });
                        }),
                        datasets: [
                          {
                            label: t("governance.models.registry.detail.costs.tokens", "Tokens"),
                            data: costs.trends.map(t => t.tokens),
                            borderColor: 'rgb(59, 130, 246)',
                            backgroundColor: 'rgba(59, 130, 246, 0.1)',
                            fill: true,
                            tension: 0.4,
                            yAxisID: 'y',
                          },
                          {
                            label: t("governance.models.registry.detail.costs.cost", "Coste (€)"),
                            data: costs.trends.map(t => t.cost),
                            borderColor: 'rgb(34, 197, 94)',
                            backgroundColor: 'rgba(34, 197, 94, 0.1)',
                            fill: true,
                            tension: 0.4,
                            yAxisID: 'y1',
                          },
                        ],
                      }}
                      options={{
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                          legend: {
                            position: 'top' as const,
                          },
                          title: {
                            display: false,
                          },
                          tooltip: {
                            mode: 'index',
                            intersect: false,
                            callbacks: {
                              label: function(context) {
                                let label = context.dataset.label || '';
                                if (label) {
                                  label += ': ';
                                }
                                if (context.parsed.y !== null) {
                                  if (context.datasetIndex === 0) {
                                    // Tokens
                                    label += context.parsed.y.toLocaleString('es-ES');
                                  } else {
                                    // Coste
                                    label += context.parsed.y.toFixed(2) + ' €';
                                  }
                                }
                                return label;
                              },
                            },
                          },
                        },
                        scales: {
                          y: {
                            type: 'linear' as const,
                            display: true,
                            position: 'left' as const,
                            beginAtZero: true,
                            title: {
                              display: true,
                              text: t("governance.models.registry.detail.costs.tokens", "Tokens"),
                            },
                            ticks: {
                              callback: function(value) {
                                return value.toLocaleString('es-ES');
                              },
                            },
                          },
                          y1: {
                            type: 'linear' as const,
                            display: true,
                            position: 'right' as const,
                            beginAtZero: true,
                            grid: {
                              drawOnChartArea: false,
                            },
                            title: {
                              display: true,
                              text: t("governance.models.registry.detail.costs.cost", "Coste (€)"),
                            },
                            ticks: {
                              callback: function(value) {
                                return (typeof value === 'number' ? value.toFixed(2) : value) + ' €';
                              },
                            },
                          },
                        },
                        interaction: {
                          mode: 'nearest',
                          axis: 'x',
                          intersect: false,
                        },
                      }}
                    />
                  </div>
                ) : (
                  <div className="h-64 border rounded-lg flex items-center justify-center">
                    <p className="text-muted-foreground">
                      {t("governance.models.registry.detail.costs.noTrendsData", "No hay datos de tendencias disponibles")}
                    </p>
                  </div>
                )}
              </CardBody>
            </Card>
          </div>
        </TabsContent>

        {/* Tab: Proyectos */}
        <TabsContent value="projects" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="w-5 h-5" />
                {t("governance.models.registry.detail.tabs.projects", "Proyectos Asociados")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {model.associatedProjects && model.associatedProjects.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.registry.detail.projects.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.detail.projects.usageCount", "Usos")}</th>
                        <th className="text-left p-2">{t("governance.models.registry.detail.projects.lastUsed", "Último Uso")}</th>
                        <th className="text-center p-2">{t("governance.models.registry.detail.projects.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {model.associatedProjects.map((project) => (
                        <tr key={project.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{project.name}</td>
                          <td className="p-2">{project.usageCount.toLocaleString()}</td>
                          <td className="p-2">{project.lastUsedAt ? new Date(project.lastUsedAt).toLocaleString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.view", "Ver Proyecto")}
                                onClick={() => setSelectedProject(project)}
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.models.registry.detail.projects.noProjects", "No hay proyectos asociados")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Métricas */}
        <TabsContent value="metrics" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.metrics.totalRequests", "Total Requests")}
                </p>
                <h2 className="text-2xl font-bold">{model.metrics?.totalRequests.toLocaleString() || 0}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.metrics.successRate", "Tasa de Éxito")}
                </p>
                <h2 className="text-2xl font-bold">{model.metrics?.successRate.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.metrics.averageLatency", "Latencia Promedio")}
                </p>
                <h2 className="text-2xl font-bold">{model.metrics?.averageLatency || 0}ms</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.registry.detail.metrics.accuracy", "Precisión")}
                </p>
                <h2 className="text-2xl font-bold">{model.metrics?.accuracy.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
          </div>

          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5" />
                {t("governance.models.registry.detail.metrics.trends", "Tendencias")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {model.metrics?.trends && model.metrics.trends.length > 0 ? (
                <div className="h-80">
                  <Line
                    data={{
                      labels: model.metrics.trends.map(t => {
                        const date = new Date(t.date);
                        return date.toLocaleDateString('es-ES', { month: 'short', day: 'numeric' });
                      }),
                      datasets: [
                        {
                          label: t("governance.models.registry.detail.metrics.successRate", "Tasa de Éxito (%)"),
                          data: model.metrics.trends.map(t => t.successRate),
                          borderColor: 'rgb(34, 197, 94)',
                          backgroundColor: 'rgba(34, 197, 94, 0.1)',
                          fill: true,
                          tension: 0.4,
                          yAxisID: 'y',
                        },
                        {
                          label: t("governance.models.registry.detail.metrics.averageLatency", "Latencia (ms)"),
                          data: model.metrics.trends.map(t => t.latency),
                          borderColor: 'rgb(59, 130, 246)',
                          backgroundColor: 'rgba(59, 130, 246, 0.1)',
                          fill: true,
                          tension: 0.4,
                          yAxisID: 'y1',
                        },
                        {
                          label: t("governance.models.registry.detail.metrics.accuracy", "Precisión (%)"),
                          data: model.metrics.trends.map(t => t.accuracy),
                          borderColor: 'rgb(168, 85, 247)',
                          backgroundColor: 'rgba(168, 85, 247, 0.1)',
                          fill: true,
                          tension: 0.4,
                          yAxisID: 'y',
                        },
                      ],
                    }}
                    options={{
                      responsive: true,
                      maintainAspectRatio: false,
                      plugins: {
                        legend: {
                          position: 'top' as const,
                        },
                        title: {
                          display: false,
                        },
                        tooltip: {
                          mode: 'index',
                          intersect: false,
                        },
                      },
                      scales: {
                        y: {
                          type: 'linear' as const,
                          display: true,
                          position: 'left' as const,
                          beginAtZero: false,
                          title: {
                            display: true,
                            text: t("governance.models.registry.detail.metrics.percentage", "Porcentaje (%)"),
                          },
                        },
                        y1: {
                          type: 'linear' as const,
                          display: true,
                          position: 'right' as const,
                          beginAtZero: false,
                          grid: {
                            drawOnChartArea: false,
                          },
                          title: {
                            display: true,
                            text: t("governance.models.registry.detail.metrics.latency", "Latencia (ms)"),
                          },
                        },
                      },
                      interaction: {
                        mode: 'nearest',
                        axis: 'x',
                        intersect: false,
                      },
                    }}
                  />
                </div>
              ) : (
                <div className="h-64 border rounded-lg flex items-center justify-center">
                  <p className="text-muted-foreground">
                    {t("governance.models.registry.detail.metrics.noTrendsData", "No hay datos de tendencias disponibles")}
                  </p>
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Despliegues */}
        <TabsContent value="deployments" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="h-5 w-5" />
                {t("governance.models.registry.detail.tabs.deployments", "Información de Despliegue")}
              </CardTitle>
            </CardHeader>
            <CardBody className="space-y-6">
              {/* Despliegues Internos */}
              {deployments.length > 0 && (
                <div>
                  <h3 className="text-lg font-semibold mb-4">
                    {t("governance.models.registry.detail.deployments.internal", "Despliegues Internos")}
                  </h3>
                  <div className="space-y-4">
                    {deployments.map((deployment) => (
                      <Card key={deployment.id} className="border-l-4 border-l-blue-500">
                        <CardBody className="space-y-3">
                          <div className="flex items-center justify-between">
                            <div className="flex items-center gap-3">
                              <h4 className="font-semibold text-lg">{deployment.name}</h4>
                               <Badge variant={deployment.status === "ACTIVE" ? "primary" : "secondary"}>
                                {deployment.status}
                              </Badge>
                              {deployment.active && (
                                <Badge variant="outline" className="bg-green-50 text-green-700 border-green-200">
                                  {t("governance.models.registry.detail.deployments.active", "Activo")}
                                </Badge>
                              )}
                            </div>
                          </div>
                          <div className="grid grid-cols-2 gap-4 text-sm">
                            {deployment.endpoint && (
                              <div>
                                <span className="text-gray-500">
                                  {t("governance.models.registry.detail.deployments.endpoint", "Endpoint")}:
                                </span>
                                <span className="ml-2 font-mono text-blue-600">{deployment.endpoint}</span>
                              </div>
                            )}
                            {deployment.namespace && (
                              <div>
                                <span className="text-gray-500">
                                  {t("governance.models.registry.detail.deployments.namespace", "Namespace")}:
                                </span>
                                <span className="ml-2">{deployment.namespace}</span>
                              </div>
                            )}
                            {deployment.cluster && (
                              <div>
                                <span className="text-gray-500">
                                  {t("governance.models.registry.detail.deployments.cluster", "Cluster")}:
                                </span>
                                <span className="ml-2">{deployment.cluster}</span>
                              </div>
                            )}
                            {deployment.provider && (
                              <div>
                                <span className="text-gray-500">
                                  {t("governance.models.registry.detail.deployments.provider", "Proveedor Cloud")}:
                                </span>
                                <span className="ml-2">{deployment.provider}</span>
                              </div>
                            )}
                            {deployment.createdAt && (
                              <div>
                                <span className="text-gray-500">
                                  {t("governance.models.registry.detail.deployments.createdAt", "Creado")}:
                                </span>
                                <span className="ml-2">{new Date(deployment.createdAt).toLocaleString()}</span>
                              </div>
                            )}
                          </div>
                        </CardBody>
                      </Card>
                    ))}
                  </div>
                </div>
              )}

              {/* Información del Proveedor Externo */}
              {providerInfo && providerInfo.type === "EXTERNAL" && (
                <div>
                  <h3 className="text-lg font-semibold mb-4">
                    {t("governance.models.registry.detail.deployments.providerInfo", "Proveedor Externo")}
                  </h3>
                  <Card className="border-l-4 border-l-purple-500">
                    <CardBody className="space-y-3">
                      <div className="flex items-center justify-between">
                        <h4 className="font-semibold text-lg">{providerInfo.displayName}</h4>
                        <Badge variant="outline" className="bg-purple-50 text-purple-700 border-purple-200">
                          {t("governance.models.registry.detail.deployments.external", "Externo")}
                        </Badge>
                      </div>
                      <div className="grid grid-cols-2 gap-4 text-sm">
                        {providerInfo.baseUrl && (
                          <div>
                            <span className="text-gray-500">
                              {t("governance.models.registry.detail.deployments.baseUrl", "URL Base")}:
                            </span>
                            <span className="ml-2 font-mono text-blue-600">{providerInfo.baseUrl}</span>
                          </div>
                        )}
                        {providerInfo.regions && providerInfo.regions.length > 0 && (
                          <div>
                            <span className="text-gray-500">
                              {t("governance.models.registry.detail.deployments.regions", "Regiones")}:
                            </span>
                            <span className="ml-2">{providerInfo.regions.join(", ")}</span>
                          </div>
                        )}
                        {providerInfo.healthCheckUrl && (
                          <div>
                            <span className="text-gray-500">
                              {t("governance.models.registry.detail.deployments.healthCheck", "Health Check")}:
                            </span>
                            <a
                              href={providerInfo.healthCheckUrl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="ml-2 text-blue-600 hover:underline"
                            >
                              {providerInfo.healthCheckUrl}
                            </a>
                          </div>
                        )}
                        {providerInfo.documentationUrl && (
                          <div>
                            <span className="text-gray-500">
                              {t("governance.models.registry.detail.deployments.documentation", "Documentación")}:
                            </span>
                            <a
                              href={providerInfo.documentationUrl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="ml-2 text-blue-600 hover:underline"
                            >
                              {t("governance.models.registry.detail.deployments.viewDocs", "Ver documentación")}
                            </a>
                          </div>
                        )}
                      </div>
                    </CardBody>
                  </Card>
                </div>
              )}

              {/* Sin despliegues ni provider */}
              {deployments.length === 0 && !providerInfo && !loadingDeployments && (
                <div className="text-center py-8 text-gray-500">
                  <Server className="h-12 w-12 mx-auto mb-4 opacity-50" />
                  <p>{t("governance.models.registry.detail.deployments.noDeployments", "No hay información de despliegue disponible")}</p>
                </div>
              )}

              {loadingDeployments && (
                <div className="text-center py-8 text-gray-500">
                  <p>{t("governance.models.registry.detail.deployments.loading", "Cargando información de despliegue...")}</p>
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Testing */}
        <TabsContent value="testing" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.models.registry.detail.testing.title", "Testing y Evaluación")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="text-center py-8">
                <BarChart3 className="h-12 w-12 mx-auto mb-4 opacity-50" />
                <p className="text-muted-foreground mb-4">
                  {t("governance.models.registry.detail.testing.description", "Ejecuta análisis de sesgo, explicabilidad y validación de modelos")}
                </p>
                <Link href={`/models/registry/${id}/testing`}>
                  <Button>
                    <BarChart3 className="w-4 h-4 mr-2" />
                    {t("governance.models.registry.detail.testing.goToTesting", "Ir a Testing")}
                  </Button>
                </Link>
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Integraciones MLOps */}
        <TabsContent value="integrations" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Cloud className="w-5 h-5" />
                {t("governance.models.registry.detail.integrations.title", "Integración MLOps")}
              </CardTitle>
            </CardHeader>
            <CardBody className="space-y-6">
              <div className="space-y-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.registry.detail.integrations.selectPlatform", "Plataforma MLOps")}
                  </label>
                  <Select
                    value={model.mlopsIntegration || ""}
                    onValueChange={(value) => {
                      setModel({ ...model, mlopsIntegration: value });
                    }}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder={t("governance.models.registry.detail.integrations.selectPlaceholder", "Seleccione una plataforma MLOps")} />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="">{t("governance.models.registry.detail.integrations.none", "Sin integración")}</SelectItem>
                      <SelectItem value="databricks">Databricks</SelectItem>
                      <SelectItem value="sagemaker">AWS SageMaker</SelectItem>
                      <SelectItem value="vertex">Google Vertex AI</SelectItem>
                      <SelectItem value="azureml">Azure Machine Learning</SelectItem>
                      <SelectItem value="mlflow">MLflow</SelectItem>
                      <SelectItem value="huggingface">Hugging Face</SelectItem>
                      <SelectItem value="kubeflow">Kubeflow</SelectItem>
                      <SelectItem value="seldon">Seldon Core</SelectItem>
                      <SelectItem value="wandb">Weights & Biases</SelectItem>
                    </SelectContent>
                  </Select>
                  <p className="text-xs text-muted-foreground">
                    {t("governance.models.registry.detail.integrations.selectDescription", "Seleccione la plataforma MLOps donde está desplegado o registrado este modelo. Las credenciales se configuran globalmente en Configuración MLOps.")}
                  </p>
                </div>

                {/* Configuración específica según la plataforma seleccionada */}
                {model.mlopsIntegration && (
                  <div className="mt-6 p-4 border rounded-lg bg-muted/50">
                    <h4 className="font-semibold mb-4">
                      {t("governance.models.registry.detail.integrations.modelConfig", "Configuración Específica del Modelo")}
                    </h4>

                    {/* Databricks */}
                    {model.mlopsIntegration === "databricks" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Workspace</label>
                          <Input
                            value={model.mlopsConfig?.workspace || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, workspace: e.target.value },
                              });
                            }}
                            placeholder="main"
                          />
                        </div>
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model Registry Path</label>
                          <Input
                            value={model.mlopsConfig?.modelPath || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelPath: e.target.value },
                              });
                            }}
                            placeholder="models.my_model"
                          />
                        </div>
                      </div>
                    )}

                    {/* SageMaker */}
                    {model.mlopsIntegration === "sagemaker" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model Registry Name</label>
                          <Input
                            value={model.mlopsConfig?.modelName || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelName: e.target.value },
                              });
                            }}
                            placeholder="my-model"
                          />
                        </div>
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Endpoint Name (opcional)</label>
                          <Input
                            value={model.mlopsConfig?.endpointName || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, endpointName: e.target.value },
                              });
                            }}
                            placeholder="my-model-endpoint"
                          />
                        </div>
                      </div>
                    )}

                    {/* Vertex AI */}
                    {model.mlopsIntegration === "vertex" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model ID</label>
                          <Input
                            value={model.mlopsConfig?.modelId || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelId: e.target.value },
                              });
                            }}
                            placeholder="1234567890123456789"
                          />
                        </div>
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Endpoint ID (opcional)</label>
                          <Input
                            value={model.mlopsConfig?.endpointId || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, endpointId: e.target.value },
                              });
                            }}
                            placeholder="9876543210987654321"
                          />
                        </div>
                      </div>
                    )}

                    {/* Azure ML */}
                    {model.mlopsIntegration === "azureml" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model Name</label>
                          <Input
                            value={model.mlopsConfig?.modelName || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelName: e.target.value },
                              });
                            }}
                            placeholder="my-model"
                          />
                        </div>
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Version (opcional)</label>
                          <Input
                            value={model.mlopsConfig?.version || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, version: e.target.value },
                              });
                            }}
                            placeholder="1"
                          />
                        </div>
                      </div>
                    )}

                    {/* MLflow */}
                    {model.mlopsIntegration === "mlflow" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model URI</label>
                          <Input
                            value={model.mlopsConfig?.modelUri || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelUri: e.target.value },
                              });
                            }}
                            placeholder="models:/my_model/Production"
                          />
                        </div>
                      </div>
                    )}

                    {/* Hugging Face */}
                    {model.mlopsIntegration === "huggingface" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Model ID</label>
                          <Input
                            value={model.mlopsConfig?.modelId || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, modelId: e.target.value },
                              });
                            }}
                            placeholder="username/model-name"
                          />
                        </div>
                      </div>
                    )}

                    {/* Kubeflow */}
                    {model.mlopsIntegration === "kubeflow" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Pipeline Run ID</label>
                          <Input
                            value={model.mlopsConfig?.runId || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, runId: e.target.value },
                              });
                            }}
                            placeholder="pipeline-run-123"
                          />
                        </div>
                      </div>
                    )}

                    {/* Seldon */}
                    {model.mlopsIntegration === "seldon" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Deployment Name</label>
                          <Input
                            value={model.mlopsConfig?.deploymentName || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, deploymentName: e.target.value },
                              });
                            }}
                            placeholder="my-model-deployment"
                          />
                        </div>
                      </div>
                    )}

                    {/* W&B */}
                    {model.mlopsIntegration === "wandb" && (
                      <div className="space-y-4">
                        <div className="space-y-2">
                          <label className="text-sm font-medium">Run Path</label>
                          <Input
                            value={model.mlopsConfig?.runPath || ""}
                            onChange={(e) => {
                              setModel({
                                ...model,
                                mlopsConfig: { ...model.mlopsConfig, runPath: e.target.value },
                              });
                            }}
                            placeholder="username/project/run-id"
                          />
                        </div>
                      </div>
                    )}

                    <div className="mt-6 pt-4 border-t">
                      <Button
                        onClick={async () => {
                          // TODO: Implementar llamada API para guardar la integración
                          alert(t("governance.models.registry.detail.integrations.saved", "Integración guardada correctamente"));
                        }}
                        className="w-full"
                      >
                        <Save className="w-4 h-4 mr-2" />
                        {t("common.save", "Guardar")}
                      </Button>
                    </div>
                  </div>
                )}

                {!model.mlopsIntegration && (
                  <div className="text-center py-8 text-muted-foreground">
                    <Cloud className="h-12 w-12 mx-auto mb-4 opacity-50" />
                    <p>{t("governance.models.registry.detail.integrations.noIntegration", "No hay integración MLOps asociada a este modelo")}</p>
                    <p className="text-sm mt-2">
                      {t("governance.models.registry.detail.integrations.noIntegrationDescription", "Seleccione una plataforma MLOps para asociar este modelo con su despliegue o registro.")}
                    </p>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Modal de Información del Proyecto */}
      <SimpleModal
        isOpen={selectedProject !== null}
        onClose={() => setSelectedProject(null)}
        title={selectedProject ? `${t("governance.models.registry.detail.projects.title", "Información del Proyecto")} - ${selectedProject.name}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedProject && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.registry.detail.projects.name", "Nombre")}
                    </label>
                    <p className="text-base font-semibold">{selectedProject.name}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.registry.detail.projects.usageCount", "Usos")}
                    </label>
                    <p className="text-base font-semibold">{selectedProject.usageCount.toLocaleString()}</p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.registry.detail.projects.lastUsed", "Último Uso")}
                    </label>
                    <p className="text-base">
                      {selectedProject.lastUsedAt
                        ? new Date(selectedProject.lastUsedAt).toLocaleString()
                        : <span className="text-muted-foreground">-</span>}
                    </p>
                  </div>
                </div>
                <div className="pt-4 border-t">
                  <p className="text-sm text-muted-foreground">
                    {t("governance.models.registry.detail.projects.description", "Este proyecto utiliza este modelo para sus operaciones.")}
                  </p>
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
