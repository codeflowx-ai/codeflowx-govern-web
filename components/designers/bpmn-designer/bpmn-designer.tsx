// components/designers/bpmn-designer/bpmn-designer.tsx
import { useEffect, useRef } from 'react';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css';

export function BpmnDesigner() {
    const containerRef = useRef<HTMLDivElement>(null);
    const modelerRef = useRef<BpmnModeler | null>(null);

    useEffect(() => {
        if (containerRef.current) {
            modelerRef.current = new BpmnModeler({
                container: containerRef.current,
                keyboard: {
                    bindTo: window
                },
                // Elementos personalizados para IA
                additionalModules: [
                    // Custom AI Agent elements
                ]
            });

            // Cargar diagrama inicial
            const initialDiagram = `<?xml version="1.0" encoding="UTF-8"?>
        <bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL">
          <bpmn:process id="Process_1" isExecutable="true">
            <bpmn:startEvent id="StartEvent_1"/>
          </bpmn:process>
        </bpmn:definitions>`;

            modelerRef.current.importXML(initialDiagram);
        }

        return () => {
            modelerRef.current?.destroy();
        };
    }, []);

    const exportXML = async () => {
        if (modelerRef.current) {
            const { xml } = await modelerRef.current.saveXML({ format: true });
            return xml;
        }
    };

    return (
        <div className="flex h-screen">
            {/* Palette de elementos BPMN + IA */}
            <div className="w-64 bg-gray-50 border-r p-4">
                <h3 className="font-bold mb-4">Elementos BPMN</h3>
                <div className="space-y-2">
                    <div className="p-2 border rounded cursor-pointer">🟢 Start Event</div>
                    <div className="p-2 border rounded cursor-pointer">📋 User Task</div>
                    <div className="p-2 border rounded cursor-pointer">⚙️ Service Task</div>
                    <div className="p-2 border rounded cursor-pointer">🤖 AI Agent Task</div>
                    <div className="p-2 border rounded cursor-pointer">🔀 Gateway</div>
                    <div className="p-2 border rounded cursor-pointer">🔴 End Event</div>
                </div>
            </div>

            {/* Canvas BPMN */}
            <div className="flex-1">
                <div ref={containerRef} className="h-full" />
            </div>

            {/* Panel de propiedades */}
            <div className="w-80 bg-gray-50 border-l p-4">
                <h3 className="font-bold mb-4">Propiedades</h3>
                {/* Propiedades del elemento seleccionado */}
            </div>
        </div>
    );
}