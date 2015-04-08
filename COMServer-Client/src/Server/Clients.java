/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class Clients {

    private class Client{
        String name;
        String nick;
        byte[] password;
        public Client(String n,String nck,byte[] pass){
            name = n;
            nick = nck;
            password = pass;
        }
    }
    Map<String,Client> registered;
    Map<Integer,String> loggedIn;
    
    public Clients(){
        registered  = new HashMap<>();
        loggedIn  = new HashMap<>();
    }
    
    public boolean registerClient(String name, String nick, byte[] password) {
       if(registered.containsKey(nick))
           return false;
       
       registered.put(nick, new Client(name,nick,password));
       
       return true;
    }
    
    public int login(int port,String nick, byte[] password) {
        if(registered.containsKey(nick)){
            if(registered.get(nick).password == password){
                loggedIn.put(port, nick);
                return 1;
            }
            return -1;
        }
        return -2; 
    }
    
    public int logout(int port){
        if(loggedIn.containsKey(port)){
            loggedIn.remove(port);
            return 0;
        }
        return -1;
    }
    
    
}
