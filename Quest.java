import java.util.HashMap;

/**
 * This class creates quests for the Brooklyn Murder GUI based game.
 *
 * Quests are set to an NPC and can be given to players by that NPC.
 * Quest have multiple functionalities including:
 * Item reward upon completion, request items from players,
 * request players to go to certain rooms, return starting,
 * started and completion messages, and unlock other quests
 * when they are started or finished and be unlocked by other quests.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */


public class Quest {
    private String name;
    private String description;
    private String completionMsg;
    private String[] interactions = new String[2];
    private HashMap<Item, Integer> requestedItems;
    private Room requestedRoom;
    private String itemReward = "";
    private boolean isAccomplished;
    private boolean questStarted;
    private int tasksLeft;
    private NPC questGiver;
    private String unlocksQuests;
    private String nextQuests;
    private int unlockCounter; // This quest successfully initiates once the quest counter reaches 0 or under.

    /**
     * Creates a new quest that can be made available to a player through an npc.
     * @param questGiver the NPC that gives the quest.
     * @param name the name of the quest.
     * @param description the quest's description.
     * @param startingInteraction the quest's starting message.
     * @param startedInteraction the quest's started message.
     * @param completionMsg the quest's completion message
     * @param unlocksQuests the quests to unlock once this one is started.
     * @param nextQuests the quests to unlock once this one is finished.
     * @param unlockCounter the amount of previous quests linked to this one
     *                      required to be started/completed for this quest to unlock.
     */
    public Quest(NPC questGiver, String name, String description, String startingInteraction, String startedInteraction, String completionMsg, String unlocksQuests, String nextQuests, int unlockCounter) {
        this.questGiver = questGiver;
        this.name = name;
        this.description = description;
        this.completionMsg = completionMsg;
        this.unlocksQuests = unlocksQuests;
        this.nextQuests = nextQuests;
        this.unlockCounter = unlockCounter;
        interactions[0] = startingInteraction;
        interactions[1] = startedInteraction;
        requestedItems = new HashMap<>();
        questStarted = false;
        tasksLeft = 0;
    }

    /**
     * Returns the quest's name and description
     * when the object is printed.
     * @return the quest's name and description.
     */
    public String toString() {
        return "Quest: " + name + ".\n" + description;
    }

    // ------ accessor methods: ------

    /**
     * Returns the item rewards for this quest.
     * @return the item rewards for this quest.
     */
    public String getItemReward() {
        return itemReward;
    }

    /**
     * Returns whether or not this quest has been completed.
     * @return whether or not this quest has been completed.
     */
    public boolean isAccomplished() {
        return isAccomplished;
    }

    /**
     * Returns the quests to unlock when this quest is started.
     * @return the quests to unlock once this one is started.
     */
    public String getUnlocksQuests() {
        return unlocksQuests;
    }

    /**
     * Returns the quests to unlock once this one has been completed.
     * @return the quests to unlock once this one has been completed.
     */
    public String getNextQuests() {
        return nextQuests;
    }

    /**
     * Returns whether or not the quest has been started.
     * @return whether or not the quest has been started.
     */
    public boolean isQuestStarted() {
        return questStarted;
    }

    /**
     * Returns any requested room for the quest.
     * @return any requested room for the quest.
     */
    public Room getRequestedRoom() {
        return requestedRoom;
    }

    /**
     * Returns the appropriate interaction depending
     * on whether the quest was started or not.
     * @return the appropriate interaction depending
     * on whether the quest was started or not.
     */
    public String getInteraction() {
        if (!questStarted) {
            return interactions[0];
        } else {
            return interactions[1];
        }
    }

    /**
     * Returns the amount of tasks left for the quest to be completed.
     * @return the amount of tasks left for the quest to be completed.
     */
    public int getTasksLeft() {
        return tasksLeft;
    }

    /**
     * Returns an item object given the name of a
     * requested item if the quest is active and
     * requires the item. Returns null otherwise.
     * @param itemName The name of the requested item.
     * @return Returns the item object if found given
     * the item name and if the quest is active.
     */
    public Item getRequestedItem(String itemName) {
        if (questStarted) {
            for (Item item : requestedItems.keySet()) {
                if (item.getName().equals(itemName) || item.getPluralName().equals(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * For a given item, returns the amount of
     * that item required to complete the quest.
     * @param item the requested item.
     * @return amount required by the quest.
     */
    public int getAmountWanted(Item item) {
        if (requestedItems.keySet().contains(item)) {
            return requestedItems.get(item);
        }
        return 0;
    }

    // ------ mutator methods: ------

    /**
     * Set item rewards to give to the completer of the quest.
     * @param reward a String of the rewards in the format:
     *               "itemName itemAmount/itemName itemAmount/..."
     */
    public void setItemReward(String reward) {

        // Must be in format "itemName1 amount/item2Name amount/. ./"
        itemReward = reward;
    }

    /**
     * Set an item the quest requires for completion.
     * @param item the item required.
     * @param amount the amount required.
     */
    public void setRequestedItems(Item item, int amount) {
        requestedItems.put(item, amount);
        tasksLeft++;
    }

    /**
     * Set a room required to visit for completion.
     * @param requestedRoom the room required.
     */
    public void setRequestedRoom(Room requestedRoom) {
        this.requestedRoom = requestedRoom;
        tasksLeft++;
    }

    /**
     * Sets whether or not the quest is started.
     * @param questStarted Boolean to set.
     */
    public void setQuestStarted(boolean questStarted) {
        this.questStarted = questStarted;
    }

    /**
     * Sets the amount of tasks left to complete the quest.
     * @param tasksLeft mount of tasks left.
     */
    public void setTasksLeft(int tasksLeft) {
        this.tasksLeft = tasksLeft;
    }

    // ------ quest mechanics: ------

    /**
     * Decrements the unlock counter and sets the quest to
     * the given NPC if that counter reaches 0.
     */
    public void initiateQuest() {
        unlockCounter--;
        if (unlockCounter <= 0) {
            questGiver.setQuest(this);
        }
    }

    /**
     * Remove an item from the requested item list
     * and completes a task.
     * @param item the item to remove.
     */
    public void removeRequestedItem(Item item) {
        requestedItems.remove(item);
        taskDone();
    }

    /**
     * Method invoked when one of the quest's task has been completed.
     * Decrements the tasks left counter
     */
    public void taskDone() {
        tasksLeft--;
    }

    /**
     * Markes the quest as completed and
     * returns the completion message.
     * @return the completion message.
     */
    public String finishQuest() {
        isAccomplished = true;
        questGiver.setQuest(null);
        return (questGiver + ": " + completionMsg + "\n");
    }
}
