// components/AnimatedChat.tsx
'use client'

import { useState, useEffect } from 'react'
import { Bot, User, Zap, Code, Database, Globe } from 'lucide-react'

interface ChatMessage {
    id: string
    type: 'user' | 'assistant'
    content: string
    timestamp: Date
    isTyping?: boolean
    codeBlock?: {
        language: string
        code: string
    }
}

const chatScript: Omit<ChatMessage, 'id' | 'timestamp'>[] = [
    {
        type: 'user',
        content: 'I need to create a hospital management system with patient records and appointments'
    },
    {
        type: 'assistant',
        content: 'Perfect! I\'ll help you create a comprehensive hospital management system. Let me generate the architecture...',
        isTyping: true
    },
    {
        type: 'assistant',
        content: 'I\'ll create a modern healthcare system with:\n\n• Patient Management Module\n• Appointment Scheduling\n• Medical Records\n• Billing Integration\n• HIPAA Compliance\n\nGenerating the backend API...'
    },
    {
        type: 'assistant',
        content: 'Here\'s your Patient entity:',
        codeBlock: {
            language: 'java',
            code: `@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true)
    private String patientId;
    
    // Additional fields...
}`
        }
    },
    {
        type: 'user',
        content: 'Great! Can you also add the frontend components?'
    },
    {
        type: 'assistant',
        content: 'Absolutely! Creating React components with TypeScript...',
        isTyping: true
    },
    {
        type: 'assistant',
        content: 'Frontend components generated! Your hospital system includes:\n\n✅ Complete backend API\n✅ React frontend\n✅ Database schema\n✅ Authentication\n✅ Docker configuration\n\nReady to download and deploy! 🚀'
    }
]

