import { useMutation, useQueryClient } from '@tanstack/react-query'
import { apiRequest } from '@/lib/api'
import { queryKeys } from '@/hooks/keys'

/**
 * Мутации позиции собраны здесь намеренно: когда заказчик определится
 * с редактированием товара, сюда добавится useUpdateProduct, а экраны
 * останутся нетронутыми.
 */

interface AddProductInput {
  productName: string
  productCost: number
}

export function useAddProduct(storageId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: ({ productName, productCost }: AddProductInput) =>
      apiRequest<void>(`/storage/${storageId}/products`, {
        method: 'POST',
        params: { productName, productCost },
      }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.storage(storageId) }),
  })
}

export function useDeleteProduct(storageId: number) {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (productId: number) =>
      apiRequest<void>(`/storage/${storageId}/products/${productId}`, { method: 'DELETE' }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.storage(storageId) }),
  })
}
