package ma.emsi.linahannouni.billingservice.feign;

import ma.emsi.linahannouni.billingservice.model.Customer;
import ma.emsi.linahannouni.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "invetory-service")
public interface ProductRestClient {
    @GetMapping("/api/products/{id}")

    public Product getProductById(@PathVariable  String id);

    @GetMapping("/api/products")
    PagedModel<Product> getAllProducts();
}
