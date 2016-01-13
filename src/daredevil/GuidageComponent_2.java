//Ceci est le component qui implemente les prototypes suivants :
//- en direction : direction par système Axe Horizontal puis Axe Vertical
//- en guidage : guidage par 3 seuils de distance en distance absolue (indépendant des axes)

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
class GuidageComponent_2 extends JComponent {
    
    private static final Dimension PREFERRED_SIZE = new Dimension(1200,900);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 30;
    Boolean dyOK = false;
    Boolean dxOK = false;

    public GuidageComponent_2(){
        
       
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved (final MouseEvent ev){
                          sayAxeDirection((int) (ev.getY()-targetY-0.5*TARGET_SIZE),(int) (ev.getX()-targetX-0.5*TARGET_SIZE));
                          sayDistanceFarClose((int) (ev.getY()-targetY-0.5*TARGET_SIZE),(int) (ev.getX()-targetX-0.5*TARGET_SIZE));
                          
                          if ((Math.abs(ev.getX()-targetX-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)&&(Math.abs(ev.getY()-targetY-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)){   
                              System.out.println("Target trouvée");
                              sayTargetFound();
                              repositionnerTarget();
                              repaint();
                          }
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
    
    private void sayAxeDirection(int dx, int dy){
        Speech freeTTSleft = new Speech("Left");
        Speech freeTTSright = new Speech("Right");
        Speech freeTTSstop = new Speech("Stop");
        Speech freeTTSup = new Speech("Up");
        Speech freeTTSdown = new Speech("Down");
        
        //VERIFICATION DE DY
        if(!dyOK){
         if (dy>0){
                            System.out.println("gauche");
                            freeTTSleft.speak();                       
        }
        else {
            System.out.println("droite");
                            freeTTSright.speak();
        }
            
        if (Math.abs(dy)<0.5*TARGET_SIZE){
                System.out.println("Y axis OK");
                freeTTSstop.speak();
                dyOK = true;
                
            }
        } else {
             if (Math.abs(dy)>0.5*TARGET_SIZE){
                System.out.println("Y axis not OK anymore");
                dyOK = false;
             }
             
             if(!dxOK){
                          // VERIFICATION DE DX

         if (dx>0){
                            System.out.println("haut");
                            freeTTSup.speak();                       
        }
        else {
            System.out.println("bas");
                            freeTTSdown.speak();
        }
            
        if (Math.abs(dx)<0.5*TARGET_SIZE){
                System.out.println("X axis OK");
                freeTTSstop.speak();
                dxOK = true;
                
            }
        } else {
                 if (Math.abs(dx)>0.5*TARGET_SIZE){
                System.out.println("X axis not OK anymore");
                dxOK = false;
                 }
                 
                 if (dxOK){
                                     System.out.println("Target found");

                 }
             }
             
        }
    }
    
    private void sayDistanceFarClose(int dx, int dy) {
                Speech freeTTSfar = new Speech("Far");
                Speech freeTTSclose = new Speech("Close");
                Speech freeTTSveryclose = new Speech("Very Close");
                if (dx*dx+dy*dy>200*200){
                    freeTTSfar.speak();
                } else if (dx*dx+dy*dy>100*100){
                   freeTTSclose.speak();
                } else {
                    freeTTSveryclose.speak();
                }
            }
    
    public void sayDistanceCoordonnees(int dx, int dy){
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
        // FONCTION Atan2 permet de transformer un doublet de coordonnees en angle en radian, ici le 0 rad est au sud.
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
               g.fillOval(targetX,targetY,TARGET_SIZE,TARGET_SIZE);
    }
    
}
