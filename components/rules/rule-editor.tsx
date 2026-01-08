"use client";

import { useState, useEffect } from "react";
import Editor from "@monaco-editor/react";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { CheckCircle2, XCircle, AlertTriangle } from "lucide-react";

interface RuleEditorProps {
  content: string;
  type: "VALIDATION" | "DROOLS" | "JSON" | "YAML" | "SCRIPT";
  onChange: (value: string) => void;
  onValidationChange?: (isValid: boolean, errors: string[]) => void;
}

export function RuleEditor({
  content,
  type,
  onChange,
  onValidationChange,
}: RuleEditorProps) {
  const [validationErrors, setValidationErrors] = useState<string[]>([]);
  const [isValid, setIsValid] = useState(true);

  // Determinar el lenguaje para Monaco Editor
  const getLanguage = () => {
    switch (type) {
      case "DROOLS":
        return "java"; // Drools usa sintaxis similar a Java
      case "JSON":
        return "json";
      case "YAML":
        return "yaml";
      case "SCRIPT":
        return "javascript";
      case "VALIDATION":
        return "plaintext";
      default:
        return "plaintext";
    }
  };

  // Validar contenido según el tipo
  const validateContent = (value: string) => {
    const errors: string[] = [];

    if (!value || value.trim().length === 0) {
      errors.push("El contenido no puede estar vacío");
      setIsValid(false);
      setValidationErrors(errors);
      onValidationChange?.(false, errors);
      return;
    }

    switch (type) {
      case "JSON":
        try {
          JSON.parse(value);
        } catch (e: any) {
          errors.push(`Error de sintaxis JSON: ${e.message}`);
        }
        break;

      case "YAML":
        // Validación básica de YAML (puede mejorarse con una librería)
        if (value.includes("\t")) {
          errors.push("YAML no debe contener tabs, use espacios");
        }
        break;

      case "DROOLS":
        // Validación básica de Drools
        if (!value.includes("rule") && !value.includes("when") && !value.includes("then")) {
          errors.push("Regla Drools debe contener al menos 'rule', 'when' y 'then'");
        }
        if (value.includes("rule") && !value.includes("when")) {
          errors.push("Regla Drools debe tener una sección 'when'");
        }
        if (value.includes("when") && !value.includes("then")) {
          errors.push("Regla Drools debe tener una sección 'then'");
        }
        break;

      case "VALIDATION":
        // Validación básica: debe tener contenido
        if (value.trim().length < 3) {
          errors.push("La regla de validación debe tener al menos 3 caracteres");
        }
        break;

      case "SCRIPT":
        // Validación básica de JavaScript
        try {
          // Solo validar sintaxis básica, no ejecutar
          new Function(value);
        } catch (e: any) {
          errors.push(`Error de sintaxis en script: ${e.message}`);
        }
        break;
    }

    setIsValid(errors.length === 0);
    setValidationErrors(errors);
    onValidationChange?.(errors.length === 0, errors);
  };

  useEffect(() => {
    validateContent(content);
  }, [content, type]);

  const handleEditorChange = (value: string | undefined) => {
    const newValue = value || "";
    onChange(newValue);
    validateContent(newValue);
  };

  return (
    <div className="space-y-2">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Badge variant="outline">{type}</Badge>
          {isValid ? (
            <Badge variant="secondary" className="gap-1">
              <CheckCircle2 className="h-3 w-3" />
              Válido
            </Badge>
          ) : (
            <Badge variant="danger" className="gap-1">
              <XCircle className="h-3 w-3" />
              Errores
            </Badge>
          )}
        </div>
        <div className="text-sm text-muted-foreground">
          {content.length} caracteres
        </div>
      </div>

      <div className="border rounded-md overflow-hidden">
        <Editor
          height="400px"
          language={getLanguage()}
          value={content}
          onChange={handleEditorChange}
          theme="vs-dark"
          options={{
            minimap: { enabled: true },
            fontSize: 14,
            lineNumbers: "on",
            roundedSelection: false,
            scrollBeyondLastLine: false,
            readOnly: false,
            automaticLayout: true,
            tabSize: 2,
            wordWrap: "on",
            formatOnPaste: true,
            formatOnType: true,
          }}
        />
      </div>

      {validationErrors.length > 0 && (
        <Alert variant="danger">
          <AlertTriangle className="h-4 w-4" />
          <AlertDescription>
            <div className="space-y-1">
              <p className="font-semibold">Errores de validación:</p>
              <ul className="list-disc list-inside space-y-1">
                {validationErrors.map((error, index) => (
                  <li key={index}>{error}</li>
                ))}
              </ul>
            </div>
          </AlertDescription>
        </Alert>
      )}

      {isValid && content.length > 0 && (
        <Alert>
          <CheckCircle2 className="h-4 w-4" />
          <AlertDescription>
            El contenido es válido y está listo para guardar.
          </AlertDescription>
        </Alert>
      )}
    </div>
  );
}
