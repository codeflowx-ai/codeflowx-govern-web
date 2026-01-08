import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK } from "@/app/config/mock";

// Mock data para orígenes
const originsMock = {
  origins: [
    {
      idxorigin: 1,
      iduuid: "550e8400-e29b-41d4-a716-446655440010",
      dtgorname: "HuggingFace Hub",
      dtgordescription: "Repositorio de datasets de HuggingFace",
      dtgortype: "EXTERNAL",
      dtgorcategory: "MARKETPLACE",
      dtgorurl: "https://huggingface.co/datasets",
      dtgorconnectionconfig: JSON.stringify({
        apiKey: "***",
        endpoint: "https://huggingface.co/api/datasets",
      }),
      dtgorcredentialsid: 1,
      dtgorstatus: "ACTIVE",
      dtgorenabled: true,
      dtgorlastsynced: "2025-12-14T10:00:00Z",
      dtgorlastsyncstatus: "SUCCESS",
      dtgorlastsyncerror: null,
      dtgorcreatedat: "2025-12-01T09:00:00Z",
      dtgorcreatedby: 1,
      dtgorupdatedat: "2025-12-14T10:00:00Z",
      dtgorupdatedby: 1,
    },
    {
      idxorigin: 2,
      iduuid: "550e8400-e29b-41d4-a716-446655440011",
      dtgorname: "Kaggle API",
      dtgordescription: "API de Kaggle para descargar datasets",
      dtgortype: "EXTERNAL",
      dtgorcategory: "MARKETPLACE",
      dtgorurl: "https://www.kaggle.com/datasets",
      dtgorconnectionconfig: JSON.stringify({
        username: "user",
        apiKey: "***",
      }),
      dtgorcredentialsid: 2,
      dtgorstatus: "ACTIVE",
      dtgorenabled: true,
      dtgorlastsynced: "2025-12-14T09:30:00Z",
      dtgorlastsyncstatus: "SUCCESS",
      dtgorlastsyncerror: null,
      dtgorcreatedat: "2025-12-02T10:00:00Z",
      dtgorcreatedby: 1,
      dtgorupdatedat: "2025-12-14T09:30:00Z",
      dtgorupdatedby: 1,
    },
    {
      idxorigin: 3,
      iduuid: "550e8400-e29b-41d4-a716-446655440012",
      dtgorname: "PostgreSQL Production DB",
      dtgordescription: "Base de datos PostgreSQL de producción",
      dtgortype: "INTERNAL",
      dtgorcategory: "DATABASE",
      dtgorurl: null,
      dtgorhost: "db.production.internal",
      dtgorport: 5432,
      dtgordatabase: "production",
      dtgorconnectionconfig: JSON.stringify({
        host: "db.production.internal",
        port: 5432,
        database: "production",
      }),
      dtgorcredentialsid: 3,
      dtgorstatus: "ACTIVE",
      dtgorenabled: true,
      dtgorlastsynced: "2025-12-14T10:15:00Z",
      dtgorlastsyncstatus: "SUCCESS",
      dtgorlastsyncerror: null,
      dtgorcreatedat: "2025-12-03T08:00:00Z",
      dtgorcreatedby: 2,
      dtgorupdatedat: "2025-12-14T10:15:00Z",
      dtgorupdatedby: 2,
    },
    {
      idxorigin: 4,
      iduuid: "550e8400-e29b-41d4-a716-446655440013",
      dtgorname: "Internal API - Sales",
      dtgordescription: "API interna de ventas",
      dtgortype: "INTERNAL",
      dtgorcategory: "API",
      dtgorurl: "https://api.internal.company.com/sales",
      dtgorconnectionconfig: JSON.stringify({
        baseUrl: "https://api.internal.company.com",
        endpoint: "/sales",
        method: "GET",
      }),
      dtgorcredentialsid: 4,
      dtgorstatus: "ACTIVE",
      dtgorenabled: true,
      dtgorlastsynced: "2025-12-14T10:20:00Z",
      dtgorlastsyncstatus: "SUCCESS",
      dtgorlastsyncerror: null,
      dtgorcreatedat: "2025-12-04T11:00:00Z",
      dtgorcreatedby: 1,
      dtgorupdatedat: "2025-12-14T10:20:00Z",
      dtgorupdatedby: 1,
    },
    {
      idxorigin: 5,
      iduuid: "550e8400-e29b-41d4-a716-446655440014",
      dtgorname: "S3 Bucket - Raw Data",
      dtgordescription: "Bucket S3 con datos en bruto",
      dtgortype: "INTERNAL",
      dtgorcategory: "CLOUD_STORAGE",
      dtgorurl: null,
      dtgorconnectionconfig: JSON.stringify({
        bucket: "raw-data-bucket",
        region: "us-east-1",
      }),
      dtgorcredentialsid: 5,
      dtgorstatus: "ACTIVE",
      dtgorenabled: true,
      dtgorlastsynced: "2025-12-14T09:45:00Z",
      dtgorlastsyncstatus: "SUCCESS",
      dtgorlastsyncerror: null,
      dtgorcreatedat: "2025-12-05T14:00:00Z",
      dtgorcreatedby: 1,
      dtgorupdatedat: "2025-12-14T09:45:00Z",
      dtgorupdatedby: 1,
    },
    {
      idxorigin: 6,
      iduuid: "550e8400-e29b-41d4-a716-446655440015",
      dtgorname: "External API - Weather Service",
      dtgordescription: "API externa de datos meteorológicos",
      dtgortype: "EXTERNAL",
      dtgorcategory: "API",
      dtgorurl: "https://api.weather.com/v1",
      dtgorconnectionconfig: JSON.stringify({
        apiKey: "***",
        endpoint: "/forecast",
      }),
      dtgorcredentialsid: 6,
      dtgorstatus: "ERROR",
      dtgorenabled: false,
      dtgorlastsynced: "2025-12-14T08:00:00Z",
      dtgorlastsyncstatus: "FAILED",
      dtgorlastsyncerror: "Connection timeout",
      dtgorcreatedat: "2025-12-06T10:00:00Z",
      dtgorcreatedby: 1,
      dtgorupdatedat: "2025-12-14T08:00:00Z",
      dtgorupdatedby: 1,
    },
  ],
  totalElements: 6,
  totalPages: 1,
  currentPage: 0,
  pageSize: 20,
};

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const page = parseInt(searchParams.get("page") || "0");
      const size = parseInt(searchParams.get("size") || "20");
      const search = searchParams.get("search") || "";
      const type = searchParams.get("type") || "";
      const status = searchParams.get("status") || "";

      let filteredOrigins = [...originsMock.origins];

      // Aplicar filtros
      if (search) {
        const searchLower = search.toLowerCase();
        filteredOrigins = filteredOrigins.filter(
          (origin) =>
            origin.dtgorname.toLowerCase().includes(searchLower) ||
            origin.dtgordescription?.toLowerCase().includes(searchLower) ||
            origin.dtgorcategory.toLowerCase().includes(searchLower)
        );
      }

      if (type) {
        filteredOrigins = filteredOrigins.filter(
          (origin) => origin.dtgortype === type
        );
      }

      if (status) {
        filteredOrigins = filteredOrigins.filter(
          (origin) => origin.dtgorstatus === status
        );
      }

      // Paginación
      const startIndex = page * size;
      const endIndex = startIndex + size;
      const paginatedOrigins = filteredOrigins.slice(startIndex, endIndex);
      const totalPages = Math.ceil(filteredOrigins.length / size);

      return NextResponse.json({
        origins: paginatedOrigins,
        totalElements: filteredOrigins.length,
        totalPages,
        currentPage: page,
        pageSize: size,
      });
    } catch (error) {
      console.error("Error loading mock origins:", error);
      return NextResponse.json(
        { error: "Error loading origins" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const gatewayUrl = process.env.NEXT_PUBLIC_BFF_GOVERNANCE_URL || 'http://localhost:8082';
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(`${gatewayUrl}/web/api/v1/governance/data/origins${queryString ? `?${queryString}` : ''}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener orígenes' },
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
      const newId = Math.max(...originsMock.origins.map(o => o.idxorigin)) + 1;
      const newUuid = `550e8400-e29b-41d4-a716-4466554400${String(newId).padStart(2, '0')}`;

      const newOrigin = {
        idxorigin: newId,
        iduuid: newUuid,
        ...body,
        dtgorcreatedat: new Date().toISOString(),
        dtgorcreatedby: 1,
        dtgorupdatedat: new Date().toISOString(),
        dtgorupdatedby: 1,
        dtgorlastsynced: null,
        dtgorlastsyncstatus: null,
        dtgorlastsyncerror: null,
      };

      originsMock.origins.push(newOrigin);
      originsMock.totalElements = originsMock.origins.length;

      return NextResponse.json({
        success: true,
        message: "Origin created successfully",
        origin: newOrigin,
        idxorigin: newId,
      });
    } catch (error) {
      console.error("Error creating mock origin:", error);
      return NextResponse.json(
        { error: "Error creating origin" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const gatewayUrl = process.env.NEXT_PUBLIC_BFF_GOVERNANCE_URL || 'http://localhost:8082';
  const body = await request.json();

  const response = await fetch(`${gatewayUrl}/web/api/v1/governance/data/origins`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al crear origen' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
