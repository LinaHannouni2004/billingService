export interface Customer {
    id: number;
    name: string;
    email: string;
}

export interface CustomerResponse {
    _embedded?: {
        customers: Customer[];
    };
    _links?: any;
    page?: any;
}
