"use client";

import { useState, useEffect } from "react";
import { X, Info } from "lucide-react";

interface DemoBannerProps {
  className?: string;
}

export default function DemoBanner({ className = "" }: DemoBannerProps) {
  const [isVisible, setIsVisible] = useState(false);
  const [demoRole, setDemoRole] = useState<string | null>(null);

  useEffect(() => {
    // Verificar si estamos en modo demo
    const checkDemoMode = () => {
      const cookies = document.cookie.split(';');
      const demoRoleCookie = cookies.find(cookie => cookie.trim().startsWith('demo_role='));
      
      if (demoRoleCookie) {
        const role = demoRoleCookie.split('=')[1];
        setDemoRole(role);
        setIsVisible(true);
      }
    };

    checkDemoMode();
  }, []);

  const handleClose = () => {
    setIsVisible(false);
  };

  if (!isVisible || !demoRole) {
    return null;
  }

  const getRoleDisplayName = (role: string) => {
    switch (role) {
      case 'agencia': return 'Agencia';
      case 'it': return 'IT';
      case 'oem': return 'OEM';
      case 'business_admin': return 'Business Admin';
      default: return role;
    }
  };

  return (
    <div className={`bg-gradient-to-r from-blue-50 to-indigo-50 border-l-4 border-blue-500 p-4 ${className}`}>
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-blue-100 rounded-full">
            <Info className="w-4 h-4 text-blue-600" />
          </div>
          <div>
            <h3 className="font-semibold text-blue-900">
              Modo Demo Activo - {getRoleDisplayName(demoRole)}
            </h3>
            <p className="text-sm text-blue-700">
              Demo con datos de ejemplo. Entregas a partir del 15/10/2025. Setup y soporte remotos.
            </p>
          </div>
        </div>
        <button
          onClick={handleClose}
          className="p-1 hover:bg-blue-100 rounded-full transition-colors"
        >
          <X className="w-4 h-4 text-blue-600" />
        </button>
      </div>
    </div>
  );
}
