/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Pedro
 */
public class Challenge {

    public String getName() {
        return this.name;
    }
    public class Question{
        int question_n;
        String question;
        String[] answers  = new String[3];
        int rightanswer;
        byte[] music;
        byte[] image;
        
      public Question(int question_n,String question,String[] answers,int correctanswer,byte[] m,byte[] image){
          this.question_n=question_n;
          this.question = question;
          this.answers = answers;
          this.rightanswer=correctanswer;
          this.music = m;
          this.image= image;
      }

        public byte[] getImage() {
            return this.image;
        }
        public byte[] getMusic() {
           return this.music;
        }

        public String getAnswer1() {
           return this.answers[0];
        }

        public String getAnswer2() {
            return this.answers[1];
        }

        public String getAnswer3() {
           return this.answers[2];
        }

        public String getQuestion() {
            return this.question;
        }
    }
    
    String name;
    int n_questions;
    Map<Integer,Question> questions;
    
    
    public Challenge(String filepath) throws IOException{
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
             byte[] music = Files.readAllBytes(Paths.get(musicPath));
             byte[] image = Files.readAllBytes(Paths.get(imagePath));
             String[] answers = new String[3];
             answers[0] = parts[2];
             answers[1] = parts[3];
             answers[2] = parts[4];
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
    
}
