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
class GuidageComponent_4 extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1920, 1200);
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

    public GuidageComponent_4() throws MidiUnavailableException, IOException {

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        this.audioStreamA = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\a.wav"));
        this.audioStreamB = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\Piano keys\\b.wav"));

        Thread mainLoop = new Thread() {
            public void run() {
                int frequence = 400;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {
                        //Action principale
                        System.out.println("hello babe");
                        System.out.println("posX :" + posX);
                        System.out.println("posY :" + posY);

                        if (dyOK) {
                            playNote();
                        }

                        //playNote();
                        frequence = 400;

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

    private void playNote() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(1024, 13);

            channel.noteOn(80, 70);
        }

    }

    private void playNote2() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(1024, 12);

            channel.noteOn(80, 100);
        }

    }

    private void playNote3() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(0, 47);

            channel.noteOn(70, 50);
        }

    }

    private void playNote4() {
        channel.allNotesOff();

        if (channel != null) {
            channel.programChange(0, 108);

            channel.noteOn(70, 100);
        }

    }

    private void repositionnerTarget() {
        targetX = (int) (Math.random() * PREFERRED_SIZE.width);
        targetY = (int) (Math.random() * PREFERRED_SIZE.height);
        audioStreamAPlayed = false;
        audioStreamBPlayed = false;
        dyOK = false;
    }

    private void sayTargetFound() {
        playNote2();
    }

    public Dimension getPreferredSize() {
        System.out.println("Get preferred size... (Thread :" + Thread.currentThread());
        return PREFERRED_SIZE;

    }

    private void sayAxeDirection(int dx, int dy) throws IOException {

        Speech freeTTSstop = new Speech("Stop");
        Speech freeTTSup = new Speech("Up");
        Speech freeTTSdown = new Speech("Down");

        //VERIFICATION DE DY
        if (!dyOK) {
            if (dy > 0) {
                System.out.println("gauche");
                if (!audioStreamAPlayed) {

                }
            } else {
                System.out.println("droite");
                if (!audioStreamBPlayed) {
                    AudioPlayer.player.start(audioStreamB);
                    audioStreamBPlayed = true;
                }
            }

            if (Math.abs(dy) < 0.5 * TARGET_SIZE) {
                playNote();
                dyOK = true;

            }
        } else {
            if (Math.abs(dy) > 0.5 * TARGET_SIZE) {
                //playNote2();
                dyOK = false;
            }

            if (!dxOK) {
                // VERIFICATION DE DX

                if (dx > 0) {
                    System.out.println("haut");
                    // playNote3();
                } else {
                    System.out.println("bas");
                    // playNote4();
                }

                if (Math.abs(dx) < 0.5 * TARGET_SIZE) {
                    System.out.println("X axis OK");
                    // freeTTSstop.speak();
                    dxOK = true;

                }
            } else {
                if (Math.abs(dx) > 0.5 * TARGET_SIZE) {
                    System.out.println("X axis not OK anymore");
                    dxOK = false;
                }

                if (dxOK) {
                    System.out.println("Target found");

                }
            }

        }
    }

    private void sayDistanceFarClose(int dx, int dy) {
        Speech freeTTSfar = new Speech("Far");
        Speech freeTTSclose = new Speech("Close");
        Speech freeTTSveryclose = new Speech("Very Close");
        if (dx * dx + dy * dy > 200 * 200) {
            freeTTSfar.speak();
        } else if (dx * dx + dy * dy > 100 * 100) {
            freeTTSclose.speak();
        } else {
            freeTTSveryclose.speak();
        }
    }

    public void sayDistanceCoordonnees(int dx, int dy) {
        String text1 = Integer.toString(dx);
        String text2 = Integer.toString(dy);
        Speech freeTTS1 = new Speech(text1);
        Speech freeTTS2 = new Speech(text2);

        freeTTS1.speak();
        System.out.println("say speech1 dx");
        freeTTS2.speak();
        System.out.println("say speech2 dy");
    }

    public void sayDirection(int dx, int dy) {
        // FONCTION Atan2 permet de transformer un doublet de coordonnees en angle en radian, ici le 0 rad est au sud.
        double angle = Math.atan2((double) dy, (double) dx);
        System.out.println(angle);

        Speech freeTTSnorth = new Speech("Go North");
        Speech freeTTSnortheast = new Speech("Go North east");
        Speech freeTTSeast = new Speech("Go east");
        Speech freeTTSsoutheast = new Speech("Go south east");
        Speech freeTTSsouth = new Speech("Go south");
        Speech freeTTSsouthwest = new Speech("Go south west");
        Speech freeTTSwest = new Speech("Go west");
        Speech freeTTSnorthwest = new Speech("Go North west");

        if (angle < -2.512) {
            freeTTSsouth.speak();
            System.out.println("go south");
        } else if (angle < -1.884) {
            freeTTSsoutheast.speak();
            System.out.println("go southeast");

        } else if (angle < -1.256) {
            freeTTSeast.speak();
            System.out.println("go east");

        } else if (angle < -0.628) {
            freeTTSnortheast.speak();
            System.out.println("go northeast");

        } else if (angle < 0.628) {
            freeTTSnorth.speak();
            System.out.println("go north");

        } else if (angle < 1.256) {
            freeTTSnorthwest.speak();
            System.out.println("go northwest");

        } else if (angle < 1.884) {
            freeTTSwest.speak();
            System.out.println("go west");

        } else if (angle < 2.512) {
            freeTTSsouthwest.speak();
            System.out.println("go southwest");

        } else {
            freeTTSsouth.speak();
            System.out.println("go south");

        };
    }

    public void paintComponent(final Graphics g) {
        System.out.println("Painting component... (Thread :" + Thread.currentThread());
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.fillOval(targetX, targetY, TARGET_SIZE, TARGET_SIZE);
    }

}
