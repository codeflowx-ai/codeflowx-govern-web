'use client';

import React, { useState, useEffect } from 'react';
import { useTranslation } from '@/app/config/i18n';
import { useRouter } from 'next/navigation';
import { calculationParameterService } from '../../services/calculationParameterService';
import { CalculationParameter, MICROSERVICES } from '@/types/calculation-parameter';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { RefreshCw, Edit, Power, PowerOff, Globe, Building2 } from 'lucide-react';
import ParameterListFilters from './ParameterListFilters';

export const ParameterList: React.FC = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const [parameters, setParameters] = useState<CalculationParameter[]>([]);
  const [loading, setLoading] = useState(false);
  const [filters, setFilters] = useState<{
    microservice?: string;
    parameterKey?: string;
    organizationId?: string;
  }>({});

  useEffect(() => {
    loadParameters();
  }, [filters]);

  const loadParameters = async () => {
    setLoading(true);
    try {
      const data = await calculationParameterService.list(filters);
      setParameters(data);
    } catch (error) {
      console.error('Error cargando parámetros:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleActive = async (id: number, active: boolean) => {
    try {
      await calculationParameterService.toggleActive(id, !active);
      loadParameters();
    } catch (error) {
      console.error('Error actualizando estado:', error);
      alert(t('governance.calculationParameters.errors.toggleError', 'Error al actualizar estado del parámetro'));
    }
  };

  return (
    <div className="space-y-4">
      {/* Filtros */}
      <Card>
        <CardHeader>
          <CardTitle>{t('governance.calculationParameters.filters.title', 'Filtros')}</CardTitle>
        </CardHeader>
        <CardBody>
          <ParameterListFilters
            filters={filters}
            onFiltersChange={setFilters}
          />
        </CardBody>
      </Card>

      {/* Lista de Parámetros */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>
              {t('governance.calculationParameters.list.title', 'Parámetros de Cálculo')}
            </CardTitle>
            <Button onClick={loadParameters} variant="outline" size="sm" disabled={loading}>
              <RefreshCw className={`h-4 w-4 mr-2 ${loading ? 'animate-spin' : ''}`} />
              {t('common.refresh', 'Actualizar')}
            </Button>
          </div>
        </CardHeader>
        <CardBody>
          {loading ? (
            <div className="text-center py-8">
              <RefreshCw className="h-8 w-8 animate-spin mx-auto text-muted-foreground" />
              <p className="mt-2 text-sm text-muted-foreground">
                {t('common.loading', 'Cargando...')}
              </p>
            </div>
          ) : parameters.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-muted-foreground">
                {t('governance.calculationParameters.list.empty', 'No se encontraron parámetros')}
              </p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b bg-muted/50">
                    <th className="text-left p-3 font-medium">
                      {t('governance.calculationParameters.table.microservice', 'Microservicio')}
                    </th>
                    <th className="text-left p-3 font-medium">
                      {t('governance.calculationParameters.table.key', 'Clave')}
                    </th>
                    <th className="text-left p-3 font-medium">
                      {t('governance.calculationParameters.table.organization', 'Organización')}
                    </th>
                    <th className="text-left p-3 font-medium">
                      {t('governance.calculationParameters.table.version', 'Versión')}
                    </th>
                    <th className="text-center p-3 font-medium">
                      {t('governance.calculationParameters.table.active', 'Activo')}
                    </th>
                    <th className="text-left p-3 font-medium">
                      {t('governance.calculationParameters.table.actions', 'Acciones')}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {parameters.map(param => (
                    <tr key={param.id} className="border-b hover:bg-muted/50 transition-colors">
                      <td className="p-3">
                        <div className="font-medium">{param.microservice}</div>
                      </td>
                      <td className="p-3">
                        <code className="text-xs bg-muted px-2 py-1 rounded">{param.parameterKey}</code>
                      </td>
                      <td className="p-3">
                        {param.organizationId ? (
                          <div className="flex items-center gap-2">
                            <Building2 className="h-4 w-4 text-muted-foreground" />
                            <span className="text-xs text-muted-foreground truncate max-w-[200px]">
                              {param.organizationId}
                            </span>
                          </div>
                        ) : (
                          <Badge variant="outline" className="bg-blue-50 text-blue-700 border-blue-200">
                            <Globe className="h-3 w-3 mr-1" />
                            {t('governance.calculationParameters.global', 'Global')}
                          </Badge>
                        )}
                      </td>
                      <td className="p-3">
                        <Badge variant="outline">v{param.version}</Badge>
                      </td>
                      <td className="p-3 text-center">
                        <Badge
                          className={
                            param.active
                              ? 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300'
                              : 'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300'
                          }
                        >
                          {param.active
                            ? t('common.active', 'Activo')
                            : t('common.inactive', 'Inactivo')}
                        </Badge>
                      </td>
                      <td className="p-3">
                        <div className="flex items-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => router.push(`/governance/calculation-parameters/${param.id}/edit`)}
                          >
                            <Edit className="h-4 w-4 mr-1" />
                            {t('common.edit', 'Editar')}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleToggleActive(param.id, param.active)}
                          >
                            {param.active ? (
                              <>
                                <PowerOff className="h-4 w-4 mr-1" />
                                {t('common.deactivate', 'Desactivar')}
                              </>
                            ) : (
                              <>
                                <Power className="h-4 w-4 mr-1" />
                                {t('common.activate', 'Activar')}
                              </>
                            )}
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
};
