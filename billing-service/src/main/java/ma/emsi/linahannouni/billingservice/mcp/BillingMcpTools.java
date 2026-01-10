package ma.emsi.linahannouni.billingservice.mcp;


import ma.emsi.linahannouni.billingservice.entities.Bill;
import ma.emsi.linahannouni.billingservice.repository.BillRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingMcpTools {

    private final BillRepository billRepository;

    public BillingMcpTools(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @McpTool(
            name = "getBillById",
            description = "Get a bill with customer and products"
    )
    public Bill getBill(
            @McpArg(description = "bill id") long id
    ) {
        return billRepository.findById(id).orElse(null);
    }

    @McpTool(
            name = "getAllBill",
            description = "Get all the bills with customers and products"
    )
    public List<Bill> getAllBill(

    ) {
        return billRepository.findAll();
    }
}


