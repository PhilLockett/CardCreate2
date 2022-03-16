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
 * Deviation is a simple class that calculates a crude deviation from the 
 * original value. The "original value" is the first total to be finalized,
 * making the first deviation zero.
 * 
 */
package phillockett65.CardCreate2.table;

public class Deviation {

	private boolean isSet = false;
	private float original = 0;
	private float current = 0;
	private float deviation = 0;

	/**
	 * Clear the current running total.
	 */
	public void reset() {
		current = 0;
	}

	/**
	 * Add value to the current running total.
	 * 
	 * @param value to add to the current running total.
	 */
	public void add(float value) {
		current += value;
	}

	/**
	 * Finalize the total and calculate the deviation. The first total to be 
	 * finalized is the original value from which all subsequent deviations are 
	 * calculated.
	 */
	public void finalize() {
		if (!isSet) {
			isSet = true;
			original = current;
		}

		deviation = (current - original) / original;
	}

	/**
	 * Get the current calculated deviation.
	 * 
	 * @return the current calculated deviation.
	 */
	public float getDeviation() {
		return deviation;
	}
}
