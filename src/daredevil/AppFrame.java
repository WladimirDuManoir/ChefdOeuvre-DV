/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.ButtonGroup;
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
public final class AppFrame extends JFrame{
    
    String fichierParse;
    
    public AppFrame(){
         setTitle("Daredevil");
        // FlowLayout experimentLayout = new FlowLayout();
         //setLayout(experimentLayout);
         
         final JLabel label = new JLabel("Bienvenue dans l'application DAREDEVIL");
         add(label,BorderLayout.NORTH);
         
         final JButton openFichier = new JButton("Sélectionner fichier");
         
         openFichier.addActionListener(new ActionListener(){
             public void actionPerformed(final ActionEvent e){
                    System.out.println("openFichier ... (Thread :"+Thread.currentThread());
                    JFileChooser chooser = new JFileChooser();//création dun nouveau filechosser
                    chooser.setApproveButtonText("Choix du fichier..."); //intitulé du bouton
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    {	
                    label.setText(chooser.getSelectedFile().getAbsolutePath()); //si un fichier est selectionné, récupérer le fichier puis sont path et l'afficher dans le champs de texte
                    fichierParse = chooser.getSelectedFile().getAbsolutePath();
                    System.out.println(fichierParse);
                    }

             }
             
         });
 
         
         final JButton parseFichier = new JButton("Parser le fichier");
         parseFichier.addActionListener(new ActionListener(){
             public void actionPerformed(final ActionEvent e){
                    System.out.println("parseFichier ... (Thread :"+Thread.currentThread());
                    
                    //PARSING
                    
                     SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
         try {
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MyHandler handler = new MyHandler();
        saxParser.parse(new File(fichierParse), handler);
        //Get Employees list
        List<Brick> brickList = handler.getBrickList();
        //print employee information
        for(Brick brick : brickList){
            System.out.println(brick);
            sayBrick(brick);
        }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
        ex.printStackTrace();
        }
         System.out.println("Parsing complete");
             }
             
         });
         
         //CREE GROUPE DE BOUTONS
         final JPanel group = new JPanel();
        group.add(openFichier);
        group.add(parseFichier);
        
        add(group,BorderLayout.CENTER);
         
         // ESPACE DE GUIDAGE
         final AppCanvas canvas = new AppCanvas();
         add(canvas,BorderLayout.SOUTH);
         
         pack();
    }
    
    public void sayBrick(Brick brick){
                    System.out.println("I am saying : "+brick);
                    String text = brick.toString();
                    Speech freeTTS = new Speech(text);
                    freeTTS.speak();

    }
    
}
