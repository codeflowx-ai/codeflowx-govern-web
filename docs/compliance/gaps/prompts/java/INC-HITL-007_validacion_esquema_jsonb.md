# PROMPT: INC-HITL-007 - Validación de Esquema JSONB

**Incidencia:** INC-HITL-007  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 14.4 (Transparency)  
**Esfuerzo Estimado:** 3-4 días  
**Tipo:** Java - Backend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Los campos JSONB (`AGTRISKASSESSMENT`, `AGTCOMPLIANCECHECK`, etc.) no tienen validación de esquema, lo que puede llevar a datos inconsistentes o incompletos. Se requiere definir JSON Schema y validar a nivel de aplicación.

---

## REQUISITOS

1. Definir JSON Schema para cada campo JSONB
2. Implementar validación a nivel de aplicación usando Jackson/JSON Schema
3. Considerar usar CHECK constraints con validación JSON en PostgreSQL 12+
4. Documentar estructura esperada de cada campo JSONB

---

## IMPLEMENTACIÓN REQUERIDA

### 1. JSON Schemas

**Archivo:** `codeflowx.govern.services/src/main/resources/schemas/risk_assessment_schema.json`

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "required": ["riskLevel"],
  "properties": {
    "riskLevel": {
      "type": "string",
      "enum": ["LOW", "MEDIUM", "HIGH", "CRITICAL"]
    },
    "riskScore": {
      "type": "number",
      "minimum": 0,
      "maximum": 1
    },
    "riskFactors": {
      "type": "array",
      "items": {
        "type": "object",
        "required": ["factor", "severity"],
        "properties": {
          "factor": {"type": "string"},
          "severity": {"type": "string", "enum": ["LOW", "MEDIUM", "HIGH"]},
          "description": {"type": "string"}
        }
      }
    },
    "requiresHitl": {"type": "boolean"},
    "hitlLevel": {
      "type": "string",
      "enum": ["AUTOMATIC", "SINGLE_APPROVER", "DUAL_APPROVAL", "COMMITTEE"]
    },
    "hitlTimeout": {"type": "string"}
  }
}
```

### 2. Servicio de Validación

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/JsonbValidationService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class JsonbValidationService {
    
    private JsonSchema riskAssessmentSchema;
    private JsonSchema complianceCheckSchema;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
    
    @PostConstruct
    public void init() throws IOException, ProcessingException {
        riskAssessmentSchema = loadSchema("schemas/risk_assessment_schema.json");
        complianceCheckSchema = loadSchema("schemas/compliance_check_schema.json");
    }
    
    private JsonSchema loadSchema(String path) throws IOException, ProcessingException {
        ClassPathResource resource = new ClassPathResource(path);
        JsonNode schemaNode = objectMapper.readTree(resource.getInputStream());
        return schemaFactory.getJsonSchema(schemaNode);
    }
    
    public void validateRiskAssessment(String jsonb) {
        if (jsonb == null) return;
        try {
            JsonNode data = objectMapper.readTree(jsonb);
            riskAssessmentSchema.validate(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid risk assessment JSONB: " + e.getMessage(), e);
        }
    }
    
    public void validateComplianceCheck(String jsonb) {
        if (jsonb == null) return;
        try {
            JsonNode data = objectMapper.readTree(jsonb);
            complianceCheckSchema.validate(data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid compliance check JSONB: " + e.getMessage(), e);
        }
    }
}
```

### 3. Validator Personalizado

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/validator/JsonbFieldValidator.java`

```java
package com.codeflowx.govern.validator;

import com.codeflowx.govern.service.hitl.JsonbValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonbFieldValidator implements ConstraintValidator<ValidJsonb, String> {
    
    @Autowired
    private JsonbValidationService validationService;
    
    private String schemaType;
    
    @Override
    public void initialize(ValidJsonb constraintAnnotation) {
        this.schemaType = constraintAnnotation.schemaType();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        
        try {
            switch (schemaType) {
                case "RISK_ASSESSMENT":
                    validationService.validateRiskAssessment(value);
                    break;
                case "COMPLIANCE_CHECK":
                    validationService.validateComplianceCheck(value);
                    break;
            }
            return true;
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                .addConstraintViolation();
            return false;
        }
    }
}
```

### 4. Anotación de Validación

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/validator/ValidJsonb.java`

```java
package com.codeflowx.govern.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonbFieldValidator.class)
@Documented
public @interface ValidJsonb {
    String message() default "Invalid JSONB format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String schemaType(); // RISK_ASSESSMENT, COMPLIANCE_CHECK, etc.
}
```

### 5. Uso en Entidades

**Archivo:** Modificar `AgentApproval.java`

```java
@ValidJsonb(schemaType = "RISK_ASSESSMENT")
@Column(name = "AGTRISKASSESSMENT", nullable = true)
private String agtriskassessment;

@ValidJsonb(schemaType = "COMPLIANCE_CHECK")
@Column(name = "AGTCOMPLIANCECHECK", nullable = true)
private String agtcompliancecheck;
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-007`
- **EU AI Act Art. 14.4:** Transparency

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 3-4 días  
**Responsable:** Backend Team  
**Fecha Límite:** 2 meses

