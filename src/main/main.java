/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author ADMIN
 */
import java.io.IOException;
import server.server;

public class main {
    public static void main(String[] args) throws IOException {
        new server().run();
    }
}
