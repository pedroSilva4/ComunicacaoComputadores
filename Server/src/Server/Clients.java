/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.UserChallenge.MapEntryComparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

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
    
    synchronized public int addPoints(String nick,int points){
        int p = this.ranking.get(nick);
        p+=points;
        this.ranking.replace(nick, p);
        return p;
    }
    
    synchronized public String getGeneralRanking() {
        MapEntryComparator mapComp =new MapEntryComparator();
        TreeSet<Map.Entry<String,Integer>> rankingl = new TreeSet<>(mapComp);
        rankingl.addAll(this.ranking.entrySet());
                              
                             Iterator it = rankingl.iterator();
                             String scores="";
                             int i=1;
                              while(it.hasNext()){
                                   Map.Entry<String,Integer> entry =(Map.Entry<String,Integer>)it.next();
                                   
                                   scores+= i+"º :"+ entry.getKey()+" : "+entry.getValue()+"\n";     
                                   i++;
                              }
                              return scores;
    }

}
