'use client';

import React from 'react';
import { useTranslation } from '@/app/config/i18n';
import { ValidationResult } from '@/types/calculation-parameter';
import { Card, CardBody } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { CheckCircle2, XCircle, AlertCircle } from 'lucide-react';

interface ParameterValidationProps {
  validation: ValidationResult;
}

const ParameterValidation: React.FC<ParameterValidationProps> = ({
  validation
}) => {
  const { t } = useTranslation();

  if (validation.valid) {
    return (
      <Card className="border-green-200 bg-green-50/50 dark:bg-green-900/10">
        <CardBody>
          <div className="flex items-center gap-3">
            <CheckCircle2 className="h-5 w-5 text-green-600 dark:text-green-400" />
            <div>
              <p className="text-sm font-medium text-green-800 dark:text-green-300">
                {t('governance.calculationParameters.validation.success', 'Validación exitosa')}
              </p>
              <p className="text-xs text-green-600 dark:text-green-400 mt-1">
                {t('governance.calculationParameters.validation.successMessage', 'Los parámetros cumplen con todas las validaciones requeridas')}
              </p>
            </div>
          </div>
        </CardBody>
      </Card>
    );
  }

  return (
    <Card className="border-destructive/50 bg-destructive/5">
      <CardBody>
        <div className="flex items-start gap-3">
          <XCircle className="h-5 w-5 text-destructive mt-0.5 flex-shrink-0" />
          <div className="flex-1 space-y-2">
            <div className="flex items-center gap-2">
              <p className="text-sm font-medium text-destructive">
                {t('governance.calculationParameters.validation.errors', 'Errores de validación')}
              </p>
              <Badge variant="danger" className="text-xs">
                {validation.errors.length}
              </Badge>
            </div>
            <ul className="list-disc list-inside text-sm text-destructive space-y-1 ml-2">
              {validation.errors.map((error, index) => (
                <li key={index} className="flex items-start gap-2">
                  <AlertCircle className="h-3 w-3 mt-0.5 flex-shrink-0" />
                  <span>{error}</span>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </CardBody>
    </Card>
  );
};

export default ParameterValidation;
