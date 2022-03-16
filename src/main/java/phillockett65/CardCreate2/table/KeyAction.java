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
 * KeyAction is a class that is responsible for managing the action state.
 * The action state tracks whether we are currently changing a selection, 
 * moving a selection or just moving the position of the "cursor". 
 * Getters and setters are provided.
 */
package phillockett65.CardCreate2.table;

public class KeyAction {

	private final int NEITHER = 0;
	private final int SELECTING = 1;
	private final int MOVING = 2;
	private int action = NEITHER;
	private boolean shift = false;
	private boolean control = false;


	public void setShift(boolean state) {
		shift = state;
		if (shift) {
			if (action == NEITHER) {
				action = SELECTING;
			}
		} else {
			if (control)
				action = MOVING;
			else
				action = NEITHER;
		}
	}

	public void setControl(boolean state) {
		control = state;
		if (control) {
			if (action == NEITHER)
				action = MOVING;
		} else {
			if (shift)
				action = SELECTING;
			else
				action = NEITHER;
		}
	}

	public boolean isSelecting() {
		return (action == SELECTING);
	}

	public boolean isMoving() {
		return (action == MOVING);
	}

}
