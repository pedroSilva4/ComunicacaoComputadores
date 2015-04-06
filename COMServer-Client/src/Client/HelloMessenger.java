/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Common.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class HelloMessenger extends Thread{
    boolean hello_time = true;
        DatagramSocket socket; 
        DatagramPacket packet;
        String host;
        int label;
        
        
        public HelloMessenger(DatagramSocket socket,String host,int label) throws UnknownHostException{
            this.socket = socket;
            this.host = host;
            this.label = label;
        }
        
        @Override
        public void run()
        {
            int label  = 1;
            while(hello_time){
                try {
                    
                    PDU message = PDU_Builder.HELLO_PDU(label);
            
                    byte[] data = PDU.toBytes(message);
                    if(data!= null)
                    packet = new DatagramPacket(data, data.length, InetAddress.getByName(host),5000);
                    else{
                        System.out.println("not okay");
                    }
                    socket.send(packet);
                    
                    if(hello_time)
                        sleep(3000);
                    
                    label++;
                } catch (IOException ex) {
                    Logger.getLogger(HelloMessenger.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HelloMessenger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
          
    
}
