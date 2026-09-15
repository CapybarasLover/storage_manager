import { Navigate, Route, Routes } from 'react-router-dom'
import { AppShell } from '@/components/AppShell'
import { ProtectedRoute } from '@/lib/auth'
import { IndexRedirect } from '@/pages/IndexRedirect'
import { ItemsTab } from '@/pages/ItemsTab'
import { LoginPage } from '@/pages/LoginPage'
import { OperationsTab } from '@/pages/OperationsTab'
import { RegisterPage } from '@/pages/RegisterPage'
import { ReportTab } from '@/pages/ReportTab'
import { StorageLayout } from '@/pages/StorageLayout'

export function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        element={
          <ProtectedRoute>
            <AppShell />
          </ProtectedRoute>
        }
      >
        <Route index element={<IndexRedirect />} />
        <Route path="/storages/:storageId" element={<StorageLayout />}>
          <Route index element={<Navigate to="items" replace />} />
          <Route path="items" element={<ItemsTab />} />
          <Route path="operations" element={<OperationsTab />} />
          <Route path="report" element={<ReportTab />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
