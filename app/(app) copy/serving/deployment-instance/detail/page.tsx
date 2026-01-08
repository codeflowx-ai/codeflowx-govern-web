"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ArrowLeft, Save, Server } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

export default function DeploymentInstanceDetailPage() {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    deploymentid: "",
    instanceid: "",
    instancename: "",
    instancetype: "",
    status: "",
    ipaddress: "",
    port: "",
    nodename: "",
    resourceusage: "",
    startedat: "",
    lastheartbeat: "",
    createdat: new Date().toISOString().split("T")[0],
    updatedat: new Date().toISOString().split("T")[0],
  });

  const handleSave = () => {
    // TODO: Implementar guardado
    console.log("Guardando:", formData);
  };

  return (
    <div className="space-y-4 p-6">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Link href="/serving/deployment-instance">
                <Button variant="ghost" size="sm">
                  <ArrowLeft className="w-4 h-4" />
                </Button>
              </Link>
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("serving.deploymentInstance.detail.title", "Deployment Instance")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("serving.deploymentInstance.detail.formTitle", "Información del Deployment Instance")}
            </p>
          </div>
          <Button onClick={handleSave} className="gap-2">
            <Save className="w-4 h-4" />
            {t("common.save", "Guardar")}
          </Button>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>
              {t("serving.deploymentInstance.detail.formTitle", "Información del Deployment Instance")}
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <Label htmlFor="deploymentid">
                  {t("serving.deploymentInstance.deploymentid", "Deployment Id")}
                </Label>
                <Input
                  id="deploymentid"
                  value={formData.deploymentid}
                  onChange={(e) =>
                    setFormData({ ...formData, deploymentid: e.target.value })
                  }
                  maxLength={255}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="instanceid">
                  {t("serving.deploymentInstance.instanceid", "Instance ID")} *
                </Label>
                <Input
                  id="instanceid"
                  value={formData.instanceid}
                  onChange={(e) =>
                    setFormData({ ...formData, instanceid: e.target.value })
                  }
                  maxLength={100}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="instancename">
                  {t("serving.deploymentInstance.instancename", "Instance Name")} *
                </Label>
                <Input
                  id="instancename"
                  value={formData.instancename}
                  onChange={(e) =>
                    setFormData({ ...formData, instancename: e.target.value })
                  }
                  maxLength={100}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="instancetype">
                  {t("serving.deploymentInstance.instancetype", "Instance Type")} *
                </Label>
                <Select
                  value={formData.instancetype}
                  onValueChange={(value) =>
                    setFormData({ ...formData, instancetype: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar tipo" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="TYPE1">Tipo 1</SelectItem>
                    <SelectItem value="TYPE2">Tipo 2</SelectItem>
                    <SelectItem value="TYPE3">Tipo 3</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="status">
                  {t("serving.deploymentInstance.status", "Status")} *
                </Label>
                <Select
                  value={formData.status}
                  onValueChange={(value) =>
                    setFormData({ ...formData, status: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar estado" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ACTIVE">Activo</SelectItem>
                    <SelectItem value="INACTIVE">Inactivo</SelectItem>
                    <SelectItem value="PENDING">Pendiente</SelectItem>
                    <SelectItem value="REJECTED">Rechazado</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="ipaddress">
                  {t("serving.deploymentInstance.ipaddress", "IP Address")}
                </Label>
                <Input
                  id="ipaddress"
                  value={formData.ipaddress}
                  onChange={(e) =>
                    setFormData({ ...formData, ipaddress: e.target.value })
                  }
                  maxLength={100}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="port">
                  {t("serving.deploymentInstance.port", "Port")}
                </Label>
                <Input
                  id="port"
                  value={formData.port}
                  onChange={(e) =>
                    setFormData({ ...formData, port: e.target.value })
                  }
                  maxLength={255}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="nodename">
                  {t("serving.deploymentInstance.nodename", "Node Name")}
                </Label>
                <Input
                  id="nodename"
                  value={formData.nodename}
                  onChange={(e) =>
                    setFormData({ ...formData, nodename: e.target.value })
                  }
                  maxLength={100}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="resourceusage">
                  {t("serving.deploymentInstance.resourceusage", "Resource Usage")}
                </Label>
                <Input
                  id="resourceusage"
                  value={formData.resourceusage}
                  onChange={(e) =>
                    setFormData({ ...formData, resourceusage: e.target.value })
                  }
                  maxLength={255}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="startedat">
                  {t("serving.deploymentInstance.startedat", "Started At")}
                </Label>
                <Input
                  id="startedat"
                  type="datetime-local"
                  value={formData.startedat}
                  onChange={(e) =>
                    setFormData({ ...formData, startedat: e.target.value })
                  }
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="lastheartbeat">
                  {t("serving.deploymentInstance.lastheartbeat", "Last Heartbeat")}
                </Label>
                <Input
                  id="lastheartbeat"
                  type="datetime-local"
                  value={formData.lastheartbeat}
                  onChange={(e) =>
                    setFormData({ ...formData, lastheartbeat: e.target.value })
                  }
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="createdat">
                  {t("serving.deploymentInstance.createdat", "Created At")} *
                </Label>
                <Input
                  id="createdat"
                  type="date"
                  value={formData.createdat}
                  onChange={(e) =>
                    setFormData({ ...formData, createdat: e.target.value })
                  }
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="updatedat">
                  {t("serving.deploymentInstance.updatedat", "Updated At")} *
                </Label>
                <Input
                  id="updatedat"
                  type="date"
                  value={formData.updatedat}
                  onChange={(e) =>
                    setFormData({ ...formData, updatedat: e.target.value })
                  }
                  required
                />
              </div>
            </div>
          </CardContent>
        </Card>
    </div>
  );
}


