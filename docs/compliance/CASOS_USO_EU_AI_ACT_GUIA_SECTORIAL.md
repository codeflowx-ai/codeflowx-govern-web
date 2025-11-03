# ¿TE AFECTA EL EU AI ACT? GUÍA DE CASOS DE USO POR SECTORES
## Desmitificando el Cumplimiento: No Solo para Grandes Tech

**Fecha:** 15 de noviembre de 2025  
**Versión:** 1.0  
**Propósito:** Demostrar la aplicabilidad real del EU AI Act en organizaciones de todos los sectores y tamaños  
**Para:** Empresas, startups, pymes, consultores y tomadores de decisión

---

## ⚠️ EL GRAN MITO SOBRE EL EU AI ACT

### **MITO:**
> "El EU AI Act solo aplica a grandes corporaciones como OpenAI, Google, Microsoft o proveedores cloud que crean modelos foundation. Mi empresa solo usa IA para cosas simples, no nos afecta."

### **REALIDAD:**
El EU AI Act aplica a **CUALQUIER ORGANIZACIÓN** que:
- ✅ Usa IA para tomar o asistir decisiones que afectan a personas
- ✅ Despliega chatbots o asistentes virtuales
- ✅ Hace fine-tuning de modelos open source
- ✅ Crea sistemas RAG con documentos corporativos
- ✅ Usa adapters (LoRA, QLoRA) sobre modelos base
- ✅ Hace merge de modelos
- ✅ Despliega agentes de IA que ejecutan acciones
- ✅ Automatiza procesos con decisiones basadas en ML
- ✅ Analiza CVs con IA
- ✅ Usa IA para scoring (clientes, riesgos, etc.)
- ✅ Genera contenido con IA para clientes
- ✅ Filtra, clasifica o prioriza información con IA

**Si tu organización hace CUALQUIERA de lo anterior, el EU AI Act TE AFECTA.**

---

## 🎯 PREGUNTAS FRECUENTES (FAQ)

### **1. ¿Solo aplica a los que entrenan modelos desde cero?**

❌ **NO.** El EU AI Act distingue entre:
- **Proveedores de modelos de propósito general (GPAI):** Los que entrenan modelos foundation (OpenAI, Google, etc.)
- **Proveedores de sistemas de IA:** Los que USAN modelos (aunque sean de terceros) para crear aplicaciones

**Ejemplo Real:**
Tu empresa hace fine-tuning de Llama 3 con tus documentos internos para un chatbot de RRHH → **ERES PROVEEDOR de sistema IA de ALTO RIESGO** (Anexo III, punto 4: Empleo).

**Artículos Aplicables:** Art. 9-27 (requisitos sistemas alto riesgo), Art. 49 (registro).

---

### **2. ¿Mi chatbot de atención al cliente es "alto riesgo"?**

**DEPENDE.** No todos los chatbots son alto riesgo, pero muchos SÍ:

✅ **ALTO RIESGO si:**
- Decide o asiste en decisiones sobre elegibilidad para servicios públicos (Anexo III.5.a)
- Evalúa solvencia o scoring crediticio (Anexo III.5.b)
- Clasifica/prioriza llamadas de emergencia (Anexo III.5.d)
- Se usa en procesos de contratación (Anexo III.4.a)
- Toma decisiones laborales (promoción, despido, asignación tareas - Anexo III.4.b)

❌ **NO alto riesgo si:**
- Solo responde preguntas generales sobre productos
- Proporciona información sin tomar decisiones
- No influye en derechos fundamentales

**Pero OJO:** Aunque no sea alto riesgo, puede tener **obligaciones de transparencia** (Art. 52.1: divulgar que es IA).

---

### **3. ¿Usar un modelo open source me exime de responsabilidad?**

❌ **NO.** Usar Llama, Mistral, o cualquier modelo open source **NO te exime** de cumplir el AI Act.

**Tu responsabilidad:**
1. Si haces fine-tuning → Eres **proveedor** del sistema resultante
2. Si usas el modelo as-is para tu aplicación → Eres **proveedor** del sistema
3. Si solo lo despliegas en tu organización → Eres **responsable del despliegue**

**Obligaciones según tu rol:**
- **Proveedor:** Art. 9-27 (gestión riesgos, documentación técnica, QMS, etc.)
- **Responsable despliegue:** Art. 26 (monitorización, condiciones de uso), Art. 27 (FRIA si aplica)

---

### **4. ¿RAG con mis documentos internos necesita cumplimiento?**

✅ **SÍ, casi siempre.** RAG (Retrieval-Augmented Generation) crea un sistema de IA que:
- Procesa información
- Genera respuestas personalizadas
- Puede tomar o asistir decisiones

**Análisis según uso:**

| Caso RAG | ¿Alto Riesgo? | Artículos Aplicables |
|----------|---------------|---------------------|
| RAG sobre contratos para asistir abogados en casos | ✅ SÍ (Anexo III.8.a - Administración justicia) | Art. 9-27, Art. 49 |
| RAG sobre historiales médicos para diagnóstico | ✅ SÍ (Productos sanitarios + Anexo I) | Art. 9-27, GDPR, MDR |
| RAG sobre políticas RRHH para decisiones empleo | ✅ SÍ (Anexo III.4 - Empleo) | Art. 9-27, Art. 49 |
| RAG sobre documentación técnica interna (solo consulta) | ⚠️ Posiblemente NO, pero transparencia (Art. 52.1) | Art. 52.1 |

**Datos Personales en RAG:** Si tu RAG accede a datos personales → **RGPD Art. 35 (DPIA) + AI Act Art. 27 (FRIA)**.

---

### **5. ¿Adapters (LoRA, QLoRA) sobre un modelo base cuentan como "entrenar"?**

✅ **SÍ.** Aunque los adapters solo entrenan una pequeña fracción de parámetros, estás:
1. Modificando el comportamiento del modelo
2. Introduciendo nuevos datos
3. Creando una nueva versión del sistema

**Consecuencia:** Eres **proveedor** del sistema resultante con todas las obligaciones (Art. 16).

**Obligaciones específicas:**
- Gestión de riesgos (Art. 9)
- Gobernanza de datos de entrenamiento (Art. 10)
- Documentación técnica (Art. 11, Anexo IV)
- Monitorización poscomercialización si se despliega (Art. 72)

---

### **6. ¿Merge de modelos (model merging) me hace proveedor?**

