package com.examle.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifiableProduct extends Product {
    protected String channel;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotifiableProduct{");
        sb.append("channel='").append(channel).append('\'');
        sb.append('}');
        return sb.toString();
    }
}