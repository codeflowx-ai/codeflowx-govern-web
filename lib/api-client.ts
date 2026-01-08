import axios from 'axios';
import { ProjectConfig, GenerationRequest, GenerationResponse, TechStack } from '@/types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

class LekaApiClient {
    private client = axios.create({
        baseURL: API_BASE_URL,
        headers: {
            'Content-Type': 'application/json',
        },
    });

    // Generar proyecto completo
    async generateProject(request: GenerationRequest): Promise<GenerationResponse> {
        const response = await this.client.post('/api/v1/generate/project', request);
        return response.data;
    }

    // Obtener templates disponibles
    async getTemplates(techStack?: Partial<TechStack>) {
        const response = await this.client.get('/api/v1/templates', {
            params: techStack,
        });
        return response.data;
    }

    // Obtener progreso de generación
    async getGenerationStatus(projectId: string) {
        const response = await this.client.get(`/api/v1/generate/status/${projectId}`);
        return response.data;
    }

    // WebSocket para streaming de código
    createCodeStream(projectId: string) {
        return new WebSocket(`${API_BASE_URL.replace('http', 'ws')}/ws/generate/${projectId}`);
    }
}

export const apiClient = new LekaApiClient();
