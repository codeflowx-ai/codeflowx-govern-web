// components/ui/Textarea.tsx
import React from 'react'

interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
    label?: string
    error?: string
    helpText?: string
}

export function Textarea({ label, error, helpText, className = '', ...props }: TextareaProps) {
    const textareaClasses = `form-control ${error ? 'is-invalid' : ''} ${className}`.trim()

    return (
        <div className="mb-3">
            {label && (
                <label htmlFor={props.id} className="form-label">
                    {label}
                    {props.required && <span className="text-danger ms-1">*</span>}
                </label>
            )}
            <textarea className={textareaClasses} {...props} />
            {error && <div className="invalid-feedback">{error}</div>}
            {helpText && !error && <div className="form-text">{helpText}</div>}
        </div>
    )
}