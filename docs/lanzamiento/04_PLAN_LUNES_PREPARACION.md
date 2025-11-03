# ⏰ PLAN LUNES - PREPARACIÓN LANZAMIENTO
## Checklist Completa 24h Antes del Martes

**Fecha:** Lunes, 18 de noviembre de 2025  
**Objetivo:** TODO listo para lanzamiento martes 09:00  
**Equipo:** Manuel + Equipo técnico + Marketing

---

## 🎯 RESUMEN EJECUTIVO LUNES

### **Prioridades del Día:**
```
1. 🔴 CRÍTICO: Web actualizada y testeada
2. 🔴 CRÍTICO: 4 guías PDF listas para descarga
3. 🔴 CRÍTICO: Posts LinkedIn preparados + imágenes
4. 🟡 IMPORTANTE: Formularios funcionando
5. 🟡 IMPORTANTE: CRM configurado
6. 🟢 NICE: Scripts demo preparados
```

---

## ⏰ TIMELINE LUNES (Hora por Hora)

### **06:00 - 08:00: INFRAESTRUCTURA**

**Responsable:** Equipo técnico

#### **Tareas:**
```
☐ Backup completo web actual
☐ Verificar todos microservicios Python (12) funcionando
☐ Verificar APIs Java (60+ endpoints) respondiendo
☐ Test smoke todos los servicios críticos
☐ Monitoring activo (Prometheus, logs)
☐ Alertas configuradas (Slack/email)
```

**Comandos Verificación:**
```bash
# Health checks microservicios
for port in 8001 8002 8003 8004 8005 8006 8007 8008 8009 8010 8012 8013; do
    curl -s http://localhost:$port/health || echo "FALLA $port"
done

# Java backend
curl http://localhost:8080/actuator/health

# Base datos
psql -U postgres -d codeflowx_govern -c "SELECT COUNT(*) FROM ANNANNEXIIICATEGORIES;" 
# Debe retornar 33
```

---

### **08:00 - 10:00: WEB - CAMBIOS CRÍTICOS**

**Responsable:** Frontend Dev

#### **Homepage:**
```
☐ Cambiar headline principal (ver doc 01_CAMBIOS_WEB)
☐ Añadir badges EU AI Act
☐ Actualizar hero section
☐ Añadir bloque diferenciador vs competencia
☐ Actualizar meta tags SEO
☐ Test responsive mobile
```

#### **Nueva Página /compliance-eu-ai-act:**
```
☐ Crear página completa
☐ Hero con mensaje clave
☐ Tabla comparativa CodeflowX vs Marco vs Otros
☐ 8 casos uso sectores (mínimo 5)
☐ FAQ específica compliance
☐ CTAs: Demo, Guías, Partners
☐ Test links internos
```

#### **Nueva Página /partners:**
```
☐ Crear página programa partners
☐ Propuesta valor clara
☐ Formulario aplicación integrado
☐ Comisiones explicadas
☐ CTA destacado
```

#### **Recursos /recursos-compliance:**
```
☐ Página recursos creada
☐ 4 guías subidas (ver siguiente sección)
☐ Formulario descarga funcionando
☐ Integración CRM para leads
☐ Email confirmación automático
```

**Test Completo Web:**
```
☐ Homepage carga < 2 seg
☐ Todas las páginas nuevas accesibles
☐ Formularios envían OK
☐ Descargas PDFs funcionan
☐ Links externos abren nueva pestaña
☐ Mobile responsive verificado
☐ Cross-browser (Chrome, Firefox, Safari)
```

---

### **10:00 - 12:00: CONTENIDO - GUÍAS PDF**

**Responsable:** Manuel

#### **4 Guías a Preparar:**

**1. Guía Sectorial EU AI Act (PDF)**
```
☐ Convertir CASOS_USO_EU_AI_ACT_GUIA_SECTORIAL.md a PDF
☐ Diseño profesional (portada, índice, paginación)
☐ Logo CodeflowX
☐ Formato A4, max 50 páginas
☐ CTA final: "Solicita demo personalizada"
☐ Subir a /public/downloads/guia-sectorial-eu-ai-act.pdf
☐ Test descarga desde web
```

