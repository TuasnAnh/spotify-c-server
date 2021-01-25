/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

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
public class LoginThread extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final DataOutputStream clientRes;
    private UserDao userDao = new UserDaoImplement();

    public LoginThread(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String username = clientReq.readLine();
            String password = clientReq.readLine();

            boolean checkLogin = userDao.checkUserLogin(username, password);
            clientRes.writeBoolean(checkLogin);
            if (checkLogin) {
                clientRes.writeInt(userDao.getUserId(username));
            }

            clientReq.close();
            clientRes.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
