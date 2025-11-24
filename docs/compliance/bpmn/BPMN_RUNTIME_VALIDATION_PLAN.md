# BPMN Runtime Validation Plan

> Objetivo: demostrar que los BPMN reorganizados, sus delegates y reglas Drools se cargan y ejecutan correctamente en `codeflowx.govern.workflow.engine`, aun cuando los microservicios externos todavía no existan o estén en desarrollo.

## 1. Alcance
- Módulos: `codeflowx.govern.workflow.lib` (delegates, facts, reglas, BPMN) y `codeflowx.govern.workflow.engine` (micro Spring Boot).
- Procesos: todos los BPMN bajo `processes/aios`, `processes/compliance`, `processes/audit`, `processes/metrics`. Se excluye `processes/education`.
- Artefactos a validar:
  1. Registro de BPMN en Flowable/Camunda (`RepositoryService` / `RuntimeService`).
  2. Registro de delegates Spring (`ApplicationContext`) y beans auxiliares (`DroolsRulesService`, clients stub).
  3. KieBases/KieSessions de Drools (`kmodule.xml` y `.drl`).

## 2. Preparación del entorno
1. **Build del lib**  
   ```bash
   cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service
   mvn -pl codeflowx.govern.workflow.lib -am clean install
   ```
2. **Arranque del engine en modo local**  
   ```bash
   cd codeflowx.govern.workflow.engine
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
3. **Variables requeridas**  
   - `FLOWABLE_DATABASE_URL`, `FLOWABLE_DATABASE_USER`, `FLOWABLE_DATABASE_PASSWORD`.
   - Stubs locales (`LEKA_BIAS_URL=http://localhost:9081/mock-bias`, etc.) mientras no existan los micros reales.

## 3. Smoke tests del engine
| Nº | Prueba | Cómo se ejecuta | Esperado |
| --- | --- | --- | --- |
| S1 | Healthcheck Spring | `curl http://localhost:8080/actuator/health` | `UP` y sin excepciones en log. |
| S2 | Repositorio BPMN | Script `RuntimeSmoke.kt` (Spring Shell) que llame `repositoryService.createDeploymentQuery()` y liste los `processDefinitionKey`; guardar output. |
| S3 | Arranque Drools | Test `DroolsRulesServiceSmokeTest` que instancie cada método `execute*Rules` con datos dummy; se espera `RuleExecutionResult.ok()` y `RULEFLOW-GROUP` correcto. |
| S4 | Delegate wiring | Test `DelegatesContextSmokeTest` que pida el bean de cada clase `@Component` bajo `com.codeflowx.govern.workflow.delegates`. Si falta alguno, falla el test. |
| S5 | Variables críticas | Script `BpmnVariableProbe` que lea `processDefinition` y verifique `processId`, `documentation` y `serviceTask` → delegate existente. Guarda reporte JSON. |

## 4. Pruebas unitarias aisladas
1. **Delegates + Execution dummy**  
   - Utilizar `org.flowable.engine.delegate.DelegateExecution` mock (Mockito) para inyectar variables mínimas.
   - Casos sugeridos: `AiComponentOnboardingRulesDelegate`, `RuntimeKpiEvaluatorDelegate`, `RiskAssessmentRulesDelegate`, `ConsentRevocationDelegate`, `IsoCorrectiveActionDelegate`.
2. **Drools Facts**  
   - Añadir tests por cada `Fact` nuevo (`AioComponentOnboardingFactTest`, etc.) que verifiquen `@Builder` y serialización.
3. **Reglas**  
   - Cargar cada `.drl` en sesión aislada y comprobar que al menos una regla se dispara cuando se cumplen las condiciones básicas.

## 5. Estrategia de mocks / stubs
| Servicio externo | Acción | Implementación propuesta |
| --- | --- | --- |
| `leka-bias-detection-service` | Exponer endpoint fake `/bias/check` que devuelve JSON estático | WireMock en el propio engine (`@TestConfiguration`). |
| `aio-telemetry-service` | Mock para KPIs/alertas | `MockWebServer` (OkHttp) + datos generados. |
| `aio-policy-service` | POST/GET políticas | Interfaz `PolicyClient` + stub in-memory. |
| `leka-risk-evaluator` | Clasificación riesgo | Script Python simple que responde riesgo `LOW`. |
| ITSM/Ticketing | Creación de tickets | Stub que sólo registra request en log/DB temporal. |

Todos los delegates deben inyectar interfaces (`BiasDetectionClient`, `TelemetryClient`, etc.) con implementación stub en perfil `dev-mock`.

## 6. Captura de evidencias
1. Guardar logs del `workflow.engine` (startup completo y resultado de S1-S5) en `docs/compliance/evidence/runtime-<fecha>.log`.
2. Añadir tabla "Runtime Validation" en `docs/compliance/bpmn/BPMN_AUDIT_STATUS.md` con:
   - Fecha
   - Versión lib (git SHA)
   - Resultado (✅ / ⚠️ / ⛔)
   - Notas (ej. “falló cliente bias → stub pendiente”).
3. Adjuntar reportes de tests (`target/surefire-reports`) y JSON de `BpmnVariableProbe`.

## 7. Plan de ejecución
1. Implementar perfil `dev-mock` con beans stub de clientes externos.
2. Crear módulo de pruebas `workflow.engine` con suites `SmokeTests`, `DelegatesTests`, `DroolsTests`.
3. Automatizar S1-S5 en GitHub Actions / Jenkins (job manual por ahora).
4. Documentar resultados en `docs/compliance/bpmn/BPMN_AUDIT_STATUS.md` y actualizar "Gaps transversales".

## 8. Riesgos y mitigaciones
- **Tiempo de arranque elevado** → usar BD H2/ PostgreSQL dockerizada localmente.
- **Dependencias Python no listas** → stubs + evidencia clara del gap.
- **Cambios en BPMN sin actualizar pruebas** → script de comparación (`processId` vs catálogo) que dispare alerta en CI.

---
Este plan debe ejecutarse antes del cierre de la auditoría para demostrar que la implementación real coincide con la documentación y que los procesos pueden activarse sin errores críticos.

