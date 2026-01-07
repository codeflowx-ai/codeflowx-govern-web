"use client";

import { useEffect, useMemo, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ArrowLeft, Loader2 } from "lucide-react";

type ApiGetResponse =
  | {
      success: true;
      data: { registrationId: number };
    }
  | { success: false; error?: string };

type ApiPostResponse =
  | {
      success: true;
      registrationId: number;
      status?: string;
    }
  | { success: false; error?: string };

export default function EuRegistrationNewPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const searchParams = useSearchParams();

  const projectId = useMemo(() => {
    const raw = searchParams.get("projectId");
    const n = raw ? Number(raw) : NaN;
    return Number.isFinite(n) ? n : null;
  }, [searchParams]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const goBack = () => {
    router.push("/governance/compliance/eu-registration");
  };

  const startOrResume = async () => {
    if (!projectId) {
      setError(
        t(
          "governance.compliance.euRegistration.new.missingProjectId",
          "Falta el parámetro projectId en la URL."
        )
      );
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError(null);

      // 1) Intentar recuperar registro por proyecto
      const getRes = await fetch(`/api/compliance/eu-registration?projectId=${projectId}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      if (getRes.ok) {
        const payload = (await getRes.json()) as ApiGetResponse;
        const regId = (payload as any)?.data?.registrationId;
        if (payload && (payload as any).success && Number.isFinite(regId)) {
          router.replace(`/governance/compliance/eu-registration/${regId}`);
          return;
        }
      }

      // 2) Si no existe, crear borrador mínimo (sección vacía) y redirigir
      const postRes = await fetch(`/api/compliance/eu-registration`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          projectId,
          section: "A",
          data: {},
        }),
      });

      if (!postRes.ok) {
        throw new Error(`No se pudo crear el borrador (HTTP ${postRes.status})`);
      }

      const postPayload = (await postRes.json()) as ApiPostResponse;
      if ((postPayload as any)?.success && Number.isFinite((postPayload as any)?.registrationId)) {
        router.replace(
          `/governance/compliance/eu-registration/${(postPayload as any).registrationId}`
        );
        return;
      }

      throw new Error(
        (postPayload as any)?.error ||
          t(
            "governance.compliance.euRegistration.new.unknownError",
            "No se pudo iniciar el registro."
          )
      );
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!mounted) return;
    void startOrResume();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [mounted, projectId]);

  if (!mounted) return null;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 flex items-center justify-center px-4">
      <Card className="w-full max-w-2xl">
        <CardHeader>
          <div className="flex items-center justify-between gap-3">
            <CardTitle>
              {t(
                "governance.compliance.euRegistration.new.title",
                "Nuevo Registro EU Database"
              )}
            </CardTitle>
            <Button variant="outline" onClick={goBack}>
              <ArrowLeft className="h-4 w-4 mr-2" />
              {t("governance.compliance.euRegistration.back", "Volver")}
            </Button>
          </div>
        </CardHeader>
        <CardBody className="space-y-4">
          <div className="flex items-center gap-2">
            <Badge variant="outline">
              projectId: {projectId ?? t("common.unknown", "desconocido")}
            </Badge>
            {loading && (
              <Badge className="bg-blue-500 text-white">
                <Loader2 className="h-3 w-3 mr-1 animate-spin" />
                {t("common.loading", "Cargando...")}
              </Badge>
            )}
            {error && <Badge variant="danger">Error</Badge>}
          </div>

          {error ? (
            <div className="space-y-3">
              <div className="text-sm text-red-700">{error}</div>
              <div className="flex gap-2">
                <Button variant="primary" onClick={startOrResume}>
                  {t("common.retry", "Reintentar")}
                </Button>
                <Button variant="outline" onClick={goBack}>
                  {t("common.backToList", "Volver al listado")}
                </Button>
              </div>
            </div>
          ) : (
            <div className="text-sm text-muted-foreground">
              {t(
                "governance.compliance.euRegistration.new.redirecting",
                "Preparando el borrador y redirigiendo al formulario…"
              )}
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}
