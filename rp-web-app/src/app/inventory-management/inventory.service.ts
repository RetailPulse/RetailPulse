import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Product} from './inventory.model';
import {BusinessEntity} from '../business-entity-management/business-entity-model';


@Injectable({
  providedIn: 'root'
})
export class InventoryService {


  constructor(private http: HttpClient) { }

}
