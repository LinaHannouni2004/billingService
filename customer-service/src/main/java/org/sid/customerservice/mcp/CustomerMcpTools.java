package org.sid.customerservice.mcp;


import org.sid.customerservice.entities.Customer;
import org.sid.customerservice.repositories.CustomerRepository;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMcpTools {

    private final CustomerRepository repo;

    public CustomerMcpTools(CustomerRepository repo) {
        this.repo = repo;
    }

    @McpTool(description = "Get all customers")
    public List<Customer> getCustomers() {
        return repo.findAll();
    }

    @McpTool(description = "Get customer by id")
    public Customer getCustomer(Long id) {
        return repo.findById(id).orElse(null);
    }
}

