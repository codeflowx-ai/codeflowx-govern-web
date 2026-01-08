// components/features/tech-stack-selector.tsx
import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';
import { Button } from '../ui/button';
import { Badge } from '../ui/badge';
import { TechStack } from '@/types';

interface TechStackSelectorProps {
    value: TechStack;
    onChange: (stack: TechStack) => void;
}

export function TechStackSelector({ value, onChange }: TechStackSelectorProps) {
    const techOptions = {
        frontend: [
            { id: 'nextjs', name: 'Next.js', icon: '⚛️' },
            { id: 'react', name: 'React', icon: '⚛️' },
            { id: 'angular', name: 'Angular', icon: '🅰️' },
            { id: 'vue', name: 'Vue.js', icon: '💚' },
        ],
        backend: [
            { id: 'springboot', name: 'Spring Boot', icon: '🍃' },
            { id: 'nodejs', name: 'Node.js', icon: '💚' },
            { id: 'dotnet', name: '.NET', icon: '🔷' },
            { id: 'python', name: 'Python', icon: '🐍' },
        ],
        database: [
            { id: 'mysql', name: 'MySQL', icon: '🐬' },
            { id: 'postgresql', name: 'PostgreSQL', icon: '🐘' },
            { id: 'mongodb', name: 'MongoDB', icon: '🍃' },
            { id: 'oracle', name: 'Oracle', icon: '🔴' },
        ],
    };

    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {Object.entries(techOptions).map(([category, options]) => (
                <Card key={category}>
                    <CardHeader>
                        <CardTitle className="capitalize">{category}</CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-2">
                        {options.map((option) => (
                            <Button
                                key={option.id}
                                variant={value[category as keyof TechStack] === option.id ? 'primary' : 'outline'}
                                className="w-full justify-start"
                                onClick={() => onChange({ ...value, [category]: option.id })}
                            >
                                <span className="mr-2">{option.icon}</span>
                                {option.name}
                            </Button>
                        ))}
                    </CardContent>
                </Card>
            ))}
        </div>
    );
}
