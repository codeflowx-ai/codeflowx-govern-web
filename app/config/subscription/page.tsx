// app/config/subscription/page.tsx
"use client";

import DevelopmentBanner from "@/components/ui/development-banner";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { useState } from "react";

interface Plan {
  id: string;
  name: string;
  priceMonthly: number;
  priceSemestral: number;
  priceAnnual: number;
  originalPriceMonthly?: number;
  features: string[];
  limits: {
    nodes: string;
    users: string;
    projects: string;
    appGeneration: string;
    models: string;
    finetuning: string;
    playground: string;
    support: string;
    consulting: string;
  };
  popular?: boolean;
  enterprise?: boolean;
}

interface Subscription {
  id: string;
  planId: string;
  billingCycle: "semestral" | "annual";
  status: "active" | "canceled" | "past_due" | "trialing";
  currentPeriodStart: string;
  currentPeriodEnd: string;
  cancelAtPeriodEnd: boolean;
  nodes: number;
  trialEnd?: string;
}

interface Banner {
  id: string;
  type: "offer" | "info" | "warning" | "success";
  title: string;
  message: string;
  action?: {
    text: string;
    url: string;
  };
  dismissible: boolean;
  expiresAt?: string;
}

// Mock banners - estos vendrían de la API
const mockBanners: Banner[] = [
  {
    id: "launch-offer",
    type: "offer",
    title: "Oferta de Lanzamiento",
    message:
      "Descuentos especiales + 10% adicional en facturación anual - Válido hasta 31 Mar 2024",
    action: {
      text: "Ver Ofertas",
      url: "#plans",
    },
    dismissible: true,
    expiresAt: "2024-03-31",
  },
  {
    id: "unique-feature",
    type: "success",
    title: "Funcionalidad Única en el Mercado",
    message:
      "Análisis automático de requisitos + Generación completa de aplicaciones empresariales desde documentos",
    dismissible: true,
  },
];

const plans: Plan[] = [
  {
    id: "starter",
    name: "Starter",
    priceMonthly: 79,
    priceSemestral: 71 * 6,
    priceAnnual: Math.round(71 * 12 * 0.9),
    originalPriceMonthly: 79,
    features: [
      "Self-hosted en tu infraestructura",
      "Modelos genéricos incluidos",
      "Playground básico",
      "Generación de código ilimitada",
      "Solo CPU requerido",
      "Soporte comunidad",
    ],
    limits: {
      nodes: "1 nodo",
      users: "Hasta 4",
      projects: "Hasta 2",
      appGeneration: "Hasta 3/mes",
      models: "2 incluidos",
      finetuning: "No incluido",
      playground: "Básico",
      support: "Comunidad",
      consulting: "Bajo presupuesto",
    },
  },
  {
    id: "pro",
    name: "Pro",
    priceMonthly: 399,
    priceSemestral: 319 * 6,
    priceAnnual: Math.round(319 * 12 * 0.9),
    originalPriceMonthly: 399,
    popular: true,
    features: [
      "Self-hosted escalable",
      "Modelos especializados por stack",
      "Fine-tuning con tu código",
      "Generación de código ilimitada",
      "IA de análisis de requisitos avanzada",
      "Soporte GPU incluido",
      "Playground avanzado",
      "Soporte prioritario",
    ],
    limits: {
      nodes: "Hasta 3 nodos",
      users: "Ilimitados",
      projects: "Ilimitados",
      appGeneration: "Hasta 25/mes",
      models: "6 preentrenados/custom",
      finetuning: "Incluido (1 modelo)",
      playground: "Avanzado",
      support: "Prioritario",
      consulting: "Bajo presupuesto",
    },
  },
  {
    id: "enterprise",
    name: "Enterprise",
    priceMonthly: 899,
    priceSemestral: 719 * 6,
    priceAnnual: Math.round(719 * 12 * 0.9),
    originalPriceMonthly: 899,
    enterprise: true,
    features: [
      "Infraestructura a medida",
      "Modelos personalizados",
      "Pipeline de entrenamiento custom",
      "Generación de código ilimitada",
      "IA de análisis empresarial completa",
      "Generación automática de aplicaciones",
      "Playground completo + verticales",
      "SLA dedicado",
      "Consultoría incluida",
    ],
    limits: {
      nodes: "A medida",
      users: "Ilimitados",
      projects: "Ilimitados",
      appGeneration: "Ilimitado",
      models: "A medida",
      finetuning: "Incluido (personalizado)",
      playground: "Completo + verticales",
      support: "SLA dedicado",
      consulting: "Incluida (personalizada)",
    },
  },
];

