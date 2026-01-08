"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Plus, Search, Eye, Edit, Trash2, FileCheck, BarChart3 } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { DataGovernanceDataset, DataGovernanceDatasetListResponse } from "../../types/data-governance";

export default function DatasetsOverviewPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [datasets, setDatasets] = useState<DataGovernanceDataset[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filters, setFilters] = useState({
    type: "",
    originType: "",
    status: "",
    standardized: "",
    project: "",
  });

  useEffect(() => {
    loadDatasets();
  }, [filters, searchTerm]);

  const loadDatasets = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams({
        page: "0",
        size: "20",
        ...(filters.type && { type: filters.type }),
        ...(filters.originType && { originType: filters.originType }),
        ...(filters.status && { status: filters.status }),
        ...(filters.standardized && { standardized: filters.standardized }),
        ...(searchTerm && { search: searchTerm }),
      });

      const response = await fetch(`/api/v1/governance/data/datasets?${params}`);
      if (response.ok) {
        const data: DataGovernanceDatasetListResponse = await response.json();
        setDatasets(data.datasets);
      }
    } catch (error) {
      console.error("Error loading datasets:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleStandardize = async (datasetId: number) => {
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/standardize`, {
        method: "POST",
      });
      if (response.ok) {
        loadDatasets();
      }
    } catch (error) {
      console.error("Error standardizing dataset:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      DRAFT: "bg-gray-100 text-gray-800",
      VALIDATED: "bg-blue-100 text-blue-800",
      APPROVED: "bg-green-100 text-green-800",
      ACTIVE: "bg-green-100 text-green-800",
      ARCHIVED: "bg-gray-100 text-gray-800",
    };
    return colors[status] || "bg-gray-100 text-gray-800";
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">
            {t("governance.data.datasets.overview.title", "Listado de Datasets")}
          </h1>
          <p className="text-muted-foreground mt-1">
            {t("governance.data.datasets.description", "Gestión de datasets con formato estándar Apache Parquet")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/datasets/create")}>
          <Plus className="mr-2 h-4 w-4" />
          {t("governance.data.datasets.overview.create", "Crear Dataset")}
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Filtros</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-6 gap-4">
            <div className="md:col-span-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("governance.data.datasets.overview.search", "Buscar datasets...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={filters.type} onValueChange={(value) => setFilters({ ...filters, type: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.datasets.overview.filters.type", "Tipo")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="TRAINING">Training</SelectItem>
                <SelectItem value="VALIDATION">Validation</SelectItem>
                <SelectItem value="TEST">Test</SelectItem>
                <SelectItem value="PRODUCTION">Production</SelectItem>
                <SelectItem value="RAG">RAG</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filters.originType} onValueChange={(value) => setFilters({ ...filters, originType: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.datasets.overview.filters.originType", "Origen")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="INTERNAL">Interno</SelectItem>
                <SelectItem value="EXTERNAL">Externo</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filters.status} onValueChange={(value) => setFilters({ ...filters, status: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.datasets.overview.filters.status", "Estado")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="DRAFT">Draft</SelectItem>
                <SelectItem value="VALIDATED">Validated</SelectItem>
                <SelectItem value="APPROVED">Approved</SelectItem>
                <SelectItem value="ACTIVE">Active</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filters.standardized} onValueChange={(value) => setFilters({ ...filters, standardized: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.datasets.overview.filters.standardized", "Estandarizado")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="true">Sí</SelectItem>
                <SelectItem value="false">No</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardBody>
      </Card>

      <Card>
        <CardBody>
          {loading ? (
            <div className="text-center py-8">Cargando...</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>{t("governance.data.datasets.overview.columns.name", "Nombre")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.type", "Tipo")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.origin", "Origen")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.quality", "Calidad")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.bias", "Sesgos")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.status", "Estado")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.standardized", "Estandarizado")}</TableHead>
                  <TableHead>{t("governance.data.datasets.overview.columns.actions", "Acciones")}</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {datasets.length === 0 ? (
                  <TableRow>
                    <td colSpan={8} className="text-center py-8">
                      No hay datasets disponibles
                    </td>
                  </TableRow>
                ) : (
                  datasets.map((dataset) => (
                    <TableRow key={dataset.idxdataset}>
                      <TableCell><span className="font-medium">{dataset.dtgname}</span></TableCell>
                      <TableCell>
                        <Badge variant="outline">{dataset.dtgtype}</Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{dataset.dtgorigintype}</Badge>
                      </TableCell>
                      <TableCell>
                        {dataset.dtgqualityscore ? (
                          <span className="text-sm">
                            {(dataset.dtgqualityscore * 100).toFixed(0)}%
                          </span>
                        ) : (
                          <span className="text-muted-foreground">-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        {dataset.dtgbiasscore ? (
                          <span className="text-sm">
                            {(dataset.dtgbiasscore * 100).toFixed(0)}%
                          </span>
                        ) : (
                          <span className="text-muted-foreground">-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <Badge className={getStatusBadge(dataset.dtgstatus)}>
                          {dataset.dtgstatus}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {dataset.dtgstandardized ? (
                          <Badge className="bg-green-100 text-green-800">
                            <FileCheck className="mr-1 h-3 w-3" />
                            Parquet
                          </Badge>
                        ) : (
                          <Badge variant="outline">No</Badge>
                        )}
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/governance/data/datasets/${dataset.idxdataset}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          {!dataset.dtgstandardized && (
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleStandardize(dataset.idxdataset)}
                            >
                              <FileCheck className="h-4 w-4" />
                            </Button>
                          )}
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/governance/data/datasets/${dataset.idxdataset}/analyze`)}
                          >
                            <BarChart3 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}
        </CardBody>
      </Card>
    </div>
  );
}
