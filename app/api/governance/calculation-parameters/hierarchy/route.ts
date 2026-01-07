/**
 * API Route para obtener jerarquía de parámetros (global + organizaciones)
 */

import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';
import { readFile } from 'fs/promises';
import { join } from 'path';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const microservice = searchParams.get('microservice');
    const parameterKey = searchParams.get('parameterKey');

    if (!microservice || !parameterKey) {
      return NextResponse.json(
        { error: 'microservice y parameterKey son requeridos' },
        { status: 400 }
      );
    }

    // Si está en modo mock, usar datos mock
    if (USE_MOCK) {
      try {
        const mockPath = join(process.cwd(), 'mocks', 'governance', 'calculation-parameters.json');
        const mockData = await readFile(mockPath, 'utf-8');
        const parameters = JSON.parse(mockData);

        // Filtrar por microservicio y clave
        const filtered = parameters.filter(
          (p: any) => p.microservice === microservice && p.parameterKey === parameterKey
        );

        // Separar globales y por organización
        const global = filtered.find((p: any) => p.organizationId === null) || null;
        const organizations = filtered.filter((p: any) => p.organizationId !== null);

        return NextResponse.json({
          global,
          organizations
        });
      } catch (error) {
        console.error('Error loading mock hierarchy:', error);
        return NextResponse.json(
          { error: 'Error al cargar jerarquía mock' },
          { status: 500 }
        );
      }
    }

    // Si no está en modo mock, llamar al backend real
    const params = new URLSearchParams({ microservice, parameterKey });
    const url = `${API_BASE_URL}/api/governance/calculation-parameters/hierarchy?${params.toString()}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      cache: 'no-store',
    });

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Error al obtener jerarquía' },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error('Error fetching hierarchy:', error);
    return NextResponse.json(
      { error: 'Error al obtener jerarquía' },
      { status: 500 }
    );
  }
}
