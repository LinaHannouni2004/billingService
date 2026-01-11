import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

/**
 * Route guard to protect routes that require authentication
 * Redirects to Keycloak login if not authenticated
 */
export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.isAuthenticated) {
        return true;
    }

    // Store attempted URL for redirecting after login
    sessionStorage.setItem('redirectUrl', state.url);

    // Redirect to login
    authService.login();
    return false;
};

/**
 * Route guard to check for specific roles
 * Usage: canActivate: [() => roleGuard(['ADMIN'])]
 */
export const roleGuard = (requiredRoles: string[]): CanActivateFn => {
    return (route, state) => {
        const authService = inject(AuthService);
        const router = inject(Router);

        if (!authService.isAuthenticated) {
            authService.login();
            return false;
        }

        const hasRole = requiredRoles.some(role => authService.hasRole(role));

        if (!hasRole) {
            // Redirect to unauthorized page or home
            router.navigate(['/']);
            return false;
        }

        return true;
    };
};
