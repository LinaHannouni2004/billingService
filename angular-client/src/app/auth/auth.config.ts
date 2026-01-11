import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
    // Keycloak server URL
    issuer: 'http://localhost:8080/realms/billing-realm',

    // Redirect URI after login
    redirectUri: window.location.origin,

    // Client ID registered in Keycloak
    clientId: 'angular-client',

    // OAuth2/OIDC response type - Authorization Code Flow
    responseType: 'code',

    // Scopes to request
    scope: 'openid profile email',

    // Show debug information in console
    showDebugInformation: true,

    // PKCE is enabled by default in angular-oauth2-oidc

    // Allow HTTP for local development (set to true in production)
    requireHttps: false,

    // Token validation
    strictDiscoveryDocumentValidation: false,

    // Silent refresh settings
    silentRefreshRedirectUri: window.location.origin + '/silent-refresh.html',

    // Session checks
    sessionChecksEnabled: true,

    // Logout URL
    postLogoutRedirectUri: window.location.origin
};
