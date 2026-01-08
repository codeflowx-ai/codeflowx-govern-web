'use client'

import { useEffect, useRef, useState } from 'react'
import { EditorView, basicSetup } from 'codemirror'
import { hoverTooltip } from '@codemirror/view'
import { EditorState } from '@codemirror/state'
import { markdown } from '@codemirror/lang-markdown'
import { oneDark } from '@codemirror/theme-one-dark'
import { indentWithTab } from '@codemirror/commands'
import { keymap } from '@codemirror/view'
import { autocompletion, CompletionContext, CompletionResult } from '@codemirror/autocomplete'
import { foldGutter } from '@codemirror/language'
import { lineNumbers, highlightActiveLineGutter } from '@codemirror/view'
import { defaultHighlightStyle, syntaxHighlighting } from '@codemirror/language'
import { highlightSelectionMatches, searchKeymap } from '@codemirror/search'
import { closeBrackets, closeBracketsKeymap } from '@codemirror/autocomplete'

interface CodeEditorProps {
  value: string
  onChange: (value: string) => void
  language?: string
  height?: string
  placeholder?: string
  readOnly?: boolean
}

// Sugerencias de autocompletado para Prompty
const promptyCompletions = [
  { label: 'name', type: 'property', info: 'Nombre del prompt' },
  { label: 'description', type: 'property', info: 'Descripción del prompt' },
  { label: 'version', type: 'property', info: 'Versión del prompt (ej: 1.0.0)' },
  { label: 'author', type: 'property', info: 'Autor del prompt' },
  { label: 'tags', type: 'property', info: 'Etiquetas del prompt (array)' },
  { label: 'Instructions', type: 'heading', info: 'Instrucciones principales para el modelo' },
  { label: 'Context', type: 'heading', info: 'Contexto adicional para el prompt' },
  { label: 'Input Variables', type: 'heading', info: 'Variables de entrada del prompt' },
  { label: 'Output Format', type: 'heading', info: 'Formato de salida esperado' },
  { label: 'Examples', type: 'heading', info: 'Ejemplos de uso' },
  { label: 'Constraints', type: 'heading', info: 'Restricciones y limitaciones' },
  { label: 'Notes', type: 'heading', info: 'Notas adicionales' },
]

// Fuente de autocompletado personalizada
function promptyAutocomplete(context: CompletionContext): CompletionResult | null {
  const word = context.matchBefore(/\w*/)
  if (!word || word.from === word.to && !context.explicit) return null

  return {
    from: word.from,
    options: promptyCompletions.map(completion => ({
      label: completion.label,
      type: completion.type,
      detail: completion.info,
    }))
  }
}

// Tooltip con información contextual
const promptyTooltip = hoverTooltip((view, pos) => {
  const { text } = view.state.doc.lineAt(pos)
  const word = text.match(/\w+/)?.[0]

  if (!word) return null

  const completion = promptyCompletions.find(c => c.label.toLowerCase() === word.toLowerCase())
  if (!completion) return null

  return {
    pos,
    end: pos,
    create: () => {
      const dom = document.createElement('div')
      dom.className = 'cm-tooltip-hover'
      dom.innerHTML = `
        <div style="padding: 8px; font-size: 12px;">
          <strong>${completion.label}</strong>
          <div style="margin-top: 4px; color: #666;">${completion.info}</div>
        </div>
      `
      return { dom }
    }
  }
})

export function CodeEditor({
  value,
  onChange,
  language = 'markdown',
  height = '400px',
  placeholder = 'Escribe tu código aquí...',
  readOnly = false,
}: CodeEditorProps) {
  const editorRef = useRef<HTMLDivElement>(null)
  const viewRef = useRef<EditorView | null>(null)
  const [theme, setTheme] = useState<'light' | 'dark'>('light')

  // Detectar tema
  useEffect(() => {
    const detectTheme = () => {
      const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches ||
                     document.documentElement.classList.contains('dark')
      setTheme(isDark ? 'dark' : 'light')
    }

    detectTheme()

    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    mediaQuery.addEventListener('change', detectTheme)

    const observer = new MutationObserver(detectTheme)
    observer.observe(document.documentElement, {
      attributes: true,
      attributeFilter: ['class']
    })

    return () => {
      mediaQuery.removeEventListener('change', detectTheme)
      observer.disconnect()
    }
  }, [])

  useEffect(() => {
    if (!editorRef.current) return

    const extensions = [
      basicSetup,
      markdown(),
      keymap.of([indentWithTab, ...closeBracketsKeymap, ...searchKeymap]),
      EditorView.updateListener.of((update) => {
        if (update.docChanged) {
          onChange(update.state.doc.toString())
        }
      }),
      EditorView.theme({
        '&': {
          fontSize: '14px',
          height: height
        },
        '.cm-content': {
          padding: '16px',
          minHeight: '100%'
        },
        '.cm-focused': {
          outline: 'none'
        },
        '.cm-editor': {
          height: '100%'
        },
        '.cm-scroller': {
          fontFamily: '"Fira Code", "JetBrains Mono", "SF Mono", Consolas, monospace'
        },
        '.cm-tooltip-hover': {
          backgroundColor: theme === 'dark' ? '#2d2d2d' : '#f8f9fa',
          border: `1px solid ${theme === 'dark' ? '#444' : '#ddd'}`,
          borderRadius: '4px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
          maxWidth: '300px',
          zIndex: 1000
        }
      }),
      syntaxHighlighting(defaultHighlightStyle),
      autocompletion({
        override: [promptyAutocomplete],
        activateOnTyping: true,
        maxRenderedOptions: 10,
        closeOnBlur: true,
        defaultKeymap: true,
      }),
      promptyTooltip,
      closeBrackets(),
      highlightSelectionMatches(),
      lineNumbers(),
      highlightActiveLineGutter(),
      foldGutter(),
      EditorView.lineWrapping,
      ...(theme === 'dark' ? [oneDark] : []),
      ...(readOnly ? [EditorState.readOnly.of(true)] : [])
    ]

    const state = EditorState.create({
      doc: value,
      extensions
    })

    const view = new EditorView({
      state,
      parent: editorRef.current
    })

    viewRef.current = view

    return () => {
      view.destroy()
    }
  }, [language, theme, height, readOnly, onChange])

  useEffect(() => {
    if (viewRef.current && viewRef.current.state.doc.toString() !== value) {
      viewRef.current.dispatch({
        changes: {
          from: 0,
          to: viewRef.current.state.doc.length,
          insert: value
        }
      })
    }
  }, [value])

  return (
    <div className="w-full border rounded-md overflow-hidden" style={{ height }}>
      <div ref={editorRef} className="w-full" style={{ height }} />
    </div>
  )
}
