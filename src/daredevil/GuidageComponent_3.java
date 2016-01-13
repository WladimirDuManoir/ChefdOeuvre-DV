//Ceci est le component qui implemente les prototypes suivants :
//- en direction : direction par système 8 directions
//- en guidage : guidage par 3 seuils de distance


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
class GuidageComponent_3 extends JComponent{
    
    private static final Dimension PREFERRED_SIZE = new Dimension(1200,900);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 30;

    public GuidageComponent_3(){
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed (final MouseEvent ev){
                             
            }
            
            public void mouseReleased (final MouseEvent ev){

            }
 
        });
        
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved (final MouseEvent ev){
                        System.out.println("distance a cible X: "+(ev.getX()-targetX-0.5*TARGET_SIZE)+",Y: "+(ev.getY()-targetY-0.5*TARGET_SIZE));
                          sayDistancePiano((int) (ev.getY()-targetY-0.5*TARGET_SIZE),(int) (ev.getX()-targetX-0.5*TARGET_SIZE));
                          if ((Math.abs(ev.getX()-targetX-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)&&(Math.abs(ev.getY()-targetY-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)){   
                              System.out.println("Target trouvée");
                              sayTargetFound();
                              repositionnerTarget();
                              repaint();
                          }
            }
                    
                    public void mouseDragged (final MouseEvent ev){
                        
            }

            

                });

    }
    
    private void repositionnerTarget() {
                targetX = (int)(Math.random()*PREFERRED_SIZE.width); 
                targetY = (int)(Math.random()*PREFERRED_SIZE.height); 
            }
    
    private void sayTargetFound(){
                    Speech freeTTStargetfound = new Speech("Target Found");
                    freeTTStargetfound.speak();
    }
    
    public Dimension getPreferredSize(){
             System.out.println("Get preferred size... (Thread :"+Thread.currentThread());
             return PREFERRED_SIZE;
             
    }
    
    private void sayDistancePiano(int dx, int dy) {
                
                if (dx*dx+dy*dy>200*200){
                    
                } else if (dx*dx+dy*dy>100*100){
                    
                } else if (dx*dx+dy*dy>100*100){ 
                    
                } else if (dx*dx+dy*dy>100*100){
                    
                } else if (dx*dx+dy*dy>100*100){
                    
                } else if (dx*dx+dy*dy>100*100){
                    
                } else if (dx*dx+dy*dy>100*100){
                 
                } else {
                    
                }
    }
                    
    public void paintComponent(final Graphics g){
              System.out.println("Painting component... (Thread :"+Thread.currentThread());
               g.setColor(Color.WHITE);
               g.fillRect(0,0,getWidth(),getHeight());
               g.setColor(Color.RED);
               g.fillOval(targetX,targetY,TARGET_SIZE,TARGET_SIZE);
    }
    
}
