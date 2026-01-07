// app/(app)/bpmn/processes/page.tsx
'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from '@/components/ui/button'
import {
  Search,
  FileText,
  Edit,
  Copy,
  Folder,
  Loader2,
  Workflow,
  ArrowLeft,
  CheckCircle,
  XCircle
} from 'lucide-react'
import Link from 'next/link'
import { BPMNProcessInfo } from '@/types/bpmn-designer'

export default function BPMNProcessesPage() {
  const router = useRouter()
  const [processes, setProcesses] = useState<BPMNProcessInfo[]>([])
  const [filteredProcesses, setFilteredProcesses] = useState<BPMNProcessInfo[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [copyingProcess, setCopyingProcess] = useState<string | null>(null)
  const [copiedProcesses, setCopiedProcesses] = useState<Set<string>>(new Set())

  useEffect(() => {
    loadProcesses()
  }, [])

  useEffect(() => {
    filterProcesses()
  }, [searchTerm, processes])

  const loadProcesses = async () => {
    try {
      setLoading(true)
      setError(null)

      // Usar directamente la API local (modo demo)
      const response = await fetch('/api/bpmn/processes/local')

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ error: 'Unknown error' }))
        throw new Error(errorData.error || 'Failed to load processes from local storage')
      }

      const data = await response.json()
      setProcesses(data)
      setFilteredProcesses(data)
    } catch (err) {
      console.error('Error loading processes:', err)
      const errorMessage = err instanceof Error ? err.message : 'Error al cargar los procesos'
      setError(errorMessage)
      // No intentar con backend en modo demo - solo mostrar el error
    } finally {
      setLoading(false)
    }
  }

  const filterProcesses = () => {
    if (!searchTerm.trim()) {
      setFilteredProcesses(processes)
      return
    }

    const term = searchTerm.toLowerCase()
    const filtered = processes.filter(process =>
      process.name.toLowerCase().includes(term) ||
      process.id.toLowerCase().includes(term) ||
      process.category?.toLowerCase().includes(term) ||
      process.path?.toLowerCase().includes(term)
    )
    setFilteredProcesses(filtered)
  }

  const copyToTemporary = async (processId: string) => {
    try {
      setCopyingProcess(processId)
      setError(null)

      // En modo demo, cargar directamente desde local y crear proceso temporal
      // Cargar desde API local (modo demo)
      const localResponse = await fetch(`/api/bpmn/processes/local/${encodeURIComponent(processId)}`)

      if (!localResponse.ok) {
        const errorData = await localResponse.json().catch(() => ({ error: 'Unknown error' }))
        throw new Error(errorData.error || 'Failed to load process from local storage')
      }

      const xml = await localResponse.text()

      // Crear ID temporal
      const tempId = `temp_${processId}_${Date.now()}`

      // Guardar en localStorage como proceso temporal (modo demo)
      const tempProcesses = JSON.parse(localStorage.getItem('bpmn_temp_processes') || '{}')
      tempProcesses[tempId] = {
        id: tempId,
        originalId: processId,
        xml: xml,
        name: processId,
        timestamp: Date.now()
      }
      localStorage.setItem('bpmn_temp_processes', JSON.stringify(tempProcesses))

      setCopiedProcesses(prev => new Set([...prev, tempId]))
      router.push(`/bpmn-designer?process=${encodeURIComponent(tempId)}`)
    } catch (err) {
      console.error('Error copying process:', err)
      setError(err instanceof Error ? err.message : 'Error al copiar el proceso')
      alert(err instanceof Error ? err.message : 'Error al copiar el proceso')
    } finally {
      setCopyingProcess(null)
    }
  }

  const openInDesigner = (processId: string) => {
    // En modo demo, intentar cargar desde local primero
    router.push(`/bpmn-designer?process=${encodeURIComponent(processId)}`)
  }

  const processesByCategory = filteredProcesses.reduce((acc, process) => {
    const category = process.category || 'other'
    if (!acc[category]) {
      acc[category] = []
    }
    acc[category].push(process)
    return acc
  }, {} as Record<string, BPMNProcessInfo[]>)

  return (
    <div className="container-fluid py-4" style={{ backgroundColor: '#ffffff', minHeight: '100vh' }}>
      {/* Header */}
      <div className="d-flex align-items-center justify-content-between mb-4">
        <div className="d-flex align-items-center gap-3">
          <Link href="/dashboard">
            <Button variant="outline" size="sm">
              <ArrowLeft size={16} className="me-2" />
              Back
            </Button>
          </Link>
          <div className="bg-info rounded-3 p-2">
            <Workflow className="text-white" size={24} />
          </div>
          <div>
            <h1 className="h4 mb-0 fw-bold" style={{ color: '#1f2937' }}>BPMN Processes</h1>
            <p className="text-muted small mb-0">Browse and manage BPMN process definitions</p>
          </div>
        </div>
      </div>

      {/* Search Bar */}
      <div className="card mb-4" style={{ borderColor: '#e5e7eb' }}>
        <div className="card-body">
          <div className="input-group">
            <span className="input-group-text" style={{ backgroundColor: '#f9fafb', borderColor: '#e5e7eb' }}>
              <Search size={18} style={{ color: '#6b7280' }} />
            </span>
            <input
              type="text"
              className="form-control"
              placeholder="Search processes by name, ID, category, or path..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{ borderColor: '#e5e7eb' }}
            />
          </div>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="alert alert-danger mb-4" role="alert">
          <XCircle size={20} className="me-2" />
          {error}
        </div>
      )}

      {/* Loading State */}
      {loading && (
        <div className="text-center py-5">
          <Loader2 size={32} className="animate-spin mb-3" style={{ color: '#6b7280' }} />
          <p className="text-muted">Loading processes...</p>
        </div>
      )}

      {/* Processes List */}
      {!loading && (
        <>
          {filteredProcesses.length === 0 ? (
            <div className="card" style={{ borderColor: '#e5e7eb' }}>
              <div className="card-body text-center py-5">
                <FileText size={48} className="mb-3 opacity-50" style={{ color: '#9ca3af' }} />
                <h5 className="fw-medium" style={{ color: '#374151' }}>
                  {searchTerm ? 'No processes found' : 'No processes available'}
                </h5>
                <p className="text-muted small">
                  {searchTerm
                    ? 'Try adjusting your search terms'
                    : 'No BPMN processes are currently available'}
                </p>
              </div>
            </div>
          ) : (
            <div className="row g-3">
              {Object.entries(processesByCategory).map(([category, categoryProcesses]) => (
                <div key={category} className="col-12">
                  <div className="card" style={{ borderColor: '#e5e7eb' }}>
                    <div className="card-header" style={{ backgroundColor: '#f9fafb', borderBottomColor: '#e5e7eb' }}>
                      <div className="d-flex align-items-center gap-2">
                        <Folder size={18} style={{ color: '#3b82f6' }} />
                        <h6 className="mb-0 fw-bold" style={{ color: '#1f2937', textTransform: 'capitalize' }}>
                          {category}
                        </h6>
                        <span className="badge bg-secondary ms-2">{categoryProcesses.length}</span>
                      </div>
                    </div>
                    <div className="card-body">
                      <div className="row g-3">
                        {categoryProcesses.map((process) => (
                          <div key={process.id} className="col-md-6 col-lg-4">
                            <div
                              className="card h-100"
                              style={{
                                borderColor: '#e5e7eb',
                                transition: 'box-shadow 0.2s',
                                cursor: 'pointer'
                              }}
                              onMouseEnter={(e) => {
                                e.currentTarget.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)'
                              }}
                              onMouseLeave={(e) => {
                                e.currentTarget.style.boxShadow = 'none'
                              }}
                            >
                              <div className="card-body">
                                <div className="d-flex align-items-start justify-content-between mb-2">
                                  <div className="flex-grow-1">
                                    <h6 className="fw-bold mb-1" style={{ color: '#1f2937' }}>
                                      {process.name}
                                    </h6>
                                    <p className="small text-muted mb-2" style={{ color: '#6b7280' }}>
                                      ID: {process.id}
                                    </p>
                                    {process.path && (
                                      <p className="small mb-0" style={{ color: '#9ca3af', fontSize: '0.75rem' }}>
                                        <code>{process.path}</code>
                                      </p>
                                    )}
                                  </div>
                                  {copiedProcesses.has(process.id) && (
                                    <CheckCircle size={20} style={{ color: '#10b981' }} />
                                  )}
                                </div>
                                <div className="d-flex gap-2 mt-3">
                                  <Button
                                    variant="outline"
                                    size="sm"
                                    className="flex-grow-1"
                                    onClick={() => copyToTemporary(process.id)}
                                    disabled={copyingProcess === process.id || copiedProcesses.has(process.id)}
                                  >
                                    {copyingProcess === process.id ? (
                                      <>
                                        <Loader2 size={14} className="me-2 animate-spin" />
                                        Copying...
                                      </>
                                    ) : copiedProcesses.has(process.id) ? (
                                      <>
                                        <CheckCircle size={14} className="me-2" />
                                        Copied
                                      </>
                                    ) : (
                                      <>
                                        <Copy size={14} className="me-2" />
                                        Copy & Edit
                                      </>
                                    )}
                                  </Button>
                                  <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => openInDesigner(process.id)}
                                  >
                                    <Edit size={14} className="me-2" />
                                    View
                                  </Button>
                                </div>
                              </div>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </>
      )}

      {/* Summary */}
      {!loading && filteredProcesses.length > 0 && (
        <div className="mt-4 text-center">
          <p className="text-muted small">
            Showing {filteredProcesses.length} of {processes.length} processes
            {searchTerm && ` matching "${searchTerm}"`}
          </p>
        </div>
      )}
    </div>
  )
}
