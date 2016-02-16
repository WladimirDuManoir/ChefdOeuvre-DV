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
import java.io.FileNotFoundException;
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
class GuidageComponent_2 extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1900,1200);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 50;
    Boolean dyOK = false;
    Boolean dxOK = false;
    private int posX = 0;
    private int posY = 0;
    Boolean cibleTrouvee = false;
    Boolean axeIntercepte = false;

    private int nbTargetFound = 0;
    private int nbErreurs = 0;

    // Variables pour le synthetiseur MIDI
    Synthesizer syn = MidiSystem.getSynthesizer();
    MidiChannel channel = syn.getChannels()[0];

    // Variables pour les sons WAV
    AudioStream audioStreamA;
    AudioStream audioStreamB;
    AudioStream audioStreamC;
    AudioStream audioStreamD;
    AudioStream audioStreamE;
    AudioStream audioStreamF;

    private boolean audioStreamAPlayed = false;
    private boolean audioStreamBPlayed = false;

    public GuidageComponent_2() throws MidiUnavailableException, IOException {

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        Thread mainLoop = new Thread() {
            public void run() {
                int frequence = 1200;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {

                        //Action principale
                        if (!dyOK) {
                            try {

                                sayDroiteGauche(posX);
                            } catch (IOException ex) {
                                Logger.getLogger(GuidageComponent_2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        if ((dyOK) && (!dxOK)) {
                            try {
                                sayHautBas(posX, posY);
                            } catch (IOException ex) {
                                Logger.getLogger(GuidageComponent_2.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

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
                    playNote(4);
                    nbTargetFound++;
                    try {
                        repositionnerTarget(e.getX());
                    } catch (IOException ex) {
                        Logger.getLogger(GuidageComponent_4.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    repaint();
                } else {
                    playNote(5);
                    nbErreurs++;
                }
                printResultats();

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

                try {
                    axeIntercepte((int) (posX - targetX - 0.5 * TARGET_SIZE));
                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_2.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    cibleTrouvee((int) (posY - targetY - 0.5 * TARGET_SIZE), (int) (posX - targetX - 0.5 * TARGET_SIZE));
                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_4.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

    private void axeIntercepte(int dx) throws IOException {
        if (!axeIntercepte) {
            if (Math.abs(dx) < 0.5 * TARGET_SIZE) {
                playNote(1);
                axeIntercepte = true;
            }
        }
        if ((Math.abs(dx) > 0.5 * TARGET_SIZE) && (axeIntercepte)) {
            axeIntercepte = false;
        }
    }

    private void cibleTrouvee(int dx, int dy) throws IOException {
        if (!cibleTrouvee) {
            if ((Math.abs(dx) < 0.5 * TARGET_SIZE) && (Math.abs(dy) < 0.5 * TARGET_SIZE)) {
                playNote(2);
                cibleTrouvee = true;
            }
        }
        if (((Math.abs(dx) > 0.5 * TARGET_SIZE) || (Math.abs(dy) > 0.5 * TARGET_SIZE)) && (cibleTrouvee)) {
            playNote(3);
            cibleTrouvee = false;
        }

    }

    private void printResultats() {
        System.out.println("***");
        System.out.println("Nombre cible trouvées: " + nbTargetFound + ", nombre erreurs :" + nbErreurs);
    }

    private void sayDroiteGauche(int x) throws IOException{
          this.audioStreamA = new AudioStream(new FileInputStream("sounds\\gauche.wav"));
        this.audioStreamB = new AudioStream(new FileInputStream("sounds\\droite.wav"));
        
        if ((x - targetX - 0.5 * TARGET_SIZE) > 0) {
            AudioPlayer.player.start(audioStreamA);
        } else {
            AudioPlayer.player.start(audioStreamB);
        }
    }
    
    private void sayHautBas(int x, int y) throws IOException {
        this.audioStreamE = new AudioStream(new FileInputStream("sounds\\haut.wav"));
        this.audioStreamF = new AudioStream(new FileInputStream("sounds\\bas.wav"));

        if ((y - targetY - 0.5 * TARGET_SIZE) > 0) {
            AudioPlayer.player.start(audioStreamE);
        } else {
            AudioPlayer.player.start(audioStreamF);

        }
    }

    private void playNote(int note) {
        channel.allNotesOff();
        if (channel != null) {

            switch (note) {
                case 1:
                    channel.programChange(1024, 13);
                    channel.noteOn(80, 50);
                    break;
                case 2:
                    channel.programChange(0, 9);
                    channel.noteOn(70, 70);
                    break;
                case 3:
                    channel.programChange(0, 9);
                    channel.noteOn(50, 70);
                    break;
                case 4:
                    channel.programChange(1024, 11);
                    channel.noteOn(75, 70);
                    break;

                case 5:
                    channel.programChange(0, 27);
                    channel.noteOn(40, 70);
                    break;
            }
        }
    }

    private void repositionnerTarget(int x) throws IOException {

        this.audioStreamC = new AudioStream(new FileInputStream("sounds\\newtargetgauche.wav"));
        this.audioStreamD = new AudioStream(new FileInputStream("sounds\\newtargetdroite.wav"));

       targetX = (int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.width - 3 * TARGET_SIZE));
        targetY = (int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.height - 3 * TARGET_SIZE));
        if (x > targetX) {
            AudioPlayer.player.start(audioStreamC);
        } else {
            AudioPlayer.player.start(audioStreamD);

        }
        audioStreamAPlayed = true;
        audioStreamBPlayed = true;
        cibleTrouvee = false;
        dyOK = false;
        dxOK = false;
    }

    public Dimension getPreferredSize() {
        System.out.println("Get preferred size... (Thread :" + Thread.currentThread());
        return PREFERRED_SIZE;

    }

    private void sayAxeDirection(int dx, int dy) throws IOException {

      

        //VERIFICATION DE DY
        if (!dyOK) {
            
            if (Math.abs(dy) < 0.5 * TARGET_SIZE) {
                dyOK = true;

            }
        } else {
            if (Math.abs(dy) > 0.5 * TARGET_SIZE) {
               
                dyOK = false;
            }

            if (!dxOK) {
                // VERIFICATION DE DX
                if (Math.abs(dx) < 0.5 * TARGET_SIZE) {
                    dxOK = true;
                }
            } else {
                if (Math.abs(dx) > 0.5 * TARGET_SIZE) {
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
        g.drawLine(targetX, 0, targetX, (int) PREFERRED_SIZE.getHeight());
        g.drawLine(targetX + TARGET_SIZE, 0, targetX + TARGET_SIZE, (int) PREFERRED_SIZE.getHeight());
    }

}
