"use client";
import { useTranslation } from "@/app/config/i18n";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import ABTestResults from "../execute/components/ABTestResults";
import { Card, CardBody } from "@/components/ui/card";
import { Loader2 } from "lucide-react";

export default function ABTestResultsPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const [result, setResult] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const resultParam = searchParams.get("result");
    if (resultParam) {
      try {
        const parsed = JSON.parse(decodeURIComponent(resultParam));
        setResult(parsed);
      } catch (e) {
        console.error("Error parsing result:", e);
      }
    }
    setLoading(false);
  }, [searchParams]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Loader2 className="w-8 h-8 animate-spin" />
      </div>
    );
  }

  if (!result) {
    return (
      <div className="space-y-4">
        <Card>
          <CardBody>
            <p>{t("governance.testing.abTest.results.noResults", "No hay resultados para mostrar")}</p>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <ABTestResults result={result} />
    </div>
  );
}

