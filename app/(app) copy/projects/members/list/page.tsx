"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Users,
  Plus,
  Search,
  Filter,
  ArrowRight,
  FolderKanban,
  UserCircle,
} from "lucide-react";
import Link from "next/link";

interface Member {
  id: number;
  name: string;
  email: string;
  role: string;
  projectName: string;
  allocation: number;
}

const mockMembers: Member[] = [
  {
    id: 1,
    name: "María García",
    email: "maria.garcia@example.com",
    role: "Desarrollador Senior",
    projectName: "Proyecto Alpha",
    allocation: 100,
  },
  {
    id: 2,
    name: "Juan Pérez",
    email: "juan.perez@example.com",
    role: "Project Manager",
    projectName: "Proyecto Beta",
    allocation: 50,
  },
  {
    id: 3,
    name: "Carlos López",
    email: "carlos.lopez@example.com",
    role: "Arquitecto",
    projectName: "Proyecto Alpha",
    allocation: 75,
  },
];

export default function MembersOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Users className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.members.title", "Miembros de Proyectos")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("projects.members.newMember", "Nuevo Miembro")}
          </Button>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("projects.members.search", "Buscar miembros...")}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockMembers.map((member) => (
            <Card
              key={member.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2 flex items-center gap-2">
                      <UserCircle className="w-5 h-5 text-primary" />
                      {member.name}
                    </CardTitle>
                    <p className="text-sm text-muted-foreground">{member.email}</p>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground">{t("projects.members.role", "Rol")}</span>
                    <Badge variant="outline">{member.role}</Badge>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground flex items-center gap-1">
                      <FolderKanban className="w-4 h-4" />
                      {t("projects.members.project", "Proyecto")}
                    </span>
                    <span className="font-medium">{member.projectName}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground">{t("projects.members.allocation", "Asignación")}</span>
                    <span className="font-medium">{member.allocation}%</span>
                  </div>
                </div>

                <Link href={`/projects/members/${member.id}`}>
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


