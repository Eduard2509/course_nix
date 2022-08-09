package com.examle.storage;

import com.examle.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Storage<O extends Product> {

    private Map<Long, O> storage = new HashMap<>();

}
