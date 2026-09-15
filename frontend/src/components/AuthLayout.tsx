import type { ReactNode } from 'react'
import { Warehouse } from 'lucide-react'
import { AUTH_ENABLED } from '@/lib/api'

export function AuthLayout({
  title,
  description,
  children,
  footer,
}: {
  title: string
  description: string
  children: ReactNode
  footer: ReactNode
}) {
  return (
    <div className="flex min-h-dvh items-center justify-center bg-background px-4 py-10">
      <div className="w-full max-w-sm">
        <div className="mb-6 flex items-center gap-2">
          <div className="flex size-9 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Warehouse className="size-4" />
          </div>
          <span className="text-lg font-semibold tracking-tight">Складской учёт</span>
        </div>

        <div className="rounded-xl border bg-card p-6 shadow-sm">
          <h1 className="text-lg font-semibold">{title}</h1>
          <p className="mt-1 text-sm text-muted-foreground">{description}</p>
          <div className="mt-5">{children}</div>
        </div>

        <div className="mt-4 text-center text-sm text-muted-foreground">{footer}</div>

        {!AUTH_ENABLED ? (
          <p className="mt-4 rounded-lg border border-dashed p-3 text-center text-xs text-muted-foreground">
            Авторизация выключена через VITE_AUTH_ENABLED=false — вход не требуется.
          </p>
        ) : null}
      </div>
    </div>
  )
}
