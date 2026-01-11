import { Routes } from '@angular/router';
import { ProductsComponent } from './components/products/products.component';
import { CustomersComponent } from './components/customers/customers.component';
import { BillsComponent } from './components/bills/bills.component';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/products', pathMatch: 'full' },
    {
        path: 'products',
        component: ProductsComponent,
        canActivate: [authGuard]  // Protected route
    },
    {
        path: 'customers',
        component: CustomersComponent,
        canActivate: [authGuard]  // Protected route
    },
    {
        path: 'bills',
        component: BillsComponent,
        canActivate: [authGuard]  // Protected route
    }
];
