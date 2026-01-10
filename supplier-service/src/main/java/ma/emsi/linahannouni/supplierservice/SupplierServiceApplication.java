package ma.emsi.linahannouni.supplierservice;

import ma.emsi.linahannouni.supplierservice.events.SupplierEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;
import java.util.function.Supplier;

@SpringBootApplication
@RestController
public class SupplierServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierServiceApplication.class, args);
    }

    // Kafka Supplier - publie automatiquement des événements
    @Bean
    public Supplier<SupplierEvent> supplierEventSupplier() {
        return () -> new SupplierEvent(
                Math.random() > 0.5 ? "ORDER" : "STOCK",
                "P" + (1 + new Random().nextInt(5)),
                1 + new Random().nextInt(100),
                new Date());
    }

    // REST endpoint pour publier manuellement
    private final StreamBridge streamBridge;

    public SupplierServiceApplication(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping("/publish")
    public SupplierEvent publish(@RequestParam String type, @RequestParam String productId, @RequestParam int qty) {
        SupplierEvent event = new SupplierEvent(type, productId, qty, new Date());
        streamBridge.send("supplier-events", event);
        return event;
    }
}
