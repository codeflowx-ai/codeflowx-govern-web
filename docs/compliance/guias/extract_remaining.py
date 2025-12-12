#!/usr/bin/env python3
"""Extraer los PDFs restantes con PyPDF2"""
import PyPDF2
from pathlib import Path

guias_dir = Path(__file__).parent
textos_dir = guias_dir / "textos_extraidos"
textos_dir.mkdir(exist_ok=True)

# PDFs faltantes
pdfs_faltantes = [
    "15-guia-documentacion-tecnica.pdf",
    "16-manual-de-checklist-de-guias-de-requisitos.pdf"
]

for pdf_name in pdfs_faltantes:
    pdf_path = guias_dir / pdf_name
    if not pdf_path.exists():
        print(f"No encontrado: {pdf_name}")
        continue

    txt_file = textos_dir / f"{pdf_path.stem}.txt"
    if txt_file.exists():
        print(f"Ya existe: {txt_file.name}")
        continue

    print(f"Extrayendo: {pdf_name}")
    try:
        texto = ""
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            for i, page in enumerate(pdf_reader.pages):
                try:
                    texto += page.extract_text() + "\n"
                except:
                    print(f"  Advertencia: Error en pagina {i+1}")

        with open(txt_file, 'w', encoding='utf-8') as f:
            f.write(texto)

        num_palabras = len(texto.split())
        print(f"  [OK] {num_palabras} palabras extraidas")
    except Exception as e:
        print(f"  [ERROR] {e}")
