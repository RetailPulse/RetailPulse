import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../services/auth.service';

import { AdminPageComponent } from './admin-page.component';

describe('AdminPageComponent', () => {
  let component: AdminPageComponent;
  let fixture: ComponentFixture<AdminPageComponent>;

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
      imports: [AdminPageComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService }, // Mock AuthService
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
