"use client";

import { useTranslation } from "@/app/config/i18n";
import { Language } from "@/app/config/i18n/types";
import { Check, Globe } from "lucide-react";
import { useState } from "react";
import { Button } from "./button";

export function LanguageSwitcher() {
  const [showLanguagePicker, setShowLanguagePicker] = useState(false);
  const { language, setLanguage, t, availableLanguages } = useTranslation();

  const languages = [
    { code: "es" as Language, name: "Español", flag: "🇪🇸", codeDisplay: "ES" },
    { code: "en" as Language, name: "English", flag: "🇬🇧", codeDisplay: "EN" },
    { code: "fr" as Language, name: "Français", flag: "🇫🇷", codeDisplay: "FR" },
    { code: "de" as Language, name: "Deutsch", flag: "🇩🇪", codeDisplay: "DE" },
    { code: "it" as Language, name: "Italiano", flag: "🇮🇹", codeDisplay: "IT" },
    { code: "pt" as Language, name: "Português", flag: "🇵🇹", codeDisplay: "PT" },
  ];

  // Filtrar solo los idiomas disponibles
  const availableLangs = languages.filter((lang) =>
    availableLanguages.includes(lang.code)
  );

  const handleLanguageChange = (langCode: Language) => {
    // Cerrar el picker primero
    setShowLanguagePicker(false);
    // Cambiar el idioma (esto recargará la página)
    setLanguage(langCode);
  };

  const getCurrentLanguage = () => {
    return languages.find((lang) => lang.code === language) || languages[0];
  };

  return (
    <div className="relative flex items-center space-x-2">
      {/* Botón de idioma */}
      <Button
        variant="outline"
        size="sm"
        onClick={() => setShowLanguagePicker(!showLanguagePicker)}
        className="p-2"
        title={t("changeLanguage", "common")}
      >
        <Globe className="h-4 w-4" />
        <span className="ml-1 text-xs font-medium">{getCurrentLanguage().codeDisplay}</span>
      </Button>

      {/* Selector de idiomas */}
      {showLanguagePicker && (
        <div className="absolute top-full right-0 mt-2 p-3 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg shadow-lg z-50 min-w-[200px]">
          <div className="text-sm font-medium mb-3 text-gray-700 dark:text-gray-300">
            {t("selectLanguage", "common")}
          </div>
          <div className="space-y-2">
            {availableLangs.map((lang) => (
              <button
                key={lang.code}
                onClick={() => handleLanguageChange(lang.code)}
                className={`flex items-center justify-between w-full p-2 rounded-lg border transition-all hover:shadow-md ${
                  language === lang.code
                    ? "border-blue-500 bg-blue-50 dark:bg-blue-900/20"
                    : "border-gray-200 dark:border-gray-600 hover:border-gray-300 dark:hover:border-gray-500"
                }`}
              >
                <div className="flex items-center space-x-3">
                  <div className="flex items-center justify-center w-8 h-8 rounded border border-gray-300 dark:border-gray-600 bg-gray-50 dark:bg-gray-700">
                    <span className="text-xs font-semibold text-gray-700 dark:text-gray-300">
                      {lang.codeDisplay}
                    </span>
                  </div>
                  <span className="font-medium text-gray-900 dark:text-gray-100">
                    {lang.name}
                  </span>
                </div>
                {language === lang.code && (
                  <Check className="h-4 w-4 text-blue-500" />
                )}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
