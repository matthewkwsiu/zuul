
/**
 * beamer is a subclass of Item
 * 
 * @author Matthew Siu
 * @version Nov 9, 2019
 */
public class Beamer extends Item
{
    //stores the charged beamer room
    private Room beamerRoom;
    
    /**
     * constructor of beamer. Initializes item constructor and beamerRoom
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Beamer(String description, double weight, String name)
    {
        super( description,  weight,  name);
        beamerRoom = null;
    }
    
    /**
     * gets the current charged room
     * 
     * @return the room the beamer was charged in
     */
    public Room getBeamerRoom()
    {
        return beamerRoom;
    }
    
    /**
     * sets the current room as a charge
     * 
     * @param room the charged room
     */
    public void setBeamerRoom(Room room)
    {
        beamerRoom = room;
    }
}
