import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const mockData = await import('@/mocks/governance/telemetry/events.json');
      const data = mockData.default || mockData;
      // Aplicar paginación al mock
      const page = parseInt(request.nextUrl.searchParams.get('page') || '0');
      const size = parseInt(request.nextUrl.searchParams.get('size') || '20');
      const start = page * size;
      const end = start + size;
      return NextResponse.json({
        ...data,
        events: data.events.slice(start, end),
        page,
        size,
      });
    } catch (error) {
      console.error('Error loading mock data:', error);
      return NextResponse.json({
        events: [],
        total: 0,
        page: 0,
        size: 20,
      });
    }
  }

  const gatewayUrl = process.env.NEXT_PUBLIC_BFF_TELEMETRY_URL || 'http://localhost:8080';
  const searchParams = request.nextUrl.searchParams;
  const projectId = searchParams.get('projectId');
  const page = searchParams.get('page') || '0';
  const size = searchParams.get('size') || '20';
  const startTime = searchParams.get('startTime');
  const endTime = searchParams.get('endTime');

  if (!projectId) {
    return NextResponse.json(
      { error: 'El parámetro projectId es requerido' },
      { status: 400 }
    );
  }

  const queryParams = new URLSearchParams();
  queryParams.append('projectId', projectId);
  queryParams.append('page', page);
  queryParams.append('size', size);
  if (startTime) queryParams.append('startTime', startTime);
  if (endTime) queryParams.append('endTime', endTime);

  const response = await fetch(`${gatewayUrl}/api/v1/telemetry/search/project?${queryParams.toString()}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al buscar eventos de telemetría por proyecto' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
