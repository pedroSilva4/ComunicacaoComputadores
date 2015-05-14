/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import Common.PDU;
import Server.ChallengesInfo;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Pedro
 */
public class REPLY_Builder {
    
    /*Indices nao usados: 3,6,8,9,19*/
    
    static public PDU REPLY_OK(int label){
        int size = 0;
        byte data[][] = new byte[22][];
        data[0] = String.valueOf(0).getBytes();
        size += data[0].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_NAME(int label, String name,int points){
        int size = 0;
        byte data[][] = new byte[22][];
        data[1] = name.getBytes();
        data[20] = String.valueOf(points).getBytes();
        size += data[1].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_NICKNAME(int label, String nickname){
        int size = 0;
        byte data[][] = new byte[22][];
        data[2] = nickname.getBytes();
        size += data[2].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_DATE(int label, int date){
        int size = 0;
        byte data[][] = new byte[22][];
        data[4] = String.valueOf(date).getBytes();
        size += data[4].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_TIME(int label, int time){
        int size = 0;
        byte data[][] = new byte[22][];
        data[5] = String.valueOf(time).getBytes();
        size += data[5].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_CHALLENGE(int label, String name,String date, String time,int n_questions){
        int size = 0;
        byte data[][] = new byte[22][];
        data[7] = name.getBytes();
        size += data[7].length;
        data[4] = date.getBytes();
        size += data[4].length;
        data[5] = time.getBytes();
        size += data[5].length;
        data[10] =String.valueOf(n_questions).getBytes();
        size += data[5].length;
        return new PDU(0.0f, 0, 0,label, 4, size, data, 0);
    }
    
    static public PDU REPLY_NUNBERQESTION(int label, int number){
        int size = 0;
        byte data[][] = new byte[22][];
        data[10] = String.valueOf(number).getBytes();
        size += data[10].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_QUESTION(int label, String question,int question_n,String[] answers){
        int size = 0;
        byte data[][] = new byte[22][];
        data[10] = String.valueOf(question_n).getBytes();
         size += data[10].length;
        data[11] = question.getBytes();
        size += data[11].length;
        String answers_s = answers[0];
        for(int i = 1;i<answers.length;i++){
            answers_s+=";"+answers[i];
        }
        data[12] =answers_s.getBytes();
        size+=data[12].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
   
    static public PDU REPLY_NUMBERANSWER(int label, int number){
        int size = 0;
        byte data[][] = new byte[22][];
        data[12] = String.valueOf(number).getBytes();
        size += data[12].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_ANSWER(int label, String answer){
        int size = 0;
        byte data[][] = new byte[22][];
        data[13] = answer.getBytes();
        size += data[13].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_ISRIGHTANWSER(int label, int isright){
        int size = 0;
        byte data[][] = new byte[22][];
        data[14] = String.valueOf(isright).getBytes();
        size += data[14].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_POINTS(int label, int points){
        int size = 0;
        byte data[][] = new byte[22][];
        data[15] = String.valueOf(points).getBytes();
        size += data[15].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_IMAGE(int label,String name,int question, int part,byte[] image, int hasnext){
        int size = 0;
        byte data[][] = new byte[22][];
        data[10] = String.valueOf(question).getBytes();
        size += data[10].length;
        data[16] = image;
        size += data[16].length;
        data[17] = String.valueOf(part).getBytes();
        size+=data[17].length;
        return new PDU(0.0f, 0, 0,label, 3, size, data, hasnext);
    }
    
    static public PDU REPLY_BLOCKNUMBER(int label, int number){
        int size = 0;
        byte data[][] = new byte[22][];
        data[17] = String.valueOf(number).getBytes();
        size += data[17].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_AUDIO(int label,String name,int question,int part, byte[] audio, int hasnext){
        int size = 0;
        byte data[][] = new byte[22][];
        data[10] = String.valueOf(question).getBytes();
        size += data[10].length;
        data[17] = String.valueOf(part).getBytes();
        size+=data[17].length;
        data[18] = audio;
        size += data[18].length;
        return new PDU(0.0f, 0, 0,label, 3, size, data, hasnext);
    }
    
    static public PDU REPLY_SCORE(int label, int score){
        int size = 0;
        byte data[][] = new byte[22][];
        data[20] = String.valueOf(score).getBytes();
        size += data[20].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_ERRO(int label, String description){
        int size = 0;
        byte data[][] = new byte[22][];
        data[21] = description.getBytes();
        size += data[21].length;
        return new PDU(0.0f, 0, 0,label, 1, size, data, 0);
    }
    
    static public PDU REPLY_LISTCHALLENGE(int label,int hasnext,ChallengesInfo info){
        int size = 0;
        
        byte[][] data  = info.getListChallenges();
        if(data!=null){
       
        size = data.length;
        }
        System.out.println("sup ");
        return new PDU(0.0f, 0, 0,label, 1, size, data, hasnext);
    }
       
}
