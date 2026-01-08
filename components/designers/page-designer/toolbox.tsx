// components/designers/page-designer/toolbox.tsx
'use client';

import { useEditor } from '@craftjs/core';
import React from 'react';
import { Button } from '../../ui/button';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Separator } from '@/components/ui/separator';

export function Toolbox() {
    const { connectors } = useEditor();

    const basicComponents = [
        { name: 'Container', icon: '📦', component: 'Container' },
        { name: 'Text', icon: '📝', component: 'Text' },
        { name: 'Button', icon: '🔘', component: 'ButtonComponent' },
        { name: 'Input', icon: '📝', component: 'InputComponent' },
        { name: 'Form', icon: '📋', component: 'FormComponent' },
        { name: 'Table', icon: '📊', component: 'TableComponent' },
        { name: 'Card', icon: '🃏', component: 'CardComponent' },
    ];

    const enterpriseComponents = [
        { name: 'Invoice Form', icon: '🧾', component: 'InvoiceForm' },
        { name: 'Customer Table', icon: '👥', component: 'CustomerTable' },
        { name: 'Report Chart', icon: '📈', component: 'ReportChart' },
        { name: 'Dashboard Card', icon: '📊', component: 'DashboardCard' },
    ];

    return (
        <ScrollArea className="h-full">
            <div className="p-4 space-y-4">
                {/* Componentes Básicos */}
                <div>
                    <h4 className="text-sm font-medium mb-2">Básicos</h4>
                    <div className="grid grid-cols-2 gap-2">
                        {basicComponents.map((comp) => (
                            <div
                                key={comp.name}
                                ref={(ref) => {
                                    connectors.create(ref!, React.createElement(comp.component));
                                }}
                            >
                                <Button
                                    variant="outline"
                                    size="sm"
                                    className="h-auto p-2 flex flex-col items-center gap-1 w-full"
                                >
                                    <span className="text-lg">{comp.icon}</span>
                                    <span className="text-xs">{comp.name}</span>
                                </Button>
                            </div>
                        ))}
                    </div>
                </div>

                <Separator />

                {/* Componentes Empresariales */}
                <div>
                    <h4 className="text-sm font-medium mb-2">Empresariales</h4>
                    <div className="space-y-2">
                        {enterpriseComponents.map((comp) => (
                            <div
                                key={comp.name}
                                ref={(ref) => {
                                    connectors.create(ref!, React.createElement(comp.component));
                                }}
                            >
                                <Button
                                    variant="outline"
                                    size="sm"
                                    className="w-full justify-start"
                                >
                                    <span className="mr-2">{comp.icon}</span>
                                    {comp.name}
                                </Button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </ScrollArea>
    );
}
