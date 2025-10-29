# 🔗 INTEGRACIÓN - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Guía completa para integrar prompts externos e internos en el sistema de gobierno

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **prompts** soporta **integración completa** con sistemas externos mediante **APIs REST**, **SDKs oficiales**, **webhooks** y **herramientas de migración**, permitiendo la **importación**, **sincronización** y **gobierno** de prompts desde cualquier fuente.

### **Tipos de Integración:**
- **Integración Externa:** Prompts de terceros y sistemas legacy
- **Integración Interna:** Prompts desarrollados internamente
- **Migración Masiva:** Importación de grandes volúmenes
- **Sincronización:** Mantenimiento de prompts actualizados

---

## 🔌 INTEGRACIÓN EXTERNA

### **1. Integración con Sistemas Legacy**

#### **Migración desde Sistemas Existentes:**
```python
from codeflowx_prompts import PromptClient
import json

# Configuración del cliente
client = PromptClient(
    api_key="your_api_key",
    base_url="https://api.codeflowx.com"
)

# Función de migración
def migrate_legacy_prompts(legacy_prompts):
    migrated_prompts = []
    
    for legacy_prompt in legacy_prompts:
        try:
            # Mapear campos del sistema legacy
            prompt_data = {
                "prmname": legacy_prompt["name"],
                "prmdescription": legacy_prompt["description"],
                "prmtype": map_prompt_type(legacy_prompt["type"]),
                "prmcategory": legacy_prompt["category"],
                "prmcontent": legacy_prompt["content"],
                "prmparameters": legacy_prompt.get("parameters", {}),
                "prmmetadata": {
                    "legacy_id": legacy_prompt["id"],
                    "migration_date": "2025-10-01",
                    "source_system": "legacy_system"
                }
            }
            
            # Crear prompt en CodeflowX
            new_prompt = client.prompts.create(prompt_data)
            migrated_prompts.append(new_prompt)
            
            print(f"Migrado: {legacy_prompt['name']} -> {new_prompt.idxprompt}")
            
        except Exception as e:
            print(f"Error migrando {legacy_prompt['name']}: {e}")
    
    return migrated_prompts

# Mapeo de tipos de prompt
def map_prompt_type(legacy_type):
    type_mapping = {
        "text_generation": "TEXT_GENERATION",
        "text_classification": "TEXT_CLASSIFICATION",
        "text_summarization": "TEXT_SUMMARIZATION",
        "qa": "QUESTION_ANSWERING",
        "code_gen": "CODE_GENERATION",
        "translation": "TRANSLATION"
    }
    return type_mapping.get(legacy_type, "TEXT_GENERATION")
```

#### **Validación Post-Migración:**
```python
def validate_migrated_prompts(prompt_ids):
    validation_results = []
    
    for prompt_id in prompt_ids:
        try:
            # Ejecutar validaciones automáticas
            safety_validation = client.prompts.validations.create(prompt_id, {
                "prmvalidationtype": "SAFETY_CHECK"
            })
            
            compliance_validation = client.prompts.validations.create(prompt_id, {
                "prmvalidationtype": "COMPLIANCE_CHECK"
            })
            
            # Verificar resultados
            if (safety_validation.prmvalidationresult == "PASS" and 
                compliance_validation.prmvalidationresult == "PASS"):
                
                # Solicitar aprobación automática
                approval = client.prompts.approvals.create(prompt_id, {
                    "prmapprovaltype": "NEW_PROMPT",
                    "prmrequestreason": "Migrated from legacy system"
                })
                
                validation_results.append({
                    "prompt_id": prompt_id,
                    "status": "SUCCESS",
                    "safety_score": safety_validation.prmvalidationscore,
                    "compliance_score": compliance_validation.prmvalidationscore
                })
            else:
                validation_results.append({
                    "prompt_id": prompt_id,
                    "status": "REQUIRES_REVIEW",
                    "safety_score": safety_validation.prmvalidationscore,
                    "compliance_score": compliance_validation.prmvalidationscore,
                    "issues": safety_validation.prmissuesfound
                })
                
        except Exception as e:
            validation_results.append({
                "prompt_id": prompt_id,
                "status": "ERROR",
                "error": str(e)
            })
    
    return validation_results
```

