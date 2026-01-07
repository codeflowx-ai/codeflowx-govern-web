'use client';

import React, { useState, useEffect } from 'react';
import { useTranslation } from '@/app/config/i18n';
import { Button } from '@/components/ui/button';
import { CodeEditor } from '@/components/ui/code-editor';
import { Sparkles } from 'lucide-react';

interface JSONEditorProps {
  value: Record<string, any>;
  onChange: (value: Record<string, any>) => void;
  onError?: (error: string | null) => void;
}

const JSONEditor: React.FC<JSONEditorProps> = ({
  value,
  onChange,
  onError
}) => {
  const { t } = useTranslation();
  const [jsonString, setJsonString] = useState('');
  const [isValid, setIsValid] = useState(true);

  useEffect(() => {
    try {
      const formatted = JSON.stringify(value, null, 2);
      setJsonString(formatted);
      setIsValid(true);
      if (onError) onError(null);
    } catch (e) {
      // Ignore
    }
  }, [value, onError]);

  const handleChange = (newValue: string) => {
    setJsonString(newValue);

    try {
      const parsed = JSON.parse(newValue);
      onChange(parsed);
      setIsValid(true);
      if (onError) onError(null);
    } catch (e: any) {
      setIsValid(false);
      if (onError) onError(t('governance.calculationParameters.form.jsonInvalid', `JSON inválido: ${e.message}`));
    }
  };

  const handleFormat = () => {
    try {
      const parsed = JSON.parse(jsonString);
      const formatted = JSON.stringify(parsed, null, 2);
      setJsonString(formatted);
      onChange(parsed);
      setIsValid(true);
      if (onError) onError(null);
    } catch (e: any) {
      if (onError) onError(t('governance.calculationParameters.form.formatError', `Error al formatear: ${e.message}`));
    }
  };

  return (
    <div className="space-y-2">
      <div className="flex justify-between items-center">
        <span className="text-xs text-muted-foreground">
          {t('governance.calculationParameters.form.jsonEditor', 'Editor JSON')}
        </span>
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={handleFormat}
        >
          <Sparkles className="h-3 w-3 mr-1" />
          {t('governance.calculationParameters.form.format', 'Formatear')}
        </Button>
      </div>
      <CodeEditor
        value={jsonString}
        onChange={handleChange}
        language="json"
        height="400px"
        placeholder='{"weights": {"evaluate": 0.35, "direct": 0.35, "monitor": 0.30}}'
      />
      {!isValid && (
        <p className="text-xs text-destructive">
          {t('governance.calculationParameters.form.jsonInvalidMessage', 'JSON inválido. Corrige los errores.')}
        </p>
      )}
    </div>
  );
};

export default JSONEditor;


