export class InventoryTransaction {
  id!: string;
  productId!:string;
  quantity!:number;
  costPricePerUnit!:number;
}

export class Column {
  field!: string;
  header!: string;
}

export interface FilterOption {
  label: string;
  value: string;
}
