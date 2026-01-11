import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="navbar-container">
        <a class="navbar-brand" routerLink="/">
          <span class="logo">📦</span>
          <span>Billing Service</span>
        </a>
        <ul class="navbar-nav">
          <li>
            <a routerLink="/products" routerLinkActive="active">
              <span>🛍️</span> Products
            </a>
          </li>
          <li>
            <a routerLink="/customers" routerLinkActive="active">
              <span>👥</span> Customers
            </a>
          </li>
          <li>
            <a routerLink="/bills" routerLinkActive="active">
              <span>🧾</span> Bills
            </a>
          </li>
        </ul>
        
        <!-- Auth Section -->
        <div class="auth-section">
          <ng-container *ngIf="authService.isAuthenticated; else loginButton">
            <div class="user-info">
              <span class="user-icon">👤</span>
              <span class="username">{{ authService.username }}</span>
              <button class="btn-logout" (click)="logout()">
                Déconnexion
              </button>
            </div>
          </ng-container>
          <ng-template #loginButton>
            <button class="btn-login" (click)="login()">
              🔐 Se connecter
            </button>
          </ng-template>
        </div>
      </div>
    </nav>
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    main {
      min-height: calc(100vh - 80px);
    }
    
    .auth-section {
      display: flex;
      align-items: center;
      margin-left: auto;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 8px 16px;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 25px;
    }
    
    .user-icon {
      font-size: 1.2rem;
    }
    
    .username {
      color: white;
      font-weight: 500;
    }
    
    .btn-login, .btn-logout {
      padding: 10px 20px;
      border: none;
      border-radius: 25px;
      cursor: pointer;
      font-size: 0.9rem;
      font-weight: 600;
      transition: all 0.3s ease;
    }
    
    .btn-login {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }
    
    .btn-login:hover {
      transform: scale(1.05);
      box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    }
    
    .btn-logout {
      background: rgba(255, 255, 255, 0.2);
      color: white;
      border: 1px solid rgba(255, 255, 255, 0.3);
    }
    
    .btn-logout:hover {
      background: #ff4757;
      border-color: #ff4757;
    }
  `]
})
export class AppComponent {
  title = 'Billing Service';

  constructor(public authService: AuthService) { }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }
}
