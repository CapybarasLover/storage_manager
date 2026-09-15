import type { OperationFilterParams, PageParams } from '@/types/api'

export const queryKeys = {
  storages: ['storages'] as const,
  storage: (storageId: number) => ['storage', storageId] as const,
  operations: (filter: OperationFilterParams, page: PageParams) => ['operations', filter, page] as const,
  report: (storageName: string, dateFrom: string, dateTo: string) =>
    ['report', storageName, dateFrom, dateTo] as const,
}
