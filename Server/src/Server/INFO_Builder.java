/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.ChallengeType;
import Common.PDU;
import Common.UserChallenge;

/**
 *
 * @author fdr
 */
public class INFO_Builder {
    
    static public PDU INFO_REGCHALLENGE(int label, UserChallenge ch){
        /*
        private final String name;
    public String data;
    private final String time;
    private final ChallengeType challengeType;
    private final Map<Integer,User> usersPlaying = new HashMap<>();
    private int nUsers = 0; 
    private int usersfinished = 0;
    private final int maker;
    private boolean canceled  =false;
        */
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = ch.getName().getBytes();
        size += data[0].length;
        data[1] = ch.getData().getBytes();
        size+= data[1].length;
        data[2] = ch.getTime().getBytes();
        size+= data[2].length;
        data[3] = ChallengeType.toBytes(ch.getChallengeType());
        size+= data[3].length;
        data[4]= (ch.getNusers()+"").getBytes();
        size+= data[4].length;
        
        return new PDU(0.0f, 0, 1,label, 5, size, data, 0);
    }
    
    static public PDU INFO_ACCEPTCHALLENGE(int label, String nickname,String name){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = nickname.getBytes();
        size += data[0].length;
        data[1] = name.getBytes();
        size+= data[1].length;
        return new PDU(0.0f, 0, 2,label, 2, size, data, 0);
    }
    
    static public PDU INFO_FINISHCHALLENGE(int label, String name){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = name.getBytes();
        size += data[0].length;
        return new PDU(0.0f, 0, 3,label, 1, size, data, 0);
    }
    
    static public PDU INFO_DATE(int label, int date){
        int size = 0;
        byte data[][] = new byte[22][];
        data[4] = String.valueOf(date).getBytes();
        size += data[4].length;
        return new PDU(0.0f, 0, 4,label, 1, size, data, 0);
    }
    
    static public PDU INFO_TIME(int label, int time){
        int size = 0;
        byte data[][] = new byte[22][];
        data[5] = String.valueOf(time).getBytes();
        size += data[5].length;
        return new PDU(0.0f, 0, 5, label, 1, size, data, 0);
    }
    
    static public PDU INFO_NUNBERQESTION(int label, int number){
        int size = 0;
        byte data[][] = new byte[22][];
        data[6] = String.valueOf(number).getBytes();
        size += data[6].length;
        return new PDU(0.0f, 0, 6,label, 1, size, data, 0);
    }
    
    static public PDU INFO_QUESTION(int label, String question, int question_n, String[] answers){
        int size = 0;
        byte data[][] = new byte[22][];
        data[7] = String.valueOf(question_n).getBytes();
        size += data[7].length;
        data[8] = question.getBytes();
        size += data[8].length;
        String answers_s = answers[0];
        for(int i = 1;i<answers.length;i++){
            answers_s+=";"+answers[i];
        }
        data[9] =answers_s.getBytes();
        size+=data[9].length;
        return new PDU(0.0f, 0, 7,label, 3, size, data, 0);
    }
    
    static public PDU INFO_NUMBERANWSER(int label, int number){
        int size = 0;
        byte data[][] = new byte[22][];
        data[10] = String.valueOf(number).getBytes();
        size += data[10].length;
        return new PDU(0.0f, 0, 8, label, 1, size, data, 0);
    }
    
    static public PDU INFO_ISRIGHTANWSER(int label, int isright){
        int size = 0;
        byte data[][] = new byte[22][];
        data[11] = String.valueOf(isright).getBytes();
        size += data[11].length;
        return new PDU(0.0f, 0, 9,label, 1, size, data, 0);
    }
    
    static public PDU INFO_IMAGE(int label, String name, int question, int part,byte[] image, int hasnext){
        int size = 0;
        byte data[][] = new byte[22][];
        data[6] = String.valueOf(question).getBytes();
        size += data[6].length;
        data[12] = image;
        size += data[12].length;
        data[13] = String.valueOf(part).getBytes();
        size+=data[13].length;
        return new PDU(0.0f, 0, 10,label, 3, size, data, hasnext);
    }
    
    static public PDU INFO_AUDIO(int label, String name, int question, int part, byte[] audio, int hasnext){
        int size = 0;
        byte data[][] = new byte[22][];
        data[6] = String.valueOf(question).getBytes();
        size += data[6].length;
        data[13] = String.valueOf(part).getBytes();
        size+=data[13].length;
        data[14] = audio;
        size += data[14].length;
        return new PDU(0.0f, 0, 11,label, 3, size, data, hasnext);
    }
    
    static public PDU INFO_SCORE(int label, int score){
        int size = 0;
        byte data[][] = new byte[22][];
        data[15] = String.valueOf(score).getBytes();
        size += data[15].length;
        return new PDU(0.0f, 0, 12,label, 1, size, data, 0);
    }
    
    static public PDU INFO_SERVERIP(int label, int ip){
        int size = 0;
        byte data[][] = new byte[22][];
        data[16] = String.valueOf(ip).getBytes();
        size += data[16].length;
        return new PDU(0.0f, 0, 14, label, 1, size, data, 0);
    }
    
    static public PDU INFO_SERVERPORT(int label, int port){
        int size = 0;
        byte data[][] = new byte[22][];
        data[17] = String.valueOf(port).getBytes();
        size += data[17].length;
        return new PDU(0.0f, 0, 15, label, 1, size, data, 0);
    }
}
