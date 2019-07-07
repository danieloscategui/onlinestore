package com.devop.project.store.category;

import com.devop.project.store.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsProductByName(String name);

    boolean existsProductByNameAndIdNot(String name, Long id);

    @Query(value = "select p from Product p order by p.name asc")
    Collection<Product> findAllSorted();

    @Query(value = "select p from Product p where lower(trim(p.name)) like %:name% ")
    Collection<Product> searchProduct(@Param("name") String name);

    Collection<Product> findByNameContainingIgnoreCase(String name);

}
