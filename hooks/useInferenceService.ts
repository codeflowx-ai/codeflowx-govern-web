import { useCallback } from 'react';

interface ChatRequest {
  messages: Array<{ role: string; content: string }>;
  useRag?: boolean;
  extraContext?: any;
  model?: string;
  technology?: string;
  architectureId?: string;
  componentType?: string;
  stream?: boolean;
  maxTokens?: number;
  temperature?: number;
  topP?: number;
  topK?: number;
  repetitionPenalty?: number;
  systemPrompt?: string;
}

interface ChatResponse {
  answer: string;
  model?: string;
  tokens?: number;
  processingTime?: number;
  context?: any;
}

interface InferenceStats {
  totalRequests: number;
  totalTokens: number;
  averageResponseTime: number;
  modelsUsed: string[];
  requestsByType: Record<string, number>;
}

export function useInferenceService() {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

  const chat = useCallback(async (request: ChatRequest): Promise<ChatResponse> => {
    const response = await fetch(`${baseUrl}/api/inference/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const chatStream = useCallback(async (request: ChatRequest): Promise<ChatResponse> => {
    const response = await fetch(`${baseUrl}/api/inference/chat/stream`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const chatWithContext = useCallback(async (request: ChatRequest): Promise<ChatResponse> => {
    const response = await fetch(`${baseUrl}/api/inference/chat/context`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const generateEmbeddings = useCallback(async (text: string, model?: string) => {
    const response = await fetch(`${baseUrl}/api/inference/embeddings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ text, model }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const rerankDocuments = useCallback(async (query: string, documents: string[], model?: string, topK?: number) => {
    const response = await fetch(`${baseUrl}/api/inference/rerank`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ query, documents, model, topK }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const completeText = useCallback(async (prompt: string, model?: string, maxTokens?: number, temperature?: number) => {
    const response = await fetch(`${baseUrl}/api/inference/completion`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ prompt, model, maxTokens, temperature }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const refactorCode = useCallback(async (code: string, language: string, refactorType: string, model?: string, instructions?: string) => {
    const response = await fetch(`${baseUrl}/api/inference/refactor`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ code, language, refactorType, model, instructions }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getAvailableModels = useCallback(async (): Promise<string[]> => {
    const response = await fetch(`${baseUrl}/api/inference/models`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const getStats = useCallback(async (): Promise<InferenceStats> => {
    const response = await fetch(`${baseUrl}/api/inference/stats`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  const healthCheck = useCallback(async () => {
    const response = await fetch(`${baseUrl}/api/inference/health`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }, [baseUrl]);

  return {
    chat,
    chatStream,
    chatWithContext,
    generateEmbeddings,
    rerankDocuments,
    completeText,
    refactorCode,
    getAvailableModels,
    getStats,
    healthCheck,
  };
} 