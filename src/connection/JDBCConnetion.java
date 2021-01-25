/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class JDBCConnetion {

    static String host = "localhost";
    static int port = 3306;
    static String databaseName = "spotify-app";

    public static Connection getConnection() {
        final String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
        final String user = "root";
        final String password = "ainsoft99";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnetion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
