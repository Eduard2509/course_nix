package com.examle.service;

import com.examle.model.ProductBundle;
import com.examle.repository.ProductBundleRepository;

import java.util.List;
import java.util.Random;

public class ProductBundleService extends ProductService<ProductBundle> {

    private final ProductBundleRepository repository;
    private static ProductBundleService instance;

    public ProductBundleService(ProductBundleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<ProductBundle> getAll() {
        return repository.getAll();
    }

    public static ProductBundleService getInstance() {
        if (instance == null) {
            instance = new ProductBundleService(ProductBundleRepository.getInstance());
        }
        return instance;
    }

    public void saveProductBundle(ProductBundle product) {
        repository.save(product);
    }

    public ProductBundle generateRandomProductBundle() {
        ProductBundle productBundle = new ProductBundle();
        Random random = new Random();
        productBundle.setAmount(random.nextInt(15));
        productBundle.setAvailable(random.nextBoolean());
        productBundle.setPrice(random.nextDouble());
        productBundle.setId(random.nextLong());
        productBundle.setTitle(random.nextFloat() + "" + random.nextDouble());
        return productBundle;
    }

    public int getSentBundles() {
        return repository.getAll().size();
    }
}
