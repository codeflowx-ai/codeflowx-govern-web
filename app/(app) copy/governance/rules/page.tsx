"use client";

import { useState, useEffect } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { Plus, Edit, Trash2, Search, Code, FileText, CheckCircle2, XCircle, Loader2 } from "lucide-react";
import { useToast } from "@/components/ui/toast";
import { RuleEditor } from "@/components/rules/rule-editor";

interface BusinessRule {
  id: number;
  uuid: string;
  name: string;
  description?: string;
  type: "VALIDATION" | "DROOLS" | "JSON" | "YAML" | "SCRIPT";
  category: "CLASSIFICATION" | "VALIDATION" | "COMPLIANCE" | "QUALITY" | "SECURITY" | "CUSTOM";
  active: boolean;
  content: string;
  configuration?: Record<string, any>;
  version?: string;
  author?: string;
  priority: number;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}

export default function RulesManagementPage() {
  const { t } = useTranslation();
  const { toast } = useToast();
  const [rules, setRules] = useState<BusinessRule[]>([]);
  const [filteredRules, setFilteredRules] = useState<BusinessRule[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("all");
  const [categoryFilter, setCategoryFilter] = useState<string>("all");
  const [activeFilter, setActiveFilter] = useState<string>("all");
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingRule, setEditingRule] = useState<BusinessRule | null>(null);
  const [formData, setFormData] = useState<Partial<BusinessRule>>({
    name: "",
    description: "",
    type: "VALIDATION",
    category: "CLASSIFICATION",
    active: true,
    content: "",
    priority: 0,
  });
  const [isContentValid, setIsContentValid] = useState(true);
  const [validationErrors, setValidationErrors] = useState<string[]>([]);

  // Cargar reglas
  useEffect(() => {
    loadRules();
  }, []);

  // Filtrar reglas
  useEffect(() => {
    let filtered = [...rules];

    if (searchTerm) {
      filtered = filtered.filter(
        (rule) =>
          rule.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          rule.description?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (typeFilter !== "all") {
      filtered = filtered.filter((rule) => rule.type === typeFilter);
    }

    if (categoryFilter !== "all") {
      filtered = filtered.filter((rule) => rule.category === categoryFilter);
    }

    if (activeFilter !== "all") {
      const isActive = activeFilter === "true";
      filtered = filtered.filter((rule) => rule.active === isActive);
    }

    setFilteredRules(filtered);
  }, [rules, searchTerm, typeFilter, categoryFilter, activeFilter]);

  const loadRules = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/governance/rules");
      if (response.ok) {
        const data = await response.json();
        setRules(data.data || []);
      } else {
        toast({
          title: "Error",
          description: "No se pudieron cargar las reglas",
          variant: "error",
        });
      }
    } catch (error) {
      console.error("Error loading rules:", error);
      toast({
        title: "Error",
        description: "Error al cargar las reglas",
        variant: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setEditingRule(null);
    setFormData({
      name: "",
      description: "",
      type: "VALIDATION",
      category: "CLASSIFICATION",
      active: true,
      content: "",
      priority: 0,
    });
    setIsDialogOpen(true);
  };

  const handleEdit = (rule: BusinessRule) => {
    setEditingRule(rule);
    setFormData({
      name: rule.name,
      description: rule.description || "",
      type: rule.type,
      category: rule.category,
      active: rule.active,
      content: rule.content,
      configuration: rule.configuration,
      version: rule.version || "",
      author: rule.author || "",
      priority: rule.priority,
    });
    setIsDialogOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (!confirm("¿Está seguro de que desea eliminar esta regla?")) {
      return;
    }

    try {
      const response = await fetch(`/api/governance/rules/${id}`, {
        method: "DELETE",
      });

      if (response.ok) {
        toast({
          title: "Éxito",
          description: "Regla eliminada correctamente",
        });
        loadRules();
      } else {
        toast({
          title: "Error",
          description: "No se pudo eliminar la regla",
          variant: "error",
        });
      }
    } catch (error) {
      console.error("Error deleting rule:", error);
      toast({
        title: "Error",
        description: "Error al eliminar la regla",
        variant: "error",
      });
    }
  };

  const handleSave = async () => {
    try {
      const url = editingRule
        ? `/api/governance/rules/${editingRule.id}`
        : "/api/governance/rules";
      const method = editingRule ? "PUT" : "POST";

      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        toast({
          title: "Éxito",
          description: editingRule ? "Regla actualizada correctamente" : "Regla creada correctamente",
        });
        setIsDialogOpen(false);
        loadRules();
      } else {
        const error = await response.json();
        toast({
          title: "Error",
          description: error.message || "Error al guardar la regla",
          variant: "error",
        });
      }
    } catch (error) {
      console.error("Error saving rule:", error);
      toast({
        title: "Error",
        description: "Error al guardar la regla",
        variant: "error",
      });
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case "DROOLS":
        return <Code className="h-4 w-4" />;
      case "JSON":
      case "YAML":
        return <FileText className="h-4 w-4" />;
      default:
        return <FileText className="h-4 w-4" />;
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case "VALIDATION":
        return "bg-blue-100 text-blue-800";
      case "DROOLS":
        return "bg-purple-100 text-purple-800";
      case "JSON":
        return "bg-green-100 text-green-800";
      case "YAML":
        return "bg-yellow-100 text-yellow-800";
      case "SCRIPT":
        return "bg-orange-100 text-orange-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="container mx-auto py-8 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold">Gestión de Reglas de Negocio</h1>
          <p className="text-muted-foreground mt-2">
            Administra y configura reglas de validación, Drools y otras reglas de negocio
          </p>
        </div>
        <Button onClick={handleCreate} className="gap-2">
          <Plus className="h-4 w-4" />
          Nueva Regla
        </Button>
      </div>

      {/* Filtros */}
      <Card>
        <CardHeader>
          <CardTitle>Filtros</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="space-y-2">
              <Label>Buscar</Label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Buscar por nombre o descripción..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Tipo</Label>
              <Select value={typeFilter} onValueChange={setTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="Todos los tipos" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos</SelectItem>
                  <SelectItem value="VALIDATION">Validación</SelectItem>
                  <SelectItem value="DROOLS">Drools</SelectItem>
                  <SelectItem value="JSON">JSON</SelectItem>
                  <SelectItem value="YAML">YAML</SelectItem>
                  <SelectItem value="SCRIPT">Script</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Categoría</Label>
              <Select value={categoryFilter} onValueChange={setCategoryFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="Todas las categorías" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todas</SelectItem>
                  <SelectItem value="CLASSIFICATION">Clasificación</SelectItem>
                  <SelectItem value="VALIDATION">Validación</SelectItem>
                  <SelectItem value="COMPLIANCE">Cumplimiento</SelectItem>
                  <SelectItem value="QUALITY">Calidad</SelectItem>
                  <SelectItem value="SECURITY">Seguridad</SelectItem>
                  <SelectItem value="CUSTOM">Personalizada</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Estado</Label>
              <Select value={activeFilter} onValueChange={setActiveFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="Todos los estados" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Todos</SelectItem>
                  <SelectItem value="true">Activas</SelectItem>
                  <SelectItem value="false">Inactivas</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Lista de Reglas */}
      {loading ? (
        <div className="flex justify-center items-center py-12">
          <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
        </div>
      ) : filteredRules.length === 0 ? (
        <Alert>
          <AlertDescription>
            No se encontraron reglas. Crea una nueva regla para comenzar.
          </AlertDescription>
        </Alert>
      ) : (
        <div className="grid grid-cols-1 gap-4">
          {filteredRules.map((rule) => (
            <Card key={rule.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      <CardTitle className="text-xl">{rule.name}</CardTitle>
                      {rule.active ? (
                        <Badge variant="primary" className="gap-1">
                          <CheckCircle2 className="h-3 w-3" />
                          Activa
                        </Badge>
                      ) : (
                        <Badge variant="secondary" className="gap-1">
                          <XCircle className="h-3 w-3" />
                          Inactiva
                        </Badge>
                      )}
                    </div>
                    {rule.description && (
                      <CardDescription>{rule.description}</CardDescription>
                    )}
                  </div>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleEdit(rule)}
                    >
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleDelete(rule.id)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="flex flex-wrap gap-2 mb-4">
                  <Badge className={getTypeColor(rule.type)}>
                    {getTypeIcon(rule.type)}
                    <span className="ml-1">{rule.type}</span>
                  </Badge>
                  <Badge variant="outline">{rule.category}</Badge>
                  {rule.priority > 0 && (
                    <Badge variant="outline">Prioridad: {rule.priority}</Badge>
                  )}
                  {rule.version && (
                    <Badge variant="outline">v{rule.version}</Badge>
                  )}
                </div>
                <div className="text-sm text-muted-foreground">
                  <p>Contenido: {rule.content.substring(0, 100)}...</p>
                  <p className="mt-1">
                    Actualizado: {new Date(rule.updatedAt).toLocaleDateString()}
                    {rule.updatedBy && ` por ${rule.updatedBy}`}
                  </p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Dialog para Crear/Editar */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>
              {editingRule ? "Editar Regla" : "Nueva Regla"}
            </DialogTitle>
            <DialogDescription>
              {editingRule
                ? "Modifica los datos de la regla de negocio"
                : "Crea una nueva regla de negocio"}
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>Nombre *</Label>
                <Input
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                  placeholder="Nombre de la regla"
                />
              </div>
              <div className="space-y-2">
                <Label>Versión</Label>
                <Input
                  value={formData.version || ""}
                  onChange={(e) =>
                    setFormData({ ...formData, version: e.target.value })
                  }
                  placeholder="1.0.0"
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Descripción</Label>
              <Textarea
                value={formData.description || ""}
                onChange={(e) =>
                  setFormData({ ...formData, description: e.target.value })
                }
                placeholder="Descripción de la regla"
                rows={2}
              />
            </div>
            <div className="grid grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label>Tipo *</Label>
                <Select
                  value={formData.type}
                  onValueChange={(value) =>
                    setFormData({ ...formData, type: value as any })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="VALIDATION">Validación</SelectItem>
                    <SelectItem value="DROOLS">Drools</SelectItem>
                    <SelectItem value="JSON">JSON</SelectItem>
                    <SelectItem value="YAML">YAML</SelectItem>
                    <SelectItem value="SCRIPT">Script</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Categoría *</Label>
                <Select
                  value={formData.category}
                  onValueChange={(value) =>
                    setFormData({ ...formData, category: value as any })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="CLASSIFICATION">Clasificación</SelectItem>
                    <SelectItem value="VALIDATION">Validación</SelectItem>
                    <SelectItem value="COMPLIANCE">Cumplimiento</SelectItem>
                    <SelectItem value="QUALITY">Calidad</SelectItem>
                    <SelectItem value="SECURITY">Seguridad</SelectItem>
                    <SelectItem value="CUSTOM">Personalizada</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label>Prioridad</Label>
                <Input
                  type="number"
                  value={formData.priority || 0}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      priority: parseInt(e.target.value) || 0,
                    })
                  }
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label>Contenido *</Label>
              <RuleEditor
                content={formData.content || ""}
                type={formData.type || "VALIDATION"}
                onChange={(value) =>
                  setFormData({ ...formData, content: value })
                }
                onValidationChange={(isValid, errors) => {
                  setIsContentValid(isValid);
                  setValidationErrors(errors);
                }}
              />
            </div>
            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                id="active"
                checked={formData.active}
                onChange={(e) =>
                  setFormData({ ...formData, active: e.target.checked })
                }
                className="rounded"
              />
              <Label htmlFor="active">Regla activa</Label>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
              Cancelar
            </Button>
            <Button
              onClick={handleSave}
              disabled={!isContentValid || !formData.name || !formData.content}
            >
              Guardar
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
