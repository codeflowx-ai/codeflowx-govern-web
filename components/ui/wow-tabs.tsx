"use client";

import * as Tabs from "@radix-ui/react-tabs";
import { ReactNode } from "react";

interface WowTabsProps {
  value: string;
  onValueChange: (value: string) => void;
  defaultValue?: string;
  children: ReactNode;
}

interface WowTabsListProps {
  children: ReactNode;
  className?: string;
}

interface WowTabsTriggerProps {
  value: string;
  children: ReactNode;
  className?: string;
}

interface WowTabsContentProps {
  value: string;
  children: ReactNode;
  className?: string;
}

// Componente principal WowTabs
export function WowTabs({ value, onValueChange, defaultValue, children }: WowTabsProps) {
  return (
    <Tabs.Root value={value} onValueChange={onValueChange} defaultValue={defaultValue} className="w-full">
      {children}
    </Tabs.Root>
  );
}

// Componente WowTabsList con estilos predefinidos
export function WowTabsList({ children, className = "" }: WowTabsListProps) {
  return (
    <Tabs.List
      className={`flex w-full bg-gray-100 border border-gray-300 rounded-2xl p-2 shadow-xl ${className}`}
      style={{
        display: 'flex',
        width: '100%',
        backgroundColor: 'rgba(243, 244, 246, 0.8)',
        border: '1px solid #d1d5db',
        borderRadius: '1rem',
        padding: '0.5rem',
        boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)'
      }}
    >
      {children}
    </Tabs.List>
  );
}

// Componente WowTabsTrigger con estilos predefinidos
export function WowTabsTrigger({ value, children, className = "" }: WowTabsTriggerProps) {
  return (
    <Tabs.Trigger
      value={value}
      className={`flex-1 px-6 py-3 rounded-xl font-medium text-sm transition-all duration-300 ease-in-out ${className}`}
      style={{
        flex: '1',
        padding: '0.75rem 1.5rem',
        borderRadius: '0.75rem',
        fontWeight: '500',
        fontSize: '0.875rem',
        transition: 'all 0.3s ease-in-out',
        border: 'none',
        cursor: 'pointer'
      }}
    >
      {children}
    </Tabs.Trigger>
  );
}

// Componente WowTabsContent
export function WowTabsContent({ value, children, className = "" }: WowTabsContentProps) {
  return (
    <Tabs.Content value={value} className={className}>
      {children}
    </Tabs.Content>
  );
}

// Exportar también los componentes originales para compatibilidad
export { Tabs };
