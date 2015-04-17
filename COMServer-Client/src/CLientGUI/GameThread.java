/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLientGUI;

import Common.ChallengeType;
import Common.PDU;
import Common.Question;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class GameThread extends Thread implements Observer{
    DatagramSocket socket;
    String name,date,time;
    int accPoints;
    int n_questions;
    Question[] questions;
    int answer = 0;
    private int answertime = 0;
  
    
    
    public GameThread(DatagramSocket socket, String name,String date, String time,int n_questions){
        this.socket = socket;
        this.name = name;
        this.date = date;
        this.time = time;
        this.n_questions =n_questions;
        questions = new Question[n_questions];
    }
    
    
    public void run(){
        boolean gametime = true;
        try {
            while(gametime){
                System.out.println("gametime");
            if(isGameTime()){
                for(int i=0;i<n_questions;i++){
                     DatagramPacket packet = new DatagramPacket(new byte[5000], 5000);
                     socket.receive(packet);
                     
                     
                     PDU question = PDU.fromBytes(packet.getData());
                     
                     byte[][] data = question.getData();
                     String qt = new String(data[11]);
                     System.out.println(qt);
                     String[] answers = new String(data[12]).split(";");
                     System.out.println(new String(data[12]));
                     
                     //imagem
                     byte[][] image =new byte[50][];//valores seguros ? 
                      int hasnextpart = 1;
                     while(hasnextpart==1){
                         
                        packet = new DatagramPacket(new byte[50000], 50000);
                        socket.receive(packet);
                        PDU imagePart =PDU.fromBytes(packet.getData());
                        data = imagePart.getData();
                        int part = Integer.parseInt(new String(data[17]));
                        image[part] = data[16];
                        
                        hasnextpart = imagePart.getHashNext();
                     }
                     hasnextpart = 1;
                     //musica
                     byte[][] music = new byte[100][];
                     while(hasnextpart==1){
                         
                        packet = new DatagramPacket(new byte[50000], 50000);
                        socket.receive(packet);
                        PDU musicPart =PDU.fromBytes(packet.getData());
                        data = musicPart.getData();
                        int part = Integer.parseInt(new String(data[17]));
                        music[part] = data[18];
                        
                        hasnextpart = musicPart.getHashNext();
                     }
                    
                     Question q  = new Question(i, qt, answers,0, music, image);
                     questions[i] = q;
                     new QuestionGUI(q,this,i).setVisible(true);
                     while(answer==0);
                     if(answer==-1){
                         //nao respondeu
                     }
                     else{
                         // respondeu xD
                     }
                     answer = 0;
                     answertime=0;
                }
            }
            else{
                sleep(30000);
            }
         }
        } catch (ParseException | InterruptedException | IOException ex) {
            Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
      private boolean isGameTime() throws ParseException {
        Calendar cal = new GregorianCalendar();
                DateFormat datef = new SimpleDateFormat("yyMMddHHmmss");
                String finaltime = this.date+this.time;
                cal.setTime(datef.parse(finaltime));
        Calendar cal2 = Calendar.getInstance();
        
        return (cal.getTimeInMillis()-cal2.getTimeInMillis() <= 30000);
    }

    @Override
    public void update(Observable o, Object arg) {
      String[] parts = ((String)arg).split(";");
      int question = Integer.valueOf(parts[0]);
      this.answer = Integer.valueOf(parts[1]);
      int time   = Integer.valueOf(parts[2]);
                      
      questions[question].setAnswer(answer);
      this.answertime = time;
      
    }
      
      
}
