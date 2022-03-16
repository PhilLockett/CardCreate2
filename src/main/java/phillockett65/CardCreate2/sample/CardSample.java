/*  PTDesigner - a simple application to design a periodic table.
 *
 *  Copyright 2020 Philip Lockett.
 *
 *  This file is part of PTDesigner.
 *
 *  PTDesigner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PTDesigner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PTDesigner.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * PTable is a class that is responsible for creating the Stage, drawing and 
 * redrawing the grid that represents the Periodic Table.
 */
package phillockett65.CardCreate2.sample;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import phillockett65.CardCreate2.ChangeChecker;
import phillockett65.CardCreate2.MainController;

public class CardSample extends Stage {
	
	private MainController main;
	private Group group;
	private Scene scene;

	private final String title;


	/**
	 * Constructor.
	 * 
	 * @param mainController	- used to call the centralized controller.
	 * @param title				- string displayed as the heading of the Stage.
	 */
	public CardSample(MainController mainController, String title) {
//		System.out.println("PTable constructed: " + title);

		this.title = title;
		setTitle(title);
		resizableProperty().setValue(false);
		setOnCloseRequest(e -> Platform.exit());

		main = mainController;
		group = new Group();

		initTable();

		scene = new Scene(group, main.getWidth(), main.getHeight());
		setScene(scene);
		show();
	}

	/**
	 * Calls the grid constructor, initializes some globals and adds the nodes 
	 * of the cells to the model. 
	 */
	private void initTable() {


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
