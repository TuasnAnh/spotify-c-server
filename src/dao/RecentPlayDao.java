/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import bean.Song;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RecentPlayDao {
    public boolean insertRecentPlay(int userId, int songId);
    public List<Song> getTopSixRecentPlay(int userId);
}
