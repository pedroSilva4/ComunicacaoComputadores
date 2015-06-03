package Server;


import Common.ChallengeType;
import Common.ClassContainer;
import Common.PDU;
import Common.UserChallenge;
import Server.Clients.Client;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
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

class ReceiverThread extends Thread{
    int extraports = 1;
     Socket sc;
    BufferedReader in; 
    DataInputStream dataIn;
     boolean isActive = true;
     ClassContainer container;
    public ReceiverThread(Socket sc,ClassContainer container){
         try {
             this.sc = sc;
             this.in =new BufferedReader(new InputStreamReader(sc.getInputStream()));
             this.dataIn = new DataInputStream(sc.getInputStream());
             this.container = container;
         } catch (IOException ex) {
             Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
     @Override
    public void run(){
        while(isActive){
           // try {
                
                int size;
            try {
                
                size = dataIn.readInt();
                   
                    byte[] data = new byte[size];
                    byte[] finaldata  =new byte[size];
                    int read = 0;
                    while(read < size){
                        System.err.println(read +":"+ size );
                      int i = dataIn.read(data);
                      if(i==-1)break;
                      System.arraycopy(data, 0, finaldata, read, i);
                      read+=i;
                    } 

                    PDU request = PDU.fromBytes(finaldata);
                    
                    ParserInfo(request);
                System.out.println(new String(request.getData()[0]));
                
                //parser
                
                //parserInfo
            } catch (IOException ex) {
                Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
             
        }
    }
    
    
    
    
    public void ParserInfo(PDU info){
        switch(info.getType()){
            case 1:{
                //recebe pedido de registo de jogo
                /*data[0] = ch.getName().getBytes();
        size += data[0].length;
        data[1] = ch.getData().getBytes();
        size+= data[1].length;
        data[2] = ch.getTime().getBytes();
        size+= data[2].length;
        data[3] = ChallengeType.toBytes(ch.getChallengeType());
        size+= data[3].length;
        data[4]= (ch.getNusers()+"").getBytes();
        size+= data[4].length;
                */
                byte[][] data = info.getData();
                
                String name = new String(data[0]);
                String date = new String(data[1]);
                
                String time = new String(data[2]);
                System.out.println(name+" "+date+" "+time);
                int nUsers = Integer.parseInt(new String(data[4]));
                ChallengeType type = ChallengeType.fromBytes(data[3]);
                
                
                
                UserChallenge us = new UserChallenge(name, date, time, type, -1);
                
                us.setNusers(nUsers);
                us.setShared();
                
                this.container.chinfo.putUserChallenge(us);
                break;
               
            }
            case 2:{//accept
                String nickname = new String(info.getData()[0]);
                String name = new String(info.getData()[1]);
                boolean us = this.container.chinfo.accept_challenge(name,extraports);
                extraports++;
                this.container.chinfo.getUserChallenge(name).setShared();
                break;
            }
            case 3:{//end challenge
                String name = new String(info.getData()[0]);
                String username = new String(info.getData()[1]);
                int points = Integer.parseInt(new String(info.getData()[2]));
                this.container.chinfo.getUserChallenge(name).finishSared(username, points);
                break;
            }
            case 4:{
                //send user points;
                //#user ended
            }
            case 5:{
                //
            }
        }
    }
}
