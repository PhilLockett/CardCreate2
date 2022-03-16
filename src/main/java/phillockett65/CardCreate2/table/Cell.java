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
 * Cell is a class that is responsible for drawing the tile that represents 
 * the chemical element on the table. The Tile includes a Rectangle node, 
 * coloured to represent the chemical subcategory, for the background and two
 * Text nodes, coloured to represent the the state (solid, liquid, gas), for 
 * the Atomic Number and element symbol.
 * 
 */
package phillockett65.CardCreate2.table;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.elements.ElementConfig;

public class Cell {

/*****************************************************************************
 * These attributes are the same for all cells. They are updated exclusively
 * by the model to keep them in sync. This approach removes the need to 
 * repeatedly pass the same data to all cells.
 */
	private static int temp;
	private static int tileSize;
	private static Desc ZDesc;
	private static Desc symbolDesc;

	public static void setTemp(int temp) {
		Cell.temp = temp;
	}

	public static void setTileSize(int size) {
		Cell.tileSize = size;
	}

	public static void setZDesc(Desc zDesc) {
		Cell.ZDesc = zDesc;
	}

	public static void setSymbolDesc(Desc symbolDesc) {
		Cell.symbolDesc = symbolDesc;
	}


/*****************************************************************************
 * These attributes are specific to each instance of a Cell object.
 */
	private ElementConfig e;

	private Color backCol;
	private Color foreCol;
	private boolean selected = false;

	private Rectangle back;
	private Text ZText;
	private Text symbolText;

	/**
	 * Constructor.
	 * 
	 * @param colour - background colour, determined by the subcategory.
	 */
	public Cell(Color colour) {
		back = new Rectangle();
		setBackground(colour);
	}

	/**
	 * Set the ElementConfig and colours for this cell.
	 * 
	 * @param e the Element to associate with the cell.
	 * @param foreCol - foreground colour, determined by the state.
	 * @param backCol - background colour, determined by the subcategory.
	 */
	public void setElement(ElementConfig e, Color foreCol, Color backCol) {
		this.e = e;

		setBackgroundColour(backCol);
		updateBackground();

		ZText = new Text(String.valueOf(e.getZ()));
		symbolText = new Text(e.getSymbol());

		setForeground(foreCol);
	}

	/**
	 * Get the tile background Rectangle node.
	 * 
	 * @return the tile background node.
	 */
	public Rectangle getBack() {
		return back;
	}

	/**
	 * Get the Atomic Number Text node.
	 * 
	 * @return the Atomic Number Text node.
	 */
	public Text getZ() {
		return ZText;
	}

	/**
	 * Get the Symbol Text node.
	 * 
	 * @return the Symbol Text node.
	 */
	public Text getSymbol() {
		return symbolText;
	}

	/**
	 * Get the associated element of this cell.
	 * 
	 * @return the associated element (may be null).
	 */
	public ElementConfig getE() {
		return e;
	}

	/**
	 * Check if the cell is considered blank, i. e. it does not have an 
	 * Element associated with it.
	 * 
	 * @return true if there is no Element associated with the cell.
	 */
	public boolean isBlank() {
		return (e == null);
	}

	/**
	 * Get the state (solid, liquid or gas) of the associated element at the 
	 * current temperature. The state is used to set the foreground colour.
	 * 
	 * @return the state.
	 */
	public int getState() {
		if (isBlank())
			return Model.UNDEFINED;

		return Model.findState(e, temp);
	}

	/**
	 * Get the Subcategory of the associated Element of the cell.
	 * 
	 * @return the Subcategory of the associated Element.
	 */
	public int getSubcategory() {
		if (isBlank())
			return 0;

		return e.getSubcategory();
	}

