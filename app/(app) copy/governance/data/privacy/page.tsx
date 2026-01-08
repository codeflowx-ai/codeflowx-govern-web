"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Shield, AlertTriangle, CheckCircle, FileCheck } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export default function PrivacyOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [stats, setStats] = useState({
    totalDatasets: 45,
    datasetsWithPII: 15,
    datasetsWithConsent: 12,
    datasetsWithDPIA: 8,
    datasetsCompliant: 32,
    datasetsNonCompliant: 13,
  });

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.privacy.title", "Gestión de Privacidad y GDPR")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.privacy.description", "Cumplimiento GDPR, gestión de PII, consentimiento y DPIA")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/dashboard")}>
          Ver Dashboard
        </Button>
      </div>

      {/* KPIs */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Datasets con PII</p>
                <p className="text-2xl font-bold">{stats.datasetsWithPII}</p>
              </div>
              <AlertTriangle className="h-8 w-8 text-orange-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Con Consentimiento</p>
                <p className="text-2xl font-bold">{stats.datasetsWithConsent}</p>
              </div>
              <CheckCircle className="h-8 w-8 text-green-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">DPIA Completado</p>
                <p className="text-2xl font-bold">{stats.datasetsWithDPIA}</p>
              </div>
              <FileCheck className="h-8 w-8 text-blue-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Compliance GDPR</p>
                <p className="text-2xl font-bold">{stats.datasetsCompliant}/{stats.totalDatasets}</p>
              </div>
              <Shield className="h-8 w-8 text-green-500" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Requisitos GDPR */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Requisitos GDPR</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 border rounded-lg">
                <div>
                  <p className="font-medium">Art. 6 - Base Legal</p>
                  <p className="text-xs text-muted-foreground">Base legal del procesamiento</p>
                </div>
                <Badge className="bg-green-100 text-green-800">
                  {stats.datasetsCompliant} cumplen
                </Badge>
              </div>
              <div className="flex items-center justify-between p-3 border rounded-lg">
                <div>
                  <p className="font-medium">Art. 7 - Consentimiento</p>
                  <p className="text-xs text-muted-foreground">Consentimiento explícito</p>
                </div>
                <Badge className="bg-yellow-100 text-yellow-800">
                  {stats.datasetsWithConsent}/{stats.datasetsWithPII} con PII
                </Badge>
              </div>
              <div className="flex items-center justify-between p-3 border rounded-lg">
                <div>
                  <p className="font-medium">Art. 35 - DPIA</p>
                  <p className="text-xs text-muted-foreground">Evaluación de impacto</p>
                </div>
                <Badge className="bg-blue-100 text-blue-800">
                  {stats.datasetsWithDPIA} completados
                </Badge>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="border-l-4 border-l-red-500">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-red-500" />
              Alertas de Compliance
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <div className="p-3 bg-red-50 rounded-lg">
                <p className="font-medium text-red-800">
                  {stats.datasetsNonCompliant} datasets no cumplen GDPR
                </p>
                <p className="text-sm text-red-600 mt-1">
                  Requieren atención inmediata
                </p>
              </div>
              <div className="p-3 bg-yellow-50 rounded-lg">
                <p className="font-medium text-yellow-800">
                  {stats.datasetsWithPII - stats.datasetsWithConsent} datasets con PII sin consentimiento
                </p>
                <p className="text-sm text-yellow-600 mt-1">
                  Acción requerida
                </p>
              </div>
            </div>
            <Button
              variant="outline"
              className="w-full mt-4"
              onClick={() => router.push("/governance/data/datasets/overview?compliance=non_compliant")}
            >
              Ver Datasets No Cumplientes
            </Button>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}


