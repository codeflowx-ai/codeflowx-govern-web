# 🚀 ARRANQUE DE SERVIDORES

## Comandos para Arrancar Ambos Servidores

### Opción 1: Dos Terminales Separadas (Recomendado)

**Terminal 1 - codeflowx-studio:**
```powershell
cd C:\Users\Manuel\Documents\git\codeflowx-studio
npm run dev
# → http://localhost:3002
```

**Terminal 2 - codeflowx-govern-web:**
```powershell
cd C:\Users\Manuel\Documents\git\codeflowx-govern-web
npm run dev
# → http://localhost:3003
```

### Opción 2: Script PowerShell (Automático)

Puedes crear un script para arrancar ambos:

```powershell
# arrancar-ambos.ps1
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'C:\Users\Manuel\Documents\git\codeflowx-studio'; npm run dev"
Start-Sleep -Seconds 3
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'C:\Users\Manuel\Documents\git\codeflowx-govern-web'; npm run dev"
```

## URLs

- **codeflowx-studio**: http://localhost:3002
- **codeflowx-govern-web**: http://localhost:3003

## Estado de Compilación

Ambos servidores compilan en segundo plano. Espera 30-60 segundos después de iniciarlos para que la compilación complete.

Verifica que estén listos cuando veas:
- `✓ Ready in X.Xs` en las terminales
- Las URLs respondan en el navegador

## Detener Servidores

Presiona `Ctrl+C` en cada terminal para detener los servidores, o:

```powershell
# Detener todos los procesos Node.js
Get-Process node | Stop-Process -Force
```

---

**Última actualización**: 2025-12-26
