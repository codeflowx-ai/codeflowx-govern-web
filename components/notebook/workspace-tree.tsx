'use client';

import React, { useState } from 'react';
import { 
    Folder, 
    FileText, 
    FileCode, 
    Brain, 
    ChevronRight, 
    ChevronDown,
    Plus,
    FolderPlus,
    FilePlus
} from 'lucide-react';
import { Button } from '@/components/ui/button';

interface WorkspaceItem {
    id: string;
    name: string;
    type: 'folder' | 'notebook' | 'python' | 'json' | 'yaml' | 'markdown';
    path: string;
    children?: WorkspaceItem[];
    isOpen?: boolean;
}

interface WorkspaceTreeProps {
    onOpenFile: (path: string, type: string) => void;
    onCreateNew: (type: string, parentPath?: string) => void;
}

export default function WorkspaceTree({ onOpenFile, onCreateNew }: WorkspaceTreeProps) {
    const [workspace, setWorkspace] = useState<WorkspaceItem[]>([
        {
            id: '1',
            name: 'Workspace',
            type: 'folder',
            path: '/workspace',
            isOpen: true,
            children: [
                {
                    id: '2',
                    name: 'Notebooks',
                    type: 'folder',
                    path: '/workspace/notebooks',
                    isOpen: true,
                    children: [
                        {
                            id: '3',
                            name: 'agente_automation.ipynb',
                            type: 'notebook',
                            path: '/workspace/notebooks/agente_automation.ipynb'
                        },
                        {
                            id: '4',
                            name: 'data_analysis.ipynb',
                            type: 'notebook',
                            path: '/workspace/notebooks/data_analysis.ipynb'
                        }
                    ]
                },
                {
                    id: '5',
                    name: 'Scripts',
                    type: 'folder',
                    path: '/workspace/scripts',
                    isOpen: false,
                    children: [
                        {
                            id: '6',
                            name: 'data_processor.py',
                            type: 'python',
                            path: '/workspace/scripts/data_processor.py'
                        },
                        {
                            id: '7',
                            name: 'api_client.py',
                            type: 'python',
                            path: '/workspace/scripts/api_client.py'
                        }
                    ]
                },
                {
                    id: '8',
                    name: 'Config',
                    type: 'folder',
                    path: '/workspace/config',
                    isOpen: false,
                    children: [
                        {
                            id: '9',
                            name: 'agent_config.json',
                            type: 'json',
                            path: '/workspace/config/agent_config.json'
                        },
                        {
                            id: '10',
                            name: 'tools.yaml',
                            type: 'yaml',
                            path: '/workspace/config/tools.yaml'
                        }
                    ]
                },
                {
                    id: '11',
                    name: 'Documentation',
                    type: 'folder',
                    path: '/workspace/docs',
                    isOpen: false,
                    children: [
                        {
                            id: '12',
                            name: 'README.md',
                            type: 'markdown',
                            path: '/workspace/docs/README.md'
                        },
                        {
                            id: '13',
                            name: 'api_docs.md',
                            type: 'markdown',
                            path: '/workspace/docs/api_docs.md'
                        }
                    ]
                }
            ]
        }
    ]);

    const toggleFolder = (itemId: string) => {
        const toggleItem = (items: WorkspaceItem[]): WorkspaceItem[] => {
            return items.map(item => {
                if (item.id === itemId) {
                    return { ...item, isOpen: !item.isOpen };
                }
                if (item.children) {
                    return { ...item, children: toggleItem(item.children) };
                }
                return item;
            });
        };
        setWorkspace(toggleItem(workspace));
    };

    const getIcon = (type: string) => {
        switch (type) {
            case 'folder': return <Folder className="w-4 h-4 text-blue-500" />;
            case 'notebook': return <Brain className="w-4 h-4 text-purple-500" />;
            case 'python': return <FileCode className="w-4 h-4 text-green-500" />;
            case 'json': return <FileText className="w-4 h-4 text-yellow-500" />;
            case 'yaml': return <FileText className="w-4 h-4 text-red-500" />;
            case 'markdown': return <FileText className="w-4 h-4 text-blue-500" />;
            default: return <FileText className="w-4 h-4 text-gray-500" />;
        }
    };

    const renderItem = (item: WorkspaceItem, level: number = 0) => {
        const isFolder = item.type === 'folder';
        const hasChildren = item.children && item.children.length > 0;
        const isOpen = item.isOpen;

        return (
            <div key={item.id}>
                <div 
                    className={`flex items-center space-x-2 px-2 py-1 hover:bg-gray-100 rounded cursor-pointer ${
                        level > 0 ? 'ml-' + (level * 4) : ''
                    }`}
                    onClick={() => {
                        if (isFolder) {
                            toggleFolder(item.id);
                        } else {
                            onOpenFile(item.path, item.type);
                        }
                    }}
                >
                    {isFolder && (
                        <span className="w-4 h-4">
                            {hasChildren && (isOpen ? <ChevronDown className="w-4 h-4" /> : <ChevronRight className="w-4 h-4" />)}
                        </span>
                    )}
                    {getIcon(item.type)}
                    <span className="text-sm truncate">{item.name}</span>
                </div>
                
                {isFolder && isOpen && hasChildren && (
                    <div className="ml-4">
                        {item.children!.map(child => renderItem(child, level + 1))}
                    </div>
                )}
            </div>
        );
    };

    return (
        <div className="h-full flex flex-col bg-white border-r">
            {/* Header del Workspace */}
            <div className="p-4 border-b">
                <div className="flex items-center justify-between mb-3">
                    <h3 className="font-semibold text-gray-800">Workspace</h3>
                    <div className="flex space-x-1">
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => onCreateNew('folder')}
                            className="p-1"
                        >
                            <FolderPlus className="w-4 h-4" />
                        </Button>
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => onCreateNew('notebook')}
                            className="p-1"
                        >
                            <FilePlus className="w-4 h-4" />
                        </Button>
                    </div>
                </div>
                
                {/* Barra de búsqueda */}
                <div className="relative">
                    <input
                        type="text"
                        placeholder="Buscar archivos..."
                        className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
            </div>

            {/* Tree del Workspace */}
            <div className="flex-1 overflow-auto p-2">
                {workspace.map(item => renderItem(item))}
            </div>

            {/* Footer con estadísticas */}
            <div className="p-3 border-t bg-gray-50">
                <div className="text-xs text-gray-500">
                    <div className="flex justify-between">
                        <span>Archivos:</span>
                        <span className="font-medium">13</span>
                    </div>
                    <div className="flex justify-between">
                        <span>Notebooks:</span>
                        <span className="font-medium">2</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
