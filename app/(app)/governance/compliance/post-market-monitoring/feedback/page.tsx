"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  MessageSquare,
  Plus,
  Edit,
  Trash2,
  X,
  CheckCircle,
  Clock,
  Archive,
  Smile,
  Meh,
  Frown,
  Star,
  ArrowLeft,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { mockProjects } from "@/app/(app)/governance/data/mockPMM";

interface UserFeedback {
  idxuserfeedback: number;
  iduuid: string;
  idxproject: number;
  projectName?: string;
  idxmodel?: number;
  usrfeedbacktext: string;
  usrfeedbackrating?: number;
  usrfeedbacksentiment?: string;
  usrfeedbacksentimentscore?: number;
  usrfeedbackcategory?: string;
  usrfeedbackuserid?: string;
  usrfeedbackuseremail?: string;
  usrfeedbackstatus: string;
  usrfeedbackresponse?: string;
  usrfeedbackrespondedat?: string;
  usrfeedbackcreatedat: string;
  usrfeedbackupdatedat?: string;
}

const USE_MOCK_DATA = process.env.NEXT_PUBLIC_USE_MOCK_DATA === "true";

// Mock data para feedback
const mockFeedback: UserFeedback[] = [
  {
    idxuserfeedback: 1,
    iduuid: "uuid-1",
    idxproject: 1,
    projectName: "AI Credit Scoring System",
    usrfeedbacktext: "El sistema funciona muy bien, las decisiones son rápidas y precisas.",
    usrfeedbackrating: 4.5,
    usrfeedbacksentiment: "POSITIVE",
    usrfeedbacksentimentscore: 0.75,
    usrfeedbackstatus: "REVIEWED",
    usrfeedbackcreatedat: "2025-11-15T10:00:00Z",
  },
  {
    idxuserfeedback: 2,
    iduuid: "uuid-2",
    idxproject: 1,
    projectName: "AI Credit Scoring System",
    usrfeedbacktext: "He notado algunos problemas con la precisión en casos límite.",
    usrfeedbackrating: 3.0,
    usrfeedbacksentiment: "NEGATIVE",
    usrfeedbacksentimentscore: -0.35,
    usrfeedbackstatus: "PENDING",
    usrfeedbackcreatedat: "2025-11-20T14:30:00Z",
  },
  {
    idxuserfeedback: 3,
    iduuid: "uuid-3",
    idxproject: 2,
    projectName: "Facial Recognition System",
    usrfeedbacktext: "Sistema aceptable, pero podría mejorar la velocidad de respuesta.",
    usrfeedbackrating: 3.5,
    usrfeedbacksentiment: "NEUTRAL",
    usrfeedbacksentimentscore: 0.05,
    usrfeedbackstatus: "RESPONDED",
    usrfeedbackresponse: "Gracias por su feedback. Estamos trabajando en optimizaciones de rendimiento.",
    usrfeedbackrespondedat: "2025-11-22T09:00:00Z",
    usrfeedbackcreatedat: "2025-11-18T11:00:00Z",
  },
  {
    idxuserfeedback: 4,
    iduuid: "uuid-4",
    idxproject: 3,
    projectName: "Healthcare Diagnostics AI System",
    usrfeedbacktext: "Excelente herramienta, muy útil para el diagnóstico médico.",
    usrfeedbackrating: 5.0,
    usrfeedbacksentiment: "POSITIVE",
    usrfeedbacksentimentscore: 0.90,
    usrfeedbackstatus: "REVIEWED",
    usrfeedbackcreatedat: "2025-11-25T16:00:00Z",
  },
];

