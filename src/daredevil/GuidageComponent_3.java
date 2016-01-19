//Ceci est le component qui implemente les prototypes suivants :
//- en guidage : guidage par gamme de piano


package daredevil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author ferreisi
 */
class GuidageComponent_3 extends JComponent{
    
    private static final Dimension PREFERRED_SIZE = new Dimension(1920,1200);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 30;
    private Zone zone = Zone.A;
    private static final int TailleZoneDistance = TARGET_SIZE;
 
    AudioStream audioStreamA;
    AudioStream audioStreamB;
    AudioStream audioStreamC;
    AudioStream audioStreamD;
    AudioStream audioStreamE;
    AudioStream audioStreamF;
    AudioStream audioStreamG;
  
    
    public GuidageComponent_3() throws IOException{
        
                addMouseListener(new MouseListener(){
                 

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if ((Math.abs(e.getX()-targetX-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)&&(Math.abs(e.getY()-targetY-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)){ 
                         repositionnerTarget();
                         repaint();
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }

                    

                });
        
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved (final MouseEvent ev){
                        System.out.println("distance a cible X: "+(ev.getX()-targetX-0.5*TARGET_SIZE)+",Y: "+(ev.getY()-targetY-0.5*TARGET_SIZE));
                        try {
                            sayDistancePiano((int) (ev.getY()-targetY-0.5*TARGET_SIZE),(int) (ev.getX()-targetX-0.5*TARGET_SIZE));
                        } catch (IOException ex) {
                            Logger.getLogger(GuidageComponent_3.class.getName()).log(Level.SEVERE, null, ex);
                        }
                          if ((Math.abs(ev.getX()-targetX-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)&&(Math.abs(ev.getY()-targetY-0.5*TARGET_SIZE)<0.5*TARGET_SIZE)){   
                              System.out.println("Target trouvée");
                              sayTargetFound();
                              repaint();
                          }
            }
                    
                    public void mouseDragged (final MouseEvent ev){
                        
            }

            

                });

    }
    
    private void repositionnerTarget() {
                targetX = TARGET_SIZE+(int)(Math.random()*(PREFERRED_SIZE.width-TARGET_SIZE)); 
                targetY = TARGET_SIZE+(int)(Math.random()*(PREFERRED_SIZE.height-TARGET_SIZE)); 
            }
    
    private void sayTargetFound(){
                    Speech freeTTStargetfound = new Speech("Target Found");
                    freeTTStargetfound.speak();
    }
    
    public Dimension getPreferredSize(){
             System.out.println("Get preferred size... (Thread :"+Thread.currentThread());
             return PREFERRED_SIZE;
             
    }
    
    public enum Zone {
    A, B, C, D,
    E, F, G 
}
    
    private void sayDistancePiano(int dx, int dy) throws FileNotFoundException, IOException {
       
        this.audioStreamA = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\a.wav"));
        this.audioStreamB = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\b.wav"));
        this.audioStreamC = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\c.wav"));
        this.audioStreamD = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\d.wav"));
        this.audioStreamE = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\e.wav"));
        this.audioStreamF = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\f.wav"));
        this.audioStreamG = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\g.wav"));
                
                if (dx*dx+dy*dy>36*TailleZoneDistance*36){
                    if (zone != Zone.A){
                                                AudioPlayer.player.start(audioStreamA);
                                                    zone = Zone.A;
                    }

                } else if (dx*dx+dy*dy>25*TailleZoneDistance*TailleZoneDistance){
                    if (zone != Zone.B){
                        AudioPlayer.player.start(audioStreamB);
                        zone = Zone.B;
                    }
                } else if (dx*dx+dy*dy>16*TailleZoneDistance*TailleZoneDistance){ 
                    if (zone != Zone.C){
                        AudioPlayer.player.start(audioStreamC);
                        zone = Zone.C;
                    }

                } else if (dx*dx+dy*dy>9*TailleZoneDistance*TailleZoneDistance){
                    if (zone != Zone.D){
                        AudioPlayer.player.start(audioStreamD);
                        zone = Zone.D;
                    }

                } else if (dx*dx+dy*dy>4*TailleZoneDistance*TailleZoneDistance){
                    if (zone != Zone.E){
                        AudioPlayer.player.start(audioStreamE);
                        zone = Zone.E;
                    }

                } else if (dx*dx+dy*dy>20*20){
                    if (zone != Zone.F){
                        AudioPlayer.player.start(audioStreamF);
                        zone = Zone.F;
                    }

                } else {
                    if (zone != Zone.G){
                        AudioPlayer.player.start(audioStreamG);
                        zone = Zone.G;
                    }
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