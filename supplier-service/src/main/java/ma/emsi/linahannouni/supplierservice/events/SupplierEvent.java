package ma.emsi.linahannouni.supplierservice.events;

import java.util.Date;

public record SupplierEvent(String type, String productId, int quantity, Date timestamp) {
}
