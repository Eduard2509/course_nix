package com.repository;

import com.config.MongoConfig;
import com.model.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.*;

public class MongoBusinessAutoRepository implements CrudRepository<BusinessAuto>{

    MongoConfig mongoConfig = new MongoConfig();
    MongoDatabase database = mongoConfig.connect("course_nix");

    private static MongoBusinessAutoRepository instance;

    public static MongoBusinessAutoRepository getInstance() {
        if (instance == null) {
            instance = new MongoBusinessAutoRepository();
        }
        return instance;
    }

    private static Document mapperFrom(BusinessAuto businessAuto) {
        Document document = new Document();
        document.append("id", businessAuto.getId());
        document.append("model", businessAuto.getModel());
        document.append("manufacturer", businessAuto.getManufacturer().name());
        document.append("price", businessAuto.getPrice());
        document.append("business_class_auto", businessAuto.getBusinessClassAuto().name());
        document.append("count", businessAuto.getCount());
        return document;
    }

    private static BusinessAuto mapperTo(Document document) {
        final BusinessAuto businessAuto = new BusinessAuto(
                document.getString("id"),
                document.getString("model"),
                Manufacturer.valueOf(document.getString("manufacturer")),
                BigDecimal.valueOf(document.get("price", Decimal128.class).doubleValue()),
                BusinessClassAuto.valueOf(document.getString("business_class_auto")),
                document.getInteger("count")
        );
        return businessAuto;
    }

    @Override
    public Optional<BusinessAuto> findById(String id) {
        Optional<BusinessAuto> businessAuto = Optional.empty();
        final Document filter = new Document();
        filter.append("id", id);
        MongoCollection<Document> autos = database.getCollection("BusinessAuto");
        FindIterable<Document> documents = autos.find(filter);
        for (Document document : documents) {
            businessAuto = Optional.of(mapperTo(document));
        }
        return businessAuto;
    }

    @Override
    public List<BusinessAuto> getAll() {
        List<BusinessAuto> businessAutos = new ArrayList<>();
        MongoCollection<Document> autos = database.getCollection("BusinessAuto");
        FindIterable<Document> documents = autos.find();
        for (Document doc : documents) {
            businessAutos.add(mapperTo(doc));
        }
        return businessAutos;
    }

    @Override
    public boolean save(BusinessAuto businessAuto) {
        MongoCollection<Document> autos = database.getCollection("BusinessAuto");
        Document document = mapperFrom(businessAuto);
        autos.insertOne(document);
        return true;
    }

    @Override
    public boolean saveAll(List<BusinessAuto> auto) {
        if (auto == null) {
            return false;
        }
        auto.forEach(this::save);
        return true;
    }


    @Override
    public boolean update(BusinessAuto businessAuto) {
        final Document filter = new Document();
        filter.append("id", businessAuto.getId());
        final Document newData = new Document();
        newData.append("model", businessAuto.getModel());
        newData.append("manufacturer", businessAuto.getManufacturer().name());
        newData.append("price", businessAuto.getPrice());
        newData.append("business_class_auto", businessAuto.getBusinessClassAuto().name());
        newData.append("count", businessAuto.getCount());
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("BusinessAuto");
        sportAutos.updateOne(filter, updateObject);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Document filter = new Document();
        filter.append("id", id);

        MongoCollection<Document> businessAutos = database.getCollection("BusinessAuto");
        businessAutos.deleteOne(filter);
        return true;
    }

    @Override
    public void clear() {

    }

    @SneakyThrows
    public void setInvoiceId(String idAuto, String invoiceId) {
        final Document filter = new Document();
        filter.append("id", idAuto);
        final Document newData = new Document();
        newData.append("invoiceId", invoiceId);
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("BusinessAuto");
        sportAutos.updateOne(filter, updateObject);
    }

    public Set<BusinessAuto> getVehiclesInvoice(Invoice invoice) {
        Set<BusinessAuto> result = new HashSet<>();
        Document autoDocument = new Document();
        autoDocument.append("invoiceId", invoice.getId());
        MongoCollection<Document> auto = database.getCollection("BusinessAuto");
        FindIterable<Document> documents = auto.find(autoDocument);
        for (Document document : documents) {
            result.add(mapperTo(document));
        }
        return result;
    }
}
