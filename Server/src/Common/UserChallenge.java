/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;


/**
 *
 * @author Pedro
 */
public class UserChallenge {
    private final String name;
    private final String data;
    private final String time;
    private final ChallengeType challengeType;
    private final Map<Integer,User> usersPlaying = new HashMap<>();
    private int nUsers = 0; 
    private int usersfinished = 0;
    private final int maker;
    private boolean canceled  =false;
    private boolean isShared = false;
    private Map<String,Integer> sharedRanking;

    /**
     * @return the name
     */
    public synchronized boolean isShared(){
        return this.isShared;
    }
    
    public synchronized TreeSet<Map.Entry<String,Integer>> getSharedRanking(){
        
       TreeSet<Map.Entry<String,Integer>> t = new TreeSet<>(new MapEntryComparator());
      
       t.addAll(sharedRanking.entrySet());
       
       return t;
    }
    
    public synchronized void setShared(){
        this.sharedRanking = new HashMap<>();
        this.isShared = true;   
    }
    
    synchronized public String getName() {
        return name;
    }

    synchronized public String getTime(){
        return this.time;
    }
    
    synchronized public String getData(){
        return this.data;
    }
    
     synchronized public ChallengeType getChType(){
        return this.challengeType;
    }
    
    
    public UserChallenge(String name, String date, String time, ChallengeType get, int makerPort) {
       this.name = name;
       this.data =date;
       this.time = time;
       this.challengeType = get;
       User u = new User();
       u.port = makerPort;
       u.points=0;
       this.usersPlaying.put(makerPort,u);
       nUsers++;
       maker = makerPort;
    }

    synchronized public void setAcceptedInfo(int port) {
              User u = new User();
              u.port = port;
              u.points=0;
              this.usersPlaying.put(port,u);
              nUsers++;
    }
    
    synchronized public void userLoggedOut(int port){
       
               usersPlaying.remove(port);
               nUsers--;   
               this.notifyAll();
    }
     synchronized public void userQuited(int port){
       usersPlaying.remove(port);
       nUsers--;
       this.notifyAll();
    }
    synchronized public int maker(){
        return this.usersPlaying.get(maker).port;
    }
    
    synchronized public boolean isGameReady(){
        if(nUsers<=1) return false;
        
        usersfinished=0;
        return true;
    } 
    
    
    synchronized public void finishSared(String username, int points){
        this.sharedRanking.put(username, points);
        usersfinished++;
        this.notifyAll();
    }
    
    synchronized public void finish(){
        usersfinished++;
        this.notifyAll();
    }
    
    synchronized public void incPoints(int port,int points){
        usersPlaying.get(port).points+=points;
    }
    
    
    synchronized public ChallengeType getChallengeType() {
        return this.challengeType;
    }

    synchronized public String getPoints(int port) {
        return this.usersPlaying.get(port).points+""; //To change body of generated methods, choose Tools | Templates.
    }
    
    synchronized public boolean allfinished(){
        return (usersfinished>=nUsers);
    }
    
    synchronized public Collection<User> getRanking(){
        return this.usersPlaying.values();
    }

    synchronized public boolean checkDate() {
        try {
            Calendar cal = new GregorianCalendar();
            DateFormat datef = new SimpleDateFormat("yyMMddHHmmss");
            String finaltime = this.getData()+this.getTime();
            cal.setTime(datef.parse(finaltime));
            Calendar cal2 = Calendar.getInstance();
            if(cal.getTimeInMillis() - cal2.getTimeInMillis() > 0  && !this.isCanceled()){
                return true;
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(UserChallenge.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    synchronized public void cancelCh() {
       this.canceled = true;
    }
    
   synchronized public boolean isCanceled(){
        return this.canceled;
    }
   
   synchronized public int getNusers(){
       return this.nUsers;
   }
   
   synchronized  public void setNusers(int n){
        nUsers = n;
   }

    synchronized public void userQuittedShared() {
       nUsers--;
       this.notifyAll();
    }
    
    
   public static class MapEntryComparator implements Comparator<Map.Entry<String,Integer>>{

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            if(o1.getValue()> o2.getValue())
                return -1;
            else return 1;
        }
        
    }
}
