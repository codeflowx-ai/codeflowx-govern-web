// components/layout/NotificationCenter.tsx
'use client'

import { useState, useEffect } from 'react'
import { Notification } from '@/types/notifications'
import { useTranslation } from '@/app/config/i18n'

// Mock notifications
const mockNotifications: Notification[] = [
    {
        id: '1',
        type: 'system',
        priority: 'high',
        title: 'Nueva versión disponible',
        message: 'Leka Server v2.1.0 está disponible con mejoras de rendimiento y nuevos modelos IA.',
        sender: {
            type: 'leka',
            name: 'Leka Team',
            avatar: '🚀'
        },
        createdAt: '2024-01-15T10:30:00Z',
        actions: [
            {
                id: 'update',
                label: 'Ver Changelog',
                type: 'primary',
                url: '/updates' // ← Enlace a la página de updates
            }
        ]
    },
    {
        id: '2',
        type: 'billing',
        priority: 'medium',
        title: 'Factura generada',
        message: 'Tu factura de enero por 319€ ha sido generada y enviada por email.',
        sender: {
            type: 'system',
            name: 'Sistema de Facturación'
        },
        createdAt: '2024-01-01T09:00:00Z',
        actions: [
            {
                id: 'view-invoice',
                label: 'Ver Factura',
                type: 'primary',
                url: '/config/invoices'
            }
        ]
    },
    {
        id: '3',
        type: 'admin',
        priority: 'medium',
        title: 'Nuevo usuario añadido',
        message: 'El administrador ha añadido a María García al equipo con rol de desarrolladora.',
        sender: {
            type: 'admin',
            name: 'Admin User',
            avatar: '👤'
        },
        createdAt: '2024-01-14T15:45:00Z'
    },
    {
        id: '4',
        type: 'feature',
        priority: 'low',
        title: 'Nuevo modelo disponible',
        message: 'Hemos añadido soporte para Vue 3 Composition API en el playground avanzado.',
        sender: {
            type: 'leka',
            name: 'Leka Product Team',
            avatar: '✨'
        },
        createdAt: '2024-01-13T12:00:00Z',
        actions: [
            {
                id: 'try-feature',
                label: 'Probar Ahora',
                type: 'primary',
                url: '/code-playground'
            }
        ]
    },
    {
        id: '5',
        type: 'security',
        priority: 'critical',
        title: 'Nuevo inicio de sesión detectado',
        message: 'Se ha detectado un inicio de sesión desde Madrid, España. Si no fuiste tú, revisa tu cuenta.',
        sender: {
            type: 'system',
            name: 'Sistema de Seguridad'
        },
        createdAt: '2024-01-15T08:20:00Z',
        actions: [
            {
                id: 'review-security',
                label: 'Revisar Actividad',
                type: 'danger',
                url: '/config/security'
            }
        ]
    },
    {
        id: '6',
        type: 'maintenance',
        priority: 'medium',
        title: 'Mantenimiento programado',
        message: 'Realizaremos mantenimiento el sábado 20 de enero de 02:00 a 04:00 CET.',
        sender: {
            type: 'leka',
            name: 'Leka Operations',
            avatar: '🔧'
        },
        createdAt: '2024-01-12T10:00:00Z',
        expiresAt: '2024-01-20T04:00:00Z'
    }
]

