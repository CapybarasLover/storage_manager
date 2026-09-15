// Зеркало DTO бэкенда. Имена полей повторяют json как есть,
// включая storageItemListDto — переименовывать на клиенте нечего.

export type ItemStatus = 'ENOUGH' | 'FEW' | 'OUT'
export type OperationType = 'ADMISSION' | 'SELL' | 'WRITE_OFF'
export type Role = 'ADMIN' | 'WORKER'

export interface StorageInfoDto {
  id: number
  name: string
}

export interface StorageItemDto {
  id: number
  name: string
  count: number
  status: ItemStatus
  cost: number
}

export interface StorageDto {
  name: string
  storageItemListDto: StorageItemDto[]
}

export interface OperationDto {
  storageName: string
  operationType: OperationType
  productName: string
  amount: number
  operationDateTime: string
  operationCost: number | null
  comment: string | null
}

export interface PageDto<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
  first: boolean
  last: boolean
  numberOfElements: number
}

export interface StorageStats {
  admissionsCount: number
  admissionsTotal: number
  sellsCount: number
  sellsTotal: number
  writeOffsCount: number
  writeOffsTotal: number
  spending: number
  revenue: number
  profit: number
}

export interface ProductStats {
  admissionsCount: number
  admissionsTotal: number
  sellsCount: number
  sellsTotal: number
  writeOffsCount: number
  writeOffsTotal: number
  productSpending: number
  productRevenue: number
  productProfit: number
}

export interface SummaryReportDto {
  storageName: string
  dateFrom: string
  dateTo: string
  generatedAt: string
  currentStock: StorageItemDto[]
  storageStats: StorageStats
  productStats: Record<string, ProductStats>
}

export interface AuthUser {
  username: string
  role: Role
}

export interface AuthResponse extends AuthUser {
  token: string
}

export interface OperationRequest {
  operationType: OperationType
  productName: string
  count: number
  operationCost?: number
  comment?: string
}

export interface OperationFilterParams {
  storageName?: string
  operationType?: OperationType
  productName?: string
  dateFrom?: string
  dateTo?: string
}

export interface PageParams {
  page?: number
  size?: number
  sort?: string
}
