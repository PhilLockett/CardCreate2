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
package phillockett65.CardCreate2.table;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import phillockett65.CardCreate2.ChangeChecker;
import phillockett65.CardCreate2.MainController;

public class PTable extends Stage {
	
	private KeyHandler keyEventHandler;

	private MainController main;
	private Group group;
	private Scene scene;

	private final String title;

	private Grid grid;
	private Selection selection;

	private Quantifier quantities;


	/**
	 * Constructor.
	 * 
	 * @param mainController	- used to call the centralized controller.
	 * @param title				- string displayed as the heading of the Stage.
	 */
	public PTable(MainController mainController, String title) {
//		System.out.println("PTable constructed: " + title);

		this.title = title;
		setTitle(title);
		resizableProperty().setValue(false);
		setOnCloseRequest(e -> Platform.exit());

		main = mainController;
		group = new Group();

		selection = new Selection(main.getRows(), main.getCols());
		keyEventHandler = new KeyHandler(this, selection);
		this.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
		this.addEventHandler(KeyEvent.KEY_RELEASED, keyEventHandler);

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
		final int rows = main.getRows();
		final int cols = main.getCols();
		boolean cellHighlighted = false;

		grid = new Grid(main);
		quantities = new Quantifier(grid);
		selection.setRows(rows);
		selection.setCols(cols);

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid.getCell(r, c);

				group.getChildren().add(cell.getBack());

				if (cell.isBlank())
					continue;

				if (cellHighlighted == false) {
					cellHighlighted = true;
					selection.setPosition(r, c);
					cell.setSelected(true);
					main.setSelected(cell);
				}

				group.getChildren().addAll(cell.getZ(), cell.getSymbol());
			}
		}

		grid.updateStates();
	}



/************************************************************************
 * Table drawing support section.
 */

	/**
	 * Updates the background of all Elements of the specified subcategory to 
	 * the latest colour.
	 * 
	 * @param subcategory with updated colour.
	 * @param colour to update with.
	 */
	public void setSubcategoryColour(int subcategory, Color colour) {
//		System.out.println("setSubcategoryColour(subcategory = " + subcategory + ")");

		grid.setSubcategoryColour(subcategory, colour);
	}

	/**
	 * Updates the foreground of all Elements in the specified state to the 
	 * latest colour.
	 * 
	 * @param state with updated colour.
	 * @param colour to update with.
	 */
	public void setStateColour(int state, Color colour) {
//		System.out.println("setStateColour(state = " + state + ")");

		grid.setStateColour(state, colour);
	}

	/**
	 * Moves the nodes of the given cell from the current Group to the given 
	 * new Group and set the current Group to the new Group.
	 * 
	 * @param cell the given cell.
	 * @param newGroup to move the nodes to.
	 */
	private void setGroup(Cell cell, Group newGroup) {
//		System.out.println("setGroup()");

		group.getChildren().removeAll(cell.getBack(), cell.getZ(), cell.getSymbol());

		newGroup.getChildren().add(cell.getBack());
		if (!cell.isBlank())
			newGroup.getChildren().addAll(cell.getZ(), cell.getSymbol());
	}

	/**
	 * If the Tile or Border size is changed we move all the nodes to a new 
	 * Group, then use it to resize the window.
	 */
	private void moveGroup() {
//		System.out.println("sizeChange(rows = " + main.getWidth() + " -> " + main.getHeight() + ")");

		final int rows = grid.getRows();
		final int cols = grid.getCols();
		Group newGroup = new Group();

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid.getCell(r, c);
				setGroup(cell, newGroup);
			}
		}

		group = newGroup;
		scene = new Scene(group, main.getWidth(), main.getHeight());
		setScene(scene);
	}

	/**
	 * Change the layout of the grid.
	 * 
	 * @param rowCkr		- Row count change.
	 * @param colCkr		- Column count change.
	 * @param tileCkr		- Tile size change (in pixels).
	 * @param brdrCkr		- Border size change (in pixels).
	 * @param tempCkr		- Temperature change.
	 */
	public void updateLayout(
			ChangeChecker rowCkr, ChangeChecker colCkr, 
			ChangeChecker tileCkr, ChangeChecker brdrCkr,
			ChangeChecker tempCkr) {

		// Adjust the selection if the grid shrinks.
		if (rowCkr.isChanged())
			selection.setRows(rowCkr.getNewValue());
		if (colCkr.isChanged())
			selection.setCols(colCkr.getNewValue());

		// Highlight the selection.
		Cell cell = getCurrentCell();
		cell.setSelected(true);
		if (!cell.isBlank())
			main.setSelected(cell);

		if (grid.updateLayout(rowCkr, colCkr, tileCkr, brdrCkr, tempCkr))
			moveGroup();
	}

	/**
	 * Reverses the order of the columns and repositions the cells.
	 */
	public void flipColumns() {
		grid.flipColumns();
		selection.flipColumns(grid.getCols());
	}

	/**
	 * Reverses the order of the rows and then repositions the cells.
	 */
	public void flipRows() {
		grid.flipRows();
		selection.flipRows(grid.getRows());
	}

	/**
	 * Make the the current quantities accessible.
	 * 
	 * @return the current quantities.
	 */
	public Quantities getQuantities() {
		return quantities.getQuantities();
	}

	/**
	 * Gets the cell that is considered the current cell by the selection.
	 * 
	 * @return the current cell.
	 */
	public Cell getCurrentCell() {
		final int row = selection.getRow();
		final int col = selection.getCol();

		return grid.getCell(row, col);
	}


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

	/**
	 * Highlight/unhighlight the currently selected cells.
	 * 
	 * @param selected	true if cell is to be highlighted, false if  
	 * 					highlighting is to be removed.
	 */
	public void highlightSelectedCells(boolean selected) {
//		System.out.println("highlightSelectedCells(" + value + ")");

		final int topRow = selection.getTop();
		final int bottomRow = selection.getBottom();
		final int leftCol = selection.getLeft();
		final int rightCol = selection.getRight();

		// Highlight sub grid.
		for (int r = topRow; r <= bottomRow; ++r) {
			for (int c = leftCol; c <= rightCol; ++c) {
				Cell cell = grid.getCell(r, c);
				cell.setSelected(selected);
			}
		}

		// Update the Details tab display.
		Cell cell = getCurrentCell();
		if (!cell.isBlank())
			main.setSelected(cell);
	}

	/**
	 * Move the currently selected cells in the direction indicated.
	 * 
	 * @param code to indicate the direction to move the cells.
	 */
	public void moveSelection(KeyCode code) {

		grid.moveSelection(selection, code);
	}

}
