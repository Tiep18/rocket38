package vn.tieppham.dao;

import vn.tieppham.entity.User;
import vn.tieppham.utils.MysqlConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao{
    public static void insert(User user) throws SQLException, IOException {
            Connection connection = MysqlConnection.createConnection();
            String sql = "INSERT INTO users (name, password) VALUE (?, ?)";
            PreparedStatement preStatement = connection.prepareStatement(sql);
            preStatement.setString(1, user.getName());
            preStatement.setString(2, user.getPassword());
            preStatement.executeUpdate();
            connection.close();
    }
    public static User getByName(User user) throws SQLException, IOException {
                Connection connection = MysqlConnection.createConnection();
                String sql = "SELECT * FROM users WHERE name = ?";
                PreparedStatement preStatement = connection.prepareStatement(sql);
                preStatement.setString(1, user.getName());
                ResultSet rs = preStatement.executeQuery();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                } else {
                    return null;
                }
                connection.close();
                return user;
    }
    public static User getByNameAndPassword(User user) throws SQLException, IOException {
        Connection connection = MysqlConnection.createConnection();
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        PreparedStatement preStatement = connection.prepareStatement(sql);
        preStatement.setString(1, user.getName());
        preStatement.setString(2, user.getPassword());
        ResultSet rs = preStatement.executeQuery();
        if (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
        } else {
            return null;
        }
        connection.close();
        return user;
    }


}
