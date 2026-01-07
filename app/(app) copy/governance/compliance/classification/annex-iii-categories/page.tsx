"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import {
  BookOpen,
  ChevronDown,
  ChevronRight,
  Search,
  Info,
  AlertCircle,
  Plus,
  Edit,
  Trash2,
  X,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  mockAnnexIIICategories,
  mockAnnexIIICatalog,
  type AnnexIIICategory,
  type AnnexIIISubcategory,
} from "../../../data/mockClassification";

export default function AnnexIIICategoriesPage() {
  const { t, language, mounted } = useTranslation();
  const [categories, setCategories] = useState<AnnexIIICategory[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(
    new Set()
  );

  // Forzar re-render cuando cambia el idioma
  useEffect(() => {
    // Este efecto se ejecutará cuando cambie el idioma
  }, [language, mounted]);

  // CRUD States
  const [isAdmin] = useState(true); // En producción, obtener del contexto de autenticación
  const [showCategoryDialog, setShowCategoryDialog] = useState(false);
  const [showSubcategoryDialog, setShowSubcategoryDialog] = useState(false);
  const [editingCategory, setEditingCategory] = useState<AnnexIIICategory | null>(null);
  const [editingSubcategory, setEditingSubcategory] = useState<{
    categoryCode: string;
    subcategory: AnnexIIISubcategory | null;
  } | null>(null);

  // Form states
  const [categoryForm, setCategoryForm] = useState({
    code: "",
    name: "",
    description: "",
  });
  const [subcategoryForm, setSubcategoryForm] = useState({
    code: "",
    name: "",
    description: "",
  });
  const [categorySubcategories, setCategorySubcategories] = useState<AnnexIIISubcategory[]>([]);
  const [newSubcategoryForm, setNewSubcategoryForm] = useState({
    code: "",
    name: "",
    description: "",
  });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      // Simular carga de datos
      await new Promise((resolve) => setTimeout(resolve, 500));

      // En producción, hacer fetch a la API
      // const response = await fetch("/api/compliance/classification/categories");
      // const data = await response.json();
      // setCategories(data.categories);

      setCategories(mockAnnexIIICategories);
    } catch (error) {
      console.error("Error loading categories:", error);
    } finally {
      setLoading(false);
    }
  };

  const toggleCategory = (categoryCode: string) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(categoryCode)) {
      newExpanded.delete(categoryCode);
    } else {
      newExpanded.add(categoryCode);
    }
    setExpandedCategories(newExpanded);
  };

  const filteredCategories = categories.filter((category) => {
    if (!searchQuery) return true;
    const query = searchQuery.toLowerCase();
    return (
      category.anncategorycode?.toLowerCase().includes(query) ||
      category.anncategoryname?.toLowerCase().includes(query) ||
      category.anncategorydescription?.toLowerCase().includes(query) ||
      category.subcategories?.some(
        (sub) =>
          sub.annsubcategorycode?.toLowerCase().includes(query) ||
          sub.annsubcategoryname?.toLowerCase().includes(query)
      )
    );
  });

  // CRUD Functions
  const handleCreateCategory = () => {
    setEditingCategory(null);
    setCategoryForm({ code: "", name: "", description: "" });
    setCategorySubcategories([]);
    setNewSubcategoryForm({ code: "", name: "", description: "" });
    setShowCategoryDialog(true);
  };

  const handleEditCategory = (category: AnnexIIICategory) => {
    setEditingCategory(category);
    setCategoryForm({
      code: category.anncategorycode || "",
      name: category.anncategoryname || "",
      description: category.anncategorydescription || "",
    });
    setCategorySubcategories([...(category.subcategories || [])]);
    setNewSubcategoryForm({ code: "", name: "", description: "" });
    setShowCategoryDialog(true);
  };

  const handleDeleteCategory = async (categoryCode: string) => {
    if (confirm(t("governance.compliance.classification.catalog.confirmDeleteCategory", "¿Está seguro de que desea eliminar esta categoría?"))) {
      setCategories(categories.filter((cat) => cat.anncategorycode !== categoryCode));
    }
  };

  const handleAddSubcategoryToCategory = () => {
    if (!newSubcategoryForm.code || !newSubcategoryForm.name || !newSubcategoryForm.description) {
      alert(t("governance.compliance.classification.catalog.fillAllFields", "Por favor, complete todos los campos"));
      return;
    }

    const newSubcategory: AnnexIIISubcategory = {
      annsubcategorycode: newSubcategoryForm.code,
      annsubcategoryname: newSubcategoryForm.name,
      annsubcategorydescription: newSubcategoryForm.description,
    };

    setCategorySubcategories([...categorySubcategories, newSubcategory]);
    setNewSubcategoryForm({ code: "", name: "", description: "" });
  };

  const handleRemoveSubcategoryFromCategory = (subcategoryCode: string) => {
    setCategorySubcategories(
      categorySubcategories.filter((sub) => sub.annsubcategorycode !== subcategoryCode)
    );
  };

  const handleSaveCategory = async () => {
    if (!categoryForm.code || !categoryForm.name || !categoryForm.description) {
      alert(t("governance.compliance.classification.catalog.fillAllFields", "Por favor, complete todos los campos"));
      return;
    }

    if (editingCategory) {
      // Editar categoría existente
      setCategories(
        categories.map((cat) =>
          cat.anncategorycode === editingCategory.anncategorycode
            ? {
                ...cat,
                anncategorycode: categoryForm.code,
                anncategoryname: categoryForm.name,
                anncategorydescription: categoryForm.description,
                subcategories: categorySubcategories,
              }
            : cat
        )
      );
    } else {
      // Crear nueva categoría
      const newCategory: AnnexIIICategory = {
        anncategorycode: categoryForm.code,
        anncategoryname: categoryForm.name,
        anncategorydescription: categoryForm.description,
        subcategories: categorySubcategories,
      };
      setCategories([...categories, newCategory]);
    }

    setShowCategoryDialog(false);
    setCategoryForm({ code: "", name: "", description: "" });
    setCategorySubcategories([]);
    setNewSubcategoryForm({ code: "", name: "", description: "" });
    setEditingCategory(null);
  };

  const handleCreateSubcategory = (categoryCode: string) => {
    setEditingSubcategory({ categoryCode, subcategory: null });
    setSubcategoryForm({ code: "", name: "", description: "" });
    setShowSubcategoryDialog(true);
  };

  const handleEditSubcategory = (categoryCode: string, subcategory: AnnexIIISubcategory) => {
    setEditingSubcategory({ categoryCode, subcategory });
    setSubcategoryForm({
      code: subcategory.annsubcategorycode,
      name: subcategory.annsubcategoryname,
      description: subcategory.annsubcategorydescription || "",
    });
    setShowSubcategoryDialog(true);
  };

  const handleDeleteSubcategory = async (categoryCode: string, subcategoryCode: string) => {
    if (confirm(t("governance.compliance.classification.catalog.confirmDeleteSubcategory", "¿Está seguro de que desea eliminar esta subcategoría?"))) {
      setCategories(
        categories.map((cat) =>
          cat.anncategorycode === categoryCode
            ? {
                ...cat,
                subcategories: (cat.subcategories || []).filter(
                  (sub) => sub.annsubcategorycode !== subcategoryCode
                ),
              }
            : cat
        )
      );
    }
  };

  const handleSaveSubcategory = async () => {
    if (!subcategoryForm.code || !subcategoryForm.name || !subcategoryForm.description) {
      alert(t("governance.compliance.classification.catalog.fillAllFields", "Por favor, complete todos los campos"));
      return;
    }

    if (!editingSubcategory) return;

    const { categoryCode, subcategory } = editingSubcategory;

    if (subcategory) {
      // Editar subcategoría existente
      setCategories(
        categories.map((cat) =>
          cat.anncategorycode === categoryCode
            ? {
                ...cat,
                subcategories: (cat.subcategories || []).map((sub) =>
                  sub.annsubcategorycode === subcategory.annsubcategorycode
                    ? {
                        annsubcategorycode: subcategoryForm.code,
                        annsubcategoryname: subcategoryForm.name,
                        annsubcategorydescription: subcategoryForm.description,
                      }
                    : sub
                ),
              }
            : cat
        )
      );
    } else {
      // Crear nueva subcategoría
      const newSubcategory: AnnexIIISubcategory = {
        annsubcategorycode: subcategoryForm.code,
        annsubcategoryname: subcategoryForm.name,
        annsubcategorydescription: subcategoryForm.description,
      };
      setCategories(
        categories.map((cat) =>
          cat.anncategorycode === categoryCode
            ? {
                ...cat,
                subcategories: [...(cat.subcategories || []), newSubcategory],
              }
            : cat
        )
      );
    }

    setShowSubcategoryDialog(false);
    setSubcategoryForm({ code: "", name: "", description: "" });
    setEditingSubcategory(null);
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
        <div className="absolute top-1/4 right-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute bottom-1/4 left-1/4 w-1.5 h-1.5 bg-blue-400/20 rounded-full animate-pulse delay-300" />
      </div>

      {/* Botón de acción fijo en la parte superior derecha */}
      {isAdmin && (
        <div className="fixed top-20 right-6 z-50">
          <Button
            onClick={handleCreateCategory}
            className="h-10 w-10 p-0 rounded-full shadow-lg backdrop-blur-md bg-primary hover:bg-primary/90 border-0"
            title={t("governance.compliance.classification.catalog.createCategory", "Crear Categoría")}
          >
            <Plus className="h-5 w-5" />
          </Button>
        </div>
      )}

      <div className="relative z-10 w-full px-4 py-6 space-y-6 max-h-[calc(100vh-8rem)] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <BookOpen className="w-8 h-8 text-blue-500" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
                {t("governance.compliance.classification.catalog.title", "Catálogo de Categorías Anexo III")}
              </h1>
            </div>
            <p className="text-muted-foreground">
              {t("governance.compliance.classification.catalog.subtitle", "Catálogo completo de categorías de sistemas de IA de alto riesgo según el Anexo III del EU AI Act")}
            </p>
          </div>
        </div>

        {/* Búsqueda */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50 mx-2">
          <CardBody>
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                type="text"
                placeholder={t("governance.compliance.classification.catalog.searchPlaceholder", "Buscar por código, nombre o descripción...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardBody>
        </Card>

        {/* Información */}
        <Card className="backdrop-blur-sm bg-blue-50/50 border-blue-200 mx-2">
          <CardBody>
            <div className="flex items-start gap-3">
              <Info className="h-5 w-5 text-blue-600 flex-shrink-0 mt-0.5" />
              <div className="text-sm text-blue-800">
                <p className="font-semibold mb-1">{t("governance.compliance.classification.catalog.aboutTitle", "Sobre el Anexo III")}</p>
                <p>
                  {t("governance.compliance.classification.catalog.aboutDescription", "El Anexo III del EU AI Act define las categorías de sistemas de IA considerados de alto riesgo. Estas categorías requieren cumplimiento estricto con los requisitos del Artículo 8 al 15 del Reglamento. Haz clic en cada categoría para ver más detalles y subcategorías.")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>

        {/* Lista de Categorías */}
        <div className="space-y-4">
          {filteredCategories.length === 0 ? (
            <Card className="backdrop-blur-sm bg-card/80 border-border/50 mx-2">
              <CardBody>
                <div className="text-center py-8 text-muted-foreground">
                  <AlertCircle className="h-12 w-12 mx-auto mb-4 opacity-50" />
                  <p>{t("governance.compliance.classification.catalog.noResults", "No se encontraron categorías que coincidan con la búsqueda.")}</p>
                </div>
              </CardBody>
            </Card>
          ) : (
            filteredCategories.map((category) => {
              const isExpanded = expandedCategories.has(category.anncategorycode);
              const examples = mockAnnexIIICatalog.examples[category.anncategorycode as keyof typeof mockAnnexIIICatalog.examples] || [];

              return (
                <Card
                  key={category.anncategorycode}
                  className="backdrop-blur-sm bg-card/80 border-border/50 hover:shadow-md transition-shadow relative mx-2"
                >
                  {/* Botones de acción fijos en la parte superior derecha */}
                  <div className="absolute top-4 right-4 flex items-center gap-1 z-10">
                    {isAdmin && (
                      <>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEditCategory(category)}
                          className="h-8 w-8 p-0 rounded-full hover:bg-accent/50 border-0"
                          title={t("common.edit", "Editar")}
                        >
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDeleteCategory(category.anncategorycode)}
                          className="h-8 w-8 p-0 rounded-full hover:bg-red-500/10 text-red-500 hover:text-red-700 border-0"
                          title={t("common.delete", "Eliminar")}
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </>
                    )}
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => toggleCategory(category.anncategorycode)}
                      className="h-8 w-8 p-0 rounded-full hover:bg-accent/50 border-0"
                      title={isExpanded ? t("common.collapse", "Contraer") : t("common.expand", "Expandir")}
                    >
                      {isExpanded ? (
                        <ChevronDown className="h-4 w-4" />
                      ) : (
                        <ChevronRight className="h-4 w-4" />
                      )}
                    </Button>
                  </div>
                  <CardHeader className="pr-32">
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-3 mb-2">
                          <Badge variant="outline" className="font-mono text-sm px-3 py-1">
                            {category.anncategorycode}
                          </Badge>
                          <CardTitle className="text-xl">{category.anncategoryname}</CardTitle>
                        </div>
                        <p className="text-muted-foreground text-sm mt-2">
                          {category.anncategorydescription}
                        </p>
                      </div>
                    </div>
                  </CardHeader>

                  {isExpanded && (
                    <CardBody className="pt-0">
                      <div className="space-y-6">
                        {/* Subcategorías */}
                        <div className="relative">
                          <div className="flex items-center justify-between mb-3">
                            <h4 className="font-semibold text-sm uppercase text-muted-foreground">
                              {t("governance.compliance.classification.subcategories", "Subcategorías")} ({(category.subcategories || []).length})
                            </h4>
                            {isAdmin && (
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => handleCreateSubcategory(category.anncategorycode)}
                                className="h-8 w-8 p-0 rounded-full hover:bg-accent/50 border-0"
                                title={t("governance.compliance.classification.catalog.createSubcategory", "Añadir Subcategoría")}
                              >
                                <Plus className="h-4 w-4" />
                              </Button>
                            )}
                          </div>
                          <div className="space-y-2">
                            {(category.subcategories || []).map((subcat) => (
                              <div
                                key={subcat.annsubcategorycode}
                                className="p-3 border rounded-lg bg-accent/30 hover:bg-accent/50 transition-colors relative"
                              >
                                {isAdmin && (
                                  <div className="absolute top-2 right-2 flex items-center gap-1">
                                    <Button
                                      variant="ghost"
                                      size="sm"
                                      onClick={() => handleEditSubcategory(category.anncategorycode, subcat)}
                                      className="h-7 w-7 p-0 rounded-full hover:bg-accent/50 border-0"
                                      title={t("common.edit", "Editar")}
                                    >
                                      <Edit className="h-3.5 w-3.5" />
                                    </Button>
                                    <Button
                                      variant="ghost"
                                      size="sm"
                                      onClick={() => handleDeleteSubcategory(category.anncategorycode, subcat.annsubcategorycode)}
                                      className="h-7 w-7 p-0 rounded-full hover:bg-red-500/10 text-red-500 hover:text-red-700 border-0"
                                      title={t("common.delete", "Eliminar")}
                                    >
                                      <Trash2 className="h-3.5 w-3.5" />
                                    </Button>
                                  </div>
                                )}
                                <div className="flex items-start gap-3 pr-16">
                                  <Badge
                                    variant="outline"
                                    className="font-mono text-xs flex-shrink-0"
                                  >
                                    {subcat.annsubcategorycode}
                                  </Badge>
                                  <div className="flex-1">
                                    <div className="font-medium text-sm mb-1">
                                      {subcat.annsubcategoryname}
                                    </div>
                                    <p className="text-xs text-muted-foreground">
                                      {subcat.annsubcategorydescription}
                                    </p>
                                  </div>
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>

                        {/* Ejemplos */}
                        {examples.length > 0 && (
                          <div>
                            <h4 className="font-semibold mb-3 text-sm uppercase text-muted-foreground">
                              {t("governance.compliance.classification.catalog.examplesTitle", "Ejemplos de Sistemas")}
                            </h4>
                            <ul className="space-y-2">
                              {examples.map((example, index) => (
                                <li
                                  key={index}
                                  className="flex items-start gap-2 text-sm"
                                >
                                  <span className="text-primary mt-1">•</span>
                                  <span className="text-muted-foreground">{example}</span>
                                </li>
                              ))}
                            </ul>
                          </div>
                        )}

                        {/* Información Adicional */}
                        <div className="pt-4 border-t">
                          <div className="grid grid-cols-2 gap-4 text-sm">
                            <div>
                              <span className="font-semibold text-muted-foreground">
                                {t("governance.compliance.classification.catalog.code", "Código")}:
                              </span>{" "}
                              <span className="font-mono">{category.anncategorycode}</span>
                            </div>
                            <div>
                              <span className="font-semibold text-muted-foreground">
                                {t("governance.compliance.classification.subcategories", "Subcategorías")}:
                              </span>{" "}
                               {(category.subcategories || []).length}
                            </div>
                          </div>
                        </div>
                      </div>
                    </CardBody>
                  )}
                </Card>
              );
            })
          )}
        </div>

        {/* Resumen */}
        <Card className="backdrop-blur-sm bg-card/80 border-border/50 mx-2">
          <CardBody>
            <div className="text-center text-sm text-muted-foreground">
              <p>
                {t("governance.compliance.classification.catalog.totalCategories", "Total de categorías")}: <span className="font-semibold">{categories.length}</span> |{" "}
                {t("governance.compliance.classification.catalog.totalSubcategories", "Total de subcategorías")}:{" "}
                <span className="font-semibold">
                  {categories.reduce(
                    (sum, cat) => sum + (cat.subcategories || []).length,
                    0
                  )}
                </span>
              </p>
            </div>
          </CardBody>
        </Card>

        {/* Diálogo para Crear/Editar Categoría */}
        <Dialog open={showCategoryDialog} onOpenChange={setShowCategoryDialog}>
          <DialogContent className="max-w-4xl w-full backdrop-blur-sm bg-background border-border shadow-lg" >
            <DialogHeader className="border-b pb-4 px-6 pt-6">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                  <DialogTitle className="text-xl font-semibold mb-2" >
                    {editingCategory
                      ? t("governance.compliance.classification.catalog.editCategory", "Editar Categoría")
                      : t("governance.compliance.classification.catalog.createCategory", "Crear Categoría")}
                  </DialogTitle>
                  <DialogDescription className="text-sm mt-2" >
                    {t("governance.compliance.classification.catalog.categoryDialogDescription", "Complete los datos de la categoría del Anexo III")}
                  </DialogDescription>
                </div>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    setShowCategoryDialog(false);
                    setCategoryForm({ code: "", name: "", description: "" });
                    setCategorySubcategories([]);
                    setNewSubcategoryForm({ code: "", name: "", description: "" });
                    setEditingCategory(null);
                  }}
                  className="h-8 w-8 p-0 flex-shrink-0 flex items-center justify-center"
                >
                  <X className="h-5 w-5" />
                </Button>
              </div>
            </DialogHeader>
            <div className="modal-body p-6 space-y-6 max-h-[70vh] overflow-y-auto">
              {/* Información de la Categoría */}
              <div className="space-y-4">
                <h3 className="text-lg font-semibold border-b pb-2">
                  {t("governance.compliance.classification.catalog.categoryInformation", "Información de la Categoría")}
                </h3>
                <div>
                  <Label htmlFor="categoryCode" className="text-sm font-medium">
                    {t("governance.compliance.classification.catalog.code", "Código")} *
                  </Label>
                  <Input
                    id="categoryCode"
                    value={categoryForm.code}
                    onChange={(e) =>
                      setCategoryForm({ ...categoryForm, code: e.target.value })
                    }
                      placeholder={t("governance.compliance.classification.catalog.code", "Código") + " (A3_X)"}
                    className="mt-2"
                  />
                </div>
                <div>
                  <Label htmlFor="categoryName" className="text-sm font-medium">
                    {t("governance.compliance.classification.catalog.name", "Nombre")} *
                  </Label>
                  <Input
                    id="categoryName"
                    value={categoryForm.name}
                    onChange={(e) =>
                      setCategoryForm({ ...categoryForm, name: e.target.value })
                    }
                    placeholder={t("governance.compliance.classification.catalog.categoryNamePlaceholder", "Nombre de la categoría")}
                    className="mt-2"
                  />
                </div>
                <div>
                  <Label htmlFor="categoryDescription" className="text-sm font-medium">
                    {t("governance.compliance.classification.catalog.description", "Descripción")} *
                  </Label>
                  <Textarea
                    id="categoryDescription"
                    value={categoryForm.description}
                    onChange={(e) =>
                      setCategoryForm({ ...categoryForm, description: e.target.value })
                    }
                    placeholder={t("governance.compliance.classification.catalog.categoryDescriptionPlaceholder", "Descripción detallada de la categoría")}
                    rows={4}
                    className="mt-2"
                  />
                </div>
              </div>

              {/* Subcategorías */}
              <div className="space-y-4 border-t pt-4">
                <div className="flex items-center justify-between">
                  <h3 className="text-lg font-semibold">
                    {t("governance.compliance.classification.subcategories", "Subcategorías")} ({categorySubcategories.length})
                  </h3>
                </div>

                {/* Lista de Subcategorías Añadidas */}
                {categorySubcategories.length > 0 && (
                  <div className="space-y-2">
                    {categorySubcategories.map((subcat, index) => (
                      <div
                        key={index}
                        className="p-3 border rounded-lg bg-accent/30 flex items-start justify-between gap-3"
                      >
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-1">
                            <Badge variant="outline" className="font-mono text-xs">
                              {subcat.annsubcategorycode}
                            </Badge>
                            <span className="font-medium text-sm">{subcat.annsubcategoryname}</span>
                          </div>
                          <p className="text-xs text-muted-foreground">{subcat.annsubcategorydescription}</p>
                        </div>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleRemoveSubcategoryFromCategory(subcat.annsubcategorycode)}
                          className="h-7 w-7 p-0 rounded-full hover:bg-red-500/10 text-red-500 hover:text-red-700 flex-shrink-0 border-0"
                          title={t("common.delete", "Eliminar")}
                        >
                          <Trash2 className="h-3.5 w-3.5" />
                        </Button>
                      </div>
                    ))}
                  </div>
                )}

                {/* Formulario para Añadir Nueva Subcategoría */}
                <div className="p-4 border-2 border-dashed rounded-lg bg-accent/10">
                  <h4 className="text-sm font-semibold mb-3">
                    {t("governance.compliance.classification.catalog.addNewSubcategory", "Añadir Nueva Subcategoría")}
                  </h4>
                  <div className="space-y-3">
                    <div>
                      <Label htmlFor="newSubcategoryCode" className="text-xs font-medium">
                        {t("governance.compliance.classification.catalog.code", "Código")} *
                      </Label>
                      <Input
                        id="newSubcategoryCode"
                        value={newSubcategoryForm.code}
                        onChange={(e) =>
                          setNewSubcategoryForm({ ...newSubcategoryForm, code: e.target.value })
                        }
                        placeholder={t("governance.compliance.classification.catalog.code", "Código") + " (A3_X_Y)"}
                        className="mt-1 text-sm"
                      />
                    </div>
                    <div>
                      <Label htmlFor="newSubcategoryName" className="text-xs font-medium">
                        {t("governance.compliance.classification.catalog.name", "Nombre")} *
                      </Label>
                      <Input
                        id="newSubcategoryName"
                        value={newSubcategoryForm.name}
                        onChange={(e) =>
                          setNewSubcategoryForm({ ...newSubcategoryForm, name: e.target.value })
                        }
                        placeholder={t("governance.compliance.classification.catalog.subcategoryNamePlaceholder", "Nombre de la subcategoría")}
                        className="mt-1 text-sm"
                      />
                    </div>
                    <div>
                      <Label htmlFor="newSubcategoryDescription" className="text-xs font-medium">
                        {t("governance.compliance.classification.catalog.description", "Descripción")} *
                      </Label>
                      <Textarea
                        id="newSubcategoryDescription"
                        value={newSubcategoryForm.description}
                        onChange={(e) =>
                          setNewSubcategoryForm({ ...newSubcategoryForm, description: e.target.value })
                        }
                        placeholder={t("governance.compliance.classification.catalog.subcategoryDescriptionPlaceholder", "Descripción detallada de la subcategoría")}
                        rows={2}
                        className="mt-1 text-sm"
                      />
                    </div>
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      onClick={handleAddSubcategoryToCategory}
                      className="w-full rounded-full hover:bg-accent/50 border-0"
                    >
                      <Plus className="h-4 w-4 mr-2" />
                      {t("governance.compliance.classification.catalog.addSubcategory", "Añadir Subcategoría")}
                    </Button>
                  </div>
                </div>
              </div>
            </div>
            <div className="modal-footer border-t pt-4 px-6 pb-6 flex justify-end gap-2">
              <Button
                variant="ghost"
                onClick={() => {
                  setShowCategoryDialog(false);
                  setCategoryForm({ code: "", name: "", description: "" });
                  setEditingCategory(null);
                }}
                className="rounded-full hover:bg-accent/50 border-0"
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleSaveCategory}
                className="rounded-full bg-primary hover:bg-primary/90 border-0"
              >
                {editingCategory
                  ? t("common.save", "Guardar")
                  : t("common.create", "Crear")}
              </Button>
            </div>
          </DialogContent>
        </Dialog>

        {/* Diálogo para Crear/Editar Subcategoría */}
        <Dialog open={showSubcategoryDialog} onOpenChange={setShowSubcategoryDialog}>
          <DialogContent className="max-w-4xl w-full backdrop-blur-sm bg-background border-border shadow-lg" >
            <DialogHeader className="border-b pb-4 px-6 pt-6">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                  <DialogTitle className="text-xl font-semibold mb-2" >
                    {editingSubcategory?.subcategory
                      ? t("governance.compliance.classification.catalog.editSubcategory", "Editar Subcategoría")
                      : t("governance.compliance.classification.catalog.createSubcategory", "Crear Subcategoría")}
                  </DialogTitle>
                  <DialogDescription className="text-sm mt-2" >
                    {t("governance.compliance.classification.catalog.subcategoryDialogDescription", "Complete los datos de la subcategoría")}
                  </DialogDescription>
                </div>
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    setShowSubcategoryDialog(false);
                    setSubcategoryForm({ code: "", name: "", description: "" });
                    setEditingSubcategory(null);
                  }}
                  className="h-8 w-8 p-0 flex-shrink-0 flex items-center justify-center"
                >
                  <X className="h-5 w-5" />
                </Button>
              </div>
            </DialogHeader>
            <div className="modal-body p-6 space-y-4">
              <div>
                <Label htmlFor="subcategoryCode" className="text-sm font-medium">
                  {t("governance.compliance.classification.catalog.code", "Código")} *
                </Label>
                <Input
                  id="subcategoryCode"
                  value={subcategoryForm.code}
                  onChange={(e) =>
                    setSubcategoryForm({ ...subcategoryForm, code: e.target.value })
                  }
                  placeholder={t("governance.compliance.classification.catalog.code", "Código") + " (A3_X_Y)"}
                  className="mt-2"
                />
              </div>
              <div>
                <Label htmlFor="subcategoryName" className="text-sm font-medium">
                  {t("governance.compliance.classification.catalog.name", "Nombre")} *
                </Label>
                <Input
                  id="subcategoryName"
                  value={subcategoryForm.name}
                  onChange={(e) =>
                    setSubcategoryForm({ ...subcategoryForm, name: e.target.value })
                  }
                  placeholder={t("governance.compliance.classification.catalog.subcategoryNamePlaceholder", "Nombre de la subcategoría")}
                  className="mt-2"
                />
              </div>
              <div>
                <Label htmlFor="subcategoryDescription" className="text-sm font-medium">
                  {t("governance.compliance.classification.catalog.description", "Descripción")} *
                </Label>
                <Textarea
                  id="subcategoryDescription"
                  value={subcategoryForm.description}
                  onChange={(e) =>
                    setSubcategoryForm({ ...subcategoryForm, description: e.target.value })
                  }
                  placeholder={t("governance.compliance.classification.catalog.subcategoryDescriptionPlaceholder", "Descripción detallada de la subcategoría")}
                  rows={3}
                  className="mt-2"
                />
              </div>
            </div>
            <div className="modal-footer border-t pt-4 px-6 pb-6 flex justify-end gap-2">
              <Button
                variant="ghost"
                onClick={() => {
                  setShowSubcategoryDialog(false);
                  setSubcategoryForm({ code: "", name: "", description: "" });
                  setEditingSubcategory(null);
                }}
                className="rounded-full hover:bg-accent/50 border-0"
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleSaveSubcategory}
                className="rounded-full bg-primary hover:bg-primary/90 border-0"
              >
                {editingSubcategory?.subcategory
                  ? t("common.save", "Guardar")
                  : t("common.create", "Crear")}
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}