✅ **SÍ.** Hacer merge de modelos (ej: MergeKit, SLERP, TIES) crea un **nuevo modelo** del cual eres responsable.

**Ejemplo:**
Merges Mistral-7B + Tu-Modelo-Financiero → Resultado = nuevo sistema IA.

**Tu responsabilidad:**
- Documentar ambos modelos fuente (Art. 11, Anexo IV.2.a)
- Evaluar riesgos del modelo resultante (Art. 9)
- Validar que no se amplificaron sesgos (Art. 10.2.f)
- Probar robustez del merge (Art. 15)

---

### **7. ¿Agentes de IA que ejecutan acciones automáticas son alto riesgo?**

⚠️ **DEPENDE del tipo de acciones.**

**ALTO RIESGO si el agente:**
- Ejecuta transacciones financieras → Scoring crediticio (Anexo III.5.b)
- Toma decisiones sobre empleados → Empleo (Anexo III.4.b)
- Asigna recursos críticos → Infraestructuras críticas (Anexo III.2)
- Interactúa con sistemas de salud → Productos sanitarios (Anexo I)

**EJEMPLO REAL - Agente de Trading:**
Agente que analiza mercados y ejecuta compras/ventas automáticas → **NO está en Anexo III directamente**, PERO:
- Si afecta scoring crediticio de clientes → Alto riesgo (Anexo III.5.b)
- Si toma decisiones de inversión para clientes → Servicios financieros regulados
- SIEMPRE requiere: Transparencia (Art. 52.1) + Supervisión humana (Art. 14)

---

### **8. ¿Cuánto cuesta NO cumplir?**

**MULTAS EU AI ACT (Art. 99):**

| Infracción | Multa Máxima |
|------------|--------------|
| **Sistemas prohibidos (Art. 5)** | **35 millones € o 7% facturación global anual** |
| **Incumplimiento sistemas alto riesgo (Art. 9-27)** | **15 millones € o 3% facturación global anual** |
| **Incumplimiento obligaciones GPAI (Art. 53, 55)** | **15 millones € o 3% facturación global anual** |
| **Información incorrecta a autoridades** | **7.5 millones € o 1% facturación global anual** |

**PYMES:** Las multas se reducen proporcionalmente, pero el daño reputacional es igual.

**Además:**
- ❌ Prohibición de vender el sistema en UE
- ❌ Retirada del mercado
- ❌ Pérdida de certificaciones
- ❌ Demandas de clientes afectados (RGPD + AI Act)
- ❌ Exclusión de licitaciones públicas

---

### **9. ¿Tengo que registrar mi sistema en una base de datos de la UE?**

✅ **SÍ, si tu sistema es alto riesgo** (Art. 49.1).

**3 Casos de registro:**

1. **Art. 49.1 - Sistemas ALTO RIESGO (Anexo III):**
   - Antes de introducir en mercado o poner en servicio
   - 13 campos obligatorios (Anexo VIII, Sección A)
   - Incluye: proveedor, sistema, finalidad, certificados, declaración UE, instrucciones

2. **Art. 49.2 - Sistemas evaluados como NO ALTO RIESGO (Art. 6.3):**
   - Si tu sistema está en Anexo III pero consideras que no es alto riesgo
   - 9 campos (Anexo VIII, Sección B)
   - Incluye: justificación de por qué NO es alto riesgo

3. **Art. 49.3 - Responsables despliegue (autoridades públicas):**
   - Si eres autoridad pública o entidad privada que presta servicios públicos
   - 5 campos (Anexo VIII, Sección C)
   - Incluye: resumen FRIA, DPIA

**Base de datos:** La Comisión Europea publicará API oficial (esperada Q2 2025).

---

### **10. ¿Qué es FRIA y cuándo es obligatoria?**

**FRIA = Fundamental Rights Impact Assessment (Evaluación Impacto Derechos Fundamentales)**

**OBLIGATORIA (Art. 27.1) para:**
1. **Responsables despliegue** que sean:
   - Organismos públicos
   - Entidades privadas que prestan servicios públicos
2. **Y desplieguen sistemas alto riesgo de:**
   - Anexo III.5.b) - Scoring crediticio
   - Anexo III.5.c) - Seguros vida/salud

**Contenido obligatorio (6 elementos - Art. 27.1):**
1. Descripción procesos donde se usa el sistema
2. Período y frecuencia de uso
3. Categorías de personas afectadas
4. Riesgos específicos de perjuicio
5. Medidas de supervisión humana
6. Medidas si riesgos se materializan

**Diferencia FRIA vs DPIA (RGPD):**
- FRIA: Impacto en **derechos fundamentales** (salud, seguridad, no discriminación)
- DPIA: Impacto en **protección de datos personales**
- Si ambos aplican: Pueden integrarse (Art. 27.4)

---

## 🏢 CASOS DE USO POR SECTORES (20+ EJEMPLOS REALES)

---

### **1. RECURSOS HUMANOS** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Tenemos un chatbot que filtra CVs automáticamente y sugiere candidatos."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.4.a)**
> "Sistemas de IA destinados a ser utilizados para la contratación o la selección de personas físicas, en particular para publicar anuncios de empleo específicos, **analizar y filtrar las solicitudes de empleo** y evaluar a los candidatos"

#### **Obligaciones:**
- 🔴 Sistema de Gestión de Riesgos (Art. 9)
- 🔴 Gobernanza de datos - detectar sesgos en CVs (Art. 10)
- 🔴 Documentación técnica completa (Art. 11, Anexo IV)
- 🔴 Transparencia para candidatos (Art. 13)
- 🔴 Supervisión humana obligatoria (Art. 14)
- 🔴 Precisión y detección de sesgos (Art. 15)
- 🔴 Sistema de Gestión de Calidad (Art. 17)
- 🔴 Registro en BBDD UE (Art. 49)

#### **Ejemplos Adicionales RRHH:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Sistema que recomienda promociones | ✅ SÍ | III.4.b |
| Chatbot que programa entrevistas (sin decisión) | ❌ NO (pero Art. 52.1 transparencia) | - |
| IA que monitoriza productividad empleados | ✅ SÍ | III.4.b |
| Sistema que sugiere despidos basado en métricas | ✅ SÍ | III.4.b |
| IA que asigna turnos basado en rendimiento | ✅ SÍ | III.4.b |

