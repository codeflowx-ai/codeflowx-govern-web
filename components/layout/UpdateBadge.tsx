// components/layout/UpdateBadge.tsx
'use client'

import { useState, useEffect } from 'react'

export function UpdateBadge() {
    const [hasNewUpdate, setHasNewUpdate] = useState(false)
    const [latestVersion, setLatestVersion] = useState('')

    useEffect(() => {
        // Simular check de nueva versión
        // En producción esto vendría de una API
        const currentVersion: string = '2.0.5'
        const latest: string = '2.1.0'

        if (currentVersion !== latest) {
            setHasNewUpdate(true)
            setLatestVersion(latest)
        }
    }, [])

    if (!hasNewUpdate) return null

    return (
        <div className="fixed bottom-4 left-4 z-50">
            <div className="bg-blue-600 text-white rounded-lg shadow-lg p-4 max-w-sm">
                <div className="flex items-start gap-3">
                    <span className="text-2xl">🚀</span>
                    <div className="flex-1">
                        <h4 className="font-medium mb-1">Nueva versión disponible</h4>
                        <p className="text-sm text-blue-100 mb-3">
                            Leka Server v{latestVersion} ya está disponible con nuevas funcionalidades.
                        </p>
                        <div className="flex gap-2">
                            <a
                                href="/updates"
                                className="px-3 py-1 bg-white text-blue-600 rounded text-sm font-medium hover:bg-blue-50"
                            >
                                Ver Cambios
                            </a>
                            <button
                                onClick={() => setHasNewUpdate(false)}
                                className="px-3 py-1 bg-blue-700 text-white rounded text-sm hover:bg-blue-800"
                            >
                                Cerrar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
