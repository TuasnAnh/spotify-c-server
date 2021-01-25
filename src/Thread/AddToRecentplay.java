/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import dao.PlaylistDao;
import dao.RecentPlayDao;
import daoImp.PlaylistDaoImplement;
import daoImp.RecentPlayDaoImplement;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class AddToRecentplay extends Thread{
    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final RecentPlayDao recentDao = new RecentPlayDaoImplement();

    public AddToRecentplay(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            int userId = clientReq.readInt();
            int songId = clientReq.readInt();
            recentDao.insertRecentPlay(userId, songId);
            
        } catch (IOException ex) {
            Logger.getLogger(SearchSong.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                serverSocket.close();
                clientReq.close();
            } catch (IOException ex) {
                Logger.getLogger(SearchSong.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
