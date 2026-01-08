interface TechnologyConfig {
    technology: {
        id: string;
        name: string;
        version: string;
        description: string;
    };
    hierarchy?: {
        rules?: Record<string, any>;
    };
    categories: ComponentCategory[];
    components: Record<string, ComponentDefinition>;
    propertyTypes: Record<string, PropertyTypeDefinition>;
    dynamicSources: Record<string, DynamicSource>;
    exporters: Record<string, Exporter>;
}

interface ComponentCategory {
    id: string;
    name: string;
    icon: string;
    order: number;
    components: string[];
}

interface ComponentDefinition {
    definition: {
        name: string;
        type: string;
        sqlType: string[];
        icon: string;
        tooltip: string;
        class: string;
        component: string;
        defaultSize: { width: number; height: number };
        acceptsChildren?: boolean;
    };
    propertyTabs: PropertyTab[];
    properties: PropertyDefinition[];
}

interface PropertyTab {
    id: string;
    name: string;
    icon: string;
    order: number;
}

interface PropertyDefinition {
    name: string;
    type: string;
    label: string;
    required?: boolean;
    default: any;
    options?: Array<{value: any; label: string}>;
    dynamicOptions?: {
        source: string;
        filter?: string;
    };
    range?: { min: number; max: number };
    min?: number;
    max?: number;
    step?: number;
    placeholder?: string;
    order: number;
    group: string;
    tab: string;
    screen?: boolean;
    render?: boolean;
    style?: boolean;
}

interface PropertyTypeDefinition {
    component: string;
    validation: string[];
    requiresOptions?: boolean;
}

interface DynamicSource {
    endpoint: string;
    method: string;
    cache: boolean;
    filters?: string[];
    mockData?: Array<{value: any; label: string}>;
}

interface Exporter {
    name: string;
    extension: string;
    templateEngine: string;
}

class ConfigLoader {
    private config: TechnologyConfig | null = null;
    private static instance: ConfigLoader;

    static getInstance(): ConfigLoader {
        if (!ConfigLoader.instance) {
            ConfigLoader.instance = new ConfigLoader();
        }
        return ConfigLoader.instance;
    }

    async loadConfig(configPath: string = '/config/toolkit.json'): Promise<TechnologyConfig> {
        try {
            const response = await fetch(configPath);
            if (!response.ok) {
                throw new Error(`Failed to load config: ${response.statusText}`);
            }
            const loadedConfig = (await response.json()) as TechnologyConfig;
            this.config = loadedConfig;
            console.log('Config loaded successfully:', this.config);
            return loadedConfig;
        } catch (error) {
            console.error('Error loading config:', error);
            throw error;
        }
    }

    getConfig(): TechnologyConfig | null {
        return this.config;
    }

    getCategories(): ComponentCategory[] {
        return this.config?.categories || [];
    }

    getComponents(): Record<string, ComponentDefinition> {
        return this.config?.components || {};
    }

    getComponent(componentId: string): ComponentDefinition | null {
        return this.config?.components[componentId] || null;
    }

    getComponentsByCategory(categoryId: string): ComponentDefinition[] {
        const category = this.config?.categories.find(c => c.id === categoryId);
        if (!category) return [];

        return category.components
            .map(id => this.config!.components[id])
            .filter(Boolean);
    }

    async resolveDynamicOptions(source: string, filter?: string): Promise<Array<{value: any; label: string}>> {
        const dynamicSource = this.config?.dynamicSources[source];
        if (!dynamicSource) return [];

        // Si hay mockData, usarlo
        if (dynamicSource.mockData) {
            let data = dynamicSource.mockData;
            if (filter && dynamicSource.filters) {
                data = data.filter((item: any) =>
                    dynamicSource.filters!.includes(filter) || item.type === filter
                );
            }
            return data;
        }

        // Si no, hacer petición real
        try {
            const response = await fetch(dynamicSource.endpoint);
            const data = await response.json();

            if (filter) {
                return data.filter((item: any) => item.type === filter);
            }

            return data;
        } catch (error) {
            console.error(`Error loading dynamic options for ${source}:`, error);
            return [];
        }
    }

    getPropertyTypes(): Record<string, PropertyTypeDefinition> {
        return this.config?.propertyTypes || {};
    }

    getExporters(): Record<string, Exporter> {
        return this.config?.exporters || {};
    }
}

export default ConfigLoader;
export type {
    TechnologyConfig,
    ComponentCategory,
    ComponentDefinition,
    PropertyDefinition,
    PropertyTab,
    PropertyTypeDefinition,
    DynamicSource,
    Exporter
};
