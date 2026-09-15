import { Skeleton } from '@/components/ui/skeleton'

export function TableSkeleton({ rows = 6, columns = 5 }: { rows?: number; columns?: number }) {
  return (
    <div className="space-y-2 p-3" aria-hidden>
      {Array.from({ length: rows }).map((_, rowIndex) => (
        <div key={rowIndex} className="flex gap-3">
          {Array.from({ length: columns }).map((_, cellIndex) => (
            <Skeleton key={cellIndex} className="h-7 flex-1" style={{ opacity: 1 - rowIndex * 0.1 }} />
          ))}
        </div>
      ))}
    </div>
  )
}
