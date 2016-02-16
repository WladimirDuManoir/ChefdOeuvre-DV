//Ceci est le component qui implemente les prototypes suivants :
//- en direction : direction par système 8 directions
//- en guidage : guidage par 3 seuils de distance
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComponent;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author ferreisi
 */
class GuidageComponent_3 extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1900,1200);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 50;
    private int posX = 0;
    private int posY = 0;

    private int nbTargetFound = 0;
    private int nbErreurs = 0;
    private int TAILLE_PALIER = 100;

    Boolean cibleTrouvee = false;

    private Zone zone;

    // Variables pour le synthetiseur MIDI
    Synthesizer syn = MidiSystem.getSynthesizer();
    MidiChannel channel = syn.getChannels()[0];

    //
    AudioStream audioStreamHaut;
    AudioStream audioStreamBas;
    AudioStream audioStreamDroite;
    AudioStream audioStreamGauche;
    AudioStream audioStreamEnHautAGauche;
    AudioStream audioStreamEnHautADroite;
    AudioStream audioStreamEnBasAGauche;
    AudioStream audioStreamEnBasADroite;
    AudioStream audioStreamLoin;
    AudioStream audioStreamProche;
    AudioStream audioStreamTresLoin;
    AudioStream audioStreamTresProche;
    AudioStream audioStreamNewTarget;

    public GuidageComponent_3() throws MidiUnavailableException, IOException, InterruptedException {

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        Thread mainLoop2 = new Thread() {
            public void run() {
                int frequence = 2000;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {
                        System.out.println("debug");
                        try {
                            if (!cibleTrouvee) {
                                sayDirection(posX - targetX-25, posY - targetY-25);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(GuidageComponent_1.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //Remise a zéro du compteur pour le timer
                        startTime = currentTime; // on réinitialise le compteur

                    }

                } while (true);
            }
        };

        mainLoop2.start();

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if ((Math.abs(e.getX() - targetX - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE) && (Math.abs(e.getY() - targetY - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE)) {
                    playNote(4);
                    nbTargetFound++;
                    try {
                        repositionnerTarget(e.getX(), e.getY());
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
                    cibleTrouvee((int) (posY - targetY - 0.5 * TARGET_SIZE), (int) (posX - targetX - 0.5 * TARGET_SIZE));
                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_4.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    distanceCible((int) (posY - targetY - 0.5 * TARGET_SIZE), (int) (posX - targetX - 0.5 * TARGET_SIZE));
                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    private void repositionnerTarget(int x, int y) throws IOException {

        this.audioStreamNewTarget = new AudioStream(new FileInputStream("sounds\\newtarget.wav"));

       targetX = (int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.width - 3 * TARGET_SIZE));
        targetY = (int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.height - 3 * TARGET_SIZE));

        AudioPlayer.player.start(audioStreamNewTarget);
        cibleTrouvee = false;
    }

    public enum Zone {

        A, B, C, D, E, F, G
    }

    private void distanceCible(int dx, int dy) throws IOException {
        System.out.println(zone);
        channel.allNotesOff();
        if (channel != null) {
            if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER * 36) {
                if (zone != Zone.G) {
                    channel.programChange(1024, 11);
                    channel.noteOn(55, 50);
                    zone = Zone.G;
                }
            } else if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER * 25){
                if (zone != Zone.F) {
                    channel.programChange(1024, 11);
                    channel.noteOn(56, 55);
                    zone = Zone.F;
                }
            } else if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER * 16) {

                if (zone != Zone.E) {
                    channel.programChange(1024, 11);
                    channel.noteOn(57, 60);
                    zone = Zone.E;
                }
            } else if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER * 9) {
                if (zone != Zone.D) {
                    channel.programChange(1024, 11);
                    channel.noteOn(58, 65);
                    zone = Zone.D;
                }
            } else if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER * 4) {
                if (zone != Zone.C) {
                    channel.programChange(1024, 11);
                    channel.noteOn(59, 70);
                    zone = Zone.C;
                }
            } else if (dx * dx + dy * dy > TAILLE_PALIER * TAILLE_PALIER) {
                if (zone != Zone.B) {
                    channel.programChange(1024, 11);
                    channel.noteOn(60, 75);
                    zone = Zone.B;
                }
            } else {
                if (zone != Zone.A) {
                    channel.programChange(1024, 11);
                    channel.noteOn(61, 80);
                    zone = Zone.A;
                }
            }
        }
    }

    private void cibleTrouvee(int dx, int dy) throws IOException {
        if (!cibleTrouvee) {
            if ((Math.abs(dx) < 0.5 * TARGET_SIZE) && (Math.abs(dy) < 0.5 * TARGET_SIZE)) {
                playNote(1);
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

    private void playNote(int note) {
        channel.allNotesOff();
        if (channel != null) {

            switch (note) {
                case 1:
                    channel.programChange(1024, 13);
                    channel.noteOn(80, 100);
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

    public Dimension getPreferredSize() {
        System.out.println("Get preferred size... (Thread :" + Thread.currentThread());
        return PREFERRED_SIZE;

    }

    public void sayDirection(int dx, int dy) throws IOException {

        this.audioStreamBas = new AudioStream(new FileInputStream("sounds\\bas.wav"));
        this.audioStreamDroite = new AudioStream(new FileInputStream("sounds\\droite.wav"));
        this.audioStreamEnBasADroite = new AudioStream(new FileInputStream("sounds\\enbasadroite.wav"));
        this.audioStreamEnBasAGauche = new AudioStream(new FileInputStream("sounds\\enbasagauche.wav"));
        this.audioStreamEnHautAGauche = new AudioStream(new FileInputStream("sounds\\enhautagauche.wav"));
        this.audioStreamGauche = new AudioStream(new FileInputStream("sounds\\gauche.wav"));
        this.audioStreamHaut = new AudioStream(new FileInputStream("sounds\\haut.wav"));
        this.audioStreamEnHautADroite = new AudioStream(new FileInputStream("sounds\\enhautadroite.wav"));

        // FONCTION Atan2 permet de transformer un doublet de coordonnees en angle en radian, ici le 0 rad est au sud.
        double angle = Math.atan2((double) dy, (double) dx);
        System.out.println(angle);

        if (angle < -2.512) {
            AudioPlayer.player.start(audioStreamDroite);
        } else if (angle < -1.884) {
            AudioPlayer.player.start(audioStreamEnBasADroite);
        } else if (angle < -1.256) {
            AudioPlayer.player.start(audioStreamBas);
        } else if (angle < -0.628) {
            AudioPlayer.player.start(audioStreamEnBasAGauche);
        } else if (angle < 0.628) {
            AudioPlayer.player.start(audioStreamGauche);
        } else if (angle < 1.256) {
            AudioPlayer.player.start(audioStreamEnHautAGauche);
        } else if (angle < 1.884) {
            AudioPlayer.player.start(audioStreamHaut);
        } else if (angle < 2.512) {
            AudioPlayer.player.start(audioStreamEnHautADroite);
        } else {
            AudioPlayer.player.start(audioStreamDroite);
        };
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
