/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImp;

import bean.Song;
import connection.JDBCConnetion;
import dao.PlaylistDao;
import dao.SongDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class PlaylistDaoImplement implements PlaylistDao {

    SongDao songDao = new SongDaoImplement();
    private static final String GET_PLAYLIST = "select * from playlist where id_user = ?";
    private static final String CHECK_EXISTED_SONG = "select * from playlist where id_song = ? and id_user = ?;";
    private static final String INSERT_PLAYLIST = "insert into playlist (id_user, id_song) values (?, ?);";
    private static final String REMOVE_PLAYLIST = "delete from playlist where id_user = ? and id_song = ?;";

    @Override
    public boolean insertIntoPlaylist(int userId, int songId) {
        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state1 = connection.prepareStatement(CHECK_EXISTED_SONG);
                PreparedStatement state2 = connection.prepareStatement(INSERT_PLAYLIST);) {

            state1.setInt(1, songId);
            state1.setInt(2, userId);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                System.out.println("The song have been already existed in playlist");
                return false;
            } else {
                state2.setInt(1, userId);
                state2.setInt(2, songId);

                int checkInsert = state2.executeUpdate();

                if (checkInsert == 1) {
                    return true;
                } else {
                    System.out.println("Error when insert into playlist");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public List<Song> getPlaylist(int userId) {
        List<Song> list = new ArrayList<>();

        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_PLAYLIST);) {
            state.setInt(1, userId);

            ResultSet rs = state.executeQuery();

            while (rs.next()) {
                list.add(songDao.getSong(rs.getInt("id_song")));
            }

            return list;
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    @Override
    public boolean removeFromPlaylist(int userId, int songId) {
         try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(REMOVE_PLAYLIST);) {
            state.setInt(1, userId);
            state.setInt(2, songId);

            int check = state.executeUpdate();

            if(check == 1) {
                return true;
            } else {
                System.out.println("Error when delete playlist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

}
