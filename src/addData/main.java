/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addData;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import dao.SongDao;
import daoImp.SongDaoImplement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;

/**
 *
 * @author ADMIN
 */
public class main {

    public static void main(String[] args) throws FileNotFoundException, IOException, SAXException, UnsupportedTagException, InvalidDataException {
        SongDao songDao = new SongDaoImplement();

        String foldername = "music/";

        File folder = new File(foldername);
        File[] listOfFiles = folder.listFiles();
        List<String> listPath = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                listPath.add(foldername + file.getName());
            }
        }

        for (int i = 0; i < listPath.size(); i++) {
            String path = listPath.get(i);
            Mp3File mp3file = new Mp3File(path);
            System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                System.out.println("Artist: " + id3v2Tag.getArtist());
                System.out.println("Title: " + id3v2Tag.getTitle());
                byte[] imageData = id3v2Tag.getAlbumImage();
                if (imageData != null) {
                    FileOutputStream file = new FileOutputStream("albumcover/" + id3v2Tag.getTitle() + ".jpeg");
                    file.write(imageData);
                    file.close();
                    songDao.addSong(path, id3v2Tag.getTitle(), id3v2Tag.getArtist(), mp3file.getLengthInSeconds(), "albumcover/" + id3v2Tag.getTitle() + ".jpeg");
                } else {
                    songDao.addSong(path, id3v2Tag.getTitle(), id3v2Tag.getArtist(), mp3file.getLengthInSeconds(), "spotify.jpg");
                }
            }
        }

    }
}
