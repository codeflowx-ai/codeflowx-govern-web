"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

/**
 * Página de redirección para crear proyecto RAG
 * Redirige a /rag/projects/new donde se maneja la creación
 */
export default function RAGProjectCreatePage() {
  const router = useRouter();

  useEffect(() => {
    router.replace("/rag/projects/new");
  }, [router]);

  return null;
}


