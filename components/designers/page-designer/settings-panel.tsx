// components/designers/page-designer/settings-panel.tsx
'use client';

import { useEditor } from '@craftjs/core';

export function SettingsPanel() {
    const { selected } = useEditor((state) => ({
        selected: state.events.selected,
    }));

    if (!selected || selected.size === 0) {
        return (
            <div className="p-4 text-sm text-muted-foreground">
                Selecciona un componente para editar sus propiedades
            </div>
        );
    }

    return (
        <div className="p-4">
            <div className="text-sm text-muted-foreground">
                Panel de propiedades del componente seleccionado
            </div>
        </div>
    );
}




