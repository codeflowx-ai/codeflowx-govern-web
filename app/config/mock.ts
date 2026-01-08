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
 * URL base del backend
 * Por defecto apunta a localhost, pero puede configurarse mediante variable de entorno
 */
export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';

/**
 * URL base del BFF (Backend for Frontend)
 * El BFF es el punto de entrada para los microservicios de negocio
 */
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || 'http://localhost:8090';

/**
 * Configuración de endpoints del backend
 * Todos los endpoints apuntan al BFF que enruta a los microservicios correspondientes
 */
export const BACKEND_ENDPOINTS = {
  // Compliance endpoints
  compliance: {
    hitl: {
      dashboard: `${BFF_BASE_URL}/api/v1/hitl/dashboard`,
      interventions: `${BFF_BASE_URL}/api/v1/hitl/interventions`,
      config: `${BFF_BASE_URL}/api/v1/hitl/config`,
      decisions: `${BFF_BASE_URL}/api/v1/hitl/decisions`,
    },
  },
};
