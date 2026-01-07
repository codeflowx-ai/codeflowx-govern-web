import {
  TelemetryKpisResponse,
  TelemetryEventsResponse,
  ComponentStatisticsResponse,
} from '../types/telemetry';

// Usar USE_MOCK para determinar si estamos en modo demo/mock
// NEXT_PUBLIC_USE_MOCK tiene prioridad sobre otras condiciones
const IS_DEMO_MODE = process.env.NEXT_PUBLIC_USE_MOCK === 'true'
  || process.env.NEXT_PUBLIC_DEMO_MODE === 'true'
  || (process.env.NEXT_PUBLIC_USE_MOCK !== 'false' && !process.env.NEXT_PUBLIC_API_URL);

class TelemetryService {
  // Usar rutas relativas para las API routes de Next.js
  private getApiUrl(path: string): string {
    // Si estamos en modo demo/mock, siempre usar rutas relativas
    if (IS_DEMO_MODE) {
      return path;
    }
    // Si hay una URL base configurada y NO estamos en modo demo, usarla
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    if (baseUrl) {
      return `${baseUrl}${path}`;
    }
    // Por defecto, usar rutas relativas
    return path;
  }

  private isDemoMode(): boolean {
    return IS_DEMO_MODE;
  }

  /**
   * Obtiene los KPIs agregados de telemetría
   */
  async getKpis(
    startTime?: string,
    endTime?: string
  ): Promise<TelemetryKpisResponse> {
    try {
      const params = new URLSearchParams();
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const url = this.getApiUrl(`/api/governance/telemetry/kpis${params.toString() ? `?${params.toString()}` : ''}`);
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch telemetry KPIs');
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching telemetry KPIs: ${error}`,
      };
    }
  }

  /**
   * Busca eventos de telemetría por contenido
   */
  async searchByContent(
    query: string,
    page: number = 0,
    size: number = 20,
    startTime?: string,
    endTime?: string
  ): Promise<TelemetryEventsResponse> {
    try {
      const params = new URLSearchParams();
      params.append('query', query);
      params.append('page', page.toString());
      params.append('size', size.toString());
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const url = this.getApiUrl(`/api/governance/telemetry/search/content?${params.toString()}`);
      const response = await fetch(url);
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Failed to search telemetry events: ${response.status} ${errorText}`);
      }
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error searching telemetry events: ${error}`,
      };
    }
  }

  /**
   * Busca eventos de telemetría por proyecto
   */
  async searchByProject(
    projectId: number,
    page: number = 0,
    size: number = 20,
    startTime?: string,
    endTime?: string
  ): Promise<TelemetryEventsResponse> {
    try {
      const params = new URLSearchParams();
      params.append('projectId', projectId.toString());
      params.append('page', page.toString());
      params.append('size', size.toString());
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const url = this.getApiUrl(`/api/governance/telemetry/search/project?${params.toString()}`);
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to search telemetry events by project');
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error searching telemetry events by project: ${error}`,
      };
    }
  }

  /**
   * Obtiene eventos de telemetría por componente
   */
  async getEventsByComponent(
    componentUuid: string,
    page: number = 0,
    size: number = 20,
    startTime?: string,
    endTime?: string
  ): Promise<TelemetryEventsResponse> {
    try {
      const params = new URLSearchParams();
      params.append('componentUuid', componentUuid);
      params.append('page', page.toString());
      params.append('size', size.toString());
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const url = this.getApiUrl(`/api/governance/telemetry/events/component?${params.toString()}`);
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch telemetry events by component');
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching telemetry events by component: ${error}`,
      };
    }
  }

  /**
   * Obtiene estadísticas por componente
   */
  async getComponentStatistics(
    componentUuids?: string[],
    startTime?: string,
    endTime?: string
  ): Promise<ComponentStatisticsResponse> {
    if (this.isDemoMode()) {
      // Mock data para desarrollo
      const mockStatistics = [
        {
          componentUuid: "550e8400-e29b-41d4-a716-446655440001",
          totalEvents: 5000,
          avgLatencyMs: 250.5,
          totalTokens: 1000000,
          totalCostUsd: 125.50,
        },
        {
          componentUuid: "550e8400-e29b-41d4-a716-446655440002",
          totalEvents: 3500,
          avgLatencyMs: 180.2,
          totalTokens: 750000,
          totalCostUsd: 95.25,
        },
        {
          componentUuid: "550e8400-e29b-41d4-a716-446655440003",
          totalEvents: 2800,
          avgLatencyMs: 320.8,
          totalTokens: 600000,
          totalCostUsd: 78.90,
        },
        {
          componentUuid: "550e8400-e29b-41d4-a716-446655440004",
          totalEvents: 4200,
          avgLatencyMs: 195.3,
          totalTokens: 850000,
          totalCostUsd: 110.75,
        },
        {
          componentUuid: "550e8400-e29b-41d4-a716-446655440005",
          totalEvents: 3100,
          avgLatencyMs: 275.6,
          totalTokens: 680000,
          totalCostUsd: 88.40,
        },
      ];

      // Filtrar por componentUuids si se proporcionan
      let filteredStats = mockStatistics;
      if (componentUuids && componentUuids.length > 0) {
        filteredStats = mockStatistics.filter((stat) =>
          componentUuids.includes(stat.componentUuid)
        );
      }

      return {
        success: true,
        data: {
          statistics: filteredStats,
          total: filteredStats.length,
        },
      };
    }

    try {
      const params = new URLSearchParams();
      if (componentUuids && componentUuids.length > 0) {
        componentUuids.forEach((uuid) => params.append('componentUuid', uuid));
      }
      if (startTime) params.append('startTime', startTime);
      if (endTime) params.append('endTime', endTime);

      const url = this.getApiUrl(`/api/governance/telemetry/statistics/component?${params.toString()}`);
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch component statistics');
      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      return {
        success: false,
        error: `Error fetching component statistics: ${error}`,
      };
    }
  }
}

export const telemetryService = new TelemetryService();
