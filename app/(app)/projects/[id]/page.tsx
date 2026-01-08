/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";

import Link from "next/link";
import { useMemo } from "react";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";

import { mockProjects } from "../_mock";
import { getLocalProjects } from "../_store";

function getStatusVariant(status: string) {
  switch (status) {
    case "active":
      return "primary";
    case "completed":
      return "secondary";
    case "on-hold":
      return "outline";
    case "cancelled":
      return "danger";
    default:
      return "outline";
  }
}

function getTypeLabel(type: string) {
  switch (type) {
    case "model":
      return "Modelo";
    case "agent":
      return "Agente";
    case "data":
      return "Datos";
    case "compliance":
      return "Cumplimiento";
    case "governance":
      return "Gobernanza";
    default:
      return type;
  }
}

export default function ProjectDetailPage({ params }: { params: { id: string } }) {
  const projectId = Number(params.id);
  const project = useMemo(() => {
    if (!Number.isFinite(projectId)) return null;
    const local = getLocalProjects();
    return [...mockProjects, ...local].find((p) => p.id === projectId) ?? null;
  }, [projectId]);

  if (!project) {
    return (
      <div className="w-full px-4 py-6 space-y-6">
        <h1 className="text-3xl font-bold text-foreground">Proyecto no encontrado</h1>
        <p className="text-muted-foreground">
          No existe un proyecto con ID {params.id}.
        </p>
        <Link href="/projects/list">
          <Button variant="outline">Volver a Proyectos</Button>
        </Link>
      </div>
    );
  }

  const remaining = Math.max(project.budget - project.spent, 0);

  return (
    <div className="w-full px-4 py-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">{project.name}</h1>
          <p className="text-gray-600 mt-2">{project.description}</p>
        </div>
        <Link href="/projects/list">
          <Button variant="outline">Volver a Proyectos</Button>
        </Link>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle>Resumen</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex flex-wrap items-center gap-2">
              <Badge variant="outline">{getTypeLabel(project.type)}</Badge>
              <Badge variant={getStatusVariant(project.status) as any}>
                {project.status}
              </Badge>
              <Badge variant="outline">Equipo: {project.teamSize}</Badge>
              {project.modelsCount != null && (
                <Badge variant="outline">Modelos: {project.modelsCount}</Badge>
              )}
              {project.agentsCount != null && (
                <Badge variant="outline">Agentes: {project.agentsCount}</Badge>
              )}
            </div>

            <div className="space-y-2">
              <div className="flex items-center justify-between text-sm">
                <span className="text-gray-600">Progreso</span>
                <span className="font-medium">{project.progress}%</span>
              </div>
              <Progress value={project.progress} className="h-2" />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
              <div>
                <span className="text-gray-600">Inicio:</span>
                <span className="ml-2 font-medium">{project.startDate}</span>
              </div>
              <div>
                <span className="text-gray-600">Fin:</span>
                <span className="ml-2 font-medium">{project.endDate}</span>
              </div>
              <div>
                <span className="text-gray-600">Presupuesto:</span>
                <span className="ml-2 font-medium">
                  {project.budget.toLocaleString("es-ES")}
                </span>
              </div>
              <div>
                <span className="text-gray-600">Gastado:</span>
                <span className="ml-2 font-medium">
                  {project.spent.toLocaleString("es-ES")}
                </span>
              </div>
              <div>
                <span className="text-gray-600">Disponible:</span>
                <span className="ml-2 font-medium">
                  {remaining.toLocaleString("es-ES")}
                </span>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Accesos rápidos</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <Link href={`/projects/model-performance-overview?projectId=${project.id}`}>
              <Button variant="outline" className="w-full">
                Performance de Modelos
              </Button>
            </Link>
            <Link href={`/projects/ai-governance-overview?projectId=${project.id}`}>
              <Button variant="outline" className="w-full">
                AI Governance Overview
              </Button>
            </Link>
            <Link href={`/projects/compliance-status?projectId=${project.id}`}>
              <Button variant="outline" className="w-full">
                Estado de Cumplimiento
              </Button>
            </Link>
            <Link href={`/projects/risk-assessment?projectId=${project.id}`}>
              <Button variant="outline" className="w-full">
                Risk Assessment
              </Button>
            </Link>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
