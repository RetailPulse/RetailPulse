import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../services/auth.service';

import { LoginFormComponent } from './login-form.component';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;

  // Mock AuthService
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  const mockAuthService = {
    login: jasmine.createSpy('login'),
    logout: jasmine.createSpy('logout'), 
    initializeAuth: jasmine.createSpy('initializeAuth').and.returnValue(Promise.resolve()),
    isAuthenticated: jasmine.createSpy('isAuthenticated').and.returnValue(true),
    accessToken: jasmine.createSpy('isAuthenticated').and.returnValue('dummy-access-token'),
    getUserRoles: jasmine.createSpy('getUserRoles').and.returnValue(['ADMIN']),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginFormComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService }, // Mock AuthService
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
