package Server;


import Common.PDU;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

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
    
    public static void main(String[] args) throws SocketException, IOException, InterruptedException {
        // TODO code application logic here
        Permission p = new SocketPermission("localhost:4999-","connect,accept,listen");
        
        threadPort = 5001;
        Clients clients = new Clients();
        ChallengesInfo challengesInfo = new ChallengesInfo();
      
        try {
          ServerConnectionHandler severHandler = new ServerConnectionHandler(args);
        } catch (WrongArgumentException ex) {
            System.err.println("Wrong arguments!");
            return;
            
        }
        
       // ServerHelloHandler helloHandler = new ServerHelloHandler(clients,challengesInfo);
        //helloHandler.start();
       // helloHandler.join();
        
    }
    
    static class ServerHelloHandler extends Thread{
        
        int port = 5000;
        
        DatagramSocket socket;
        Clients clients;
        ChallengesInfo challengeInfo;
        public ServerHelloHandler(Clients clients, ChallengesInfo challengeInfo) throws SocketException, IOException{
            socket = new DatagramSocket(port);
            this.clients = clients;
            this.challengeInfo= challengeInfo;
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
                  
                  
                  //inicia a thread do cliente no servidor
                  new ClientHandler(firstLabel,threadPort,packet,clients,challengeInfo).start();
                  
                  //incrementa para proximo cliente
                  threadPort++;
             } catch (IOException ex) {
                 Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
             }
           }
           
        }
    }
    
    
    static class ServerConnectionHandler{
        
        private ServerSocket socketMaster;
        private Socket socketSlave;
        public ServerConnectionHandler(String[] args) throws WrongArgumentException{
       try{
            switch(args[0]){
                case "-master":{
                    socketMaster  = new ServerSocket(50000);
                    //runnable que recebe pedidos de slaves
                }
                case "-slave":{
                    String ip = args[1];
                    socketSlave = new Socket(ip,50000);
                }
                default: throw new WrongArgumentException();
            }
        }catch(IOException e){
            System.out.println("Ocorreu um erro a inicializar o servidor");
        }catch(ArrayIndexOutOfBoundsException e){
            throw new WrongArgumentException();
        }
       
       
        
        }
    }

    private static class WrongArgumentException extends Exception {

        public WrongArgumentException() {
        }
    }
}
