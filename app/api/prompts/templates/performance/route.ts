import { NextResponse } from "next/server";

export async function GET() {
  // Mock data para desarrollo
  const mockData = {
    items: [
      {
        totalItems: 150,
        activeItems: 120,
        deployedItems: 80,
        trainingItems: 30,
        offlineItems: 10,
        avgScore: 8.5,
      },
      {
        totalItems: 200,
        activeItems: 180,
        deployedItems: 150,
        trainingItems: 20,
        offlineItems: 0,
        avgScore: 9.2,
      },
      {
        totalItems: 95,
        activeItems: 70,
        deployedItems: 50,
        trainingItems: 15,
        offlineItems: 10,
        avgScore: 7.8,
      },
      {
        totalItems: 300,
        activeItems: 280,
        deployedItems: 250,
        trainingItems: 20,
        offlineItems: 0,
        avgScore: 9.5,
      },
      {
        totalItems: 85,
        activeItems: 60,
        deployedItems: 40,
        trainingItems: 20,
        offlineItems: 5,
        avgScore: 7.2,
      },
    ],
    total: 5,
    page: 1,
    pageSize: 10,
  };

  return NextResponse.json(mockData);
}
