"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { motion } from "framer-motion";
import { AlertTriangle, ArrowLeft, Save, Send } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

// Mock data
const mockAlert = {
  id: 1,
  name: "High CPU Usage Alert",
  type: "PERFORMANCE",
  severity: "HIGH",
  status: "ACTIVE",
};

export default function AlertResponseFormPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [formData, setFormData] = useState({
    decision: "",
    notes: "",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implementar guardado
    console.log("Guardando respuesta:", formData);
    router.back();
  };

  const navigateBack = () => {
    router.back();
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="space-y-4"
        >
          {/* Primera línea: Título y subtítulo */}
          <div className="flex items-center gap-3">
            <AlertTriangle className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold">
                {t("monitoring.alerts.responseForm.title", "Respuesta a Alerta Crítica")}
              </h1>
              <p className="text-muted-foreground">
                {t(
                  "monitoring.alerts.responseForm.description",
                  "Formulario de respuesta a alerta"
                )}
              </p>
            </div>
          </div>

          {/* Segunda línea: Botones */}
          <div className="flex items-center justify-between">
            <Button
              variant="ghost"
              size="sm"
              onClick={navigateBack}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="w-4 h-4" />
              {t("common.back", "Volver")}
            </Button>
            <div className="flex items-center gap-4">
              <Button type="button" variant="outline" onClick={navigateBack}>
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button type="submit" form="alert-response-form" className="gap-2">
                <Save className="w-4 h-4" />
                {t("common.save", "Guardar")}
              </Button>
            </div>
          </div>
        </motion.div>

        {/* Información de la Alerta */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
        >
          <Card>
            <CardHeader>
              <CardTitle>
                {t("monitoring.alerts.responseForm.alertInfo", "Información de la Alerta")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label className="text-muted-foreground">
                    {t("monitoring.alerts.responseForm.alertName", "Nombre")}
                  </Label>
                  <p className="font-medium">{mockAlert.name}</p>
                </div>
                <div>
                  <Label className="text-muted-foreground">
                    {t("monitoring.alerts.responseForm.severity", "Severidad")}
                  </Label>
                  <p className="font-medium">{mockAlert.severity}</p>
                </div>
                <div>
                  <Label className="text-muted-foreground">
                    {t("monitoring.alerts.responseForm.type", "Tipo")}
                  </Label>
                  <p className="font-medium">{mockAlert.type}</p>
                </div>
                <div>
                  <Label className="text-muted-foreground">
                    {t("monitoring.alerts.responseForm.status", "Estado")}
                  </Label>
                  <p className="font-medium">{mockAlert.status}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Formulario de Respuesta */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
        >
          <Card>
            <CardHeader>
              <CardTitle>
                {t("monitoring.alerts.responseForm.formData", "Form Data")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form id="alert-response-form" onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="decision">
                    {t("monitoring.alerts.responseForm.decision", "Decision")} *
                  </Label>
                  <Input
                    id="decision"
                    value={formData.decision}
                    onChange={(e) =>
                      setFormData({ ...formData, decision: e.target.value })
                    }
                    placeholder={t(
                      "monitoring.alerts.responseForm.decisionPlaceholder",
                      "Enter decision..."
                    )}
                    required
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="notes">
                    {t("monitoring.alerts.responseForm.notes", "Notes")}
                  </Label>
                  <Textarea
                    id="notes"
                    value={formData.notes}
                    onChange={(e) =>
                      setFormData({ ...formData, notes: e.target.value })
                    }
                    placeholder={t(
                      "monitoring.alerts.responseForm.notesPlaceholder",
                      "Enter notes..."
                    )}
                    rows={6}
                  />
                </div>
              </form>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </div>
  );
}
