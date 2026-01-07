'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useTranslation } from '@/app/config/i18n';
import { usePageTitle } from '@/components/contexts/PageTitleContext';
import { calculationParameterService } from '../../services/calculationParameterService';
import {
  CalculationParameterRequest,
  ValidationResult,
  MICROSERVICES
} from '@/types/calculation-parameter';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Checkbox } from '@/components/ui/checkbox';
import { ArrowLeft, Save, CheckCircle2, Loader2, Settings, Server, Key, Globe, Building2, FileCode } from 'lucide-react';
import JSONEditor from './JSONEditor';
import ParameterValidation from './ParameterValidation';

interface ParameterFormProps {
  parameterId?: number;
  onSave?: () => void;
  onCancel?: () => void;
}

export const ParameterForm: React.FC<ParameterFormProps> = ({
  parameterId,
  onSave,
  onCancel
}) => {
  const { t } = useTranslation();
  const { setPageTitle } = usePageTitle();
  const router = useRouter();
  const [formData, setFormData] = useState<CalculationParameterRequest>({
    microservice: '',
    organizationId: null,
    parameterKey: '',
    parameters: {},
    description: '',
    appliesToEndpoint: '',
    active: true
  });

  const [validation, setValidation] = useState<ValidationResult | null>(null);
  const [jsonError, setJsonError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const title = parameterId
      ? t('governance.calculationParameters.form.editTitle', 'Editar Parámetro')
      : t('governance.calculationParameters.form.createTitle', 'Crear Parámetro');
    document.title = `${title} - CodeflowX`;
    setPageTitle(title);
    return () => setPageTitle(null);
  }, [parameterId, t, setPageTitle]);

  useEffect(() => {
    if (parameterId) {
      loadParameter();
    }
  }, [parameterId]);

  const loadParameter = async () => {
    if (!parameterId) return;
    setLoading(true);
    try {
      const param = await calculationParameterService.getById(parameterId);
      setFormData({
        microservice: param.microservice,
        organizationId: param.organizationId,
        parameterKey: param.parameterKey,
        parameters: param.parameters,
        description: param.description || '',
        appliesToEndpoint: param.appliesToEndpoint || '',
        active: param.active
      });
    } catch (error) {
      console.error('Error cargando parámetro:', error);
      alert(t('governance.calculationParameters.errors.loadError', 'Error al cargar el parámetro'));
    } finally {
      setLoading(false);
    }
  };

  const handleValidate = async () => {
    if (!formData.microservice || !formData.parameterKey) {
      alert(t('governance.calculationParameters.errors.selectBeforeValidate', 'Selecciona microservicio y clave antes de validar'));
      return;
    }

    try {
      const result = await calculationParameterService.validate({
        microservice: formData.microservice,
        parameterKey: formData.parameterKey,
        parameters: formData.parameters
      });
      setValidation(result);
    } catch (error) {
      console.error('Error validando:', error);
      alert(t('governance.calculationParameters.errors.validateError', 'Error al validar parámetros'));
    }
  };

  const handleSave = async () => {
    if (!formData.microservice || !formData.parameterKey) {
      alert(t('governance.calculationParameters.errors.completeRequired', 'Completa los campos requeridos'));
      return;
    }

    await handleValidate();

    if (validation && !validation.valid) {
      alert(t('governance.calculationParameters.errors.fixErrors', 'Corrige los errores antes de guardar'));
      return;
    }

    setSaving(true);
    try {
      if (parameterId) {
        await calculationParameterService.update(parameterId, formData);
      } else {
        await calculationParameterService.create(formData);
      }

      if (onSave) {
        onSave();
      } else {
        router.push('/governance/calculation-parameters');
      }
    } catch (error: any) {
      console.error('Error guardando:', error);
      alert(error.message || t('governance.calculationParameters.errors.saveError', 'Error al guardar parámetro'));
    } finally {
      setSaving(false);
    }
  };

  const selectedMicroservice = MICROSERVICES.find(
    m => m.value === formData.microservice
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  const handleCancel = () => {
    if (onCancel) {
      onCancel();
    } else {
      router.push('/governance/calculation-parameters');
    }
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center gap-3">
        <Button
          variant="ghost"
          size="sm"
          onClick={handleCancel}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          {t('common.back', 'Volver')}
        </Button>
        <div className="flex items-center gap-3">
          <Settings className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {parameterId
              ? t('governance.calculationParameters.form.editTitle', 'Editar Parámetro')
              : t('governance.calculationParameters.form.createTitle', 'Crear Parámetro')}
          </h1>
        </div>
      </div>

      {/* Form Card */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t('governance.calculationParameters.form.basicInfo', 'Información Básica')}
          </CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          {/* Campos en una fila */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* Microservicio */}
            <div className="space-y-2">
              <Label className="flex items-center gap-2">
                <Server className="h-4 w-4" />
                {t('governance.calculationParameters.form.microservice', 'Microservicio')} *
              </Label>
              <select
                className="w-full px-3 py-2 border border-input bg-background rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                value={formData.microservice}
                onChange={(e) => setFormData({ ...formData, microservice: e.target.value, parameterKey: '' })}
                disabled={!!parameterId}>
                <option value="">{t('governance.calculationParameters.form.selectMicroservice', 'Selecciona un microservicio...')}</option>
                {MICROSERVICES.map(m => (
                  <option key={m.value} value={m.value}>
                    {m.label}
                  </option>
                ))}
              </select>
            </div>

            {/* Clave de parámetro */}
            <div className="space-y-2">
              <Label className="flex items-center gap-2">
                <Key className="h-4 w-4" />
                {t('governance.calculationParameters.form.parameterKey', 'Clave de Parámetro')} *
              </Label>
              <select
                className="w-full px-3 py-2 border border-input bg-background rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                value={formData.parameterKey}
                onChange={(e) => setFormData({ ...formData, parameterKey: e.target.value })}
                disabled={!selectedMicroservice || !!parameterId}>
                <option value="">{t('governance.calculationParameters.form.selectKey', 'Selecciona una clave...')}</option>
                {selectedMicroservice?.parameterKeys.map(key => (
                  <option key={key} value={key}>
                    {key}
                  </option>
                ))}
              </select>
            </div>

            {/* Organización */}
            <div className="space-y-2">
              <Label className="flex items-center gap-2">
                {formData.organizationId ? (
                  <Building2 className="h-4 w-4" />
                ) : (
                  <Globe className="h-4 w-4" />
                )}
                {t('governance.calculationParameters.form.organization', 'Organización')}
              </Label>
              <Input
                type="text"
                placeholder={t('governance.calculationParameters.form.organizationPlaceholder', 'UUID o vacío para global')}
                value={formData.organizationId || ''}
                onChange={e => setFormData({
                  ...formData,
                  organizationId: e.target.value || null
                })}
                disabled={!!parameterId}
                className="text-sm"
              />
            </div>
          </div>
          <p className="text-xs text-muted-foreground">
            {t('governance.calculationParameters.form.organizationHelp', 'Deja vacío para crear parámetros globales aplicables a todas las organizaciones')}
          </p>
        </CardBody>
      </Card>

      {/* Parámetros JSON Card */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <FileCode className="h-5 w-5" />
            {t('governance.calculationParameters.form.parameters', 'Parámetros JSON')} *
          </CardTitle>
        </CardHeader>
        <CardBody>
          <JSONEditor
            value={formData.parameters}
            onChange={params => {
              setFormData({ ...formData, parameters: params });
              setJsonError(null);
            }}
            onError={error => setJsonError(error)}
          />
          {jsonError && (
            <p className="mt-2 text-sm text-destructive">{jsonError}</p>
          )}
        </CardBody>
      </Card>

      {/* Metadata Card */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t('governance.calculationParameters.form.metadata', 'Metadatos')}
          </CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          {/* Descripción y Endpoint en una fila */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Descripción */}
            <div className="space-y-2">
              <Label>
                {t('governance.calculationParameters.form.description', 'Descripción')}
              </Label>
              <Textarea
                rows={2}
                value={formData.description}
                onChange={e => setFormData({ ...formData, description: e.target.value })}
                placeholder={t('governance.calculationParameters.form.descriptionPlaceholder', 'Descripción del propósito de estos parámetros')}
                className="text-sm"
              />
            </div>

            {/* Endpoint */}
            <div className="space-y-2">
              <Label>
                {t('governance.calculationParameters.form.endpoint', 'Endpoint')}
              </Label>
              <Input
                type="text"
                value={formData.appliesToEndpoint || ''}
                onChange={e => setFormData({ ...formData, appliesToEndpoint: e.target.value })}
                placeholder="/api/board/calculate-edm-metrics"
                className="text-sm"
              />
            </div>
          </div>

          {/* Activo */}
          <div className="flex items-center space-x-2">
            <Checkbox
              id="active"
              checked={formData.active}
              onChange={(e) => setFormData({ ...formData, active: e.target.checked })}
            />
            <Label htmlFor="active" className="cursor-pointer text-sm">
              {t('governance.calculationParameters.form.active', 'Activo')}
            </Label>
          </div>
        </CardBody>
      </Card>

      {/* Validación */}
      {validation && (
        <ParameterValidation validation={validation} />
      )}

      {/* Actions */}
      <div className="flex justify-end gap-3">
        <Button
          variant="outline"
          onClick={handleValidate}
          disabled={!formData.microservice || !formData.parameterKey}
        >
          <CheckCircle2 className="h-4 w-4 mr-2" />
          {t('governance.calculationParameters.form.validate', 'Validar')}
        </Button>
        <Button
          variant="outline"
          onClick={handleCancel}
        >
          {t('common.cancel', 'Cancelar')}
        </Button>
        <Button
          onClick={handleSave}
          disabled={saving || !formData.microservice || !formData.parameterKey}
        >
          {saving ? (
            <>
              <Loader2 className="h-4 w-4 mr-2 animate-spin" />
              {t('common.saving', 'Guardando...')}
            </>
          ) : (
            <>
              <Save className="h-4 w-4 mr-2" />
              {t('common.save', 'Guardar')}
            </>
          )}
        </Button>
      </div>
    </div>
  );
};


