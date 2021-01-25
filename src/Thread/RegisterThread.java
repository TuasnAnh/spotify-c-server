/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Thread;

import bean.User;
import dao.UserDao;
import daoImp.UserDaoImplement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
public class RegisterThread extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final DataOutputStream clientRes;
    private UserDao userDao = new UserDaoImplement();

    public RegisterThread(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
        this.clientRes = new DataOutputStream(serverSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String username = clientReq.readLine();

            if (!userDao.checkExistedUser(username)) {
                clientRes.writeBytes("usernameVerified");
                clientRes.writeByte('\n');

                String password = clientReq.readLine();
                String accoutName = clientReq.readLine();
                String dateOfBirth = clientReq.readLine();
                String city = clientReq.readLine();
                int phone = clientReq.readInt();
                String email = clientReq.readLine();
                boolean checkInsert = userDao.insertUser(new User(username, password, accoutName, dateOfBirth, city, phone, email));
                clientRes.writeBoolean(checkInsert);
            } else {
                clientRes.writeBytes("usernameExisted");
            }

            clientReq.close();
            clientRes.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("error");
            Logger.getLogger(RegisterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
