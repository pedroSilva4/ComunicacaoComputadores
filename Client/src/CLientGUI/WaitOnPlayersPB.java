/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLientGUI;

import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public final class WaitOnPlayersPB extends JPanel {
    private static final JProgressBar progressBar = new JProgressBar() {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new PProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };
    public WaitOnPlayersPB() {
        super(new BorderLayout());
        //progress1.setForeground(new Color(0xAAFFAAAA));
        progressBar.setStringPainted(true);
        progressBar.setFont(progressBar.getFont().deriveFont(24f));

        
        JPanel p = new JPanel(new GridLayout(1, 2));
       
        p.add(progressBar);
        add(p);
        setPreferredSize(new Dimension(320, 240));
       
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI(true);
            }
        });
    }
    public static void createAndShowGUI(boolean b) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("À Espera dos Outros Jogadores");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new WaitOnPlayersPB());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
         
          SwingWorker<String, Void> worker = new PTask(frame,b) {
                    @Override public void done() {
                       
                        frame.dispose();
                    }
                };
                worker.addPropertyChangeListener(new PProgressListener(progressBar));
                worker.execute();
    }
}

class PTask extends SwingWorker<String, Void> {
    private final JFrame parent;
    boolean b;
    public PTask(JFrame p,boolean b){
        parent = p;
        this.b = b;
    }
    @Override public String doInBackground() {
        int current = 0;
        int lengthOfTask = 100;
        while (current <= lengthOfTask && !isCancelled() && this.b) {
            try { // dummy task
                Thread.sleep(75);
            } catch (InterruptedException ie) {
                return "Interrupted";
            }
            setProgress(100 * current/lengthOfTask);
            current++;
            if(current == 100) current = 0;
        }
        parent.dispose();
        
        return "Done";
    }
}

class PProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;
    PProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }
    @Override public void propertyChange(PropertyChangeEvent evt) {
        String strPropertyName = evt.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }
}

class PProgressCircleUI extends BasicProgressBarUI {
    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;
    }
    @Override public void paint(Graphics g, JComponent c) {
        //public void paintDeterminate(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double degree = 360 * progressBar.getPercentComplete();
        double sz = Math.min(barRectWidth, barRectHeight);
        double cx = b.left + barRectWidth  * .5;
        double cy = b.top  + barRectHeight * .5;
        double or = sz * .5;
        //double ir = or - 20;
        double ir = or * .5; //.8;
        Shape inner  = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
        Shape outer  = new Ellipse2D.Double(cx - or, cy - or, sz, sz);
        Shape sector = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        // draw the track
        g2.setPaint(new Color(0xDDDDDD));
        g2.fill(background);

        // draw the circular sector
        //AffineTransform at = AffineTransform.getScaleInstance(-1.0, 1.0);
        //at.translate(-(barRectWidth + b.left * 2), 0);
        //AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(degree), cx, cy);
        //g2.fill(at.createTransformedShape(area));
        g2.setPaint(progressBar.getForeground());
        g2.fill(foreground);
        g2.dispose();

       
       
    }
}
