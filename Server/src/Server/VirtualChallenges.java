/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ChallengeType;
import Common.User;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pedro
 */
public class VirtualChallenges {
    
    class VirtualChallenge{
       
        int id;
        String ipServidor;
        String name;
        String username;
        int points;
        boolean makerhasfinished = false;
        ChallengeType chType;
        
        int playersfinished = 0;
        Map<Integer,User> players;
        
        boolean isCanceled = false;
        
        public VirtualChallenge(String serv,String name){
            this.ipServidor = serv;
            this.name = name;
            this.players = new HashMap<>();   
            
        }
        
        public VirtualChallenge(String serv,String name,ChallengeType ch){
            this.ipServidor = serv;
            this.name = name;
            this.chType = ch;
            this.players = new HashMap<>();   
            
        }
    }
   
    Map<String,VirtualChallenge> challenges;
    
    public VirtualChallenges(){
        challenges = new HashMap<>();
    }
    
    public synchronized void add(String ipServidor,String name){
        VirtualChallenge put = challenges.put(name, new VirtualChallenge(ipServidor, name));
    }
    
    public synchronized void addChallengeData(ChallengeType t,String name){
        this.challenges.get(name).chType=t;
    }
    
    public synchronized void accept(String name,User user){
        this.challenges.get(name).players.put(user.port, user);
        
    }
    
    public synchronized void cancel(String name){
        this.challenges.get(name).isCanceled = true;
    }
    
    
    public synchronized boolean isAgo(String name){
        VirtualChallenge ch = this.challenges.get(name);
        if(ch.isCanceled || ch.players.size()<1)
            return false;
        
        return true;
    }
    
    public synchronized void finish(String name){
        this.challenges.get(name).playersfinished++;
        
    }
    
    
    public synchronized boolean isEnded(String name){
        VirtualChallenge ch = this.challenges.get(name);
        if(ch.makerhasfinished && (ch.playersfinished >= ch.players.size()))
            return true;
        
        return false;       
    }
}