### **2. Integración con APIs de Terceros**

#### **OpenAI Integration:**
```python
import openai
from codeflowx_prompts import PromptClient

class OpenAIIntegration:
    def __init__(self, openai_api_key, codeflowx_api_key):
        self.openai_client = openai.OpenAI(api_key=openai_api_key)
        self.codeflowx_client = PromptClient(
            api_key=codeflowx_api_key,
            base_url="https://api.codeflowx.com"
        )
    
    def sync_openai_prompts(self):
        """Sincroniza prompts desde OpenAI"""
        try:
            # Obtener prompts de OpenAI (si tienen API para esto)
            openai_prompts = self.get_openai_prompts()
            
            for prompt in openai_prompts:
                # Crear prompt en CodeflowX
                codeflowx_prompt = self.codeflowx_client.prompts.create({
                    "prmname": f"OpenAI_{prompt['name']}",
                    "prmdescription": prompt.get("description", ""),
                    "prmtype": "TEXT_GENERATION",
                    "prmcategory": "openai_integration",
                    "prmcontent": prompt["content"],
                    "prmparameters": prompt.get("parameters", {}),
                    "prmmetadata": {
                        "source": "openai",
                        "openai_id": prompt["id"],
                        "sync_date": "2025-10-01"
                    }
                })
                
                print(f"Sincronizado: {prompt['name']} -> {codeflowx_prompt.idxprompt}")
                
        except Exception as e:
            print(f"Error en sincronización OpenAI: {e}")
    
    def get_openai_prompts(self):
        """Obtiene prompts desde OpenAI (ejemplo)"""
        # Esta función dependería de la API específica de OpenAI
        return [
            {
                "id": "prompt_1",
                "name": "Customer Support",
                "content": "You are a helpful customer support assistant...",
                "parameters": {"temperature": 0.7}
            }
        ]
```

#### **Hugging Face Integration:**
```python
from huggingface_hub import HfApi
from codeflowx_prompts import PromptClient

class HuggingFaceIntegration:
    def __init__(self, hf_token, codeflowx_api_key):
        self.hf_api = HfApi(token=hf_token)
        self.codeflowx_client = PromptClient(
            api_key=codeflowx_api_key,
            base_url="https://api.codeflowx.com"
        )
    
    def sync_hf_prompts(self, repo_id):
        """Sincroniza prompts desde Hugging Face"""
        try:
            # Obtener archivos del repositorio
            files = self.hf_api.list_repo_files(repo_id)
            prompt_files = [f for f in files if f.endswith('.json')]
            
            for file in prompt_files:
                # Descargar archivo de prompt
                prompt_data = self.hf_api.hf_hub_download(
                    repo_id=repo_id,
                    filename=file
                )
                
                # Procesar y crear prompt en CodeflowX
                with open(prompt_data, 'r') as f:
                    prompt_content = json.load(f)
                
                codeflowx_prompt = self.codeflowx_client.prompts.create({
                    "prmname": f"HF_{prompt_content['name']}",
                    "prmdescription": prompt_content.get("description", ""),
                    "prmtype": "TEXT_GENERATION",
                    "prmcategory": "huggingface",
                    "prmcontent": prompt_content["prompt"],
                    "prmmetadata": {
                        "source": "huggingface",
                        "repo_id": repo_id,
                        "file": file,
                        "sync_date": "2025-10-01"
                    }
                })
                
                print(f"Sincronizado desde HF: {prompt_content['name']}")
                
        except Exception as e:
            print(f"Error en sincronización Hugging Face: {e}")
```

