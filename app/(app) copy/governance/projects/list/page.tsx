"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  FolderKanban,
  Plus,
  Search,
  Filter,
  ArrowRight,
  Calendar,
  Users,
  DollarSign,
} from "lucide-react";
import Link from "next/link";
interface Project {
  id: number;
  name: string;
  description: string;
  status: "active" | "completed" | "on-hold" | "cancelled";
  startDate: string;
  endDate: string;
  budget: number;
  spent: number;
  teamSize: number;
  progress: number;
}
const mockProjects: Project[] = [
  {
    id: 1,
    name: "Proyecto Alpha",
    description: "Sistema de gestión empresarial",
    status: "active",
    startDate: "2024-01-15",
    endDate: "2024-12-31",
    budget: 500000,
    spent: 325000,
    teamSize: 12,
    progress: 65,
  },
  {
    id: 2,
    name: "Proyecto Beta",
    description: "Plataforma de e-commerce",
    status: "active",
    startDate: "2024-03-01",
    endDate: "2025-02-28",
    budget: 750000,
    spent: 180000,
    teamSize: 18,
    progress: 24,
  },
  {
    id: 3,
    name: "Proyecto Gamma",
    description: "Aplicación móvil",
    status: "completed",
    startDate: "2023-06-01",
    endDate: "2024-05-31",
    budget: 300000,
    spent: 285000,
    teamSize: 8,
    progress: 100,
  },
];
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
export default function ProjectsOverviewPage() {
  const { t } = useTranslation();
  return (
    <div className="min-h-screen w-full bg-background">
      <div className="relative z-10">
      </div>
      <div className="relative z-10 w-full px-6 px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <FolderKanban className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("governance.projects.overview.title", "Proyectos de Cumplimiento Normativo")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("governance.projects.overview.newProject", "Nuevo Proyecto")}
          </Button>
        </div>
        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("governance.projects.overview.search", "Buscar proyectos...")}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockProjects.map((project) => (
            <Card
              key={project.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{project.name}</CardTitle>
                    <p className="text-sm text-muted-foreground line-clamp-2">{project.description}</p>
                  </div>
                  <Badge variant={getStatusColor(project.status) as any}>
                    {t(`governance.projects.status.${project.status}`, project.status)}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-muted-foreground">{t("governance.projects.overview.progress", "Progreso")}</span>
                    <span className="font-medium">{project.progress}%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${project.progress}%` }}
                    />
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div className="flex items-center gap-2">
                    <Users className="w-4 h-4 text-muted-foreground" />
                    <span>{project.teamSize} {t("governance.projects.overview.members", "miembros")}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <DollarSign className="w-4 h-4 text-muted-foreground" />
                    <span>${(project.spent / 1000).toLocaleString()}K</span>
                  </div>
                </div>
                <Link href={`/governance/projects/${project.id}`}>
                  <Button variant="outline" className="w-full">
                    {t("common.details", "Detalles")}
                    <ArrowRight className="w-4 h-4 ml-2" />
                  </Button>
                </Link>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}


