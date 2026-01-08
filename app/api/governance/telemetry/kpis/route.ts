import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const startTime = searchParams.get('startTime');
      const endTime = searchParams.get('endTime');

      // Si no hay parámetros de tiempo, usar datos por defecto
      if (!startTime || !endTime) {
        const mockData = await import('@/mocks/governance/telemetry/kpis.json');
        return NextResponse.json(mockData.default || mockData);
      }

      // Parsear fechas
      const start = new Date(startTime);
      const end = new Date(endTime);
      const year = start.getFullYear();
      const month = start.getMonth();

      // Solo generar datos para 2024 y 2025
      if (year !== 2024 && year !== 2025) {
        return NextResponse.json({
          totalEvents: 0,
          totalCostUsd: 0,
          totalTokens: 0,
          avgLatencyMs: 0,
          componentCount: 0,
          startTime: startTime,
          endTime: endTime,
        });
      }

      // Generar datos variados según el mes y año con patrones realistas
      // Patrones estacionales: verano más alto, invierno más bajo
      const monthMultipliers: Record<number, number> = {
        0: 0.55,   // Enero - menor actividad post-navidad
        1: 0.65,   // Febrero - bajo
        2: 0.80,   // Marzo - recuperación
        3: 0.95,   // Abril - creciendo
        4: 1.15,   // Mayo - alta actividad
        5: 1.35,   // Junio - muy alta (inicio verano)
        6: 1.50,   // Julio - máximo (verano)
        7: 1.40,   // Agosto - alta (verano)
        8: 1.10,   // Septiembre - vuelta al trabajo
        9: 1.05,   // Octubre - estable
        10: 0.90,  // Noviembre - bajando
        11: 0.70,  // Diciembre - menor actividad por festividades
      };

      // Ajuste por año: 2025 tiene 25% más actividad que 2024 (crecimiento realista)
      const yearMultiplier = year === 2025 ? 1.25 : 1.0;

      // Bases diferentes por año para mayor realismo
      const baseEvents2024 = 28000;
      const baseEvents2025 = 35000;
      const baseEvents = year === 2025 ? baseEvents2025 : baseEvents2024;

      const baseCost2024 = 210.0;
      const baseCost2025 = 265.0;
      const baseCost = year === 2025 ? baseCost2025 : baseCost2024;

      const baseTokens2024 = 1200000;
      const baseTokens2025 = 1500000;
      const baseTokens = year === 2025 ? baseTokens2025 : baseTokens2024;

      const baseLatency2024 = 320.0;
      const baseLatency2025 = 295.0; // Mejor latencia en 2025 (optimizaciones)
      const baseLatency = year === 2025 ? baseLatency2025 : baseLatency2024;

      const monthMultiplier = monthMultipliers[month] || 1.0;
      const finalMultiplier = monthMultiplier * yearMultiplier;

      // Agregar variación aleatoria más amplia (±15%) para mayor realismo
      const randomVariation = 0.85 + Math.random() * 0.3; // Entre 0.85 y 1.15

      // Calcular valores finales con variaciones
      const totalEvents = Math.round(baseEvents * finalMultiplier * randomVariation);
      const totalCostUsd = parseFloat((baseCost * finalMultiplier * randomVariation).toFixed(2));
      const totalTokens = Math.round(baseTokens * finalMultiplier * randomVariation);

      // Latencia con variación según mes (verano puede tener más latencia por carga)
      const latencyVariation = month >= 5 && month <= 7 ? 20 : -10; // Verano +20ms, otros -10ms
      const avgLatencyMs = parseFloat((baseLatency + latencyVariation + (Math.random() * 40 - 20)).toFixed(1));

      return NextResponse.json({
        totalEvents,
        totalCostUsd,
        totalTokens,
        avgLatencyMs,
        componentCount: 8,
        startTime: startTime,
        endTime: endTime,
      });
    } catch (error) {
      console.error('Error loading mock data:', error);
      // Fallback a datos hardcodeados si falla la importación
      return NextResponse.json({
        totalEvents: 125000,
        totalCostUsd: 1250.50,
        totalTokens: 5000000,
        avgLatencyMs: 250.5,
        componentCount: 15,
        startTime: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString(),
        endTime: new Date().toISOString(),
      });
    }
  }

  const bffUrl = process.env.NEXT_PUBLIC_BFF_TELEMETRY_URL || 'http://localhost:8084';
  const searchParams = request.nextUrl.searchParams;
  const startTime = searchParams.get('startTime');
  const endTime = searchParams.get('endTime');

  const queryParams = new URLSearchParams();
  if (startTime) queryParams.append('startTime', startTime);
  if (endTime) queryParams.append('endTime', endTime);

  const response = await fetch(`${bffUrl}/api/v1/telemetry/kpis${queryParams.toString() ? `?${queryParams.toString()}` : ''}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener KPIs de telemetría' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
