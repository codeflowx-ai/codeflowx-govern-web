// components/bpmn/AssignTaskDialog.tsx - Modal con SimpleModal y Wow Factor
'use client'

import { BPMNTask, BPMNUser } from '@/types/bpmn'
import { useTranslation } from '@/app/config/i18n'
import { SimpleModal } from '@/components/ui/SimpleModal'
import { Button } from '@/components/ui/button'
import { Table, TableHeader, TableBody, TableRow, TableCell } from '@/components/ui/table'
import { useState } from 'react'

interface AssignTaskDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  task: BPMNTask | null
  users: BPMNUser[]
  onAssign: (userId: string) => void
}

export function AssignTaskDialog({
  open,
  onOpenChange,
  task,
  users,
  onAssign
}: AssignTaskDialogProps) {
  const { t } = useTranslation()
  const [selectedUser, setSelectedUser] = useState<BPMNUser | null>(null)

  const handleAssign = () => {
    if (selectedUser) {
      onAssign(selectedUser.username)
      setSelectedUser(null)
    }
  }

  return (
    <SimpleModal
      isOpen={open}
      onClose={() => {
        onOpenChange(false)
        setSelectedUser(null)
      }}
      title={t('taskInbox.assignDialog.title', 'Asignar Tarea a Usuario')}
      maxWidth="max-w-3xl"
    >
      <div className="space-y-4">
        {task && (
          <div className="p-4 rounded-lg bg-muted/50 border border-border">
            <div className="font-medium">{t('taskInbox.assignDialog.task', 'Tarea')}: {task.name}</div>
            {task.description && (
              <div className="text-sm text-muted-foreground mt-1">{task.description}</div>
            )}
          </div>
        )}

        <div className="text-sm text-muted-foreground">
          <strong>{t('taskInbox.assignDialog.availableUsers', 'Usuarios disponibles')}:</strong> {users.length}
        </div>

        <div style={{ maxHeight: '300px', overflowY: 'auto' }} className="border border-border rounded-lg">
          <Table hover>
            <TableHeader>
              <TableRow>
                <TableCell header scope="col" style={{ width: '120px' }}>
                  {t('taskInbox.assignDialog.username', 'Usuario')}
                </TableCell>
                <TableCell header scope="col">
                  {t('taskInbox.assignDialog.fullname', 'Nombre Completo')}
                </TableCell>
                <TableCell header scope="col">
                  {t('taskInbox.assignDialog.email', 'Email')}
                </TableCell>
              </TableRow>
            </TableHeader>
            <TableBody>
              {users.map((user) => (
                <TableRow
                  key={user.username}
                  onClick={() => setSelectedUser(user)}
                  style={{
                    cursor: 'pointer',
                    backgroundColor: selectedUser?.username === user.username ? 'rgba(59, 130, 246, 0.1)' : undefined
                  }}
                  className="hover:bg-muted/50 transition-colors duration-200"
                >
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.fullname}</TableCell>
                  <TableCell>{user.email}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>

        <div className="flex gap-2 justify-end pt-4 border-t border-border">
          <Button
            variant="outline"
            onClick={() => {
              onOpenChange(false)
              setSelectedUser(null)
            }}
          >
            {t('taskInbox.assignDialog.cancel', 'Cancelar')}
          </Button>
          <Button
            onClick={handleAssign}
            disabled={!selectedUser}
            className="hover:scale-105 transition-transform duration-200"
          >
            {t('taskInbox.assignDialog.assign', 'Asignar')}
          </Button>
        </div>
      </div>
    </SimpleModal>
  )
}
