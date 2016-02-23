/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author ferreisi
 */
public final class MainFrame extends JFrame {

    String fichierParse;
    static List<Brick> brickList;
    private static int guidage;

    public MainFrame(int guidage) throws IOException, MidiUnavailableException, InterruptedException {
        setTitle("Daredevil");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel label = new JLabel("Bienvenue dans l'application DAREDEVIL");
        // add(label, BorderLayout.NORTH);

        final JButton openFichier = new JButton("Sélectionner fichier");

        openFichier.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.out.println("openFichier ... (Thread :" + Thread.currentThread());
                JFileChooser chooser = new JFileChooser();//création dun nouveau filechosser
                chooser.setApproveButtonText("Choix du fichier..."); //intitulé du bouton
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    label.setText(chooser.getSelectedFile().getAbsolutePath()); //si un fichier est selectionné, récupérer le fichier puis sont path et l'afficher dans le champs de texte
                    fichierParse = chooser.getSelectedFile().getAbsolutePath();
                    System.out.println(fichierParse);
                }

            }

        });

        final JButton parseFichier = new JButton("Parser le fichier");

        parseFichier.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.out.println("parseFichier ... (Thread :" + Thread.currentThread());

                //PARSING
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    MyHandler handler = new MyHandler();
                    saxParser.parse(new File(fichierParse), handler);

                    //Get Brick list
                    brickList = handler.getBrickList();

                } catch (ParserConfigurationException | SAXException | IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Parsing complete");
            }

        });

        final JButton buttonSayBrickList = new JButton("Enoncer liste briques");

        buttonSayBrickList.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.out.println("buttonSayBrickList");
                sayBrickList(brickList);

            }
        });

        final JButton afficherBrickList = new JButton("Afficher liste briques");

        afficherBrickList.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.out.println("afficherBrickList");
                //OUVERTURE NOUVELLE FENETRE
                VisualisationParserFrame visuParserFrame = new VisualisationParserFrame();
                visuParserFrame.setVisible(true);
            }
        });

        //CREE GROUPE DE BOUTONS
        final JPanel group = new JPanel();
        group.add(openFichier);
        group.add(parseFichier);
        group.add(buttonSayBrickList);
        group.add(afficherBrickList);

        //add(group, BorderLayout.CENTER);
        switch (guidage) {
            case 1:        // ESPACE DE GUIDAGE 8 DIRECTIONS 4 LEVELS OF DISTANCE
                add(new GuidageComponent_1());
                break;
            case 2:          // GUIDAGE: horizontal/vertical MODE: son droite/gauche haut/bas  
                add(new GuidageComponent_2());
                break;
            case 3:          // GUIDAGE: la boussole MODE: la game de piano
                add(new GuidageComponent_3());
                break;
            case 4:          // BON ESPACE DE GUIDAGE PAR LES AXES ET LE SON CONTINU
                add(new GuidageComponent_4());
                break;
            case 5:          // BON ESPACE DE GUIDAGE PAR LES AXES ET LE SON CONTINU
                add(new GuidageComponentNext(null));
                break;
            default:
                System.err.println("Guidage numùber ain't right ! ");
                break;
        }
        pack();
    }

    public static int getGuidage() {
        return guidage;
    }

    public static void setGuidage(int guidage) {
        MainFrame.guidage = guidage;
    }

    public void sayBrick(Brick brick) {
        System.out.println("I am saying : " + brick);
        String text = brick.toString();
        Speech freeTTS = new Speech(text);
        freeTTS.speak();
    }

    public void sayBrickList(List<Brick> brickList) {
        for (Brick brick : brickList) {
            System.out.println(brick);
            sayBrick(brick);
        }
    }

}
