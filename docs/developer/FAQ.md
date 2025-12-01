# FAQ - Preguntas Frecuentes

## SDKs

**P: ¿Qué SDK debo usar?**
R: Python para ML/AI, TypeScript para web, Java para enterprise.

**P: ¿Puedo usar múltiples SDKs?**
R: Sí, cada SDK es independiente.

**P: ¿Cómo obtengo API key?**
R: Desde Developer Console → API Keys → Generar.

**P: ¿Las API keys expiran?**
R: No, pero puedes revocarlas desde Developer Console.

## Plugins

**P: ¿Puedo actualizar un plugin?**
R: Sí, sube nueva versión con versión mayor.

**P: ¿Los plugins afectan rendimiento?**
R: Hooks tienen timeout (default: 5s). Si exceden, se ignoran.

**P: ¿Puedo desactivar un plugin?**
R: Sí, desde Developer Console.

**P: ¿Puedo añadir procesos/pantallas/reglas?**
R: Sí, con plugin tipo `FULL_EXTENSION`.

## MCP

**P: ¿Qué son los hooks MCP?**
R: Puntos de interceptación (preInvoke, postInvoke, etc.).

**P: ¿Cuántos hooks puedo implementar?**
R: Todos los que necesites. Cada hook es independiente.

## Troubleshooting

**P: Error "API key is required"**
R: Verifica variable `CODEFLOWX_API_KEY` o pásala directamente.

**P: Error "Network error"**
R: Verifica conectividad y URL base: `https://gateway.codeflowx.ai`.

**P: Error "Rate limit exceeded"**
R: Límite: 100 req/s. Implementa retry con backoff.

**Más:** [Troubleshooting](TROUBLESHOOTING.md)
