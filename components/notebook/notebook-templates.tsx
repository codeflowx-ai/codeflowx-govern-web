'use client';

import React, { useState } from 'react';
import { ChevronDown, Brain, Bot, Database, Globe, Zap, FileText, Settings } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { NotebookCell } from '@/app/config/notebook-types';

interface NotebookTemplate {
    id: string;
    name: string;
    description: string;
    icon: React.ReactNode;
    category: string;
    cells: NotebookCell[];
}

interface NotebookTemplatesProps {
    onSelectTemplate: (template: NotebookTemplate) => void;
}

export default function NotebookTemplates({ onSelectTemplate }: NotebookTemplatesProps) {
    const [isOpen, setIsOpen] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState<string>('all');

    const templates: NotebookTemplate[] = [
        {
            id: '1',
            name: 'Agente de Automatización',
            description: 'Agente básico para automatizar tareas repetitivas',
            icon: <Bot className="w-5 h-5" />,
            category: 'automation',
            cells: [
                {
                    id: '1',
                    type: 'config',
                    content: JSON.stringify({
                        name: 'Agente de Automatización',
                        description: 'Agente para automatizar tareas repetitivas',
                        model: 'gpt-4',
                        temperature: 0.7,
                        max_tokens: 2000,
                        tools: ['file_processor', 'web_search'],
                        system_prompt: 'Eres un agente especializado en automatizar tareas repetitivas y procesos de negocio.'
                    }, null, 2),
                    language: 'json',
                    status: 'idle'
                },
                {
                    id: '2',
                    type: 'tool',
                    content: `def file_processor(file_path: str, operation: str = 'read'):
    """Procesa archivos del sistema"""
    # Implementa tu lógica aquí
    return f"Archivo {file_path} procesado con operación {operation}"`,
                    language: 'python',
                    status: 'idle'
                },
                {
                    id: '3',
                    type: 'code',
                    content: `# Crear instancia del agente
from agent_framework import Agent

agent = Agent(
    name="Agente de Automatización",
    model="gpt-4",
    temperature=0.7
)

# Agregar herramientas
agent.add_tool(file_processor)

print("Agente configurado para automatización!")`,
                    language: 'python',
                    status: 'idle'
                }
            ]
        },
        {
            id: '2',
            name: 'Agente de Análisis de Datos',
            description: 'Agente especializado en análisis y visualización de datos',
            icon: <Database className="w-5 h-5" />,
            category: 'data',
            cells: [
                {
                    id: '1',
                    type: 'config',
                    content: JSON.stringify({
                        name: 'Agente de Análisis de Datos',
                        description: 'Agente para análisis y visualización de datos',
                        model: 'gpt-4',
                        temperature: 0.3,
                        max_tokens: 3000,
                        tools: ['database_query', 'data_visualization'],
                        system_prompt: 'Eres un agente especializado en análisis de datos, estadísticas y visualizaciones.'
                    }, null, 2),
                    language: 'json',
                    status: 'idle'
                },
                {
                    id: '2',
                    type: 'tool',
                    content: `def data_visualization(data, chart_type='bar'):
    """Crea visualizaciones de datos"""
    import matplotlib.pyplot as plt
    import pandas as pd
    
    if chart_type == 'bar':
        plt.bar(data.keys(), data.values())
    elif chart_type == 'line':
        plt.plot(list(data.keys()), list(data.values()))
    
    plt.title('Visualización de Datos')
    plt.show()
    return "Gráfico generado exitosamente"`,
                    language: 'python',
                    status: 'idle'
                }
            ]
        },
        {
            id: '3',
            name: 'Agente de Investigación Web',
            description: 'Agente para investigación y análisis de información web',
            icon: <Globe className="w-5 h-5" />,
            category: 'research',
            cells: [
                {
                    id: '1',
                    type: 'config',
                    content: JSON.stringify({
                        name: 'Agente de Investigación Web',
                        description: 'Agente para investigación y análisis web',
                        model: 'gpt-4',
                        temperature: 0.5,
                        max_tokens: 2500,
                        tools: ['web_search', 'content_analyzer'],
                        system_prompt: 'Eres un agente especializado en investigación web y análisis de contenido.'
                    }, null, 2),
                    language: 'json',
                    status: 'idle'
                },
                {
                    id: '2',
                    type: 'tool',
                    content: `def content_analyzer(url: str):
    """Analiza el contenido de una URL"""
    import requests
    from bs4 import BeautifulSoup
    
    response = requests.get(url)
    soup = BeautifulSoup(response.content, 'html.parser')
    
    # Extraer título y contenido principal
    title = soup.find('title').text if soup.find('title') else 'Sin título'
    content = soup.get_text()[:500] + "..."
    
    return {
        'title': title,
        'content': content,
        'url': url
    }`,
                    language: 'python',
                    status: 'idle'
                }
            ]
        },
        {
            id: '4',
            name: 'Agente de Chat Inteligente',
            description: 'Agente conversacional con memoria y contexto',
            icon: <Zap className="w-5 h-5" />,
            category: 'chat',
            cells: [
                {
                    id: '1',
                    type: 'config',
                    content: JSON.stringify({
                        name: 'Agente de Chat Inteligente',
                        description: 'Agente conversacional con memoria',
                        model: 'gpt-4',
                        temperature: 0.8,
                        max_tokens: 1500,
                        tools: ['memory_manager', 'context_analyzer'],
                        system_prompt: 'Eres un agente conversacional amigable y útil con excelente memoria.'
                    }, null, 2),
                    language: 'json',
                    status: 'idle'
                },
                {
                    id: '2',
                    type: 'tool',
                    content: `class MemoryManager:
    def __init__(self):
        self.conversation_history = []
        self.user_preferences = {}
    
    def add_to_history(self, message, role):
        self.conversation_history.append({
            'role': role,
            'content': message,
            'timestamp': datetime.now()
        })
    
    def get_context(self, limit=10):
        return self.conversation_history[-limit:]`,
                    language: 'python',
                    status: 'idle'
                }
            ]
        }
    ];

    const categories = [
        { id: 'all', name: 'Todas', icon: <Brain className="w-4 h-4" /> },
        { id: 'automation', name: 'Automatización', icon: <Bot className="w-4 h-4" /> },
        { id: 'data', name: 'Datos', icon: <Database className="w-4 h-4" /> },
        { id: 'research', name: 'Investigación', icon: <Globe className="w-4 h-4" /> },
        { id: 'chat', name: 'Chat', icon: <Zap className="w-4 h-4" /> }
    ];

    const filteredTemplates = selectedCategory === 'all' 
        ? templates 
        : templates.filter(t => t.category === selectedCategory);

    return (
        <div className="relative">
            <Button
                variant="outline"
                onClick={() => setIsOpen(!isOpen)}
                className="flex items-center space-x-2"
            >
                <FileText className="w-4 h-4" />
                <span>Plantillas</span>
                <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
            </Button>

            {isOpen && (
                <div className="absolute top-full left-0 mt-2 w-80 bg-white border border-gray-200 rounded-lg shadow-lg z-50">
                    {/* Header */}
                    <div className="p-4 border-b">
                        <h3 className="font-semibold text-gray-800 mb-3">Plantillas de Notebook</h3>
                        
                        {/* Filtros por categoría */}
                        <div className="flex space-x-2 mb-3">
                            {categories.map(category => (
                                <button
                                    key={category.id}
                                    onClick={() => setSelectedCategory(category.id)}
                                    className={`flex items-center space-x-1 px-2 py-1 rounded text-xs transition-colors ${
                                        selectedCategory === category.id
                                            ? 'bg-blue-100 text-blue-700'
                                            : 'text-gray-600 hover:bg-gray-100'
                                    }`}
                                >
                                    {category.icon}
                                    <span>{category.name}</span>
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* Lista de plantillas */}
                    <div className="max-h-96 overflow-auto">
                        {filteredTemplates.map(template => (
                            <div
                                key={template.id}
                                className="p-4 border-b last:border-b-0 hover:bg-gray-50 cursor-pointer transition-colors"
                                onClick={() => {
                                    onSelectTemplate(template);
                                    setIsOpen(false);
                                }}
                            >
                                <div className="flex items-start space-x-3">
                                    <div className="text-blue-500 mt-1">
                                        {template.icon}
                                    </div>
                                    <div className="flex-1">
                                        <h4 className="font-medium text-gray-800 mb-1">
                                            {template.name}
                                        </h4>
                                        <p className="text-sm text-gray-600 mb-2">
                                            {template.description}
                                        </p>
                                        <div className="flex items-center space-x-2 text-xs text-gray-500">
                                            <span className="flex items-center space-x-1">
                                                <Settings className="w-3 h-3" />
                                                <span>{template.cells.length} celdas</span>
                                            </span>
                                            <span className="capitalize">{template.category}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Footer */}
                    <div className="p-3 border-t bg-gray-50">
                        <p className="text-xs text-gray-500 text-center">
                            Selecciona una plantilla para crear un notebook preconfigurado
                        </p>
                    </div>
                </div>
            )}
        </div>
    );
}
