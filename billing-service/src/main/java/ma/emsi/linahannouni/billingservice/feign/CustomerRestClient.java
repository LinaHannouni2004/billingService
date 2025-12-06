package ma.emsi.linahannouni.billingservice.feign;

import ma.emsi.linahannouni.billingservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "Customer-service")
public interface CustomerRestClient {
    @GetMapping("/api/customers/{id}")

     Customer findCustomerById(@PathVariable Long id);

    @GetMapping("/api/customers")
    PagedModel<Customer> getAllCustomers();
}
