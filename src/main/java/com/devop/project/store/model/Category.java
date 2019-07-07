package com.devop.project.store.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long id;
    @Column
    private String name;
    @Column
    private String code;
    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
