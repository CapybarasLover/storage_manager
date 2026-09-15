import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { AuthLayout } from '@/components/AuthLayout'
import { RoleField } from '@/components/RoleField'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ApiError } from '@/lib/api'
import { useAuth } from '@/lib/auth'
import type { Role } from '@/types/api'

export function RegisterPage() {
  const { register, status } = useAuth()
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [confirmation, setConfirmation] = useState('')
  const [role, setRole] = useState<Role>('WORKER')
  const [error, setError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})
  const [pending, setPending] = useState(false)

  if (status === 'authenticated') {
    return <Navigate to="/" replace />
  }

  const mismatch = confirmation.length > 0 && password !== confirmation
  const canSubmit = Boolean(username.trim()) && Boolean(password) && password === confirmation && !pending

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (!canSubmit) return
    setError(null)
    setFieldErrors({})
    setPending(true)
    try {
      await register(username.trim(), password, role)
      navigate('/', { replace: true })
    } catch (cause) {
      if (cause instanceof ApiError && cause.errors?.length) {
        setFieldErrors(Object.fromEntries(cause.errors.map((item) => [item.field, item.message])))
      }
      setError(messageFor(cause))
    } finally {
      setPending(false)
    }
  }

  return (
    <AuthLayout
      title="Регистрация"
      description="Роль определяет, что можно делать со складом."
      footer={
        <>
          Уже есть учётная запись?{' '}
          <Link to="/login" className="font-medium text-foreground underline underline-offset-4">
            Войти
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="space-y-1.5">
          <Label htmlFor="register-username">Логин</Label>
          <Input
            id="register-username"
            autoComplete="username"
            autoFocus
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            aria-invalid={Boolean(fieldErrors.username)}
          />
          {fieldErrors.username ? <p className="text-xs text-destructive">{fieldErrors.username}</p> : null}
        </div>

        <div className="space-y-1.5">
          <Label htmlFor="register-password">Пароль</Label>
          <Input
            id="register-password"
            type="password"
            autoComplete="new-password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            aria-invalid={Boolean(fieldErrors.password)}
          />
          {fieldErrors.password ? <p className="text-xs text-destructive">{fieldErrors.password}</p> : null}
        </div>

        <div className="space-y-1.5">
          <Label htmlFor="register-confirmation">Повторите пароль</Label>
          <Input
            id="register-confirmation"
            type="password"
            autoComplete="new-password"
            value={confirmation}
            onChange={(event) => setConfirmation(event.target.value)}
            aria-invalid={mismatch}
          />
          {mismatch ? <p className="text-xs text-destructive">Пароли не совпадают</p> : null}
        </div>

        <RoleField value={role} onChange={setRole} />

        {error ? (
          <p role="alert" className="text-sm text-destructive">
            {error}
          </p>
        ) : null}

        <Button type="submit" className="w-full" disabled={!canSubmit}>
          {pending ? 'Создаём…' : 'Зарегистрироваться'}
        </Button>
      </form>
    </AuthLayout>
  )
}

function messageFor(cause: unknown): string {
  if (cause instanceof ApiError) {
    if (cause.status === 409) return 'Такой логин уже занят'
    if (cause.status === 404) return 'Сервис авторизации недоступен'
    if (cause.errors?.length) return 'Проверьте заполнение полей'
    return cause.detail ?? cause.title ?? `Ошибка ${cause.status}`
  }
  return 'Не удалось связаться с сервером'
}
