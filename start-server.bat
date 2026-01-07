@echo off
echo Deteniendo procesos Node.js en puerto 3002...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3002') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 2 /nobreak >nul
echo.
echo Iniciando servidor Next.js en puerto 3002...
echo.
cd /d %~dp0
npm run dev
