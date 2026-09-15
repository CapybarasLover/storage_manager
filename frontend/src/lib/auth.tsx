import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { AUTH_ENABLED, apiRequest, getToken, setToken, UNAUTHORIZED_EVENT } from '@/lib/api'
import type { AuthResponse, AuthUser, Role } from '@/types/api'

type AuthStatus = 'loading' | 'authenticated' | 'anonymous'

interface AuthContextValue {
  user: AuthUser | null
  status: AuthStatus
  isAdmin: boolean
  login: (username: string, password: string) => Promise<void>
  register: (username: string, password: string, role: Role) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

/** Пока бэкенд авторизации не готов, VITE_AUTH_ENABLED=false пускает внутрь админом. */
const LOCAL_USER: AuthUser = { username: 'Локальный режим', role: 'ADMIN' }

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(AUTH_ENABLED ? null : LOCAL_USER)
  const [status, setStatus] = useState<AuthStatus>(AUTH_ENABLED ? 'loading' : 'authenticated')

  // Восстановление сессии по сохранённому токену. Пока идёт проверка,
  // показываем сплэш — иначе на секунду мигает форма входа.
  useEffect(() => {
    if (!AUTH_ENABLED) return
    if (!getToken()) {
      setStatus('anonymous')
      return
    }
    let cancelled = false
    apiRequest<AuthUser>('/auth/me')
      .then((me) => {
        if (cancelled) return
        setUser(me)
        setStatus('authenticated')
      })
      .catch(() => {
        if (cancelled) return
        setToken(null)
        setUser(null)
        setStatus('anonymous')
      })
    return () => {
      cancelled = true
    }
  }, [])

  // 401 в любом запросе означает, что сессия кончилась.
  useEffect(() => {
    if (!AUTH_ENABLED) return
    const onUnauthorized = () => {
      setUser(null)
      setStatus('anonymous')
    }
    window.addEventListener(UNAUTHORIZED_EVENT, onUnauthorized)
    return () => window.removeEventListener(UNAUTHORIZED_EVENT, onUnauthorized)
  }, [])

  const accept = useCallback((response: AuthResponse) => {
    setToken(response.token)
    setUser({ username: response.username, role: response.role })
    setStatus('authenticated')
  }, [])

  const login = useCallback(
    async (username: string, password: string) => {
      accept(await apiRequest<AuthResponse>('/auth/login', { method: 'POST', body: { username, password } }))
    },
    [accept],
  )

  const register = useCallback(
    async (username: string, password: string, role: Role) => {
      accept(await apiRequest<AuthResponse>('/auth/register', { method: 'POST', body: { username, password, role } }))
    },
    [accept],
  )

  const logout = useCallback(() => {
    setToken(null)
    setUser(null)
    setStatus(AUTH_ENABLED ? 'anonymous' : 'authenticated')
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({ user, status, isAdmin: user?.role === 'ADMIN', login, register, logout }),
    [user, status, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth вызван вне AuthProvider')
  return context
}

export function ProtectedRoute({ children }: { children: ReactNode }) {
  const { status } = useAuth()
  const location = useLocation()

  if (status === 'loading') return <AuthSplash />
  if (status === 'anonymous') return <Navigate to="/login" replace state={{ from: location }} />
  return <>{children}</>
}

/**
 * Скрывает разрушающие действия у работника. Это подсказка интерфейса,
 * а не защита: 403 должен приходить с бэкенда независимо.
 */
export function RoleGate({ admin, children }: { admin?: boolean; children: ReactNode }) {
  const { isAdmin } = useAuth()
  if (admin && !isAdmin) return null
  return <>{children}</>
}

function AuthSplash() {
  return (
    <div className="flex min-h-dvh items-center justify-center bg-background">
      <div className="flex flex-col items-center gap-3 text-muted-foreground">
        <div className="size-6 animate-spin rounded-full border-2 border-muted-foreground/30 border-t-foreground" />
        <p className="text-sm">Проверяем сессию…</p>
      </div>
    </div>
  )
}
