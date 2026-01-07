// components/auth/OtpInput.tsx
'use client'

import { useState, useRef, useEffect } from 'react'

interface OtpInputProps {
    length?: number
    onComplete: (otp: string) => void
    value: string
    onChange: (value: string) => void
}

export function OtpInput({ length = 6, onComplete, value, onChange }: OtpInputProps) {
    const inputRefs = useRef<(HTMLInputElement | null)[]>([])

    const handleChange = (index: number, digit: string) => {
        if (!/^\d*$/.test(digit)) return

        const newOtp = value.split('')
        newOtp[index] = digit
        const newValue = newOtp.join('').slice(0, length)

        onChange(newValue)

        // Auto-focus next input
        if (digit && index < length - 1) {
            inputRefs.current[index + 1]?.focus()
        }

        // Call onComplete when all digits are filled
        if (newValue.length === length) {
            onComplete(newValue)
        }
    }

    const handleKeyDown = (index: number, e: React.KeyboardEvent) => {
        if (e.key === 'Backspace' && !value[index] && index > 0) {
            inputRefs.current[index - 1]?.focus()
        }
    }

    const handlePaste = (e: React.ClipboardEvent) => {
        e.preventDefault()
        const pastedData = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, length)
        onChange(pastedData)

        if (pastedData.length === length) {
            onComplete(pastedData)
        }
    }

    return (
        <div className="flex gap-2 justify-center">
            {Array.from({ length }, (_, index) => (
                <input
                    key={index}
                    ref={(el) => { inputRefs.current[index] = el; }}
                    type="text"
                    maxLength={1}
                    value={value[index] || ''}
                    onChange={(e) => handleChange(index, e.target.value)}
                    onKeyDown={(e) => handleKeyDown(index, e)}
                    onPaste={handlePaste}
                    className="w-12 h-12 text-center text-xl font-mono border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
            ))}
        </div>
    )
}
