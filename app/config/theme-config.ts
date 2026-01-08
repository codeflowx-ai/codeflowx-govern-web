// Configuración completa de temas para CodeflowX
// Solo temas Trekker - Sistema unificado y moderno

export interface ThemeConfig {
  id: string;
  name: string;
  description: string;
  colors: {
    // Colores principales
    primary: string;
    secondary: string;
    accent: string;

    // Colores de fondo
    background: string;
    surface: string;
    card: string;
    popover: string;

    // Colores específicos para header y sidebar
    header: {
      background: string;
      text: string;
      border: string;
      hover: string;
    };
    sidebar: {
      background: string;
      text: string;
      border: string;
      hover: string;
      active: string;
    };
    footer: {
      background: string;
      text: string;
      border: string;
    };

    // Colores de texto
    text: string;
    textSecondary: string;
    textMuted: string;

    // Colores de bordes
    border: string;
    borderLight: string;

    // Colores de estado
    success: string;
    warning: string;
    error: string;
    info: string;

    // Colores de hover
    hover: string;
    hoverLight: string;

    // Colores de sombras
    shadow: string;
    shadowLight: string;

    // Efectos especiales Trekker
    glow: string;
  };
}

export const themes: ThemeConfig[] = [
  // TEMAS TREKKER - Sistema unificado
  {
    id: "trekker-federation",
    name: "🌌 Leka Federation",
    description: "Tema oficial de la Federación Unida de Planetas",
    colors: {
      primary: "#1e40af", // Azul profundo de la Federación
      secondary: "#3b82f6", // Azul medio
      accent: "#8b5cf6", // Púrpura para destacar
      background: "#0f172a", // Azul muy oscuro
      surface: "#1e293b", // Azul oscuro
      card: "#334155", // Azul grisáceo
      popover: "#475569", // Azul grisáceo claro
      header: {
        background: "#000000", // Negro
        text: "#f8fafc", // Blanco puro
        border: "#475569", // Azul grisáceo
        hover: "#1a1a1a", // Negro ligeramente más claro para hover
      },
      sidebar: {
        background: "#000000", // Negro
        text: "#cbd5e1", // Gris azulado claro
        border: "#334155", // Azul grisáceo
        hover: "#1a1a1a", // Negro ligeramente más claro para hover
        active: "#3b82f6", // Azul medio
      },
      footer: {
        background: "#000000", // Negro
        text: "#cbd5e1", // Gris azulado claro
        border: "#334155", // Azul grisáceo
      },
      text: "#f8fafc", // Blanco puro
      textSecondary: "#cbd5e1", // Gris azulado claro
      textMuted: "#94a3b8", // Gris azulado medio
      border: "#475569", // Azul grisáceo
      borderLight: "#64748b", // Azul grisáceo claro
      success: "#10b981", // Verde
      warning: "#f59e0b", // Naranja
      error: "#ef4444", // Rojo
      info: "#3b82f6", // Azul
      hover: "#334155", // Azul grisáceo oscuro
      hoverLight: "#475569", // Azul grisáceo
      shadow: "0 10px 15px -3px rgba(30, 64, 175, 0.3)", // Sombra azul
      shadowLight: "0 4px 6px -1px rgba(30, 64, 175, 0.2)",
      glow: "0 0 20px rgba(59, 130, 246, 0.3)", // Brillo azul
    },
  },
  {
    id: "trekker-earth",
    name: "🌍 Leka Earth",
    description: "Tema terroso con blancos y tonos naturales",
    colors: {
      primary: "#8b7355", // Marrón tierra
      secondary: "#a68b5b", // Beige oscuro
      accent: "#6b5d4f", // Marrón medio
      background: "#faf8f5", // Blanco sucio muy claro
      surface: "#f5f1eb", // Blanco sucio claro
      card: "#f0ebe3", // Blanco sucio medio
      popover: "#ebe5dc", // Blanco sucio más oscuro
      header: {
        background: "#000000", // Negro para mejor contraste
        text: "#f5f1eb", // Blanco sucio
        border: "#d4c4b0", // Beige medio
        hover: "#1a1815", // Negro con toque beige
      },
      sidebar: {
        background: "#000000", // Negro para mejor contraste
        text: "#e8ddd0", // Blanco sucio claro
        border: "#d4c4b0", // Beige medio
        hover: "#1a1815", // Negro con toque beige
        active: "#8b7355", // Marrón tierra
      },
      footer: {
        background: "#000000", // Negro para mejor contraste
        text: "#e8ddd0", // Blanco sucio claro
        border: "#d4c4b0", // Beige medio
      },
      text: "#2c2416", // Marrón muy oscuro (casi negro)
      textSecondary: "#4a3e2e", // Marrón oscuro
      textMuted: "#6b5d4f", // Marrón medio
      border: "#d4c4b0", // Beige medio
      borderLight: "#e8ddd0", // Blanco sucio claro
      success: "#6b8e23", // Verde oliva
      warning: "#d4a574", // Beige cálido
      error: "#c97d60", // Terracota
      info: "#8b7355", // Marrón tierra
      hover: "#f0ebe3", // Blanco sucio medio
      hoverLight: "#f5f1eb", // Blanco sucio claro
      shadow: "0 10px 15px -3px rgba(139, 115, 85, 0.15)", // Sombra suave marrón
      shadowLight: "0 4px 6px -1px rgba(139, 115, 85, 0.1)",
      glow: "0 0 20px rgba(139, 115, 85, 0.2)", // Brillo suave marrón
    },
  },
  {
    id: "trekker-vulcan",
    name: "🖖 Leka Vulcan",
    description: "Tema lógico y eficiente inspirado en Vulcano",
    colors: {
      primary: "#047857", // Verde más oscuro y menos brillante
      secondary: "#059669", // Verde medio
      accent: "#d97706", // Cobre para destacar
      background: "#064e3b", // Verde oscuro
      surface: "#065f46", // Verde medio oscuro
      card: "#047857", // Verde medio
      popover: "#059669", // Verde medio claro
      header: {
        background: "#000000", // Negro para mejor contraste
        text: "#d1fae5", // Verde claro
        border: "#022c22", // Verde muy oscuro
        hover: "#0a1f1a", // Negro con toque verde muy sutil
      },
      sidebar: {
        background: "#000000", // Negro para mejor contraste
        text: "#a7f3d0", // Verde medio claro
        border: "#022c22", // Verde muy oscuro
        hover: "#0a1f1a", // Negro con toque verde muy sutil
        active: "#047857", // Verde medio
      },
      footer: {
        background: "#000000", // Negro para mejor contraste
        text: "#a7f3d0", // Verde medio claro
        border: "#022c22", // Verde muy oscuro
      },
      text: "#ecfdf5", // Verde muy claro
      textSecondary: "#d1fae5", // Verde claro
      textMuted: "#86efac", // Verde medio claro, menos saturado
      border: "#064e3b", // Verde oscuro
      borderLight: "#047857", // Verde medio
      success: "#16a34a", // Verde
      warning: "#ca8a04", // Amarillo
      error: "#dc2626", // Rojo
      info: "#2563eb", // Azul
      hover: "#064e3b", // Verde oscuro
      hoverLight: "#047857", // Verde medio
      shadow: "0 10px 15px -3px rgba(4, 120, 87, 0.15)", // Sombra verde más suave
      shadowLight: "0 4px 6px -1px rgba(4, 120, 87, 0.1)",
      glow: "0 0 20px rgba(4, 120, 87, 0.2)", // Brillo verde más suave
    },
  },
  {
    id: "trekker-klingon",
    name: "⚔️ Leka Klingon",
    description: "Tema guerrero y poderoso para batallas épicas",
    colors: {
      primary: "#dc2626", // Rojo sangre de Klingon
      secondary: "#f59e0b", // Dorado de batalla
      accent: "#7c2d12", // Marrón rojizo
      background: "#450a0a", // Rojo muy oscuro
      surface: "#7f1d1d", // Rojo oscuro
      card: "#991b1b", // Rojo medio oscuro
      popover: "#b91c1c", // Rojo medio
      header: {
        background: "#2a0505", // Rojo muy oscuro de la paleta
        text: "#fef2f2", // Rojo muy claro
        border: "#450a0a", // Rojo muy oscuro
        hover: "#450a0a",
      },
      sidebar: {
        background: "#2a0505", // Rojo muy oscuro de la paleta
        text: "#fecaca", // Rojo claro
        border: "#450a0a", // Rojo muy oscuro
        hover: "#450a0a",
        active: "#dc2626", // Rojo sangre
      },
      footer: {
        background: "#2a0505", // Rojo muy oscuro de la paleta
        text: "#fecaca", // Rojo claro
        border: "#450a0a", // Rojo muy oscuro
      },
      text: "#fef2f2", // Rojo muy claro
      textSecondary: "#fecaca", // Rojo claro
      textMuted: "#fca5a5", // Rojo medio claro
      border: "#991b1b", // Rojo medio oscuro
      borderLight: "#b91c1c", // Rojo medio
      success: "#16a34a", // Verde
      warning: "#f59e0b", // Dorado
      error: "#dc2626", // Rojo
      info: "#2563eb", // Azul
      hover: "#991b1b", // Rojo medio oscuro
      hoverLight: "#b91c1c", // Rojo medio
      shadow: "0 10px 15px -3px rgba(220, 38, 38, 0.3)", // Sombra roja
      shadowLight: "0 4px 6px -1px rgba(220, 38, 38, 0.2)",
      glow: "0 0 20px rgba(220, 38, 38, 0.4)", // Brillo rojo intenso
    },
  },
  {
    id: "trekker-borg",
    name: "🔮 Leka Borg",
    description: "Tema colectivo y tecnológico para asimilación",
    colors: {
      primary: "#059669",
      secondary: "#10b981",
      accent: "#06b6d4",
      background: "#064e3b",
      surface: "#065f46",
      card: "#047857",
      popover: "#059669",
      header: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#d1fae5",
        border: "#064e3b",
        hover: "#064e3b",
      },
      sidebar: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#a7f3d0",
        border: "#064e3b",
        hover: "#064e3b",
        active: "#059669",
      },
      footer: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#a7f3d0",
        border: "#064e3b",
      },
      text: "#ecfdf5",
      textSecondary: "#d1fae5",
      textMuted: "#a7f3d0",
      border: "#047857",
      borderLight: "#059669",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#047857",
      hoverLight: "#059669",
      shadow: "0 10px 15px -3px rgba(5, 150, 105, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(5, 150, 105, 0.2)",
      glow: "0 0 20px rgba(5, 150, 105, 0.4)",
    },
  },
  {
    id: "trekker-romulan",
    name: "🕵️ Leka Romulan",
    description: "Tema misterioso y estratégico para operaciones encubiertas",
    colors: {
      primary: "#7c3aed",
      secondary: "#8b5cf6",
      accent: "#a855f7",
      background: "#1e1b4b",
      surface: "#312e81",
      card: "#4338ca",
      popover: "#4f46e5",
      header: {
        background: "#0f0a1f", // Púrpura muy oscuro de la paleta
        text: "#e9d5ff",
        border: "#1e1b4b",
        hover: "#1e1b4b",
      },
      sidebar: {
        background: "#0f0a1f", // Púrpura muy oscuro de la paleta
        text: "#c4b5fd",
        border: "#1e1b4b",
        hover: "#1e1b4b",
        active: "#7c3aed",
      },
      footer: {
        background: "#0f0a1f", // Púrpura muy oscuro de la paleta
        text: "#c4b5fd",
        border: "#1e1b4b",
      },
      text: "#faf5ff",
      textSecondary: "#e9d5ff",
      textMuted: "#c4b5fd",
      border: "#4338ca",
      borderLight: "#4f46e5",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#4338ca",
      hoverLight: "#4f46e5",
      shadow: "0 10px 15px -3px rgba(124, 58, 237, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(124, 58, 237, 0.2)",
      glow: "0 0 20px rgba(124, 58, 237, 0.4)",
    },
  },
  {
    id: "trekker-cardassian",
    name: "🏛️ Leka Cardassian",
    description: "Tema autoritario y estructurado para el orden",
    colors: {
      primary: "#b45309",
      secondary: "#d97706",
      accent: "#f59e0b",
      background: "#581c03",
      surface: "#7c2d12",
      card: "#92400e",
      popover: "#b45309",
      header: {
        background: "#000000", // Negro para mejor contraste
        text: "#fde68a",
        border: "#2a1500",
        hover: "#1a0f00",
      },
      sidebar: {
        background: "#000000", // Negro para mejor contraste
        text: "#fbbf24",
        border: "#2a1500",
        hover: "#1a0f00",
        active: "#b45309",
      },
      footer: {
        background: "#000000", // Negro para mejor contraste
        text: "#fbbf24",
        border: "#2a1500",
      },
      text: "#fef3c7",
      textSecondary: "#fde68a",
      textMuted: "#fbbf24",
      border: "#7c2d12",
      borderLight: "#92400e",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#7c2d12",
      hoverLight: "#92400e",
      shadow: "0 10px 15px -3px rgba(180, 83, 9, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(180, 83, 9, 0.2)",
      glow: "0 0 20px rgba(180, 83, 9, 0.4)",
    },
  },
  {
    id: "trekker-ferengi",
    name: "💰 Leka Ferengi",
    description: "Tema comercial y lucrativo para negocios",
    colors: {
      primary: "#ca8a04",
      secondary: "#eab308",
      accent: "#facc15",
      background: "#422006",
      surface: "#451a03",
      card: "#581c03",
      popover: "#7c2d12",
      header: {
        background: "#2a1800", // Dorado muy oscuro de la paleta
        text: "#fde68a",
        border: "#422006",
        hover: "#422006",
      },
      sidebar: {
        background: "#2a1800", // Dorado muy oscuro de la paleta
        text: "#fbbf24",
        border: "#422006",
        hover: "#422006",
        active: "#ca8a04",
      },
      footer: {
        background: "#2a1800", // Dorado muy oscuro de la paleta
        text: "#fbbf24",
        border: "#422006",
      },
      text: "#fef3c7",
      textSecondary: "#fde68a",
      textMuted: "#fbbf24",
      border: "#581c03",
      borderLight: "#7c2d12",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#581c03",
      hoverLight: "#7c2d12",
      shadow: "0 10px 15px -3px rgba(202, 138, 4, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(202, 138, 4, 0.2)",
      glow: "0 0 20px rgba(202, 138, 4, 0.4)",
    },
  },
  {
    id: "trekker-betazoid",
    name: "🧠 Leka Betazoid",
    description: "Tema empático y psíquico para la telepatía",
    colors: {
      primary: "#ec4899",
      secondary: "#f472b6",
      accent: "#f9a8d4",
      background: "#4c1d95",
      surface: "#5b21b6",
      card: "#6d28d9",
      popover: "#7c3aed",
      header: {
        background: "#1a0a2e", // Púrpura rosa muy oscuro de la paleta
        text: "#fce7f3",
        border: "#4c1d95",
        hover: "#4c1d95",
      },
      sidebar: {
        background: "#1a0a2e", // Púrpura rosa muy oscuro de la paleta
        text: "#fbcfe8",
        border: "#4c1d95",
        hover: "#4c1d95",
        active: "#ec4899",
      },
      footer: {
        background: "#1a0a2e", // Púrpura rosa muy oscuro de la paleta
        text: "#fbcfe8",
        border: "#4c1d95",
      },
      text: "#fdf2f8",
      textSecondary: "#fce7f3",
      textMuted: "#fbcfe8",
      border: "#6d28d9",
      borderLight: "#7c3aed",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#6d28d9",
      hoverLight: "#7c3aed",
      shadow: "0 10px 15px -3px rgba(236, 72, 153, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(236, 72, 153, 0.2)",
      glow: "0 0 20px rgba(236, 72, 153, 0.4)",
    },
  },
  {
    id: "trekker-trill",
    name: "🔄 Leka Trill",
    description: "Tema simbiótico y dual para experiencias compartidas",
    colors: {
      primary: "#0891b2",
      secondary: "#0e7490",
      accent: "#155e75",
      background: "#0e7490",
      surface: "#0891b2",
      card: "#06b6d4",
      popover: "#22d3ee",
      header: {
        background: "#000000", // Negro para mejor contraste
        text: "#ccfbf1",
        border: "#051a24",
        hover: "#051a24",
      },
      sidebar: {
        background: "#000000", // Negro para mejor contraste
        text: "#99f6e4",
        border: "#051a24",
        hover: "#051a24",
        active: "#0891b2",
      },
      footer: {
        background: "#000000", // Negro para mejor contraste
        text: "#99f6e4",
        border: "#051a24",
      },
      text: "#f0fdfa",
      textSecondary: "#ccfbf1",
      textMuted: "#99f6e4",
      border: "#0891b2",
      borderLight: "#06b6d4",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#0891b2",
      hoverLight: "#06b6d4",
      shadow: "0 10px 15px -3px rgba(8, 145, 178, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(8, 145, 178, 0.2)",
      glow: "0 0 20px rgba(8, 145, 178, 0.4)",
    },
  },
  {
    id: "trekker-andorian",
    name: "❄️ Leka Andorian",
    description: "Tema helado y noble para guerreros del hielo",
    colors: {
      primary: "#0ea5e9",
      secondary: "#0284c7",
      accent: "#0369a1",
      background: "#0c4a6e",
      surface: "#075985",
      card: "#0e7490",
      popover: "#0891b2",
      header: {
        background: "#051a24", // Azul muy oscuro de la paleta
        text: "#e0f2fe",
        border: "#0c4a6e",
        hover: "#0c4a6e",
      },
      sidebar: {
        background: "#051a24", // Azul muy oscuro de la paleta
        text: "#bae6fd",
        border: "#0c4a6e",
        hover: "#0c4a6e",
        active: "#0ea5e9",
      },
      footer: {
        background: "#051a24", // Azul muy oscuro de la paleta
        text: "#bae6fd",
        border: "#0c4a6e",
      },
      text: "#f0f9ff",
      textSecondary: "#e0f2fe",
      textMuted: "#bae6fd",
      border: "#0e7490",
      borderLight: "#0891b2",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#0e7490",
      hoverLight: "#0891b2",
      shadow: "0 10px 15px -3px rgba(14, 165, 233, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(14, 165, 233, 0.2)",
      glow: "0 0 20px rgba(14, 165, 233, 0.4)",
    },
  },
  {
    id: "trekker-tellarite",
    name: "🏗️ Leka Tellarite",
    description: "Tema robusto y práctico para ingenieros",
    colors: {
      primary: "#a16207",
      secondary: "#b45309",
      accent: "#ca8a04",
      background: "#451a03",
      surface: "#581c03",
      card: "#7c2d12",
      popover: "#92400e",
      header: {
        background: "#2a1500", // Marrón muy oscuro de la paleta
        text: "#fde68a",
        border: "#451a03",
        hover: "#451a03",
      },
      sidebar: {
        background: "#2a1500", // Marrón muy oscuro de la paleta
        text: "#fbbf24",
        border: "#451a03",
        hover: "#451a03",
        active: "#a16207",
      },
      footer: {
        background: "#2a1500", // Marrón muy oscuro de la paleta
        text: "#fbbf24",
        border: "#451a03",
      },
      text: "#fef3c7",
      textSecondary: "#fde68a",
      textMuted: "#fbbf24",
      border: "#7c2d12",
      borderLight: "#92400e",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#7c2d12",
      hoverLight: "#92400e",
      shadow: "0 10px 15px -3px rgba(161, 98, 7, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(161, 98, 7, 0.2)",
      glow: "0 0 20px rgba(161, 98, 7, 0.4)",
    },
  },
  {
    id: "trekker-denobulan",
    name: "🏥 Leka Denobulan",
    description: "Tema médico y científico para la investigación",
    colors: {
      primary: "#059669",
      secondary: "#047857",
      accent: "#065f46",
      background: "#064e3b",
      surface: "#065f46",
      card: "#047857",
      popover: "#059669",
      header: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#d1fae5",
        border: "#064e3b",
        hover: "#064e3b",
      },
      sidebar: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#a7f3d0",
        border: "#064e3b",
        hover: "#064e3b",
        active: "#059669",
      },
      footer: {
        background: "#022c22", // Verde muy oscuro de la paleta
        text: "#a7f3d0",
        border: "#064e3b",
      },
      text: "#ecfdf5",
      textSecondary: "#d1fae5",
      textMuted: "#a7f3d0",
      border: "#047857",
      borderLight: "#059669",
      success: "#16a34a",
      warning: "#ca8a04",
      error: "#dc2626",
      info: "#2563eb",
      hover: "#047857",
      hoverLight: "#059669",
      shadow: "0 10px 15px -3px rgba(5, 150, 105, 0.3)",
      shadowLight: "0 4px 6px -1px rgba(5, 150, 105, 0.2)",
      glow: "0 0 20px rgba(5, 150, 105, 0.4)",
    },
  },
];

// Función para aplicar tema
export function applyTheme(themeId: string) {
  const theme = themes.find((t) => t.id === themeId);
  if (!theme) return;

  const root = document.documentElement;

  // Limpiar clases de tema anteriores
  root.className = root.className.replace(/trekker-\w+/g, "");

  // Aplicar nueva clase de tema
  root.className += ` ${themeId}`;

  // Aplicar variables CSS del tema
  Object.entries(theme.colors).forEach(([key, value]) => {
    if (typeof value === "string" && value.startsWith("#")) {
      root.style.setProperty(`--theme-${key}`, value);
    } else if (typeof value === "object" && value !== null) {
      // Manejar objetos anidados (header, sidebar, footer)
      Object.entries(value).forEach(([nestedKey, nestedValue]) => {
        if (typeof nestedValue === "string" && nestedValue.startsWith("#")) {
          root.style.setProperty(`--theme-${key}-${nestedKey}`, nestedValue);
        }
      });
    }
  });
}
