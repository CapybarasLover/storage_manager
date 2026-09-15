import { Warehouse } from 'lucide-react'
import { useState } from 'react'
import { Navigate } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import { CreateStorageDialog } from '@/components/CreateStorageDialog'
import { EmptyState } from '@/components/EmptyState'
import { ErrorState } from '@/components/ErrorState'
import { Skeleton } from '@/components/ui/skeleton'
import { useStorages } from '@/hooks/useStorages'

/** Корень открывает первый склад — работать без выбранного склада всё равно нечем. */
export function IndexRedirect() {
  const { data, isLoading, isError, error, refetch } = useStorages()
  const [creating, setCreating] = useState(false)

  if (isLoading) {
    return (
      <main className="flex-1 p-6">
        <Skeleton className="h-8 w-56" />
        <Skeleton className="mt-4 h-64 w-full" />
      </main>
    )
  }

  if (isError) {
    return (
      <main className="flex-1 p-6">
        <ErrorState error={error} onRetry={() => void refetch()} />
      </main>
    )
  }

  if (data?.length) {
    return <Navigate to={`/storages/${data[0].id}/items`} replace />
  }

  return (
    <main className="flex flex-1 items-center justify-center p-6">
      <EmptyState
        icon={Warehouse}
        title="Складов пока нет"
        description="Создайте первый склад, чтобы завести товары и вести по ним операции."
        action={<Button onClick={() => setCreating(true)}>Создать склад</Button>}
      />
      <CreateStorageDialog open={creating} onOpenChange={setCreating} />
    </main>
  )
}
