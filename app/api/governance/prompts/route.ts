import { NextRequest, NextResponse } from "next/server";

const PROMPTS_SERVICE_URL = process.env.PROMPTS_SERVICE_URL || "http://localhost:8084";

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const page = searchParams.get("page") || "0";
    const size = searchParams.get("size") || "10";
    const projectId = searchParams.get("projectId");
    const status = searchParams.get("status");
    const search = searchParams.get("search");

    // Construir URL con query params
    const url = new URL(`${PROMPTS_SERVICE_URL}/api/v1/governance/prompts`);
    url.searchParams.set("page", page);
    url.searchParams.set("size", size);
    if (projectId) url.searchParams.set("projectId", projectId);
    if (status) url.searchParams.set("status", status);
    if (search) url.searchParams.set("search", search);

    const response = await fetch(url.toString(), {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Prompts service error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error fetching prompts:", error);
    return NextResponse.json(
      { error: "Error al obtener prompts", items: [], total: 0, page: 0, size: 10, totalPages: 0 },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    const response = await fetch(`${PROMPTS_SERVICE_URL}/api/v1/governance/prompts`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      throw new Error(`Prompts service error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error creating prompt:", error);
    return NextResponse.json(
      { error: "Error al crear prompt" },
      { status: 500 }
    );
  }
}
