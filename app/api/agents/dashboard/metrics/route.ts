import { NextResponse } from "next/server";

export async function GET() {
  // Mock data para desarrollo
  const mockData = {
    totalAgents: 24,
    activeAgents: 18,
    deployedAgents: 15,
    healthScore: 87,
    avgPerformanceScore: 92,
    offlineAgents: 3,
    recentAgents: [
      { id: 1, name: "Agent Alpha", status: "ACTIVE", version: "v2.1.0" },
      { id: 2, name: "Agent Beta", status: "ACTIVE", version: "v1.9.5" },
      { id: 3, name: "Agent Gamma", status: "DEPLOYED", version: "v2.0.3" },
      { id: 4, name: "Agent Delta", status: "ACTIVE", version: "v1.8.2" },
    ],
  };

  return NextResponse.json(mockData);
}
