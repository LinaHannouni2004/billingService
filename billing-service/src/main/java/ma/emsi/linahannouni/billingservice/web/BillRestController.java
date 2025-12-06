package ma.emsi.linahannouni.billingservice.web;

import ma.emsi.linahannouni.billingservice.entities.Bill;
import ma.emsi.linahannouni.billingservice.feign.CustomerRestClient;
import ma.emsi.linahannouni.billingservice.feign.ProductRestClient;
import ma.emsi.linahannouni.billingservice.repository.BillRepository;
import ma.emsi.linahannouni.billingservice.repository.productItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
@Autowired
    private BillRepository billRepository;
    @Autowired
    private productItemRepository productItemRepository;
    @Autowired
    private CustomerRestClient customerRestRepository;
    @Autowired
    private ProductRestClient productRestClient;
    @Autowired
    private CustomerRestClient customerRestClient;

    @GetMapping("/bills/{id}")
    public Bill getBill(@PathVariable long id) {
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.findCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;
    }
}
