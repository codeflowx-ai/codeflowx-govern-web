'use client';

import React, { useState, useRef, useEffect } from 'react';
import { Send, Bot, User, Loader2, X, Minimize2, Maximize2 } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface Message {
    id: string;
    role: 'user' | 'assistant';
    content: string;
    timestamp: Date;
}

interface AIAssistantProps {
    onCodeSuggestion?: (code: string) => void;
}

export default function AIAssistant({ onCodeSuggestion }: AIAssistantProps) {
    const [isOpen, setIsOpen] = useState(true);
    const [isMinimized, setIsMinimized] = useState(false);
    const [messages, setMessages] = useState<Message[]>([
        {
            id: '1',
            role: 'assistant',
            content: '¡Hola! Soy tu asistente de IA para el notebook. Puedo ayudarte con:\n\n• Explicaciones de código Python\n• Sugerencias para agentes de IA\n• Debugging y optimización\n• Mejores prácticas\n\n¿En qué puedo ayudarte hoy?',
            timestamp: new Date()
        }
    ]);
    const [inputValue, setInputValue] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const messagesEndRef = useRef<HTMLDivElement>(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const handleSendMessage = async () => {
        if (!inputValue.trim() || isLoading) return;

        const userMessage: Message = {
            id: Date.now().toString(),
            role: 'user',
            content: inputValue,
            timestamp: new Date()
        };

        setMessages(prev => [...prev, userMessage]);
        setInputValue('');
        setIsLoading(true);

        // Simular respuesta del asistente
        setTimeout(() => {
            const response = generateAIResponse(inputValue);
            const assistantMessage: Message = {
                id: (Date.now() + 1).toString(),
                role: 'assistant',
                content: response,
                timestamp: new Date()
            };
            setMessages(prev => [...prev, assistantMessage]);
            setIsLoading(false);
        }, 1000 + Math.random() * 2000);
    };

    const generateAIResponse = (userInput: string): string => {
        const input = userInput.toLowerCase();
        
        if (input.includes('agente') && input.includes('python')) {
            return `Para crear un agente en Python, te recomiendo usar este patrón:

\`\`\`python
from typing import List, Dict, Any
import asyncio

class Agent:
    def __init__(self, name: str, tools: List[str] = None):
        self.name = name
        self.tools = tools or []
        self.memory = []
    
    async def run(self, task: str) -> str:
        # Implementa la lógica del agente aquí
        result = f"Agente {self.name} ejecutando: {task}"
        self.memory.append({"task": task, "result": result})
        return result
    
    def add_tool(self, tool: str):
        self.tools.append(tool)
\`\`\`

¿Te gustaría que te ayude a implementar alguna funcionalidad específica?`;
        }
        
        if (input.includes('herramienta') || input.includes('tool')) {
            return `Las herramientas son funciones que extienden las capacidades de tu agente. Aquí tienes un ejemplo:

\`\`\`python
def web_search(query: str, max_results: int = 5):
    """Herramienta para búsquedas web"""
    import requests
    
    # Simular búsqueda web
    results = [
        f"Resultado {i}: Información sobre {query}"
        for i in range(1, max_results + 1)
    ]
    
    return {
        "query": query,
        "results": results,
        "count": len(results)
    }

# Usar la herramienta
agent.add_tool(web_search)
response = agent.run("Busca información sobre machine learning")
\`\`\`

¿Necesitas ayuda con alguna herramienta específica?`;
        }
        
        if (input.includes('error') || input.includes('problema')) {
            return `Para resolver problemas comunes en notebooks de IA:

1. **Verifica las importaciones**: Asegúrate de que todas las librerías estén instaladas
2. **Revisa la sintaxis**: Usa el highlighting del editor para detectar errores
3. **Ejecuta celdas en orden**: Las variables deben estar definidas antes de usarlas
4. **Maneja excepciones**: Usa try-catch para debugging

\`\`\`python
try:
    result = agent.run("tarea compleja")
except Exception as e:
    print(f"Error: {e}")
    # Implementa fallback aquí
\`\`\`

¿Puedes compartir el error específico que estás viendo?`;
        }
        
        if (input.includes('mejor') || input.includes('optimizar')) {
            return `Para optimizar tu código de agentes de IA:

**Patrones recomendados:**
- Usa async/await para operaciones I/O
- Implementa caché para resultados costosos
- Usa generadores para datasets grandes
- Implementa rate limiting para APIs

**Ejemplo de optimización:**
\`\`\`python
import asyncio
from functools import lru_cache

@lru_cache(maxsize=100)
def expensive_operation(data: str) -> str:
    # Operación costosa
    return f"Procesado: {data}"

async def process_batch(items: List[str]):
    tasks = [expensive_operation(item) for item in items]
    return await asyncio.gather(*tasks)
\`\`\`

¿Hay alguna parte específica que quieras optimizar?`;
        }
        
        return `Entiendo que quieres ayuda con "${userInput}". Te puedo ayudar con:

• **Código Python** para agentes de IA
• **Arquitectura** de sistemas inteligentes
• **Debugging** y resolución de problemas
• **Optimización** y mejores prácticas
• **Integración** con APIs y servicios

¿Puedes ser más específico sobre lo que necesitas? Por ejemplo:
- "¿Cómo crear un agente que procese archivos?"
- "¿Cómo optimizar el rendimiento de mi código?"
- "¿Cómo integrar con una API externa?"`;
    };

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSendMessage();
        }
    };

    const quickActions = [
        { label: 'Crear Agente', prompt: '¿Cómo crear un agente básico en Python?' },
        { label: 'Herramientas', prompt: '¿Cómo implementar herramientas personalizadas?' },
        { label: 'Debug', prompt: '¿Cómo debuggear mi código de agente?' },
        { label: 'Optimizar', prompt: '¿Cómo optimizar el rendimiento de mi agente?' }
    ];

    if (isMinimized) {
        return (
            <div className="fixed bottom-4 right-4 z-50">
                <Button
                    onClick={() => setIsMinimized(false)}
                    className="bg-blue-600 hover:bg-blue-700 text-white rounded-full w-12 h-12 shadow-lg"
                >
                    <Bot className="w-6 h-6" />
                </Button>
            </div>
        );
    }

    return (
        <div className={`fixed bottom-4 right-4 z-50 ${isOpen ? 'w-96' : 'w-0'} transition-all duration-300`}>
            <div className="bg-white border border-gray-200 rounded-lg shadow-lg h-96 flex flex-col">
                {/* Header */}
                <div className="flex items-center justify-between p-3 border-b bg-blue-600 text-white rounded-t-lg">
                    <div className="flex items-center space-x-2">
                        <Bot className="w-5 h-5" />
                        <span className="font-medium">Asistente IA</span>
                    </div>
                    <div className="flex items-center space-x-1">
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setIsMinimized(true)}
                            className="text-white hover:bg-blue-700 p-1"
                        >
                            <Minimize2 className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setIsOpen(false)}
                            className="text-white hover:bg-blue-700 p-1"
                        >
                            <X className="w-4 h-4" />
                        </Button>
                    </div>
                </div>

                {/* Quick Actions */}
                <div className="p-3 border-b bg-gray-50">
                    <div className="text-xs text-gray-600 mb-2">Acciones rápidas:</div>
                    <div className="flex flex-wrap gap-2">
                        {quickActions.map((action, index) => (
                            <button
                                key={index}
                                onClick={() => setInputValue(action.prompt)}
                                className="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded hover:bg-blue-200 transition-colors"
                            >
                                {action.label}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Messages */}
                <div className="flex-1 overflow-auto p-3 space-y-3">
                    {messages.map((message) => (
                        <div
                            key={message.id}
                            className={`flex ${message.role === 'user' ? 'justify-end' : 'justify-start'}`}
                        >
                            <div
                                className={`max-w-xs px-3 py-2 rounded-lg ${
                                    message.role === 'user'
                                        ? 'bg-blue-600 text-white'
                                        : 'bg-gray-100 text-gray-800'
                                }`}
                            >
                                <div className="text-sm whitespace-pre-wrap">{message.content}</div>
                                <div className={`text-xs mt-1 ${
                                    message.role === 'user' ? 'text-blue-200' : 'text-gray-500'
                                }`}>
                                    {message.timestamp.toLocaleTimeString()}
                                </div>
                            </div>
                        </div>
                    ))}
                    
                    {isLoading && (
                        <div className="flex justify-start">
                            <div className="bg-gray-100 text-gray-800 px-3 py-2 rounded-lg">
                                <div className="flex items-center space-x-2">
                                    <Loader2 className="w-4 h-4 animate-spin" />
                                    <span className="text-sm">Pensando...</span>
                                </div>
                            </div>
                        </div>
                    )}
                    
                    <div ref={messagesEndRef} />
                </div>

                {/* Input */}
                <div className="p-3 border-t">
                    <div className="flex space-x-2">
                        <textarea
                            value={inputValue}
                            onChange={(e) => setInputValue(e.target.value)}
                            onKeyPress={handleKeyPress}
                            placeholder="Escribe tu pregunta..."
                            className="flex-1 px-3 py-2 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-500"
                            rows={2}
                        />
                        <Button
                            onClick={handleSendMessage}
                            disabled={!inputValue.trim() || isLoading}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg disabled:opacity-50"
                        >
                            <Send className="w-4 h-4" />
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
