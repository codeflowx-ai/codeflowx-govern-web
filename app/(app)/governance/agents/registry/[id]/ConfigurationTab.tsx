"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { Settings, Plus, Trash2, Edit, Save, X, Loader2, ArrowUp, ArrowDown, GripVertical, FileCode, Brain, CheckCircle2 } from "lucide-react";
import { useState, useEffect, useRef } from "react";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { SimpleModal } from "@/components/ui/SimpleModal";

interface AgentProcessConfig {
  idxcoragentprocessconfig?: number;
  agentId: number;
  processType: string;
  processKey: string;
  conditions?: Record<string, any>;
  priority: number;
  isActive: boolean;
  createdBy?: string;
  updatedBy?: string;
  createdAt?: string;
  updatedAt?: string;
}

interface AgentRuleConfig {
  idxcoragentruleconfig?: number;
  agentId: number;
  ruleType: string;
  ruleId: string;
  executionOrder: number;
  priority: number;
  conditions?: Record<string, any>;
  isActive: boolean;
  createdBy?: string;
  updatedBy?: string;
  createdAt?: string;
  updatedAt?: string;
}

interface AgentAlgorithmConfig {
  idxcoragentalgorithmconfig?: number;
  agentId: number;
  service: string;
  algorithmName: string;
  context?: string;
  configuration?: Record<string, any>;
  priority: number;
  isActive: boolean;
  createdBy?: string;
  updatedBy?: string;
  createdAt?: string;
  updatedAt?: string;
}

interface AgentExecutionOrder {
  idxcoragentexecutionorder?: number;
  agentId: number;
  executionType: "PROCESS" | "RULE" | "ALGORITHM";
  executionOrder: number;
  configId: string;
  configKey?: string;
  conditions?: Record<string, any>;
  isActive: boolean;
  createdAt?: string;
  updatedAt?: string;
}

interface ConfigurationTabProps {
  agentId: string;
  agentUuid?: string;
}

