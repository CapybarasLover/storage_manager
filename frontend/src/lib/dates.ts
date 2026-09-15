import { format, startOfMonth, startOfQuarter, subDays } from 'date-fns'

export interface DateRange {
  dateFrom: string
  dateTo: string
}

/** Календарная дата в формате, который ждёт LocalDate на бэкенде. */
export function toIsoDate(date: Date): string {
  return format(date, 'yyyy-MM-dd')
}

export function today(): string {
  return toIsoDate(new Date())
}

export interface RangePreset {
  key: string
  label: string
  build: () => DateRange
}

export const RANGE_PRESETS: RangePreset[] = [
  {
    key: 'today',
    label: 'Сегодня',
    build: () => ({ dateFrom: today(), dateTo: today() }),
  },
  {
    key: 'week',
    label: '7 дней',
    build: () => ({ dateFrom: toIsoDate(subDays(new Date(), 6)), dateTo: today() }),
  },
  {
    key: 'month',
    label: 'Месяц',
    build: () => ({ dateFrom: toIsoDate(startOfMonth(new Date())), dateTo: today() }),
  },
  {
    key: 'quarter',
    label: 'Квартал',
    build: () => ({ dateFrom: toIsoDate(startOfQuarter(new Date())), dateTo: today() }),
  },
]

export function matchPreset(range: Partial<DateRange>): string | null {
  if (!range.dateFrom || !range.dateTo) return null
  const found = RANGE_PRESETS.find((preset) => {
    const built = preset.build()
    return built.dateFrom === range.dateFrom && built.dateTo === range.dateTo
  })
  return found?.key ?? null
}

/**
 * Даты валидирует фронт: у /report/summary параметры вообще без аннотаций,
 * а у фильтра операций будущая дата вернёт 400.
 */
export function validateRange(dateFrom?: string, dateTo?: string): string | null {
  if (!dateFrom || !dateTo) return null
  const max = today()
  if (dateFrom > max || dateTo > max) return 'Дата не может быть в будущем'
  if (dateFrom > dateTo) return 'Дата начала должна быть не позже даты конца'
  return null
}
