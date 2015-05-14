/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLientGUI;

import Client.PDU_Builder;
import Client.User;
import Common.PDU;
import Common.Question;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

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
    int label;
    User user;
    Lobby.buttonBlocktrigger b;
    
    
    public GameThread(DatagramSocket socket, String name,String date, String time,int n_questions,int label,Lobby.buttonBlocktrigger b,User u){
        this.socket = socket;
        this.name = name;
        this.date = date;
        this.time = time;
        this.n_questions =n_questions;
        questions = new Question[n_questions];
        this.label = label;
        this.b = b;
        this.user = u;
        accPoints = 0;
    }
    
    
    public void run(){
        boolean gametime = true;
        try {
            while(gametime){
                System.out.println("gametime");
                
                if(isGameTime()){
                    b.blockButtons();
                    System.out.println("//////->"+n_questions);
                    
                    LoadChallengeProgressBar.createAndShowGUI();
                    
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
                          int part = 0;
                         while(hasnextpart==1){

                            packet = new DatagramPacket(new byte[50000], 50000);
                            socket.receive(packet);
                            PDU imagePart =PDU.fromBytes(packet.getData());
                            data = imagePart.getData();
                            part = Integer.parseInt(new String(data[17]));
                            image[part] = data[16];

                            hasnextpart = imagePart.getHashNext();
                         }
                         //confirma packets
                         boolean confirmed = false;
                         while(!confirmed){
                                ArrayList<Integer> missingParts = PDU.check_state(image,part);
                                if(missingParts==null){
                                    //confirma
                                    PDU confirmPDU = PDU_Builder.OK_PDU(label);
                                    byte[] datasend = PDU.toBytes(confirmPDU);
                                    packet = new DatagramPacket(datasend, datasend.length);
                                    socket.send(packet);
                                    confirmed = true;
                                }
                                else{
                                    //ou pede para transmitir
                                    for(int p : missingParts){
                                        //request
                                        PDU retransmitPDU = PDU_Builder.RETRANSMIT(label,this.name,i, p);
                                        byte[] datasend = PDU.toBytes(retransmitPDU);
                                        packet = new DatagramPacket(datasend, datasend.length);
                                        socket.send(packet);
                                        //receive
                                         packet = new DatagramPacket(new byte[50000], 50000);
                                         socket.receive(packet);
                                         PDU imagePart =PDU.fromBytes(packet.getData());
                                         data = imagePart.getData();
                                         part = Integer.parseInt(new String(data[17]));
                                         image[part] = data[16];
                                    }
                                }
                         }
                         
                         hasnextpart = 1;
                         //musica
                         byte[][] music = new byte[100][];
                         while(hasnextpart==1){

                            packet = new DatagramPacket(new byte[50000], 50000);
                            socket.receive(packet);
                            PDU musicPart =PDU.fromBytes(packet.getData());
                            data = musicPart.getData();
                            part = Integer.parseInt(new String(data[17]));
                            music[part] = data[18];

                            hasnextpart = musicPart.getHashNext();
                         }
                         
                         confirmed = false;
                         while(!confirmed){
                                ArrayList<Integer> missingParts = PDU.check_state(music,part);
                                if(missingParts==null){
                                    //confirma
                                    PDU confirmPDU = PDU_Builder.OK_PDU(label);
                                    byte[] datasend = PDU.toBytes(confirmPDU);
                                    packet = new DatagramPacket(datasend, datasend.length);
                                    socket.send(packet);
                                    confirmed = true;
                                }
                                else{
                                    //ou pede para transmitir
                                    for(int p : missingParts){
                                        //request
                                        PDU retransmitPDU = PDU_Builder.RETRANSMIT(label,this.name,i, p);
                                        byte[] datasend = PDU.toBytes(retransmitPDU);
                                        packet = new DatagramPacket(datasend, datasend.length);
                                        socket.send(packet);
                                        //receive
                                         packet = new DatagramPacket(new byte[50000], 50000);
                                         socket.receive(packet);
                                         PDU musicPart =PDU.fromBytes(packet.getData());
                                         data = musicPart.getData();
                                         part = Integer.parseInt(new String(data[17]));
                                         music[part] = data[18];
                                    }
                                }
                         }

                         Question q  = new Question(i, qt, answers,0, music, image);
                         questions[i] = q;
                         new QuestionGUI(q,this,i).setVisible(true);
                         while(answer==0){
                             sleep(1000);
                         }
                         
                         if(answer==-1){
                             //nao respondeu
                             System.out.println("nao respondeu de todo");
                         }
                         if(answer == -2){
                             //Comando Quit na janela e por isso tem de ser enviado para
                             // o servidor para terminar o desafio
                             
                             PDU quit_PDU = PDU_Builder.QUIT_PDU(label);
                             byte[] dataTosend =  PDU.toBytes(quit_PDU);
                             packet = new DatagramPacket(dataTosend,dataTosend.length);
                             socket.send(packet);
                             System.out.println("enviou pedido Quit");
                             break;
                             
                             
                         }
                        // respondeu xD
                         System.out.println("respondeu "+answer);
                         PDU rightanswer = PDU_Builder.ANSWER(label, answer, name, i);
                         byte[] dataTosend =  PDU.toBytes(rightanswer);
                         packet = new DatagramPacket(dataTosend,dataTosend.length);
                         socket.send(packet);
                          System.out.println("enviou reposta");
                         //aguardar pela resposta -- vai dar pontos.

                         packet = new DatagramPacket(new byte[1024], 1024);
                         socket.receive(packet);
                         rightanswer  = PDU.fromBytes(packet.getData());
                         int isrightAnswer = Integer.parseInt(new String(rightanswer.getData()[14]));
                         int points = -1;
                         System.out.println("is"+answer+" a resposta correta?" + isrightAnswer);
                         if(isrightAnswer==1){
                           points = 2;
                           System.out.print("Correcto!\n");
                         }

                         this.user.points+=points;
                         accPoints+=points;
                         //pontos incrementados, proxima questao!
                         
                         
                         answer = 0;
                         answertime=0;
                    }
                    gametime = false;
                    System.out.println("desafio terminado");
                    new ErrorWindow("Jogo Terminado","Pontos Obtidos :"+accPoints+"\nTotal Pontos: "+user.points, "message", new JFrame()).wshow();
                    b.enableButtons();
                }
                else{
                    sleep(5000);
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
        
        return (cal.getTimeInMillis()-cal2.getTimeInMillis() <= 10000);
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
