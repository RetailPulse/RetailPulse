import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8080', // Update with your authorization server URL
  redirectUri: window.location.origin,
  clientId: 'client',
  responseType: 'code',
  scope: 'openid',
  useSilentRefresh: true,
  useHttpBasicAuth: false,
  disablePKCE: false,
  showDebugInformation: true
};

export const apiConfig = {
  url: 'http://localhost:8085/'
}

export const environment = {
  production: false,
  authEnabled: true,
  devModeRole: 'ADMIN' //'OPERATOR' //
};
