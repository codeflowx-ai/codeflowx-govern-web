// components/playground/PromptLibrary.tsx
'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import {
    Search,
    Plus,
    Star,
    Copy,
    Edit,
    Trash2,
    Code,
    FileText,
    MessageSquare,
    Lightbulb,
    Zap,
    Brain,
    Filter,
    Tag,
    Clock,
    TrendingUp,
    Heart,
    Eye,
    Play,
    MoreHorizontal
} from 'lucide-react'
interface PromptTemplate {
    id: string
    name: string
    description: string
    prompt: string
    category: string
    tags: string[]
    author: string
    usage: number
    rating: number
    starred: boolean
    createdAt: string
    variables?: string[]
}

const promptTemplates: PromptTemplate[] = [
    {
        id: '1',
        name: 'Code Review Assistant',
        description: 'Comprehensive code review with suggestions for improvements',
        prompt: `Please review the following code and provide detailed feedback on:

1. **Code Quality**: Readability, maintainability, and best practices
2. **Performance**: Potential optimizations and bottlenecks
3. **Security**: Vulnerabilities and security considerations
4. **Architecture**: Design patterns and structural improvements
5. **Testing**: Suggestions for test coverage and test cases

Code to review:
\`\`\`{language}
{code}
\`\`\`

Please provide specific, actionable recommendations with examples where possible.`,
        category: 'Development',
        tags: ['code-review', 'best-practices', 'optimization'],
        author: 'LEKA AI',
        usage: 1247,
        rating: 4.8,
        starred: true,
        createdAt: '2024-01-15',
        variables: ['language', 'code']
    },
    {
        id: '2',
        name: 'API Documentation Generator',
        description: 'Generate comprehensive API documentation from code',
        prompt: `Generate detailed API documentation for the following endpoint:

**Endpoint**: {endpoint}
**Method**: {method}
**Code**:
\`\`\`{language}
{code}
\`\`\`

Please include:
- Endpoint description and purpose
- Request/response schemas
- Parameter descriptions
- Example requests and responses
- Error codes and handling
- Authentication requirements
- Rate limiting information

Format the documentation in a clear, professional manner suitable for developers.`,
        category: 'Documentation',
        tags: ['api', 'documentation', 'swagger'],
        author: 'LEKA AI',
        usage: 892,
        rating: 4.6,
        starred: false,
        createdAt: '2024-01-10',
        variables: ['endpoint', 'method', 'language', 'code']
    },
    {
        id: '3',
        name: 'Bug Debugging Assistant',
        description: 'Help identify and fix bugs in code',
        prompt: `I'm encountering a bug in my code. Please help me debug it:

**Error Message**: {error}
**Code**:
\`\`\`{language}
{code}
\`\`\`

**Expected Behavior**: {expected}
**Actual Behavior**: {actual}

Please:
1. Identify the root cause of the issue
2. Explain why this error is occurring
3. Provide a corrected version of the code
4. Suggest preventive measures to avoid similar issues
5. Recommend debugging techniques for this type of problem`,
        category: 'Development',
        tags: ['debugging', 'troubleshooting', 'error-fixing'],
        author: 'LEKA AI',
        usage: 1156,
        rating: 4.9,
        starred: true,
        createdAt: '2024-01-08',
        variables: ['error', 'language', 'code', 'expected', 'actual']
    },
    {
        id: '4',
        name: 'Technical Concept Explainer',
        description: 'Explain complex technical concepts in simple terms',
        prompt: `Please explain the concept of "{concept}" in a clear, easy-to-understand way:

1. **Simple Definition**: What is {concept} in plain English?
2. **How It Works**: Break down the mechanism or process
3. **Real-World Analogy**: Use a relatable comparison
4. **Practical Examples**: Show concrete use cases
5. **Benefits & Drawbacks**: Pros and cons
6. **When to Use**: Appropriate scenarios and contexts

Target audience: {audience}
Technical level: {level}

Use examples, diagrams (in text), and step-by-step explanations where helpful.`,
        category: 'Education',
        tags: ['explanation', 'learning', 'concepts'],
        author: 'LEKA AI',
        usage: 743,
        rating: 4.7,
        starred: false,
        createdAt: '2024-01-05',
        variables: ['concept', 'audience', 'level']
    },
    {
        id: '5',
        name: 'Test Case Generator',
        description: 'Generate comprehensive test cases for functions',
        prompt: `Generate comprehensive test cases for the following function:

\`\`\`{language}
{code}
\`\`\`

Please create test cases covering:

1. **Happy Path**: Normal, expected inputs and outputs
2. **Edge Cases**: Boundary conditions and limits
3. **Error Cases**: Invalid inputs and error handling
4. **Performance Tests**: Large inputs or stress conditions
5. **Integration Tests**: How it works with other components

For each test case, provide:
- Test description
- Input values
- Expected output
- Test rationale

Format the tests in {test_framework} syntax if specified, otherwise use pseudocode.`,
        category: 'Testing',
        tags: ['testing', 'unit-tests', 'qa'],
        author: 'LEKA AI',
        usage: 634,
        rating: 4.5,
        starred: false,
        createdAt: '2024-01-03',
        variables: ['language', 'code', 'test_framework']
    }
]

