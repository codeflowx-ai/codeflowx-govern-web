"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, Save } from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { DataGovernanceOrigin } from "../../../types/data-governance";

export default function EditDataOriginPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const originId = params.id as string;
  const [origin, setOrigin] = useState<DataGovernanceOrigin | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [formData, setFormData] = useState({
    dtgorname: "",
    dtgordescription: "",
    dtgortype: "INTERNAL" as "INTERNAL" | "EXTERNAL",
    dtgorcategory: "DATABASE" as "DATABASE" | "API" | "FILE_SYSTEM" | "CLOUD_STORAGE" | "MARKETPLACE" | "WEB",
    dtgorurl: "",
    dtgorhost: "",
    dtgorport: "",
    dtgordatabase: "",
    dtgorconnectionconfig: "",
    dtgorauthmethod: "NONE" as "NONE" | "BASIC" | "OAUTH2" | "API_KEY" | "CERTIFICATE",
    dtgorsyncmethod: "MANUAL" as "MANUAL" | "SCHEDULED" | "REAL_TIME" | "WEBHOOK",
    dtgorsyncfrequency: "ON_DEMAND" as "HOURLY" | "DAILY" | "WEEKLY" | "MONTHLY" | "ON_DEMAND",
    dtgorsyncschedule: "",
    dtgorenabled: true,
    dtgorstatus: "ACTIVE" as "ACTIVE" | "INACTIVE" | "DEPRECATED" | "ERROR",
  });

  useEffect(() => {
    if (originId) {
      loadOrigin();
    }
  }, [originId]);

  const loadOrigin = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/origins/${originId}`);
      if (response.ok) {
        const data = await response.json();
        setOrigin(data);
        setFormData({
          dtgorname: data.dtgorname || "",
          dtgordescription: data.dtgordescription || "",
          dtgortype: data.dtgortype || "INTERNAL",
          dtgorcategory: data.dtgorcategory || "DATABASE",
          dtgorurl: data.dtgorurl || "",
          dtgorhost: data.dtgorhost || "",
          dtgorport: data.dtgorport?.toString() || "",
          dtgordatabase: data.dtgordatabase || "",
          dtgorconnectionconfig: data.dtgorconnectionconfig
            ? JSON.stringify(JSON.parse(data.dtgorconnectionconfig), null, 2)
            : "",
          dtgorauthmethod: data.dtgorauthmethod || "NONE",
          dtgorsyncmethod: data.dtgorsyncmethod || "MANUAL",
          dtgorsyncfrequency: data.dtgorsyncfrequency || "ON_DEMAND",
          dtgorsyncschedule: data.dtgorsyncschedule || "",
          dtgorenabled: data.dtgorenabled !== false,
          dtgorstatus: data.dtgorstatus || "ACTIVE",
        });
      }
    } catch (error) {
      console.error("Error loading origin:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setSaving(true);
      const payload = {
        ...formData,
        dtgorport: formData.dtgorport ? parseInt(formData.dtgorport) : null,
        dtgorconnectionconfig: formData.dtgorconnectionconfig
          ? JSON.stringify(JSON.parse(formData.dtgorconnectionconfig))
          : null,
      };

      const response = await fetch(`/api/v1/governance/data/origins/${originId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        router.push(`/governance/data/origins/${originId}`);
      } else {
        alert("Error al guardar el origen");
      }
    } catch (error) {
      console.error("Error saving origin:", error);
      alert("Error al guardar el origen");
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className="w-full p-6">Cargando...</div>;
  }

  if (!origin) {
    return <div className="w-full p-6">Origen no encontrado</div>;
  }

  return (
    <div className="w-full p-6 space-y-6">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">
          {t("governance.data.origins.edit.title", "Editar Origen de Datos")}
        </h1>
        <p className="text-muted-foreground mt-2">
          {origin?.dtgorname || "Origen de datos"}
        </p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          onClick={() => router.push(`/governance/data/origins/${originId}`)}
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("governance.data.origins.edit.actions.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button type="submit" form="origin-edit-form" disabled={saving}>
            {saving ? (
              <>
                <Save className="mr-2 h-4 w-4 animate-spin" />
                Guardando...
              </>
            ) : (
              <>
                <Save className="mr-2 h-4 w-4" />
                Guardar Cambios
              </>
            )}
          </Button>
        </div>
      </div>

      <form id="origin-edit-form" onSubmit={handleSubmit}>
        <Card>
          <CardHeader>
            <CardTitle>
              {t("governance.data.origins.edit.title", "Editar Origen de Datos")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="md:col-span-2">
                <Label htmlFor="name">
                  {t("governance.data.origins.create.form.name", "Nombre")} *
                </Label>
                <Input
                  id="name"
                  value={formData.dtgorname}
                  onChange={(e) => setFormData({ ...formData, dtgorname: e.target.value })}
                  required
                />
              </div>

              <div className="md:col-span-2">
                <Label htmlFor="description">
                  {t("governance.data.origins.create.form.description", "Descripción")}
                </Label>
                <Textarea
                  id="description"
                  value={formData.dtgordescription}
                  onChange={(e) => setFormData({ ...formData, dtgordescription: e.target.value })}
                  rows={3}
                />
              </div>

              <div>
                <Label htmlFor="type">
                  {t("governance.data.origins.create.form.type", "Tipo")} *
                </Label>
                <Select
                  value={formData.dtgortype}
                  onValueChange={(value) =>
                    setFormData({ ...formData, dtgortype: value as "INTERNAL" | "EXTERNAL" })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="INTERNAL">Interno</SelectItem>
                    <SelectItem value="EXTERNAL">Externo</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div>
                <Label htmlFor="category">
                  {t("governance.data.origins.create.form.category", "Categoría")} *
                </Label>
                <Select
                  value={formData.dtgorcategory}
                  onValueChange={(value: any) =>
                    setFormData({ ...formData, dtgorcategory: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="DATABASE">Base de Datos</SelectItem>
                    <SelectItem value="API">API</SelectItem>
                    <SelectItem value="FILE_SYSTEM">Sistema de Archivos</SelectItem>
                    <SelectItem value="CLOUD_STORAGE">Almacenamiento en la Nube</SelectItem>
                    <SelectItem value="MARKETPLACE">Marketplace</SelectItem>
                    <SelectItem value="WEB">Web</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {formData.dtgorcategory === "API" && (
                <div className="md:col-span-2">
                  <Label htmlFor="url">
                    {t("governance.data.origins.detail.info.url", "URL")}
                  </Label>
                  <Input
                    id="url"
                    type="url"
                    value={formData.dtgorurl}
                    onChange={(e) => setFormData({ ...formData, dtgorurl: e.target.value })}
                    placeholder="https://api.example.com"
                  />
                </div>
              )}

              {formData.dtgorcategory === "DATABASE" && (
                <>
                  <div>
                    <Label htmlFor="host">
                      {t("governance.data.origins.detail.info.host", "Host")}
                    </Label>
                    <Input
                      id="host"
                      value={formData.dtgorhost}
                      onChange={(e) => setFormData({ ...formData, dtgorhost: e.target.value })}
                      placeholder="localhost"
                    />
                  </div>

                  <div>
                    <Label htmlFor="port">
                      {t("governance.data.origins.detail.info.port", "Puerto")}
                    </Label>
                    <Input
                      id="port"
                      type="number"
                      value={formData.dtgorport}
                      onChange={(e) => setFormData({ ...formData, dtgorport: e.target.value })}
                      placeholder="5432"
                    />
                  </div>

                  <div>
                    <Label htmlFor="database">
                      {t("governance.data.origins.detail.info.database", "Base de Datos")}
                    </Label>
                    <Input
                      id="database"
                      value={formData.dtgordatabase}
                      onChange={(e) => setFormData({ ...formData, dtgordatabase: e.target.value })}
                      placeholder="database_name"
                    />
                  </div>
                </>
              )}

              <div className="md:col-span-2">
                <Label htmlFor="connectionConfig">
                  {t("governance.data.origins.create.form.connectionConfig", "Configuración de Conexión")}
                </Label>
                <Textarea
                  id="connectionConfig"
                  value={formData.dtgorconnectionconfig}
                  onChange={(e) => setFormData({ ...formData, dtgorconnectionconfig: e.target.value })}
                  placeholder='{"key": "value"}'
                  rows={5}
                  className="font-mono text-xs"
                />
                <p className="text-xs text-muted-foreground mt-1">
                  Formato JSON
                </p>
              </div>

              <div>
                <Label htmlFor="authMethod">
                  {t("governance.data.origins.create.form.authMethod", "Método de Autenticación")}
                </Label>
                <Select
                  value={formData.dtgorauthmethod}
                  onValueChange={(value: any) =>
                    setFormData({ ...formData, dtgorauthmethod: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="NONE">Ninguno</SelectItem>
                    <SelectItem value="BASIC">Basic</SelectItem>
                    <SelectItem value="OAUTH2">OAuth 2.0</SelectItem>
                    <SelectItem value="API_KEY">API Key</SelectItem>
                    <SelectItem value="CERTIFICATE">Certificado</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div>
                <Label htmlFor="syncMethod">
                  {t("governance.data.origins.create.form.syncMethod", "Método de Sincronización")}
                </Label>
                <Select
                  value={formData.dtgorsyncmethod}
                  onValueChange={(value: any) =>
                    setFormData({ ...formData, dtgorsyncmethod: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="MANUAL">Manual</SelectItem>
                    <SelectItem value="SCHEDULED">Programado</SelectItem>
                    <SelectItem value="REAL_TIME">Tiempo Real</SelectItem>
                    <SelectItem value="WEBHOOK">Webhook</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {formData.dtgorsyncmethod === "SCHEDULED" && (
                <>
                  <div>
                    <Label htmlFor="syncFrequency">
                      {t("governance.data.origins.create.form.syncFrequency", "Frecuencia")}
                    </Label>
                    <Select
                      value={formData.dtgorsyncfrequency}
                      onValueChange={(value: any) =>
                        setFormData({ ...formData, dtgorsyncfrequency: value })
                      }
                    >
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="HOURLY">Cada Hora</SelectItem>
                        <SelectItem value="DAILY">Diario</SelectItem>
                        <SelectItem value="WEEKLY">Semanal</SelectItem>
                        <SelectItem value="MONTHLY">Mensual</SelectItem>
                        <SelectItem value="ON_DEMAND">Bajo Demanda</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>

                  <div>
                    <Label htmlFor="syncSchedule">
                      {t("governance.data.origins.create.form.syncSchedule", "Schedule (Cron)")}
                    </Label>
                    <Input
                      id="syncSchedule"
                      value={formData.dtgorsyncschedule}
                      onChange={(e) => setFormData({ ...formData, dtgorsyncschedule: e.target.value })}
                      placeholder="0 0 * * *"
                    />
                  </div>
                </>
              )}

              <div>
                <Label htmlFor="status">
                  {t("governance.data.origins.detail.info.status", "Estado")}
                </Label>
                <Select
                  value={formData.dtgorstatus}
                  onValueChange={(value: any) =>
                    setFormData({ ...formData, dtgorstatus: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ACTIVE">Activo</SelectItem>
                    <SelectItem value="INACTIVE">Inactivo</SelectItem>
                    <SelectItem value="DEPRECATED">Deprecado</SelectItem>
                    <SelectItem value="ERROR">Error</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="enabled"
                  checked={formData.dtgorenabled}
                  onChange={(e) => setFormData({ ...formData, dtgorenabled: e.target.checked })}
                  className="h-4 w-4"
                />
                <Label htmlFor="enabled" className="cursor-pointer">
                  {t("governance.data.origins.detail.info.enabled", "Habilitado")}
                </Label>
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-4 border-t">
              <Button
                type="button"
                variant="outline"
                onClick={() => router.push(`/governance/data/origins/${originId}`)}
              >
                {t("governance.data.origins.edit.actions.cancel", "Cancelar")}
              </Button>
              <Button type="submit" disabled={saving}>
                <Save className="mr-2 h-4 w-4" />
                {saving
                  ? t("governance.data.origins.edit.actions.saving", "Guardando...")
                  : t("governance.data.origins.edit.actions.save", "Guardar")}
              </Button>
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
