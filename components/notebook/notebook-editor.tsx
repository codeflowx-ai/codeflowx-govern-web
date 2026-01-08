'use client';

import React, { useState, useEffect } from 'react';
import { NotebookCell, AgentTool, AgentConfig } from '@/app/config/notebook-types-simple';
import { mockExecutionResults } from '@/app/config/notebook-mock-data';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { ResizablePanel, SplitterLayout } from '@/components/ui/resizable-panel';
import NotebookCellComponent from './notebook-cell';
import WorkspaceTree from './workspace-tree';
import NotebookTemplates from './notebook-templates';
import AIAssistant from './ai-assistant';
import { 
    Plus, 
    Save,
    Download,
    Upload,
    Brain,
    Zap,
    Code,
    Settings,
    Wrench,
    TestTube,
    FileText,
    Play,
    Square,
    RotateCcw,
    FolderOpen,
    Star,
    BookOpen
} from 'lucide-react';
import DevelopmentBanner from '@/components/ui/development-banner';

interface NotebookEditorProps {
    sessionId?: string;
}

export default function NotebookEditor({ sessionId }: NotebookEditorProps) {
    const [cells, setCells] = useState<NotebookCell[]>([]);
    const [activeCell, setActiveCell] = useState<string | null>(null);
    const [isMockMode, setIsMockMode] = useState(true);
    const [isRunning, setIsRunning] = useState(false);
    const [showWorkspace, setShowWorkspace] = useState(true);
    const [currentSession, setCurrentSession] = useState<string>('Untitled Notebook');
    const [workspaceWidth, setWorkspaceWidth] = useState(300);
    const [chatWidth, setChatWidth] = useState(350);

    useEffect(() => {
        // Cargar sesión existente o crear nueva
        if (sessionId) {
            // TODO: Cargar desde API
            loadSession(sessionId);
        } else {
            createNewSession();
        }
    }, [sessionId]);

    const createNewSession = () => {
        const newCell: NotebookCell = {
            id: '1',
            type: 'code',
            content: '# Bienvenido al Notebook de Agentes de IA\n\nEscribe tu código Python aquí para crear agentes inteligentes.',
            language: 'python',
            status: 'idle'
        };
        setCells([newCell]);
        setActiveCell('1');
    };

    const loadSession = (id: string) => {
        // TODO: Implementar carga desde API
        console.log('Cargando sesión:', id);
    };

    const addCell = (type: NotebookCell['type']) => {
        const newCell: NotebookCell = {
            id: Date.now().toString(),
            type,
            content: getDefaultContent(type),
            language: getLanguageForType(type),
            status: 'idle'
        };
        
        setCells(prev => [...prev, newCell]);
        setActiveCell(newCell.id);
    };

    const getDefaultContent = (type: NotebookCell['type']): string => {
        switch (type) {
            case 'config':
                return JSON.stringify({
                    name: 'Mi Agente',
                    description: 'Descripción del agente',
                    model: 'gpt-4',
                    temperature: 0.7,
                    max_tokens: 2000,
                    tools: [],
                    system_prompt: 'Eres un asistente inteligente...'
                }, null, 2);
            case 'tool':
                return `def mi_herramienta(param1: str, param2: int = 10):
    """Descripción de la herramienta"""
    # Implementa tu lógica aquí
    result = f"Procesando {param1} con {param2}"
    return result`;
            case 'test':
                return `# Prueba tu agente aquí
# response = agent.run("Tu prompt de prueba")
# print(response)`;
            case 'markdown':
                return '# Título\n\nDescripción de la funcionalidad...';
            default:
                return '# Nueva celda de código\n\nEscribe tu código aquí...';
        }
    };

    const getLanguageForType = (type: NotebookCell['type']): NotebookCell['language'] => {
        switch (type) {
            case 'config': return 'json';
            case 'tool': return 'python';
            case 'test': return 'python';
            case 'markdown': return 'markdown';
            default: return 'python';
        }
    };

    const updateCellContent = (cellId: string, content: string) => {
        setCells(prev => prev.map(cell => 
            cell.id === cellId ? { ...cell, content } : cell
        ));
    };

    const executeCell = async (cellId: string) => {
        const cell = cells.find(c => c.id === cellId);
        if (!cell) return;

        // Marcar como ejecutándose
        setCells(prev => prev.map(c => 
            c.id === cellId ? { ...c, status: 'running' } : c
        ));

        if (isMockMode) {
            // Simular ejecución
            await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 2000));
            
            const mockResult = getMockResult(cell);
            setCells(prev => prev.map(c => 
                c.id === cellId ? { 
                    ...c, 
                    status: 'success', 
                    output: mockResult.output 
                } : c
            ));
        } else {
            // TODO: Ejecutar en backend real
            console.log('Ejecutando en backend:', cell.content);
        }
    };

    const executeAllCells = async () => {
        if (isRunning) return;
        
        setIsRunning(true);
        const executableCells = cells.filter(cell => cell.type === 'code' || cell.type === 'tool');
        
        for (let i = 0; i < executableCells.length; i++) {
            const cell = executableCells[i];
            await executeCell(cell.id);
            
            // Pequeña pausa entre celdas
            if (i < executableCells.length - 1) {
                await new Promise(resolve => setTimeout(resolve, 500));
            }
        }
        
        setIsRunning(false);
    };

    const stopExecution = () => {
        setIsRunning(false);
        // Marcar todas las celdas como idle
        setCells(prev => prev.map(cell => ({ ...cell, status: 'idle' })));
    };

    const resetAllCells = () => {
        setCells(prev => prev.map(cell => ({ ...cell, status: 'idle', output: undefined })));
    };

    const getMockResult = (cell: NotebookCell) => {
        // Simular diferentes tipos de output basado en el contenido
        if (cell.content.includes('web_search')) {
            return mockExecutionResults.web_search;
        } else if (cell.content.includes('database_query')) {
            return mockExecutionResults.database_query;
        } else if (cell.content.includes('file_processor')) {
            return mockExecutionResults.file_processor;
        } else {
            return {
                success: true,
                output: `✅ Ejecutado exitosamente en ${(Math.random() * 2 + 0.5).toFixed(2)}s\n\nOutput:\n${cell.content.split('\n').slice(0, 3).join('\n')}...`,
                executionTime: Math.random() * 2 + 0.5
            };
        }
    };

    const deleteCell = (cellId: string) => {
        setCells(prev => prev.filter(cell => cell.id !== cellId));
        if (activeCell === cellId) {
            setActiveCell(cells.length > 1 ? cells[0].id : null);
        }
    };

    const moveCellUp = (cellId: string) => {
        setCells(prev => {
            const index = prev.findIndex(cell => cell.id === cellId);
            if (index <= 0) return prev;
            
            const newCells = [...prev];
            [newCells[index], newCells[index - 1]] = [newCells[index - 1], newCells[index]];
            return newCells;
        });
    };

    const moveCellDown = (cellId: string) => {
        setCells(prev => {
            const index = prev.findIndex(cell => cell.id === cellId);
            if (index >= prev.length - 1) return prev;
            
            const newCells = [...prev];
            [newCells[index], newCells[index + 1]] = [newCells[index + 1], newCells[index]];
            return newCells;
        });
    };

    const handleTemplateSelect = (template: any) => {
        setCells(template.cells);
        setCurrentSession(template.name);
        setActiveCell(template.cells[0]?.id || null);
    };

    const handleOpenFile = (path: string, type: string) => {
        console.log('Abriendo archivo:', path, type);
        // TODO: Implementar apertura de archivos
    };

    const handleCreateNew = (type: string, parentPath?: string) => {
        console.log('Creando nuevo:', type, parentPath);
        // TODO: Implementar creación de archivos
    };

    const createNotebookAsTemplate = () => {
        // TODO: Implementar creación de plantilla
        console.log('Creando notebook como plantilla:', {
            name: currentSession,
            cells: cells,
            description: 'Notebook creado por el usuario'
        });
        
        // Mostrar mensaje de éxito
        alert('Notebook guardado como plantilla exitosamente');
    };

    return (
        <div className="h-screen flex flex-col bg-gray-50">
            {/* Header del Notebook */}
            <div className="bg-white border-b px-6 py-3 flex items-center justify-between">
                <div className="flex items-center space-x-4">
                    <h1 className="text-xl font-semibold">{currentSession}</h1>
                    <div className="flex items-center space-x-2">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                            isMockMode ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'
                        }`}>
                            {isMockMode ? 'Modo Mock' : 'Modo Real'}
                        </span>
                        <DevelopmentBanner showEarlyAdopterButton={isMockMode} />
                    </div>
                </div>
                
                <div className="flex items-center space-x-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => setIsMockMode(!isMockMode)}
                    >
                        {isMockMode ? 'Cambiar a Real' : 'Cambiar a Mock'}
                    </Button>
                    <Button variant="outline" size="sm">
                        <Save className="w-4 h-4 mr-2" />
                        Guardar
                    </Button>
                    <Button variant="outline" size="sm">
                        <Download className="w-4 h-4 mr-2" />
                        Exportar
                    </Button>
                    <Button 
                        variant="outline" 
                        size="sm"
                        onClick={createNotebookAsTemplate}
                        className="text-purple-600 hover:text-purple-700 border-purple-300"
                    >
                        <Star className="w-4 h-4 mr-2" />
                        Crear Plantilla
                    </Button>
                </div>
            </div>

            {/* Toolbar de Acciones */}
            <div className="bg-white border-b px-6 py-3 flex items-center justify-between">
                <div className="flex items-center space-x-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => addCell('code')}
                    >
                        <Code className="w-4 h-4 mr-2" />
                        Código
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => addCell('config')}
                    >
                        <Settings className="w-4 h-4 mr-2" />
                        Configuración
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => addCell('tool')}
                    >
                        <Wrench className="w-4 h-4 mr-2" />
                        Herramienta
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => addCell('test')}
                    >
                        <TestTube className="w-4 h-4 mr-2" />
                        Test
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={() => addCell('markdown')}
                    >
                        <FileText className="w-4 h-4 mr-2" />
                        Markdown
                    </Button>
                </div>

                <div className="flex items-center space-x-2">
                    <Button
                        variant="success"
                        size="sm"
                        onClick={executeAllCells}
                        disabled={isRunning || cells.length === 0}
                    >
                        <Play className="w-4 h-4 mr-2" />
                        Ejecutar Todo
                    </Button>
                    <Button
                        variant="danger"
                        size="sm"
                        onClick={stopExecution}
                        disabled={!isRunning}
                    >
                        <Square className="w-4 h-4 mr-2" />
                        Detener
                    </Button>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={resetAllCells}
                    >
                        <RotateCcw className="w-4 h-4 mr-2" />
                        Resetear
                    </Button>
                </div>
            </div>

            {/* Contenido Principal con Splitters */}
            <div className="flex-1 flex overflow-hidden">
                {/* Workspace Izquierdo */}
                {showWorkspace && (
                    <ResizablePanel
                        minWidth={200}
                        maxWidth={600}
                        defaultWidth={workspaceWidth}
                        onResize={setWorkspaceWidth}
                        className="bg-white border-r border-gray-200"
                    >
                        <WorkspaceTree 
                            onOpenFile={handleOpenFile}
                            onCreateNew={handleCreateNew}
                        />
                    </ResizablePanel>
                )}

                {/* Contenido del Notebook */}
                <div className="flex-1 flex overflow-hidden">
                    <div className="flex-1 overflow-auto p-6 space-y-4">
                        {cells.map((cell, index) => (
                            <NotebookCellComponent
                                key={cell.id}
                                cell={cell}
                                index={index}
                                totalCells={cells.length}
                                onUpdate={updateCellContent}
                                onExecute={executeCell}
                                onDelete={deleteCell}
                                onMoveUp={moveCellUp}
                                onMoveDown={moveCellDown}
                            />
                        ))}
                        
                        {cells.length === 0 && (
                            <div className="text-center py-12 text-gray-500">
                                <Brain className="w-16 h-16 mx-auto mb-4 text-gray-300" />
                                <p className="text-lg">No hay celdas en tu notebook</p>
                                <p className="text-sm">Usa los botones de arriba para agregar celdas</p>
                            </div>
                        )}
                    </div>

                    {/* Asistente de IA a la Derecha */}
                    <ResizablePanel
                        minWidth={250}
                        maxWidth={500}
                        defaultWidth={chatWidth}
                        onResize={setChatWidth}
                        className="bg-white border-l border-gray-200"
                    >
                        <AIAssistant />
                    </ResizablePanel>
                </div>
            </div>
        </div>
    );
}
