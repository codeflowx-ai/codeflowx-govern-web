# 🔌 CONFIGURACIÓN DE PUERTOS

## Estado Actual

Para evitar conflictos de puertos, los servidores están configurados en puertos diferentes:

### ✅ codeflowx-studio
- **Puerto**: 3002
- **URL**: http://localhost:3002
- **Estado**: ✅ Funcionando perfectamente

### ✅ codeflowx-govern-web
- **Puerto**: 3003 (cambiado desde 3002)
- **URL**: http://localhost:3003
- **Estado**: ✅ Compilando

## Cambios Realizados

Se modificó `package.json` de `codeflowx-govern-web` para usar el puerto 3003:
- `npm run dev` → `next dev -p 3003`
- `npm start` → `next start -p 3003`

## Uso

Ahora puedes tener ambos servidores corriendo simultáneamente:

```bash
# Terminal 1 - codeflowx-studio
cd codeflowx-studio
npm run dev
# → http://localhost:3002

# Terminal 2 - codeflowx-govern-web
cd codeflowx-govern-web
npm run dev
# → http://localhost:3003
```

## Notas

- Ambos servidores pueden correr al mismo tiempo sin conflictos
- Cada uno tiene su propio proceso Node.js
- No hay interferencia entre ellos

---

**Fecha**: 2025-12-26
**Puertos**: 3002 (studio) | 3003 (govern-web)
