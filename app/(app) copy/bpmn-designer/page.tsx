// app/(app)/bpmn-designer/page.tsx
'use client'

import { Button } from '@/components/ui/button'
import {
  ArrowLeft,
  ArrowRight,
  Circle,
  Code,
  Diamond,
  Download,
  GitBranch,
  Mail,
  MousePointer,
  Play,
  PlayCircle,
  Plus,
  Redo,
  Save,
  Send,
  Server,
  Settings,
  Square,
  StopCircle,
  Target,
  Timer,
  Trash2,
  Undo,
  User,
  Workflow,
  XCircle,
  ZoomIn,
  ZoomOut,
  Loader2
} from 'lucide-react'
import Link from 'next/link'
import { useRef, useState, useEffect } from 'react'
import { useSearchParams } from 'next/navigation'
import { BPMNElement, BPMNConnection, BPMNProcessInfo } from '@/types/bpmn-designer'
import { parseBPMNXML } from './utils/bpmnParser'
import { generateBPMNXML } from './utils/bpmnGenerator'

const bpmnElements = [
  {
    id: 'start-event',
    type: 'start',
    name: 'Start Event',
    icon: <Circle size={16} />,
    category: 'Events',
    description: 'Indicates where a process starts',
    defaultSize: { width: 36, height: 36 }
  },
  {
    id: 'start-timer',
    type: 'start',
    subtype: 'timer',
    name: 'Timer Start',
    icon: <Timer size={16} />,
    category: 'Events',
    description: 'Process starts at a specific time',
    defaultSize: { width: 36, height: 36 }
  },
  {
    id: 'start-message',
    type: 'start',
    subtype: 'message',
    name: 'Message Start',
    icon: <Mail size={16} />,
    category: 'Events',
    description: 'Process starts when a message is received',
    defaultSize: { width: 36, height: 36 }
  },
  {
    id: 'end-event',
    type: 'end',
    name: 'End Event',
    icon: <Target size={16} />,
    category: 'Events',
    description: 'Indicates where a process ends',
    defaultSize: { width: 36, height: 36 }
  },
  {
    id: 'end-message',
    type: 'end',
    subtype: 'message',
    name: 'Message End',
    icon: <Send size={16} />,
    category: 'Events',
    description: 'Process ends by sending a message',
    defaultSize: { width: 36, height: 36 }
  },
  {
    id: 'user-task',
    type: 'task',
    subtype: 'user',
    name: 'User Task',
    icon: <User size={16} />,
    category: 'Tasks',
    description: 'Task performed by a human user',
    defaultSize: { width: 100, height: 80 }
  },
  {
    id: 'service-task',
    type: 'task',
    subtype: 'service',
    name: 'Service Task',
    icon: <Server size={16} />,
    category: 'Tasks',
    description: 'Automated task performed by a system',
    defaultSize: { width: 100, height: 80 }
  },
  {
    id: 'script-task',
    type: 'task',
    subtype: 'script',
    name: 'Script Task',
    icon: <Code size={16} />,
    category: 'Tasks',
    description: 'Task that executes a script',
    defaultSize: { width: 100, height: 80 }
  },
  {
    id: 'exclusive-gateway',
    type: 'gateway',
    subtype: 'exclusive',
    name: 'Exclusive Gateway',
    icon: <Diamond size={16} />,
    category: 'Gateways',
    description: 'Splits or merges flow based on conditions',
    defaultSize: { width: 50, height: 50 }
  },
  {
    id: 'parallel-gateway',
    type: 'gateway',
    subtype: 'parallel',
    name: 'Parallel Gateway',
    icon: <GitBranch size={16} />,
    category: 'Gateways',
    description: 'Splits flow into parallel paths',
    defaultSize: { width: 50, height: 50 }
  }
]

