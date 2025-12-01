#!/usr/bin/env python3
"""
Script para actualizar todos los ViewModels para que extiendan de BaseFront
y eliminar métodos logActivity duplicados.
"""

import os
import re
from pathlib import Path

def find_method_end(content, start_pos):
    """Encuentra el final de un método empezando desde la posición start_pos"""
    brace_count = 0
    i = start_pos
    in_string = False
    string_char = None

    while i < len(content):
        char = content[i]

        # Manejar strings
        if char in ('"', "'") and (i == 0 or content[i-1] != '\\'):
            if not in_string:
                in_string = True
                string_char = char
            elif char == string_char:
                in_string = False
                string_char = None

        if not in_string:
            if char == '{':
                brace_count += 1
            elif char == '}':
                brace_count -= 1
                if brace_count == 0:
                    return i + 1

        i += 1

    return len(content)

def process_viewmodel(file_path):
    """Procesa un archivo ViewModel para agregar BaseFront y eliminar logActivity"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        original_content = content
        modified = False

        # Obtener el nombre de la clase
        class_match = re.search(r'public\s+class\s+(\w+ViewModel)', content)
        if not class_match:
            return False

        class_name = class_match.group(1)

        # 1. Verificar si ya extiende de BaseFront
        if f'extends BaseFront<{class_name}>' in content:
            extends_basefront = True
        elif 'extends BaseFront' in content:
            # Ya extiende pero con otro genérico, lo dejamos
            extends_basefront = True
        else:
            extends_basefront = False

        # 2. Verificar si tiene el import de BaseFront
        has_import = 'import com.codeflowx.framework.zkoss.BaseFront;' in content

        # 3. Buscar métodos logActivity privados/protected para eliminar (siempre, incluso si ya extiende de BaseFront)
        # Buscar con y sin throws
        log_activity_pattern1 = r'(private|protected)\s+void\s+logActivity\s*\([^)]*\)\s*throws[^{]*\{'
        log_activity_pattern2 = r'(private|protected)\s+void\s+logActivity\s*\([^)]*\)\s*\{'
        log_activity_matches = list(re.finditer(log_activity_pattern1, content))
        log_activity_matches.extend(re.finditer(log_activity_pattern2, content))

        methods_to_remove = []
        for match in log_activity_matches:
            start = match.start()
            end = find_method_end(content, match.end() - 1)
            methods_to_remove.append((start, end))

        # Si no extiende de BaseFront, necesitamos hacer cambios
        if not extends_basefront:
            # Buscar la declaración de la clase
            class_decl_pattern = r'public\s+class\s+' + re.escape(class_name) + r'(\s+extends\s+\w+[^{<]*)?'
            class_decl_match = re.search(class_decl_pattern, content)

            if class_decl_match:
                if class_decl_match.group(1):
                    # Ya extiende de otra clase, reemplazar
                    old_extend = class_decl_match.group(1).strip()
                    new_decl = f'public class {class_name} extends BaseFront<{class_name}>'
                    content = content[:class_decl_match.start()] + new_decl + content[class_decl_match.end():]
                else:
                    # No extiende de nada, agregar
                    new_decl = f'public class {class_name} extends BaseFront<{class_name}>'
                    content = content[:class_decl_match.end()] + ' extends BaseFront<' + class_name + '>' + content[class_decl_match.end():]
                modified = True

        # Agregar import si falta (solo si no extiende de BaseFront o si se modificó)
        if not has_import and not extends_basefront:
            # Buscar el paquete
            package_match = re.search(r'package\s+([^;]+);', content)
            if package_match:
                # Buscar el primer import o la primera línea después del paquete
                after_package = package_match.end()
                # Buscar si hay imports existentes
                first_import = re.search(r'\nimport\s+', content[after_package:])
                if first_import:
                    # Insertar antes del primer import
                    insert_pos = after_package + first_import.start()
                else:
                    # Insertar después del paquete con una línea en blanco
                    insert_pos = after_package
                    # Buscar el siguiente salto de línea
                    next_newline = content.find('\n', insert_pos)
                    if next_newline != -1:
                        insert_pos = next_newline + 1

                new_import = 'import com.codeflowx.framework.zkoss.BaseFront;\n'
                content = content[:insert_pos] + new_import + content[insert_pos:]
                modified = True

        # Eliminar métodos logActivity privados/protected (siempre, incluso si ya extiende de BaseFront)
        if methods_to_remove:
            # Eliminar de atrás hacia adelante para no afectar las posiciones
            for start, end in reversed(methods_to_remove):
                # Buscar hacia atrás para incluir espacios, saltos de línea
                real_start = start
                while real_start > 0 and content[real_start-1] in ' \t\n\r':
                    real_start -= 1
                # Si hay un comentario de línea antes, incluirlo
                if real_start > 0:
                    # Buscar comentarios de línea
                    comment_start = content.rfind('//', max(0, real_start-100), real_start)
                    if comment_start != -1:
                        # Verificar que no esté dentro de un string
                        newline_before = content.rfind('\n', max(0, comment_start-100), comment_start)
                        if newline_before != -1:
                            real_start = newline_before + 1

                # Eliminar el método
                content = content[:real_start] + content[end:]
                modified = True

        if modified:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True

        return False
    except Exception as e:
        print(f"Error procesando {file_path}: {e}")
        import traceback
        traceback.print_exc()
        return False

def main():
    """Función principal"""
    base_dir = Path('/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java')

    # Buscar todos los ViewModels
    viewmodel_files = list(base_dir.rglob('*ViewModel.java'))

    print(f"Encontrados {len(viewmodel_files)} archivos ViewModel")

    modified_count = 0
    for file_path in viewmodel_files:
        if process_viewmodel(file_path):
            modified_count += 1
            if modified_count % 10 == 0:
                print(f"Modificados: {modified_count}...")

    print(f"\nTotal de archivos modificados: {modified_count}")

if __name__ == '__main__':
    main()





























