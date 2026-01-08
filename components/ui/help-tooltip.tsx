"use client";

import { useTranslation } from "@/app/config/i18n";
import { HelpCircle } from "lucide-react";
import { useState } from "react";
import { SimpleModal } from "./SimpleModal";

interface HelpTooltipProps {
  helpKey: string; // Clave para las traducciones (ej: "dataSources.dashboard.help")
  className?: string;
}

export default function HelpTooltip({
  helpKey,
  className = "",
}: HelpTooltipProps) {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = useState(false);

  const title = t(`${helpKey}.title`, "Ayuda");
  const content = t(
    `${helpKey}.content`,
    "Información de ayuda no disponible."
  );

  return (
    <>
      <button
        onClick={() => setIsOpen(true)}
        className={`inline-flex items-center justify-center w-6 h-6 text-blue-500 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-950/20 rounded-full transition-all duration-200 hover:scale-110 ${className}`}
        title={t("common.help", "Ayuda")}
      >
        <HelpCircle className="w-4 h-4" />
      </button>

      <SimpleModal
        isOpen={isOpen}
        onClose={() => setIsOpen(false)}
        title={title}
        maxWidth="max-w-3xl"
      >
        <div className="space-y-6">
          <div className="prose prose-sm max-w-none">
            <div
              className="text-muted-foreground leading-relaxed text-sm font-light"
              dangerouslySetInnerHTML={{ __html: content }}
            />
          </div>

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
