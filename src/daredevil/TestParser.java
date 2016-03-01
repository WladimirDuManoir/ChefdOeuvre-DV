/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import static daredevil.MainFrame.brickList;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public final class TestParser extends JFrame {

    String fichierParse;
    List<Brick> liste_briques;

    public TestParser() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Daredevil");
        // FlowLayout experimentLayout = new FlowLayout();
        //setLayout(experimentLayout);

        final JLabel label = new JLabel("Aucun fichier");
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

//        final JButton parseFichier = new JButton("Parser le fichier");
//
//        parseFichier.addActionListener(new ActionListener() {
//            public void actionPerformed(final ActionEvent e) {
//                System.out.println("parseFichier ... (Thread :" + Thread.currentThread());
//
//                //PARSING
//                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//                try {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    MyHandler handler = new MyHandler();
//                    saxParser.parse(new File(fichierParse), handler);
//                    
//                    liste_briques = handler.getBrickList();
//                    //print employee information
//                    for (Brick brick : liste_briques) {
//                        System.out.println(brick);
//                        sayBrick(brick);
//                    }
//                } catch (ParserConfigurationException | SAXException | IOException ex) {
//                    ex.printStackTrace();
//                }
//                System.out.println("Parsing complete");
//            }
//
//        });

        final JButton commencerGuidage = new JButton("Commencer le guidage");

        commencerGuidage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                 System.out.println("parseFichier ... (Thread :" + Thread.currentThread());

                //PARSING
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    MyHandler handler = new MyHandler();
                    saxParser.parse(new File(fichierParse), handler);
                    
                    liste_briques = handler.getBrickList();
                    //print employee information
                    for (Brick brick : liste_briques) {
                        System.out.println(brick);
                        sayBrick(brick);
                    }
                } catch (ParserConfigurationException | SAXException | IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Parsing complete");
                
                System.out.println("commencerGuidage ... (Thread :" + Thread.currentThread());
                final JFrame frame = new JFrame();
                
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setResizable(false);
                try {
                    frame.add(new GuidageComponentNext(liste_briques));
                } catch (MidiUnavailableException ex) {
                    Logger.getLogger(TestParser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TestParser.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.pack();
                frame.setVisible(true);

            }
        });

        //CREE GROUPE DE BOUTONS
        final JPanel group = new JPanel();
        group.add(openFichier);
        //group.add(parseFichier);
        group.add(commencerGuidage);
        add(group, BorderLayout.CENTER);

        pack();
    }

    public void sayBrick(Brick brick) {
        String text = brick.toString();
    }

    public static void main(String[] args) {

        System.out.println("Starting application... (Thread :" + Thread.currentThread());

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                System.out.println("Creating GUI... (Thread :" + Thread.currentThread());
                final TestParser frame = new TestParser();
                frame.setVisible(true);
            }
        });
    }
}
