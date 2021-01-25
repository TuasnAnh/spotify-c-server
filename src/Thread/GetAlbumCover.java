/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import dao.SongDao;
import daoImp.SongDaoImplement;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ADMIN
 */
public class GetAlbumCover extends Thread {

    Socket serverSocket;
    DataInputStream clientReq;
    DataOutputStream clientRes;
    SongDao songDao = new SongDaoImplement();

    public GetAlbumCover(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            int songId = clientReq.readInt();
            String imgUrl = songDao.getImgUrl(songId);

            if (imgUrl != null) {
                BufferedImage image = ImageIO.read(new File(imgUrl));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);
                
                int size = baos.size();
                clientRes.writeInt(size);
                clientRes.write(baos.toByteArray(), 0, size);
                clientRes.flush();
                System.out.println("send image success");
            }
            serverSocket.close();
            clientReq.close();
            clientRes.close();
        } catch (IOException ex) {
            Logger.getLogger(GetAlbumCover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
