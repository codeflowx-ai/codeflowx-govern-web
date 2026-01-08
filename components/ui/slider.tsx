// components/ui/Slider.tsx
'use client'

import React from 'react'

interface SliderProps {
    value: number[]
    onValueChange: (value: number[]) => void
    min?: number
    max?: number
    step?: number
    className?: string
}

export function Slider({
                           value,
                           onValueChange,
                           min = 0,
                           max = 100,
                           step = 1,
                           className = ''
                       }: SliderProps) {
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        onValueChange([parseInt(e.target.value)])
    }

    return (
        <input
            type="range"
            className={`form-range ${className}`}
            min={min}
            max={max}
            step={step}
            value={value[0] || 0}
            onChange={handleChange}
        />
    )
}