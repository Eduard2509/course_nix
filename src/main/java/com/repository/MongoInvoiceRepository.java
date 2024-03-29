package com.repository;

import com.config.HibernateFactoryUtil;
import com.config.MongoConfig;
import com.google.gson.*;
import com.model.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Filters.gt;

public class MongoInvoiceRepository {

    MongoConfig mongoConfig = new MongoConfig();
    MongoDatabase database = mongoConfig.connect("course_nix");
    private static MongoInvoiceRepository instance;

    private static Gson getGson() {
        JsonSerializer<LocalDateTime> ser = (localDateTime, type, jsonSerializationContext) ->
                localDateTime == null ? null : new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));

        JsonDeserializer<LocalDateTime> deser = (json, typeOfT, jsonDeserializationContext) ->
            LocalDateTime.parse(json.getAsString() + " 00:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH));

        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, ser)
                .registerTypeAdapter(LocalDateTime.class, deser).create();
    }


    public static MongoInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new MongoInvoiceRepository();
        }
        return instance;
    }

    private static Document mapperFrom(Invoice invoice) {
        Gson gson = getGson();
        Document parse = Document.parse(gson.toJson(invoice));
        return parse;
    }

    private static Invoice mapperTo(Document document) {
        Gson gson = getGson();
        return gson.fromJson(document.toJson(), Invoice.class);
    }

    public Optional<Invoice> findById(String id) {
        Optional<Invoice> invoice = Optional.empty();
        final Document filter = new Document();
        filter.append("id", id);
        MongoCollection<Document> autos = database.getCollection("Invoice");
        FindIterable<Document> documents = autos.find(filter);
        for (Document document : documents) {
            invoice = Optional.of(mapperTo(document));
            invoice.get().setVehicles(getVehiclesInvoice(invoice.get()));
        }
        return invoice;
    }

    private Set<Vehicle> getVehiclesInvoice(Invoice invoice) {
        Set<Vehicle> resultSet = new HashSet<>();
        MongoAutoRepository autoRepository = MongoAutoRepository.getInstance();
        MongoBusinessAutoRepository businessAutoRepository = MongoBusinessAutoRepository.getInstance();
        MongoSportAutoRepository sportAutoRepository = MongoSportAutoRepository.getInstance();
        resultSet.addAll(autoRepository.getVehiclesInvoice(invoice));
        resultSet.addAll(businessAutoRepository.getVehiclesInvoice(invoice));
        resultSet.addAll(sportAutoRepository.getVehiclesInvoice(invoice));
        return resultSet;
    }

    public Set<Invoice> getAll() {
        List<Invoice> invoices = new ArrayList<>();
        MongoCollection<Document> autos = database.getCollection("Invoice");
        FindIterable<Document> documents = autos.find();
        for (Document doc : documents) {
            Invoice invoice = mapperTo(doc);
            invoice.setVehicles(getVehiclesInvoice(invoice));
            invoices.add(invoice);
        }
        return new HashSet<>(invoices);
    }

    public boolean save(Invoice invoice) {
        MongoCollection<Document> invoices = database.getCollection("Invoice");
        Document document = mapperFrom(invoice);
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(Auto.class.getSimpleName())) {
                MongoAutoRepository mongoAutoRepository = MongoAutoRepository.getInstance();
                mongoAutoRepository.setInvoiceId(a.getId(), invoice.getId());
            }
        });
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(BusinessAuto.class.getSimpleName())) {
                MongoBusinessAutoRepository mongoBusinessAutoRepository = MongoBusinessAutoRepository.getInstance();
                mongoBusinessAutoRepository.setInvoiceId(a.getId(), invoice.getId());;
            }
        });
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(SportAuto.class.getSimpleName())) {
                MongoSportAutoRepository mongoSportAutoRepository = MongoSportAutoRepository.getInstance();
                mongoSportAutoRepository.setInvoiceId(a.getId(), invoice.getId());
            }
        });
        invoices.insertOne(document);

        return true;
    }

    public boolean saveAll(List<Invoice> invoices) {
        if (invoices == null) {
            return false;
        }
        invoices.forEach(this::save);
        return true;
    }

    public boolean update(Invoice invoice) {
        final Document filter = new Document();
        filter.append("id", invoice.getId());
        final Document newData = mapperFrom(invoice);;
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("Invoice");
        sportAutos.updateOne(filter, updateObject);
        return true;
    }

    public boolean delete(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        MongoCollection<Document> invoices = database.getCollection("Invoice");
        invoices.deleteOne(filter);

        MongoCollection<Document> auto = database.getCollection("Auto");
        final Document filterAuto = new Document();
        filter.append("invoiceId", id);
        FindIterable<Document> documents = auto.find(filterAuto);
        for (Document doc : documents) {
            if (doc.getString("invoiceId").equals(id)) {
                auto.deleteOne(doc);
            }
        }

        MongoCollection<Document> businessAuto = database.getCollection("BusinessAuto");
        final Document filterBusinessAuto = new Document();
        filter.append("invoiceId", id);
        FindIterable<Document> documentsBusiness = businessAuto.find(filterBusinessAuto);
        for (Document doc : documentsBusiness) {
            if (doc.getString("invoiceId").equals(id)) {
                businessAuto.deleteOne(doc);
            }
        }

        MongoCollection<Document> sportAuto = database.getCollection("SportAuto");
        final Document filterSportAuto = new Document();
        filter.append("invoiceId", id);
        FindIterable<Document> documentsSport = sportAuto.find(filterSportAuto);
        for (Document doc : documentsSport) {
            if (doc.getString("invoiceId").equals(id)) {
                sportAuto.deleteOne(doc);
            }
        }

        return true;
    }

    @SneakyThrows
    public int getCountInvoiceInDB() {
        long invoice = database.getCollection("Invoice").countDocuments();
        return Math.toIntExact(invoice);
    }

    @SneakyThrows
    public void updateDateInvoice(String id) {
        final Document filter = new Document();
        filter.append("id", id);
        final Document newData = new Document();
        newData.append("created", LocalDateTime.now());
        final Document updateObject = new Document();
        updateObject.append("$set", newData);
        MongoCollection<Document> sportAutos = database.getCollection("Invoice");
        sportAutos.updateOne(filter, updateObject);
    }

    @SneakyThrows
    public Map groupInvoiceByPrice() {
        Map<BigDecimal, Integer> prices = new HashMap<>();
        MongoCollection<Document> invoice = database.getCollection("Invoice");
        AggregateIterable<Document> aggregate = invoice.aggregate(Arrays.asList(
                group("$price", Accumulators.sum("count", 1)),
                sort(Sorts.descending("count"))));
        MongoCursor<Document> iterator = aggregate.iterator();
        while (iterator.hasNext()) {
            Document iter = iterator.next();
            prices.put(BigDecimal.valueOf(iter.get("_id", Double.class)), (Integer) iter.get("count"));
        }
        return prices;
    }

    @SneakyThrows
    public List<Invoice> getInvoiceMorePrice(BigDecimal priceLimit) {
        List<Invoice> resultList = new ArrayList<>();
        MongoCollection<Document> invoices = database.getCollection("Invoice");
        Bson filter = gt("price", priceLimit);
        FindIterable<Document> documents = invoices.find(filter);
        for (Document document : documents) {
            Invoice invoice = mapperTo(document);
            invoice.setVehicles(getVehiclesInvoice(invoice));
            resultList.add(invoice);
        }
        return resultList;
    }
}
