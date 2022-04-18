/*  CardCreate2 - a JavaFX based playing card image generator.
 *
 *  Copyright 2022 Philip Lockett.
 *
 *  This file is part of CardCreate2.
 *
 *  CardCreate2 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CardCreate2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CardCreate2.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Boilerplate code responsible for launching the JavaFX application. 
 */
package phillockett65.CardCreate2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Main.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(App.class.getResource("application.css").toExternalForm());

        ObservableList<Image> icons = stage.getIcons();
        icons.add(new Image(getClass().getResourceAsStream("icon48.png")));
        icons.add(new Image(getClass().getResourceAsStream("icon32.png")));
        icons.add(new Image(getClass().getResourceAsStream("icon16.png")));

        stage.setTitle("Playing Card Generator 2.0");
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.resizableProperty().setValue(false);
        stage.setScene(scene);

        Controller controller = fxmlLoader.getController();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ALT:
                controller.decreaseSize();
                break;

            case CONTROL: 
                controller.increaseSize();
                break;

            default:
                break;
            }
        });

        scene.setOnKeyReleased(event -> {
            controller.release();
        });

        stage.show();

        controller.init(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}