// hooks/useInference.ts
'use client';

import { useState, useCallback, useEffect } from 'react';
import { InferenceRequest, ComparisonRequest, ModelResponse, RAGRequest, RAGResponse, TrainingRequest, TrainingResponse } from '@/app/ai-playground/types/playground';
import { config } from '@/app/config/environment';

// Mock service simple
class MockService {
    private static instance: MockService;

    static getInstance(): MockService {
        if (!MockService.instance) {
            MockService.instance = new MockService();
        }
        return MockService.instance;
    }

    generateInferenceResponse(request: InferenceRequest): Promise<ModelResponse> {
        return Promise.resolve({
            id: 'mock_' + Date.now(),
            modelId: request.modelId,
            content: 'Esta es una respuesta mock del modelo ' + request.modelId,
            tokens: 150,
            cost: 0.002,
            responseTime: 1.5
        });
    }
}

export type BackendStatus = 'unknown' | 'available' | 'unavailable';
export type MockResponse = any;

interface UseInferenceReturn {
    // Estado
    useMock: boolean;
    setUseMock: (useMock: boolean) => void;
    backendStatus: BackendStatus;

    // Métodos de inferencia
    generateText: (request: InferenceRequest) => Promise<ModelResponse>;
    compareModels: (request: ComparisonRequest) => Promise<ModelResponse[]>;
    queryRAG: (request: RAGRequest) => Promise<RAGResponse>;
    startTraining: (request: TrainingRequest) => Promise<TrainingResponse>;

    // Utilidades
    checkBackendHealth: () => Promise<void>;
    isMockResponse: (response: any) => response is MockResponse;
}

