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

//    @SneakyThrows
//    private List<Auto> getAutoFromInvoice(String id) {
//        List<Auto> result = new ArrayList<>();
//        final String sql = "SELECT public.\"Invoice\".id, public.\"Auto\".model," +
//                "public.\"Auto\".manufacturer, public.\"Auto\".price," +
//                "public.\"Auto\".body_type, public.\"Auto\".count," +
//                "public.\"Auto\".currency, public.\"Auto\".created FROM public.\"Invoice\" INNER JOIN public.\"Auto\"" +
//                "ON public.\"Invoice\".id = public.\"Auto\".foreign_key WHERE id = ?";
//        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            final ResultSet resultSet = preparedStatement.executeQuery();
//            preparedStatement.setString(1, id);
//            if (resultSet.next()) {
//                result.add(DBAutoRepository.getInstance().mapRowToObject(resultSet));
//            }
//        }
//        return result;
//    }
//
//    @SneakyThrows
//    public List<BusinessAuto> getBusinessAutoFromInvoice(String id) {
//        List<BusinessAuto> result = new ArrayList<>();
//        final String sql = "SELECT * public.\"Invoice\".id,public.\"BusinessAuto\".id, public.\"BusinessAuto\".model," +
//                "public.\"BusinessAuto\".manufacturer, public.\"BusinessAuto\".price," +
//                "public.\"BusinessAuto\".business_class_auto, public.\"BusinessAuto\".count " +
//                "FROM public.\"Invoice\" INNER JOIN public.\"BusinessAuto\"" +
//                "ON public.\"Invoice\".id = public.\"BusinessAuto\".foreign_key WHERE id = ?";
//        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            final ResultSet resultSet = preparedStatement.executeQuery();
//            preparedStatement.setString(1, id);
//            if (resultSet.next()) {
//                result.add(DBBusinessAutoRepository.getInstance().mapRowToObject(resultSet));
//            }
//        }
//        return result;
//    }
//
//    @SneakyThrows
//    private List<SportAuto> getSportAutoFromInvoice(String id) {
//        List<SportAuto> result = new ArrayList<>();
//        final String sql = "SELECT * public.\"Invoice\".id, public.\"SportAuto\".model," +
//                "public.\"SportAuto\".manufacturer, public.\"SportAuto\".price, " +
//                "public.\"SportAuto\".body_type, public.\"SportAuto\".max_speed, " +
//                "public.\"SportAuto\".count FROM public.\"Invoice\" INNER JOIN public.\"SportAuto\" " +
//                "ON public.\"Invoice\".id = public.\"SportAuto\".foreign_key WHERE id = ?";
//        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            final ResultSet resultSet = preparedStatement.executeQuery();
//            preparedStatement.setString(1, id);
//            if (resultSet.next()) {
//                result.add(DBSportAutoRepository.getInstance().mapRowToObject(resultSet));
//            }
//        }
//        return result;
//    }
//
//    private List<Vehicle> complectedVehicleInvoice(String id) {
//        List<Auto> autoFromInvoice = getAutoFromInvoice(id);
//        List<Vehicle> vehicleInvoice = new ArrayList<>(autoFromInvoice);
//        List<BusinessAuto> businessAutoFromInvoice = getBusinessAutoFromInvoice(id);
//        vehicleInvoice.addAll(businessAutoFromInvoice);
//        List<SportAuto> sportAutoFromInvoice = getSportAutoFromInvoice(id);
//        vehicleInvoice.addAll(sportAutoFromInvoice);
//        return vehicleInvoice;
//    }

    @SneakyThrows
    private Set<Auto> mapRowToAuto(ResultSet rs) {
        Set<Auto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new Auto(rs.getString("A.id"),
                        rs.getString("A.model"),
                        Manufacturer.valueOf(rs.getString("A.manufacturer")),
                        rs.getBigDecimal("A.price"),
                        rs.getString("A.body_type"),
                        rs.getInt("A.count"),
                        null,
                        null,
                        rs.getString("currency"),
                        rs.getTimestamp("A.created").toLocalDateTime()));
        }
        return result;
    }

    @SneakyThrows
    private Set<BusinessAuto> mapRowToBusinessAuto(ResultSet rs) {
        Set<BusinessAuto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new BusinessAuto(rs.getString("B.id"),
                        rs.getString("B.model"),
                        Manufacturer.valueOf(rs.getString("B.manufacturer")),
                        rs.getBigDecimal("B.price"),
                        BusinessClassAuto.valueOf(rs.getString("business_class_auto")),
                        rs.getInt("B.count")));
        }
        return result;
    }

    @SneakyThrows
    private Set<SportAuto> mapRowToSportAuto(ResultSet rs) {
        Set<SportAuto> result = new HashSet<>();
        while (rs.next()) {
                result.add(new SportAuto(rs.getString("C.id"),
                        rs.getString("C.model"),
                        Manufacturer.valueOf(rs.getString("C.manufacturer")),
                        rs.getBigDecimal("C.price"),
                        rs.getString("C.body_type"),
                        rs.getInt("max_speed"),
                        rs.getInt("C.count")));
        }
        return result;
    }

    @SneakyThrows
    private List<Vehicle> getVehicleFromInvoice(ResultSet rs) {
        Set<Vehicle> result = new HashSet<>();
        if (rs.getString("A.id") != null) {
            result.addAll(mapRowToAuto(rs));
        }
        if (rs.getString("B.id") != null) {
            result.addAll(mapRowToBusinessAuto(rs));
        }
        if (rs.getString("C.id") != null) {
            result.addAll(mapRowToSportAuto(rs));
        }
        return new ArrayList<>(result);
    }

    @SneakyThrows
    private Invoice mapRowToObject(ResultSet rs) {
        return new Invoice(
                rs.getString("id"),
                rs.getTimestamp("created").toLocalDateTime(),
                getVehicleFromInvoice(rs));
    }

    @SneakyThrows
    public List<Invoice> getAll() {
        List<Invoice> result = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Invoice\"" +
                    "LEFT JOIN \"Auto\" A on \"Invoice\".id = A.foreign_key" +
                    "LEFT JOIN \"BusinessAuto\" B on \"Invoice\".id = B.foreign_key" +
                    "LEFT JOIN \"SportAuto\" C on \"Invoice\".id = C.foreign_key ");
            while (resultSet.next()) {
                result.add(mapRowToObject(resultSet));
            }
            return result;
        }
    }

    @SneakyThrows
    public boolean save(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("BusinessAuto must not be null");
        }
        final String sql = "INSERT INTO public.\"Invoice\"(id, created) VALUES (?,?)";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, invoice.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(invoice.getCreated()));
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
        final String sql = "DELETE FROM public.\"Invoice\" WHERE id = ?" +
                "UPDATE public.\"Auto\" SET foreign_key = null WHERE foreign_key = ?" +
                "UPDATE public.\"BusinessAuto\" SET foreign_key = null WHERE foreign_key = ?" +
                "UPDATE public.\"SportAuto\" SET foreign_key = null WHERE foreign_key = ?";
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

