/*  Elements - a simple capture of chemical element information.
 *
 *  Copyright 2020 Philip Lockett.
 *
 *  This file is part of Elements.
 *
 *  Elements is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Elements is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Elements.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * ElectronDesc is a simple class that captures the electron shell number and 
 * the index to the subshell name (i.e. s, p, d, f, g, h). All attributes are 
 * set by the constructor and are finalized.
 */
package phillockett65.CardCreate2.elements;

public class ElectronDesc {

	private final int shell;
	private final int subshell;
	
	/**
	 * Get the shell Number of electron (1 - 10).
	 * 
	 * @return the shell Number of electron.
	 */
	public int getShell() {
		return shell;
	}

	/**
	 * Get the subshell Number of electron (0 to 5).
	 * 
	 * @return the subshell Number of electron.
	 */
	public int getSubshell() {
		return subshell;
	}

	/**
	 * Get the subshell Name of electron (0 to 5).
	 * 
	 * @return the subshell Name of electron.
	 */
	public char getSubshellName() {
		return Elements.getSubshellName(subshell);
	}

	/**
	 * Constructor.
	 * 
	 * @param shell - the electron shell number.
	 * @param subshell - index to subshell name.
	 */
	public ElectronDesc(int shell, int subshell) {
		this.shell = shell;
		this.subshell = subshell;
	}

	@Override
	public String toString() {
		return Integer.toString(shell) + getSubshellName();
	}
}
