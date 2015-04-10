package Client;


import Common.PDU;
import com.sun.swing.internal.plaf.basic.resources.basic;
import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header: return  choose License Headers in Project Properties.
 * To change this template file: return  choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro
 */
public final class PDU_Builder {
     
    static public PDU HELLO_PDU(int label){
        return new PDU(0.0f, 0,1, label, 0, 0, null,0);
    }
    
    static public PDU REGISTER_PDU(String name,String nick,byte[] password,int label){
        int size = 0;
        byte[][] data = new byte[3][];
        data[0] = name.getBytes();
        size+= data[0].length;
        data[1] = nick.getBytes();
        size+= data[1].length;
        data[2] = password;
        size+= data[2].length;
        return new PDU(0.0f, 0, 2,label, 3, size, data, 0);
    }    
    
    static public PDU LOGIN_PDU(String nick,byte[] password,int label){
        int size = 0;
        byte[][] data = new byte[2][];
        data[0] = nick.getBytes();
        size+= data[0].length;
        data[1] = password;
        size+= data[1].length;
        
        return new PDU(0.0f, 0, 3,label, 2, size, data, 0);
        
    }
    
    static public PDU LOGOUT_PDU(int label){
        return new PDU(0.0f,0,4,label,0,0,null,0);
    }
    
    static public PDU QUIT_PDU(int label){
        return new PDU(0.0f,0,5,label,0,0,null,0);
    }
    
    static public PDU END_PDU(int label){
        return new PDU(0.0f,0,6,label,0,0,null,0);
    }
    
    static public PDU LIST_CHALLENGES(int label){
        return new PDU(0.0f,0,7,label,0,0,null,0);
    }
    
    static public PDU MAKE_CHALLENGE(int label,String challenge,int date,int time){
        int size = 0;
        byte[][] data = new byte[3][];
        data[0] = challenge.getBytes();
        size += data[0].length;
        data[1] = String.valueOf(date).getBytes();
        size += data[1].length;
        data[2] = String.valueOf(time).getBytes();
        size += data[2].length;
        
        return new PDU(0.0f,0,8,label,3,size,data,0);
    }
    
    static public PDU ACCEPT_CHALLENGE(int label,String challenge){
        int size = 0;
        byte[][] data = new byte[1][];
        data[0] = challenge.getBytes();
        size += data[0].length;
        
        return new PDU(0.0f,0,9,label,1,size,data,0);
    }
    
    static public PDU DELETE_CHALLENGE(int label,String challenge){
        int size = 0;
        byte[][] data = new byte[1][];
        data[0] = challenge.getBytes();
        size += data[0].length;
        return new PDU(0.0f,0,10,label,1,size,data,0);
    }
    
    static public PDU ANSWER(int label,int choice,String challenge,int question){
        int size = 0;
        byte[][] data = new byte[3][];
        data[0] = String.valueOf(choice).getBytes();
        size += data[0].length;
        data[1] = challenge.getBytes();
        size += data[1].length;
        data[2] = String.valueOf(question).getBytes();
        size += data[2].length;
        
        return new PDU(0.0f,0,11,label,3,size,data,0);
    }
    
    static public PDU RETRANSMIT(int label,String challenge,int question,int block){
        int size = 0;
        byte[][] data = new byte[3][];
        data[0] = challenge.getBytes();
        size += data[0].length;
        data[1] = String.valueOf(question).getBytes();
        size += data[1].length;
        data[2] = String.valueOf(block).getBytes();
        size += data[2].length;
        
        return new PDU(0.0f,0,12,label,3,size,data,0);
    }
    
    static public PDU LIST_RANKING(int label){
        return new PDU(0.0f,0,13,label,0,0,null,0);
    }
   
}