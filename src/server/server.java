/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Thread.AddToPlaylist;
import Thread.AddToRecentplay;
import Thread.ChangePassword;
import Thread.DownloadMusic;
import Thread.GetAlbumCover;
import Thread.GetPlaylist;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Thread
import Thread.LoginThread;
import Thread.RegisterThread;
import Thread.GetRecentplay;
import Thread.GetSuggestSong;
import Thread.GetUserInfor;
import Thread.PlaySong;
import Thread.RemoveFromPlaylist;
import Thread.SearchSong;

/**
 *
 * @author ADMIN
 */
public class server {

    private final int port = 4567;

    public server() {

    }

    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started!");

        while (true) {
            System.out.println("server waiting!");
            Socket server = serverSocket.accept();
            DataInputStream requestType = new DataInputStream(server.getInputStream());
            String clientRequest = requestType.readLine();
            System.out.println("Action: " + clientRequest);

            switch (clientRequest) {
                case "register":
                    new RegisterThread(server).start();
                    break;
                case "login":
                    new LoginThread(server).start();
                    break;
                case "getSuggest":
                    new GetSuggestSong(server).start();
                    break;
                case "getRecentplay":
                    new GetRecentplay(server).start();
                    break;
                case "search":
                    new SearchSong(server).start();
                    break;
                case "getPlaylist":
                    new GetPlaylist(server).start();
                    break;
                case "getUserInfor":
                    new GetUserInfor(server).start();
                    break;
                case "addToPlaylist":
                    new AddToPlaylist(server).start();
                    break;
                case "removeFromPlaylist":
                    new RemoveFromPlaylist(server).start();
                    break;
                case "addRecentplay":
                    new AddToRecentplay(server).start();
                    break;
                case "play":
                    new PlaySong(server).start();
                    break;
                case "changepassword":
                    new ChangePassword(server).start();
                    break;
                case "downloadmusic":
                    new DownloadMusic(server).start();
                    break;
                case "getAlbumCover":
                    new GetAlbumCover(server).start();
            }
        }
    }
}
