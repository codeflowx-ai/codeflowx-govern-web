"use client";

import { useTranslation } from "@/app/config/i18n";
import { useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import { FileText, Plus, Edit, Trash2, Eye, RefreshCw, Search, Filter } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
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

interface Prompt {
  id: number;
  uuid?: string;
  name: string;
  description: string;
  version: string;
  status: string;
  projectId?: number;
  projectName?: string;
  createdAt: string;
  updatedAt: string;
  isactive: boolean;
  content?: string;
  parameters?: string;
}

export default function PromptsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [promptToDelete, setPromptToDelete] = useState<Prompt | null>(null);

  // Mock data basado en versiones para unificar la demo
  const mockData: Prompt[] = [
    {
      id: 1,
      name: "Customer Support Prompt",
      description: "Versión inicial",
      version: "1.0.0",
      status: "ACTIVE",
      projectName: "Customer Service AI",
      createdAt: "2024-01-15T10:00:00Z",
      updatedAt: "2024-01-15T10:00:00Z",
      isactive: true,
      content: "Contenido del prompt v1.0.0",
      parameters: "temperature=0.7, max_tokens=1000",
    },
    {
      id: 2,
      name: "Content Generation Prompt",
      description: "Mejoras en claridad",
      version: "1.1.0",
      status: "ACTIVE",
      projectName: "Content AI",
      createdAt: "2024-01-20T14:30:00Z",
      updatedAt: "2024-01-20T14:30:00Z",
      isactive: true,
      content: "Contenido del prompt v1.1.0",
      parameters: "temperature=0.7, max_tokens=1200",
    },
    {
      id: 3,
      name: "Data Analysis Prompt",
      description: "Optimización de tokens",
      version: "1.2.0",
      status: "DRAFT",
      projectName: "Analytics AI",
      createdAt: "2024-02-01T09:15:00Z",
      updatedAt: "2024-02-01T09:15:00Z",
      isactive: true,
      content: "Contenido del prompt v1.2.0",
      parameters: "temperature=0.8, max_tokens=1500",
    },
    {
      id: 4,
      name: "Translation Prompt",
      description: "Refactorización completa",
      version: "2.0.0",
      status: "PENDING",
      projectName: "Translation AI",
      createdAt: "2024-02-10T11:45:00Z",
      updatedAt: "2024-02-10T11:45:00Z",
      isactive: true,
      content: "Contenido del prompt v2.0.0",
      parameters: "temperature=0.75, max_tokens=2000",
    },
    {
      id: 5,
      name: "Code Generation Prompt",
      description: "Corrección de bugs",
      version: "2.1.0",
      status: "INACTIVE",
      projectName: "Dev Tools AI",
      createdAt: "2024-02-15T16:20:00Z",
      updatedAt: "2024-02-15T16:20:00Z",
      isactive: false,
      content: "Contenido del prompt v2.1.0",
      parameters: "temperature=0.75, max_tokens=2000",
    },
  ];

  const filteredPrompts = useMemo(() => {
    let filtered = mockData;

    // Filtrar por búsqueda
    if (searchTerm) {
      filtered = filtered.filter(
        (p) =>
          p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.version.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Filtrar por estado
    if (statusFilter !== "all") {
      filtered = filtered.filter((p) => p.status === statusFilter);
    }

    return filtered;
  }, [searchTerm, statusFilter]);

  const handleDelete = () => {
    if (!promptToDelete) return;
    // TODO: Implementar eliminación real cuando esté disponible
    console.log("Delete prompt:", promptToDelete.id);
    setDeleteDialogOpen(false);
    setPromptToDelete(null);
  };

  const handleToggleActive = async (prompt: Prompt) => {
    try {
      const newActiveState = !prompt.isactive;

      // Llamar a la API para actualizar el estado
      const response = await fetch(`/api/governance/prompts/${prompt.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          isactive: newActiveState,
        }),
      });

      if (response.ok) {
        // Actualizar el estado local
        const updatedPrompts = mockData.map(p =>
          p.id === prompt.id ? { ...p, isactive: newActiveState } : p
        );
        // Recargar la página para reflejar los cambios
        window.location.reload();
      } else {
        alert(t("governance.prompts.toggleError", "Error al cambiar el estado del prompt"));
      }
    } catch (error) {
      console.error("Error toggling active state:", error);
      alert(t("governance.prompts.toggleError", "Error al cambiar el estado del prompt"));
    }
  };

  const getStatusBadge = (status: string, isactive: boolean) => {
    if (!isactive) {
      return (
        <Badge className="bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300">
          {t("governance.prompts.status.inactive", "Inactivo")}
        </Badge>
      );
    }

    const statusConfig: Record<string, { label: string; className: string }> = {
      ACTIVE: {
        label: t("governance.prompts.status.active", "Activo"),
        className: "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300",
      },
      DRAFT: {
        label: t("governance.prompts.status.draft", "Borrador"),
        className: "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300",
      },
      PENDING: {
        label: t("governance.prompts.status.pending", "Pendiente"),
        className: "bg-blue-100 text-blue-800 dark:bg-blue-900/20 dark:text-blue-300",
      },
      INACTIVE: {
        label: t("governance.prompts.status.inactive", "Inactivo"),
        className: "bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300",
      },
    };

    const config = statusConfig[status] || statusConfig.DRAFT;
    return <Badge className={config.className}>{config.label}</Badge>;
  };


  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <FileText className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.prompts.title", "Gestión de Prompts")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.prompts.subtitle", "Gestiona y administra todos tus prompts de IA")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button onClick={() => window.location.reload()} variant="outline" size="sm">
            <RefreshCw className="h-4 w-4 mr-2" />
            {t("common.refresh", "Actualizar")}
          </Button>
          <Button onClick={() => window.location.href = "/governance/prompts/register"} size="sm">
            <Plus className="h-4 w-4 mr-2" />
            {t("governance.prompts.create", "Crear Prompt")}
          </Button>
        </div>
      </div>

      {/* Filtros */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium mb-2">
            <Search className="w-4 h-4 inline mr-1" />
            {t("governance.prompts.search", "Buscar")}
          </label>
          <Input
            type="text"
            placeholder={t("governance.prompts.searchPlaceholder", "Buscar por nombre o descripción...")}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-2">
            <Filter className="w-4 h-4 inline mr-1" />
            {t("governance.prompts.filterStatus", "Estado")}
          </label>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="w-full px-3 py-2 border rounded-md bg-background"
          >
            <option value="all">{t("common.all", "Todos")}</option>
            <option value="ACTIVE">{t("governance.prompts.status.active", "Activo")}</option>
            <option value="DRAFT">{t("governance.prompts.status.draft", "Borrador")}</option>
            <option value="PENDING">{t("governance.prompts.status.pending", "Pendiente")}</option>
            <option value="INACTIVE">{t("governance.prompts.status.inactive", "Inactivo")}</option>
          </select>
        </div>
        <div className="flex items-end">
          <Button
            variant="outline"
            onClick={() => {
              setSearchTerm("");
              setStatusFilter("all");
            }}
            className="w-full"
          >
            {t("common.clearFilters", "Limpiar Filtros")}
          </Button>
        </div>
      </div>

      {/* Tabla de Prompts */}
      <div className="border rounded-lg overflow-hidden">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b bg-muted/50">
              <th className="text-left p-3 font-medium">
                {t("governance.prompts.table.name", "Nombre")}
              </th>
              <th className="text-left p-3 font-medium">
                {t("governance.prompts.table.description", "Descripción")}
              </th>
              <th className="text-left p-3 font-medium">
                {t("governance.prompts.table.version", "Versión")}
              </th>
              <th className="text-left p-3 font-medium">
                {t("governance.prompts.table.project", "Proyecto")}
              </th>
              <th className="text-center p-3 font-medium">
                {t("governance.prompts.table.status", "Estado")}
              </th>
              <th className="text-left p-3 font-medium">
                {t("governance.prompts.table.updated", "Actualizado")}
              </th>
              <th className="text-center p-3 font-medium">
                {t("governance.prompts.table.actions", "Acciones")}
              </th>
            </tr>
          </thead>
          <tbody>
            {filteredPrompts.length === 0 ? (
              <tr>
                <td colSpan={7} className="text-center p-8 text-muted-foreground">
                  {t("governance.prompts.noResults", "No se encontraron prompts")}
                </td>
              </tr>
            ) : (
              filteredPrompts.map((prompt) => (
                <tr
                  key={prompt.id}
                  className="border-b hover:bg-muted/50 cursor-pointer"
                  onClick={(e) => {
                    e.stopPropagation();
                    router.push(`/governance/prompts/${prompt.id}`);
                  }}
                >
                  <td className="p-3 font-medium">{prompt.name}</td>
                  <td className="p-3 text-muted-foreground max-w-xs truncate">
                    {prompt.description || "-"}
                  </td>
                  <td className="p-3">{prompt.version || "1.0.0"}</td>
                  <td className="p-3">{prompt.projectName || "-"}</td>
                  <td className="p-3 text-center">
                    {getStatusBadge(prompt.status, prompt.isactive)}
                  </td>
                  <td className="p-3 text-muted-foreground">
                    {new Date(prompt.updatedAt).toLocaleDateString()}
                  </td>
                  <td className="p-3">
                    <div className="flex items-center justify-center gap-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                    router.push(`/governance/prompts/${prompt.id}`);
                  }}
                        title={t("governance.prompts.actions.view", "Ver detalle")}
                      >
                        <Eye className="w-4 h-4" />
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => window.location.href = `/governance/prompts/${prompt.id}`}
                        title={t("governance.prompts.actions.edit", "Editar")}
                      >
                        <Edit className="w-4 h-4" />
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                          handleToggleActive(prompt);
                        }}
                        title={
                          prompt.isactive
                            ? t("governance.prompts.actions.deactivate", "Desactivar")
                            : t("governance.prompts.actions.activate", "Activar")
                        }
                        className={prompt.isactive ? "text-yellow-600" : "text-green-600"}
                      >
                        {prompt.isactive ? "⏸" : "▶"}
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => {
                          setPromptToDelete(prompt);
                          setDeleteDialogOpen(true);
                        }}
                        title={t("governance.prompts.actions.delete", "Eliminar")}
                        className="text-red-600 hover:text-red-700"
                      >
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Diálogo de confirmación de eliminación */}
      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>
              {t("governance.prompts.delete.title", "¿Eliminar prompt?")}
            </AlertDialogTitle>
            <AlertDialogDescription>
              {t(
                "governance.prompts.delete.description",
                "Esta acción no se puede deshacer. El prompt será eliminado permanentemente."
              )}
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>
              {t("common.cancel", "Cancelar")}
            </AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete} className="bg-red-600 hover:bg-red-700">
              {t("common.delete", "Eliminar")}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
