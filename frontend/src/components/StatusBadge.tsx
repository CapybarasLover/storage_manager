import { Badge } from '@/components/ui/badge'
import { STATUS_META } from '@/lib/constants'
import { cn } from '@/lib/utils'
import type { ItemStatus } from '@/types/api'

/** Цвет дублируется текстом: на статус нельзя полагаться только по оттенку. */
export function StatusBadge({ status, className }: { status: ItemStatus; className?: string }) {
  const meta = STATUS_META[status]
  return (
    <Badge className={cn(meta.badgeClassName, className)}>
      <span aria-hidden className={cn('size-1.5 rounded-full', meta.dotClassName)} />
      {meta.label}
    </Badge>
  )
}
