/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
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

    public MainFrame() throws IOException, MidiUnavailableException {
        setTitle("Daredevil");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JLabel label = new JLabel("Bienvenue dans l'application DAREDEVIL");
        add(label, BorderLayout.NORTH);

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

        // ESPACE DE GUIDAGE 8 DIRECTIONS 3 LEVELS OF DISTANCE
        
//        final GuidageComponent_1 canvas = new GuidageComponent_1();
//        add(canvas, BorderLayout.SOUTH);
        
          // ESPACE DE GUIDAGE HORIZONTAL/VERTICAL 3 LEVELS OF DISTANCE
        
//            final GuidageComponent_2 canvas = new GuidageComponent_2();
//            add(canvas, BorderLayout.SOUTH);
            
         // ESPACE DE GUIDAGE PAR LE SON
            
//            final GuidageComponent_3 canvas = new GuidageComponent_3();
//            add(canvas, BorderLayout.SOUTH);
            
         // BON ESPACE DE GUIDAGE PAR LES AXES ET LE SON CONTINU
            
            final GuidageComponent_4 canvas = new GuidageComponent_4();
            add(canvas, BorderLayout.SOUTH);


        pack();
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
