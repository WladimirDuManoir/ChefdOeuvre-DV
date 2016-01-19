/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.EventQueue;
import javax.swing.WindowConstants;

/**
 *
 * @author ferreisi
 */
public class Daredevil {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Starting application... (Thread :"+Thread.currentThread());
       System.setProperty("mbrola.base", "C:\\Users\\ferreisi\\Desktop\\freetts-1.2.2-bin\\freetts-1.2\\mbrola");

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                        System.out.println("Creating GUI... (Thread :"+Thread.currentThread());
                        final MainFrame frame = new MainFrame();
                        frame.setVisible(true);
            }
        });
        
    }
    
}
