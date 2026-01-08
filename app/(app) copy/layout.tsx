// app/(app)/layout.tsx - Layout para páginas de la aplicación
import { AppLayout } from "@/components/layout/AppLayout";

export default function AppPagesLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return <AppLayout>{children}</AppLayout>;
}


