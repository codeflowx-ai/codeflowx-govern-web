#!/usr/bin/env python3
"""
Script para analizar las guías de compliance extraídas de los PDFs
"""
from pathlib import Path
import re
from collections import defaultdict

def analizar_guia(texto, nombre_archivo):
    """Analiza una guía y extrae información clave"""
    analisis = {
        'nombre': nombre_archivo,
        'palabras': len(texto.split()),
        'lineas': len(texto.splitlines()),
        'secciones': [],
        'requisitos': [],
        'conceptos_clave': [],
        'referencias_normativas': []
    }

    # Buscar secciones (títulos con números o en mayúsculas)
    lineas = texto.split('\n')
    for i, linea in enumerate(lineas):
        linea_limpia = linea.strip()

        # Detectar secciones (números seguidos de texto, títulos en mayúsculas)
        if re.match(r'^\d+[\.\)]\s+[A-ZÁÉÍÓÚÑ]', linea_limpia) or \
           re.match(r'^[A-ZÁÉÍÓÚÑ][A-ZÁÉÍÓÚÑ\s]{10,}', linea_limpia):
            if len(linea_limpia) < 200:  # Evitar párrafos largos
                analisis['secciones'].append(linea_limpia)

        # Buscar requisitos (palabras clave)
        if any(palabra in linea_limpia.lower() for palabra in ['debe', 'deberá', 'requisito', 'obligatorio', 'exigencia']):
            if len(linea_limpia) > 20 and len(linea_limpia) < 300:
                analisis['requisitos'].append(linea_limpia[:200])

        # Buscar referencias normativas
        if re.search(r'(artículo|art\.|reglamento|directiva|norma|ISO|EN|IEC)', linea_limpia, re.IGNORECASE):
            if len(linea_limpia) < 200:
                analisis['referencias_normativas'].append(linea_limpia)

    # Conceptos clave (palabras frecuentes relevantes)
    palabras_relevantes = [
        'sistema de ia', 'riesgo', 'conformidad', 'evaluación', 'transparencia',
        'vigilancia', 'incidente', 'documentación', 'calidad', 'gobernanza',
        'datos', 'ciberseguridad', 'precisión', 'solidez', 'humano'
    ]

    texto_lower = texto.lower()
    for concepto in palabras_relevantes:
        if concepto in texto_lower:
            analisis['conceptos_clave'].append(concepto)

    # Limitar listas
    analisis['secciones'] = analisis['secciones'][:20]
    analisis['requisitos'] = analisis['requisitos'][:15]
    analisis['referencias_normativas'] = list(set(analisis['referencias_normativas']))[:10]

    return analisis

def generar_resumen(analisis_todos):
    """Genera un resumen consolidado de todas las guías"""
    resumen = {
        'total_guias': len(analisis_todos),
        'total_palabras': sum(a['palabras'] for a in analisis_todos),
        'temas_principales': defaultdict(int),
        'requisitos_comunes': [],
        'estructura_general': []
    }

    # Contar conceptos clave
    for analisis in analisis_todos:
        for concepto in analisis['conceptos_clave']:
            resumen['temas_principales'][concepto] += 1

    return resumen

def main():
    textos_dir = Path(__file__).parent / "textos_extraidos"

    if not textos_dir.exists():
        print("Error: No se encuentra el directorio de textos extraidos")
        return

    # Obtener todos los archivos de texto
    txt_files = sorted(textos_dir.glob("*.txt"))

    if not txt_files:
        print("No se encontraron archivos de texto")
        return

    print("="*70)
    print("ANALISIS DE GUIAS DE COMPLIANCE - REGLAMENTO DE IA")
    print("="*70)

    analisis_todos = []

    for txt_file in txt_files:
        print(f"\nAnalizando: {txt_file.name}")

        with open(txt_file, 'r', encoding='utf-8') as f:
            texto = f.read()

        analisis = analizar_guia(texto, txt_file.stem)
        analisis_todos.append(analisis)

        print(f"  Palabras: {analisis['palabras']:,}")
        print(f"  Secciones encontradas: {len(analisis['secciones'])}")
        print(f"  Requisitos identificados: {len(analisis['requisitos'])}")
        print(f"  Conceptos clave: {', '.join(analisis['conceptos_clave'][:5])}")

    # Resumen general
    resumen = generar_resumen(analisis_todos)

    print("\n" + "="*70)
    print("RESUMEN GENERAL")
    print("="*70)
    print(f"Total de guías analizadas: {resumen['total_guias']}")
    print(f"Total de palabras procesadas: {resumen['total_palabras']:,}")
    print(f"\nTemas principales (frecuencia):")
    for tema, frecuencia in sorted(resumen['temas_principales'].items(), key=lambda x: x[1], reverse=True):
        print(f"  - {tema}: {frecuencia} guías")

    # Guardar análisis detallado
    output_file = Path(__file__).parent / "analisis_guias.txt"
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("="*70 + "\n")
        f.write("ANALISIS DETALLADO DE GUIAS DE COMPLIANCE\n")
        f.write("="*70 + "\n\n")

        for analisis in analisis_todos:
            f.write(f"\n{'='*70}\n")
            f.write(f"GUIA: {analisis['nombre']}\n")
            f.write(f"{'='*70}\n")
            f.write(f"Palabras: {analisis['palabras']:,}\n")
            f.write(f"Líneas: {analisis['lineas']:,}\n\n")

            if analisis['secciones']:
                f.write("PRINCIPALES SECCIONES:\n")
                for i, seccion in enumerate(analisis['secciones'][:10], 1):
                    f.write(f"  {i}. {seccion}\n")
                f.write("\n")

            if analisis['requisitos']:
                f.write("REQUISITOS IDENTIFICADOS:\n")
                for i, req in enumerate(analisis['requisitos'][:10], 1):
                    f.write(f"  {i}. {req}\n")
                f.write("\n")

            if analisis['conceptos_clave']:
                f.write(f"CONCEPTOS CLAVE: {', '.join(analisis['conceptos_clave'])}\n\n")

    print(f"\nAnalisis detallado guardado en: {output_file}")

    # Generar resumen ejecutivo
    resumen_file = Path(__file__).parent / "resumen_ejecutivo_guias.txt"
    with open(resumen_file, 'w', encoding='utf-8') as f:
        f.write("RESUMEN EJECUTIVO - GUIAS DE COMPLIANCE REGLAMENTO DE IA\n")
        f.write("="*70 + "\n\n")
        f.write(f"Total de guías: {resumen['total_guias']}\n")
        f.write(f"Total de palabras: {resumen['total_palabras']:,}\n\n")

        f.write("TEMAS PRINCIPALES:\n")
        for tema, frecuencia in sorted(resumen['temas_principales'].items(), key=lambda x: x[1], reverse=True):
            f.write(f"  - {tema}: presente en {frecuencia} guías\n")

        f.write("\n\nESTRUCTURA DE GUIAS:\n")
        for analisis in analisis_todos:
            f.write(f"\n{analisis['nombre']}:\n")
            f.write(f"  - {analisis['palabras']:,} palabras\n")
            f.write(f"  - {len(analisis['secciones'])} secciones principales\n")
            f.write(f"  - {len(analisis['requisitos'])} requisitos identificados\n")

    print(f"Resumen ejecutivo guardado en: {resumen_file}")

if __name__ == "__main__":
    main()
