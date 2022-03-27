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

	private Group group;
	private Scene scene;
	private Handle handle;

	private final String title;


	/**
	 * Constructor.
	 * 
	 * @param mainController	- used to call the centralized controller.
	 * @param mainModel			- used to call the centralized data model.
	 * @param title				- string displayed as the heading of the Stage.
	 */
	public CardSample(Controller mainController, Model mainModel, String title) {
//		System.out.println("PTable constructed: " + title);

		this.title = title;
		setTitle(title);
		resizableProperty().setValue(false);
		setOnCloseRequest(e -> Platform.exit());

		controller = mainController;
		model = mainModel;
		group = new Group();

		initCardSample();

		scene = new Scene(group, model.getWidth(), model.getHeight());
		setScene(scene);
		this.setX(20);
		this.setY(20);
		handle = new Handle(model.getHandleImage());
		group.getChildren().add(handle);
		handle.set(20, 10);

		show();
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
