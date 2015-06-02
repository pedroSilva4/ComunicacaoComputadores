package Server;


import Common.ChallengeType;
import Server.INFO_Builder;
import Common.PDU;
import Common.UserChallenge;
import Server.ServerComunication;
import java.io.IOException;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public class test1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Socket sc = new Socket("localhost", 50000);
        
        ServerComunication com = new ServerComunication(1, sc, null);
        com.startReceiver();
        
       
        ChallengeType cht = new ChallengeType("C:\\Users\\Pedro\\Documents\\GitHub\\ComunicacaoComputadores\\Server\\Challenges\\desafio-02.txt");
        UserChallenge ch = new UserChallenge("arroz", "150615", "124000", cht, 3);
         PDU pdu = INFO_Builder.INFO_REGCHALLENGE(0,ch );
         
         com.send(pdu);
        
        
        
    }
    
}
