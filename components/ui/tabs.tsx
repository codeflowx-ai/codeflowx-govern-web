// components/ui/Tabs.tsx
'use client'

import React, { useState, createContext, useContext } from 'react'
import { cn } from '@/lib/utils'

interface TabsContextType {
  activeTab: string
  setActiveTab: (tab: string) => void
}

const TabsContext = createContext<TabsContextType | undefined>(undefined)

interface TabsProps {
  defaultValue: string
  children: React.ReactNode
  className?: string
  value?: string
  onValueChange?: (value: string) => void
}

interface TabsListProps {
  children: React.ReactNode
  className?: string
}

interface TabsTriggerProps {
  value: string
  children: React.ReactNode
  disabled?: boolean
  className?: string
}

interface TabsContentProps {
  value: string
  children: React.ReactNode
  className?: string
}

export function Tabs({ defaultValue, children, className = '', value: controlledValue, onValueChange }: TabsProps) {
  const [internalValue, setInternalValue] = useState(defaultValue)

  // Usar valor controlado si se proporciona, sino usar estado interno
  const activeTab = controlledValue !== undefined ? controlledValue : internalValue
  const setActiveTab = (tab: string) => {
    if (onValueChange) {
      onValueChange(tab)
    } else {
      setInternalValue(tab)
    }
  }

  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      <div className={className}>
        {children}
      </div>
    </TabsContext.Provider>
  )
}

export function TabsList({ children, className = '' }: TabsListProps) {
  return (
    <div
      className={cn(
        "inline-flex h-10 items-center justify-center rounded-md bg-muted p-1 text-muted-foreground gap-1",
        className
      )}
      role="tablist"
    >
      {children}
    </div>
  )
}

export function TabsTrigger({ value, children, disabled = false, className = '' }: TabsTriggerProps) {
  const context = useContext(TabsContext)
  if (!context) throw new Error('TabsTrigger must be used within Tabs')

  const { activeTab, setActiveTab } = context
  const isActive = activeTab === value

  return (
    <button
      className={cn(
        "inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 relative",
        isActive
          ? "bg-background text-foreground shadow-sm font-semibold before:absolute before:bottom-0 before:left-0 before:right-0 before:h-0.5 before:bg-[var(--theme-primary,#3b82f6)] before:rounded-t"
          : "text-muted-foreground hover:text-foreground hover:bg-background/50",
        className
      )}
      type="button"
      role="tab"
      aria-selected={isActive}
      disabled={disabled}
      onClick={() => !disabled && setActiveTab(value)}
      data-state={isActive ? 'active' : 'inactive'}
    >
      {children}
    </button>
  )
}

export function TabsContent({ value, children, className = '' }: TabsContentProps) {
  const context = useContext(TabsContext)
  if (!context) throw new Error('TabsContent must be used within Tabs')

  const { activeTab } = context

  if (activeTab !== value) return null

  return (
    <div
      className={cn(
        "mt-2 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
        className
      )}
      role="tabpanel"
      data-state="active"
    >
      {children}
    </div>
  )
}
