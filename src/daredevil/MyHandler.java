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
 
    boolean bAge;

 
    //getter method for employee list
    public List<Brick> getBrickList() {
        return brickList;
    }
 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("Brick")) {
            //create a new Brick and put it in Map
            String id = attributes.getValue("designID");
            //initialize Brick object and set id attribute
            brick = new Brick();
            if (id != null) {
                brick.setId(Integer.parseInt(id));
            }
            
            //initialize list
            if (brickList == null)
                brickList = new ArrayList<>();
        };
    }
 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Brick")) {
            //add Employee object to list
            brickList.add(brick);
        }
    }
 
 
//    @Override
//    public void characters(char ch[], int start, int length) throws SAXException {
// 
//        if (bAge) {
//            //age element, set Employee age
//           brick.setAge(Integer.parseInt(new String(ch, start, length)));
//           bAge = false;
//        }
//    }
}
