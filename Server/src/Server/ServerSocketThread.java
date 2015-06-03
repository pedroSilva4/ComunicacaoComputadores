/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ClassContainer;
import Common.UserChallenge;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class ServerSocketThread extends Thread{
    
    ServerSocket ss;
    ClassContainer container;
    Map<Integer,ServerComunication> sockets;
    int id;
    public ServerSocketThread(int first_id,ServerSocket ss, ClassContainer container, Map<Integer,ServerComunication> sockets){
    this.id = first_id;
    this.ss = ss;
    
    this.sockets = sockets;
    this.container = container;
    }
    
    
    @Override
    public void run(){
        
        Socket s; 
        
        while(true){
            try {
                s =  ss.accept();
               
                ServerComunication coms = new ServerComunication(id,s,container);
                sockets.put(id, coms );
                id++;
                
                /*
                pew pew stuff!!!!
                */
                
                
                List<UserChallenge> list = container.chinfo.getActiveChallenges();
                
                new Sender(s, INFO_Builder.INFO_SYNC(0, list)).start();
                
                
                coms.startReceiver();
                
                
                
                
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
