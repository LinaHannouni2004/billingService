package ma.emsi.linahannouni.billingservice.events;

import java.util.Date;

/**
 * Event published when a new bill is created
 */
public record BillCreatedEvent(
        Long billId,
        Long customerId,
        Date billingDate,
        int itemCount,
        Double totalAmount) {
}
