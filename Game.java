import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *  This class is the main class of the "Brooklyn Murder" application.
 *  "Brooklyn Murder" is a very simple murder mystery game.  Users
 *  have to solve the case of the abduction of their friends and the
 *  murder of a witness. They do so by walking around a fictitious scenery
 *  based on the Brooklyn Nine-nine TV series, solving quests and gathering
 *  evidence. In the end they have to accuse the correct suspect to win.
 *
 *  To play this game, simply launch the JavaFX application.
 *  (On BlueJ right-click the GUI class and select Run JavaFX Application)
 *
 *  This main class creates and initialises all the others except the GUI:
 *  it creates a player loads the game setup and starts the game.  It also
 *  evaluates and executes the commands that implement the game's mechanics.
 *
 * @author Henri Boistel de Belloy
 * @co-author  Michael Kölling and David J. Barnes
 * @version 2018.11.30
 */

public class Game
{
    private Player player;
    private ArrayList<NPC> allCharacters;
    private ArrayList<Room> allRooms;
    private HashMap<String, Quest> allQuests;
    private HashMap<String, Item> allItems;

    /**
     * Create the game, the player and
     * load all of the game's components.
     */
    public Game()
    {
        GameSetup setup = new GameSetup();
        allCharacters = setup.getAllCharacters();
        allRooms = setup.getAllRooms();
        allQuests = setup.getAllQuests();
        allItems = setup.getAllItems();
        player = new Player(allRooms.get(0));
    }

    // ------ implementations of user commands: ------

    /**
     * Prints a help message for the user
     * to be oututted on the GUI.
     * @return The help message.
     */
    public String printHelp() {
        String helpMsg = "";
        helpMsg += ("You're friends have been abducted and you want to get them back. " +
                "Hopefully Brooklyn's 99th precinct can help you with that.\n\n" +
                "Use the actions available to you below to go around the precinct, " +
                "talk to characters, complete quests and solve the murder. You can use " +
                "the notes area on the right if you want to write anything down.");
        return helpMsg;
    }

    /**
     * Allows the player to move to an adjacent room
     * given the name of the next room.
     * @param direction the name of the next room.
     * @return any message to be outputted on the GUI.
     */
    public String goRoom(String direction) {
        String output = "";
        player.changeRoom(direction);
        output += checkForRoomQuest();

        //If player moved to teleporter room, teleport him
        if (player.getCurrentRoom() == allRooms.get(9)) {
            output += "\nYou went to the holding cell. As you did not want to be there you teleported away.\n";
            teleportPlayer();
        }

        //Call end of turn
        nextTurn();

        return output;
    }

    /**
     * Allows the player to go to his previous room.
     * @return any message to be outputted on the GUI.
     */
    public String previousRoom() {
        String output = player.goBack();
        output += checkForRoomQuest();

        //Call end of turn
        nextTurn();

        return output;
    }

    /**
     * Allows the player to talk to NPCs
     * and receive quests from them.
     * @param characterName the name of the
     *           character to interact with.
     * @return the interaction to be outputted on the GUI.
     */
    public String interact(String characterName) {
        String output = "";
        NPC character = player.getCurrentRoom().characterInRoom(characterName);
        output += character.interact();

        //Check if the player has just accepted a quest and updates the player's active quests.
        if (character.getQuest() != null) {
            if (!player.getActiveQuests().contains(character.getQuest()) && character.getQuest().isQuestStarted()) {
                player.addActiveQuest(character.getQuest());
                for (String questName : character.getQuest().getUnlocksQuests().split("/")) {
                    if (!questName.equals("")) {
                        allQuests.get(questName).initiateQuest();
                    }
                }
            }
            //Add to the output any quest complition messages there may be.
            output += checkForFinishedQuest(character.getQuest());
        }
        return output;
    }

    /**
     * Allows the player to give items to NPCs
     * and complete quests
     * @param characterName name of the NC to give to
     * @param itemName name of the item to give
     * @return any output message for the GUI
     */
    public String give(String characterName, String itemName) {
        String output = "";
        NPC character = player.getCurrentRoom().characterInRoom(characterName);

        //Check if the NPC has a quest requiring that item.
        if (character.getQuest() != null && character.getQuest().isQuestStarted()) {
            Item item = character.getQuest().getRequestedItem(itemName);
            if (item != null) {
                int amountToGive = character.getQuest().getAmountWanted(item);

                //If item successfully given to NPC output a success
                // message and checks if a quest was finished.
                if (player.give(item, amountToGive)) {
                    output = ("Successfully gave " + amountToGive + " " +
                            item.getStringName(amountToGive) + " to " + character.getName() + "\n");
                    character.getQuest().removeRequestedItem(item);
                    output += ("\n" + checkForFinishedQuest(character.getQuest()));
                }
            } else {

                //Otherwise output that the NPC does not want this item.
                output = (character.getName() + " does not want any " + itemName + ".\n");
            }
        } else {
            output = (character.getName() + " does not want any " + itemName + ".\n");
        }
        return output;
    }

    /**
     * Look for items and characters in
     * the same room as the player.
     * @return the items and characters in the same
     * room as the player to be shown on the GUI.
     */
    public String look() {
        return player.getCurrentRoom().lookAround();
    }

    /**
     * Allows the player to take items
     * from his current room.
     * @param itemName the item to take.
     * @return any output message for the GUI.
     */
    public String takeItem(String itemName){
        return player.takeItem(itemName);
    }

