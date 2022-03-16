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
 * Quantifier is a class that is responsible for calculating values that 
 * attempt to quantify the quality of the current grid arrangement and are 
 * displayed by the Status tab. This is a basic, first attempt at this and 
 * will probably evolve.
 */
package phillockett65.CardCreate2.table;

import phillockett65.CardCreate2.elements.ElementConfig;

public class Quantifier {

	private Grid grid;

	private int elementCount = 0;
	private int neighbourCount = 0;
	private Deviation electronShellSimilarity;
	private Deviation electronSubshellSimilarity;
	private Deviation electronConfigSimilarity;

	/**
	 * Constructor.
	 * 
	 * @param grid reference used to calculate the quantities.
	 */
	public Quantifier(Grid grid) {
		this.grid = grid;

		electronShellSimilarity = new Deviation();
		electronSubshellSimilarity = new Deviation();
		electronConfigSimilarity = new Deviation();
	}

	/**
	 * Reset the reference to the grid to be analyzed.
	 * 
	 * @param grid	the new grid to analyze.
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Calculate the values that attempt to quantify the quality of the 
	 * current grid arrangement. 
	 */
	private void update() {
//		System.out.println("update()");

		elementCount = 0;
		neighbourCount = 0;
		electronShellSimilarity.reset();
		electronSubshellSimilarity.reset();
		electronConfigSimilarity.reset();

		final int rows = grid.getRows();
		final int cols = grid.getCols();

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid.getCell(r, c);
				if (cell.isBlank())
					continue;

				elementCount++;
				Cell adjacent;
				if (r > 0) {
					adjacent = grid.getCell(r-1, c);
					if (!adjacent.isBlank()) {
						// Neighbour to the North.
						neighbourCount++;
						calcSimilarities(cell.getE(), adjacent.getE());
					}
				}

				if (r < rows-1) {
					adjacent = grid.getCell(r+1, c);
					if (!adjacent.isBlank()) {
						// Neighbour to the South.
						neighbourCount++;
						calcSimilarities(cell.getE(), adjacent.getE());
					}
				}

				if (c > 0) {
					adjacent = grid.getCell(r, c-1);
					if (!adjacent.isBlank()) {
						// Neighbour to the West.
						neighbourCount++;
						calcSimilarities(cell.getE(), adjacent.getE());
					}
				}

				if (c < cols-1) {
					adjacent = grid.getCell(r, c+1);
					if (!adjacent.isBlank()) {
						// Neighbour to the East.
						neighbourCount++;
						calcSimilarities(cell.getE(), adjacent.getE());
					}
				}
			}
		}

		electronShellSimilarity.finalize();
		electronSubshellSimilarity.finalize();
		electronConfigSimilarity.finalize();
	}

	/**
	 * Calculate the Similarities of the lists of interest and add them to the 
	 * running totals.
	 * 
	 * @param e			current Element.
	 * @param adjacent	neighboring Element.
	 */
	private void calcSimilarities(ElementConfig e, ElementConfig adjacent) {
		electronShellSimilarity.add(calcSimilarity(e.getElectronShellCounts(), adjacent.getElectronShellCounts()));
		electronSubshellSimilarity.add(calcSimilarity(e.getElectronSubshellCounts(), adjacent.getElectronSubshellCounts()));
		electronConfigSimilarity.add(calcSimilarity(e.getElectronConfigurationCounts(), adjacent.getElectronConfigurationCounts()));
	}

	/**
	 * Calculate the similarity of two lists of numbers.
	 * 
	 * @param a first list.
	 * @param b second list.
	 * @return the similarity of the two lists.
	 */
	private float calcSimilarity(int[] a, int[] b) {
		final int aLen = a.length;
		final int bLen = b.length;
		float current = 0;
		int diff = 0;
		if (aLen < bLen) {
			for (int s = 0; s < aLen; ++s) {
				diff = a[s] - b[s];
				if (diff < 0)
					diff *= -1;
				current += diff;
			}
			for (int s = aLen; s < bLen; ++s)
				current += b[s];

			current /= 2 * bLen;
		} else {
			for (int s = 0; s < bLen; ++s) {
				diff = a[s] - b[s];
				if (diff < 0)
					diff *= -1;
				current += diff;
			}
			for (int s = bLen; s < aLen; ++s)
				current += a[s];

			current /= 2 * aLen;
		}

		return current;
	}

	/**
	 * Get the latest Quantities that indicate the quality of the current grid 
	 * arrangement. A recalculation is performed if necessary.
	 * 
	 * @return
	 */
	public Quantities getQuantities() {
		update();

		Quantities quantities = new Quantities();

		quantities.setElementCount(elementCount);
		quantities.setNeighbourCount(neighbourCount);
		quantities.setElectronShellSimilarity(electronShellSimilarity.getDeviation());
		quantities.setElectronSubshellSimilarity(electronSubshellSimilarity.getDeviation());
		quantities.setElectronConfigSimilarity(electronConfigSimilarity.getDeviation());

		return quantities;
	}

}
