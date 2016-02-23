//Ceci est le component qui implemente les prototypes suivants :
//- en direction : direction par système Axe Horizontal puis Axe Vertical
//- en guidage : guidage par 3 seuils de distance en distance absolue (indépendant des axes)
package daredevil;

import static daredevil.MainFrame.brickList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import t2s.son.LecteurTexte;

/**
 *
 * @author ferreisi
 */
class GuidageComponentNext extends JComponent {

    private static final Dimension PREFERRED_SIZE = new Dimension(1900, 1200);
    private static final int FREQUENCE_MIN = 100;
    private static final int SUPPLEMENT_FREQUENCE = 600;
    private static final int DISTANCE_PLOTS_SEUIL = 100;
    private static final int VOLUME_MIN = 0;
    private static final int VOLUME_MAX = 30;
    private static final int PLOT_REF_X = 25;
    private static final int PLOT_REF_Y = 30;

    private int targetX;
    private int targetY;

    private static final int TARGET_SIZE = 15;
    Boolean dyOK = false;
    Boolean dxOK = false;
    private int posX = 0;
    private int posY = 0;
    private int posXbuf = 0;
    private int posYbuf = 0;

    private boolean randomisationPiece = false;
    private int indice_piece = 0;

    private int nbTargetFound = 0;
    private int nbErreurs = 0;

    ArrayList<Caracteristiques_Pieces> liste_caracteristiques_pieces;
    ArrayList<Caracteristiques_Pieces> liste_caracteristiques_pieces_aparcourir;

    private boolean parcoursReconstitue = false;

    private boolean pivoterPiece = false;

    class Caracteristiques_Pieces {

        public int id;
        public int posXplot;
        public int posYplot;
        public boolean orientation;

        public Caracteristiques_Pieces(int id, int xp, int yp, boolean o) {
            this.id = id;
            this.posXplot = xp;
            this.posYplot = yp;
            this.orientation = o;
        }
    }

    // Variables pour le synthetiseur MIDI
    Synthesizer syn = MidiSystem.getSynthesizer();
    MidiChannel channel = syn.getChannels()[0];

    BufferedImage legovertical = null;
    BufferedImage legohorizontal = null;
    BufferedImage legoverticaltransparent = null;
    BufferedImage legohorizontaltransparent = null;
    BufferedImage legofond = null;
    BufferedImage legodepart = null;
    BufferedImage legoarrivee = null;
    BufferedImage legodeparttransparent = null;
    BufferedImage legoarriveetransparent = null;
    BufferedImage legoija = null;
    BufferedImage legoijatransparent = null;

    private boolean boollegovertical = true;
    private boolean booltargetvertical = false;

    private Random random;

    final LecteurTexte lecteur = new LecteurTexte();

    private boolean lectureEnCours = false;

    private int legoCourant = 0;

