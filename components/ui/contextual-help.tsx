"use client";

import { useTranslation } from "@/app/config/i18n";
import { HelpCircle } from "lucide-react";
import { useState, useEffect } from "react";
import { SimpleModal } from "./SimpleModal";

interface HelpContent {
  title: string;
  body: string;
}

interface ContextualHelpProps {
  routeId: string;
  className?: string;
}

export default function ContextualHelp({
  routeId,
  className = "",
}: ContextualHelpProps) {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);
  const [helpContent, setHelpContent] = useState<HelpContent | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const loadHelpContent = async () => {
    setIsLoading(true);
    try {
      // Cargar contenido de ayuda desde el archivo JSON
      const response = await fetch('/help/help.es.json');
      const helpData = await response.json();
      
      const content = helpData[routeId];
      if (content) {
        setHelpContent(content);
      } else {
        // Fallback a traducciones si no se encuentra en JSON
        setHelpContent({
          title: t(`${routeId}.help.title`, "Ayuda"),
          body: t(`${routeId}.help.content`, "Información de ayuda no disponible.")
        });
      }
    } catch (error) {
      console.error('Error loading help content:', error);
      // Fallback a traducciones en caso de error
      setHelpContent({
        title: t(`${routeId}.help.title`, "Ayuda"),
        body: t(`${routeId}.help.content`, "Información de ayuda no disponible.")
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleOpen = () => {
    if (!helpContent) {
      loadHelpContent();
    }
    setIsOpen(true);
  };

  return (
    <>
      <button
        onClick={handleOpen}
        className={`inline-flex items-center justify-center w-6 h-6 text-blue-500 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-950/20 rounded-full transition-all duration-200 hover:scale-110 ${className}`}
        title={t("common.help", "Ayuda")}
      >
        <HelpCircle className="w-4 h-4" />
      </button>

      <SimpleModal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        title={helpContent?.title || "Ayuda"}
        maxWidth="max-w-3xl"
      >
        <div className="space-y-6">
          {isLoading ? (
            <div className="flex items-center justify-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
              <span className="ml-3 text-gray-600">Cargando ayuda...</span>
            </div>
          ) : (
            <div className="prose prose-sm max-w-none">
              <div
                className="text-muted-foreground leading-relaxed text-sm font-light"
                dangerouslySetInnerHTML={{ 
                  __html: helpContent?.body || "Información de ayuda no disponible." 
                }}
              />
            </div>
          )}

          {/* Footer con botón de cerrar más visible */}
          <div className="flex justify-end pt-4 border-t border-border">
            <button
              onClick={() => setIsOpen(false)}
              className="px-3 py-1.5 bg-blue-500 hover:bg-blue-600 text-white rounded-md transition-all duration-200 hover:scale-105 text-xs font-normal"
            >
              Entendido
            </button>
          </div>
        </div>
      </SimpleModal>
    </>
  );
}
