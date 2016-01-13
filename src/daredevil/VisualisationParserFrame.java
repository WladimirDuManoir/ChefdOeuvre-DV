/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author ferreisi
 */
class VisualisationParserFrame extends JFrame{

    public VisualisationParserFrame() {
                 setTitle("Visualisation Parser");
                 final VisualisationParserComponent visuParser = new VisualisationParserComponent();
                 add(visuParser);
                 JButton bouton = new JButton("clickme");
                 add(bouton);
    
         pack();

    }
    
    
}
