"use client";

import { Button } from "@/components/ui/button";
import { X } from "lucide-react";
import { ReactNode } from "react";

interface SimpleModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
  maxWidth?: string;
}

export function SimpleModal({
  isOpen,
  onClose,
  title,
  children,
  maxWidth = "max-w-4xl",
}: SimpleModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Overlay con fondo opaco */}
      <div
        className="absolute inset-0 bg-black/80"
        onClick={onClose}
      />

      {/* Modal con fondo opaco y tamaño aumentado */}
      <div
        className={`relative bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-xl shadow-2xl ${maxWidth} w-full max-h-[90vh] overflow-y-auto m-4 transition-all duration-300`}
      >
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-900">
          <h2 className="text-xl font-semibold text-foreground flex items-center gap-2">
            {title}
          </h2>
          <Button
            variant="ghost"
            size="sm"
            onClick={onClose}
            className="h-8 w-8 p-0 hover:bg-destructive/10 hover:text-destructive transition-all duration-200"
          >
            <X className="h-4 w-4" />
          </Button>
        </div>

        {/* Content con más padding */}
        <div className="p-8">{children}</div>
      </div>
    </div>
  );
}