### **3. Integración con Bases de Datos Externas**

#### **MySQL Integration:**
```python
import mysql.connector
from codeflowx_prompts import PromptClient

class MySQLIntegration:
    def __init__(self, mysql_config, codeflowx_api_key):
        self.mysql_conn = mysql.connector.connect(**mysql_config)
        self.codeflowx_client = PromptClient(
            api_key=codeflowx_api_key,
            base_url="https://api.codeflowx.com"
        )
    
    def sync_mysql_prompts(self, table_name):
        """Sincroniza prompts desde MySQL"""
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            cursor.execute(f"SELECT * FROM {table_name}")
            mysql_prompts = cursor.fetchall()
            
            for prompt in mysql_prompts:
                # Mapear campos de MySQL a CodeflowX
                codeflowx_prompt = self.codeflowx_client.prompts.create({
                    "prmname": prompt["name"],
                    "prmdescription": prompt.get("description", ""),
                    "prmtype": prompt["type"],
                    "prmcategory": prompt.get("category", "mysql_import"),
                    "prmcontent": prompt["content"],
                    "prmparameters": json.loads(prompt.get("parameters", "{}")),
                    "prmmetadata": {
                        "source": "mysql",
                        "mysql_id": prompt["id"],
                        "table": table_name,
                        "sync_date": "2025-10-01"
                    }
                })
                
                print(f"Sincronizado desde MySQL: {prompt['name']}")
                
        except Exception as e:
            print(f"Error en sincronización MySQL: {e}")
        finally:
            cursor.close()
```

---

## 🏠 INTEGRACIÓN INTERNA

### **1. Desarrollo de Prompts Internos**

#### **Template de Desarrollo:**
```python
from codeflowx_prompts import PromptClient
import json

class InternalPromptDevelopment:
    def __init__(self, api_key):
        self.client = PromptClient(
            api_key=api_key,
            base_url="https://api.codeflowx.com"
        )
    
    def create_prompt_template(self, template_config):
        """Crea un prompt desde template"""
        template = {
            "prmname": template_config["name"],
            "prmdescription": template_config["description"],
            "prmtype": template_config["type"],
            "prmcategory": template_config["category"],
            "prmcontent": self.build_prompt_content(template_config),
            "prmparameters": template_config.get("parameters", {}),
            "prmmetadata": {
                "development": "internal",
                "template": template_config["template_name"],
                "version": "1.0.0"
            }
        }
        
        return self.client.prompts.create(template)
    
    def build_prompt_content(self, config):
        """Construye contenido del prompt desde template"""
        templates = {
            "customer_support": """
You are a helpful customer support assistant for {company_name}.
Your role is to assist customers with {support_areas}.

Guidelines:
- Be polite and professional
- Provide accurate information
- Escalate complex issues to human agents
- Follow company policies

Customer query: {customer_query}
""",
            "content_generation": """
You are a content writer specializing in {content_type}.
Create engaging content for {target_audience}.

Requirements:
- Tone: {tone}
- Length: {length}
- Style: {style}
- Include: {requirements}

Topic: {topic}
"""
        }
        
        template = templates.get(config["template_name"], "")
        return template.format(**config["variables"])
```

#### **Pipeline de Desarrollo:**
```python
def development_pipeline(prompt_config):
    """Pipeline completo de desarrollo de prompt"""
    
    # 1. Crear prompt inicial
    prompt = create_prompt_template(prompt_config)
    
    # 2. Ejecutar validaciones
    safety_check = client.prompts.validations.create(prompt.idxprompt, {
        "prmvalidationtype": "SAFETY_CHECK"
    })
    
    compliance_check = client.prompts.validations.create(prompt.idxprompt, {
        "prmvalidationtype": "COMPLIANCE_CHECK"
    })
    
    # 3. Testing automático
    test_results = run_automated_tests(prompt.idxprompt)
    
    # 4. Optimización si es necesario
    if test_results["performance_score"] < 80:
        optimization = client.prompts.optimize(prompt.idxprompt, {
            "target_reduction": 10.0
        })
    
    # 5. Solicitar aprobación
    approval = client.prompts.approvals.create(prompt.idxprompt, {
        "prmapprovaltype": "NEW_PROMPT",
        "prmrequestreason": "Internal development - ready for production"
    })
    
    return {
        "prompt": prompt,
        "safety_check": safety_check,
        "compliance_check": compliance_check,
        "test_results": test_results,
        "approval": approval
    }
```

