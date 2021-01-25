/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import dao.SongDao;
import daoImp.SongDaoImplement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class DownloadMusic extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final SongDao songDao = new SongDaoImplement();
    private final DataOutputStream clientRes;

    public DownloadMusic(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            int userId = clientReq.readInt();
            int songId = clientReq.readInt();

            String filepath = songDao.getSongPath(songId);
            String filename = filepath.split("/")[1];
            //send file
            File fi = new File(filepath);

            try (DataInputStream fis = new DataInputStream(new FileInputStream(filepath))) {
                clientRes.writeUTF(filename);
                clientRes.flush();
                
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                
                while (true) {
                    int read = 0;
                    if (fis != null) {
                        read = fis.read(buf);
                    }
                    
                    if (read == -1) {
                        break;
                    }
                    clientRes.write(buf, 0, read);
                }
                clientRes.flush();
            }
            System.out.println("File Transfer Complete");

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
