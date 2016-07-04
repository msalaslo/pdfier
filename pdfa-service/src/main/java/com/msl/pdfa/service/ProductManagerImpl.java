package com.msl.pdfa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msl.pdfa.domain.Product;
import com.msl.pdfa.repository.ProductDao;

@Component
public class ProductManagerImpl implements ProductManager {

	private static final long serialVersionUID = 1L;
	
    @Autowired
    private ProductDao productDao;

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

	public List<Product> getProducts() {
		return productDao.getProductList();
	}

	public void increasePrice(int percentage) {
        List<Product> products = productDao.getProductList();
        if (products != null) {
            for (Product product : products) {
                double newPrice = product.getPrice().doubleValue() * 
                                    (100 + percentage)/100;
                product.setPrice(newPrice);
                productDao.saveProduct(product);
            }
        }
    }
}