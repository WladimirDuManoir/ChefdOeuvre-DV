/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.EventQueue;

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
       
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                        System.out.println("Creating GUI... (Thread :"+Thread.currentThread());
                        final AppFrame frame = new AppFrame();
                        frame.setVisible(true);
            }
        });
        
    }
    
}
