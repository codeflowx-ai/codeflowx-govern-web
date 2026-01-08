import { NextResponse } from "next/server";
import { NextRequest } from "next/server";

const BFF_BASE_URL = process.env.BFF_BASE_URL || "http://localhost:8084";

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const page = searchParams.get("page") || "0";
    const size = searchParams.get("size") || "20";
    const status = searchParams.get("status") || null;
    const search = searchParams.get("search") || null;

    // Construir URL con query params
    let url = `${BFF_BASE_URL}/api/v1/governance/agents/registry/list?page=${page}&size=${size}`;
    if (status) url += `&status=${status}`;
    if (search) url += `&search=${search}`;

    // Llamar al BFF real
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`BFF returned ${response.status}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error loading agents:", error);
    // Fallback a mock data si el BFF no está disponible
    const mockData = {
      items: [
        {
          id: 1,
          uuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          name: "Credit Scoring Agent",
          description: "Agent for credit risk assessment",
          status: "ACTIVE",
          version: "v2.1.0",
          domain: "Finance",
          createdAt: "2024-01-10",
          lastModified: "2024-01-15",
        },
        {
          id: 2,
          uuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
          name: "Document OCR Agent",
          description: "Optical character recognition agent",
          status: "ACTIVE",
          version: "v1.9.5",
          domain: "Document Processing",
          createdAt: "2024-01-08",
          lastModified: "2024-01-14",
        },
        {
          id: 3,
          uuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
          name: "Fraud Detection Agent",
          description: "Real-time fraud detection agent",
          status: "PENDING",
          version: "v2.0.3",
          domain: "Security",
          createdAt: "2024-01-12",
          lastModified: "2024-01-16",
        },
        {
          id: 4,
          uuid: "d4e5f6a7-b8c9-4012-d345-678901234567",
          name: "Invoice Processing Agent",
          description: "Automated invoice processing and validation",
          status: "ACTIVE",
          version: "v1.8.2",
          domain: "Finance",
          createdAt: "2024-01-09",
          lastModified: "2024-01-13",
        },
        {
          id: 5,
          uuid: "e5f6a7b8-c9d0-4123-e456-789012345678",
          name: "Customer Onboarding Agent",
          description: "Streamlined customer onboarding process",
          status: "ACTIVE",
          version: "v2.2.1",
          domain: "Customer Service",
          createdAt: "2024-01-11",
          lastModified: "2024-01-17",
        },
        {
          id: 6,
          uuid: "f6a7b8c9-d0e1-4234-f567-890123456789",
          name: "Risk Assessment Agent",
          description: "Comprehensive risk analysis and assessment",
          status: "DEPLOYED",
          version: "v1.7.0",
          domain: "Finance",
          createdAt: "2024-01-07",
          lastModified: "2024-01-12",
        },
      ],
      total: 6,
      active: 4,
      pending: 1,
    };

    return NextResponse.json(mockData);
  }
}