interface PromptLibraryProps {
    onPromptSelect: (prompt: string) => void
}

export function PromptLibrary({ onPromptSelect }: PromptLibraryProps) {
    const [searchQuery, setSearchQuery] = useState('')
    const [selectedCategory, setSelectedCategory] = useState('all')
    const [showFavorites, setShowFavorites] = useState(false)
    const [selectedPrompt, setSelectedPrompt] = useState<PromptTemplate | null>(null)

    const categories = ['all', ...Array.from(new Set(promptTemplates.map(p => p.category)))]

    const filteredPrompts = promptTemplates.filter(prompt => {
        const matchesSearch = prompt.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            prompt.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
            prompt.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()))

        const matchesCategory = selectedCategory === 'all' || prompt.category === selectedCategory
        const matchesFavorites = !showFavorites || prompt.starred

        return matchesSearch && matchesCategory && matchesFavorites
    })

    const getCategoryIcon = (category: string) => {
        switch (category) {
            case 'Development': return <Code size={16} />
            case 'Documentation': return <FileText size={16} />
            case 'Testing': return <Zap size={16} />
            case 'Education': return <Brain size={16} />
            default: return <MessageSquare size={16} />
        }
    }

    const usePrompt = (prompt: PromptTemplate) => {
        onPromptSelect(prompt.prompt)
    }

    const toggleStar = (id: string, e: React.MouseEvent) => {
        e.stopPropagation()
        // TODO: Implement star toggle
    }

    return (
        <div className="h-100 d-flex flex-column">
            {/* Header */}
            <div className="p-3 border-bottom">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h6 className="mb-0 fw-bold">Prompt Library</h6>
                    <Button variant="primary" size="sm">
                        <Plus size={16} className="me-1" />
                        New Prompt
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
                        placeholder="Search prompts..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>

                {/* Filters */}
                <div className="d-flex gap-1 mb-2">
                    <select
                        className="form-select form-select-sm"
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                    >
                        {categories.map(cat => (
                            <option key={cat} value={cat}>
                                {cat === 'all' ? 'All Categories' : cat}
                            </option>
                        ))}
                    </select>

                    <Button
                        variant={showFavorites ? "primary" : "outline"}
                        size="sm"
                        onClick={() => setShowFavorites(!showFavorites)}
                    >
                        <Star size={14} fill={showFavorites ? 'currentColor' : 'none'} />
                    </Button>
                </div>
            </div>

            {/* Prompts List */}
            <div className="flex-fill overflow-auto">
                {filteredPrompts.length === 0 ? (
                    <div className="p-4 text-center text-muted">
                        <Lightbulb size={32} className="mb-2 opacity-50" />
                        <p className="small mb-0">No prompts found</p>
                    </div>
                ) : (
                    <div className="p-2">
                        {filteredPrompts.map(prompt => (
                            <div
                                key={prompt.id}
                                className="mb-2 cursor-pointer"
                                onClick={() => setSelectedPrompt(prompt)}
                            >
                            <Card className="hover-lift">
                                <CardBody className="p-3">
                                    <div className="d-flex justify-content-between align-items-start mb-2">
                                        <div className="flex-fill">
                                            <div className="d-flex align-items-center gap-2 mb-1">
                                                {getCategoryIcon(prompt.category)}
                                                <h6 className="mb-0 small fw-medium">{prompt.name}</h6>
                                                {prompt.starred && (
                                                    <Star size={14} className="text-warning" fill="currentColor" />
                                                )}
                                            </div>
                                            <p className="text-muted small mb-2">{prompt.description}</p>
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
                                                <li><button className="dropdown-item small" onClick={() => usePrompt(prompt)}>
                                                    <Play size={12} className="me-2" />Use Prompt
                                                </button></li>
                                                <li><button className="dropdown-item small">
                                                    <Copy size={12} className="me-2" />Copy
                                                </button></li>
                                                <li><button className="dropdown-item small">
                                                    <Edit size={12} className="me-2" />Edit
                                                </button></li>
                                                <li><hr className="dropdown-divider" /></li>
                                                <li><button className="dropdown-item small text-danger">
                                                    <Trash2 size={12} className="me-2" />Delete
                                                </button></li>
                                            </ul>
                                        </div>
                                    </div>

                                    {/* Tags */}
                                    <div className="d-flex flex-wrap gap-1 mb-2">
                                        {prompt.tags.slice(0, 3).map(tag => (
                                            <Badge key={tag} variant="light" className="border text-xs">
                                                {tag}
                                            </Badge>
                                        ))}
                                        {prompt.tags.length > 3 && (
                                            <Badge variant="light" className="border text-xs">
                                                +{prompt.tags.length - 3}
                                            </Badge>
                                        )}
                                    </div>

                                    {/* Stats */}
                                    <div className="d-flex justify-content-between align-items-center small text-muted">
                                        <div className="d-flex align-items-center gap-3">
                      <span>
                        <Eye size={10} className="me-1" />
                          {prompt.usage}
                      </span>
                                            <span>
                        <Star size={10} className="me-1" />
                                                {prompt.rating}
                      </span>
                                        </div>
                                        <span>{prompt.createdAt}</span>
                                    </div>

                                    {/* Action Buttons */}
                                    <div className="d-flex gap-2 mt-2">
                                        <Button
                                            variant="primary"
                                            size="sm"
                                            className="flex-fill"
                                            onClick={(e) => {
                                                e.stopPropagation()
                                                usePrompt(prompt)
                                            }}
                                        >
                                            Use Prompt
                                        </Button>
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={(e) => toggleStar(prompt.id, e)}
                                        >
                                            <Star size={14} fill={prompt.starred ? 'currentColor' : 'none'} />
                                        </Button>
                                    </div>
                                </CardBody>
                            </Card>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Prompt Detail Modal */}
            {selectedPrompt && (
                <div className="modal fade show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog modal-lg">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{selectedPrompt.name}</h5>
                                <button
                                    type="button"
                                    className="btn-close"
                                    onClick={() => setSelectedPrompt(null)}
                                ></button>
                            </div>
                            <div className="modal-body">
                                <p className="text-muted mb-3">{selectedPrompt.description}</p>

                                {selectedPrompt.variables && selectedPrompt.variables.length > 0 && (
                                    <div className="mb-3">
                                        <h6 className="small fw-bold">Variables:</h6>
                                        <div className="d-flex flex-wrap gap-1">
                                            {selectedPrompt.variables.map(variable => (
                                                <Badge key={variable} variant="info" className="small">
                                                    {`{${variable}}`}
                                                </Badge>
                                            ))}
                                        </div>
                                    </div>
                                )}

                                <div className="mb-3">
                                    <h6 className="small fw-bold">Prompt:</h6>
                                    <pre className="bg-light p-3 rounded small" style={{ whiteSpace: 'pre-wrap' }}>
                    {selectedPrompt.prompt}
                  </pre>
                                </div>

                                <div className="row g-3 small">
                                    <div className="col-md-6">
                                        <strong>Category:</strong> {selectedPrompt.category}
                                    </div>
                                    <div className="col-md-6">
                                        <strong>Author:</strong> {selectedPrompt.author}
                                    </div>
                                    <div className="col-md-6">
                                        <strong>Usage:</strong> {selectedPrompt.usage} times
                                    </div>
                                    <div className="col-md-6">
                                        <strong>Rating:</strong> {selectedPrompt.rating}/5
                                    </div>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <Button variant="outline" onClick={() => setSelectedPrompt(null)}>
                                    Close
                                </Button>
                                <Button variant="primary" onClick={() => {
                                    usePrompt(selectedPrompt)
                                    setSelectedPrompt(null)
                                }}>
                                    Use This Prompt
                                </Button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
