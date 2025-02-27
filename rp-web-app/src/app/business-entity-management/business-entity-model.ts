export interface BusinessEntity {
  id: number;
  name: string;
  location: string;
  external?: string;
  type: string;
  active: boolean;
}

export interface BusinessEntityDTO {
  name: string;
  location: string;
  external?: string;
  type: string;
}

export const BusinessEntityType = [
  { label: 'Shop', value: 'Shop' },
  { label: 'Central Inventory', value: 'CentralInventory' },
  { label: 'Supplier', value: 'Supplier' },
];