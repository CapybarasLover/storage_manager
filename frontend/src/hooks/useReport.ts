import { useQuery } from '@tanstack/react-query'
import { apiRequest } from '@/lib/api'
import { queryKeys } from '@/hooks/keys'
import type { SummaryReportDto } from '@/types/api'

export function useReport(storageName: string | undefined, dateFrom: string, dateTo: string, enabled = true) {
  return useQuery({
    queryKey: queryKeys.report(storageName ?? '', dateFrom, dateTo),
    queryFn: () =>
      apiRequest<SummaryReportDto>('/report/summary', {
        params: { storageName, dateFrom, dateTo },
      }),
    enabled: enabled && Boolean(storageName),
  })
}
