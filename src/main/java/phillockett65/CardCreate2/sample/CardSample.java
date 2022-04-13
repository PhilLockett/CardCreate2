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
 * CardSample is a class that is responsible for creating the Stage, drawing 
 * and refreshing the card.
 */
package phillockett65.CardCreate2.sample;

import javafx.application.Platform;
// import javafx.beans.value.ChangeListener;
// import javafx.beans.value.ObservableValue;
// import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import phillockett65.CardCreate2.Controller;
import phillockett65.CardCreate2.Model;

public class CardSample extends Stage {
    
    private Controller controller;
    private Model model;

    // private Stage stage;
    private Scene scene;
    private Group group;
    private Rectangle card;
    private Handle handle;

    private double dx;	// Difference between the size of the stage and the size of the scene.
    private double dy;
    private double xScale = 1D;
    private double yScale = 1D;


    /**
     * Constructor.
     * 
     * @param mainController	- used to call the centralized controller.
     * @param mainModel			- used to call the centralized data model.
     * @param title				- string displayed as the heading of the Stage.
     */
    public CardSample(Controller mainController, Model mainModel, String title) {
//		System.out.println("CardSample constructed: " + title);

        // stage = this;
        resizableProperty().setValue(false);
        setOnCloseRequest(e -> Platform.exit());
        initStyle(StageStyle.TRANSPARENT);

        controller = mainController;
        model = mainModel;

        this.show();
        
        initializeCardSample();
    }

    /**
     * Calls the grid constructor, initializes some globals and adds the nodes 
     * of the cells to the model. 
     */
    private void initializeCardSample() {

        final float WIDTH = Default.WIDTH.getFloat();
        final float HEIGHT = Default.HEIGHT.getFloat();

        group = model.getGroup();

        scene = new Scene(group, WIDTH, HEIGHT, Color.TRANSPARENT);
        drawBlankCard();

        this.setScene(scene);
        this.setX(20);
        this.setY(20);

        dx = this.getWidth() - WIDTH;
        dy = this.getHeight() - HEIGHT;

        this.setMinWidth(Default.MIN_WIDTH.getFloat() + dx);
        this.setMinHeight(Default.MIN_HEIGHT.getFloat() + dy);
        this.setMaxWidth(Default.MAX_WIDTH.getFloat() + dx);
        this.setMaxHeight(Default.MAX_HEIGHT.getFloat() + dy);

        scene.setOnMouseClicked(event -> {
            // System.out.println("setOnMouseClicked(" + event.getButton() + ") on scene");
            if (event.isAltDown()) {
                if (!event.isControlDown())
                    model.decCurrent();
            }
            else
            if (event.isControlDown()) {
                model.incCurrent();
            }
            else {
                model.setNextPayload();
                controller.syncToCurrentCardItem();
            }
        });

    }

    /**
     * Called on initialization to set up the blank card.
     */
    private void drawBlankCard() {
        final double width = model.getCalculatedWidth();
        final double height = model.getHeight();
        final Color color = model.getBackgroundColour();
        card = new Rectangle(width, height, color);

        final double radius = model.getRadiusPX();
        card.setArcWidth(radius);
        card.setArcHeight(radius);
        card.setStroke(Color.BLACK);
        card.setStrokeWidth(1);
        group.getChildren().add(card);
    }

    // Stage stage;
    // private void addChangeListener() {
    //     stage = this;

    //     ChangeListener<Number> listener = new ChangeListener<Number>() {
    //         private Point2D stageSize = null ;
    //         private Point2D previousStageSize = new Point2D(stage.getWidth(), stage.getHeight());

    //         @Override
    //         public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

    //             if (stageSize == null) {
    //                 Platform.runLater(() -> {
    //                     System.out.printf("Old: (%.1f, %.1f); new: (%.1f, %.1f)%n", 
    //                             previousStageSize.getX(), previousStageSize.getY(), 
    //                             stageSize.getX(), stageSize.getY());
    //                     previousStageSize = stageSize;
    //                     stageSize = null;
    //                 });
    //             }
    //             stageSize = new Point2D(stage.getWidth(), stage.getHeight());
    //         }

    //     };

    //     this.widthProperty().addListener(listener);
    //     this.heightProperty().addListener(listener);
    // }

    /**
     * Initialization after a base directory has been selected.
     */
    public void init() {
        handle = model.getHandle();

        handle.setOnMouseClicked(event -> {
            // System.out.println("setOnMouseClicked(" + event.getButton() + ") on handle");
            event.consume();
        });

        handle.setOnMousePressed(event -> {
            // System.out.println("setOnMousePressed(" + event.getButton() + ") on handle");
            dx = event.getSceneX() - model.getCurrentX();
            dy = event.getSceneY() - model.getCurrentY();
            xScale = 100 / model.getCalculatedWidth();
            yScale = 100 / model.getHeight();
        });

        handle.setOnMouseReleased(event -> {
            // System.out.println("setOnMouseReleased(" + event.getButton() + ") on handle");
        });

        handle.setOnMouseDragged(event -> {
            // System.out.println("setOnMouseDragged(" + event.getButton() + ") on handle");
            double xPos = (event.getSceneX() - dx) * xScale;
            double yPos = (event.getSceneY() - dy) * yScale;
            model.setCurrentX(xPos, true);
            model.setCurrentY(yPos, true);
        });
    }

/************************************************************************
 * Synchronize interface.
 */

    /**
     * Synchronise to the current background colour.
     */
    public void syncBackgroundColour() {
        card.setFill(model.getBackgroundColour());
    }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        final double width = model.getCalculatedWidth();
        final double height = model.getHeight();

        this.setWidth(width + dx);
        this.setHeight(height + dy);

        final double radius = model.getRadiusPX();
        card.setArcWidth(radius);
        card.setArcHeight(radius);
        card.setWidth(width);
        card.setHeight(height);
    }

}
