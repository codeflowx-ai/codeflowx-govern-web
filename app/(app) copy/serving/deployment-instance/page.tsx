"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Plus, Search, Server, Edit, Trash2 } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function DeploymentInstanceOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [instancetypeFilter, setInstancetypeFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  // Mock data
  const [data] = useState([
    {
      id: 1,
      deploymentid: "DEP-001",
      instanceid: "INST-001",
      instancename: "Instance 1",
      instancetype: "TYPE1",
      status: "ACTIVE",
    },
    {
      id: 2,
      deploymentid: "DEP-002",
      instanceid: "INST-002",
      instancename: "Instance 2",
      instancetype: "TYPE2",
      status: "INACTIVE",
    },
    {
      id: 3,
      deploymentid: "DEP-003",
      instanceid: "INST-003",
      instancename: "Instance 3",
      instancetype: "TYPE1",
      status: "PENDING",
    },
  ]);

  const metrics = {
    total: data.length,
    active: data.filter((d) => d.status === "ACTIVE").length,
    pending: data.filter((d) => d.status === "PENDING").length,
    inactive: data.filter((d) => d.status === "INACTIVE").length,
  };

  const getStatusBadge = (status: string) => {
    const variants: Record<string, string> = {
      ACTIVE: "primary",
      INACTIVE: "secondary",
      PENDING: "outline",
      REJECTED: "danger",
    };
    return (
      <Badge variant={variants[status] as any}>
        {status}
      </Badge>
    );
  };

  return (
    <div className="space-y-4 p-6">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("serving.deploymentInstance.title", "Deployment Instance")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("serving.deploymentInstance.subtitle", "Gestión de instancias de despliegue")}
            </p>
          </div>
          <Link href="/serving/deployment-instance/detail">
            <Button className="gap-2">
              <Plus className="w-4 h-4" />
              {t("serving.deploymentInstance.register", "Registrar Deployment Instance")}
            </Button>
          </Link>
        </div>

        {/* Metrics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Total</p>
              <p className="text-3xl font-bold">{metrics.total}</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Activos</p>
              <p className="text-3xl font-bold">{metrics.active}</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Pendientes</p>
              <p className="text-3xl font-bold">{metrics.pending}</p>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground mb-2">Inactivos</p>
              <p className="text-3xl font-bold">{metrics.inactive}</p>
            </CardContent>
          </Card>
        </div>

        {/* Filters */}
        <Card>
          <CardContent className="p-4">
            <div className="flex flex-wrap gap-4 items-center">
              <div className="flex-1 min-w-[200px]">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                  <Input
                    placeholder={t("common.search", "Buscar...")}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
              <Select value={instancetypeFilter} onValueChange={setInstancetypeFilter}>
                <SelectTrigger className="w-[200px]">
                  <SelectValue placeholder="Filtrar por Instancetype" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="TYPE1">TIPO 1</SelectItem>
                  <SelectItem value="TYPE2">TIPO 2</SelectItem>
                </SelectContent>
              </Select>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-[200px]">
                  <SelectValue placeholder="Filtrar por Status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">TODOS</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVO</SelectItem>
                  <SelectItem value="INACTIVE">INACTIVO</SelectItem>
                  <SelectItem value="PENDING">PENDIENTE</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>

        {/* Table */}
        <Card>
          <CardHeader>
            <CardTitle>
              {t("serving.deploymentInstance.list", "Listado de Deployment Instances")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>Deployment Id</TableHead>
                  <TableHead>Instance ID</TableHead>
                  <TableHead>Instance Name</TableHead>
                  <TableHead>Instance Type</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Acciones</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {data.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>{item.id}</TableCell>
                    <TableCell>{item.deploymentid}</TableCell>
                    <TableCell>{item.instanceid}</TableCell>
                    <TableCell>{item.instancename}</TableCell>
                    <TableCell>{item.instancetype}</TableCell>
                    <TableCell>{getStatusBadge(item.status)}</TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Link href={`/serving/deployment-instance/detail?id=${item.id}`}>
                          <Button variant="ghost" size="sm">
                            <Edit className="w-4 h-4" />
                          </Button>
                        </Link>
                        <Button variant="ghost" size="sm">
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
    </div>
  );
}


