'use client';

import React from 'react';
import { useTranslation } from '@/app/config/i18n';
import { MICROSERVICES } from '@/types/calculation-parameter';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Button } from '@/components/ui/button';
import { Filter, X } from 'lucide-react';

interface ParameterListFiltersProps {
  filters: {
    microservice?: string;
    parameterKey?: string;
    organizationId?: string;
  };
  onFiltersChange: (filters: {
    microservice?: string;
    parameterKey?: string;
    organizationId?: string;
  }) => void;
}

const ParameterListFilters: React.FC<ParameterListFiltersProps> = ({
  filters,
  onFiltersChange
}) => {
  const { t } = useTranslation();
  const selectedMicroservice = MICROSERVICES.find(
    m => m.value === filters.microservice
  );

  const hasFilters = filters.microservice || filters.parameterKey || filters.organizationId;

  const clearFilters = () => {
    onFiltersChange({});
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div>
        <Label className="flex items-center gap-2 mb-2">
          <Filter className="h-4 w-4" />
          {t('governance.calculationParameters.filters.microservice', 'Microservicio')}
        </Label>
        <Select
          value={filters.microservice || ''}
          onValueChange={(value) => onFiltersChange({
            ...filters,
            microservice: value || undefined,
            parameterKey: undefined
          })}
        >
          <SelectTrigger>
            <SelectValue placeholder={t('common.all', 'Todos')} />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">{t('common.all', 'Todos')}</SelectItem>
            {MICROSERVICES.map(m => (
              <SelectItem key={m.value} value={m.value}>
                {m.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div>
        <Label className="mb-2">
          {t('governance.calculationParameters.filters.parameterKey', 'Clave de Parámetro')}
        </Label>
        <Select
          value={filters.parameterKey || ''}
          onValueChange={(value) => onFiltersChange({
            ...filters,
            parameterKey: value || undefined
          })}
          disabled={!selectedMicroservice}
        >
          <SelectTrigger>
            <SelectValue placeholder={t('common.all', 'Todas')} />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="">{t('common.all', 'Todas')}</SelectItem>
            {selectedMicroservice?.parameterKeys.map(key => (
              <SelectItem key={key} value={key}>
                {key}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div>
        <Label className="mb-2">
          {t('governance.calculationParameters.filters.organization', 'Organización (UUID)')}
        </Label>
        <Input
          type="text"
          placeholder={t('governance.calculationParameters.filters.organizationPlaceholder', 'UUID o vacío para globales')}
          value={filters.organizationId || ''}
          onChange={e => onFiltersChange({
            ...filters,
            organizationId: e.target.value || undefined
          })}
        />
      </div>

      <div className="flex items-end">
        <Button
          variant="outline"
          onClick={clearFilters}
          disabled={!hasFilters}
          className="w-full"
        >
          <X className="h-4 w-4 mr-2" />
          {t('common.clearFilters', 'Limpiar Filtros')}
        </Button>
      </div>
    </div>
  );
};

export default ParameterListFilters;


