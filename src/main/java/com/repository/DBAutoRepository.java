package com.repository;

import com.annotations.Autowired;
import com.annotations.Singleton;
import com.config.JDBCConfig;
import com.model.Auto;
import com.model.Manufacturer;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class DBAutoRepository implements CrudRepository<Auto> {

    private static DBAutoRepository instance;
    private static Connection connection;

    @Autowired
    public DBAutoRepository() {
        connection = JDBCConfig.getConnection();
    }

    public static DBAutoRepository getInstance() {
        if (instance == null) {
            instance = new DBAutoRepository();
        }
        return instance;
    }

    @SneakyThrows
    public Auto mapRowToObject(ResultSet rs) {
        return new Auto(
                rs.getString("id"),
                rs.getString("model"),
                Manufacturer.valueOf(rs.getString("manufacturer")),
                BigDecimal.valueOf(rs.getDouble("price")),
                rs.getString("body_type"),
                rs.getInt("count"),
                null,
                null,
                rs.getString("currency"),
                rs.getTimestamp("created").toLocalDateTime()
        );
    }

    @SneakyThrows
    @Override
    public Optional<Auto> findById(String id) {
        final String sql = "SELECT * FROM public.\"Auto\" WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToObject(resultSet));
            } else {
                return Optional.empty();
            }
        }
    }

    @SneakyThrows
    @Override
    public List<Auto> getAll() {
        List<Auto> result = new ArrayList<>();
        try(final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Auto\"");
            while (resultSet.next()) {
                result.add(mapRowToObject(resultSet));
            }
            return result;
        }
    }

    @SneakyThrows
    @Override
    public boolean save(Auto auto) {
        if (auto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        if (auto.getPrice().equals(BigDecimal.ZERO)) {
            auto.setPrice(BigDecimal.valueOf(-1));
        }
        final String sql = "INSERT INTO public.\"Auto\"(id, model, manufacturer, " +
                "price, body_type, count, currency, created) VALUES (?,?,?,?,?,?,?,?)";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, auto.getId());
            preparedStatement.setString(2, auto.getModel());
            preparedStatement.setString(3, auto.getManufacturer().name());
            preparedStatement.setBigDecimal(4, auto.getPrice());
            preparedStatement.setString(5, auto.getBodyType());
            preparedStatement.setInt(6, auto.getCount());
//        preparedStatement.setObject(6, auto.getEngine().getBrand());
            preparedStatement.setString(7, auto.getCurrency());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(auto.getCreated()));
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean saveAll(List<Auto> auto) {
        if (auto == null) {
            return false;
        }
        auto.forEach(this::save);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(Auto auto) {
        String sql = "UPDATE public.\"Auto\" SET model=?, manufacturer=?, " +
                "price=?, body_type=?, count=?, currency=?, created=? WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, auto.getModel());
            preparedStatement.setString(2, auto.getManufacturer().name());
            preparedStatement.setDouble(3, auto.getPrice().doubleValue());
            preparedStatement.setString(4, auto.getBodyType());
            preparedStatement.setInt(5, auto.getCount());
//        preparedStatement.setObject(6, auto.getEngine());
            preparedStatement.setString(6, auto.getCurrency());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(auto.getCreated()));
            preparedStatement.setString(8, auto.getId());
            preparedStatement.execute();
            return true;
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(String id) {
        if (id == null) {
            return false;
        }
        final String sql = "DELETE FROM public.\"Auto\" WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
            return true;
        }
    }

    @SneakyThrows
    public void clear() {
        try(final Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM public.\"Auto\"");
        }
    }

    @SneakyThrows
    public void setInvoiceId(String idAuto, String invoiceId) {
        final String sql = "UPDATE public.\"Auto\" SET foreign_key=? WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,invoiceId);
            preparedStatement.setString(2,idAuto);
            preparedStatement.execute();
        }
    }
}
