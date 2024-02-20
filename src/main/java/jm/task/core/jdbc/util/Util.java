package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL_KEY = "db.url";
    private static final String USER_NAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static Connection connection;
    private static SessionFactory factory;

    private Util() {
    }

    public static Connection open() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        PropertiesUtil.get(URL_KEY),
                        PropertiesUtil.get(USER_NAME_KEY),
                        PropertiesUtil.get(PASSWORD_KEY)
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static void closeConnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getFactory(){
        Configuration configuration = new Configuration()
//                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "admin")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "none")
//                .setProperty("hibernate.show_sql", "true")
//                .setProperty("hibernate.format_sql", "true")
                .addAnnotatedClass(User.class);

        factory = configuration.buildSessionFactory();
    }

    public static Session getSession() {
        getFactory();
        return factory.openSession();
    }

    public static void closeSessionFactory() {
        factory.close();
    }

}