### **2. Integración con Sistemas Internos**

#### **CRM Integration:**
```python
class CRMIntegration:
    def __init__(self, crm_api_key, codeflowx_api_key):
        self.crm_client = CRMClient(api_key=crm_api_key)
        self.codeflowx_client = PromptClient(
            api_key=codeflowx_api_key,
            base_url="https://api.codeflowx.com"
        )
    
    def sync_crm_prompts(self):
        """Sincroniza prompts desde CRM"""
        try:
            # Obtener templates de CRM
            crm_templates = self.crm_client.get_email_templates()
            
            for template in crm_templates:
                # Convertir template de CRM a prompt
                prompt_data = {
                    "prmname": f"CRM_{template['name']}",
                    "prmdescription": template.get("description", ""),
                    "prmtype": "TEXT_GENERATION",
                    "prmcategory": "crm_integration",
                    "prmcontent": template["content"],
                    "prmparameters": {
                        "temperature": 0.7,
                        "max_tokens": 500
                    },
                    "prmmetadata": {
                        "source": "crm",
                        "crm_template_id": template["id"],
                        "sync_date": "2025-10-01"
                    }
                }
                
                codeflowx_prompt = self.codeflowx_client.prompts.create(prompt_data)
                print(f"Sincronizado desde CRM: {template['name']}")
                
        except Exception as e:
            print(f"Error en sincronización CRM: {e}")
```

---

## 🔄 SINCRONIZACIÓN Y MANTENIMIENTO

### **1. Sincronización Automática**

#### **Scheduler de Sincronización:**
```python
import schedule
import time
from datetime import datetime

class PromptSynchronizer:
    def __init__(self, integrations):
        self.integrations = integrations
    
    def schedule_sync(self):
        """Programa sincronizaciones automáticas"""
        
        # Sincronización diaria
        schedule.every().day.at("02:00").do(self.daily_sync)
        
        # Sincronización semanal
        schedule.every().monday.at("03:00").do(self.weekly_sync)
        
        # Sincronización mensual
        schedule.every().month.do(self.monthly_sync)
        
        # Ejecutar scheduler
        while True:
            schedule.run_pending()
            time.sleep(60)
    
    def daily_sync(self):
        """Sincronización diaria"""
        print(f"Iniciando sincronización diaria: {datetime.now()}")
        
        for integration in self.integrations:
            try:
                integration.sync()
                print(f"Sincronización exitosa: {integration.name}")
            except Exception as e:
                print(f"Error en sincronización {integration.name}: {e}")
    
    def weekly_sync(self):
        """Sincronización semanal"""
        print(f"Iniciando sincronización semanal: {datetime.now()}")
        
        # Sincronización completa
        for integration in self.integrations:
            try:
                integration.full_sync()
                print(f"Sincronización completa exitosa: {integration.name}")
            except Exception as e:
                print(f"Error en sincronización completa {integration.name}: {e}")
```

### **2. Monitoreo de Cambios**

