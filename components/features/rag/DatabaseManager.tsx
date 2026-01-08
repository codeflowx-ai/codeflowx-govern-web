'use client';

import React, { useState } from 'react';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Textarea } from '@/components/ui/textarea';
import { 
  Database, 
  Plus, 
  Settings,
  Play,
  Trash2,
  Eye,
  TestTube,
  CheckCircle,
  AlertCircle,
  Clock
} from 'lucide-react';

interface DatabaseConfig {
  id: string;
  name: string;
  dbType: string;
  host: string;
  port: number;
  database: string;
  username: string;
  password: string;
  tables: string[];
  reverseMapping: boolean;
  sqlQuery?: string;
  status: 'active' | 'inactive' | 'error';
  lastSync?: Date;
}

interface DatabaseManagerProps {
  projectId?: string;
}

export default function DatabaseManager({ projectId }: DatabaseManagerProps) {
  const [configs, setConfigs] = useState<DatabaseConfig[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [showScheduleModal, setShowScheduleModal] = useState(false);
  const [isTestingConnection, setIsTestingConnection] = useState(false);
  const [isVerifyingQuery, setIsVerifyingQuery] = useState(false);
  const [queryResults, setQueryResults] = useState<any>(null);
  const [availableTables, setAvailableTables] = useState<string[]>([]);
  const [activeTab, setActiveTab] = useState('configs');
  const [scheduledTasks, setScheduledTasks] = useState<any[]>([]);
  const [newTask, setNewTask] = useState({
    name: '',
    databaseId: '',
    frequency: 'daily',
    cronExpression: '0 0 * * *',
    enabled: true
  });

  const [formData, setFormData] = useState<Omit<DatabaseConfig, 'id' | 'status' | 'lastSync'>>({
    name: '',
    dbType: 'postgresql',
    host: '',
    port: 5432,
    database: '',
    username: '',
    password: '',
    tables: [],
    reverseMapping: true,
    sqlQuery: ''
  });

  const handleSubmit = () => {
    const newConfig: DatabaseConfig = {
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
      dbType: 'postgresql',
      host: '',
      port: 5432,
      database: '',
      username: '',
      password: '',
      tables: [],
      reverseMapping: true,
      sqlQuery: ''
    });
  };

  const handleDelete = (id: string) => {
    if (confirm('¿Estás seguro de que quieres eliminar esta configuración de base de datos?')) {
      setConfigs(configs.filter(config => config.id !== id));
    }
  };

  const handleCreateTask = () => {
    const task = {
      ...newTask,
      id: Date.now().toString(),
      createdAt: new Date()
    };
    setScheduledTasks([...scheduledTasks, task]);
    setShowScheduleModal(false);
    setNewTask({
      name: '',
      databaseId: '',
      frequency: 'daily',
      cronExpression: '0 0 * * *',
      enabled: true
    });
  };

  const testConnection = async () => {
    setIsTestingConnection(true);
    try {
      // Simulación de prueba de conexión
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // Simular obtención de tablas disponibles
      const mockTables = [
        'users',
        'products',
        'orders',
        'order_items',
        'categories',
        'reviews',
        'v_user_summary',
        'v_product_stats'
      ];
      setAvailableTables(mockTables);
      
      alert('Conexión exitosa! Se encontraron ' + mockTables.length + ' tablas/vistas.');
    } catch (error) {
      alert('Error de conexión: ' + error);
    } finally {
      setIsTestingConnection(false);
    }
  };

  const verifyQuery = async () => {
    if (!formData.sqlQuery?.trim()) {
      alert('Por favor ingresa una consulta SQL');
      return;
    }

    setIsVerifyingQuery(true);
    try {
      // Simulación de verificación de consulta
      await new Promise(resolve => setTimeout(resolve, 1500));
      
      // Simular resultados de consulta
      const mockResults = {
        columns: ['id', 'name', 'email', 'created_at'],
        data: [
          [1, 'Juan Pérez', 'juan@ejemplo.com', '2024-01-15'],
          [2, 'María García', 'maria@ejemplo.com', '2024-01-16'],
          [3, 'Carlos López', 'carlos@ejemplo.com', '2024-01-17']
        ],
        rowCount: 3
      };
      setQueryResults(mockResults);
    } catch (error) {
      alert('Error en la consulta: ' + error);
    } finally {
      setIsVerifyingQuery(false);
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

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestión de Bases de Datos</h1>
          <p className="text-gray-600 mt-1">Configurar conexiones y extraer datos de bases de datos</p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm">
            <Settings className="w-4 h-4 mr-2" />
            Configuración
          </Button>
          <Button size="sm" onClick={() => setShowModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Nueva Conexión
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
            Conexiones
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
                  <Database className="w-16 h-16 mx-auto mb-4 text-gray-300" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">No hay conexiones configuradas</h3>
                  <p className="text-gray-600 mb-4">Agrega tu primera conexión de base de datos para comenzar</p>
                  <Button onClick={() => setShowModal(true)}>
                    <Plus className="w-4 h-4 mr-2" />
                    Agregar Conexión
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
                          <Database className="w-6 h-6 text-blue-500" />
                          <div>
                            <CardTitle className="text-lg">{config.name}</CardTitle>
                            <p className="text-sm text-gray-600">{config.host}:{config.port}/{config.database}</p>
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
                            <span className="text-gray-500">Usuario:</span>
                            <p className="font-medium">{config.username}</p>
                          </div>
                          <div>
                            <span className="text-gray-500">Tablas:</span>
                            <p className="font-medium">{config.tables.length}</p>
                          </div>
                        </div>
                        
                        {config.lastSync && (
                          <div className="text-sm text-gray-500">
                            Última sincronización: {new Date(config.lastSync).toLocaleString()}
                          </div>
                        )}

                        <div className="flex items-center gap-2">
                          <Button size="sm" variant="outline">
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
          <div className="space-y-4">
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle>Sincronización de Datos</CardTitle>
                  <Button onClick={() => setShowScheduleModal(true)}>
                    <Plus className="w-4 h-4 mr-2" />
                    Crear Tarea Programada
                  </Button>
                </div>
              </CardHeader>
              <CardBody>
                {scheduledTasks.length === 0 ? (
                  <div className="text-center py-8">
                    <Clock className="w-12 h-12 text-gray-400 mx-auto mb-4" />
                    <p className="text-gray-600">No hay tareas programadas configuradas</p>
                    <p className="text-sm text-gray-500">Crea una tarea para sincronización automática</p>
                  </div>
                ) : (
                  <div className="space-y-4">
                    {scheduledTasks.map((task) => (
                      <div key={task.id} className="flex items-center justify-between p-4 border rounded-lg">
                        <div>
                          <h4 className="font-medium">{task.name}</h4>
                          <p className="text-sm text-gray-600">
                            Frecuencia: {task.frequency} | Cron: {task.cronExpression}
                          </p>
                          <p className="text-xs text-gray-500">
                            Creada: {new Date(task.createdAt).toLocaleDateString()}
                          </p>
                        </div>
                        <div className="flex items-center gap-2">
                          <Badge variant={task.enabled ? "primary" : "secondary"}>
                            {task.enabled ? "Activa" : "Inactiva"}
                          </Badge>
                          <Button size="sm" variant="outline">
                            <Play className="w-4 h-4" />
                          </Button>
                          <Button size="sm" variant="outline" className="text-red-600">
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </CardBody>
            </Card>
          </div>
        )}
      </div>

      {/* Modal para nueva conexión */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <CardHeader>
              <CardTitle>Nueva Conexión de Base de Datos</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Nombre de la conexión</label>
                    <Input
                      value={formData.name}
                      onChange={(e) => setFormData({...formData, name: e.target.value})}
                      placeholder="Mi Base de Datos"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Tipo de Base de Datos</label>
                    <select 
                      value={formData.dbType || 'postgresql'}
                      onChange={(e) => setFormData({...formData, dbType: e.target.value})}
                      className="w-full p-2 border border-gray-300 rounded-md"
                    >
                      <option value="postgresql">PostgreSQL</option>
                      <option value="mysql">MySQL</option>
                      <option value="oracle">Oracle</option>
                      <option value="sqlserver">SQL Server</option>
                      <option value="sap">SAP HANA</option>
                      <option value="mongodb">MongoDB</option>
                      <option value="redis">Redis</option>
                      <option value="cassandra">Cassandra</option>
                      <option value="elasticsearch">Elasticsearch</option>
                    </select>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Host</label>
                    <Input
                      value={formData.host}
                      onChange={(e) => setFormData({...formData, host: e.target.value})}
                      placeholder="localhost"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Puerto</label>
                    <Input
                      type="number"
                      value={formData.port}
                      onChange={(e) => setFormData({...formData, port: parseInt(e.target.value)})}
                      placeholder="5432"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Base de datos</label>
                    <Input
                      value={formData.database}
                      onChange={(e) => setFormData({...formData, database: e.target.value})}
                      placeholder="mi_base_datos"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Usuario</label>
                    <Input
                      value={formData.username}
                      onChange={(e) => setFormData({...formData, username: e.target.value})}
                      placeholder="usuario"
                    />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Contraseña</label>
                    <Input
                      type="password"
                      value={formData.password}
                      onChange={(e) => setFormData({...formData, password: e.target.value})}
                      placeholder="contraseña"
                    />
                  </div>
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Consulta SQL (opcional)</label>
                  <Textarea
                    value={formData.sqlQuery || ''}
                    onChange={(e) => setFormData({...formData, sqlQuery: e.target.value})}
                    placeholder="SELECT * FROM users WHERE active = true"
                    rows={3}
                  />
                </div>

                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="reverseMapping"
                    checked={formData.reverseMapping}
                    onChange={(e) => setFormData({...formData, reverseMapping: e.target.checked})}
                    className="rounded"
                  />
                  <label htmlFor="reverseMapping" className="text-sm">
                    Habilitar mapeo inverso
                  </label>
                </div>

                <div className="flex gap-2">
                  <Button 
                    onClick={testConnection}
                    disabled={isTestingConnection}
                    variant="outline"
                  >
                    {isTestingConnection ? (
                      <>
                        <Clock className="w-4 h-4 mr-2 animate-spin" />
                        Probando...
                      </>
                    ) : (
                      <>
                        <TestTube className="w-4 h-4 mr-2" />
                        Probar Conexión
                      </>
                    )}
                  </Button>
                  <Button 
                    onClick={verifyQuery}
                    disabled={isVerifyingQuery || !formData.sqlQuery}
                    variant="outline"
                  >
                    {isVerifyingQuery ? (
                      <>
                        <Clock className="w-4 h-4 mr-2 animate-spin" />
                        Verificando...
                      </>
                    ) : (
                      <>
                        <CheckCircle className="w-4 h-4 mr-2" />
                        Verificar Query
                      </>
                    )}
                  </Button>
                </div>

                {queryResults && (
                  <div className="border rounded-lg p-4 bg-gray-50">
                    <h4 className="font-medium mb-2">Resultados de la consulta:</h4>
                    <div className="text-sm">
                      <p>Columnas: {queryResults.columns.join(', ')}</p>
                      <p>Filas: {queryResults.rowCount}</p>
                    </div>
                  </div>
                )}

                <div className="flex justify-end gap-2">
                  <Button variant="outline" onClick={() => setShowModal(false)}>
                    Cancelar
                  </Button>
                  <Button onClick={handleSubmit}>
                    Guardar Conexión
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      )}

      {/* Modal para crear tarea programada */}
      {showScheduleModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md">
            <CardHeader>
              <CardTitle>Crear Tarea Programada</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium">Nombre de la tarea</label>
                  <Input
                    value={newTask.name}
                    onChange={(e) => setNewTask({...newTask, name: e.target.value})}
                    placeholder="Sincronización diaria"
                  />
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Base de datos</label>
                  <select 
                    value={newTask.databaseId}
                    onChange={(e) => setNewTask({...newTask, databaseId: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                  >
                    <option value="">Seleccionar base de datos</option>
                    {configs.map((config) => (
                      <option key={config.id} value={config.id}>
                        {config.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Frecuencia</label>
                  <select 
                    value={newTask.frequency}
                    onChange={(e) => setNewTask({...newTask, frequency: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                  >
                    <option value="hourly">Cada hora</option>
                    <option value="daily">Diaria</option>
                    <option value="weekly">Semanal</option>
                    <option value="monthly">Mensual</option>
                    <option value="custom">Personalizada</option>
                  </select>
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Expresión Cron</label>
                  <Input
                    value={newTask.cronExpression}
                    onChange={(e) => setNewTask({...newTask, cronExpression: e.target.value})}
                    placeholder="0 0 * * *"
                  />
                  <p className="text-xs text-gray-500">
                    Formato: minuto hora día mes día_semana
                  </p>
                </div>

                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="enabled"
                    checked={newTask.enabled}
                    onChange={(e) => setNewTask({...newTask, enabled: e.target.checked})}
                    className="rounded"
                  />
                  <label htmlFor="enabled" className="text-sm">
                    Tarea activa
                  </label>
                </div>

                <div className="flex justify-end gap-2">
                  <Button variant="outline" onClick={() => setShowScheduleModal(false)}>
                    Cancelar
                  </Button>
                  <Button onClick={handleCreateTask}>
                    Crear Tarea
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      )}
    </div>
  );
} 