export default function ConfigurationTab({ agentId, agentUuid }: ConfigurationTabProps) {
  const { t, mounted } = useTranslation();
  const [processConfigs, setProcessConfigs] = useState<AgentProcessConfig[]>([]);
  const [ruleConfigs, setRuleConfigs] = useState<AgentRuleConfig[]>([]);
  const [algorithmConfigs, setAlgorithmConfigs] = useState<AgentAlgorithmConfig[]>([]);
  const [executionOrder, setExecutionOrder] = useState<AgentExecutionOrder[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [activeTab, setActiveTab] = useState("execution-order");
  const [draggedIndex, setDraggedIndex] = useState<number | null>(null);
  const [draggedRuleIndex, setDraggedRuleIndex] = useState<number | null>(null);
  const [editingProcess, setEditingProcess] = useState<AgentProcessConfig | null>(null);
  const [editingAlgorithm, setEditingAlgorithm] = useState<AgentAlgorithmConfig | null>(null);
  const [showProcessModal, setShowProcessModal] = useState(false);
  const [showAlgorithmModal, setShowAlgorithmModal] = useState(false);
  const [isDemoMode, setIsDemoMode] = useState(false);
  const isDemoModeRef = useRef(false);

  useEffect(() => {
    if (agentId) {
      loadConfigurations();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [agentId]);

  const loadConfigurations = async () => {
    setLoading(true);
    try {
      // Intentar cargar primero procesos para detectar modo demo
      await loadProcessConfigs();

      // Cargar el resto (verifican isDemoModeRef internamente)
      await Promise.all([
        loadRuleConfigs(),
        loadAlgorithmConfigs(),
        loadExecutionOrder(),
      ]);
    } catch (error) {
      console.error("Error loading configurations:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadExecutionOrder = async () => {
    // Si ya detectamos modo demo, construir desde configuraciones sin hacer llamada HTTP (usar ref para verificación síncrona)
    if (isDemoModeRef.current) {
      if (processConfigs.length > 0 || ruleConfigs.length > 0 || algorithmConfigs.length > 0) {
        buildExecutionOrderFromConfigs();
      }
      return;
    }

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2000); // Timeout de 2 segundos

      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/execution-order`, {
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (response.ok) {
        const data = await response.json();
        if (data && Array.isArray(data) && data.length > 0) {
          setExecutionOrder(data);
        } else {
          // Si no hay orden guardado, construir desde configuraciones (pero solo si ya están cargadas)
          if (processConfigs.length > 0 || ruleConfigs.length > 0 || algorithmConfigs.length > 0) {
            buildExecutionOrderFromConfigs();
          }
        }
      } else {
        // Si el backend no responde correctamente, activar modo demo
        isDemoModeRef.current = true;
        setIsDemoMode(true);
        if (processConfigs.length > 0 || ruleConfigs.length > 0 || algorithmConfigs.length > 0) {
          buildExecutionOrderFromConfigs();
        }
      }
    } catch (error) {
      // Cualquier error (timeout, conexión rechazada, etc.) activa modo demo
      isDemoModeRef.current = true;
      setIsDemoMode(true);
      if (processConfigs.length > 0 || ruleConfigs.length > 0 || algorithmConfigs.length > 0) {
        buildExecutionOrderFromConfigs();
      }
    }
  };

  const buildExecutionOrderFromConfigs = () => {
    const order: AgentExecutionOrder[] = [];
    let orderNum = 1;

    // Agregar procesos
    processConfigs
      .filter(c => c.isActive)
      .sort((a, b) => b.priority - a.priority)
      .forEach(config => {
        order.push({
          agentId: parseInt(agentId),
          executionType: "PROCESS",
          executionOrder: orderNum++,
          configId: config.idxcoragentprocessconfig?.toString() || "",
          configKey: config.processKey,
          isActive: true,
        });
      });

    // Agregar reglas
    ruleConfigs
      .filter(c => c.isActive)
      .sort((a, b) => a.executionOrder - b.executionOrder)
      .forEach(config => {
        order.push({
          agentId: parseInt(agentId),
          executionType: "RULE",
          executionOrder: orderNum++,
          configId: config.idxcoragentruleconfig?.toString() || "",
          configKey: config.ruleId,
          isActive: true,
        });
      });

    // Agregar algoritmos
    algorithmConfigs
      .filter(c => c.isActive)
      .sort((a, b) => b.priority - a.priority)
      .forEach(config => {
        order.push({
          agentId: parseInt(agentId),
          executionType: "ALGORITHM",
          executionOrder: orderNum++,
          configId: config.idxcoragentalgorithmconfig?.toString() || "",
          configKey: config.algorithmName,
          isActive: true,
        });
      });

    setExecutionOrder(order);
  };

  const loadProcessConfigs = async () => {
    // En modo demo, usar directamente mock data sin hacer llamada HTTP
    const mockData: AgentProcessConfig[] = [
      {
        idxcoragentprocessconfig: 1,
        agentId: parseInt(agentId),
        processType: "APPROVAL",
        processKey: "agent-approval-v1",
        priority: 10,
        isActive: true,
        createdAt: new Date().toISOString(),
      },
    ];

    // Si ya detectamos modo demo, usar directamente mock data (usar ref para verificación síncrona)
    if (isDemoModeRef.current) {
      setProcessConfigs(mockData);
      return;
    }

    try {
      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/processes`);
      if (response.ok) {
        const data = await response.json();
        setProcessConfigs(data);
      } else {
        // Modo demo: usar mock data y marcar como demo
        isDemoModeRef.current = true;
        setIsDemoMode(true);
        setProcessConfigs(mockData);
      }
    } catch (error) {
      // Modo demo: usar mock data y marcar como demo (no hacer más llamadas HTTP)
      isDemoModeRef.current = true;
      setIsDemoMode(true);
      setProcessConfigs(mockData);
    }
  };

  const loadRuleConfigs = async () => {
    // En modo demo, usar directamente mock data sin hacer llamada HTTP
    const mockData: AgentRuleConfig[] = [
      {
        idxcoragentruleconfig: 1,
        agentId: parseInt(agentId),
        ruleType: "DROOLS",
        ruleId: "low-confidence-rule",
        executionOrder: 1,
        priority: 10,
        isActive: true,
        createdAt: new Date().toISOString(),
      },
    ];

    // Si ya detectamos modo demo, usar directamente mock data sin hacer llamada HTTP (usar ref para verificación síncrona)
    if (isDemoModeRef.current) {
      setRuleConfigs(mockData);
      return;
    }

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2000); // Timeout de 2 segundos

      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/rules`, {
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (response.ok) {
        const data = await response.json();
        setRuleConfigs(data);
      } else {
        // Si el backend no responde correctamente, activar modo demo
        isDemoModeRef.current = true;
        setIsDemoMode(true);
        setRuleConfigs(mockData);
      }
    } catch (error) {
      // Cualquier error (timeout, conexión rechazada, etc.) activa modo demo
      isDemoModeRef.current = true;
      setIsDemoMode(true);
      setRuleConfigs(mockData);
    }
  };

  const loadAlgorithmConfigs = async () => {
    // En modo demo, usar directamente mock data sin hacer llamada HTTP
    const mockData: AgentAlgorithmConfig[] = [
      {
        idxcoragentalgorithmconfig: 1,
        agentId: parseInt(agentId),
        service: "bias-detection",
        algorithmName: "statistical_parity",
        context: "agent-evaluation",
        priority: 10,
        isActive: true,
        createdAt: new Date().toISOString(),
      },
    ];

    // Si ya detectamos modo demo, usar directamente mock data sin hacer llamada HTTP (usar ref para verificación síncrona)
    if (isDemoModeRef.current) {
      setAlgorithmConfigs(mockData);
      return;
    }

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2000); // Timeout de 2 segundos

      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/algorithms`, {
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (response.ok) {
        const data = await response.json();
        setAlgorithmConfigs(data);
      } else {
        // Si el backend no responde correctamente, activar modo demo
        isDemoModeRef.current = true;
        setIsDemoMode(true);
        setAlgorithmConfigs(mockData);
      }
    } catch (error) {
      // Cualquier error (timeout, conexión rechazada, etc.) activa modo demo
      isDemoModeRef.current = true;
      setIsDemoMode(true);
      setAlgorithmConfigs(mockData);
    }
  };

  useEffect(() => {
    // Reconstruir orden de ejecución cuando cambien las configuraciones (solo si no hay orden guardado y no está cargando)
    if (!loading && executionOrder.length === 0 && (processConfigs.length > 0 || ruleConfigs.length > 0 || algorithmConfigs.length > 0)) {
      buildExecutionOrderFromConfigs();
    }
  }, [loading, executionOrder.length, processConfigs.length, ruleConfigs.length, algorithmConfigs.length]);

  const handleSaveProcess = async (config: AgentProcessConfig) => {
    setSaving(true);
    try {
      const url = editingProcess?.idxcoragentprocessconfig
        ? `/api/v1/governance/agents/registry/${agentId}/configuration/processes/${editingProcess.idxcoragentprocessconfig}`
        : `/api/v1/governance/agents/registry/${agentId}/configuration/processes`;

      const method = editingProcess?.idxcoragentprocessconfig ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...config, agentId: parseInt(agentId) }),
      });

      if (response.ok) {
        await loadProcessConfigs();
        await loadExecutionOrder();
        setShowProcessModal(false);
        setEditingProcess(null);
      } else {
        // Modo demo: simular guardado exitoso
        const savedConfig: AgentProcessConfig = {
          ...config,
          idxcoragentprocessconfig: editingProcess?.idxcoragentprocessconfig || Date.now(),
          agentId: parseInt(agentId),
          createdAt: editingProcess?.createdAt || new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        };

        if (editingProcess?.idxcoragentprocessconfig) {
          setProcessConfigs(processConfigs.map(c =>
            c.idxcoragentprocessconfig === editingProcess.idxcoragentprocessconfig
              ? savedConfig
              : c
          ));
        } else {
          setProcessConfigs([...processConfigs, savedConfig]);
        }

        setShowProcessModal(false);
        setEditingProcess(null);
      }
    } catch (error) {
      console.warn("Error saving process config (demo mode):", error);
      // Modo demo: simular guardado exitoso
      const savedConfig: AgentProcessConfig = {
        ...config,
        idxcoragentprocessconfig: editingProcess?.idxcoragentprocessconfig || Date.now(),
        agentId: parseInt(agentId),
        createdAt: editingProcess?.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      if (editingProcess?.idxcoragentprocessconfig) {
        setProcessConfigs(processConfigs.map(c =>
          c.idxcoragentprocessconfig === editingProcess.idxcoragentprocessconfig
            ? savedConfig
            : c
        ));
      } else {
        setProcessConfigs([...processConfigs, savedConfig]);
      }

      setShowProcessModal(false);
      setEditingProcess(null);
    } finally {
      setSaving(false);
    }
  };

  const handleSaveRules = async () => {
    setSaving(true);
    try {
      // Ordenar por executionOrder y eliminar duplicados antes de guardar
      const sortedRules = [...ruleConfigs]
        .filter(c => c.isActive)
        .sort((a, b) => a.executionOrder - b.executionOrder);

      // Eliminar duplicados (mismo ruleId y ruleType)
      const uniqueRules = sortedRules.filter((rule, index, self) =>
        index === self.findIndex((r) => r.ruleId === rule.ruleId && r.ruleType === rule.ruleType)
      );

      // Reasignar executionOrder secuencial
      const rulesToSave = uniqueRules.map((c, index) => ({
        ...c,
        executionOrder: index + 1,
        agentId: parseInt(agentId),
      }));

      // Actualizar estado local con reglas únicas y ordenadas
      setRuleConfigs(rulesToSave);

      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/rules/batch`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ rules: rulesToSave }),
      });

      if (response.ok) {
        await loadRuleConfigs();
        await loadExecutionOrder();
        alert("Reglas guardadas correctamente");
      } else {
        // Modo demo: simular guardado exitoso
        alert("Reglas guardadas correctamente (modo demo)");
      }
    } catch (error) {
      console.warn("Error saving rules (demo mode):", error);
      // Modo demo: simular guardado exitoso
      alert("Reglas guardadas correctamente (modo demo)");
    } finally {
      setSaving(false);
    }
  };

  const handleToggleRule = (ruleId: string, ruleType: string) => {
    const existingIndex = ruleConfigs.findIndex((r) => r.ruleId === ruleId && r.ruleType === ruleType);

    if (existingIndex >= 0) {
      // Remover regla
      const newConfigs = ruleConfigs.filter((_, i) => i !== existingIndex);
      // Reordenar executionOrder
      const updatedConfigs = newConfigs.map((r, i) => ({ ...r, executionOrder: i + 1 }));
      setRuleConfigs(updatedConfigs);
      // Actualizar orden de ejecución
      buildExecutionOrderFromConfigs();
    } else {
      // Agregar regla (verificar que no esté duplicada)
      const newConfig: AgentRuleConfig = {
        agentId: parseInt(agentId),
        ruleType: ruleType,
        ruleId: ruleId,
        executionOrder: ruleConfigs.length + 1,
        priority: 10,
        isActive: true,
      };
      const updatedConfigs = [...ruleConfigs, newConfig];
      setRuleConfigs(updatedConfigs);
      // Actualizar orden de ejecución
      buildExecutionOrderFromConfigs();
    }
  };

  const handleMoveRule = (index: number, direction: "up" | "down") => {
    // Ordenar primero por executionOrder para trabajar con el orden correcto
    const sortedConfigs = [...ruleConfigs].sort((a, b) => a.executionOrder - b.executionOrder);

    if (direction === "up" && index > 0) {
      [sortedConfigs[index], sortedConfigs[index - 1]] = [sortedConfigs[index - 1], sortedConfigs[index]];
    } else if (direction === "down" && index < sortedConfigs.length - 1) {
      [sortedConfigs[index], sortedConfigs[index + 1]] = [sortedConfigs[index + 1], sortedConfigs[index]];
    }

    // Reordenar executionOrder secuencialmente
    const updatedConfigs = sortedConfigs.map((r, i) => ({ ...r, executionOrder: i + 1 }));
    setRuleConfigs(updatedConfigs);
    // Actualizar orden de ejecución
    buildExecutionOrderFromConfigs();
  };

  const handleDragStartRule = (index: number) => {
    setDraggedRuleIndex(index);
  };

  const handleDragOverRule = (e: React.DragEvent, index: number) => {
    e.preventDefault();
    if (draggedRuleIndex === null || draggedRuleIndex === index) return;

    // Ordenar primero por executionOrder
    const sortedConfigs = [...ruleConfigs].sort((a, b) => a.executionOrder - b.executionOrder);
    const newConfigs = [...sortedConfigs];
    const draggedRule = newConfigs[draggedRuleIndex];
    newConfigs.splice(draggedRuleIndex, 1);
    newConfigs.splice(index, 0, draggedRule);

    // Reordenar executionOrder secuencialmente
    const updatedConfigs = newConfigs.map((r, i) => ({ ...r, executionOrder: i + 1 }));
    setRuleConfigs(updatedConfigs);
    setDraggedRuleIndex(index);
    // Actualizar orden de ejecución
    buildExecutionOrderFromConfigs();
  };

  const handleDragEndRule = () => {
    setDraggedRuleIndex(null);
  };

  const handleRemoveRule = (index: number) => {
    // Ordenar primero por executionOrder para trabajar con el orden correcto
    const sortedConfigs = [...ruleConfigs].sort((a, b) => a.executionOrder - b.executionOrder);
    const newConfigs = sortedConfigs.filter((_, i) => i !== index);
    // Reordenar executionOrder secuencialmente
    setRuleConfigs(newConfigs.map((r, i) => ({ ...r, executionOrder: i + 1 })));
    // Actualizar orden de ejecución
    buildExecutionOrderFromConfigs();
  };

  const handleSaveExecutionOrder = async () => {
    setSaving(true);
    try {
      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/configuration/execution-order`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(executionOrder.map((o, i) => ({ ...o, executionOrder: i + 1 }))),
      });

      if (response.ok) {
        await loadExecutionOrder();
        alert("Orden de ejecución guardado correctamente");
      } else {
        // Modo demo: simular guardado exitoso
        alert("Orden de ejecución guardado correctamente (modo demo)");
      }
    } catch (error) {
      console.warn("Error saving execution order (demo mode):", error);
      alert("Orden de ejecución guardado correctamente (modo demo)");
    } finally {
      setSaving(false);
    }
  };

  const handleMoveExecutionStep = (index: number, direction: "up" | "down") => {
    const sortedOrder = [...executionOrder].sort((a, b) => a.executionOrder - b.executionOrder);
    if (direction === "up" && index > 0) {
      [sortedOrder[index], sortedOrder[index - 1]] = [sortedOrder[index - 1], sortedOrder[index]];
    } else if (direction === "down" && index < sortedOrder.length - 1) {
      [sortedOrder[index], sortedOrder[index + 1]] = [sortedOrder[index + 1], sortedOrder[index]];
    }
    // Reordenar secuencialmente
    setExecutionOrder(sortedOrder.map((o, i) => ({ ...o, executionOrder: i + 1 })));
  };

  const handleDragStart = (index: number) => {
    setDraggedIndex(index);
  };

  const handleDragOver = (e: React.DragEvent, index: number) => {
    e.preventDefault();
    if (draggedIndex === null || draggedIndex === index) return;

    const sortedOrder = [...executionOrder].sort((a, b) => a.executionOrder - b.executionOrder);
    const newOrder = [...sortedOrder];
    const draggedStep = newOrder[draggedIndex];
    newOrder.splice(draggedIndex, 1);
    newOrder.splice(index, 0, draggedStep);

    setExecutionOrder(newOrder.map((o, i) => ({ ...o, executionOrder: i + 1 })));
    setDraggedIndex(index);
  };

  const handleDragEnd = () => {
    setDraggedIndex(null);
  };

  const handleRemoveExecutionStep = (index: number) => {
    const newOrder = executionOrder.filter((_, i) => i !== index);
    setExecutionOrder(newOrder.map((o, i) => ({ ...o, executionOrder: i + 1 })));
  };

  const getExecutionStepLabel = (step: AgentExecutionOrder): string => {
    if (step.executionType === "PROCESS") {
      const process = processConfigs.find(p => p.idxcoragentprocessconfig?.toString() === step.configId);
      return process ? `${process.processType}: ${process.processKey}` : step.configKey || "Proceso";
    } else if (step.executionType === "RULE") {
      const rule = ruleConfigs.find(r => r.idxcoragentruleconfig?.toString() === step.configId);
      return rule ? `${rule.ruleType}: ${rule.ruleId}` : step.configKey || "Regla";
    } else {
      const algorithm = algorithmConfigs.find(a => a.idxcoragentalgorithmconfig?.toString() === step.configId);
      return algorithm ? `${algorithm.service}: ${algorithm.algorithmName}` : step.configKey || "Algoritmo";
    }
  };

  const getExecutionStepIcon = (step: AgentExecutionOrder) => {
    if (step.executionType === "PROCESS") {
      return <Settings className="w-4 h-4 text-blue-500" />;
    } else if (step.executionType === "RULE") {
      // Diferenciar entre Drools y LLM_PROMPT
      // Buscar por configId primero, si no existe buscar por configKey (ruleId)
      const rule = ruleConfigs.find(r =>
        (step.configId && r.idxcoragentruleconfig?.toString() === step.configId) ||
        (!step.configId && r.ruleId === step.configKey)
      );
      if (rule?.ruleType === "LLM_PROMPT") {
        return <Brain className="w-4 h-4 text-purple-500" />;
      }
      return <FileCode className="w-4 h-4 text-primary" />;
    } else if (step.executionType === "ALGORITHM") {
      return <Brain className="w-4 h-4 text-purple-500" />;
    }
    return null;
  };

  const getExecutionStepTypeLabel = (step: AgentExecutionOrder): string => {
    if (step.executionType === "PROCESS") {
      return "PROCESS";
    } else if (step.executionType === "RULE") {
      // Diferenciar entre Drools y LLM_PROMPT
      // Buscar por configId primero, si no existe buscar por configKey (ruleId)
      const rule = ruleConfigs.find(r =>
        (step.configId && r.idxcoragentruleconfig?.toString() === step.configId) ||
        (!step.configId && r.ruleId === step.configKey)
      );
      if (rule?.ruleType === "LLM_PROMPT") {
        return "LLM_PROMPT";
      }
      return "DROOLS";
    } else if (step.executionType === "ALGORITHM") {
      return "ALGORITHM";
    }
    return step.executionType;
  };

  const handleSaveAlgorithm = async (config: AgentAlgorithmConfig) => {
    setSaving(true);
    try {
      const url = editingAlgorithm?.idxcoragentalgorithmconfig
        ? `/api/v1/governance/agents/registry/${agentId}/configuration/algorithms/${editingAlgorithm.idxcoragentalgorithmconfig}`
        : `/api/v1/governance/agents/registry/${agentId}/configuration/algorithms`;

      const method = editingAlgorithm?.idxcoragentalgorithmconfig ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...config, agentId: parseInt(agentId) }),
      });

      if (response.ok) {
        await loadAlgorithmConfigs();
        await loadExecutionOrder();
        setShowAlgorithmModal(false);
        setEditingAlgorithm(null);
      } else {
        // Modo demo: simular guardado exitoso
        const savedConfig: AgentAlgorithmConfig = {
          ...config,
          idxcoragentalgorithmconfig: editingAlgorithm?.idxcoragentalgorithmconfig || Date.now(),
          agentId: parseInt(agentId),
          createdAt: editingAlgorithm?.createdAt || new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        };

        if (editingAlgorithm?.idxcoragentalgorithmconfig) {
          setAlgorithmConfigs(algorithmConfigs.map(c =>
            c.idxcoragentalgorithmconfig === editingAlgorithm.idxcoragentalgorithmconfig
              ? savedConfig
              : c
          ));
        } else {
          setAlgorithmConfigs([...algorithmConfigs, savedConfig]);
        }

        setShowAlgorithmModal(false);
        setEditingAlgorithm(null);
      }
    } catch (error) {
      console.warn("Error saving algorithm config (demo mode):", error);
      // Modo demo: simular guardado exitoso
      const savedConfig: AgentAlgorithmConfig = {
        ...config,
        idxcoragentalgorithmconfig: editingAlgorithm?.idxcoragentalgorithmconfig || Date.now(),
        agentId: parseInt(agentId),
        createdAt: editingAlgorithm?.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      if (editingAlgorithm?.idxcoragentalgorithmconfig) {
        setAlgorithmConfigs(algorithmConfigs.map(c =>
          c.idxcoragentalgorithmconfig === editingAlgorithm.idxcoragentalgorithmconfig
            ? savedConfig
            : c
        ));
      } else {
        setAlgorithmConfigs([...algorithmConfigs, savedConfig]);
      }

      setShowAlgorithmModal(false);
      setEditingAlgorithm(null);
    } finally {
      setSaving(false);
    }
  };

  const handleDeleteProcess = async (id: number) => {
    if (!confirm("¿Está seguro de eliminar esta configuración de proceso?")) return;

    try {
      const response = await fetch(
        `/api/v1/governance/agents/registry/${agentId}/configuration/processes/${id}`,
        { method: "DELETE" }
      );
      if (response.ok) {
        await loadProcessConfigs();
      } else {
        // Modo demo: eliminar del estado local
        setProcessConfigs(processConfigs.filter(c => c.idxcoragentprocessconfig !== id));
      }
    } catch (error) {
      console.warn("Error deleting process config (demo mode):", error);
      // Modo demo: eliminar del estado local
      setProcessConfigs(processConfigs.filter(c => c.idxcoragentprocessconfig !== id));
    }
  };


  const handleDeleteAlgorithm = async (id: number) => {
    if (!confirm("¿Está seguro de eliminar esta configuración de algoritmo?")) return;

    try {
      const response = await fetch(
        `/api/v1/governance/agents/registry/${agentId}/configuration/algorithms/${id}`,
        { method: "DELETE" }
      );
      if (response.ok) {
        await loadAlgorithmConfigs();
      } else {
        // Modo demo: eliminar del estado local
        setAlgorithmConfigs(algorithmConfigs.filter(c => c.idxcoragentalgorithmconfig !== id));
      }
    } catch (error) {
      console.warn("Error deleting algorithm config (demo mode):", error);
      // Modo demo: eliminar del estado local
      setAlgorithmConfigs(algorithmConfigs.filter(c => c.idxcoragentalgorithmconfig !== id));
    }
  };

  if (!mounted) {
    return null;
  }

  return (
    <div className="space-y-4">
      <Tabs value={activeTab} onValueChange={setActiveTab} defaultValue={activeTab}>
        <TabsList>
          <TabsTrigger value="execution-order">Orden de Ejecución</TabsTrigger>
          <TabsTrigger value="processes">Procesos BPMN</TabsTrigger>
          <TabsTrigger value="rules">Reglas</TabsTrigger>
          <TabsTrigger value="algorithms">Algoritmos</TabsTrigger>
        </TabsList>

        {/* Tab: Orden de Ejecución Unificado */}
        <TabsContent value="execution-order">
          <Card>
            <CardHeader>
              <CardTitle>Orden de Ejecución Unificado</CardTitle>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="flex justify-center py-8">
                  <Loader2 className="h-6 w-6 animate-spin" />
                </div>
              ) : (
                <div className="space-y-6">
                  {/* Información */}
                  <div className="bg-blue-50 dark:bg-blue-950/20 p-4 rounded-lg border border-blue-200 dark:border-blue-800">
                    <p className="text-sm text-blue-900 dark:text-blue-100">
                      <strong>Información:</strong> Define el orden de ejecución entre procesos BPMN, reglas y algoritmos.
                      Los pasos se ejecutan en el orden mostrado (1, 2, 3...). Puedes reordenar arrastrando o usando los botones ↑↓.
                    </p>
                  </div>

                  {/* Orden de Ejecución */}
                  {executionOrder.length > 0 ? (
                    <div className="space-y-3">
                      <Label className="text-base font-semibold">
                        Pasos de Ejecución (Orden: 1, 2, 3...)
                      </Label>
                      <div className="space-y-2">
                        {executionOrder
                          .sort((a, b) => a.executionOrder - b.executionOrder)
                          .map((step, index) => {
                            const uniqueKey = `${step.executionType}-${step.configId}`;
                            return (
                            <div
                              key={uniqueKey}
                              draggable
                              onDragStart={() => handleDragStart(index)}
                              onDragOver={(e) => handleDragOver(e, index)}
                              onDragEnd={handleDragEnd}
                              className={`flex items-center gap-3 p-3 rounded-lg border bg-primary/5 border-primary/30 cursor-move transition-all ${
                                draggedIndex === index ? "opacity-50 scale-95" : "hover:bg-primary/10"
                              }`}
                            >
                              <GripVertical className="w-4 h-4 text-muted-foreground cursor-grab active:cursor-grabbing" />
                              <div className="flex-shrink-0">
                                <span className="text-sm font-medium text-muted-foreground">
                                  {step.executionOrder}
                                </span>
                              </div>
                              <div className="flex-1">
                                <div className="flex items-center gap-2">
                                  {getExecutionStepIcon(step)}
                                  <Label className="text-sm font-medium">{getExecutionStepLabel(step)}</Label>
                                  <Badge variant={
                                    step.executionType === "PROCESS" ? "primary" :
                                    step.executionType === "RULE" ? "secondary" : "outline"
                                  }>
                                    {getExecutionStepTypeLabel(step)}
                                  </Badge>
                                </div>
                              </div>
                              <div className="flex gap-1">
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0"
                                  onClick={() => handleMoveExecutionStep(index, "up")}
                                  disabled={index === 0}
                                  title="Mover arriba"
                                >
                                  <ArrowUp className="w-4 h-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0"
                                  onClick={() => handleMoveExecutionStep(index, "down")}
                                  disabled={index === executionOrder.length - 1}
                                  title="Mover abajo"
                                >
                                  <ArrowDown className="w-4 h-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0 text-destructive"
                                  onClick={() => handleRemoveExecutionStep(index)}
                                  title="Eliminar"
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </div>
                            </div>
                            );
                          })}
                      </div>
                    </div>
                  ) : (
                    <p className="text-muted-foreground text-center py-8">
                      No hay pasos de ejecución configurados. Configure procesos, reglas o algoritmos primero.
                    </p>
                  )}

                  {/* Botón Guardar */}
                  <div className="flex justify-end pt-4 border-t">
                    <Button onClick={handleSaveExecutionOrder} disabled={saving || executionOrder.length === 0}>
                      {saving ? (
                        <>
                          <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                          Guardando...
                        </>
                      ) : (
                        <>
                          <Save className="w-4 h-4 mr-2" />
                          Guardar Orden de Ejecución
                        </>
                      )}
                    </Button>
                  </div>
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Procesos BPMN */}
        <TabsContent value="processes">
          <Card>
            <CardHeader>
              <div className="flex justify-between items-center">
                <CardTitle>Configuración de Procesos BPMN</CardTitle>
                <Button
                  onClick={() => {
                    setEditingProcess({
                      agentId: parseInt(agentId),
                      processType: "APPROVAL",
                      processKey: "",
                      priority: 10,
                      isActive: true,
                    });
                    setShowProcessModal(true);
                  }}
                >
                  <Plus className="h-4 w-4 mr-2" />
                  Agregar Proceso
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="flex justify-center py-8">
                  <Loader2 className="h-6 w-6 animate-spin" />
                </div>
              ) : processConfigs.length === 0 ? (
                <p className="text-muted-foreground text-center py-8">
                  No hay configuraciones de procesos. Agregue una para comenzar.
                </p>
              ) : (
                <div className="space-y-4">
                  {processConfigs.map((config) => (
                    <Card key={config.idxcoragentprocessconfig}>
                      <CardBody>
                        <div className="flex justify-between items-start">
                          <div className="space-y-2">
                            <div className="flex items-center gap-2">
                              <Label className="font-semibold">{config.processType}</Label>
                              <Badge variant={config.isActive ? "primary" : "secondary"}>
                                {config.isActive ? "Activo" : "Inactivo"}
                              </Badge>
                            </div>
                            <p className="text-sm text-muted-foreground">
                              Proceso: {config.processKey}
                            </p>
                            <p className="text-sm text-muted-foreground">
                              Prioridad: {config.priority}
                            </p>
                          </div>
                          <div className="flex gap-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => {
                                setEditingProcess(config);
                                setShowProcessModal(true);
                              }}
                            >
                              <Edit className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => handleDeleteProcess(config.idxcoragentprocessconfig!)}
                            >
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          </div>
                        </div>
                      </CardBody>
                    </Card>
                  ))}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Reglas */}
        <TabsContent value="rules">
          <Card>
            <CardHeader>
              <CardTitle>Configuración de Reglas de Revisión</CardTitle>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="flex justify-center py-8">
                  <Loader2 className="h-6 w-6 animate-spin" />
                </div>
              ) : (
                <div className="space-y-6">
                  {/* Reglas Seleccionadas con Orden de Ejecución */}
                  {ruleConfigs.length > 0 && (
                    <div className="space-y-3 border-b pb-4">
                      <Label className="text-base font-semibold">
                        Reglas Seleccionadas (Orden de Ejecución)
                      </Label>
                      <p className="text-xs text-muted-foreground">
                        Arrastra las reglas para reordenarlas o usa los botones ↑↓
                      </p>
                      <div className="space-y-2">
                        {ruleConfigs
                          .sort((a, b) => a.executionOrder - b.executionOrder)
                          .map((config, index) => {
                            const uniqueKey = `${config.ruleType}-${config.ruleId}`;
                            return (
                            <div
                              key={uniqueKey}
                              draggable
                              onDragStart={() => handleDragStartRule(index)}
                              onDragOver={(e) => handleDragOverRule(e, index)}
                              onDragEnd={handleDragEndRule}
                              className={`flex items-center gap-3 p-3 rounded-lg border bg-primary/5 border-primary/30 cursor-move transition-all ${
                                draggedRuleIndex === index ? "opacity-50 scale-95" : "hover:bg-primary/10"
                              }`}
                            >
                              <GripVertical className="w-4 h-4 text-muted-foreground cursor-grab active:cursor-grabbing" />
                              <div className="flex-shrink-0">
                                <span className="text-sm font-medium text-muted-foreground">
                                  {config.executionOrder}
                                </span>
                              </div>
                              <div className="flex-1">
                                <div className="flex items-center gap-2">
                                  {config.ruleType === "DROOLS" ? (
                                    <FileCode className="w-4 h-4 text-primary" />
                                  ) : (
                                    <Brain className="w-4 h-4 text-purple-500" />
                                  )}
                                  <Label className="text-sm font-medium">{config.ruleId}</Label>
                                  <Badge variant={config.ruleType === "DROOLS" ? "primary" : "secondary"}>
                                    {config.ruleType}
                                  </Badge>
                                </div>
                              </div>
                              <div className="flex gap-1">
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0"
                                  onClick={() => handleMoveRule(index, "up")}
                                  disabled={index === 0}
                                  title="Mover arriba"
                                >
                                  <ArrowUp className="w-4 h-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0"
                                  onClick={() => handleMoveRule(index, "down")}
                                  disabled={index === ruleConfigs.length - 1}
                                  title="Mover abajo"
                                >
                                  <ArrowDown className="w-4 h-4" />
                                </Button>
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-8 w-8 p-0 text-destructive"
                                  onClick={() => handleRemoveRule(index)}
                                  title="Eliminar"
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </div>
                            </div>
                            );
                          })}
                      </div>
                    </div>
                  )}

                  {/* Información */}
                  <div className="bg-blue-50 dark:bg-blue-950/20 p-4 rounded-lg border border-blue-200 dark:border-blue-800">
                    <p className="text-sm text-blue-900 dark:text-blue-100">
                      <strong>Información:</strong> Seleccione reglas Drools o LLM que determinan cuándo las decisiones requieren revisión HITL.
                      Las reglas se ejecutan en orden de ejecución (1, 2, 3...).
                    </p>
                  </div>

                  {/* Lista de Reglas Disponibles */}
                  <div className="space-y-4">
                    <Label className="text-base font-semibold">Reglas Disponibles</Label>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      {/* Drools Rules */}
                      <div className="space-y-2">
                        <Label className="text-sm font-medium">Reglas Drools</Label>
                        <div className="space-y-2 max-h-[400px] overflow-y-auto">
                          {[
                            { id: "low-confidence-rule", name: "Baja Confianza", description: "Revisa decisiones con confianza < 60%" },
                            { id: "high-value-rule", name: "Alto Valor", description: "Revisa decisiones con valor > €10,000" },
                            { id: "decision-review", name: "Revisión de Decisión", description: "Determina cuándo una decisión requiere revisión HITL" },
                          ].map((droolsRule) => {
                            const isSelected = ruleConfigs.some(r => r.ruleId === droolsRule.id && r.ruleType === "DROOLS");
                            return (
                              <div
                                key={droolsRule.id}
                                className={`flex items-start gap-3 p-3 rounded-lg border cursor-pointer transition-colors ${
                                  isSelected
                                    ? "bg-primary/10 border-primary/50"
                                    : "bg-muted/30 border-border hover:bg-muted/50"
                                }`}
                                onClick={() => handleToggleRule(droolsRule.id, "DROOLS")}
                              >
                                <div className="flex-shrink-0 mt-1">
                                  {isSelected ? (
                                    <span className="text-primary">✓</span>
                                  ) : (
                                    <div className="w-4 h-4 rounded-full border-2 border-muted-foreground" />
                                  )}
                                </div>
                                <div className="flex-1">
                                  <Label className="text-sm font-medium">{droolsRule.name}</Label>
                                  <p className="text-xs text-muted-foreground mt-1">{droolsRule.description}</p>
                                </div>
                              </div>
                            );
                          })}
                        </div>
                      </div>

                      {/* LLM Prompts */}
                      <div className="space-y-2">
                        <Label className="text-sm font-medium">Prompts LLM</Label>
                        <div className="space-y-2 max-h-[400px] overflow-y-auto">
                          {[
                            { id: "ethics-review-prompt", name: "Revisión Ética", description: "Evalúa aspectos éticos de la decisión" },
                            { id: "compliance-check-prompt", name: "Verificación de Cumplimiento", description: "Verifica cumplimiento con regulaciones" },
                          ].map((prompt) => {
                            const isSelected = ruleConfigs.some(r => r.ruleId === prompt.id && r.ruleType === "LLM_PROMPT");
                            return (
                              <div
                                key={prompt.id}
                                className={`flex items-start gap-3 p-3 rounded-lg border cursor-pointer transition-colors ${
                                  isSelected
                                    ? "bg-purple-500/10 border-purple-500/50"
                                    : "bg-muted/30 border-border hover:bg-muted/50"
                                }`}
                                onClick={() => handleToggleRule(prompt.id, "LLM_PROMPT")}
                              >
                                <div className="flex-shrink-0 mt-1">
                                  {isSelected ? (
                                    <span className="text-purple-500">✓</span>
                                  ) : (
                                    <div className="w-4 h-4 rounded-full border-2 border-muted-foreground" />
                                  )}
                                </div>
                                <div className="flex-1">
                                  <Label className="text-sm font-medium">{prompt.name}</Label>
                                  <p className="text-xs text-muted-foreground mt-1">{prompt.description}</p>
                                </div>
                              </div>
                            );
                          })}
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Botón Guardar */}
                  <div className="flex justify-end pt-4 border-t">
                    <Button onClick={handleSaveRules} disabled={saving}>
                      {saving ? (
                        <>
                          <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                          Guardando...
                        </>
                      ) : (
                        <>
                          <Save className="w-4 h-4 mr-2" />
                          Guardar Reglas
                        </>
                      )}
                    </Button>
                  </div>
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Algoritmos */}
        <TabsContent value="algorithms">
          <Card>
            <CardHeader>
              <div className="flex justify-between items-center">
                <CardTitle>Configuración de Algoritmos</CardTitle>
                <Button
                  onClick={() => {
                    setEditingAlgorithm({
                      agentId: parseInt(agentId),
                      service: "bias-detection",
                      algorithmName: "",
                      priority: 10,
                      isActive: true,
                    });
                    setShowAlgorithmModal(true);
                  }}
                >
                  <Plus className="h-4 w-4 mr-2" />
                  Agregar Algoritmo
                </Button>
              </div>
            </CardHeader>
            <CardBody>
              {loading ? (
                <div className="flex justify-center py-8">
                  <Loader2 className="h-6 w-6 animate-spin" />
                </div>
              ) : algorithmConfigs.length === 0 ? (
                <p className="text-muted-foreground text-center py-8">
                  No hay configuraciones de algoritmos. Agregue una para comenzar.
                </p>
              ) : (
                <div className="space-y-4">
                  {algorithmConfigs.map((config) => (
                    <Card key={config.idxcoragentalgorithmconfig}>
                      <CardBody>
                        <div className="flex justify-between items-start">
                          <div className="space-y-2">
                            <div className="flex items-center gap-2">
                              <Label className="font-semibold">{config.service}</Label>
                              <Badge variant={config.isActive ? "primary" : "secondary"}>
                                {config.isActive ? "Activo" : "Inactivo"}
                              </Badge>
                            </div>
                            <p className="text-sm text-muted-foreground">
                              Algoritmo: {config.algorithmName}
                            </p>
                            {config.context && (
                              <p className="text-sm text-muted-foreground">
                                Contexto: {config.context}
                              </p>
                            )}
                            <p className="text-sm text-muted-foreground">
                              Prioridad: {config.priority}
                            </p>
                          </div>
                          <div className="flex gap-2">
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => {
                                setEditingAlgorithm(config);
                                setShowAlgorithmModal(true);
                              }}
                            >
                              <Edit className="h-4 w-4" />
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => handleDeleteAlgorithm(config.idxcoragentalgorithmconfig!)}
                            >
                              <Trash2 className="h-4 w-4" />
                            </Button>
                          </div>
                        </div>
                      </CardBody>
                    </Card>
                  ))}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Modal Proceso */}
      {showProcessModal && editingProcess && (
        <ProcessConfigModal
          config={editingProcess}
          onSave={handleSaveProcess}
          onClose={() => {
            setShowProcessModal(false);
            setEditingProcess(null);
          }}
          saving={saving}
        />
      )}


      {/* Modal Algoritmo */}
      {showAlgorithmModal && editingAlgorithm && (
        <AlgorithmConfigModal
          config={editingAlgorithm}
          onSave={handleSaveAlgorithm}
          onClose={() => {
            setShowAlgorithmModal(false);
            setEditingAlgorithm(null);
          }}
          saving={saving}
        />
      )}
    </div>
  );
}