#### **Webhook Handler:**
```python
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/webhooks/prompts', methods=['POST'])
def handle_prompt_webhook():
    """Maneja webhooks de cambios en prompts"""
    try:
        payload = request.get_json()
        
        event_type = payload['event']
        prompt_data = payload['data']
        
        if event_type == 'prompt.updated':
            handle_prompt_update(prompt_data)
        elif event_type == 'prompt.approved':
            handle_prompt_approval(prompt_data)
        elif event_type == 'version.created':
            handle_version_creation(prompt_data)
        
        return jsonify({"status": "success"}), 200
        
    except Exception as e:
        print(f"Error procesando webhook: {e}")
        return jsonify({"error": str(e)}), 500

def handle_prompt_update(prompt_data):
    """Maneja actualización de prompt"""
    print(f"Prompt actualizado: {prompt_data['idxprompt']}")
    
    # Notificar a sistemas externos
    notify_external_systems(prompt_data)
    
    # Actualizar caché
    update_cache(prompt_data)
    
    # Registrar en log
    log_prompt_change(prompt_data)

def handle_prompt_approval(prompt_data):
    """Maneja aprobación de prompt"""
    print(f"Prompt aprobado: {prompt_data['idxprompt']}")
    
    # Activar prompt en sistemas de producción
    activate_prompt_in_production(prompt_data)
    
    # Notificar a equipos relevantes
    notify_teams(prompt_data)
```

---

## 🛠️ HERRAMIENTAS DE INTEGRACIÓN

### **1. CLI Tool**

#### **Instalación:**
```bash
pip install codeflowx-prompts-cli
```

#### **Uso:**
```bash
# Configurar API key
codeflowx-prompts config --api-key your_api_key

# Importar prompts desde archivo
codeflowx-prompts import --file prompts.json --source external

# Sincronizar con sistema externo
codeflowx-prompts sync --source mysql --config mysql_config.json

# Validar prompts importados
codeflowx-prompts validate --prompt-ids 1,2,3,4,5

# Generar reporte de integración
codeflowx-prompts report --type integration --output integration_report.html
```

### **2. Docker Integration**

#### **Dockerfile:**
```dockerfile
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .

CMD ["python", "integration_service.py"]
```

#### **Docker Compose:**
```yaml
version: '3.8'

services:
  prompt-integration:
    build: .
    environment:
      - CODEFLOWX_API_KEY=${CODEFLOWX_API_KEY}
      - MYSQL_HOST=${MYSQL_HOST}
      - MYSQL_USER=${MYSQL_USER}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
    volumes:
      - ./config:/app/config
      - ./logs:/app/logs
    restart: unless-stopped
    
  scheduler:
    build: .
    command: python scheduler.py
    environment:
      - CODEFLOWX_API_KEY=${CODEFLOWX_API_KEY}
    depends_on:
      - prompt-integration
    restart: unless-stopped
```

---

## 📊 MONITOREO DE INTEGRACIÓN

### **1. Métricas de Integración**

#### **Dashboard de Métricas:**
```python
class IntegrationMetrics:
    def __init__(self, client):
        self.client = client
    
    def get_integration_stats(self):
        """Obtiene estadísticas de integración"""
        stats = {
            "total_prompts": 0,
            "external_prompts": 0,
            "internal_prompts": 0,
            "sync_success_rate": 0,
            "validation_success_rate": 0,
            "approval_success_rate": 0
        }
        
        # Obtener todos los prompts
        prompts = self.client.prompts.list()
        stats["total_prompts"] = len(prompts)
        
        # Contar por fuente
        for prompt in prompts:
            metadata = json.loads(prompt.prmmetadata or "{}")
            source = metadata.get("source", "internal")
            
            if source == "internal":
                stats["internal_prompts"] += 1
            else:
                stats["external_prompts"] += 1
        
        # Calcular tasas de éxito
        stats["sync_success_rate"] = self.calculate_sync_success_rate()
        stats["validation_success_rate"] = self.calculate_validation_success_rate()
        stats["approval_success_rate"] = self.calculate_approval_success_rate()
        
        return stats
    
    def calculate_sync_success_rate(self):
        """Calcula tasa de éxito de sincronización"""
        # Implementar lógica de cálculo
        return 95.5
    
    def calculate_validation_success_rate(self):
        """Calcula tasa de éxito de validación"""
        # Implementar lógica de cálculo
        return 87.3
    
    def calculate_approval_success_rate(self):
        """Calcula tasa de éxito de aprobación"""
        # Implementar lógica de cálculo
        return 92.1
```

