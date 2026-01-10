import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Customer, CustomerResponse } from '../models/customer.model';

@Injectable({
    providedIn: 'root'
})
export class CustomerService {
    private apiUrl = '/api/customer-service/api/customers';

    constructor(private http: HttpClient) { }

    getCustomers(): Observable<Customer[]> {
        return this.http.get<CustomerResponse>(this.apiUrl).pipe(
            map(response => response._embedded?.customers || [])
        );
    }

    getCustomer(id: number): Observable<Customer> {
        return this.http.get<Customer>(`${this.apiUrl}/${id}`);
    }
}
