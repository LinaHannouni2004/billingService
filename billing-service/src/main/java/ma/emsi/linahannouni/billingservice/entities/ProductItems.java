package ma.emsi.linahannouni.billingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItems {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String productId;
    @ManyToOne
    private Bill bill;
    private int   quantity;
    private double unitprice;

}
