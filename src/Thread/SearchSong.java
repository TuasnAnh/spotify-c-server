/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.Song;
import dao.SongDao;
import daoImp.SongDaoImplement;
import java.io.DataInputStream;
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
public class SearchSong extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final DataOutputStream clientRes;
    private final SongDao songDao = new SongDaoImplement();
    private List<Song> searchList;

    public SearchSong(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String searchKey = clientReq.readLine();

            searchList = songDao.searchSong(searchKey);

            if (searchList == null) {
                clientRes.writeInt(0);
            } else {
                clientRes.writeInt(searchList.size());
            }
            if (searchList.size() > 0) {
                searchList.forEach(s -> {
                    try {
                        clientRes.writeInt(s.getSongId());
//                        clientRes.writeBytes(s.getName());
//                        clientRes.writeByte('\n');
//                        clientRes.writeBytes(s.getArtist());
//                        clientRes.writeByte('\n');
                        clientRes.writeUTF(s.getName());
                        clientRes.writeUTF(s.getArtist());
                        clientRes.writeDouble(s.getTime());
                    } catch (IOException ex) {
                        Logger.getLogger(GetSuggestSong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else {
                System.out.println("Empty Search");
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchSong.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                serverSocket.close();
                clientReq.close();
                clientRes.close();
            } catch (IOException ex) {
                Logger.getLogger(SearchSong.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
