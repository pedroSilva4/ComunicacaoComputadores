/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ClassContainer;
import Common.PDU;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class ServerComunication {
    int id;
    Socket sc;
    ReceiverThread rec;
    ClassContainer container;
    public ServerComunication(int id,Socket socket,ClassContainer container){
        this.id = id;
        this.sc = socket;
        this.rec = new ReceiverThread(sc,container);
        this.container = container;
    }
    
    public void startReceiver(){
        this.rec.start();
    }
    
    public void joinReceiver(){
        this.rec.isActive = false;
        try {
            this.rec.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerComunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(PDU message){
        try {
            new Sender(sc, message).start();
            
        } catch (IOException ex) {
            Logger.getLogger(ServerComunication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
