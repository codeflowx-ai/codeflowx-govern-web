import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const mockData = await import('@/mocks/governance/telemetry/statistics.json');
      const data = mockData.default || mockData;
      const componentUuids = request.nextUrl.searchParams.getAll('componentUuid');
      // Filtrar por componentUuids si se proporcionan
      let filteredStatistics = data.statistics;
      if (componentUuids.length > 0) {
        filteredStatistics = data.statistics.filter((s: any) =>
          componentUuids.includes(s.componentUuid)
        );
      }
      return NextResponse.json({
        statistics: filteredStatistics,
        total: filteredStatistics.length,
      });
    } catch (error) {
      console.error('Error loading mock data:', error);
      return NextResponse.json({
        statistics: [],
        total: 0,
      });
    }
  }

  const bffUrl = process.env.NEXT_PUBLIC_BFF_TELEMETRY_URL || 'http://localhost:8084';
  const searchParams = request.nextUrl.searchParams;
  const componentUuids = searchParams.getAll('componentUuid');
  const startTime = searchParams.get('startTime');
  const endTime = searchParams.get('endTime');

  const queryParams = new URLSearchParams();
  componentUuids.forEach((uuid) => queryParams.append('componentUuid', uuid));
  if (startTime) queryParams.append('startTime', startTime);
  if (endTime) queryParams.append('endTime', endTime);

  const response = await fetch(`${bffUrl}/api/v1/telemetry/statistics/component?${queryParams.toString()}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener estadísticas de componentes' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
