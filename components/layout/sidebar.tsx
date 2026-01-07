// components/layout/Sidebar.tsx - Refactor multi-rol y selector de módulo
"use client";

import { useTranslation } from "@/app/config/i18n";
import { getMenuByModule, getModuleByPath } from "@/app/config/modules";
import { cn } from "@/lib/utils";
import {
  Activity,
  AlertCircle,
  AlertTriangle,
  BarChart3,
  Bell,
  Ban,
  BookOpen,
  Bot,
  Brain,
  Building,
  Calendar,
  CheckCircle,
  ChevronLeft,
  ClipboardCheck,
  ClipboardList,
  Clock,
  Cloud,
  Code,
  Cpu,
  CreditCard,
  Database,
  DollarSign,
  FileCode,
  FileSignature,
  FileText,
  FlaskConical,
  FolderOpen,
  Gauge,
  GitBranch,
  GitCompare,
  Github,
  Globe,
  GraduationCap,
  Grid,
  Hash,
  Heart,
  Image,
  Inbox,
  Key,
  Layers,
  Lightbulb,
  Lock,
  Map,
  Menu as MenuIcon,
  MessageCircle,
  MessageSquare,
  Mic,
  Network,
  Package,
  Palette,
  Play,
  Plug,
  Receipt,
  Rocket,
  RotateCcw,
  Search,
  Server,
  Settings,
  Shield,
  ShoppingCart,
  Sparkles,
  Tag,
  Target,
  TestTube,
  TrendingUp,
  Upload,
  UserCheck,
  Users,
  Workflow,
  Wrench,
  Zap,
} from "lucide-react";
import { usePathname } from "next/navigation";
import { useEffect, useState } from "react";
import { createPortal } from "react-dom";

// Función helper para obtener el componente de icono
const getIconComponent = (iconName: string) => {
  const iconComponents: Record<
    string,
    React.ComponentType<{ className?: string }>
  > = {
    BarChart3,
    Zap,
    Plug,
    GraduationCap,
    BookOpen,
    TrendingUp,
    Settings,
    FolderOpen,
    FileText,
    MessageSquare,
    Bot,
    Building,
    Palette,
    Workflow,
    Brain,
    ShoppingCart,
    Target,
    Package,
    GitBranch,
    Users,
    CreditCard,
    Receipt,
    Rocket,
    Sparkles,
    Map,
    ClipboardList,
    ClipboardCheck,
    Lightbulb,
    Shield,
    Search,
    Hash,
    Lock,
    Bell,
    MessageCircle,
    Image,
    Database,
    Mic,
    Layers,
    Grid,
    Server,
    DollarSign,
    Activity,
    Globe,
    Cloud,
    CheckCircle,
    AlertTriangle,
    AlertCircle,
    TestTube,
    Code,
    Github,
    Upload,
    Network,
    Key,
    Gauge,
    Clock,
    Cpu,
    Heart,
    Inbox,
    Calendar,
    RotateCcw,
    MenuIcon,
    Tag,
    FileCode,
    FileSignature,
    UserCheck,
    Ban,
    FlaskConical,
    Play,
    GitCompare,
    Wrench,
  };

  return iconComponents[iconName] || BarChart3;
};

