"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Edit,
  Key,
  Lock,
  Shield,
  User,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface CloudCredentialDetail {
  id: number;
  cloudProviderId: number;
  credentialName: string;
  credentialType: "API_KEY" | "OAUTH" | "SERVICE_ACCOUNT" | "IAM_ROLE";
  accessKey?: string;
  secretKey?: string;
  token?: string;
  isEncrypted: boolean;
  expiresAt?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

const mockCredential: CloudCredentialDetail = {
  id: 1,
  cloudProviderId: 1,
  credentialName: "AWS Production Credentials",
  credentialType: "API_KEY",
  isEncrypted: true,
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function CloudCredentialDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const credential = mockCredential;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/credentials"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <Key className="w-8 h-8 text-primary" />
                {credential.credentialName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={credential.isActive ? "primary" : "secondary"}>
              {credential.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <User className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.credentialType", "Credential Type")}
                    </label>
                    <p className="text-foreground font-medium">{credential.credentialType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{credential.cloudProvider}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.encrypted", "Encrypted")}
                    </label>
                    <p className="text-foreground font-medium">
                      {credential.isEncrypted ? t("common.yes", "Yes") : t("common.no", "No")}
                    </p>
                  </div>
                  {credential.expiresAt && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.expiresAt", "Expires At")}
                      </label>
                      <p className="text-foreground font-medium">
                        {new Date(credential.expiresAt).toLocaleString()}
                      </p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Shield className="w-5 h-5 text-primary" />
                  {t("infrastructure.security", "Security")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="flex items-center gap-2">
                  <Lock className="w-4 h-4 text-muted-foreground" />
                  <span className="text-sm text-foreground">
                    {credential.isEncrypted ? "Encrypted" : "Not Encrypted"}
                  </span>
                </div>
              </CardContent>
            </Card>

            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Key className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(credential.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(credential.updatedAt).toLocaleString()}
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}
