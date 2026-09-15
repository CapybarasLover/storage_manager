import { Menu, Warehouse } from 'lucide-react'
import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import { Button } from '@/components/ui/button'
import { Sheet, SheetContent, SheetTrigger } from '@/components/ui/sheet'
import { StorageSidebar } from '@/components/StorageSidebar'

export function AppShell() {
  const [drawerOpen, setDrawerOpen] = useState(false)

  return (
    <div className="flex min-h-dvh bg-background">
      <aside className="sticky top-0 hidden h-dvh w-64 shrink-0 border-r lg:block">
        <StorageSidebar />
      </aside>

      <div className="flex min-w-0 flex-1 flex-col">
        <div className="flex items-center gap-2 border-b px-3 py-2 lg:hidden">
          <Sheet open={drawerOpen} onOpenChange={setDrawerOpen}>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon" aria-label="Открыть список складов">
                <Menu />
              </Button>
            </SheetTrigger>
            <SheetContent title="Склады">
              <StorageSidebar onNavigate={() => setDrawerOpen(false)} />
            </SheetContent>
          </Sheet>
          <Warehouse className="size-4 text-muted-foreground" />
          <span className="text-sm font-semibold">Складской учёт</span>
        </div>

        <Outlet />
      </div>
    </div>
  )
}
