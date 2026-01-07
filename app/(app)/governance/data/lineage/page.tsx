"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { GitBranch, ArrowRight, Database } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export default function LineageOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [lineage, setLineage] = useState([
    {
      from: "Raw Customer Data",
      to: "Customer Dataset v1.0",
      type: "TRANSFORMATION",
      timestamp: "2025-01-10T08:00:00Z",
    },
    {
      from: "Customer Dataset v1.0",
      to: "Customer Aggregated",
      type: "AGGREGATION",
      timestamp: "2025-01-12T14:00:00Z",
    },
  ]);

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.lineage.title", "Línea de Base de Datos")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.lineage.description", "Trazabilidad y dependencias entre datasets")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/dashboard")}>
          Ver Dashboard
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Flujos de Datos</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            {lineage.map((item, idx) => (
              <div key={idx} className="flex items-center gap-4 p-4 border rounded-lg">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <Database className="h-4 w-4 text-blue-500" />
                    <span className="font-medium">{item.from}</span>
                  </div>
                </div>
                <ArrowRight className="h-5 w-5 text-muted-foreground" />
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <Database className="h-4 w-4 text-green-500" />
                    <span className="font-medium">{item.to}</span>
                  </div>
                </div>
                <Badge variant="outline">{item.type}</Badge>
                <span className="text-xs text-muted-foreground">
                  {new Date(item.timestamp).toLocaleDateString()}
                </span>
              </div>
            ))}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
