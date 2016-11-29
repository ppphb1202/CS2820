package production;

import java.util.LinkedList;


public class Bin{
     /* class Bin
        @author: Kane Templeton
        bin of items to be transferred on belt
     */
    
    private LinkedList<Item> itemsContained;
    
    public Bin() {
        itemsContained = new LinkedList<>();
    }
    
    public void addItem(Item I) {
        itemsContained.addLast(I);
    }
    
    public Item removeItem() {
        return itemsContained.removeFirst();
    }
    
    public int numberOfItems() {return itemsContained.size();}
    
    public LinkedList<Item> containedItems(){return itemsContained;}
    

}
