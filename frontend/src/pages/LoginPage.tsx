import { useState } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { AuthLayout } from '@/components/AuthLayout'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ApiError } from '@/lib/api'
import { useAuth } from '@/lib/auth'

export function LoginPage() {
  const { login, status } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [pending, setPending] = useState(false)

  if (status === 'authenticated') {
    return <Navigate to="/" replace />
  }

  const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname ?? '/'

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    setError(null)
    setPending(true)
    try {
      await login(username.trim(), password)
      navigate(from, { replace: true })
    } catch (cause) {
      setError(messageFor(cause))
    } finally {
      setPending(false)
    }
  }

  return (
    <AuthLayout
      title="Вход"
      description="Введите логин и пароль, выданные администратором."
      footer={
        <>
          Нет учётной записи?{' '}
          <Link to="/register" className="font-medium text-foreground underline underline-offset-4">
            Зарегистрироваться
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-1.5">
          <Label htmlFor="login-username">Логин</Label>
          <Input
            id="login-username"
            autoComplete="username"
            autoFocus
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            aria-invalid={Boolean(error)}
          />
        </div>
        <div className="space-y-1.5">
          <Label htmlFor="login-password">Пароль</Label>
          <Input
            id="login-password"
            type="password"
            autoComplete="current-password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            aria-invalid={Boolean(error)}
          />
        </div>
        {error ? (
          <p role="alert" className="text-sm text-destructive">
            {error}
          </p>
        ) : null}
        <Button type="submit" className="w-full" disabled={pending || !username.trim() || !password}>
          {pending ? 'Входим…' : 'Войти'}
        </Button>
      </form>
    </AuthLayout>
  )
}

function messageFor(cause: unknown): string {
  if (cause instanceof ApiError) {
    if (cause.status === 401 || cause.status === 403) return 'Неверный логин или пароль'
    if (cause.status === 404) return 'Сервис авторизации недоступен'
    return cause.detail ?? cause.title ?? `Ошибка ${cause.status}`
  }
  return 'Не удалось связаться с сервером'
}
