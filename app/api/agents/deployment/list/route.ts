import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      {
        id: "d1e2f3a4-b5c6-4789-d012-345678901234",
        uuid: "d1e2f3a4-b5c6-4789-d012-345678901234",
        agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
        agentName: "Credit Scoring Agent",
        deploymentType: "PRODUCTION",
        status: "ACTIVE",
        environment: "prod",
        deployedAt: "2024-01-15",
      },
      {
        id: "e2f3a4b5-c6d7-4890-e123-456789012345",
        uuid: "e2f3a4b5-c6d7-4890-e123-456789012345",
        agentUuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
        agentName: "Document OCR Agent",
        deploymentType: "STAGING",
        status: "PENDING",
        environment: "staging",
        deployedAt: "2024-01-16",
      },
      {
        id: "f3a4b5c6-d7e8-4901-f234-567890123456",
        uuid: "f3a4b5c6-d7e8-4901-f234-567890123456",
        agentUuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
        agentName: "Fraud Detection Agent",
        deploymentType: "DEVELOPMENT",
        status: "ACTIVE",
        environment: "dev",
        deployedAt: "2024-01-17",
      },
    ],
    total: 3,
    active: 2,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
