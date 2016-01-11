/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;


/**
 *
 * @author ferreisi
 */
class AppCanvas extends JComponent{
    
    private static final Dimension PREFERRED_SIZE = new Dimension(400,300);
    
    public AppCanvas(){
        addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent ev){
                             System.out.println("MousePressed... (Thread :"+Thread.currentThread());
                             
            }
        });
    }
    
    public Dimension getPreferredSize(){
             System.out.println("Get preferred size... (Thread :"+Thread.currentThread());
             return PREFERRED_SIZE;
             
    }
    
    public void paintComponent(final Graphics g){
              System.out.println("Painting component... (Thread :"+Thread.currentThread());
               g.setColor(Color.WHITE);
               g.fillRect(0,0,getWidth(),getHeight());
    }
    
}
