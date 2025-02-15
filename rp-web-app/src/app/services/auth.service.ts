import {Injectable} from '@angular/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {authConfig, environment} from '../../environments/environment';
import {jwtDecode} from 'jwt-decode';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  constructor(private router: Router, private oauthService: OAuthService) {
    this.oauthService.configure(authConfig);
    this.oauthService.setupAutomaticSilentRefresh();
  }

  // Configure OAuth2 settings
  public initializeAuth() {

    if (!environment.authEnabled) {
      console.log("Authentication is disabled. Using dummy token.");      
      return Promise.resolve();
    }

    return this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      if (this.oauthService.hasValidAccessToken()) {
        console.log("Token: \r\n" + this.accessToken);        
      } else {
        console.log('User is not logged in');
        this.router.navigate(['/login']);
      }
    }).catch(error => {
      console.log('Error during OAuth configuration', error);
      this.router.navigate(['/login']);
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

    return this.oauthService.getAccessToken();
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