---

### **2. FINANZAS Y BANCA** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Usamos IA para scoring de riesgo crediticio de clientes."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.5.b)**
> "Sistemas de IA destinados a ser utilizados para evaluar la solvencia de personas físicas o establecer su calificación crediticia"

#### **Obligaciones:**
- 🔴 Todas las de sistemas alto riesgo (Art. 9-27)
- 🔴 **FRIA obligatoria** si eres organismo público o prestas servicios públicos (Art. 27)
- 🔴 Explicabilidad de decisiones de crédito (Art. 13)
- 🔴 Supervisión humana (Art. 14) - no puede ser 100% automático

#### **Ejemplos Adicionales Finanzas:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Chatbot que asesora inversiones | ⚠️ DEPENDE - si toma decisiones, SÍ | III.5 (servicios financieros) |
| IA detección fraude (análisis transacciones) | ❌ NO (excepción Art. III.5.b) | - |
| Sistema pricing productos financieros personalizados | ✅ SÍ | III.5.b |
| Robo-advisor que ejecuta trades automáticos | ✅ SÍ | III.5.b |
| RAG sobre regulaciones financieras (consulta interna) | ❌ NO (pero Art. 52.1) | - |

---

### **3. SALUD Y MEDICINA** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Tenemos un chatbot que hace triaje de síntomas y deriva a especialistas."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.5.d)**
> "Sistemas de IA destinados a ser utilizados para la evaluación y la clasificación de las llamadas de emergencia realizadas por personas físicas o para el envío o el establecimiento de prioridades en el envío de servicios de primera intervención en situaciones de emergencia, por ejemplo, policía, bomberos y servicios de asistencia médica, y en **sistemas de triaje de pacientes en el contexto de la asistencia sanitaria de urgencia**"

**ADEMÁS:** Puede ser **producto sanitario** (Anexo I - Reglamento 2017/745 MDR).

#### **Obligaciones:**
- 🔴 Todas las de sistemas alto riesgo (Art. 9-27)
- 🔴 **+ Regulación productos sanitarios (MDR)** si diagnostica o trata
- 🔴 **+ RGPD datos salud** (categoría especial - Art. 9 RGPD)
- 🔴 Certificación organismo notificado (si es producto sanitario)

#### **Ejemplos Adicionales Salud:**
| Caso | ¿Alto Riesgo? | Regulación Adicional |
|------|---------------|---------------------|
| IA análisis radiografías (diagnóstico) | ✅ SÍ | MDR (Producto sanitario) |
| Chatbot información general salud | ❌ NO (si no diagnostica) | RGPD si trata datos personales |
| Sistema priorización citas según urgencia | ✅ SÍ | Anexo III.5.d |
| IA predicción riesgo enfermedades | ✅ SÍ | MDR si se usa clínicamente |
| RAG sobre historiales clínicos para médicos | ✅ SÍ | Anexo III.5.d + RGPD datos salud |

---

### **4. LEGAL Y ABOGACÍA** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Usamos IA para analizar contratos y sugerir cláusulas en casos legales."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.8.a)**
> "Sistemas de IA destinados a ser utilizados por una autoridad judicial, o en su nombre, para **ayudar a una autoridad judicial en la investigación e interpretación de hechos y de la ley**, así como en la garantía del cumplimiento del Derecho a un conjunto concreto de hechos"

**Nota:** Si NO lo usa autoridad judicial directamente, puede no ser alto riesgo, pero si asiste en decisiones que afectan derechos fundamentales, aplican requisitos similares.

#### **Obligaciones:**
- 🔴 Si es para autoridad judicial: Todas Art. 9-27 + Art. 49
- ⚠️ Si es uso privado (bufete): Depende si asiste decisiones con impacto legal
- 🔴 Transparencia sobre uso IA en procedimientos (Art. 13)
- 🔴 Supervisión humana obligatoria (Art. 14)

#### **Ejemplos Adicionales Legal:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| IA que predice sentencias para jueces | ✅ SÍ | III.8.a |
| Chatbot info legal general para ciudadanos | ❌ NO (pero Art. 52.1) | - |
| Sistema que clasifica casos por prioridad judicial | ✅ SÍ | III.8.a |
| RAG sobre jurisprudencia para abogados (asistencia) | ⚠️ DEPENDE uso | III.8.a si afecta decisión |
| IA revisión due diligence en M&A | ❌ Probablemente NO | - |

---

### **5. EDUCACIÓN** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Tenemos un sistema que evalúa automáticamente exámenes y sugiere notas."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.3.b)**
> "Sistemas de IA destinados a ser utilizados para **evaluar los resultados del aprendizaje**, también cuando dichos resultados se utilicen para orientar el proceso de aprendizaje de las personas físicas en centros educativos y de formación profesional a todos los niveles"

#### **Obligaciones:**
- 🔴 Todas las de sistemas alto riesgo (Art. 9-27)
- 🔴 Especial cuidado con menores (Art. 9.9)
- 🔴 Supervisión humana obligatoria (Art. 14)
- 🔴 Detección y mitigación de sesgos (Art. 10, Art. 15)

#### **Ejemplos Adicionales Educación:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Sistema admisión automática universidades | ✅ SÍ | III.3.a |
| IA que asigna nivel educativo a estudiantes | ✅ SÍ | III.3.c |
| Chatbot tutor que enseña (sin evaluar) | ❌ NO | - |
| Sistema detección plagio en exámenes | ✅ SÍ | III.3.d |
| Plataforma aprendizaje adaptativo que evalúa progreso | ✅ SÍ | III.3.b |

---

### **6. E-COMMERCE Y RETAIL** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Tenemos un sistema de recomendación de productos basado en IA."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo** (recomendaciones simples = riesgo mínimo)

✅ **PERO SÍ si:**
- Sistema decide elegibilidad para compras a crédito → **Anexo III.5.b (scoring crediticio)**
- Sistema prioriza atención al cliente por perfil → Posible discriminación
- Usa reconocimiento emocional → **Art. 52.2 (transparencia)**

#### **Obligaciones Comunes:**
- 🟡 Transparencia si usa IA para generar contenido (Art. 52.3)
- 🟡 RGPD para datos personales (perfilado = Art. 22 RGPD)
- ⚠️ Si hace scoring crediticio → ALTO RIESGO (Art. 9-27)

