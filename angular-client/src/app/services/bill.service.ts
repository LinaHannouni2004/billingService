import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bill } from '../models/bill.model';

@Injectable({
    providedIn: 'root'
})
export class BillService {
    private apiUrl = '/api/billing-service/api/bills';

    constructor(private http: HttpClient) { }

    getBill(id: number): Observable<Bill> {
        return this.http.get<Bill>(`${this.apiUrl}/${id}`);
    }
}
