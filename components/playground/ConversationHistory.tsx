// components/playground/ConversationHistory.tsx
'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
    Search,
    Plus,
    MoreHorizontal,
    MessageSquare,
    Clock,
    Star,
    Trash2,
    Edit,
    Copy,
    Download,
    Filter,
    Calendar
} from 'lucide-react'
interface Conversation {
    id: string
    name: string
    lastMessage: string
    timestamp: string
    messageCount: number
    model: string
    starred: boolean
    tags: string[]
}

const conversations: Conversation[] = [
    {
        id: '1',
        name: 'React Component Optimization',
        lastMessage: 'How can I optimize this React component for better performance?',
        timestamp: '2 hours ago',
        messageCount: 15,
        model: 'Claude 3 Sonnet',
        starred: true,
        tags: ['React', 'Performance', 'Code Review']
    },
    {
        id: '2',
        name: 'API Documentation Generation',
        lastMessage: 'Generate comprehensive API documentation for this REST service...',
        timestamp: '1 day ago',
        messageCount: 8,
        model: 'GPT-4',
        starred: false,
        tags: ['API', 'Documentation', 'REST']
    },
    {
        id: '3',
        name: 'Database Query Optimization',
        lastMessage: 'Help me optimize this SQL query for better performance...',
        timestamp: '2 days ago',
        messageCount: 12,
        model: 'Claude 3 Opus',
        starred: true,
        tags: ['SQL', 'Database', 'Performance']
    },
    {
        id: '4',
        name: 'Machine Learning Model Training',
        lastMessage: 'What\'s the best approach for training this neural network?',
        timestamp: '3 days ago',
        messageCount: 25,
        model: 'GPT-4',
        starred: false,
        tags: ['ML', 'Neural Networks', 'Training']
    },
    {
        id: '5',
        name: 'CSS Grid Layout Help',
        lastMessage: 'I need help creating a responsive grid layout...',
        timestamp: '1 week ago',
        messageCount: 6,
        model: 'Claude 3 Haiku',
        starred: false,
        tags: ['CSS', 'Grid', 'Responsive']
    }
]

interface ConversationHistoryProps {
    selectedConversation: string | null
    onConversationSelect: (id: string) => void
    onNewConversation: () => void
}

