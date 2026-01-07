"use client";

import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useToast } from "@/components/ui/toast";

import { mockProjects, type Project } from "../_mock";
import { getLocalProjects, upsertLocalProject } from "../_store";

type ProjectType = Project["type"];

export default function ProjectsNewPage() {
  const router = useRouter();
  const { toast } = useToast();

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [type, setType] = useState<ProjectType>("model");
  const [budget, setBudget] = useState("100000");
  const [teamSize, setTeamSize] = useState("5");
  const [saving, setSaving] = useState(false);

  const nextId = useMemo(() => {
    const all = [...mockProjects, ...getLocalProjects()];
    const max = all.reduce((acc, p) => Math.max(acc, p.id), 0);
    return max + 1;
  }, []);

  const onCreate = async () => {
    const trimmedName = name.trim();
    if (!trimmedName) {
      toast({
        title: "Falta nombre",
        description: "Ingresa el nombre del proyecto.",
        variant: "warning",
      });
      return;
    }

    const now = new Date();
    const startDate = now.toISOString().slice(0, 10);
    const end = new Date(now);
    end.setMonth(end.getMonth() + 6);
    const endDate = end.toISOString().slice(0, 10);

    const parsedBudget = Number(budget);
    const parsedTeam = Number(teamSize);

    const project: Project = {
      id: nextId,
      name: trimmedName,
      description: description.trim() || "Proyecto creado desde la demo",
      type,
      status: "active",
      startDate,
      endDate,
      budget: Number.isFinite(parsedBudget) ? parsedBudget : 100000,
      spent: 0,
      teamSize: Number.isFinite(parsedTeam) ? parsedTeam : 5,
      progress: 0,
    };

    try {
      setSaving(true);
      upsertLocalProject(project);
      toast({
        title: "Proyecto creado",
        description: `Se creó el proyecto #${project.id}.`,
        variant: "success",
      });
      router.push(`/projects/${project.id}`);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Nuevo Proyecto</h1>
            <p className="text-muted-foreground mt-2">
              Demo: se guarda en tu navegador (localStorage).
            </p>
          </div>
          <Link href="/projects/list">
            <Button variant="outline">Volver</Button>
          </Link>
        </div>

        <Card className="max-w-3xl">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              Datos del proyecto <Badge variant="outline">ID #{nextId}</Badge>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm text-muted-foreground">Nombre</label>
                <Input value={name} onChange={(e) => setName(e.target.value)} />
              </div>
              <div className="space-y-2">
                <label className="text-sm text-muted-foreground">Tipo</label>
                <select
                  value={type}
                  onChange={(e) => setType(e.target.value as ProjectType)}
                  className="w-full px-3 py-2 border border-border rounded-lg bg-background"
                >
                  <option value="model">Modelo</option>
                  <option value="agent">Agente</option>
                  <option value="data">Datos</option>
                  <option value="compliance">Cumplimiento</option>
                  <option value="governance">Gobernanza</option>
                </select>
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm text-muted-foreground">Descripción</label>
              <Input
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <label className="text-sm text-muted-foreground">Presupuesto</label>
                <Input
                  value={budget}
                  onChange={(e) => setBudget(e.target.value)}
                />
              </div>
              <div className="space-y-2">
                <label className="text-sm text-muted-foreground">Tamaño del equipo</label>
                <Input
                  value={teamSize}
                  onChange={(e) => setTeamSize(e.target.value)}
                />
              </div>
            </div>

            <div className="flex gap-2 pt-2">
              <Button onClick={onCreate} disabled={saving}>
                {saving ? "Creando..." : "Crear"}
              </Button>
              <Link href="/projects/list">
                <Button variant="outline" disabled={saving}>
                  Cancelar
                </Button>
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
