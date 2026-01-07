// components/MockToggle.tsx
'use client';

import { useState } from 'react';
import { useInference, BackendStatus } from '@/hooks/useInference';
import { config } from '@/app/config/environment';

export function MockToggle() {
    const { useMock, setUseMock, backendStatus, checkBackendHealth } = useInference();
    const [isRefreshing, setIsRefreshing] = useState(false);

    // Solo mostrar en desarrollo o si está habilitado en config
    if (!config.enableMockToggle) return null;

    const handleToggle = () => {
        setUseMock(!useMock);
        if (!useMock) {
            // Si estamos cambiando a backend, verificar salud
            checkBackendHealth();
        }
    };

    const handleRefreshHealth = async () => {
        setIsRefreshing(true);
        await checkBackendHealth();
        setIsRefreshing(false);
    };

    const getStatusInfo = () => {
        switch (backendStatus) {
            case 'available':
                return {
                    color: 'green',
                    text: 'Backend Conectado',
                    icon: '🟢',
                    bgColor: 'bg-green-100',
                    textColor: 'text-green-700',
                    borderColor: 'border-green-300'
                };
            case 'unavailable':
                return {
                    color: 'red',
                    text: 'Backend Desconectado',
                    icon: '🔴',
                    bgColor: 'bg-red-100',
                    textColor: 'text-red-700',
                    borderColor: 'border-red-300'
                };
            default:
                return {
                    color: 'yellow',
                    text: 'Verificando...',
                    icon: '🟡',
                    bgColor: 'bg-yellow-100',
                    textColor: 'text-yellow-700',
                    borderColor: 'border-yellow-300'
                };
        }
    };

    const status = getStatusInfo();

    return (
        <div className="fixed top-4 left-4 z-50">
            <div className={`${status.bgColor} ${status.borderColor} border rounded-lg p-4 shadow-lg max-w-xs`}>
                {/* Header */}
                <div className="flex items-center justify-between mb-3">
                    <div className="flex items-center gap-2">
                        <span className="text-lg">🔧</span>
                        <span className={`font-medium ${status.textColor}`}>
                            Modo Desarrollo
                        </span>
                    </div>
                    <button
                        onClick={handleRefreshHealth}
                        disabled={isRefreshing}
                        className={`p-1 rounded hover:bg-white/50 transition-colors ${status.textColor}`}
                        title="Verificar estado del backend"
                    >
                        <svg
                            className={`w-4 h-4 ${isRefreshing ? 'animate-spin' : ''}`}
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                            />
                        </svg>
                    </button>
                </div>

                {/* Toggle Switch */}
                <div className="flex items-center justify-between mb-3">
                    <label className="flex items-center gap-2 text-sm cursor-pointer">
                        <div className="relative">
                            <input
                                type="checkbox"
                                checked={useMock}
                                onChange={handleToggle}
                                className="sr-only"
                            />
                            <div className={`
                                w-10 h-6 rounded-full transition-colors duration-200 ease-in-out
                                ${useMock ? 'bg-blue-500' : 'bg-gray-300'}
                            `}>
                                <div className={`
                                    w-4 h-4 bg-white rounded-full shadow transform transition-transform duration-200 ease-in-out
                                    ${useMock ? 'translate-x-5' : 'translate-x-1'}
                                `} />
                            </div>
                        </div>
                        <span className={`font-medium ${status.textColor}`}>
                            {useMock ? 'Mock Activado' : 'Backend Real'}
                        </span>
                    </label>
                </div>

                {/* Status Indicator */}
                <div className={`flex items-center gap-2 text-sm ${status.textColor}`}>
                    <span>{status.icon}</span>
                    <span>{status.text}</span>
                </div>

                {/* Info */}
                {useMock && (
                    <div className="mt-2 text-xs text-gray-600 bg-white/50 rounded p-2">
                        <div className="font-medium mb-1">📝 Usando datos simulados</div>
                        <div className="space-y-1">
                            <div>• Respuestas generadas localmente</div>
                            <div>• Sin conexión al backend</div>
                            <div>• Ideal para desarrollo</div>
                        </div>
                    </div>
                )}

                {!useMock && backendStatus === 'unavailable' && (
                    <div className="mt-2 text-xs text-red-600 bg-red-50 rounded p-2">
                        <div className="font-medium mb-1">⚠️ Backend no disponible</div>
                        <div>Cambiando automáticamente a mock...</div>
                    </div>
                )}

                {/* Environment Info */}
                <div className="mt-3 pt-2 border-t border-gray-200">
                    <div className="text-xs text-gray-500">
                        <div>Entorno: {process.env.NEXT_PUBLIC_ENV || 'development'}</div>
                        <div>Backend: {config.backendUrl}</div>
                    </div>
                </div>
            </div>
        </div>
    );
}
