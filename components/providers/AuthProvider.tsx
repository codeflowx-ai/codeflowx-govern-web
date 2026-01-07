// components/providers/AuthProvider.tsx
'use client'

import { useState, useEffect, createContext, useContext, ReactNode } from 'react'

interface User {
    id: string
    email: string
    name: string
    role: string
    permissions: string[]
    tenantId: string
}

interface AuthContextType {
    user: User | null
    login: (email: string, password: string) => Promise<void>
    logout: () => void
    hasPermission: (permission: string) => boolean
    isLoading: boolean
}

export const AuthContext = createContext<AuthContextType | null>(null)

interface AuthProviderProps {
    children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [user, setUser] = useState<User | null>(null)
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        // Verificar autenticación al cargar
        const checkAuth = async () => {
            try {
                const token = localStorage.getItem('isAuthenticated')
                if (token === 'true') {
                    const userData = localStorage.getItem('user')
                    if (userData) {
                        setUser(JSON.parse(userData))
                    }
                }
            } catch (error) {
                console.error('Error checking auth:', error)
            } finally {
                setIsLoading(false)
            }
        }

        checkAuth()
    }, [])

    const login = async (email: string, password: string) => {
        setIsLoading(true)
        try {
            // Aquí iría la lógica real de login
            // Por ahora usamos los datos del localStorage que se setean en el login
            const userData = localStorage.getItem('user')
            if (userData) {
                setUser(JSON.parse(userData))
            }
        } catch (error) {
            throw new Error('Login failed')
        } finally {
            setIsLoading(false)
        }
    }

    const logout = () => {
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('user')
        setUser(null)
    }

    const hasPermission = (permission: string): boolean => {
        if (!user) return false

        // Admin tiene todos los permisos
        if (user.role === 'admin') return true

        // Verificar permisos específicos
        return user.permissions.includes(permission)
    }

    const value = {
        user,
        login,
        logout,
        hasPermission,
        isLoading
    }

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

export function useAuth() {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider')
    }
    return context
}
