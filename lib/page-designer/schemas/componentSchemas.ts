export interface PropertySchema {
    type: 'text' | 'number' | 'boolean' | 'select' | 'color' | 'textarea' | 'range';
    label: string;
    defaultValue: any;
    options?: Array<{ value: any; label: string }>;
    min?: number;
    max?: number;
    step?: number;
    placeholder?: string;
    validation?: {
        required?: boolean;
        pattern?: string;
        min?: number;
        max?: number;
    };
}

export interface ComponentSchema {
    type: string;
    name: string;
    category: string;
    properties: Record<string, PropertySchema>;
}

export const componentSchemas: Record<string, ComponentSchema> = {
    input: {
        type: 'input',
        name: 'Text Input',
        category: 'basic',
        properties: {
            label: {
                type: 'text',
                label: 'Label',
                defaultValue: 'Input Label',
                placeholder: 'Enter label text'
            },
            placeholder: {
                type: 'text',
                label: 'Placeholder',
                defaultValue: 'Enter text...',
                placeholder: 'Placeholder text'
            },
            required: {
                type: 'boolean',
                label: 'Required',
                defaultValue: false
            },
            disabled: {
                type: 'boolean',
                label: 'Disabled',
                defaultValue: false
            },
            maxLength: {
                type: 'number',
                label: 'Max Length',
                defaultValue: 100,
                min: 1,
                max: 1000
            },
            inputType: {
                type: 'select',
                label: 'Input Type',
                defaultValue: 'text',
                options: [
                    { value: 'text', label: 'Text' },
                    { value: 'email', label: 'Email' },
                    { value: 'password', label: 'Password' },
                    { value: 'number', label: 'Number' },
                    { value: 'tel', label: 'Phone' }
                ]
            }
        }
    },
    button: {
        type: 'button',
        name: 'Button',
        category: 'basic',
        properties: {
            text: {
                type: 'text',
                label: 'Button Text',
                defaultValue: 'Click me',
                placeholder: 'Button text'
            },
            variant: {
                type: 'select',
                label: 'Variant',
                defaultValue: 'primary',
                options: [
                    { value: 'primary', label: 'Primary' },
                    { value: 'secondary', label: 'Secondary' },
                    { value: 'outline', label: 'Outline' },
                    { value: 'ghost', label: 'Ghost' },
                    { value: 'danger', label: 'Danger' }
                ]
            },
            disabled: {
                type: 'boolean',
                label: 'Disabled',
                defaultValue: false
            }
        }
    }
};