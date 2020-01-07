import java.util.Random;
/**
 * Transporter room is a subclass of room
 * 
 * It is a special room where going anywhere will transport player to a random room
 *
 * @author Matthew Siu
 * @version Nov 9, 2019
 */
public class TransporterRoom extends Room
{
    /**
     * Initializes the transporter room as the 
     */
    public TransporterRoom(String description)
    {
        super(description);
        //this adds "random" as an exit, so that it shows up on the exit description of the room
        setExit("random", null);
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     * 
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
    
    /**
     * Choose a random room.
     * 
     * @return The room we end up in upon leaving this one.
     */
    public Room findRandomRoom()
    {
        Random r = new Random();
        //returns a random room from the arraylist
        return getRooms().get(r.nextInt(getRooms().size()));
    }
    
    
}