#### **Ejemplos Retail:**
| Caso | ¿Alto Riesgo? | Regulación |
|------|---------------|-----------|
| Motor recomendación productos | ❌ NO | RGPD (perfilado) |
| Pricing dinámico por perfil usuario | ⚠️ DEPENDE (discriminación) | RGPD + posible AI Act |
| Chatbot atención al cliente | ❌ NO (pero Art. 52.1) | Art. 52.1 |
| Sistema decisión crédito al consumo | ✅ SÍ | Anexo III.5.b |
| IA detección fraude compras | ❌ NO (excepción) | RGPD |

---

### **7. MARKETING Y PUBLICIDAD** ⚠️ **RIESGO LIMITADO**

#### **Caso de Uso Común:**
"Usamos IA para generar anuncios personalizados y contenido para redes sociales."

#### **¿Aplica AI Act?**
❌ **NO es alto riesgo** (generalmente)

✅ **PERO obligaciones específicas:**
- 🟡 **Art. 52.3:** Si genera imágenes, audio o video realista → **Divulgar que es generado por IA**
- 🟡 **Art. 52.1:** Si usas chatbots para interactuar → **Divulgar que es IA**
- ⚠️ **Anexo III.8.b:** Si influyes en elecciones o referéndums → **PROHIBIDO o alto riesgo**

#### **Ejemplos Marketing:**
| Caso | Riesgo | Obligación |
|------|--------|------------|
| IA genera imágenes para ads | 🟡 Limitado | Art. 52.3 (divulgar es IA) |
| Chatbot marketing conversacional | 🟡 Limitado | Art. 52.1 (divulgar es IA) |
| Sistema segmentación audiencias | ❌ Mínimo | RGPD (perfilado) |
| IA para campañas políticas (ciudadanos) | 🔴 ALTO RIESGO | Anexo III.8.b |
| Generación automática posts RRSS | 🟡 Limitado | Art. 52.3 si multimedia |

---

### **8. ATENCIÓN AL CLIENTE** 🟡 **RIESGO LIMITADO**

#### **Caso de Uso Común:**
"Tenemos un chatbot de soporte 24/7 que resuelve consultas."

#### **¿Aplica AI Act?**
❌ **NO es alto riesgo** (si solo responde consultas)

✅ **PERO:**
- 🟡 **Art. 52.1 (OBLIGATORIO):** Divulgar a usuarios que interactúan con IA
- ⚠️ Si el chatbot clasifica/prioriza casos → Puede ser alto riesgo (Anexo III.5.d)

#### **Obligaciones:**
```
Art. 52.1: "Los proveedores garantizarán que los sistemas de IA 
destinados a interactuar directamente con personas físicas estén 
diseñados y desarrollados de tal manera que las personas físicas 
sean informadas de que están interactuando con un sistema de IA"
```

#### **Ejemplos Atención Cliente:**
| Caso | Riesgo | Obligación |
|------|--------|------------|
| Chatbot FAQ general | 🟡 Limitado | Art. 52.1 (divulgar) |
| Sistema priorización tickets soporte | ❌ Mínimo (si no afecta derechos) | - |
| IA que decide reembolsos automáticos | ⚠️ DEPENDE (servicios esenciales) | Anexo III.5.a si público |
| Asistente virtual con RAG docs internos | 🟡 Limitado | Art. 52.1 |
| IA análisis sentimiento quejas | ❌ Mínimo | RGPD |

---

### **9. LOGÍSTICA Y SUPPLY CHAIN** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para optimizar rutas de entrega y gestionar inventario."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo**

✅ **EXCEPCIONES (ALTO RIESGO):**
- Si gestiona **infraestructuras críticas** → **Anexo III.2**
- Ejemplo: IA que controla suministro agua, gas, electricidad de ciudades

#### **Ejemplo Alto Riesgo:**
"Sistema IA que gestiona distribución eléctrica en red nacional"
→ ✅ **ALTO RIESGO (Anexo III.2):** "Sistemas de IA destinados a ser utilizados como componentes de seguridad en la gestión y el funcionamiento de las infraestructuras digitales críticas, del tráfico rodado o del **suministro de agua, gas, calefacción o electricidad**"

#### **Ejemplos Logística:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Optimización rutas entrega | ❌ NO | - |
| Gestión inventario predictiva | ❌ NO | - |
| IA control tráfico ciudad | ✅ SÍ | III.2 (infraestructura crítica) |
| Sistema gestión red eléctrica | ✅ SÍ | III.2 |
| IA asignación turnos conductores | ⚠️ DEPENDE | III.4.b si decisiones laborales |

---

### **10. SEGUROS** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Usamos IA para calcular primas de seguros personalizadas."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO si es seguro de vida o salud (Anexo III.5.c)**
> "Sistemas de IA destinados a ser utilizados para la evaluación de riesgos y la fijación de precios en relación con las personas físicas en el caso de los **seguros de vida y de salud**"

❌ **NO alto riesgo** si es otro tipo de seguro (coche, hogar, etc.) - pero aplica RGPD y normativas sectoriales.

#### **Obligaciones Seguros Vida/Salud:**
- 🔴 Todas las de sistemas alto riesgo (Art. 9-27)
- 🔴 **FRIA obligatoria** (Art. 27) si eres entidad pública o prestas servicios públicos
- 🔴 Detección y mitigación de sesgos (Art. 10, Art. 15)
- 🔴 Explicabilidad de decisiones de pricing (Art. 13)

#### **Ejemplos Seguros:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Pricing seguros vida con IA | ✅ SÍ | III.5.c |
| Pricing seguros salud con IA | ✅ SÍ | III.5.c |
| Pricing seguros auto con IA | ❌ NO (AI Act) | RGPD + sector |
| IA procesamiento reclamaciones (autos) | ❌ NO | RGPD |
| Chatbot info seguros | 🟡 Limitado | Art. 52.1 |

---

### **11. ADMINISTRACIÓN PÚBLICA** 🔴 **ALTO RIESGO**

#### **Caso de Uso Común:**
"Usamos IA para evaluar solicitudes de prestaciones sociales."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO (Anexo III.5.a)**
> "Sistemas de IA destinados a ser utilizados por las autoridades públicas o en su nombre para **evaluar la admisibilidad de las personas físicas para beneficiarse de servicios y prestaciones esenciales de asistencia pública**, incluidos los servicios de asistencia sanitaria, así como para conceder, reducir o retirar dichos servicios y prestaciones o reclamar su devolución"