// Modal para Proceso
function ProcessConfigModal({
  config,
  onSave,
  onClose,
  saving,
}: {
  config: AgentProcessConfig;
  onSave: (config: AgentProcessConfig) => void;
  onClose: () => void;
  saving: boolean;
}) {
  const [formData, setFormData] = useState<AgentProcessConfig>(config);

  useEffect(() => {
    setFormData(config);
  }, [config]);

  return (
    <SimpleModal
      isOpen={true}
      onClose={onClose}
      title={config.idxcoragentprocessconfig ? "Editar Proceso" : "Nuevo Proceso"}
    >
      <div className="space-y-4">
        <div>
          <Label>Tipo de Proceso</Label>
          <Select
            value={formData.processType}
            onValueChange={(value) => setFormData({ ...formData, processType: value })}
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="APPROVAL">Aprobación</SelectItem>
              <SelectItem value="CERTIFICATION">Certificación</SelectItem>
              <SelectItem value="RETIREMENT">Retiro</SelectItem>
              <SelectItem value="GOVERNANCE_POLICY">Política de Gobierno</SelectItem>
              <SelectItem value="COMPLIANCE_ASSESSMENT">Evaluación de Cumplimiento</SelectItem>
              <SelectItem value="ETHICS_ASSESSMENT">Evaluación Ética</SelectItem>
              <SelectItem value="DECISION_REVIEW">Revisión de Decisión</SelectItem>
              <SelectItem value="ROLLBACK_APPROVAL">Aprobación de Reversión</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label>Clave del Proceso BPMN</Label>
          <Input
            value={formData.processKey}
            onChange={(e) => setFormData({ ...formData, processKey: e.target.value })}
            placeholder="agent-approval-v1"
          />
        </div>
        <div>
          <Label>Prioridad</Label>
          <Input
            type="number"
            value={formData.priority}
            onChange={(e) => setFormData({ ...formData, priority: parseInt(e.target.value) || 0 })}
          />
        </div>
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={formData.isActive}
            onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
          />
          <Label>Activo</Label>
        </div>
        <div className="flex justify-end gap-2">
          <Button variant="outline" onClick={onClose} disabled={saving}>
            Cancelar
          </Button>
          <Button onClick={() => onSave(formData)} disabled={saving}>
            {saving ? <Loader2 className="h-4 w-4 animate-spin" /> : <Save className="h-4 w-4 mr-2" />}
            Guardar
          </Button>
        </div>
      </div>
    </SimpleModal>
  );
}


