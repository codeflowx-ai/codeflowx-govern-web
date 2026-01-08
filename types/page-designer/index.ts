//types/page-designer/index.ts
export interface DesignerComponent {
    id: string;
    type: string;
    name: string;
    properties: Record<string, any>;
    position: { x: number; y: number };
    size: { width: number; height: number };
    children?: DesignerComponent[];
    validation?: ValidationRule[];
    events?: ComponentEvent[];
}

export interface ValidationRule {
    type: 'required' | 'pattern' | 'min' | 'max' | 'custom';
    value?: any;
    message: string;
}

export interface ComponentEvent {
    type: 'click' | 'change' | 'submit' | 'custom';
    action: string;
    parameters?: Record<string, any>;
}

export interface DesignerState {
    components: DesignerComponent[];
    selectedComponent: string | null;
    canvasSize: { width: number; height: number };
    zoom: number;
    gridEnabled: boolean;
}

export interface ComponentDefinition {
    type: string;
    name: string;
    icon: string;
    category: string;
    defaultProps: Record<string, any>;
    propertySchema: Record<string, any>;
    defaultSize: { width: number; height: number };
}

export interface DroppedComponent {
    id: string;
    type: string;
    name: string;
    x: number;
    y: number;
    width: number;
    height: number;
    properties: Record<string, any>;
    parentId?: string; // ID del componente padre
    children?: DroppedComponent[]; // Componentes hijos
    zIndex?: number;
    locked?: boolean;
}
export interface DropZoneInfo {
    componentId: string;
    zone: string;
    bounds: DOMRect;
    accepts: string[];
}
export interface HierarchyRule {
    acceptsChildren: boolean;
    allowedChildren: string[];
    maxChildren: number;
    dropZones: string[];
    autoCreateChildren?: boolean;
    childTemplate?: Partial<DroppedComponent>;
}
// ========== EXTENSIONES EMPRESARIALES ==========

export interface EnterpriseConfig {
    mode: 'standard' | 'enterprise-forms' | 'enterprise-dashboard';
    title: string;
    subtitle?: string;
    features: {
        gridLayout: boolean;
        databaseMapping: boolean;
        masterDetail: boolean;
        templates: boolean;
        export: string[];
        validation: boolean;
    };
    layout: {
        showTemplatePanel: boolean;
        showDatabasePanel: boolean;
        defaultGridColumns: number;
        snapToGrid: boolean;
    };
}

export interface EnterpriseData {
    toolkit?: any; // ComponentToolkit del JSON
    templates?: any[]; // FormTemplate[]
    entities?: any[]; // DatabaseEntity[]
}

export interface DatabaseField {
    id: string;
    name: string;
    type: string;
    required: boolean;
    defaultValue?: any;
}

export interface DatabaseEntity {
    id: string;
    name: string;
    table: string;
    fields: DatabaseField[];
}

// Extender DesignerState para modo empresarial
export interface EnterpriseDesignerState extends DesignerState {
    mode: 'standard' | 'enterprise-forms' | 'enterprise-dashboard';
    enterpriseConfig?: EnterpriseConfig;
    enterpriseData?: EnterpriseData;
    selectedEntity?: DatabaseEntity;
    gridColumns: number;
    snapToGrid: boolean;
    templates: any[];
}
