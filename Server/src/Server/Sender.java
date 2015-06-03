package Server;


import Common.PDU;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public class Sender extends Thread{
    DataOutputStream out;
    final Socket sc;
    PDU message;
    public Sender(Socket sc,PDU message) throws IOException{
        this.sc = sc;
            this.out = new DataOutputStream(sc.getOutputStream());
            this.message = message;
          
    }

    @Override
    public void run(){
    
        try {
            byte[] tosend = PDU.toBytes(message);
            System.out.println(tosend.length);
            
            synchronized(sc.getOutputStream()){
                
            this.out.writeInt(tosend.length);
            this.out.write(tosend,0,tosend.length);
            this.out.flush();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}
