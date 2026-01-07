import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK } from "@/app/config/mock";
import datasetsMock from "@/mocks/governance/data/datasets.json";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const page = parseInt(searchParams.get("page") || "0");
      const size = parseInt(searchParams.get("size") || "20");
      const search = searchParams.get("search") || "";
      const type = searchParams.get("type") || "";
      const originType = searchParams.get("originType") || "";
      const status = searchParams.get("status") || "";
      const standardized = searchParams.get("standardized") || "";

      let filteredDatasets = [...datasetsMock.datasets];

      // Aplicar filtros
      if (search) {
        const searchLower = search.toLowerCase();
        filteredDatasets = filteredDatasets.filter(
          (dataset) =>
            dataset.dtgname.toLowerCase().includes(searchLower) ||
            dataset.dtgdescription?.toLowerCase().includes(searchLower)
        );
      }

      if (type) {
        filteredDatasets = filteredDatasets.filter(
          (dataset) => dataset.dtgtype === type
        );
      }

      if (originType) {
        filteredDatasets = filteredDatasets.filter(
          (dataset) => dataset.dtgorigintype === originType
        );
      }

      if (status) {
        filteredDatasets = filteredDatasets.filter(
          (dataset) => dataset.dtgstatus === status
        );
      }

      if (standardized !== "") {
        const isStandardized = standardized === "true";
        filteredDatasets = filteredDatasets.filter(
          (dataset) => dataset.dtgstandardized === isStandardized
        );
      }

      // Paginación
      const startIndex = page * size;
      const endIndex = startIndex + size;
      const paginatedDatasets = filteredDatasets.slice(startIndex, endIndex);
      const totalPages = Math.ceil(filteredDatasets.length / size);

      return NextResponse.json({
        datasets: paginatedDatasets,
        totalElements: filteredDatasets.length,
        totalPages,
        currentPage: page,
        pageSize: size,
      });
    } catch (error) {
      console.error("Error loading mock datasets:", error);
      return NextResponse.json(
        { error: "Error loading datasets" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const bffUrl = process.env.NEXT_PUBLIC_BFF_GOVERNANCE_URL || 'http://localhost:8082';
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(`${bffUrl}/api/v1/governance/data/datasets${queryString ? `?${queryString}` : ''}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener datasets' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}

export async function POST(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const body = await request.json();

      // Generar nuevo ID
      const newId = Math.max(...datasetsMock.datasets.map(d => d.idxdataset)) + 1;
      const newUuid = `550e8400-e29b-41d4-a716-44665544000${String(newId).padStart(2, '0')}`;

      const newDataset = {
        idxdataset: newId,
        iduuid: newUuid,
        dtgname: body.dtgname,
        dtgdescription: body.dtgdescription || "",
        dtgtype: body.dtgtype || "TRAINING",
        dtgorigintype: body.dtgorigintype || "EXTERNAL",
        idxorigin: body.idxorigin || null,
        dtgformat: body.dtgformat || "CSV",
        dtgfilepath: body.sourceId || body.sourceUrl || null,
        dtgfilesize: undefined,
        dtgchecksum: undefined,
        dtgqualityscore: undefined,
        dtgbiasscore: undefined,
        dtgrepresentativityscore: undefined,
        dtgcompliancestatus: "REVIEW",
        dtgversion: "1.0.0",
        dtgversionhash: null,
        idxparentdataset: null,
        dtgstatus: "DRAFT",
        dtgapproved: false,
        dtgapprovedby: null,
        dtgapprovedat: null,
        dtgstoragetype: "MINIO",
        dtgstoragepath: null,
        dtgsize: null,
        dtgrecordcount: null,
        dtgstandardized: body.dtgstandardized || false,
        dtgstandardizedformat: body.dtgstandardized ? "PARQUET" : null,
        dtgstandardizedpath: null,
        dtglastsynced: null,
        dtglastsyncstatus: null,
        dtglastsyncerror: null,
        dtgnextsync: null,
        dtgcreatedat: new Date().toISOString(),
        dtgcreatedby: 1,
        dtgupdatedat: new Date().toISOString(),
        dtgupdatedby: 1,
        dtgarchivedat: null,
        dtgarchivedby: null,
        idxproject: body.idxproject || null,
      };

      datasetsMock.datasets.push(newDataset as any);
      datasetsMock.totalElements = datasetsMock.datasets.length;

      return NextResponse.json({
        success: true,
        message: "Dataset created successfully",
        dataset: newDataset,
        idxdataset: newId,
      });
    } catch (error) {
      console.error("Error creating mock dataset:", error);
      return NextResponse.json(
        { error: "Error creating dataset" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const bffUrl = process.env.NEXT_PUBLIC_BFF_GOVERNANCE_URL || 'http://localhost:8082';
  const body = await request.json();

  const response = await fetch(`${bffUrl}/api/v1/governance/data/datasets`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al crear dataset' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
