package Client;


import Common.PDU;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
public class Client {

    /**
     * @param args the command line arguments
     */
    static int ComunicationPort;
    public static void main(String[] args) throws SocketException, IOException, InterruptedException {
       
      
        String Server_host = "localhost";
        DatagramSocket socket = new DatagramSocket(4444);
        int label  = 1;
        HelloMessenger hellom = new HelloMessenger(socket,Server_host,label);
        hellom.start();
        
        DatagramPacket ACK = new DatagramPacket(new byte[256], 256);
        socket.receive(ACK);
        PDU reply = PDU.fromBytes(ACK.getData());
        if(reply.getType()==0)
        {
            System.out.println(ACK.getAddress().getHostAddress()+": "+ ACK.getPort() + " -->  says it's okay");
            ComunicationPort = ACK.getPort();
            hellom.hello_time = false;
            
        }
            hellom.join();
            socket.connect(InetAddress.getByName(Server_host), ComunicationPort);
            
        
    }
    
    
                
    
}
    

