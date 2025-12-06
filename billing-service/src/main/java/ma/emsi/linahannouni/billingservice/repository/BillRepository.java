package ma.emsi.linahannouni.billingservice.repository;

import ma.emsi.linahannouni.billingservice.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository   extends JpaRepository<Bill,Long> {

}
