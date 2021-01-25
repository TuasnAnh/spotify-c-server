/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImp;

import bean.Song;
import connection.JDBCConnetion;
import dao.RecentPlayDao;
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
public class RecentPlayDaoImplement implements RecentPlayDao {

    private final SongDao songDao = new SongDaoImplement();

    private static final int recentLimiter = 6;
    private static final String CHECK_EXISTED_RECENT = "SELECT * FROM recentplay where id_user = ? and id_song = ?;";
    private static final String DELTE_EXISTED_RECENT = "delete FROM recentplay where id_user = ? and id_song = ?;";

    private static final String GET_ROW_COUNT = "SELECT COUNT(*) FROM recentplay where id_user = ?;";
    private static final String DELETE_LASTEST_SONG = "delete from recentplay where id_user = ? order by id limit 1;";
    private static final String INSERT_RECENT_PLAY = "insert into recentplay (id_user, id_song) values (?, ?);";
    private static final String GET_TOP_RECENT_PLAY = "select * from recentplay where id_user = ? order by id desc limit " + recentLimiter + ";";

    @Override
    public boolean insertRecentPlay(int userId, int songId) {
        // TOPO: if the song is already in recent play so dont add
        try (Connection connection = JDBCConnetion.getConnection();
                PreparedStatement state1 = connection.prepareStatement(GET_ROW_COUNT);
                PreparedStatement state2 = connection.prepareStatement(DELETE_LASTEST_SONG);
                PreparedStatement state3 = connection.prepareStatement(INSERT_RECENT_PLAY);
                PreparedStatement state4 = connection.prepareStatement(CHECK_EXISTED_RECENT);
                PreparedStatement state5 = connection.prepareStatement(DELTE_EXISTED_RECENT);) {

            state4.setInt(1, userId);
            state4.setInt(2, songId);
            ResultSet rs1 = state4.executeQuery();
            if (rs1.next()) {
                // remove existed recent play then add new one
                state5.setInt(1, userId);
                state5.setInt(2, songId);
                int check = state5.executeUpdate();
                if (check == 1) {
                    state3.setInt(1, userId);
                    state3.setInt(2, songId);

                    int checkInsert = state3.executeUpdate();
                    if (checkInsert == 1) {
                        return true;
                    } else {
                        System.out.println("Error when insert into recent play");
                    }
                }
            } else {
                // check limited row then add new one
                state1.setInt(1, userId);
                ResultSet rs = state1.executeQuery();
                rs.next();

                int rowCount = rs.getInt(1);
                System.out.println("ROW COUNT:" + rowCount);
                if (rowCount >= 6) {
                    state2.setInt(1, userId);

                    if (state2.executeUpdate() == 1) {
                        state3.setInt(1, userId);
                        state3.setInt(2, songId);

                        int checkInsert = state3.executeUpdate();
                        if (checkInsert == 1) {
                            return true;
                        } else {
                            System.out.println("Error when insert into recent play");
                        }

                    } else {
                        System.out.println("Error when delete recent play");
                    }
                } else {
                    state3.setInt(1, userId);
                    state3.setInt(2, songId);

                    int checkInsert = state3.executeUpdate();
                    if (checkInsert == 1) {
                        return true;
                    } else {
                        System.out.println("Error when insert into recent play");
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Song> getTopSixRecentPlay(int userId) {
        List<Song> list = new ArrayList<>();

        try (Connection connection = JDBCConnetion.getConnection(); PreparedStatement state = connection.prepareStatement(GET_TOP_RECENT_PLAY);) {
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
}
