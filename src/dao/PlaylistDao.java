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
public interface PlaylistDao {
    public List<Song> getPlaylist(int userId);
    public boolean insertIntoPlaylist(int userId, int songId);
    public boolean removeFromPlaylist(int userId, int songId);
}
