import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const mockData = await import('@/mocks/governance/telemetry/events.json');
      const data = mockData.default || mockData;
      const componentUuid = request.nextUrl.searchParams.get('componentUuid');
      // Filtrar por componentUuid si se proporciona
      let filteredEvents = data.events;
      if (componentUuid) {
        filteredEvents = data.events.filter((e: any) => e.componentUuid === componentUuid);
      }
      // Aplicar paginación al mock
      const page = parseInt(request.nextUrl.searchParams.get('page') || '0');
      const size = parseInt(request.nextUrl.searchParams.get('size') || '20');
      const start = page * size;
      const end = start + size;
      return NextResponse.json({
        events: filteredEvents.slice(start, end),
        total: filteredEvents.length,
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

  const bffUrl = process.env.NEXT_PUBLIC_BFF_TELEMETRY_URL || 'http://localhost:8084';
  const searchParams = request.nextUrl.searchParams;
  const componentUuid = searchParams.get('componentUuid');
  const page = searchParams.get('page') || '0';
  const size = searchParams.get('size') || '20';
  const startTime = searchParams.get('startTime');
  const endTime = searchParams.get('endTime');

  if (!componentUuid) {
    return NextResponse.json(
      { error: 'El parámetro componentUuid es requerido' },
      { status: 400 }
    );
  }

  const queryParams = new URLSearchParams();
  queryParams.append('componentUuid', componentUuid);
  queryParams.append('page', page);
  queryParams.append('size', size);
  if (startTime) queryParams.append('startTime', startTime);
  if (endTime) queryParams.append('endTime', endTime);

  const response = await fetch(`${bffUrl}/api/v1/telemetry/events/component?${queryParams.toString()}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener eventos de telemetría por componente' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
