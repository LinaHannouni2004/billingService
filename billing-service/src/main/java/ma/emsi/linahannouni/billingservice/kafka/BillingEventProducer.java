package ma.emsi.linahannouni.billingservice.kafka;

import ma.emsi.linahannouni.billingservice.entities.Bill;
import ma.emsi.linahannouni.billingservice.entities.ProductItems;
import ma.emsi.linahannouni.billingservice.events.BillCreatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/**
 * Kafka producer service for publishing billing events
 */
@Service
public class BillingEventProducer {

    private final StreamBridge streamBridge;

    public BillingEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    /**
     * Publish a BillCreatedEvent to Kafka when a bill is created
     */
    public void publishBillCreated(Bill bill) {
        // Calculate total amount from product items
        double totalAmount = bill.getProductItems().stream()
                .mapToDouble(item -> item.getUnitprice() * item.getQuantity())
                .sum();

        BillCreatedEvent event = new BillCreatedEvent(
                bill.getId(),
                bill.getCustomerId(),
                bill.getBillingDate(),
                bill.getProductItems().size(),
                totalAmount);

        boolean sent = streamBridge.send("billEventProducer-out-0", event);
        if (sent) {
            System.out.println("✅ Published BillCreatedEvent: " + event);
        } else {
            System.err.println("❌ Failed to publish BillCreatedEvent for bill: " + bill.getId());
        }
    }
}
