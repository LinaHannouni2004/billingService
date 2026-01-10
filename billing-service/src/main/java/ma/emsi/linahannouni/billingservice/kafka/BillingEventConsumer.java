package ma.emsi.linahannouni.billingservice.kafka;

import ma.emsi.linahannouni.billingservice.events.BillCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Kafka consumer for receiving billing events
 */
@Component
public class BillingEventConsumer {

    /**
     * Consumer function that receives BillCreatedEvent from Kafka
     * This can be used for analytics, notifications, or other processing
     */
    @Bean
    public Consumer<BillCreatedEvent> billEventConsumer() {
        return event -> {
            System.out.println("======================================");
            System.out.println("📥 Received BillCreatedEvent from Kafka:");
            System.out.println("   Bill ID: " + event.billId());
            System.out.println("   Customer ID: " + event.customerId());
            System.out.println("   Billing Date: " + event.billingDate());
            System.out.println("   Item Count: " + event.itemCount());
            System.out.println("   Total Amount: " + event.totalAmount());
            System.out.println("======================================");

            // Add your business logic here:
            // - Send notifications
            // - Update analytics
            // - Trigger other workflows
        };
    }
}
