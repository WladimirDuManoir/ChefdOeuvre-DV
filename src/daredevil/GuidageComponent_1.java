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
class GuidageComponent_1 extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1920, 1200);
    private int targetX = 400;
    private int targetY = 400;
    private static final int TARGET_SIZE = 50;
    private int posX = 0;
    private int posY = 0;

    private int nbTargetFound = 0;
    private int nbErreurs = 0;

    Boolean cibleTrouvee = false;

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

    public GuidageComponent_1() throws MidiUnavailableException, IOException, InterruptedException {

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        Thread mainLoop1 = new Thread() {
            public void run() {
                int frequence = 3000;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {

                        try {
                            if (!cibleTrouvee) {
                                sayDistanceFarClose(posX - targetX, posY - targetY);
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

        Thread mainLoop2 = new Thread() {
            public void run() {
                int frequence = 1500;
                Boolean onOff = true;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {
                        System.out.println("debug");
                        if (onOff) {
                            try {
                                if (!cibleTrouvee) {
                                    sayDirection(posX - targetX, posY - targetY);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(GuidageComponent_1.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            onOff = false;
                        } else {
                            onOff = true;
                        }
                        //Remise a zéro du compteur pour le timer
                        startTime = currentTime; // on réinitialise le compteur

                    }

                } while (true);
            }
        };

        mainLoop1.start();
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
            }

        });
    }

    private void repositionnerTarget(int x, int y) throws IOException {

        this.audioStreamNewTarget = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\newtarget.wav"));

        targetX = (int) (0.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.width - 0.5 * TARGET_SIZE));
        targetY = (int) (0.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.height - 0.5 * TARGET_SIZE));

        AudioPlayer.player.start(audioStreamNewTarget);
        cibleTrouvee = false;
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

    private void sayDistanceFarClose(int dx, int dy) throws IOException {

        this.audioStreamLoin = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\loin.wav"));
        this.audioStreamTresLoin = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\tresloin.wav"));
        this.audioStreamTresProche = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\tresproche.wav"));
        this.audioStreamProche = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\proche.wav"));

        if (dx * dx + dy * dy > 600 * 600) {
            AudioPlayer.player.start(audioStreamTresLoin);

        } else if (dx * dx + dy * dy > 400 * 400) {
            AudioPlayer.player.start(audioStreamLoin);

        } else if (dx * dx + dy * dy > 200 * 200) {
            AudioPlayer.player.start(audioStreamProche);

        } else {
            AudioPlayer.player.start(audioStreamTresProche);

        }
    }

    public void sayDirection(int dx, int dy) throws IOException {

        this.audioStreamBas = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\bas.wav"));
        this.audioStreamDroite = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\droite.wav"));
        this.audioStreamEnBasADroite = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\enbasadroite.wav"));
        this.audioStreamEnBasAGauche = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\enbasagauche.wav"));
        this.audioStreamEnHautAGauche = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\enhautagauche.wav"));
        this.audioStreamGauche = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\gauche.wav"));
        this.audioStreamHaut = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\haut.wav"));
        this.audioStreamEnHautADroite = new AudioStream(new FileInputStream("C:\\Users\\ferreisi\\Desktop\\enhautadroite.wav"));

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
