package com.repository;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.config.JDBCConfig;
import com.model.BusinessAuto;
import com.model.BusinessClassAuto;
import com.model.Manufacturer;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class DBBusinessAutoRepository implements CrudRepository<BusinessAuto> {

    private static DBBusinessAutoRepository instance;
    private static Connection connection;

    @Autowired
    public DBBusinessAutoRepository() {
        connection = JDBCConfig.getConnection();
    }

    public static DBBusinessAutoRepository getInstance() {
        if (instance == null) {
            instance = new DBBusinessAutoRepository();
        }
        return instance;
    }

    @SneakyThrows
    public BusinessAuto mapRowToObject(ResultSet rs) {
        return new BusinessAuto(
                rs.getString("id"),
                rs.getString("model"),
                Manufacturer.valueOf(rs.getString("manufacturer")),
                BigDecimal.valueOf(rs.getDouble("price")),
                BusinessClassAuto.valueOf(rs.getString("business_class_auto")),
                rs.getInt("count")
        );
    }

    @SneakyThrows
    public List<BusinessAuto> getAll() {
        List<BusinessAuto> result = new ArrayList<>();
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"BusinessAuto\"");
        while (resultSet.next()) {
            result.add(mapRowToObject(resultSet));
        }
        return result;
    }

    @SneakyThrows
    public boolean save(BusinessAuto businessAuto) {
        if (businessAuto == null) {
            throw new IllegalArgumentException("BusinessAuto must not be null");
        }
        if (businessAuto.getPrice().equals(BigDecimal.ZERO)) {
            businessAuto.setPrice(BigDecimal.valueOf(-1));
        }
        final String sql = "INSERT INTO public.\"BusinessAuto\"(id, model, manufacturer, " +
                "price, business_class_auto, count) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, businessAuto.getId());
            preparedStatement.setString(2, businessAuto.getModel());
            preparedStatement.setString(3, businessAuto.getManufacturer().name());
            preparedStatement.setBigDecimal(4, businessAuto.getPrice());
            preparedStatement.setString(5, businessAuto.getBusinessClassAuto().name());
            preparedStatement.setInt(6, businessAuto.getCount());
            return preparedStatement.execute();
        }
    }

    public boolean saveAll(List<BusinessAuto> businessAuto) {
        if (businessAuto == null) {
            return false;
        }
        businessAuto.forEach(this::save);
        return true;
    }

    @SneakyThrows
    public boolean update(BusinessAuto businessAuto) {
        String sql = "UPDATE public.\"BusinessAuto\" SET model=?, manufacturer=?, " +
                "price=?, business_class_auto=?, count=? WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, businessAuto.getModel());
            preparedStatement.setString(2, businessAuto.getManufacturer().name());
            preparedStatement.setBigDecimal(3, businessAuto.getPrice());
            preparedStatement.setString(4, businessAuto.getBusinessClassAuto().name());
            preparedStatement.setInt(5, businessAuto.getCount());
            preparedStatement.setString(6, businessAuto.getId());
//        preparedStatement.setObject(6, auto.getEngine());
            preparedStatement.executeUpdate();
            return true;
        }
    }


    @SneakyThrows
    public boolean delete(String id) {
        final String sql = "DELETE FROM public.\"BusinessAuto\" WHERE id = ?";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
            return true;
        }
    }


    @SneakyThrows
    public Optional<BusinessAuto> findById(String id) {
        final String sql = "SELECT * FROM public.\"BusinessAuto\" WHERE id = ?";
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
    public void setInvoiceId(String idAuto, String invoiceId) {
        final String sql = "UPDATE public.\"BusinessAuto\" SET foreign_key=? WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,invoiceId);
            preparedStatement.setString(2,idAuto);
            preparedStatement.execute();
        }
    }

    @Override
    @SneakyThrows
    public void clear() {
        try(final Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM public.\"BusinessAuto\"");
        }
    }
}