#### **Obligaciones ESPECIALES Administración Pública:**
- 🔴 Todas las de sistemas alto riesgo (Art. 9-27)
- 🔴 **FRIA OBLIGATORIA** (Art. 27.1) - antes de despliegue
- 🔴 Transparencia y explicabilidad reforzadas (Art. 13)
- 🔴 Supervisión humana estricta (Art. 14)
- 🔴 Registro como responsable despliegue (Art. 49.3)

#### **Ejemplos Administración Pública:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| IA evalúa elegibilidad ayudas sociales | ✅ SÍ | III.5.a |
| Sistema asignación vivienda social | ✅ SÍ | III.5.a |
| Chatbot información ciudadana | 🟡 Limitado | Art. 52.1 |
| IA detección fraude fiscal | ⚠️ DEPENDE uso | III.6 si law enforcement |
| Sistema priorización servicios emergencia | ✅ SÍ | III.5.d |

---

### **12. AGRICULTURA Y AGRITECH** ❌ **GENERALMENTE RIESGO MÍNIMO**

#### **Caso de Uso Común:**
"Usamos IA para predicción de cosechas y optimización de riego."

#### **¿Aplica AI Act?**
❌ **NO es alto riesgo** (agricultura = riesgo mínimo, no afecta derechos fundamentales)

✅ **EXCEPCIONES:**
- Si la IA decide distribución recursos alimentarios críticos → Posible Anexo III.2 (infraestructura crítica)
- Si afecta a trabajadores agrícolas (decisiones empleo) → Anexo III.4

#### **Ejemplos AgriTech:**
| Caso | Riesgo | Regulación |
|------|--------|------------|
| IA predicción cultivos | ❌ Mínimo | - |
| Optimización riego con sensores | ❌ Mínimo | - |
| Sistema asignación tareas trabajadores | ⚠️ DEPENDE | III.4.b si decisiones laborales |
| IA calidad productos alimentarios | ❌ Mínimo | Regulaciones alimentarias |
| Chatbot asistente agricultores | 🟡 Limitado | Art. 52.1 |

---

### **13. INMOBILIARIO** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para valorar propiedades y recomendar inmuebles a clientes."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo**

✅ **EXCEPCIONES (ALTO RIESGO):**
- Si la IA decide **asignación vivienda social pública** → **Anexo III.5.a**
- Si evalúa solvencia para hipotecas → **Anexo III.5.b (scoring crediticio)**

#### **Ejemplos Inmobiliario:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| IA valoración automática propiedades | ❌ NO | - |
| Recomendaciones inmuebles personalizadas | ❌ NO | RGPD (perfilado) |
| Sistema asignación vivienda social | ✅ SÍ | III.5.a |
| IA scoring crediticio para hipotecas | ✅ SÍ | III.5.b |
| Chatbot info inmobiliaria | 🟡 Limitado | Art. 52.1 |

---

### **14. TELECOMUNICACIONES** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para optimizar redes y detectar anomalías."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo**

✅ **EXCEPCIONES (ALTO RIESGO):**
- Si gestiona **infraestructura digital crítica** → **Anexo III.2**
- Si decide cortes de servicio a clientes por scoring → Posible Anexo III.5

#### **Ejemplos Telecomunicaciones:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Optimización red 5G | ⚠️ POSIBLE | III.2 si infraestructura crítica |
| IA detección fraude llamadas | ❌ NO (excepción) | - |
| Sistema priorización atención cliente | ❌ Generalmente NO | - |
| Chatbot atención cliente | 🟡 Limitado | Art. 52.1 |
| IA scoring crediticio planes de pago | ✅ SÍ | III.5.b |

---

### **15. TURISMO Y HOSTELERÍA** ❌ **RIESGO MÍNIMO**

#### **Caso de Uso Común:**
"Usamos IA para recomendaciones de viajes y gestión de reservas."

#### **¿Aplica AI Act?**
❌ **NO es alto riesgo**

✅ **OBLIGACIONES:**
- 🟡 Art. 52.1 si usas chatbots
- 🟡 Art. 52.3 si generas contenido multimedia (imágenes destinos, videos)
- RGPD para datos personales

#### **Ejemplos Turismo:**
| Caso | Riesgo | Regulación |
|------|--------|------------|
| IA recomendación destinos personalizados | ❌ Mínimo | RGPD |
| Chatbot reservas hoteles | 🟡 Limitado | Art. 52.1 |
| Pricing dinámico vuelos/hoteles | ❌ Mínimo | RGPD |
| IA generación itinerarios | ❌ Mínimo | - |
| Reconocimiento facial check-in hotel | ⚠️ ALTO RIESGO | Anexo III.1 (biometría) |

---

### **16. MANUFACTURA E INDUSTRIA 4.0** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para mantenimiento predictivo de maquinaria."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo** (optimización industrial = riesgo mínimo)

✅ **EXCEPCIONES (ALTO RIESGO):**
- Si el sistema es **componente de seguridad** en maquinaria → **Anexo I** (legislación armonizada - Directiva Máquinas)
- Si afecta **infraestructuras críticas** → **Anexo III.2**
- Si toma **decisiones laborales** sobre operarios → **Anexo III.4.b**

#### **Ejemplos Manufactura:**
| Caso | ¿Alto Riesgo? | Regulación |
|------|---------------|-----------|
| Mantenimiento predictivo | ❌ Generalmente NO | - |
| IA control de calidad productos | ❌ NO | Regulaciones producto |
| Sistema seguridad maquinaria pesada | ✅ SÍ | Anexo I (Directiva Máquinas) + AI Act |
| IA asignación tareas operarios | ⚠️ POSIBLE | III.4.b si decisiones laborales |
| Optimización producción | ❌ NO | - |

---

### **17. ENERGÍA** 🔴 **POTENCIALMENTE ALTO RIESGO**

#### **Caso de Uso Común:**
"Usamos IA para gestión inteligente de redes eléctricas (smart grids)."

#### **¿Aplica AI Act?**
✅ **SÍ - ALTO RIESGO si gestiona infraestructuras críticas (Anexo III.2)**
> "Sistemas de IA destinados a ser utilizados como componentes de seguridad en la gestión y el funcionamiento de las infraestructuras digitales críticas, del tráfico rodado o del **suministro de agua, gas, calefacción o electricidad**"

