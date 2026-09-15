import { toast } from 'sonner'
import { ApiError } from '@/lib/api'

/**
 * Единый разбор ошибок для действий пользователя. 401 молчит —
 * там уже сработал редирект на вход.
 */
export function showApiError(error: unknown, fallback = 'Не удалось выполнить действие') {
  if (error instanceof ApiError) {
    if (error.status === 401) return
    if (error.status === 403) {
      toast.error('Недостаточно прав', { description: 'Действие доступно только администратору.' })
      return
    }
    toast.error(error.detail ?? error.title ?? fallback)
    return
  }
  toast.error(fallback, { description: 'Проверьте соединение с сервером.' })
}
