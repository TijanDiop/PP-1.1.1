package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    private static final Connection CONNECTION = Util.open();

    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS Users(
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    lastName VARCHAR(128) NOT NULL,
                    age SMALLINT NOT NULL)
                """;

        try (var statement = CONNECTION.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица создана!");
        } catch (SQLException e) {
            System.out.println("Ошибка создания таблицы");
            e.printStackTrace();
        }

    }

    public void dropUsersTable() {
        String sql = """
                DROP TABLE IF EXISTS Users
                """;
        try (var statement = CONNECTION.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица удалена!");
        } catch (SQLException e) {
            System.out.println("Ошибка удаления таблицы");
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO Users (name,lastname, age)
                VALUES (?,?,?)
                """;
        try (var prepareStatement = CONNECTION.prepareStatement(sql)) {
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setInt(3, age);

            prepareStatement.executeUpdate();
            System.out.println("Пользователь с именем " + name + " добавлен в БД!");
        } catch (SQLException e) {
            System.out.println("Ошибка добавления в таблицу");
            e.printStackTrace();
        }

    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM Users WHERE id = ?
                """;
        try (var prepareStatement = CONNECTION.prepareStatement(sql)) {
            prepareStatement.setLong(1, id);
            prepareStatement.executeUpdate();
            System.out.println("Пользователь с id " + id + " был удалён из БД!");
        } catch (SQLException e) {
            System.out.println("Ошибка удаления из таблицы");
            e.printStackTrace();
        }


    }

    public List<User> getAllUsers() {
        List<User> tableUser = null;
        String sql = """
                 SELECT id, name, lastname, age FROM Users
                """;
        try (var prepareStatement = CONNECTION.prepareStatement(sql)) {
            ResultSet resultSet = prepareStatement.executeQuery();
            tableUser = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getByte(4));
                user.setId(resultSet.getLong(1));
                tableUser.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка получения пользователей из таблицы");
            e.printStackTrace();
        }
        return tableUser;

    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE Users RESTART IDENTITY
                """;
        try (var statement = CONNECTION.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица отчищена!");
        } catch (SQLException e) {
            System.out.println("Ошибка отчистки таблицы");
            e.printStackTrace();
        }

    }
}
