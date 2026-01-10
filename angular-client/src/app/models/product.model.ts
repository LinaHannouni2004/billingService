export interface Product {
    id: string;
    name: string;
    price: number;
    quantity: number;
}

export interface ProductResponse {
    _embedded?: {
        products: Product[];
    };
    _links?: any;
    page?: any;
}
