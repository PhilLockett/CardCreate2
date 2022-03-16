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
 * Model is a class that captures the dynamic shared data plus some supporting 
 * constants and provides access via getters and setters.
 */
package phillockett65.CardCreate2;

import javafx.scene.paint.Color;
import phillockett65.CardCreate2.elements.ElementConfig;
import phillockett65.CardCreate2.elements.Elements;
import phillockett65.CardCreate2.table.Cell;
import phillockett65.CardCreate2.table.Desc;

public class Model {

	public final static int UNDEFINED = 0;
	public final static int SOLID = 1;
	public final static int LIQUID = 2;
	public final static int GAS = 3;
	public final static int MAX_STATE = 4;

	public final static int METALLOID = 1;
	public final static int UNKNOWN = 2;
	public final static int ALKALI_METAL = 3;
	public final static int ALKALINE_EARTH_METAL = 4;
	public final static int LANTHANIDE = 5;
	public final static int ACTINIDE = 6;
	public final static int TRANSITION_METAL = 7;
	public final static int POST_TRANSITION_METAL = 8;
	public final static int REACTIVE_NONMETAL = 9;
	public final static int NOBLE_GAS = 10;
	public final static int MAX_SUBCATEGORY = 11;

	public final static int INIT_TEMP = 274;
	public final static int INIT_TILE_SIZE = 50;
	public final static int INIT_BORDER_SIZE = 2;

	private int temp;
	private int maxTemp = 0;

	private int rows = 10;
	private int cols = 18;
	private int tileSize;
	private int borderSize;

	private Desc Z = new Desc();
	private Desc symbol = new Desc();

	private String[] states = { "UNDEFINED", "Solid", "Liquid", "Gas" };
	private Color[] stateColours = {
			Color.GRAY,
			Color.BLACK,
			Color.BLUE,
			Color.RED
		};

	private String[] subcategories = new String[MAX_SUBCATEGORY+1];
	private final Color[] subcategoryColours = {
			Color.rgb(224, 224, 224),
			Color.rgb(204, 204, 153),
			Color.rgb(224, 224, 224),
			Color.rgb(232, 232, 232),
			Color.rgb(255, 102, 102),
			Color.rgb(255, 222, 173),
			Color.rgb(255, 191, 255),
			Color.rgb(255, 153, 204),
			Color.rgb(255, 192, 192),
			Color.rgb(204, 204, 204),
			Color.rgb(240, 255, 143),
			Color.rgb(192, 255, 255)
		};

	/**
	 * Determines whether the given element is solid, liquid or gas at the 
	 * given temperature.
	 * 
	 * @param e the given element.
	 * @param temp the given temperature.
	 * @return the state.
	 */
	public static int findState(ElementConfig e, float temp) {
		final float melt = e.getMelt();
		final float boil = e.getBoil();

		if ((melt == 0) && (boil == 0))
			return Model.UNDEFINED;

		if (temp < melt)
			return Model.SOLID;

		if (temp < boil)
			return Model.LIQUID;

		return Model.GAS;
	}

	/**
	 * Default Constructor.
	 */
	public Model() {
//		System.out.println("Model constructed.");
		for (int i = 0; i < MAX_SUBCATEGORY; ++i) {
			subcategories[i] = new String(Elements.getSubcategory(i));
		}

		// Call the setters for these values here to ensure that Cell Class 
		// variables are kept in sync. This is only a problem if there are 
		// multiple instances of the Model.
		setTemp(INIT_TEMP);
		setTileSize(INIT_TILE_SIZE);
		setBorderSize(INIT_BORDER_SIZE);

		Cell.setZDesc(Z);
		Cell.setSymbolDesc(symbol);

		maxTemp = findMaxTemp();
	}

	/**
	 * Scans the list of Elements to find the highest melting/boiling point 
	 * temperature.
	 * 
	 * @return the highest melting/boiling point temperature.
	 */
	private int findMaxTemp() {
		float max = 0F;
		for (int i = Elements.firstKey(); i <= Elements.lastKey(); i = Elements.nextKey(i)) {
			if (!Elements.isKeyValid(i)) {
				continue;
			}
			ElementConfig e = Elements.element(i);
			if (e.getBoil() > max)
				max = e.getBoil();
			if (e.getMelt() > max)
				max = e.getMelt();
		}

		return (int)max + 1;
	}

	public int getMaxTemp() {
		return maxTemp;
	}
	public int getTemp() {
		return temp;
	}
	public void setTemp(int temp) {
		this.temp = temp;
		Cell.setTemp(temp);
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public int getTileSize() {
		return tileSize;
	}
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
		Cell.setTileSize(tileSize);

		Z.setSize((float)tileSize / 4F);
		Z.setDx((int)((float)tileSize * 0.8));
		Z.setDy((int)(Z.getSize() * 1.2));

		symbol.setSize((float)tileSize * 0.6F);
		symbol.setDx(0);
		symbol.setDy((int)(symbol.getSize() * 1.4));
	}
	public int getBorderSize() {
		return borderSize;
	}
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}
	public int getStepSize() {
		return borderSize+tileSize;
	}

	public int getWidth() {
		return ((tileSize+borderSize) * cols) + borderSize;
	}
	public int getHeight() {
		return ((tileSize+borderSize) * rows) + borderSize;
	}

	public Desc getZ() {
		return Z;
	}
	public Desc getSymbol() {
		return symbol;
	}

	public boolean isValidStateIndex(int index) {
		if ((index < 0) || (index >= MAX_STATE))
			return false;

		return true;
	}
	public boolean setStateColour(int index, Color colour) {
//		System.out.println("setStateColour(" + index + ", " + colour.toString() + ");");
		if (!isValidStateIndex(index))
			return false;

		stateColours[index] = colour;

		return true;
	}
	public Color getStateColour(int index) {
//		System.out.println("getStateColour(" + index + ");");
		if (!isValidStateIndex(index))
			return null;

		return stateColours[index];
	}
	public boolean setStateString(int index, String state) {
//		System.out.println("setStateString(" + index + ", " + state + ");");
		if (!isValidStateIndex(index))
			return false;

		states[index] = state;

		return true;
	}
	public String getStateString(int index) {
//		System.out.println("getStateString(" + index + ");");
		if (!isValidStateIndex(index))
			return null;

		return states[index];
	}


	public boolean isValidSubcategoryIndex(int index) {
		if ((index < 0) || (index >= MAX_SUBCATEGORY))
			return false;

		return true;
	}
	public boolean setSubcategoryColour(int index, Color colour) {
//		System.out.println("setSubcategoryColour(" + index + ", " + colour.toString() + ");");
		if (!isValidSubcategoryIndex(index))
			return false;

		subcategoryColours[index] = colour;

		return true;
	}
	public Color getSubcategoryColour(int index) {
//		System.out.println("getSubcategoryColour(" + index + ");");
		if (!isValidSubcategoryIndex(index))
			return null;

		return subcategoryColours[index];
	}
	public boolean setSubcategoryString(int index, String subcategory) {
//		System.out.println("setSubcategoryString(" + index + ", " + subcategory + ");");
		if (!isValidSubcategoryIndex(index))
			return false;

		subcategories[index] = subcategory;

		return true;
	}
	public String getSubcategoryString(int index) {
//		System.out.println("getSubcategoryString(" + index + ");");
		if (!isValidSubcategoryIndex(index))
			return null;

		return subcategories[index];
	}
}
