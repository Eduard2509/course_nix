package com.repository;

import com.config.JDBCConfig;
import com.model.*;
import com.service.InvoiceService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DBInvoiceRepository {
    private static final DBAutoRepository DB_AUTO_REPOSITORY = DBAutoRepository.getInstance();
    private static final DBBusinessAutoRepository DB_BUSINESS_AUTO_REPOSITORY = DBBusinessAutoRepository.getInstance();
    private static final DBSportAutoRepository DB_SPORT_AUTO_REPOSITORY = DBSportAutoRepository.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);
    private static DBInvoiceRepository instance;
    private static Connection connection;

    public DBInvoiceRepository() {
        connection = JDBCConfig.getConnection();
    }

    public static DBInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new DBInvoiceRepository();
        }
        return instance;
    }

    @SneakyThrows
    private Auto mapRowToAuto(ResultSet rs) {
        return new Auto(rs.getString("auto_id"),
                rs.getString("model_auto"),
                Manufacturer.valueOf(rs.getString("manufacturer_auto")),
                rs.getBigDecimal("price_auto"),
                rs.getString("body_type_auto"),
                rs.getInt("count_auto"),
                null,
                null,
                rs.getString("currency_auto"),
                rs.getTimestamp("created_auto").toLocalDateTime());
    }

    @SneakyThrows
    private BusinessAuto mapRowToBusinessAuto(ResultSet rs) {
        return new BusinessAuto(rs.getString("business_auto_id"),
                rs.getString("business_auto_model"),
                Manufacturer.valueOf(rs.getString("business_auto_manufacturer")),
                rs.getBigDecimal("business_auto_price"),
                BusinessClassAuto.valueOf(rs.getString("business_class_auto")),
                rs.getInt("business_auto_count"));
    }

    @SneakyThrows
    private SportAuto mapRowToSportAuto(ResultSet rs) {
        return new SportAuto(rs.getString("sport_auto_id"),
                rs.getString("sport_auto_model"),
                Manufacturer.valueOf(rs.getString("sport_auto_manufacturer")),
                rs.getBigDecimal("sport_auto_price"),
                rs.getString("sport_auto_body_type"),
                rs.getInt("sport_auto_max_speed"),
                rs.getInt("sport_auto_count"));
    }

    @SneakyThrows
    private Set<Vehicle> getVehicleFromInvoice(ResultSet rs) {
        Set<Vehicle> result = new HashSet<>();
        if (rs.getString("auto_id") != null) {
            result.add(mapRowToAuto(rs));
        }
        if (rs.getString("business_auto_id") != null) {
            result.add(mapRowToBusinessAuto(rs));
        }
        if (rs.getString("sport_auto_id") != null) {
            result.add(mapRowToSportAuto(rs));
        }
        return result;
    }

    @SneakyThrows
    private Invoice mapRowToObject(ResultSet rs) {
        Invoice invoice = null;
        if (rs.next()) {
            invoice = new Invoice(
                    rs.getString("invoice_id"),
                    rs.getTimestamp("invoice_created").toLocalDateTime(),
                    getVehicleFromInvoice(rs),
                    rs.getBigDecimal("invoice_price"));
        }
        return invoice;
    }

    @SneakyThrows
    public List<Invoice> getAll() {
        String sql = "SELECT public.\"Auto\".id AS auto_id, public.\"Auto\".model AS model_auto, " +
                "public.\"Auto\".manufacturer AS manufacturer_auto, public.\"Auto\".price AS price_auto, " +
                " public.\"Auto\".body_type AS body_type_auto, public.\"Auto\".count AS count_auto, " +
                " public.\"Auto\".currency AS currency_auto, public.\"Auto\".created AS created_auto," +
                "public.\"Auto\".foreign_key AS auto_foreign_key, " +
                " public.\"BusinessAuto\".id AS business_auto_id, " +
                "public.\"BusinessAuto\".model AS business_auto_model, " +
                "public.\"BusinessAuto\".manufacturer AS business_auto_manufacturer, " +
                "public.\"BusinessAuto\".price AS business_auto_price, " +
                "public.\"BusinessAuto\".business_class_auto AS business_class_auto, " +
                "public.\"BusinessAuto\".count AS business_auto_count, " +
                "public.\"BusinessAuto\".foreign_key AS business_auto_foreign_key," +
                "public.\"SportAuto\".id AS sport_auto_id, public.\"SportAuto\".model AS sport_auto_model, " +
                "public.\"SportAuto\".manufacturer AS sport_auto_manufacturer, " +
                "public.\"SportAuto\".price AS sport_auto_price," +
                " public.\"SportAuto\".body_type AS sport_auto_body_type, " +
                "public.\"SportAuto\".max_speed AS sport_auto_max_speed, " +
                "public.\"SportAuto\".foreign_key AS sport_auto_foreign_key, " +
                "public.\"SportAuto\".count AS sport_auto_count, public.\"Invoice\".id AS invoice_id, " +
                "public.\"Invoice\".created AS invoice_created, public.\"Invoice\".price AS invoice_price" +
                " FROM public.\"Invoice\" " +
                " LEFT JOIN \"Auto\" on \"Invoice\".id = \"Auto\".foreign_key " +
                " LEFT JOIN \"BusinessAuto\" on \"Invoice\".id = \"BusinessAuto\".foreign_key " +
                " LEFT JOIN \"SportAuto\" on \"Invoice\".id = \"SportAuto\".foreign_key ";
        Set<Invoice> setInvoices = new HashSet<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Invoice invoice = mapRowToObject(resultSet);
                if (invoice != null && setInvoices.stream().anyMatch(res -> res.getId().equals(invoice.getId()))) {
                    Set<Vehicle> vehicleFromInvoice = getVehicleFromInvoice(resultSet);
                    for (Vehicle vehicle : vehicleFromInvoice) {
                        for (Invoice oldInvoice : setInvoices) {
                            if (oldInvoice.getId().equals(invoice.getId())) {
                                if (!(setInvoices.contains(vehicle))) {
                                    oldInvoice.getVehicles().addAll(vehicleFromInvoice);
                                }
                            }
                        }
                    }
                } else {
                    setInvoices.add(invoice);
                }
            }
        }
        return new ArrayList<>(setInvoices);
    }

    private BigDecimal getSumInvoice(Invoice invoice) {
        BigDecimal sum = BigDecimal.valueOf(0);
        Set<Vehicle> vehicles = invoice.getVehicles();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleType().equals(VehicleType.AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
            if (vehicle.getVehicleType().equals(VehicleType.BUSINESS_AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
            if (vehicle.getVehicleType().equals(VehicleType.SPORT_AUTO)) {
                sum = sum.add(vehicle.getPrice());
            }
        }
        return sum;
    }

    @SneakyThrows
    public boolean save(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("BusinessAuto must not be null");
        }
        final String sql = "INSERT INTO public.\"Invoice\"(id, created, price) VALUES (?,?,?)";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, invoice.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(invoice.getCreated()));
            preparedStatement.setBigDecimal(3, getSumInvoice(invoice));
            preparedStatement.execute();
        }
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(Auto.class.getSimpleName())) {
                DB_AUTO_REPOSITORY.setInvoiceId(a.getId(), invoice.getId());
            }
        });
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(BusinessAuto.class.getSimpleName())) {
                DB_BUSINESS_AUTO_REPOSITORY.setInvoiceId(a.getId(), invoice.getId());
            }
        });
        invoice.getVehicles().forEach(a -> {
            if (a.getClass().getSimpleName().equals(SportAuto.class.getSimpleName())) {
                DB_SPORT_AUTO_REPOSITORY.setInvoiceId(a.getId(), invoice.getId());
            }
        });
        LOGGER.info("Invoice id save: {}", invoice.getId());
        return true;
    }

    public boolean saveAll(List<Invoice> invoices) {
        if (invoices == null) {
            return false;
        }
        invoices.forEach(this::save);
        return true;
    }

    @SneakyThrows
    public boolean update(Invoice invoice) {
        String sql = "UPDATE public.\"Invoice\" SET created=? WHERE id = ?";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, invoice.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(invoice.getCreated()));
            return preparedStatement.execute();
        }
    }

    @SneakyThrows
    public boolean delete(String id) {
        final String sqlInvoice = "DELETE FROM public.\"Invoice\" WHERE id = ? ";
        final String sqlAuto = "UPDATE public.\"Auto\" SET foreign_key = null WHERE foreign_key = ? ";
        final String sqlBusinessAuto = "UPDATE public.\"BusinessAuto\" SET foreign_key = null WHERE foreign_key = ? ";
        final String sqlSportAuto = "UPDATE public.\"SportAuto\" SET foreign_key = null WHERE foreign_key = ? ";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlAuto)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        }

        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlBusinessAuto)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        }
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlSportAuto)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        }
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sqlInvoice)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        }
        return true;
    }


    @SneakyThrows
    public Optional<Invoice> findById(String id) {
        final String sql = "SELECT public.\"Auto\".id AS auto_id, public.\"Auto\".model AS model_auto, " +
                "public.\"Auto\".manufacturer AS manufacturer_auto, public.\"Auto\".price AS price_auto, " +
                " public.\"Auto\".body_type AS body_type_auto, public.\"Auto\".count AS count_auto, " +
                " public.\"Auto\".currency AS currency_auto, public.\"Auto\".created AS created_auto," +
                "public.\"Auto\".foreign_key AS auto_foreign_key, " +
                " public.\"BusinessAuto\".id AS business_auto_id, " +
                "public.\"BusinessAuto\".model AS business_auto_model, " +
                "public.\"BusinessAuto\".manufacturer AS business_auto_manufacturer, " +
                "public.\"BusinessAuto\".price AS business_auto_price, " +
                "public.\"BusinessAuto\".business_class_auto AS business_class_auto, " +
                "public.\"BusinessAuto\".count AS business_auto_count, " +
                "public.\"BusinessAuto\".foreign_key AS business_auto_foreign_key," +
                "public.\"SportAuto\".id AS sport_auto_id, public.\"SportAuto\".model AS sport_auto_model, " +
                "public.\"SportAuto\".manufacturer AS sport_auto_manufacturer, " +
                "public.\"SportAuto\".price AS sport_auto_price, public.\"SportAuto\".body_type AS sport_auto_body_type, " +
                "public.\"SportAuto\".max_speed AS sport_auto_max_speed, " +
                "public.\"SportAuto\".foreign_key AS sport_auto_foreign_key, " +
                "public.\"SportAuto\".count AS sport_auto_count, public.\"Invoice\".id AS invoice_id, " +
                "public.\"Invoice\".created AS invoice_created, public.\"Invoice\".price AS invoice_price" +
                " FROM public.\"Invoice\" " +
                " LEFT JOIN \"Auto\" on \"Invoice\".id = \"Auto\".foreign_key " +
                " LEFT JOIN \"BusinessAuto\" on \"Invoice\".id = \"BusinessAuto\".foreign_key " +
                " LEFT JOIN \"SportAuto\" on \"Invoice\".id = \"SportAuto\".foreign_key " +
                " WHERE \"Invoice\".id = ?";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToObject(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public int getCountInvoiceInDB() {
        String sql = "SELECT COUNT(id) as count FROM public.\"Invoice\"";
        try (Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
            return 0;
        }
    }

    @SneakyThrows
    public void updateDateInvoice(String id) {
        String sql = "UPDATE public.\"Invoice\" SET created=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, id);
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    public Map<BigDecimal, Integer> groupInvoiceByPrice() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT price, count(*) FROM public.\"Invoice\" GROUP BY price ")) {
            Map<BigDecimal, Integer> prices = new HashMap<>();
            while (resultSet.next()) {
                prices.put(resultSet.getBigDecimal("price"), resultSet.getInt("count"));
            }
            return prices;
        }
    }

    @SneakyThrows
    public void clear() {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("UPDATE public.\"Auto\" SET foreign_key = null");
        }
        try (final Statement statement = connection.createStatement()) {
            statement.execute("UPDATE public.\"BusinessAuto\" SET foreign_key = null");
        }
        try (final Statement statement = connection.createStatement()) {
            statement.execute("UPDATE public.\"SportAuto\" SET foreign_key = null");
        }
        try (final Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM public.\"Invoice\"");
        }
    }


    @SneakyThrows
    public List<Invoice> getInvoiceMorePrice(BigDecimal priceLimit) {
        List<Invoice> invoices = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT public.\"Auto\".id AS auto_id, public.\"Auto\".model AS model_auto, " +
                        "public.\"Auto\".manufacturer AS manufacturer_auto, public.\"Auto\".price AS price_auto, " +
                        " public.\"Auto\".body_type AS body_type_auto, public.\"Auto\".count AS count_auto, " +
                        " public.\"Auto\".currency AS currency_auto, public.\"Auto\".created AS created_auto," +
                        "public.\"Auto\".foreign_key AS auto_foreign_key, " +
                        " public.\"BusinessAuto\".id AS business_auto_id, " +
                        "public.\"BusinessAuto\".model AS business_auto_model, " +
                        "public.\"BusinessAuto\".manufacturer AS business_auto_manufacturer, " +
                        "public.\"BusinessAuto\".price AS business_auto_price, " +
                        "public.\"BusinessAuto\".business_class_auto AS business_class_auto, " +
                        "public.\"BusinessAuto\".count AS business_auto_count, " +
                        "public.\"BusinessAuto\".foreign_key AS business_auto_foreign_key," +
                        "public.\"SportAuto\".id AS sport_auto_id, public.\"SportAuto\".model AS sport_auto_model, " +
                        "public.\"SportAuto\".manufacturer AS sport_auto_manufacturer, " +
                        "public.\"SportAuto\".price AS sport_auto_price," +
                        " public.\"SportAuto\".body_type AS sport_auto_body_type, " +
                        "public.\"SportAuto\".max_speed AS sport_auto_max_speed, " +
                        "public.\"SportAuto\".foreign_key AS sport_auto_foreign_key, " +
                        "public.\"SportAuto\".count AS sport_auto_count, public.\"Invoice\".id AS invoice_id, " +
                        "public.\"Invoice\".created AS invoice_created, public.\"Invoice\".price AS invoice_price" +
                        " FROM public.\"Invoice\" " +
                        " LEFT JOIN \"Auto\" on \"Invoice\".id = \"Auto\".foreign_key " +
                        " LEFT JOIN \"BusinessAuto\" on \"Invoice\".id = \"BusinessAuto\".foreign_key " +
                        " LEFT JOIN \"SportAuto\" on \"Invoice\".id = \"SportAuto\".foreign_key " +
                        " WHERE \"Invoice\".price > ?")) {
            preparedStatement.setBigDecimal(1, priceLimit);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Invoice invoice = mapRowToObject(rs);
                if (invoices.stream().anyMatch(res -> res.getId().equals(invoice.getId()))) {
                    Set<Vehicle> vehicleFromInvoice = getVehicleFromInvoice(rs);
                    invoice.getVehicles().addAll(vehicleFromInvoice);
                } else {
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }
}