**2. Gobierno IA Ciclo Completo (PDF)**
```
☐ Convertir GOBIERNO_IA_CICLO_VIDA_COMPLETO_GUIA.md a PDF
☐ Mismo diseño que guía 1
☐ Incluir infografías 7 dimensiones gobierno
☐ Subir a /public/downloads/gobierno-ia-ciclo-completo.pdf
☐ Test descarga
```

**3. Matriz Compliance Post-Implementación EN (PDF)**
```
☐ Convertir COMPLIANCE_VERIFICATION_MATRIX_EXTERNAL_POST_IMPLEMENTATION.md a PDF
☐ Destacar disclaimers legales (primera página)
☐ Tabla compliance con color coding
☐ Subir a /public/downloads/compliance-matrix-en.pdf
☐ Test descarga
```

**4. Matriz Compliance Post-Implementación ES (PDF)**
```
☐ Convertir MATRIZ_VERIFICACION_CUMPLIMIENTO_EXTERNA_POST_IMPLEMENTACION.md a PDF
☐ Misma estructura que versión EN
☐ Disclaimers legales destacados
☐ Subir a /public/downloads/compliance-matrix-es.pdf
☐ Test descarga
```

**Diseño PDFs:**
```
Portada:
- Logo CodeflowX
- Título guía
- Subtítulo "EU AI Act Compliance"
- Fecha noviembre 2025

Pie página:
- Logo pequeño
- "codeflowx.com"
- Número página

Última página:
- CTA: "¿Listo para 100% compliance?"
- "Solicita demo: [link]"
- "Partners: [link]"
```

---

### **12:00 - 14:00: LINKEDIN - POSTS E IMÁGENES**

**Responsable:** Manuel + Diseñador

#### **Posts:**
```
☐ 5 posts escritos y revisados (ver doc 03_LINKEDIN)
☐ Posts guardados en drafts LinkedIn
☐ Programar posts en Buffer/Hootsuite (backup)
☐ Links testeados en cada post
☐ Hashtags verificados (no banned)
```

#### **Imágenes/Infografías (5):**

**Imagen 1: Countdown (Post Teaser 06:00)**
```
☐ Diseño minimalista
☐ Texto: "Lanzamiento Hoy 09:00"
☐ Logo CodeflowX
☐ Colores marca
☐ Dimensiones: 1200x627px (LinkedIn optimal)
```

**Imagen 2: Comparativa 65 vs 15 (Post Principal 09:00)**
```
☐ Gráfico barras horizontal
☐ CodeflowX: 65 capacidades (verde)
☐ Marco: 15 capacidades (gris)
☐ Otros: 10 capacidades (gris claro)
☐ Título: "100% vs Parcial Coverage EU AI Act"
☐ Footer: codeflowx.com
☐ Dimensiones: 1200x627px
```

**Imagen 3: Caso Uso Agencia (Post 12:00)**
```
☐ Diseño antes/después
☐ Lado izquierdo: Problema (❌ rojo)
☐ Lado derecho: Solución (✅ verde)
☐ Iconos visuales (agencia, cliente, compliance)
☐ Dimensiones: 1200x627px
```

**Imagen 4: Diagrama Hash Chain (Post Técnico 15:00)**
```
☐ Visual técnico pero limpio
☐ 3 logs conectados con flechas
☐ Hashes visibles (simplificados)
☐ Highlight "Inmutable"
☐ Código snippet (pseudo-código legible)
☐ Dimensiones: 1200x627px
```

**Imagen 5: Esquema Partner (Post 18:00)**
```
☐ Flujo: Consultor → Cliente → 💰
☐ Números ejemplo: "20% comisión"
☐ CTA visual: "Solicita ser partner"
☐ Dimensiones: 1200x627px
```

**Verificación Imágenes:**
```
☐ Todas en 1200x627px (LinkedIn óptimo)
☐ Formato PNG o JPG < 5MB
☐ Texto legible mobile (tamaño fuente suficiente)
☐ Logo CodeflowX presente
☐ Brand colors consistentes
☐ Sin errores ortográficos
☐ Guardadas en carpeta /assets/linkedin-lanzamiento/
```

---