//    @SneakyThrows
//    public List<Vehicle> getRandomListVehicle() {
//        ArrayList<Vehicle> vehicleList = new ArrayList<>();
//        Random random = new Random();
//        int randomCountAuto = random.nextInt(0, 5);
//        for (int i = 0; i < randomCountAuto; i++) {
//            int randomTypeAuto = random.nextInt(0, 3);
//            if (randomTypeAuto == 0) {
//                String sql = "SELECT column FROM \"Auto\" ORDER BY RANDOM() LIMIT 1";
//                Connection connection = JDBCConfig.getConnection();
//                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                    final ResultSet resultSet = preparedStatement.executeQuery();
//                    Auto auto = DB_AUTO_REPOSITORY.mapRowToObject(resultSet);
//                    vehicleList.add(auto);
//                }
//            }
//            if (randomTypeAuto == 1) {
//                String sql = "SELECT column FROM \"BusinessAuto\" ORDER BY RANDOM() LIMIT 1";
//                Connection connection = JDBCConfig.getConnection();
//                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                    final ResultSet resultSet = preparedStatement.executeQuery();
//                    BusinessAuto businessAuto = DB_BUSINESS_AUTO_REPOSITORY.mapRowToObject(resultSet);
//                    vehicleList.add(businessAuto);
//                }
//            }
//            if (randomTypeAuto == 2) {
//                String sql = "SELECT * FROM \"SportAuto\" ORDER BY RANDOM() LIMIT 1";
//                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                    final ResultSet resultSet = preparedStatement.executeQuery();
//                    SportAuto sportAuto = DB_SPORT_AUTO_REPOSITORY.mapRowToObject(resultSet);
//                    vehicleList.add(sportAuto);
//                }
//            }
//        }
//        return vehicleList;
//    }


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
        String sql = "UPDATE FROM public.\"Invoice\" SET created = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setString(2, id);
    }

    @SneakyThrows
    public void groupInvoiceByPrice() {
        Map<String, BigDecimal> map = new HashMap<>();
        String sql = "SELECT id, price " +
                "FROM public.\"Invoice\" GROUP BY priceInvoice";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            map.put(resultSet.getString("id"), resultSet.getBigDecimal("priceInvoice"));
        }
    }
}
