package Server;


import Common.ClassContainer;
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
        
        try {
            // TODO code application logic here
            Permission p = new SocketPermission("localhost:4999-","connect,accept,listen");
            
            threadPort = 5001;
            Clients clients = new Clients();
            ChallengesInfo challengesInfo = new ChallengesInfo();
            VirtualChallenges virtualInfo = new VirtualChallenges();
            int id = 0;
            ClassContainer container = new ClassContainer(clients, challengesInfo, virtualInfo);
           
            ServerConnectionHandler tcp_Init = new ServerConnectionHandler(args);
            HashMap<Integer,ServerComunication> coms = new HashMap<>();
            
            if(tcp_Init.SlaveSocket!=null){
                ServerComunication com = new ServerComunication(id, tcp_Init.SlaveSocket, container);
                coms.put(id, com);
                com.startReceiver();
            }
   
            ServerSocketThread sst = new ServerSocketThread(id,tcp_Init.MasterSocket,container,coms);
            sst.start();
            
            
            
            ServerHelloHandler helloHandler = new ServerHelloHandler(clients,challengesInfo,virtualInfo,coms);
            helloHandler.start();
            helloHandler.join();
            
            sst.join();
        } catch (WrongArgumentException ex) {
            System.err.println("argumentos : <myport> or <myport> <serverIP> <serverPort>");
        }
          
        
        
    }
    
    static class ServerHelloHandler extends Thread{
        
        int port = 5000;
        
        DatagramSocket socket;
        Clients clients;
        ChallengesInfo challengeInfo;
        VirtualChallenges virtualInfo;
        HashMap<Integer, ServerComunication> coms;
        public ServerHelloHandler(Clients clients, ChallengesInfo challengeInfo,VirtualChallenges virtualinfo,HashMap<Integer,ServerComunication> coms) throws SocketException, IOException{
            this.socket = new DatagramSocket(this.port);
            this.clients = clients;
            this.challengeInfo= challengeInfo;
            this.virtualInfo = virtualinfo;
            this.coms = coms;
        }
        
        @Override
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
                  new ClientHandler(firstLabel,threadPort,packet,clients,challengeInfo,virtualInfo,coms).start();
                  
                  //incrementa para proximo cliente
                  threadPort++;
             } catch (IOException ex) {
                 Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
             }
           }
           
        }
    }
    
    
    static class WrongArgumentException extends Exception {

        public WrongArgumentException() {
        }
    }

   
}
