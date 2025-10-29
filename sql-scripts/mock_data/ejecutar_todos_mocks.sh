#!/bin/bash
# =====================================================
# Script Maestro - Ejecución de Datos Mock
# CodeFlowX Govern Platform
# =====================================================

set -e  # Salir si hay error

DB_NAME="${DB_NAME:-codeflowx_govern}"
DB_USER="${DB_USER:-postgres}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "═══════════════════════════════════════════════════════"
echo "  CARGA DE DATOS MOCK - CodeFlowX Govern"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "Base de datos: $DB_NAME"
echo "Usuario: $DB_USER"
echo "Host: $DB_HOST:$DB_PORT"
echo ""
echo "═══════════════════════════════════════════════════════"
echo ""

echo "📦 1. Cargando core..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 01_core_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "📦 2. Cargando projects..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 02_projects_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "📦 3. Cargando prompts..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 03_prompts_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "📦 4. Cargando agents..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 04_agents_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "📦 5. Cargando models..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 05_models_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "📦 6. Cargando training..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 06_training_mock_data.sql
echo "   ✓ Completado"
echo ""

echo "═══════════════════════════════════════════════════════"
echo "✅ CARGA DE DATOS MOCK COMPLETADA"
echo "═══════════════════════════════════════════════════════"
