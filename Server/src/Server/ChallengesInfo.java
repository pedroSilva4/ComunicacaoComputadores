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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
       
       challenges.put(name, new UserChallenge(name,date,time,challengesTypes.get(type),port));
       
       return true;
    }
    
    synchronized public boolean accept_challenge(String name,int port)
    {
        if(!challenges.containsKey(name)) return false;
       
        challenges.get(name).setAcceptedInfo(port);
        
        return true;
    }
    
    public static void loadChallenges(HashMap<String,ChallengeType> challenges) {
        
        File folder = new File("Challenges");
       if(folder.exists()){
            for(File f: folder.listFiles()){
                if(f.getPath().endsWith(".txt")){
                    try {
                        System.out.println(f.getPath());
                        ChallengeType ch = new ChallengeType(f.getPath());
                        System.err.println(f.getPath());
                        challenges.put(ch.getName(), ch);
                        System.out.println("Challenge "+ch.getName()+" Carregado");
                    } catch (IOException ex) {
                       System.err.println("Ocurreu um erro ao carregar desafio");
                    }
                }
            }
        }else{
            System.err.println("Nao existem ficheiros de desafio");
        }
    }

    public synchronized byte[][] getListChallenges() {
       if(this.challenges.keySet().isEmpty()) return null;
        
       byte[][] res = new byte[this.challenges.keySet().size()][]; 
       int i = 0;
       for(String ug: this.challenges.keySet())
       {
           
           if(this.challenges.get(ug).checkDate()){
                res[i] = (ug+","+challenges.get(ug).getData()+","+challenges.get(ug).getTime()+","+challenges.get(ug).getChType().n_questions).getBytes();
                i++;
           }
       }
       return res;
    }

    public synchronized UserChallenge getUserChallenge(String name) {
        return this.challenges.get(name);
    }

    public synchronized void putUserChallenge(UserChallenge us) {
        this.challenges.put(us.getName(), us);
        
    }
    
    
    public synchronized List<UserChallenge> getActiveChallenges(){
        
        List<UserChallenge> list = new ArrayList<>();
        for(UserChallenge ch : this.challenges.values()){
            if(ch.checkDate()){
                list.add(ch);
            }
        }
        
        return list;
    }
}
