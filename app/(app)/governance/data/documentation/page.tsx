"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { FileText, Search, Plus, Eye } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { DataGovernanceDatasetDocumentation } from "../types/data-governance";

export default function DocumentationOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [docs, setDocs] = useState<Array<DataGovernanceDatasetDocumentation & { datasetName: string }>>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterType, setFilterType] = useState("");

  useEffect(() => {
    loadDocumentation();
  }, [searchTerm, filterType]);

  const loadDocumentation = async () => {
    try {
      setLoading(true);
      // Mock data
      const mockDocs: Array<DataGovernanceDatasetDocumentation & { datasetName: string }> = [
        {
          idxdoc: 1,
          idxdataset: 1,
          datasetName: "Customer Dataset v1.0",
          dtgdocumenttype: "DECISION" as DataGovernanceDatasetDocumentation["dtgdocumenttype"],
          dtgdocumenttitle: "Decisión: Aprobación para uso en producción",
          dtgdocumentcontent: "Se aprueba el uso de este dataset en producción...",
          dtgdocumentauthor: 1,
          dtgdocumentdate: "2025-01-13T15:00:00Z",
          dtgdocumentversion: "1.0",
          dtgdocumentattachments: [],
          dtgdocumenttags: "aprobación, producción",
          dtgdocumentstatus: "PUBLISHED",
        },
        {
          idxdoc: 2,
          idxdataset: 1,
          datasetName: "Customer Dataset v1.0",
          dtgdocumenttype: "TRANSFORMATION" as DataGovernanceDatasetDocumentation["dtgdocumenttype"],
          dtgdocumenttitle: "Transformación: Filtrado por edad",
          dtgdocumentcontent: "Se aplicó un filtro para incluir solo registros con edad > 18...",
          dtgdocumentauthor: 1,
          dtgdocumentdate: "2025-01-10T08:00:00Z",
          dtgdocumentversion: "1.0",
          dtgdocumentattachments: [],
          dtgdocumenttags: "transformación",
          dtgdocumentstatus: "PUBLISHED",
        },
      ];

      let filtered = mockDocs;

      if (searchTerm) {
        const searchLower = searchTerm.toLowerCase();
        filtered = filtered.filter(
          (d) =>
            d.dtgdocumenttitle.toLowerCase().includes(searchLower) ||
            d.dtgdocumentcontent.toLowerCase().includes(searchLower) ||
            d.datasetName.toLowerCase().includes(searchLower)
        );
      }

      if (filterType) {
        filtered = filtered.filter((d) => d.dtgdocumenttype === filterType);
      }

      setDocs(filtered);
    } catch (error) {
      console.error("Error loading documentation:", error);
    } finally {
      setLoading(false);
    }
  };

  const typeNames: Record<string, string> = {
    DECISION: "Decisión",
    TRANSFORMATION: "Transformación",
    APPROVAL: "Aprobación",
    REJECTION: "Rechazo",
    CHANGE: "Cambio",
    INCIDENT: "Incidente",
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.documentation.title", "Documentación y Trazabilidad")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.documentation.description", "Historial de decisiones y transformaciones")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/dashboard")}>
          Ver Dashboard
        </Button>
      </div>

      {/* Filtros */}
      <Card>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="md:col-span-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Buscar documentación..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={filterType} onValueChange={setFilterType}>
              <SelectTrigger>
                <SelectValue placeholder="Tipo de Documentación" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="DECISION">Decisión</SelectItem>
                <SelectItem value="TRANSFORMATION">Transformación</SelectItem>
                <SelectItem value="APPROVAL">Aprobación</SelectItem>
                <SelectItem value="REJECTION">Rechazo</SelectItem>
                <SelectItem value="CHANGE">Cambio</SelectItem>
                <SelectItem value="INCIDENT">Incidente</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardBody>
      </Card>

      {/* Lista de Documentación */}
      <div className="space-y-4">
        {loading ? (
          <div className="text-center py-8">Cargando documentación...</div>
        ) : docs.length === 0 ? (
          <Card>
            <CardBody className="text-center py-8">
              <p className="text-muted-foreground">No se encontró documentación</p>
            </CardBody>
          </Card>
        ) : (
          docs.map((doc) => (
            <Card key={doc.idxdoc} className="border-l-4 border-l-green-500">
              <CardBody>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-2">
                      <Badge variant="outline">
                        {typeNames[doc.dtgdocumenttype] || doc.dtgdocumenttype}
                      </Badge>
                      <h4 className="font-semibold">{doc.dtgdocumenttitle}</h4>
                      {doc.dtgdocumentversion && (
                        <Badge variant="outline" className="text-xs">
                          v{doc.dtgdocumentversion}
                        </Badge>
                      )}
                    </div>
                    <p className="text-sm text-muted-foreground mb-2">
                      Dataset: <span className="font-medium">{doc.datasetName}</span>
                    </p>
                    <p className="text-sm mb-3 line-clamp-2">{doc.dtgdocumentcontent}</p>
                    <div className="flex items-center gap-4 text-xs text-muted-foreground">
                      <span>{new Date(doc.dtgdocumentdate).toLocaleString()}</span>
                      {doc.dtgdocumenttags && (
                        <div className="flex gap-1">
                          {doc.dtgdocumenttags.split(", ").map((tag) => (
                            <Badge key={tag} variant="outline" className="text-xs">
                              {tag}
                            </Badge>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => router.push(`/governance/data/datasets/${doc.idxdataset}#documentation`)}
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
