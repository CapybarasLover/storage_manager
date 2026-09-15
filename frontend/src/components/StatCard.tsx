import type { ReactNode } from 'react'
import { cn } from '@/lib/utils'

interface StatCardProps {
  label: string
  value: ReactNode
  hint?: ReactNode
  valueClassName?: string
  className?: string
}

export function StatCard({ label, value, hint, valueClassName, className }: StatCardProps) {
  return (
    <div className={cn('rounded-xl border bg-card p-4', className)}>
      <p className="text-xs font-medium uppercase tracking-wide text-muted-foreground">{label}</p>
      <p className={cn('tabular mt-2 text-2xl font-semibold', valueClassName)}>{value}</p>
      {hint ? <p className="mt-1 text-xs text-muted-foreground">{hint}</p> : null}
    </div>
  )
}
