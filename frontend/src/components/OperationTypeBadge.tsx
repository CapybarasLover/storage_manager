import { Badge } from '@/components/ui/badge'
import { OPERATION_META } from '@/lib/constants'
import { cn } from '@/lib/utils'
import type { OperationType } from '@/types/api'

export function OperationTypeBadge({ type, className }: { type: OperationType; className?: string }) {
  const meta = OPERATION_META[type]
  return <Badge className={cn(meta.badgeClassName, className)}>{meta.label}</Badge>
}
