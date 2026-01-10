import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BillService } from '../../services/bill.service';
import { Bill } from '../../models/bill.model';

@Component({
    selector: 'app-bills',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
    <div class="container">
      <div class="page-header">
        <h1>🧾 Bills</h1>
        <p>Search and view invoice details</p>
      </div>

      <!-- Search Card -->
      <div class="card" style="margin-bottom: 1.5rem;">
        <div class="card-header">
          <h2 class="card-title">🔍 Search Bill</h2>
        </div>
        <div class="search-form">
          <div class="form-group" style="margin-bottom: 0;">
            <label class="form-label">Bill ID</label>
            <div class="search-input-group">
              <input type="number" class="form-control" 
                     [(ngModel)]="searchId" 
                     placeholder="Enter bill ID (e.g., 1)" 
                     (keyup.enter)="searchBill()">
              <button class="btn btn-primary" (click)="searchBill()" [disabled]="loading">
                @if (loading) {
                  <span class="spinner" style="width:20px;height:20px;border-width:2px;"></span>
                } @else {
                  <span>🔍 Search</span>
                }
              </button>
            </div>
          </div>
        </div>
      </div>

      @if (errorMessage) {
        <div class="alert alert-error">{{ errorMessage }}</div>
      }

      <!-- Bill Details -->
      @if (bill) {
        <div class="card">
          <div class="card-header">
            <h2 class="card-title">📄 Bill #{{ bill.id }}</h2>
            <span class="badge badge-success">Confirmed</span>
          </div>

          <div class="bill-info">
            <div class="info-grid">
              <div class="info-item">
                <span class="label">📅 Date</span>
                <span class="value">{{ bill.billingDate | date:'longDate' }}</span>
              </div>
              <div class="info-item">
                <span class="label">👤 Customer</span>
                <span class="value">
                  @if (bill.customer) {
                    {{ bill.customer.name }} ({{ bill.customer.email }})
                  } @else {
                    Customer ID: {{ bill.customerId }}
                  }
                </span>
              </div>
            </div>
          </div>

          <h3 class="section-title">🛒 Items</h3>
          <div class="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Quantity</th>
                  <th>Unit Price</th>
                  <th>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                @for (item of bill.productItems; track item.id) {
                  <tr>
                    <td>
                      @if (item.product) {
                        {{ item.product.name }}
                      } @else {
                        Product ID: {{ item.productId }}
                      }
                    </td>
                    <td>{{ item.quantity }}</td>
                    <td class="price">{{ item.unitprice | currency:'EUR' }}</td>
                    <td class="price">{{ item.quantity * item.unitprice | currency:'EUR' }}</td>
                  </tr>
                }
              </tbody>
              <tfoot>
                <tr class="total-row">
                  <td colspan="3"><strong>Total</strong></td>
                  <td class="price"><strong>{{ getTotal() | currency:'EUR' }}</strong></td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>
      } @else if (!loading && !errorMessage) {
        <div class="card">
          <div class="empty-state">
            <p>🔍 Enter a bill ID above to view invoice details</p>
          </div>
        </div>
      }
    </div>
  `,
    styles: [`
    .search-input-group {
      display: flex;
      gap: 1rem;
    }
    .search-input-group .form-control {
      flex: 1;
    }
    .bill-info {
      padding: 1rem 0;
      margin-bottom: 1.5rem;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }
    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
    }
    .info-item {
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
    }
    .info-item .label {
      color: var(--gray);
      font-size: 0.875rem;
    }
    .info-item .value {
      color: var(--light);
      font-weight: 500;
    }
    .section-title {
      margin: 1rem 0;
      font-size: 1rem;
      color: var(--primary-light);
    }
    tfoot {
      border-top: 2px solid rgba(99, 102, 241, 0.3);
    }
    .total-row td {
      padding-top: 1rem;
      font-size: 1.1rem;
    }
  `]
})
export class BillsComponent {
    searchId: number | null = null;
    bill: Bill | null = null;
    loading = false;
    errorMessage = '';

    constructor(private billService: BillService) { }

    searchBill(): void {
        if (!this.searchId) {
            this.errorMessage = 'Please enter a valid bill ID';
            return;
        }

        this.loading = true;
        this.errorMessage = '';
        this.bill = null;

        this.billService.getBill(this.searchId).subscribe({
            next: (bill) => {
                this.bill = bill;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error fetching bill:', err);
                this.errorMessage = `Bill #${this.searchId} not found. Make sure the bill exists and backend services are running.`;
                this.loading = false;
            }
        });
    }

    getTotal(): number {
        if (!this.bill) return 0;
        return this.bill.productItems.reduce((sum, item) => sum + (item.quantity * item.unitprice), 0);
    }
}
