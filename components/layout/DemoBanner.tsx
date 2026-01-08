'use client'

import { useEffect, useState } from 'react'
import { AlertTriangle, X } from 'lucide-react'

export function DemoBanner() {
    const [isDemoMode, setIsDemoMode] = useState(false)
    const [isVisible, setIsVisible] = useState(true)

    useEffect(() => {
        const checkDemoMode = () => {
            const demoMode = process.env.NEXT_PUBLIC_DEMO_MODE === 'true' || !process.env.NEXT_PUBLIC_API_URL
            setIsDemoMode(demoMode)
        }
        checkDemoMode()
    }, [])

    if (!isDemoMode || !isVisible) {
        return null
    }

    return (
        <div className="fixed top-4 right-4 z-50 max-w-sm">
            <div className="bg-amber-50 border border-amber-200 rounded-lg shadow-lg p-4">
                <div className="flex items-start space-x-3">
                    <AlertTriangle className="h-5 w-5 text-amber-600 flex-shrink-0 mt-0.5" />
                    <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-amber-800">
                            <span className="font-bold">Modo Demo Activo</span>
                        </p>
                        <p className="text-xs text-amber-700 mt-1">
                            Todos los datos mostrados son sintéticos para demostrar las funcionalidades de la plataforma
                        </p>
                    </div>
                    <button
                        onClick={() => setIsVisible(false)}
                        className="ml-2 flex-shrink-0 p-1 text-amber-600 hover:text-amber-800 transition-colors"
                        aria-label="Cerrar notificación"
                    >
                        <X className="h-4 w-4" />
                    </button>
                </div>
            </div>
        </div>
    )
}