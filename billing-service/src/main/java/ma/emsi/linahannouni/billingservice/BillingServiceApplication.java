package ma.emsi.linahannouni.billingservice;

import ma.emsi.linahannouni.billingservice.entities.Bill;
import ma.emsi.linahannouni.billingservice.entities.ProductItems;
import ma.emsi.linahannouni.billingservice.feign.CustomerRestClient;
import ma.emsi.linahannouni.billingservice.feign.ProductRestClient;
import ma.emsi.linahannouni.billingservice.kafka.BillingEventProducer;
import ma.emsi.linahannouni.billingservice.model.Customer;
import ma.emsi.linahannouni.billingservice.model.Product;
import ma.emsi.linahannouni.billingservice.repository.BillRepository;
import ma.emsi.linahannouni.billingservice.repository.productItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BillRepository billRepository,
            productItemRepository productItemRepository,
            CustomerRestClient customerRestClient,
            ProductRestClient productRestClient,
            BillingEventProducer billingEventProducer) {

        return args -> {
            // Retry logic to wait for services to be available in Eureka
            int maxRetries = 5;
            int retryDelayMs = 5000; // 5 seconds between retries

            Collection<Customer> customers = null;
            Collection<Product> products = null;

            for (int i = 0; i < maxRetries; i++) {
                try {
                    System.out.println("Attempting to fetch customers and products (attempt " + (i + 1) + "/"
                            + maxRetries + ")...");
                    customers = customerRestClient.getAllCustomers().getContent();
                    products = productRestClient.getAllProducts().getContent();
                    System.out.println("Successfully connected to customer-service and inventory-service!");
                    break; // Success, exit retry loop
                } catch (Exception e) {
                    System.err.println("Services not available yet: " + e.getMessage());
                    if (i < maxRetries - 1) {
                        System.out.println("Waiting " + (retryDelayMs / 1000) + " seconds before retry...");
                        Thread.sleep(retryDelayMs);
                    } else {
                        System.err.println("Max retries reached. Skipping data initialization.");
                        System.err.println(
                                "Make sure customer-service and inventory-service are running and registered with Eureka.");
                        return; // Exit without crashing
                    }
                }
            }

            if (customers == null || products == null) {
                System.err.println("Could not fetch customers or products. Skipping data initialization.");
                return;
            }

            List<Product> productList = new ArrayList<>(products);

            for (Customer customer : customers) {
                Bill bill = Bill.builder()
                        .billingDate(new Date())
                        .customerId(customer.getId())
                        .productItems(new ArrayList<>())
                        .build();
                billRepository.save(bill);

                for (Product product : productList) {
                    ProductItems productItem = ProductItems.builder()
                            .bill(bill)
                            .productId(product.getId())
                            .quantity(1 + new Random().nextInt(10))
                            .unitprice(product.getPrice())
                            .build();

                    productItemRepository.save(productItem);
                    bill.getProductItems().add(productItem);
                }

                // Publish bill created event to Kafka
                billingEventProducer.publishBillCreated(bill);
            }
            System.out.println("Data initialization completed successfully!");
        };
    }
}
