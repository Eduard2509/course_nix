package com.repository;


import com.annotations.Autowired;
import com.annotations.Singleton;
import com.config.JDBCConfig;
import com.model.Manufacturer;
import com.model.SportAuto;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton

public class DBSportAutoRepository implements CrudRepository<SportAuto> {

    private static DBSportAutoRepository instance;
    private static Connection connection;

    @Autowired
    public DBSportAutoRepository() {
        connection = JDBCConfig.getConnection();
    }

    public static DBSportAutoRepository getInstance() {
        if (instance == null) {
            instance = new DBSportAutoRepository();
        }
        return instance;
    }

    @SneakyThrows
    public SportAuto mapRowToObject(ResultSet rs) {
        return new SportAuto(
                rs.getString("id"),
                rs.getString("model"),
                Manufacturer.valueOf(rs.getString("manufacturer")),
                BigDecimal.valueOf(rs.getDouble("price")),
                rs.getString("body_type"),
                rs.getInt("max_speed"),
                rs.getInt("count")
        );
    }

    @SneakyThrows
    @Override
    public Optional<SportAuto> findById(String id) {
        final String sql = "SELECT * FROM public.\"SportAuto\" WHERE id = ?";
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, id);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(mapRowToObject(resultSet));
        } else {
            return Optional.empty();
        }
    }

    @SneakyThrows
    @Override
    public List<SportAuto> getAll() {
        List<SportAuto> result = new ArrayList<>();
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"SportAuto\"");
        while (resultSet.next()) {
            result.add(mapRowToObject(resultSet));
        }
        return result;
    }

    @SneakyThrows
    @Override
    public boolean save(SportAuto sportAuto) {
        if (sportAuto == null) {
            throw new IllegalArgumentException("Auto must not be null");
        }
        if (sportAuto.getPrice().equals(BigDecimal.ZERO)) {
            sportAuto.setPrice(BigDecimal.valueOf(-1));
        }
        final String sql = "INSERT INTO public.\"SportAuto\"(id, model, manufacturer, " +
                "price, body_type, max_speed, count) VALUES (?,?,?,?,?,?,?)";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sportAuto.getId());
            preparedStatement.setString(2, sportAuto.getModel());
            preparedStatement.setString(3, sportAuto.getManufacturer().name());
            preparedStatement.setBigDecimal(4, sportAuto.getPrice());
            preparedStatement.setString(5, sportAuto.getBodyType());
            preparedStatement.setInt(6, sportAuto.getMaxSpeed());
            preparedStatement.setInt(7, sportAuto.getCount());
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean saveAll(List<SportAuto> sportAuto) {
        if (sportAuto == null) {
            return false;
        }
        sportAuto.forEach(this::save);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean update(SportAuto sportAuto) {
        String sql = "UPDATE public.\"SportAuto\" SET model=?, manufacturer=?, " +
                "price=?, body_type=?, max_speed=?, count=? WHERE id = ?";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sportAuto.getModel());
            preparedStatement.setString(2, sportAuto.getManufacturer().name());
            preparedStatement.setBigDecimal(3, sportAuto.getPrice());
            preparedStatement.setString(4, sportAuto.getBodyType());
            preparedStatement.setInt(5, sportAuto.getMaxSpeed());
            preparedStatement.setInt(6, sportAuto.getCount());
            preparedStatement.setString(7, sportAuto.getId());
            preparedStatement.executeUpdate();
            return true;
        }
    }

    public boolean check(SportAuto sportAuto) {
        if (sportAuto.getPrice() == null) {
            sportAuto.setPrice(BigDecimal.ZERO);
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean delete(String id) {
        final String sql = "DELETE FROM public.\"BusinessAuto\" WHERE id = ?";
        try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();
            return true;
        }
    }

    @SneakyThrows
    public void setInvoiceId(String idAuto, String invoiceId) {
        final String sql = "UPDATE public.\"SportAuto\" SET foreign_key=? WHERE id = ?";
        try(final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,invoiceId);
            preparedStatement.setString(2,idAuto);
            preparedStatement.execute();
        }
    }
}
