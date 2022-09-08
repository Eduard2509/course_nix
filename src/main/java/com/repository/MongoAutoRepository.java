package com.repository;

import com.config.MongoConfig;
import com.google.gson.*;
import com.model.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MongoAutoRepository implements CrudRepository<Auto> {
        private static Gson gson = getGson();
    MongoConfig mongoConfig = new MongoConfig();
    MongoDatabase database = mongoConfig.connect("course_nix");
    private static MongoAutoRepository instance;

    private static Gson getGson() {
        JsonSerializer<LocalDateTime> ser = (localDateTime, type, jsonSerializationContext) ->
                localDateTime == null ? null : new JsonPrimitive(localDateTime.toString());
        JsonDeserializer<LocalDateTime> deser = (json, typeOfT, context) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S");
            return LocalDateTime.now();
        };
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, ser)
                .registerTypeAdapter(LocalDateTime.class, deser).create();
    }


//    static JsonSerializer<LocalDateTime> ser = new JsonSerializer<LocalDateTime>() {
//        @Override
//        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext
//                context) {
//            return src == null ? null : new JsonPrimitive(src.toString());
//        }
//    };
//
//    static JsonDeserializer<LocalDateTime> deser = new JsonDeserializer<LocalDateTime>() {
//        @Override
//        public LocalDateTime deserialize(JsonElement json, Type typeOfT,
//                                JsonDeserializationContext context) throws JsonParseException {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.S");
//            return LocalDateTime.now();
//        }
//    };
//
//    static Gson gson = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, ser)
//            .registerTypeAdapter(LocalDateTime.class, deser).create();

    public static MongoAutoRepository getInstance() {
        if (instance == null) {
            instance = new MongoAutoRepository();
        }
        return instance;
    }

    public static Document mapperFrom(Auto auto) {
        return Document.parse(gson.toJson(auto));
    }

    public static Auto mapperTo(Document document) {
        return gson.fromJson(document.toJson(), Auto.class);
    }

    @Override
    public Optional<Auto> findById(String id) {
        Optional<Auto> auto = Optional.empty();
        final Document filter = new Document();
        filter.append("id", id);
        MongoCollection<Document> autos = database.getCollection("Auto");
        FindIterable<Document> documents = autos.find(filter);
        for (Document document : documents) {
            auto = Optional.of(mapperTo(document));
        }
        return auto;
    }

    @Override
    public List<Auto> getAll() {
        List<Auto> businessAutos = new ArrayList<>();
        MongoCollection<Document> autos = database.getCollection("Auto");
        FindIterable<Document> documents = autos.find();
        for (Document doc : documents) {
            businessAutos.add(mapperTo(doc));
        }
        return businessAutos;
    }

    @Override
    public boolean save(Auto auto) {
        MongoCollection<Document> autos = database.getCollection("Auto");
        Document document = mapperFrom(auto);
        autos.insertOne(document);
        return true;
    }

    @Override
    public boolean saveAll(List<Auto> auto) {
        if (auto == null) {
            return false;
        }
        auto.forEach(this::save);
        return true;
    }


    @Override
    public boolean update(Auto auto) {
        final Document filter = new Document();
        filter.append("id", auto.getId());
        final Document newData = mapperFrom(auto);;
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("Auto");
        sportAutos.updateOne(filter, updateObject);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Document filter = new Document();
        filter.append("id", id);

        MongoCollection<Document> businessAutos = database.getCollection("Auto");
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
        MongoCollection<Document> sportAutos = database.getCollection("Auto");
        sportAutos.updateOne(filter, updateObject);
    }

    public Set<Auto> getVehiclesInvoice(Invoice invoice) {
        Set<Auto> result = new HashSet<>();
        Document autoDocument = new Document();
        autoDocument.append("invoiceId", invoice.getId());
        MongoCollection<Document> auto = database.getCollection("Auto");
        FindIterable<Document> documents = auto.find(autoDocument);
        for (Document document : documents) {
            result.add(mapperTo(document));
        }
        return result;
    }
}
