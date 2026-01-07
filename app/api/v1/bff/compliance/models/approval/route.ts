import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const search = searchParams.get("search") || "";
      const riskLevel = searchParams.get("riskLevel") || "";
      const owner = searchParams.get("owner") || "";

      // Mock data
      const mockApprovals = [
        {
          id: 1,
          modelName: "GPT-4 Fine-tuned",
          modelType: "TEXT",
          owner: "admin@example.com",
          riskLevel: "HIGH",
          requestDate: "2024-01-15T10:30:00",
          status: "PENDING",
        },
        {
          id: 2,
          modelName: "Claude Vision",
          modelType: "MULTIMODAL",
          owner: "user@example.com",
          riskLevel: "MEDIUM",
          requestDate: "2024-01-14T14:20:00",
          status: "PENDING",
        },
        {
          id: 3,
          modelName: "Stable Diffusion",
          modelType: "IMAGE",
          owner: "dev@example.com",
          riskLevel: "LOW",
          requestDate: "2024-01-13T09:15:00",
          status: "PENDING",
        },
      ];

      let filteredApprovals = [...mockApprovals];

      // Aplicar filtros
      if (search) {
        const searchLower = search.toLowerCase();
        filteredApprovals = filteredApprovals.filter(
          (approval) =>
            approval.modelName.toLowerCase().includes(searchLower) ||
            approval.owner.toLowerCase().includes(searchLower)
        );
      }

      if (riskLevel && riskLevel !== "ALL") {
        filteredApprovals = filteredApprovals.filter(
          (approval) => approval.riskLevel === riskLevel
        );
      }

      if (owner) {
        filteredApprovals = filteredApprovals.filter((approval) =>
          approval.owner.toLowerCase().includes(owner.toLowerCase())
        );
      }

      return NextResponse.json(filteredApprovals);
    } catch (error) {
      console.error("Error loading mock approvals:", error);
      return NextResponse.json(
        { error: "Error loading approvals" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(
    `${BFF_BASE_URL}/api/v1/bff/compliance/models/approval${queryString ? `?${queryString}` : ""}`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al obtener aprobaciones" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
