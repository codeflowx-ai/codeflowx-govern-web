"use client";

import { themes, type ThemeConfig } from "@/app/config/theme-config";
import { useTheme } from "@/components/hooks/useTheme";
import { useTranslation } from "@/app/config/i18n";
import { Check, Monitor, Palette } from "lucide-react";
import { useState } from "react";
import { Button } from "./button";

export function ThemeSwitcher() {
  const [showThemePicker, setShowThemePicker] = useState(false);
  const { currentTheme, changeTheme, useSystemTheme } = useTheme();
  const { t, language } = useTranslation();

  const getCurrentThemeConfig = (): ThemeConfig | undefined => {
    return themes.find((t) => t.id === currentTheme);
  };

  // Función para cambiar tema y cerrar menú
  const handleThemeChange = (themeId: string) => {
    changeTheme(themeId);
    setShowThemePicker(false); // Cerrar el menú al seleccionar tema
  };

  return (
    <div className="relative flex items-center space-x-2">
      {/* Botón de temas */}
      <Button
        variant="outline"
        size="sm"
        onClick={() => setShowThemePicker(!showThemePicker)}
        className="p-2"
        title={t("layout.theme.changeTheme")}
      >
        <Palette className="h-4 w-4" />
      </Button>

      {/* Botón de tema del sistema */}
      <Button
        variant="outline"
        size="sm"
        onClick={useSystemTheme}
        className="p-2"
        title={t("layout.theme.useSystemTheme")}
      >
        <Monitor className="h-4 w-4" />
      </Button>

      {/* Selector de temas */}
      {showThemePicker && (
        <div className="absolute top-full right-0 mt-2 p-4 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg shadow-lg z-50 min-w-[320px] max-h-[500px] overflow-y-auto">
          <div className="text-sm font-medium mb-4 text-gray-700 dark:text-gray-300">
            {t("layout.theme.themesTitle")}
          </div>
          <div className="grid grid-cols-1 gap-3">
            {themes.map((theme) => (
              <button
                key={theme.id}
                onClick={() => handleThemeChange(theme.id)}
                className={`flex items-center justify-between p-3 rounded-lg border transition-all hover:shadow-md ${
                  currentTheme === theme.id
                    ? "border-blue-500 bg-blue-50 dark:bg-blue-900/20"
                    : "border-gray-200 dark:border-gray-600 hover:border-gray-300 dark:hover:border-gray-500"
                }`}
              >
                <div className="flex items-center space-x-3">
                  {/* Preview del tema */}
                  <div className="flex space-x-1">
                    <div
                      className="w-4 h-4 rounded-full border border-gray-300"
                      style={{ backgroundColor: theme.colors.primary }}
                    />
                    <div
                      className="w-4 h-4 rounded-full border border-gray-300"
                      style={{ backgroundColor: theme.colors.secondary }}
                    />
                    <div
                      className="w-4 h-4 rounded-full border border-gray-300"
                      style={{ backgroundColor: theme.colors.accent }}
                    />
                  </div>
                  <div className="text-left">
                    <div className="font-medium text-gray-900 dark:text-gray-100">
                      {t(`layout.theme.themes.${theme.id}.name`, theme.name)}
                    </div>
                    <div className="text-sm text-gray-500 dark:text-gray-400">
                      {t(`layout.theme.themes.${theme.id}.description`, theme.description)}
                    </div>
                  </div>
                </div>
                {currentTheme === theme.id && (
                  <Check className="h-5 w-5 text-blue-500" />
                )}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
