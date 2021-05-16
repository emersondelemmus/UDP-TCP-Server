/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles_gps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brendanmcantosh, emersondelemmus
 */
public class TCP_Listener extends Thread {
int port;
buildArrays build;
public TCP_Listener(int port, buildArrays build){
    this.port = port;
    this.build = build;
}
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(this.port))
        {
            while(true)
            {
                Socket socket = server.accept();
                new TCP_Sender(socket,build).start();
                System.out.println("\nGetVeh.jar accepted connection request...");
                
            }
        } catch (IOException ex) {
        Logger.getLogger(TCP_Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
