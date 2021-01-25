/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImp;

import bean.Song;
import bean.User;
import connection.JDBCConnetion;
import dao.SongDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class SongDaoImplement implements SongDao {

    int limitsong = 4;

    private static final String INSERT_SONG = "insert into song (sourcePath) values (?);";
    private static final String GET_INSERTED_SONG = "select id from song order by id desc";
    private static final String INSERT_SONG_DETAIL = "insert into songDetail (id_song, title, author, duration, imgurl) values (?, ?, ?, ?, ?);";

    private static final String GET_SUGGEST_SONG = "select * from songDetail order by rand() limit ?;";
    private static final String GET_SEARCH_SONG = "select * from songDetail where title like \"% ? %\";";
    private static final String GET_SONG = "select * from songDetail where id_song = ?";
    private static final String GET_SONG_IMG_URL = "select imgUrl from songDetail where id_song = ?";
    private static final String GET_SONG_PATH = "select sourcePath from song where id = ?;";

    @Override
    public List<Song> getSuggestionSong() {
        List<Song> list = new ArrayList<>();

        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_SUGGEST_SONG);) {
            state.setInt(1, limitsong);

            ResultSet rs = state.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("title"));
                list.add(new Song(rs.getInt("id_song"), rs.getString("title"), rs.getString("author"), rs.getDouble("duration")));
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public List<Song> searchSong(String key) {
        List<Song> list = new ArrayList<>();

        try (Connection connection = JDBCConnetion.getConnection(); Statement state = connection.createStatement();) {
            String sql = "select * from songDetail where title like \"" + key + "%\";";
            System.out.println(key);
            ResultSet rs = state.executeQuery(sql);

            while (rs.next()) {
                System.out.println("co nay");
                list.add(new Song(rs.getInt("id_song"), rs.getString("title"), rs.getString("author"), rs.getDouble("duration")));
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public Song getSong(int songId) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_SONG);) {
            state.setInt(1, songId);

            ResultSet rs = state.executeQuery();
            rs.next();

            return new Song(rs.getInt("id_song"), rs.getString("title"), rs.getString("author"), rs.getDouble("duration"));

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getSongPath(int songId) {
        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_SONG_PATH);) {
            state.setInt(1, songId);

            ResultSet rs = state.executeQuery();
            rs.next();

            return rs.getString("sourcePath");

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public boolean addSong(String path, String title, String author, double length, String imgurl) {

        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state = connection.prepareStatement(INSERT_SONG);
                PreparedStatement state2 = connection.prepareStatement(INSERT_SONG_DETAIL);
                PreparedStatement state3 = connection.prepareStatement(GET_INSERTED_SONG);) {
            state.setString(1, path);
            int check = state.executeUpdate();

            if (check == 1) {
                ResultSet rs = state3.executeQuery();
                if (rs.next()) {
                    int songId = rs.getInt(1);

                    state2.setInt(1, songId);
                    state2.setString(2, title);
                    state2.setString(3, author);
                    state2.setDouble(4, length);
                    state2.setString(5, imgurl);

                    int check2 = state2.executeUpdate();
                    if (check2 == 1) {
                        return true;
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public String getImgUrl(int songId) {
        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state = connection.prepareStatement(GET_SONG_IMG_URL);) {
            state.setInt(1, songId);

            ResultSet rs = state.executeQuery();
            rs.next();

            return rs.getString("imgUrl");

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
