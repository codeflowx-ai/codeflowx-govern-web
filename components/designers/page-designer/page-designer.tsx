// components/designers/page-designer/page-designer.tsx
'use client';

import { Editor, Frame, Element } from '@craftjs/core';
import { useState } from 'react';
import { Toolbox } from './toolbox';
import { SettingsPanel } from './settings-panel';
import { Canvas } from './canvas';
import { Toolbar } from './toolbar';
import { Button } from '../../ui/button';
import { Card } from '../../ui/card';

// Componentes básicos para el diseñador
import {
    Container,
    Text,
    ButtonComponent,
    InputComponent,
    FormComponent,
    TableComponent,
    CardComponent
} from './basic-components';

// Componentes empresariales
import {
    InvoiceForm,
    CustomerTable,
    ReportChart,
    DashboardCard
} from './enterprise-components';

export function PageDesigner() {
    const [selectedComponent, setSelectedComponent] = useState<string | null>(null);

    return (
        <div className="flex h-screen bg-background">
            {/* Toolbox - Componentes disponibles */}
            <div className="w-64 bg-card border-r">
                <div className="p-4 border-b">
                    <h3 className="font-semibold">Componentes</h3>
                </div>
                <Toolbox />
            </div>

            {/* Canvas Principal */}
            <div className="flex-1 flex flex-col">
                {/* Toolbar superior */}
                <div className="h-14 border-b bg-card px-4 flex items-center justify-between">
                    <Toolbar />
                    <div className="flex gap-2">
                        <Button variant="outline" size="sm">Preview</Button>
                        <Button variant="outline" size="sm">Export</Button>
                        <Button size="sm">Generate Code</Button>
                    </div>
                </div>

                {/* Área de diseño */}
                <div className="flex-1 p-4 bg-muted/20">
                    <Editor
                        resolver={{
                            Container,
                            Text,
                            ButtonComponent,
                            InputComponent,
                            FormComponent,
                            TableComponent,
                            CardComponent,
                            // Componentes empresariales
                            InvoiceForm,
                            CustomerTable,
                            ReportChart,
                            DashboardCard,
                        }}
                    >
                        <Frame>
                            <Element is={Canvas} canvas />
                        </Frame>
                    </Editor>
                </div>
            </div>

            {/* Panel de propiedades */}
            <div className="w-80 bg-card border-l">
                <div className="p-4 border-b">
                    <h3 className="font-semibold">Propiedades</h3>
                </div>
                <SettingsPanel />
            </div>
        </div>
    );
}
