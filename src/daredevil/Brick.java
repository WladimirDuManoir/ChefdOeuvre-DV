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
    private int posX;
     private int posY;
    private int posZ;
    private int rot;
    private int color;

    public Brick() {
    }
   
    
     public Brick(int id, int posX, int posY, int posZ, int rot, int color) {
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
    
     public int getPosX() {
        return this.posX;
    }
     
      public int getPosY() {
        return this.posY;
    }
      
       public int getPosZ() {
        return this.posZ;
    }
    
    public void setPos(int x,int y,int z) {
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
        return "Brick:: ID = "+this.id;//+", Pos = " + this.posX + "," + this.posY + "," + this.posZ + ", Rot = "+this.rot + ", Color = " + this.color;
    }
    
}
