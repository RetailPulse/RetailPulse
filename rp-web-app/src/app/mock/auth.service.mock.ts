// mocks/auth-service.mock.ts
import { AuthService } from '../services/auth.service';

export const createMockAuthService = (): jasmine.SpyObj<AuthService> => {
  return {
    login: jasmine.createSpy('login'),
    logout: jasmine.createSpy('logout'),
    initializeAuth: jasmine.createSpy('initializeAuth').and.returnValue(Promise.resolve()),
    get isAuthenticated() { return true; },
    get accessToken() { return 'dummy-access-token'; },
    getUserRole: jasmine.createSpy('getUserRole').and.returnValue(['ADMIN']),
    router: jasmine.createSpyObj('router', ['navigate']),
    oauthService: jasmine.createSpyObj('oauthService', ['initImplicitFlow', 'logOut']),
  };
};