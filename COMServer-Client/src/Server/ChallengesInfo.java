/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.Challenge;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Pedro
 */
public class ChallengesInfo {
    Map<String,UserChallenge> challenges ;
    Map<String,Challenge> challengesTypes ;
     
    public ChallengesInfo(){
         challenges = new HashMap<>();
         challengesTypes = new HashMap<>();
         loadChallenges((HashMap<String, Challenge>) challengesTypes);
    }
    
    synchronized public boolean make_challenge(String name,Date date,Time time,InetAddress makerAddress,int port){
       Random r =new Random();
       int i = r.nextInt(this.challengesTypes.keySet().size());
       String type= (String)challengesTypes.keySet().toArray()[i];
       if(challenges.containsKey(name)) return false;
       
       challenges.put(name, new UserChallenge(name,date,time,challengesTypes.get(type),port,makerAddress));
       
       return true;
    }
    
    synchronized public boolean accept_challenge(String name,InetAddress acceptedAddress,int port)
    {
        if(!challenges.containsKey(name)) return false;
       
        challenges.get(name).setAcceptedInfo(acceptedAddress, port);
       
        
        return true;
    }
    
    public static void loadChallenges(HashMap<String,Challenge> challenges) {
        
        File folder = new File("Challenges");
        
        for(File f: folder.listFiles()){
            if(f.getPath().endsWith(".ch")){
                try {
                    System.out.println(f.getPath());
                    Challenge ch = new Challenge(f.getPath());
                    challenges.put(ch.getName(), ch);
                    System.out.println("Challenge "+ch.getName()+" Carregado");
                } catch (IOException ex) {
                   System.out.println("Ocurreu um erro ao carregar desafio");
                }
            }
        }
    }
}
