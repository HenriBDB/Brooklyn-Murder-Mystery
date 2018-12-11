import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class contains a single static method which can be called to display
 * a confirmation window containing a given message and yes and no buttons.
 *
 * This code is partially based off the following tutorial:
 * https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class ConfirmWindow {

    private static boolean answer;

    /**
     * Displays a confirmation window with a 'yes' and a 'no' button
     * @param title Title of the window
     * @param message Message to appear on the page
     * @return Returns true if the user clicked yes and false if no was clicked
     */
    public static boolean display(String title, String message) {
        //Setup the window
        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(250);

        //Prevent user interaction with other windows until this one is dealt with
        window.initModality(Modality.APPLICATION_MODAL);

        //Create the message to be shown and confirmation buttons
        Label msg = new Label(message);
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);
        msg.setMaxWidth(400);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        Button noButton = new Button("No");
        noButton.setOnAction(e ->{
            answer = false;
            window.close();
        });

        //Set the buttons horizontally beside each other
        HBox confirmButtons = new HBox(20);
        confirmButtons.getChildren().addAll(yesButton, noButton);
        confirmButtons.setAlignment(Pos.CENTER);

        //Display the buttons below the message
        VBox layout = new VBox(20);
        layout.getChildren().addAll(msg, confirmButtons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        //Display the window and wait for it to close, returning user response
        window.setScene(new Scene(layout));
        window.showAndWait();
        return answer;
    }

}