export const useInference = (): UseInferenceReturn => {
    const [useMock, setUseMock] = useState(config.useMock);
    const [backendStatus, setBackendStatus] = useState<BackendStatus>('unknown');
    const mockService = MockService.getInstance();

    // Health check del backend
    const checkBackendHealth = useCallback(async () => {
        try {
            const response = await fetch('/api/v1/health', {
                method: 'GET',
                signal: AbortSignal.timeout(5000) // 5 segundos timeout
            });
            setBackendStatus(response.ok ? 'available' : 'unavailable');
        } catch (error) {
            console.warn('Backend health check failed:', error);
            setBackendStatus('unavailable');
        }
    }, []);

    // Verificar salud del backend al montar
    useEffect(() => {
        if (!useMock) {
            checkBackendHealth();
        }
    }, [useMock, checkBackendHealth]);

    // Generar texto con fallback a mock
    const generateText = useCallback(async (request: InferenceRequest): Promise<ModelResponse> => {
        // Intentar backend real primero si no está en modo mock
        if (!useMock && backendStatus === 'available') {
            try {
                const response = await fetch('/api/v1/inference/generate', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${getToken()}`
                    },
                    body: JSON.stringify(request),
                    signal: AbortSignal.timeout(30000) // 30 segundos timeout
                });

                if (response.ok) {
                    const data = await response.json();
                    return data;
                } else {
                    console.warn('Backend returned error:', response.status);
                    if (config.fallbackToMock) {
                        console.log('Falling back to mock...');
                        return mockService.generateInferenceResponse(request);
                    }
                    throw new Error(`Backend error: ${response.status}`);
                }
            } catch (error) {
                console.warn('Backend request failed, using mock:', error);
                if (config.fallbackToMock) {
                    return mockService.generateInferenceResponse(request);
                }
                throw error;
            }
        }

        // Usar mock directamente
        return mockService.generateInferenceResponse(request);
    }, [useMock, backendStatus, mockService]);

    // Comparar modelos con fallback a mock
    const compareModels = useCallback(async (request: ComparisonRequest): Promise<ModelResponse[]> => {
        if (!useMock && backendStatus === 'available') {
            try {
                const response = await fetch('/api/v1/inference/compare', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${getToken()}`
                    },
                    body: JSON.stringify(request),
                    signal: AbortSignal.timeout(30000)
                });

                if (response.ok) {
                    const data = await response.json();
                    return data;
                } else {
                    console.warn('Backend returned error:', response.status);
                    if (config.fallbackToMock) {
                        const responses = await Promise.all(request.modelIds.map(modelId => mockService.generateInferenceResponse({ ...request, modelId })));
                        return responses;
                    }
                    throw new Error(`Backend error: ${response.status}`);
                }
            } catch (error) {
                console.warn('Backend request failed, using mock:', error);
                if (config.fallbackToMock) {
                    const responses = await Promise.all(request.modelIds.map(modelId => mockService.generateInferenceResponse({ ...request, modelId })));
                    return responses;
                }
                throw error;
            }
        }

        const responses = await Promise.all(request.modelIds.map(modelId => mockService.generateInferenceResponse({ ...request, modelId })));
        return responses;
    }, [useMock, backendStatus, mockService]);

    // Query RAG con fallback a mock
    const queryRAG = useCallback(async (request: RAGRequest): Promise<RAGResponse> => {
        if (!useMock && backendStatus === 'available') {
            try {
                const response = await fetch('/api/v1/rag/query', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${getToken()}`
                    },
                    body: JSON.stringify(request),
                    signal: AbortSignal.timeout(30000)
                });

                if (response.ok) {
                    const data = await response.json();
                    return data;
                } else {
                    console.warn('Backend returned error:', response.status);
                    if (config.fallbackToMock) {
                        return {
                            id: 'mock_rag_' + Date.now(),
                            content: 'Esta es una respuesta RAG mock',
                            sources: [
                                { title: 'Documento Mock 1', snippet: 'Contenido del documento 1', score: 0.95 },
                                { title: 'Documento Mock 2', snippet: 'Contenido del documento 2', score: 0.87 }
                            ],
                            modelId: request.modelId,
                            responseTime: 2.1
                        };
                    }
                    throw new Error(`Backend error: ${response.status}`);
                }
            } catch (error) {
                console.warn('Backend request failed, using mock:', error);
                if (config.fallbackToMock) {
                    return {
                        id: 'mock_rag_' + Date.now(),
                        content: 'Esta es una respuesta RAG mock',
                        sources: [
                            { title: 'Documento Mock 1', snippet: 'Contenido del documento 1', score: 0.95 },
                            { title: 'Documento Mock 2', snippet: 'Contenido del documento 2', score: 0.87 }
                        ],
                        modelId: request.modelId,
                        responseTime: 2.1
                    };
                }
                throw error;
            }
        }

        return {
            id: 'mock_rag_' + Date.now(),
            content: 'Esta es una respuesta RAG mock',
            sources: [
                { title: 'Documento Mock 1', snippet: 'Contenido del documento 1', score: 0.95 },
                { title: 'Documento Mock 2', snippet: 'Contenido del documento 2', score: 0.87 }
            ],
            modelId: request.modelId,
            responseTime: 2.1
        };
    }, [useMock, backendStatus]);

    // Iniciar entrenamiento con fallback a mock
    const startTraining = useCallback(async (request: TrainingRequest): Promise<TrainingResponse> => {
        if (!useMock && backendStatus === 'available') {
            try {
                const response = await fetch('/api/v1/training/start', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${getToken()}`
                    },
                    body: JSON.stringify(request),
                    signal: AbortSignal.timeout(30000)
                });

                if (response.ok) {
                    const data = await response.json();
                    return data;
                } else {
                    console.warn('Backend returned error:', response.status);
                    if (config.fallbackToMock) {
                        return {
                            id: 'mock_training_' + Date.now(),
                            status: 'pending',
                            progress: 0
                        };
                    }
                    throw new Error(`Backend error: ${response.status}`);
                }
            } catch (error) {
                console.warn('Backend request failed, using mock:', error);
                if (config.fallbackToMock) {
                    return {
                        id: 'mock_training_' + Date.now(),
                        status: 'pending',
                        progress: 0
                    };
                }
                throw error;
            }
        }

        return {
            id: 'mock_training_' + Date.now(),
            status: 'pending',
            progress: 0
        };
    }, [useMock, backendStatus]);

    // Verificar si una respuesta es mock
    const isMockResponse = useCallback((response: any): response is MockResponse => {
        return response && (response.id?.startsWith('mock_') || response.job_id?.startsWith('mock_'));
    }, []);

    return {
        useMock,
        setUseMock,
        backendStatus,
        generateText,
        compareModels,
        queryRAG,
        startTraining,
        checkBackendHealth,
        isMockResponse
    };
};

// Función helper para obtener token
const getToken = (): string => {
    if (typeof window !== 'undefined') {
        return localStorage.getItem('token') || '';
    }
    return '';
};
