/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ChallengeType;
import Common.PDU;
import Common.UserChallenge;
import java.util.List;

/**
 *
 * @author fdr
 */
public class INFO_Builder {
    
    static public PDU INFO_REGCHALLENGE(int label, UserChallenge ch){
      
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = ch.getName().getBytes();
        size += data[0].length;
        data[1] = ch.getData().getBytes();
        size+= data[1].length;
        data[2] = ch.getTime().getBytes();
        size+= data[2].length;
        data[3] = ChallengeType.toBytes(ch.getChallengeType());
        size+= data[3].length;
        data[4]= (ch.getNusers()+"").getBytes();
        size+= data[4].length;
        
        return new PDU(0.0f, 0, 1,label, 5, size, data, 0);
    }
    
    static public PDU INFO_ACCEPTCHALLENGE(int label, String nickname,String name){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = nickname.getBytes();
        size += data[0].length;
        data[1] = name.getBytes();
        size+= data[1].length;
        return new PDU(0.0f, 0, 2,label, 2, size, data, 0);
    }
    
    static public PDU INFO_FINISHCHALLENGE(int label, String name,String username,String points){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = name.getBytes();
        size += data[0].length;
        data[1] = username.getBytes();
        size += data[1].length;
        data[2] = points.getBytes();
        size+= data[2].length;
        return new PDU(0.0f, 0, 3,label, 1, size, data, 0);
    }
    
    static public PDU INFO_QUIT(int label, String name){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = name.getBytes();
        size += data[0].length;
        return new PDU(0.0f, 0, 4,label, 1, size, data, 0);
    }
    
    static public PDU INFO_TIME(int label, int time){
        int size = 0;
        byte data[][] = new byte[22][];
        data[5] = String.valueOf(time).getBytes();
        size += data[5].length;
        return new PDU(0.0f, 0, 5, label, 1, size, data, 0);
    }
    
    static public PDU INFO_CANCELA(int label, String name){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = name.getBytes();
        size += data[0].length;
        return new PDU(0.0f, 0, 6,label, 1, size, data, 0);
    }
     
    static public PDU INFO_SYNC(int label, List<UserChallenge> chs){
        //este é tipo pew pew !!!!!!!!
        //isto é a cena que faz sincronismo!!!!!
        //usar com cuidado!
        
        int size = chs.size();
        byte data[][] = new byte[size][];
        size=0;
        int i=0 ;
        for(UserChallenge us : chs){
            data[i] = PDU.toBytes(INFO_REGCHALLENGE(label, us));
            size+=data[i].length;
            i++;
        
        }
        
        return new PDU(0.0f, 0, 7, label, i+1, size, data, 0);
        
    }
}
