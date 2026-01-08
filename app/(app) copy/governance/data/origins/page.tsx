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
import { Plus, Search, Eye, Edit, Trash2, Globe, Database, Link } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

import { DataGovernanceOrigin } from "../types/data-governance";

interface DataOriginListResponse {
  origins: DataGovernanceOrigin[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export default function DataOriginsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [origins, setOrigins] = useState<DataGovernanceOrigin[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filters, setFilters] = useState({
    type: "",
    status: "",
  });

  useEffect(() => {
    loadOrigins();
  }, [filters, searchTerm]);

  const loadOrigins = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams({
        page: "0",
        size: "20",
        ...(filters.type && { type: filters.type }),
        ...(filters.status && { status: filters.status }),
        ...(searchTerm && { search: searchTerm }),
      });

      const response = await fetch(`/api/v1/governance/data/origins?${params}`);
      if (response.ok) {
        const data: DataOriginListResponse = await response.json();
        setOrigins(data.origins);
      } else {
        // Si falla, usar datos vacíos
        setOrigins([]);
      }
    } catch (error) {
      console.error("Error loading origins:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: "bg-green-100 text-green-800",
      INACTIVE: "bg-gray-100 text-gray-800",
      DEPRECATED: "bg-yellow-100 text-yellow-800",
      ERROR: "bg-red-100 text-red-800",
    };
    return colors[status] || "bg-gray-100 text-gray-800";
  };

  const getTypeIcon = (type: string) => {
    if (type === "EXTERNAL") {
      return <Globe className="h-4 w-4" />;
    }
    return <Database className="h-4 w-4" />;
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">
            {t("governance.data.origins.title", "Orígenes de Datos")}
          </h1>
          <p className="text-muted-foreground mt-1">
            {t("governance.data.origins.description", "Gestión de orígenes internos y externos de datos")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/origins/create")}>
          <Plus className="mr-2 h-4 w-4" />
          {t("governance.data.origins.create", "Crear Origen")}
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Filtros</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="md:col-span-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("governance.data.origins.search", "Buscar orígenes...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <Select value={filters.type} onValueChange={(value) => setFilters({ ...filters, type: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.origins.filters.type", "Tipo")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="INTERNAL">Interno</SelectItem>
                <SelectItem value="EXTERNAL">Externo</SelectItem>
              </SelectContent>
            </Select>
            <Select value={filters.status} onValueChange={(value) => setFilters({ ...filters, status: value })}>
              <SelectTrigger>
                <SelectValue placeholder={t("governance.data.origins.filters.status", "Estado")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="">Todos</SelectItem>
                <SelectItem value="ACTIVE">Activo</SelectItem>
                <SelectItem value="INACTIVE">Inactivo</SelectItem>
                <SelectItem value="ERROR">Error</SelectItem>
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
                  <TableHead>{t("governance.data.origins.columns.name", "Nombre")}</TableHead>
                  <TableHead>{t("governance.data.origins.columns.type", "Tipo")}</TableHead>
                  <TableHead>{t("governance.data.origins.columns.source", "Fuente")}</TableHead>
                  <TableHead>{t("governance.data.origins.columns.status", "Estado")}</TableHead>
                  <TableHead>{t("governance.data.origins.columns.lastSync", "Última Sincronización")}</TableHead>
                  <TableHead>{t("governance.data.origins.columns.actions", "Acciones")}</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {origins.length === 0 ? (
                  <TableRow>
                    <td colSpan={6} className="text-center py-8">
                      No hay orígenes disponibles
                    </td>
                  </TableRow>
                ) : (
                  origins.map((origin) => (
                    <TableRow key={origin.idxorigin}>
                      <TableCell><span className="font-medium">{origin.dtgorname}</span></TableCell>
                      <TableCell>
                        <Badge variant="outline" className="flex items-center gap-1 w-fit">
                          {getTypeIcon(origin.dtgortype)}
                          {origin.dtgortype}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          {origin.dtgorurl ? (
                            <a
                              href={origin.dtgorurl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="flex items-center gap-1 text-blue-600 hover:underline"
                            >
                              <Link className="h-3 w-3" />
                              {origin.dtgorcategory}
                            </a>
                          ) : (
                            <span>{origin.dtgorcategory}</span>
                          )}
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge className={getStatusBadge(origin.dtgorstatus)}>
                          {origin.dtgorstatus}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {origin.dtgorlastsynced ? (
                          <span className="text-sm">
                            {new Date(origin.dtgorlastsynced).toLocaleString()}
                          </span>
                        ) : (
                          <span className="text-muted-foreground">-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/governance/data/origins/${origin.idxorigin}`)}
                          >
                            <Eye className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/governance/data/origins/${origin.idxorigin}/edit`)}
                          >
                            <Edit className="h-4 w-4" />
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