export function AnimatedChat() {
    const [messages, setMessages] = useState<ChatMessage[]>([])
    const [currentMessageIndex, setCurrentMessageIndex] = useState(0)
    const [currentText, setCurrentText] = useState('')
    const [isTyping, setIsTyping] = useState(false)
    const [showCursor, setShowCursor] = useState(true)

    // Cursor blinking effect
    useEffect(() => {
        const cursorInterval = setInterval(() => {
            setShowCursor(prev => !prev)
        }, 500)
        return () => clearInterval(cursorInterval)
    }, [])

    // Main animation logic
    useEffect(() => {
        if (currentMessageIndex >= chatScript.length) {
            // Reset animation after completion
            setTimeout(() => {
                setMessages([])
                setCurrentMessageIndex(0)
                setCurrentText('')
            }, 5000)
            return
        }

        const currentScriptMessage = chatScript[currentMessageIndex]

        // Add user messages instantly
        if (currentScriptMessage.type === 'user') {
            const newMessage: ChatMessage = {
                id: `msg-${Date.now()}`,
                ...currentScriptMessage,
                timestamp: new Date()
            }
            setMessages(prev => [...prev, newMessage])

            // Move to next message after a pause
            setTimeout(() => {
                setCurrentMessageIndex(prev => prev + 1)
            }, 1500)
            return
        }

        // Handle assistant messages with typing effect
        if (currentScriptMessage.type === 'assistant') {
            // Show typing indicator first if message has isTyping
            if (currentScriptMessage.isTyping) {
                const typingMessage: ChatMessage = {
                    id: `typing-${Date.now()}`,
                    type: 'assistant',
                    content: '',
                    timestamp: new Date(),
                    isTyping: true
                }
                setMessages(prev => [...prev, typingMessage])
                setIsTyping(true)

                // Remove typing indicator and start actual message
                setTimeout(() => {
                    setMessages(prev => prev.filter(m => !m.isTyping))
                    setIsTyping(false)
                    startTypingMessage(currentScriptMessage)
                }, 2000)
            } else {
                startTypingMessage(currentScriptMessage)
            }
        }
    }, [currentMessageIndex])

    const startTypingMessage = (scriptMessage: Omit<ChatMessage, 'id' | 'timestamp'>) => {
        setCurrentText('')
        const fullText = scriptMessage.content
        let charIndex = 0

        const typingInterval = setInterval(() => {
            if (charIndex < fullText.length) {
                setCurrentText(fullText.substring(0, charIndex + 1))
                charIndex++
            } else {
                clearInterval(typingInterval)

                // Add complete message
                const completeMessage: ChatMessage = {
                    id: `msg-${Date.now()}`,
                    ...scriptMessage,
                    timestamp: new Date()
                }
                setMessages(prev => [...prev, completeMessage])
                setCurrentText('')

                // Move to next message
                setTimeout(() => {
                    setCurrentMessageIndex(prev => prev + 1)
                }, 1000)
            }
        }, 30) // Typing speed
    }

    return (
        <div className="position-relative">
            <div className="card border-0 shadow-lg" style={{ height: '500px' }}>
                <div className="card-header text-white d-flex align-items-center"
                     style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
                    <div className="bg-white bg-opacity-20 rounded-circle p-2 me-3">
                        <Bot size={20} />
                    </div>
                    <div>
                        <h6 className="mb-0">LEKA AI Assistant</h6>
                        <small className="opacity-75">Enterprise Code Generator</small>
                    </div>
                    <div className="ms-auto">
            <span className="badge bg-success bg-opacity-20 text-white border-0">
              <div className="bg-success rounded-circle me-2 d-inline-block" style={{width: '8px', height: '8px'}}></div>
              Online
            </span>
                    </div>
                </div>

                <div className="card-body p-0 d-flex flex-column" style={{ height: '400px' }}>
                    <div className="flex-grow-1 overflow-auto p-3" style={{ maxHeight: '350px' }}>
                        {messages.map((message) => (
                            <div key={message.id} className={`d-flex mb-3 ${message.type === 'user' ? 'justify-content-end' : ''}`}>
                                <div className={`d-flex ${message.type === 'user' ? 'flex-row-reverse' : ''}`} style={{ maxWidth: '85%' }}>
                                    {/* Avatar */}
                                    <div className={`flex-shrink-0 ${message.type === 'user' ? 'ms-2' : 'me-2'}`}>
                                        <div className={`rounded-circle d-flex align-items-center justify-content-center ${
                                            message.type === 'user'
                                                ? 'bg-primary text-white'
                                                : 'bg-light text-primary'
                                        }`} style={{ width: '32px', height: '32px' }}>
                                            {message.type === 'user' ? <User size={16} /> : <Bot size={16} />}
                                        </div>
                                    </div>

                                    {/* Message Content */}
                                    <div className="flex-grow-1">
                                        {message.isTyping ? (
                                            <div className="bg-light rounded-3 p-3">
                                                <div className="d-flex align-items-center">
                                                    <div className="typing-indicator me-2">
                                                        <span></span>
                                                        <span></span>
                                                        <span></span>
                                                    </div>
                                                    <small className="text-muted">LEKA AI is thinking...</small>
                                                </div>
                                            </div>
                                        ) : (
                                            <div className={`rounded-3 p-3 ${
                                                message.type === 'user'
                                                    ? 'bg-primary text-white'
                                                    : 'bg-light'
                                            }`}>
                                                <div className="small" style={{ whiteSpace: 'pre-line' }}>
                                                    {message.content}
                                                </div>

                                                {/* Code Block */}
                                                {message.codeBlock && (
                                                    <div className="mt-3">
                                                        <div className="bg-dark rounded-3 overflow-hidden">
                                                            <div className="bg-secondary bg-opacity-25 px-3 py-2 d-flex align-items-center justify-content-between">
                                                                <div className="d-flex align-items-center">
                                                                    <Code size={14} className="me-2" />
                                                                    <small className="text-light">{message.codeBlock.language}</small>
                                                                </div>
                                                                <button className="btn btn-outline-light btn-sm py-0 px-2">
                                                                    <small>Copy</small>
                                                                </button>
                                                            </div>
                                                            <pre className="p-3 mb-0 text-light small" style={{ fontSize: '0.75rem' }}>
                                <code>{message.codeBlock.code}</code>
                              </pre>
                                                        </div>
                                                    </div>
                                                )}
                                            </div>
                                        )}
                                        <div className="text-muted small mt-1" style={{ fontSize: '0.7rem' }}>
                                            {message.timestamp.toLocaleTimeString()}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}

                        {/* Current typing message */}
                        {currentText && (
                            <div className="d-flex mb-3">
                                <div className="d-flex" style={{ maxWidth: '85%' }}>
                                    <div className="flex-shrink-0 me-2">
                                        <div className="bg-light text-primary rounded-circle d-flex align-items-center justify-content-center"
                                             style={{ width: '32px', height: '32px' }}>
                                            <Bot size={16} />
                                        </div>
                                    </div>
                                    <div className="flex-grow-1">
                                        <div className="bg-light rounded-3 p-3">
                                            <div className="small" style={{ whiteSpace: 'pre-line' }}>
                                                {currentText}
                                                {showCursor && <span className="text-primary">|</span>}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Input Area */}
                    <div className="border-top p-3">
                        <div className="input-group">
                            <input
                                type="text"
                                className="form-control border-0 bg-light"
                                placeholder="Ask me to generate any enterprise application..."
                                disabled
                            />
                            <button className="btn btn-primary border-0">
                                <Zap size={16} />
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Floating Action Buttons */}
            <div className="position-absolute bottom-0 end-0 p-3">
                <div className="d-flex flex-column gap-2">
                    <button className="btn btn-primary btn-sm rounded-circle shadow d-flex align-items-center justify-content-center"
                            style={{ width: '40px', height: '40px' }}>
                        <Globe size={16} />
                    </button>
                    <button className="btn btn-primary btn-sm rounded-circle shadow d-flex align-items-center justify-content-center"
                            style={{ width: '40px', height: '40px' }}>
                        <Database size={16} />
                    </button>
                    <button className="btn btn-primary btn-sm rounded-circle shadow d-flex align-items-center justify-content-center"
                            style={{ width: '40px', height: '40px' }}>
                        <Code size={16} />
                    </button>
                </div>
            </div>

            <style jsx>{`
        .typing-indicator {
          display: flex;
          align-items: center;
        }
        
        .typing-indicator span {
          height: 8px;
          width: 8px;
          background-color: #6c757d;
          border-radius: 50%;
          display: inline-block;
          margin-right: 4px;
          animation: typing 1.4s infinite ease-in-out;
        }
        
        .typing-indicator span:nth-child(1) {
          animation-delay: -0.32s;
        }
        
        .typing-indicator span:nth-child(2) {
          animation-delay: -0.16s;
        }
        
        @keyframes typing {
          0%, 80%, 100% {
            transform: scale(0.8);
            opacity: 0.5;
          }
          40% {
            transform: scale(1);
            opacity: 1;
          }
        }
      `}</style>
        </div>
    )
}