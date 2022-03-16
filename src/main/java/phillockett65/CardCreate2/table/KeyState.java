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
 * KeyState is a simple class that captures the state of a key press, but also
 * indicates if there is a state change by the return value of setPressed().
 */
package phillockett65.CardCreate2.table;

import javafx.scene.input.KeyCode;

public class KeyState {

	private boolean pressed = false;
	private final KeyCode key;

	/**
	 * Constructor.
	 * 
	 * @param keyCode the associated key code for this key state.
	 */
	public KeyState(KeyCode keyCode) {
		key = keyCode;
	}

	/**
	 * Get the associated key code.
	 * 
	 * @return the associated key code.
	 */
	public KeyCode getKey() {
		return key;
	}

	/**
	 * Get the current key press state.
	 * 
	 * @return key press state.
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * Set key press state, but indicate if state changes or not.
	 * 
	 * @param press true if key pressed, false if released.
	 * @return true if state changes, false otherwise.
	 */
	public boolean setPressed(boolean press) {
		if (press == pressed)
			return false;

		pressed = press;
		
		return true;
	}
	
	
}
