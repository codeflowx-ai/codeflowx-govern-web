/**
 * API Route para parámetros de cálculo
 * Proxy hacia el backend Java
 */

import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';
import { readFile } from 'fs/promises';
import { join } from 'path';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export async function GET(request: NextRequest) {
  try {
    // Si está en modo mock, usar datos mock
    if (USE_MOCK) {
      try {
        const mockPath = join(process.cwd(), 'mocks', 'governance', 'calculation-parameters.json');
        const mockData = await readFile(mockPath, 'utf-8');
        let parameters = JSON.parse(mockData);

        // Aplicar filtros si existen
        const searchParams = request.nextUrl.searchParams;
        const microservice = searchParams.get('microservice');
        const parameterKey = searchParams.get('parameterKey');
        const organizationId = searchParams.get('organizationId');

        if (microservice) {
          parameters = parameters.filter((p: any) => p.microservice === microservice);
        }
        if (parameterKey) {
          parameters = parameters.filter((p: any) => p.parameterKey === parameterKey);
        }
        if (organizationId) {
          parameters = parameters.filter((p: any) => p.organizationId === organizationId);
        }

        return NextResponse.json(parameters);
      } catch (error) {
        console.error('Error loading mock calculation parameters:', error);
        // Fallback a datos mock hardcodeados
        const fallbackMock = [
          {
            id: 1,
            uuid: "a1b2c3d4-e5f6-7890-1234-567890abcdef",
            microservice: "board-governance",
            organizationId: null,
            parameterKey: "edm_metrics",
            parameters: {
              weights: { evaluate: 0.35, direct: 0.35, monitor: 0.30 },
              thresholds: { excellent: 0.90, healthy: 0.75, moderate: 0.60 }
            },
            version: 1,
            active: true,
            createdAt: "2024-11-01T10:00:00Z",
            updatedAt: "2024-11-01T10:00:00Z"
          }
        ];
        return NextResponse.json(fallbackMock);
      }
    }

    // Si no está en modo mock, llamar al backend real
    const searchParams = request.nextUrl.searchParams;
    const microservice = searchParams.get('microservice');
    const parameterKey = searchParams.get('parameterKey');
    const organizationId = searchParams.get('organizationId');

    const params = new URLSearchParams();
    if (microservice) params.append('microservice', microservice);
    if (parameterKey) params.append('parameterKey', parameterKey);
    if (organizationId) params.append('organizationId', organizationId);

    const url = `${API_BASE_URL}/api/governance/calculation-parameters${params.toString() ? '?' + params.toString() : ''}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      cache: 'no-store',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching calculation parameters:', error);
    return NextResponse.json(
      { error: 'Error al obtener parámetros' },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Si está en modo mock, simular creación
    if (USE_MOCK) {
      const newParameter = {
        id: Math.floor(Math.random() * 10000),
        uuid: `mock-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
        ...body,
        version: 1,
        active: body.active !== undefined ? body.active : true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        createdBy: 'mock-user',
        updatedBy: 'mock-user'
      };
      return NextResponse.json(newParameter, { status: 201 });
    }

    // Si no está en modo mock, llamar al backend real
    const url = `${API_BASE_URL}/api/governance/calculation-parameters`;

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return NextResponse.json(
        { error: errorData.message || 'Error al crear parámetro' },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    console.error('Error creating calculation parameter:', error);
    return NextResponse.json(
      { error: 'Error al crear parámetro' },
      { status: 500 }
    );
  }
}
