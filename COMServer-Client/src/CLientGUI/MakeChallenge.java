/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLientGUI;

import Client.GameThread;
import Common.PDU_Builder;
import Common.User;
import Common.PDU;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author fdr
 */
public class MakeChallenge extends javax.swing.JDialog {

    /**
     * Creates new form MakeChallenge
     */
    DatagramSocket socket;
    int label;
    GameThread gt;
    public MakeChallenge(java.awt.Frame parent, boolean modal, DatagramSocket socket,int label,GameThread gt) {
        super(parent, modal);
        initComponents();
        this.label = label;
        this.socket = socket;
        this.setLocationRelativeTo(null);
        this.gt = gt;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        makeChallenge_label_nome = new javax.swing.JLabel();
        makeChallenge_tf_nome = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        makeChallenge_ftf_Data = new javax.swing.JFormattedTextField();
        makeChallenge_date = new javax.swing.JLabel();
        makeChallenge_tff_hora = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        makeChallenge_label_nome.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        makeChallenge_label_nome.setText("Name Challenge:");

        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        try {
            makeChallenge_ftf_Data.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        makeChallenge_ftf_Data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeChallenge_ftf_DataActionPerformed(evt);
            }
        });

        makeChallenge_date.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        makeChallenge_date.setText("Date:");

        try {
            makeChallenge_tff_hora.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        makeChallenge_tff_hora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeChallenge_tff_horaActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("No minimo daqui a 5 min");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(makeChallenge_label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(makeChallenge_date, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(makeChallenge_ftf_Data, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(makeChallenge_tff_hora, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(makeChallenge_tf_nome)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(makeChallenge_label_nome)
                    .addComponent(makeChallenge_tf_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(makeChallenge_ftf_Data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(makeChallenge_date)
                    .addComponent(makeChallenge_tff_hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            String name = this.makeChallenge_tf_nome.getText();
            String date = this.makeChallenge_ftf_Data.getText();
            String time= this.makeChallenge_tff_hora.getText();
           
            // pora data e ho ra com estrutura correcta
            String[] tokens = date.split("/");
            
            
            date= tokens[2].substring(2)+tokens[1]+tokens[0];
            if(date.trim().equals(""))
            {
                date = null;
            }
            String[] tokenstime = time.split(":");
            time = tokenstime[0]+tokenstime[1]+"00";
            if(time.trim().equals("00"))
            {
                time = null;
            }
            if(time == null && date == null) System.out.println("tudo nulo");
            
            if((time == null && date == null) || (time != null && date != null)){
                PDU request = PDU_Builder.MAKE_CHALLENGE(label, name, date, time);
                byte[] data  = PDU.toBytes(request);
                DatagramPacket packet = new DatagramPacket(data, data.length);

                socket.send(packet);

                packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);

                PDU reply = PDU.fromBytes(packet.getData());

                parse_reply(reply);

                this.dispose();
                return;
            }
           
            new ErrorWindow("Erro", "Erro ao criar Challenge, deve preencher\n os dois campos para o horario\n ou nenhum deles", 
                            "error", new JFrame()).wshow();
            
        } catch (IOException ex) {
            Logger.getLogger(MakeChallenge.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void makeChallenge_ftf_DataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeChallenge_ftf_DataActionPerformed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_makeChallenge_ftf_DataActionPerformed

    private void makeChallenge_tff_horaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeChallenge_tff_horaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_makeChallenge_tff_horaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MakeChallenge.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MakeChallenge.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MakeChallenge.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MakeChallenge.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MakeChallenge dialog = new MakeChallenge(new javax.swing.JFrame(), true,null,0,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel makeChallenge_date;
    private javax.swing.JFormattedTextField makeChallenge_ftf_Data;
    private javax.swing.JLabel makeChallenge_label_nome;
    private javax.swing.JTextField makeChallenge_tf_nome;
    private javax.swing.JFormattedTextField makeChallenge_tff_hora;
    // End of variables declaration//GEN-END:variables

    void parse_reply(PDU reply){
        byte[][] data = reply.getData();
        
        if(data[21] == null){
            String date = new String(data[4]);
            String time = new String(data[5]);
            String name = new String(data[7]);
            int n_questions = Integer.parseInt(new String(data[10]));
            
            
           
            gt.setChallengeData(name, date, time, n_questions, label);
            gt.start();
            new ErrorWindow("Messagem", "Challenge Created", "Message",(JFrame)this.getParent()).wshow();
        }
        else{
            new ErrorWindow("Erro", new String(data[21]), "error", (JFrame)this.getParent()).wshow();
        }
    }




}
