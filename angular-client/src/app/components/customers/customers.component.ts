import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
    selector: 'app-customers',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="container">
      <div class="page-header">
        <h1>👥 Customers</h1>
        <p>View all registered customers</p>
      </div>

      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon primary">👥</div>
          <div class="stat-content">
            <h3>{{ customers.length }}</h3>
            <p>Total Customers</p>
          </div>
        </div>
      </div>

      <!-- Customers Card -->
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">📋 Customer List</h2>
          <button class="btn btn-primary btn-sm" (click)="loadCustomers()">🔄 Refresh</button>
        </div>

        @if (loading) {
          <div class="loading">
            <div class="spinner"></div>
          </div>
        } @else if (errorMessage) {
          <div class="alert alert-error">{{ errorMessage }}</div>
        } @else if (customers.length === 0) {
          <div class="empty-state">
            <p>No customers found.</p>
          </div>
        } @else {
          <div class="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                @for (customer of customers; track customer.id) {
                  <tr>
                    <td>
                      <span class="badge badge-success">#{{ customer.id }}</span>
                    </td>
                    <td>{{ customer.name }}</td>
                    <td>
                      <a href="mailto:{{ customer.email }}" class="email-link">
                        📧 {{ customer.email }}
                      </a>
                    </td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        }
      </div>
    </div>
  `,
    styles: [`
    .email-link {
      color: var(--primary-light);
      text-decoration: none;
      transition: color 0.2s;
    }
    .email-link:hover {
      color: var(--primary);
    }
  `]
})
export class CustomersComponent implements OnInit {
    customers: Customer[] = [];
    loading = false;
    errorMessage = '';

    constructor(private customerService: CustomerService) { }

    ngOnInit(): void {
        this.loadCustomers();
    }

    loadCustomers(): void {
        this.loading = true;
        this.errorMessage = '';

        this.customerService.getCustomers().subscribe({
            next: (customers) => {
                this.customers = customers;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading customers:', err);
                this.errorMessage = 'Failed to load customers. Make sure backend services are running.';
                this.loading = false;
            }
        });
    }
}
