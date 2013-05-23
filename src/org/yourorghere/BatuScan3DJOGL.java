package org.yourorghere;

import Ruang2D.Ruang2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;



/**
 * BatuScan3DJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class BatuScan3DJOGL{
    
    public static JFrame frame = new JFrame("Batu Scan 2D with JOGL");
    final static int WINDOW_WIDTH = 800;  // Width of the drawable
    final static int WINDOW_HEIGHT = 480; // Height of the drawable
    
    public static void main(String[] args) {

        final Ruang2D R2D = new Ruang2D("data/model.xls", WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setContentPane(R2D);
        frame.addWindowListener(new WindowAdapter() {
            @Override 
            public void windowClosing(WindowEvent e) {
                // Use a dedicate thread to run the stop() to ensure that the
                // animator stops before program exits.
                new Thread() {
                    @Override 
                    public void run() {
                        R2D.animator.stop(); // stop the animator loop
                        System.exit(0);
                    }
                }.start();
            }
        });
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        R2D.animator.start(); // start the animation loop
    }

}

