package com.examle.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductBundle extends Product {
    protected int amount;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductBundle{");
        sb.append("amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}