    /**
     * Inspect an item in the player's inventory.
     * @param itemName the item to inspect.
     * @return The item name and description to
     * be displayed on the GUI.
     */
    public String inspectItem(String itemName){
        return player.inspectItem(itemName);
    }

    // ------ Accessor methods ------

    /**
     * Get the current player.
     * @return the game's player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * get all NPCs in the game.
     * @return an ArrayList of all NPCs in the game
     */
    public ArrayList<NPC> getAllCharacters() {
        return allCharacters;
    }

    /**
     * get the player's inventory in a user friendly display.
     * @return the string display to be outputted on the GUI.
     */
    public String displayInventory(){
        return player.showInventory();
    }

    /**
     * get the player's quests in a user friendly display.
     * @return the string display to be outputted on the GUI.
     */
    public String displayActiveQuests() {
        return player.showActiveQuests();
    }

    // ------ Miscellaneous ------

    /**
     * Checks if the player is ready to accuse a suspect.
     * @return true/false depending on if the player started
     * the last quest allowing him to make an accusation.
     */
    public boolean canAccuse() {
        return (allQuests.get("wuntch1").isQuestStarted()
                && !allQuests.get("holt0").isAccomplished());
    }

    /**
     * Gina guides the player to a random room.
     * @return any message to be outputted on the GUI.
     */
    public String letGinaGuide() {
        String output = "";

        //Check if Gina's first quest is done allowing
        // this functionality to be used by the player.
        if (allQuests.get("gina0").isAccomplished()) {
            if (player.getCurrentRoom().getCharacters().contains(allCharacters.get(3))) {
                output += "Surprise! Gina has guided you to a random room.\n\n";

                //Teleport the player to a random room.
                output += teleportPlayer();
            } else {
                output += "You must be in the same room as Gina to let her guide you.\n";
            }
        } else {
            output += "Talk to Gina after you started the investigation before you can use this command.\n" +
                    "You can find her wandering around.\n";
        }
        return output;
    }

    // ------ Private methods ------

    /**
     * Teleports the player to a random room.
     * @return any message to be outputted on the GUI.
     */
    private String teleportPlayer() {
        Random random = new Random();
        int roomIndex;

        //Avoid teleporting the player to the teleporter room.
        while ((roomIndex = random.nextInt(allRooms.size())) == 9) {
            continue;
        }

        player.changeRoom(allRooms.get(roomIndex));
        String output = checkForRoomQuest();
        nextTurn();
        return output;
    }

    /**
     * Checks if a given quest is accomplished by the player.
     * @param quest the quest to check.
     * @return Any message to be outputted on the GUI
     * including completion messages and potential rewards.
     */
    private String checkForFinishedQuest(Quest quest) {
        String output = "";

        //Check if all tasks for th quest have been done
        if (quest.getTasksLeft() == 0) {
            output = quest.finishQuest();

            //Check for item rewards
            String itemRewards = quest.getItemReward();
            if (itemRewards != "") {
                for (String reward : itemRewards.split("/")) {
                    String itemName = reward.split(" ")[0];
                    Item item = allItems.get(itemName);
                    int amount = Integer.parseInt(reward.split(" ")[1]);
                    output += "\n" + player.addItemToInv(item, amount);
                }
            }

            //Unlock next quests
            String nextQuests = quest.getNextQuests();
            if (nextQuests != "") {
                for (String questName : nextQuests.split("/")) {
                    allQuests.get(questName).initiateQuest();
                }
            }
            player.removeActiveQuest(quest);
        }
        return output;
    }

    /**
     * Check if there is a quest attached
     * to the player's current room.
     * @return Any message to be outputted on the GUI
     * including completion messages and potential rewards.
     */
    private String checkForRoomQuest() {
        String output = "";
        Room room = player.getCurrentRoom();
        if (player.getQuestRooms().keySet().contains(room)) {
            Quest quest = player.getQuestRooms().get(room);
            quest.taskDone();
            player.getQuestRooms().remove(room, quest);
            output += checkForFinishedQuest(quest);
        }
        return output;
    }

    /**
     * Calls for end of turn. Executes any methods
     * that need to be called at the end of each turn.
     */
    private void nextTurn() {
        for (NPC character : allCharacters) {
            character.changeRoom();
        }
    }

    //Game end-state methods

    /**
     * Called when the game is won. Completes the final
     * quests and allows all NPCs to finally move.
     * @return the game completion messages to be
     * displayed to the user.
     */
    public String gameWon() {
        String output = "";

        //Make all characters able to move.
        for (NPC character : allCharacters) {
            character.setCanMove(true);
        }

        //Returns the final quest completion dialogues.
        allQuests.get("wuntch1").taskDone();
        allQuests.get("holt0").taskDone();
        output += checkForFinishedQuest(allQuests.get("wuntch1"));
        output += checkForFinishedQuest(allQuests.get("holt0"));
        return output;
    }

    /**
     * Called when the game is lost.
     * @return the game over message
     * to be shown to the user.
     */
    public String gameOver() {
        String output = "";
        output += ("Unfortunately you have accused the wrong person and because of you an innocent man went to jail.\nYour unforgivable mistake means you can not partake in the case anymore.\nHopefully your friends will still be found.\n");
        output += ("\nGoodbye and thank you for playing.\n(You will be exited from the game once you close this window)");
        return output;
    }
}