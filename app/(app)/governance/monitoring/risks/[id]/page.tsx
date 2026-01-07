import Link from "next/link";
import { notFound } from "next/navigation";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";

import { mockRiskIndicators } from "../../_mock";

export default function GovernanceMonitoringRiskDetailPage({
  params,
}: {
  params: { id: string };
}) {
  const risk = mockRiskIndicators.find((r) => r.id === params.id);
  if (!risk) return notFound();

  const getRiskLevelColor = (level: string) => {
    switch (level) {
      case "low":
        return "success";
      case "medium":
        return "warning";
      case "high":
        return "danger";
      case "critical":
        return "danger";
      default:
        return "outline";
    }
  };

  const getMitigationLabel = (status: string) => {
    switch (status) {
      case "none":
        return "Ninguna";
      case "planned":
        return "Planificada";
      case "in-progress":
        return "En progreso";
      case "completed":
        return "Completada";
      default:
        return status;
    }
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Detalle de Riesgo</h1>
          <p className="text-gray-600 mt-2">{risk.name}</p>
        </div>
        <Link href="/governance/monitoring">
          <Button variant="outline">Volver</Button>
        </Link>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>{risk.name}</CardTitle>
            <Badge variant={getRiskLevelColor(risk.riskLevel)}>
              {risk.riskLevel.toUpperCase()}
            </Badge>
          </div>
        </CardHeader>
        <CardBody className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="space-y-2">
              <div className="flex items-center justify-between text-sm">
                <span className="text-gray-600">Probabilidad</span>
                <span className="font-medium">{risk.probability}%</span>
              </div>
              <Progress value={risk.probability} className="h-2" />
            </div>

            <div className="space-y-2">
              <div className="flex items-center justify-between text-sm">
                <span className="text-gray-600">Impacto</span>
                <span className="font-medium">{risk.impact}%</span>
              </div>
              <Progress value={risk.impact} className="h-2" />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <div>
              <span className="text-gray-600">Estado de mitigación:</span>
              <span className="ml-2 font-medium">
                {getMitigationLabel(risk.mitigationStatus)}
              </span>
            </div>
            <div>
              <span className="text-gray-600">Última evaluación:</span>
              <span className="ml-2 font-medium">{risk.lastAssessment}</span>
            </div>
            <div>
              <span className="text-gray-600">Próxima evaluación:</span>
              <span className="ml-2 font-medium">{risk.nextAssessment}</span>
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
