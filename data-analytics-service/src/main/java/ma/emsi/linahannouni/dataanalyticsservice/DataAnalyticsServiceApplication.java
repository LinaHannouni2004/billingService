package ma.emsi.linahannouni.dataanalyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@SpringBootApplication
@RestController
public class DataAnalyticsServiceApplication {

    // Window-based event tracking (like kafka-spring-stream windowedBy)
    private final ConcurrentLinkedQueue<TimestampedEvent> orderEvents = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<TimestampedEvent> stockEvents = new ConcurrentLinkedQueue<>();
    private static final long WINDOW_SIZE_MS = 5000; // 5 second window like kafka-spring-stream

    public static void main(String[] args) {
        SpringApplication.run(DataAnalyticsServiceApplication.class, args);
    }

    // Consumer Kafka - Supplier Events
    @Bean
    public Consumer<SupplierEvent> supplierEventConsumer() {
        return event -> {
            System.out.println("� " + event.type() + " | " + event.productId());
            TimestampedEvent te = new TimestampedEvent(Instant.now());
            if ("ORDER".equals(event.type())) {
                orderEvents.add(te);
            } else {
                stockEvents.add(te);
            }
        };
    }

    // Count events in the last window
    private long countInWindow(ConcurrentLinkedQueue<TimestampedEvent> queue) {
        Instant cutoff = Instant.now().minusMillis(WINDOW_SIZE_MS);
        // Remove old events
        queue.removeIf(e -> e.timestamp().isBefore(cutoff));
        return queue.size();
    }

    // Streaming temps réel (comme kafka-spring-stream /analytics)
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Long>> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> {
                    Map<String, Long> data = new LinkedHashMap<>();
                    data.put("ORDER", countInWindow(orderEvents));
                    data.put("STOCK", countInWindow(stockEvents));
                    return data;
                });
    }

    // REST - Analytics summary
    @GetMapping("/analytics")
    public Map<String, Long> analytics() {
        Map<String, Long> data = new LinkedHashMap<>();
        data.put("ORDER", countInWindow(orderEvents));
        data.put("STOCK", countInWindow(stockEvents));
        return data;
    }

    // Records
    public record SupplierEvent(String type, String productId, int quantity, Date timestamp) {
    }

    record TimestampedEvent(Instant timestamp) {
    }
}
