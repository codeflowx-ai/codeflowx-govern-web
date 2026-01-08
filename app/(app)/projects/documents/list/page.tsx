"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  FileText,
  Plus,
  Search,
  Filter,
  ArrowRight,
  FolderKanban,
  Download,
  User,
} from "lucide-react";
import Link from "next/link";

interface Document {
  id: number;
  name: string;
  description: string;
  projectName: string;
  size: number;
  uploadedBy: string;
  uploadedAt: string;
}

const mockDocuments: Document[] = [
  {
    id: 1,
    name: "Especificación de Requisitos",
    description: "Documento de especificación de requisitos del sistema",
    projectName: "Proyecto Alpha",
    size: 3145728,
    uploadedBy: "María García",
    uploadedAt: "2024-11-15",
  },
  {
    id: 2,
    name: "Plan de Proyecto",
    description: "Planificación y cronograma del proyecto",
    projectName: "Proyecto Beta",
    size: 1572864,
    uploadedBy: "Juan Pérez",
    uploadedAt: "2024-11-10",
  },
  {
    id: 3,
    name: "Informe de Progreso",
    description: "Informe mensual de progreso del proyecto",
    projectName: "Proyecto Alpha",
    size: 1048576,
    uploadedBy: "Carlos López",
    uploadedAt: "2024-11-20",
  },
];

const formatFileSize = (bytes: number): string => {
  if (bytes < 1024) return bytes + " B";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + " KB";
  return (bytes / (1024 * 1024)).toFixed(2) + " MB";
};

export default function DocumentsOverviewPage() {
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
            <FileText className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.documents.title", "Documentos de Proyectos")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("projects.documents.newDocument", "Nuevo Documento")}
          </Button>
        </div>

        <div className="flex gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input
              type="text"
              placeholder={t("projects.documents.search", "Buscar documentos...")}
              className="w-full pl-10 pr-4 py-2 bg-background/60 backdrop-blur-md border border-border/50 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <Button variant="outline">
            <Filter className="w-4 h-4 mr-2" />
            {t("common.filter", "Filtrar")}
          </Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockDocuments.map((document) => (
            <Card
              key={document.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{document.name}</CardTitle>
                    <p className="text-sm text-muted-foreground line-clamp-2">{document.description}</p>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground flex items-center gap-1">
                      <FolderKanban className="w-4 h-4" />
                      {t("projects.documents.project", "Proyecto")}
                    </span>
                    <span className="font-medium">{document.projectName}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground">{t("projects.documents.size", "Tamaño")}</span>
                    <span className="font-medium">{formatFileSize(document.size)}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground flex items-center gap-1">
                      <User className="w-4 h-4" />
                      {t("projects.documents.uploadedBy", "Subido por")}
                    </span>
                    <span className="font-medium">{document.uploadedBy}</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-muted-foreground">{t("projects.documents.uploadedAt", "Fecha")}</span>
                    <span className="font-medium">{new Date(document.uploadedAt).toLocaleDateString()}</span>
                  </div>
                </div>

                <div className="flex gap-2">
                  <Button variant="outline" size="sm" className="flex-1">
                    <Download className="w-4 h-4 mr-2" />
                    {t("projects.documents.download", "Descargar")}
                  </Button>
                  <Link href={`/projects/documents/${document.id}`}>
                    <Button variant="outline" size="sm" className="flex-1">
                      {t("common.details", "Detalles")}
                      <ArrowRight className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
