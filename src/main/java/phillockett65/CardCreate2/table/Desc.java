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
 * Desc is a simple class that captures the position and size of element 
 * descriptions, such as the element symbol and atomic number, used to display
 * a description of each element on the periodic table. This is used for 
 * optimization purposes and is calculated and set whenever the tile size 
 * changes. Getters and setters are provided for each attribute.
 */
package phillockett65.CardCreate2.table;

public class Desc {
	private float size;
	private int dx;
	private int dy;

	/**
	 * Gets the font size cast as an integer for convenience.
	 * 
	 * @return font size cast as an int.
	 */
	public int getSizeInt() {
		return (int) size;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

}
