/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.Server.WrongArgumentException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Pedro
 */
public class ServerConnectionHandler {
    
    final ServerSocket MasterSocket;
    final Socket SlaveSocket;
    String connectionIP = "";
    int myport = 50000;
    int serverPort = 50000;
    public ServerConnectionHandler(String[] args) throws WrongArgumentException, IOException{
        try{
            if(args.length<1){
                throw  new WrongArgumentException();
            }
            else{
                if(args.length==1)
                {
                   myport = Integer.parseInt(args[0]); 
                   MasterSocket = new ServerSocket(myport);
                   SlaveSocket  =null;
                }
                else{
                    myport = Integer.parseInt(args[0]);
                    connectionIP = args[1];
                    serverPort = Integer.parseInt(args[2]);
                    MasterSocket = new ServerSocket(myport);
                    SlaveSocket = new Socket(connectionIP, serverPort);
                }
            }
        }catch(ArrayIndexOutOfBoundsException ex){
            throw new WrongArgumentException();
        }
    
    }
    
}

