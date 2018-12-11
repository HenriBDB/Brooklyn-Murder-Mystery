import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class creates players for the Brooklyn Murder GUI based game.
 *
 * Players can utilise the game's mechanics.
 * They ca move around room, pickup items, give out items,
 * look around, interact with NPCs and complete quests.
 *
 * They posses an inventory containing their items
 * and a list of active quests.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */


public class Player {
    private Room currentRoom;
    private ArrayList<Room> previousRooms;
    private ArrayList<Item> inventory;
    private int inventoryMaxSize = 10;
    private ArrayList<Quest> activeQuests;
    private HashMap<Room, Quest> questRooms;

    public Player(Room startingRoom){
        currentRoom = startingRoom;
        previousRooms = new ArrayList<>();
        inventory = new ArrayList<>();
        activeQuests = new ArrayList<>();
        questRooms = new HashMap<>();
    }

    // ------ accessor methods: ------

    /**
     * Returns the player's active quests.
     * @return the player's active quests.
     */
    public ArrayList<Quest> getActiveQuests() {
        return activeQuests;
    }

    /**
     * Returns the player's current room.
     * @return the player's current room.
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Returns the rooms linked to one of the player's
     * active quests mapped to the corresponding quest.
     * @return the rooms linked to one of the player's
     * active quests mapped to the corresponding quest.
     */
    public HashMap<Room, Quest> getQuestRooms() {
        return questRooms;
    }

    /**
     * Returns the player's inventory.
     * @return the player's inventory as an ArrayList.
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    // ------ Player machanics: ------

    /**
     * Allows the player to change rooms.
     * @param direction the name of the room to go to.
     */
    public void changeRoom(String direction){
        Room nextRoom = currentRoom.getExit(direction);

        //Add the previous room to the player's path
        previousRooms.add(currentRoom);
        currentRoom = nextRoom;
    }

    /**
     * Allows the player to change rooms.
     * @param room the room object the player should go to.
     */
    public void changeRoom(Room room) {

        //Add the previous room to the player's path
        previousRooms.add(currentRoom);
        currentRoom = room;
    }

    /**
     * Allows the player to go back to the
     * room he/she was in before.
     * @return any message to be displayed on the GUI.
     */
    public String goBack() {

        //Check if there is a previous room
        if (previousRooms.size() != 0) {
            currentRoom = previousRooms.get(previousRooms.size()-1);
            previousRooms.remove(previousRooms.size()-1);
            return "";
        } else {
            return "There is no previous room.\n";
        }
    }

    /**
     * Allows the player to inspect an item
     * from his/her inventory.
     * @param itemName the item to inspect.
     * @return the inspection to be shown on the GUI.
     */
    public String inspectItem(String itemName){
        String output = "";
        Item itemToInspect = itemInInv(itemName);
        output += ("Amount in inventory: " + itemToInspect.getAmount()
                + " - Max amount you can hold: " + itemToInspect.getMaxAmount() + "\n");
        output += (itemToInspect.getName() + ": " + itemToInspect.getDescription()+"\n");
        return output;
    }

    /**
     * Allos the player to take an item.
     * @param itemName item to take.
     * @return any message to output on the GUI.
     */
    public String takeItem(String itemName){
        String output = "";
        Item itemToTake = null;
        int amountToAdd = 0;

        //Get the item object and the amount that can be taken
        for (Item item : currentRoom.getItemsInRoom().keySet()) {
            if (item.getName().equals(itemName) || item.getPluralName().equals(itemName)) {
                itemToTake = item;
                amountToAdd = currentRoom.getItemAmount(item);
            }
        }

        //Check if item can be picked up
        if (!itemToTake.isPickable()) {
            output += "This item can not be picked up.\n";
        } else {

            //Try adding item to inventory, take max that can be
            // taken if inventory does not have enough space for all
            if (itemInInv(itemToTake.getName()) != null) {
                output+= itemToTake.take(amountToAdd);
                currentRoom.setItemAmount(itemToTake, itemToTake.getExtraAmount());
            } else if (inventory.size() >= inventoryMaxSize) {
                output += ("Inventory full - Could not take " + itemToTake.getName() + ".\n");
            } else {
                inventory.add(itemToTake);
                output += itemToTake.take(amountToAdd);
                currentRoom.setItemAmount(itemToTake, itemToTake.getExtraAmount());
            }

            //Remove the item from the room if there were no leftovers when taking it
            if (itemToTake.getExtraAmount() == 0) {
                currentRoom.removeItem(itemToTake);
            }
        }
        return output;
    }

    /**
     * Gives an item from the player's inventory and returns
     * whether the item was successfully given or not.
     * @param item Item to give.
     * @param amountToGive Amount of that item to give.
     * @return Returns true if item successfully given.
     */
    public boolean give(Item item, int amountToGive) {
        if (!inventory.contains(item)) {
            return false;
        } else if (item.getAmount() < amountToGive) {
            return false;
        }
        item.remove(amountToGive);
        if (item.getAmount()==0) {
            inventory.remove(item);
        }
        return true;
    }

    // ------ miscellaneous methods: ------

    /**
     * Add the max amount of an item to the inventory
     * given an upper bound the inventory limits.
     * @param item item to add.
     * @param amountToAdd amount to add.
     * @return any message to output to the GUI.
     */
    public String addItemToInv(Item item, int amountToAdd) {
        if (itemInInv(item.getName()) != null) {
            return item.take(amountToAdd);
        } else if (inventory.size() >= inventoryMaxSize) {
            return ("Inventory full - Could not take " + item.getName() + ".\n");
        } else {
            inventory.add(item);
            return item.take(amountToAdd);
        }
    }

    /**
     * Add a quest to the player's active quests.
     * @param quest the quest to add.
     */
    public void addActiveQuest(Quest quest) {
        activeQuests.add(quest);
        if (quest.getRequestedRoom() != null) {
            questRooms.put(quest.getRequestedRoom(), quest);
        }
    }

    /**
     * Remove a quest from a player's active quests.
     * @param quest the quest to remove.
     */
    public void removeActiveQuest(Quest quest) {
        activeQuests.remove(quest);
    }

    /**
     * Returns the players active quests nicely displayed in a string.
     * @return a string of the player's active quests.
     */
    public String showActiveQuests(){
        String quests = "";

        //Check if there are any active quests
        if (activeQuests.size()==0) {
            quests += "You currently have no active quest.";
        } else {
            for (Quest quest : activeQuests) {
                quests += (quest+"\n\n");
            }
        }
        return quests;
    }

    /**
     * Returns the player's inventory nicely displayed in a string.
     * @return a string of the player's inventory.
     */
    public String showInventory(){
        String inventoryDisplay = "";
        //Check if inventory is not empty
        if (inventory.size() == 0){
            inventoryDisplay += "Your inventory is empty.";
        } else {
            for (Item item : inventory) {
                inventoryDisplay += (item.toString()+"\n");
            }
        }
        return inventoryDisplay;
    }

    // ------ private methods: ------

    /**
     * Gets an item object in the inventory given an item name.
     * @param itemName the item's name.
     * @return the item object.
     */
    private Item itemInInv(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equals(itemName) || item.getPluralName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
