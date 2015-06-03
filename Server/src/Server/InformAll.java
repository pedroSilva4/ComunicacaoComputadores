/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.PDU;
import java.util.HashMap;

/**
 *
 * @author Pedro
 */
public class InformAll extends Thread{
    HashMap<Integer,ServerComunication> coms;
    PDU message;
    public InformAll( HashMap<Integer,ServerComunication> coms,PDU message){
        this.coms = coms;
        this.message = message;
    }
    
    
    @Override
    public void run(){
        for(ServerComunication com : coms.values()){
            com.send(message);
        }
    }
}
