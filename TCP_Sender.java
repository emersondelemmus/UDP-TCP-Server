/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicles_gps;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brendanmcantosh, emersondelemmus
 */
public class TCP_Sender extends Thread {
    private List<VehData> All;
    private List<VehData> Active;
    private jsonObj json = new jsonObj();

    private Socket socket;
    buildArrays build;
    Gson gson = new Gson();

    TCP_Sender(Socket socket, buildArrays build) {
        this.socket = socket;
        this.build = build;
        
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(30000);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            OutputStream output = socket.getOutputStream();  
            //OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("\nConnected from ip: " + socket.getInetAddress() + "\n");
            boolean stop = false;
            while(!stop)
            {
            String str = reader.readLine();
            if(str == null)
            {
                stop = true;
                System.out.println("Connection closed");
                continue;
            }
            
            
            //Parse request
            //If valid send all or available
            // all {"status":"all","vehicles"[]}
            //If invalad send {"status": "invalad","vehicles":[]}
            String send = null;
            if (str.equals("<AVL><vehicles>active</vehicles></AVL>")) //All
            {
                
                System.out.println("Active");
                json.status = "Active";
                json.vehicles = build.getActive();
                
                send = gson.toJson(json) + "\n";
                System.out.println(send);
                out.writeBytes(send);
                continue;
            }// Active
            else if (str.equals("<AVL><vehicles>all</vehicles></AVL>"))
            {
                System.out.println("All");
                
                json.status = "all";
                json.vehicles = build.getAll();
                
                send = gson.toJson(json) + "\n";
                System.out.println(send);
                out.writeBytes(send);
                continue;
            }
            else
            {
                System.out.println("invalid XML");
                json.status = "invalid";
                json.vehicles = build.getAll();
                json.vehicles.clear();
                
                send = gson.toJson(json) + "\n";
                System.out.println(send);
                out.writeBytes(send);
                continue;
            }
                
            }
 
            
            
        } catch (IOException ex) {
            Logger.getLogger(TCP_Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class jsonObj {
        String status;
        List<VehData> vehicles;
    }
    
}
