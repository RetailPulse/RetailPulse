import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Product} from './product.model';
import {apiConfig} from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = apiConfig.url + 'api/products'; // Replace with your API URL  

  constructor(private http: HttpClient) { }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }
  createProduct(newProduct: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, newProduct);
  }
  deleteProduct(productId:String): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}`);
  }

}
