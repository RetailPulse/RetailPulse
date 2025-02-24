import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import {User, createUserDTO} from './user.model';
import {apiConfig} from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http: HttpClient = inject(HttpClient);
  private apiUrl = apiConfig.user_api_url + 'api/users'; // Replace with your API URL  

  constructor() { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  createUser(newUser: User): Observable<User> {

    const create_user_dto: createUserDTO = {
      username: newUser.username,
      password: 'password1',
      email: newUser.email,
      name: newUser.name,
      roles: newUser.roles,
    };
    
    return this.http.post<User>(this.apiUrl, create_user_dto).pipe(
      catchError((err) => {        
        throw new Error(err.error.message);
      })
    );
  }

  editUser(currUser: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${currUser.id}`, currUser);
  }

  deleteUser(userId:number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`);
  }

}
