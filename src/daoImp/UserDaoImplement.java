/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImp;

import bean.User;
import dao.UserDao;
import connection.JDBCConnetion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class UserDaoImplement implements UserDao {

    private static final String CHECK_EXISTED_USER = "select * from usersaccount where username = ?";
    private static final String CHECK_USER_LOGIN = "select * from usersaccount where username = ? and passwords = ?";
    private static final String INSERT_USER_INFO = "insert into users (accountName, dateOfBirth, city, phone, email)  values (?, ?, ?, ?, ?);";
    private static final String INSERT_USER_ACCOUNT = "insert into usersaccount (id_u, username, passwords) values (?, ?, ?);";
    private static final String GET_USER_ID = "select id_u from usersaccount where username = ?";
    private static final String GET_USER_INFO = "select * from users where id = ?";
    private static final String GET_LASTEST_USER_ID = "select id from users order by id desc";
    private static final String CHANGE_PASSWORD = "update usersaccount set passwords =? where username =?";

    @Override
    public boolean checkExistedUser(String username) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(CHECK_EXISTED_USER);) {
            state.setString(1, username);

            ResultSet rs = state.executeQuery();
            rs.next();
            return rs.first();

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean insertUser(User user) {
        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state1 = connection.prepareStatement(INSERT_USER_INFO);
                PreparedStatement state2 = connection.prepareStatement(GET_LASTEST_USER_ID);
                PreparedStatement state3 = connection.prepareStatement(INSERT_USER_ACCOUNT);) {

            state1.setString(1, user.getAccountName());
            state1.setString(2, user.getDateOfBirth());
            state1.setString(3, user.getCity());
            state1.setInt(4, user.getPhone());
            state1.setString(5, user.getEmail());

            int check = state1.executeUpdate();
            if (check == 1) {
                ResultSet rs = state2.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt(1);
                    state3.setInt(1, userId);
                    state3.setString(2, user.getUsername());
                    state3.setString(3, user.getPassword());
                    int check2 = state3.executeUpdate();

                    if (check2 == 1) {
                        return true;
                    } else {
                        System.out.println("Failed insert data to usersaccount table");
                    }
                }

            } else {
                System.out.println("Failed insert data to users table");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public User getUserInfo(int userId) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_USER_INFO);) {
            state.setInt(1, userId);

            ResultSet rs = state.executeQuery();
            rs.next();

            return new User(userId, rs.getString("accountName"), rs.getString("dateOfBirth"), rs.getString("city"), rs.getInt("phone"), rs.getString("email"));
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public int getUserId(String username) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_USER_ID);) {
            state.setString(1, username);

            ResultSet rs = state.executeQuery();
            rs.next();

            return rs.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public boolean checkUserLogin(String username, String password) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(CHECK_USER_LOGIN);) {
            state.setString(1, username);
            state.setString(2, password);

            ResultSet rs = state.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean ChangePass(String username, String oldpassword, String newpassword) {
        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state = connection.prepareStatement(CHANGE_PASSWORD);) {

            if (checkUserLogin(username, oldpassword)) {
                state.setString(1, newpassword);
                state.setString(2, username);
                int change = state.executeUpdate();
                if (change == 1) {
                    return true;
                } else {
                    System.out.println("Failed to change password");
                }
            } else {
                System.out.println("Wrong username or oldpassword");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

}
