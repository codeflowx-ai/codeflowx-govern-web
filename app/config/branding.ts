/**
 * Configuración de Branding del Cliente
 *
 * Permite personalizar el logo y nombre que se muestra en el header.
 * Se puede configurar mediante variables de entorno o valores por defecto.
 */

export interface BrandingConfig {
  /** Ruta al logo del cliente (relativa a /public) */
  logoPath: string;
  /** Texto alternativo para el logo */
  logoAlt: string;
  /** Nombre de la empresa/cliente a mostrar */
  companyName: string;
  /** Si es true, muestra el nombre con gradientes de colores (estilo CodeflowX) */
  useGradientName: boolean;
  /** Si es true, muestra solo el logo sin texto */
  logoOnly: boolean;
}

/**
 * Configuración de branding del cliente
 *
 * Variables de entorno disponibles:
 * - NEXT_PUBLIC_BRANDING_LOGO_PATH: Ruta al logo (ej: /img/knowmad-mood-logo.svg)
 * - NEXT_PUBLIC_BRANDING_LOGO_ALT: Texto alternativo del logo
 * - NEXT_PUBLIC_BRANDING_COMPANY_NAME: Nombre de la empresa
 * - NEXT_PUBLIC_BRANDING_USE_GRADIENT: "true" para usar gradientes en el nombre
 * - NEXT_PUBLIC_BRANDING_LOGO_ONLY: "true" para mostrar solo el logo sin texto
 */
export const brandingConfig: BrandingConfig = {
  // Por defecto, usar el logo de Knowmad Mood
  logoPath: process.env.NEXT_PUBLIC_BRANDING_LOGO_PATH || "/img/knowmad-mood-logo2.png",
  logoAlt: process.env.NEXT_PUBLIC_BRANDING_LOGO_ALT || "Knowmad Mood",
  companyName: process.env.NEXT_PUBLIC_BRANDING_COMPANY_NAME || "Knowmad Mood",
  useGradientName: process.env.NEXT_PUBLIC_BRANDING_USE_GRADIENT === "true",
  logoOnly: process.env.NEXT_PUBLIC_BRANDING_LOGO_ONLY === "true",
};

/**
 * Configuración por defecto de CodeflowX (para referencia)
 */
export const defaultCodeflowXBranding: BrandingConfig = {
  logoPath: "/img/icono1.png",
  logoAlt: "CodeflowX Icon",
  companyName: "CodeflowX",
  useGradientName: true,
  logoOnly: false,
};
