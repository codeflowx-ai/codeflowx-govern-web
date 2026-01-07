// Archivo principal de internacionalización - Combina todos los módulos
import { useEffect, useState } from "react";
import { i18nConfig } from "./config";
import { commonTranslations } from "./modules/common";
import { dashboardTranslations } from "./modules/dashboard";
import { governanceTranslations } from "./modules/governance/index";
import { layoutTranslations } from "./modules/layout";
import { authTranslations } from "./modules/auth";
import { coreTranslations } from "./modules/core";
import { servingTranslations } from "./modules/serving";
import { ragTranslations } from "./modules/rag/index";
import { agentsTranslations } from "./modules/agents/index";
import { promptsTranslations } from "./modules/prompts";
import { odsImpactTranslations } from "./modules/compliance/ods-impact";
import { bpmnFormsTranslations } from "./modules/bpmn-forms";
import { Language } from "./types";
// Importar otros módulos aquí cuando se creen
// import { complianceTranslations } from "./modules/compliance";
// etc.

// Combinar todas las traducciones
const combineTranslations = (): Record<Language, any> => {
  const languages: Language[] = ["es", "en", "fr", "de", "it", "pt"];

  const combined: Record<string, any> = {};

  languages.forEach((lang) => {
    combined[lang] = {
      common: commonTranslations[lang],
      dashboard: dashboardTranslations[lang],
      governance: governanceTranslations[lang],
      layout: layoutTranslations[lang],
      auth: authTranslations[lang],
      core: coreTranslations[lang],
      serving: servingTranslations[lang],
      rag: ragTranslations[lang],
      agents: agentsTranslations[lang],
      prompts: promptsTranslations[lang],
      compliance: {
        odsImpact: odsImpactTranslations[lang].compliance?.odsImpact || {},
      },
      bpmnForms: bpmnFormsTranslations[lang],
      // Añadir otros módulos aquí:
      // compliance: complianceTranslations[lang],
      // etc.
    };
  });

  return combined as Record<Language, any>;
};

export const translations = combineTranslations();

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    try {
      // Intentar obtener el idioma guardado
      const savedLanguage = localStorage.getItem("codeflowx-language") as Language;

      let validLanguage: Language = "es";

      if (savedLanguage && i18nConfig.availableLanguages.includes(savedLanguage)) {
        // Si hay un idioma guardado y es válido, usarlo
        validLanguage = savedLanguage;
      } else {
        // Si no hay idioma guardado, detectar el idioma del navegador
        const browserLanguage = navigator.language || (navigator as any).userLanguage;
        const browserLangCode = browserLanguage.split("-")[0] as Language;

        // Si el idioma del navegador está disponible, usarlo
        if (i18nConfig.availableLanguages.includes(browserLangCode)) {
          validLanguage = browserLangCode;
          // Guardar el idioma detectado
          localStorage.setItem("codeflowx-language", browserLangCode);
        }
      }

      setLanguageState(validLanguage);
    } catch (error) {
      console.error("Error leyendo idioma de localStorage:", error);
      setLanguageState("es");
    }

    // Escuchar cambios de idioma desde otras pestañas o componentes
    const handleLanguageChange = () => {
      try {
        const savedLanguage =
          (localStorage.getItem("codeflowx-language") as Language) || "es";
        const validLanguage = i18nConfig.availableLanguages.includes(savedLanguage)
          ? savedLanguage
          : "es";
        setLanguageState((currentLang) => {
          if (validLanguage !== currentLang) {
            return validLanguage;
          }
          return currentLang;
        });
      } catch (error) {
        console.error("Error leyendo idioma de localStorage:", error);
      }
    };

    window.addEventListener("languagechange", handleLanguageChange);
    window.addEventListener("storage", handleLanguageChange);

    return () => {
      window.removeEventListener("languagechange", handleLanguageChange);
      window.removeEventListener("storage", handleLanguageChange);
    };
  }, []); // Remover language de las dependencias para evitar bucle infinito

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      const saved = localStorage.getItem("codeflowx-language") as Language;
      // Validar que el idioma guardado esté en la lista de idiomas disponibles
      if (saved && i18nConfig.availableLanguages.includes(saved)) {
        return saved;
      }
      return "es";
    }
    return "es";
  };

  const setLanguage = (lang: Language) => {
    if (typeof window !== "undefined") {
      // Validar que el idioma esté disponible
      if (!i18nConfig.availableLanguages.includes(lang)) {
        console.warn(`Idioma ${lang} no está disponible`);
        return;
      }
      // Guardar en localStorage primero
      try {
      localStorage.setItem("codeflowx-language", lang);
      // Actualizar estado inmediatamente
      setLanguageState(lang);
        // Forzar actualización del DOM inmediatamente
        window.dispatchEvent(new Event("languagechange"));
      } catch (error) {
        console.error("Error guardando idioma en localStorage:", error);
      }
      // Recargar la página para aplicar el cambio en todos los componentes
      // Usar un pequeño delay para asegurar que el localStorage se guarde
      setTimeout(() => {
      window.location.reload();
      }, 50);
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    // Usar el estado del idioma actual, no getLanguage()
    const currentLanguage = language;
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match: string, param: string) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: i18nConfig.availableLanguages,
  };
}

// Re-exportar tipos y configuración
export { i18nConfig } from "./config";
export type { I18nConfig, Language } from "./types";
