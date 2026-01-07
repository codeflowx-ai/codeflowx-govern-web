import { NextResponse } from "next/server";
import { mockAnnexIIICategories } from "@/app/(app)/governance/data/mockClassification";

export async function GET(request: Request) {
  try {
    // Retornar todas las categorías del Anexo III
    return NextResponse.json({
      success: true,
      categories: mockAnnexIIICategories,
    });
  } catch (error) {
    console.error("Error in categories API:", error);
    return NextResponse.json(
      { success: false, error: "Internal server error" },
      { status: 500 }
    );
  }
}
