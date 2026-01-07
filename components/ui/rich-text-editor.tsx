'use client';

import React, { useMemo } from 'react';
import dynamic from 'next/dynamic';
import 'react-quill/dist/quill.snow.css';

// Importación dinámica para evitar problemas de SSR
const ReactQuill = dynamic(() => import('react-quill'), { ssr: false });

interface RichTextEditorProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  className?: string;
  minHeight?: string;
}

export function RichTextEditor({
  value,
  onChange,
  placeholder = 'Escribe aquí...',
  className = '',
  minHeight = '300px'
}: RichTextEditorProps) {
  const modules = useMemo(() => ({
    toolbar: [
      [{ 'header': [1, 2, 3, false] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'align': [] }],
      ['link', 'blockquote', 'code-block'],
      ['clean']
    ],
  }), []);

  const formats = [
    'header',
    'bold', 'italic', 'underline', 'strike',
    'list', 'bullet',
    'color', 'background',
    'align',
    'link', 'blockquote', 'code-block'
  ];

  return (
    <div className={className}>
      <ReactQuill
        theme="snow"
        value={value}
        onChange={onChange}
        modules={modules}
        formats={formats}
        placeholder={placeholder}
        style={{
          backgroundColor: 'var(--theme-background)',
          color: 'var(--theme-text)',
        }}
      />
      <style jsx global>{`
        .quill {
          background-color: var(--theme-background);
          color: var(--theme-text);
        }
        .quill .ql-container {
          min-height: ${minHeight};
          font-size: 14px;
          color: var(--theme-text);
        }
        .quill .ql-editor {
          min-height: ${minHeight};
          color: var(--theme-text);
          line-height: 1.6;
        }
        .quill .ql-editor.ql-blank::before {
          color: var(--theme-textMuted);
          font-style: normal;
        }
        .quill .ql-toolbar {
          background-color: var(--theme-surface);
          border-color: var(--theme-border);
          border-top-left-radius: 0.5rem;
          border-top-right-radius: 0.5rem;
        }
        .quill .ql-container {
          border-color: var(--theme-border);
          border-bottom-left-radius: 0.5rem;
          border-bottom-right-radius: 0.5rem;
        }
        .quill .ql-stroke {
          stroke: var(--theme-text);
        }
        .quill .ql-fill {
          fill: var(--theme-text);
        }
        .quill .ql-picker-label {
          color: var(--theme-text);
        }
        .quill .ql-picker-options {
          background-color: var(--theme-background);
          border-color: var(--theme-border);
        }
        .quill .ql-picker-item {
          color: var(--theme-text);
        }
        .quill .ql-picker-item:hover {
          background-color: var(--theme-hover);
        }
        .quill .ql-editor p,
        .quill .ql-editor ol,
        .quill .ql-editor ul {
          margin: 0.5rem 0;
        }
        .quill .ql-editor ol,
        .quill .ql-editor ul {
          padding-left: 1.5rem;
        }
      `}</style>
    </div>
  );
}
