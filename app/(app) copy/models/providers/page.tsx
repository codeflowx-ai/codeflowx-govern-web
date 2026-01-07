"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Eye, Key, Plus, Search, Server, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";

interface Provider {
  id: number;
  name: string;
  displayName: string;
  description: string;
  providerType: string; // EXTERNAL, INTERNAL
  status: string; // ACTIVE, INACTIVE, PENDING
  authType: string;
  baseUrl: string;
  credentials?: ProviderCredential[];
}

interface ProviderCredential {
  id: number;
  name: string;
  type: string;
  environment: string;
  status: string;
  projectId?: number;
  projectName?: string;
  expiresAt?: string;
  lastUsedAt?: string;
  usageCount: number;
}

interface Project {
  id: number;
  name: string;
}

export default function ProviderOverviewPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState<Provider[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [providerTypeFilter, setProviderTypeFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [authTypeFilter, setAuthTypeFilter] = useState<string>("ALL");
  const [selectedProvider, setSelectedProvider] = useState<Provider | null>(null);
  const [showCredentialModal, setShowCredentialModal] = useState(false);
  const [newCredential, setNewCredential] = useState({
    name: "",
    type: "",
    value: "",
    environment: "",
    projectId: "",
    expiresAt: "",
  });
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    pending: 0,
    inactive: 0,
  });

  useEffect(() => {
    loadData();
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      // TODO: Implementar llamada API
      const mockProjects: Project[] = [
        { id: 1, name: "Proyecto Demo" },
        { id: 2, name: "Proyecto RAG" },
        { id: 3, name: "Proyecto Compliance" },
      ];
      setProjects(mockProjects);
    } catch (error) {
      console.error("Error loading projects:", error);
    }
  };

  const loadData = async () => {
    try {
      setLoading(true);
      const mockData: Provider[] = [
        {
          id: 1,
          name: "openai",
          displayName: "OpenAI",
          description: "OpenAI API provider",
          providerType: "EXTERNAL",
          status: "ACTIVE",
          authType: "API_KEY",
          baseUrl: "https://api.openai.com",
          credentials: [
            {
              id: 1,
              name: "OpenAI Production Key",
              type: "API_KEY",
              environment: "PRODUCTION",
              status: "ACTIVE",
              projectId: 1,
              projectName: "Proyecto Demo",
              expiresAt: "2025-12-31",
              lastUsedAt: "2024-01-15T10:30:00",
              usageCount: 1250,
            },
            {
              id: 2,
              name: "OpenAI Dev Key",
              type: "API_KEY",
              environment: "DEVELOPMENT",
              status: "ACTIVE",
              usageCount: 45,
            },
          ],
        },
      ];
      setItems(mockData);
      setStats({
        total: mockData.length,
        active: 1,
        pending: 0,
        inactive: 0,
      });
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredItems = items.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const getProviderTypeBadge = (type: string) => {
    if (type === "EXTERNAL") {
      return <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50">EXTERNAL</Badge>;
    } else if (type === "INTERNAL") {
      return <Badge className="bg-purple-500/20 text-purple-400 border-purple-500/50">INTERNAL</Badge>;
    }
    return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">{type}</Badge>;
  };

  const getStatusBadge = (status: string) => {
    if (status === "ACTIVE") {
      return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">ACTIVE</Badge>;
    } else if (status === "INACTIVE") {
      return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">INACTIVE</Badge>;
    } else if (status === "PENDING") {
      return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">PENDING</Badge>;
    }
    return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">{status}</Badge>;
  };


  const handleCreateCredential = async () => {
    if (!newCredential.name || !newCredential.type || !newCredential.value || !newCredential.environment) {
      alert(t("governance.models.providers.modal.credential.create.validationError", "Por favor, complete todos los campos obligatorios"));
      return;
    }
    if (!selectedProvider) return;

    try {
      // TODO: Implementar llamada API
      const newCred: ProviderCredential = {
        id: (selectedProvider.credentials?.length || 0) + 1,
        name: newCredential.name,
        type: newCredential.type,
        environment: newCredential.environment,
        status: "ACTIVE",
        projectId: newCredential.projectId ? parseInt(newCredential.projectId) : undefined,
        projectName: newCredential.projectId ? projects.find(p => p.id === parseInt(newCredential.projectId))?.name : undefined,
        expiresAt: newCredential.expiresAt || undefined,
        usageCount: 0,
      };

      const updatedProvider = {
        ...selectedProvider,
        credentials: [...(selectedProvider.credentials || []), newCred],
      };

      setItems(items.map(item => item.id === selectedProvider.id ? updatedProvider : item));
      setShowCredentialModal(false);
      setSelectedProvider(null);
      setNewCredential({
        name: "",
        type: "",
        value: "",
        environment: "",
        projectId: "",
        expiresAt: "",
      });
    } catch (error) {
      console.error("Error creating credential:", error);
    }
  };

  const handleViewCredentials = (provider: Provider) => {
    setSelectedProvider(provider);
    setShowCredentialModal(true);
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Server className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.providers.title", "Proveedores de Modelos")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9 mb-2">
          {t("governance.models.providers.subtitle", "Gestión de proveedores de servicios de modelos")}
        </p>
      </div>

      {/* Action Buttons */}
      <div className="flex items-center justify-end">
        <Button
          onClick={() => { window.location.href = "/models/providers/new"; }}
          className="bg-primary hover:bg-primary/90"
        >
          <Plus className="h-4 w-4 mr-2" />
          {t("governance.models.providers.addButton", "Agregar Proveedor")}
        </Button>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.providers.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.providers.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.active}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.providers.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{stats.pending}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.providers.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.inactive}</h2>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.providers.filters.search", "Filtros")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.providers.filters.search", "Búsqueda")}
              </label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.models.providers.filters.searchPlaceholder", "Buscar...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.providers.filters.providerType", "Tipo de Proveedor")}
              </label>
              <Select value={providerTypeFilter} onValueChange={setProviderTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.providers.filters.providerTypePlaceholder", "Filtrar por Provider Type")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.providers.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="EXTERNAL">EXTERNAL</SelectItem>
                  <SelectItem value="INTERNAL">INTERNAL</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.providers.filters.status", "Estado")}
              </label>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.providers.filters.statusPlaceholder", "Filtrar por Status")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.providers.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                  <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                  <SelectItem value="PENDING">PENDING</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.providers.filters.authType", "Tipo de Autenticación")}
              </label>
              <Select value={authTypeFilter} onValueChange={setAuthTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.providers.filters.authTypePlaceholder", "Filtrar por Authtype")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.providers.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="API_KEY">API_KEY</SelectItem>
                  <SelectItem value="OAUTH">OAUTH</SelectItem>
                  <SelectItem value="BEARER_TOKEN">BEARER_TOKEN</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>
      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.providers.table.title", "Listado")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.providers.table.noResults", "No hay datos")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.providers.table.id", "ID")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.name", "Name")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.displayName", "Displayname")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.description", "Description")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.providerType", "Provider Type")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.status", "Estado")}</th>
                    <th className="text-left p-2">{t("governance.models.providers.table.baseUrl", "Baseurl")}</th>
                    <th className="text-center p-2">{t("governance.models.providers.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.name}</td>
                      <td className="p-2">{item.displayName}</td>
                      <td className="p-2">{item.description}</td>
                      <td className="p-2">{getProviderTypeBadge(item.providerType)}</td>
                      <td className="p-2">{getStatusBadge(item.status)}</td>
                      <td className="p-2 font-mono text-sm">{item.baseUrl}</td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            title={t("governance.models.providers.credentials.title", "Credenciales")}
                            onClick={() => handleViewCredentials(item)}
                          >
                            <Key className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            title={t("common.edit", "Editar")}
                            onClick={() => { window.location.href = `/models/providers/${item.id}?edit=true`; }}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm" className="text-red-400 hover:text-red-300" title={t("common.delete", "Eliminar")}>
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
      {/* Modal de Gestión de Credenciales */}
      <SimpleModal
        isOpen={showCredentialModal}
        onClose={() => {
          setShowCredentialModal(false);
          setSelectedProvider(null);
          setNewCredential({
            name: "",
            type: "",
            value: "",
            environment: "",
            projectId: "",
            expiresAt: "",
          });
        }}
        title={selectedProvider ? `${t("governance.models.providers.credentials.title", "Credenciales")} - ${selectedProvider.displayName}` : t("governance.models.providers.credentials.title", "Credenciales")}
        maxWidth="max-w-4xl"
      >
        <div className="space-y-4">
          {/* Botón para agregar credencial */}
          <div className="flex justify-end">
            <Button
              onClick={() => {
                // Abrir modal de creación de credencial
                // Por simplicidad, lo haremos inline
              }}
              className="bg-primary hover:bg-primary/90"
              size="sm"
            >
              <Plus className="h-4 w-4 mr-2" />
              {t("governance.models.providers.credentials.addButton", "Agregar Credencial")}
            </Button>
          </div>
          {/* Formulario de nueva credencial */}
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="text-lg">{t("governance.models.providers.modal.credential.create.title", "Agregar Credencial")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.name", "Nombre de la Credencial")} *
                  </label>
                  <Input
                    placeholder={t("governance.models.providers.modal.credential.create.namePlaceholder", "Ingrese un nombre para la credencial")}
                    value={newCredential.name}
                    onChange={(e) => setNewCredential({ ...newCredential, name: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.type", "Tipo de Credencial")} *
                  </label>
                  <Select value={newCredential.type} onValueChange={(value) => setNewCredential({ ...newCredential, type: value })}>
                    <SelectTrigger>
                      <SelectValue placeholder={t("governance.models.providers.modal.credential.create.typePlaceholder", "Seleccione el tipo")} />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="API_KEY">API_KEY</SelectItem>
                      <SelectItem value="ACCESS_TOKEN">ACCESS_TOKEN</SelectItem>
                      <SelectItem value="SECRET_KEY">SECRET_KEY</SelectItem>
                      <SelectItem value="BEARER_TOKEN">BEARER_TOKEN</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.value", "Valor (API Key, Token, etc.)")} *
                  </label>
                  <Input
                    type="password"
                    placeholder={t("governance.models.providers.modal.credential.create.valuePlaceholder", "Ingrese el valor de la credencial")}
                    value={newCredential.value}
                    onChange={(e) => setNewCredential({ ...newCredential, value: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.environment", "Ambiente")} *
                  </label>
                  <Select value={newCredential.environment} onValueChange={(value) => setNewCredential({ ...newCredential, environment: value })}>
                    <SelectTrigger>
                      <SelectValue placeholder={t("governance.models.providers.modal.credential.create.environmentPlaceholder", "Seleccione el ambiente")} />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="PRODUCTION">PRODUCTION</SelectItem>
                      <SelectItem value="DEVELOPMENT">DEVELOPMENT</SelectItem>
                      <SelectItem value="STAGING">STAGING</SelectItem>
                      <SelectItem value="TESTING">TESTING</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.project", "Proyecto (Opcional)")}
                  </label>
                  <Select value={newCredential.projectId} onValueChange={(value) => setNewCredential({ ...newCredential, projectId: value })}>
                    <SelectTrigger>
                      <SelectValue placeholder={t("governance.models.providers.modal.credential.create.projectPlaceholder", "Seleccione un proyecto o deje vacío para global")} />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="">{t("governance.models.providers.modal.credential.create.projectGlobal", "Global (Sin proyecto)")}</SelectItem>
                      {projects.map((project) => (
                        <SelectItem key={project.id} value={project.id.toString()}>
                          {project.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.credential.create.expiresAt", "Fecha de Expiración (Opcional)")}
                  </label>
                  <Input
                    type="date"
                    placeholder={t("governance.models.providers.modal.credential.create.expiresAtPlaceholder", "Seleccione fecha de expiración")}
                    value={newCredential.expiresAt}
                    onChange={(e) => setNewCredential({ ...newCredential, expiresAt: e.target.value })}
                  />
                </div>
              </div>
              <div className="flex justify-end gap-2 pt-4">
                <Button
                  variant="outline"
                  onClick={() => {
                    setNewCredential({
                      name: "",
                      type: "",
                      value: "",
                      environment: "",
                      projectId: "",
                      expiresAt: "",
                    });
                  }}
                >
                  {t("common.cancel", "Cancelar")}
                </Button>
                <Button onClick={handleCreateCredential} className="bg-primary hover:bg-primary/90">
                  {t("governance.models.providers.modal.credential.create.createButton", "Agregar Credencial")}
                </Button>
              </div>
            </CardContent>
          </Card>
          {/* Lista de credenciales existentes */}
          {selectedProvider && selectedProvider.credentials && selectedProvider.credentials.length > 0 ? (
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.providers.credentials.title", "Credenciales")}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.type", "Tipo")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.environment", "Ambiente")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.status", "Estado")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.project", "Proyecto")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.expiresAt", "Expira")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.lastUsed", "Último Uso")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.usageCount", "Usos")}</th>
                        <th className="text-center p-2">{t("governance.models.providers.credentials.table.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {selectedProvider.credentials.map((cred) => (
                        <tr key={cred.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{cred.name}</td>
                          <td className="p-2">{cred.type}</td>
                          <td className="p-2">{cred.environment}</td>
                          <td className="p-2">{getStatusBadge(cred.status)}</td>
                          <td className="p-2">{cred.projectName || <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.expiresAt ? new Date(cred.expiresAt).toLocaleDateString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.lastUsedAt ? new Date(cred.lastUsedAt).toLocaleString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.usageCount}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button variant="ghost" size="sm" title={t("common.view", "Ver")}>
                                <Eye className="w-4 h-4" />
                              </Button>
                              <Button variant="ghost" size="sm" className="text-red-400 hover:text-red-300" title={t("common.delete", "Eliminar")}>
                                <Trash2 className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </CardContent>
            </Card>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.providers.credentials.noCredentials", "No hay credenciales registradas")}
            </div>
          )}
        </div>
      </SimpleModal>
    </div>
  );
}