### **14:00 - 15:00: ALMUERZO + REVIEW MITAD DÍA**

**Check mitad día:**
```
☐ Web cambios críticos → DONE o EN PROGRESO
☐ 4 PDFs → DONE o EN PROGRESO
☐ 5 imágenes LinkedIn → DONE o EN PROGRESO
☐ Issues críticos identificados → EN RESOLUCIÓN
```

**Si algo bloqueado:** Reunión rápida equipo resolver.

---

### **15:00 - 17:00: FORMULARIOS Y CRM**

**Responsable:** Backend Dev + Manuel

#### **Formulario Descarga Guías:**
```
☐ Campos: Nombre, Email, Empresa, Rol, Checkboxes uso IA
☐ Validación frontend (email válido, campos obligatorios)
☐ Validación backend
☐ Envío email confirmación automático
☐ Email contiene link descarga PDF
☐ Integración CRM (añadir lead automático)
☐ Tag lead: "Guide Download - [nombre guía]"
☐ Test formulario end-to-end
```

**Formulario Solicitar Demo:**
```
☐ Campos: Nombre, Email, Empresa, Teléfono, Mensaje
☐ Validación frontend/backend
☐ Envío email confirmación usuario
☐ Notificación email equipo ventas
☐ Integración CRM → Lead "Demo Request"
☐ Auto-asignar a Manuel (o ventas)
☐ Trigger: Email seguimiento 24h si no contacto
☐ Test formulario
```

**Formulario Partners:**
```
☐ Campos: Nombre, Email, Empresa, Tipo (consultor/agencia/otro), 
    LinkedIn, ¿Por qué quieres ser partner?, Clientes actuales (#)
☐ Validación
☐ Email confirmación
☐ Email Manuel para revisar aplicación
☐ Integración CRM → Lead "Partner Application"
☐ Tag: "Potencial Partner - Evaluar"
☐ Test formulario
```

#### **CRM Setup:**

**HubSpot/PipeDrive/Otro:**
```
☐ Campos personalizados creados:
    - eu_ai_act_interest (Sí/No)
    - guide_downloaded (Sí/No + ¿cuál?)
    - demo_requested (Fecha)
    - partner_application (Sí/No)
    - sector (Dropdown: Banca, Legal, Salud, etc.)
☐ Pipelines configurados:
    - Pipeline "Leads": Nuevo → Contactado → Calificado → Demo → Propuesta → Cerrado
    - Pipeline "Partners": Aplicación → Evaluación → Aprobado → Onboarding → Activo
☐ Automatizaciones:
    - Lead nuevo → Email bienvenida + tarea Manuel contactar 24h
    - Demo request → Tarea Manuel/Ventas agendar 1h
    - Partner app → Tarea Manuel revisar 48h
☐ Webhooks integración web → CRM testeados
```

---

### **17:00 - 18:00: DEMOS Y SCRIPTS**

**Responsable:** Manuel

#### **Script Demo Estándar (15 min):**
```
☐ Slide deck actualizado con mensajes nuevos
☐ Screenshots plataforma actualizados
☐ Demo environment testeado (no bugs visuales)
☐ Ejemplos datos reales (anonimizados)
☐ Script conversación:
    Min 0-2:  Intro + Pain point cliente
    Min 2-7:  Demo plataforma (3 features clave)
    Min 7-12: Compliance coverage (mostrar 65 capacidades)
    Min 12-14: Pricing + ROI
    Min 14-15: Next steps + CTA
☐ FAQs preparadas (vs Marco, pricing, implementación)
```

#### **Demo Sectores (Preparar 3):**
```
☐ Demo Agencias Marketing (casos uso contenido IA)
☐ Demo Banca/Finanzas (scoring, fraude)
☐ Demo Legal/Compliance (contratos, due diligence)
```

**Calendario Demo:**
```
☐ Google Calendar / Calendly configurado
☐ Slots disponibles mar-vie 10:00-18:00
☐ Duración: 15 min (buffer 5 min entre demos)
☐ Link Zoom/Meet generado automático
☐ Email recordatorio 24h antes + 1h antes
☐ Test booking desde web
```

---

### **18:00 - 19:00: MARKETING ASSETS**

