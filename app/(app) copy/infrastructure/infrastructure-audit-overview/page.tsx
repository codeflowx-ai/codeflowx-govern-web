"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Eye,
  FileText,
  Search,
  User,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface InfrastructureAudit {
  id: number;
  resourceName: string;
  resourceType: string;
  action: string;
  userName: string;
  timestamp: string;
}

const mockAudits: InfrastructureAudit[] = [
  {
    id: 1,
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    action: "UPDATE",
    userName: "admin@example.com",
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    id: 2,
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    action: "CREATE",
    userName: "admin@example.com",
    timestamp: "2024-01-14T15:30:00Z",
  },
];

export default function InfrastructureAuditOverviewPage() {   const { t } = useTranslation();
  const [audits, setAudits] = useState<InfrastructureAudit[]>(mockAudits);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredAudits = audits.filter((audit) =>
    audit.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    audit.action.toLowerCase().includes(searchTerm.toLowerCase()) ||
    audit.userName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getActionBadge = (action: string) => {
    const colors: Record<string, string> = {
      CREATE: "bg-green-500/20 text-green-500 border-green-500/50",
      UPDATE: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      DELETE: "bg-red-500/20 text-red-500 border-red-500/50",
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
        <div className="flex items-center justify-between"><div className="flex items-center gap-3">
          <FileText className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("infrastructure.infrastructureAuditOverview", "Infrastructure Audit Overview")}
          </h1>
        </div></div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search audit logs...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="space-y-4">
          {filteredAudits.map((audit) => (
            <Card
              key={audit.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardContent className="pt-6">
                <div className="flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-lg font-semibold text-foreground">{audit.resourceName}</h3>
                      <Badge className={getActionBadge(audit.action)}>
                        {audit.action}
                      </Badge>
                      <Badge variant="outline">{audit.resourceType}</Badge>
                    </div>
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <div className="flex items-center gap-1">
                        <User className="w-4 h-4" />
                        {audit.userName}
                      </div>
                      <div>{new Date(audit.timestamp).toLocaleString()}</div>
                    </div>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => window.location.href = `/infrastructure/infrastructure-audit-detail?id=${audit.id}`}
                  >
                    <Eye className="w-4 h-4 mr-2" />
                    {t("common.details", "Details")}
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredAudits.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <FileText className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noAuditsFound", "No audit logs found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