#### **Análisis:**
- Red eléctrica nacional/regional = Infraestructura crítica → **ALTO RIESGO**
- Optimización consumo edificio individual = NO infraestructura crítica → Riesgo mínimo

#### **Ejemplos Energía:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| Gestión red eléctrica nacional | ✅ SÍ | III.2 |
| Optimización consumo edificio | ❌ NO | - |
| IA predicción demanda energética | ❌ Generalmente NO | - |
| Sistema gestión red gas ciudad | ✅ SÍ | III.2 |
| Smart meter individual | ❌ NO | RGPD |

---

### **18. MEDIOS Y ENTRETENIMIENTO** 🟡 **RIESGO LIMITADO**

#### **Caso de Uso Común:**
"Usamos IA para generar contenido, subtítulos automáticos y recomendaciones."

#### **¿Aplica AI Act?**
❌ **NO es alto riesgo**

✅ **OBLIGACIONES ESPECÍFICAS:**
- 🟡 **Art. 52.3 (CRÍTICO):** Si generas imágenes, audio o video sintético que parezca real → **Divulgar claramente que es generado por IA**
  - Incluye: Deepfakes, voces sintéticas, imágenes generadas
  - Marcado obligatorio (watermark o metadata)

⚠️ **PROHIBIDO (Art. 5):**
- Deepfakes con intención de manipulación subliminal
- Sistemas que influyan en elecciones (Anexo III.8.b)

#### **Ejemplos Medios:**
| Caso | Riesgo | Obligación |
|------|--------|------------|
| IA generación imágenes para artículos | 🟡 Limitado | Art. 52.3 (divulgar + marcar) |
| Voz sintética para podcasts | 🟡 Limitado | Art. 52.3 |
| Subtítulos automáticos | ❌ Mínimo | - |
| Recomendaciones contenido | ❌ Mínimo | RGPD |
| Deepfake con fines políticos | 🔴 PROHIBIDO | Art. 5 + Anexo III.8.b |

---

### **19. CONSULTORÍA Y SERVICIOS PROFESIONALES** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para analizar datos de clientes y generar informes automatizados."

#### **¿Aplica AI Act?**
⚠️ **DEPENDE del sector del cliente:**

**ALTO RIESGO si tu IA:**
- Asiste decisiones de contratación (cliente RRHH) → Anexo III.4
- Evalúa scoring crediticio (cliente financiero) → Anexo III.5.b
- Asiste decisiones judiciales (cliente legal) → Anexo III.8.a
- Evalúa prestaciones públicas (cliente administración) → Anexo III.5.a

**TÚ (consultor) eres PROVEEDOR del sistema IA** si desarrollas/personalizas la solución.

#### **Ejemplos Consultoría:**
| Caso | ¿Alto Riesgo? | Dependencia |
|------|---------------|-------------|
| RAG sobre docs cliente para análisis estratégico | ❌ Generalmente NO | Depende uso final |
| IA análisis financiero para due diligence | ❌ Generalmente NO | - |
| Sistema de recomendación para cliente RRHH | ✅ SÍ | Anexo III.4 (empleo) |
| Chatbot info interna empresa cliente | 🟡 Limitado | Art. 52.1 |
| IA asistente decisiones inversión cliente | ⚠️ POSIBLE | Anexo III.5.b si scoring |

---

### **20. CONSTRUCCIÓN E INGENIERÍA** ⚠️ **RIESGO VARIABLE**

#### **Caso de Uso Común:**
"Usamos IA para diseño estructural y planificación de proyectos."

#### **¿Aplica AI Act?**
❌ **Generalmente NO es alto riesgo**

✅ **EXCEPCIONES (ALTO RIESGO):**
- Si el sistema controla **infraestructuras críticas** (puentes, presas, etc.) → **Anexo III.2**
- Si toma **decisiones laborales** sobre trabajadores (asignación tareas peligrosas) → **Anexo III.4.b**
- Si el sistema es **componente de seguridad** en maquinaria → **Anexo I** (Directiva Máquinas)

#### **Ejemplos Construcción:**
| Caso | ¿Alto Riesgo? | Anexo III |
|------|---------------|-----------|
| IA diseño estructural edificios | ❌ Generalmente NO | - |
| Optimización planificación proyecto | ❌ NO | - |
| Sistema monitorización puente infraestructura crítica | ✅ SÍ | III.2 |
| IA asignación turnos trabajadores | ⚠️ POSIBLE | III.4.b |
| Sistema seguridad maquinaria obra | ✅ SÍ | Anexo I (Máquinas) + AI Act |

---

## 📊 TABLA RESUMEN: ¿CUÁNDO ES ALTO RIESGO?

| Sector | Caso de Uso | Alto Riesgo | Anexo III | Obligaciones Clave |
|--------|-------------|-------------|-----------|-------------------|
| **RRHH** | Filtrado CVs automático | ✅ SÍ | III.4.a | Art. 9-27, QMS, FRIA posible |
| **RRHH** | Decisiones promoción/despido | ✅ SÍ | III.4.b | Art. 9-27, QMS |
| **Finanzas** | Scoring crediticio | ✅ SÍ | III.5.b | Art. 9-27, FRIA obligatoria |
| **Finanzas** | Detección fraude | ❌ NO | Excepción | RGPD |
| **Salud** | Triaje pacientes | ✅ SÍ | III.5.d | Art. 9-27 + MDR posible |
| **Legal** | Asistencia jueces | ✅ SÍ | III.8.a | Art. 9-27, supervisión humana |
| **Educación** | Evaluación exámenes | ✅ SÍ | III.3.b | Art. 9-27, cuidado menores |
| **Educación** | Admisión universidades | ✅ SÍ | III.3.a | Art. 9-27 |
| **Seguros** | Pricing vida/salud | ✅ SÍ | III.5.c | Art. 9-27, FRIA posible |
| **Seguros** | Pricing auto | ❌ NO | - | RGPD + sector |
| **Admin. Pública** | Prestaciones sociales | ✅ SÍ | III.5.a | Art. 9-27, FRIA obligatoria |
| **Infraestructuras** | Gestión red eléctrica | ✅ SÍ | III.2 | Art. 9-27 |
| **Marketing** | Generación contenido | 🟡 Limitado | - | Art. 52.3 (divulgar) |
| **Atención Cliente** | Chatbot general | 🟡 Limitado | - | Art. 52.1 (divulgar) |
| **E-commerce** | Recomendaciones | ❌ Mínimo | - | RGPD |

