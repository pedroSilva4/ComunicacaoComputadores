/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.REPLY_Builder;
import Common.UserChallenge;
import Common.ChallengeType;
import Common.PDU;
import Common.Question;
import Common.User;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro
 */
public class ClientHandler extends Thread{
    int port;
    DatagramSocket socket;
    int packetPort;
    InetAddress packetAdress;
    int currentLabel;
    Clients clients;
    ChallengesInfo challengeInfo;    
    String challengeMorA = null;
    VirtualChallenges virtualInfo;
    HashMap<Integer,ServerComunication> coms;
   
    
    public ClientHandler(int firstLabel,int port,DatagramPacket packet,Clients clients, ChallengesInfo challengeInfo,
                                                VirtualChallenges virtualinfo,HashMap<Integer,ServerComunication> coms) throws SocketException
    {
        this.port = port;
        socket = new DatagramSocket(port);
        packetPort = packet.getPort();
        packetAdress = packet.getAddress();
        this.currentLabel = firstLabel;
        this.clients = clients;
        this.challengeInfo=challengeInfo;
        this.virtualInfo = virtualinfo;
        this.coms = coms;
    }
    
    public void run(){
       byte[] data = PDU.toBytes(REPLY_Builder.REPLY_OK(currentLabel));
       DatagramPacket packet = new DatagramPacket(data, data.length, packetAdress, packetPort);
       
        try {
            
            socket.send(packet);
        }catch(IOException e){
             Logger.getLogger("ClientHandler", e.getMessage());
            socket.close();
            return;
        }
            boolean logout = false;
            //currentLabel incrementa toma sempre o valor do PDU recebido pelo servidor, 
            //pois o servidor so faz reply, e os reply tens a mesma label que as mensagens dos clientes
            DatagramPacket request,reply;
            socket.connect(packetAdress, packetPort);
            boolean b;
        
            while(!logout){
                 b = hasChallengeNow();
              if(!b){
                  //if(challengeMorA==null){
                    request = new DatagramPacket(new byte[1024],1024,packetAdress,packetPort); 
                    
                    try{
                        if(challengeMorA!=null)
                             socket.setSoTimeout(5000);
                        else 
                             socket.setSoTimeout(0);
                        
                        socket.receive( request );

                        PDU requestPDU = PDU.fromBytes(request.getData());
                          System.out.println("rquest tipo -> " + requestPDU.getType());
                        PDU replyPDU  = parsePDU(requestPDU);
                        if(replyPDU!= null){
                          if(requestPDU.getType()==4){
                            if(challengeMorA!=null) 
                             {
                                 this.challengeInfo.getUserChallenge(challengeMorA).userLoggedOut(port);
                                 challengeMorA = null;
                                 System.out.println("Challenge Cancelled for me!");
                                 try {                    
                                       socket.setSoTimeout(0);

                                     } catch (SocketException ex) {
                                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                                     }

                             }
                              
                          }
                          byte[] replyData = PDU.toBytes(replyPDU);
                          reply = new DatagramPacket(replyData, replyData.length, packetAdress, packetPort);
                          socket.send(reply);
                          
                         
                          
                        }
                }catch(SocketException ex){
                   System.out.println("ClientHandler -> "+ex.getMessage());
                 }catch (IOException ex) {                            
                         System.out.println("ClientHandler -> "+ex.getMessage());
                     }                            
              }
              else{
                  
                  System.out.println("GameTime");
                  ChallengeType ch  =this.challengeInfo.getUserChallenge(challengeMorA).getChallengeType();
                  Map<Integer,Question> questions = ch.getQuestions();
                  
                  boolean quitted =false;
                  try {
                      if(this.challengeInfo.getUserChallenge(challengeMorA).isCanceled() || !this.challengeInfo.getUserChallenge(challengeMorA).isGameReady())
                      {
                          PDU erro = REPLY_Builder.REPLY_ERRO(0,"Erro : Challenge Cancelado!");
                          data = PDU.toBytes(erro);
                          packet = new DatagramPacket(data, data.length);
                          socket.send(packet);
                          quitted = true;
                          
                      }
                      else{
                      for(int i: questions.keySet()){
                  
                          PDU question = REPLY_Builder.REPLY_QUESTION(0,questions.get(i).question, i, questions.get(i).answers);
                          data = PDU.toBytes(question);
                          packet = new DatagramPacket(data, data.length);
                          socket.send(packet);
                          System.out.println("questao enviada");
                          sleep(50);
                          
                          byte[][] media = questions.get(i).getImage();
                          int hasnext = 1;
                          for(int j = 0; j<media.length;j++){
                              
                              if(j==media.length-1) hasnext = 0;
                              
                              PDU imagem = REPLY_Builder.REPLY_IMAGE(0,challengeMorA,i, j,media[j],hasnext);
                              data = PDU.toBytes(imagem);
                              packet = new DatagramPacket(data, data.length);
                              socket.send(packet);
                              sleep(50);
                          }
                          //envia confirmação ou retransmissao?
                          boolean confirmed = false;
                          while(!confirmed){
                              packet = new DatagramPacket(new byte[1024], 1024);
                              socket.setSoTimeout(2500);
                              socket.receive(packet);
                              PDU qConfirm = PDU.fromBytes(packet.getData());
                              if(qConfirm.getType()==0){
                                    confirmed = true;
                              }
                              else{
                                  //retransmit
                                  int partToTransmit = Integer.parseInt(new String(qConfirm.getData()[2]));
                                   PDU imagem = REPLY_Builder.REPLY_IMAGE(0,challengeMorA,i, partToTransmit,media[partToTransmit],0);
                                   data = PDU.toBytes(imagem);
                                   packet = new DatagramPacket(data, data.length);
                                   socket.send(packet);
                              }
                          }
                          //imagem enviada
                          hasnext =1;
                          media = questions.get(i).getMusic();
                          for(int j = 0; j<media.length;j++){
                              if(j==media.length-1) hasnext = 0;
                              
                              PDU musica = REPLY_Builder.REPLY_AUDIO(0,challengeMorA,i,j,media[j],hasnext);
                              data = PDU.toBytes(musica);
                              packet = new DatagramPacket(data, data.length);
                              socket.send(packet);
                              sleep(50);
                          }
                          //envia confirmação ou retransmissao?
                          confirmed = false;
                          while(!confirmed){
                              packet = new DatagramPacket(new byte[1024], 1024);
                              socket.setSoTimeout(2500);
                              socket.receive(packet);
                              PDU qConfirm = PDU.fromBytes(packet.getData());
                              if(qConfirm.getType()==0){
                                    confirmed = true;
                              }
                              else{
                                  //retransmit
                                  int partToTransmit = Integer.parseInt(new String(qConfirm.getData()[2]));
                                   PDU musica = REPLY_Builder.REPLY_AUDIO(0,challengeMorA,i, partToTransmit,media[partToTransmit],0);
                                   data = PDU.toBytes(musica);
                                   packet = new DatagramPacket(data, data.length);
                                   socket.send(packet);
                              }
                          }
                          //musica enviada
                          packet = new DatagramPacket(new byte[1024],1024);
                          socket.setSoTimeout(65000);
                          socket.receive(packet);
                          
                          PDU answer = PDU.fromBytes(packet.getData());
                          
                          if(answer.getType()==5){
                              System.out.println("jogador Desistiu!");
                              quitted= true;
                              this.challengeInfo.getUserChallenge(challengeMorA).userQuited(port);
                              break;
                          }
                          if(answer.getType()==11){
                               int answerOpt = Integer.parseInt(new String(answer.getData()[0]));
                               int isright = 0;
                              //verificar se resposta correta e incrementar pontos.
                               if((questions.get(i).getRightAwnser())==answerOpt){
                                   this.clients.addPoints(clients.loggedIn.get(port), 2);
                                   this.challengeInfo.getUserChallenge(challengeMorA).incPoints(port, 2);
                                   isright = 1;
                                   
                               }else{
                                    this.clients.addPoints(clients.loggedIn.get(port), -1);
                                    this.challengeInfo.getUserChallenge(challengeMorA).incPoints(port, -1);
                               }
                              //responde ao pedido
                             
                               data = PDU.toBytes( REPLY_Builder.REPLY_ISRIGHTANWSER(0,isright));
                               packet = new DatagramPacket(data, data.length);
                               socket.send(packet);
                               
                              //depois passa para a proxima pergunta.
                            }
                        } 
                      }
                  }catch (IOException | InterruptedException ex) {
                          System.out.println("ClientHandler -> "+ex.getMessage());
                          this.challengeInfo.challenges.get(challengeMorA).userLoggedOut(port);
                          
                      }
                  //recebe pedido de end se nao saiu
                  if(!quitted){
                      
                      try {
                          packet = new DatagramPacket(new byte[1024], 1024);
                          socket.setSoTimeout(2500);
                          socket.receive(packet);
                          PDU endRequest = PDU.fromBytes(packet.getData());
                          
                          challengeInfo.getUserChallenge(challengeMorA).finish();
                          
                          UserChallenge uch = challengeInfo.getUserChallenge(challengeMorA);
                        
                                System.out.print("Waiting");
                                while(!uch.allfinished()){
                                   sleep(100);
                                   System.out.print(".");
                                }
                          
                          
                          Collection<User> usersRankingByport = uch.getRanking();
                          
                          String scores = "";
                          
                          for(User u : usersRankingByport){
                              scores+=this.clients.loggedIn.get(u.port)+" : "+u.points+"\n";
                          }
                          
                          PDU endReply  = REPLY_Builder.REPLY_SCOREALL(endRequest.getLabel(),scores );
                          data = PDU.toBytes(endReply);
                          packet = new DatagramPacket(data,data.length);
                          socket.send(packet);
                           socket.setSoTimeout(0);
                           
                          System.out.println(this.clients.loggedIn.get(port)+" : "+this.challengeInfo.getUserChallenge(challengeMorA).getPoints(port));
                      } catch (IOException ex) {
                          Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                      } catch (InterruptedException ex) {
                          Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                      }
                  }
                  //
                  challengeMorA = null;
                 
              }
                
            }
        
    }
    
