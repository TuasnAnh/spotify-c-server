/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.User;
import dao.UserDao;
import daoImp.UserDaoImplement;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class GetUserInfor extends Thread {

    private final Socket serverSocket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final UserDao userDao = new UserDaoImplement();

    public GetUserInfor(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        dis = new DataInputStream(serverSocket.getInputStream());
        dos = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            int userId = dis.readInt();

            User user = userDao.getUserInfo(userId);
//            dos.writeBytes(user.getAccountName());
//            dos.writeByte('\n');
            dos.writeUTF(user.getAccountName());
            dos.writeInt(user.getPhone());
            dos.writeUTF(user.getDateOfBirth());
            dos.writeUTF(user.getCity());
            dos.writeUTF(user.getEmail());
//            dos.writeBytes(user.getDateOfBirth());
//            dos.writeByte('\n');
//            dos.writeBytes(user.getCity());
//            dos.writeByte('\n');
//            dos.writeBytes(user.getEmail());
//            dos.writeByte('\n');
        } catch (IOException ex) {
            Logger.getLogger(GetUserInfor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dis.close();
                dos.close();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(GetUserInfor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
