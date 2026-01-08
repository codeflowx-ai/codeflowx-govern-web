// Configuración de internacionalización simplificada para CodeflowX
import { useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones básicas
export const translations = {
  es: {
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      no: "No",
      ok: "OK",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      demo: "Demo",
      experience: "Experiencia",
      role: "Rol",
      agency: "Agencia",
      it: "IT",
      oem: "OEM",
      enterprise: "Empresa",
      startup: "Startup",
      developer: "Desarrollador",
      dataScientist: "Científico de Datos",
      businessUser: "Usuario de Negocio",
      technicalUser: "Usuario Técnico",
      access: "Acceso",
      projects: "Proyectos",
      generation: "Generación",
      knowledge: "Conocimiento",
      evaluation: "Evaluación",
      portals: "Portales",
      domains: "Dominios",
      stacks: "Stacks",
      models: "Modelos",
      serving: "Serving",
      mlflow: "MLflow",
      sdk: "SDK",
      summary: "Resumen",
      complete: "Completo",
      basic: "Básico",
      technical: "Técnico",
      business: "Negocio",
      description: "Descripción",
      icon: "Icono",
      color: "Color",
      bannerColor: "Color de Banner",
      href: "Enlace"
    }
  },
  en: {
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      ok: "OK",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      demo: "Demo",
      experience: "Experience",
      role: "Role",
      agency: "Agency",
      it: "IT",
      oem: "OEM",
      enterprise: "Enterprise",
      startup: "Startup",
      developer: "Developer",
      dataScientist: "Data Scientist",
      businessUser: "Business User",
      technicalUser: "Technical User",
      access: "Access",
      projects: "Projects",
      generation: "Generation",
      knowledge: "Knowledge",
      evaluation: "Evaluation",
      portals: "Portals",
      domains: "Domains",
      stacks: "Stacks",
      models: "Models",
      serving: "Serving",
      mlflow: "MLflow",
      sdk: "SDK",
      summary: "Summary",
      complete: "Complete",
      basic: "Basic",
      technical: "Technical",
      business: "Business",
      description: "Description",
      icon: "Icon",
      color: "Color",
      bannerColor: "Banner Color",
      href: "Link"
    }
  }
};

// Hook simplificado para usar traducciones
export function useTranslation() {
  const [language, setLanguage] = useState<Language>("es");

  const t = (key: string, params?: Record<string, any>): string => {
    const keys = key.split('.');
    let value: any = translations[language];

    for (const k of keys) {
      if (value && typeof value === 'object') {
        value = value[k];
      } else {
        return key; // Fallback a la clave si no se encuentra
      }
    }

    if (typeof value === 'string') {
      // Reemplazar parámetros si existen
      if (params) {
        return value.replace(/\{\{(\w+)\}\}/g, (match, param) => {
          return params[param] || match;
        });
      }
      return value;
    }

    return key; // Fallback a la clave si no es string
  };

  const changeLanguage = (newLanguage: Language) => {
    setLanguage(newLanguage);
    if (typeof window !== 'undefined') {
      localStorage.setItem('codeflowx-language', newLanguage);
    }
  };

  return {
    t,
    language,
    changeLanguage,
    availableLanguages: i18nConfig.availableLanguages
  };
}
