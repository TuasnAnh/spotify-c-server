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
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ADMIN
 */
public class PlaySong extends Thread {

    private final Socket serverSocket;
    private final DataInputStream clientReq;
    private final SongDao songDao = new SongDaoImplement();

    public PlaySong(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.clientReq = new DataInputStream(serverSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            int songId = clientReq.readInt();

            String songPath = songDao.getSongPath(songId);

            DataOutputStream dos = new DataOutputStream(serverSocket.getOutputStream());
            dos.writeBytes(songPath);
            dos.writeByte('\n');

            Song songInfo = songDao.getSong(songId);
            if (songInfo != null) {
                dos.writeUTF(songInfo.getName());
                dos.writeUTF(songInfo.getArtist());
                dos.writeDouble(songInfo.getTime());

                System.out.println("Sending song:" + songInfo.getName());
                File file = new File(songPath);
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
                AudioInputStream sendAudio = AudioSystem.getAudioInputStream(decodedFormat, audio);

                byte[] data = new byte[4096];
                int count;
                int counter = 0;

                while ((count = sendAudio.read(data, 0, data.length)) != -1) {
//                    System.out.println(counter++ + " " + count);
                    dos.writeInt(count);
                    dos.flush();
                    dos.write(data, 0, count);
                    dos.flush();
                }
                System.out.println("Send file success");
            }

        } catch (IOException ex) {
            Logger.getLogger(SearchSong.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(PlaySong.class.getName()).log(Level.SEVERE, null, ex);
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
