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
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;


/**
 *
 * @author ferreisi
 */
class AppCanvas extends JComponent{
    
    private static final Dimension PREFERRED_SIZE = new Dimension(800,600);
    private static final int TARGET_X = 400;
    private static final int TARGET_Y = 400;
        private static final int TARGET_SIZE = 30;


    
    public AppCanvas(){
        addMouseListener(new MouseAdapter() {
            public void mousePressed (final MouseEvent ev){
                             
            }
            
            public void mouseReleased (final MouseEvent ev){

            }
 
        });
        
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved (final MouseEvent ev){
                        System.out.println("distance a cible X: "+(ev.getX()-TARGET_X-0.5*TARGET_SIZE)+",Y: "+(ev.getY()-TARGET_Y-0.5*TARGET_SIZE));
                        //sayDistance((int) (ev.getX()-TARGET_X-0.5*TARGET_SIZE), (int) (ev.getY()-TARGET_Y-0.5*TARGET_SIZE));
                          sayDirection((int) (ev.getY()-TARGET_Y-0.5*TARGET_SIZE),(int) (ev.getX()-TARGET_X-0.5*TARGET_SIZE));
            }
                    
                    public void mouseDragged (final MouseEvent ev){
                        
            }
                });

    }
    
    public Dimension getPreferredSize(){
             System.out.println("Get preferred size... (Thread :"+Thread.currentThread());
             return PREFERRED_SIZE;
             
    }
    
    public void sayDistance(int dx, int dy){
                    String text1 = Integer.toString(dx);
                    String text2 = Integer.toString(dy);
                    Speech freeTTS1 = new Speech(text1);
                    Speech freeTTS2 = new Speech(text2);

                    freeTTS1.speak();
                    System.out.println("say speech1 dx");
                    freeTTS2.speak();
                    System.out.println("say speech2 dy");     
    }
    
    public void sayDirection(int dx, int dy){
                    double angle = Math.atan2((double) dy,(double) dx);
                    System.out.println(angle);
             
                    Speech freeTTSnorth = new Speech("Go North");
                    Speech freeTTSnortheast = new Speech("Go North east");
                    Speech freeTTSeast = new Speech("Go east");
                    Speech freeTTSsoutheast = new Speech("Go south east");
                    Speech freeTTSsouth = new Speech("Go south");
                    Speech freeTTSsouthwest = new Speech("Go south west");
                    Speech freeTTSwest = new Speech("Go west");
                    Speech freeTTSnorthwest = new Speech("Go North west");

                    if (angle < -2.512) {freeTTSsouth.speak();
                    System.out.println("go south");
                    }
                    else if (angle < -1.884) {freeTTSsoutheast.speak();
                                        System.out.println("go southeast");

                    }
                    else if (angle < -1.256) {freeTTSeast.speak();
                                        System.out.println("go east");

                    }
                    else if (angle < -0.628) {freeTTSnortheast.speak();
                                        System.out.println("go northeast");

                    }
                    else if (angle < 0.628) {freeTTSnorth.speak();
                                        System.out.println("go north");

                    }
                    else if (angle < 1.256) {freeTTSnorthwest.speak();
                                        System.out.println("go northwest");

                    }
                    else if (angle < 1.884) {freeTTSwest.speak();
                                        System.out.println("go west");

                    }
                    else if (angle < 2.512) {freeTTSsouthwest.speak();
                                        System.out.println("go southwest");

                    }
                    else {freeTTSsouth.speak();
                                              System.out.println("go south");
  
                            };
    }
    
    public void paintComponent(final Graphics g){
              System.out.println("Painting component... (Thread :"+Thread.currentThread());
               g.setColor(Color.WHITE);
               g.fillRect(0,0,getWidth(),getHeight());
               g.setColor(Color.RED);
               g.fillOval(TARGET_X,TARGET_Y,TARGET_SIZE,TARGET_SIZE);
    }
    
}
