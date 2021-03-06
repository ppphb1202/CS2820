package production;

import java.awt.Graphics;

class BeltSpace implements Tickable {
    
    /*  class BeltSpace
        @author: Kane Templeton
        Represents an individual space of the belt.
        Belt is made of a strip of these spaces on the floor
    */
    
    private int spaceX,spaceY;
    private Bin bin; //bin of items stored at the belt space
    private Parcel par;
    private boolean visible;
    
    public BeltSpace(int x, int y) {
        spaceX=x;
        spaceY=y;
        bin=null;
        visible=true;
        par=null;
    }
    public boolean isVisible() {return visible;}
    public void setVisible(boolean vis) {visible=vis;}
    
    public void setParcel(Parcel p){par=p;}
    public Parcel getParcel(){return par;}
    public void setBin(Bin b){bin=b;}
    public Bin getBin(){return bin;}

    /*  tick()
        @author: Kane Templeton
        transfer bin to the next belt space
    */
    public void tick() {
        Production.getMaster().getMasterFloor().getBelt().advanceBin(spaceY);
    }

    /*  getX(),getY(),setCoordinates(x,y)
        @author: Kane Templeton
        not necessary for the belt to function, but they
        were necessary to implement the Tickable interface
    */
    public int getX() {return spaceX;}
    public int getY() {return spaceY;}
    public void setCoordinates(int x, int y) {
        spaceX=x;
        spaceY=y;
    }
    
    /*  getID()
        @author: Kane Templeton
        tells the visualizer which image to use
        uses the belt image if there is no bin on the space
        otherwise uses the bin image
    */
    public int getID() {
        if (bin!=null)
            return Constants.BIN_ID;
        if (par!=null)
            return Constants.PARCEL_ID;
        
        return Constants.BELT_ID;
    }
    
    /*  hasBin()
        @author: Kane Templeton
        returns true if the belt space contains a bin of items
    */
    public boolean hasBin() {
        return bin!=null;
    }
    public boolean hasParcel() {
        return par!=null;
    }
    
}

public class Belt implements Tickable {
    
    /*  class Belt
        @author: Kane Templeton
        Moves bins of items
    */
    
    private FloorEntity[] spaces;
    private boolean visible;
    
    public Belt() {
        spaces = new FloorEntity[Production.FLOOR_SIZE];
        int x=0;
        int y=Production.FLOOR_SIZE-1;
        for (int i=0; i<spaces.length; i++) {
            BeltSpace s = new BeltSpace(x,y--);
            spaces[i]=new FloorEntity(s);
        }
        visible=true;
        Production.controls().addEntity(this);
    }
    
    public boolean isVisible() {return visible;}
    public void setVisible(boolean vis) {visible=vis;}
    
    /*  advanceBin(id)
        @author: Kane Templeton
        advance bin to the next space on belt
    */
    public void advanceBin(int id) {
        if (id<0) 
            return;
        if (id==convert(Constants.PACKER_POS)) {
            BeltSpace current = belt(id);
            if (current.hasBin()) {
                Parcel P = new Parcel(current.getBin());
                current.setBin(null);
                current.setParcel(P);
            }   
        }
            
        if (id>=spaces.length-1) { //at final position
            BeltSpace finalSpace = belt(spaces.length-1);
            Parcel P = finalSpace.getParcel();
            if (P!=null) {
                Production.getMaster().ship(P);
                finalSpace.setParcel(null);
            }
            return;
        }
        
        BeltSpace spaceFrom = belt(id);
        BeltSpace spaceTo = belt(id+1);
        if (spaceFrom.hasBin()) {
            spaceTo.setBin(spaceFrom.getBin());
            spaceFrom.setBin(null);
        }
        if (spaceFrom.hasParcel()) {
            spaceTo.setParcel(spaceFrom.getParcel());
            spaceFrom.setParcel(null);
        }
    }
    
    /*  render(g)
        @author: Kane Templeton
        render each space in the belt
    */
    public void render(Graphics g) {
        for (FloorEntity e:spaces)
            e.render(g);
    }
    
    
    public int getX(){return 0;}
    public int getY(){return Production.FLOOR_SIZE-1;}
    public void setCoordinates(int x, int y){} //not necessary
    public int getID(){return spaces[0].getEntityType();} //not necessary
    
    /*  space(y)
        @author: Kane Templeton
        returns the Belt Space at spacecs[y]
        y=19 is the beginning of the belt
    */
    private BeltSpace belt(int y) {
        return (BeltSpace)spaces[y].getTickable();
    }
    
    private int convert(int coord) {
        return spaces.length-coord-1;
    }
    
    public void placeBin(int y,Bin B) {
        belt(convert(y)).setBin(B);
    }
    public boolean isEmpty() {
        for (FloorEntity s:spaces)
            if (((BeltSpace)s.getTickable()).hasBin())
                return false;
        return true;
    }
    
    
    
    
    /*
        tick()
        @author: Kane Templeton
        belt ticks all of its spaces
    */
    public void tick() {
        for (FloorEntity e:spaces)
            e.getTickable().tick();
    }

}
