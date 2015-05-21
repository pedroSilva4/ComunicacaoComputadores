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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Pedro
 */
public class UserChallenge {
    private final String name;
    public String data;
    private final String time;
    private final ChallengeType challengeType;
    private final Map<Integer,User> usersPlaying = new HashMap<>();
    private int nUsers = 0; 
    private int usersfinished = 0;
    private final int maker;
    private boolean canceled  =false;

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

    public boolean checkDate() {
        try {
            Calendar cal = new GregorianCalendar();
            DateFormat datef = new SimpleDateFormat("yyMMddHHmmss");
            String finaltime = this.getData()+this.getTime();
            cal.setTime(datef.parse(finaltime));
            Calendar cal2 = Calendar.getInstance();
            if(cal.getTimeInMillis() - cal2.getTimeInMillis() > 0 ){
                return true;
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(UserChallenge.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    public void cancelCh() {
       this.canceled = true;
    }
    
    public boolean isCanceled(){
        return this.canceled;
    }
}
