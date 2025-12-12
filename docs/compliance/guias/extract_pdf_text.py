#!/usr/bin/env python3
"""
Script para extraer texto de los PDFs de guías de compliance
"""
import os
import sys
from pathlib import Path

try:
    import PyPDF2
    HAS_PYPDF2 = True
except ImportError:
    HAS_PYPDF2 = False

try:
    import pdfplumber
    HAS_PDFPLUMBER = True
except ImportError:
    HAS_PDFPLUMBER = False

def extract_with_pypdf2(pdf_path):
    """Extrae texto usando PyPDF2"""
    text = ""
    try:
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            for page in pdf_reader.pages:
                text += page.extract_text() + "\n"
    except Exception as e:
        print(f"Error con PyPDF2: {e}")
    return text

def extract_with_pdfplumber(pdf_path):
    """Extrae texto usando pdfplumber (mejor calidad)"""
    text = ""
    try:
        with pdfplumber.open(pdf_path) as pdf:
            for page in pdf.pages:
                page_text = page.extract_text()
                if page_text:
                    text += page_text + "\n"
    except Exception as e:
        print(f"Error con pdfplumber: {e}")
    return text

def extract_pdf_text(pdf_path):
    """Extrae texto del PDF usando la mejor librería disponible"""
    # Intentar primero con pdfplumber (mejor calidad)
    if HAS_PDFPLUMBER:
        try:
            texto = extract_with_pdfplumber(pdf_path)
            if texto and len(texto.strip()) > 100:  # Verificar que se extrajo contenido significativo
                return texto
        except Exception as e:
            print(f"    Advertencia: pdfplumber fallo, intentando PyPDF2...")

    # Fallback a PyPDF2
    if HAS_PYPDF2:
        return extract_with_pypdf2(pdf_path)
    else:
        raise ImportError("No hay librerías de PDF disponibles. Instala pdfplumber o PyPDF2")

def main():
    # Directorio de las guías
    guias_dir = Path(__file__).parent

    # Crear directorio para textos extraídos
    textos_dir = guias_dir / "textos_extraidos"
    textos_dir.mkdir(exist_ok=True)

    # Obtener todos los PDFs
    pdf_files = sorted(guias_dir.glob("*.pdf"))

    if not pdf_files:
        print("No se encontraron archivos PDF")
        return

    print(f"Encontrados {len(pdf_files)} archivos PDF")
    print(f"Extrayendo texto...")

    resultados = []

    for pdf_file in pdf_files:
        print(f"\nProcesando: {pdf_file.name}")

        # Verificar si ya existe el archivo de texto
        txt_file = textos_dir / f"{pdf_file.stem}.txt"
        if txt_file.exists():
            print(f"  [INFO] Ya existe, omitiendo...")
            with open(txt_file, 'r', encoding='utf-8') as f:
                texto = f.read()
            num_palabras = len(texto.split())
            num_lineas = len(texto.splitlines())
            resultados.append({
                'archivo': pdf_file.name,
                'texto_file': txt_file.name,
                'palabras': num_palabras,
                'lineas': num_lineas,
                'exito': True
            })
            continue

        try:
            texto = extract_pdf_text(pdf_file)

            # Guardar texto extraído
            with open(txt_file, 'w', encoding='utf-8') as f:
                f.write(texto)

            # Estadísticas
            num_palabras = len(texto.split())
            num_lineas = len(texto.splitlines())

            resultados.append({
                'archivo': pdf_file.name,
                'texto_file': txt_file.name,
                'palabras': num_palabras,
                'lineas': num_lineas,
                'exito': True
            })

            print(f"  [OK] Extraido: {num_palabras} palabras, {num_lineas} lineas")

        except Exception as e:
            print(f"  [ERROR] Error: {e}")
            resultados.append({
                'archivo': pdf_file.name,
                'exito': False,
                'error': str(e)
            })

    # Resumen
    print("\n" + "="*60)
    print("RESUMEN DE EXTRACCIÓN")
    print("="*60)
    exitosos = sum(1 for r in resultados if r.get('exito', False))
    print(f"Archivos procesados: {len(resultados)}")
    print(f"Exitosos: {exitosos}")
    print(f"Fallidos: {len(resultados) - exitosos}")

    if exitosos > 0:
        print(f"\nTextos guardados en: {textos_dir}")
        print("\nArchivos extraídos:")
        for r in resultados:
            if r.get('exito'):
                print(f"  - {r['texto_file']} ({r['palabras']} palabras)")

if __name__ == "__main__":
    main()
