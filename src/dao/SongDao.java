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
public interface SongDao {
    public List<Song> getSuggestionSong();
    public List<Song> searchSong(String key);
    public Song getSong(int songId);
    public String getSongPath(int songId);
    public boolean addSong(String path, String title, String author, double length, String imgurl);
    public String getImgUrl(int songId);

}