// Modal para Algoritmo
function AlgorithmConfigModal({
  config,
  onSave,
  onClose,
  saving,
}: {
  config: AgentAlgorithmConfig;
  onSave: (config: AgentAlgorithmConfig) => void;
  onClose: () => void;
  saving: boolean;
}) {
  const [formData, setFormData] = useState<AgentAlgorithmConfig>(config);

  useEffect(() => {
    setFormData(config);
  }, [config]);

  return (
    <SimpleModal
      isOpen={true}
      onClose={onClose}
      title={config.idxcoragentalgorithmconfig ? "Editar Algoritmo" : "Nuevo Algoritmo"}
    >
      <div className="space-y-4">
        <div>
          <Label>Servicio</Label>
          <Select
            value={formData.service}
            onValueChange={(value) => setFormData({ ...formData, service: value })}
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="bias-detection">Detección de Sesgos</SelectItem>
              <SelectItem value="llm-evaluation">Evaluación LLM</SelectItem>
              <SelectItem value="agent-monitoring">Monitoreo de Agentes</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label>Nombre del Algoritmo</Label>
          <Input
            value={formData.algorithmName}
            onChange={(e) => setFormData({ ...formData, algorithmName: e.target.value })}
            placeholder="statistical_parity"
          />
        </div>
        <div>
          <Label>Contexto</Label>
          <Input
            value={formData.context || ""}
            onChange={(e) => setFormData({ ...formData, context: e.target.value })}
            placeholder="agent-evaluation"
          />
        </div>
        <div>
          <Label>Prioridad</Label>
          <Input
            type="number"
            value={formData.priority}
            onChange={(e) => setFormData({ ...formData, priority: parseInt(e.target.value) || 0 })}
          />
        </div>
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={formData.isActive}
            onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
          />
          <Label>Activo</Label>
        </div>
        <div className="flex justify-end gap-2">
          <Button variant="outline" onClick={onClose} disabled={saving}>
            Cancelar
          </Button>
          <Button onClick={() => onSave(formData)} disabled={saving}>
            {saving ? <Loader2 className="h-4 w-4 animate-spin" /> : <Save className="h-4 w-4 mr-2" />}
            Guardar
          </Button>
        </div>
      </div>
    </SimpleModal>
  );
}
