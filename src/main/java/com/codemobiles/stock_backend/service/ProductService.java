package com.codemobiles.stock_backend.service;

import java.util.List;

import com.codemobiles.stock_backend.controller.request.ProductRequest;
import com.codemobiles.stock_backend.model.Product;

public interface ProductService {
	List<Product> getAllProducts();

	Product getProductById(Long id);

	Product createProduct(ProductRequest productRequest);

	Product updateProduct(ProductRequest productRequest, Long id);

	void deleteProduct(Long id);

	Product getProductByName(String name);

	List<Product> getProductByNameAndStock(String name, int stock);

	List<Product> getProductOutOfStock();

	List<Product> getProductByNameAndPrice(String name, int price);

}
