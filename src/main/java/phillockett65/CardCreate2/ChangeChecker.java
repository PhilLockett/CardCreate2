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
 * ChangeChecker is a simple helper class that is used to capture a change to 
 * an integer value.
 */
package phillockett65.CardCreate2;

public class ChangeChecker {
	private int oldValue;
	private int newValue;

	/**
	 * Constructor.
	 * 
	 * @param oldVal	- The old value of the integer.
	 * @param newVal	- The new value of the integer.
	 */
	public ChangeChecker(int oldVal, int newVal) {
		oldValue = oldVal;
		newValue = newVal;
	}

	/**
	 * Constructor for convenience.
	 * 
	 * @param oldVal	- The old value of the integer.
	 * @param newVal	- The new value of the integer passed as a String.
	 */
	public ChangeChecker(int oldVal, String newVal) {
		oldValue = oldVal;
		newValue = Integer.parseInt(newVal);
	}

	/**
	 * Get the old value of the integer.
	 * 
	 * @return the old value of the integer.
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Get the new value of the integer.
	 * 
	 * @return the new value of the integer.
	 */
	public int getNewValue() {
		return newValue;
	}

	/**
	 * Indicate if the integer has changed.
	 * 
	 * @return true if the integer has changed, false otherwise.
	 */
	public boolean isChanged() {
		return oldValue != newValue;
	}

	/**
	 * Indicate if the integer has increased.
	 * 
	 * @return true if the integer has increased, false otherwise.
	 */
	public boolean isIncreased() {
		return oldValue < newValue;
	}

	/**
	 * Indicate if the integer has decreased.
	 * 
	 * @return true if the integer has decreased, false otherwise.
	 */
	public boolean isDecreased() {
		return oldValue > newValue;
	}

	/**
	 * Get the difference between the old value and the new value. A positive 
	 * return value indicates an increase in the value.
	 * 
	 * @return the difference between the old value and the new value.
	 */
	public int difference() {
		return newValue - oldValue;
	}
}