const mockSubscription: Subscription = {
  id: "sub_123",
  planId: "pro",
  billingCycle: "annual",
  status: "active",
  currentPeriodStart: "2024-01-01",
  currentPeriodEnd: "2024-12-31",
  cancelAtPeriodEnd: false,
  nodes: 2,
};

export default function SubscriptionPage() {
  const [subscription, setSubscription] =
    useState<Subscription>(mockSubscription);
  const [banners, setBanners] = useState<Banner[]>(mockBanners);
  const [showChangePlan, setShowChangePlan] = useState(false);
  const [showCancelConfirm, setShowCancelConfirm] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [showContactModal, setShowContactModal] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState<string>("");
  const [selectedBilling, setSelectedBilling] = useState<
    "semestral" | "annual"
  >("annual");
  const [viewMode, setViewMode] = useState<"semestral" | "annual">("annual");
  const [successMessage, setSuccessMessage] = useState("");

  const currentPlan = plans.find((p) => p.id === subscription.planId);

  const dismissBanner = (bannerId: string) => {
    setBanners((prev) => prev.filter((banner) => banner.id !== bannerId));
  };

  const getBannerStyles = (type: Banner["type"]) => {
    switch (type) {
      case "offer":
        return "bg-gradient-to-r from-blue-600 to-purple-600 text-white";
      case "success":
        return "bg-gradient-to-r from-green-500 to-emerald-600 text-white";
      case "warning":
        return "bg-gradient-to-r from-yellow-500 to-orange-500 text-white";
      case "info":
        return "bg-gradient-to-r from-blue-500 to-cyan-500 text-white";
      default:
        return "bg-gray-100 text-gray-900";
    }
  };

  const handleChangePlan = (planId: string) => {
    if (planId === "enterprise") {
      setShowContactModal(true);
      return;
    }
    setSelectedPlan(planId);
    setShowChangePlan(true);
  };

  const confirmChangePlan = () => {
    setSubscription((prev) => ({
      ...prev,
      planId: selectedPlan,
      billingCycle: selectedBilling,
    }));
    setShowChangePlan(false);
    setSuccessMessage(
      "Plan actualizado correctamente. Los cambios se aplicarán en el próximo ciclo de facturación."
    );
    setShowSuccessModal(true);
  };

  const handleCancelSubscription = () => {
    setSubscription((prev) => ({
      ...prev,
      cancelAtPeriodEnd: true,
    }));
    setShowCancelConfirm(false);
    setSuccessMessage(
      "La subscripción se cancelará al final del período actual."
    );
    setShowSuccessModal(true);
  };

  const handleReactivate = () => {
    setSubscription((prev) => ({
      ...prev,
      cancelAtPeriodEnd: false,
    }));
    setSuccessMessage("Subscripción reactivada correctamente.");
    setShowSuccessModal(true);
  };

  const handleAddNodes = () => {
    setShowContactModal(true);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-green-100 text-green-700";
      case "trialing":
        return "bg-blue-100 text-blue-700";
      case "past_due":
        return "bg-yellow-100 text-yellow-700";
      case "canceled":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  const getDiscountPercentage = (original: number, current: number) => {
    return Math.round((1 - current / original) * 100);
  };

  const getCurrentPrice = (plan: Plan) => {
    if (viewMode === "annual") {
      return {
        price: plan.priceAnnual,
        period: "año",
        monthlyEquivalent: Math.round(plan.priceAnnual / 12),
        savings: plan.priceSemestral * 2 - plan.priceAnnual,
      };
    } else {
      return {
        price: plan.priceSemestral,
        period: "6 meses",
        monthlyEquivalent: Math.round(plan.priceSemestral / 6),
        savings: 0,
      };
    }
  };

  const getCurrentSubscriptionPrice = () => {
    if (!currentPlan) return { price: 0, period: "", monthlyEquivalent: 0 };

    if (subscription.billingCycle === "annual") {
      return {
        price: currentPlan.priceAnnual,
        period: "año",
        monthlyEquivalent: Math.round(currentPlan.priceAnnual / 12),
      };
    } else {
      return {
        price: currentPlan.priceSemestral,
        period: "6 meses",
        monthlyEquivalent: Math.round(currentPlan.priceSemestral / 6),
      };
    }
  };

  const currentSubscriptionPrice = getCurrentSubscriptionPrice();

  return (
    <div className="p-6 space-y-4">
      
      {/* Header */}
      <div className="space-y-2">
        <h1 className="text-2xl font-bold text-gray-900">
          💳 Subscripción Leka Server
        </h1>
        <p className="text-gray-600">
          Gestiona tu licencia self-hosted y facturación
        </p>
        {/* Banner de Desarrollo - Versión 1.0.0 Operativa */}
        <DevelopmentBanner
          type="operational"
          customText="✅ Versión 1.0.0 - Operativa"
          showEarlyAdopterButton={false}
          className="justify-start"
        />
      </div>

      {/* Dynamic Banners */}
      {banners.map((banner) => (
        <div
          key={banner.id}
          className={`${getBannerStyles(banner.type)} rounded-lg p-4`}
        >
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3 flex-1">
              <span className="text-2xl">
                {banner.type === "offer" && "🚀"}
                {banner.type === "success" && "✨"}
                {banner.type === "warning" && "⚠️"}
                {banner.type === "info" && "ℹ️"}
              </span>
              <div className="flex-1">
                <h3 className="font-bold text-lg">{banner.title}</h3>
                <p className="text-sm opacity-90">{banner.message}</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              {banner.action && (
                <a
                  href={banner.action.url}
                  className="bg-white bg-opacity-20 hover:bg-opacity-30 px-4 py-2 rounded-lg text-sm font-medium transition-colors"
                >
                  {banner.action.text}
                </a>
              )}
              {banner.dismissible && (
                <button
                  onClick={() => dismissBanner(banner.id)}
                  className="text-white hover:text-gray-200 p-1"
                >
                  <svg
                    className="w-5 h-5"
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
              )}
            </div>
          </div>
        </div>
      ))}

      {/* Important Notice */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <div className="flex items-start gap-3">
          <span className="text-blue-600 text-xl">ℹ️</span>
          <div>
            <h3 className="font-medium text-blue-900 mb-1">
              Licencia Self-Hosted
            </h3>
            <p className="text-sm text-blue-700">
              La licencia cubre únicamente el software Leka Server y soporte.
              Los costes de infraestructura (servidores, GPU, almacenamiento)
              son gestionados por tu empresa.
            </p>
          </div>
        </div>
      </div>

      {/* Current Subscription */}
      <div className="bg-white rounded-lg border border-gray-200 p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          Subscripción Actual
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <div className="flex items-center gap-3 mb-4">
              <h3 className="text-xl font-bold text-gray-900">
                {currentPlan?.name}
              </h3>
              <span
                className={`px-3 py-1 text-sm rounded-full font-medium ${getStatusColor(
                  subscription.status
                )}`}
              >
                {subscription.status}
              </span>
              <span className="px-2 py-1 text-xs bg-purple-100 text-purple-700 rounded-full">
                {subscription.billingCycle === "annual" ? "Anual" : "Semestral"}
              </span>
            </div>

            <div className="space-y-3">
              <div>
                <span className="text-sm text-gray-500">Precio</span>
                <p className="text-2xl font-bold text-gray-900">
                  {currentSubscriptionPrice.price}€/
                  {currentSubscriptionPrice.period}
                </p>
                <p className="text-sm text-gray-500">
                  Equivale a {currentSubscriptionPrice.monthlyEquivalent}€/mes
                </p>
                {subscription.billingCycle === "annual" && (
                  <p className="text-sm text-green-600 font-medium">
                    ✓ Incluye 10% descuento anual
                  </p>
                )}
              </div>

              <div>
                <span className="text-sm text-gray-500">Nodos activos</span>
                <p className="text-lg font-semibold text-gray-900">
                  {subscription.nodes} / {currentPlan?.limits.nodes}
                </p>
              </div>

              <div>
                <span className="text-sm text-gray-500">Período actual</span>
                <p className="text-sm text-gray-900">
                  {new Date(
                    subscription.currentPeriodStart
                  ).toLocaleDateString()}{" "}
                  -{" "}
                  {new Date(subscription.currentPeriodEnd).toLocaleDateString()}
                </p>
              </div>

              {subscription.cancelAtPeriodEnd && (
                <div className="p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
                  <p className="text-sm text-yellow-800">
                    ⚠️ Tu subscripción se cancelará el{" "}
                    {new Date(
                      subscription.currentPeriodEnd
                    ).toLocaleDateString()}
                  </p>
                </div>
              )}
            </div>
          </div>

          <div>
            <h4 className="font-medium text-gray-900 mb-3">Límites Actuales</h4>
            <div className="space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Nodos Kubernetes</span>
                <span className="font-medium">{currentPlan?.limits.nodes}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Usuarios</span>
                <span className="font-medium">{currentPlan?.limits.users}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Proyectos activos</span>
                <span className="font-medium">
                  {currentPlan?.limits.projects}
                </span>
              </div>
              <div className="flex justify-between text-sm border-t border-gray-200 pt-2">
                <span className="text-gray-600 font-medium">
                  🚀 Apps desde requisitos
                </span>
                <span className="font-bold text-blue-600">
                  {currentPlan?.limits.appGeneration}
                </span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Generación código</span>
                <span className="font-medium text-green-600">Ilimitada</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Modelos IA</span>
                <span className="font-medium">
                  {currentPlan?.limits.models}
                </span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Fine-tuning</span>
                <span className="font-medium">
                  {currentPlan?.limits.finetuning}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="flex gap-3 mt-6">
          {subscription.cancelAtPeriodEnd ? (
            <button
              onClick={handleReactivate}
              className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
            >
              🔄 Reactivar Subscripción
            </button>
          ) : (
            <button
              onClick={() => setShowCancelConfirm(true)}
              className="px-4 py-2 bg-red-100 text-red-700 rounded-lg hover:bg-red-200"
            >
              ❌ Cancelar Subscripción
            </button>
          )}

          <button
            onClick={handleAddNodes}
            className="px-4 py-2 bg-blue-100 text-blue-700 rounded-lg hover:bg-blue-200"
          >
            ➕ Añadir Nodos
          </button>
        </div>
      </div>

      {/* Billing Toggle */}
      <div
        id="plans"
        className="bg-white rounded-lg border border-gray-200 p-6"
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-lg font-semibold text-gray-900">
            Planes Disponibles
          </h2>

          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-600">Facturación:</span>
            <div className="flex bg-gray-100 rounded-lg p-1">
              <button
                onClick={() => setViewMode("semestral")}
                className={`px-4 py-2 text-sm font-medium rounded-md transition-colors ${
                  viewMode === "semestral"
                    ? "bg-white text-gray-900 shadow-sm"
                    : "text-gray-600 hover:text-gray-900"
                }`}
              >
                Semestral
              </button>
              <button
                onClick={() => setViewMode("annual")}
                className={`px-4 py-2 text-sm font-medium rounded-md transition-colors relative ${
                  viewMode === "annual"
                    ? "bg-white text-gray-900 shadow-sm"
                    : "text-gray-600 hover:text-gray-900"
                }`}
              >
                Anual
                <span className="absolute -top-1 -right-1 bg-green-500 text-white text-xs px-1 rounded-full">
                  -10%
                </span>
              </button>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {plans.map((plan) => {
            const pricing = getCurrentPrice(plan);
            return (
              <div
                key={plan.id}
                className={`border rounded-lg p-6 relative ${
                  plan.id === subscription.planId
                    ? "border-blue-500 bg-blue-50"
                    : "border-gray-200"
                } ${plan.popular ? "ring-2 ring-blue-500" : ""} ${
                  plan.enterprise ? "ring-2 ring-purple-500" : ""
                }`}
              >
                {plan.popular && (
                  <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
                    <span className="bg-blue-500 text-white px-3 py-1 text-sm rounded-full">
                      Más Popular
                    </span>
                  </div>
                )}

                {plan.enterprise && (
                  <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
                    <span className="bg-purple-500 text-white px-3 py-1 text-sm rounded-full">
                      Enterprise
                    </span>
                  </div>
                )}

                {plan.id === subscription.planId && (
                  <div className="absolute -top-3 right-4">
                    <span className="bg-green-500 text-white px-3 py-1 text-sm rounded-full">
                      Plan Actual
                    </span>
                  </div>
                )}

                <div className="text-center mb-6">
                  <h3 className="text-xl font-bold text-gray-900 mb-2">
                    {plan.name}
                  </h3>
                  <div className="mb-2">
                    {plan.originalPriceMonthly && viewMode === "semestral" && (
                      <div className="flex items-center justify-center gap-2 mb-1">
                        <span className="text-lg text-gray-400 line-through">
                          {plan.originalPriceMonthly * 6}€/6 meses
                        </span>
                        <span className="bg-green-500 text-white px-2 py-1 text-xs rounded-full">
                          -
                          {getDiscountPercentage(
                            plan.originalPriceMonthly * 6,
                            pricing.price
                          )}
                          %
                        </span>
                      </div>
                    )}
                    <div className="text-3xl font-bold text-gray-900 mb-1">
                      {pricing.price}€
                    </div>
                    <div className="text-sm text-gray-500">
                      por {pricing.period}
                    </div>
                    <div className="text-sm text-gray-600 mt-1">
                      {pricing.monthlyEquivalent}€/mes
                    </div>
                  </div>
                  {viewMode === "annual" && pricing.savings > 0 && (
                    <p className="text-xs text-green-600 font-medium">
                      💰 Ahorras {pricing.savings}€ al año
                    </p>
                  )}
                </div>

                <ul className="space-y-3 mb-6">
                  {plan.features.map((feature, index) => (
                    <li key={index} className="flex items-start gap-2 text-sm">
                      <span className="text-green-500 mt-0.5">✓</span>
                      <span className="text-gray-700">{feature}</span>
                    </li>
                  ))}
                </ul>

                <div className="space-y-2 mb-6 text-sm border-t border-gray-200 pt-4">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Nodos K8s</span>
                    <span className="font-medium">{plan.limits.nodes}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Usuarios</span>
                    <span className="font-medium">{plan.limits.users}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Proyectos</span>
                    <span className="font-medium">{plan.limits.projects}</span>
                  </div>
                  <div className="flex justify-between border-t border-gray-100 pt-2">
                    <span className="text-gray-600 font-medium">
                      🚀 Apps automáticas
                    </span>
                    <span className="font-bold text-blue-600">
                      {plan.limits.appGeneration}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Código manual</span>
                    <span className="font-medium text-green-600">
                      Ilimitado
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Modelos IA</span>
                    <span className="font-medium">{plan.limits.models}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Fine-tuning</span>
                    <span className="font-medium">
                      {plan.limits.finetuning}
                    </span>
                  </div>
                </div>

                <button
                  onClick={() => handleChangePlan(plan.id)}
                  disabled={plan.id === subscription.planId}
                  className={`w-full py-2 px-4 rounded-lg font-medium ${
                    plan.id === subscription.planId
                      ? "bg-gray-100 text-gray-500 cursor-not-allowed"
                      : plan.enterprise
                      ? "bg-purple-600 text-white hover:bg-purple-700"
                      : "bg-blue-600 text-white hover:bg-blue-700"
                  }`}
                >
                  {plan.id === subscription.planId
                    ? "Plan Actual"
                    : plan.enterprise
                    ? "Contactar Ventas"
                    : "Cambiar a Este Plan"}
                </button>
              </div>
            );
          })}
        </div>
      </div>

      {/* Infrastructure Note */}
      <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
        <h3 className="font-medium text-gray-900 mb-2">
          📋 Información Importante
        </h3>
        <div className="text-sm text-gray-600 space-y-1">
          <p>
            • <strong>Self-hosted:</strong> Despliegas Leka Server en tu propia
            infraestructura
          </p>
          <p>
            • <strong>Facturación:</strong> Semestral (cada 6 meses) o anual con
            10% descuento
          </p>
          <p>
            • <strong>Generación código:</strong> Ilimitada en playground y
            desarrollo manual
          </p>
          <p>
            • <strong>Apps automáticas:</strong> Límite en generación completa
            desde documentos
          </p>
          <p>
            • <strong>Kubernetes:</strong> Licencia por número de nodos del
            cluster
          </p>
          <p>
            • <strong>Hardware:</strong> CPU/GPU, almacenamiento y red son
            responsabilidad del cliente
          </p>
          <p>
            • <strong>Diferenciador único:</strong> Análisis de requisitos +
            generación automática de apps
          </p>
        </div>
      </div>

      {/* Change Plan Modal */}
      {showChangePlan && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Confirmar Cambio de Plan
            </h3>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Ciclo de facturación:
              </label>
              <div className="flex gap-2">
                <button
                  onClick={() => setSelectedBilling("semestral")}
                  className={`flex-1 px-3 py-2 text-sm rounded-lg border ${
                    selectedBilling === "semestral"
                      ? "border-blue-500 bg-blue-50 text-blue-700"
                      : "border-gray-300 text-gray-700 hover:bg-gray-50"
                  }`}
                >
                  Semestral
                </button>
                <button
                  onClick={() => setSelectedBilling("annual")}
                  className={`flex-1 px-3 py-2 text-sm rounded-lg border relative ${
                    selectedBilling === "annual"
                      ? "border-blue-500 bg-blue-50 text-blue-700"
                      : "border-gray-300 text-gray-700 hover:bg-gray-50"
                  }`}
                >
                  Anual
                  <span className="absolute -top-1 -right-1 bg-green-500 text-white text-xs px-1 rounded-full">
                    -10%
                  </span>
                </button>
              </div>
            </div>

            <p className="text-gray-600 mb-6">
              ¿Confirmas el cambio al plan{" "}
              {plans.find((p) => p.id === selectedPlan)?.name} con facturación{" "}
              {selectedBilling}? El cambio se aplicará en el próximo ciclo de
              facturación.
            </p>
            <div className="flex gap-3">
              <button
                onClick={() => setShowChangePlan(false)}
                className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button
                onClick={confirmChangePlan}
                className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
              >
                Confirmar Cambio
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Cancel Confirmation Modal */}
      {showCancelConfirm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <div className="text-center">
              <div className="text-4xl mb-4">😢</div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                ¿Cancelar Subscripción?
              </h3>
              <p className="text-gray-600 mb-6">
                Tu subscripción permanecerá activa hasta el{" "}
                {new Date(subscription.currentPeriodEnd).toLocaleDateString()}.
                Después perderás acceso a las funcionalidades premium.
              </p>
              <div className="flex gap-3">
                <button
                  onClick={() => setShowCancelConfirm(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
                >
                  Mantener Subscripción
                </button>
                <button
                  onClick={handleCancelSubscription}
                  className="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
                >
                  Cancelar Subscripción
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Success Modal */}
      <SimpleModal
        isOpen={showSuccessModal}
        onClose={() => setShowSuccessModal(false)}
        title="¡Operación Exitosa!"
        maxWidth="max-w-md"
      >
        <div className="text-center">
          <div className="text-4xl mb-4">✅</div>
          <p className="text-gray-600 mb-6">{successMessage}</p>
          <button
            onClick={() => setShowSuccessModal(false)}
            className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Entendido
          </button>
        </div>
      </SimpleModal>

      {/* Contact Modal */}
      <SimpleModal
        isOpen={showContactModal}
        onClose={() => setShowContactModal(false)}
        title="Contactar Ventas"
        maxWidth="max-w-md"
      >
        <div className="text-center">
          <div className="text-4xl mb-4">📞</div>
          <p className="text-gray-600 mb-6">
            Para el plan Enterprise o añadir nodos adicionales, nuestro equipo
            de ventas te contactará para crear una propuesta personalizada.
          </p>
          <div className="space-y-3">
            <div className="text-sm text-gray-600">
              <p>
                <strong>Email:</strong> sales@leka.com
              </p>
              <p>
                <strong>Teléfono:</strong> +34 900 123 456
              </p>
            </div>
            <button
              onClick={() => setShowContactModal(false)}
              className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
              Cerrar
            </button>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}
