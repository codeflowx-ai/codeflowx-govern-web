// components/playground/ModelSelector.tsx
'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardBody } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import {
    Bot,
    CheckCircle,
    Info,
    Zap,
    Clock,
    Brain,
    ChevronDown
} from 'lucide-react'

interface Model {
    id: string
    name: string
    description: string
    provider: string
    maxTokens: number
    costPer1k: number
    speed: 'fast' | 'medium' | 'slow'
    capabilities: string[]
    recommended?: boolean
}

const models: Model[] = [
    {
        id: 'claude-3-opus',
        name: 'Claude 3 Opus',
        description: 'Most capable model for complex reasoning and analysis',
        provider: 'Anthropic',
        maxTokens: 4096,
        costPer1k: 0.015,
        speed: 'slow',
        capabilities: ['Advanced reasoning', 'Code generation', 'Creative writing', 'Analysis'],
        recommended: true
    },
    {
        id: 'claude-3-sonnet',
        name: 'Claude 3 Sonnet',
        description: 'Balanced performance and speed for most tasks',
        provider: 'Anthropic',
        maxTokens: 4096,
        costPer1k: 0.003,
        speed: 'medium',
        capabilities: ['General tasks', 'Code review', 'Writing', 'Q&A'],
        recommended: true
    },
    {
        id: 'claude-3-haiku',
        name: 'Claude 3 Haiku',
        description: 'Fastest model for quick responses',
        provider: 'Anthropic',
        maxTokens: 4096,
        costPer1k: 0.00025,
        speed: 'fast',
        capabilities: ['Quick responses', 'Simple tasks', 'Chat']
    },
    {
        id: 'gpt-4-turbo',
        name: 'GPT-4 Turbo',
        description: 'Latest GPT-4 with improved performance',
        provider: 'OpenAI',
        maxTokens: 8192,
        costPer1k: 0.01,
        speed: 'medium',
        capabilities: ['Advanced reasoning', 'Code generation', 'Multimodal']
    },
    {
        id: 'gpt-3.5-turbo',
        name: 'GPT-3.5 Turbo',
        description: 'Fast and cost-effective for most tasks',
        provider: 'OpenAI',
        maxTokens: 4096,
        costPer1k: 0.0015,
        speed: 'fast',
        capabilities: ['General tasks', 'Chat', 'Simple coding']
    }
]

interface ModelSelectorProps {
    selectedModel: string
    onModelChange: (modelId: string) => void
}

export function ModelSelector({ selectedModel, onModelChange }: ModelSelectorProps) {
    const [showDetails, setShowDetails] = useState(false)

    const getSpeedColor = (speed: string) => {
        switch (speed) {
            case 'fast': return 'success'
            case 'medium': return 'warning'
            case 'slow': return 'danger'
            default: return 'secondary'
        }
    }

    const getSpeedIcon = (speed: string) => {
        switch (speed) {
            case 'fast': return <Zap size={14} />
            case 'medium': return <Clock size={14} />
            case 'slow': return <Brain size={14} />
            default: return <Info size={14} />
        }
    }

    return (
        <div className="dropdown">
            <Button
                variant="primary"
                className="d-flex align-items-center"
                data-bs-toggle="dropdown"
            >
                <Bot size={16} className="me-2" />
                {models.find(m => m.id === selectedModel)?.name || 'Select Model'}
                <i className="bi bi-chevron-down ms-2"></i>
            </Button>

            <div className="dropdown-menu dropdown-menu-end p-0" style={{ width: '400px' }}>
                <div className="p-3 border-bottom">
                    <div className="d-flex justify-content-between align-items-center">
                        <h6 className="mb-0">Choose AI Model</h6>
                        <Button
                            variant="outline"
                            size="sm"
                            onClick={() => setShowDetails(!showDetails)}
                        >
                            {showDetails ? 'Simple' : 'Details'}
                        </Button>
                    </div>
                </div>

                <div className="p-2" style={{ maxHeight: '400px', overflowY: 'auto' }}>
                    {models.map(model => (
                        <div
                            key={model.id}
                            className={`p-3 rounded cursor-pointer hover-bg-light ${
                                selectedModel === model.id ? 'bg-primary bg-opacity-10 border border-primary' : ''
                            }`}
                            onClick={() => onModelChange(model.id)}
                        >
                            <div className="d-flex justify-content-between align-items-start mb-2">
                                <div className="flex-fill">
                                    <div className="d-flex align-items-center gap-2 mb-1">
                                        <h6 className="mb-0">{model.name}</h6>
                                        {model.recommended && <Badge variant="warning" className="small">Recommended</Badge>}
                                        {selectedModel === model.id && <CheckCircle size={16} className="text-primary" />}
                                    </div>
                                    <p className="text-muted small mb-2">{model.description}</p>

                                    <div className="d-flex align-items-center gap-3 small">
                                        <span className="text-muted">{model.provider}</span>
                                        <Badge variant={getSpeedColor(model.speed)} className="d-flex align-items-center gap-1">
                                            {getSpeedIcon(model.speed)}
                                            {model.speed}
                                        </Badge>
                                    </div>
                                </div>
                            </div>

                            {showDetails && (
                                <div className="mt-2 pt-2 border-top">
                                    <div className="row g-2 small">
                                        <div className="col-6">
                                            <span className="text-muted">Max tokens:</span>
                                            <span className="ms-1 fw-medium">{model.maxTokens.toLocaleString()}</span>
                                        </div>
                                        <div className="col-6">
                                            <span className="text-muted">Cost/1K:</span>
                                            <span className="ms-1 fw-medium">${model.costPer1k}</span>
                                        </div>
                                    </div>

                                    <div className="mt-2">
                                        <div className="text-muted small mb-1">Capabilities:</div>
                                        <div className="d-flex flex-wrap gap-1">
                                            {model.capabilities.map((cap, index) => (
                                                <Badge key={index} variant="light" className="border small">
                                                    {cap}
                                                </Badge>
                                            ))}
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                    ))}
                </div>

                <div className="p-3 border-top bg-light">
                    <div className="d-flex align-items-center text-muted small">
                        <Info size={14} className="me-2" />
                        <span>Model performance may vary based on task complexity</span>
                    </div>
                </div>
            </div>
        </div>
    )
}
