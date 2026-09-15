import { Label } from '@/components/ui/label'
import { Segmented } from '@/components/ui/segmented'
import type { Role } from '@/types/api'

/**
 * Выбор роли вынесен отдельно: если его заменят на инвайт-код,
 * остальная форма регистрации не пострадает.
 */
export function RoleField({ value, onChange }: { value: Role; onChange: (role: Role) => void }) {
  return (
    <div className="space-y-1.5">
      <Label htmlFor="role-field">Роль</Label>
      <Segmented<Role>
        label="Роль"
        value={value}
        onValueChange={onChange}
        options={[
          { value: 'WORKER', label: 'Работник' },
          { value: 'ADMIN', label: 'Админ' },
        ]}
      />
      <p className="text-xs text-muted-foreground">
        {value === 'ADMIN'
          ? 'Ведёт операции, а также редактирует и удаляет позиции.'
          : 'Ведёт операции и смотрит отчёты. Удалять позиции не может.'}
      </p>
    </div>
  )
}
