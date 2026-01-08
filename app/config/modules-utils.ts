// Utilidades para trabajar con módulos y categorías

import { ModuleConfig, modulesConfig } from "./modules";

/**
 * Obtiene módulos agrupados por categoría
 */
export const getModulesByCategory = (): Record<string, ModuleConfig[]> => {
  return modulesConfig.reduce((acc, module) => {
    if (!acc[module.category]) {
      acc[module.category] = [];
    }
    acc[module.category].push(module);
    return acc;
  }, {} as Record<string, ModuleConfig[]>);
};

/**
 * Obtiene módulos de una categoría específica ordenados
 */
export const getModulesByCategoryName = (
  category: "governance" | "development" | "admin"
): ModuleConfig[] => {
  return modulesConfig
    .filter((module) => module.category === category)
    .sort((a, b) => a.order - b.order);
};

/**
 * Obtiene módulos de gobierno y cumplimiento
 */
export const getGovernanceModules = (): ModuleConfig[] => {
  return getModulesByCategoryName("governance");
};

/**
 * Obtiene módulos de desarrollo
 */
export const getDevelopmentModules = (): ModuleConfig[] => {
  return getModulesByCategoryName("development");
};

/**
 * Obtiene módulos administrativos
 */
export const getAdminModules = (): ModuleConfig[] => {
  return getModulesByCategoryName("admin");
};

/**
 * Obtiene el nombre de la categoría traducido
 */
export const getCategoryLabel = (
  category: "governance" | "development" | "admin",
  language: string = "es"
): string => {
  const labels: Record<
    string,
    Record<"governance" | "development" | "admin", string>
  > = {
    es: {
      governance: "Gobierno y Cumplimiento",
      development: "Desarrollo",
      admin: "Administración",
    },
    en: {
      governance: "Governance & Compliance",
      development: "Development",
      admin: "Administration",
    },
    fr: {
      governance: "Gouvernance et Conformité",
      development: "Développement",
      admin: "Administration",
    },
    de: {
      governance: "Governance & Compliance",
      development: "Entwicklung",
      admin: "Verwaltung",
    },
    it: {
      governance: "Governance e Conformità",
      development: "Sviluppo",
      admin: "Amministrazione",
    },
    pt: {
      governance: "Governança e Conformidade",
      development: "Desenvolvimento",
      admin: "Administração",
    },
  };

  return labels[language]?.[category] || category;
};

/**
 * Obtiene el orden de las categorías para mostrar en el menú
 */
export const getCategoryOrder = (): ("governance" | "development" | "admin")[] => {
  return ["governance", "development", "admin"];
};
