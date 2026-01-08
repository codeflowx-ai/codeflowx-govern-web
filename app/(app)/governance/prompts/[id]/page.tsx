"use client";

import { useTranslation } from "@/app/config/i18n";
import { usePageTitle } from "@/components/contexts/PageTitleContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  ArrowLeft,
  Edit,
  Trash2,
  Brain,
  FileText,
  GitBranch,
  CheckCircle,
  AlertTriangle,
  BarChart3,
  Shield,
  TrendingUp,
  Loader2,
  RefreshCw,
  Save,
  FileCode,
  X,
  XCircle,
  Bot,
  Cpu,
  Database,
  FolderTree,
} from "lucide-react";
import { useState, useEffect, useRef } from "react";
import { useRouter, useParams, useSearchParams } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import { CodeEditor } from "@/components/ui/code-editor";
import { getPromptyTemplate } from "@/lib/prompty-template";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

interface Prompt {
  idxprompt: number;
  prmuuid?: string;
  prmname: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmversion: string;
  prmstatus: string;
  prmapprovalstatus: string;
  projectId?: number;
  projectName?: string;
  createdAt: string;
  updatedAt: string;
}

interface PromptVersion {
  idxpromptversion: number;
  prmversion: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
  prmstatus: string;
  prmcreatedat: string;
}

interface PromptValidation {
  idxpromptvalidation: number;
  prmvalidationtype: string;
  prmvalidationresult: string;
  prmvalidationscore: number;
  prmvalidationdetails: string;
  prmissuesfound: number;
  prmcreatedat: string;
}

interface PromptFormData {
  prmname: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
  prmstatus: string;
  prmversion?: string;
}

