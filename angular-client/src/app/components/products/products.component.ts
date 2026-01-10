import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
    selector: 'app-products',
    standalone: true,
    imports: [CommonModule, FormsModule],
    template: `
    <div class="container">
      <div class="page-header">
        <h1>🛍️ Products</h1>
        <p>Manage your product inventory</p>
      </div>

      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon primary">📦</div>
          <div class="stat-content">
            <h3>{{ products.length }}</h3>
            <p>Total Products</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon success">💰</div>
          <div class="stat-content">
            <h3>{{ getTotalValue() | currency:'EUR' }}</h3>
            <p>Total Inventory Value</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon warning">📊</div>
          <div class="stat-content">
            <h3>{{ getTotalQuantity() }}</h3>
            <p>Total Items</p>
          </div>
        </div>
      </div>

      <div class="grid grid-3">
        <!-- Add Product Form -->
        <div class="card">
          <div class="card-header">
            <h2 class="card-title">➕ Add New Product</h2>
          </div>

          @if (successMessage) {
            <div class="alert alert-success">{{ successMessage }}</div>
          }
          @if (errorMessage) {
            <div class="alert alert-error">{{ errorMessage }}</div>
          }

          <form (ngSubmit)="addProduct()">
            <div class="form-group">
              <label class="form-label">Product ID</label>
              <input type="text" class="form-control" 
                     [(ngModel)]="newProduct.id" 
                     name="id" 
                     placeholder="e.g., P001" required>
            </div>
            <div class="form-group">
              <label class="form-label">Product Name</label>
              <input type="text" class="form-control" 
                     [(ngModel)]="newProduct.name" 
                     name="name" 
                     placeholder="e.g., MacBook Pro" required>
            </div>
            <div class="form-group">
              <label class="form-label">Price (€)</label>
              <input type="number" class="form-control" 
                     [(ngModel)]="newProduct.price" 
                     name="price" 
                     placeholder="e.g., 1299.99" 
                     step="0.01" required>
            </div>
            <div class="form-group">
              <label class="form-label">Quantity</label>
              <input type="number" class="form-control" 
                     [(ngModel)]="newProduct.quantity" 
                     name="quantity" 
                     placeholder="e.g., 50" required>
            </div>
            <button type="submit" class="btn btn-primary" [disabled]="loading">
              @if (loading) {
                <span class="spinner" style="width:20px;height:20px;border-width:2px;"></span>
              } @else {
                <span>Add Product</span>
              }
            </button>
          </form>
        </div>

        <!-- Products Table -->
        <div class="card" style="grid-column: span 2;">
          <div class="card-header">
            <h2 class="card-title">📋 Product List</h2>
            <button class="btn btn-primary btn-sm" (click)="loadProducts()">🔄 Refresh</button>
          </div>

          @if (loading && products.length === 0) {
            <div class="loading">
              <div class="spinner"></div>
            </div>
          } @else if (products.length === 0) {
            <div class="empty-state">
              <p>No products found. Add your first product!</p>
            </div>
          } @else {
            <div class="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  @for (product of products; track product.id) {
                    <tr>
                      <td><code>{{ product.id }}</code></td>
                      <td>{{ product.name }}</td>
                      <td class="price">{{ product.price | currency:'EUR' }}</td>
                      <td>
                        <span class="badge" [class.badge-success]="product.quantity > 10" 
                              [class.badge-warning]="product.quantity <= 10 && product.quantity > 0"
                              [class.badge-danger]="product.quantity === 0">
                          {{ product.quantity }} units
                        </span>
                      </td>
                      <td>
                        <button class="btn btn-danger btn-sm" (click)="deleteProduct(product.id)">
                          🗑️ Delete
                        </button>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        </div>
      </div>
    </div>
  `,
    styles: [`
    code {
      background: rgba(99, 102, 241, 0.2);
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 0.85rem;
      color: var(--primary-light);
    }
  `]
})
export class ProductsComponent implements OnInit {
    products: Product[] = [];
    newProduct: Product = { id: '', name: '', price: 0, quantity: 0 };
    loading = false;
    successMessage = '';
    errorMessage = '';

    constructor(private productService: ProductService) { }

    ngOnInit(): void {
        this.loadProducts();
    }

    loadProducts(): void {
        this.loading = true;
        this.productService.getProducts().subscribe({
            next: (products) => {
                this.products = products;
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading products:', err);
                this.errorMessage = 'Failed to load products. Make sure backend services are running.';
                this.loading = false;
            }
        });
    }

    addProduct(): void {
        if (!this.newProduct.id || !this.newProduct.name) return;

        this.loading = true;
        this.successMessage = '';
        this.errorMessage = '';

        this.productService.createProduct(this.newProduct).subscribe({
            next: () => {
                this.successMessage = `Product "${this.newProduct.name}" added successfully!`;
                this.newProduct = { id: '', name: '', price: 0, quantity: 0 };
                this.loadProducts();
            },
            error: (err) => {
                console.error('Error adding product:', err);
                this.errorMessage = 'Failed to add product. Please try again.';
                this.loading = false;
            }
        });
    }

    deleteProduct(id: string): void {
        if (confirm('Are you sure you want to delete this product?')) {
            this.productService.deleteProduct(id).subscribe({
                next: () => {
                    this.successMessage = 'Product deleted successfully!';
                    this.loadProducts();
                },
                error: (err) => {
                    console.error('Error deleting product:', err);
                    this.errorMessage = 'Failed to delete product.';
                }
            });
        }
    }

    getTotalValue(): number {
        return this.products.reduce((sum, p) => sum + (p.price * p.quantity), 0);
    }

    getTotalQuantity(): number {
        return this.products.reduce((sum, p) => sum + p.quantity, 0);
    }
}
