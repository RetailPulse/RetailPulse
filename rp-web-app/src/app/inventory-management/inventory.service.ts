import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {apiConfig} from '../../environments/environment';
import {InventoryTransaction} from './inventory.model';


@Injectable({
  providedIn: 'root'
})


export class InventoryService {

  private inventoryTransactionApiUrl = `${apiConfig.backend_api_url}api/inventoryTransaction`;

  constructor(private http: HttpClient) { }

  getInventoryTransaction(): Observable<InventoryTransaction[]> {
    return this.http.get<InventoryTransaction[]>(this.inventoryTransactionApiUrl);
  }

}
