import Link from "next/link";
import { notFound } from "next/navigation";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";

import { mockPolicyViolations } from "../../_mock";

export default function GovernanceMonitoringViolationDetailPage({
  params,
}: {
  params: { id: string };
}) {
  const violation = mockPolicyViolations.find((v) => v.id === params.id);
  if (!violation) return notFound();

  const getSeverityVariant = (severity: string) => {
    switch (severity) {
      case "low":
        return "info";
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

  const getStatusVariant = (status: string) => {
    switch (status) {
      case "open":
        return "danger";
      case "investigating":
        return "warning";
      case "resolved":
        return "success";
      default:
        return "outline";
    }
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case "open":
        return "Abierta";
      case "investigating":
        return "En investigación";
      case "resolved":
        return "Resuelta";
      default:
        return status;
    }
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Detalle de Violación
          </h1>
          <p className="text-gray-600 mt-2">{violation.policy}</p>
        </div>
        <Link href="/governance/monitoring">
          <Button variant="outline">Volver</Button>
        </Link>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>{violation.policy}</CardTitle>
            <div className="flex items-center gap-2">
              <Badge variant={getSeverityVariant(violation.severity)}>
                Severidad: {violation.severity.toUpperCase()}
              </Badge>
              <Badge variant={getStatusVariant(violation.status)}>
                {getStatusLabel(violation.status)}
              </Badge>
            </div>
          </div>
        </CardHeader>
        <CardBody className="space-y-4">
          <div>
            <p className="text-sm text-gray-600">Descripción</p>
            <p className="text-gray-900 mt-1">{violation.description}</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
            <div>
              <span className="text-gray-600">Detectado:</span>
              <span className="ml-2 font-medium">{violation.detectedAt}</span>
            </div>
            <div>
              <span className="text-gray-600">Asignado a:</span>
              <span className="ml-2 font-medium">{violation.assignedTo}</span>
            </div>
            <div>
              <span className="text-gray-600">ID:</span>
              <span className="ml-2 font-medium">{violation.id}</span>
            </div>
          </div>

          <div>
            <p className="text-sm text-gray-600">Recursos afectados</p>
            <ul className="list-disc pl-5 mt-2 space-y-1">
              {violation.affectedResources.map((r) => (
                <li key={r} className="text-gray-900">
                  {r}
                </li>
              ))}
            </ul>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
