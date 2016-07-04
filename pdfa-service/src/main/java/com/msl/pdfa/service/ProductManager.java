package com.msl.pdfa.service;

import java.io.Serializable;
import java.util.List;

import com.msl.pdfa.domain.Product;

public interface ProductManager extends Serializable {

    public void increasePrice(int percentage);
    
    public List<Product> getProducts();

}
