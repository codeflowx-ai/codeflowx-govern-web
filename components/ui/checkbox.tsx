// components/ui/Checkbox.tsx
import React from 'react'

interface CheckboxProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  description?: string
}

export function Checkbox({ label, description, className = '', ...props }: CheckboxProps) {
  return (
      <div className="form-check">
        <input
            className={`form-check-input ${className}`}
            type="checkbox"
            {...props}
        />
        {label && (
            <label className="form-check-label" htmlFor={props.id}>
              {label}
              {description && <div className="text-muted small">{description}</div>}
            </label>
        )}
      </div>
  )
}
