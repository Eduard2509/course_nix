package com.examle.repository;

import com.examle.model.Product;

import java.util.List;

public interface ProductRepository<K extends Product> {

    public K save(K product);

    public List<K> getAll();
}
