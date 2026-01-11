import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/**
 * HTTP Interceptor to add JWT Bearer token to all API requests
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);

    // Skip if no valid token or if calling Keycloak endpoints
    if (!authService.isAuthenticated || req.url.includes('realms/')) {
        return next(req);
    }

    // Clone request and add Authorization header
    const authReq = req.clone({
        setHeaders: {
            Authorization: `Bearer ${authService.accessToken}`
        }
    });

    return next(authReq);
};
