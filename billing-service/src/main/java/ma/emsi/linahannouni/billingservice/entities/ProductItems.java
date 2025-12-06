package ma.emsi.linahannouni.billingservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.linahannouni.billingservice.model.Product;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Bill bill;
    private int   quantity;
    private double unitprice;
    @Transient
    private Product product;

}
