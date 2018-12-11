import javafx.scene.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * This class contains a single static method which can be calld to
 * display a pop-up containing a given message and with a given title.
 *
 * This code is partially based off the following tutorial:
 * https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class AlertBox {

    /**
     * Displays a pop-up window
     * @param title Title of the window
     * @param message Message to appear on the page
     */
    public static void display(String title, String message) {
        Stage alert = new Stage();

        //Prevent user interaction with other windows until this one is dealt with
        alert.initModality(Modality.APPLICATION_MODAL);

        //Set title, message and close button
        alert.setTitle(title);
        Label msg = new Label(message);
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> alert.close());

        //Set the layout vertically
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(msg, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Show the window and resume code once it is closed
        alert.setMinWidth(250);
        alert.setScene(new Scene(layout));
        alert.showAndWait();
    }

}
