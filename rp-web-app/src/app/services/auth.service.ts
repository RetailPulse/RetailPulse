import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from '../../environments/environment';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  
  constructor(private oauthService: OAuthService) {
  }

  // Configure OAuth2 settings  
  public configureOAuth(): Promise<boolean> {
    this.oauthService.configure(authConfig);

    return this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oauthService.hasValidAccessToken()) {
        console.log('User is logged in');
        sessionStorage.setItem('accessToken', this.oauthService.getAccessToken());        
        return true;
      } else {
        console.log('User is not logged in');
        return true;
      }
    }).catch(error => {
      console.log('Error during OAuth configuration', error);
      return false;
    });
  }

  login() {
    this.oauthService.initCodeFlow();
  }

  logout() {
    this.oauthService.logOut();
    sessionStorage.removeItem('accessToken'); // Remove the token from session storage
  }

  get isAuthenticated(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  get accessToken(): string {
    let token = sessionStorage.getItem('accessToken');
    if (!token) {
      token = this.oauthService.getAccessToken();
      if (token) {
        sessionStorage.setItem('accessToken', token);
      }
    }
    return token?.toString();
  }

  public getUserRole(): string[] {    
    if (!this.accessToken) {
      return ['UNAUTHORIZED'];
    }
    interface DecodedToken {
      roles: Array<string>;
    }
    let decodedToken = jwtDecode<DecodedToken>(this.accessToken);

    return decodedToken.roles;
  }
  
}