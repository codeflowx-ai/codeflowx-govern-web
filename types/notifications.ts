// types/notifications.ts
export interface Notification {
    id: string
    type: 'system' | 'admin' | 'billing' | 'security' | 'feature' | 'maintenance'
    priority: 'low' | 'medium' | 'high' | 'critical'
    title: string
    message: string
    sender: {
        type: 'leka' | 'admin' | 'system'
        name: string
        avatar?: string
    }
    createdAt: string
    readAt?: string
    expiresAt?: string
    actions?: NotificationAction[]
    metadata?: {
        planId?: string
        userId?: string
        tenantId?: string
        url?: string
    }
}

export interface NotificationAction {
    id: string
    label: string
    type: 'primary' | 'secondary' | 'danger'
    url?: string
    action?: string
}