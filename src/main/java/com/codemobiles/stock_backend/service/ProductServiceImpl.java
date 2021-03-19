package com.codemobiles.stock_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.codemobiles.stock_backend.controller.request.ProductRequest;
import com.codemobiles.stock_backend.exception.ProductNotFoundException;
import com.codemobiles.stock_backend.model.Product;
import com.codemobiles.stock_backend.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private StorageService storageService;
	private ProductRepository productsRepository;

	ProductServiceImpl(StorageService storageService, ProductRepository productsRepository) {
		this.storageService = storageService;
		this.productsRepository = productsRepository;
	}

	@Override
	public List<Product> getAllProducts() {
		return productsRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
	}

	@Override
	public Product getProductById(Long id) {
		Optional<Product> product = productsRepository.findById(id);
		if (product.isPresent()) {
			return product.get();
		}
		throw new ProductNotFoundException(id);
	}

	@Override
	public Product createProduct(ProductRequest productRequest) {
		String fileName = storageService.store(productRequest.getImage());
		Product data = new Product();
		data.setName(productRequest.getName()).setImage(fileName).setPrice(productRequest.getPrice())
				.setStock(productRequest.getStock());

		return productsRepository.save(data);
	}

	@Override
	public Product updateProduct(ProductRequest productRequest, Long id) {
		String fileName = storageService.store(productRequest.getImage());

		Optional<Product> product = productsRepository.findById(id);
		if (product.isPresent()) {
			Product existingProduct = product.get();
			if (fileName != null) {
				existingProduct.setImage(fileName);
			}
			existingProduct.setName(productRequest.getName()).setPrice(productRequest.getPrice())
					.setStock(productRequest.getStock());
			return productsRepository.save(existingProduct);
		}
		throw new ProductNotFoundException(id);
	}

	@Override
	public void deleteProduct(Long id) {
		try {
			productsRepository.deleteById(id);
		} catch (Exception e) {
			throw new ProductNotFoundException(id);
		}
	}

	@Override
	public Product getProductByName(String name) {
		Optional<Product> product = productsRepository.findTopByName(name);
		if (product.isPresent()) {
			return product.get();
		}
		throw new ProductNotFoundException(name);
	}

	@Override
	public List<Product> getProductByNameAndStock(String name, int stock) {
		return productsRepository.findByNameContainingAndStockGreaterThanOrderByStockDesc(name, stock);
	}

	@Override
	public List<Product> getProductOutOfStock() {
		return productsRepository.checkOutOfStock();
	}

	@Override
	public List<Product> getProductByNameAndPrice(String name, int price) {
		return productsRepository.searchNameAndPrice(name, price);
	}

}
