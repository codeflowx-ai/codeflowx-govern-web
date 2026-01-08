// app/layout.tsx - Layout principal de CodeflowX
import { AuthProvider } from "@/components/providers/AuthProvider";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
// import { DevModeToggle } from '@/components/dev/DevModeToggle'

const inter = Inter({ subsets: ["latin"] });

export async function generateMetadata(): Promise<Metadata> {
  return {
    title: "CodeflowX AI OS",
    description: "Enterprise-grade AI code generation for modern applications",
    other: {
      "Cache-Control": "no-cache, no-store, must-revalidate",
      Pragma: "no-cache",
      Expires: "0",
    },
  };
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <head>
        {/* Umami Analytics */}
        <script
          defer
          src="https://analytics.codeflowx.cloud/script.js"
          data-website-id="edd21c8e-c20f-4bfe-b7f4-b5a7533fb722"
        ></script>

        {/* Script para aplicar tema inmediatamente y evitar flash */}
        <script
          dangerouslySetInnerHTML={{
            __html: `
              (function() {
                try {
                  // Prevenir FOUC (Flash of Unstyled Content)
                  document.documentElement.style.visibility = 'hidden';

                  // Aplicar tema inmediatamente (evitar localStorage en SSR)
                  const savedTheme = (typeof window !== 'undefined' && window.localStorage)
                    ? window.localStorage.getItem('codeflowx-theme') || 'trekker-federation'
                    : 'trekker-federation';
                  document.documentElement.className = savedTheme;

                  // Aplicar variables CSS del tema
                  const themes = {
                    'trekker-federation': {
                      primary: '#1e40af',
                      secondary: '#3b82f6',
                      accent: '#8b5cf6',
                      background: '#0f172a',
                      surface: '#1e293b',
                      card: '#334155',
                      popover: '#475569',
                      header: { background: '#000000', text: '#f8fafc', border: '#475569', hover: '#1a1a1a' },
                      sidebar: { background: '#000000', text: '#cbd5e1', border: '#334155', hover: '#1a1a1a', active: '#3b82f6' },
                      footer: { background: '#000000', text: '#cbd5e1', border: '#334155' },
                      text: '#f8fafc',
                      textSecondary: '#cbd5e1',
                      textMuted: '#94a3b8',
                      border: '#475569',
                      borderLight: '#64748b',
                      success: '#10b981',
                      warning: '#f59e0b',
                      error: '#ef4444',
                      info: '#3b82f6',
                      hover: '#334155',
                      hoverLight: '#475569',
                      shadow: '0 10px 15px -3px rgba(30, 64, 175, 0.3)',
                      shadowLight: '0 4px 6px -1px rgba(30, 64, 175, 0.2)',
                      glow: '0 0 20px rgba(30, 64, 175, 0.4)'
                    }
                  };

                  const theme = themes[savedTheme];
                  if (theme) {
                    Object.entries(theme).forEach(([key, value]) => {
                      if (typeof value === 'string' && value.startsWith('#')) {
                        document.documentElement.style.setProperty('--theme-' + key, value);
                      } else if (typeof value === 'object' && value !== null) {
                        // Manejar objetos anidados (header, sidebar, footer)
                        Object.entries(value).forEach(([nestedKey, nestedValue]) => {
                          if (typeof nestedValue === 'string' && nestedValue.startsWith('#')) {
                            document.documentElement.style.setProperty('--theme-' + key + '-' + nestedKey, nestedValue);
                          }
                        });
                      }
                    });

                    // Forzar aplicación inmediata del fondo negro para trekker-federation
                    if (savedTheme === 'trekker-federation') {
                      const header = document.querySelector('header');
                      const aside = document.querySelector('aside');
                      const footer = document.querySelector('footer');
                      if (header) {
                        header.style.backgroundColor = '#000000';
                        header.style.background = '#000000';
                      }
                      if (aside) {
                        aside.style.backgroundColor = '#000000';
                        aside.style.background = '#000000';
                      }
                      if (footer) {
                        footer.style.backgroundColor = '#000000';
                        footer.style.background = '#000000';
                      }
                    }
                  }

                  // Aplicar estilos críticos inmediatamente
                  if (document.documentElement) {
                    document.documentElement.style.backgroundColor = '#0f172a';
                  }
                  if (document.body) {
                    document.body.style.backgroundColor = '#0f172a';
                    document.body.style.color = '#f8fafc';
                  }

                  // Mostrar contenido cuando esté listo
                  window.addEventListener('load', function() {
                    document.documentElement.style.visibility = 'visible';
                  });

                  // Fallback: mostrar después de un tiempo máximo
                  setTimeout(function() {
                    document.documentElement.style.visibility = 'visible';
                  }, 100);

                } catch (e) {
                  // Error silencioso - solo mostrar contenido
                  if (document.documentElement) {
                    document.documentElement.style.visibility = 'visible';
                  }
                }
              })();
            `,
          }}
        />
        {/* Prevenir FOUC con CSS crítico */}
        <style
          dangerouslySetInnerHTML={{
            __html: `
              html { visibility: hidden; }
              html.trekker-federation { visibility: visible; }
              html.trekker-federation body { background-color: #0f172a !important; color: #f8fafc !important; }
              html.trekker-federation .bg-gray-50, html.trekker-federation .bg-white { background-color: #0f172a !important; }
              html.trekker-federation .text-gray-600, html.trekker-federation .text-gray-700, html.trekker-federation .text-gray-800, html.trekker-federation .text-gray-900 { color: #f8fafc !important; }
            `,
          }}
        />
      </head>
      <body className={inter.className}>
        <AuthProvider>{children}</AuthProvider>
        {/* <DevModeToggle /> */}
      </body>
    </html>
  );
}
