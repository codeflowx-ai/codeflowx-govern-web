'use client'

import { useState, useEffect } from 'react'
import { usePathname } from 'next/navigation'
import { MessageCircle, X, Send, Bot } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent } from '@/components/ui/card'

interface Message {
    id: string
    text: string
    sender: 'user' | 'bot'
    timestamp: Date
}

// Simulación de obtención de rol (en modo demo)
function getUserRole() {
    try {
        if (typeof window !== 'undefined') {
            return localStorage.getItem('userRole') || 'viewer'
        }
    } catch (error) {
        console.log('localStorage no disponible')
    }
    return 'viewer'
}

// Mensajes demo por contexto
const demoResponses: Record<string, Record<string, string>> = {
    '/dashboard': {
        admin: 'Como administrador, puedes gestionar usuarios, roles y ver estadísticas avanzadas en el dashboard.',
        developer: 'Como developer, aquí puedes ver el estado de tus proyectos y tareas asignadas.',
        viewer: 'Como viewer, tienes acceso de solo lectura al dashboard principal.',
        default: 'Bienvenido al dashboard. ¿Sobre qué necesitas ayuda?'
    },
    '/team': {
        admin: 'En la sección de equipo puedes añadir, editar o eliminar miembros y asignar roles.',
        developer: 'Puedes ver los miembros de tu equipo y sus roles.',
        viewer: 'Puedes consultar la lista de miembros del equipo.',
        default: 'Aquí puedes gestionar o consultar los miembros del equipo.'
    },
    '/training-center': {
        admin: 'Aquí puedes asignar cursos y monitorizar el progreso de los usuarios.',
        developer: 'Accede a los recursos de formación técnica y tutoriales.',
        viewer: 'Consulta los materiales de formación disponibles.',
        default: 'Bienvenido al área de formación. ¿Qué tema te interesa?'
    },
    'default': {
        default: '¡Hola! Soy Leka, tu asistente virtual. ¿En qué puedo ayudarte hoy?'
    }
}

export function FloatingChatbot() {
    const [isOpen, setIsOpen] = useState(false)
    const [messages, setMessages] = useState<Message[]>([])
    const [inputValue, setInputValue] = useState('')
    const [isTyping, setIsTyping] = useState(false)
    const pathname = usePathname()
    const [userRole, setUserRole] = useState('viewer')

    // Mensaje de bienvenida contextual
    useEffect(() => {
        const role = getUserRole()
        setUserRole(role)
        let welcome = ''
        if (demoResponses[pathname] && demoResponses[pathname][role]) {
            welcome = demoResponses[pathname][role]
        } else if (demoResponses[pathname] && demoResponses[pathname]['default']) {
            welcome = demoResponses[pathname]['default']
        } else {
            welcome = `¡Hola! Soy Leka, tu asistente virtual. ¿En qué puedo ayudarte sobre esta sección de la plataforma?`
        }
        setMessages([
            {
                id: '1',
                text: welcome,
                sender: 'bot',
                timestamp: new Date()
            }
        ])
    }, [pathname])

    const handleSendMessage = async () => {
        if (!inputValue.trim()) return

        const userMessage: Message = {
            id: Date.now().toString(),
            text: inputValue,
            sender: 'user',
            timestamp: new Date()
        }

        setMessages(prev => [...prev, userMessage])
        setInputValue('')
        setIsTyping(true)

        setTimeout(() => {
            let response = ''
            if (demoResponses[pathname] && demoResponses[pathname][userRole]) {
                response = `(${userRole} - ${pathname}) Respuesta demo: ${demoResponses[pathname][userRole]}`
            } else if (demoResponses[pathname] && demoResponses[pathname]['default']) {
                response = `(${pathname}) Respuesta demo: ${demoResponses[pathname]['default']}`
            } else {
                response = 'Respuesta demo: ¡Hola! Soy Leka, tu asistente virtual. ¿En qué puedo ayudarte sobre esta sección de la plataforma?'
            }
            const botMessage: Message = {
                id: (Date.now() + 1).toString(),
                text: response,
                sender: 'bot',
                timestamp: new Date()
            }
            setMessages(prev => [...prev, botMessage])
            setIsTyping(false)
        }, 1000)
    }

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault()
            handleSendMessage()
        }
    }

    return (
        <>
            {/* Botón flotante */}
            <Button
                onClick={() => setIsOpen(!isOpen)}
                className="fixed bottom-4 right-4 w-14 h-14 rounded-full shadow-lg bg-primary-600 hover:bg-primary-700 z-50"
            >
                {isOpen ? <X className="w-6 h-6" /> : <MessageCircle className="w-6 h-6" />}
            </Button>

            {/* Chat window */}
            {isOpen && (
                <Card className="fixed bottom-20 right-4 w-96 h-96 shadow-xl border-0 z-50">
                    <CardContent className="p-0 h-full flex flex-col">
                        {/* Header */}
                        <div className="bg-primary-600 text-white p-4 rounded-t-lg">
                            <div className="flex items-center space-x-2">
                                <Bot className="w-5 h-5" />
                                <div>
                                    <h3 className="font-semibold">Asistente Leka</h3>
                                    <p className="text-xs opacity-90">{`Página: ${pathname} | Rol: ${userRole}`}</p>
                                </div>
                            </div>
                        </div>

                        {/* Messages */}
                        <div className="flex-1 overflow-y-auto p-4 space-y-4 bg-gray-50">
                            {messages.map((message) => (
                                <div
                                    key={message.id}
                                    className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                                >
                                    <div
                                        className={`max-w-xs px-3 py-2 rounded-lg ${
                                            message.sender === 'user'
                                                ? 'bg-primary-600 text-white'
                                                : 'bg-white text-gray-900 border'
                                        }`}
                                    >
                                        <p className="text-sm">{message.text}</p>
                                        <p className="text-xs opacity-70 mt-1">
                                            {message.timestamp.toLocaleTimeString()}
                                        </p>
                                    </div>
                                </div>
                            ))}
                            {isTyping && (
                                <div className="flex justify-start">
                                    <div className="bg-white text-gray-900 border px-3 py-2 rounded-lg">
                                        <div className="flex space-x-1">
                                            <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                                            <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.1s' }}></div>
                                            <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* Input */}
                        <div className="p-4 bg-white border-t">
                            <div className="flex space-x-2">
                                <Input
                                    value={inputValue}
                                    onChange={(e) => setInputValue(e.target.value)}
                                    onKeyPress={handleKeyPress}
                                    placeholder="Escribe tu pregunta..."
                                    className="flex-1"
                                />
                                <Button
                                    onClick={handleSendMessage}
                                    disabled={!inputValue.trim()}
                                >
                                    <Send className="w-4 h-4" />
                                </Button>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            )}
        </>
    )
}
