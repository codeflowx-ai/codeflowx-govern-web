# Análisis de Seguridad - Sistema de Autenticación

## ⚠️ Problemas de Seguridad Actuales

### 1. **Almacenamiento en localStorage (CRÍTICO)**

**Problema:**
```typescript
localStorage.setItem("isAuthenticated", "true")
localStorage.setItem("user", JSON.stringify({...}))
```

**Riesgos:**
- ❌ **Vulnerable a XSS (Cross-Site Scripting)**: Cualquier script malicioso puede acceder a localStorage
- ❌ **No es httpOnly**: Accesible desde JavaScript, expuesto a ataques
- ❌ **No tiene expiración automática**: Los datos persisten indefinidamente
- ❌ **No se envía automáticamente**: Requiere código manual para incluir en requests
- ❌ **Accesible desde cualquier script**: Incluso scripts de terceros

### 2. **No hay Token JWT Real**

**Problema:**
- Solo se guarda `"isAuthenticated": "true"` como string
- No hay token JWT con firma, expiración, o claims
- No hay validación del token en el servidor

### 3. **Datos Sensibles en localStorage**

**Problema:**
- Información del usuario (roles, email, etc.) expuesta en localStorage
- Accesible por cualquier script en la página

### 4. **Falta de Validación**

**Problema:**
- No se valida expiración del token
- No se verifica la firma del token
- No hay refresh tokens

## ✅ Solución Segura Recomendada

### Opción 1: Cookies HttpOnly (RECOMENDADO)

**Ventajas:**
- ✅ Protegido contra XSS (httpOnly)
- ✅ Protegido contra CSRF (sameSite=Strict)
- ✅ Envío automático en requests
- ✅ Expiración automática del navegador

**Implementación:**

```typescript
// app/api/auth/login/route.ts
import { NextRequest, NextResponse } from 'next/server'
import { cookies } from 'next/headers'

export async function POST(request: NextRequest) {
  const { email, password } = await request.json()

  // Validar credenciales
  const user = await authenticateUser(email, password)

  if (user) {
    // Generar JWT token
    const token = generateJWT(user)

    // Guardar en cookie httpOnly
    const response = NextResponse.json({ success: true, user })

    response.cookies.set('auth-token', token, {
      httpOnly: true,        // No accesible desde JavaScript
      secure: true,          // Solo HTTPS en producción
      sameSite: 'strict',    // Protección CSRF
      maxAge: 60 * 60 * 24,  // 24 horas
      path: '/'
    })

    return response
  }

  return NextResponse.json({ error: 'Invalid credentials' }, { status: 401 })
}
```

### Opción 2: Token en Memory + Refresh Token en Cookie

**Ventajas:**
- ✅ Token en memoria (más seguro que localStorage)
- ✅ Refresh token en cookie httpOnly
- ✅ Renovación automática

**Implementación:**

```typescript
// components/providers/AuthProvider.tsx
'use client'

import { useState, useEffect } from 'react'

export function AuthProvider({ children }) {
  const [token, setToken] = useState<string | null>(null)
  const [user, setUser] = useState<User | null>(null)

  useEffect(() => {
    // Obtener token del servidor (cookie httpOnly)
    fetch('/api/auth/refresh', {
      credentials: 'include' // Incluir cookies
    })
      .then(res => res.json())
      .then(data => {
        if (data.token) {
          setToken(data.token) // Guardar solo en memoria
          setUser(data.user)
        }
      })
  }, [])

  // Usar token en requests
  const apiCall = async (url: string) => {
    return fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}` // Token en memoria
      },
      credentials: 'include'
    })
  }
}
```

### Opción 3: NextAuth.js (MÁS RECOMENDADO)

**Ventajas:**
- ✅ Framework probado y seguro
- ✅ Múltiples proveedores (OAuth, JWT, etc.)
- ✅ Gestión automática de sesiones
- ✅ Protección CSRF integrada
- ✅ Refresh tokens automático

**Implementación:**

```typescript
// app/api/auth/[...nextauth]/route.ts
import NextAuth from 'next-auth'
import CredentialsProvider from 'next-auth/providers/credentials'

export const authOptions = {
  providers: [
    CredentialsProvider({
      name: 'Credentials',
      credentials: {
        email: { label: "Email", type: "email" },
        password: { label: "Password", type: "password" }
      },
      async authorize(credentials) {
        const user = await authenticateUser(
          credentials.email,
          credentials.password
        )
        if (user) {
          return {
            id: user.id,
            email: user.email,
            name: user.name,
            roles: user.roles
          }
        }
        return null
      }
    })
  ],
  session: {
    strategy: 'jwt',
    maxAge: 24 * 60 * 60, // 24 horas
  },
  pages: {
    signIn: '/auth/login',
  },
  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        token.roles = user.roles
      }
      return token
    },
    async session({ session, token }) {
      session.user.roles = token.roles
      return session
    }
  }
}

export default NextAuth(authOptions)
```

## 🔒 Mejores Prácticas de Seguridad

### 1. **Nunca usar localStorage para tokens**
```typescript
// ❌ MAL
localStorage.setItem('token', token)

// ✅ BIEN
// Cookie httpOnly o token en memoria
```

### 2. **Validar tokens en el servidor**
```typescript
// app/api/protected/route.ts
import { verifyToken } from '@/lib/auth'

export async function GET(request: NextRequest) {
  const token = request.cookies.get('auth-token')?.value

  if (!token) {
    return NextResponse.json({ error: 'Unauthorized' }, { status: 401 })
  }

  try {
    const decoded = verifyToken(token)
    // Usar decoded para autorización
  } catch (error) {
    return NextResponse.json({ error: 'Invalid token' }, { status: 401 })
  }
}
```

### 3. **Implementar Refresh Tokens**
```typescript
// Token de acceso: 15 minutos (en memoria)
// Refresh token: 7 días (en cookie httpOnly)
```

### 4. **Protección CSRF**
```typescript
// Usar sameSite=Strict en cookies
// Implementar tokens CSRF para operaciones críticas
```

### 5. **HTTPS Obligatorio en Producción**
```typescript
// secure: true solo en producción
secure: process.env.NODE_ENV === 'production'
```

## 📋 Plan de Migración

1. **Fase 1**: Implementar NextAuth.js o cookies httpOnly
2. **Fase 2**: Migrar todas las llamadas API para usar el nuevo sistema
3. **Fase 3**: Eliminar localStorage de autenticación
4. **Fase 4**: Implementar refresh tokens
5. **Fase 5**: Agregar validación de tokens en todas las rutas protegidas

## ⚡ Acción Inmediata Recomendada

**Para producción, usar NextAuth.js** es la opción más segura y mantenible.

¿Quieres que implemente la solución segura ahora?
