package Server;


import Common.Challenge;
import Common.PDU;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        Clients clients = new Clients();
        Map<String,Challenge> Challenges = new HashMap<>();
        loadChallenges((HashMap<String, Challenge>) Challenges);
        new ServerHelloHandler(clients).start();
    }
    
    static class ServerHelloHandler extends Thread{
        
        int port = 5000;
        
        DatagramSocket socket;
        Clients clients;
        public ServerHelloHandler(Clients clients) throws SocketException, IOException{
            socket = new DatagramSocket(port);
            this.clients = clients;
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
                  connectionsMap.put(threadPort, new ClientHandler(firstLabel,threadPort,packet,clients));
                  
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
    
    public static void loadChallenges(HashMap<String,Challenge> challenges) {
        
        File folder = new File("Challenges");
        
        for(File f: folder.listFiles()){
            if(f.getPath().endsWith(".ch")){
                try {
                    System.out.println(f.getPath());
                    Challenge ch = new Challenge(f.getPath());
                    challenges.put(ch.getName(), ch);
                    System.out.println("Challenge "+ch.getName()+" Carregado");
                } catch (IOException ex) {
                   System.out.println("Ocurreu um erro ao carregar desafio");
                }
            }
        }
    }
}
