import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * The GUI class creates the GUI for the game and launches it.
 * All event handlers make reference to game mechanics and
 * methods present in the game class.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class GUI extends Application{

    private Stage window;
    private Menu talk, take, changeRoomAction, give, inspectItem, accuse;
    private TextArea inventoryDisplay, quests, roomDescription, generalOutput;

    private Game game;

    /**
     * Main method of the program, launches the game.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        //Start and setup the game
        game = new Game();

        //Setup the GUI
        window = primaryStage;
        window.setTitle("Brooklyn Murder");

        GridPane layout = new GridPane();

        //Left panel displaying quests and player inventory.
        quests = new TextArea();
        quests.setWrapText(true);
        quests.setEditable(false);
        Label questTitle = new Label("Your Quests");

        inventoryDisplay = new TextArea();
        inventoryDisplay.setWrapText(true);
        inventoryDisplay.setEditable(false);
        Label inventoryTitle = new Label("Your Inventory");

        //Center pane displaying action outputs and current room information
        roomDescription = new TextArea();
        roomDescription.setWrapText(true);
        roomDescription.setText(game.getPlayer().getCurrentRoom().getDescription());
        roomDescription.setEditable(false);
        Label roomTitle = new Label("Current Room");

        generalOutput = new TextArea();
        generalOutput.setWrapText(true);
        generalOutput.setEditable(false);

        //Right pane displaying notes with NPCs
        TextArea notes = new TextArea();
        notes.setText("Notes: \n\nWelcome to Brooklyn Nine-nine murder mystery game!\n" +
                "Your friends have been abducted.\n" +
                "In order to find them, you decided to go to the police.\n" +
                "Press 'help' if you need help.\n");
        notes.setWrapText(true);

        //Bottom bar displaying available commands.
        HBox commands = new HBox(10);

        Button lookAround = new Button("look around");
        lookAround.setOnAction(e -> generalOutput.setText(game.look()));

        Button letGinaGuide = new Button("let Gina guide");
        letGinaGuide.setOnAction(e -> {
            generalOutput.setText(game.letGinaGuide());
            updateGUI();
        });

        Button helpButton = new Button("help");
        helpButton.setOnAction(e -> generalOutput.setText(game.printHelp()));

        Button back = new Button("go back");
        back.setOnAction(e -> {
            generalOutput.setText(game.previousRoom());
            updateGUI();
        });

        Button quit = new Button("quit");
        quit.setOnAction(e -> closeGame());

        //Menu bar as part of the bottom bar containing actions
        //that take parameters like talk <NPC> or take <Item>
        MenuBar actions = new MenuBar();
        changeRoomAction = new Menu("Go To");
        take = new Menu("Take");
        talk = new Menu("Talk");
        give = new Menu("Give Item");
        inspectItem = new Menu("Inspect");
        accuse = new Menu("Accuse");
        for (NPC character : game.getAllCharacters()) {
            MenuItem accuseOption = new MenuItem(character.getName());
            if (character.getName().equals("Charles")) {
                accuseOption.setOnAction(e -> gameWon());
            } else {
                accuseOption.setOnAction(e -> gameLost());
            }
            accuse.getItems().add(accuseOption);
        }
        accuse.setVisible(false);

        actions.getMenus().addAll(changeRoomAction, take, talk, give, inspectItem, accuse);

        commands.getChildren().addAll(actions, back, lookAround, letGinaGuide, helpButton, quit);

        //Setup the GridPane with all the components
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setHgap(10);
        layout.setVgap(8);

        GridPane.setConstraints(questTitle, 0, 0, 1, 1);
        GridPane.setConstraints(quests, 0, 1, 1, 2);
        GridPane.setConstraints(inventoryTitle, 0, 3, 1, 1);
        GridPane.setConstraints(inventoryDisplay, 0, 4, 1, 1);
        GridPane.setConstraints(commands, 0, 5, 3, 1);
        GridPane.setConstraints(roomTitle, 1, 0, 1, 1);
        GridPane.setConstraints(roomDescription, 1, 1, 1, 2);
        GridPane.setConstraints(generalOutput, 1, 3, 1, 2);
        GridPane.setConstraints(notes, 2, 0, 1, 5);
        layout.getChildren().addAll(questTitle, quests, inventoryTitle, inventoryDisplay, commands, roomTitle, roomDescription, generalOutput, notes);


        //Ask for quit confirmation when uses closes the game
        window.setOnCloseRequest(e -> {
            e.consume();
            closeGame();
        });

        //Initialise the GUI and show the window
        updateGUI();
        window.setScene(new Scene(layout, 700,400));
        window.show();
    }

    public void updateGUI() {
        //Update the left panel with quests and player inventory.
        quests.setText(game.displayActiveQuests());
        inventoryDisplay.setText(game.displayInventory());

        //Update the menu commands to match the available options given the current room.
        talk.getItems().setAll();
        for (NPC character: game.getPlayer().getCurrentRoom().getCharacters()) {
            MenuItem characterOption = new MenuItem(character.getName());
            characterOption.setOnAction(e -> {
                generalOutput.setText(game.interact(character.getName().toLowerCase()));
                updateGUI();
            });
            talk.getItems().add(characterOption);
        }
        take.getItems().setAll();
        for (Item item: game.getPlayer().getCurrentRoom().getItemsInRoom().keySet()) {
            MenuItem itemOption = new MenuItem(item.getName());
            itemOption.setOnAction(e -> {
                generalOutput.setText(game.takeItem(item.getName()));
                updateGUI();
            });
            take.getItems().add(itemOption);
        }
        changeRoomAction.getItems().setAll();
        for (String exitName : game.getPlayer().getCurrentRoom().getAllExits().keySet()) {
            MenuItem exitOption = new MenuItem(exitName);
            exitOption.setOnAction(e -> {
                generalOutput.setText(game.goRoom(exitName));
                updateGUI();
            });
            changeRoomAction.getItems().add(exitOption);
        }
        give.getItems().setAll();
        for (NPC character: game.getPlayer().getCurrentRoom().getCharacters()) {
            Menu characterMenu = new Menu(character.getName());
            for (Item item : game.getPlayer().getInventory()) {
                MenuItem menuItem = new MenuItem(item.getName());
                characterMenu.getItems().add(menuItem);
                menuItem.setOnAction(e -> {
                    generalOutput.setText(game.give(character.getName().toLowerCase(), item.getName()));
                    updateGUI();
                });
            }
            give.getItems().add(characterMenu);
        }
        inspectItem.getItems().setAll();
        for (Item item: game.getPlayer().getInventory()) {
            MenuItem itemOption = new MenuItem(item.getName());
            itemOption.setOnAction(e -> {
                generalOutput.setText(game.inspectItem(item.getName()));
                updateGUI();
            });
            inspectItem.getItems().add(itemOption);
        }

        if (game.canAccuse()) {
            accuse.setVisible(true);
        }

        //Update the room description
        roomDescription.setText(game.getPlayer().getCurrentRoom().getDescription());
    }

    /**
     * Confirmation window that asks for user confirmation whenever
     * he/she closes the game by pressing quit or closing the window.
     */
    private void closeGame() {
        if (ConfirmWindow.display("Quit", "Are you sure you want to quit?")) {
            window.close();
        }
    }

    /**
     * Displays the game over pop-up and terminates the game.
     */
    private void gameLost() {
        AlertBox.display("You Lost", game.gameOver());
        window.close();
    }

    /**
     * Displays the game won window and asks if the user wishes to
     * remain in the game. Terminates the game if the user chooses no.
     */
    private void gameWon() {
        String congratsMessage = "Congratulations on finding the murderer. Here is a snippet of the arrest:\n";
        String winMessage = game.gameWon();
        String continueMessage = "\nDo you wan to continue roaming around the precinct?";
        if (!ConfirmWindow.display("You Won", congratsMessage + winMessage + continueMessage)) {
            window.close();
        }
        accuse.setVisible(false);
    }
}