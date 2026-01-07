// components/ui/Card.tsx
'use client'

import React from 'react'
import { cn } from '@/lib/utils'

interface CardProps {
    children: React.ReactNode
    className?: string
    hover?: boolean
    style?: React.CSSProperties
}

interface CardHeaderProps {
    children: React.ReactNode
    className?: string
}

interface CardBodyProps {
    children: React.ReactNode
    className?: string
}

interface CardTitleProps {
    children: React.ReactNode
    className?: string
    as?: 'h1' | 'h2' | 'h3' | 'h4' | 'h5' | 'h6'
}

interface CardTextProps {
    children: React.ReactNode
    className?: string
}

interface CardContentProps {
    children: React.ReactNode
    className?: string
}

interface CardDescriptionProps {
    children: React.ReactNode
    className?: string
}

export function Card({ children, className = '', hover = false, style }: CardProps) {
    // Detectar si hay una clase de fondo personalizada (incluyendo variantes con opacidad)
    const hasCustomBg = className && /bg-[\w-]+(\/\d+)?/.test(className);

    // Construir las clases base sin el fondo por defecto si hay uno personalizado
    const baseClasses = cn(
        'rounded-lg border border-border shadow-sm',
        !hasCustomBg && 'bg-card',
        hover && 'transition-all duration-200 hover:shadow-md hover:-translate-y-1'
    );

    // Aplicar el className personalizado al final para que sobrescriba todo
    return (
        <div className={cn(baseClasses, className)} style={style}>
            {children}
        </div>
    )
}

export function CardHeader({ children, className = '' }: CardHeaderProps) {
    return (
        <div className={cn('px-6 py-4 border-b border-border', className)}>
            {children}
        </div>
    )
}

export function CardBody({ children, className = '' }: CardBodyProps) {
    return (
        <div className={cn('px-6 py-4', className)}>
            {children}
        </div>
    )
}

export function CardTitle({ children, className = '', as: Component = 'h3' }: CardTitleProps) {
    return (
        <Component className={cn('text-lg font-semibold text-foreground', className)}>
            {children}
        </Component>
    )
}

export function CardText({ children, className = '' }: CardTextProps) {
    return (
        <p className={cn('text-muted-foreground', className)}>
            {children}
        </p>
    )
}

export function CardContent({ children, className = '' }: CardContentProps) {
    return (
        <div className={cn('px-6 py-4', className)}>
            {children}
        </div>
    )
}

export function CardDescription({ children, className = '' }: CardDescriptionProps) {
    return (
        <p className={cn('text-sm text-muted-foreground', className)}>
            {children}
        </p>
    )
}
