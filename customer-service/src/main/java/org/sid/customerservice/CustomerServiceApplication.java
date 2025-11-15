package org.sid.customerservice;

import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository) {
        return args -> {

            customerRepository.save(Customer.builder()
                            .name("David")
                    .email("david@gmail.com")
                    .build());

            customerRepository.save(Customer.builder()
                    .name("lina")
                    .email("lina@gmail.com")
                    .build());

            customerRepository.save(Customer.builder()
                    .name("ayman")
                    .email("ayman@gmail.com")
                    .build());

            customerRepository.findAll().forEach(c-> {
                        System.out.println("=======================");
                        System.out.println(c.getId());
                        System.out.println(c.getName());
                        System.out.println(c.getEmail());
                        System.out.println("=======================");


                    }
            );
        };
    }



}