export function ConversationHistory({
                                        selectedConversation,
                                        onConversationSelect,
                                        onNewConversation
                                    }: ConversationHistoryProps) {
    const [searchQuery, setSearchQuery] = useState('')
    const [showFilters, setShowFilters] = useState(false)
    const [filterBy, setFilterBy] = useState('all')

    const filteredConversations = conversations.filter(conv => {
        const matchesSearch = conv.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            conv.lastMessage.toLowerCase().includes(searchQuery.toLowerCase()) ||
            conv.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()))

        const matchesFilter = filterBy === 'all' ||
            (filterBy === 'starred' && conv.starred) ||
            (filterBy === 'recent' && new Date(conv.timestamp).getTime() > Date.now() - 7 * 24 * 60 * 60 * 1000)

        return matchesSearch && matchesFilter
    })

    const toggleStar = (id: string, e: React.MouseEvent) => {
        e.stopPropagation()
        // TODO: Implement star toggle
    }

    return (
        <div className="h-100 d-flex flex-column">
            {/* Header */}
            <div className="p-3 border-bottom">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h6 className="mb-0 fw-bold">Conversations</h6>
                    <Button variant="primary" size="sm" onClick={onNewConversation}>
                        <Plus size={16} />
                    </Button>
                </div>

                {/* Search */}
                <div className="input-group input-group-sm mb-2">
          <span className="input-group-text">
            <Search size={14} />
          </span>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search conversations..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>

                {/* Filters */}
                <div className="d-flex gap-1">
                    <Button
                        variant={showFilters ? "primary" : "outline"}
                        size="sm"
                        onClick={() => setShowFilters(!showFilters)}
                    >
                        <Filter size={14} className="me-1" />
                        Filter
                    </Button>

                    <div className="dropdown">
                        <Button variant="outline" size="sm" data-bs-toggle="dropdown">
                            <Calendar size={14} className="me-1" />
                            {filterBy === 'all' ? 'All' : filterBy === 'starred' ? 'Starred' : 'Recent'}
                        </Button>
                        <ul className="dropdown-menu">
                            <li><button className="dropdown-item" onClick={() => setFilterBy('all')}>All Conversations</button></li>
                            <li><button className="dropdown-item" onClick={() => setFilterBy('starred')}>Starred Only</button></li>
                            <li><button className="dropdown-item" onClick={() => setFilterBy('recent')}>Recent (7 days)</button></li>
                        </ul>
                    </div>
                </div>

                {showFilters && (
                    <div className="mt-2 p-2 bg-light rounded">
                        <div className="small text-muted mb-1">Filter by tags:</div>
                        <div className="d-flex flex-wrap gap-1">
                            {['React', 'API', 'SQL', 'ML', 'CSS', 'Performance'].map(tag => (
                                <Badge key={tag} variant="outline" className="cursor-pointer small">
                                    {tag}
                                </Badge>
                            ))}
                        </div>
                    </div>
                )}
            </div>

            {/* Conversations List */}
            <div className="flex-fill overflow-auto">
                {filteredConversations.length === 0 ? (
                    <div className="p-4 text-center text-muted">
                        <MessageSquare size={32} className="mb-2 opacity-50" />
                        <p className="small mb-0">No conversations found</p>
                    </div>
                ) : (
                    filteredConversations.map(conv => (
                        <div
                            key={conv.id}
                            className={`p-3 border-bottom cursor-pointer hover-bg-light ${
                                selectedConversation === conv.id ? 'bg-primary bg-opacity-10 border-primary border-end-0' : ''
                            }`}
                            onClick={() => onConversationSelect(conv.id)}
                        >
                            <div className="d-flex justify-content-between align-items-start mb-2">
                                <h6 className="mb-0 small fw-medium text-truncate flex-fill me-2">{conv.name}</h6>
                                <div className="d-flex align-items-center gap-1">
                                    <button
                                        className={`btn btn-sm p-0 ${conv.starred ? 'text-warning' : 'text-muted'}`}
                                        onClick={(e) => toggleStar(conv.id, e)}
                                        style={{ background: 'none', border: 'none' }}
                                    >
                                        <Star size={14} fill={conv.starred ? 'currentColor' : 'none'} />
                                    </button>
                                    <Badge variant="light" className="border small">{conv.messageCount}</Badge>
                                </div>
                            </div>

                            <p className="text-muted small mb-2 text-truncate">{conv.lastMessage}</p>

                            <div className="d-flex justify-content-between align-items-center">
                                <div className="d-flex align-items-center gap-2">
                  <span className="text-muted" style={{ fontSize: '0.75rem' }}>
                    <Clock size={10} className="me-1" />
                      {conv.timestamp}
                  </span>
                                    <Badge variant="light" className="border text-xs">
                                        {conv.model}
                                    </Badge>
                                </div>

                                <div className="dropdown">
                                    <button
                                        className="btn btn-sm btn-outline-secondary p-1"
                                        data-bs-toggle="dropdown"
                                        onClick={(e) => e.stopPropagation()}
                                    >
                                        <MoreHorizontal size={12} />
                                    </button>
                                    <ul className="dropdown-menu dropdown-menu-end">
                                        <li><button className="dropdown-item small"><Edit size={12} className="me-2" />Rename</button></li>
                                        <li><button className="dropdown-item small"><Copy size={12} className="me-2" />Duplicate</button></li>
                                        <li><button className="dropdown-item small"><Download size={12} className="me-2" />Export</button></li>
                                        <li><hr className="dropdown-divider" /></li>
                                        <li><button className="dropdown-item small text-danger"><Trash2 size={12} className="me-2" />Delete</button></li>
                                    </ul>
                                </div>
                            </div>

                            {/* Tags */}
                            {conv.tags.length > 0 && (
                                <div className="mt-2">
                                    <div className="d-flex flex-wrap gap-1">
                                        {conv.tags.slice(0, 3).map(tag => (
                                            <Badge key={tag} variant="light" className="border text-xs">
                                                {tag}
                                            </Badge>
                                        ))}
                                        {conv.tags.length > 3 && (
                                            <Badge variant="light" className="border text-xs">
                                                +{conv.tags.length - 3}
                                            </Badge>
                                        )}
                                    </div>
                                </div>
                            )}
                        </div>
                    ))
                )}
            </div>

            {/* Footer Stats */}
            <div className="p-3 border-top bg-light">
                <div className="d-flex justify-content-between align-items-center small text-muted">
                    <span>{filteredConversations.length} conversations</span>
                    <span>{conversations.reduce((sum, conv) => sum + conv.messageCount, 0)} total messages</span>
                </div>
            </div>
        </div>
    )
}
