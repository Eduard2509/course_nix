package com.examle.service;

import com.examle.model.NotifiableProduct;
import com.examle.model.Product;
import com.examle.model.ProductBundle;
import com.examle.repository.ProductRepository;

import java.util.List;
import java.util.Random;

public class ProductService<O extends Product> {

    private static final NotifiableProductService NOTIFIABLE_PRODUCT_SERVICE = NotifiableProductService.getInstance();
    private static final ProductBundleService PRODUCT_BUNDLE_SERVICE = ProductBundleService.getInstance();

    private final ProductRepository<O> repository;

    public ProductService(ProductRepository<O> repository) {
        this.repository = repository;
    }

    public List<O> getAll() {
        return repository.getAll();
    }

    public void generateRandomProduct() {
        Random random = new Random();
        random.nextInt(0, 2);
        if (random.nextInt() < 1) {
            ProductBundle productBundle = PRODUCT_BUNDLE_SERVICE.generateRandomProductBundle();
            PRODUCT_BUNDLE_SERVICE.saveProductBundle(productBundle);
        } else {
            NotifiableProduct product = NOTIFIABLE_PRODUCT_SERVICE.generateRandomNotifiableProduct();
            NOTIFIABLE_PRODUCT_SERVICE.saveNotifiableProduct(product);

        }
    }
}
