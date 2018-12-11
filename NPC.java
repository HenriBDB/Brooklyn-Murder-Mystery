import java.util.*;

/**
 * This class creates NPCs for the Brooklyn Murder GUI based game.
 *
 * NPCs can interact with a player and give out quests.
 * For their quest, they can receive items.
 *
 * NPCs can wander around in the game if their boolean
 * canMove is set to true.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */


public class NPC {
    private String name;
    private Room currentRoom;
    private Quest currentQuest;
    private boolean canMove;
    private String [] defaultInteractions;

    /**
     * Creates an NPC object with a name and a
     * starting point on the map.
     * @param name the NPC's name.
     * @param startingRoom the NPC's starting room.
     */
    public NPC(String name, Room startingRoom) {
        this.name = name;
        currentRoom = startingRoom;
        currentRoom.addCharacter(this);
        currentQuest = null;
        canMove = false;
    }

    /**
     * Prints out the name of the NPC whenever
     * the object is being printed.
     * @return the nam of the NPC.
     */
    public String toString() {
        return name;
    }

    // ------ mutator methods: ------

    /**
     * Set the NPC's default interactions.
     * @param interactions String of default
     * interactions each separated by a '/'.
     */
    public void setDefaultInteractions(String interactions) {
        this.defaultInteractions = interactions.split("/");
    }

    /**
     * Sets whether an NPC can move around or not.
     * @param canMove true/false whether the NPC can move or not.
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * Sets the quest for the NPC.
     * @param quest the quest to give to the NPC.
     */
    public void setQuest(Quest quest) {
        currentQuest = quest;
    }

    // ------ accessor methods: ------

    /**
     * Get the NPC's name.
     * @return the NPC's name.
     */
    public String getName(){
        return name;
    }

    /**
     * Get the NPC's current quest.
     * @return any active quest the NPC has to offer.
     */
    public Quest getQuest() {
        return currentQuest;
    }

    // ------ NPC mechanics: ------

    /**
     * Allows the NPC to change rooms and wander around.
     */
    public void changeRoom() {

        //Check if the NPC can move
        if (canMove) {

            //Create a 1/3 chance for the NPC to move
            Random rand = new Random();
            if (rand.nextInt(3) == 0) {

                //Get a random adjacent room to move to
                HashMap<String, Room> possibleExits = currentRoom.getAllExits();
                Object[] possibleDirections = possibleExits.keySet().toArray();
                int directionIndex = rand.nextInt(possibleDirections.length);

                // Remove character from previous room and add it to new room's
                // character list
                currentRoom.removeCharacter(this);
                currentRoom = possibleExits.get(possibleDirections[directionIndex]);
                currentRoom.addCharacter(this);
            }
        }
    }

    /**
     * Allows interactions with the NPC.
     * @return the String result of the interaction.
     */
    public String interact() {
        String output = "";
        Random rand = new Random();

        //Return a default interaction if NPC has no quest to offer
        if (currentQuest == null) {
            output += ( name + ": " + defaultInteractions[rand.nextInt(defaultInteractions.length)]+"\n");
        }

        //Return its quest's interaction otherwise
        else if (currentQuest.getTasksLeft() != 0) {
            output += (name + ": "+currentQuest.getInteraction()+"\n");
            if (!currentQuest.isQuestStarted()) {
                output += acceptQuest(currentQuest);
            }
        }
        return output;
    }

    /**
     * Asks the user if he wants to start the quest
     * of the NPC he is interacting with.
     * @param quest The quest to start.
     * @return The output message to display on the GUI.
     */
    private String acceptQuest(Quest quest) {
        String message = "";
        message += quest.getInteraction();
        message += "\n\n";
        message += "Do you want to accept this quest?";

        if (ConfirmWindow.display("Quest Request", message)) {
            quest.setQuestStarted(true);
            currentQuest = quest;
            return (name + ": Thank you for accepting my quest.\n");
        } else {
            return (name + ": You have declined this quest.\nWhenever you are ready to accept it, just talk to " + name + ".\n");
        }

    }
}
