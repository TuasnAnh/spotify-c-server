/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;
    private int songId;
    private String artist;
    private String name;
    private Double time;
    private String imgurl;

    public Song(int songId, String artist, String name, Double time) {
        this.songId = songId;
        this.artist = artist;
        this.name = name;
        this.time = time;
    }

    public Song(int songId, String artist, String name, Double time, String imgurl) {
        this.songId = songId;
        this.artist = artist;
        this.name = name;
        this.time = time;
        this.imgurl = imgurl;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}
