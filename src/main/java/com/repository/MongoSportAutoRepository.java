package com.repository;

import com.config.MongoConfig;
import com.model.Auto;
import com.model.Invoice;
import com.model.Manufacturer;
import com.model.SportAuto;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.*;

public class MongoSportAutoRepository implements CrudRepository<SportAuto> {

    private static MongoSportAutoRepository instance;
    MongoConfig mongoConfig = new MongoConfig();
    MongoDatabase database = mongoConfig.connect("course_nix");

    public static MongoSportAutoRepository getInstance() {
        if (instance == null) {
            instance = new MongoSportAutoRepository();
        }
        return instance;
    }

    private static Document mapperFrom(SportAuto sportAuto) {
        Document document = new Document();
        document.append("id", sportAuto.getId());
        document.append("model", sportAuto.getModel());
        document.append("manufacturer", sportAuto.getManufacturer().name());
        document.append("price", sportAuto.getPrice());
        document.append("body_type", sportAuto.getBodyType());
        document.append("max_speed", sportAuto.getMaxSpeed());
        document.append("count", sportAuto.getCount());
        return document;
    }

    private static SportAuto mapperTo(Document document) {
        final SportAuto sportAuto = new SportAuto(
                document.getString("id"),
                document.getString("model"),
                Manufacturer.valueOf(document.getString("manufacturer")),
                BigDecimal.valueOf(document.get("price", Decimal128.class).doubleValue()),
                document.getString("body_type"),
                document.getInteger("max_speed"),
                document.getInteger("count")
        );
        return sportAuto;
    }

    @Override
    public Optional<SportAuto> findById(String id) {
        Optional<SportAuto> sportAuto = Optional.empty();
        final Document filter = new Document();
        filter.append("id", id);
        MongoCollection<Document> autos = database.getCollection("SportAuto");
            FindIterable<Document> documents = autos.find(filter);
            for (Document document : documents) {
                sportAuto = Optional.of(mapperTo(document));
            }
            return sportAuto;
    }

    @Override
    public List<SportAuto> getAll() {
        List<SportAuto> sportAutos = new ArrayList<>();
        MongoCollection<Document> autos = database.getCollection("SportAuto");
        FindIterable<Document> documents = autos.find();
        for (Document doc : documents) {
            sportAutos.add(mapperTo(doc));
        }
        return sportAutos;
    }

    @Override
    public boolean save(SportAuto auto) {
        MongoCollection<Document> autos = database.getCollection("SportAuto");
        Document document = mapperFrom(auto);
        autos.insertOne(document);
        return true;
    }

    @Override
    public boolean saveAll(List<SportAuto> auto) {
        if (auto == null) {
            return false;
        }
        auto.forEach(this::save);
        return true;
    }


    @Override
    public boolean update(SportAuto sportAuto) {
        final Document filter = new Document();
        filter.append("id", sportAuto.getId());
        final Document newData = new Document();
        newData.append("model", sportAuto.getModel());
        newData.append("manufacturer", sportAuto.getManufacturer().name());
        newData.append("price", sportAuto.getPrice());
        newData.append("body_type", sportAuto.getBodyType());
        newData.append("max_speed", sportAuto.getMaxSpeed());
        newData.append("count", sportAuto.getCount());
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("SportAuto");
        sportAutos.updateOne(filter, updateObject);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Document filter = new Document();
        filter.append("id", id);

        MongoCollection<Document> sportAutos = database.getCollection("SportAuto");
        sportAutos.deleteOne(filter);
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
        MongoCollection<Document> sportAutos = database.getCollection("SportAuto");
        sportAutos.updateOne(filter, updateObject);
    }

    public Set<SportAuto> getVehiclesInvoice(Invoice invoice) {
        Set<SportAuto> result = new HashSet<>();
        Document autoDocument = new Document();
        autoDocument.append("invoiceId", invoice.getId());
        MongoCollection<Document> auto = database.getCollection("SportAuto");
        FindIterable<Document> documents = auto.find(autoDocument);
        for (Document document : documents) {
            result.add(mapperTo(document));
        }
        return result;
    }
}
