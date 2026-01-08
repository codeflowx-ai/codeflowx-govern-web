"use client";

// Tipos específicos para Integraciones (Data Governance)

export type DatabaseType =
  | "POSTGRESQL"
  | "MYSQL"
  | "MARIADB"
  | "SQLSERVER"
  | "ORACLE"
  | "DB2"
  | "DB2_ZOS"
  | "SQLITE"
  | "MONGODB"
  | "CASSANDRA"
  | "REDIS"
  | "ELASTICSEARCH"
  | "NEO4J"
  | "DYNAMODB"
  | "S3"
  | "AZURE_BLOB"
  | "GCS"
  | "ADLS_GEN2"
  | "HDFS"
  | "MINIO"
  | "IBM_COS"
  | "OCI_OBJECT_STORAGE"
  | "SNOWFLAKE"
  | "DATABRICKS"
  | "BIGQUERY"
  | "REDSHIFT"
  | "SAGEMAKER"
  | "VERTEX_AI"
  | "AZURE_ML"
  | "MLFLOW"
  | "DATABRICKS_ML"
  | "KUBEFLOW"
  | "SELDON"
  | "WAND_B"
  | "HUGGINGFACE"
  | "KAGGLE"
  | "API";

export type AuthenticationType =
  | "NONE"
  | "USERNAME_PASSWORD"
  | "API_KEY"
  | "OAUTH2"
  | "JWT"
  | "AWS_IAM"
  | "AZURE_AD"
  | "GCP_SERVICE_ACCOUNT"
  | "CERTIFICATE";

export interface DatabaseIntegration {
  idxintegration: number;
  iduuid: string;
  dtgname: string;
  dtgdescription?: string;
  dtgdatabasetype: DatabaseType;
  dtgcategory?: string;
  dtghost?: string;
  dtgport?: number;
  dtgdatabase?: string;
  dtgauthenticationtype: AuthenticationType;
  dtgusername?: string;
  dtgpassword?: string;
  dtgclientid?: string;
  dtgclientsecret?: string;
  dtgtenantid?: string;
  dtgsslrequired?: boolean;
  dtgsslmode?: string;
  dtgtimeout?: number;
  dtgpoolsize?: number;
  dtgenabled?: boolean;
  dtgsyncenabled?: boolean;
  dtgsyncfrequencyhours?: number;
  dtglastsyncat?: string;
  dtgsyncstatus?: "SUCCESS" | "FAILED" | "PENDING" | "SYNCING";
  dtglasterror?: string;
  dtgcreatedat?: string;
  originsCount?: number;
  datasetsCount?: number;
}
