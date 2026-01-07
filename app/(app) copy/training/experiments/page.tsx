"use client";

import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  Plus,
  Settings,
} from "lucide-react";

export default function ExperimentsPage() {
  return (
    <div className="min-h-screen bg-background p-6">

      <div className="w-full space-y-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-4xl font-bold text-foreground mb-2">
              Experiments
            </h1>
            <p className="text-muted-foreground text-lg">
              Experimentos de entrenamiento
            </p>
          </div>
          <div className="flex gap-3">
            <Button variant="outline">
              <Settings className="w-4 h-4 mr-2" />
              Configuración
            </Button>
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              Nuevo
            </Button>
          </div>
        </div>

        {/* Content */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5" />
              Experiments Dashboard
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="py-12">
              <p className="text-muted-foreground text-lg">
                Esta página está en desarrollo. Próximamente tendrás acceso a todas las funcionalidades.
              </p>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}


