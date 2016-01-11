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
    
     public String toString() {
         //  TABLEAU CORRESPONDANCE COULEUR
        return "Brick:: ID = "+this.id+", Color = " + this.color
         + ", Pos = " + this.posX + "," + this.posY + "," + this.posZ
                + ", Rot = "+this.rot ;

    }
    
}