export function NotificationCenter() {
    const { t, language } = useTranslation()
    const [notifications, setNotifications] = useState<Notification[]>(mockNotifications)
    const [showDropdown, setShowDropdown] = useState(false)
    const [filter, setFilter] = useState<'all' | 'unread'>('all')

    const unreadCount = notifications.filter(n => !n.readAt).length

    const markAsRead = (notificationId: string) => {
        setNotifications(prev => prev.map(notification =>
            notification.id === notificationId
                ? { ...notification, readAt: new Date().toISOString() }
                : notification
        ))
    }

    const markAllAsRead = () => {
        setNotifications(prev => prev.map(notification => ({
            ...notification,
            readAt: notification.readAt || new Date().toISOString()
        })))
    }

    const deleteNotification = (notificationId: string) => {
        setNotifications(prev => prev.filter(n => n.id !== notificationId))
    }

    const getPriorityColor = (priority: string) => {
        switch (priority) {
            case 'critical': return 'text-red-500'
            case 'high': return 'text-orange-500'
            case 'medium': return 'text-blue-500'
            case 'low': return 'text-gray-500'
            default: return 'text-gray-500'
        }
    }

    const getPriorityLabel = (priority: string) => {
        return t(`layout.notifications.priority.${priority}`, priority)
    }

    const getTypeIcon = (type: string) => {
        switch (type) {
            case 'system': return '⚙️'
            case 'admin': return '👤'
            case 'billing': return '💳'
            case 'security': return '🔒'
            case 'feature': return '✨'
            case 'maintenance': return '🔧'
            default: return '📢'
        }
    }

    const filteredNotifications = filter === 'unread'
        ? notifications.filter(n => !n.readAt)
        : notifications

    const sortedNotifications = filteredNotifications.sort((a, b) =>
        new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
    )

    return (
        <div className="relative">
            <button
                onClick={() => setShowDropdown(!showDropdown)}
                className="relative p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
            >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-5 5v-5zM11 19H6a2 2 0 01-2-2V7a2 2 0 012-2h5m5 0v5" />
                </svg>
                {unreadCount > 0 && (
                    <span 
                        className="absolute -top-1 -right-1 text-xs rounded-full w-5 h-5 flex items-center justify-center"
                        style={{
                            backgroundColor: "var(--theme-error)",
                            color: "#ffffff",
                        }}
                    >
            {unreadCount > 9 ? '9+' : unreadCount}
          </span>
                )}
            </button>

            {showDropdown && (
                <div
                    className="absolute right-0 mt-2 w-96 rounded-lg shadow-lg border z-50 max-h-96 overflow-hidden"
                    style={{
                        backgroundColor: "var(--theme-popover)",
                        borderColor: "var(--theme-border)",
                        boxShadow: "var(--theme-shadow)",
                    }}
                >
                    {/* Header */}
                    <div
                        className="p-4 border-b"
                        style={{
                            borderBottomColor: "var(--theme-border)",
                        }}
                    >
                        <div className="flex items-center justify-between mb-3">
                            <h3
                                className="text-lg font-semibold"
                                style={{ color: "var(--theme-text)" }}
                            >
                                {t("layout.notifications.title")}
                            </h3>
                            <button
                                onClick={() => setShowDropdown(false)}
                                className="hover:opacity-70 transition-opacity"
                                style={{ color: "var(--theme-text-muted)" }}
                            >
                                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>

                        <div className="flex items-center justify-between">
                            <div className="flex gap-2">
                                <button
                                    onClick={() => setFilter('all')}
                                    className="px-3 py-1 text-sm rounded-full transition-colors"
                                    style={{
                                        backgroundColor: filter === 'all' ? "var(--theme-primary)" : "transparent",
                                        color: filter === 'all' ? "#ffffff" : "var(--theme-text-secondary)",
                                    }}
                                    onMouseEnter={(e) => {
                                        if (filter !== 'all') {
                                            e.currentTarget.style.backgroundColor = "var(--theme-hover)";
                                        }
                                    }}
                                    onMouseLeave={(e) => {
                                        if (filter !== 'all') {
                                            e.currentTarget.style.backgroundColor = "transparent";
                                        }
                                    }}
                                >
                                    {t("layout.notifications.all")} ({notifications.length})
                                </button>
                                <button
                                    onClick={() => setFilter('unread')}
                                    className="px-3 py-1 text-sm rounded-full transition-colors"
                                    style={{
                                        backgroundColor: filter === 'unread' ? "var(--theme-primary)" : "transparent",
                                        color: filter === 'unread' ? "#ffffff" : "var(--theme-text-secondary)",
                                    }}
                                    onMouseEnter={(e) => {
                                        if (filter !== 'unread') {
                                            e.currentTarget.style.backgroundColor = "var(--theme-hover)";
                                        }
                                    }}
                                    onMouseLeave={(e) => {
                                        if (filter !== 'unread') {
                                            e.currentTarget.style.backgroundColor = "transparent";
                                        }
                                    }}
                                >
                                    {t("layout.notifications.unread")} ({unreadCount})
                                </button>
                            </div>

                            {unreadCount > 0 && (
                                <button
                                    onClick={markAllAsRead}
                                    className="text-sm transition-opacity hover:opacity-70"
                                    style={{ color: "var(--theme-primary)" }}
                                >
                                    {t("layout.notifications.markAllAsRead")}
                                </button>
                            )}
                        </div>
                    </div>

                    {/* Notifications List */}
                    <div className="max-h-80 overflow-y-auto">
                        {sortedNotifications.length === 0 ? (
                            <div className="p-8 text-center" style={{ color: "var(--theme-text-muted)" }}>
                                <div className="text-4xl mb-2">📭</div>
                                <p>{t("layout.notifications.noNotifications")}</p>
                            </div>
                        ) : (
                            sortedNotifications.map((notification) => (
                                <div
                                    key={notification.id}
                                    className="p-4 border-b transition-colors"
                                    style={{
                                        borderBottomColor: "var(--theme-border-light)",
                                        backgroundColor: !notification.readAt ? "var(--theme-hover-light)" : "transparent",
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.backgroundColor = "var(--theme-hover)";
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.backgroundColor = !notification.readAt ? "var(--theme-hover-light)" : "transparent";
                                    }}
                                >
                                    <div className="flex items-start gap-3">
                                        <div className="flex-shrink-0">
                                            {notification.sender.avatar ? (
                                                <span className="text-2xl">{notification.sender.avatar}</span>
                                            ) : (
                                                <span className="text-xl">{getTypeIcon(notification.type)}</span>
                                            )}
                                        </div>

                                        <div className="flex-1 min-w-0">
                                            <div className="flex items-start justify-between">
                                                <div className="flex-1">
                                                    <div className="flex items-center gap-2 mb-1">
                                                        <h4
                                                            className="text-sm font-medium truncate"
                                                            style={{ color: "var(--theme-text)" }}
                                                        >
                                                            {notification.title}
                                                        </h4>
                                                        {!notification.readAt && (
                                                            <div
                                                                className="w-2 h-2 rounded-full flex-shrink-0"
                                                                style={{ backgroundColor: "var(--theme-primary)" }}
                                                            ></div>
                                                        )}
                                                    </div>
                                                    <p
                                                        className="text-sm mb-2"
                                                        style={{ color: "var(--theme-text-secondary)" }}
                                                    >
                                                        {notification.message}
                                                    </p>
                                                    <div
                                                        className="flex items-center gap-2 text-xs"
                                                        style={{ color: "var(--theme-text-muted)" }}
                                                    >
                                                        <span>{notification.sender.name}</span>
                                                        <span>•</span>
                                                        <span>{new Date(notification.createdAt).toLocaleDateString()}</span>
                                                        <span
                                                            className={`${getPriorityColor(notification.priority)} font-medium`}
                                                        >
                                                            {getPriorityLabel(notification.priority)}
                                                        </span>
                                                    </div>
                                                </div>

                                                <div className="flex items-center gap-1 ml-2">
                                                    {!notification.readAt && (
                                                        <button
                                                            onClick={() => markAsRead(notification.id)}
                                                            className="p-1 transition-colors hover:opacity-70"
                                                            style={{ color: "var(--theme-text-muted)" }}
                                                            title={t("layout.notifications.markAsRead")}
                                                            onMouseEnter={(e) => {
                                                                e.currentTarget.style.color = "var(--theme-primary)";
                                                            }}
                                                            onMouseLeave={(e) => {
                                                                e.currentTarget.style.color = "var(--theme-text-muted)";
                                                            }}
                                                        >
                                                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                                            </svg>
                                                        </button>
                                                    )}
                                                    <button
                                                        onClick={() => deleteNotification(notification.id)}
                                                        className="p-1 transition-colors hover:opacity-70"
                                                        style={{ color: "var(--theme-text-muted)" }}
                                                        title={t("layout.notifications.delete")}
                                                        onMouseEnter={(e) => {
                                                            e.currentTarget.style.color = "var(--theme-error)";
                                                        }}
                                                        onMouseLeave={(e) => {
                                                            e.currentTarget.style.color = "var(--theme-text-muted)";
                                                        }}
                                                    >
                                                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                        </svg>
                                                    </button>
                                                </div>
                                            </div>

                                            {notification.actions && notification.actions.length > 0 && (
                                                <div className="flex gap-2 mt-3">
                                                    {notification.actions.map((action) => (
                                                        <button
                                                            key={action.id}
                                                            onClick={() => {
                                                                if (action.url) {
                                                                    window.location.href = action.url
                                                                }
                                                                markAsRead(notification.id)
                                                            }}
                                                            className="px-3 py-1 text-xs rounded-full font-medium transition-colors"
                                                            style={{
                                                                backgroundColor: action.type === 'primary'
                                                                    ? "var(--theme-primary)"
                                                                    : action.type === 'danger'
                                                                        ? "var(--theme-error)"
                                                                        : "var(--theme-surface)",
                                                                color: action.type === 'primary' || action.type === 'danger'
                                                                    ? "#ffffff"
                                                                    : "var(--theme-text)",
                                                            }}
                                                            onMouseEnter={(e) => {
                                                                if (action.type === 'primary') {
                                                                    e.currentTarget.style.opacity = "0.9";
                                                                } else if (action.type === 'danger') {
                                                                    e.currentTarget.style.opacity = "0.9";
                                                                } else {
                                                                    e.currentTarget.style.backgroundColor = "var(--theme-hover)";
                                                                }
                                                            }}
                                                            onMouseLeave={(e) => {
                                                                if (action.type === 'primary') {
                                                                    e.currentTarget.style.opacity = "1";
                                                                } else if (action.type === 'danger') {
                                                                    e.currentTarget.style.opacity = "1";
                                                                } else {
                                                                    e.currentTarget.style.backgroundColor = "var(--theme-surface)";
                                                                }
                                                            }}
                                                        >
                                                            {action.label}
                                                        </button>
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>

                    {/* Footer */}
                    <div
                        className="p-3 border-t"
                        style={{
                            borderTopColor: "var(--theme-border)",
                            backgroundColor: "var(--theme-surface)",
                        }}
                    >
                        <button
                            onClick={() => {
                                setShowDropdown(false)
                                window.location.href = '/notifications'
                            }}
                            className="w-full text-sm font-medium transition-opacity hover:opacity-70"
                            style={{ color: "var(--theme-primary)" }}
                        >
                            {t("layout.notifications.viewAll")}
                        </button>
                    </div>
                </div>
            )}
        </div>
    )
}
