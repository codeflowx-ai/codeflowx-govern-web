"use client";

import { applyTheme } from "@/app/config/theme-config";
import { useEffect, useState } from "react";

export function useTheme() {
  const [currentTheme, setCurrentTheme] =
    useState<string>("trekker-federation");
  const [isThemeLoaded, setIsThemeLoaded] = useState(false);
  const [isClient, setIsClient] = useState(false);

  // Aplicar tema inmediatamente al montar
  useEffect(() => {
    setIsClient(true);

    try {
      const savedTheme =
        localStorage.getItem("codeflowx-theme") || "trekker-federation";

      // Aplicar tema inmediatamente
      applyTheme(savedTheme);
      setCurrentTheme(savedTheme);
      setIsThemeLoaded(true);
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
      console.log("Tema cambiado a:", themeId);
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
    isClient,
    changeTheme,
    useSystemTheme,
  };
}
