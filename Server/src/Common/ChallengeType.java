/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Pedro
 */
public class ChallengeType implements Serializable{

    public String getName() {
        return this.name;
    }
   
      
    
    public String name;
    public int n_questions;
    public Map<Integer,Question> questions;
    
    
    public ChallengeType(String filepath) throws IOException{
       String[] pathparts = filepath.split("\\"+File.separator);
       name = pathparts[pathparts.length-1].split("\\.")[0];
       questions = new HashMap<>();
       InputStreamReader reader =  new FileReader(filepath);
       BufferedReader buffer = new BufferedReader(reader);
       String musicsDir = buffer.readLine().split("=")[1];
       String imagesDir = buffer.readLine().split("=")[1];
       this.n_questions = Integer.valueOf(buffer.readLine().split("=")[1]);
       int nq = 1;
       
       for(int i = 3; i< n_questions+3;i++)
       {
            String[] parts = buffer.readLine().split("(\",\")|(\",)|(,\")");
            
            
             String musicPath = "Challenges"+File.separator+musicsDir+File.separator+parts[0].split(",")[0];
             String imagePath = "Challenges"+File.separator+imagesDir+File.separator+parts[0].split(",")[1];
             byte[][] music = divideArray(Files.readAllBytes(Paths.get(musicPath)),48000);
             byte[][] image = divideArray(Files.readAllBytes(Paths.get(imagePath)),48000);
             String[] answers = new String[3];
             answers[0] = parts[2];
             answers[1] = parts[3];
             answers[2] = parts[4];
              System.out.println(parts[5]);
             Question q = new Question(nq,parts[1],answers,Integer.parseInt(parts[5]),music,image);
             this.questions.put(nq,q);
            
            /*System.out.println(musicPath);
            System.out.println(imagePath);
            System.out.println(parts[1]);
            System.out.println(parts[2]);
            System.out.println(parts[3]);
            System.out.println(parts[4]);
            System.out.println(parts[5]);
            
           */
            //new ByteArrayInputStream();
            nq++;
       }
       reader.close();
       buffer.close();
        /*
        byte[] finalarr = Files.readAllBytes(Paths.get(musicPath));
        ByteArrayInputStream input;
        BufferedInputStream in ;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(finalarr);
        byte[] arr = out.toByteArray();
        System.out.print((float)finalarr.length/48000.f);
        input = new ByteArrayInputStream(arr);
        in = new BufferedInputStream(input);
        Player mp3player = new Player(in);
        
        mp3player.play();
        */
    }
     public static byte[][] divideArray(byte[] source, int chunksize) {


        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
            start += chunksize ;
        }

        return ret;
    }

    synchronized public Map<Integer, Question> getQuestions() {
       return this.questions;
    }
    
    
    
    static public byte[] toBytes(ChallengeType ch){
        try {
            byte[] object;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out;
            out = new ObjectOutputStream(bos);   
            out.writeObject(ch);
            object = bos.toByteArray();
            out.close();
            bos.close();
            
            return object;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    static public ChallengeType fromBytes(byte[] bytes){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in;
            in = new ObjectInputStream(bis);
            ChallengeType o = (ChallengeType) in.readObject();
            bis.close();
            in.close();
            
            return o;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
