import {BusinessEntity} from '../business-entity-management/business-entity.model';

export class InventoryTransaction {
  productId!:string;
  quantity!:number;
  costPerUnit!:number;
  source!: number;
  destination!: number;
}





