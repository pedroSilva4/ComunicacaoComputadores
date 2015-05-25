/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CLientGUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Pedro
 */
public class ErrorWindow {
    
    private String title;
    private String message;
    private int tipo;
    private JFrame frame;
    public ErrorWindow(String title,String message,String tipo,JFrame frame)
    {
        this.title = title;
        this.message = message;

        if("error".equals(tipo)) this.tipo=JOptionPane.ERROR_MESSAGE;
        if("warning".equals(tipo)) this.tipo=JOptionPane.WARNING_MESSAGE;
        else this.tipo = JOptionPane.PLAIN_MESSAGE;
    }
    
    public void wshow()
    {
        JOptionPane.showMessageDialog(frame, message, title, tipo);
    }
    
}
