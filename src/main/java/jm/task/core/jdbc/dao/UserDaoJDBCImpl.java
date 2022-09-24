package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection workConnection;

    static {
        try {
            workConnection = Util.checkConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS user ("
                + "id BIGINT AUTO_INCREMENT, "
                + "name       VARCHAR(60), "
                + "lastName  VARCHAR(60), "
                + "age        TINYINT, "
                + "primary key (id))";
        try (Statement st = workConnection.createStatement()) {
            st.executeUpdate(sqlCreateTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String dropUser = "DROP TABLE IF EXISTS user";
        try (PreparedStatement preparedStatement = workConnection.prepareStatement(dropUser)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertUser = "INSERT INTO user (name, lastName, age)" + "VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = workConnection.prepareStatement(insertUser)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String removeID = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = workConnection.prepareStatement(removeID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = null;
        String settings = "SELECT id, name, lastName, age FROM user ";
        try (Statement st = workConnection.createStatement();
             ResultSet resultSet = st.executeQuery(settings)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        String clear = "TRUNCATE user";
        try (Statement st = workConnection.createStatement()) {
            st.executeUpdate(clear);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
