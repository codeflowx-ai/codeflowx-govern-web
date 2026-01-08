// components/ui/Select.tsx
'use client'

import React, { useState, useEffect } from 'react'

interface SelectProps {
    value?: string
    onValueChange?: (value: string) => void
    children: React.ReactNode
    disabled?: boolean
    className?: string
}

interface SelectTriggerProps {
    children: React.ReactNode
    className?: string
}

interface SelectContentProps {
    children: React.ReactNode
}

interface SelectItemProps {
    value: string
    children: React.ReactNode
}

interface SelectValueProps {
    placeholder?: string
}

// Context para manejar el estado del select
const SelectContext = React.createContext<{
    value?: string
    onValueChange?: (value: string) => void
    isOpen: boolean
    setIsOpen: (open: boolean) => void
}>({
    isOpen: false,
    setIsOpen: () => {}
})

export function Select({ value, onValueChange, children, disabled, className }: SelectProps) {
    const [isOpen, setIsOpen] = useState(false)

    // Cerrar al hacer click fuera
    React.useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (isOpen) {
                setIsOpen(false)
            }
        }
        if (isOpen) {
            document.addEventListener('click', handleClickOutside)
        }
        return () => {
            document.removeEventListener('click', handleClickOutside)
        }
    }, [isOpen])

    return (
        <SelectContext.Provider value={{ value, onValueChange, isOpen, setIsOpen }}>
            <div
                className={`dropdown ${isOpen ? 'show' : ''} ${className}`}
                style={{
                    position: 'relative',
                    zIndex: isOpen ? 10000 : 'auto',
                    isolation: 'isolate'
                }}
                onClick={(e) => e.stopPropagation()}
            >
                {children}
            </div>
        </SelectContext.Provider>
    )
}

export function SelectTrigger({ children, className }: SelectTriggerProps) {
    const { isOpen, setIsOpen } = React.useContext(SelectContext)

    return (
        <button
            className={`btn btn-outline-secondary dropdown-toggle w-100 d-flex justify-content-between align-items-center ${isOpen ? 'show' : ''} ${className}`}
            type="button"
            onClick={(e) => {
                e.preventDefault()
                e.stopPropagation()
                setIsOpen(!isOpen)
            }}
            aria-expanded={isOpen}
        >
            {children}
        </button>
    )
}

export function SelectContent({ children }: SelectContentProps) {
    const { isOpen } = React.useContext(SelectContext)

    if (!isOpen) return null

    return (
        <ul
            className="dropdown-menu w-100 show"
            style={{
                display: isOpen ? 'block' : 'none',
                zIndex: 10000,
                position: 'absolute',
                top: '100%',
                left: 0,
                right: 0,
                backgroundColor: 'hsl(var(--background))',
                opacity: 1,
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
                border: '1px solid hsl(var(--border))',
                borderRadius: 'calc(var(--radius) - 2px)',
                marginTop: '0.25rem'
            }}
        >
            {children}
        </ul>
    )
}

export function SelectItem({ value, children }: SelectItemProps) {
    const { onValueChange, setIsOpen, value: selectedValue } = React.useContext(SelectContext)

    const handleClick = () => {
        onValueChange?.(value)
        setIsOpen(false)
    }

    const isSelected = selectedValue === value

    return (
        <li>
            <button
                className={`dropdown-item w-100 text-left ${isSelected ? 'active' : ''}`}
                type="button"
                onClick={handleClick}
                style={{
                    padding: '0.5rem 1rem',
                    border: 'none',
                    background: isSelected ? 'hsl(var(--accent))' : 'transparent',
                    cursor: 'pointer',
                    transition: 'background-color 0.2s'
                }}
                onMouseEnter={(e) => {
                    if (!isSelected) {
                        e.currentTarget.style.backgroundColor = 'hsl(var(--muted))'
                    }
                }}
                onMouseLeave={(e) => {
                    if (!isSelected) {
                        e.currentTarget.style.backgroundColor = 'transparent'
                    }
                }}
            >
                {children}
            </button>
        </li>
    )
}

export function SelectValue({ placeholder }: SelectValueProps) {
    const { value } = React.useContext(SelectContext)

    return (
        <span>{value || placeholder}</span>
    )
}
