/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class ServerSocketThread extends Thread{
    
    ServerSocket ss;
    Clients clients;
    ChallengesInfo challengesInfo;
    ArrayList<Socket> sockets;
    public ServerSocketThread(ServerSocket ss, Clients clients,ChallengesInfo challengesInfo){
    
    this.ss = ss;
    this.clients = clients;
    this.challengesInfo = challengesInfo;
    this.sockets = new ArrayList<>();
    }
    
    
    @Override
    public void run(){
        
        Socket s; 
        
        while(true){
            try {
                s =  ss.accept();
                sockets.add(s);
                new ServerHandler(clients,challengesInfo,s,sockets).start();
                
                
            } catch (IOException ex) {
                Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    ss.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ServerSocketThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        
    }
    
}
