/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.PDU;

/**
 *
 * @author Pedro
 */
public class REPLY_Builder {
    
      static public PDU REPLY_OK(int label){
           int size = 0;
           byte data[][] = new byte[21][];
           data[0] = String.valueOf(0).getBytes();
          
           return new PDU(0.0f, 0, 0,label, 1, 0, null, 0);
    }
      
}
