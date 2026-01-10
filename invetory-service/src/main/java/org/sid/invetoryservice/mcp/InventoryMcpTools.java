package org.sid.invetoryservice.mcp;


import org.sid.invetoryservice.entities.Product;
import org.sid.invetoryservice.repositories.ProductRepository;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryMcpTools {

    private final ProductRepository repo;

    public InventoryMcpTools(ProductRepository repo) {
        this.repo = repo;
    }

    @McpTool(description = "Get all products")
    public List<Product> getProducts() {
        return repo.findAll();
    }
}

