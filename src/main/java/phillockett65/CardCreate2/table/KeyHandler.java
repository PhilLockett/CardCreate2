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
 * KeyHandler is a class that is responsible for handling key pressing, 
 * managing the user selection and requesting PTable to highlight and redraw 
 * the grid.
 */
package phillockett65.CardCreate2.table;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler implements EventHandler<KeyEvent> {

	private PTable table;
	private Selection selection;

	private KeyState up;
	private KeyState down;
	private KeyState left;
	private KeyState right;

	// Key->Command Map to handled key presses. Inspired by 
	// https://programming.guide/java/function-pointers-in-java.html
	private KeyAction action;
	private Map<KeyCode, Runnable> pressess;
	private Map<KeyCode, Runnable> releases;

	/**
	 * Constructor.
	 * 
	 * Responsible for creating the user selection and initializing the link 
	 * back to table.
	 * 
	 * @param table	- the link to table.
	 * @param sel	- the Selection made by the user.
	 */
	public KeyHandler(PTable table, Selection sel) {
//		System.out.println("KeyHandler constructed.");
		this.table = table;

		up = new KeyState(KeyCode.UP);
		down = new KeyState(KeyCode.DOWN);
		left = new KeyState(KeyCode.LEFT);
		right = new KeyState(KeyCode.RIGHT);

		selection = sel;

		// Add Key to Command Mappings.
		action = new KeyAction();

		pressess = new HashMap<>();
		pressess.put(KeyCode.SHIFT, () -> setShift(true));
		pressess.put(KeyCode.CONTROL, () -> setControl(true));
		pressess.put(KeyCode.UP, () -> handlePressed(up));
		pressess.put(KeyCode.DOWN, () -> handlePressed(down));
		pressess.put(KeyCode.LEFT, () -> handlePressed(left));
		pressess.put(KeyCode.RIGHT, () -> handlePressed(right));

		releases = new HashMap<>();
		releases.put(KeyCode.SHIFT, () -> setShift(false));
		releases.put(KeyCode.CONTROL, () -> setControl(false));
		releases.put(KeyCode.UP, () -> handleReleased(up));
		releases.put(KeyCode.DOWN, () -> handleReleased(down));
		releases.put(KeyCode.LEFT, () -> handleReleased(left));
		releases.put(KeyCode.RIGHT, () -> handleReleased(right));
	}

	/**
	 * Request PTable to update the Window Title to reflect the Shift and 
	 * Control key states.
	 */
	private void updateTitle(KeyAction action) {
		if (action.isSelecting()) {
			table.augmentTitle(" - MULTI SELECT");
			return;
		}

		if (action.isMoving()) {
			table.augmentTitle(" - MOVE SELECTION");
			return;
		}

		table.augmentTitle("");
	}

	/**
	 * Use the shift state to update the current action and the Window Title.
	 */
	private void setShift(boolean pressed) {
//		System.out.println("setShift(" + pressed + ")");
		action.setShift(pressed);
		updateTitle(action);
	}

	/**
	 * Use the control state to update the current action and the Window Title.
	 */
	private void setControl(boolean pressed) {
//		System.out.println("setControl(" + pressed + ")");
		action.setControl(pressed);
		updateTitle(action);
	}

	/**
	 * Move the current position unless we are creating a selection, in which 
	 * case we leave it unchanged and use it as the non-moving point in a 
	 * selection.
	 */
	private void saveCurrent(KeyAction action) {
		if (action.isSelecting())
			return;
		selection.saveCurrent();
	}

	/**
	 * Handle arrow key press.
	 */
	private void handlePressed(KeyState key) {
//		System.out.println("handleUp()");
		if (!key.setPressed(true))
			return;		// Ignore key repeat.

		final KeyCode direction =  key.getKey();
		if (action.isMoving()) {
			if (selection.isMove(direction)) {
				table.moveSelection(direction);
				selection.move(direction);
			}

		} else {
			if (selection.isPosition(direction)) {
				table.highlightSelectedCells(false);
				selection.position(direction);
				saveCurrent(action);
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle arrow key release.
	 */
	private void handleReleased(KeyState key) {
		key.setPressed(false);
	}


	@Override
	public void handle(KeyEvent e) {
		final KeyCode key = e.getCode();

		if (e.getEventType() == KeyEvent.KEY_PRESSED)
			if (pressess.containsKey(key))
				pressess.get(key).run();

		if (e.getEventType() == KeyEvent.KEY_RELEASED)
			if (releases.containsKey(key))
				releases.get(key).run();
	}
	
}
