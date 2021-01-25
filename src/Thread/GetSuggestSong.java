/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.Song;
import dao.SongDao;
import daoImp.SongDaoImplement;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class GetSuggestSong extends Thread {

    Socket serverSocket;
    DataOutputStream oos;
    SongDao songDao = new SongDaoImplement();
    private List<Song> listSuggestion;

    public GetSuggestSong(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.oos = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        listSuggestion = songDao.getSuggestionSong();
        try {
            if (listSuggestion != null || !listSuggestion.isEmpty()) {
                oos.writeInt(listSuggestion.size());
                listSuggestion.forEach(s -> {
                    try {
                        oos.writeInt(s.getSongId());
//                        oos.writeBytes(s.getName());
//                        oos.writeByte('\n');
//                        oos.writeBytes(s.getArtist());
//                        oos.writeByte('\n');
                        oos.writeUTF(s.getName());
                        oos.writeUTF(s.getArtist());
                        oos.writeDouble(s.getTime());
                    } catch (IOException ex) {
                        Logger.getLogger(GetSuggestSong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

            serverSocket.close();
            oos.close();
        } catch (IOException ex) {
            System.out.println("Error when sending suggestion list");
            Logger.getLogger(GetSuggestSong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
