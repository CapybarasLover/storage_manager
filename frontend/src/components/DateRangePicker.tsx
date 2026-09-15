import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { matchPreset, RANGE_PRESETS, today, type DateRange } from '@/lib/dates'
import { cn } from '@/lib/utils'

interface DateRangePickerProps {
  value: DateRange
  onChange: (range: DateRange) => void
  error?: string | null
  idPrefix: string
  className?: string
}

export function DateRangePicker({ value, onChange, error, idPrefix, className }: DateRangePickerProps) {
  const activePreset = matchPreset(value)
  const max = today()

  return (
    <div className={cn('space-y-2', className)}>
      <div className="flex flex-wrap items-end gap-3">
        <div className="space-y-1.5">
          <Label htmlFor={`${idPrefix}-from`} className="text-xs text-muted-foreground">
            С
          </Label>
          <Input
            id={`${idPrefix}-from`}
            type="date"
            className="w-[10.5rem]"
            max={max}
            value={value.dateFrom}
            aria-invalid={Boolean(error)}
            onChange={(event) => onChange({ ...value, dateFrom: event.target.value })}
          />
        </div>
        <div className="space-y-1.5">
          <Label htmlFor={`${idPrefix}-to`} className="text-xs text-muted-foreground">
            По
          </Label>
          <Input
            id={`${idPrefix}-to`}
            type="date"
            className="w-[10.5rem]"
            max={max}
            value={value.dateTo}
            aria-invalid={Boolean(error)}
            onChange={(event) => onChange({ ...value, dateTo: event.target.value })}
          />
        </div>
        <div className="flex flex-wrap gap-1 pb-0.5">
          {RANGE_PRESETS.map((preset) => (
            <button
              key={preset.key}
              type="button"
              onClick={() => onChange(preset.build())}
              aria-pressed={activePreset === preset.key}
              className={cn(
                'h-8 cursor-pointer rounded-md border px-2.5 text-xs font-medium transition-colors',
                activePreset === preset.key
                  ? 'border-transparent bg-primary text-primary-foreground'
                  : 'hover:bg-accent',
              )}
            >
              {preset.label}
            </button>
          ))}
        </div>
      </div>
      {error ? (
        <p role="alert" className="text-xs text-destructive">
          {error}
        </p>
      ) : null}
    </div>
  )
}
