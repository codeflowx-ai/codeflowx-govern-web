import { NextRequest, NextResponse } from "next/server";
import { DatabaseIntegration } from "@/app/(app)/governance/data/types/integrations";

// Mock data para integraciones
const mockIntegrations: DatabaseIntegration[] = [
  {
    idxintegration: 1,
    iduuid: "550e8400-e29b-41d4-a716-446655440001",
    dtgname: "PostgreSQL Producción",
    dtgdescription: "Base de datos principal de producción",
    dtgdatabasetype: "POSTGRESQL",
    dtghost: "prod-db.example.com",
    dtgport: 5432,
    dtgdatabase: "production",
    dtgauthenticationtype: "USERNAME_PASSWORD",
    dtgusername: "app_user",
    dtgsslrequired: true,
    dtgsslmode: "require",
    dtgtimeout: 30,
    dtgpoolsize: 10,
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 1,
    dtglastsyncat: "2025-01-14T10:30:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-01T00:00:00Z",
    originsCount: 3,
    datasetsCount: 15,
  },
  {
    idxintegration: 2,
    iduuid: "550e8400-e29b-41d4-a716-446655440002",
    dtgname: "MongoDB Analytics",
    dtgdescription: "Base de datos NoSQL para análisis",
    dtgdatabasetype: "MONGODB",
    dtghost: "mongodb-analytics.example.com",
    dtgport: 27017,
    dtgdatabase: "analytics",
    dtgauthenticationtype: "USERNAME_PASSWORD",
    dtgusername: "analytics_user",
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 6,
    dtglastsyncat: "2025-01-14T08:00:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-05T00:00:00Z",
    originsCount: 1,
    datasetsCount: 8,
  },
  {
    idxintegration: 3,
    iduuid: "550e8400-e29b-41d4-a716-446655440003",
    dtgname: "S3 Data Lake",
    dtgdescription: "Almacenamiento de objetos en S3",
    dtgdatabasetype: "S3",
    dtghost: "s3.amazonaws.com",
    dtgdatabase: "data-lake-bucket",
    dtgauthenticationtype: "AWS_IAM",
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 24,
    dtglastsyncat: "2025-01-13T12:00:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 2,
    datasetsCount: 45,
  },
  {
    idxintegration: 4,
    iduuid: "550e8400-e29b-41d4-a716-446655440004",
    dtgname: "Databricks Workspace",
    dtgdescription: "Workspace de Databricks para ML",
    dtgdatabasetype: "DATABRICKS",
    dtghost: "workspace.cloud.databricks.com",
    dtgauthenticationtype: "OAUTH2",
    dtgclientid: "databricks-client",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-12T00:00:00Z",
    originsCount: 0,
    datasetsCount: 0,
  },
  {
    idxintegration: 5,
    iduuid: "550e8400-e29b-41d4-a716-446655440005",
    dtgname: "Snowflake Analytics",
    dtgdescription: "Snowflake para BI y analítica",
    dtgdatabasetype: "SNOWFLAKE",
    dtghost: "acme.snowflakecomputing.com",
    dtgauthenticationtype: "USERNAME_PASSWORD",
    dtgusername: "sf_user",
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 12,
    dtglastsyncat: "2025-01-14T07:00:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-09T00:00:00Z",
    originsCount: 4,
    datasetsCount: 22,
  },
  {
    idxintegration: 6,
    iduuid: "550e8400-e29b-41d4-a716-446655440006",
    dtgname: "Cassandra Events",
    dtgdescription: "Cassandra para eventos de alta escala",
    dtgdatabasetype: "CASSANDRA",
    dtghost: "cassandra.events.example.com",
    dtgport: 9042,
    dtgdatabase: "events",
    dtgauthenticationtype: "USERNAME_PASSWORD",
    dtgusername: "events_user",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-11T00:00:00Z",
    originsCount: 1,
    datasetsCount: 6,
  },
  {
    idxintegration: 7,
    iduuid: "550e8400-e29b-41d4-a716-446655440007",
    dtgname: "HuggingFace Hub",
    dtgdescription: "Modelos y datasets desde HuggingFace",
    dtgdatabasetype: "HUGGINGFACE",
    dtgauthenticationtype: "API_KEY",
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 24,
    dtglastsyncat: "2025-01-13T18:00:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 5,
    datasetsCount: 18,
  },
  {
    idxintegration: 8,
    iduuid: "550e8400-e29b-41d4-a716-446655440008",
    dtgname: "SageMaker Registry",
    dtgdescription: "Model registry y endpoints en AWS SageMaker",
    dtgdatabasetype: "SAGEMAKER",
    dtgauthenticationtype: "AWS_IAM",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 0,
    datasetsCount: 0,
  },
  {
    idxintegration: 9,
    iduuid: "550e8400-e29b-41d4-a716-446655440009",
    dtgname: "Vertex AI",
    dtgdescription: "Modelos gestionados en Vertex AI",
    dtgdatabasetype: "VERTEX_AI",
    dtgauthenticationtype: "GCP_SERVICE_ACCOUNT",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 0,
    datasetsCount: 0,
  },
  {
    idxintegration: 10,
    iduuid: "550e8400-e29b-41d4-a716-446655440010",
    dtgname: "Azure ML",
    dtgdescription: "Model registry y endpoints en Azure ML",
    dtgdatabasetype: "AZURE_ML",
    dtgauthenticationtype: "AZURE_AD",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 0,
    datasetsCount: 0,
  },
  {
    idxintegration: 11,
    iduuid: "550e8400-e29b-41d4-a716-446655440011",
    dtgname: "MLflow Standalone",
    dtgdescription: "Registro de modelos MLflow",
    dtgdatabasetype: "MLFLOW",
    dtgauthenticationtype: "NONE",
    dtgenabled: true,
    dtgsyncenabled: false,
    dtgcreatedat: "2025-01-10T00:00:00Z",
    originsCount: 0,
    datasetsCount: 0,
  },
  {
    idxintegration: 12,
    iduuid: "550e8400-e29b-41d4-a716-446655440012",
    dtgname: "BigQuery Marketing",
    dtgdescription: "Almacén analítico BigQuery",
    dtgdatabasetype: "BIGQUERY",
    dtgauthenticationtype: "GCP_SERVICE_ACCOUNT",
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 12,
    dtglastsyncat: "2025-01-14T06:00:00Z",
    dtgsyncstatus: "SUCCESS",
    dtgcreatedat: "2025-01-07T00:00:00Z",
    originsCount: 2,
    datasetsCount: 12,
  },
];