**Responsable:** Marketing

#### **Email Templates:**

**Email Bienvenida (Descarga Guía):**
```
Subject: Tu guía EU AI Act + Próximos pasos

Hola [Nombre],

Gracias por descargar "[Nombre Guía]".

📄 Aquí está tu guía: [Link descarga]

Además, te puede interesar:
→ Las otras 3 guías: [Links]
→ Demo personalizada 15 min: [Link calendario]
→ Programa Partners (si eres consultor): [Link]

¿Preguntas? Responde este email directamente.

Saludos,
Manuel González
Founder, CodeflowX

P.D. Si tu empresa usa IA en producción, agenda demo.
El EU AI Act entra en vigor en 3 meses.

[Footer: Logo, links sociales, unsubscribe]
```

**Email Confirmación Demo:**
```
Subject: Demo confirmada - [Fecha] [Hora]

Hola [Nombre],

Tu demo CodeflowX está confirmada:
📅 [Fecha completa]
🕐 [Hora] (15 minutos)
🔗 [Link Zoom/Meet]

Añade a tu calendario: [Link .ics]

Antes de la demo, ¿puedes responder 2 preguntas rápidas?
1. ¿Qué casos de uso IA estás explorando?
2. ¿Conoces el EU AI Act?

Esto me ayuda a personalizar la demo.

Nos vemos [día],
Manuel

[Footer]
```

**Email Follow-up Partner:**
```
Subject: Tu aplicación Partner - Próximos pasos

Hola [Nombre],

Gracias por aplicar al Programa Partners CodeflowX.

Estamos revisando tu aplicación.
Respuesta en máximo 48h.

Mientras tanto:
→ Lee sobre el programa: [Link]
→ Mira nuestro demo: [Link video]
→ Preguntas: Responde este email

Saludos,
Manuel

[Footer]
```

**Preparar Emails:**
```
☐ 3 templates escritos
☐ Guardados en HubSpot/Sistema email
☐ Variables dinámicas configuradas ([Nombre], [Link], etc.)
☐ Envío test a email personal Manuel
☐ Verificar links todos funcionan
☐ Verificar diseño mobile
```

---

### **19:00 - 20:00: TESTING FINAL Y CHECKLIST**

**Responsable:** Todo el equipo

#### **Test End-to-End Completo:**

**Flujo 1: Usuario Descarga Guía**
```
1. ☐ Entrar homepage
2. ☐ Click "Descargar guía"
3. ☐ Rellenar formulario
4. ☐ Submit
5. ☐ Recibir email confirmación < 1 min
6. ☐ Click link descarga PDF
7. ☐ PDF descarga OK
8. ☐ Lead aparece en CRM con tags correctos
```

**Flujo 2: Usuario Solicita Demo**
```
1. ☐ Entrar /compliance-eu-ai-act
2. ☐ Click "Solicitar Demo"
3. ☐ Formulario → Submit
4. ☐ Recibir email confirmación
5. ☐ Recibir email Manuel notificación
6. ☐ Lead en CRM → Pipeline "Leads" → Tarea creada
7. ☐ Calendario demo funciona (booking slot)
```

**Flujo 3: Consultor Aplica Partner**
```
1. ☐ Entrar /partners
2. ☐ Formulario aplicación → Submit
3. ☐ Email confirmación recibido
4. ☐ Email Manuel para revisar
5. ☐ Lead CRM → Pipeline "Partners"
```

**Cross-Browser Testing:**
```
☐ Chrome (Win/Mac)
☐ Firefox
☐ Safari (Mac/iOS)
☐ Edge
☐ Mobile Chrome (Android)
☐ Mobile Safari (iOS)
```

**Performance Testing:**
```
☐ Homepage < 2 seg carga (Google PageSpeed)
☐ Lighthouse score > 90
☐ Todas las páginas < 3 seg
☐ Imágenes optimizadas (comprimidas)
☐ CDN activo (si aplica)
```

---

### **20:00 - 21:00: PREPARACIÓN PERSONAL MARTES**

**Responsable:** Manuel

