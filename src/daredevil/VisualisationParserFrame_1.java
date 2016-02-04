/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 *
 * @author ferreisi
 */
class VisualisationParserFrame extends JFrame{

        String s = new String();
    
    public VisualisationParserFrame() {
                 setTitle("Visualisation Parser");
                 final VisualisationParserComponent visuParser = new VisualisationParserComponent();
                 add(visuParser,BorderLayout.SOUTH);
                 JButton bouton = new JButton("clickme");
                 add(bouton,BorderLayout.NORTH);
                 s = convertBrickList(MainFrame.brickList);
                 JTextArea label = new JTextArea(s);         
                 add(label,BorderLayout.CENTER);
    
         pack();

    }
    
    private String convertBrickList(List<Brick> brickList){
        String liste= new String();
        for (Brick brick : brickList) {
            liste = liste.concat(brick.toString());
            liste = liste.concat("\n");
        }
        return liste;

    }
    
}
