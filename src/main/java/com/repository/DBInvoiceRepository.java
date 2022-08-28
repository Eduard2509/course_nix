package com.repository;

import com.config.JDBCConfig;
import com.model.*;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DBInvoiceRepository {
    private static final DBAutoRepository DB_AUTO_REPOSITORY = DBAutoRepository.getInstance();
    private static final DBBusinessAutoRepository DB_BUSINESS_AUTO_REPOSITORY = DBBusinessAutoRepository.getInstance();
    private static final DBSportAutoRepository DB_SPORT_AUTO_REPOSITORY = DBSportAutoRepository.getInstance();
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
    private Set<Auto> mapRowToAuto(ResultSet rs) {
        Set<Auto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new Auto(rs.getString("auto_id"),
                        rs.getString("model_auto"),
                        Manufacturer.valueOf(rs.getString("manufacturer_auto")),
                        rs.getBigDecimal("price_auto"),
                        rs.getString("body_type_auto"),
                        rs.getInt("count_auto"),
                        null,
                        null,
                        rs.getString("currency_auto"),
                        rs.getTimestamp("created_auto").toLocalDateTime()));
        }
        return result;
    }

    @SneakyThrows
    private Set<BusinessAuto> mapRowToBusinessAuto(ResultSet rs) {
        Set<BusinessAuto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new BusinessAuto(rs.getString("business_auto_id"),
                        rs.getString("business_auto_model"),
                        Manufacturer.valueOf(rs.getString("business_auto_manufacturer")),
                        rs.getBigDecimal("business_auto_price"),
                        BusinessClassAuto.valueOf(rs.getString("business_class_auto")),
                        rs.getInt("business_auto_count")));
        }
        return result;
    }

    @SneakyThrows
    private Set<SportAuto> mapRowToSportAuto(ResultSet rs) {
        Set<SportAuto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new SportAuto(rs.getString("sport_auto_id"),
                        rs.getString("sport_auto_model"),
                        Manufacturer.valueOf(rs.getString("sport_auto_manufacturer")),
                        rs.getBigDecimal("sport_auto_price"),
                        rs.getString("sport_auto_body_type"),
                        rs.getInt("sport_auto_max_speed"),
                        rs.getInt("sport_auto_count")));
        }
        return result;
    }

    @SneakyThrows
    private List<Vehicle> getVehicleFromInvoice(ResultSet rs) {
        Set<Vehicle> result = new HashSet<>();
        if (rs.getString("auto_id") != null) {
            result.addAll(mapRowToAuto(rs));
        }
        if (rs.getString("business_auto_id") != null) {
            result.addAll(mapRowToBusinessAuto(rs));
        }
        if (rs.getString("sport_auto_id") != null) {
            result.addAll(mapRowToSportAuto(rs));
        }
        return new ArrayList<>(result);
    }

    @SneakyThrows
    private Invoice mapRowToObject(ResultSet rs) {
        return new Invoice(
                rs.getString("id"),
                rs.getTimestamp("created").toLocalDateTime(),
                getVehicleFromInvoice(rs),
                rs.getBigDecimal("price"));
    }

    @SneakyThrows
    public List<Invoice> getAll() {
        String sql ="SELECT public.\"Auto\".id AS auto_id, public.\"Auto\".model AS model_auto, " +
                "public.\"Auto\".manufacturer AS manufacturer_auto, public.\"Auto\".price AS price_auto, " +
                " public.\"Auto\".body_type AS body_type_auto, public.\"Auto\".count AS count_auto, " +
                " public.\"Auto\".currency AS currency_auto, public.\"Auto\".created AS created_auto, " +
                " public.\"BusinessAuto\".id AS business_auto_id, " +
                "public.\"BusinessAuto\".model AS business_auto_model, " +
                "public.\"BusinessAuto\".manufacturer AS business_auto_manufacturer, " +
                "public.\"BusinessAuto\".price AS business_auto_price, " +
                "public.\"BusinessAuto\".business_class_auto AS business_class_auto, " +
                "public.\"BusinessAuto\".count AS business_auto_count, " +
                "public.\"SportAuto\".id AS sport_auto_id, public.\"SportAuto\".model AS sport_auto_model, " +
                "public.\"SportAuto\".manufacturer AS sport_auto_manufacturer, " +
                "public.\"SportAuto\".price AS sport_auto_price, public.\"SportAuto\".body_type AS sport_auto_body_type, " +
                "public.\"SportAuto\".max_speed AS sport_auto_max_speed, " +
                "public.\"SportAuto\".count AS sport_auto_count " +
                " FROM public.\"Invoice\", " +
                " LEFT JOIN public.\"Auto\" A on public.\"Invoice\".id = A.foreign_key, " +
                " LEFT JOIN public.\"BusinessAuto\" B on public.\"Invoice\".id = B.foreign_key, " +
                " LEFT JOIN public.\"SportAuto\" C on public.\"Invoice\".id = C.foreign_key ";
        List<Invoice> result = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(mapRowToObject(resultSet));
            }
            return result;
        }
    }

    private BigDecimal getSumInvoice(Invoice invoice) {
        BigDecimal sum = BigDecimal.valueOf(0);
        List<Vehicle> vehicles = invoice.getVehicles();
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
        final String sql = "DELETE FROM public.\"Invoice\" WHERE id = ?\n " +
                "DELETE FROM public.\"Auto\" WHERE foreign_key = ? " +
                "DELETE FROM public.\"BusinessAuto\" WHERE foreign_key = ?" +
                "DELETE FROM public.\"SportAuto\" WHERE foreign_key = ?";
//                "UPDATE public.\"Auto\" SET foreign_key = null WHERE foreign_key = ? " +
//                "UPDATE public.\"BusinessAuto\" SET foreign_key = null WHERE foreign_key = ? " +
//                "UPDATE public.\"SportAuto\" SET foreign_key = null WHERE foreign_key = ? ";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, id);
            preparedStatement.setString(4, id);
            preparedStatement.execute();
            return true;
        }
    }


    @SneakyThrows
    public Optional<Invoice> findById(String id) {
        final String sql = "SELECT * FROM public.\"Invoice\" WHERE id = ?" +
                "LEFT JOIN \"Auto\" A on \"Invoice\".id = A.foreign_key," +
                "LEFT JOIN \"BusinessAuto\" B on \"Invoice\".id = B.foreign_key," +
                "LEFT JOIN \"SportAuto\" C on \"Invoice\".id = C.foreign_key";
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
        Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt("count");
        }
        return 0;
    }

    @SneakyThrows
    public void updateDateInvoice(String id) {
        String sql = "UPDATE public.\"Invoice\" SET created=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(2, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public Map groupInvoiceByPrice() {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT price, count(*) FROM public.\"Invoice\" GROUP BY price ")) {
                Map<BigDecimal, Integer> prices = new HashMap<>();
                while (resultSet.next()) {
                    prices.put(resultSet.getBigDecimal("price"), resultSet.getInt("count"));
                }
                return prices;
            }
    }

    @SneakyThrows
    public void clear() {
        try(final Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM public.\"Invoice\"");
        }
    }

    @SneakyThrows
    public List<Invoice> getInvoiceMorePrice(BigDecimal priceLimit) {
        List<Invoice> invoices = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM public.\"Invoice\" WHERE price > ?")) {
            preparedStatement.setBigDecimal(1, priceLimit);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                invoices.add(new Invoice(rs.getString("id"),
                        rs.getTimestamp("created").toLocalDateTime(),
                        null,
                        rs.getBigDecimal("price")));
            }
        }
        return invoices;
    }
}
