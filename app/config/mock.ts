/**
 * Configuración del sistema de mocks
 *
 * Permite activar/desactivar mocks centralizadamente mediante variable de entorno.
 * Cuando se ponga operativa, solo hay que cambiar esta variable sin revisar cada pantalla.
 *
 * Por defecto, en desarrollo (NODE_ENV !== 'production'), usa mocks si no se especifica la variable.
 *
 * Nota: En API routes (servidor), las variables NEXT_PUBLIC_* no están disponibles.
 * Por eso verificamos tanto USE_MOCK (servidor) como NEXT_PUBLIC_USE_MOCK (cliente).
 */
const USE_MOCK_ENV = process.env.USE_MOCK;
const NEXT_PUBLIC_USE_MOCK_ENV = process.env.NEXT_PUBLIC_USE_MOCK;
const NODE_ENV = process.env.NODE_ENV || 'development';

// Si está explícitamente habilitado
const explicitlyEnabled = USE_MOCK_ENV === 'true' || NEXT_PUBLIC_USE_MOCK_ENV === 'true';

// Si está explícitamente deshabilitado
const explicitlyDisabled = USE_MOCK_ENV === 'false' || NEXT_PUBLIC_USE_MOCK_ENV === 'false';

// Si estamos en desarrollo y no está explícitamente deshabilitado
const isDevelopment = NODE_ENV !== 'production';
const useByDefaultInDev = isDevelopment && !explicitlyDisabled;

export const USE_MOCK = explicitlyEnabled || useByDefaultInDev;

// Log para debugging (solo en desarrollo)
if (isDevelopment && typeof console !== 'undefined') {
  console.log('[MOCK CONFIG]', {
    USE_MOCK_ENV,
    NEXT_PUBLIC_USE_MOCK_ENV,
    NODE_ENV,
    explicitlyEnabled,
    explicitlyDisabled,
    useByDefaultInDev,
    USE_MOCK,
  });
}

/**
 * URL base del gateway-web
 * El gateway-web es el punto de entrada único para todas las peticiones del frontend
 * Prefijo: /web/api/v1
 */
export const GATEWAY_WEB_URL = process.env.NEXT_PUBLIC_GATEWAY_WEB_URL || 'http://localhost:8080';
export const GATEWAY_WEB_BASE = `${GATEWAY_WEB_URL}/web/api/v1`;

/**
 * URL base del backend (DEPRECATED - usar GATEWAY_WEB_URL)
 * @deprecated Usar GATEWAY_WEB_URL en su lugar
 */
export const BACKEND_BASE_URL = GATEWAY_WEB_URL;

/**
 * URL base del BFF (DEPRECATED - usar GATEWAY_WEB_URL)
 * @deprecated Usar GATEWAY_WEB_URL con prefijo /web/api/v1 en su lugar
 */
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || 'http://localhost:8090';

/**
 * Configuración de endpoints del backend
 * Todos los endpoints deben usar el gateway-web: /web/api/v1/...
 */
export const BACKEND_ENDPOINTS = {
  // Compliance endpoints
  compliance: {
    hitl: {
      dashboard: `${GATEWAY_WEB_BASE}/compliance/hitl/dashboard`,
      interventions: `${GATEWAY_WEB_BASE}/compliance/hitl/interventions`,
      config: `${GATEWAY_WEB_BASE}/compliance/hitl/config`,
      decisions: `${GATEWAY_WEB_BASE}/compliance/hitl/decisions`,
    },
    assessments: {
      list: `${GATEWAY_WEB_BASE}/compliance/compliance-assessments`,
      byId: (id: number) => `${GATEWAY_WEB_BASE}/compliance/compliance-assessments/${id}`,
      byProject: (projectId: number) => `${GATEWAY_WEB_BASE}/compliance/compliance-assessments/project/${projectId}`,
      metrics: `${GATEWAY_WEB_BASE}/compliance/compliance-assessments/metrics`,
      scoreDistribution: `${GATEWAY_WEB_BASE}/compliance/compliance-assessments/metrics/score-distribution`,
    },
  },
};
