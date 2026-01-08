"use client";

import { applyTheme } from "@/app/config/theme-config";
import { useEffect, useState } from "react";

export function useTheme() {
  const [currentTheme, setCurrentTheme] =
    useState<string>("trekker-federation");
  const [isThemeLoaded, setIsThemeLoaded] = useState(false);

  // Aplicar tema inmediatamente al montar
  useEffect(() => {
    try {
      const savedTheme =
        localStorage.getItem("codeflowx-theme") || "trekker-federation";

      // Aplicar tema inmediatamente
      applyTheme(savedTheme);
      setCurrentTheme(savedTheme);
      setIsThemeLoaded(true);

      // Forzar aplicación inmediata del fondo negro para trekker-federation
      if (savedTheme === 'trekker-federation') {
        const applyBlackBackground = () => {
          const header = document.querySelector('header');
          const aside = document.querySelector('aside');
          const footer = document.querySelector('footer');
          if (header) {
            header.style.setProperty('background-color', '#000000', 'important');
            header.style.setProperty('background', '#000000', 'important');
          }
          if (aside) {
            aside.style.setProperty('background-color', '#000000', 'important');
            aside.style.setProperty('background', '#000000', 'important');
          }
          if (footer) {
            footer.style.setProperty('background-color', '#000000', 'important');
            footer.style.setProperty('background', '#000000', 'important');
          }
        };

        // Aplicar después de un pequeño delay para asegurar que los elementos estén en el DOM
        setTimeout(applyBlackBackground, 100);
        setTimeout(applyBlackBackground, 500);
      }
    } catch (error) {
      console.error("Error aplicando tema:", error);
      // Fallback: aplicar tema por defecto
      applyTheme("trekker-federation");
      setCurrentTheme("trekker-federation");
      setIsThemeLoaded(true);
    }
  }, []);

  // Función para cambiar tema
  const changeTheme = (themeId: string) => {
    try {
      setCurrentTheme(themeId);
      applyTheme(themeId);
      localStorage.setItem("codeflowx-theme", themeId);

      // Forzar aplicación inmediata del fondo negro para trekker-federation
      if (themeId === 'trekker-federation') {
        setTimeout(() => {
          const header = document.querySelector('header');
          const aside = document.querySelector('aside');
          const footer = document.querySelector('footer');
          if (header) {
            header.style.backgroundColor = '#000000';
            header.style.background = '#000000';
          }
          if (aside) {
            aside.style.backgroundColor = '#000000';
            aside.style.background = '#000000';
          }
          if (footer) {
            footer.style.backgroundColor = '#000000';
            footer.style.background = '#000000';
          }
        }, 100);
      }
    } catch (error) {
      console.error("Error cambiando tema:", error);
    }
  };

  // Función para obtener tema del sistema
  const useSystemTheme = () => {
    const systemTheme = "trekker-federation"; // Siempre usar trekker-federation por ahora
    changeTheme(systemTheme);
  };

  return {
    currentTheme,
    isThemeLoaded,
    changeTheme,
    useSystemTheme,
  };
}
