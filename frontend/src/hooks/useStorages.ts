import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { apiRequest } from '@/lib/api'
import { queryKeys } from '@/hooks/keys'
import type { StorageDto, StorageInfoDto } from '@/types/api'

export function useStorages() {
  return useQuery({
    queryKey: queryKeys.storages,
    queryFn: () => apiRequest<StorageInfoDto[]>('/storage'),
  })
}

export function useStorage(storageId: number | null) {
  return useQuery({
    queryKey: queryKeys.storage(storageId ?? 0),
    queryFn: () => apiRequest<StorageDto>(`/storage/${storageId}`),
    enabled: storageId !== null,
  })
}

export function useCreateStorage() {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (name: string) => apiRequest<void>('/storage', { method: 'POST', params: { name } }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.storages }),
  })
}
