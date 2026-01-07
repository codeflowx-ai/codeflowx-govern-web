// components/layout/AppLayout.tsx
"use client";

import { useTranslation } from "@/app/config/i18n";
import { useTheme } from "@/components/hooks/useTheme";
import { Header } from "./Header";
import { Sidebar } from "./sidebar";
import { PageTitleProvider } from "@/components/contexts/PageTitleContext";
import { ToastProvider } from "@/components/ui/toast";

interface AppLayoutProps {
  children: React.ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  const { t } = useTranslation();
  const { isThemeLoaded } = useTheme();

  // Mostrar loading mientras el tema se carga
  if (!isThemeLoaded) {
    return (
      <div
        className="flex h-screen items-center justify-center"
        style={{ backgroundColor: "var(--theme-background)" }}
      >
        <div
          className="text-lg"
          style={{ color: "var(--theme-text)" }}
        >
          {t("layout.footer.loadingTheme")}
        </div>
      </div>
    );
  }

  return (
    <PageTitleProvider>
      <ToastProvider>
        <div className="flex h-screen" style={{ backgroundColor: "var(--theme-background)" }}>
        <Sidebar />
        <div className="flex-1 flex flex-col overflow-hidden">
          <Header />
          <main className="flex-1 overflow-y-auto" style={{ backgroundColor: "var(--theme-background)" }}>
            <div className="px-6 py-4">{children}</div>
          </main>
        {/* Footer Principal */}
        <footer
          className="px-6 py-3 border-t"
          style={{
            backgroundColor: "var(--theme-footer-background)",
            borderTopColor: "var(--theme-footer-border)",
            color: "var(--theme-footer-text)",
          }}
        >
          <div className="flex items-center justify-between max-w-4xl mx-auto">
            {/* Información del sistema */}
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-2">
                <div
                  className="w-2 h-2 rounded-full animate-pulse"
                  style={{ backgroundColor: "var(--theme-success)" }}
                ></div>
                <span className="text-sm font-medium" style={{ color: "var(--theme-footer-text)" }}>
                  CodeflowX v1.0 - {t("layout.footer.systemActive")}
                </span>
              </div>
              <span
                className="text-xs px-2 py-1 rounded-full"
                style={{
                  backgroundColor: "var(--theme-primary)",
                  color: "#ffffff"
                }}
              >
                {t("layout.footer.aiPowered")}
              </span>
            </div>

            {/* Enlaces útiles */}
            <div className="flex items-center gap-3">
              <a
                href="/docs"
                className="text-xs hover:underline transition-colors"
                style={{ color: "var(--theme-footer-text)" }}
              >
                {t("layout.footer.documentation")}
              </a>
              <a
                href="/support"
                className="text-xs hover:underline transition-colors"
                style={{ color: "var(--theme-footer-text)" }}
              >
                {t("layout.footer.support")}
              </a>
              <a
                href="/status"
                className="text-xs hover:underline transition-colors"
                style={{ color: "var(--theme-footer-text)" }}
              >
                {t("layout.footer.status")}
              </a>
            </div>
          </div>
        </footer>
        </div>
      </div>
      </ToastProvider>
    </PageTitleProvider>
  );
}
