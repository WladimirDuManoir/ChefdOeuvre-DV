/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author ferreisi
 */
class VisualisationParserComponent extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(100,200);

    
    public VisualisationParserComponent() {
        this.setSize(PREFERRED_SIZE);
           
    }
 
     public void paintComponent(final Graphics g){
              System.out.println("Painting component... (Thread :"+Thread.currentThread());
               g.setColor(Color.RED);
               g.fillRect(0,0,getWidth(),getHeight());
    }
    
}
