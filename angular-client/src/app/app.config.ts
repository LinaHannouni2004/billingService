import { ApplicationConfig, provideZoneChangeDetection, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { routes } from './app.routes';
import { authInterceptor } from './auth/auth.interceptor';
import { AuthService } from './auth/auth.service';

/**
 * Factory function to initialize OAuth2 on app startup
 */
function initializeOAuth(authService: AuthService) {
    return () => authService.configure();
}

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        provideHttpClient(withInterceptors([authInterceptor])),
        provideOAuthClient(),
        {
            provide: APP_INITIALIZER,
            useFactory: initializeOAuth,
            deps: [AuthService],
            multi: true
        }
    ]
};
