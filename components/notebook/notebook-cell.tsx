'use client';

import React, { useState } from 'react';
import { Editor } from '@monaco-editor/react';
import { NotebookCell } from '@/app/config/notebook-types-simple';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { 
    Play, 
    Trash2, 
    Settings, 
    Code, 
    Wrench, 
    TestTube, 
    FileText,
    Copy,
    Check,
    MoveUp,
    MoveDown
} from 'lucide-react';

interface NotebookCellProps {
    cell: NotebookCell;
    index: number;
    totalCells: number;
    onUpdate: (cellId: string, content: string) => void;
    onExecute: (cellId: string) => void;
    onDelete: (cellId: string) => void;
    onMoveUp: (cellId: string) => void;
    onMoveDown: (cellId: string) => void;
}

export default function NotebookCellComponent({ 
    cell, 
    index,
    totalCells,
    onUpdate, 
    onExecute, 
    onDelete,
    onMoveUp,
    onMoveDown
}: NotebookCellProps) {
    const [copied, setCopied] = useState(false);

    const getCellIcon = (type: NotebookCell['type']) => {
        switch (type) {
            case 'config': return <Settings className="w-4 h-4" />;
            case 'tool': return <Wrench className="w-4 h-4" />;
            case 'test': return <TestTube className="w-4 h-4" />;
            case 'markdown': return <FileText className="w-4 h-4" />;
            default: return <Code className="w-4 h-4" />;
        }
    };

    const getStatusColor = (status: NotebookCell['status']) => {
        switch (status) {
            case 'running': return 'text-blue-500';
            case 'success': return 'text-green-500';
            case 'error': return 'text-red-500';
            default: return 'text-gray-400';
        }
    };

    const getStatusText = (status: NotebookCell['status']) => {
        switch (status) {
            case 'running': return 'Ejecutando...';
            case 'success': return 'Completado';
            case 'error': return 'Error';
            default: return 'Pendiente';
        }
    };

    const handleCopy = async () => {
        try {
            await navigator.clipboard.writeText(cell.content);
            setCopied(true);
            setTimeout(() => setCopied(false), 2000);
        } catch (err) {
            console.error('Error al copiar:', err);
        }
    };

    const getEditorHeight = () => {
        const lines = cell.content.split('\n').length;
        return Math.max(200, Math.min(400, lines * 20 + 40));
    };

    const getMonacoLanguage = (language: string) => {
        switch (language) {
            case 'python': return 'python';
            case 'json': return 'json';
            case 'yaml': return 'yaml';
            case 'markdown': return 'markdown';
            default: return 'python';
        }
    };

    return (
        <Card className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-2">
                <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                        {getCellIcon(cell.type)}
                        <span className="text-sm font-medium text-gray-600">
                            {cell.type.charAt(0).toUpperCase() + cell.type.slice(1)}
                        </span>
                        <span className={`text-xs ${getStatusColor(cell.status)}`}>
                            {getStatusText(cell.status)}
                        </span>
                    </div>
                    
                    <div className="flex items-center space-x-2">
                        {/* Controles de orden */}
                        <div className="flex items-center space-x-1 mr-2">
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => onMoveUp(cell.id)}
                                disabled={index === 0}
                                className="text-gray-400 hover:text-gray-600 p-1"
                            >
                                <MoveUp className="w-3 h-3" />
                            </Button>
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => onMoveDown(cell.id)}
                                disabled={index === totalCells - 1}
                                className="text-gray-400 hover:text-gray-600 p-1"
                            >
                                <MoveDown className="w-3 h-4" />
                            </Button>
                        </div>
                        
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={handleCopy}
                            className="text-gray-500 hover:text-gray-700"
                        >
                            {copied ? <Check className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
                        </Button>
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => onExecute(cell.id)}
                            disabled={cell.status === 'running'}
                            className="text-green-600 hover:text-green-800"
                        >
                            <Play className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => onDelete(cell.id)}
                            className="text-red-500 hover:text-red-700"
                        >
                            <Trash2 className="w-4 h-4" />
                        </Button>
                    </div>
                </div>
            </CardHeader>
            
            <CardContent className="pt-0">
                <div className="mb-4 border border-gray-200 rounded-lg overflow-hidden">
                    <Editor
                        height={getEditorHeight()}
                        language={getMonacoLanguage(cell.language)}
                        value={cell.content}
                        onChange={(value: string | undefined) => onUpdate(cell.id, value || '')}
                        theme="vs-dark"
                        options={{
                            minimap: { enabled: false },
                            fontSize: 14,
                            lineNumbers: 'on',
                            roundedSelection: false,
                            scrollBeyondLastLine: false,
                            automaticLayout: true,
                            wordWrap: 'on',
                            folding: true,
                            lineDecorationsWidth: 10,
                            lineNumbersMinChars: 3,
                            suggestOnTriggerCharacters: true,
                            quickSuggestions: true,
                            parameterHints: { enabled: true },
                            hover: { enabled: true },
                            contextmenu: true,
                            find: { addExtraSpaceOnTop: false },
                            smoothScrolling: true,
                            cursorBlinking: 'smooth',
                            cursorSmoothCaretAnimation: 'on',
                        }}
                    />
                </div>
                
                {cell.output && (
                    <div className="bg-gray-100 p-4 rounded-lg border-l-4 border-green-500">
                        <div className="text-sm text-gray-600 mb-2 font-medium">Output:</div>
                        <pre className="text-sm text-gray-800 whitespace-pre-wrap font-mono">{cell.output}</pre>
                    </div>
                )}

                {cell.status === 'running' && (
                    <div className="mt-4 flex items-center space-x-2 text-blue-600">
                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
                        <span className="text-sm">Ejecutando código...</span>
                    </div>
                )}
            </CardContent>
        </Card>
    );
}
