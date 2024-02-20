package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery("""
                    CREATE TABLE IF NOT EXISTS Users(
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    lastName VARCHAR(128) NOT NULL,
                    age SMALLINT NOT NULL)
                    """).executeUpdate();

            transaction.commit();

            System.out.println("Таблица Users создана!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка создания таблицы Users");
                e.printStackTrace();
            }
        }

    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery("""
                    DROP TABLE IF EXISTS Users
                    """).executeUpdate();

            transaction.commit();

            System.out.println("Таблица Users удалена!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка удаления таблицы Users");
                e.printStackTrace();
            }
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            transaction.commit();

            System.out.println("Пользователь с именем " + name + " добавлен в БД!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка добавления в таблицу");
                e.printStackTrace();
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE User WHERE id = :id");
            int id1 = query.setParameter("id", id)
                    .executeUpdate();

            transaction.commit();

            System.out.println("Пользователь с id " + id + " был удалён из БД!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка удаления из таблицы");
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> list = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            Query<User> fromUser = session.createQuery("FROM User ", User.class);
            list = fromUser.list();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка получения пользователей из таблицы");
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE User");
            query.executeUpdate();

            transaction.commit();

            System.out.println("Таблица Users отчищена!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Ошибка отчистки таблицы");
                e.printStackTrace();
            }
        }

    }
}