export default function PromptDetailPage() {
  const { t } = useTranslation();
  const { setPageTitle } = usePageTitle();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const promptId = params.id as string;

  const [prompt, setPrompt] = useState<Prompt | null>(null);
  const [versions, setVersions] = useState<PromptVersion[]>([]);
  const [validations, setValidations] = useState<PromptValidation[]>([]);
  const [loading, setLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);
  const [evaluating, setEvaluating] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [analyzeMessage, setAnalyzeMessage] = useState<{ type: 'success' | 'error' | 'info' | null; text: string }>({ type: null, text: '' });
  const [evaluateMessage, setEvaluateMessage] = useState<{ type: 'success' | 'error' | 'info' | null; text: string }>({ type: null, text: '' });
  const [activeTab, setActiveTab] = useState("overview");


  // Estados para batch testing con CSV
  const [batchTesting, setBatchTesting] = useState(false);
  const [csvFile, setCsvFile] = useState<File | null>(null);
  const [parsedTestCases, setParsedTestCases] = useState<any[]>([]);
  const [judgeModelId, setJudgeModelId] = useState<string>("");
  const [batchResults, setBatchResults] = useState<any[]>([]);
  const [showBatchResults, setShowBatchResults] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const originalContentRef = useRef<string>("");
  const [formData, setFormData] = useState<PromptFormData>({
    prmname: "",
    prmdescription: "",
    prmcontent: "",
    prmparameters: "temperature=0.7, max_tokens=1000",
    prmchanges: "",
    prmstatus: "DRAFT",
    prmversion: "1.0.0",
  });
  const [relations, setRelations] = useState<{
    agents: Array<{ id: number; name: string; relationId: number }>;
    models: Array<{ id: number; name: string; relationId: number }>;
    rags: Array<{ id: number; name: string; relationId: number }>;
  }>({
    agents: [],
    models: [],
    rags: [],
  });
  const [addAgentDialogOpen, setAddAgentDialogOpen] = useState(false);
  const [addModelDialogOpen, setAddModelDialogOpen] = useState(false);
  const [addRagDialogOpen, setAddRagDialogOpen] = useState(false);
  const [availableAgents, setAvailableAgents] = useState<Array<{ id: number; name: string }>>([]);
  const [availableModels, setAvailableModels] = useState<Array<{ id: number; name: string }>>([]);
  const [availableRags, setAvailableRags] = useState<Array<{ id: number; name: string }>>([]);
  const [selectedAgentId, setSelectedAgentId] = useState<number | null>(null);
  const [selectedModelId, setSelectedModelId] = useState<number | null>(null);
  const [selectedRagId, setSelectedRagId] = useState<number | null>(null);
  const [loadingAvailable, setLoadingAvailable] = useState(false);
  const [associating, setAssociating] = useState(false);

  useEffect(() => {
    if (promptId) {
      loadPromptData();
    }
  }, [promptId]);

  useEffect(() => {
    const editParam = searchParams.get('edit');
    setIsEditMode(editParam === 'true');
  }, [searchParams]);

  // Establecer título de la página
  useEffect(() => {
    if (prompt?.prmname) {
      const title = isEditMode
        ? t("prompts.detail.title.edit", "Editar Prompt: {name}", { name: prompt.prmname })
        : prompt.prmname;
      document.title = `${title} - CodeflowX`;
      setPageTitle(title);
    } else {
      const defaultTitle = t("prompts.detail.title", "Detalle del Prompt");
      document.title = `${defaultTitle} - CodeflowX`;
      setPageTitle(defaultTitle);
    }

    return () => {
      setPageTitle(null);
    };
  }, [prompt, isEditMode, t, setPageTitle]);

  const loadPromptData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/prompts/${promptId}`);
      if (response.ok) {
        const data = await response.json();
        setPrompt(data);

        // Cargar datos del formulario
        setFormData({
          prmname: data.prmname || data.name || "",
          prmdescription: data.prmdescription || data.description || "",
          prmcontent: data.prmcontent || data.content || "",
          prmparameters: data.prmparameters || data.parameters || "temperature=0.7, max_tokens=1000",
          prmchanges: "",
          prmstatus: data.prmstatus || data.status || "DRAFT",
          prmversion: data.prmversion || data.version || "1.0.0",
        });
        originalContentRef.current = data.prmcontent || data.content || "";

        // Cargar versiones
        const versionsResponse = await fetch(`/api/prompts/${promptId}/versions`);
        if (versionsResponse.ok) {
          const versionsData = await versionsResponse.json();
          setVersions(versionsData);
        }

        // Cargar validaciones
        const validationsResponse = await fetch(`/api/prompts/${promptId}/validations`);
        if (validationsResponse.ok) {
          const validationsData = await validationsResponse.json();
          setValidations(validationsData);
        }

        // Cargar relaciones
        loadRelations();
      }
    } catch (error) {
      console.error("Error loading prompt:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadRelations = async () => {
    try {
      const [agentsRes, modelsRes, ragsRes] = await Promise.all([
        fetch(`/api/governance/prompts/${promptId}/agents`).catch(() => ({ ok: false })),
        fetch(`/api/governance/prompts/${promptId}/models`).catch(() => ({ ok: false })),
        fetch(`/api/governance/prompts/${promptId}/rags`).catch(() => ({ ok: false })),
      ]);

      const agents = agentsRes.ok && 'json' in agentsRes ? await agentsRes.json() : [];
      const models = modelsRes.ok && 'json' in modelsRes ? await modelsRes.json() : [];
      const rags = ragsRes.ok && 'json' in ragsRes ? await ragsRes.json() : [];

      setRelations({
        agents: Array.isArray(agents) ? agents.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
        models: Array.isArray(models) ? models.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
        rags: Array.isArray(rags) ? rags.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
      });
    } catch (error) {
      console.error("Error al cargar relaciones:", error);
    }
  };

  const handleDisassociate = async (type: 'agents' | 'models' | 'rags', relationId: number, relatedId: number) => {
    try {
      const endpoint = type === 'agents' ? 'agents' : type === 'models' ? 'models' : 'rags';
      const paramName = type === 'agents' ? 'agentId' : type === 'models' ? 'modelId' : 'ragSystemId';
      const response = await fetch(`/api/governance/prompts/${promptId}/${endpoint}?${paramName}=${relatedId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setRelations(prev => ({
          ...prev,
          [type]: prev[type].filter(r => r.relationId !== relationId),
        }));
      }
    } catch (error) {
      console.error(`Error al desasociar ${type}:`, error);
    }
  };

  const loadAvailableAgents = async () => {
    try {
      setLoadingAvailable(true);
      const response = await fetch('/api/governance/agents/registry');
      if (response.ok) {
        const data = await response.json();
        const agents = Array.isArray(data) ? data : (data.items || []);
        // Filtrar agents ya asociados
        const associatedIds = relations.agents.map(a => a.id);
        setAvailableAgents(agents.filter((a: any) => !associatedIds.includes(a.idxagent || a.id)));
      }
    } catch (error) {
      console.error("Error al cargar agents disponibles:", error);
      // Mock data si falla
      setAvailableAgents([
        { id: 1, name: "Customer Support Agent" },
        { id: 2, name: "Sales Assistant Agent" },
        { id: 3, name: "Technical Support Agent" },
      ].filter(a => !relations.agents.some(ra => ra.id === a.id)));
    } finally {
      setLoadingAvailable(false);
    }
  };

  const loadAvailableModels = async () => {
    try {
      setLoadingAvailable(true);
      const response = await fetch('/api/governance/models');
      if (response.ok) {
        const data = await response.json();
        const models = Array.isArray(data) ? data : (data.items || []);
        // Filtrar models ya asociados
        const associatedIds = relations.models.map(m => m.id);
        setAvailableModels(models.filter((m: any) => !associatedIds.includes(m.idxmodel || m.id)));
      }
    } catch (error) {
      console.error("Error al cargar models disponibles:", error);
      // Mock data si falla
      setAvailableModels([
        { id: 1, name: "GPT-4" },
        { id: 2, name: "GPT-3.5 Turbo" },
        { id: 3, name: "Claude 3 Opus" },
        { id: 4, name: "Llama 2" },
      ].filter(m => !relations.models.some(rm => rm.id === m.id)));
    } finally {
      setLoadingAvailable(false);
    }
  };

  const loadAvailableRags = async () => {
    try {
      setLoadingAvailable(true);
      const response = await fetch('/api/governance/rags');
      if (response.ok) {
        const data = await response.json();
        const rags = Array.isArray(data) ? data : (data.items || []);
        // Filtrar RAGs ya asociados
        const associatedIds = relations.rags.map(r => r.id);
        setAvailableRags(rags.filter((r: any) => !associatedIds.includes(r.idxragsystem || r.id)));
      }
    } catch (error) {
      console.error("Error al cargar RAGs disponibles:", error);
      // Mock data si falla
      setAvailableRags([
        { id: 1, name: "Company Knowledge Base" },
        { id: 2, name: "Product Documentation RAG" },
        { id: 3, name: "Customer Support RAG" },
      ].filter(r => !relations.rags.some(rr => rr.id === r.id)));
    } finally {
      setLoadingAvailable(false);
    }
  };

  const handleAssociateAgent = async () => {
    if (!selectedAgentId) return;
    try {
      setAssociating(true);
      const response = await fetch(`/api/governance/prompts/${promptId}/agents/${selectedAgentId}?createdBy=system`, {
        method: 'POST',
      });

      if (response.ok) {
        await loadRelations();
        setAddAgentDialogOpen(false);
        setSelectedAgentId(null);
      } else {
        alert(t("prompts.detail.agents.associateError", "Error al asociar el agent"));
      }
    } catch (error) {
      console.error("Error al asociar agent:", error);
      alert(t("prompts.detail.agents.associateError", "Error al asociar el agent"));
    } finally {
      setAssociating(false);
    }
  };

  const handleAssociateModel = async () => {
    if (!selectedModelId) return;
    try {
      setAssociating(true);
      const response = await fetch(`/api/governance/prompts/${promptId}/models/${selectedModelId}?createdBy=system`, {
        method: 'POST',
      });

      if (response.ok) {
        await loadRelations();
        setAddModelDialogOpen(false);
        setSelectedModelId(null);
      } else {
        alert(t("prompts.detail.models.associateError", "Error al asociar el model"));
      }
    } catch (error) {
      console.error("Error al asociar model:", error);
      alert(t("prompts.detail.models.associateError", "Error al asociar el model"));
    } finally {
      setAssociating(false);
    }
  };

  const handleAssociateRag = async () => {
    if (!selectedRagId) return;
    try {
      setAssociating(true);
      const response = await fetch(`/api/governance/prompts/${promptId}/rags/${selectedRagId}?createdBy=system`, {
        method: 'POST',
      });

      if (response.ok) {
        await loadRelations();
        setAddRagDialogOpen(false);
        setSelectedRagId(null);
      } else {
        alert(t("prompts.detail.rags.associateError", "Error al asociar el RAG system"));
      }
    } catch (error) {
      console.error("Error al asociar RAG:", error);
      alert(t("prompts.detail.rags.associateError", "Error al asociar el RAG system"));
    } finally {
      setAssociating(false);
    }
  };

  const loadPromptyTemplate = () => {
    const template = getPromptyTemplate(formData.prmname || "My New Prompt", formData.prmdescription || "A description for my new prompt.");
    setFormData((prev) => ({ ...prev, prmcontent: template }));
  };

  const handleInputChange = (field: keyof PromptFormData, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const contentChanged = formData.prmcontent !== originalContentRef.current;

      if (contentChanged) {
        // Crear nueva versión automáticamente
        const response = await fetch(`/api/prompts/${promptId}/versions`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            prmversion: formData.prmversion,
            prmdescription: formData.prmdescription,
            prmcontent: formData.prmcontent,
            prmparameters: formData.prmparameters,
            prmstatus: formData.prmstatus,
            prmchanges: formData.prmchanges || "Actualización automática: contenido modificado",
          }),
        });

        if (!response.ok) {
          throw new Error("Error al crear nueva versión");
        }
      } else {
        // Actualizar sin crear nueva versión (solo metadata)
        const response = await fetch(`/api/prompts/${promptId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            prmname: formData.prmname,
            prmdescription: formData.prmdescription,
            prmparameters: formData.prmparameters,
            prmstatus: formData.prmstatus,
          }),
        });

        if (!response.ok) {
          throw new Error("Error al actualizar el prompt");
        }
      }

      // Recargar datos y salir del modo edición
      await loadPromptData();
      setIsEditMode(false);
      router.push(`/governance/prompts/${promptId}`);
    } catch (error) {
      console.error("Error al guardar prompt:", error);
      alert(t("governance.prompts.register.error", "Error al guardar el prompt"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    setIsEditMode(false);
    router.push(`/governance/prompts/${promptId}`);
  };

  const handleAnalyze = async () => {
    try {
      setAnalyzing(true);
      setAnalyzeMessage({ type: 'info', text: t("prompts.detail.analyze.processing", "Iniciando análisis de cumplimiento normativo (EU AI Act), seguridad y detección de sesgos...") });

      // Crear validación de análisis de cumplimiento y calidad
      const validationData = {
        prmvalidationtype: "COMPLIANCE",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "Análisis de cumplimiento normativo (EU AI Act), seguridad (prompt injection), detección de sesgos y calidad del prompt",
        prmissuesfound: 0,
        prmstatus: "ACTIVE",
      };

      const response = await fetch(`/api/prompts/${promptId}/validations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(validationData),
      });

      if (response.ok) {
        // Recargar validaciones
        await loadPromptData();
        setAnalyzeMessage({ type: 'success', text: t("prompts.detail.analyze.success", "Análisis de cumplimiento normativo iniciado correctamente. Verificando compliance (EU AI Act), seguridad y sesgos. Los resultados estarán disponibles en la pestaña de Validaciones.") });
        // Limpiar mensaje después de 5 segundos
        setTimeout(() => setAnalyzeMessage({ type: null, text: '' }), 5000);
      } else {
        setAnalyzeMessage({ type: 'error', text: t("prompts.detail.analyze.error", "Error al iniciar el análisis de cumplimiento. Por favor, inténtelo de nuevo.") });
      }
    } catch (error) {
      console.error("Error analyzing prompt:", error);
      setAnalyzeMessage({ type: 'error', text: t("prompts.detail.analyze.error", "Error al iniciar el análisis de cumplimiento. Por favor, inténtelo de nuevo.") });
    } finally {
      setAnalyzing(false);
    }
  };

  const handleEvaluate = async () => {
    try {
      setEvaluating(true);
      setEvaluateMessage({ type: 'info', text: t("prompts.detail.evaluate.processing", "Iniciando evaluación de rendimiento: latencia, tokens consumidos, costos y calidad de respuestas...") });

      // Crear validación de evaluación de performance
      const validationData = {
        prmvalidationtype: "PERFORMANCE",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "Evaluación de rendimiento: latencia, tokens consumidos, costos operativos, precisión y calidad de las respuestas generadas",
        prmissuesfound: 0,
        prmstatus: "ACTIVE",
      };

      const response = await fetch(`/api/prompts/${promptId}/validations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(validationData),
      });

      if (response.ok) {
        // Recargar validaciones
        await loadPromptData();
        setEvaluateMessage({ type: 'success', text: t("prompts.detail.evaluate.success", "Evaluación de rendimiento iniciada correctamente. Analizando latencia, tokens, costos y calidad. Los resultados estarán disponibles en la pestaña de Validaciones.") });
        // Limpiar mensaje después de 5 segundos
        setTimeout(() => setEvaluateMessage({ type: null, text: '' }), 5000);
      } else {
        setEvaluateMessage({ type: 'error', text: t("prompts.detail.evaluate.error", "Error al iniciar la evaluación de rendimiento. Por favor, inténtelo de nuevo.") });
      }
    } catch (error) {
      console.error("Error evaluating prompt:", error);
      setEvaluateMessage({ type: 'error', text: t("prompts.detail.evaluate.error", "Error al iniciar la evaluación de rendimiento. Por favor, inténtelo de nuevo.") });
    } finally {
      setEvaluating(false);
    }
  };

  const handleDelete = async () => {
    try {
      const response = await fetch(`/api/prompts/${promptId}`, {
        method: "DELETE",
      });

      if (response.ok) {
        router.push("/governance/prompts");
      } else {
        alert(t("prompts.detail.delete.error", "Error al eliminar el prompt"));
      }
    } catch (error) {
      console.error("Error deleting prompt:", error);
      alert(t("prompts.detail.delete.error", "Error al eliminar el prompt"));
    }
  };



  // Funciones para batch testing con CSV
  const parseCSV = (csvText: string): any[] => {
    const lines = csvText.split('\n').filter(line => line.trim());
    if (lines.length === 0) return [];

    // Detectar headers
    const headers = lines[0].split(',').map(h => h.trim().toLowerCase());
    const testCases: any[] = [];

    for (let i = 1; i < lines.length; i++) {
      const values = lines[i].split(',').map(v => v.trim());
      const testCase: any = {};

      headers.forEach((header, index) => {
        if (header.includes('input') || header.includes('entrada')) {
          testCase.input = values[index] || '';
        } else if (header.includes('expected') || header.includes('esperado') || header.includes('output')) {
          testCase.expectedResponse = values[index] || '';
        } else if (header.includes('id') || header.includes('test')) {
          testCase.id = values[index] || `test-${i}`;
        } else {
          testCase[header] = values[index] || '';
        }
      });

      if (testCase.input || testCase.expectedResponse) {
        testCases.push(testCase);
      }
    }

    return testCases;
  };

  const handleCSVUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.name.endsWith('.csv')) {
      alert(t("prompts.detail.tests.csv.invalid", "Por favor, seleccione un archivo CSV válido"));
      return;
    }

    setCsvFile(file);
    const reader = new FileReader();

    reader.onload = (e) => {
      const text = e.target?.result as string;
      const parsed = parseCSV(text);
      setParsedTestCases(parsed);

      if (parsed.length === 0) {
        alert(t("prompts.detail.tests.csv.empty", "El archivo CSV no contiene test cases válidos. Formato esperado: input,expectedResponse"));
      } else {
        alert(t("prompts.detail.tests.csv.parsed", "{count} test cases cargados correctamente", { count: String(parsed.length) }));
      }
    };

    reader.readAsText(file);
  };

  const handleBatchTest = async () => {
    if (parsedTestCases.length === 0) {
      alert(t("prompts.detail.tests.batch.noCases", "Por favor, cargue un archivo CSV con test cases"));
      return;
    }

    if (!selectedModelId) {
      alert(t("prompts.detail.tests.selectModel", "Por favor, seleccione un modelo para probar"));
      return;
    }

    try {
      setBatchTesting(true);
      setBatchResults([]);
      setShowBatchResults(true);

      const results: any[] = [];

      // Ejecutar cada test case
      for (let i = 0; i < parsedTestCases.length; i++) {
        const testCase = parsedTestCases[i];

        try {
          // Ejecutar el test
          const testResponse = await fetch(`/api/governance/prompts/${promptId}/test`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              modelId: selectedModelId,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
            }),
          });

          if (testResponse.ok) {
            const testResult = await testResponse.json();
            const actualResponse = testResult.response || testResult.text || testResult.content || '';

            // Si hay modelo juez, evaluar con el juez
            let judgeScore = null;
            let judgeFeedback = null;

            if (judgeModelId && testCase.expectedResponse) {
              try {
                const judgeResponse = await fetch(`/api/governance/prompts/${promptId}/test/judge`, {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    judgeModelId: judgeModelId,
                    prompt: prompt?.prmcontent || '',
                    input: testCase.input || '',
                    actualResponse: actualResponse,
                    expectedResponse: testCase.expectedResponse,
                  }),
                });

                if (judgeResponse.ok) {
                  const judgeResult = await judgeResponse.json();
                  judgeScore = judgeResult.score;
                  judgeFeedback = judgeResult.feedback;
                }
              } catch (error) {
                console.error("Error en evaluación del juez:", error);
              }
            }

            // Comparar con respuesta esperada si existe
            let similarityScore = null;
            if (testCase.expectedResponse) {
              try {
                const compareResponse = await fetch(`/api/governance/prompts/${promptId}/test/compare`, {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    actualResponse: actualResponse,
                    expectedResponse: testCase.expectedResponse,
                  }),
                });

                if (compareResponse.ok) {
                  const compareResult = await compareResponse.json();
                  similarityScore = compareResult.similarityScore;
                }
              } catch (error) {
                console.error("Error comparando respuestas:", error);
              }
            }

            results.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: actualResponse,
              similarityScore: similarityScore,
              judgeScore: judgeScore,
              judgeFeedback: judgeFeedback,
              tokensUsed: testResult.tokensUsed,
              latency: testResult.latency,
              cost: testResult.cost,
              success: similarityScore !== null ? similarityScore >= 0.8 : judgeScore !== null ? judgeScore >= 0.8 : null,
              error: null,
            });
          } else {
            const error = await testResponse.json();
            results.push({
              id: testCase.id || `test-${i + 1}`,
              input: testCase.input || '',
              expectedResponse: testCase.expectedResponse || '',
              actualResponse: '',
              error: error.error || t("prompts.detail.tests.batch.error", "Error al ejecutar test"),
              success: false,
            });
          }
        } catch (error: any) {
          results.push({
            id: testCase.id || `test-${i + 1}`,
            input: testCase.input || '',
            expectedResponse: testCase.expectedResponse || '',
            actualResponse: '',
            error: error.message || t("prompts.detail.tests.batch.error", "Error al ejecutar test"),
            success: false,
          });
        }
      }

      setBatchResults(results);
    } catch (error) {
      console.error("Error en batch testing:", error);
      alert(t("prompts.detail.tests.batch.error", "Error al ejecutar batch de tests"));
    } finally {
      setBatchTesting(false);
    }
  };

  const getStatusBadge = (status: string): "success" | "secondary" | "outline" | "danger" => {
    const variants: Record<string, "success" | "secondary" | "outline" | "danger"> = {
      ACTIVE: "success",
      DRAFT: "secondary",
      PENDING: "outline",
      INACTIVE: "danger",
    };
    return variants[status] || "outline";
  };

  const getValidationTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      SAFETY: "bg-red-500/20 text-red-500",
      COMPLIANCE: "bg-blue-500/20 text-blue-500",
      PERFORMANCE: "bg-green-500/20 text-green-500",
      BIAS: "bg-yellow-500/20 text-yellow-500",
    };
    return colors[type] || "bg-gray-500/20 text-gray-500";
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
        <div className="relative z-10 w-full px-6 py-6 flex items-center justify-center min-h-[60vh]">
          <Loader2 className="w-8 h-8 animate-spin text-primary" />
        </div>
      </div>
    );
  }

  if (!prompt) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
        <div className="relative z-10 w-full px-6 py-6">
          <div className="text-center py-12">
            <AlertTriangle className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
            <p className="text-muted-foreground">
              {t("prompts.detail.notFound", "Prompt no encontrado")}
            </p>
            <Button
              variant="outline"
              onClick={() => router.push("/governance/prompts")}
              className="mt-4"
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("common.back", "Volver")}
            </Button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6 pb-20">
        {/* Header */}
        <div className="space-y-4">
          {/* Primera línea: Título y Subtítulo */}
          <div className="flex items-center gap-3">
            <Brain className="w-6 h-6 text-primary flex-shrink-0" />
            <div className="flex-1">
              <div className="flex items-center gap-3">
                <h1 className="text-2xl font-bold text-foreground">
                  {isEditMode
                    ? t("prompts.detail.title.edit", "Editar Prompt: {name}", { name: prompt.prmname })
                    : prompt.prmname}
                </h1>
                <Badge variant={getStatusBadge(prompt.prmstatus)}>
                  {prompt.prmstatus}
                </Badge>
              </div>
              <p className="text-sm text-muted-foreground mt-1">
                {isEditMode
                  ? t("prompts.detail.subtitle.edit", "Modifica la información del prompt")
                  : prompt.prmdescription || t("prompts.detail.subtitle", "Detalle del prompt de IA")}
              </p>
            </div>
          </div>

          {/* Segunda línea: Botón Volver (izquierda) + Botones de acción (derecha) */}
          <div className="flex items-center justify-between">
            <Button
              variant="ghost"
              onClick={() => router.push("/governance/prompts")}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("common.back", "Volver")}
            </Button>
            <div className="flex items-center gap-2">
              <Button
                variant="outline"
                onClick={handleAnalyze}
                disabled={analyzing}
                className="gap-2"
              >
                {analyzing ? (
                  <Loader2 className="w-4 h-4 animate-spin" />
                ) : (
                  <BarChart3 className="w-4 h-4" />
                )}
                {t("prompts.detail.analyze", "Analizar Cumplimiento")}
              </Button>
              <Button
                variant="outline"
                onClick={handleEvaluate}
                disabled={evaluating}
                className="gap-2"
              >
                {evaluating ? (
                  <Loader2 className="w-4 h-4 animate-spin" />
                ) : (
                  <TrendingUp className="w-4 h-4" />
                )}
                {t("prompts.detail.evaluate", "Evaluar Rendimiento")}
              </Button>
              <Button
                variant="outline"
                onClick={() => setIsEditMode(!isEditMode)}
                className="gap-2"
              >
                <Edit className="w-4 h-4" />
                {isEditMode ? t("common.cancel", "Cancelar") : t("common.edit", "Editar")}
              </Button>
              <Button
                variant="danger"
                onClick={() => setDeleteDialogOpen(true)}
                className="gap-2"
              >
                <Trash2 className="w-4 h-4" />
                {t("common.delete", "Eliminar")}
              </Button>
            </div>
          </div>
        </div>

        {/* Mensajes de Análisis y Evaluación */}
        <div className="space-y-2">
          {analyzeMessage.type && (
            <div className={`flex items-center gap-2 p-3 rounded-md ${
              analyzeMessage.type === 'success'
                ? 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800'
                : analyzeMessage.type === 'error'
                ? 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800'
                : 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 border border-blue-200 dark:border-blue-800'
            }`}>
              {analyzeMessage.type === 'info' && <Loader2 className="w-4 h-4 animate-spin" />}
              {analyzeMessage.type === 'success' && <CheckCircle className="w-4 h-4" />}
              {analyzeMessage.type === 'error' && <XCircle className="w-4 h-4" />}
              <span className="text-sm">{analyzeMessage.text}</span>
              <button
                onClick={() => setAnalyzeMessage({ type: null, text: '' })}
                className="ml-auto hover:opacity-70"
              >
                <X className="w-4 h-4" />
              </button>
            </div>
          )}
          {evaluateMessage.type && (
            <div className={`flex items-center gap-2 p-3 rounded-md ${
              evaluateMessage.type === 'success'
                ? 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800'
                : evaluateMessage.type === 'error'
                ? 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800'
                : 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 border border-blue-200 dark:border-blue-800'
            }`}>
              {evaluateMessage.type === 'info' && <Loader2 className="w-4 h-4 animate-spin" />}
              {evaluateMessage.type === 'success' && <CheckCircle className="w-4 h-4" />}
              {evaluateMessage.type === 'error' && <XCircle className="w-4 h-4" />}
              <span className="text-sm">{evaluateMessage.text}</span>
              <button
                onClick={() => setEvaluateMessage({ type: null, text: '' })}
                className="ml-auto hover:opacity-70"
              >
                <X className="w-4 h-4" />
              </button>
            </div>
          )}
        </div>

        {/* Formulario de Edición */}
        {isEditMode && prompt && (
          <form onSubmit={handleSubmit}>
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>
                    {t("prompts.register.form.title", "Información del Prompt")}
                  </CardTitle>
                  <div className="flex items-center gap-2">
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleAnalyze}
                      disabled={analyzing || isSubmitting}
                      className="gap-2"
                    >
                      {analyzing ? (
                        <Loader2 className="w-4 h-4 animate-spin" />
                      ) : (
                        <BarChart3 className="w-4 h-4" />
                      )}
                      {t("prompts.register.analyze", "Analizar Cumplimiento")}
                    </Button>
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleEvaluate}
                      disabled={evaluating || isSubmitting}
                      className="gap-2"
                    >
                      {evaluating ? (
                        <Loader2 className="w-4 h-4 animate-spin" />
                      ) : (
                        <TrendingUp className="w-4 h-4" />
                      )}
                      {t("prompts.register.evaluate", "Evaluar Rendimiento")}
                    </Button>
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleCancel}
                      disabled={isSubmitting}
                    >
                      <ArrowLeft className="w-4 h-4 mr-2" />
                      {t("common.cancel", "Cancelar")}
                    </Button>
                    <Button type="submit" disabled={isSubmitting}>
                      <Save className="w-4 h-4 mr-2" />
                      {isSubmitting
                        ? t("prompts.register.form.saving", "Guardando...")
                        : t("governance.prompts.register.form.save", "Guardar Prompt")}
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {/* Mensajes de Análisis y Evaluación en modo edición */}
                <div className="space-y-2">
                  {analyzeMessage.type && (
                    <div className={`flex items-center gap-2 p-3 rounded-md ${
                      analyzeMessage.type === 'success'
                        ? 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800'
                        : analyzeMessage.type === 'error'
                        ? 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800'
                        : 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 border border-blue-200 dark:border-blue-800'
                    }`}>
                      {analyzeMessage.type === 'info' && <Loader2 className="w-4 h-4 animate-spin" />}
                      {analyzeMessage.type === 'success' && <CheckCircle className="w-4 h-4" />}
                      {analyzeMessage.type === 'error' && <XCircle className="w-4 h-4" />}
                      <span className="text-sm">{analyzeMessage.text}</span>
                      <button
                        type="button"
                        onClick={() => setAnalyzeMessage({ type: null, text: '' })}
                        className="ml-auto hover:opacity-70"
                      >
                        <X className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                  {evaluateMessage.type && (
                    <div className={`flex items-center gap-2 p-3 rounded-md ${
                      evaluateMessage.type === 'success'
                        ? 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 border border-green-200 dark:border-green-800'
                        : evaluateMessage.type === 'error'
                        ? 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800'
                        : 'bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-400 border border-blue-200 dark:border-blue-800'
                    }`}>
                      {evaluateMessage.type === 'info' && <Loader2 className="w-4 h-4 animate-spin" />}
                      {evaluateMessage.type === 'success' && <CheckCircle className="w-4 h-4" />}
                      {evaluateMessage.type === 'error' && <XCircle className="w-4 h-4" />}
                      <span className="text-sm">{evaluateMessage.text}</span>
                      <button
                        type="button"
                        onClick={() => setEvaluateMessage({ type: null, text: '' })}
                        className="ml-auto hover:opacity-70"
                      >
                        <X className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                </div>

                {/* Primera fila: Nombre, Versión y Estado */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="prmname">
                      {t("prompts.register.form.name", "Nombre")}
                    </Label>
                    <Input
                      id="prmname"
                      value={formData.prmname}
                      onChange={(e) => handleInputChange("prmname", e.target.value)}
                      placeholder={t("prompts.register.form.namePlaceholder", "Nombre del prompt")}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <Label htmlFor="prmversion">
                        {t("prompts.register.form.version", "Versión")}
                      </Label>
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        onClick={() => router.push("/governance/prompts/versioning/overview")}
                        className="text-xs h-auto py-1 px-2"
                      >
                        {t("prompts.register.form.viewVersions", "Ver Versiones")}
                      </Button>
                    </div>
                    <Input
                      id="prmversion"
                      value={formData.prmversion || "1.0.0"}
                      readOnly
                      disabled
                      className="bg-muted cursor-not-allowed"
                    />
                    {formData.prmcontent !== originalContentRef.current && (
                      <p className="text-xs text-amber-600 dark:text-amber-400">
                        {t("prompts.register.form.newVersionWarning", "⚠ Se creará una nueva versión al guardar porque el contenido ha cambiado")}
                      </p>
                    )}
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="prmstatus">
                      {t("prompts.register.form.status", "Estado")}
                    </Label>
                    <Select
                      value={formData.prmstatus}
                      onValueChange={(value) => handleInputChange("prmstatus", value)}
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="DRAFT">
                          {t("prompts.register.form.status.draft", "Borrador")}
                        </SelectItem>
                        <SelectItem value="ACTIVE">
                          {t("prompts.register.form.status.active", "Activo")}
                        </SelectItem>
                        <SelectItem value="INACTIVE">
                          {t("prompts.register.form.status.inactive", "Inactivo")}
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                {/* Segunda fila: Descripción, Cambios, Parámetros */}
                <div className="flex flex-col md:flex-row gap-4">
                  <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                    <Label htmlFor="prmdescription">
                      {t("prompts.register.form.description", "Descripción")}
                    </Label>
                    <Textarea
                      id="prmdescription"
                      value={formData.prmdescription}
                      onChange={(e) => handleInputChange("prmdescription", e.target.value)}
                      placeholder={t("prompts.register.form.descriptionPlaceholder", "Descripción detallada del prompt")}
                      rows={3}
                      className="w-full"
                    />
                  </div>
                  <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                    <Label htmlFor="prmchanges">
                      {t("prompts.register.form.changes", "Cambios / Notas")}
                    </Label>
                    <Textarea
                      id="prmchanges"
                      value={formData.prmchanges}
                      onChange={(e) => handleInputChange("prmchanges", e.target.value)}
                      placeholder={t("prompts.register.form.changesPlaceholder", "Notas sobre los cambios o características de esta versión...")}
                      rows={3}
                      className="w-full"
                    />
                  </div>
                  <div className="space-y-2 flex-1" style={{ flex: '0 0 30%' }}>
                    <Label htmlFor="prmparameters">
                      {t("prompts.register.form.parameters", "Parámetros")}
                    </Label>
                    <Textarea
                      id="prmparameters"
                      value={formData.prmparameters}
                      onChange={(e) => handleInputChange("prmparameters", e.target.value)}
                      placeholder="temperature=0.7, max_tokens=1000"
                      rows={3}
                      className="w-full"
                      required
                    />
                  </div>
                </div>

                {/* Contenido del Prompt con Editor de Código */}
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="prmcontent">
                      {t("prompts.register.form.content", "Contenido del Prompt")}
                    </Label>
                    <Button
                      type="button"
                      variant="outline"
                      size="sm"
                      onClick={loadPromptyTemplate}
                      className="flex items-center gap-2"
                    >
                      <FileCode className="w-4 h-4" />
                      {t("prompts.register.form.loadTemplate", "Cargar Plantilla")}
                    </Button>
                  </div>
                  <CodeEditor
                    value={formData.prmcontent}
                    onChange={(value) => handleInputChange("prmcontent", value || "")}
                    language="markdown"
                    height="400px"
                    placeholder={t("prompts.register.form.contentPlaceholder", "Escribe el contenido del prompt aquí... Usa el formato Prompty para una estructura profesional.")}
                  />
                </div>

                {/* Relaciones */}
                <div className="space-y-4 pt-4 border-t">
                  <div>
                    <h3 className="text-sm font-semibold mb-3">
                      {t("prompts.register.form.relations", "Relaciones")}
                    </h3>
                    <div className="space-y-2 mb-4">
                      <div className="flex items-center gap-2">
                        <Bot className="w-4 h-4 text-muted-foreground" />
                        <Label className="text-sm">
                          {t("prompts.register.form.agents", "Agents")}
                        </Label>
                      </div>
                      <div className="flex flex-wrap gap-2">
                        {relations.agents.length === 0 ? (
                          <span className="text-sm text-muted-foreground">
                            {t("prompts.register.form.noAgents", "No hay agents asociados")}
                          </span>
                        ) : (
                          relations.agents.map((agent) => (
                            <Badge key={agent.relationId} variant="secondary" className="flex items-center gap-1">
                              {agent.name}
                              <button
                                type="button"
                                onClick={() => handleDisassociate('agents', agent.relationId, agent.id)}
                                className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                              >
                                <X className="w-3 h-3" />
                              </button>
                            </Badge>
                          ))
                        )}
                      </div>
                    </div>
                    <div className="space-y-2 mb-4">
                      <div className="flex items-center gap-2">
                        <Cpu className="w-4 h-4 text-muted-foreground" />
                        <Label className="text-sm">
                          {t("prompts.register.form.models", "Models")}
                        </Label>
                      </div>
                      <div className="flex flex-wrap gap-2">
                        {relations.models.length === 0 ? (
                          <span className="text-sm text-muted-foreground">
                            {t("prompts.register.form.noModels", "No hay models asociados")}
                          </span>
                        ) : (
                          relations.models.map((model) => (
                            <Badge key={model.relationId} variant="secondary" className="flex items-center gap-1">
                              {model.name}
                              <button
                                type="button"
                                onClick={() => handleDisassociate('models', model.relationId, model.id)}
                                className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                              >
                                <X className="w-3 h-3" />
                              </button>
                            </Badge>
                          ))
                        )}
                      </div>
                    </div>
                    <div className="space-y-2">
                      <div className="flex items-center gap-2">
                        <Database className="w-4 h-4 text-muted-foreground" />
                        <Label className="text-sm">
                          {t("prompts.register.form.rags", "RAG Systems")}
                        </Label>
                      </div>
                      <div className="flex flex-wrap gap-2">
                        {relations.rags.length === 0 ? (
                          <span className="text-sm text-muted-foreground">
                            {t("prompts.register.form.noRags", "No hay RAG systems asociados")}
                          </span>
                        ) : (
                          relations.rags.map((rag) => (
                            <Badge key={rag.relationId} variant="secondary" className="flex items-center gap-1">
                              {rag.name}
                              <button
                                type="button"
                                onClick={() => handleDisassociate('rags', rag.relationId, rag.id)}
                                className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                              >
                                <X className="w-3 h-3" />
                              </button>
                            </Badge>
                          ))
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </form>
        )}

        {/* Tabs - Solo mostrar si no está en modo edición */}
        {!isEditMode && (
          <Tabs defaultValue={activeTab} value={activeTab} onValueChange={setActiveTab}>
          <TabsList>
            <TabsTrigger value="overview">
              {t("prompts.detail.tabs.overview", "Resumen")}
            </TabsTrigger>
            <TabsTrigger value="content">
              {t("prompts.detail.tabs.content", "Contenido")}
            </TabsTrigger>
            <TabsTrigger value="versions">
              {t("prompts.detail.tabs.versions", "Versiones")} ({versions.length})
            </TabsTrigger>
            <TabsTrigger value="validations">
              {t("prompts.detail.tabs.validations", "Validaciones")} ({validations.length})
            </TabsTrigger>
            <TabsTrigger value="project">
              {t("prompts.detail.tabs.project", "Proyecto")}
            </TabsTrigger>
            <TabsTrigger value="agents">
              {t("prompts.detail.tabs.agents", "Agents")} ({relations.agents.length})
            </TabsTrigger>
            <TabsTrigger value="models">
              {t("prompts.detail.tabs.models", "Models")} ({relations.models.length})
            </TabsTrigger>
            <TabsTrigger value="rags">
              {t("prompts.detail.tabs.rags", "RAG Systems")} ({relations.rags.length})
            </TabsTrigger>
          </TabsList>

          {/* Overview Tab */}
          <TabsContent value="overview" className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardContent className="pt-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-muted-foreground mb-1">
                        {t("prompts.detail.overview.version", "Versión")}
                      </p>
                      <p className="text-2xl font-bold">{prompt.prmversion}</p>
                    </div>
                    <GitBranch className="w-8 h-8 text-primary opacity-50" />
                  </div>
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardContent className="pt-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-muted-foreground mb-1">
                        {t("prompts.detail.overview.validations", "Validaciones")}
                      </p>
                      <p className="text-2xl font-bold">{validations.length}</p>
                    </div>
                    <CheckCircle className="w-8 h-8 text-primary opacity-50" />
                  </div>
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardContent className="pt-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-muted-foreground mb-1">
                        {t("prompts.detail.overview.versions", "Versiones")}
                      </p>
                      <p className="text-2xl font-bold">{versions.length}</p>
                    </div>
                    <FileText className="w-8 h-8 text-primary opacity-50" />
                  </div>
                </CardContent>
              </Card>

              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardContent className="pt-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-muted-foreground mb-1">
                        {t("prompts.detail.overview.approval", "Aprobación")}
                      </p>
                      <Badge variant={getStatusBadge(prompt.prmapprovalstatus)}>
                        {prompt.prmapprovalstatus}
                      </Badge>
                    </div>
                    <Shield className="w-8 h-8 text-primary opacity-50" />
                  </div>
                </CardContent>
              </Card>
            </div>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle>
                  {t("prompts.detail.overview.information", "Información General")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("prompts.detail.overview.project", "Proyecto")}
                    </p>
                    <p className="font-medium">{prompt.projectName || "-"}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("prompts.detail.overview.created", "Creado")}
                    </p>
                    <p className="font-medium">
                      {new Date(prompt.createdAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("prompts.detail.overview.updated", "Actualizado")}
                    </p>
                    <p className="font-medium">
                      {new Date(prompt.updatedAt).toLocaleDateString()}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("prompts.detail.overview.parameters", "Parámetros")}
                    </p>
                    <p className="font-medium text-sm">{prompt.prmparameters}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Content Tab */}
          <TabsContent value="content" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle>
                  {t("prompts.detail.content.title", "Contenido del Prompt")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <pre className="bg-muted/50 p-4 rounded-lg overflow-x-auto text-sm">
                  {prompt.prmcontent}
                </pre>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Versions Tab */}
          <TabsContent value="versions" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle>
                  {t("prompts.detail.versions.title", "Historial de Versiones")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>{t("prompts.detail.versions.version", "Versión")}</TableHead>
                      <TableHead>{t("prompts.detail.versions.description", "Descripción")}</TableHead>
                      <TableHead>{t("prompts.detail.versions.status", "Estado")}</TableHead>
                      <TableHead>{t("prompts.detail.versions.date", "Fecha")}</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {versions.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={4} className="text-center text-muted-foreground">
                          {t("prompts.detail.versions.empty", "No hay versiones registradas")}
                        </TableCell>
                      </TableRow>
                    ) : (
                      versions.map((version) => (
                        <TableRow key={version.idxpromptversion}>
                          <TableCell className="font-medium">{version.prmversion}</TableCell>
                          <TableCell>{version.prmdescription}</TableCell>
                          <TableCell>
                            <Badge variant={getStatusBadge(version.prmstatus)}>
                              {version.prmstatus}
                            </Badge>
                          </TableCell>
                          <TableCell>
                            {new Date(version.prmcreatedat).toLocaleDateString()}
                          </TableCell>
                        </TableRow>
                      ))
                    )}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Validations Tab */}
          <TabsContent value="validations" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>
                    {t("prompts.detail.validations.title", "Validaciones y Análisis")}
                  </CardTitle>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={loadPromptData}
                    className="gap-2"
                  >
                    <RefreshCw className="w-4 h-4" />
                    {t("common.refresh", "Actualizar")}
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>{t("prompts.detail.validations.type", "Tipo")}</TableHead>
                      <TableHead>{t("prompts.detail.validations.result", "Resultado")}</TableHead>
                      <TableHead>{t("prompts.detail.validations.score", "Score")}</TableHead>
                      <TableHead>{t("prompts.detail.validations.issues", "Issues")}</TableHead>
                      <TableHead>{t("prompts.detail.validations.date", "Fecha")}</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {validations.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={5} className="text-center text-muted-foreground">
                          {t("prompts.detail.validations.empty", "No hay validaciones registradas")}
                        </TableCell>
                      </TableRow>
                    ) : (
                      validations.map((validation) => (
                        <TableRow key={validation.idxpromptvalidation}>
                          <TableCell>
                            <Badge className={getValidationTypeBadge(validation.prmvalidationtype)}>
                              {validation.prmvalidationtype}
                            </Badge>
                          </TableCell>
                          <TableCell>
                            <Badge
                              variant={
                                validation.prmvalidationresult === "PASSED"
                                  ? "success"
                                  : validation.prmvalidationresult === "FAILED"
                                  ? "danger"
                                  : "outline"
                              }
                            >
                              {validation.prmvalidationresult}
                            </Badge>
                          </TableCell>
                          <TableCell className="font-semibold">
                            {validation.prmvalidationscore.toFixed(1)}
                          </TableCell>
                          <TableCell>{validation.prmissuesfound}</TableCell>
                          <TableCell>
                            {new Date(validation.prmcreatedat).toLocaleDateString()}
                          </TableCell>
                        </TableRow>
                      ))
                    )}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Project Tab */}
          <TabsContent value="project" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center gap-2">
                  <FolderTree className="w-5 h-5 text-primary" />
                  <CardTitle>
                    {t("prompts.detail.project.title", "Proyecto Asociado")}
                  </CardTitle>
                </div>
              </CardHeader>
              <CardContent>
                {prompt.projectId && prompt.projectName ? (
                  <div className="space-y-4">
                    <div className="flex items-center gap-2 p-4 rounded-lg bg-muted/50 border">
                      <FolderTree className="w-6 h-6 text-primary" />
                      <div className="flex-1">
                        <p className="font-semibold text-lg">{prompt.projectName}</p>
                        <p className="text-sm text-muted-foreground">
                          {t("prompts.detail.project.id", "ID del Proyecto")}: {prompt.projectId}
                        </p>
                      </div>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                          // TODO: Navegar a la página de detalle del proyecto
                          router.push(`/governance/projects/${prompt.projectId}`);
                        }}
                      >
                        {t("prompts.detail.project.view", "Ver Proyecto")}
                      </Button>
                    </div>
                    <div className="p-4 rounded-lg bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800">
                      <p className="text-sm text-blue-700 dark:text-blue-400">
                        {t("prompts.detail.project.info", "Este prompt está asociado a un proyecto. El proyecto determina los requisitos de cumplimiento normativo y gobernanza aplicables.")}
                      </p>
                    </div>
                  </div>
                ) : (
                  <div className="text-center py-12">
                    <FolderTree className="w-16 h-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <p className="text-muted-foreground mb-2">
                      {t("prompts.detail.project.noProject", "Este prompt no está asociado a ningún proyecto")}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {t("prompts.detail.project.noProjectInfo", "Los prompts sin proyecto asociado no tienen requisitos de cumplimiento normativo específicos.")}
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

          {/* Agents Tab */}
          <TabsContent value="agents" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <Bot className="w-5 h-5 text-primary" />
                    <CardTitle>
                      {t("prompts.detail.agents.title", "Agents Asociados")}
                    </CardTitle>
                    <Badge variant="secondary">
                      {relations.agents.length}
                    </Badge>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={async () => {
                      await loadAvailableAgents();
                      setAddAgentDialogOpen(true);
                    }}
                  >
                    {t("prompts.detail.agents.add", "Agregar Agent")}
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                {relations.agents.length === 0 ? (
                  <div className="text-center py-12">
                    <Bot className="w-16 h-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <p className="text-muted-foreground mb-2">
                      {t("prompts.detail.agents.noAgents", "No hay agents asociados a este prompt")}
                    </p>
                    <p className="text-sm text-muted-foreground mb-4">
                      {t("prompts.detail.agents.noAgentsInfo", "Los agents pueden usar este prompt para generar respuestas.")}
                    </p>
                    <Button
                      variant="outline"
                      onClick={async () => {
                        await loadAvailableAgents();
                        setAddAgentDialogOpen(true);
                      }}
                    >
                      {t("prompts.detail.agents.addFirst", "Agregar Primer Agent")}
                    </Button>
                  </div>
                ) : (
                  <div className="space-y-2">
                    {relations.agents.map((agent) => (
                      <div
                        key={agent.relationId}
                        className="flex items-center justify-between p-4 rounded-lg bg-muted/50 border hover:bg-muted/70 transition-colors"
                      >
                        <div className="flex items-center gap-3">
                          <Bot className="w-5 h-5 text-primary" />
                          <div>
                            <p className="font-medium">{agent.name}</p>
                            <p className="text-sm text-muted-foreground">
                              {t("prompts.detail.agents.id", "ID")}: {agent.id}
                            </p>
                          </div>
                        </div>
                        <div className="flex items-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              // TODO: Navegar a la página de detalle del agent
                              router.push(`/governance/agents/${agent.id}`);
                            }}
                          >
                            {t("prompts.detail.agents.view", "Ver")}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDisassociate('agents', agent.relationId, agent.id)}
                            className="text-destructive hover:text-destructive hover:bg-destructive/10"
                          >
                            <X className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

          {/* Models Tab */}
          <TabsContent value="models" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <Cpu className="w-5 h-5 text-primary" />
                    <CardTitle>
                      {t("prompts.detail.models.title", "Models Asociados")}
                    </CardTitle>
                    <Badge variant="secondary">
                      {relations.models.length}
                    </Badge>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={async () => {
                      await loadAvailableModels();
                      setAddModelDialogOpen(true);
                    }}
                  >
                    {t("prompts.detail.models.add", "Agregar Model")}
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                {relations.models.length === 0 ? (
                  <div className="text-center py-12">
                    <Cpu className="w-16 h-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <p className="text-muted-foreground mb-2">
                      {t("prompts.detail.models.noModels", "No hay models asociados a este prompt")}
                    </p>
                    <p className="text-sm text-muted-foreground mb-4">
                      {t("prompts.detail.models.noModelsInfo", "Los models pueden usar este prompt directamente en chat sin necesidad de un agent.")}
                    </p>
                    <Button
                      variant="outline"
                      onClick={async () => {
                        await loadAvailableModels();
                        setAddModelDialogOpen(true);
                      }}
                    >
                      {t("prompts.detail.models.addFirst", "Agregar Primer Model")}
                    </Button>
                  </div>
                ) : (
                  <div className="space-y-2">
                    {relations.models.map((model) => (
                      <div
                        key={model.relationId}
                        className="flex items-center justify-between p-4 rounded-lg bg-muted/50 border hover:bg-muted/70 transition-colors"
                      >
                        <div className="flex items-center gap-3">
                          <Cpu className="w-5 h-5 text-primary" />
                          <div>
                            <p className="font-medium">{model.name}</p>
                            <p className="text-sm text-muted-foreground">
                              {t("prompts.detail.models.id", "ID")}: {model.id}
                            </p>
                          </div>
                        </div>
                        <div className="flex items-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              // TODO: Navegar a la página de detalle del model
                              router.push(`/governance/models/${model.id}`);
                            }}
                          >
                            {t("prompts.detail.models.view", "Ver")}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDisassociate('models', model.relationId, model.id)}
                            className="text-destructive hover:text-destructive hover:bg-destructive/10"
                          >
                            <X className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

          {/* RAG Systems Tab */}
          <TabsContent value="rags" className="space-y-4">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <Database className="w-5 h-5 text-primary" />
                    <CardTitle>
                      {t("prompts.detail.rags.title", "RAG Systems Asociados")}
                    </CardTitle>
                    <Badge variant="secondary">
                      {relations.rags.length}
                    </Badge>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={async () => {
                      await loadAvailableRags();
                      setAddRagDialogOpen(true);
                    }}
                  >
                    {t("prompts.detail.rags.add", "Agregar RAG System")}
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                {relations.rags.length === 0 ? (
                  <div className="text-center py-12">
                    <Database className="w-16 h-16 text-muted-foreground mx-auto mb-4 opacity-50" />
                    <p className="text-muted-foreground mb-2">
                      {t("prompts.detail.rags.noRags", "No hay RAG systems asociados a este prompt")}
                    </p>
                    <p className="text-sm text-muted-foreground mb-4">
                      {t("prompts.detail.rags.noRagsInfo", "Los sistemas RAG pueden usar este prompt para generar respuestas enriquecidas con conocimiento contextual.")}
                    </p>
                    <Button
                      variant="outline"
                      onClick={async () => {
                        await loadAvailableRags();
                        setAddRagDialogOpen(true);
                      }}
                    >
                      {t("prompts.detail.rags.addFirst", "Agregar Primer RAG System")}
                    </Button>
                  </div>
                ) : (
                  <div className="space-y-2">
                    {relations.rags.map((rag) => (
                      <div
                        key={rag.relationId}
                        className="flex items-center justify-between p-4 rounded-lg bg-muted/50 border hover:bg-muted/70 transition-colors"
                      >
                        <div className="flex items-center gap-3">
                          <Database className="w-5 h-5 text-primary" />
                          <div>
                            <p className="font-medium">{rag.name}</p>
                            <p className="text-sm text-muted-foreground">
                              {t("prompts.detail.rags.id", "ID")}: {rag.id}
                            </p>
                          </div>
                        </div>
                        <div className="flex items-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              // TODO: Navegar a la página de detalle del RAG system
                              router.push(`/governance/rags/${rag.id}`);
                            }}
                          >
                            {t("prompts.detail.rags.view", "Ver")}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDisassociate('rags', rag.relationId, rag.id)}
                            className="text-destructive hover:text-destructive hover:bg-destructive/10"
                          >
                            <X className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>

        </Tabs>
        )}
      </div>

      {/* Delete Dialog */}
      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>
              {t("prompts.detail.delete.title", "Eliminar Prompt")}
            </AlertDialogTitle>
            <AlertDialogDescription>
              {t(
                "prompts.detail.delete.description",
                "¿Estás seguro de que deseas eliminar este prompt? Esta acción no se puede deshacer."
              )}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>
              {t("common.cancel", "Cancelar")}
            </AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-destructive">
              {t("common.delete", "Eliminar")}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Add Agent Dialog */}
      <Dialog open={addAgentDialogOpen} onOpenChange={setAddAgentDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {t("prompts.detail.agents.addDialog.title", "Agregar Agent")}
            </DialogTitle>
            <DialogDescription>
              {t("prompts.detail.agents.addDialog.description", "Selecciona un agent para asociar con este prompt")}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            {loadingAvailable ? (
              <div className="flex items-center justify-center py-8">
                <Loader2 className="w-6 h-6 animate-spin text-primary" />
              </div>
            ) : availableAgents.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">
                {t("prompts.detail.agents.addDialog.noAvailable", "No hay agents disponibles para asociar")}
              </p>
            ) : (
              <div className="space-y-2 max-h-[400px] overflow-y-auto">
                {availableAgents.map((agent) => (
                  <div
                    key={agent.id}
                    className={`p-3 rounded-lg border cursor-pointer transition-colors ${
                      selectedAgentId === agent.id
                        ? "bg-primary/10 border-primary"
                        : "bg-muted/50 border-border hover:bg-muted"
                    }`}
                    onClick={() => setSelectedAgentId(agent.id)}
                  >
                    <div className="flex items-center gap-3">
                      <Bot className="w-5 h-5 text-primary" />
                      <div className="flex-1">
                        <p className="font-medium">{agent.name}</p>
                        <p className="text-sm text-muted-foreground">ID: {agent.id}</p>
                      </div>
                      {selectedAgentId === agent.id && (
                        <CheckCircle className="w-5 h-5 text-primary" />
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setAddAgentDialogOpen(false);
                setSelectedAgentId(null);
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={handleAssociateAgent}
              disabled={!selectedAgentId || associating}
            >
              {associating ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  {t("common.associating", "Asociando...")}
                </>
              ) : (
                t("common.associate", "Asociar")
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Add Model Dialog */}
      <Dialog open={addModelDialogOpen} onOpenChange={setAddModelDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {t("prompts.detail.models.addDialog.title", "Agregar Model")}
            </DialogTitle>
            <DialogDescription>
              {t("prompts.detail.models.addDialog.description", "Selecciona un model para asociar con este prompt")}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            {loadingAvailable ? (
              <div className="flex items-center justify-center py-8">
                <Loader2 className="w-6 h-6 animate-spin text-primary" />
              </div>
            ) : availableModels.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">
                {t("prompts.detail.models.addDialog.noAvailable", "No hay models disponibles para asociar")}
              </p>
            ) : (
              <div className="space-y-2 max-h-[400px] overflow-y-auto">
                {availableModels.map((model) => (
                  <div
                    key={model.id}
                    className={`p-3 rounded-lg border cursor-pointer transition-colors ${
                      selectedModelId === model.id
                        ? "bg-primary/10 border-primary"
                        : "bg-muted/50 border-border hover:bg-muted"
                    }`}
                    onClick={() => setSelectedModelId(model.id)}
                  >
                    <div className="flex items-center gap-3">
                      <Cpu className="w-5 h-5 text-primary" />
                      <div className="flex-1">
                        <p className="font-medium">{model.name}</p>
                        <p className="text-sm text-muted-foreground">ID: {model.id}</p>
                      </div>
                      {selectedModelId === model.id && (
                        <CheckCircle className="w-5 h-5 text-primary" />
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setAddModelDialogOpen(false);
                setSelectedModelId(null);
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={handleAssociateModel}
              disabled={!selectedModelId || associating}
            >
              {associating ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  {t("common.associating", "Asociando...")}
                </>
              ) : (
                t("common.associate", "Asociar")
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Add RAG Dialog */}
      <Dialog open={addRagDialogOpen} onOpenChange={setAddRagDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {t("prompts.detail.rags.addDialog.title", "Agregar RAG System")}
            </DialogTitle>
            <DialogDescription>
              {t("prompts.detail.rags.addDialog.description", "Selecciona un RAG system para asociar con este prompt")}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            {loadingAvailable ? (
              <div className="flex items-center justify-center py-8">
                <Loader2 className="w-6 h-6 animate-spin text-primary" />
              </div>
            ) : availableRags.length === 0 ? (
              <p className="text-center text-muted-foreground py-4">
                {t("prompts.detail.rags.addDialog.noAvailable", "No hay RAG systems disponibles para asociar")}
              </p>
            ) : (
              <div className="space-y-2 max-h-[400px] overflow-y-auto">
                {availableRags.map((rag) => (
                  <div
                    key={rag.id}
                    className={`p-3 rounded-lg border cursor-pointer transition-colors ${
                      selectedRagId === rag.id
                        ? "bg-primary/10 border-primary"
                        : "bg-muted/50 border-border hover:bg-muted"
                    }`}
                    onClick={() => setSelectedRagId(rag.id)}
                  >
                    <div className="flex items-center gap-3">
                      <Database className="w-5 h-5 text-primary" />
                      <div className="flex-1">
                        <p className="font-medium">{rag.name}</p>
                        <p className="text-sm text-muted-foreground">ID: {rag.id}</p>
                      </div>
                      {selectedRagId === rag.id && (
                        <CheckCircle className="w-5 h-5 text-primary" />
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
          <DialogFooter>
            <Button
              variant="outline"
              onClick={() => {
                setAddRagDialogOpen(false);
                setSelectedRagId(null);
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={handleAssociateRag}
              disabled={!selectedRagId || associating}
            >
              {associating ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  {t("common.associating", "Asociando...")}
                </>
              ) : (
                t("common.associate", "Asociar")
              )}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
