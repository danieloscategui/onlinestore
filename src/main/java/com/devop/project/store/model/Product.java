package com.devop.project.store.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long id;

    @NotBlank(message = "{product.name.notBlank}")
    @Column
    private String name;

    @NotBlank(message = "{product.code.notBlank}")
    @Column
    private String code;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id_category")
    private Category category;

    @Column
    private String description;

    @NotNull(message = "{product.price.notNull}")
    @Column
    private BigDecimal price;
}