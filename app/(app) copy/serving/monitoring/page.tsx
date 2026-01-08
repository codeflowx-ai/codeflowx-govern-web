"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  Plus,
  Settings,
} from "lucide-react";

export default function MonitoringPage() {
  const { t } = useTranslation();

  return (
    <div className="space-y-4 p-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Activity className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("serving.monitoring.title", "Monitoring")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("serving.monitoring.subtitle", "Monitoreo de servicios")}
            </p>
          </div>
          <div className="flex gap-3">
            <Button variant="outline">
              <Settings className="w-4 h-4 mr-2" />
              {t("common.settings", "Configuración")}
            </Button>
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Nuevo")}
            </Button>
          </div>
        </div>

        {/* Content */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5" />
              Monitoring Dashboard
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="text-center py-12">
              <p className="text-muted-foreground text-lg">
                Esta página está en desarrollo. Próximamente tendrás acceso a todas las funcionalidades.
              </p>
            </div>
          </CardBody>
        </Card>
    </div>
  );
}


