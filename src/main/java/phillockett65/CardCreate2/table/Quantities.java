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
 * Quantities is a simple class that is responsible for passing values to the 
 * Status tab. Only setters and tweaked getters are provided. The getters 
 * return the value as a String for convenience.
 */
package phillockett65.CardCreate2.table;

public class Quantities {

	private int elementCount = 0;
	private int neighbourCount = 0;
	private float electronShellSimilarity;
	private float electronSubshellSimilarity;
	private float electronConfigSimilarity;

	public String getElementCount() {
		return String.valueOf(elementCount);
	}
	public void setElementCount(int elementCount) {
		this.elementCount = elementCount;
	}
	public String getNeighbourCount() {
		return String.valueOf(neighbourCount);
	}
	public void setNeighbourCount(int neighbourCount) {
		this.neighbourCount = neighbourCount;
	}
	public String getElectronShellSimilarity() {
		return String.valueOf(electronShellSimilarity);
	}
	public String getElectronShellSimilarityPercent() {
		return String.valueOf(electronShellSimilarity * 100);
	}
	public void setElectronShellSimilarity(float electronShellSimilarity) {
		this.electronShellSimilarity = electronShellSimilarity;
	}
	public String getElectronSubshellSimilarity() {
		return String.valueOf(electronSubshellSimilarity);
	}
	public String getElectronSubshellSimilarityPercent() {
		return String.valueOf(electronSubshellSimilarity * 100);
	}
	public void setElectronSubshellSimilarity(float electronSubshellSimilarity) {
		this.electronSubshellSimilarity = electronSubshellSimilarity;
	}
	public String getElectronConfigSimilarity() {
		return String.valueOf(electronConfigSimilarity);
	}
	public String getElectronConfigSimilarityPercent() {
		return String.valueOf(electronConfigSimilarity * 100);
	}
	public void setElectronConfigSimilarity(float electronConfigSimilarity) {
		this.electronConfigSimilarity = electronConfigSimilarity;
	}
}