export default function PMMFeedbackPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const projectIdFromUrl = searchParams.get("projectId");

  // TODO: Obtener del token JWT o contexto de autenticación
  const currentUser = {
    id: "current-user-id", // TODO: Obtener del token
    email: "user@example.com", // TODO: Obtener del token
  };

  const [feedback, setFeedback] = useState<UserFeedback[]>([]);
  const [availableProjects, setAvailableProjects] = useState<Array<{ id: number; name: string }>>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isResponseDialogOpen, setIsResponseDialogOpen] = useState(false);
  const [selectedFeedback, setSelectedFeedback] = useState<UserFeedback | null>(null);
  const [projectFilter, setProjectFilter] = useState<string>(projectIdFromUrl || "");
  const [statusFilter, setStatusFilter] = useState<string>("");
  const [sentimentFilter, setSentimentFilter] = useState<string>("");
  const [responseText, setResponseText] = useState("");

  const [newFeedback, setNewFeedback] = useState<Partial<UserFeedback>>({
    idxproject: projectIdFromUrl ? parseInt(projectIdFromUrl) : 0,
    usrfeedbacktext: "",
    usrfeedbackrating: undefined,
    usrfeedbackcategory: "",
    usrfeedbackuserid: currentUser.id,
    usrfeedbackuseremail: currentUser.email,
    usrfeedbackstatus: "PENDING",
  });

  const feedbackCategories = [
    { value: "PERFORMANCE", label: t("governance.compliance.pmm.feedback.categories.PERFORMANCE", "Rendimiento") },
    { value: "ACCURACY", label: t("governance.compliance.pmm.feedback.categories.ACCURACY", "Precisión") },
    { value: "USABILITY", label: t("governance.compliance.pmm.feedback.categories.USABILITY", "Usabilidad") },
    { value: "BIAS", label: t("governance.compliance.pmm.feedback.categories.BIAS", "Sesgo") },
    { value: "PRIVACY", label: t("governance.compliance.pmm.feedback.categories.PRIVACY", "Privacidad") },
    { value: "SECURITY", label: t("governance.compliance.pmm.feedback.categories.SECURITY", "Seguridad") },
    { value: "OTHER", label: t("governance.compliance.pmm.feedback.categories.OTHER", "Otro") },
  ];

  useEffect(() => {
    loadProjects();
    loadFeedback();
  }, []);

  useEffect(() => {
    if (projectIdFromUrl) {
      setProjectFilter(projectIdFromUrl);
      setNewFeedback((prev) => ({
        ...prev,
        idxproject: parseInt(projectIdFromUrl),
      }));
    }
  }, [projectIdFromUrl]);

  useEffect(() => {
    loadFeedback();
  }, [projectFilter, statusFilter, sentimentFilter]);

  const loadProjects = async () => {
    try {
      if (USE_MOCK_DATA) {
        setAvailableProjects(mockProjects);
      } else {
        const response = await fetch("/api/v1/projects");
        if (response.ok) {
          const data = await response.json();
          setAvailableProjects(data);
        }
      }
    } catch (error) {
      console.error("Error loading projects:", error);
    }
  };

  const loadFeedback = async () => {
    try {
      setLoading(true);
      if (USE_MOCK_DATA) {
        let filtered = [...mockFeedback];
        if (projectFilter) {
          filtered = filtered.filter((f) => f.idxproject.toString() === projectFilter);
        }
        if (statusFilter) {
          filtered = filtered.filter((f) => f.usrfeedbackstatus === statusFilter);
        }
        if (sentimentFilter) {
          filtered = filtered.filter((f) => f.usrfeedbacksentiment === sentimentFilter);
        }
        setFeedback(filtered);
      } else {
        const params = new URLSearchParams();
        if (projectFilter) params.append("projectId", projectFilter);
        if (statusFilter) params.append("status", statusFilter);
        if (sentimentFilter) params.append("sentiment", sentimentFilter);

        const response = await fetch(`/api/v1/pmm/feedback?${params.toString()}`);
        if (response.ok) {
          const data = await response.json();
          setFeedback(data);
        }
      }
    } catch (error) {
      console.error("Error loading feedback:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateFeedback = async () => {
    try {
      if (USE_MOCK_DATA) {
        const newId = Math.max(...mockFeedback.map((f) => f.idxuserfeedback), 0) + 1;
        const created: UserFeedback = {
          ...newFeedback,
          idxuserfeedback: newId,
          iduuid: `uuid-${newId}`,
          projectName: availableProjects.find((p) => p.id === newFeedback.idxproject)?.name,
          usrfeedbackcreatedat: new Date().toISOString(),
          usrfeedbackstatus: "PENDING",
        } as UserFeedback;
        setFeedback([created, ...feedback]);
      } else {
        const response = await fetch("/api/v1/pmm/feedback", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(newFeedback),
        });
        if (response.ok) {
          await loadFeedback();
        }
      }
      setIsDialogOpen(false);
      resetForm();
    } catch (error) {
      console.error("Error creating feedback:", error);
    }
  };

  const handleRespondToFeedback = async () => {
    if (!selectedFeedback || !responseText.trim()) return;

    try {
      if (USE_MOCK_DATA) {
        setFeedback(
          feedback.map((f) =>
            f.idxuserfeedback === selectedFeedback.idxuserfeedback
              ? {
                  ...f,
                  usrfeedbackresponse: responseText,
                  usrfeedbackstatus: "RESPONDED",
                  usrfeedbackrespondedat: new Date().toISOString(),
                }
              : f
          )
        );
      } else {
        const response = await fetch(`/api/v1/pmm/feedback/${selectedFeedback.idxuserfeedback}/respond`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ response: responseText }),
        });
        if (response.ok) {
          await loadFeedback();
        }
      }
      setIsResponseDialogOpen(false);
      setSelectedFeedback(null);
      setResponseText("");
    } catch (error) {
      console.error("Error responding to feedback:", error);
    }
  };

  const handleUpdateStatus = async (feedbackItem: UserFeedback, newStatus: string) => {
    try {
      if (USE_MOCK_DATA) {
        setFeedback(
          feedback.map((f) =>
            f.idxuserfeedback === feedbackItem.idxuserfeedback
              ? { ...f, usrfeedbackstatus: newStatus }
              : f
          )
        );
      } else {
        const response = await fetch(`/api/v1/pmm/feedback/${feedbackItem.idxuserfeedback}/status`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ status: newStatus }),
        });
        if (response.ok) {
          await loadFeedback();
        }
      }
    } catch (error) {
      console.error("Error updating status:", error);
    }
  };

  const handleDeleteFeedback = async (id: number) => {
    if (!confirm(t("governance.compliance.pmm.feedback.confirmDelete", "¿Eliminar este feedback?"))) {
      return;
    }

    try {
      if (USE_MOCK_DATA) {
        setFeedback(feedback.filter((f) => f.idxuserfeedback !== id));
      } else {
        const response = await fetch(`/api/v1/pmm/feedback/${id}`, {
          method: "DELETE",
        });
        if (response.ok) {
          await loadFeedback();
        }
      }
    } catch (error) {
      console.error("Error deleting feedback:", error);
    }
  };

  const resetForm = () => {
    setNewFeedback({
      idxproject: projectIdFromUrl ? parseInt(projectIdFromUrl) : 0,
      usrfeedbacktext: "",
      usrfeedbackrating: undefined,
      usrfeedbackcategory: "",
      usrfeedbackuserid: currentUser.id,
      usrfeedbackuseremail: currentUser.email,
      usrfeedbackstatus: "PENDING",
    });
  };

  const openResponseDialog = (feedbackItem: UserFeedback) => {
    setSelectedFeedback(feedbackItem);
    setResponseText(feedbackItem.usrfeedbackresponse || "");
    setIsResponseDialogOpen(true);
  };

  const getSentimentIcon = (sentiment?: string) => {
    switch (sentiment) {
      case "POSITIVE":
        return <Smile className="w-4 h-4 text-green-500" />;
      case "NEGATIVE":
        return <Frown className="w-4 h-4 text-red-500" />;
      default:
        return <Meh className="w-4 h-4 text-yellow-500" />;
    }
  };

  const getSentimentBadge = (sentiment?: string) => {
    switch (sentiment) {
      case "POSITIVE":
        return <Badge className="bg-green-500">Positive</Badge>;
      case "NEGATIVE":
        return <Badge className="bg-red-500">Negative</Badge>;
      default:
        return <Badge variant="secondary">Neutral</Badge>;
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "PENDING":
        return (
          <Badge variant="secondary">
            <Clock className="w-3 h-3 mr-1" />
            {t("governance.compliance.pmm.feedback.status.pending", "Pendiente")}
          </Badge>
        );
      case "REVIEWED":
        return (
          <Badge className="bg-blue-500">
            <CheckCircle className="w-3 h-3 mr-1" />
            {t("governance.compliance.pmm.feedback.status.reviewed", "Revisado")}
          </Badge>
        );
      case "RESPONDED":
        return (
          <Badge className="bg-green-500">
            <CheckCircle className="w-3 h-3 mr-1" />
            {t("governance.compliance.pmm.feedback.status.responded", "Respondido")}
          </Badge>
        );
      case "ARCHIVED":
        return (
          <Badge variant="outline">
            <Archive className="w-3 h-3 mr-1" />
            {t("governance.compliance.pmm.feedback.status.archived", "Archivado")}
          </Badge>
        );
      default:
        return <Badge variant="secondary">{status}</Badge>;
    }
  };

  const filteredFeedback = feedback.filter((f) => {
    if (projectFilter && f.idxproject.toString() !== projectFilter) return false;
    if (statusFilter && f.usrfeedbackstatus !== statusFilter) return false;
    if (sentimentFilter && f.usrfeedbacksentiment !== sentimentFilter) return false;
    return true;
  });

  return (
    <div className="w-full space-y-6 p-6">
      {/* Header */}
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-3 mb-2">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => {
                globalThis.window.location.href = "/governance/compliance/post-market-monitoring";
              }}
              className="h-8 w-8"
            >
              <ArrowLeft className="h-4 w-4" />
            </Button>
            <h1 className="text-3xl font-bold">
              {t("governance.compliance.pmm.feedback.title", "Feedback de Usuarios")}
            </h1>
          </div>
          <p className="text-muted-foreground mt-2 ml-11">
            {t(
              "governance.compliance.pmm.feedback.subtitle",
              "Gestiona y analiza el feedback de usuarios sobre sistemas de IA"
            )}
          </p>
        </div>
        <Button onClick={() => setIsDialogOpen(true)}>
          <Plus className="w-4 h-4 mr-2" />
          {t("governance.compliance.pmm.feedback.create", "Nuevo Feedback")}
        </Button>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle>{t("governance.compliance.pmm.feedback.filters", "Filtros")}</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <Label>{t("governance.compliance.pmm.feedback.project", "Proyecto")}</Label>
              {projectIdFromUrl ? (
                <div className="mt-2 p-2 bg-muted rounded-md">
                  {availableProjects.find((p) => p.id.toString() === projectIdFromUrl)?.name ||
                    projectIdFromUrl}
                </div>
              ) : (
                <select
                  value={projectFilter}
                  onChange={(e) => setProjectFilter(e.target.value)}
                  className="w-full mt-2 p-2 border rounded-md"
                >
                  <option value="">{t("common.all", "Todos")}</option>
                  {availableProjects.map((project) => (
                    <option key={project.id} value={project.id.toString()}>
                      {project.id} - {project.name}
                    </option>
                  ))}
                </select>
              )}
            </div>
            <div>
              <Label>{t("governance.compliance.pmm.feedback.status", "Estado")}</Label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full mt-2 p-2 border rounded-md"
              >
                <option value="">{t("common.all", "Todos")}</option>
                <option value="PENDING">
                  {t("governance.compliance.pmm.feedback.status.pending", "Pendiente")}
                </option>
                <option value="REVIEWED">
                  {t("governance.compliance.pmm.feedback.status.reviewed", "Revisado")}
                </option>
                <option value="RESPONDED">
                  {t("governance.compliance.pmm.feedback.status.responded", "Respondido")}
                </option>
                <option value="ARCHIVED">
                  {t("governance.compliance.pmm.feedback.status.archived", "Archivado")}
                </option>
              </select>
            </div>
            <div>
              <Label>{t("governance.compliance.pmm.feedback.sentiment", "Sentimiento")}</Label>
              <select
                value={sentimentFilter}
                onChange={(e) => setSentimentFilter(e.target.value)}
                className="w-full mt-2 p-2 border rounded-md"
              >
                <option value="">{t("common.all", "Todos")}</option>
                <option value="POSITIVE">Positive</option>
                <option value="NEUTRAL">Neutral</option>
                <option value="NEGATIVE">Negative</option>
              </select>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Feedback List */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.compliance.pmm.feedback.list", "Feedback")} ({filteredFeedback.length})
          </CardTitle>
        </CardHeader>
        <CardBody>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredFeedback.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.compliance.pmm.feedback.noData", "No hay feedback disponible")}
            </div>
          ) : (
            <div className="space-y-4">
              {filteredFeedback.map((item) => (
                <Card key={item.idxuserfeedback} className="border-l-4 border-l-primary">
                  <CardBody>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <span className="font-semibold">{item.projectName || item.idxproject}</span>
                          {getStatusBadge(item.usrfeedbackstatus)}
                          {getSentimentBadge(item.usrfeedbacksentiment)}
                          {item.usrfeedbackrating && (
                            <div className="flex items-center gap-1">
                              <Star className="w-4 h-4 text-yellow-500 fill-yellow-500" />
                              <span className="text-sm">{item.usrfeedbackrating.toFixed(1)}</span>
                            </div>
                          )}
                        </div>
                        <p className="text-sm text-muted-foreground mb-2">{item.usrfeedbacktext}</p>
                        {item.usrfeedbackresponse && (
                          <div className="mt-2 p-2 bg-muted rounded-md">
                            <p className="text-xs font-semibold mb-1">
                              {t("governance.compliance.pmm.feedback.response", "Respuesta:")}
                            </p>
                            <p className="text-sm">{item.usrfeedbackresponse}</p>
                          </div>
                        )}
                        <div className="flex items-center gap-4 mt-2 text-xs text-muted-foreground">
                          <span>
                            {new Date(item.usrfeedbackcreatedat).toLocaleDateString()}
                          </span>
                          {item.usrfeedbackuseremail && <span>{item.usrfeedbackuseremail}</span>}
                        </div>
                      </div>
                      <div className="flex gap-2">
                        {item.usrfeedbackstatus !== "RESPONDED" && (
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => openResponseDialog(item)}
                          >
                            <MessageSquare className="w-4 h-4" />
                          </Button>
                        )}
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleUpdateStatus(item, "ARCHIVED")}
                        >
                          <Archive className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDeleteFeedback(item.idxuserfeedback)}
                        >
                          <Trash2 className="w-4 h-4" />
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

      {/* Create Feedback Dialog */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl shadow-xl">
          <Card className="border-0 shadow-none">
            {/* HEADER */}
            <CardHeader className="relative pb-4">
              <Button
                variant="ghost"
                size="sm"
                className="absolute right-4 top-4 h-6 w-6"
                onClick={() => setIsDialogOpen(false)}
              >
                <X className="h-4 w-4" />
              </Button>
              <CardTitle className="pr-8">
                {t("governance.compliance.pmm.feedback.create", "Nuevo Feedback")}
              </CardTitle>
              <DialogDescription>
                {t(
                  "governance.compliance.pmm.feedback.dialogDescription",
                  "Registra feedback de usuarios sobre el sistema de IA"
                )}
              </DialogDescription>
            </CardHeader>

            {/* BODY */}
            <CardBody className="space-y-4">
              <div>
                <Label htmlFor="project">{t("governance.compliance.pmm.feedback.project", "Proyecto")}</Label>
                <Select
                  value={(newFeedback.idxproject || 0).toString()}
                  onValueChange={(value) =>
                    setNewFeedback({ ...newFeedback, idxproject: parseInt(value) })
                  }
                  disabled={!!projectIdFromUrl}
                >
                  <SelectTrigger className="mt-2">
                    <SelectValue
                      placeholder={t("governance.compliance.pmm.feedback.selectProject", "Seleccionar proyecto")}
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {availableProjects.map((project) => (
                      <SelectItem key={project.id} value={project.id.toString()}>
                        {project.id} - {project.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              <div>
                <Label htmlFor="feedbackText">{t("governance.compliance.pmm.feedback.text", "Feedback")}</Label>
                <Textarea
                  id="feedbackText"
                  value={newFeedback.usrfeedbacktext || ""}
                  onChange={(e) =>
                    setNewFeedback({ ...newFeedback, usrfeedbacktext: e.target.value })
                  }
                  placeholder={t(
                    "governance.compliance.pmm.feedback.textPlaceholder",
                    "Describe tu experiencia con el sistema..."
                  )}
                  rows={4}
                  className="mt-2"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="rating">{t("governance.compliance.pmm.feedback.rating", "Rating (1-5)")}</Label>
                  <Input
                    id="rating"
                    type="number"
                    min="1"
                    max="5"
                    step="0.1"
                    value={newFeedback.usrfeedbackrating || ""}
                    onChange={(e) =>
                      setNewFeedback({
                        ...newFeedback,
                        usrfeedbackrating: parseFloat(e.target.value) || undefined,
                      })
                    }
                    className="mt-2"
                  />
                </div>
                <div>
                  <Label htmlFor="category">{t("governance.compliance.pmm.feedback.category", "Categoría")}</Label>
                  <Select
                    value={newFeedback.usrfeedbackcategory || ""}
                    onValueChange={(value) =>
                      setNewFeedback({ ...newFeedback, usrfeedbackcategory: value })
                    }
                  >
                    <SelectTrigger className="mt-2">
                      <SelectValue placeholder={t("governance.compliance.pmm.feedback.selectCategory", "Seleccionar categoría")} />
                    </SelectTrigger>
                    <SelectContent>
                      {feedbackCategories.map((category) => (
                        <SelectItem key={category.value} value={category.value}>
                          {category.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="userId">{t("governance.compliance.pmm.feedback.userId", "User ID")}</Label>
                  <Input
                    id="userId"
                    value={newFeedback.usrfeedbackuserid || ""}
                    disabled
                    readOnly
                    className="mt-2 bg-muted cursor-not-allowed"
                  />
                </div>
                <div>
                  <Label htmlFor="userEmail">{t("governance.compliance.pmm.feedback.userEmail", "Email")}</Label>
                  <Input
                    id="userEmail"
                    type="email"
                    value={newFeedback.usrfeedbackuseremail || ""}
                    disabled
                    readOnly
                    className="mt-2 bg-muted cursor-not-allowed"
                  />
                </div>
              </div>
            </CardBody>

            {/* FOOTER */}
            <div className="px-6 py-4 border-t border-gray-200 flex justify-end gap-2">
              <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button onClick={handleCreateFeedback}>
                {t("common.create", "Crear")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>

      {/* Response Dialog */}
      <Dialog open={isResponseDialogOpen} onOpenChange={setIsResponseDialogOpen}>
        <DialogContent className="max-w-2xl shadow-xl">
          <Card className="border-0 shadow-none">
            {/* HEADER */}
            <CardHeader className="relative pb-4">
              <Button
                variant="ghost"
                size="sm"
                className="absolute right-4 top-4 h-6 w-6"
                onClick={() => setIsResponseDialogOpen(false)}
              >
                <X className="h-4 w-4" />
              </Button>
              <CardTitle className="pr-8">
                {t("governance.compliance.pmm.feedback.respond", "Responder Feedback")}
              </CardTitle>
              <DialogDescription>
                {selectedFeedback?.usrfeedbacktext}
              </DialogDescription>
            </CardHeader>

            {/* BODY */}
            <CardBody className="space-y-4">
              <div>
                <Label htmlFor="responseText">{t("governance.compliance.pmm.feedback.response", "Respuesta")}</Label>
                <Textarea
                  id="responseText"
                  value={responseText}
                  onChange={(e) => setResponseText(e.target.value)}
                  rows={4}
                  placeholder={t(
                    "governance.compliance.pmm.feedback.responsePlaceholder",
                    "Escribe tu respuesta..."
                  )}
                  className="mt-2"
                />
              </div>
            </CardBody>

            {/* FOOTER */}
            <div className="px-6 py-4 border-t border-gray-200 flex justify-end gap-2">
              <Button variant="outline" onClick={() => setIsResponseDialogOpen(false)}>
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button onClick={handleRespondToFeedback}>
                {t("common.send", "Enviar")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}
