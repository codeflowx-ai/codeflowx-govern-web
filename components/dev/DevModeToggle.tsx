// components/dev/DevModeToggle.tsx
'use client'

import { useState, useEffect } from 'react'

export function DevModeToggle() {
    const [showErrors, setShowErrors] = useState(false)
    const [isDev, setIsDev] = useState(false)

    useEffect(() => {
        setIsDev(process.env.NODE_ENV === 'development')
        setShowErrors(localStorage.getItem('showErrors') === 'true')
    }, [])

    const toggleErrorDisplay = () => {
        const newValue = !showErrors
        setShowErrors(newValue)
        localStorage.setItem('showErrors', newValue.toString())
        window.location.reload() // Recargar para aplicar cambios
    }

    if (!isDev) return null

    return (
        <div className="fixed bottom-4 left-4 z-50">
            <button
                onClick={toggleErrorDisplay}
                className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                    showErrors
                        ? 'bg-red-600 text-white hover:bg-red-700'
                        : 'bg-gray-600 text-white hover:bg-gray-700'
                }`}
                title="Toggle error display mode"
            >
                🐛 {showErrors ? 'Hide Errors' : 'Show Errors'}
            </button>
        </div>
    )
}