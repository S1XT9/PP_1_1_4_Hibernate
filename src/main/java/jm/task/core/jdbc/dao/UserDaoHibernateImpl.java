package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction tr = null;
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS user ("
                + "id BIGINT AUTO_INCREMENT, "
                + "name       VARCHAR(60), "
                + "lastName  VARCHAR(60), "
                + "age        TINYINT, "
                + "primary key (id))";
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.createSQLQuery(sqlCreateTable).executeUpdate();
            tr.commit();
            System.out.println("Table is exist");
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tr = null;
        String dropUser = "DROP TABLE IF EXISTS user";
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.createSQLQuery(dropUser).executeUpdate();
            tr.commit();
            System.out.println("Table dropped");
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tr = null;
        User userExamp = new User(name, lastName, age);
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.save(userExamp);
             session.save(userExamp);
            tr.commit();
            System.out.println("New User: " + userExamp);
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tr = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            User userdelete = session.get(User.class, id);
            session.delete(userdelete);
            tr.commit();
            System.out.println("User " + userdelete + " has been deleted");
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        Transaction tr = null;
        String settings ="SELECT id, name, lastName, age FROM user " ;
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            allUsers = session.createSQLQuery(settings).getResultList();
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public void cleanUsersTable() {
        Transaction tr = null;
        String clear = "TRUNCATE user";
        try (Session session = Util.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.createSQLQuery(clear).executeUpdate();
            tr.commit();
            System.out.println("Table user has been cleaned");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
