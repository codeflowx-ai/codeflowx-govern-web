import ConfigLoader from './ConfigLoader';
import type { DroppedComponent } from '@/types/page-designer';

export class HierarchyManager {
    private static instance: HierarchyManager;
    private hierarchyRules: Record<string, any> = {};

    static getInstance(): HierarchyManager {
        if (!HierarchyManager.instance) {
            HierarchyManager.instance = new HierarchyManager();
        }
        return HierarchyManager.instance;
    }

    async loadRules() {
        const configLoader = ConfigLoader.getInstance();
        const config = configLoader.getConfig();
        this.hierarchyRules = config?.hierarchy?.rules || {};
    }

    canAcceptChild(parentType: string, childType: string): boolean {
        const rule = this.hierarchyRules[parentType];
        if (!rule || !rule.acceptsChildren) return false;

        if (rule.allowedChildren.includes('*')) return true;
        return rule.allowedChildren.includes(childType);
    }

    getDropZones(componentType: string): string[] {
        const rule = this.hierarchyRules[componentType];
        return rule?.dropZones || ['content'];
    }

    getMaxChildren(componentType: string): number {
        const rule = this.hierarchyRules[componentType];
        return rule?.maxChildren || -1; // -1 = unlimited
    }

    shouldAutoCreateChildren(componentType: string): boolean {
        const rule = this.hierarchyRules[componentType];
        return rule?.autoCreateChildren || false;
    }

    getChildTemplate(componentType: string): Partial<DroppedComponent> | null {
        const rule = this.hierarchyRules[componentType];
        return rule?.childTemplate || null;
    }

    // Convertir estructura plana a jerárquica
    buildHierarchy(components: DroppedComponent[]): DroppedComponent[] {
        const componentMap = new Map<string, DroppedComponent>();
        const rootComponents: DroppedComponent[] = [];

        // Crear mapa de componentes
        components.forEach(comp => {
            componentMap.set(comp.id, { ...comp, children: [] });
        });

        // Construir jerarquía
        components.forEach(comp => {
            const component = componentMap.get(comp.id)!;

            if (comp.parentId) {
                const parent = componentMap.get(comp.parentId);
                if (parent) {
                    parent.children = parent.children || [];
                    parent.children.push(component);
                } else {
                    // Padre no encontrado, agregar a raíz
                    rootComponents.push(component);
                }
            } else {
                rootComponents.push(component);
            }
        });

        return rootComponents;
    }

    // Convertir estructura jerárquica a plana
    flattenHierarchy(components: DroppedComponent[]): DroppedComponent[] {
        const flattened: DroppedComponent[] = [];

        const flatten = (comps: DroppedComponent[], parentId?: string) => {
            comps.forEach(comp => {
                const flatComp = { ...comp, parentId };
                delete flatComp.children; // Remover children para estructura plana
                flattened.push(flatComp);

                if (comp.children && comp.children.length > 0) {
                    flatten(comp.children, comp.id);
                }
            });
        };

        flatten(components);
        return flattened;
    }

    // Encontrar componente por ID en jerarquía
    findComponent(components: DroppedComponent[], id: string): DroppedComponent | null {
        for (const comp of components) {
            if (comp.id === id) return comp;

            if (comp.children) {
                const found = this.findComponent(comp.children, id);
                if (found) return found;
            }
        }
        return null;
    }

    // Obtener ruta de componente (breadcrumb)
    getComponentPath(components: DroppedComponent[], targetId: string): DroppedComponent[] {
        const path: DroppedComponent[] = [];

        const findPath = (comps: DroppedComponent[], id: string): boolean => {
            for (const comp of comps) {
                path.push(comp);

                if (comp.id === id) return true;

                if (comp.children && findPath(comp.children, id)) {
                    return true;
                }

                path.pop();
            }
            return false;
        };

        findPath(components, targetId);
        return path;
    }

    // Validar si se puede mover componente
    canMoveComponent(
        components: DroppedComponent[],
        componentId: string,
        newParentId?: string
    ): boolean {
        const component = this.findComponent(components, componentId);
        if (!component) return false;

        // No se puede mover a sí mismo o a sus hijos
        if (newParentId === componentId) return false;

        if (newParentId) {
            const newParent = this.findComponent(components, newParentId);
            if (!newParent) return false;

            // Verificar si el nuevo padre puede aceptar este tipo de hijo
            if (!this.canAcceptChild(newParent.type, component.type)) return false;

            // Verificar límite de hijos
            const maxChildren = this.getMaxChildren(newParent.type);
            if (maxChildren > 0 && (newParent.children?.length || 0) >= maxChildren) {
                return false;
            }

            // Verificar que no se mueva a un descendiente
            const path = this.getComponentPath(components, newParentId);
            if (path.some(p => p.id === componentId)) return false;
        }

        return true;
    }
}

export default HierarchyManager;
