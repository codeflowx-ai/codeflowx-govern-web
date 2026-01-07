// components/CodeMirrorEditor.tsx
'use client'

import { useEffect, useRef } from 'react'
import { EditorView, basicSetup } from 'codemirror'
import { EditorState } from '@codemirror/state'
import { javascript } from '@codemirror/lang-javascript'
import { java } from '@codemirror/lang-java'
import { json } from '@codemirror/lang-json'
import { html } from '@codemirror/lang-html'
import { css } from '@codemirror/lang-css'
import { python } from '@codemirror/lang-python'
import { oneDark } from '@codemirror/theme-one-dark'
import { indentWithTab } from '@codemirror/commands'
import { keymap } from '@codemirror/view'
import { autocompletion } from '@codemirror/autocomplete'
import { lintGutter, linter } from '@codemirror/lint'
import { foldGutter } from '@codemirror/language'
import { lineNumbers, highlightActiveLineGutter } from '@codemirror/view'
import { defaultHighlightStyle, syntaxHighlighting } from '@codemirror/language'
import { highlightSelectionMatches, searchKeymap } from '@codemirror/search'
import { closeBrackets, closeBracketsKeymap } from '@codemirror/autocomplete'

interface CodeMirrorEditorProps {
    value: string
    onChange: (value: string) => void
    language: string
    theme?: 'light' | 'dark'
    height?: string
    onCursorChange?: (line: number, column: number) => void
    showMinimap?: boolean
    fontSize?: number
    wordWrap?: boolean
    showLineNumbers?: boolean
    autoSave?: boolean
    readOnly?: boolean
}

export function CodeMirrorEditor({
                                     value,
                                     onChange,
                                     language,
                                     theme = 'light',
                                     height = '100%',
                                     onCursorChange,
                                     showMinimap = true,
                                     fontSize = 14,
                                     wordWrap = true,
                                     showLineNumbers = true,
                                     autoSave = true,
                                     readOnly = false
                                 }: CodeMirrorEditorProps) {
    const editorRef = useRef<HTMLDivElement>(null)
    const viewRef = useRef<EditorView | null>(null)

    const getLanguageExtension = (lang: string) => {
        switch (lang.toLowerCase()) {
            case 'javascript':
            case 'js':
            case 'typescript':
            case 'ts':
            case 'tsx':
            case 'jsx':
                return javascript({ jsx: true, typescript: lang.includes('ts') })
            case 'java':
                return java()
            case 'json':
                return json()
            case 'html':
                return html()
            case 'css':
                return css()
            case 'python':
            case 'py':
                return python()
            default:
                return javascript()
        }
    }

    useEffect(() => {
        if (!editorRef.current) return

        const extensions = [
            basicSetup,
            getLanguageExtension(language),
            keymap.of([indentWithTab, ...closeBracketsKeymap, ...searchKeymap]),
            EditorView.updateListener.of((update) => {
                if (update.docChanged) {
                    onChange(update.state.doc.toString())
                }
                if (update.selectionSet && onCursorChange) {
                    const cursor = update.state.selection.main.head
                    const line = update.state.doc.lineAt(cursor)
                    onCursorChange(line.number, cursor - line.from + 1)
                }
            }),
            EditorView.theme({
                '&': {
                    fontSize: `${fontSize}px`,
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
                }
            }),
            syntaxHighlighting(defaultHighlightStyle),
            autocompletion(),
            closeBrackets(),
            highlightSelectionMatches(),
            ...(showLineNumbers ? [lineNumbers(), highlightActiveLineGutter()] : []),
            foldGutter(),
            ...(wordWrap ? [EditorView.lineWrapping] : []),
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
    }, [language, theme, fontSize, wordWrap, showLineNumbers, readOnly])

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
        <div className="w-full h-full overflow-hidden">
            <div ref={editorRef} className="w-full h-full" />
        </div>
    )
}