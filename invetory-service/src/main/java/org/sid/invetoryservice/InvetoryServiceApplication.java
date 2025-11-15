package org.sid.invetoryservice;

import org.sid.invetoryservice.entities.Product;
import org.sid.invetoryservice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InvetoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvetoryServiceApplication.class, args);
    }



    @Bean
    CommandLineRunner init(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()

                    .id(UUID.randomUUID().toString())
                            .name("computer")
                            .price(3200)
                            .quantity(11)
                    .build()


            );
            productRepository.save(Product.builder()

                    .id(UUID.randomUUID().toString())
                    .name("Printer")
                    .price(3200)
                    .quantity(11)
                    .build()


            );
            productRepository.save(Product.builder()

                    .id(UUID.randomUUID().toString())
                    .name("Smart phone")
                    .price(3200)
                    .quantity(11)
                    .build()


            );


        };
    }
}
