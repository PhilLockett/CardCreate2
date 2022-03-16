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
 * SubshellDesc is a simple class that captures the subshell name (i.e. s, p, 
 * d, f, g, h), the count of orbitals within the subshell and the maximum 
 * number of electrons within the subshell. All attributes are set by the 
 * constructor and are finalized.
 */
package phillockett65.CardCreate2.elements;

public class SubshellDesc {

	private final char Name;
	private final int Orbitals;
	private final int Electrons;

	/**
	 * Get the Name of the subshell.
	 * 
	 * @return the Name of the subshell.
	 */
	public char getName() {
		return Name;
	}

	/**
	 * Get the maximum number of Orbitals in the subshell.
	 * 
	 * @return the maximum number of Orbitals in the subshell.
	 */
	public int getOrbitals() {
		return Orbitals;
	}

	/**
	 * Get the maximum number of Electrons in the subshell.
	 * 
	 * @return the maximum number of Electrons in the subshell.
	 */
	public int getElectrons() {
		return Electrons;
	}

	/**
	 * Constructor.
	 * 
	 * @param Name     the letter to indicate the subshell (s, p, d, f, g, h).
	 * @param Orbitals number of Orbitals in the subshell.
	 */
	public SubshellDesc(char Name, int Orbitals) {
		this.Name = Name;
		this.Orbitals = Orbitals;
		this.Electrons = Orbitals * 2;
	}
}
