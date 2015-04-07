/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.PDU;
import Client.PDU_Builder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class ClientHandler extends Thread{
    int port;
    DatagramSocket socket;
    int packetPort;
    InetAddress packetAdress;
    int currentLabel;
    Clients clients;
            
    
    public ClientHandler(int firstLabel,int port,DatagramPacket packet,Clients clients) throws SocketException{
        this.port = port;
        socket = new DatagramSocket(port);
        packetPort = packet.getPort();
        packetAdress = packet.getAddress();
        this.currentLabel = firstLabel;
        this.clients = clients;
        
    }
    
    public void run(){
       byte[] data = PDU.toBytes(REPLY_Builder.REPLY_OK(currentLabel));
       DatagramPacket packet = new DatagramPacket(data, data.length, packetAdress, packetPort);
        try {
            socket.send(packet);
            
            boolean logout = false;
            //currentLabel incrementa toma sempre o valor do PDU recebido pelo servidor, 
            //pois o servidor so faz reply, e os reply tens a mesma label que as mensagens dos clientes
            DatagramPacket request,reply;
            socket.connect(packetAdress, packetPort);
            while(!logout){
              request = new DatagramPacket(new byte[1024],1024,packetAdress,packetPort);  
              
              socket.receive(request);
              
              PDU requestPDU = PDU.fromBytes(request.getData());
                System.out.println("rquest tipo -> " + requestPDU.getType());
              PDU replyPDU  = parsePDU(requestPDU);
             // if(replyPDU!= null){
                if(replyPDU.getType()==4) logout=true;

                byte[] replyData = PDU.toBytes(replyPDU);
                reply = new DatagramPacket(replyData, replyData.length, packetAdress, packetPort);
                socket.send(reply);
              //}
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            socket.close();
        }
    }
    
    public PDU parsePDU(PDU requestPDU){
        byte[][] fields= requestPDU.getData();
        if(fields!=null){
            switch(requestPDU.getType()){
                case 2:{//register

                    String name  = new String(fields[0]);
                    String nick = new String(fields[1]);
                    byte[] password = fields[2];

                    boolean ok = clients.registerClient(name,nick,password);

                    if(!ok)//return erro
                        return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "ja existe alguem com esse nickname");
                    //return register reply
                    return REPLY_Builder.REPLY_OK(requestPDU.getLabel());
                }
                case 3:{//login
                    String nick = new String(fields[0]);
                    byte[] password = fields[1];
                    
                    int ok = clients.login(port,nick,password);
                    
                    if(ok== -1)
                       return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(),"password errada");
                    if(ok== -2)
                       return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(),"nick nao existe");
                        
                    return REPLY_Builder.REPLY_NICKNAME(requestPDU.getLabel(), nick);
                }   
                case 4:{//logout
                       return null;
                }
            }
       }
        return null;
    }
    
}
