"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from "@/components/ui/tabs";
import {
  ArrowLeft,
  Database,
  Table as TableIcon,
  FileText,
  Play,
  CheckCircle2,
  XCircle,
  Loader2,
  ChevronRight,
  ChevronDown,
  RefreshCw,
  Plus,
  X,
} from "lucide-react";
import { DatabaseIntegration } from "../../../types/integrations";

interface SchemaTable {
  name: string;
  type: "TABLE" | "VIEW" | "COLLECTION" | "BUCKET";
  schema?: string;
  rowCount?: number;
  size?: string;
  columns?: SchemaColumn[];
  expanded?: boolean;
}

interface SchemaColumn {
  name: string;
  type: string;
  nullable: boolean;
  primaryKey?: boolean;
  foreignKey?: string;
}

interface SQLQuery {
  name: string;
  query: string;
  description?: string;
}

export default function ExploreIntegrationPage() {
  const params = useParams();
  const router = useRouter();
  const { t } = useTranslation();
  const integrationId = parseInt(params.id as string);

  const [integration, setIntegration] = useState<DatabaseIntegration | null>(null);
  const [loading, setLoading] = useState(true);
  const [tables, setTables] = useState<SchemaTable[]>([]);
  const [selectedTables, setSelectedTables] = useState<Set<string>>(new Set());
  const [expandedTables, setExpandedTables] = useState<Set<string>>(new Set());
  const [sqlQueries, setSqlQueries] = useState<SQLQuery[]>([]);
  const [isQueryDialogOpen, setIsQueryDialogOpen] = useState(false);
  const [currentQuery, setCurrentQuery] = useState<SQLQuery>({
    name: "",
    query: "",
    description: "",
  });
  const [testingQuery, setTestingQuery] = useState(false);
  const [queryResult, setQueryResult] = useState<any>(null);
  const [queryError, setQueryError] = useState<string | null>(null);

  useEffect(() => {
    loadIntegration();
    loadSchema();
  }, [integrationId]);

  const loadIntegration = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/integrations/${integrationId}`);
      if (response.ok) {
        const data = await response.json();
        setIntegration(data);
      } else if (response.status === 404) {
        console.error("Integración no encontrada:", integrationId);
        // No establecer integration a null aquí, dejar que el componente maneje el estado
      }
    } catch (error) {
      console.error("Error cargando integración:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadSchema = async () => {
    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integrationId}/schema`
      );
      if (response.ok) {
        const data = await response.json();
        setTables(data.tables || []);
      }
    } catch (error) {
      console.error("Error cargando esquema:", error);
    }
  };

  const loadTableColumns = async (tableName: string, schema?: string) => {
    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integrationId}/schema/${tableName}?schema=${schema || ""}`
      );
      if (response.ok) {
        const data = await response.json();
        setTables((prev) =>
          prev.map((t) =>
            t.name === tableName && t.schema === schema
              ? { ...t, columns: data.columns, expanded: true }
              : t
          )
        );
        setExpandedTables((prev) => new Set([...prev, `${schema || ""}.${tableName}`]));
      }
    } catch (error) {
      console.error("Error cargando columnas:", error);
    }
  };

  const toggleTableExpansion = (table: SchemaTable) => {
    const key = `${table.schema || ""}.${table.name}`;
    if (expandedTables.has(key)) {
      setExpandedTables((prev) => {
        const next = new Set(prev);
        next.delete(key);
        return next;
      });
    } else {
      loadTableColumns(table.name, table.schema);
    }
  };

  const toggleTableSelection = (table: SchemaTable) => {
    const key = `${table.schema || ""}.${table.name}`;
    setSelectedTables((prev) => {
      const next = new Set(prev);
      if (next.has(key)) {
        next.delete(key);
      } else {
        next.add(key);
      }
      return next;
    });
  };

  const handleTestQuery = async () => {
    if (!currentQuery.query.trim()) return;

    setTestingQuery(true);
    setQueryError(null);
    setQueryResult(null);

    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integrationId}/query/test`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ query: currentQuery.query }),
        }
      );

      if (response.ok) {
        const data = await response.json();
        setQueryResult(data);
      } else {
        const error = await response.json();
        setQueryError(error.message || "Error ejecutando consulta");
      }
    } catch (error: any) {
      setQueryError(error.message || "Error ejecutando consulta");
    } finally {
      setTestingQuery(false);
    }
  };

  const handleSaveQuery = async () => {
    if (!currentQuery.name || !currentQuery.query.trim()) return;

    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integrationId}/queries`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(currentQuery),
        }
      );

      if (response.ok) {
        const data = await response.json();
        setSqlQueries((prev) => [...prev, data]);
        setIsQueryDialogOpen(false);
        setCurrentQuery({ name: "", query: "", description: "" });
      }
    } catch (error) {
      console.error("Error guardando consulta:", error);
    }
  };

  const [creatingOrigins, setCreatingOrigins] = useState(false);

  const handleCreateOrigin = async () => {
    const selected = Array.from(selectedTables);
    if (selected.length === 0 && sqlQueries.length === 0) {
      alert(t("governance.data.integrations.explore.errors.selectTableOrQuery"));
      return;
    }

    try {
      setCreatingOrigins(true);

      // Llamar al endpoint de catalogación
      const response = await fetch(
        `/api/v1/governance/integrations/${integrationId}/catalog-origins`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ selectedTables: selected }),
        }
      );

      if (response.ok) {
        const createdOrigins = await response.json();
        alert(
          t("governance.data.integrations.explore.success.originsCreated", `${createdOrigins.length} orígenes creados exitosamente`, {
            count: String(createdOrigins.length),
          })
        );
        // Navegar a la lista de orígenes
        router.push("/governance/data/origins");
      } else {
        const error = await response.json();
        alert(
          error.error ||
            t("governance.data.integrations.explore.errors.createFailed")
        );
      }
    } catch (error) {
      console.error("Error creando orígenes:", error);
      alert(t("governance.data.integrations.explore.errors.createFailed"));
    } finally {
      setCreatingOrigins(false);
    }
  };

  if (loading) {
    return (
      <div className="w-full py-6 px-6">
        <div className="text-center">{t("common.loading")}</div>
      </div>
    );
  }

  if (!integration) {
    return (
      <div className="w-full py-6 px-6">
        <div className="text-center">{t("governance.data.integrations.explore.errors.integrationNotFound")}</div>
      </div>
    );
  }

  return (
    <div className="w-full py-6 space-y-6 px-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <Button variant="ghost" onClick={() => router.push("/governance/data/integrations")}>
            <ArrowLeft className="mr-2 h-4 w-4" />
            {t("governance.data.integrations.explore.back")}
          </Button>
          <div>
            <h1 className="text-3xl font-bold">
              {t("governance.data.integrations.explore.title")}: {integration.dtgname}
            </h1>
            <p className="text-muted-foreground mt-2">
              {integration.dtgdatabasetype} - {integration.dtghost || integration.dtgdatabase}
            </p>
          </div>
        </div>
        <Button
          onClick={handleCreateOrigin}
          disabled={(selectedTables.size === 0 && sqlQueries.length === 0) || creatingOrigins}
        >
          {creatingOrigins ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              {t("common.creating", "Creando...")}
            </>
          ) : (
            t("governance.data.integrations.explore.createOrigin")
          )}
        </Button>
      </div>

      <Tabs defaultValue="tables" className="space-y-4">
        <TabsList>
          <TabsTrigger value="tables">{t("governance.data.integrations.explore.tabs.tables")}</TabsTrigger>
          <TabsTrigger value="queries">{t("governance.data.integrations.explore.tabs.queries")}</TabsTrigger>
        </TabsList>

        <TabsContent value="tables" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>{t("governance.data.integrations.explore.schema.title")}</CardTitle>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => loadSchema()}
                >
                  <RefreshCw className="mr-2 h-4 w-4" />
                  {t("governance.data.integrations.explore.schema.refresh")}
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              {tables.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.data.integrations.explore.schema.empty")}
                </div>
              ) : (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead className="w-12">{""}</TableHead>
                      <TableHead className="w-12">{""}</TableHead>
                      <TableHead>{t("governance.data.integrations.explore.schema.columns.name")}</TableHead>
                      <TableHead>{t("governance.data.integrations.explore.schema.columns.schema")}</TableHead>
                      <TableHead>{t("governance.data.integrations.explore.schema.columns.type")}</TableHead>
                      <TableHead>{t("governance.data.integrations.explore.schema.columns.rows")}</TableHead>
                      <TableHead>{t("governance.data.integrations.explore.schema.columns.size")}</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {tables.map((table) => {
                      const key = `${table.schema || ""}.${table.name}`;
                      const isExpanded = expandedTables.has(key);
                      const isSelected = selectedTables.has(key);
                      return (
                        <>
                          <TableRow
                            key={key}
                            className={isSelected ? "bg-muted" : ""}
                          >
                            <TableCell>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => toggleTableExpansion(table)}
                              >
                                {isExpanded ? (
                                  <ChevronDown className="h-4 w-4" />
                                ) : (
                                  <ChevronRight className="h-4 w-4" />
                                )}
                              </Button>
                            </TableCell>
                            <TableCell>
                              <input
                                type="checkbox"
                                checked={isSelected}
                                onChange={() => toggleTableSelection(table)}
                              />
                            </TableCell>
                            <TableCell>
                              <div className="flex items-center space-x-2">
                                <TableIcon className="h-4 w-4" />
                                <span className="font-medium">{table.name}</span>
                              </div>
                            </TableCell>
                            <TableCell>{table.schema || "-"}</TableCell>
                            <TableCell>
                              <Badge variant="outline">{table.type}</Badge>
                            </TableCell>
                            <TableCell>{table.rowCount?.toLocaleString() || "-"}</TableCell>
                            <TableCell>{table.size || "-"}</TableCell>
                          </TableRow>
                          {isExpanded && table.columns && (
                            <TableRow>
                              <TableCell colSpan={7}>
                                <div className="pl-8 space-y-2">
                                  <div className="font-semibold text-sm mb-2">{t("governance.data.integrations.explore.schema.expandColumns")}</div>
                                  <div className="grid grid-cols-4 gap-2 text-sm">
                                    {table.columns.map((col) => (
                                      <div
                                        key={col.name}
                                        className="flex items-center space-x-2 p-2 border rounded"
                                      >
                                        <span className="font-medium">{col.name}</span>
                                        <Badge variant="outline" className="text-xs">
                                          {col.type}
                                        </Badge>
                                        {col.primaryKey && (
                                          <Badge variant="primary" className="text-xs">PK</Badge>
                                        )}
                                        {!col.nullable && (
                                          <Badge variant="secondary" className="text-xs">NOT NULL</Badge>
                                        )}
                                      </div>
                                    ))}
                                  </div>
                                </div>
                              </TableCell>
                            </TableRow>
                          )}
                        </>
                      );
                    })}
                  </TableBody>
                </Table>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="queries" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>{t("governance.data.integrations.explore.queries.title")}</CardTitle>
                <Button onClick={() => setIsQueryDialogOpen(true)}>
                  <Plus className="mr-2 h-4 w-4" />
                  {t("governance.data.integrations.explore.queries.create")}
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              {sqlQueries.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.data.integrations.explore.queries.empty")}
                </div>
              ) : (
                <div className="space-y-4">
                  {sqlQueries.map((query) => (
                    <Card key={query.name}>
                      <CardHeader>
                        <CardTitle className="text-lg">{query.name}</CardTitle>
                        {query.description && (
                          <p className="text-sm text-muted-foreground">{query.description}</p>
                        )}
                      </CardHeader>
                      <CardContent>
                        <div className="bg-muted p-4 rounded font-mono text-sm">
                          {query.query}
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      <Dialog open={isQueryDialogOpen} onOpenChange={setIsQueryDialogOpen}>
        <DialogContent className="max-w-4xl p-0">
          <Card className="border-0 shadow-none">
            {/* HEADER */}
            <CardHeader className="flex flex-row items-center justify-between pb-4">
              <div className="flex-1">
                <CardTitle className="text-2xl font-bold">
                  {t("governance.data.integrations.explore.queries.dialog.title", "Nueva Consulta SQL")}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-2">
                  {t("governance.data.integrations.explore.queries.dialog.description", "Crea y prueba una consulta SQL personalizada para esta integración")}
                </p>
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="h-6 w-6"
                onClick={() => setIsQueryDialogOpen(false)}
              >
                <X className="h-4 w-4" />
              </Button>
            </CardHeader>

            {/* BODY */}
            <CardBody className="space-y-6">
              <div className="space-y-2">
                <Label className="text-base font-semibold">
                  {t("governance.data.integrations.explore.queries.dialog.name", "Nombre de la Consulta")} *
                </Label>
                <Input
                  value={currentQuery.name}
                  onChange={(e) =>
                    setCurrentQuery({ ...currentQuery, name: e.target.value })
                  }
                  placeholder={t("governance.data.integrations.explore.queries.dialog.namePlaceholder", "Ej: Consulta de usuarios activos")}
                  className="h-11"
                />
              </div>

              <div className="space-y-2">
                <Label className="text-base font-semibold">
                  {t("governance.data.integrations.explore.queries.dialog.description", "Descripción")}
                </Label>
                <Input
                  value={currentQuery.description}
                  onChange={(e) =>
                    setCurrentQuery({ ...currentQuery, description: e.target.value })
                  }
                  placeholder={t("governance.data.integrations.explore.queries.dialog.descriptionPlaceholder", "Descripción opcional de la consulta")}
                  className="h-11"
                />
              </div>

              <div className="space-y-2">
                <Label className="text-base font-semibold">
                  {t("governance.data.integrations.explore.queries.dialog.query", "Consulta SQL")} *
                </Label>
                <Textarea
                  value={currentQuery.query}
                  onChange={(e) =>
                    setCurrentQuery({ ...currentQuery, query: e.target.value })
                  }
                  placeholder={t("governance.data.integrations.explore.queries.dialog.queryPlaceholder", "SELECT * FROM tabla WHERE condicion")}
                  className="font-mono min-h-[250px] text-sm"
                />
              </div>

              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  onClick={handleTestQuery}
                  disabled={testingQuery || !currentQuery.query.trim()}
                  className="h-11"
                >
                  {testingQuery ? (
                    <>
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                      {t("common.testing", "Probando...")}
                    </>
                  ) : (
                    <>
                      <Play className="mr-2 h-4 w-4" />
                      {t("governance.data.integrations.explore.queries.dialog.test", "Probar Consulta")}
                    </>
                  )}
                </Button>
              </div>

              {queryError && (
                <div className="bg-destructive/10 dark:bg-destructive/20 text-destructive dark:text-destructive-foreground p-4 rounded-lg flex items-center gap-2 border border-destructive/20">
                  <XCircle className="h-5 w-5 flex-shrink-0" />
                  <span className="text-sm">{queryError}</span>
                </div>
              )}

              {queryResult && (
                <div className="space-y-2">
                  <div className="bg-green-50 dark:bg-green-950/30 text-green-900 dark:text-green-100 p-4 rounded-lg flex items-center gap-2 border border-green-200 dark:border-green-800">
                    <CheckCircle2 className="h-5 w-5 flex-shrink-0" />
                    <span className="text-sm font-medium">
                      {t("governance.data.integrations.explore.queries.dialog.testSuccess", "Consulta ejecutada exitosamente. {count} filas encontradas").replace(
                        "{count}",
                        String(queryResult.rowCount || 0)
                      )}
                    </span>
                  </div>
                  {queryResult.columns && (
                    <div className="text-sm text-muted-foreground bg-muted/50 p-3 rounded-lg">
                      <span className="font-medium">Columnas: </span>
                      <span>{queryResult.columns.join(", ")}</span>
                    </div>
                  )}
                </div>
              )}
            </CardBody>

            {/* FOOTER */}
            <div className="border-t px-6 py-4 flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => {
                  setIsQueryDialogOpen(false);
                  setCurrentQuery({ name: "", query: "", description: "" });
                  setQueryError(null);
                  setQueryResult(null);
                }}
                className="h-11"
              >
                {t("governance.data.integrations.explore.queries.dialog.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleSaveQuery}
                disabled={!currentQuery.name || !currentQuery.query.trim()}
                className="h-11"
              >
                <FileText className="mr-2 h-4 w-4" />
                {t("governance.data.integrations.explore.queries.dialog.save", "Guardar Consulta")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}
