import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class Room {

    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<Item, Integer> items;
    private ArrayList<NPC> characters;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
        characters = new ArrayList<>();
    }

    // ------ Accessor methods: ------

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     * @return A description of this room
     */
    public String getDescription() {
        return "You are " + description + ".\n";
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction".
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    /**
     * Returns all possible exits for this room.
     * @return all possible exits for this room.
     */
    public HashMap<String, Room> getAllExits() {
        return exits;
    }

    /**
     * Returns all characters in the room.
     * @return all characters in the room.
     */
    public ArrayList<NPC> getCharacters() {
        return characters;
    }

    /**
     * Returns the items in the room.
     * @return Items in the room in the form of a
     * dictionary with the name as a key.
     */
    public HashMap<Item, Integer> getItemsInRoom(){
        return items;
    }

    /**
     * Returns the amount of a given item in the room.
     * @param item the item in the room.
     * @return the amount of a given item in the room.
     */
    public int getItemAmount(Item item) {
        return items.get(item);
    }

    // ------ mutator methods: ------

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Changes the amount present in the room of a given item.
     * @param item item in the room.
     * @param newAmount new amount to set.
     */
    public void setItemAmount(Item item, int newAmount) {
        items.replace(item, newAmount);
    }

    // ------ room mechanics: ------

    /**
     * Adds a given item to the room.
     * @param item The item to add to the room.
     * @param amount The amount of this item to add to the room.
     */
    public void addItem(Item item, int amount){
        items.put(item, amount);
    }

    /**
     * Remove a given item from the room.
     * @param item the item to remove
     */
    public void removeItem(Item item){
        items.remove(item);
    }

    /**
     * Add an NPC to the list of characters in the room.
     * @param character NPC to add to the room.
     */
    public void addCharacter(NPC character) {
        characters.add(character);
    }

    /**
     * Remove an NPC from the list of characters in the room.
     * @param character NPC to remove from the room.
     */
    public void removeCharacter(NPC character) {
        characters.remove(character);
    }

    /**
     * Returns the NPC object of a character
     * in the room given his name.
     * @param characterName the name of the NPC.
     * @return the NPC object.
     */
    public NPC characterInRoom(String characterName) {
        for (NPC character : characters) {
            if (character.getName().toLowerCase().equals(characterName)) {
                return character;
            }
        }
        return null;
    }

    /**
     * Returns a string containing the characters and items in the room.
     * @return the characters and items in the room as a string.
     */
    public String lookAround() {
        String output = "";
        // Show characters in the room
        if (characters.size() == 1) {
            output += (characters.get(0) + " is in the room.\n");
        } else if (characters.size()>1) {
            output += (characters.get(0));
            for (int i = 1; i<characters.size(); i++) {
                output += (", " + characters.get(i));
            }
            output += " are in the room.\n";
        } else {
            output += "You are alone in the room.\n";
        }

        // Show objects in the room
        if (items.size() == 0) {
            output += "There are no items you can take in this room.\n";
        } else {
            output += "You see: ";
            for (Item item : items.keySet()) {
                output += (items.get(item)+" "+item.getName()+"\n");
            }
        }
        return output;
    }
}