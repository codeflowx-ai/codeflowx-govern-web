// components/designers/page-designer/enterprise-components.tsx
'use client';

import { useNode } from '@craftjs/core';
import { Card, CardContent, CardHeader, CardTitle } from '../../ui/card';
import { Input } from '../../ui/input';
import { Button } from '../../ui/button';
import { Label } from '../../ui/label';
import { Textarea } from '../../ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../../ui/select';

export const InvoiceForm = ({
                                title = "Formulario de Facturación",
                                customerName = "",
                                amount = 0,
                                ...props
                            }) => {
    const {
        connectors: { connect, drag },
        selected,
        actions: { setProp }
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    return (
        <div ref={(ref) => { connect(drag(ref!)); }}>
        <Card
            className={`${selected ? 'ring-2 ring-blue-500' : ''} cursor-move`}
            {...props}
        >
            <CardHeader>
                <CardTitle>{title}</CardTitle>
            </CardHeader>
            <CardContent>
                <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-2">
                        <Label>Cliente</Label>
                        <Input
                            placeholder="Nombre del cliente"
                            value={customerName}
                            onChange={(e) => setProp((props: any) => props.customerName = e.target.value)}
                        />
                    </div>
                    <div className="space-y-2">
                        <Label>Importe</Label>
                        <Input
                            type="number"
                            placeholder="0.00"
                            value={amount}
                            onChange={(e) => setProp((props: any) => props.amount = parseFloat(e.target.value))}
                        />
                    </div>
                    <div className="space-y-2 col-span-2">
                        <Label>Descripción</Label>
                        <Textarea placeholder="Descripción de la factura" />
                    </div>
                    <div className="space-y-2">
                        <Label>Tipo de Factura</Label>
                        <Select>
                            <SelectTrigger>
                                <SelectValue placeholder="Seleccionar tipo" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="service">Servicios</SelectItem>
                                <SelectItem value="product">Productos</SelectItem>
                                <SelectItem value="mixed">Mixta</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="space-y-2">
                        <Label>Estado</Label>
                        <Select>
                            <SelectTrigger>
                                <SelectValue placeholder="Estado" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="draft">Borrador</SelectItem>
                                <SelectItem value="sent">Enviada</SelectItem>
                                <SelectItem value="paid">Pagada</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="col-span-2 flex gap-2 pt-4">
                        <Button className="flex-1">Guardar</Button>
                        <Button variant="outline" className="flex-1">Cancelar</Button>
                    </div>
                </div>
            </CardContent>
        </Card>
        </div>
    );
};

// Panel de configuración para el componente
export const InvoiceFormSettings = () => {
    const {
        actions: { setProp },
        title,
        customerName,
        amount
    } = useNode((node) => ({
        title: node.data.props.title,
        customerName: node.data.props.customerName,
        amount: node.data.props.amount,
    }));

    return (
        <div className="space-y-4">
            <div>
                <Label>Título</Label>
                <Input
                    value={title}
                    onChange={(e) => setProp((props: any) => props.title = e.target.value)}
                />
            </div>
            <div>
                <Label>Cliente por defecto</Label>
                <Input
                    value={customerName}
                    onChange={(e) => setProp((props: any) => props.customerName = e.target.value)}
                />
            </div>
            <div>
                <Label>Importe por defecto</Label>
                <Input
                    type="number"
                    value={amount}
                    onChange={(e) => setProp((props: any) => props.amount = parseFloat(e.target.value))}
                />
            </div>
        </div>
    );
};

export const CustomerTable = ({ customers = [] }) => {
    const {
        connectors: { connect, drag },
        selected
    } = useNode((state) => ({
        selected: state.events.selected,
    }));

    const defaultCustomers = [
        { id: 1, name: 'Juan Pérez', email: 'juan@email.com', phone: '123-456-789' },
        { id: 2, name: 'María García', email: 'maria@email.com', phone: '987-654-321' },
        { id: 3, name: 'Carlos López', email: 'carlos@email.com', phone: '555-123-456' },
    ];

    const displayCustomers = customers.length > 0 ? customers : defaultCustomers;

    return (
        <div ref={(ref) => { connect(drag(ref!)); }}>
        <Card
            className={`${selected ? 'ring-2 ring-blue-500' : ''} cursor-move`}
        >
            <CardHeader>
                <CardTitle>Tabla de Clientes</CardTitle>
            </CardHeader>
            <CardContent>
                <div className="overflow-x-auto">
                    <table className="w-full border-collapse border border-gray-300">
                        <thead>
                        <tr className="bg-gray-50">
                            <th className="border border-gray-300 p-2 text-left">ID</th>
                            <th className="border border-gray-300 p-2 text-left">Nombre</th>
                            <th className="border border-gray-300 p-2 text-left">Email</th>
                            <th className="border border-gray-300 p-2 text-left">Teléfono</th>
                            <th className="border border-gray-300 p-2 text-left">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        {displayCustomers.map((customer) => (
                            <tr key={customer.id}>
                                <td className="border border-gray-300 p-2">{customer.id}</td>
                                <td className="border border-gray-300 p-2">{customer.name}</td>
                                <td className="border border-gray-300 p-2">{customer.email}</td>
                                <td className="border border-gray-300 p-2">{customer.phone}</td>
                                <td className="border border-gray-300 p-2">
                                    <div className="flex gap-1">
                                        <Button size="sm" variant="outline">Editar</Button>
                                        <Button size="sm" variant="danger">Eliminar</Button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </CardContent>
        </Card>
        </div>
    );
};

CustomerTable.craft = {
    displayName: 'Tabla Clientes',
    props: {
        customers: [],
    },
    rules: {
        canDrag: () => true,
    },
};

export const ReportChart = ({ title = 'Gráfico de Reporte' }: { title?: string }) => {
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
                <CardContent>
                    <div className="h-64 flex items-center justify-center bg-gray-100 rounded">
                        <span className="text-gray-400">Gráfico de Reporte</span>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
};

ReportChart.craft = {
    displayName: 'Gráfico de Reporte',
    props: {
        title: 'Gráfico de Reporte',
    },
    rules: {
        canDrag: () => true,
    },
};

export const DashboardCard = ({ title = 'Tarjeta de Dashboard', value = '0' }: { title?: string; value?: string }) => {
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
                <CardContent>
                    <div className="text-3xl font-bold">{value}</div>
                </CardContent>
            </Card>
        </div>
    );
};

DashboardCard.craft = {
    displayName: 'Tarjeta de Dashboard',
    props: {
        title: 'Tarjeta de Dashboard',
        value: '0',
    },
    rules: {
        canDrag: () => true,
    },
};
