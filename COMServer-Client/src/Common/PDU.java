package Common;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class PDU implements Serializable{
    private int inc = 0;
    private final float version;
    private final int security;
    private final int id;
    private final int type;
    private final int nFields;
    private final int size; //max 48k
    private final byte[][] data;
    private final int hasnext;

    public PDU(float version,int security, int type,int label, int nFields, int size, byte[][] data, int hasnext){
        this.version = version;
        this.security = security;
        this.id = label;
        this.type = type;
        this.nFields = nFields;
        this.size = size;
        this.data = data;
        this.hasnext = hasnext;
    }
    
    static public byte[] toBytes(PDU pdu){
        try {
            byte[] object;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out;
            out = new ObjectOutputStream(bos);   
            out.writeObject(pdu);
            object = bos.toByteArray();
            out.close();
            bos.close();
            
            return object;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    static public PDU fromBytes(byte[] bytes){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in;
            in = new ObjectInputStream(bis);
            PDU o = (PDU) in.readObject();
            bis.close();
            in.close();
            
            return o;
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
    
    public String toString()
    {
        StringBuilder sb  = new StringBuilder();
        sb.append(this.type);
        return sb.toString();
    }

    public int getType() {
        return this.type;
    }
    
    public int getLabel(){
        return this.id;
    }
    
    public byte[][] getData(){
        return this.data;
    }
    
    
}
