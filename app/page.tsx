"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function LandingPage() {
  const router = useRouter();

  useEffect(() => {
    // Redirigir directamente al login
    router.push("/auth/login");
  }, [router]);

  // Mostrar mensaje de redirección mientras se procesa
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-blue-900 to-slate-900">
      
      <div className="text-center text-white">
        <div className="text-2xl font-bold mb-4">CodeflowX</div>
        <div className="text-lg">Redirigiendo al login...</div>
      </div>
    </div>
  );
}
