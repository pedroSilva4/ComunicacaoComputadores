package Server;


import Common.PDU;
import Client.PDU_Builder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
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
public class Server {

    /**
     * @param args the command line arguments
     */
    private static  int threadPort;
    private static Map<Integer,ClientHandler> connectionsMap;
    public static void main(String[] args) throws SocketException, IOException {
        // TODO code application logic here
        threadPort = 5001;
        connectionsMap = new HashMap<>();
        new ServerHelloHandler().start();
    }
    
    static class ServerHelloHandler extends Thread{
        
        int port = 5000;
        
        DatagramSocket socket;
        public ServerHelloHandler() throws SocketException, IOException{
            socket = new DatagramSocket(port);
        }
        
        public void run(){
           while(true){
            try {
                   
                    
                  byte[] buff  =new byte[256];
                  DatagramPacket packet = new DatagramPacket(buff, buff.length);             
                  socket.receive(packet);
     
                  byte[] data = packet.getData();
                  PDU message = PDU.fromBytes(data);
                  int firstLabel  =message.getLabel();
                  System.out.println(message.toString());
                  
                  //recebe hello
                  connectionsMap.put(threadPort, new ClientHandler(firstLabel,threadPort,packet));
                  
                  //inicia a thread do cliente no servidor
                  connectionsMap.get(threadPort).start();
                  
                  //incrementa para proximo cliente
                  threadPort++;
             } catch (IOException ex) {
                 Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
             }
           }
           
        }
    }
}
