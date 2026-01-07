// components/ui/Switch.tsx
'use client'

import React from 'react'

interface SwitchProps {
    checked: boolean
    onChange: (checked: boolean) => void
    label?: string
    description?: string
    disabled?: boolean
    size?: 'sm' | 'lg'
    id?: string
}

export function Switch({
                           checked,
                           onChange,
                           label,
                           description,
                           disabled = false,
                           size,
                           id
                       }: SwitchProps) {
    const switchId = id || `switch-${Math.random().toString(36).substr(2, 9)}`

    return (
        <div className="form-check form-switch">
            <input
                className={`form-check-input ${size ? `form-check-input-${size}` : ''}`}
                type="checkbox"
                role="switch"
                id={switchId}
                checked={checked}
                onChange={(e) => onChange(e.target.checked)}
                disabled={disabled}
            />
            {label && (
                <label className="form-check-label" htmlFor={switchId}>
                    {label}
                    {description && <div className="text-muted small">{description}</div>}
                </label>
            )}
        </div>
    )
}