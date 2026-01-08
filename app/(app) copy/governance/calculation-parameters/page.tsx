'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useTranslation } from '@/app/config/i18n';
import { usePageTitle } from '@/components/contexts/PageTitleContext';
import { ParameterList } from './components/ParameterList';
import { ParameterForm } from './components/ParameterForm';
import { Button } from '@/components/ui/button';
import { Settings, Plus } from 'lucide-react';

export default function CalculationParametersPage() {
  const { t } = useTranslation();
  const { setPageTitle } = usePageTitle();
  const router = useRouter();
  const [showForm, setShowForm] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    const title = t('governance.calculationParameters.title', 'Parámetros de Cálculo');
    document.title = `${title} - CodeflowX`;
    setPageTitle(title);
    return () => setPageTitle(null);
  }, [t, setPageTitle]);

  const handleSave = () => {
    setShowForm(false);
    setRefreshKey(prev => prev + 1);
  };

  const handleCancel = () => {
    setShowForm(false);
  };

  if (showForm) {
    return (
      <div className="space-y-4">
        <ParameterForm
          onSave={handleSave}
          onCancel={handleCancel}
        />
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Settings className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t('governance.calculationParameters.title', 'Parámetros de Cálculo')}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t('governance.calculationParameters.subtitle', 'Gestiona los parámetros configurables para los cálculos de métricas de AI Governance')}
          </p>
        </div>
        <Button onClick={() => setShowForm(true)} size="sm">
          <Plus className="h-4 w-4 mr-2" />
          {t('governance.calculationParameters.create', 'Crear Parámetro')}
        </Button>
      </div>

      <ParameterList key={refreshKey} />
    </div>
  );
}


