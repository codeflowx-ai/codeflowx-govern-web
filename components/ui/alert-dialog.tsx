'use client'

import React, { useEffect } from 'react'
import { cn } from '@/lib/utils'

interface AlertDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  children: React.ReactNode
}

interface AlertDialogContentProps {
  children: React.ReactNode
  className?: string
}

interface AlertDialogHeaderProps {
  children: React.ReactNode
  className?: string
}

interface AlertDialogTitleProps {
  children: React.ReactNode
  className?: string
}

interface AlertDialogDescriptionProps {
  children: React.ReactNode
  className?: string
}

interface AlertDialogFooterProps {
  children: React.ReactNode
  className?: string
}

interface AlertDialogActionProps {
  children: React.ReactNode
  onClick?: () => void
  className?: string
}

interface AlertDialogCancelProps {
  children: React.ReactNode
  onClick?: () => void
  className?: string
}

const AlertDialogContext = React.createContext<{
  open: boolean
  onOpenChange: (open: boolean) => void
}>({
  open: false,
  onOpenChange: () => {}
})

export function AlertDialog({ open, onOpenChange, children }: AlertDialogProps) {
  return (
    <AlertDialogContext.Provider value={{ open, onOpenChange }}>
      {children}
    </AlertDialogContext.Provider>
  )
}

export function AlertDialogContent({ children, className = '' }: AlertDialogContentProps) {
  const { open, onOpenChange } = React.useContext(AlertDialogContext)

  useEffect(() => {
    if (open) {
      document.body.style.overflow = 'hidden'
    } else {
      document.body.style.overflow = 'unset'
    }

    return () => {
      document.body.style.overflow = 'unset'
    }
  }, [open])

  if (!open) return null

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 bg-black/50 z-50"
        onClick={() => onOpenChange(false)}
      />
      {/* Dialog Content */}
      <div
        className="fixed inset-0 z-50 flex items-center justify-center"
        onClick={(e) => {
          if (e.target === e.currentTarget) {
            onOpenChange(false)
          }
        }}
      >
        <div className={cn(
          "bg-background border border-border rounded-lg shadow-lg p-6 max-w-md w-full mx-4 z-50",
          className
        )}>
          {children}
        </div>
      </div>
    </>
  )
}

export function AlertDialogHeader({ children, className = '' }: AlertDialogHeaderProps) {
  return (
    <div className={cn("flex flex-col space-y-2 text-center sm:text-left", className)}>
      {children}
    </div>
  )
}

export function AlertDialogTitle({ children, className = '' }: AlertDialogTitleProps) {
  return (
    <h2 className={cn("text-lg font-semibold text-foreground", className)}>
      {children}
    </h2>
  )
}

export function AlertDialogDescription({ children, className = '' }: AlertDialogDescriptionProps) {
  return (
    <p className={cn("text-sm text-muted-foreground", className)}>
      {children}
    </p>
  )
}

export function AlertDialogFooter({ children, className = '' }: AlertDialogFooterProps) {
  return (
    <div className={cn("flex flex-col-reverse sm:flex-row sm:justify-end sm:space-x-2 mt-4", className)}>
      {children}
    </div>
  )
}

export function AlertDialogAction({ children, onClick, className = '' }: AlertDialogActionProps) {
  const { onOpenChange } = React.useContext(AlertDialogContext)

  const handleClick = () => {
    if (onClick) {
      onClick()
    }
    onOpenChange(false)
  }

  return (
    <button
      type="button"
      className={cn(
        "inline-flex items-center justify-center rounded-md bg-primary text-primary-foreground px-4 py-2 text-sm font-medium hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2",
        className
      )}
      onClick={handleClick}
    >
      {children}
    </button>
  )
}

export function AlertDialogCancel({ children, onClick, className = '' }: AlertDialogCancelProps) {
  const { onOpenChange } = React.useContext(AlertDialogContext)

  const handleClick = () => {
    if (onClick) {
      onClick()
    }
    onOpenChange(false)
  }

  return (
    <button
      type="button"
      className={cn(
        "inline-flex items-center justify-center rounded-md border border-input bg-background px-4 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 sm:mt-0",
        className
      )}
      onClick={handleClick}
    >
      {children}
    </button>
  )
}