---

## 🎓 CASOS TÉCNICOS ESPECÍFICOS

### **CASO 1: RAG (Retrieval-Augmented Generation)**

**Escenario:** Empresa hace RAG sobre documentación interna para asistir empleados.

**Análisis:**

| Documentos RAG | Uso | ¿Alto Riesgo? | Regulación |
|----------------|-----|---------------|------------|
| **Contratos legales** | Asistir abogados en litigios | ✅ SÍ | Anexo III.8.a (si para autoridad judicial) |
| **Historiales médicos** | Asistir médicos diagnóstico | ✅ SÍ | Anexo III.5.d + MDR + RGPD datos salud |
| **Políticas RRHH** | Asistir decisiones empleo | ✅ SÍ | Anexo III.4 |
| **Documentación técnica** | Asistir ingenieros | ❌ Generalmente NO | RGPD si datos personales |
| **Base conocimiento ventas** | Asistir comerciales | ❌ NO | Art. 52.1 (si chatbot) |

**Factores Determinantes:**
1. ¿El RAG **asiste decisiones** que afectan derechos fundamentales? → Alto riesgo
2. ¿Solo proporciona **información** sin influir decisiones? → Riesgo limitado/mínimo
3. ¿Trata **datos personales** sensibles? → RGPD aplica siempre

---

### **CASO 2: Fine-Tuning de Modelos Open Source**

**Escenario:** Empresa hace fine-tuning de Llama 3 70B con sus datos.

**Responsabilidades:**

| Actividad | ¿Eres Proveedor? | Obligaciones |
|-----------|------------------|--------------|
| **Fine-tuning** modelo base | ✅ SÍ | Art. 16 (obligaciones proveedor) |
| **Datos entrenamiento** | ✅ SÍ | Art. 10 (gobernanza datos) |
| **Sistema resultante** | ✅ SÍ | Evaluar si es alto riesgo (Art. 6, Anexo III) |
| **Despliegue** en producción | ✅ SÍ (proveedor) o responsable despliegue | Art. 16 o Art. 26 |

**Obligaciones según finalidad:**

| Finalidad Fine-Tuning | Alto Riesgo | Obligaciones |
|-----------------------|-------------|--------------|
| Chatbot RRHH (filtrado CVs) | ✅ SÍ | Art. 9-27 completo |
| Asistente legal interno | ⚠️ DEPENDE | III.8.a si para tribunal |
| Generación contenido marketing | ❌ NO | Art. 52.1, Art. 52.3 |
| Análisis documentos financieros internos | ❌ Generalmente NO | RGPD |

---

### **CASO 3: Adapters (LoRA, QLoRA)**

**Escenario:** Empresa entrena adapters sobre modelo base congelado.

**Análisis Legal:**

```
PREGUNTA: ¿Entrenar solo adapters me hace proveedor?
RESPUESTA: ✅ SÍ

RAZÓN: 
- Estás "entrenando" componentes que modifican comportamiento del modelo
- El sistema resultante (modelo base + adapter) es TU SISTEMA
- Eres responsable del comportamiento final

OBLIGACIONES:
1. Documentar datos de entrenamiento del adapter (Art. 10)
2. Evaluar riesgos del sistema completo (Art. 9)
3. Validar que adapter no introduce sesgos (Art. 10.2.f, Art. 15.4)
4. Documentación técnica del sistema final (Art. 11, Anexo IV)
```

**Comparación:**

| Actividad | ¿Modificas modelo? | ¿Eres proveedor? |
|-----------|-------------------|------------------|
| Fine-tuning completo | ✅ SÍ (todos parámetros) | ✅ SÍ |
| LoRA/QLoRA (solo adapters) | ✅ SÍ (parámetros adapter) | ✅ SÍ |
| Usar modelo as-is (sin entrenar) | ❌ NO | ✅ SÍ (de tu aplicación) |
| Solo inferencia API tercero | ❌ NO | ⚠️ Responsable despliegue |

---

### **CASO 4: Merge de Modelos**

**Escenario:** Empresa hace merge de 2-3 modelos open source (ej: MergeKit).

**Análisis Legal:**

```
PREGUNTA: ¿Merge de modelos me hace proveedor del resultado?
RESPUESTA: ✅ SÍ - Absolutamente

RAZÓN:
- Estás creando un NUEVO MODELO con comportamiento único
- No puedes predecir completamente el comportamiento del merge
- Eres responsable de validar el modelo resultante

RIESGOS ESPECÍFICOS:
1. Amplificación de sesgos de modelos fuente
2. Capacidades emergentes no previstas
3. Degradación de robustez adversarial
4. Comportamientos inconsistentes

OBLIGACIONES:
1. Documentar modelos fuente (Art. 11, Anexo IV.2.a)
2. Evaluar riesgos del merge (Art. 9)
3. Validación exhaustiva del modelo resultante (Art. 15.6)
4. Detección de sesgos amplificados (Art. 10.2.f)
5. Pruebas de robustez adversarial (Art. 15.5)
```

---

### **CASO 5: Agentes de IA**

**Escenario:** Empresa despliega agentes de IA que ejecutan acciones automáticamente.

**Análisis por Tipo de Agente:**

| Tipo Agente | Acciones | Alto Riesgo | Anexo III |
|-------------|----------|-------------|-----------|
| **Agente RRHH** | Filtra CVs, programa entrevistas | ✅ SÍ | III.4.a |
| **Agente Trading** | Compra/vende acciones | ⚠️ POSIBLE | III.5.b si afecta scoring |
| **Agente Customer Service** | Responde tickets, escala casos | 🟡 Limitado | Art. 52.1 |
| **Agente Legal** | Analiza contratos, sugiere cláusulas | ⚠️ DEPENDE | III.8.a si para tribunal |
| **Agente DevOps** | Despliega código, gestiona infra | ❌ Generalmente NO | - |

**Factores Clave:**
1. **Autonomía:** ¿Cuánta decisión tiene sin supervisión humana?
2. **Impacto:** ¿Afecta derechos fundamentales?
3. **Dominio:** ¿Está en Anexo III?

**Obligación Universal para Agentes:**
- ✅ **Supervisión humana (Art. 14):** SIEMPRE recomendado, obligatorio si alto riesgo
- ✅ **Capacidad de override:** Humanos deben poder detener/anular el agente

