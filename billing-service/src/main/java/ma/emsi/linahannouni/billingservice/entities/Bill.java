package ma.emsi.linahannouni.billingservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill {

@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date billingDate;
    private String customerId;
    @OneToMany(mappedBy = "bill")
    private List<ProductItems> productItems=new ArrayList<>();
}