interface MenuItem {
  name: string;
  href?: string;
  icon: React.ComponentType<{ className?: string }>;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

export function Sidebar() {
  const { t, language } = useTranslation();
  const [collapsed, setCollapsed] = useState(false);
  const [activeModule, setActiveModule] = useState<string>("Dashboard");
  const [hoveredItem, setHoveredItem] = useState<string | null>(null);
  const [tooltipPosition, setTooltipPosition] = useState<{ top: number; left: number } | null>(null);
  const pathname = usePathname();

  // Obtener usuario del localStorage
  const getUserData = () => {
    try {
      const userData = localStorage.getItem("user");
      return userData ? JSON.parse(userData) : null;
    } catch (error) {
      console.error("Error parsing user data:", error);
      return null;
    }
  };

  const user = getUserData();
  const userRoles: string[] =
    user?.roles || (user?.role ? [user.role] : ["admin", "developer"]);

  // Función para detectar módulo desde URL
  const detectModuleFromURL = () => {
    const currentModule = getModuleByPath(pathname);
    if (currentModule && currentModule !== activeModule) {
      setActiveModule(currentModule);
      localStorage.setItem("activeModule", currentModule);
    }
  };

  // Escuchar eventos del header
  useEffect(() => {
    // Detectar módulo inicial desde URL
    detectModuleFromURL();

    // Escuchar eventos del header
    const handleActiveModuleChanged = (event: CustomEvent) => {
      const newModule = event.detail?.moduleName;
      if (newModule) {
        setActiveModule(newModule);
      }
    };

    window.addEventListener(
      "activeModuleChanged",
      handleActiveModuleChanged as EventListener
    );
    return () => {
      window.removeEventListener(
        "activeModuleChanged",
        handleActiveModuleChanged as EventListener
      );
    };
  }, []); // Sin dependencias

  // Detectar módulo cuando cambie la URL
  useEffect(() => {
    detectModuleFromURL();
  }, [pathname]);

  // Obtener menú del módulo activo
  const projectMenu = getMenuByModule(activeModule);

  // Convertir iconos de string a componentes
  const convertMenuIcons = (items: any[]): MenuItem[] => {
    return items.map((item) => ({
      ...item,
      icon: getIconComponent(item.icon),
      children: item.children ? convertMenuIcons(item.children) : undefined,
    }));
  };

  const convertedMenu = convertMenuIcons(projectMenu);

  // Filtrar menú por roles (simplificado)
  const filterMenuByRoles = (items: MenuItem[]): MenuItem[] => {
    if (!userRoles.length) return items;

    return items.filter((item) => {
      const hasAccess = item.roles.some((role) => userRoles.includes(role));
      // Debug para Compliance menu
      if (item.name.includes("Compliance") || item.name.includes("FRIA") || item.name.includes("Classification")) {
        console.log("🔍 Compliance Menu Item:", {
          name: item.name,
          itemRoles: item.roles,
          userRoles: userRoles,
          hasAccess: hasAccess
        });
      }
      return hasAccess;
    });
  };

  const filteredModules = filterMenuByRoles(convertedMenu);

  // Renderizar item del menú
  const renderMenuItem = (item: MenuItem) => {
    const IconComponent = item.icon;
    const isActive = pathname === item.href;
    // Traducir el nombre del menú
    const translatedName = t(`layout.sidebar.menuItems.${item.name}`, item.name);
    const isHovered = hoveredItem === item.name;

    return (
      <button
        key={item.name}
        onClick={() => {
          if (item.href) {
            window.location.href = item.href;
          }
        }}
        className={cn(
          "w-full flex items-center gap-3 rounded-lg px-3 py-2 transition-all relative",
          isActive ? "font-medium" : "opacity-80 hover:opacity-100",
          collapsed ? "justify-center" : ""
        )}
        style={{
          backgroundColor: isActive
            ? "var(--theme-sidebar-active)"
            : "transparent",
          color: isActive
            ? "var(--theme-primary)"
            : "var(--theme-sidebar-text)",
        }}
        onMouseEnter={(e) => {
          if (!isActive) {
            e.currentTarget.style.backgroundColor =
              "var(--theme-sidebar-hover)";
          }
          if (collapsed) {
            const rect = e.currentTarget.getBoundingClientRect();
            setHoveredItem(item.name);
            setTooltipPosition({
              top: rect.top + rect.height / 2,
              left: rect.right + 12,
            });
          }
        }}
        onMouseLeave={(e) => {
          if (!isActive) {
            e.currentTarget.style.backgroundColor = "transparent";
          }
          if (collapsed) {
            setHoveredItem(null);
            setTooltipPosition(null);
          }
        }}
      >
        <IconComponent
          className="w-5 h-5 flex-shrink-0"
        />
        {!collapsed && <span className="truncate">{translatedName}</span>}
      </button>
    );
  };

  return (
    <>
      <aside
        className={cn(
          "h-screen flex flex-col transition-all duration-300 relative",
          collapsed ? "w-20" : "w-64"
        )}
        style={{
          backgroundColor: "var(--theme-sidebar-background)",
          overflow: "visible",
        }}
      >
      {/* Branding y botón de contraer/expandir */}
      <div
        className={cn(
          "p-4 flex items-center gap-2 relative",
          collapsed ? "justify-center" : ""
        )}
        style={{
          backgroundColor: "var(--theme-sidebar-background)",
        }}
      >
        <div className={cn("flex items-center", collapsed ? "justify-center w-full" : "space-x-3")}>
          <button
            onClick={() => collapsed && setCollapsed(false)}
            className={cn(
              "flex-shrink-0 transition-opacity",
              collapsed ? "cursor-pointer hover:opacity-80" : "cursor-default"
            )}
            title={collapsed ? t("layout.sidebar.expand", "Expandir sidebar") : undefined}
          >
            <img
              src="/img/icono1.png"
              alt="CodeflowX Icon"
              className={cn("flex-shrink-0", collapsed ? "w-8 h-8" : "w-10 h-10")}
            />
          </button>
          {/* Logo Corporativo CodeflowX con Múltiples Colores */}
          {!collapsed && (
            <div className="flex items-center">
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
                Code
              </span>
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
                flow
              </span>
              <span
                className="text-xl font-bold select-none bg-gradient-to-r from-orange-500 via-amber-500 to-yellow-500 bg-clip-text text-transparent"
                style={{
                  background:
                    "linear-gradient(135deg, var(--theme-warning) 0%, #f59e0b 50%, #eab308 100%)",
                  WebkitBackgroundClip: "text",
                  WebkitTextFillColor: "transparent",
                  backgroundClip: "text",
                }}
              >
                X
              </span>
            </div>
          )}
        </div>
        {!collapsed && (
          <button
            onClick={() => setCollapsed(!collapsed)}
            className="ml-auto p-1 rounded-md hover:bg-opacity-80 transition-all"
            style={{
              backgroundColor: "var(--theme-sidebar-hover)",
              color: "var(--theme-sidebar-text)",
            }}
          >
            <ChevronLeft
              className={cn(
                "h-4 w-4 transition-transform",
                collapsed && "rotate-180"
              )}
            />
          </button>
        )}
      </div>

      {/* Encabezado del módulo activo */}
      {activeModule && (
        <div
          className="flex items-center gap-2 px-4 py-3 border-b"
          style={{
            backgroundColor: "transparent",
            borderBottomColor: "var(--theme-sidebar-border)",
          }}
        >
          <BarChart3 className="w-5 h-5" style={{ color: "var(--theme-sidebar-text)" }} />
          {!collapsed && (
            <span
              className="text-base font-semibold truncate"
              style={{ color: "var(--theme-sidebar-text)" }}
            >
              {t("layout.sidebar.module")}: {activeModule}
            </span>
          )}
        </div>
      )}

      {/* Menú de módulos */}
      <div className="flex-1 overflow-y-auto overflow-x-visible p-4 space-y-2">
        {filteredModules.map((item) => renderMenuItem(item))}
      </div>

      {/* Botón de expandir cuando está colapsado */}
      {collapsed && (
        <div className="p-4 border-t" style={{ borderTopColor: "var(--theme-sidebar-border)" }}>
          <button
            onClick={() => setCollapsed(false)}
            className="w-full p-2 rounded-md hover:bg-opacity-80 transition-all flex items-center justify-center group"
            style={{
              backgroundColor: "var(--theme-sidebar-hover)",
              color: "var(--theme-sidebar-text)",
            }}
            title={t("layout.sidebar.expand", "Expandir sidebar")}
            onMouseEnter={(e) => {
              e.currentTarget.style.backgroundColor = "var(--theme-sidebar-active)";
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.backgroundColor = "var(--theme-sidebar-hover)";
            }}
          >
            <ChevronLeft
              className="h-5 w-5 transition-transform rotate-180 group-hover:scale-110"
            />
          </button>
        </div>
      )}

      </aside>
      {/* Tooltip flotante para items cuando está colapsado - renderizado fuera del aside */}
      {collapsed && hoveredItem && tooltipPosition && typeof window !== "undefined"
        ? createPortal(
            <div
              className="fixed px-3 py-2 rounded-lg text-sm whitespace-nowrap z-[9999] pointer-events-none shadow-lg"
              style={{
                top: `${tooltipPosition.top}px`,
                left: `${tooltipPosition.left}px`,
                transform: "translateY(-50%)",
                backgroundColor: "var(--theme-popover)",
                color: "var(--theme-text)",
                border: "1px solid var(--theme-border)",
                boxShadow: "var(--theme-shadow)",
              }}
            >
              {t(`layout.sidebar.menuItems.${hoveredItem}`, hoveredItem)}
              <div
                className="absolute right-full top-1/2 -translate-y-1/2 w-2 h-2 rotate-45"
                style={{
                  backgroundColor: "var(--theme-popover)",
                  borderRight: "1px solid var(--theme-border)",
                  borderBottom: "1px solid var(--theme-border)",
                }}
              />
            </div>,
            document.body
          )
        : null}
    </>
  );
}