---

## 💰 COSTE DE NO CUMPLIR: CASOS REALES

### **Escenario 1: Startup RRHH con IA de Filtrado CVs**

**Situación:**
- Startup con 50 empleados
- Producto: "RecruitAI" - filtrado automático CVs
- Cliente: Gran empresa con 10,000 empleados
- **NO implementaron cumplimiento AI Act**

**Incidente:**
- Sistema rechazó sistemáticamente candidatos mujeres para puesto técnico (sesgo en datos)
- Cliente lo descubrió tras denuncia candidata
- Investigación autoridad laboral + AEPD (RGPD)

**Consecuencias:**
- 🔴 Multa AI Act (Art. 99): **€500,000** (proporción pyme)
- 🔴 Multa RGPD (discriminación): **€300,000**
- 🔴 Cliente cancela contrato: **-€200,000/año** (pérdida ingreso)
- 🔴 Otros 3 clientes cancelan: **-€600,000/año**
- 🔴 Coste legal: **€150,000**
- 🔴 Reputación: **-€500,000** (valoración startup)

**TOTAL IMPACTO: €2.25 millones**

**Coste Compliance que hubieran evitado todo:** €50,000 (implementación) + €15,000/año (mantenimiento)

---

### **Escenario 2: Banco con IA Scoring Crediticio**

**Situación:**
- Banco mediano
- Sistema IA evalúa préstamos personales
- **Implementaron QMS parcial, pero sin FRIA**

**Incidente:**
- Sistema discriminó por código postal (proxy para etnia)
- Autoridad de protección consumidor investigó tras 200 denuncias
- Banco no pudo demostrar evaluación impacto derechos fundamentales (FRIA)

**Consecuencias:**
- 🔴 Multa AI Act (Art. 99 - sin FRIA): **€5 millones**
- 🔴 Orden suspensión uso sistema: **3 meses** (pérdida negocio)
- 🔴 Compensaciones afectados: **€2 millones**
- 🔴 Auditoría obligatoria: **€500,000**
- 🔴 Daño reputacional: **-15% valoración bursátil**

**TOTAL IMPACTO: €7.5+ millones**

**FRIA + Compliance adecuado hubieran costado:** €200,000 (inicial) + €50,000/año

---

## ✅ CHECKLIST RÁPIDA: ¿TE AFECTA?

Responde SÍ/NO a estas preguntas:

### **BLOQUE 1: USO DE IA**
- [ ] ¿Usas IA para tomar o asistir decisiones que afectan a personas?
- [ ] ¿Tienes chatbots o asistentes virtuales que interactúan con usuarios?
- [ ] ¿Usas IA para analizar, filtrar o evaluar personas (empleados, candidatos, clientes)?
- [ ] ¿Automatizas decisiones con ML/IA?
- [ ] ¿Generas contenido con IA (texto, imágenes, audio, video)?

**Si respondiste SÍ a cualquiera → El AI Act TE AFECTA.**

---

### **BLOQUE 2: DESARROLLO IA**
- [ ] ¿Haces fine-tuning de modelos (aunque sean open source)?
- [ ] ¿Entrenas adapters (LoRA, QLoRA, etc.)?
- [ ] ¿Haces merge de modelos?
- [ ] ¿Creas sistemas RAG con tus documentos?
- [ ] ¿Despliegas modelos en producción?

**Si respondiste SÍ a cualquiera → Eres PROVEEDOR de sistema IA.**

---

### **BLOQUE 3: ALTO RIESGO** (Anexo III)
- [ ] ¿Tu IA se usa en RRHH (contratación, evaluaciones, despidos)?
- [ ] ¿Tu IA evalúa solvencia o scoring crediticio?
- [ ] ¿Tu IA se usa en salud (diagnóstico, triaje, asistencia médica)?
- [ ] ¿Tu IA se usa en educación (admisiones, evaluaciones, asignación nivel)?
- [ ] ¿Tu IA se usa en servicios públicos (prestaciones, servicios esenciales)?
- [ ] ¿Tu IA asiste autoridades judiciales o policiales?
- [ ] ¿Tu IA gestiona infraestructuras críticas (energía, agua, tráfico)?
- [ ] ¿Tu IA se usa en seguros de vida o salud?

**Si respondiste SÍ a cualquiera → Tu sistema es ALTO RIESGO → Art. 9-27 COMPLETO.**

---

## 📞 PRÓXIMOS PASOS

### **Si tu caso es ALTO RIESGO:**

1. ✅ **URGENTE:** Evaluación de cumplimiento actual
2. ✅ Implementar Sistema de Gestión de Riesgos (Art. 9)
3. ✅ Documentación técnica completa (Art. 11, Anexo IV)
4. ✅ Implementar QMS (Art. 17)
5. ✅ FRIA si aplica (Art. 27)
6. ✅ Preparar registro BBDD UE (Art. 49)

### **Si tu caso es RIESGO LIMITADO:**

1. ✅ Implementar transparencia (Art. 52.1, 52.3)
2. ✅ RGPD compliance (si datos personales)
3. ✅ Documentación básica

### **Si NO ESTÁS SEGURO:**

1. ✅ Consultar Anexo III completo
2. ✅ Evaluación con experto legal
3. ✅ Usar herramientas de clasificación automática

---

## 🎯 CONCLUSIÓN

**EL EU AI ACT APLICA A CASI TODOS LOS USOS EMPRESARIALES DE IA.**

No es solo para:
- ❌ OpenAI, Google, Microsoft
- ❌ Proveedores cloud
- ❌ Grandes corporaciones

**Es para:**
- ✅ Cualquier empresa que use IA para tomar decisiones
- ✅ Startups con chatbots
- ✅ Pymes que hacen fine-tuning
- ✅ Empresas que usan RAG
- ✅ Organizaciones que despliegan agentes
- ✅ Consultores que desarrollan soluciones IA

**La pregunta no es "¿Me afecta?"**  
**La pregunta es "¿Cuánto me afecta y qué debo hacer?"**

---

**Para evaluación personalizada de tu caso:**  
**compliance@codeflowx.com**

**Para demostración de herramientas de facilitación de cumplimiento:**  
**demo@codeflowx.com**

---

**© 2025 CodeflowX. Documento informativo - No constituye asesoramiento legal.**

