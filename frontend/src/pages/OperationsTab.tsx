import { ArrowDown, ArrowUp, ChevronLeft, ChevronRight, ScrollText, SearchX } from 'lucide-react'
import { useMemo } from 'react'
import { useSearchParams } from 'react-router-dom'
import { EmptyState } from '@/components/EmptyState'
import { ErrorState } from '@/components/ErrorState'
import { DateRangePicker } from '@/components/DateRangePicker'
import { OperationTypeBadge } from '@/components/OperationTypeBadge'
import { TableSkeleton } from '@/components/TableSkeleton'
import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import { Segmented } from '@/components/ui/segmented'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { useOperations } from '@/hooks/useOperations'
import { useStorage, useStorages } from '@/hooks/useStorages'
import { OPERATION_META, OPERATION_TYPES, PAGE_SIZES } from '@/lib/constants'
import { validateRange } from '@/lib/dates'
import { formatDateTime, formatMoney, operations as operationsPlural } from '@/lib/format'
import { cn } from '@/lib/utils'
import { useStorageContext } from '@/pages/StorageLayout'
import type { OperationType } from '@/types/api'

const ALL = '__all'
const DEFAULT_SORT = 'operationDateTime,desc'

type TypeFilter = OperationType | 'ALL'

export function OperationsTab() {
  const { storage } = useStorageContext()
  const [searchParams, setSearchParams] = useSearchParams()
  const { data: storages } = useStorages()

  // Склад предзаполнен выбранным слева; ?storage=all снимает фильтр.
  const storageParam = searchParams.get('storage')
  const storageName =
    storageParam === null ? storage?.name : storageParam === 'all' ? undefined : storageParam

  const productName = searchParams.get('product') ?? undefined
  const typeFilter = (searchParams.get('type') as TypeFilter | null) ?? 'ALL'
  const dateFrom = searchParams.get('from') ?? ''
  const dateTo = searchParams.get('to') ?? ''
  const page = Number(searchParams.get('page') ?? '0') || 0
  const size = Number(searchParams.get('size') ?? '20') || 20
  const sort = searchParams.get('sort') ?? DEFAULT_SORT

  const dateError = validateRange(dateFrom || undefined, dateTo || undefined)

  // Имя товара бэкенд матчит точным равенством, поэтому только выбор из списка.
  const filterStorage = storages?.find((candidate) => candidate.name === storageName)
  const { data: productSource } = useStorage(filterStorage?.id ?? null)
  const products = useMemo(
    () =>
      (productSource?.storageItemListDto ?? [])
        .map((item) => item.name)
        .sort((a, b) => a.localeCompare(b, 'ru')),
    [productSource],
  )

  const filter = {
    storageName,
    productName,
    operationType: typeFilter === 'ALL' ? undefined : typeFilter,
    dateFrom: dateFrom || undefined,
    dateTo: dateTo || undefined,
  }

  const query = useOperations(filter, { page, size, sort }, !dateError)

  function update(changes: Record<string, string | undefined>, resetPage = true) {
    const next = new URLSearchParams(searchParams)
    for (const [key, value] of Object.entries(changes)) {
      if (value === undefined) next.delete(key)
      else next.set(key, value)
    }
    if (resetPage) next.delete('page')
    setSearchParams(next, { replace: true })
  }

  function resetFilters() {
    const next = new URLSearchParams(searchParams)
    for (const key of ['storage', 'product', 'type', 'from', 'to', 'page', 'sort']) next.delete(key)
    setSearchParams(next, { replace: true })
  }

  function toggleSort(field: string) {
    const [currentField, currentDirection] = sort.split(',')
    const direction = currentField === field && currentDirection === 'asc' ? 'desc' : 'asc'
    update({ sort: `${field},${direction}` }, false)
  }

  const anyFilterApplied =
    storageParam !== null ||
    Boolean(productName) ||
    typeFilter !== 'ALL' ||
    Boolean(dateFrom) ||
    Boolean(dateTo)

  const rows = query.data?.content ?? []
  const totalPages = query.data?.totalPages ?? 0

  return (
    <div className="space-y-4">
      <div className="space-y-4 rounded-xl border bg-card p-4">
        <div className="grid gap-4 sm:grid-cols-2">
          <div className="space-y-1.5">
            <Label htmlFor="filter-storage">Склад</Label>
            <Select
              value={storageName ?? ALL}
              onValueChange={(value) =>
                update({
                  storage: value === ALL ? 'all' : value,
                  // Товары у другого склада свои — старый выбор потеряет смысл.
                  product: undefined,
                })
              }
            >
              <SelectTrigger id="filter-storage">
                <SelectValue placeholder="Все склады" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value={ALL}>Все склады</SelectItem>
                {storages?.map((candidate) => (
                  <SelectItem key={candidate.id} value={candidate.name}>
                    {candidate.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-1.5">
            <Label htmlFor="filter-product">Товар</Label>
            <Select
              value={productName ?? ALL}
              onValueChange={(value) => update({ product: value === ALL ? undefined : value })}
              disabled={!storageName}
            >
              <SelectTrigger id="filter-product">
                <SelectValue placeholder="Все товары" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value={ALL}>Все товары</SelectItem>
                {products.map((name) => (
                  <SelectItem key={name} value={name}>
                    {name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {!storageName ? (
              <p className="text-xs text-muted-foreground">Выберите склад, чтобы фильтровать по товару</p>
            ) : null}
          </div>

        </div>

        <div className="space-y-1.5">
          <Label>Тип операции</Label>
          <Segmented<TypeFilter>
            label="Тип операции"
            value={typeFilter}
            onValueChange={(value) => update({ type: value === 'ALL' ? undefined : value })}
            className="flex-nowrap sm:max-w-xl"
            options={[
              { value: 'ALL', label: 'Все' },
              ...OPERATION_TYPES.map((type) => ({
                value: type,
                label: OPERATION_META[type].label,
                activeClassName: OPERATION_META[type].textClassName,
              })),
            ]}
          />
        </div>

        <DateRangePicker
          idPrefix="operations"
          value={{ dateFrom, dateTo }}
          error={dateError}
          onChange={(range) => update({ from: range.dateFrom || undefined, to: range.dateTo || undefined })}
        />

        {anyFilterApplied ? (
          <Button variant="ghost" size="sm" onClick={resetFilters}>
            Сбросить фильтры
          </Button>
        ) : null}
      </div>

      <div className="rounded-xl border bg-card">
        {dateError ? (
          <EmptyState icon={SearchX} title="Поправьте период" description={dateError} />
        ) : query.isLoading ? (
          <TableSkeleton columns={7} />
        ) : query.isError ? (
          <ErrorState error={query.error} onRetry={() => void query.refetch()} />
        ) : !rows.length ? (
          anyFilterApplied ? (
            <EmptyState
              icon={SearchX}
              title="По фильтрам ничего не найдено"
              description="Попробуйте расширить период или снять часть условий."
              action={
                <Button variant="outline" size="sm" onClick={resetFilters}>
                  Сбросить
                </Button>
              }
            />
          ) : (
            <EmptyState
              icon={ScrollText}
              title="Операций ещё нет"
              description="Журнал заполнится, как только по товарам пройдут поступления, продажи или списания."
            />
          )
        ) : (
          <Table className={cn(query.isFetching && 'opacity-60 transition-opacity')}>
            <TableHeader>
              <TableRow>
                <SortableHead field="operationDateTime" sort={sort} onToggle={toggleSort}>
                  Дата и время
                </SortableHead>
                <SortableHead field="storageName" sort={sort} onToggle={toggleSort}>
                  Склад
                </SortableHead>
                <SortableHead field="productName" sort={sort} onToggle={toggleSort}>
                  Товар
                </SortableHead>
                <SortableHead field="operationType" sort={sort} onToggle={toggleSort}>
                  Тип
                </SortableHead>
                <SortableHead field="amount" sort={sort} onToggle={toggleSort} align="right">
                  Кол-во
                </SortableHead>
                <SortableHead field="operationCost" sort={sort} onToggle={toggleSort} align="right">
                  Сумма
                </SortableHead>
                <TableHead>Комментарий</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {rows.map((operation, index) => (
                <TableRow key={`${operation.operationDateTime}-${operation.productName}-${index}`}>
                  <TableCell className="tabular whitespace-nowrap text-muted-foreground">
                    {formatDateTime(operation.operationDateTime)}
                  </TableCell>
                  <TableCell>{operation.storageName}</TableCell>
                  <TableCell className="font-medium">{operation.productName}</TableCell>
                  <TableCell>
                    <OperationTypeBadge type={operation.operationType} />
                  </TableCell>
                  <TableCell className="tabular text-right">{operation.amount}</TableCell>
                  <TableCell className="tabular text-right">{formatMoney(operation.operationCost)}</TableCell>
                  <TableCell className="max-w-[16rem] truncate text-muted-foreground" title={operation.comment ?? ''}>
                    {operation.comment || '—'}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </div>

      {query.data && rows.length > 0 ? (
        <div className="flex flex-wrap items-center gap-3">
          <p className="text-sm text-muted-foreground">
            {operationsPlural(query.data.totalElements)} · страница {page + 1} из {Math.max(totalPages, 1)}
          </p>

          <div className="ml-auto flex items-center gap-2">
            <Select value={String(size)} onValueChange={(value) => update({ size: value })}>
              <SelectTrigger className="h-8 w-[5.5rem]" aria-label="Размер страницы">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {PAGE_SIZES.map((option) => (
                  <SelectItem key={option} value={String(option)}>
                    {option}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <Button
              variant="outline"
              size="icon"
              className="size-8"
              aria-label="Предыдущая страница"
              disabled={query.data.first}
              onClick={() => update({ page: String(page - 1) }, false)}
            >
              <ChevronLeft />
            </Button>
            <Button
              variant="outline"
              size="icon"
              className="size-8"
              aria-label="Следующая страница"
              disabled={query.data.last}
              onClick={() => update({ page: String(page + 1) }, false)}
            >
              <ChevronRight />
            </Button>
          </div>
        </div>
      ) : null}
    </div>
  )
}

function SortableHead({
  field,
  sort,
  onToggle,
  children,
  align = 'left',
}: {
  field: string
  sort: string
  onToggle: (field: string) => void
  children: React.ReactNode
  align?: 'left' | 'right'
}) {
  const [currentField, currentDirection] = sort.split(',')
  const active = currentField === field

  return (
    <TableHead className={align === 'right' ? 'text-right' : undefined}>
      <button
        type="button"
        onClick={() => onToggle(field)}
        aria-sort={active ? (currentDirection === 'asc' ? 'ascending' : 'descending') : 'none'}
        className={cn(
          'inline-flex cursor-pointer items-center gap-1 rounded transition-colors hover:text-foreground',
          active && 'text-foreground',
        )}
      >
        {children}
        {active ? (
          currentDirection === 'asc' ? (
            <ArrowUp className="size-3" />
          ) : (
            <ArrowDown className="size-3" />
          )
        ) : null}
      </button>
    </TableHead>
  )
}
