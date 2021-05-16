/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles_gps;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brendanmcantosh, emersondelemmus
 */
public class UDP_Connection extends Thread {

    private final int port;
    buildArrays build;
    
    public UDP_Connection(int port, buildArrays build) {
        this.port = port;
        this.build = build;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        try (DatagramSocket clientSocket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];
            //clientSocket.setSoTimeout(30000);
            System.out.println("Server is listening on port 9099 for UDP messages\n");
                   
            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, 0, buffer.length);
                clientSocket.receive(dp);

                String recievedM = new String(dp.getData());
                recievedM = recievedM.trim();
                System.out.println("RECEIVED: " + recievedM);
                
                errorCheck(recievedM);
            }

        } catch (SocketException ex) {
            Logger.getLogger(UDP_Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDP_Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String calcCheckSum(String msg) {
        int checksum = 0;

        int end = msg.indexOf('*');
        if (end == -1) {
            end = msg.length();
        }
        for (int i = 1; i < end; i++) {
            checksum = checksum ^ msg.charAt(i);
        }
        String hex = Integer.toHexString(checksum);
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();

    }
    
    public void errorCheck(String msg){
                String orgChksum = msg.substring(msg.lastIndexOf('*')+1);
                VehData dat = new VehData();
                String checkSum = calcCheckSum(msg);
                String pt = msg.substring(0, msg.lastIndexOf('*'));
                //System.out.println(pt);
                String[] data = pt.split(",");
                if (msg.startsWith("$")) {
                    
                    //System.out.println(checkSum);
                    
                    if(!checkSum.equals(orgChksum))
                    {
                        System.out.println("Checksum Invalid");
                        return;
                    }
                    if(!sizeCheck(dat,data))
                    {
                        System.out.println("Too many/few statements");
                    }
                    
                } 
                else 
                    System.out.println("Does not start with $");
                
                build.buildArrays(dat);
                //put string into array of all vehicles. add local time into array
                //make thread that goes through the array and deletes any value where time > 30 seconds
                
    }

    private boolean sizeCheck(VehData dat, String[] data) {
        LocalTime t = LocalTime.now();
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
        dat.ident = data[12];
        dat.status = data[2];
        dat.time = t.format(tf);
        dat.latitude = calcLat(data[3],data[4]);
        dat.longitude = calcLonge(data[5], data[6]);
        dat.speed = data[7];
        dat.heading = data[8];
        
        return true;
    }

    private String calcLat(String lat, String pos) {
        float temp = (float) (Float.parseFloat(lat.substring(2)) / 60.0);
        temp += Float.parseFloat(lat.substring(0,1));
        if (pos.startsWith("N"))
            temp = -temp;
        
        String ret = Float.toString(temp);
        return ret;
    }

    private String calcLonge(String lon, String pos) {
        float temp = (float) (Float.parseFloat(lon.substring(2)) / 60.0);
        temp += Float.parseFloat(lon.substring(0,1));
        if (pos.startsWith("W"))
            temp = -temp;
        
        String ret = Float.toString(temp);
        return ret;
    }
    
    

    
}
