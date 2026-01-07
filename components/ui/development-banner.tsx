"use client";

import { Mail } from "lucide-react";
import { useState } from "react";
import { Button } from "./button";

interface DevelopmentBannerProps {
  showEarlyAdopterButton?: boolean;
  className?: string;
  type?: "roadmap" | "operational" | "q1-2026" | "q2-2026" | "development";
  customText?: string;
  customIcon?: string;
  customColor?: "purple" | "green" | "blue" | "orange" | "red";
}

export default function DevelopmentBanner({
  showEarlyAdopterButton = true,
  className = "",
  type = "roadmap",
  customText,
  customIcon,
  customColor = "purple",
}: DevelopmentBannerProps) {
  const [showEarlyAdopterDialog, setShowEarlyAdopterDialog] = useState(false);

  const handleEarlyAdopterSubmit = (formData: any) => {
    // TODO: Implementar envío de formulario
    console.log("Early adopter form submitted:", formData);
    alert("¡Gracias por tu interés! Te contactaremos pronto.");
    setShowEarlyAdopterDialog(false);
  };

  // Configuración por tipo
  const getBannerConfig = () => {
    switch (type) {
      case "operational":
        return {
          text: customText || "✅ Funcionalidad Operativa",
          icon: customIcon || "✅",
          colors:
            "from-green-100 to-emerald-100 text-green-800 border-green-200",
          bgColor: "bg-green-100",
          textColor: "text-green-800",
          borderColor: "border-green-200",
        };
      case "q1-2026":
        return {
          text: customText || "🚀 Roadmap Q1 2026",
          icon: customIcon || "🚀",
          colors: "from-blue-100 to-indigo-100 text-blue-800 border-blue-200",
          bgColor: "bg-blue-100",
          textColor: "text-blue-800",
          borderColor: "border-blue-200",
        };
      case "q2-2026":
        return {
          text: customText || "⚡ Roadmap Q2 2026",
          icon: customIcon || "⚡",
          colors:
            "from-orange-100 to-amber-100 text-orange-800 border-orange-200",
          bgColor: "bg-orange-100",
          textColor: "text-orange-800",
          borderColor: "border-orange-200",
        };

      case "development":
        return {
          text: customText || "Versión 1.1.0 - En desarrollo",
          icon: customIcon || "🔧",
          colors: "from-blue-100 to-indigo-100 text-blue-800 border-blue-200",
          bgColor: "bg-blue-100",
          textColor: "text-blue-800",
          borderColor: "border-blue-200",
        };
      case "roadmap":
      default:
        return {
          text: customText || "Versión 1.0.0 - En desarrollo",
          icon: customIcon || "🔧",
          colors: "from-blue-100 to-indigo-100 text-blue-800 border-blue-200",
          bgColor: "bg-blue-100",
          textColor: "text-blue-800",
          borderColor: "border-blue-200",
        };
    }
  };

  const config = getBannerConfig();

  return (
    <>
      <div className={`flex items-center space-x-2 ${className}`}>
        <span
          className={`px-2 py-1 rounded-full text-xs font-medium bg-gradient-to-r ${config.colors} border`}
        >
          {config.icon} {config.text}
        </span>

        {showEarlyAdopterButton && (
          <Button
            variant="outline"
            size="sm"
            onClick={() => setShowEarlyAdopterDialog(true)}
            className="text-green-600 hover:text-green-700 border-green-300 bg-green-50 hover:bg-green-100"
          >
            <Mail className="w-4 h-4 mr-2" />
            Early Adopter
          </Button>
        )}
      </div>

      {/* Diálogo de Early Adopter */}
      {showEarlyAdopterDialog && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-96 max-w-md">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold text-gray-900">
                🚀 Early Adopter
              </h3>
              <button
                onClick={() => setShowEarlyAdopterDialog(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg
                  className="w-6 h-6"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>

            {/* Mensaje comercial */}
            <div className="mb-6 p-4 bg-gradient-to-r from-blue-50 to-purple-50 rounded-lg border border-blue-200">
              <h4 className="font-semibold text-blue-900 mb-2">
                💎 Beneficios Exclusivos
              </h4>
              <ul className="text-sm text-blue-800 space-y-1">
                <li>
                  • <strong>Acceso prioritario</strong> a nuevas funcionalidades
                </li>
                <li>
                  • <strong>Descuentos especiales</strong> en lanzamiento
                  oficial
                </li>
                <li>
                  • <strong>Influencia directa</strong> en el desarrollo del
                  producto
                </li>
                <li>
                  • <strong>Soporte premium</strong> durante la fase beta
                </li>
              </ul>
            </div>

            <form
              onSubmit={(e) => {
                e.preventDefault();
                const formData = new FormData(e.currentTarget);
                handleEarlyAdopterSubmit({
                  email: formData.get("email"),
                  name: formData.get("name"),
                  comments: formData.get("comments"),
                });
              }}
            >
              <div className="space-y-4">
                <div>
                  <label
                    htmlFor="email"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Correo Electrónico *
                  </label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    required
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="tu@email.com"
                  />
                </div>

                <div>
                  <label
                    htmlFor="name"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Nombre *
                  </label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    required
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Tu nombre completo"
                  />
                </div>

                <div>
                  <label
                    htmlFor="comments"
                    className="block text-sm font-medium text-gray-700 mb-1"
                  >
                    Comentarios
                  </label>
                  <textarea
                    id="comments"
                    name="comments"
                    rows={3}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="¿Qué funcionalidades te gustaría ver primero?"
                  />
                </div>

                <div className="flex space-x-3 pt-2">
                  <button
                    type="button"
                    onClick={() => setShowEarlyAdopterDialog(false)}
                    className="flex-1 px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="flex-1 px-4 py-2 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-md hover:from-blue-700 hover:to-purple-700 font-medium"
                  >
                    ¡Quiero Ser Early Adopter!
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
}
