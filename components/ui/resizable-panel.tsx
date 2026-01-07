'use client';

import React, { useState, useRef, useEffect } from 'react';

interface ResizablePanelProps {
    children: React.ReactNode;
    minWidth?: number;
    maxWidth?: number;
    defaultWidth?: number;
    onResize?: (width: number) => void;
    className?: string;
}

export function ResizablePanel({ 
    children, 
    minWidth = 200, 
    maxWidth = 800, 
    defaultWidth = 300,
    onResize,
    className = ''
}: ResizablePanelProps) {
    const [width, setWidth] = useState(defaultWidth);
    const [isResizing, setIsResizing] = useState(false);
    const startX = useRef(0);
    const startWidth = useRef(0);

    const handleMouseDown = (e: React.MouseEvent) => {
        setIsResizing(true);
        startX.current = e.clientX;
        startWidth.current = width;
        document.body.style.cursor = 'col-resize';
        document.body.style.userSelect = 'none';
    };

    const handleMouseMove = (e: MouseEvent) => {
        if (!isResizing) return;

        const deltaX = e.clientX - startX.current;
        const newWidth = Math.max(minWidth, Math.min(maxWidth, startWidth.current + deltaX));
        
        setWidth(newWidth);
        onResize?.(newWidth);
    };

    const handleMouseUp = () => {
        setIsResizing(false);
        document.body.style.cursor = '';
        document.body.style.userSelect = '';
    };

    useEffect(() => {
        if (isResizing) {
            document.addEventListener('mousemove', handleMouseMove);
            document.addEventListener('mouseup', handleMouseUp);
        }

        return () => {
            document.removeEventListener('mousemove', handleMouseMove);
            document.removeEventListener('mouseup', handleMouseUp);
        };
    }, [isResizing]);

    return (
        <div 
            className={`relative ${className}`}
            style={{ width: `${width}px` }}
        >
            {children}
            <div
                className="absolute right-0 top-0 bottom-0 w-1 bg-gray-300 hover:bg-blue-500 cursor-col-resize transition-colors"
                onMouseDown={handleMouseDown}
            />
        </div>
    );
}

interface SplitterLayoutProps {
    leftPanel: React.ReactNode;
    rightPanel: React.ReactNode;
    leftMinWidth?: number;
    leftMaxWidth?: number;
    leftDefaultWidth?: number;
    onLeftResize?: (width: number) => void;
    className?: string;
}

export function SplitterLayout({
    leftPanel,
    rightPanel,
    leftMinWidth = 200,
    leftMaxWidth = 600,
    leftDefaultWidth = 300,
    onLeftResize,
    className = ''
}: SplitterLayoutProps) {
    return (
        <div className={`flex h-full ${className}`}>
            <ResizablePanel
                minWidth={leftMinWidth}
                maxWidth={leftMaxWidth}
                defaultWidth={leftDefaultWidth}
                onResize={onLeftResize}
                className="border-r border-gray-200"
            >
                {leftPanel}
            </ResizablePanel>
            <div className="flex-1">
                {rightPanel}
            </div>
        </div>
    );
}