export default function BPMNDesignerPage() {
  const searchParams = useSearchParams()
  const canvasRef = useRef<HTMLDivElement>(null)
  const [elements, setElements] = useState<BPMNElement[]>([])
  const [connections, setConnections] = useState<BPMNConnection[]>([])
  const [selectedElement, setSelectedElement] = useState<BPMNElement | null>(null)
  const [tool, setTool] = useState('select')
  const [zoom, setZoom] = useState(100)
  const [showGrid, setShowGrid] = useState(true)
  const [isConnecting, setIsConnecting] = useState(false)
  const [connectionStart, setConnectionStart] = useState<string | null>(null)
  const [processName, setProcessName] = useState('New Process')
  const [availableProcesses, setAvailableProcesses] = useState<BPMNProcessInfo[]>([])
  const [selectedProcessId, setSelectedProcessId] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [containerHeight, setContainerHeight] = useState(0)

  // Calcular altura disponible dinámicamente
  useEffect(() => {
    const calculateHeight = () => {
      // Obtener altura del viewport completo
      const viewportHeight = window.innerHeight
      // Buscar el main container para obtener su posición
      const mainElement = document.querySelector('main')
      const footerElement = document.querySelector('footer')

      if (mainElement) {
        const mainRect = mainElement.getBoundingClientRect()
        let availableHeight = viewportHeight - mainRect.top

        // Si hay footer, restar su altura
        if (footerElement) {
          const footerRect = footerElement.getBoundingClientRect()
          availableHeight = footerRect.top - mainRect.top
        }

        // Restar el padding del main (py-4 = 16px arriba y abajo = 32px total)
        availableHeight = availableHeight - 32

        setContainerHeight(Math.max(availableHeight, 600)) // Mínimo 600px
      } else {
        // Fallback: usar viewport menos estimación
        setContainerHeight(viewportHeight - 180)
      }
    }

    // Calcular inmediatamente y después de delays para asegurar que el DOM esté listo
    calculateHeight()
    setTimeout(calculateHeight, 100)
    setTimeout(calculateHeight, 500)

    window.addEventListener('resize', calculateHeight)
    return () => window.removeEventListener('resize', calculateHeight)
  }, [])

  const loadProcessList = async () => {
    try {
      const response = await fetch('/api/bpmn/processes')
      if (response.ok) {
        const data = await response.json()
        setAvailableProcesses(data)
      }
    } catch (err) {
      console.error('Error loading process list:', err)
    }
  }

  // Cargar lista de procesos al montar
  useEffect(() => {
    loadProcessList()
  }, [])

  // Cargar proceso desde URL si existe
  useEffect(() => {
    const processParam = searchParams.get('process')
    if (processParam) {
      loadProcess(processParam)
    }
  }, [searchParams])


  // Cargar proceso seleccionado
  const loadProcess = async (processId: string) => {
    setLoading(true)
    setError(null)
    try {
      // Determinar si es un proceso temporal
      const isTemporary = processId.startsWith('temp_')

      // En modo demo, verificar localStorage primero para procesos temporales
      if (isTemporary) {
        try {
          const tempProcesses = JSON.parse(localStorage.getItem('bpmn_temp_processes') || '{}')
          if (tempProcesses[processId]) {
            const xmlString = tempProcesses[processId].xml
            const process = await parseBPMNXML(xmlString)
            setElements(process.elements)
            setConnections(process.connections)
            setProcessName(process.name)
            setSelectedProcessId(processId)
            setLoading(false)
            return
          }
        } catch (localError) {
          console.log('No se encontró en localStorage, intentando con API...')
        }
      }

      // Intentar cargar desde API local primero (modo demo)
      let endpoint = isTemporary
        ? `/api/bpmn/processes/temporary/${encodeURIComponent(processId)}`
        : `/api/bpmn/processes/local/${encodeURIComponent(processId)}`

      let response = await fetch(endpoint)

      // Si falla la API local, intentar con el backend
      if (!response.ok && !isTemporary) {
        endpoint = `/api/bpmn/processes/${encodeURIComponent(processId)}`
        response = await fetch(endpoint)
      }

      // Si es temporal y falló localStorage, intentar con backend
      if (!response.ok && isTemporary) {
        endpoint = `/api/bpmn/processes/temporary/${encodeURIComponent(processId)}`
        response = await fetch(endpoint)
      }

      if (!response.ok) {
        throw new Error('Failed to load process')
      }

      const xmlString = await response.text()

      // Parsear XML a modelo interno
      const process = await parseBPMNXML(xmlString)

      // Actualizar estado del diseñador
      setElements(process.elements)
      setConnections(process.connections)
      setProcessName(process.name)
      setSelectedProcessId(processId)
    } catch (err) {
      console.error('Error loading process:', err)
      setError(err instanceof Error ? err.message : 'Error al cargar el proceso')
      alert('Error al cargar el proceso')
    } finally {
      setLoading(false)
    }
  }

  const handleDragStart = (e: React.DragEvent, elementType: any) => {
    e.dataTransfer.setData('bpmn-element', JSON.stringify(elementType))
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    const elementData = e.dataTransfer.getData('bpmn-element')
    if (elementData && canvasRef.current) {
      const elementType = JSON.parse(elementData)
      const rect = canvasRef.current.getBoundingClientRect()
      const x = (e.clientX - rect.left) / (zoom / 100)
      const y = (e.clientY - rect.top) / (zoom / 100)

      const newElement: BPMNElement = {
        id: `${elementType.id}_${Date.now()}`,
        type: elementType.type,
        subtype: elementType.subtype,
        name: elementType.name,
        description: elementType.description,
        x: x - elementType.defaultSize.width / 2,
        y: y - elementType.defaultSize.height / 2,
        width: elementType.defaultSize.width,
        height: elementType.defaultSize.height,
        properties: {
          assignee: '',
          dueDate: '',
          priority: 50,
          formKey: '',
          documentation: ''
        },
        connections: {
          incoming: [],
          outgoing: []
        }
      }

      setElements([...elements, newElement])
    }
  }

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
  }

  const handleElementClick = (element: BPMNElement) => {
    if (tool === 'select') {
      setSelectedElement(element)
    } else if (tool === 'connect') {
      if (!isConnecting) {
        setIsConnecting(true)
        setConnectionStart(element.id)
      } else if (connectionStart && connectionStart !== element.id) {
        // Create connection
        const newConnection: BPMNConnection = {
          id: `connection_${Date.now()}`,
          type: 'sequence',
          source: connectionStart,
          target: element.id,
          properties: {}
        }
        setConnections([...connections, newConnection])

        // Update element connections
        setElements(elements.map(el => {
          if (el.id === connectionStart) {
            return { ...el, connections: { ...el.connections, outgoing: [...el.connections.outgoing, newConnection.id] } }
          }
          if (el.id === element.id) {
            return { ...el, connections: { ...el.connections, incoming: [...el.connections.incoming, newConnection.id] } }
          }
          return el
        }))

        setIsConnecting(false)
        setConnectionStart(null)
      }
    }
  }

  const deleteSelectedElement = () => {
    if (selectedElement) {
      // Remove connections related to this element
      const updatedConnections = connections.filter(conn =>
        conn.source !== selectedElement.id && conn.target !== selectedElement.id
      )
      setConnections(updatedConnections)

      // Remove element
      setElements(elements.filter(el => el.id !== selectedElement.id))
      setSelectedElement(null)
    }
  }

  const handleExport = () => {
    const process = {
      id: selectedProcessId || 'new-process',
      name: processName,
      elements,
      connections,
      namespace: 'http://www.activiti.org/test',
      isExecutable: true,
    }

    const xml = generateBPMNXML(process)

    // Descargar como archivo
    const blob = new Blob([xml], { type: 'application/xml' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `${process.id}.bpmn20.xml`
    a.click()
    URL.revokeObjectURL(url)
  }

  const handleSave = async () => {
    if (!selectedProcessId) {
      alert('Debe cargar un proceso existente para guardar')
      return
    }

    setLoading(true)
    setError(null)
    try {
      const process = {
        id: selectedProcessId,
        name: processName,
        elements,
        connections,
        namespace: 'http://www.activiti.org/test',
        isExecutable: true,
      }

      const xml = generateBPMNXML(process)

      // Validar antes de guardar
      const validationResponse = await fetch('/api/bpmn/processes/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ xml }),
      })

      const validation = await validationResponse.json()

      if (!validation.valid) {
        const errorMsg = `Errores de validación:\n${validation.errors?.join('\n') || 'Error desconocido'}`
        setError(errorMsg)
        alert(errorMsg)
        return
      }

      // Determinar si es un proceso temporal
      const isTemporary = selectedProcessId.startsWith('temp_')

      // En modo demo, guardar procesos temporales en localStorage
      if (isTemporary) {
        try {
          const tempProcesses = JSON.parse(localStorage.getItem('bpmn_temp_processes') || '{}')
          if (tempProcesses[selectedProcessId]) {
            tempProcesses[selectedProcessId].xml = xml
            tempProcesses[selectedProcessId].name = processName
            tempProcesses[selectedProcessId].timestamp = Date.now()
            localStorage.setItem('bpmn_temp_processes', JSON.stringify(tempProcesses))
            alert('Proceso temporal guardado exitosamente (modo demo)')
            setError(null)
            setLoading(false)
            return
          }
        } catch (localError) {
          console.log('Error guardando en localStorage, intentando con API...')
        }
      }

      const endpoint = isTemporary
        ? `/api/bpmn/processes/temporary/${encodeURIComponent(selectedProcessId)}`
        : `/api/bpmn/processes/${encodeURIComponent(selectedProcessId)}`

      // Guardar proceso
      const saveResponse = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ xml }),
      })

      const result = await saveResponse.json()

      if (result.success) {
        alert(isTemporary ? 'Proceso temporal guardado exitosamente' : 'Proceso guardado exitosamente')
        setError(null)
      } else {
        const errorMsg = `Error al guardar: ${result.errors?.join('\n') || 'Error desconocido'}`
        setError(errorMsg)
        alert(errorMsg)
      }
    } catch (err) {
      console.error('Error saving process:', err)
      const errorMsg = err instanceof Error ? err.message : 'Error al guardar el proceso'
      setError(errorMsg)
      alert(errorMsg)
    } finally {
      setLoading(false)
    }
  }

  const renderBPMNElement = (element: BPMNElement) => {
    const isSelected = selectedElement?.id === element.id
    const isConnectionStart = connectionStart === element.id

    let elementStyle = {
      position: 'absolute' as const,
      left: element.x,
      top: element.y,
      width: element.width,
      height: element.height,
      cursor: tool === 'select' ? 'pointer' : tool === 'connect' ? 'crosshair' : 'default'
    }

    const commonClasses = `hover-shadow cursor-pointer`

    const borderColor = isSelected
      ? '#3b82f6'
      : isConnectionStart
        ? '#10b981'
        : '#d1d5db'

    switch (element.type) {
      case 'start':
        return (
          <div
            key={element.id}
            style={{
              ...elementStyle,
              borderColor,
              borderWidth: isSelected || isConnectionStart ? '2px' : '1px',
              backgroundColor: '#d1fae5',
              borderStyle: 'solid'
            }}
            onClick={() => handleElementClick(element)}
            className={`rounded-circle d-flex align-items-center justify-content-center ${commonClasses}`}
          >
            {element.subtype === 'timer' && <Timer size={16} style={{ color: '#059669' }} />}
            {element.subtype === 'message' && <Mail size={16} style={{ color: '#059669' }} />}
            {!element.subtype && <PlayCircle size={16} style={{ color: '#059669' }} />}
          </div>
        )

      case 'end':
        return (
          <div
            key={element.id}
            style={{
              ...elementStyle,
              borderColor,
              borderWidth: isSelected || isConnectionStart ? '2px' : '4px',
              backgroundColor: '#fee2e2',
              borderStyle: 'solid'
            }}
            onClick={() => handleElementClick(element)}
            className={`rounded-circle d-flex align-items-center justify-content-center ${commonClasses}`}
          >
            {element.subtype === 'message' && <Send size={16} style={{ color: '#dc2626' }} />}
            {!element.subtype && <StopCircle size={16} style={{ color: '#dc2626' }} />}
          </div>
        )

      case 'task':
        return (
          <div
            key={element.id}
            style={{
              ...elementStyle,
              borderColor,
              borderWidth: isSelected || isConnectionStart ? '2px' : '1px',
              backgroundColor: '#dbeafe',
              borderStyle: 'solid'
            }}
            onClick={() => handleElementClick(element)}
            className={`rounded p-2 ${commonClasses}`}
          >
            <div className="d-flex align-items-center mb-1">
              {element.subtype === 'user' && <User size={16} style={{ color: '#2563eb' }} />}
              {element.subtype === 'service' && <Server size={16} style={{ color: '#2563eb' }} />}
              {element.subtype === 'script' && <Code size={16} style={{ color: '#2563eb' }} />}
              {!element.subtype && <Square size={16} style={{ color: '#2563eb' }} />}
            </div>
            <div className="small fw-medium text-center" style={{ color: '#2563eb' }}>
              {element.name}
            </div>
          </div>
        )

      case 'gateway':
        return (
          <div
            key={element.id}
            style={{
              ...elementStyle,
              borderColor,
              borderWidth: isSelected || isConnectionStart ? '2px' : '1px',
              backgroundColor: '#fef3c7',
              borderStyle: 'solid',
              transform: 'rotate(45deg)'
            }}
            onClick={() => handleElementClick(element)}
            className={`d-flex align-items-center justify-content-center ${commonClasses}`}
          >
            <div style={{ transform: 'rotate(-45deg)' }}>
              {element.subtype === 'exclusive' && <XCircle size={16} style={{ color: '#d97706' }} />}
              {element.subtype === 'parallel' && <Plus size={16} style={{ color: '#d97706' }} />}
              {element.subtype === 'inclusive' && <Circle size={16} style={{ color: '#d97706' }} />}
            </div>
          </div>
        )

      default:
        return (
          <div
            key={element.id}
            style={{
              ...elementStyle,
              borderColor,
              borderWidth: isSelected || isConnectionStart ? '2px' : '1px',
              backgroundColor: '#f9fafb',
              borderStyle: 'solid'
            }}
            onClick={() => handleElementClick(element)}
            className={`rounded p-2 d-flex align-items-center justify-content-center ${commonClasses}`}
          >
            <span className="small" style={{ color: '#6b7280' }}>{element.name}</span>
          </div>
        )
    }
  }

  const renderConnection = (connection: BPMNConnection) => {
    const sourceElement = elements.find(el => el.id === connection.source)
    const targetElement = elements.find(el => el.id === connection.target)

    if (!sourceElement || !targetElement) return null

    const startX = sourceElement.x + sourceElement.width / 2
    const startY = sourceElement.y + sourceElement.height / 2
    const endX = targetElement.x + targetElement.width / 2
    const endY = targetElement.y + targetElement.height / 2

    return (
      <svg
        key={connection.id}
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '1200px',
          height: '800px',
          pointerEvents: 'none',
          zIndex: 2
        }}
      >
        <defs>
          <marker
            id={`arrowhead-${connection.id}`}
            markerWidth="10"
            markerHeight="7"
            refX="9"
            refY="3.5"
            orient="auto"
          >
            <polygon
              points="0 0, 10 3.5, 0 7"
              fill={connection.type === 'message' ? '#f59e0b' : '#374151'}
            />
          </marker>
        </defs>
        <line
          x1={startX}
          y1={startY}
          x2={endX}
          y2={endY}
          stroke={connection.type === 'message' ? '#f59e0b' : '#374151'}
          strokeWidth="2"
          strokeDasharray={connection.type === 'association' ? '5,5' : 'none'}
          markerEnd={`url(#arrowhead-${connection.id})`}
        />
        {connection.label && (
          <text
            x={(startX + endX) / 2}
            y={(startY + endY) / 2 - 5}
            textAnchor="middle"
            className="small fill-muted"
          >
            {connection.label}
          </text>
        )}
      </svg>
    )
  }

  return (
    <div
      className="d-flex flex-column"
      style={{
        backgroundColor: '#ffffff',
        margin: '-16px -24px',
        padding: 0,
        minHeight: containerHeight > 0 ? `${containerHeight}px` : '100%',
        height: containerHeight > 0 ? `${containerHeight}px` : '100%',
        width: 'calc(100% + 48px)',
        maxWidth: 'none',
        overflow: 'hidden',
        display: 'flex',
        flexDirection: 'column',
        position: 'relative',
        flex: '1 1 auto'
      }}
    >
      {/* Header */}
      <nav className="navbar navbar-light border-bottom" style={{ backgroundColor: '#ffffff', borderBottomColor: '#e5e7eb' }}>
        <div className="container-fluid px-4">
          <div className="d-flex align-items-center">
            <Link href="/dashboard">
              <Button variant="outline" size="sm" className="me-3">
                <ArrowLeft size={16} className="me-2" />
                Back
              </Button>
            </Link>
            <div className="d-flex align-items-center">
              <div className="bg-info rounded-3 p-2 me-2">
                <Workflow className="text-white" size={20} />
              </div>
              <div>
                <h1 className="h6 mb-0 fw-bold">BPMN Designer</h1>
                <p className="text-muted small mb-0">Business Process Modeling</p>
              </div>
            </div>
          </div>

          <div className="d-flex align-items-center gap-2">
            <div className="btn-group" role="group">
              <button
                type="button"
                className={`btn btn-sm ${tool === 'select' ? 'btn-primary' : 'btn-outline-secondary'}`}
                onClick={() => setTool('select')}
              >
                <MousePointer size={16} />
              </button>
              <button
                type="button"
                className={`btn btn-sm ${tool === 'connect' ? 'btn-primary' : 'btn-outline-secondary'}`}
                onClick={() => setTool('connect')}
              >
                <ArrowRight size={16} />
              </button>
            </div>
            <Button variant="outline" size="sm">
              <Play size={16} className="me-1" />
              Simulate
            </Button>
            <Button variant="outline" size="sm" onClick={handleExport} disabled={loading}>
              <Download size={16} className="me-1" />
              Export
            </Button>
            <Button size="sm" onClick={handleSave} disabled={loading || !selectedProcessId}>
              {loading ? <Loader2 size={16} className="me-1 animate-spin" /> : <Save size={16} className="me-1" />}
              Save
            </Button>
          </div>
        </div>
      </nav>

      {/* Toolbar */}
      <div className="border-bottom" style={{ backgroundColor: '#ffffff', borderBottomColor: '#e5e7eb' }}>
        <div className="container-fluid px-4 py-2">
          <div className="d-flex align-items-center justify-content-between">
            <div className="d-flex align-items-center gap-3">
              <div className="d-flex gap-1">
                <Button variant="outline" size="sm">
                  <Undo size={16} />
                </Button>
                <Button variant="outline" size="sm">
                  <Redo size={16} />
                </Button>
              </div>

              <div className="vr"></div>

              <div className="d-flex gap-1">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={deleteSelectedElement}
                  disabled={!selectedElement}
                >
                  <Trash2 size={16} />
                </Button>
              </div>

              <div className="vr"></div>

              <div className="d-flex align-items-center gap-2">
                <input
                  type="text"
                  className="form-control form-control-sm"
                  placeholder="Process Name"
                  value={processName}
                  onChange={(e) => setProcessName(e.target.value)}
                  style={{ width: '200px' }}
                />
              </div>

              <div className="vr"></div>

              {/* Selector de Procesos */}
              <div className="d-flex align-items-center gap-2">
                <label className="form-label small mb-0">Cargar Proceso:</label>
                <select
                  className="form-select form-select-sm"
                  value={selectedProcessId || ''}
                  onChange={(e) => {
                    if (e.target.value) {
                      loadProcess(e.target.value)
                    } else {
                      setSelectedProcessId(null)
                      setElements([])
                      setConnections([])
                      setProcessName('New Process')
                    }
                  }}
                  disabled={loading}
                  style={{ width: '250px' }}
                >
                  <option value="">-- Nuevo proceso --</option>
                  {availableProcesses.map(proc => (
                    <option key={proc.id} value={proc.id}>
                      {proc.name} ({proc.category})
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="d-flex align-items-center gap-3">
              {error && (
                <div className="alert alert-danger py-1 px-2 mb-0 small">
                  {error}
                </div>
              )}
              <div className="form-check form-switch">
                <input
                  className="form-check-input"
                  type="checkbox"
                  id="gridToggle"
                  checked={showGrid}
                  onChange={(e) => setShowGrid(e.target.checked)}
                />
                <label className="form-check-label small" htmlFor="gridToggle">
                  Grid
                </label>
              </div>

              <div className="vr"></div>

              <div className="d-flex align-items-center gap-2">
                <Button variant="outline" size="sm" onClick={() => setZoom(Math.max(25, zoom - 25))}>
                  <ZoomOut size={16} />
                </Button>
                <span className="small" style={{ minWidth: '50px', textAlign: 'center' }}>{zoom}%</span>
                <Button variant="outline" size="sm" onClick={() => setZoom(Math.min(200, zoom + 25))}>
                  <ZoomIn size={16} />
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-grow-1 d-flex overflow-hidden" style={{ minHeight: 0, flex: '1 1 auto', height: '100%' }}>
        {/* Element Palette */}
        <div className="border-end" style={{ width: '280px', flexShrink: 0, backgroundColor: '#ffffff', borderRightColor: '#e5e7eb', display: 'flex', flexDirection: 'column' }}>
          <div className="p-3 border-bottom" style={{ borderBottomColor: '#e5e7eb', flexShrink: 0 }}>
            <h6 className="small fw-bold mb-0" style={{ color: '#1f2937' }}>BPMN Elements</h6>
          </div>
          <div className="overflow-auto" style={{ flex: 1, minHeight: 0 }}>
            <div className="p-3">
              {Object.entries(
                bpmnElements.reduce((acc, element) => {
                  if (!acc[element.category]) {
                    acc[element.category] = []
                  }
                  acc[element.category].push(element)
                  return acc
                }, {} as Record<string, any[]>)
              ).map(([category, elements]) => (
                <div key={category} className="mb-4">
                  <h6 className="small fw-bold mb-3" style={{ color: '#6b7280' }}>{category.toUpperCase()}</h6>
                  <div className="d-grid gap-2">
                    {elements.map((element) => (
                      <div
                        key={element.id}
                        draggable
                        onDragStart={(e) => handleDragStart(e, element)}
                        className="d-flex align-items-center p-2 border rounded cursor-grab"
                        style={{
                          borderColor: '#e5e7eb',
                          backgroundColor: '#ffffff',
                          transition: 'background-color 0.2s'
                        }}
                        onMouseEnter={(e) => {
                          e.currentTarget.style.backgroundColor = '#f9fafb'
                        }}
                        onMouseLeave={(e) => {
                          e.currentTarget.style.backgroundColor = '#ffffff'
                        }}
                      >
                        {element.icon}
                        <div className="flex-grow-1 ms-2">
                          <p className="small fw-medium mb-0" style={{ color: '#1f2937' }}>{element.name}</p>
                          <p style={{ fontSize: '0.75rem', color: '#6b7280' }}>{element.description}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Canvas */}
        <div className="flex-grow-1 position-relative overflow-auto" style={{ backgroundColor: '#f9fafb', minWidth: 0, flex: '1 1 auto', width: '100%' }}>
          <div className="p-4" style={{ minWidth: 'fit-content', minHeight: 'fit-content', width: '100%', height: '100%' }}>
            <div
              ref={canvasRef}
              className="shadow-sm"
              style={{
                width: '1200px',
                height: '800px',
                minWidth: '1200px',
                minHeight: '800px',
                backgroundColor: '#ffffff',
                transform: `scale(${zoom / 100})`,
                transformOrigin: 'top left',
                boxShadow: '0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06)',
                position: 'relative',
                overflow: 'visible'
              }}
              onDrop={handleDrop}
              onDragOver={handleDragOver}
            >
              {/* Grid - Fondo completo */}
              {showGrid && (
                <div
                  style={{
                    position: 'absolute',
                    top: '0px',
                    left: '0px',
                    width: '1200px',
                    height: '800px',
                    pointerEvents: 'none',
                    backgroundImage: `
                      linear-gradient(rgba(0,0,0,0.08) 1px, transparent 1px),
                      linear-gradient(90deg, rgba(0,0,0,0.08) 1px, transparent 1px)
                    `,
                    backgroundSize: '20px 20px',
                    backgroundPosition: '0 0',
                    backgroundRepeat: 'repeat',
                    zIndex: 0,
                    margin: 0,
                    padding: 0
                  }}
                />
              )}

              {/* Connections */}
              <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', zIndex: 2, pointerEvents: 'none' }}>
                {connections.map(renderConnection)}
              </div>

              {/* Elements */}
              <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', zIndex: 3 }}>
                {elements.map(renderBPMNElement)}
              </div>

              {/* Empty State */}
              {elements.length === 0 && (
                <div className="position-absolute top-50 start-50 translate-middle text-center" style={{ color: '#6b7280' }}>
                  <Workflow size={64} className="mb-3 opacity-50" style={{ color: '#9ca3af' }} />
                  <h5 className="fw-medium" style={{ color: '#374151' }}>Start Building Your Process</h5>
                  <p className="small" style={{ color: '#6b7280' }}>Drag BPMN elements from the palette to begin</p>
                </div>
              )}

              {/* Connection Mode Indicator */}
              {isConnecting && (
                <div className="position-absolute top-0 start-0 m-3">
                  <div className="alert alert-success py-2 px-3 mb-0">
                    <div className="d-flex align-items-center small">
                      <ArrowRight size={16} className="me-2" />
                      <span>Click target element to connect</span>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Properties Panel */}
        <div className="border-start" style={{ width: '300px', flexShrink: 0, backgroundColor: '#ffffff', borderLeftColor: '#e5e7eb', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
          <div className="p-3 border-bottom" style={{ borderBottomColor: '#e5e7eb', flexShrink: 0 }}>
            <h6 className="small fw-bold mb-0" style={{ color: '#1f2937' }}>Properties</h6>
          </div>
          <div style={{ flex: 1, overflowY: 'auto', minHeight: 0 }}>

          {selectedElement ? (
            <div className="p-3">
              <div className="mb-4">
                <h6 className="small fw-bold mb-3">Element Details</h6>
                <div className="mb-3">
                  <label className="form-label small">Name</label>
                  <input
                    type="text"
                    className="form-control form-control-sm"
                    value={selectedElement.name}
                    onChange={(e) => {
                      const updatedElements = elements.map(el =>
                        el.id === selectedElement.id ? { ...el, name: e.target.value } : el
                      )
                      setElements(updatedElements)
                      setSelectedElement({ ...selectedElement, name: e.target.value })
                    }}
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label small">Description</label>
                  <textarea
                    className="form-control form-control-sm"
                    rows={3}
                    value={selectedElement.description || ''}
                    onChange={(e) => {
                      const updatedElements = elements.map(el =>
                        el.id === selectedElement.id ? { ...el, description: e.target.value } : el
                      )
                      setElements(updatedElements)
                      setSelectedElement({ ...selectedElement, description: e.target.value })
                    }}
                  />
                </div>
              </div>

              {selectedElement.type === 'task' && (
                <div className="mb-4">
                  <h6 className="small fw-bold mb-3">Task Properties</h6>
                  {selectedElement.subtype === 'user' && (
                    <>
                      <div className="mb-3">
                        <label className="form-label small">Form Key</label>
                        <input
                          type="text"
                          className="form-control form-control-sm"
                          placeholder="plataforma/workflow/form.zul"
                          value={selectedElement.properties.formKey || ''}
                          onChange={(e) => {
                            const updatedElements = elements.map(el =>
                              el.id === selectedElement.id
                                ? { ...el, properties: { ...el.properties, formKey: e.target.value } }
                                : el
                            )
                            setElements(updatedElements)
                            setSelectedElement({
                              ...selectedElement,
                              properties: { ...selectedElement.properties, formKey: e.target.value }
                            })
                          }}
                        />
                      </div>
                      <div className="mb-3">
                        <label className="form-label small">Candidate Groups</label>
                        <input
                          type="text"
                          className="form-control form-control-sm"
                          placeholder="group1,group2"
                          value={selectedElement.properties.candidateGroups?.join(',') || ''}
                          onChange={(e) => {
                            const groups = e.target.value.split(',').map(g => g.trim()).filter(g => g)
                            const updatedElements = elements.map(el =>
                              el.id === selectedElement.id
                                ? { ...el, properties: { ...el.properties, candidateGroups: groups } }
                                : el
                            )
                            setElements(updatedElements)
                            setSelectedElement({
                              ...selectedElement,
                              properties: { ...selectedElement.properties, candidateGroups: groups }
                            })
                          }}
                        />
                      </div>
                      <div className="mb-3">
                        <label className="form-label small">Assignee</label>
                        <input
                          type="text"
                          className="form-control form-control-sm"
                          placeholder="user@example.com"
                          value={selectedElement.properties.assignee || ''}
                          onChange={(e) => {
                            const updatedElements = elements.map(el =>
                              el.id === selectedElement.id
                                ? { ...el, properties: { ...el.properties, assignee: e.target.value } }
                                : el
                            )
                            setElements(updatedElements)
                            setSelectedElement({
                              ...selectedElement,
                              properties: { ...selectedElement.properties, assignee: e.target.value }
                            })
                          }}
                        />
                      </div>
                    </>
                  )}
                  {selectedElement.subtype === 'service' && (
                    <div className="mb-3">
                      <label className="form-label small">Delegate Class</label>
                      <input
                        type="text"
                        className="form-control form-control-sm"
                        placeholder="com.example.Delegate"
                        value={selectedElement.properties.delegateClass || ''}
                        onChange={(e) => {
                          const updatedElements = elements.map(el =>
                            el.id === selectedElement.id
                              ? { ...el, properties: { ...el.properties, delegateClass: e.target.value } }
                              : el
                          )
                          setElements(updatedElements)
                          setSelectedElement({
                            ...selectedElement,
                            properties: { ...selectedElement.properties, delegateClass: e.target.value }
                          })
                        }}
                      />
                    </div>
                  )}
                  <div className="mb-3">
                    <label className="form-label small">Priority</label>
                    <select
                      className="form-select form-select-sm"
                      value={selectedElement.properties.priority || 50}
                      onChange={(e) => {
                        const updatedElements = elements.map(el =>
                          el.id === selectedElement.id
                            ? { ...el, properties: { ...el.properties, priority: parseInt(e.target.value) } }
                            : el
                        )
                        setElements(updatedElements)
                        setSelectedElement({
                          ...selectedElement,
                          properties: { ...selectedElement.properties, priority: parseInt(e.target.value) }
                        })
                      }}
                    >
                      <option value="0">Low</option>
                      <option value="25">Normal</option>
                      <option value="50">High</option>
                      <option value="75">Urgent</option>
                    </select>
                  </div>
                </div>
              )}

              <div>
                <h6 className="small fw-bold mb-3">Documentation</h6>
                <textarea
                  className="form-control form-control-sm"
                  rows={4}
                  placeholder="Add documentation for this element..."
                  value={selectedElement.properties.documentation || ''}
                  onChange={(e) => {
                    const updatedElements = elements.map(el =>
                      el.id === selectedElement.id
                        ? { ...el, properties: { ...el.properties, documentation: e.target.value } }
                        : el
                    )
                    setElements(updatedElements)
                    setSelectedElement({
                      ...selectedElement,
                      properties: { ...selectedElement.properties, documentation: e.target.value }
                    })
                  }}
                />
              </div>
            </div>
          ) : (
            <div className="p-3 text-center" style={{ color: '#6b7280' }}>
              <Settings size={48} className="mb-3 opacity-50" style={{ color: '#9ca3af' }} />
              <p className="small" style={{ color: '#6b7280' }}>Select an element to edit properties</p>
            </div>
          )}
          </div>
        </div>
      </div>

      <style jsx>{`
        .cursor-grab {
          cursor: grab;
        }

        .cursor-grab:active {
          cursor: grabbing;
        }

        .cursor-pointer {
          cursor: pointer;
        }

        .hover-bg-light:hover {
          background-color: rgba(0, 0, 0, 0.05) !important;
        }

        .hover-shadow:hover {
          box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075) !important;
        }
      `}</style>
    </div>
  )
}


