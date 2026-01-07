"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Ban, Search, Shield, CheckCircle, XCircle, X, FileText } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface ProhibitedSystemCatalog {
  id: number;
  name: string;
  category: string;
  description: string;
  keywords: string[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

const mockCatalog: ProhibitedSystemCatalog[] = [
  {
    id: 1,
    name: "Social Scoring by Public Authorities",
    category: "ART_5_1_C",
    description: "AI systems for social scoring by public authorities",
    keywords: ["social scoring", "public authority", "citizen rating", "behavioral scoring"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 2,
    name: "Real-time Remote Biometric Identification",
    category: "ART_5_1_D",
    description: "Real-time remote biometric identification in publicly accessible spaces",
    keywords: ["biometric", "facial recognition", "real-time", "public space", "surveillance"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 3,
    name: "Subliminal Manipulation",
    category: "ART_5_1_A",
    description: "AI systems using subliminal techniques to distort behavior",
    keywords: ["subliminal", "manipulation", "behavioral distortion"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 4,
    name: "Vulnerability Exploitation",
    category: "ART_5_1_B",
    description: "AI systems exploiting vulnerabilities of specific groups",
    keywords: ["vulnerability", "exploitation", "targeted groups"],
    active: false,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
];

const categoryLabels: Record<string, string> = {
  ART_5_1_A: "Art. 5.1.a",
  ART_5_1_B: "Art. 5.1.b",
  ART_5_1_C: "Art. 5.1.c",
  ART_5_1_D: "Art. 5.1.d",
};

export default function ProhibitedSystemsCatalogPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [catalog, setCatalog] = useState<ProhibitedSystemCatalog[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterCategory, setFilterCategory] = useState<string>("ALL");
  const [filterActive, setFilterActive] = useState<string>("ALL");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState({
    name: "",
    category: "ART_5_1_A",
    description: "",
    keywords: "",
    active: true,
  });

  useEffect(() => {
    loadCatalog();
  }, []);

  const loadCatalog = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));
      setCatalog(mockCatalog);
    } catch (error) {
      console.error("Error loading catalog:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredCatalog = catalog.filter((item) => {
    const matchesSearch =
      item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.keywords.some((kw) => kw.toLowerCase().includes(searchTerm.toLowerCase()));
    const matchesCategory = filterCategory === "ALL" || item.category === filterCategory;
    const matchesActive =
      filterActive === "ALL" ||
      (filterActive === "ACTIVE" && item.active) ||
      (filterActive === "INACTIVE" && !item.active);
    return matchesSearch && matchesCategory && matchesActive;
  });

  const handleToggleActive = async (id: number) => {
    setCatalog(
      catalog.map((item) =>
        item.id === id ? { ...item, active: !item.active } : item
      )
    );
  };


  const handleView = (id: number) => {
    const item = catalog.find((i) => i.id === id);
    if (item) {
      setFormData({
        name: item.name,
        category: item.category,
        description: item.description,
        keywords: item.keywords.join(", "),
        active: item.active,
      });
      setEditingId(id);
    }
  };



  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-red-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-red-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-red-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          {/* Título y descripción a la izquierda */}
          <div className="flex-1 space-y-2">
            <div className="flex items-center gap-3">
              <Shield className="w-8 h-8 text-red-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-red-500 to-red-700 bg-clip-text text-transparent">
                {t("governance.prohibitedSystems.catalogTitle")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.prohibitedSystems.catalogSubtitle")}
            </p>
          </div>
          {/* Botones de acción a la derecha */}
          <div className="flex items-center gap-4">
            <Button
              onClick={() => {
                if (typeof window !== "undefined") {
                  window.location.href = "/governance/compliance/prohibited-systems";
                }
              }}
              variant="outline"
              className="backdrop-blur-md bg-background/60"
            >
              {t("governance.prohibitedSystems.backToVerification")}
            </Button>
          </div>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody>
            <div className="flex flex-wrap items-center gap-4">
              <div className="relative flex-1 min-w-[200px]">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  type="text"
                  placeholder={t("governance.prohibitedSystems.search")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10 backdrop-blur-md bg-background/60"
                />
              </div>
              <select
                value={filterCategory}
                onChange={(e) => setFilterCategory(e.target.value)}
                className="px-3 py-2 border rounded-md text-sm backdrop-blur-md bg-background/60"
              >
                <option value="ALL">{t("governance.prohibitedSystems.allCategories")}</option>
                <option value="ART_5_1_A">Art. 5.1.a</option>
                <option value="ART_5_1_B">Art. 5.1.b</option>
                <option value="ART_5_1_C">Art. 5.1.c</option>
                <option value="ART_5_1_D">Art. 5.1.d</option>
              </select>
              <select
                value={filterActive}
                onChange={(e) => setFilterActive(e.target.value)}
                className="px-3 py-2 border rounded-md text-sm backdrop-blur-md bg-background/60"
              >
                <option value="ALL">{t("governance.prohibitedSystems.allStatus")}</option>
                <option value="ACTIVE">{t("governance.prohibitedSystems.active")}</option>
                <option value="INACTIVE">{t("governance.prohibitedSystems.inactive")}</option>
              </select>
            </div>
          </CardBody>
        </Card>

        {/* Catalog List */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.prohibitedSystems.catalogItems")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              {filteredCatalog.map((item) => (
                <Card
                  key={item.id}
                  className="backdrop-blur-md bg-background/40 hover:bg-background/60 transition-all border-border/50"
                >
                  <CardBody className="space-y-3">
                    <div className="space-y-2">
                      <div className="flex items-start justify-between gap-2">
                        <span className="font-semibold text-base line-clamp-2">{item.name}</span>
                        <Badge variant="danger" className="text-xs whitespace-nowrap">
                          {categoryLabels[item.category]}
                        </Badge>
                      </div>
                      {item.active ? (
                        <Badge className="bg-green-500 text-xs">
                          <CheckCircle className="h-3 w-3 mr-1" />
                          {t("governance.prohibitedSystems.active")}
                        </Badge>
                      ) : (
                        <Badge variant="outline" className="text-xs">
                          <XCircle className="h-3 w-3 mr-1" />
                          {t("governance.prohibitedSystems.inactive")}
                        </Badge>
                      )}
                    </div>
                    <p className="text-xs text-muted-foreground line-clamp-3">
                      {item.description}
                    </p>
                    <div>
                      <div className="text-xs font-semibold mb-1">
                        {t("governance.prohibitedSystems.keywords")}:
                      </div>
                      <div className="flex flex-wrap gap-1">
                        {item.keywords.slice(0, 3).map((keyword, idx) => (
                          <Badge key={idx} className="text-xs bg-blue-100 text-blue-800 border-blue-300 dark:bg-blue-900/30 dark:text-blue-200 dark:border-blue-700">
                            {keyword}
                          </Badge>
                        ))}
                        {item.keywords.length > 3 && (
                          <Badge className="text-xs bg-blue-100 text-blue-800 border-blue-300 dark:bg-blue-900/30 dark:text-blue-200 dark:border-blue-700">
                            +{item.keywords.length - 3}
                          </Badge>
                        )}
                      </div>
                    </div>
                    <div className="text-xs text-muted-foreground space-y-1">
                      <div>
                        {t("governance.prohibitedSystems.created")}: {new Date(item.createdAt).toLocaleDateString()}
                      </div>
                      <div>
                        {t("governance.prohibitedSystems.updated")}: {new Date(item.updatedAt).toLocaleDateString()}
                      </div>
                    </div>
                    <div className="flex items-center gap-2 pt-2 border-t">
                      <Button
                        size="sm"
                        variant="outline"
                        className="flex-1"
                        onClick={() => handleView(item.id)}
                      >
                        <FileText className="h-3 w-3 mr-1" />
                        {t("common.view") || "Visualizar"}
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => handleToggleActive(item.id)}
                        title={item.active ? "Desactivar detección" : "Activar detección"}
                      >
                        {item.active ? (
                          <XCircle className="h-3 w-3 text-red-500" />
                        ) : (
                          <CheckCircle className="h-3 w-3 text-green-500" />
                        )}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              ))}
              {filteredCatalog.length === 0 && (
                <div className="col-span-full text-center py-8 text-muted-foreground">
                  {t("governance.prohibitedSystems.noItemsFound")}
                </div>
              )}
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Dialog de Edición/Creación */}
      <Dialog open={editingId !== null} onOpenChange={(open) => !open && setEditingId(null)}>
        <DialogContent className="max-w-2xl p-0">
          <Card className="border-0 shadow-none">
            <CardHeader className="flex flex-row items-center justify-between pb-4">
              <div className="flex-1">
                <CardTitle>
                  {t("common.view") || "Visualizar Sistema Prohibido"}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-2">
                  Información del sistema prohibido según Art. 5 del EU AI Act. Los datos son de solo lectura.
                </p>
              </div>
              <Button
                variant="ghost"
                size="sm"
                className="h-6 w-6"
                onClick={() => setEditingId(null)}
              >
                <X className="h-4 w-4" />
              </Button>
            </CardHeader>
            <CardBody className="space-y-4">
              {/* Información fija (no editable) */}
              <div className="space-y-2 p-3 bg-muted/50 rounded-lg border">
                <div className="flex items-center justify-between">
                  <Label className="text-sm font-semibold">Nombre</Label>
                  <Badge variant="outline" className="text-xs">
                    {categoryLabels[formData.category]}
                  </Badge>
                </div>
                <p className="text-sm text-muted-foreground">{formData.name}</p>
                <p className="text-xs text-muted-foreground mt-1">
                  El nombre y categoría son fijos según el Art. 5 del EU AI Act y no se pueden modificar.
                </p>
              </div>
              <div className="space-y-2">
                <Label htmlFor="description">
                  {t("governance.prohibitedSystems.description") || "Descripción"}
                </Label>
                <div className="w-full px-3 py-2 border rounded-md text-sm bg-muted/50 min-h-[100px]">
                  <p className="text-sm whitespace-pre-wrap">{formData.description || "Sin descripción"}</p>
                </div>
              </div>
              <div className="space-y-2">
                <Label htmlFor="keywords">
                  {t("governance.prohibitedSystems.keywords")}
                  <span className="text-xs text-muted-foreground ml-2">
                    (Usadas para detección automática)
                  </span>
                </Label>
                <div className="w-full px-3 py-2 border rounded-md text-sm bg-muted/50">
                  <div className="flex flex-wrap gap-1">
                    {formData.keywords
                      .split(",")
                      .map((k) => k.trim())
                      .filter((k) => k.length > 0)
                      .map((keyword, idx) => (
                        <Badge key={idx} className="text-xs bg-blue-100 text-blue-800 border-blue-300 dark:bg-blue-900/30 dark:text-blue-200 dark:border-blue-700">
                          {keyword}
                        </Badge>
                      ))}
                    {formData.keywords.split(",").filter((k) => k.trim().length > 0).length === 0 && (
                      <span className="text-xs text-muted-foreground">Sin keywords definidas</span>
                    )}
                  </div>
                </div>
              </div>
              <div className="flex items-center space-x-2 p-3 bg-muted/30 rounded-lg border">
                <div className="flex items-center justify-center h-4 w-4">
                  {formData.active ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <XCircle className="h-4 w-4 text-red-500" />
                  )}
                </div>
                <div className="flex-1">
                  <Label className="text-sm font-normal">
                    {formData.active
                      ? t("governance.prohibitedSystems.active")
                      : t("governance.prohibitedSystems.inactive")}
                  </Label>
                  <p className="text-xs text-muted-foreground">
                    {formData.active
                      ? "El sistema se verifica automáticamente en los proyectos."
                      : "El sistema está desactivado y no se verifica en los proyectos."}
                  </p>
                </div>
              </div>
            </CardBody>
            <div className="border-t px-6 py-4 flex justify-end gap-2">
              <Button onClick={() => setEditingId(null)}>
                {t("common.close") || "Cerrar"}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}
