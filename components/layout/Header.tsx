"use client";

import { useTranslation } from "@/app/config/i18n";
import {
  canAccessModule,
  getMenuByModule,
  getModuleByPath,
  getModulesByRoles,
  modulesConfig,
} from "@/app/config/modules";
import { brandingConfig } from "@/app/config/branding";
import { LanguageSwitcher } from "@/components/ui/language-switcher";
import { ThemeSwitcher } from "@/components/ui/theme-switcher";
import {
  Activity,
  BarChart3,
  Bot,
  Brain,
  ChevronDown,
  ClipboardCheck,
  Code,
  Database,
  FileText,
  Github,
  Globe,
  GraduationCap,
  Grid,
  Inbox,
  Layers,
  LogOut,
  Package,
  Plug,
  Search,
  Server,
  Settings,
  Shield,
  ShoppingCart,
  Target,
  TestTube,
  Zap,
} from "lucide-react";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { NotificationCenter } from "./NotificationCenter";
import { usePageTitle } from "@/components/contexts/PageTitleContext";

// Mapa de iconos para usar con la configuración centralizada
const iconMap: Record<string, any> = {
  BarChart3,
  Zap,
  Brain,
  Database,
  Server,
  Shield,
  Target,
  Layers,
  Plug,
  GraduationCap,
  FileText,
  Settings,
  Code,
  Bot,
  Search,
  Globe,
  ShoppingCart,
  Github,
  Activity,
  Inbox,
  ClipboardCheck,
  Package,
  TestTube,
};

interface User {
  name: string;
  email: string;
  roles: string[];
}

