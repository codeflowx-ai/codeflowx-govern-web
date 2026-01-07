"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  FileText,
  User,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface InfrastructureAuditDetail {
  id: number;
  resourceId: number;
  resourceType: string;
  action: string;
  userId: number;
  userName: string;
  timestamp: string;
  details: string;
  createdAt: string;
  resourceName: string;
}

const mockAudit: InfrastructureAuditDetail = {
  id: 1,
  resourceId: 1,
  resourceType: "LOAD_BALANCER",
  action: "UPDATE",
  userId: 1,
  userName: "admin@example.com",
  timestamp: "2024-01-15T10:00:00Z",
  details: "Updated load balancer configuration",
  createdAt: "2024-01-15T10:00:00Z",
  resourceName: "Production Load Balancer",
};

export default function InfrastructureAuditDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const audit = mockAudit;

  const getActionBadge = (action: string) => {
    const colors: Record<string, string> = {
      CREATE: "bg-green-500/20 text-green-500 border-green-500/50",
      UPDATE: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      DELETE: "bg-red-500/20 text-red-500 border-red-500/50",
      VIEW: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[action] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/infrastructure-audit-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <FileText className="w-8 h-8 text-primary" />
                {t("infrastructure.auditRecord", "Audit Record")} #{audit.id}
              </h1>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.auditDetails", "Audit Details")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.action", "Action")}
                    </label>
                    <Badge className={getActionBadge(audit.action)}>
                      {audit.action}
                    </Badge>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{audit.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceName", "Resource Name")}
                    </label>
                    <p className="text-foreground font-medium">{audit.resourceName}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.timestamp", "Timestamp")}
                    </label>
                    <p className="text-foreground font-medium">
                      {new Date(audit.timestamp).toLocaleString()}
                    </p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.details", "Details")}
                    </label>
                    <p className="text-foreground font-medium">{audit.details}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <User className="w-5 h-5 text-primary" />
                  {t("infrastructure.userInformation", "User Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.userName", "User Name")}
                  </label>
                  <p className="text-sm text-foreground">{audit.userName}</p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.userId", "User ID")}
                  </label>
                  <p className="text-sm text-foreground">{audit.userId}</p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}


