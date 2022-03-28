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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import phillockett65.CardCreate2.Controller;
import phillockett65.CardCreate2.Model;

public class CardSample extends Stage {
	
	private Controller controller;
	private Model model;

	// private Stage stage;
	private Scene scene;
	private Group group;
	private Handle handle;

	private double dx;	// Difference between the size of the stage and the size of the scene.
	private double dy;

	private final String title;


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
		this.title = title;
		setTitle(title);
		resizableProperty().setValue(false);
		setOnCloseRequest(e -> Platform.exit());

		controller = mainController;
		model = mainModel;
		group = model.getGroup();

		initCardSample();

		final float WIDTH = Default.WIDTH.getFloat();
		final float HEIGHT = Default.HEIGHT.getFloat();
		scene = new Scene(group, WIDTH, HEIGHT);
		this.setScene(scene);
		this.setX(20);
		this.setY(20);

		System.out.println("Default Size " + WIDTH + " and " + HEIGHT);
		System.out.println("Scene Size set to " + scene.getWidth() + " and " + scene.getHeight());


//        ChangeListener<Number> listener = new ChangeListener<Number>() {
//            private Point2D stageSize = null ;
//            private Point2D previousStageSize = new Point2D(stage.getWidth(), stage.getHeight());
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//
//                if (stageSize == null) {
//                    Platform.runLater(() -> {
//                        System.out.printf("Old: (%.1f, %.1f); new: (%.1f, %.1f)%n", 
//                                previousStageSize.getX(), previousStageSize.getY(), 
//                                stageSize.getX(), stageSize.getY());
//                        previousStageSize = stageSize;
//                        stageSize = null;
//                    });
//                }
//                stageSize = new Point2D(stage.getWidth(), stage.getHeight());
//            }
//
//        };

//        this.widthProperty().addListener(listener);
//        this.heightProperty().addListener(listener);


		handle = model.getHandle();
		handle.set(20, 10);

		this.show();

		System.out.println("Stage Size set to " + this.getWidth() + " and " + this.getHeight());
		dx = this.getWidth() - WIDTH;
		dy = this.getHeight() - HEIGHT;
		System.out.println("Stage deltas " + dx + " and " + dy + "   " + (dy-dx));

		this.setMinWidth(Default.MIN_WIDTH.getFloat() + dx);
		this.setMinHeight(Default.MIN_HEIGHT.getFloat() + dy);
		this.setMaxWidth(Default.MAX_WIDTH.getFloat() + dx);
		this.setMaxHeight(Default.MAX_HEIGHT.getFloat() + dy);

	}


    public void syncBackgroundColour() {
		scene.setFill(model.getBackgroundColour());
    }

    public void syncCardSize() {
    	this.setWidth(model.getCalculatedWidth() + dx);
    	this.setHeight(model.getHeight() + dy);
    }

    public void syncCurrentCard() {
    	System.out.println("syncCurrentCard()");
    }


	/**
	 * Temp for debug.
     * @param x coordinate of the centre of the current Item as a percentage of the card width.
     * @param y coordinate of the centre of the current Item as a percentage of the card height.
	 */
    public void setHandle(double x, double y) {
    	// Convert to pixels.
    	final long xPos = model.percentageToPX(x); 
    	final long yPos = model.percentageToPX(y); 

    	handle.set(xPos, yPos);
    }

	/**
	 * Calls the grid constructor, initializes some globals and adds the nodes 
	 * of the cells to the model. 
	 */
	private void initCardSample() {


	}



/************************************************************************
 * Table drawing support section.
 */



/************************************************************************
 * Key handling support code.
 */

	/**
	 * Allow the KeyHandler to update the window title to indicate the 
	 * Shift/Control key press sate.
	 * 
	 * @param action - String to indicate the current user action.
	 */
	public void augmentTitle(String action) {
		setTitle(title + action);
	}



}
