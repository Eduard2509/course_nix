package com.examle.model;

import lombok.Data;

@Data
public abstract class Product{
    protected long id;
    protected boolean available;
    protected String title;
    protected double price;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", available=").append(available);
        sb.append(", title='").append(title).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}