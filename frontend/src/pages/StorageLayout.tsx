import { Outlet, useOutletContext, useParams } from 'react-router-dom'
import { ErrorState } from '@/components/ErrorState'
import { WorkspaceHeader } from '@/components/WorkspaceHeader'
import { useStorage } from '@/hooks/useStorages'
import type { StorageDto } from '@/types/api'

export interface StorageContext {
  storageId: number
  storage?: StorageDto
  isLoading: boolean
}

export function useStorageContext() {
  return useOutletContext<StorageContext>()
}

export function StorageLayout() {
  const { storageId } = useParams()
  const parsed = Number(storageId)
  const id = Number.isInteger(parsed) && parsed > 0 ? parsed : null
  const query = useStorage(id)

  const context: StorageContext = {
    storageId: id ?? 0,
    storage: query.data,
    isLoading: query.isLoading,
  }

  return (
    <>
      <WorkspaceHeader storageId={id ?? 0} storage={query.data} isLoading={query.isLoading} />
      <main className="min-w-0 flex-1 px-4 py-5 sm:px-6">
        {id === null ? (
          <ErrorState error={new Error('bad id')} />
        ) : query.isError ? (
          <ErrorState error={query.error} onRetry={() => void query.refetch()} />
        ) : (
          <Outlet context={context} />
        )}
      </main>
    </>
  )
}
