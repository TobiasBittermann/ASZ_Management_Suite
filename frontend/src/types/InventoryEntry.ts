export type InventoryEntry = {
    id: number;
    inventoryId: number;
    drinkId: number;
    initialQuantity: number;
    quantity: number | null;
    shrinkage: number | null;
    unitValue: number;
    totalValue: number;
    shrinkageValue: number;
}
