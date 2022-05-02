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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import phillockett65.CardCreate2.Controller;
import phillockett65.CardCreate2.Model;

public class CardSample extends Stage {
    
    private Controller controller;
    private Model model;

    private Scene scene;
    private Rectangle card;

    private double mx;
    private double my;

    private double dx;	// Difference between the size of the stage and the size of the scene.
    private double dy;
    private double xScale = 1D;
    private double yScale = 1D;

    private double scale = 0D;

    private boolean resize = false;


    /**
     * Constructor.
     * 
     * @param mainController    - used to call the centralized controller.
     * @param mainModel         - used to call the centralized data model.
     * @param title             - string displayed as the heading of the Stage.
     */
    public CardSample(Controller mainController, Model mainModel, String title) {
//		System.out.println("CardSample constructed: " + title);

        resizableProperty().setValue(false);
        setOnCloseRequest(e -> Platform.exit());
        initStyle(StageStyle.TRANSPARENT);

        controller = mainController;
        model = mainModel;

        this.show();
        
        initializeCardSample();
    }

    /**
     * Initializes the stage and adds some handlers to the scene.
     */
    private void initializeCardSample() {

        final float WIDTH = Default.WIDTH.getFloat();
        final float HEIGHT = Default.HEIGHT.getFloat();

        scene = new Scene(model.getGroup(), WIDTH, HEIGHT, Color.TRANSPARENT);
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

        scene.setOnMouseEntered(event -> {
            scale = 0D;
        });

        scene.setOnScroll(event -> {
            scale += event.getTextDeltaX() + event.getTextDeltaY();
            int inc = (int)scale;
            if (inc != 0) {
                scale -= inc;
                model.resizeCurrent(-inc);
            }
        });

        scene.setOnKeyTyped(event -> {
            switch (event.getCharacter()) {
            case "+":
                model.incCurrent();
                break;

            case "-":
                model.decCurrent();
                break;

            default:
                break;
            }
        });

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case SHIFT:
                controller.moveSample(true);
                break;

            case ALT:
                controller.decreaseSize();
                break;

            case CONTROL: 
                controller.increaseSize();
                break;

            case TAB: 
                model.setNextPayload();
                controller.syncToCurrentCardItem();
                break;

            case UP: 
                if (event.isShiftDown())
                    moveUp();
                else
                    model.moveCurrentUp();
                break;

            case DOWN: 
                if (event.isShiftDown())
                    moveDown();
                else
                    model.moveCurrentDown();
                break;

            case LEFT: 
                if (event.isShiftDown())
                    moveLeft();
                else
                    model.moveCurrentLeft();
                break;

            case RIGHT: 
                if (event.isShiftDown())
                    moveRight();
                else
                    model.moveCurrentRight();
                break;

            default:
                break;
            }
        });

        scene.setOnKeyReleased(event -> {
            controller.release();
        });

    }

    private void moveUp() {
        double pos = this.getY() - Default.STEP_COUNT.getFloat();
        if (pos < 0)
            pos = 0;
        this.setY(pos);
    }

    private void moveDown() {
        double pos = this.getY() + Default.STEP_COUNT.getFloat();
        this.setY(pos);
    }

    private void moveLeft() {
        double pos = this.getX() - Default.STEP_COUNT.getFloat();
        if (pos < 0)
            pos = 0;
        this.setX(pos);
    }

    private void moveRight() {
        double pos = this.getX() + Default.STEP_COUNT.getFloat();
        this.setX(pos);
    }

    /**
     * Called on initialization to set up the blank card.
     */
    private void drawBlankCard() {
        final double width = model.getWidth();
        final double height = model.getHeight();
        final Color color = model.getBackgroundColour();
        card = new Rectangle(width, height, color);

        card.setArcWidth(model.getArcWidthPX());
        card.setArcHeight(model.getArcHeightPX());
        card.setStroke(Color.BLACK);
        card.setStrokeWidth(1);
        model.getGroup().getChildren().add(card);
    }

    /**
     * Initialization after a base directory has been selected.
     */
    public void init() {
        Handle handle = model.getHandle();

        handle.setOnMouseClicked(event -> {
            if (!resize)
                event.consume();
        });

        handle.setOnMousePressed(event -> {
            mx = event.getSceneX() - model.getCurrentX();
            my = event.getSceneY() - model.getCurrentY();
            xScale = 100 / model.getWidth();
            yScale = 100 / model.getHeight();
            scene.setCursor(Cursor.CLOSED_HAND);
        });

        handle.setOnMouseReleased(event -> {
            scene.setCursor(Cursor.OPEN_HAND);
        });

        handle.setOnMouseDragged(event -> {
            double xPos = (event.getSceneX() - mx) * xScale;
            double yPos = (event.getSceneY() - my) * yScale;
            model.setCurrentPos(xPos, yPos);
        });

        handle.setOnMouseEntered(event -> {
            scene.setCursor(Cursor.OPEN_HAND);
        });

        handle.setOnMouseExited(event -> {
            scene.setCursor(Cursor.DEFAULT);
        });

    }



    /************************************************************************
     * Synchronize interface.
     */

    public void setResize(boolean state) {
        resize = state;
    }

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
        final double width = model.getWidth();
        final double height = model.getHeight();

        this.setWidth(width + dx);
        this.setHeight(height + dy);

        card.setArcWidth(model.getArcWidthPX());
        card.setArcHeight(model.getArcHeightPX());
        card.setWidth(width);
        card.setHeight(height);
    }

}