    public GuidageComponentNext(List<Brick> liste_briques) throws MidiUnavailableException, IOException {

        liste_caracteristiques_pieces_aparcourir = new ArrayList<Caracteristiques_Pieces>();

        liste_caracteristiques_pieces_aparcourir = transformerListBrickEnArrayListCaracteristiquesPieces(liste_briques);

        targetX = 15 * liste_caracteristiques_pieces_aparcourir.get(indice_piece).posXplot;
        targetY = 15 * liste_caracteristiques_pieces_aparcourir.get(indice_piece).posYplot;

        booltargetvertical = liste_caracteristiques_pieces_aparcourir.get(indice_piece).orientation;

        liste_caracteristiques_pieces = new ArrayList<Caracteristiques_Pieces>();

        syn.open();
        final MidiChannel[] mc = syn.getChannels();
        Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
        syn.loadInstrument(instr[33]);

        try {
            legovertical = ImageIO.read(new File("images\\lego.jpg"));
            legohorizontal = ImageIO.read(new File("images\\legohorizontal.jpg"));
            legoverticaltransparent = ImageIO.read(new File("images\\legotransparent.jpg"));
            legohorizontaltransparent = ImageIO.read(new File("images\\legohorizontaltransparent.jpg"));
            legofond = ImageIO.read(new File("images\\legofond.jpg"));
            legodepart = ImageIO.read(new File("images\\legodepart.jpg"));
            legoarriveetransparent = ImageIO.read(new File("images\\legoarriveetransparent.jpg"));
            legodeparttransparent = ImageIO.read(new File("images\\legodeparttransparent.jpg"));
            legoija = ImageIO.read(new File("images\\legoija.jpg"));
        } catch (IOException e) {
        }

        Thread mainLoop = new Thread() {
            public void run() {
                int frequence = 400;
                long startTime = System.currentTimeMillis();
                do {
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime; // calcul du temps écoulé

                    if (elapsedTime > frequence) {

                        //Action principale
                        if (!parcoursReconstitue) {
                            if ((dyOK) && (dxOK)) {
                                playNote(1);
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
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (parcoursReconstitue) {
                    lancerThreadParole("Parcours reconstitué.");
                } else {

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if ((arrondiBrique(e.getX()) == arrondiBrique(targetX)) && ((arrondiBrique(e.getY()) == arrondiBrique(targetY))) && (boollegovertical == booltargetvertical)) {
                            playNote(4);
                            nbTargetFound++;
                            liste_caracteristiques_pieces.add(new Caracteristiques_Pieces(liste_caracteristiques_pieces_aparcourir.get(indice_piece).id, arrondiBrique(targetX), arrondiBrique(targetY), booltargetvertical));
                            try {
                                repositionnerTarget(e.getX(), e.getY());
                            } catch (IOException ex) {
                                Logger.getLogger(GuidageComponent_4.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            repaint();
                        } else if ((Math.abs(e.getX() - targetX - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE) && (Math.abs(e.getY() - targetY - 0.5 * TARGET_SIZE) < 0.5 * TARGET_SIZE) && (boollegovertical != booltargetvertical)) {
                            lancerThreadParole("Faites pivoter la pièce");
                        } else {
                            direDistancePlots(e.getX(), e.getY());
                            nbErreurs++;
                        }
                        printResultats();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        direDistancePlots(e.getX(), e.getY());

                    }
                    repaint();

                }
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
        }
        );

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(final MouseEvent ev) {

                //Update coordonnees de la souris
                posX = ev.getX();
                posY = ev.getY();

                if (arrondiBrique(posX) != arrondiBrique(posXbuf)) {
                    playNote(6);
                }

                if (arrondiBrique(posY) != arrondiBrique(posYbuf)) {
                    playNote(7);
                }

                try {
                    changeDXandDY(ev.getX(), ev.getY());

                } catch (IOException ex) {
                    Logger.getLogger(GuidageComponent_4.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                posXbuf = posX;
                posYbuf = posY;

                repaint();
            }

        });

        setKeyBindings();
        random = new Random();
    }

    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "VK_R");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "VK_LEFT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "VK_RIGHT");

        actionMap.put("VK_R", new KeyAction("VK_R"));
        actionMap.put("VK_LEFT", new KeyAction("VK_LEFT"));
        actionMap.put("VK_RIGHT", new KeyAction("VK_RIGHT"));

    }

    private class KeyAction extends AbstractAction {

        public KeyAction(String actionCommand) {
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvt) {
            if (actionEvt.getActionCommand().toString() == "VK_R") {
                String pivot = new String("");

                if (boollegovertical == booltargetvertical) {
                    pivoterPiece = false;
                } else {
                    pivoterPiece = true;
                }

                if (!pivoterPiece) {
                    pivot = "Faites pivoter la pièce.";
                }

                if (boollegovertical) {
                    lancerThreadParole("Pièce à l'horizontal." + pivot);
                    boollegovertical = false;
                } else {
                    lancerThreadParole("Pièce à la verticale." + pivot);

                    boollegovertical = true;
                }
                playNote(8);
            }

            if (actionEvt.getActionCommand().toString() == "VK_LEFT") {
                changerlego(1);
            }

            if (actionEvt.getActionCommand().toString() == "VK_RIGHT") {
                changerlego(-1);

            }

            repaint();
        }
    }

    private void lancerThreadParole(final String s) {
        Thread parole = new Thread() {
            private volatile Thread currentThread = null;

            public void run() {
                currentThread = Thread.currentThread();
                lectureEnCours = true;
                lecteur.setTexte(s);
                lecteur.playAll();
                lectureEnCours = false;
            }

            public void cancel() {
                currentThread.interrupt();
            }
        };
        if (!lectureEnCours) {
            parole.start();
        }
    }

    private void changerlego(int i) {
        legoCourant += i;
        if (legoCourant == -1) {
            legoCourant = 3;
        }
        if (legoCourant == 4) {
            legoCourant = 0;
        }

        switch (legoCourant) {
            case 0:
                lancerThreadParole("Pièce route");
                break;
            case 1:
                lancerThreadParole("Pièce point de départ");
                break;
            case 2:
                lancerThreadParole("Pièce point d'arrivée");
                break;
            case 3:
                lancerThreadParole("Pièce I J A");
        }
    }

    private int getDistancePlots(int x, int y) {
        int posXplot = arrondiBrique(x) / 15;
        int posYplot = arrondiBrique(y) / 15;
        int targetXplot = arrondiBrique(targetX) / 15;
        int targetYplot = arrondiBrique(targetY) / 15;

        int distanceXplot = targetXplot - posXplot;
        int distanceYplot = targetYplot - posYplot;

        return (Math.abs(distanceXplot) + Math.abs(distanceYplot));
    }

    private void direDistancePlots(int x, int y) {
        int posXplot = arrondiBrique(x) / 15;
        int posYplot = arrondiBrique(y) / 15;
        int targetXplot = arrondiBrique(targetX) / 15;
        int targetYplot = arrondiBrique(targetY) / 15;

        int distanceXplot = targetXplot - posXplot;
        int distanceYplot = targetYplot - posYplot;

        String pivot = new String("");

        if (boollegovertical == booltargetvertical) {
            pivoterPiece = false;
        } else {
            pivoterPiece = true;
        }

        if (pivoterPiece) {
            pivot = "Faites pivoter la pièce.";
        }

        if ((distanceXplot == 0) && (distanceYplot == 0)) {
            lancerThreadParole("Vous êtes sur la cible." + pivot);
        } else if ((distanceXplot == 0) && (distanceYplot != 0)) {

            if (distanceYplot < 0) {
                lancerThreadParole(Integer.toString(-distanceYplot) + " vers le haut." + pivot);
            } else {
                lancerThreadParole(Integer.toString(distanceYplot) + " vers le bas." + pivot);
            }
        } else if ((distanceXplot != 0) && (distanceYplot == 0)) {
            if (distanceXplot < 0) {
                lancerThreadParole(Integer.toString(-distanceXplot) + " vers la gauche." + pivot);
            } else {
                lancerThreadParole(Integer.toString(distanceXplot) + " vers la droite." + pivot);
            }
        } else {

            if (distanceXplot < 0) {
                if (distanceYplot < 0) {
                    lancerThreadParole(Integer.toString(-distanceXplot) + " vers la gauche." + Integer.toString(-distanceYplot) + " vers le haut." + pivot);
                } else {
                    lancerThreadParole(Integer.toString(-distanceXplot) + " vers la gauche." + Integer.toString(distanceYplot) + " vers le bas." + pivot);
                }
            } else {
                if (distanceYplot < 0) {
                    lancerThreadParole(Integer.toString(distanceXplot) + " vers la droite." + Integer.toString(-distanceYplot) + " vers le haut." + pivot);
                } else {
                    lancerThreadParole(Integer.toString(distanceXplot) + " vers la droite." + Integer.toString(distanceYplot) + " vers le bas." + pivot);
                }
            }

        }

    }

    private void printResultats() {
        System.out.println("***");
        System.out.println("Nombre cible trouvées: " + nbTargetFound + ", nombre erreurs :" + nbErreurs);
    }

    private int transformerDistancePlotEnVolume(int d) {
        System.out.println("d : " + d);

        if (d > DISTANCE_PLOTS_SEUIL) {
            return 0;
        } else {

            System.out.println("Volume : " + ((float) (-(float) (d - DISTANCE_PLOTS_SEUIL) / DISTANCE_PLOTS_SEUIL) * VOLUME_MAX) + VOLUME_MIN);
            return ((int) ((float) (-(float) (d - DISTANCE_PLOTS_SEUIL) / DISTANCE_PLOTS_SEUIL) * VOLUME_MAX) + VOLUME_MIN);
        }
    }

    private void playNote(int note) {
        int volume = 50;
        //volume = transformerDistancePlotEnVolume(getDistancePlots(posX, posY));
        channel.allNotesOff();

        if (channel != null) {

            switch (note) {
                case 1:
                    channel.programChange(1024, 13);
                    channel.noteOn(80, 90);
                    break;
                case 2:
                    channel.programChange(0, 9);
                    channel.noteOn(70, 80);
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
                case 6:
                    channel.programChange(10, 115);
                    channel.noteOn(75, volume);
                    break;
                case 7:
                    channel.programChange(10, 115);
                    channel.noteOn(77, volume);
                    break;
                case 8:
                    channel.programChange(10, 116);
                    channel.noteOn(76, volume);
                    break;
            }
        }
    }

    private void repositionnerTarget(int x, int y) throws IOException {
        boolean orientationBuf = true;

        if (randomisationPiece) {

            targetX = arrondiBrique((int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.width - 3 * TARGET_SIZE)));
            targetY = arrondiBrique((int) (1.5 * TARGET_SIZE + Math.random() * (PREFERRED_SIZE.height - 3 * TARGET_SIZE)));

            orientationBuf = booltargetvertical;
            if (random.nextBoolean()) {
                booltargetvertical = true;
            } else {
                booltargetvertical = false;
            }
            if (orientationBuf != booltargetvertical) {
                pivoterPiece = true;
            }

            direDistancePlots(x, y);

            dyOK = false;
            dxOK = false;

        } else {
            indice_piece++;

            if (indice_piece >= liste_caracteristiques_pieces_aparcourir.size()) {
                parcoursReconstitue = true;
                lancerThreadParole("Parcours reconstitué.");
            } else {
                targetX = 15 * liste_caracteristiques_pieces_aparcourir.get(indice_piece).posXplot;
                targetY = 15 * liste_caracteristiques_pieces_aparcourir.get(indice_piece).posYplot;

                orientationBuf = booltargetvertical;

                booltargetvertical = liste_caracteristiques_pieces_aparcourir.get(indice_piece).orientation;
                if (orientationBuf != booltargetvertical) {
                    pivoterPiece = true;
                }

                direDistancePlots(x, y);
                dyOK = false;
                dxOK = false;

                System.out.println("Indice : " + indice_piece);
                System.out.println("Size liste : " + liste_caracteristiques_pieces_aparcourir.size());
            }

        }
    }

    public Dimension getPreferredSize() {
        System.out.println("Get preferred size... (Thread :" + Thread.currentThread());
        return PREFERRED_SIZE;

    }

    private void changeDXandDY(int x, int y) throws IOException {

        if (arrondiBrique(x) == arrondiBrique(targetX)) {
            dxOK = true;
        } else {
            dxOK = false;
        }

        if (arrondiBrique(y) == arrondiBrique(targetY)) {
            dyOK = true;
        } else {
            dyOK = false;
        }

    }

    public int arrondiBrique(int pos) {
        return (pos - (pos % 15));
    }

    public ArrayList<Caracteristiques_Pieces> transformerListBrickEnArrayListCaracteristiquesPieces(List<Brick> brickList) {
        ArrayList<Caracteristiques_Pieces> array = new ArrayList<Caracteristiques_Pieces>();
        Boolean orientation = false;
        int correctionX = 0;
        int correctionY = 0;
        int nouvelId = 0;
        for (Brick brick : brickList) {
            System.out.println("Id brick :" + brick.getId());

            switch (brick.getRot()) {
                case 1:
                    orientation = false;
                    correctionX = 0;
                    correctionY = 0;
                    break;
                case 2:
                    orientation = true;
                    correctionY = +1;
                    correctionX = 0;
                    break;
                case 3:
                    orientation = false;
                    correctionX = -3;
                    correctionY = +1;
                    break;
                case 4:
                    orientation = true;
                    correctionX = -1;
                    correctionY = -2;
                    break;
            }

            switch (brick.getId()) {
                case 3001:
                    // Route basique
                    nouvelId = 0;
                    break;
                case 3003:
                    //TODO IJA 
                    nouvelId = 3;
                    break;
                case 3062:
                    //point de depart
                    nouvelId = 1;
                    break;
                case 4589:
                    // point d'arrivee
                    nouvelId = 2;
                    break;
                case 3040:
                    //TODO Personnage
                    nouvelId = 0;
                    break;
                default:
                    nouvelId = 0;
                    break;

            }
            array.add(new Caracteristiques_Pieces(nouvelId, PLOT_REF_X + correctionX + (int) Math.round(((float) ((brick.getPosX() + 0.4) / 0.8))), PLOT_REF_Y + correctionY + (int) Math.round(((float) ((brick.getPosZ() + 0.4) / 0.8))), orientation));

        }
        return array;
    }

    public void paintComponent(final Graphics g) {
        int iw = legofond.getWidth(this);
        int ih = legofond.getHeight(this);
        for (int x = 0; x < getWidth(); x += iw) {
            for (int y = 0; y < getHeight(); y += ih) {
                g.drawImage(legofond, x, y, iw, ih, this);
            }
        }

        for (int i = 0; i < liste_caracteristiques_pieces_aparcourir.size(); i++) {
            switch (liste_caracteristiques_pieces_aparcourir.get(i).id) {
                case 0:
                    if (liste_caracteristiques_pieces_aparcourir.get(i).orientation == false) {
                        g.drawImage(legohorizontaltransparent, liste_caracteristiques_pieces_aparcourir.get(i).posXplot * 15, liste_caracteristiques_pieces_aparcourir.get(i).posYplot * 15, this);
                    } else {
                        g.drawImage(legoverticaltransparent, liste_caracteristiques_pieces_aparcourir.get(i).posXplot * 15, liste_caracteristiques_pieces_aparcourir.get(i).posYplot * 15, this);
                    }
                    break;
                case 1:
                    g.drawImage(legodeparttransparent, liste_caracteristiques_pieces_aparcourir.get(i).posXplot * 15, liste_caracteristiques_pieces_aparcourir.get(i).posYplot * 15, this);
                    break;
                case 2:
                    g.drawImage(legoarriveetransparent, liste_caracteristiques_pieces_aparcourir.get(i).posXplot * 15, liste_caracteristiques_pieces_aparcourir.get(i).posYplot * 15, this);
                    break;
            }
        }

        for (int i = 0; i < liste_caracteristiques_pieces.size(); i++) {
            switch (liste_caracteristiques_pieces.get(i).id) {
                case 0:
                    if (liste_caracteristiques_pieces.get(i).orientation) {
                        g.drawImage(legovertical, liste_caracteristiques_pieces.get(i).posXplot, liste_caracteristiques_pieces.get(i).posYplot, this);
                    } else {
                        g.drawImage(legohorizontal, liste_caracteristiques_pieces.get(i).posXplot, liste_caracteristiques_pieces.get(i).posYplot, this);
                    }
                    break;
                case 1:
                    g.drawImage(legodeparttransparent, liste_caracteristiques_pieces.get(i).posXplot, liste_caracteristiques_pieces.get(i).posYplot, this);

                    break;
                case 2:
                    g.drawImage(legoarriveetransparent, liste_caracteristiques_pieces.get(i).posXplot, liste_caracteristiques_pieces.get(i).posYplot, this);
                    break;
            }

        }

        if (dyOK && dxOK) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.YELLOW);
        }

        if (!parcoursReconstitue) {
            switch (liste_caracteristiques_pieces_aparcourir.get(indice_piece).id) {
                case 0:
                    if (booltargetvertical) {
                        g.drawLine(targetX - 1, targetY - 1, targetX - 1, targetY + 60);
                        g.drawLine(targetX - 1, targetY - 1, targetX + 30, targetY - 1);
                        g.drawLine(targetX + 30, targetY - 1, targetX + 30, targetY + 60);
                        g.drawLine(targetX - 1, targetY + 60, targetX + 30, targetY + 60);
                    } else {
                        g.drawLine(targetX - 1, targetY - 1, targetX - 1, targetY + 30);
                        g.drawLine(targetX - 1, targetY - 1, targetX + 60, targetY - 1);
                        g.drawLine(targetX + 60, targetY - 1, targetX + 60, targetY + 30);
                        g.drawLine(targetX - 1, targetY + 30, targetX + 60, targetY + 30);
                    }
                    break;
                case 1:
                    g.drawLine(targetX - 1, targetY - 1, targetX - 1, targetY + 15);
                    g.drawLine(targetX - 1, targetY - 1, targetX + 15, targetY - 1);
                    g.drawLine(targetX + 15, targetY - 1, targetX + 15, targetY + 15);
                    g.drawLine(targetX - 1, targetY + 15, targetX + 15, targetY + 15);
                    break;
                case 2:
                    g.drawLine(targetX - 1, targetY - 1, targetX - 1, targetY + 15);
                    g.drawLine(targetX - 1, targetY - 1, targetX + 15, targetY - 1);
                    g.drawLine(targetX + 15, targetY - 1, targetX + 15, targetY + 15);
                    g.drawLine(targetX - 1, targetY + 15, targetX + 15, targetY + 15);
                    break;
                case 3:
                    g.drawLine(targetX - 1, targetY - 1, targetX - 1, targetY + 30);
                    g.drawLine(targetX - 1, targetY - 1, targetX + 30, targetY - 1);
                    g.drawLine(targetX + 30, targetY - 1, targetX + 30, targetY + 30);
                    g.drawLine(targetX - 1, targetY + 30, targetX + 30, targetY + 30);
                    break;
            }

        }

        switch (legoCourant) {
            case 0:
                if (boollegovertical) {
                    g.drawImage(legovertical, arrondiBrique(posX), arrondiBrique(posY), 30, 60, null);
                } else {
                    g.drawImage(legohorizontal, arrondiBrique(posX), arrondiBrique(posY), 60, 30, null);
                }
                break;
            case 1:
                g.drawImage(legodepart, arrondiBrique(posX), arrondiBrique(posY), 15, 15, this);
                break;
            case 2:
                g.drawImage(legoarrivee, arrondiBrique(posX), arrondiBrique(posY), 15, 15, this);
                break;
        }

        g.setColor(Color.YELLOW);
        g.drawString("Nombre de briques trouvées : " + Integer.toString(nbTargetFound), 10, 15);
        g.drawString("Nombre d'erreurs : " + Integer.toString(nbErreurs), 10, 30);
    }
}
