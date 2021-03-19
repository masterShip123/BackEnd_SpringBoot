package com.codemobiles.stock_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codemobiles.stock_backend.model.Product;

// DAO
public interface ProductRepository extends JpaRepository<Product, Long> {

	// SELECT * FROM Product WHERE name = 'foo' LIMIT 1
	Optional<Product> findTopByName(String name);

	// SELECT * FROM Product WHERE name LIKE '%foo%' AND stock > x order by stock
	// desc
	List<Product> findByNameContainingAndStockGreaterThanOrderByStockDesc(String name, int stock);

	@Query(value = "SELECT * FROM PRODUCT WHERE STOCK = 0", nativeQuery = true)
	List<Product> checkOutOfStock();

	@Query(value = "SELECT * FROM Product WHERE name LIKE %:product_name% AND price > :price", nativeQuery = true)
	List<Product> searchNameAndPrice(@Param("product_name") String name, int price);
}
