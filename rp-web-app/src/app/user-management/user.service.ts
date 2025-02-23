import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from './user.model';
import {apiConfig} from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = apiConfig.user_api_url + 'api/users'; // Replace with your API URL  

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  createUser(newUser: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, newUser);
  }

  editUser(currUser: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, currUser);
  }

  deletUser(productId:String): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${productId}`);
  }

}