    public PDU parsePDU(PDU requestPDU){
       
       
            switch(requestPDU.getType()){
                case 2:{//register
                     byte[][] fields= requestPDU.getData();
                    String name  = new String(fields[0]);
                    String nick = new String(fields[1]);
                    byte[] password = fields[2];

                    boolean ok = clients.registerClient(name,nick,password);

                    if(!ok)//return erro
                        return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "ja existe alguem com esse nickname");
                    //return register reply
                    return REPLY_Builder.REPLY_OK(requestPDU.getLabel());
                }
                case 3:{//login
                     byte[][] fields= requestPDU.getData();
                    String nick = new String(fields[0]);
                    byte[] password = fields[1];
                    
                    int ok = clients.login(port,nick,password);
                    
                    if(ok== -1)
                       return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(),"password errada");
                    if(ok== -2)
                       return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(),"nick nao existe");
                        
                    //Guardar Username;
                    
                    return REPLY_Builder.REPLY_NAME(requestPDU.getLabel(), clients.getName(nick),clients.getPoints(nick));
                }   
                case 4:{//logout
                    int ok = clients.logout(port);
                    
                    if(ok == -1)
                        return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "não foi feito o login desse utilizador");
                    
                    return REPLY_Builder.REPLY_OK(requestPDU.getLabel());
                }
                case 7:{
                        //
                    
                    return REPLY_Builder.REPLY_LISTCHALLENGE(requestPDU.getLabel(),0,challengeInfo);
                }
                case 8:{/*make challenges - (nome/data prevista/hora prevista) - 
                                       lançar um desafio, se não conter data nem hora, por defeito é começa daqui a 5 minutos.*/
                                       //this.challengeInfo.make_challenge(null, null, null, packetAdress, port)
                    byte[][] fields= requestPDU.getData();
                    
                    String name = new String(fields[0]);
                    
                    boolean b  =false;
                    SimpleDateFormat datef = new SimpleDateFormat("yyMMdd");
                    SimpleDateFormat timef = new SimpleDateFormat("HHmmss");
                    SimpleDateFormat totalf  = new SimpleDateFormat("yyMMddHHmmss");
                    
                    String date,time;
                     GregorianCalendar cal = new GregorianCalendar();
                                    cal.setTime(Date.from(Instant.now()));
                    if(fields[1] == null && fields[2]== null){

                        cal.add(Calendar.MINUTE, 1);

                        date = datef.format(cal.getTime());
                        time = timef.format(cal.getTime());

                    }else{
                        date = new String(fields[1]);
                        time = new String(fields[2]);
                        
                         GregorianCalendar cal2 = new GregorianCalendar();
                        try {
                            cal2.setTime(totalf.parse(date+time));
                            //verificar se minimo de 5 min
                            if(cal2.getTimeInMillis()< cal.getTimeInMillis()+300000)
                            {
                                cal.add(Calendar.MINUTE, 5);

                                 date = datef.format(cal.getTime());
                                 time = timef.format(cal.getTime());
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       
                    }
                    
                    b = this.challengeInfo.make_challenge(name,date,time, packetAdress, port);
                    if(b){
                      this.challengeMorA = name;
                      int n_questions = this.challengeInfo.getUserChallenge(name).getChType().n_questions;
                      
                      new InformAll(coms, INFO_Builder.INFO_REGCHALLENGE(requestPDU.getLabel(),
                                            this.challengeInfo.getUserChallenge(name)))
                              .start();
                      
                      
                      return REPLY_Builder.REPLY_CHALLENGE(requestPDU.getLabel(), name,date,time,n_questions);
                      
                    }else 
                      return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "JA existe um Challenge com esse NOme");
                }
                case 9:{//acept challenge - (nome do desafio) - nao pode aceitar desafios dele proprio
                     
                     byte[][] fields= requestPDU.getData();
                    
                     String name = new String(fields[0]);
                        
                     boolean b = this.challengeInfo.accept_challenge(name,port);
                     
                     
                     if(!b) return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "Desafio não existe, ou ja esta a ser jogado");
                     
                     this.challengeMorA=name;
                     return REPLY_Builder.REPLY_OK(requestPDU.getLabel());
                }
                case 10:{//delele challenge - (nome do desafio) - ou apaga o que é destinado, e o que fez.
                    
                    byte[][] fields = requestPDU.getData();
                    
                    String name = new String(fields[0]);
                    
                    if(challengeMorA!=null){
                        UserChallenge uch = this.challengeInfo.getUserChallenge(name);
                        if(uch!=null){
                            if(uch.maker()==port){
                                challengeMorA=null;
                                uch.cancelCh();
                                return REPLY_Builder.REPLY_OK(requestPDU.getLabel());
                            }
                        }
                    }
                   
                    return REPLY_Builder.REPLY_ERRO(requestPDU.getLabel(), "Nao pode remover o Challenge com nome "+ name);
                   
     
                }
                case 13:{//list ranking - (sem parametros) - 
                    
                    
                    return null;
                }
                
                
                
            }
       
        return null;
    }

    private boolean hasChallengeNow() {
        
        if(challengeMorA==null) return false;
        UserChallenge ch = this.challengeInfo.getUserChallenge(this.challengeMorA);
       
       if(ch!=null){
            try {
                Calendar cal = new GregorianCalendar();
                DateFormat datef = new SimpleDateFormat("yyMMddHHmmss");
                String finaltime = ch.getData()+ch.getTime();
                cal.setTime(datef.parse(finaltime));
                Calendar cal2 = Calendar.getInstance();
                if(cal.getTimeInMillis() - cal2.getTimeInMillis() < 1000){
                    return true;
                }
            } catch (ParseException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       
       return false;
    }

    
}