	/**
	 * Set the coordinates of the tile origin in pixels. Uses the Class 
	 * variable tile size and Atomic Number and Symbol descriptors..
	 * 
	 * @param x coordinate of tile position.
	 * @param y coordinate of tile position.
	 */
	public void setPosition(int x, int y) {
		back.setX(x);
		back.setY(y);

		if (isBlank())
			return;

		final int width = (int)(ZText.getLayoutBounds().getWidth());
		int px = x + tileSize - width;
		int py = y + ZDesc.getDy();
		ZText.setX(px); 
		ZText.setY(py);

		px = x + symbolDesc.getDx();
		py = y + symbolDesc.getDy();
		symbolText.setX(px); 
		symbolText.setY(py);
	}

	/**
	 * Set the dimensions of the Rectangle used as a background to the Class  
	 * variable tileSize.
	 */
	public void setBackgroundSize() {
//		System.out.println("setTileSize()");

		back.setWidth(tileSize);
		back.setHeight(tileSize);
	}

	/**
	 * Note the background colour for this Cell. This may change if the 
	 * subcategory colour is changed.
	 * 
	 * @param colour - the background colour to use.
	 */
	private void setBackgroundColour(Color colour) {
//		System.out.println("setBackgroundColour(" + backCol.toString() + ")");
		backCol = colour;
	}

	/**
	 * Set the colour of the Rectangle used as a background to the colour set
	 * by setBackgroundColour(). The colour may be inverted if the cell is 
	 * selected.
	 */
	private void updateBackground() {
//		System.out.println("showBackground()");

		Color colour = backCol;
		if (selected)
			colour = colour.invert();

		back.setFill(colour);
	}

	/**
	 * Set up the background Tile size and colour. The colour may be inverted 
	 * if the cell is selected.
	 * Note: the colour is derived from the Subcategory of the Element, or 
	 * UNKNOWN colour if no Element is associated with the cell.
	 * 
	 * @param colour to set background to.
	 */
	public void setBackground(Color colour) {
//		System.out.println("setBackground(" + colour.toString() + ")");

		setBackgroundSize();
		setBackgroundColour(colour);
		updateBackground();
	}

	/**
	 * Set the font size of the Text nodes used to display the Atomic Number 
	 * and Symbol to the font size in the Class variable descriptors.
	 */
	public void setFontSize() {
//		System.out.println("setFontSize()");

		if (isBlank())
			return;

		ZText.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, ZDesc.getSizeInt()));
		symbolText.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, symbolDesc.getSizeInt()));
	}

	/**
	 * Note the foreground colour for this Cell. This may change if the 
	 * state colour is changed or the temperature is changed.
	 * 
	 * @param colour - the foreground colour to use.
	 */
	private void setForegroundColour(Color colour) {
//		System.out.println("setForegroundColour(" + foreCol.toString() + ")");

		foreCol = colour;
	}

	/**
	 * Set the colour of the Text nodes used to display the Atomic Number and 
	 * Symbol to the colour set by setForegroundColour(). The colour may be 
	 * inverted if the cell is selected.
	 */
	private void updateForeground() {
//		System.out.println("showForeground()");

		if (isBlank())
			return;

		Color colour = foreCol;
		if (selected)
			colour = colour.invert();

		ZText.setFill(colour);
		symbolText.setFill(colour);
	}

	/**
	 * Set up the Text nodes used to display the Atomic Number and Symbol to 
	 * the font size in the Class variable descriptors and using the supplied 
	 * colour. The colour may be inverted if the cell is selected.
	 * Note: the colour is derived from the State of the Element at the 
	 * current temperature.
	 * 
	 * @param colour to set foreground to.
	 */
	public void setForeground(Color colour) {
//		System.out.println("setForeground(" + colour.toString() + ")");

		if (isBlank())
			return;

		setFontSize();
		setForegroundColour(colour);
		updateForeground();
	}

	/**
	 * Indicate whether a cell should be highlighted. This notes the selected 
	 * state and refreshes the Tile colour.
	 * 
	 * @param selected	- flag to indicate if the cell should be display as 
	 *					  highlighted.
	 */
	public void setSelected(boolean selected) {
//		System.out.println("setSelected(" + selected + ")");
		this.selected = selected;
		updateBackground();

		if (!isBlank())
			updateForeground();
	}

}
