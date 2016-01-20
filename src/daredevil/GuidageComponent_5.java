//Ceci est le component qui implemente les prototypes suivants :
//- en direction : direction par système Axe Horizontal puis Axe Vertical
//- en guidage : guidage par 3 seuils de distance en distance absolue (indépendant des axes)
package daredevil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComponent;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author ferreisi
 */
class GuidageComponent_5 extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1920, 1200);
    private static final int FREQUENCE_MIN = 100;
    private static final int SUPPLEMENT_FREQUENCE = 600;
    private static final int PITCH_MIN = 55;
    private static final int SUPPLEMENT_PITCH = 30;
    
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 50;
    Boolean dyOK = false;
    Boolean dxOK = false;
    private int posX = 0;
    private int posY = 0;

    // Variables pour le synthetiseur MIDI
    Synthesizer syn = MidiSystem.getSynthesizer();
    MidiChannel channel = syn.getChannels()[0];

    // Variables pour les sons WAV
    AudioStream audioStreamA;
    AudioStream audioStreamB;

    private boolean audioStreamAPlayed = false;
    private boolean audioStreamBPlayed = false;

    public GuidageComponent_5() throws MidiUnavailableException, IOException {

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        Thread mainLoop = new Thread() {
            public void run() {
                int frequence = 400;
                int pitch = 70;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {
 
                        //Action principale
                        System.out.println("hello babe");
                        System.out.println("posX :" + posX);
                        System.out.println("posY :" + posY);

                        playNote(pitch);
                        

                        //playNote();
                        pitch = changePitch(posX);
                        frequence = changeFrequence(posY);

                        //Remise a zéro du compteur pour le timer
                        startTime = currentTime; // on réinitialise le compteur
                    }

                } while (true);
            }
        };
        
        mainLoop.start();

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if ((Math.abs(e.getX() - targetX - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE) && (Math.abs(e.getY() - targetY - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE)) {
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
            public void mouseMoved(final MouseEvent ev) {

                //Update coordonnees de la souris
                posX = ev.getX();
                posY = ev.getY();

                try {
                    sayAxeDirection((int) (posY - targetY - 0.5 * TARGET_SIZE), (int) (posX - targetX - 0.5 * TARGET_SIZE));
                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_4.class.getName()).log(Level.SEVERE, null, ex);
                }

                if ((Math.abs(ev.getX() - targetX - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE) && (Math.abs(ev.getY() - targetY - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE)) {
                    System.out.println("Target trouvée");
                    sayTargetFound();
                    repaint();
                }
            }

        });

    }

    private int changeFrequence(int y) {
        System.out.println("change freq");
        int dy = Math.abs(posY - targetY);
        System.out.println(dy);
        return (int) (FREQUENCE_MIN + (dy / PREFERRED_SIZE.getHeight()) * SUPPLEMENT_FREQUENCE);
    }

    private int changePitch(int x) {
        System.out.println("change freq");
        int dx = Math.abs(posX - targetX);
        System.out.println(dx);
        return (int) (PITCH_MIN + (dx / PREFERRED_SIZE.getWidth()) * SUPPLEMENT_PITCH);
    }
    
    private void playNote(int pitch) {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(1024, 13);
            channel.noteOn(pitch, 50);
        }

    }

    private void playNote2() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(0, 9);

            channel.noteOn(70, 70);
        }

    }
    
    private void playNote3() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(0, 9);

            channel.noteOn(50, 70);
        }

    }

    private void repositionnerTarget() {
        targetX = (int) (0.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.width - 0.5 * TARGET_SIZE));
        targetY = (int) (0.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.height - 0.5 * TARGET_SIZE));
        audioStreamAPlayed = false;
        audioStreamBPlayed = false;
        dyOK = false;
        dxOK = false;
    }

    private void sayTargetFound() {
    }

    public Dimension getPreferredSize() {
        System.out.println("Get preferred size... (Thread :" + Thread.currentThread());
        return PREFERRED_SIZE;

    }

    private void sayAxeDirection(int dx, int dy) throws IOException {

        this.audioStreamA = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\agauche.wav"));
        this.audioStreamB = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\adroite.wav"));

        //VERIFICATION DE DY
        if (!dyOK) {
            if (dy > 0) {
                System.out.println("gauche");
                if (!audioStreamAPlayed) {
                    AudioPlayer.player.start(audioStreamA);
                    audioStreamAPlayed = true;

                }
            } else {
                System.out.println("droite");
                if (!audioStreamBPlayed) {
                    AudioPlayer.player.start(audioStreamB);
                    audioStreamBPlayed = true;
                }
            }

            if (Math.abs(dy) < 0.5 * TARGET_SIZE) {
                dyOK = true;

            }
        } else {
            if (Math.abs(dy) > 0.5 * TARGET_SIZE) {
                audioStreamAPlayed = false;
                audioStreamBPlayed = false;
                playNote3();
                dyOK = false;
                
                
            }

            if (!dxOK) {
                // VERIFICATION DE DX

                if (Math.abs(dx) < 0.5 * TARGET_SIZE) {
                    playNote2();                   
                    dxOK = true;

                }
            } else {
                if (Math.abs(dx) > 0.5 * TARGET_SIZE) {
                    playNote3();
                    dxOK = false;
                }
            }
        }
    }

    public void paintComponent(final Graphics g) {
        System.out.println("Painting component... (Thread :" + Thread.currentThread());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.fillRect(targetX, targetY, TARGET_SIZE, TARGET_SIZE);
    }

}
