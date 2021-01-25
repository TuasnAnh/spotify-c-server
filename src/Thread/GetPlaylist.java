/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.Song;
import dao.PlaylistDao;
import dao.SongDao;
import daoImp.PlaylistDaoImplement;
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
public class GetPlaylist extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final DataOutputStream clientRes;
    private final PlaylistDao playlistDao = new PlaylistDaoImplement();
    private List<Song> playlist;

    public GetPlaylist(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            int userId = clientReq.readInt();

            playlist = playlistDao.getPlaylist(userId);

            if (playlist == null) {
                clientRes.writeInt(0);
            } else {
                clientRes.writeInt(playlist.size());
            }
            if (playlist.size() > 0) {
                playlist.forEach(s -> {
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
                System.out.println("Empty Playlist");
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
