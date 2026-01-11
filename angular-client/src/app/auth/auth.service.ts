import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from './auth.config';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
    public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

    constructor(private oauthService: OAuthService) {
        // Subscribe to events
        this.oauthService.events.subscribe(event => {
            if (event.type === 'token_received') {
                this.isAuthenticatedSubject.next(true);
            }
            if (event.type === 'logout' || event.type === 'session_terminated') {
                this.isAuthenticatedSubject.next(false);
            }
        });
    }

    /**
     * Configure OAuth2 and attempt to login with existing token
     */
    async configure(): Promise<void> {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        try {
            await this.oauthService.loadDiscoveryDocumentAndTryLogin();
            this.isAuthenticatedSubject.next(this.oauthService.hasValidAccessToken());
        } catch (error) {
            console.error('OAuth configuration error:', error);
        }
    }

    /**
     * Initiate login flow - redirects to Keycloak
     */
    login(): void {
        this.oauthService.initCodeFlow();
    }

    /**
     * Logout and redirect to Keycloak logout
     */
    logout(): void {
        this.oauthService.logOut();
        this.isAuthenticatedSubject.next(false);
    }

    /**
     * Check if user has a valid access token
     */
    get isAuthenticated(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    /**
     * Get the current access token for API calls
     */
    get accessToken(): string {
        return this.oauthService.getAccessToken();
    }

    /**
     * Get the ID token claims (user info)
     */
    get identityClaims(): any {
        return this.oauthService.getIdentityClaims();
    }

    /**
     * Get username from token claims
     */
    get username(): string {
        const claims = this.identityClaims;
        return claims ? claims['preferred_username'] : '';
    }

    /**
     * Get user email from token claims
     */
    get email(): string {
        const claims = this.identityClaims;
        return claims ? claims['email'] : '';
    }

    /**
     * Get user roles from token
     */
    get roles(): string[] {
        const token = this.oauthService.getAccessToken();
        if (!token) return [];

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.realm_access?.roles || [];
        } catch {
            return [];
        }
    }

    /**
     * Check if user has a specific role
     */
    hasRole(role: string): boolean {
        return this.roles.includes(role);
    }
}
