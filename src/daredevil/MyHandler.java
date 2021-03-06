/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {

    //List to hold Brick objects
    private List<Brick> brickList = null;
    private Brick brick = null;

    //getter method for employee list
    public List<Brick> getBrickList() {
        return brickList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("Part")) {
            //create a new Brick and put it in Map
            String id = attributes.getValue("designID");
            String color = attributes.getValue("materials");

            //initialize Brick object and set id attribute
            brick = new Brick();
            if (id != null) {
                brick.setId(Integer.parseInt(id));
            }
            if (color != null) {
                brick.setColor((int) Float.parseFloat(color.split(",")[0]));
            }

            
            //initialize list
            if (brickList == null) {
                brickList = new ArrayList<>();
            }
        } else if (qName.equalsIgnoreCase("Bone")) {
            String transformation = attributes.getValue("transformation");
            String delims = "[,]";
            String[] tokens = transformation.split(delims);

             //ETABLIR CORRESPONDANCE ROTATION
            //C'est a dire faire une fonction qui selon les valeurs de
            //la matrice de rotation, renvoie Nord, Sud, Est, Ouest
            // Elle est faite dans la classe Brick. A reorganiser...
            brick.setPos(
//                    (Math.round(Float.parseFloat(tokens[9]) * 10)) / 10,
//                    (Math.round((Float.parseFloat(tokens[10]) * 10))) / 10,
//                    (Math.round((Float.parseFloat(tokens[11]) * 10))) / 10);
                    (float) Math.round(Float.parseFloat(tokens[9])*10)/10,
                    (float) Math.round(Float.parseFloat(tokens[10])*10)/10,
                    (float) Math.round(Float.parseFloat(tokens[11])*10)/10);
                  

            if (Integer.parseInt(tokens[0]) == 1) {
                brick.setRot(1);
            } else if (Integer.parseInt(tokens[0]) == -1) {
                brick.setRot(3);
            } else if (Integer.parseInt(tokens[0]) == 0) {
                if (Integer.parseInt(tokens[2]) == -1) {
                    brick.setRot(4);
                } else if (Integer.parseInt(tokens[2]) == 1) {
                    brick.setRot(2);
                }
            }

        };

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Part")) {
            //add Employee object to list
            brickList.add(brick);
        }
    }
}
