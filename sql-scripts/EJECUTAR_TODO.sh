#!/bin/bash
# =====================================================
# Script Maestro - Instalación Completa
# CodeFlowX Govern Platform
# =====================================================

set -e

DB_NAME="${DB_NAME:-codeflowx_govern}"
DB_USER="${DB_USER:-postgres}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "═══════════════════════════════════════════════════════"
echo "  INSTALACIÓN COMPLETA - CodeFlowX Govern"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "Base de datos: $DB_NAME"
echo "Usuario: $DB_USER"
echo "Host: $DB_HOST:$DB_PORT"
echo ""
echo "═══════════════════════════════════════════════════════"
echo ""

echo "📊 1. Creando tablas..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 01_create_tables.sql
echo "   ✓ 161 tablas creadas"
echo ""

echo "🔍 2. Creando índices..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 02_indexes.sql
echo "   ✓ 740 índices creados"
echo ""

echo "🔗 3. Creando Foreign Keys..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 03_foreign_keys.sql
echo "   ✓ 93 Foreign Keys creadas"
echo ""

echo "📝 4. Agregando comentarios..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 04_comments.sql
echo "   ✓ Documentación agregada"
echo ""

echo "🔄 5. Creando triggers..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 05_triggers.sql
echo "   ✓ Triggers automáticos creados"
echo ""

echo "═══════════════════════════════════════════════════════"
echo "  CARGANDO DATOS MOCK (DEMO)"
echo "═══════════════════════════════════════════════════════"
echo ""

cd mock_data

echo "👥 6. Cargando Core (usuarios, roles)..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 01_core_mock_data.sql
echo "   ✓ Completado"

echo "📁 7. Cargando Projects..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 02_projects_mock_data.sql
echo "   ✓ Completado"

echo "💬 8. Cargando Prompts..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 03_prompts_mock_data.sql
echo "   ✓ Completado"

echo "🤖 9. Cargando Agents..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 04_agents_mock_data.sql
echo "   ✓ Completado"

echo "🧠 10. Cargando Models..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 05_models_mock_data.sql
echo "   ✓ Completado"

echo "🔬 11. Cargando Training..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 06_training_mock_data.sql
echo "   ✓ Completado"

echo "📊 12. Cargando Dashboard..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 07_dashboard_mock_data.sql
echo "   ✓ Completado"

cd ..

echo ""
echo "═══════════════════════════════════════════════════════"
echo "✅ INSTALACIÓN COMPLETA EXITOSA"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "✓ 161 tablas creadas"
echo "✓ 740 índices"
echo "✓ 93 Foreign Keys"
echo "✓ 50 triggers automáticos"
echo "✓ Datos mock cargados"
echo ""
echo "═══════════════════════════════════════════════════════"
