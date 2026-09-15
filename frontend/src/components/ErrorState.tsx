import { TriangleAlert } from 'lucide-react'
import { ApiError } from '@/lib/api'
import { Button } from '@/components/ui/button'
import { EmptyState } from '@/components/EmptyState'

export function ErrorState({ error, onRetry }: { error: unknown; onRetry?: () => void }) {
  const message =
    error instanceof ApiError
      ? (error.detail ?? error.title ?? `Ошибка ${error.status}`)
      : 'Не удалось связаться с сервером'

  return (
    <EmptyState
      icon={TriangleAlert}
      title="Не удалось загрузить данные"
      description={message}
      action={
        onRetry ? (
          <Button variant="outline" size="sm" onClick={onRetry}>
            Повторить
          </Button>
        ) : null
      }
    />
  )
}
