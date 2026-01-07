"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { AlertTriangle, Plus, Search, Eye } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { DataGovernanceDatasetRisk } from "../types/data-governance";

export default function RisksOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [risks, setRisks] = useState<Array<DataGovernanceDatasetRisk & { datasetName: string }>>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filters, setFilters] = useState({
    type: "",
    status: "",
    probability: "",
    impact: "",
  });

  useEffect(() => {
    loadRisks();
  }, [filters, searchTerm]);

  const loadRisks = async () => {
    try {
      setLoading(true);
      // Mock: En producción, esto vendría de un endpoint consolidado
      const mockRisks = [
        {
          idxrisk: 1,
          idxdataset: 1,
          datasetName: "Customer Dataset v1.0",
          dtgrisktype: "QUALITY",
          dtgriskname: "Baja completitud de datos",
          dtgriskdescription: "El dataset tiene un 15% de valores nulos en campos críticos",
          dtgriskprobability: "HIGH",
          dtgriskimpact: "MEDIUM",
          dtgriskscore: 0.70,
          dtgriskstatus: "IDENTIFIED",
          dtgmitigationplan: "Implementar validación de datos en origen",
          dtgmitigationstatus: "NOT_STARTED",
          dtgresponsible: 1,
          dtgtargetdate: "2025-01-20T00:00:00Z",
          dtgcreatedat: "2025-01-14T10:00:00Z",
          dtgcreatedby: 1,
        },
        {
          idxrisk: 2,
          idxdataset: 1,
          datasetName: "Customer Dataset v1.0",
          dtgrisktype: "BIAS",
          dtgriskname: "Sesgo de género detectado",
          dtgriskdescription: "Distribución desbalanceada: 70% hombres, 25% mujeres",
          dtgriskprobability: "MEDIUM",
          dtgriskimpact: "HIGH",
          dtgriskscore: 0.60,
          dtgriskstatus: "ASSESSED",
          dtgmitigationplan: "Aumentar representatividad",
          dtgmitigationstatus: "IN_PROGRESS",
          dtgresponsible: 2,
          dtgtargetdate: "2025-01-25T00:00:00Z",
          dtgcreatedat: "2025-01-13T09:00:00Z",
          dtgcreatedby: 1,
        },
        {
          idxrisk: 3,
          idxdataset: 2,
          datasetName: "Sales Data Q4 2024",
          dtgrisktype: "PRIVACITY",
          dtgriskname: "PII detectado sin consentimiento",
          dtgriskdescription: "El dataset contiene emails sin evidencia de consentimiento",
          dtgriskprobability: "HIGH",
          dtgriskimpact: "CRITICAL",
          dtgriskscore: 1.0,
          dtgriskstatus: "IDENTIFIED",
          dtgmitigationplan: "Obtener consentimiento o anonimizar",
          dtgmitigationstatus: "NOT_STARTED",
          dtgresponsible: 3,
          dtgtargetdate: "2025-01-18T00:00:00Z",
          dtgcreatedat: "2025-01-14T11:00:00Z",
          dtgcreatedby: 1,
        },
      ];

      let filtered = mockRisks;

      if (searchTerm) {
        const searchLower = searchTerm.toLowerCase();
        filtered = filtered.filter(
          (r) =>
            r.dtgriskname.toLowerCase().includes(searchLower) ||
            r.datasetName.toLowerCase().includes(searchLower)
        );
      }

      if (filters.type) {
        filtered = filtered.filter((r) => r.dtgrisktype === filters.type);
      }

      if (filters.status) {
        filtered = filtered.filter((r) => r.dtgriskstatus === filters.status);
      }

      setRisks(filtered as Array<DataGovernanceDatasetRisk & { datasetName: string }>);
    } catch (error) {
      console.error("Error loading risks:", error);
    } finally {
      setLoading(false);
    }
  };

  const getRiskScoreColor = (score?: number) => {
    if (!score) return "bg-gray-100 text-gray-800";
    if (score >= 0.8) return "bg-red-100 text-red-800";
    if (score >= 0.6) return "bg-yellow-100 text-yellow-800";
    return "bg-green-100 text-green-800";
  };

  const getRiskTypeColor = (type: string) => {
    const colors: Record<string, string> = {
      QUALITY: "bg-blue-100 text-blue-800",
      BIAS: "bg-purple-100 text-purple-800",
      SECURITY: "bg-red-100 text-red-800",
      PRIVACITY: "bg-orange-100 text-orange-800",
      COMPLIANCE: "bg-yellow-100 text-yellow-800",
      LEGAL: "bg-indigo-100 text-indigo-800",
    };
    return colors[type] || "bg-gray-100 text-gray-800";
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.risks.title", "Gestión de Riesgos de Datos")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.risks.description", "Identificación, evaluación y mitigación de riesgos en datasets")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/dashboard")}>
          Ver Dashboard
        </Button>
      </div>

      {/* Filtros */}
      <Card>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
            <div className="md:col-span-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Buscar riesgos..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={filters.type} onValueChange={(value) => setFilters({ ...filters, type: value })}>
              <SelectTrigger>
                <SelectValue placeholder="Tipo de Riesgo" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="QUALITY">Calidad</SelectItem>
                <SelectItem value="BIAS">Sesgos</SelectItem>
                <SelectItem value="SECURITY">Seguridad</SelectItem>
                <SelectItem value="PRIVACITY">Privacidad</SelectItem>
                <SelectItem value="COMPLIANCE">Compliance</SelectItem>
                <SelectItem value="LEGAL">Legal</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filters.status} onValueChange={(value) => setFilters({ ...filters, status: value })}>
              <SelectTrigger>
                <SelectValue placeholder="Estado" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="IDENTIFIED">Identificado</SelectItem>
                <SelectItem value="ASSESSED">Evaluado</SelectItem>
                <SelectItem value="TREATED">Tratado</SelectItem>
                <SelectItem value="MONITORED">Monitoreado</SelectItem>
                <SelectItem value="CLOSED">Cerrado</SelectItem>
              </SelectContent>
            </Select>
            <Button variant="outline" onClick={() => setFilters({ type: "", status: "", probability: "", impact: "" })}>
              Limpiar
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Lista de Riesgos */}
      <div className="space-y-4">
        {loading ? (
          <div className="text-center py-8">Cargando riesgos...</div>
        ) : risks.length === 0 ? (
          <Card>
            <CardBody className="text-center py-8">
              <p className="text-muted-foreground">No se encontraron riesgos</p>
            </CardBody>
          </Card>
        ) : (
          risks.map((risk) => (
            <Card key={risk.idxrisk} className="border-l-4 border-l-orange-500">
              <CardBody>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      <Badge className={getRiskTypeColor(risk.dtgrisktype)}>
                        {risk.dtgrisktype}
                      </Badge>
                      <h4 className="font-semibold">{risk.dtgriskname}</h4>
                      {risk.dtgriskscore && (
                        <Badge className={getRiskScoreColor(risk.dtgriskscore)}>
                          Score: {(risk.dtgriskscore * 100).toFixed(0)}%
                        </Badge>
                      )}
                    </div>
                    <p className="text-sm text-muted-foreground mb-2">
                      Dataset: <span className="font-medium">{risk.datasetName}</span>
                    </p>
                    <p className="text-sm text-muted-foreground mb-3">{risk.dtgriskdescription}</p>
                    <div className="flex gap-4 text-sm">
                      <div>
                        <span className="text-muted-foreground">Probabilidad:</span>
                        <Badge variant="outline" className="ml-2">{risk.dtgriskprobability}</Badge>
                      </div>
                      <div>
                        <span className="text-muted-foreground">Impacto:</span>
                        <Badge variant="outline" className="ml-2">{risk.dtgriskimpact}</Badge>
                      </div>
                      <div>
                        <span className="text-muted-foreground">Estado:</span>
                        <Badge variant="outline" className="ml-2">{risk.dtgriskstatus}</Badge>
                      </div>
                    </div>
                  </div>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => router.push(`/governance/data/datasets/${risk.idxdataset}#risks`)}
                  >
                    <Eye className="h-4 w-4 mr-2" />
                    Ver Dataset
                  </Button>
                </div>
              </CardBody>
            </Card>
          ))
        )}
      </div>
    </div>
  );
}
