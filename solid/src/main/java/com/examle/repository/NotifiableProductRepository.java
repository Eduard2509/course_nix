package com.examle.repository;

import com.examle.model.NotifiableProduct;
import com.examle.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class NotifiableProductRepository implements ProductRepository<NotifiableProduct> {

    private static NotifiableProductRepository instance;

    public static NotifiableProductRepository getInstance() {
        if (instance == null) {
            instance = new NotifiableProductRepository();
        }
        return instance;
    }

    Storage<NotifiableProduct> storage = new Storage<>();

    @Override
    public NotifiableProduct save(NotifiableProduct product) {
        return storage.getStorage().put(product.getId(), product);
    }

    @Override
    public List<NotifiableProduct> getAll() {
        return new ArrayList<>(storage.getStorage().values());
    }


    public String generateAddressForNotification() {
        return "somerandommail@gmail.com";
    }

}
