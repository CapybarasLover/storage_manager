import { useState } from 'react'
import { toast } from 'sonner'
import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useCreateStorage, useStorages } from '@/hooks/useStorages'
import { ApiError } from '@/lib/api'
import { LIMITS } from '@/lib/constants'

export function CreateStorageDialog({ open, onOpenChange }: { open: boolean; onOpenChange: (open: boolean) => void }) {
  const [name, setName] = useState('')
  const [error, setError] = useState<string | null>(null)
  const createStorage = useCreateStorage()
  const { refetch } = useStorages()
  const navigate = useNavigate()

  const trimmed = name.trim()
  const tooLong = trimmed.length > LIMITS.storageName

  function close(next: boolean) {
    if (!next) {
      setName('')
      setError(null)
    }
    onOpenChange(next)
  }

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (!trimmed || tooLong) return
    setError(null)
    try {
      await createStorage.mutateAsync(trimmed)
      // Свежесозданный склад нужно сразу открыть, поэтому дожидаемся списка.
      const { data } = await refetch()
      const created = data?.find((storage) => storage.name === trimmed)
      toast.success(`Склад «${trimmed}» создан`)
      close(false)
      if (created) navigate(`/storages/${created.id}/items`)
    } catch (cause) {
      const message =
        cause instanceof ApiError
          ? (cause.fieldError('name') ?? cause.detail ?? cause.title ?? 'Не удалось создать склад')
          : 'Не удалось создать склад'
      setError(message)
    }
  }

  return (
    <Dialog open={open} onOpenChange={close}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Новый склад</DialogTitle>
          <DialogDescription>Название видно в журнале операций и в отчётах.</DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-1.5">
            <div className="flex items-baseline justify-between">
              <Label htmlFor="storage-name">Название</Label>
              <span className="text-xs text-muted-foreground tabular">
                {trimmed.length}/{LIMITS.storageName}
              </span>
            </div>
            <Input
              id="storage-name"
              autoFocus
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Основной склад"
              aria-invalid={Boolean(error) || tooLong}
            />
            {tooLong ? (
              <p className="text-xs text-destructive">Не длиннее {LIMITS.storageName} символов</p>
            ) : null}
            {error ? (
              <p role="alert" className="text-xs text-destructive">
                {error}
              </p>
            ) : null}
          </div>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => close(false)}>
              Отмена
            </Button>
            <Button type="submit" disabled={!trimmed || tooLong || createStorage.isPending}>
              {createStorage.isPending ? 'Создаём…' : 'Создать'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
