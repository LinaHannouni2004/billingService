import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, RouterLink, RouterLinkActive],
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
  `]
})
export class AppComponent {
    title = 'Billing Service';
}
