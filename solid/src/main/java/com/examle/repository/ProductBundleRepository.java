package com.examle.repository;

import com.examle.model.ProductBundle;
import com.examle.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class ProductBundleRepository implements ProductRepository<ProductBundle> {

    private static ProductBundleRepository instance;
    ProductBundle productBundle = new ProductBundle();
    Storage<ProductBundle> storage = new Storage<>();

    public static ProductBundleRepository getInstance() {
        if (instance == null) {
            instance = new ProductBundleRepository();
        }
        return instance;
    }

    public int getAmountInBundle() {
        return productBundle.getAmount();
    }

    @Override
    public ProductBundle save(ProductBundle product) {
        return storage.getStorage().put(product.getId(), product);
    }

    @Override
    public List<ProductBundle> getAll() {
        return new ArrayList<>(storage.getStorage().values());
    }
}
