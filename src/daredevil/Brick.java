/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daredevil;

/**
 *
 * @author ferreisi
 */
public class Brick {
    
    private int id;
    private float posX;
     private float posY;
    private float posZ;
    private int rot;
    private int color;

    public Brick() {
        
    }
   
    
     public Brick(int id, float posX, float posY, float posZ, int rot, int color) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rot = rot;
        this.color = color;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
     public float getPosX() {
        return this.posX;
    }
     
      public float getPosY() {
        return this.posY;
    }
      
       public float getPosZ() {
        return this.posZ;
    }
    
    public void setPos(float x,float y,float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }
    
     public int getRot() {
        return rot;
    }
    
    public void setRot(int r) {
        this.rot = r;
    }
    
     public int getColor() {
        return color;
    }
    
    public void setColor(int c) {
        this.color = c;
    }
    
    // TRANSFORMER CA EN CLASSE ENUM
    public String convertRotToString(int r){
        String s = new String();
        switch (r) {
            case 1:  s = "NORTH";
                     break;
            case 2:  s = "EAST";
                     break;
            case 3:  s = "SOUTH";
                     break;
            case 4:  s = "WEST";
                     break;
            default: System.out.println("gerer erreur ici");
                    break;
            }
                    return s;
        
    }
    
    // TRANSFORMER CA EN CLASSE ENUM
    public String convertColorToString(int c){
        String s = new String();
        switch (c) {
            case 21:  s = "Rouge";
                     break;
            case 1:  s = "Blanc";
                     break;
            case 26:  s = "Noir";
                     break;
            case 24:  s = "Jaune";
                     break;
            case 28:  s = "Vert";
                     break;
            case 23:  s = "Bleu";
                     break;
            case 124:  s = "Violet";
                     break;
            case 221:  s = "Rose";
                     break;
            case 194: s = "Rouge bordeaux";
                break;
                    case 154 :s = "Gris clair";
                        break;
            default: s = "I do not know this color";
                    break;
            }
         return s;
    }
    
     public String toString() {
         //  TABLEAU CORRESPONDANCE COULEUR
        return "Brick:: ID = "+this.id+", Color = " + convertColorToString(this.color)
         + ", Pos = " + this.posX + "," + this.posY + "," + this.posZ
                + ", Rot = "+convertRotToString(this.rot) ;
    }
}