#### **Checklist Personal:**
```
☐ Ropa planificada (casual profesional para posibles videos)
☐ Setup oficina limpio (background calls/videos)
☐ Portátil cargado
☐ Móvil cargado
☐ Notificaciones LinkedIn activas
☐ Alarma 05:30 martes
☐ Café/té preparado noche antes 😄
```

#### **Materiales a Mano Martes:**
```
☐ Doc 02_ESTRATEGIA_VS_MARCO.md (respuestas preparadas)
☐ Doc 03_LINKEDIN posts (copiar/pegar rápido)
☐ Templates respuestas comentarios LinkedIn
☐ Acceso CRM (laptop + móvil)
☐ Calendly link demos a mano
☐ Links todas las páginas nuevas
```

#### **Backups:**
```
☐ Posts LinkedIn guardados en Google Docs (si LinkedIn cae)
☐ Imágenes en Dropbox (backup)
☐ Passwords importantes en gestor
☐ Plan B si web cae (página estática backup)
```

---

## 🚨 CHECKLIST FINAL LUNES 21:00

### **CRÍTICO (Bloquea Lanzamiento):**
```
☐ Web homepage actualizada
☐ Página /compliance-eu-ai-act creada
☐ 4 PDFs subidos y descargables
☐ Formularios funcionando
☐ Posts LinkedIn escritos
☐ 5 imágenes LinkedIn creadas
☐ Microservicios todos funcionando (health checks OK)
```

### **IMPORTANTE (No bloquea pero prioritario):**
```
☐ Página /partners creada
☐ CRM integrado
☐ Email templates activos
☐ Demo script actualizado
☐ Calendly configurado
```

### **NICE-TO-HAVE (Puede hacerse martes mañana):**
```
☐ Posts programados en Buffer
☐ Analytics configurado
☐ Heatmaps activos
☐ A/B testing setup
```

---

## 🔥 SI ALGO FALLA LUNES

### **Plan B Scenarios:**

**Si Web No Lista:**
```
→ Plan B: Lanzar solo página /compliance-eu-ai-act (mínimo viable)
→ Homepage cambios pueden esperar 24h
→ Lo crítico: Nueva página compliance + formularios + PDFs
```

**Si PDFs No Listos:**
```
→ Plan B: Lanzar con 2 PDFs (Guía Sectorial + Gobierno IA)
→ Otros 2 añadir miércoles
```

**Si Imágenes LinkedIn No Listas:**
```
→ Plan B: Posts sin imagen (menos impacto pero funciona)
→ Añadir imágenes en edits posteriores
```

**Si Formularios No Funcionan:**
```
→ Plan B: Google Forms temporal
→ Integración CRM manual martes
→ Fix formularios web miércoles
```

---

## 📞 CONTACTOS EMERGENCIA LUNES

**Frontend Dev:** [Teléfono]  
**Backend Dev:** [Teléfono]  
**Diseñador:** [Teléfono]  
**Hosting Support:** [Teléfono/Chat]

**Si problema crítico 21:00-23:00:** Resolver esa noche.  
**Si problema crítico después 23:00:** Reunión 06:00 martes resolver.

---

## ✅ SIGN-OFF LUNES 21:00

**Antes de irte a dormir:**

```
☐ Ejecutar checklist final (arriba)
☐ Tomar screenshots web actualizada (evidencia)
☐ Test último formulario (asegurar funcionando)
☐ Verificar posts LinkedIn guardados
☐ Email equipo "Todo listo para mañana"
☐ Dormir bien (crítico para rendimiento martes)
```

---

## 🎯 MARTES 05:30 - ARRANQUE

**Al levantarte:**
```
1. ☐ Verificar web sigue funcionando (health check rápido)
2. ☐ Verificar microservicios activos
3. ☐ Revisar CRM funcionando
4. ☐ Abrir LinkedIn drafts
5. ☐ Café ☕
6. ☐ Post Teaser 06:00 → GO!
```

---

## 🏆 MINDSET LUNES

**Recuerda:**
> "Mañana lanzamos algo que el mercado necesita.
> Hemos trabajado duro. Estamos listos.
> CodeflowX va a cambiar el compliance de IA en Europa.
> 
> Hoy preparamos. Mañana ejecutamos. 🚀"

---

**Fin Documento Plan Lunes**

