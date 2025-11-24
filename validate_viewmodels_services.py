#!/usr/bin/env python3
"""
Script para validar que los ViewModels están usando los servicios dedicados
en lugar de businessService directamente.
"""

import os
import re
from pathlib import Path
from collections import defaultdict

# Directorio base
BASE_DIR = Path(__file__).parent
VIEWMODELS_DIR = BASE_DIR / "src/main/java"
SERVICES_DIR = Path("/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.services/src/main/java/com/codeflowx/govern/service")

# Patrones para detectar uso de businessService
BUSINESS_SERVICE_PATTERNS = [
    r'businessService\.findAllEntity\s*\(([^,]+)\.class',
    r'businessService\.findById\s*\(([^,]+)\.class',
    r'businessService\.save\s*\(',
    r'businessService\.update\s*\(',
    r'businessService\.removeFromID\s*\(([^,]+)\.class',
    r'businessService\.findAllView\s*\(([^,]+)\.class',
]

# Patrones para detectar uso de servicios
SERVICE_PATTERNS = [
    r'@WireVariable\s+private\s+(\w+Service)\s+(\w+);',
    r'private\s+(\w+Service)\s+(\w+);',
]

def find_entity_class_name(line):
    """Extrae el nombre de la clase de entidad de una línea de código"""
    # Buscar patrones como Entity.class, EntityName.class
    match = re.search(r'(\w+)\.class', line)
    if match:
        return match.group(1)
    return None

def find_viewmodel_files():
    """Encuentra todos los archivos ViewModel"""
    viewmodels = []
    for root, dirs, files in os.walk(VIEWMODELS_DIR):
        for file in files:
            if file.endswith("ViewModel.java"):
                viewmodels.append(Path(root) / file)
    return viewmodels

def find_service_files():
    """Encuentra todos los archivos Service"""
    services = {}
    if not SERVICES_DIR.exists():
        return services

    for root, dirs, files in os.walk(SERVICES_DIR):
        for file in files:
            if file.endswith("Service.java"):
                service_name = file.replace("Service.java", "")
                services[service_name] = Path(root) / file
    return services

def analyze_viewmodel(vm_file):
    """Analiza un ViewModel para detectar uso de businessService"""
    issues = []
    uses_services = []
    entities_used = set()

    try:
        content = vm_file.read_text(encoding='utf-8')

        # Detectar servicios inyectados
        for pattern in SERVICE_PATTERNS:
            matches = re.finditer(pattern, content)
            for match in matches:
                service_name = match.group(1)
                uses_services.append(service_name)

        # Detectar uso directo de businessService
        for pattern in BUSINESS_SERVICE_PATTERNS:
            matches = re.finditer(pattern, content)
            for match in matches:
                if len(match.groups()) > 0:
                    entity_name = match.group(1)
                    entities_used.add(entity_name)

                    # Determinar qué servicio debería usar
                    # Ejemplo: Model -> ModelService, Agent -> AgentService
                    service_name = f"{entity_name}Service"

                    issues.append({
                        'line': content[:match.start()].count('\n') + 1,
                        'pattern': match.group(0),
                        'entity': entity_name,
                        'expected_service': service_name,
                        'type': 'businessService_direct_use'
                    })

        return {
            'file': str(vm_file.relative_to(BASE_DIR)),
            'uses_services': uses_services,
            'entities_used': list(entities_used),
            'issues': issues
        }
    except Exception as e:
        return {
            'file': str(vm_file.relative_to(BASE_DIR)),
            'error': str(e)
        }

def main():
    print("=" * 80)
    print("VALIDACIÓN DE VIEWMODELS - USO DE SERVICIOS")
    print("=" * 80)
    print()

    # Encontrar archivos
    print("Buscando ViewModels...")
    viewmodels = find_viewmodel_files()
    print(f"Encontrados {len(viewmodels)} ViewModels")
    print()

    print("Buscando Services...")
    services = find_service_files()
    print(f"Encontrados {len(services)} Services")
    print()

    # Analizar ViewModels
    print("Analizando ViewModels...")
    results = []
    for vm_file in viewmodels:
        result = analyze_viewmodel(vm_file)
        results.append(result)

    # Generar reporte
    print("\n" + "=" * 80)
    print("REPORTE DE VALIDACIÓN")
    print("=" * 80)
    print()

    # ViewModels con problemas
    viewmodels_with_issues = [r for r in results if 'issues' in r and r['issues']]
    viewmodels_ok = [r for r in results if 'issues' in r and not r['issues']]

    print(f"✅ ViewModels correctos (usan servicios): {len(viewmodels_ok)}")
    print(f"⚠️  ViewModels con problemas (usan businessService): {len(viewmodels_with_issues)}")
    print()

    if viewmodels_with_issues:
        print("=" * 80)
        print("VIEWMODELS QUE NECESITAN MIGRACIÓN")
        print("=" * 80)
        print()

        for result in viewmodels_with_issues:
            print(f"\n📄 {result['file']}")
            if result['uses_services']:
                print(f"   Servicios usados: {', '.join(result['uses_services'])}")

            if result['issues']:
                print(f"   ⚠️  Problemas encontrados: {len(result['issues'])}")
                for issue in result['issues'][:5]:  # Mostrar máximo 5
                    print(f"      - Línea {issue['line']}: {issue['pattern'][:60]}...")
                    print(f"        Entidad: {issue['entity']} -> Debería usar: {issue['expected_service']}")
                if len(result['issues']) > 5:
                    print(f"      ... y {len(result['issues']) - 5} más")

    # Resumen por entidad
    print("\n" + "=" * 80)
    print("RESUMEN POR ENTIDAD")
    print("=" * 80)
    print()

    entity_usage = defaultdict(list)
    for result in viewmodels_with_issues:
        if 'issues' in result:
            for issue in result['issues']:
                entity_usage[issue['entity']].append(result['file'])

    for entity, files in sorted(entity_usage.items()):
        service_name = f"{entity}Service"
        service_exists = service_name in services
        status = "✅" if service_exists else "❌"
        print(f"{status} {entity}")
        print(f"   Servicio esperado: {service_name} {'(existe)' if service_exists else '(NO EXISTE)'}")
        print(f"   ViewModels afectados: {len(files)}")
        for file in files[:3]:
            print(f"      - {file}")
        if len(files) > 3:
            print(f"      ... y {len(files) - 3} más")
        print()

    # Guardar reporte en archivo
    report_file = BASE_DIR / "viewmodels_validation_report.txt"
    with open(report_file, 'w', encoding='utf-8') as f:
        f.write("REPORTE DE VALIDACIÓN DE VIEWMODELS\n")
        f.write("=" * 80 + "\n\n")
        f.write(f"ViewModels analizados: {len(results)}\n")
        f.write(f"ViewModels correctos: {len(viewmodels_ok)}\n")
        f.write(f"ViewModels con problemas: {len(viewmodels_with_issues)}\n\n")

        if viewmodels_with_issues:
            f.write("\nVIEWMODELS QUE NECESITAN MIGRACIÓN:\n")
            f.write("-" * 80 + "\n")
            for result in viewmodels_with_issues:
                f.write(f"\n{result['file']}\n")
                for issue in result['issues']:
                    f.write(f"  Línea {issue['line']}: {issue['pattern']}\n")
                    f.write(f"    -> Debería usar: {issue['expected_service']}\n")

    print(f"\n📄 Reporte guardado en: {report_file}")

if __name__ == "__main__":
    main()
