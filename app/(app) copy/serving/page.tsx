"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Activity,
  AlertTriangle,
  BarChart3,
  CheckCircle,
  Clock,
  Database,
  Globe,
  Monitor,
  Server,
  Settings,
  TrendingUp,
  Users,
} from "lucide-react";

export default function ServingPage() {
  const { t } = useTranslation();
  const metrics = [
    {
      title: "Endpoints Activos",
      value: "24",
      change: "+12%",
      icon: Globe,
      color: "text-green-500",
    },
    {
      title: "Requests/min",
      value: "1,247",
      change: "+8%",
      icon: Activity,
      color: "text-blue-500",
    },
    {
      title: "Latencia Promedio",
      value: "45ms",
      change: "-15%",
      icon: Clock,
      color: "text-purple-500",
    },
    {
      title: "Uptime",
      value: "99.9%",
      change: "+0.1%",
      icon: CheckCircle,
      color: "text-green-500",
    },
  ];

  const endpoints = [
    {
      id: 1,
      name: "GPT-4 API",
      url: "https://api.videcoding.com/gpt4",
      status: "active",
      requests: 1247,
      latency: 45,
      uptime: 99.9,
    },
    {
      id: 2,
      name: "Claude API",
      url: "https://api.videcoding.com/claude",
      status: "active",
      requests: 892,
      latency: 52,
      uptime: 99.8,
    },
    {
      id: 3,
      name: "Embeddings API",
      url: "https://api.videcoding.com/embeddings",
      status: "active",
      requests: 2156,
      latency: 38,
      uptime: 99.9,
    },
  ];

  return (
    <div className="space-y-4 p-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("serving.title", "Model Serving")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("serving.subtitle", "Gestiona y monitorea tus endpoints de modelos")}
            </p>
          </div>
          <div className="flex gap-3">
            <Button variant="outline">
              <Settings className="w-4 h-4 mr-2" />
              {t("common.settings", "Configuración")}
            </Button>
            <Button>
              <Server className="w-4 h-4 mr-2" />
              {t("common.add", "Nuevo Endpoint")}
            </Button>
          </div>
        </div>

        {/* Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {metrics.map((metric, index) => {
            const IconComponent = metric.icon;
            return (
              <Card key={index}>
                <CardBody className="p-6">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-muted-foreground mb-1">{metric.title}</p>
                      <p className="text-3xl font-bold">{metric.value}</p>
                      <p className={`text-sm ${metric.color}`}>{metric.change}</p>
                    </div>
                    <IconComponent className={`w-8 h-8 ${metric.color}`} />
                  </div>
                </CardBody>
              </Card>
            );
          })}
        </div>

        {/* Tabs */}
        <Tabs defaultValue="endpoints" className="space-y-6">
          <TabsList className="flex space-x-1 p-1">
            <TabsTrigger value="endpoints" className="flex-1">Endpoints</TabsTrigger>
            <TabsTrigger value="monitoring" className="flex-1">Monitoring</TabsTrigger>
            <TabsTrigger value="resources" className="flex-1">Recursos</TabsTrigger>
          </TabsList>

          <TabsContent value="endpoints" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Globe className="w-5 h-5" />
                  Endpoints Activos
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-4">
                  {endpoints.map((endpoint) => (
                    <div key={endpoint.id} className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="flex items-center gap-4">
                        <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                        <div>
                          <h3 className="font-semibold">{endpoint.name}</h3>
                          <p className="text-sm text-muted-foreground">{endpoint.url}</p>
                        </div>
                      </div>
                      <div className="flex items-center gap-6">
                        <div className="text-center">
                          <p className="font-semibold">{endpoint.requests}</p>
                          <p className="text-xs text-muted-foreground">requests/min</p>
                        </div>
                        <div className="text-center">
                          <p className="font-semibold">{endpoint.latency}ms</p>
                          <p className="text-xs text-muted-foreground">latencia</p>
                        </div>
                        <div className="text-center">
                          <p className="font-semibold">{endpoint.uptime}%</p>
                          <p className="text-xs text-muted-foreground">uptime</p>
                        </div>
                        <Button size="sm" variant="ghost">
                          <Settings className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </CardBody>
            </Card>
          </TabsContent>

          <TabsContent value="monitoring" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Monitor className="w-5 h-5" />
                  Monitoreo en Tiempo Real
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="h-64 border rounded-lg flex items-center justify-center">
                  <p className="text-muted-foreground">Gráfico de monitoreo en tiempo real</p>
                </div>
              </CardBody>
            </Card>
          </TabsContent>

          <TabsContent value="resources" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Database className="w-5 h-5" />
                  Recursos del Sistema
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="p-4 border rounded-lg">
                    <h3 className="font-semibold mb-2">CPU</h3>
                    <p className="text-2xl font-bold text-green-500">45%</p>
                  </div>
                  <div className="p-4 border rounded-lg">
                    <h3 className="font-semibold mb-2">Memoria</h3>
                    <p className="text-2xl font-bold text-blue-500">67%</p>
                  </div>
                  <div className="p-4 border rounded-lg">
                    <h3 className="font-semibold mb-2">GPU</h3>
                    <p className="text-2xl font-bold text-purple-500">23%</p>
                  </div>
                </div>
              </CardBody>
            </Card>
          </TabsContent>
        </Tabs>
    </div>
  );
}


