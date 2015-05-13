/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLientGUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.beans.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

public final class LoadChallengeProgressBar extends JPanel {
    
    private final JProgressBar progress1 = new JProgressBar() {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };
    private static final JProgressBar progress2 = new JProgressBar() {
        @Override public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        }
    };
    public LoadChallengeProgressBar() {
        super(new BorderLayout());
        //progress1.setForeground(new Color(0xAAFFAAAA));
        progress2.setStringPainted(true);
        progress2.setFont(progress2.getFont().deriveFont(24f));

        //JSlider slider = new JSlider();
        //slider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
        //progress1.setModel(slider.getModel());
        //add(slider, BorderLayout.NORTH);
        JPanel p = new JPanel(new GridLayout(1, 2));
       // p.add(progress1);
        p.add(progress2);
        add(p);
        setPreferredSize(new Dimension(320, 240));
       
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("A Carregar Challenge");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new LoadChallengeProgressBar());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
         
          SwingWorker<String, Void> worker = new Task(frame) {
                    @Override public void done() {
                        frame.dispose();
                    }
                };
                worker.addPropertyChangeListener(new ProgressListener(progress2));
                worker.execute();
    }
}

class ProgressCircleUI extends BasicProgressBarUI {
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

        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
}

class Task extends SwingWorker<String, Void> {
    private final Random rnd = new Random();
    private final JFrame parent;
    public Task(JFrame p){
        parent = p;
    }
    @Override public String doInBackground() {
        int current = 0;
        int lengthOfTask = 100;
        while (current <= lengthOfTask && !isCancelled()) {
            try { // dummy task
                Thread.sleep(85);
            } catch (InterruptedException ie) {
                return "Interrupted";
            }
            setProgress(100 * current / lengthOfTask);
            current++;
        }
        parent.dispose();
        
        return "Done";
        
        
        
    }
}

class ProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;
    ProgressListener(JProgressBar progressBar) {
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
