/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles_gps;

import java.util.concurrent.ExecutorService;

/**
 *
 * @author brendanmcantosh, emersondelemmus
 */
public class Vehicles_GPS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        buildArrays build = new buildArrays();
        UDP_Connection udp = new UDP_Connection(9099,build);
        TCP_Listener tcp = new TCP_Listener(9099, build);
        
        udp.start();
        tcp.start();
        build.start();
    }
    
    
}
