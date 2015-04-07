/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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
    }
    Map<String,Client> registered;
    Map<Integer,String> loggedIn;
    
    public Clients(){
        registered  = new HashMap<>();
        loggedIn  = new HashMap<>();
    }
}
