'use client';

import React, { useState } from 'react';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Textarea } from '@/components/ui/textarea';
import { 
  Server, 
  Plus, 
  Settings,
  Play,
  Trash2,
  Eye,
  TestTube,
  CheckCircle,
  AlertCircle,
  Clock,
  Link,
  Globe
} from 'lucide-react';

interface ApiConfig {
  id: string;
  name: string;
  baseUrl: string;
  endpoints: string[];
  headers: Record<string, string>;
  authentication: 'none' | 'basic' | 'bearer' | 'api_key';
  authConfig?: Record<string, string>;
  schedule: 'manual' | 'hourly' | 'daily' | 'weekly' | 'custom';
  cronExpression?: string;
  status: 'active' | 'inactive' | 'error';
  lastSync?: Date;
}

interface ApiManagerProps {
  projectId?: string;
}

export default function ApiManager({ projectId }: ApiManagerProps) {
  const [configs, setConfigs] = useState<ApiConfig[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [showConfigModal, setShowConfigModal] = useState(false);
  const [showScheduleModal, setShowScheduleModal] = useState(false);
  const [selectedConfig, setSelectedConfig] = useState<ApiConfig | null>(null);
  const [isDiscovering, setIsDiscovering] = useState(false);
  const [discoveredEndpoints, setDiscoveredEndpoints] = useState<string[]>([]);
  const [activeTab, setActiveTab] = useState('configs');

  const [formData, setFormData] = useState<Omit<ApiConfig, 'id' | 'status' | 'lastSync'>>({
    name: '',
    baseUrl: '',
    endpoints: [],
    headers: {},
    authentication: 'none',
    authConfig: {},
    schedule: 'manual',
    cronExpression: ''
  });

  const handleSubmit = () => {
    const newConfig: ApiConfig = {
      ...formData,
      id: Date.now().toString(),
      status: 'inactive',
      lastSync: undefined
    };
    setConfigs([...configs, newConfig]);
    setShowModal(false);
    resetForm();
  };

  const resetForm = () => {
    setFormData({
      name: '',
      baseUrl: '',
      endpoints: [],
      headers: {},
      authentication: 'none',
      authConfig: {},
      schedule: 'manual',
      cronExpression: ''
    });
  };

  const handleDelete = (id: string) => {
    if (confirm('¿Estás seguro de que quieres eliminar esta configuración de API?')) {
      setConfigs(configs.filter(config => config.id !== id));
    }
  };

  const handleOpenConfig = (config: ApiConfig) => {
    setSelectedConfig(config);
    setShowConfigModal(true);
  };

  const handleSaveConfig = () => {
    if (selectedConfig) {
      setConfigs(configs.map(config => 
        config.id === selectedConfig.id ? selectedConfig : config
      ));
      setShowConfigModal(false);
      setSelectedConfig(null);
    }
  };

  const discoverEndpoints = async (baseUrl: string) => {
    setIsDiscovering(true);
    try {
      // Simulación de descubrimiento de endpoints
      await new Promise(resolve => setTimeout(resolve, 2000));
      const mockEndpoints = [
        '/users',
        '/users/{id}',
        '/products',
        '/products/{id}',
        '/orders',
        '/orders/{id}',
        '/auth/login',
        '/auth/logout'
      ];
      setDiscoveredEndpoints(mockEndpoints);
    } catch (error) {
      console.error('Error discovering endpoints:', error);
    } finally {
      setIsDiscovering(false);
    }
  };

  const testApiConnection = async (config: ApiConfig) => {
    try {
      // Simulación de prueba de conexión
      await new Promise(resolve => setTimeout(resolve, 1500));
      alert('Conexión exitosa a la API!');
    } catch (error) {
      alert('Error de conexión: ' + error);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'active': return 'bg-green-100 text-green-800';
      case 'inactive': return 'bg-gray-100 text-gray-800';
      case 'error': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getAuthLabel = (auth: string) => {
    switch (auth) {
      case 'none': return 'Sin autenticación';
      case 'basic': return 'Basic Auth';
      case 'bearer': return 'Bearer Token';
      case 'api_key': return 'API Key';
      default: return auth;
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestión de APIs REST</h1>
          <p className="text-gray-600 mt-1">Configurar conexiones y extraer datos de APIs REST</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => setShowConfigModal(true)}>
            <Settings className="w-4 h-4 mr-2" />
            Configuración
          </Button>
          <Button size="sm" onClick={() => setShowModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Nueva API
          </Button>
        </div>
      </div>

      {/* Navegación por tabs */}
      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          <button
            onClick={() => setActiveTab('configs')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'configs'
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            APIs
          </button>
          <button
            onClick={() => setActiveTab('sync')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'sync'
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Sincronización
          </button>
        </nav>
      </div>

      {/* Contenido de tabs */}
      <div className="space-y-4">
        {activeTab === 'configs' && (
          <div className="space-y-4">
            {configs.length === 0 ? (
              <Card>
                <CardBody className="text-center py-12">
                  <Server className="w-16 h-16 mx-auto mb-4 text-gray-300" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">No hay APIs configuradas</h3>
                  <p className="text-gray-600 mb-4">Agrega tu primera configuración de API REST para comenzar</p>
                  <Button onClick={() => setShowModal(true)}>
                    <Plus className="w-4 h-4 mr-2" />
                    Agregar API
                  </Button>
                </CardBody>
              </Card>
            ) : (
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {configs.map((config) => (
                  <Card key={config.id} className="hover:shadow-md transition-shadow">
                    <CardHeader>
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                          <Server className="w-6 h-6 text-purple-500" />
                          <div>
                            <CardTitle className="text-lg">{config.name}</CardTitle>
                            <p className="text-sm text-gray-600">{config.baseUrl}</p>
                          </div>
                        </div>
                        <Badge className={getStatusColor(config.status)}>
                          {config.status}
                        </Badge>
                      </div>
                    </CardHeader>
                    <CardBody>
                      <div className="space-y-3">
                        <div className="grid grid-cols-2 gap-4 text-sm">
                          <div>
                            <span className="text-gray-500">Endpoints:</span>
                            <p className="font-medium">{config.endpoints.length}</p>
                          </div>
                          <div>
                            <span className="text-gray-500">Autenticación:</span>
                            <p className="font-medium">{getAuthLabel(config.authentication)}</p>
                          </div>
                        </div>
                        
                        {config.lastSync && (
                          <div className="text-sm text-gray-500">
                            Última sincronización: {new Date(config.lastSync).toLocaleString()}
                          </div>
                        )}

                        <div className="flex items-center gap-2">
                          <Button 
                            size="sm" 
                            variant="outline"
                            onClick={() => testApiConnection(config)}
                          >
                            <TestTube className="w-4 h-4 mr-2" />
                            Probar
                          </Button>
                          <Button size="sm" variant="outline">
                            <Play className="w-4 h-4 mr-2" />
                            Sincronizar
                          </Button>
                          <Button size="sm" variant="outline">
                            <Eye className="w-4 h-4 mr-2" />
                            Ver Datos
                          </Button>
                          <Button 
                            size="sm" 
                            variant="outline" 
                            className="text-red-600"
                            onClick={() => handleDelete(config.id)}
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    </CardBody>
                  </Card>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'sync' && (
          <Card>
            <CardHeader>
              <CardTitle>Sincronización de APIs</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div className="text-center py-8">
                  <Clock className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                  <p className="text-gray-600">Configura tareas programadas para sincronización automática de APIs</p>
                  <Button className="mt-4" onClick={() => setShowScheduleModal(true)}>
                    <Plus className="w-4 h-4 mr-2" />
                    Crear Tarea Programada
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        )}
      </div>

      {/* Modal para nueva API */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <CardHeader>
              <CardTitle>Nueva Configuración de API REST</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Nombre de la API</label>
                    <Input
                      value={formData.name}
                      onChange={(e) => setFormData({...formData, name: e.target.value})}
                      placeholder="Mi API"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">URL Base</label>
                    <Input
                      value={formData.baseUrl}
                      onChange={(e) => setFormData({...formData, baseUrl: e.target.value})}
                      placeholder="https://api.ejemplo.com"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Tipo de Autenticación</label>
                  <select 
                    className="w-full p-2 border rounded-md"
                    value={formData.authentication}
                    onChange={(e) => setFormData({...formData, authentication: e.target.value as any})}
                  >
                    <option value="none">Sin autenticación</option>
                    <option value="basic">Basic Auth</option>
                    <option value="bearer">Bearer Token</option>
                    <option value="api_key">API Key</option>
                  </select>
                </div>

                {formData.authentication === 'basic' && (
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Usuario</label>
                      <Input
                        placeholder="usuario"
                        onChange={(e) => setFormData({
                          ...formData, 
                          authConfig: {...formData.authConfig, username: e.target.value}
                        })}
                      />
                    </div>
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Contraseña</label>
                      <Input
                        type="password"
                        placeholder="contraseña"
                        onChange={(e) => setFormData({
                          ...formData, 
                          authConfig: {...formData.authConfig, password: e.target.value}
                        })}
                      />
                    </div>
                  </div>
                )}

                {formData.authentication === 'bearer' && (
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Bearer Token</label>
                    <Input
                      placeholder="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                      onChange={(e) => setFormData({
                        ...formData, 
                        authConfig: {...formData.authConfig, token: e.target.value}
                      })}
                    />
                  </div>
                )}

                {formData.authentication === 'api_key' && (
                  <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Nombre del Header</label>
                      <Input
                        placeholder="X-API-Key"
                        onChange={(e) => setFormData({
                          ...formData, 
                          authConfig: {...formData.authConfig, headerName: e.target.value}
                        })}
                      />
                    </div>
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Valor de la API Key</label>
                      <Input
                        placeholder="tu-api-key-aqui"
                        onChange={(e) => setFormData({
                          ...formData, 
                          authConfig: {...formData.authConfig, apiKey: e.target.value}
                        })}
                      />
                    </div>
                  </div>
                )}

                <div className="space-y-2">
                  <label className="text-sm font-medium">Headers Personalizados (JSON)</label>
                  <Textarea
                    placeholder='{"Content-Type": "application/json", "Accept": "application/json"}'
                    rows={3}
                    onChange={(e) => {
                      try {
                        const headers = JSON.parse(e.target.value);
                        setFormData({...formData, headers});
                      } catch (error) {
                        // Ignorar errores de JSON inválido
                      }
                    }}
                  />
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Endpoints</label>
                  <Textarea
                    placeholder="/users&#10;/products&#10;/orders"
                    rows={3}
                    onChange={(e) => {
                      const endpoints = e.target.value.split('\n').filter(endpoint => endpoint.trim());
                      setFormData({...formData, endpoints});
                    }}
                  />
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Programación</label>
                  <select 
                    className="w-full p-2 border rounded-md"
                    value={formData.schedule}
                    onChange={(e) => setFormData({...formData, schedule: e.target.value as any})}
                  >
                    <option value="manual">Manual</option>
                    <option value="hourly">Cada hora</option>
                    <option value="daily">Diario</option>
                    <option value="weekly">Semanal</option>
                    <option value="custom">Personalizado (Cron)</option>
                  </select>
                </div>

                {formData.schedule === 'custom' && (
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Expresión Cron</label>
                    <Input
                      placeholder="0 9 * * *"
                      value={formData.cronExpression || ''}
                      onChange={(e) => setFormData({...formData, cronExpression: e.target.value})}
                    />
                  </div>
                )}

                <div className="flex gap-2">
                  <Button 
                    onClick={() => discoverEndpoints(formData.baseUrl)}
                    disabled={isDiscovering || !formData.baseUrl}
                    variant="outline"
                  >
                    {isDiscovering ? (
                      <>
                        <Clock className="w-4 h-4 mr-2 animate-spin" />
                        Descubriendo...
                      </>
                    ) : (
                      <>
                        <Globe className="w-4 h-4 mr-2" />
                        Descubrir Endpoints
                      </>
                    )}
                  </Button>
                </div>

                {discoveredEndpoints.length > 0 && (
                  <div className="border rounded-lg p-4 bg-gray-50">
                    <h4 className="font-medium mb-2">Endpoints descubiertos:</h4>
                    <div className="space-y-1">
                      {discoveredEndpoints.map((endpoint, index) => (
                        <div key={index} className="text-sm text-gray-700">
                          {endpoint}
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                <div className="flex justify-end gap-2">
                  <Button variant="outline" onClick={() => setShowModal(false)}>
                    Cancelar
                  </Button>
                  <Button onClick={handleSubmit}>
                    Guardar API
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      )}

      {/* Modal de configuración global */}
      {showConfigModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">Configuración Global de APIs</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Timeout por defecto (segundos)</label>
                <input type="number" className="w-full p-2 border rounded" defaultValue="30" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Reintentos automáticos</label>
                <input type="number" className="w-full p-2 border rounded" defaultValue="3" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Rate limiting (requests/min)</label>
                <input type="number" className="w-full p-2 border rounded" defaultValue="60" />
              </div>
              <div className="flex items-center space-x-2">
                <input type="checkbox" id="enableLogs" defaultChecked />
                <label htmlFor="enableLogs" className="text-sm">Habilitar logs detallados</label>
              </div>
            </div>
            <div className="flex gap-2 mt-6">
              <Button onClick={() => setShowConfigModal(false)} className="flex-1">
                Guardar
              </Button>
              <Button variant="outline" onClick={() => setShowConfigModal(false)} className="flex-1">
                Cancelar
              </Button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de tarea programada */}
      {showScheduleModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">Crear Tarea Programada</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Nombre de la tarea</label>
                <input type="text" className="w-full p-2 border rounded" placeholder="Sincronización diaria" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">APIs a sincronizar</label>
                <select className="w-full p-2 border rounded">
                  <option value="">Seleccionar APIs...</option>
                  {configs.map(config => (
                    <option key={config.id} value={config.id}>{config.name}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Frecuencia</label>
                <select className="w-full p-2 border rounded">
                  <option value="hourly">Cada hora</option>
                  <option value="daily">Diario</option>
                  <option value="weekly">Semanal</option>
                  <option value="custom">Personalizado (cron)</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Hora de ejecución</label>
                <input type="time" className="w-full p-2 border rounded" defaultValue="02:00" />
              </div>
              <div className="flex items-center space-x-2">
                <input type="checkbox" id="enableNotifications" defaultChecked />
                <label htmlFor="enableNotifications" className="text-sm">Notificar al completar</label>
              </div>
            </div>
            <div className="flex gap-2 mt-6">
              <Button onClick={() => setShowScheduleModal(false)} className="flex-1">
                Crear Tarea
              </Button>
              <Button variant="outline" onClick={() => setShowScheduleModal(false)} className="flex-1">
                Cancelar
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
} 