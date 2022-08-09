package com.examle.service;

import com.examle.model.NotifiableProduct;
import com.examle.repository.NotifiableProductRepository;

import java.util.List;
import java.util.Random;

public class NotifiableProductService extends ProductService<NotifiableProduct> {

    private static NotifiableProductService instance;
    private final NotifiableProductRepository repository;

    public NotifiableProductService(NotifiableProductRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public static NotifiableProductService getInstance() {
        if (instance == null) {
            instance = new NotifiableProductService(NotifiableProductRepository.getInstance());
        }
        return instance;
    }

    @Override
    public List<NotifiableProduct> getAll() {
        return repository.getAll();
    }

    public void saveNotifiableProduct(NotifiableProduct product) {
        repository.save(product);
    }


    public NotifiableProduct generateRandomNotifiableProduct() {
        NotifiableProduct product = new NotifiableProduct();
        Random random = new Random();
        product.setId(random.nextLong());
        product.setTitle(random.nextFloat() + "" + random.nextDouble());
        product.setAvailable(random.nextBoolean());
        product.setChannel(random.nextBoolean() + "" + random.nextDouble());
        product.setPrice(random.nextDouble());

        return product;
    }


    public int getSentNotifications() {
        return repository.getAll().size();
    }


}