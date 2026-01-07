"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Bot,
  Brain,
  Database,
  Eye,
  Filter,
  FolderKanban,
  Plus,
  Search,
  Shield,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useEffect, useMemo, useState } from "react";

import { mockProjects } from "../_mock";
import { getLocalProjects } from "../_store";

const getStatusColor = (status: string) => {
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
};

const getTypeIcon = (type: string) => {
  switch (type) {
    case "model":
      return Brain;
    case "agent":
      return Bot;
    case "data":
      return Database;
    case "compliance":
      return Shield;
    case "governance":
      return TrendingUp;
    default:
      return FolderKanban;
  }
};

const getTypeLabel = (type: string) => {
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
      return "Gobierno";
    default:
      return type;
  }
};

export default function ProjectsOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [projects, setProjects] = useState(() => mockProjects);

  useEffect(() => {
    // merge: mock + localStorage (demo)
    const local = getLocalProjects();
    if (local.length > 0) {
      const merged = [...mockProjects, ...local].sort((a, b) => a.id - b.id);
      setProjects(merged);
    }
  }, []);

  const filteredProjects = useMemo(() => {
    const q = searchTerm.trim().toLowerCase();
    if (!q) return projects;
    return projects.filter((p) => {
      const haystack = `${p.name} ${p.description}`.toLowerCase();
      return haystack.includes(q);
    });
  }, [projects, searchTerm]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <FolderKanban className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.overview.title", "Proyectos")}
            </h1>
          </div>
          <Link href="/projects/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("projects.overview.newProject", "Nuevo Proyecto")}
            </Button>
          </Link>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("projects.overview.search", "Buscar proyectos...")}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="p-0">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.name", "Nombre del Proyecto")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.type", "Tipo")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.status", "Estado")}
                    </th>
                    <th className="px-6 py-4 text-left text-sm font-semibold text-foreground">
                      {t("projects.overview.progress", "Progreso")}
                    </th>
                    <th className="px-6 py-4 text-center text-sm font-semibold text-foreground">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredProjects.map((project) => {
                    const TypeIcon = getTypeIcon(project.type);
                    return (
                      <tr
                        key={project.id}
                        className="border-b border-border/30 hover:bg-background/40 transition-colors"
                      >
                        <td className="px-6 py-4">
                          <div className="flex flex-col">
                            <span className="font-medium text-foreground">{project.name}</span>
                            <span className="text-sm text-muted-foreground line-clamp-1 max-w-md">
                              {project.description}
                            </span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2">
                            <TypeIcon className="w-4 h-4 text-primary" />
                            <Badge variant="outline">{getTypeLabel(project.type)}</Badge>
                            {project.modelsCount && (
                              <span className="text-xs text-muted-foreground">
                                {project.modelsCount} {t("projects.overview.models", "modelos")}
                              </span>
                            )}
                            {project.agentsCount && (
                              <span className="text-xs text-muted-foreground">
                                {project.agentsCount} {t("projects.overview.agents", "agentes")}
                              </span>
                            )}
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <Badge variant={getStatusColor(project.status) as any}>
                            {t(`projects.status.${project.status}`, project.status)}
                          </Badge>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2 min-w-[120px]">
                            <div className="flex-1 bg-muted rounded-full h-2">
                              <div
                                className="bg-primary h-2 rounded-full transition-all duration-500"
                                style={{ width: `${project.progress}%` }}
                              />
                            </div>
                            <span className="text-sm font-medium w-12 text-right">{project.progress}%</span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center justify-center gap-2">
                            <Link href={`/projects/${project.id}`}>
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                <Eye className="w-4 h-4" />
                              </Button>
                            </Link>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