export function Header() {
  const { t, language } = useTranslation();
  const { pageTitle } = usePageTitle();
  const [user, setUser] = useState<User | null>(null);
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const [showAppsMenu, setShowAppsMenu] = useState(false);
  const [modules, setModules] = useState<any[]>([]);
  const router = useRouter();
  const pathname = usePathname();

  // Cargar usuario solo una vez al montar
  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (userData) {
      const u = JSON.parse(userData);
      setUser(u);
      if (u.roles) {
        // Usar la configuración centralizada
        const userModules = getModulesByRoles(u.roles);
        const modulesWithIcons = userModules.map((module) => ({
          ...module,
          icon: iconMap[module.icon] || BarChart3,
        }));
        setModules(modulesWithIcons);
      } else {
        setModules([]);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Solo ejecutar una vez al montar

  // Detectar módulo activo cuando cambia el pathname o cuando el usuario está disponible
  useEffect(() => {
    if (!user?.roles) return;

    // Detectar módulo activo basado en la URL actual
    const currentModule = getModuleByPath(pathname);
    if (currentModule) {
      // Verificar si el módulo es válido para el usuario
      const isValidModule = canAccessModule(user.roles, currentModule);

      if (isValidModule) {
        localStorage.setItem("activeModule", currentModule);
        window.dispatchEvent(new Event("activeModuleChanged"));
      } else {
        // Si el módulo no es válido, establecer Dashboard como fallback
        localStorage.setItem("activeModule", "Dashboard");
        window.dispatchEvent(new Event("activeModuleChanged"));
      }
    } else {
      // Establecer Dashboard como módulo activo por defecto si no hay uno activo
      const currentActiveModule = localStorage.getItem("activeModule");
      if (!currentActiveModule) {
        localStorage.setItem("activeModule", "Dashboard");
        window.dispatchEvent(new Event("activeModuleChanged"));
      }
    }
  }, [pathname, user?.roles]);

  const handleLogout = () => {
    localStorage.removeItem("isAuthenticated");
    localStorage.removeItem("user");
    router.push("/");
  };

  const setActiveModule = (moduleName: string) => {
    // Establecer en localStorage
    localStorage.setItem("activeModule", moduleName);

    // Disparar evento personalizado
    window.dispatchEvent(
      new CustomEvent("activeModuleChanged", {
        detail: { moduleName },
      })
    );

    // Cerrar menú
    setShowAppsMenu(false);

    // Navegar directamente al módulo
    const moduleMenu = getMenuByModule(moduleName);
    const defaultPage = moduleMenu.find((item) => item.isDefault);
    if (defaultPage && defaultPage.href) {
      // Navegación directa para evitar problemas del router
      window.location.href = defaultPage.href;
    } else {
      window.location.href = "/dashboard";
    }
  };

  if (!user) return null;

  return (
    <>
      <header
        className="px-6 py-3 relative shadow-sm"
        style={{
          backgroundColor: "var(--theme-header-background)",
          boxShadow: "var(--theme-shadowLight)",
        }}
      >
        <div className="flex items-center justify-between" style={{ backgroundColor: "transparent" }}>
          <div className="flex items-center gap-4" style={{ backgroundColor: "transparent" }}>
            <div className="relative">
              <button
                onClick={() => setShowAppsMenu((v) => !v)}
                className="p-2 rounded-lg transition-colors"
                style={{
                  backgroundColor: "transparent",
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor = "var(--theme-header-hover)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = "transparent";
                }}
                title={t("layout.header.changeModule")}
              >
                <Grid className="w-6 h-6" style={{ color: "var(--theme-header-text)" }} />
              </button>
              {showAppsMenu && (
                <div
                  className="absolute left-0 mt-2 w-64 rounded-lg shadow-lg border z-50 header-dropdown-menu"
                  style={{
                    backgroundColor: "var(--theme-header-background)",
                    borderColor: "var(--theme-header-border)",
                    boxShadow: "var(--theme-shadow)",
                  }}
                >
                  <div
                    className="p-3 border-b text-xs font-semibold"
                    style={{
                      borderBottomColor: "var(--theme-header-border)",
                      color: "var(--theme-header-text)",
                    }}
                  >
                    {t("layout.header.modules")}
                  </div>
                  <ul className="divide-y" style={{ borderColor: "var(--theme-header-border)" }}>
                    {modules.map((mod) => {
                      const ModIcon = mod.icon;
                      // Traducir el nombre del módulo
                      const translatedModuleName = t(`layout.header.moduleNames.${mod.name}`, mod.name);
                      return (
                        <li key={mod.name}>
                          <button
                            onClick={() => setActiveModule(mod.name)}
                            className="w-full flex items-center gap-3 px-4 py-3 text-sm transition-colors"
                            style={{
                              color: "var(--theme-header-text)",
                              backgroundColor: "transparent",
                            }}
                            onMouseEnter={(e) => {
                              e.currentTarget.style.backgroundColor = "var(--theme-header-hover)";
                            }}
                            onMouseLeave={(e) => {
                              e.currentTarget.style.backgroundColor = "transparent";
                            }}
                          >
                            <ModIcon className="w-5 h-5" />
                            <span className="font-medium">{translatedModuleName}</span>
                          </button>
                        </li>
                      );
                    })}
                  </ul>
                </div>
              )}
            </div>
            <div className="flex items-center space-x-4">
              <div className="flex items-center gap-3">
                {/* Logo configurable del cliente */}
                <img
                  src={brandingConfig.logoPath}
                  alt={brandingConfig.logoAlt}
                  className="h-10 w-auto"
                  style={{ maxWidth: "200px" }}
                />
                {/* Nombre de la empresa - Solo se muestra si logoOnly es false */}
                {!brandingConfig.logoOnly && (
                  <div className="flex items-center">
                    {brandingConfig.useGradientName ? (
                      // Estilo con gradientes (estilo CodeflowX original)
                      <>
                        <span
                          className="text-xl font-bold select-none bg-gradient-to-r from-blue-600 via-blue-500 to-blue-600 bg-clip-text text-transparent"
                          style={{
                            background:
                              "linear-gradient(135deg, var(--theme-primary) 0%, var(--theme-secondary) 50%, var(--theme-accent) 100%)",
                            WebkitBackgroundClip: "text",
                            WebkitTextFillColor: "transparent",
                            backgroundClip: "text",
                          }}
                        >
                          {brandingConfig.companyName.split(" ")[0] || brandingConfig.companyName}
                        </span>
                        {brandingConfig.companyName.split(" ").length > 1 && (
                          <>
                            <span
                              className="text-xl font-bold select-none bg-gradient-to-r from-green-500 via-emerald-500 to-teal-500 bg-clip-text text-transparent"
                              style={{
                                background:
                                  "linear-gradient(135deg, var(--theme-success) 0%, #10b981 50%, #0d9488 100%)",
                                WebkitBackgroundClip: "text",
                                WebkitTextFillColor: "transparent",
                                backgroundClip: "text",
                              }}
                            >
                              {" "}
                              {brandingConfig.companyName.split(" ").slice(1).join(" ")}
                            </span>
                          </>
                        )}
                      </>
                    ) : (
                      // Estilo simple sin gradientes
                      <span
                        className="text-xl font-bold select-none"
                        style={{ color: "var(--theme-header-text)" }}
                      >
                        {brandingConfig.companyName}
                      </span>
                    )}
                  </div>
                )}
              </div>
              <div className="flex flex-col">
                {pageTitle ? (
                  <h1
                    className="text-lg font-semibold"
                    style={{ color: "var(--theme-header-text)" }}
                  >
                    {pageTitle}
                  </h1>
                ) : (
                  <h1
                    className="text-lg font-semibold"
                    style={{ color: "var(--theme-header-text)" }}
                  >
                    {t("layout.header.welcomeMessage", undefined, {
                      name: user.name.split(" ")[0],
                    })}
                  </h1>
                )}
              </div>
            </div>
          </div>

          <div className="flex items-center gap-4" style={{ backgroundColor: "transparent" }}>
            <div
              className="flex items-center gap-2 text-sm"
              style={{ color: "var(--theme-header-text)" }}
            >
              <div
                className="w-2 h-2 rounded-full"
                style={{ backgroundColor: "var(--theme-success)" }}
              ></div>
              <span>{t("layout.header.securedWith2FA")}</span>
            </div>

            <ThemeSwitcher />
            <LanguageSwitcher />
            <NotificationCenter />

            <div className="relative">
              <button
                onClick={() => setShowUserMenu(!showUserMenu)}
                className="flex items-center gap-3 p-2 rounded-lg transition-colors"
                style={{
                  color: "var(--theme-header-text)",
                  backgroundColor: "transparent",
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor =
                    "var(--theme-header-hover)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = "transparent";
                }}
              >
                <div
                  className="w-8 h-8 rounded-full flex items-center justify-center"
                  style={{
                    backgroundColor: "var(--theme-surface)",
                    color: "var(--theme-header-text)",
                  }}
                >
                  <span
                    className="text-sm font-medium"
                    style={{ color: "var(--theme-header-text)" }}
                  >
                    {user.name
                      .split(" ")
                      .map((n) => n[0])
                      .join("")}
                  </span>
                </div>
                <div className="text-left">
                  <div
                    className="text-sm font-medium"
                    style={{ color: "var(--theme-header-text)" }}
                  >
                    {user.name}
                  </div>
                  <div
                    className="text-xs"
                    style={{ color: "var(--theme-textSecondary)" }}
                  >
                    {user.email}
                  </div>
                </div>
                <ChevronDown
                  className="w-4 h-4"
                  style={{ color: "var(--theme-textMuted)" }}
                />
              </button>

              {showUserMenu && (
                <div
                  className="absolute right-0 mt-2 w-48 rounded-lg shadow-lg border py-1 z-50"
                  style={{
                    backgroundColor: "var(--theme-card)",
                    borderColor: "var(--theme-border)",
                    boxShadow: "var(--theme-shadow)",
                  }}
                >
                  <div
                    className="px-4 py-2 border-b"
                    style={{ borderBottomColor: "var(--theme-border)" }}
                  >
                    <div
                      className="text-sm font-medium"
                      style={{ color: "var(--theme-text)" }}
                    >
                      {user.name}
                    </div>
                    <div
                      className="text-xs"
                      style={{ color: "var(--theme-textSecondary)" }}
                    >
                      {user.email}
                    </div>
                  </div>
                  <button
                    className="w-full text-left px-4 py-2 text-sm flex items-center gap-2 transition-colors"
                    style={{ color: "var(--theme-text)" }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.backgroundColor =
                        "var(--theme-hover)";
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.backgroundColor = "transparent";
                    }}
                  >
                    <Settings className="w-4 h-4" />
                    {t("layout.header.settings")}
                  </button>
                  <button
                    className="w-full text-left px-4 py-2 text-sm flex items-center gap-2 transition-colors"
                    style={{ color: "var(--theme-text)" }}
                    onMouseEnter={(e) => {
                      e.currentTarget.style.backgroundColor =
                        "var(--theme-hover)";
                    }}
                    onMouseLeave={(e) => {
                      e.currentTarget.style.backgroundColor = "transparent";
                    }}
                  >
                    <Shield className="w-4 h-4" />
                    {t("layout.header.security")}
                  </button>
                  <div
                    className="border-t mt-1"
                    style={{ borderTopColor: "var(--theme-border)" }}
                  >
                    <button
                      onClick={() => {
                        setShowUserMenu(false);
                        setShowLogoutConfirm(true);
                      }}
                      className="w-full text-left px-4 py-2 text-sm flex items-center gap-2 transition-colors"
                      style={{ color: "var(--theme-error)" }}
                      onMouseEnter={(e) => {
                        e.currentTarget.style.backgroundColor =
                          "var(--theme-hover)";
                      }}
                      onMouseLeave={(e) => {
                        e.currentTarget.style.backgroundColor = "transparent";
                      }}
                    >
                      <LogOut className="w-4 h-4" />
                      {t("layout.header.logout")}
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </header>

      {showLogoutConfirm && (
        <div
          className="fixed inset-0 flex items-center justify-center z-50"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
        >
          <div
            className="rounded-lg p-6 w-full max-w-sm"
            style={{
              backgroundColor: "var(--theme-card)",
              border: "1px solid var(--theme-border)",
              boxShadow: "var(--theme-shadow)",
            }}
          >
            <div className="text-center">
              <LogOut
                className="w-12 h-12 mx-auto mb-4"
                style={{ color: "var(--theme-textMuted)" }}
              />
              <h3
                className="text-lg font-semibold mb-2"
                style={{ color: "var(--theme-text)" }}
              >
                {t("layout.header.logoutConfirm")}
              </h3>
              <p
                className="text-sm mb-6"
                style={{ color: "var(--theme-textSecondary)" }}
              >
                {t("layout.header.logoutMessage")}
              </p>
              <div className="flex gap-3">
                <button
                  onClick={() => setShowLogoutConfirm(false)}
                  className="flex-1 px-4 py-2 border rounded-lg transition-colors"
                  style={{
                    borderColor: "var(--theme-border)",
                    color: "var(--theme-text)",
                    backgroundColor: "transparent",
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.backgroundColor =
                      "var(--theme-hover)";
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.backgroundColor = "transparent";
                  }}
                >
                  {t("layout.header.cancel")}
                </button>
                <button
                  onClick={handleLogout}
                  className="flex-1 px-4 py-2 rounded-lg transition-colors"
                  style={{
                    backgroundColor: "var(--theme-error)",
                    color: "#ffffff",
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.opacity = "0.9";
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.opacity = "1";
                  }}
                >
                  {t("layout.header.logout")}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
