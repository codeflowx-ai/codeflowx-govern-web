// components/designers/page-designer/canvas.tsx
'use client';

import { useNode } from '@craftjs/core';

export function Canvas({ children }: { children?: React.ReactNode }) {
    const {
        connectors: { connect, drag },
    } = useNode();

    return (
        <div
            ref={(ref) => { connect(drag(ref!)); }}
            className="min-h-[500px] p-4 bg-white border-2 border-dashed border-gray-300 rounded-lg"
        >
            {children || (
                <div className="text-center text-gray-400 py-8">
                    Arrastra componentes aquí
                </div>
            )}
        </div>
    );
}

Canvas.craft = {
    displayName: 'Canvas',
};
