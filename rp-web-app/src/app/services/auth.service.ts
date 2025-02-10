import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig, environment } from '../../environments/environment';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  
  constructor(private oauthService: OAuthService) {
  }

  // Configure OAuth2 settings  
  public configureOAuth(): Promise<boolean> {
    if (!environment.authEnabled) {
      console.log('Authentication is disabled. Skipping OAuth configuration');
      return Promise.resolve(true);
    }

    this.oauthService.configure(authConfig);

    return this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oauthService.hasValidAccessToken()) {
        console.log('User is logged in');
        // sessionStorage.setItem('accessToken', this.oauthService.getAccessToken());        
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
    if (!environment.authEnabled) {
      console.log('Authentication is disabled');
      return;
    }
    this.oauthService.initCodeFlow();
  }

  logout() {
    if (!environment.authEnabled) {
      console.log('Authentication is disabled');
      return;
    }
    this.oauthService.logOut();
    // sessionStorage.removeItem('accessToken'); // Remove the token from session storage
  }

  get isAuthenticated(): boolean {
    if (!environment.authEnabled) {
      return true;
    }
    return this.oauthService.hasValidAccessToken();
  }

  get accessToken(): string {
    if (!environment.authEnabled) {
      return 'dummy-access-token';
    }
    let token = sessionStorage.getItem('accessToken');
    if (!token) {
      token = this.oauthService.getAccessToken();
      if (token) {
        sessionStorage.setItem('accessToken', token);
      }
    }
    return token?.toString();
  }

  public isTokenExpired(token: string): boolean {
    const decodedToken: any = jwtDecode(token);
    const expirationDate = new Date(0);
    expirationDate.setUTCSeconds(decodedToken.exp);
    return expirationDate < new Date();
  }

  private refreshToken(): Promise<void> {
    return this.oauthService.refreshToken().then(() => {
      const newToken = this.oauthService.getAccessToken();
      sessionStorage.setItem('accessToken', newToken);
    }).catch(error => {
      console.log('Error refreshing token', error);
      this.logout();
    });
  }

  public async ensureTokenIsValid(): Promise<void> {
    const token = this.accessToken;
    if (this.isTokenExpired(token)) {
      await this.refreshToken();
    }
  }
  
  public getUserRole(): string[] {   
    if (!environment.authEnabled) {
      return [environment.devModeRole];
    } 
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