export async function GET(request: NextRequest) {
  try {
    // En producción, aquí se haría la llamada al BFF
    // const response = await fetch(`${BFF_URL}/api/v1/governance/data/integrations`);
    // return NextResponse.json(await response.json());

    return NextResponse.json(mockIntegrations);
  } catch (error) {
    console.error("Error fetching integrations:", error);
    return NextResponse.json({ error: "Error al obtener integraciones" }, { status: 500 });
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // En producción, aquí se haría la llamada al BFF
    // const response = await fetch(`${BFF_URL}/api/v1/governance/data/integrations`, {
    //   method: "POST",
    //   headers: { "Content-Type": "application/json" },
    //   body: JSON.stringify(body),
    // });
    // return NextResponse.json(await response.json());

    // Mock: crear nueva integración
    const newIntegration: DatabaseIntegration = {
      idxintegration: mockIntegrations.length + 1,
      iduuid: `550e8400-e29b-41d4-a716-44665544000${mockIntegrations.length + 1}`,
      ...body,
      dtgcreatedat: new Date().toISOString(),
      dtgsyncstatus: "PENDING",
      originsCount: 0,
      datasetsCount: 0,
    };

    return NextResponse.json(newIntegration, { status: 201 });
  } catch (error) {
    console.error("Error creating integration:", error);
    return NextResponse.json({ error: "Error al crear integración" }, { status: 500 });
  }
}
