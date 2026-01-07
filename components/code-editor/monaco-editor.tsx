// components/code-editor/monaco-editor.tsx
import { Editor } from '@monaco-editor/react';
import { Card } from '../ui/card';

interface MonacoEditorProps {
    value: string;
    language: string;
    onChange?: (value: string) => void;
    readOnly?: boolean;
    height?: string;
}

export function MonacoEditor({
                                 value,
                                 language,
                                 onChange,
                                 readOnly = false,
                                 height = '400px'
                             }: MonacoEditorProps) {
    return (
        <Card className="overflow-hidden">
            <Editor
                height={height}
                language={language}
                value={value}
                onChange={(val) => onChange?.(val || '')}
                theme="vs-dark"
                options={{
                    readOnly,
                    minimap: { enabled: false },
                    fontSize: 14,
                    lineNumbers: 'on',
                    roundedSelection: false,
                    scrollBeyondLastLine: false,
                    automaticLayout: true,
                }}
            />
        </Card>
    );
}