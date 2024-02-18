package backend.sculptor.domain.stone.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public abstract class Product {
    @Id @GeneratedValue
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;
}