import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  standalone: true
})
export class AppComponent {
  title = 'test-app';

  constructor(private http: HttpClient) {
    console.log('AppComponent: constructor');

    // this.http.get('http://localhost:9090/hello').subscribe({
    //   next: (response) => {
    //     console.log('Response:', response);
    //   },
    //   error: (error) => {
    //     console.error('Error:', error);
    //   }
    // });
  }

}
