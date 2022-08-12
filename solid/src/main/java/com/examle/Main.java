package com.examle;

import com.examle.model.NotifiableProduct;
import com.examle.model.ProductBundle;
import com.examle.repository.NotifiableProductRepository;
import com.examle.service.NotifiableProductService;
import com.examle.service.ProductBundleService;
import com.examle.service.ProductService;

import java.util.List;

public class Main {
    private static final NotifiableProductService NOTIFIABLE_PRODUCT_SERVICE = NotifiableProductService.getInstance();
    private static final ProductBundleService PRODUCT_BUNDLE_SERVICE = ProductBundleService.getInstance();

    public static void main(String[] args) {
        ProductService<NotifiableProduct> utils = new ProductService(new NotifiableProductRepository());
        for (int i = 0; i < 7; i++) {
            utils.generateRandomProduct();
        }
        List<NotifiableProduct> products = NOTIFIABLE_PRODUCT_SERVICE.getAll();
        List<ProductBundle> productsBundle = PRODUCT_BUNDLE_SERVICE.getAll();
        products.forEach(System.out::println);
        productsBundle.forEach(System.out::println);
        System.out.println("sent notifications: " + NOTIFIABLE_PRODUCT_SERVICE.getSentNotifications());
        System.out.println("sent bundles: " + PRODUCT_BUNDLE_SERVICE.getSentBundles());
    }
}
