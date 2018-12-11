import java.util.ArrayList;
import java.util.HashMap;

/**
 * The GameSetup class initialises all static aspects of the game.
 * It creates all the NPCs, rooms, quests and items and stores them.
 * A getter method exists for each compilation of rooms, quests,
 * items or NPCs.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class GameSetup {
    private ArrayList<NPC> allCharacters;
    private ArrayList<Room> allRooms;
    private HashMap<String, Quest> allQuests;
    private HashMap<String, Item> allItems;

    private Item letter,
            hotdog,
            toycar,
            binder,
            yoghurt,
            axe;

    private NPC charles,
            jake,
            gina,
            holt,
            amy,
            rosa,
            hitchcock,
            scully,
            policeOfficer,
            wuntch,
            terry;

    private Room bullpen,
            breakRoom,
            interrogationRoom,
            evidenceRoom,
            kitchen,
            holtOffice,
            street,
            copyRoom,
            holdingCell,
            precinctLobby;

    public GameSetup() {
        allItems = loadItems();
        allRooms = loadRooms();
        allCharacters = loadCharacters();
        allQuests = loadQuests();
    }

    /**
     * Creates and loads all items in the game.
     * @return A HashMap(String itemName, Item item)
     *     containing every item in the game
     */
    public HashMap<String, Item> loadItems() {
        HashMap<String, Item> itemList = new HashMap<>();

        letter = new Item("letter", "letters", "Contains information on Holt you don't want to know about.", 0, 1);
        itemList.put("letter", letter);

        hotdog = new Item("hotdog", "hotdogs", "Can feed a hungry person.", 0, 2);
        itemList.put("hotdog", hotdog);

        toycar = new Item("toycar", "toycars", "Most likely belongs to a child or infant.", 0, 1);
        itemList.put("toycar", toycar);

        binder = new Item("binder", "binders", "Contains boot camp images", 0, 1);
        itemList.put("binder", binder);

        yoghurt = new Item("yoghurt", "yoghurts", "Honey flavored greek yoghurt; delicious!", 0, 1);
        itemList.put("yoghurt", yoghurt);

        axe = new Item("axe", "axes", "", 0, 1);
        axe.setPickable(false);
        itemList.put("axe", axe);

        return itemList;
    }

    /**
     * Creates, sets up and loads all rooms in the game
     * @return an ArrayList(Room room)
     * of all rooms in the game.
     */
    public ArrayList<Room> loadRooms() {
        ArrayList<Room> roomList = new ArrayList<>();

        street = new Room("in the street. You see a stand that seems to sell delicious hot dogs");
        roomList.add(street);
        bullpen = new Room("in the bullpen. There are food leftovers on Hitchcock's and Scully's desks, a binder on Amy's desk and an axe on Rosa's");
        roomList.add(bullpen);
        breakRoom = new Room("in the break room. The worn out sofas and old tables make it look really comfortable");
        roomList.add(breakRoom);
        interrogationRoom = new Room("in the interrogation room. A corpse lies in plain sight.\nBeside her you see a yoghurt, a toycar and a binder.");
        roomList.add(interrogationRoom);
        evidenceRoom = new Room("in the evidence room. Please no public display of affection in here; you could kill a Captain");
        roomList.add(evidenceRoom);
        kitchen = new Room("in the kitchen. You can microwave your breakfast here");
        roomList.add(kitchen);
        holtOffice = new Room("in Captain Holt's office. Captain Holt might have something for you");
        roomList.add(holtOffice);
        precinctLobby = new Room("in Brooklyn's 99th precinct. You are now in the lobby");
        roomList.add(precinctLobby);
        copyRoom = new Room("in the copy room. Terry just fixed the japanese high-tech printer");
        roomList.add(copyRoom);
        holdingCell = new Room("in the holding cell amongst locked up perpetrators");
        roomList.add(holdingCell);

        bullpen.setExit("evidence room", evidenceRoom);
        bullpen.setExit("copy room", copyRoom);
        bullpen.setExit("holt's office", holtOffice);
        bullpen.setExit("break room", breakRoom);
        bullpen.setExit("holding cell", holdingCell);
        bullpen.setExit("precinct lobby", precinctLobby);
        bullpen.setExit("kitchen", kitchen);
        bullpen.setExit("interrogation room", interrogationRoom);
        bullpen.addItem(axe, 1);

        copyRoom.setExit("bullpen", bullpen);

        holtOffice.setExit("bullpen", bullpen);

        breakRoom.setExit("bullpen", bullpen);

        holdingCell.setExit("bullpen", bullpen);


        kitchen.setExit("bullpen", bullpen);

        interrogationRoom.setExit("bullpen", bullpen);
        interrogationRoom.addItem(binder, 1);
        interrogationRoom.addItem(yoghurt, 1);
        interrogationRoom.addItem(toycar, 1);

        evidenceRoom.setExit("bullpen", bullpen);

        street.setExit("precinct lobby", precinctLobby);
        street.addItem(hotdog, 6);

        precinctLobby.setExit("street", street);
        precinctLobby.setExit("bullpen", bullpen);

        return roomList;
    }

    /**
     * Creates, sets up and loads all NPCs from the game
     * @return ArrayList(NPC npc)
     * containing all NPCs in the game.
     */
    public ArrayList<NPC> loadCharacters() {
        ArrayList<NPC> characterList = new ArrayList<>();

        holt = new NPC("Holt", holtOffice);
        characterList.add(holt);
        holt.setDefaultInteractions("Actually, I was calling you a goat. You goat./Coat, coat, jacket, coat. Is this a police precinct or a Turkish bazaar?/You grackle./Wuntch just got served./Madeline Wuntch. Good to see you but,\nif you're here, who's guarding Hades?/I have an extra graphing calculator I could leave in there for the children.");

        amy = new NPC("Amy", evidenceRoom);
        characterList.add(amy);
        amy.setDefaultInteractions("This one says Die Pig. And worst of all, they didn't put the comma between die and pig./It'll cheer the captain up. He'll be over the moon. He may even lean back in his chair and nod slightly./Sergeant, I'm learning so much. We both have blue hand towels. We have the same microwave. And, once I buy coasters made out of geodes, we'll both have those./Hello, friends. Who here would like to see a presentation of crime statistics as a function of demographics and time?/Who wants to see a picture of a dead body?");

        charles = new NPC("Charles", bullpen);
        characterList.add(charles);
        charles.setDefaultInteractions("What about me? What if something happens to Jake, and he never gets to meet my baby?\nI don't want to hang out with some stupid baby who's never met Jake./The guy was from Canada, said it was probably his fault for getting robbed, and apologized for wasting my time./My Nana always said, 'Bad news first because the good news is probably a lie.'\nFun fact: she made me cry a lot./He should already think you're great. Like with my dad.\nHe doesn't need me to prove to him that Jake Peralta's the best cop in the precinct, he knows it.");

        gina = new NPC("Gina", breakRoom);
        characterList.add(gina);
        gina.setDefaultInteractions("Hi, Gina Linetti. The human form of the 100 emoji./My mother cried on the day I was born because she knew she would never be better than me./I feel like I’m the Paris of people./As everyone knows, my spirit animal is nature’s greatest predator, the wolf./What? The only thing I’m not good at is modesty, because I’m great at it./I’d describe the workflow today as dismal with a tiny dash of pathetic.");

        hitchcock = new NPC("Hitchcock", kitchen);
        characterList.add(hitchcock);
        hitchcock.setDefaultInteractions("No, I think it's a pizza./Finally warming up breakfast./That's it I'm licking all these bagels./Every Sunday after church, Scully and I have dinner together at Wing Sluts.\nLast night Scully said he was sick so I went by myself.\nGuess who was there healthy as a clam.\nMr liar and his new tart Cyndi Schatz./Not to brag but Scully and I have a combined total of 14 arrests./We're a package deal everyone knows that.");

        jake = new NPC("Jake", bullpen);
        characterList.add(jake);
        jake.setDefaultInteractions("'I'm so confused I don't know what's happening right now': title of your sex tape./Great, I'd like your $8-Est bottle of wine, please./I swear, these perps are so stupid. I'd make a better criminal than any of 'em./Yeah. Okay, here it goes. Ames, I love you. I love how smart you are.\nI love how beautiful you are. I love your face, and I love your butt.\nI should've written this down first.");

        policeOfficer = new NPC("Officer", interrogationRoom);
        characterList.add(policeOfficer);
        policeOfficer.setDefaultInteractions("Reporting for duty./I haven't found any new evidence./Maybe Chief Wuntch has more information.");

        rosa = new NPC("Rosa", evidenceRoom);
        characterList.add(rosa);
        rosa.setDefaultInteractions("What kind of woman doesn't have an axe?/I don't ask people out. I just tell them where w're going./I didn’t understand why people care so much about dumb dogs until I got a dumb dog myself./I hate small talk. Let’s drink in silence./You can hate people and still think they’re hot.");

        scully = new NPC("Scully", kitchen);
        characterList.add(scully);
        scully.setDefaultInteractions("That's because it's all wart./I think it's a cookie./What's the time? 10am? No wonder I'm starving./We are turnips./Chips don't count. My doctor said they had zero nutritional value.");

        terry = new NPC("Terry", copyRoom);
        characterList.add(terry);
        terry.setDefaultInteractions("I’m playing Kwazy Cupcakes, I’m hydrated as hell, and I’m listening to Sheryl Crow. I’ve got my own party going on./I feel like a proud mama hen whose baby chicks have learned to fly!/We’re having a fancy tea party. I am the king of Origami napkins./I care about my friends. Now eat your carrots, or I’ll rip your tiny head off./Correction, you bring Vacation Terry, and he is no man's boss. When the slippers are filled, Terry is chilled./I'm a detective. I will detect.");

        wuntch = new NPC("Wuntch", interrogationRoom);
        characterList.add(wuntch);
        wuntch.setDefaultInteractions("Raymond, I don't think we need to say anything./Raymond, always a pleasure to call on a vanquished foe./How do I know you're not wearing a wire?\nI need to pat you down./I'm surprised you didn't see what was going on in there./Hello Raymond, you're looking old and sickly./I love my life");

        gina.setCanMove(true);

        return characterList;
    }

    /**
     * Creates, sets up and loads all quests from the game
     * @return Returns a HashMap(String codeName, Quest quest)
     * for each quest in the game
     */
    public HashMap<String, Quest> loadQuests() {
        HashMap<String, Quest> questList = new HashMap<>();

        Quest holt0 = new Quest(holt, "Solving the case", "You need to find out who abducted your friends and murdered the witness.", "Hello there. I am Captain Holt and I am in charge of the case relating to your friends' abduction. Would you like to help me with the case? Talk to me again if you accept to get started.", "Have you found the culprit?", "Congratulations! You found the murderer!\nOur precinct is now safer than ever.", "holt1/gina0", "", 0);
        holt0.setTasksLeft(1);
        questList.put("holt0", holt0);

        Quest holt1 = new Quest(holt, "Go to the murder Scene", "Investigate the crime scene by going to the interrogation room.", "The murder took place in the interrogation room. You should head there to start your interrogation.", "Have you gone to the interrogation room already?", "You should start your investigation here on the crime scene. Make sure to look around for clues and note everything down.", "", "holt0/wuntch0/jake0/scully0", 1);
        holt1.setRequestedRoom(interrogationRoom);
        questList.put("holt1", holt1);

        Quest amy0 = new Quest(amy, "Find Amy's binder", "Amy has lost her binder and wants you to find it for her.", "Hello, my name is Amy. Would you be so kind as to do me a favor? While cleaning as usual my bootcamp binders, one of them disappeared from my desk. It holds so many dear memories. Could you please find it for me.", "Have you found my binder yet?", "Yes! That's the one. Wait… it's covered in blood. This will be so hard to clean!", "", "wuntch1", 1);
        amy0.setRequestedItems(binder, 1);
        questList.put("amy0", amy0);

        Quest gina0 = new Quest(gina, "Let Gina guide you", "", "", "", "Hey! Want a little of Gina fun? Type 'let gina guide me' when you are in the same room as me for a little surprise.", "", "", 1);
        questList.put("gina0", gina0);

        Quest jake0 = new Quest(jake, "Find one of Nikolaj's toys", "Bring Jake one of Charles' son's toys so he can prove that Charles is losing them everywhere.", "Charles you always leave your stuff lying around.\nIt's not because you have a child now that you can litter the precinct with toys.\nCharles: That's not true I don't leave Nikolaj's toys lying around.\nJake: Hey you there, if you find one of Nikolaj's toys for me I'll help you with your case.", "Have you found one of Nikolaj's toys yet?", "Thank you. With this I can finally prove Charles wrong!\nHere's one of Holt's letter I found in the bin.\nTrust me you don't want to read it.\nI'm sure you could get some valuable information in exchange for it.", "", "wuntch1", 1);
        jake0.setRequestedItems(toycar, 1);
        jake0.setItemReward("letter 1");
        questList.put("jake0", jake0);

        Quest scully0 = new Quest(scully, "Get Scully two hotdog", "Find two hotdogs for Scully and Hitchcock so they don't have to leave their chaors to have lunch.", "I'm starving, Hitchcock wanna go get some hotdogs?\nHitchcock: Sure but can we do it without getting out of our chairs?\nScully: You over there, would you mind getting us hotdogs?", "What time is it? 10am? No wonder I'm starving!", "That was a good hotdog. Let me tell you something.\nI think Terry did it.\nHe's been acting weirdly recently like observing the precinct with binoculars.", "", "terry0", 1);
        scully0.setRequestedItems(hotdog, 2);
        questList.put("scully0", scully0);

        Quest terry0 = new Quest(terry, "Find Terry's yoghurt", "Find one of Terry's missing yoghurt so he can move on with his investigation on the yoghurt thief.", "I will catch that thief and rip his tiny head off.\nCan you believe that someone has been stealing my favourite honey flavoured greek yoghurts?\nI have been scouting the whole precinct with these binoculars from the copy room but haven't found anything yet.\nWould you mind helping me with my investigation by finding one of the stolen pots for me?", "Have you found a stolen yoghurt yet?", "Honey flavored greek yoghurt: that's the right one.\nThe thief will pay for his wrong doing!", "", "wuntch1", 1);
        terry0.setRequestedItems(yoghurt, 1);
        questList.put("terry0", terry0);

        Quest wuntch0 = new Quest(wuntch, "Get dirt on Holt", "Chief Wuntch will only help you if you succeed on acquiring compromising material on Captain Holt.", "Hello, I am Chief Wuntch. I oversee the crime scene and all details relating to it get reported to me.\nMaybe I could give you a few if you helped me. All I need is a good story I can use against Holt.", "Have you found any compromising material on Captain Holt yet?", "Thank you for this. As promise here are some details about the murder:\nwe know victim was killed by someone from the precinct and with a flat object like a cricket bat or a book.", "", "amy0", 1);
        wuntch0.setRequestedItems(letter, 1);
        questList.put("wuntch0", wuntch0);

        Quest wuntch1 = new Quest(wuntch, "Make an accusation.", "It is time you officially announce who you believe is the culprit.", "You should by now have build up a solid case and have an idea of who the culprit is.\nUse the 'accuse' action below to accuse the person you believe abducted your friends.\nBe careful, you only have one accusation.", "Use the 'accuse' action to accuse the person you believe abducted your friends.", "Charles Boyle you are under arrest for murder and abduction. Everything you say from now one can and will be held against you.\nCharles: I didn't want it to go down like this. I was just jealous that Jake was being friendly to other people, it made me feel like a side friend. Abduction and murder of a witness was not what I had in mind. I messed up...\nWuntch: I will make sure to use this confession in court.", "", "", 3);
        wuntch1.setTasksLeft(1);
        questList.put("wuntch1", wuntch1);

        holt0.initiateQuest();

        return questList;
    }

    /**
     * Used to get all loaded rooms
     * @return ArrayList(Room room)
     * containing all loaded rooms
     */
    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    /**
     * Used to get all loaded NPCs
     * @return ArrayList(NPC npc)
     * containing all loaded NPCs
     */
    public ArrayList<NPC> getAllCharacters() {
        return allCharacters;
    }

    /**
     * Used to get all loaded quests
     * @return HashMap(String codeName, Quest quest)
     * containing all loaded quest
     */
    public HashMap<String, Quest> getAllQuests() {
        return allQuests;
    }

    /**
     * Used to get all loaded items
     * @return ArrayList(Item item)
     * containing all loaded items
     */
    public HashMap<String, Item> getAllItems() {
        return allItems;
    }
}
