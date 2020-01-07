import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A3 Solution
 * 
 * @author Matthew Siu
 * @version Nov 9, 2019
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Item inventory;
    private int stamina;
    
        
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        
        inventory = null;
        stamina = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, transporterRoom;
        Item chair, bar, computer, computer2, tree, cookie, beamer, beamer2;
        
        // create some items
        chair = new Item("chair: a wooden chair",5, "chair");
        bar = new Item("bar: a long bar with stools",95.67, "bar");
        computer = new Item("computer: a PC",10, "computer");
        computer2 = new Item("computer2: a Mac",5, "computer2");
        tree = new Item("tree: a fir tree",500.5, "tree");
        cookie = new Item("cookie: a cookie", 65, "cookie");
        beamer = new Beamer("beamer: a beamer", 200, "beamer");
        beamer2 = new Beamer("beamer: a beamer", 200, "beamer");
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporterRoom = new TransporterRoom("The transporter room");
        
        // put items in the rooms
        outside.addItem(tree);
        outside.addItem(tree);
        outside.addItem(cookie);
        outside.addItem(beamer);
        theatre.addItem(chair);
        pub.addItem(bar);
        lab.addItem(chair);
        lab.addItem(computer);
        lab.addItem(chair);
        lab.addItem(computer2);
        office.addItem(chair);
        office.addItem(computer);
        office.addItem(beamer2);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", transporterRoom);
        
        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(getDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")){
            take(command);
        }
        else if (commandWord.equals("drop")){
            drop(command);
        }
        else if (commandWord.equals("charge")){
            charge(command);
        }
        else if (commandWord.equals("fire")){
            fire(command);
        }
        
        
        
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(getDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(getDescription());
        }
    }
    
    /**
     * prints out the description of the room, including player information
     * prints out description of the room
     * prints out whether the player is carrying anything
     * 
     * @return a String including long description and inventory information
     */
    private String getDescription()
    {
        String inventoryStatement = "";
        if(inventory == null){
            inventoryStatement = "You are not carrying anything.";
        }
        else{
            inventoryStatement = "You are carrying a " + inventory.getName() + ".";
        }
        
        return currentRoom.getLongDescription() + "\n" + inventoryStatement;
    
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
            return;
        } 
        if (inventory == null)
        {
            System.out.println("You are not holding anything");
            return;
        }
        if (inventory != null && !inventory.getName().equals("cookie")){
            // if inventory is empty or not a cookie
            System.out.println("You have no food.");
            return;
        }
        
        // output that we have eaten
        System.out.println("You have eaten and are no longer hungry.");
        //remove cookie
        inventory = null;
        stamina = 5;
        
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(getDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(getDescription());
            }
        }
    }
    
    /** 
     * "take" was entered. Check the rest of the command to see
     * whether we take the item.
     * 
     * If already holding something, will not pick up item
     * If not, will pick up item, given it is in the room
     * 
     * 
     * @param command The command to be processed
     */
    private void take(Command command)
    {
        if(command.hasSecondWord()){
            if(inventory != null)
            {
                System.out.println("You are already holding something.");
                return;
            }
            
            //checks for stamina, unless the second word is a cookie
            if(stamina == 0 && !command.getSecondWord().equals("cookie"))
            {   
                System.out.println("You have no stamina, pick up a cookie.");
                return;
            }
                
            //checks the room for the item, gets it if it is in the room
            //otherwise, inventory is set to null
            inventory = currentRoom.getItem(command.getSecondWord());
            if(inventory != null)
            {
                System.out.println("You picked up " + command.getSecondWord() + ".");
                //reduces stamina by one
                stamina--;
                if(inventory.getName().equals("cookie"))
                {
                    //if the item is a cookie, don't remove stamina
                    stamina++;
                }
                return;
            }
            //if inventory is null, the item isn't in the room (getItem returned null)
            System.out.println("That item is not in the room");
            return;        
        }
        //if there is no item specified
        System.out.println("Take what?");
    }
    
    /** 
     * "drop" was entered. Check the rest of the command to see
     * whether we take the item.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command)
    {
        if(command.hasSecondWord()){
            System.out.println("Drop what? (don't need second word)");
        }
        else {
            if(inventory == null){
                System.out.println("You have nothing to drop.");
            }
            else{
                System.out.println("You have dropped " + inventory.getName() + ".");
                //will drop the item into the current room
                currentRoom.addItem(inventory);
                //will remove item from inventory
                inventory = null;
                
            }
        }            
    }
    
    /**
     * "charge" was entered. Check the rest of the command to see what happens
     * 
     * @param command The command to be processed
     */
    private void charge(Command command)
    {
        if(inventory == null)
        {
            //first check, is player holding anything?
            System.out.println("You are not carrying a beamer");
            return;
        }
        if(!inventory.getName().equals("beamer"))
        {
            //second check, is player holding a beamer? If not, enter this if statement
            //note: could probably put together with first check
            System.out.println("You are not carrying a beamer");
            return;
        }        
        if(command.hasSecondWord()){
            //third check, did player put a second word? If so, don't do anything
            System.out.println("Charge what?");
            return;
        }
        //the player is carrying a beamer
        if(((Beamer)inventory).getBeamerRoom() != null)
        {
            //forth check, is the beamer already charged? If so, don't do anything
            System.out.println("Beamer is already charged");
            return;
        }
        //if it passes all these checks, would be holding a non-charged beamer, and just commanded to charge
        //so charge beamer to current room
        ((Beamer)inventory).setBeamerRoom(currentRoom);
        System.out.println("Beamer has been charged");
    }
    
    /**
     * "charge" was entered. Check the rest of the command to see what happens
     * 
     * @param command The command to be processed
     */
    private void fire(Command command)
    {
        if(inventory == null)
        {
            //first check, is player holding anything?
            System.out.println("You are not carrying a beamer");
            return;
        }
        if(!inventory.getName().equals("beamer"))
        {
            //second check, is player holding a beamer? If not, enter this if statement
            //note: could probably put together with first check
            System.out.println("You are not carrying a beamer");
            return;
        }        
        if(command.hasSecondWord()){
            //third check, did player put a second word? If so, don't do anything
            System.out.println("Fire what?");
            return;
        }
        if(((Beamer)inventory).getBeamerRoom() == null)
        {
            //forth check, is the beamer not charged? If so, don't do anything
            System.out.println("Beamer is not charged");
            return;
        }
        
        //If it passes all these checked, the player is carrying a charged beamer
        //so fire the beamer
        System.out.println("Beamer has been fired");
        //Assumption: this movement is also stored in stackback
        previousRoom = currentRoom; // store the previous room
        previousRoomStack.push(currentRoom); // and add to previous room stack
        currentRoom = ((Beamer)inventory).getBeamerRoom(); //set current room to beamerRoom
        ((Beamer)inventory).setBeamerRoom(null); //uncharge the beamer
        System.out.println(getDescription());
    }
    
}
