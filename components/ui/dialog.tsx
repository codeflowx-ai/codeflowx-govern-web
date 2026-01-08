// components/ui/Dialog.tsx
'use client'

import React, { useEffect } from 'react'

interface DialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  children: React.ReactNode
}

interface DialogTriggerProps {
  children: React.ReactNode
  asChild?: boolean
}

interface DialogContentProps {
  children: React.ReactNode
  className?: string
  maxWidth?: string
}

interface DialogHeaderProps {
  children: React.ReactNode
  className?: string
}

interface DialogTitleProps {
  children: React.ReactNode
  className?: string
}

interface DialogDescriptionProps {
  children: React.ReactNode
  className?: string
}

const DialogContext = React.createContext<{
  open: boolean
  onOpenChange: (open: boolean) => void
}>({
  open: false,
  onOpenChange: () => {}
})

export function Dialog({ open, onOpenChange, children }: DialogProps) {
  return (
      <DialogContext.Provider value={{ open, onOpenChange }}>
        {children}
        {open && <div className="modal-backdrop fade show"></div>}
      </DialogContext.Provider>
  )
}

export function DialogTrigger({ children }: DialogTriggerProps) {
  return <>{children}</>
}

export function DialogContent({ children, className = '', maxWidth = '900px' }: DialogContentProps) {
  const { open, onOpenChange } = React.useContext(DialogContext)

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
      <div
          className="modal fade show d-block"
          tabIndex={-1}
          style={{ zIndex: 1055 }}
          onClick={(e) => {
            if (e.target === e.currentTarget) {
              onOpenChange(false)
            }
          }}
      >
      <div className="modal-dialog modal-dialog-centered modal-lg" style={{ maxWidth }}>
        <div className={`modal-content ${className}`} style={{ backgroundColor: 'var(--theme-background)', color: 'var(--theme-text)' }}>
            {children}
          </div>
        </div>
      </div>
  )
}

export function DialogHeader({ children, className = '' }: DialogHeaderProps) {
  return (
      <div className={`modal-header ${className}`}>
        {children}
      </div>
  )
}

export function DialogTitle({ children, className = '' }: DialogTitleProps) {
  return (
      <h5 className={`modal-title ${className}`}>
        {children}
      </h5>
  )
}

export function DialogDescription({ children, className = '' }: DialogDescriptionProps) {
  return (
      <p className={`text-muted-foreground ${className}`} style={{ display: 'block', marginTop: '0.5rem', opacity: 1, visibility: 'visible' }}>
        {children}
      </p>
  )
}

interface DialogFooterProps {
  children: React.ReactNode
  className?: string
}

export function DialogFooter({ children, className = '' }: DialogFooterProps) {
  return (
      <div className={`modal-footer ${className}`}>
        {children}
      </div>
  )
}
