/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.Song;
import dao.RecentPlayDao;
import daoImp.RecentPlayDaoImplement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * s
 *
 * @author ADMIN
 */
public class GetRecentplay extends Thread {

    Socket serverSocket;
    DataOutputStream dos;
    DataInputStream dis;
    RecentPlayDao recentDao = new RecentPlayDaoImplement();
    private List<Song> listRecentPlay;

    public GetRecentplay(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.dos = new DataOutputStream(serverSocket.getOutputStream());
        this.dis = new DataInputStream(serverSocket.getInputStream());
    }

    @Override
    public void run() {

        try {
            int userId = dis.readInt();

            listRecentPlay = recentDao.getTopSixRecentPlay(userId);
            dos.writeInt(listRecentPlay.size());

            if (listRecentPlay != null || !listRecentPlay.isEmpty()) {
                listRecentPlay.forEach(s -> {
                    try {
                        dos.writeInt(s.getSongId());
//                        dos.writeBytes(s.getName());
//                        dos.writeByte('\n');
//                        dos.writeBytes(s.getArtist());
//                        dos.writeByte('\n');
                        dos.writeUTF(s.getName());
                        dos.writeUTF(s.getArtist());
                        dos.writeDouble(s.getTime());
                    } catch (IOException ex) {
                        Logger.getLogger(GetSuggestSong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

            serverSocket.close();
            dos.close();
            dis.close();
        } catch (IOException ex) {
            System.out.println("Error when sending recent play list");
            Logger.getLogger(GetSuggestSong.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
