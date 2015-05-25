/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

/**
 *
 * @author Pedro
 */
public class Question {
      int question_n;
        public String question;
        public String[] answers  = new String[3];
        int rightanswer;
        byte[][] music;
        byte[][] image;
    public Question(int question_n,String question,String[] answers,int correctanswer,byte[][] m,byte[][] image){
          this.question_n=question_n;
          this.question = question;
          this.answers = answers;
          this.rightanswer=correctanswer;
          this.music = m;
          this.image= image;
      }

        public byte[][] getImage() {
            return this.image;
        }
        public byte[][] getMusic() {
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

    public int getRightAwnser() {
        return this.rightanswer;
    }

    public void setAnswer(int answer) {
        this.rightanswer = answer;
    }
    }

