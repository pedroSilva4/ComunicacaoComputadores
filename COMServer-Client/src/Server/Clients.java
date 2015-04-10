/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class Clients {

    public class Client{
        String name;
        String nick;
        byte[] password;
        public Client(String n,String nck,byte[] pass){
            name = n;
            nick = nck;
            password = pass;
        }
    }
    
    public  Map<String,Client> registered;
    public Map<Integer,String> loggedIn;
    public Map<String,Integer> ranking;
    
    public Clients(){
        registered  = new HashMap<>();
        loggedIn  = new HashMap<>();
        ranking = new HashMap<>();
    }
    
    synchronized public boolean registerClient(String name, String nick, byte[] password) {
       if(registered.containsKey(nick))
           return false;
       
       registered.put(nick, new Client(name,nick,password));
       ranking.put(nick,0);
       
       return true;
    }
    
    synchronized public int login(int port,String nick, byte[] password) {
        if(registered.containsKey(nick)){
            if(Arrays.equals(registered.get(nick).password, password)){
                loggedIn.put(port, nick);
                return 1;
            }
            return -1;
        }
        return -2; 
    }
    
    synchronized public int logout(int port){
        if(loggedIn.containsKey(port)){
            loggedIn.remove(port);
            return 0;
        }
        return -1;
    }
    
    synchronized public String getName(String nick){
        return this.registered.get(nick).name;
    }
    
    synchronized public int getPoints(String nick){
        return this.ranking.get(nick);
    }
    
    
}
