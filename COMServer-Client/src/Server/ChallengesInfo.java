/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ChallengeType;
import Common.UserChallenge;
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
    Map<String,ChallengeType> challengesTypes ;
     
    public ChallengesInfo(){
         challenges = new HashMap<>();
         challengesTypes = new HashMap<>();
         loadChallenges((HashMap<String, ChallengeType>) challengesTypes);
    }
    
    synchronized public boolean make_challenge(String name,String date,String time,InetAddress makerAddress,int port){
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
    
    public static void loadChallenges(HashMap<String,ChallengeType> challenges) {
        
        File folder = new File("Challenges");
        
        for(File f: folder.listFiles()){
            if(f.getPath().endsWith(".txt")){
                try {
                    System.out.println(f.getPath());
                    ChallengeType ch = new ChallengeType(f.getPath());
                    challenges.put(ch.getName(), ch);
                    System.out.println("Challenge "+ch.getName()+" Carregado");
                } catch (IOException ex) {
                   System.out.println("Ocurreu um erro ao carregar desafio");
                }
            }
        }
    }

    synchronized byte[][] getListChallenges() {
       if(this.challenges.keySet().isEmpty()) return null;
        
       byte[][] res = new byte[this.challenges.keySet().size()][]; 
       int i = 0;
       for(String ug: this.challenges.keySet())
       {
           res[i] = (ug+","+challenges.get(ug).data+","+challenges.get(ug).time+","+challenges.get(ug).challenge.n_questions).getBytes();
           i++;
       }
       return res;
    }

    synchronized UserChallenge getUserChallenge(String name) {
        return this.challenges.get(name);
    }
}
