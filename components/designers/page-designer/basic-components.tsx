// components/designers/page-designer/basic-components.tsx
'use client';

import { useNode } from '@craftjs/core';
import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card';
import { Button } from '../../ui/button';
import { Input } from '../../ui/input';

export const Container = ({ children, ...props }: { children?: React.ReactNode }) => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div
            ref={(ref) => { connect(drag(ref!)); }}
            className={`p-4 ${selected ? 'ring-2 ring-blue-500' : ''}`}
            {...props}
        >
            {children}
        </div>
    );
};

Container.craft = {
    displayName: 'Container',
};

export const Text = ({ children = 'Texto' }: { children?: React.ReactNode }) => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <p
            ref={(ref) => { connect(drag(ref!)); }}
            className={selected ? 'ring-2 ring-blue-500' : ''}
        >
            {children}
        </p>
    );
};

Text.craft = {
    displayName: 'Text',
};

export const ButtonComponent = ({ children = 'Button' }: { children?: React.ReactNode }) => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div ref={(ref) => { connect(drag(ref!)); }} className={selected ? 'ring-2 ring-blue-500' : ''}>
            <Button>{children}</Button>
        </div>
    );
};

ButtonComponent.craft = {
    displayName: 'Button',
};

export const InputComponent = () => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div ref={(ref) => { connect(drag(ref!)); }} className={selected ? 'ring-2 ring-blue-500' : ''}>
            <Input placeholder="Input" />
        </div>
    );
};

InputComponent.craft = {
    displayName: 'Input',
};

export const FormComponent = ({ children }: { children?: React.ReactNode }) => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <form
            ref={(ref) => { connect(drag(ref!)); }}
            className={`p-4 border ${selected ? 'ring-2 ring-blue-500' : ''}`}
        >
            {children}
        </form>
    );
};

FormComponent.craft = {
    displayName: 'Form',
};

export const TableComponent = () => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div ref={(ref) => { connect(drag(ref!)); }} className={selected ? 'ring-2 ring-blue-500' : ''}>
            <table className="w-full border">
                <thead>
                    <tr>
                        <th className="border p-2">Header 1</th>
                        <th className="border p-2">Header 2</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td className="border p-2">Cell 1</td>
                        <td className="border p-2">Cell 2</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

TableComponent.craft = {
    displayName: 'Table',
};

export const CardComponent = ({ title = 'Card Title', children }: { title?: string; children?: React.ReactNode }) => {
    const {
        connectors: { connect, drag },
        selected,
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div ref={(ref) => { connect(drag(ref!)); }} className={selected ? 'ring-2 ring-blue-500' : ''}>
            <Card>
                <CardHeader>
                    <CardTitle>{title}</CardTitle>
                </CardHeader>
                <CardContent>{children}</CardContent>
            </Card>
        </div>
    );
};

CardComponent.craft = {
    displayName: 'Card',
};
