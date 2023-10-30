package vn.tieppham.dao;

import vn.tieppham.entity.User;
import vn.tieppham.utils.MysqlConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao{
    public void insert(User user) throws SQLException, IOException {
            Connection connection = MysqlConnection.createConnection();
            String sql = "INSERT INTO users (name, password) VALUE (?, ?)";
            PreparedStatement preStatement = connection.prepareStatement(sql);
            preStatement.setString(1, user.getName());
            preStatement.setString(2, user.getPassword());
            preStatement.executeUpdate();
            connection.close();
    }
    public User getOne(User user, boolean isRegister) {
            try {
                Connection connection = MysqlConnection.createConnection();
                String sql = isRegister ? "SELECT * FROM users WHERE name = ?" : "SELECT * FROM users WHERE name = ? AND password = ?";
                PreparedStatement preStatement = connection.prepareStatement(sql);
                preStatement.setString(1, user.getName());
                if(!isRegister) {
                    preStatement.setString(2, user.getPassword());
                }
                ResultSet rs = preStatement.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                } else {
                    if (!isRegister)
                        System.out.println("Name or password is not correct");
                    return null;
                }
                connection.close();
                return user;
            } catch (SQLException | IOException e) {
                System.out.println(e);
                return null;
            }

    }
    public boolean register(User user) {
        try {
            if(getOne(user, true) != null) {
                System.err.println("Name already exists");
                return false;
            }
            insert(user);
            System.out.println("Register successfully");
        } catch (SQLException | IOException e) {
            System.err.println("Register failure");
        }
        return true;
    }
    public User login(User user) {
        try {
            User loginUser = this.getOne(user, false);
            if(loginUser != null) {
                Connection connection = MysqlConnection.createConnection();
                String sql = "UPDATE users SET isLogin = true WHERE id = ?";
                PreparedStatement preStatement = connection.prepareStatement(sql);
                preStatement.setInt(1, user.getId());
                int rs = preStatement.executeUpdate();
                connection.close();
                if(rs == 1) {
                    System.out.println("Login successfully");
                    return user;
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Log in failure");
        }
        return null;
    }

    public void logout (User user) {
        try {
            User loginUser = this.getOne(user, false);
            Connection connection = MysqlConnection.createConnection();
            String sql = "UPDATE users SET isLogin = false WHERE id = ?";
            PreparedStatement preStatement = connection.prepareStatement(sql);
            preStatement.setInt(1, user.getId());
            int rs = preStatement.executeUpdate();
            if(rs == 1) {
                System.out.println("Logout successfully");
            }
            connection.close();
        } catch (SQLException | IOException e) {
            System.out.println(e);
        }
    }

}
