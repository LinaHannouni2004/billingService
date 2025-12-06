package ma.emsi.linahannouni.billingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.linahannouni.billingservice.model.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bill {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date billingDate;
    private Long customerId;
    @OneToMany(mappedBy = "bill")
    private List<ProductItems> productItems=new ArrayList<>();
    @Transient
    private Customer customer;
}
