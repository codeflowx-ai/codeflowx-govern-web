"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Textarea } from "@/components/ui/textarea";
import {
  CheckCircle2,
  ClipboardCheck,
  Eye,
  Search,
  X,
  XCircle,
} from "lucide-react";
import { useEffect, useState } from "react";
interface ModelApproval {
  id: number;
  modelName: string;
  modelType: string;
  owner: string;
  riskLevel: "HIGH" | "MEDIUM" | "LOW";
  requestDate: string;
  status: string;
}
export default function ModelApprovalPage() {
  const { t } = useTranslation();
  const [approvals, setApprovals] = useState<ModelApproval[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [riskLevelFilter, setRiskLevelFilter] = useState<string>("ALL");
  const [ownerFilter, setOwnerFilter] = useState("");
  const [selectedApproval, setSelectedApproval] = useState<ModelApproval | null>(null);
  const [showApprovalModal, setShowApprovalModal] = useState(false);
  const [showRejectionModal, setShowRejectionModal] = useState(false);
  const [showDetailModal, setShowDetailModal] = useState(false);
  const [approvalComments, setApprovalComments] = useState("");
  const [rejectionReason, setRejectionReason] = useState("");
  const [stats, setStats] = useState({
    totalPending: 0,
    totalApproved: 0,
    totalRejected: 0,
  });
  useEffect(() => {
    loadData();
  }, []);
  const loadData = async () => {
    try {
      setLoading(true);
      const searchParams = new URLSearchParams();
      if (searchQuery) searchParams.append("search", searchQuery);
      if (riskLevelFilter !== "ALL") searchParams.append("riskLevel", riskLevelFilter);
      if (ownerFilter) searchParams.append("owner", ownerFilter);

      const response = await fetch(
        `/api/v1/bff/compliance/models/approval${searchParams.toString() ? `?${searchParams.toString()}` : ""}`
      );

      if (response.ok) {
        const data: ModelApproval[] = await response.json();
        setApprovals(data);
        setStats({
          totalPending: data.filter((a) => a.status === "PENDING").length,
          totalApproved: data.filter((a) => a.status === "APPROVED").length,
          totalRejected: data.filter((a) => a.status === "REJECTED").length,
        });
        return;
      }

      // Fallback a mock data si la API falla
      const mockData: ModelApproval[] = [
        {
          id: 1,
          modelName: "GPT-4 Fine-tuned",
          modelType: "TEXT",
          owner: "admin@example.com",
          riskLevel: "HIGH",
          requestDate: "2024-01-15T10:30:00",
          status: "PENDING",
        },
        {
          id: 2,
          modelName: "Claude Vision",
          modelType: "MULTIMODAL",
          owner: "user@example.com",
          riskLevel: "MEDIUM",
          requestDate: "2024-01-14T14:20:00",
          status: "PENDING",
        },
        {
          id: 3,
          modelName: "Stable Diffusion",
          modelType: "IMAGE",
          owner: "dev@example.com",
          riskLevel: "LOW",
          requestDate: "2024-01-13T09:15:00",
          status: "PENDING",
        },
      ];
      setApprovals(mockData);
      setStats({
        totalPending: mockData.length,
        totalApproved: 12,
        totalRejected: 3,
      });
    } catch (error) {
      console.error("Error loading approvals:", error);
    } finally {
      setLoading(false);
    }
  };
  const filteredApprovals = approvals.filter((approval) => {
    const matchesSearch =
      approval.modelName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      approval.owner.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesRisk = riskLevelFilter === "ALL" || approval.riskLevel === riskLevelFilter;
    const matchesOwner = !ownerFilter || approval.owner.toLowerCase().includes(ownerFilter.toLowerCase());
    return matchesSearch && matchesRisk && matchesOwner;
  });
  const handleApprove = async () => {
    if (selectedApproval) {
      // TODO: Implementar llamada API
      console.log("Approving model:", selectedApproval.id, approvalComments);
      setShowApprovalModal(false);
      setSelectedApproval(null);
      setApprovalComments("");
      loadData();
    }
  };
  const handleReject = async () => {
    if (selectedApproval && rejectionReason) {
      // TODO: Implementar llamada API
      console.log("Rejecting model:", selectedApproval.id, rejectionReason);
      setShowRejectionModal(false);
      setSelectedApproval(null);
      setRejectionReason("");
      loadData();
    }
  };
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };
  const getRiskBadgeColor = (risk: string) => {
    switch (risk) {
      case "HIGH":
        return "bg-red-500/20 text-red-400 border-red-500/50";
      case "MEDIUM":
        return "bg-yellow-500/20 text-yellow-400 border-yellow-500/50";
      case "LOW":
        return "bg-green-500/20 text-green-400 border-green-500/50";
      default:
        return "bg-gray-500/20 text-gray-400 border-gray-500/50";
    }
  };
  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <ClipboardCheck className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.approval.title", "Aprobaciones de Modelos")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t("governance.models.approval.subtitle", "Gestión de solicitudes de aprobación pendientes")}
        </p>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.approval.metrics.pending", "Pendientes de Aprobación")}
            </p>
            <h2 className="text-2xl font-bold">{stats.totalPending}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.approval.metrics.approved", "Modelos Aprobados")}
            </p>
            <h2 className="text-2xl font-bold">{stats.totalApproved}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.approval.metrics.rejected", "Modelos Rechazados")}
            </p>
            <h2 className="text-2xl font-bold">{stats.totalRejected}</h2>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.approval.filters.title", "Filtros")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.approval.filters.riskLevel", "Nivel de Riesgo")}
              </label>
              <Select value={riskLevelFilter} onValueChange={setRiskLevelFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.approval.filters.all", "Todos")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.approval.filters.all", "Todos")}</SelectItem>
                  <SelectItem value="HIGH">{t("governance.models.approval.filters.high", "Alto")}</SelectItem>
                  <SelectItem value="MEDIUM">{t("governance.models.approval.filters.medium", "Medio")}</SelectItem>
                  <SelectItem value="LOW">{t("governance.models.approval.filters.low", "Bajo")}</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.approval.filters.owner", "Owner")}
              </label>
              <Input
                placeholder={t("governance.models.approval.filters.ownerPlaceholder", "Filtrar por owner")}
                value={ownerFilter}
                onChange={(e) => setOwnerFilter(e.target.value)}
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.approval.filters.search", "Búsqueda")}
              </label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.models.approval.filters.searchPlaceholder", "Buscar...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
      {/* Tabla de Modelos Pendientes */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <ClipboardCheck className="w-5 h-5" />
            {t("governance.models.approval.table.title", "Modelos Pendientes de Aprobación")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredApprovals.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.approval.table.noResults", "No hay modelos pendientes de aprobación")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.approval.table.modelName", "Nombre Modelo")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.type", "Tipo")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.owner", "Owner")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.riskLevel", "Risk Level")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.requestDate", "Fecha Solicitud")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.status", "Estado")}</th>
                    <th className="text-center p-2">{t("governance.models.approval.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredApprovals.map((approval) => (
                    <tr
                      key={approval.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2">
                        <button
                          onClick={() => {
                            setSelectedApproval(approval);
                            setShowApprovalModal(true);
                          }}
                          className="text-primary hover:underline font-medium"
                        >
                          {approval.modelName}
                        </button>
                      </td>
                      <td className="p-2 text-center text-muted-foreground">
                        {approval.modelType}
                      </td>
                      <td className="p-2 text-center text-muted-foreground">
                        {approval.owner}
                      </td>
                      <td className="p-2 text-center">
                        <Badge className={getRiskBadgeColor(approval.riskLevel)}>
                          {approval.riskLevel}
                        </Badge>
                      </td>
                      <td className="p-2 text-center text-muted-foreground">
                        {formatDate(approval.requestDate)}
                      </td>
                      <td className="p-2 text-center">
                        <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50">
                          {t("governance.models.approval.table.statusUnderReview", "EN REVISIÓN")}
                        </Badge>
                      </td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setSelectedApproval(approval);
                              setShowDetailModal(true);
                            }}
                            title={t("common.view", "Ver")}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="text-green-400 hover:text-green-300"
                            onClick={() => {
                              setSelectedApproval(approval);
                              setShowApprovalModal(true);
                            }}
                            title={t("governance.models.approval.table.approve", "Aprobar")}
                          >
                            <CheckCircle2 className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="text-red-400 hover:text-red-300"
                            onClick={() => {
                              setSelectedApproval(approval);
                              setShowRejectionModal(true);
                            }}
                            title={t("governance.models.approval.table.reject", "Rechazar")}
                          >
                            <XCircle className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
      {/* Modal de Aprobación */}
      <SimpleModal
        isOpen={showApprovalModal}
        onClose={() => {
          setShowApprovalModal(false);
          setSelectedApproval(null);
          setApprovalComments("");
        }}
        title={t("governance.models.approval.modal.approve.title", "Aprobar Modelo")}
      >
        {selectedApproval && (
          <div className="space-y-4">
            <div className="p-4 bg-green-500/20 border border-green-500/50 rounded-lg">
              <p className="text-green-400 font-semibold flex items-center gap-2">
                <CheckCircle2 className="w-5 h-5" />
                {t("governance.models.approval.modal.approve.confirm", "¿Confirmar aprobación del modelo?")}
              </p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.modelName", "Nombre del Modelo")}:</p>
              <p className="text-muted-foreground">{selectedApproval.modelName}</p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.type", "Tipo")}:</p>
              <p className="text-muted-foreground">{selectedApproval.modelType}</p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.owner", "Owner")}:</p>
              <p className="text-muted-foreground">{selectedApproval.owner}</p>
            </div>
            <div className="space-y-2">
              <label className="font-bold">
                {t("governance.models.approval.modal.approve.comments", "Comentarios (opcional)")}:
              </label>
              <Textarea
                value={approvalComments}
                onChange={(e) => setApprovalComments(e.target.value)}
                placeholder={t("governance.models.approval.modal.approve.commentsPlaceholder", "Agregue comentarios sobre la aprobación...")}
                rows={4}
              />
            </div>
            <div className="flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => {
                  setShowApprovalModal(false);
                  setSelectedApproval(null);
                  setApprovalComments("");
                }}
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button onClick={handleApprove} className="bg-green-600 hover:bg-green-700">
                <CheckCircle2 className="w-4 h-4 mr-2" />
                {t("governance.models.approval.modal.approve.confirmButton", "Confirmar Aprobación")}
              </Button>
            </div>
          </div>
        )}
      </SimpleModal>
      {/* Modal de Rechazo */}
      <SimpleModal
        isOpen={showRejectionModal}
        onClose={() => {
          setShowRejectionModal(false);
          setSelectedApproval(null);
          setRejectionReason("");
        }}
        title={t("governance.models.approval.modal.reject.title", "Rechazar Modelo")}
      >
        {selectedApproval && (
          <div className="space-y-4">
            <div className="p-4 bg-red-500/20 border border-red-500/50 rounded-lg">
              <p className="text-red-400 font-semibold flex items-center gap-2">
                <XCircle className="w-5 h-5" />
                {t("governance.models.approval.modal.reject.confirm", "¿Confirmar rechazo del modelo?")}
              </p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.modelName", "Nombre del Modelo")}:</p>
              <p className="text-muted-foreground">{selectedApproval.modelName}</p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.type", "Tipo")}:</p>
              <p className="text-muted-foreground">{selectedApproval.modelType}</p>
            </div>
            <div className="space-y-2">
              <p className="font-bold">{t("governance.models.approval.modal.owner", "Owner")}:</p>
              <p className="text-muted-foreground">{selectedApproval.owner}</p>
            </div>
            <div className="space-y-2">
              <label className="font-bold text-red-400">
                {t("governance.models.approval.modal.reject.reason", "Razón del Rechazo (OBLIGATORIO)")} *
              </label>
              <Textarea
                value={rejectionReason}
                onChange={(e) => setRejectionReason(e.target.value)}
                placeholder={t("governance.models.approval.modal.reject.reasonPlaceholder", "Debe especificar la razón del rechazo...")}
                rows={4}
                required
              />
              <p className="text-sm text-muted-foreground">
                {t("governance.models.approval.modal.reject.reasonInfo", "Esta información será enviada al owner del modelo")}
              </p>
            </div>
            <div className="flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => {
                  setShowRejectionModal(false);
                  setSelectedApproval(null);
                  setRejectionReason("");
                }}
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleReject}
                disabled={!rejectionReason}
                className="bg-red-600 hover:bg-red-700"
              >
                <XCircle className="w-4 h-4 mr-2" />
                {t("governance.models.approval.modal.reject.confirmButton", "Confirmar Rechazo")}
              </Button>
            </div>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}


