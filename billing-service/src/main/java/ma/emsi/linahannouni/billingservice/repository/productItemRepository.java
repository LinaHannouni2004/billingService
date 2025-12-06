package ma.emsi.linahannouni.billingservice.repository;

import ma.emsi.linahannouni.billingservice.entities.ProductItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface productItemRepository extends JpaRepository<ProductItems,Long> {
}
