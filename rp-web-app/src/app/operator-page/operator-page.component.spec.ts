import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { routes } from '../app.routes';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

import { createMockAuthService } from '../mock/auth.service.mock';
import { ConfirmationService } from 'primeng/api'; 

import { OperatorPageComponent } from './operator-page.component';

describe('OperatorPageComponent', () => {
  let component: OperatorPageComponent;
  let fixture: ComponentFixture<OperatorPageComponent>;

  beforeEach(async () => {
    // Mock AuthService
    const mockAuthService = createMockAuthService();

    await TestBed.configureTestingModule({
      imports: [OperatorPageComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
        provideAnimations(),
        provideAnimationsAsync(),
        { provide: AuthService, useValue: mockAuthService }, // Mock AuthService
        ConfirmationService,
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperatorPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
