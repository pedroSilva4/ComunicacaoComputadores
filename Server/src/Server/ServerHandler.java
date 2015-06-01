/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.INFO_Builder;
import Common.PDU;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
class ServerHandler extends Thread{
    
    Socket sc;
    Clients cls;
    ChallengesInfo chs;
    ArrayList<Socket> sockets;
    public ServerHandler(Clients clients, ChallengesInfo challengesInfo, Socket s,ArrayList<Socket> sockets) {
        this.sc = s;
        this.cls = clients;
        this.chs = challengesInfo;
        this.sockets = sockets; 
    }
   
    public void run(){
        
        
      //   new InformAll(INFO_Builder.INFO_SERVERIP(0,1), sc, sockets).start();
         try {
            
                InputStream in = sc.getInputStream();
                OutputStream out = sc.getOutputStream();
                DataInputStream dis = new DataInputStream(in);
                boolean run = true;
                while (run) {


                    int size = dis.readInt();
                    byte[] data = new byte[size];
                    byte[] finaldata  =new byte[size];
                    int read = 0;
                    while(read < size){
                      int i = dis.read(data);
                      if(i==-1)break;
                      System.arraycopy(data, 0, finaldata, read, i);
                      read+=i;
                    } 

                    PDU request = PDU.fromBytes(finaldata);
                    
                    //parser;
                    PDU reply = parseAndDoWhateveryouDO(request);
                    
                    //responder
                    data = PDU.toBytes(reply);
                    out.write(data.length);
                    out.write(data);
                    
                    
                    
                    //informar todos,isto so acontece em alguns casos como anuncios e etc.
                   // new InformAll(null,sc,sockets).start(); 
                   
                }
            sc.close();
           

        } catch (IOException ex) {
           
        }
    }
    
    
    class InformAll extends Thread{
        Socket sc; 
        ArrayList<Socket> sockets;
        byte[] pdu;
        InformAll(PDU pdu,Socket sc, ArrayList<Socket> sockets){
            this.sc = sc;
            this.sockets  =sockets;
            this.pdu = PDU.toBytes(pdu);
        }
        
        
       public void run(){
           for(Socket s : sockets){
                   if(sc==null || !s.equals(sc)){
                       try {
                           
                           OutputStream out = s.getOutputStream();
                           
                           
                           out.write(this.pdu.length);
                           out.write(this.pdu);
                           
                       } catch (IOException ex) {
                           Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                       
                       }
                    } 
           }
        }
    }
    
    
    private PDU parseAndDoWhateveryouDO(PDU request){
        
        int type =request.getType();
        
        switch(type){
            case 1:{
               
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
        }
        
        return null;
    }
}