### **2. Alertas de Integración**

#### **Sistema de Alertas:**
```python
class IntegrationAlerts:
    def __init__(self, client):
        self.client = client
    
    def check_integration_health(self):
        """Verifica salud de integraciones"""
        alerts = []
        
        # Verificar sincronizaciones fallidas
        failed_syncs = self.get_failed_syncs()
        if len(failed_syncs) > 5:
            alerts.append({
                "type": "SYNC_FAILURE",
                "severity": "HIGH",
                "message": f"{len(failed_syncs)} sincronizaciones fallidas",
                "details": failed_syncs
            })
        
        # Verificar validaciones fallidas
        failed_validations = self.get_failed_validations()
        if len(failed_validations) > 10:
            alerts.append({
                "type": "VALIDATION_FAILURE",
                "severity": "MEDIUM",
                "message": f"{len(failed_validations)} validaciones fallidas",
                "details": failed_validations
            })
        
        # Verificar aprobaciones pendientes
        pending_approvals = self.get_pending_approvals()
        if len(pending_approvals) > 20:
            alerts.append({
                "type": "PENDING_APPROVALS",
                "severity": "LOW",
                "message": f"{len(pending_approvals)} aprobaciones pendientes",
                "details": pending_approvals
            })
        
        return alerts
    
    def send_alerts(self, alerts):
        """Envía alertas a sistemas de monitoreo"""
        for alert in alerts:
            # Enviar a Slack
            self.send_slack_alert(alert)
            
            # Enviar email
            self.send_email_alert(alert)
            
            # Registrar en log
            self.log_alert(alert)
```

---

## ✅ CHECKLIST DE INTEGRACIÓN

### **Pre-Integración:**
- [ ] **API Key configurada** y validada
- [ ] **Permisos verificados** para operaciones necesarias
- [ ] **Conexión de red** establecida y probada
- [ ] **Formato de datos** mapeado correctamente
- [ ] **Validaciones** configuradas según requerimientos

### **Durante la Integración:**
- [ ] **Importación incremental** para evitar sobrecarga
- [ ] **Validación automática** de cada prompt importado
- [ ] **Logging completo** de operaciones
- [ ] **Manejo de errores** robusto implementado
- [ ] **Rollback plan** preparado

### **Post-Integración:**
- [ ] **Validación de datos** importados
- [ ] **Testing de funcionalidad** completa
- [ ] **Monitoreo activo** configurado
- [ ] **Documentación actualizada**
- [ ] **Equipo capacitado** en nuevas funcionalidades

### **Mantenimiento Continuo:**
- [ ] **Sincronización automática** configurada
- [ ] **Monitoreo de métricas** activo
- [ ] **Alertas configuradas** para problemas
- [ ] **Backup y recovery** planificado
- [ ] **Actualizaciones regulares** programadas

---

## ✅ CONCLUSIÓN

La **integración del módulo prompts** proporciona capacidades **completas y robustas** para conectar sistemas externos e internos con:

- 🔌 **Integración externa** con sistemas legacy y APIs de terceros
- 🏠 **Desarrollo interno** con templates y pipelines automatizados
- 🔄 **Sincronización automática** con monitoreo continuo
- 🛠️ **Herramientas especializadas** para facilitar integraciones
- 📊 **Monitoreo y alertas** para mantener salud del sistema
- ✅ **Checklist completo** para garantizar integraciones exitosas

**Esta capacidad de integración** es fundamental para el éxito del gobierno de prompts de IA en entornos empresariales complejos.

