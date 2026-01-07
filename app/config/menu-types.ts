// Tipos y schemas para la administración de menús y páginas
// Estos tipos serán usados tanto en la interfaz como en la base de datos

export interface Menu {
  id: string;
  name: string; // "Code Playground"
  icon: string; // "Zap"
  description: string; // "Desarrollo de código y funcionalidades técnicas"
  isActive: boolean;
  order: number;
  roles: string[]; // ["developer", "devops"]
  createdAt: string;
  updatedAt: string;
}

export interface Page {
  id: string;
  menuId: string; // FK a Menu
  name: string; // "Code Editor"
  href: string; // "/code-playground"
  icon: string; // "Zap"
  roles: string[]; // ["admin", "developer", "devops"]
  isDefault: boolean;
  order: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface MenuWithPages extends Menu {
  pages: Page[];
}

// Tipos para la interfaz de administración
export interface MenuFormData {
  name: string;
  icon: string;
  description: string;
  roles: string[];
  order: number;
}

export interface PageFormData {
  menuId: string;
  name: string;
  href: string;
  icon: string;
  roles: string[];
  isDefault: boolean;
  order: number;
}

// Roles disponibles en el sistema
export const availableRoles = [
  // Roles de negocio (nuevos)
  "agencia",
  "it",
  "oem",
  "business_admin",
  // Roles técnicos (existentes)
  "admin",
  "developer",
  "viewer",
  "business_analytics",
  "ai_analytics",
  "ai_developer",
  "devops",
  "project_manager",
  "architect",
  "ai_architect",
];

// Iconos disponibles para menús y páginas
export const availableIcons = [
  // Iconos básicos
  "BarChart3",
  "Zap",
  "Brain",
  "Database",
  "Server",
  "Shield",
  "Target",
  "Layers",
  "Plug",
  "GraduationCap",
  "FileText",
  "Settings",
  "Code",
  "Bot",
  "Search",
  "Globe",
  "ShoppingCart",
  "Github",
  "Activity",
  "Users",
  "Cpu",
  "HardDrive",
  "Network",
  "Memory",
  "Eye",
  "Filter",
  "RefreshCw",
  "TrendingUp",
  "TrendingDown",
  "Gauge",
  "Clock",
  "Plus",
  "Edit",
  "Trash2",
  "CheckCircle",
  "AlertTriangle",
  "X",
  "Save",
  "Inbox",

  // Iconos de comunicación
  "MessageCircle",
  "MessageSquare",
  "Mail",
  "Phone",
  "Video",
  "Mic",
  "Headphones",
  "Speaker",
  "Volume2",
  "VolumeX",

  // Iconos de desarrollo
  "Terminal",
  "Command",
  "GitBranch",
  "GitCommit",
  "GitPullRequest",
  "GitMerge",
  "GitCompare",
  "GitBranchPlus",
  "GitPullRequestClosed",
  "Bug",
  "TestTube",
  "Beaker",
  "FlaskConical",

  // Iconos de diseño y UI
  "Palette",
  "Brush",
  "Droplets",
  "Image",
  "Camera",
  "Video",
  "Film",
  "Music",
  "Headphones",
  "Speaker",
  "Volume2",

  // Iconos de negocio
  "Building",
  "Briefcase",
  "CreditCard",
  "DollarSign",
  "Receipt",
  "Calculator",
  "PieChart",
  "BarChart",
  "LineChart",
  "TrendingUp",
  "TrendingDown",
  "Target",
  "Flag",
  "Award",
  "Trophy",

  // Iconos de seguridad
  "Lock",
  "Unlock",
  "Key",
  "Fingerprint",
  "Shield",
  "ShieldCheck",
  "ShieldX",
  "ShieldAlert",
  "ShieldOff",
  "ShieldQuestion",

  // Iconos de archivos y documentos
  "File",
  "FileText",
  "FileImage",
  "FileVideo",
  "FileAudio",
  "FileCode",
  "FileArchive",
  "FileSpreadsheet",
  "FilePresentation",
  "Folder",
  "FolderOpen",
  "FolderPlus",
  "FolderMinus",

  // Iconos de navegación
  "Home",
  "Map",
  "MapPin",
  "Navigation",
  "Compass",
  "Globe",
  "World",
  "Flag",
  "Location",
  "Route",

  // Iconos de tiempo y calendario
  "Calendar",
  "Clock",
  "Timer",
  "Stopwatch",
  "Hourglass",
  "Sun",
  "Moon",
  "Cloud",
  "CloudRain",
  "CloudLightning",

  // Iconos de herramientas
  "Wrench",
  "Screwdriver",
  "Hammer",
  "Drill",
  "Saw",
  "Tool",
  "Cog",
  "Settings",
  "Sliders",
  "Tune",

  // Iconos de transporte
  "Car",
  "Truck",
  "Bike",
  "Plane",
  "Ship",
  "Rocket",
  "Train",
  "Bus",
  "Subway",
  "Helicopter",

  // Iconos de naturaleza
  "Tree",
  "Leaf",
  "Flower",
  "Mountain",
  "Sun",
  "Moon",
  "Star",
  "Cloud",
  "Rainbow",
  "Fire",
  "Water",

  // Iconos de deportes
  "Trophy",
  "Medal",
  "Award",
  "Flag",
  "Target",
  "Bow",
  "Sword",
  "Shield",
  "Helmet",
  "Boot",

  // Iconos de tecnología avanzada
  "Cpu",
  "Memory",
  "HardDrive",
  "Network",
  "Wifi",
  "Bluetooth",
  "Satellite",
  "Radar",
  "Antenna",
  "Transmitter",
  "Receiver",

  // Iconos de IA y ML
  "Brain",
  "Neurons",
  "Circuit",
  "Chip",
  "Microchip",
  "Robot",
  "Bot",
  "Automation",
  "Smartphone",
  "Tablet",

  // Iconos de datos
  "Database",
  "Table",
  "Columns",
  "Rows",
  "Grid",
  "List",
  "TreeStructure",
  "Network",
  "Graph",
  "Chart",
  "Analytics",

  // Iconos de colaboración
  "Users",
  "User",
  "UserPlus",
  "UserMinus",
  "UserCheck",
  "Team",
  "Group",
  "Collaboration",
  "Handshake",
  "Partnership",

  // Iconos de notificaciones
  "Bell",
  "BellRing",
  "BellOff",
  "Notification",
  "Alert",
  "Warning",
  "Info",
  "Help",
  "Question",
  "Exclamation",

  // Iconos de estado
  "Check",
  "X",
  "Minus",
  "Plus",
  "Circle",
  "Square",
  "Triangle",
  "Diamond",
  "Heart",
  "Star",
  "ThumbsUp",
  "ThumbsDown",
];
