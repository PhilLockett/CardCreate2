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
 * Elements is a static class that contains a number of tables and getters to 
 * access the data. These tables are:
 * 
 * Subcategories
 * A list of the names of the chemical subcategories AKA Element Category.
 * 
 * SubshellDescriptions
 * A table containing subshell descriptions, which includes the name (s, p, d, 
 * f...), the number of orbitals within the subshell and the maximum number of
 * electrons within the subshell.
 * 
 * ElectronConfigurations
 * A table containing electron configuration descriptions, which includes the
 * electron shell number and an index into SubshellDescriptions to obtain the 
 * subshell name.
 * 
 * ElectronSubshells
 * A table containing electron subshell descriptions, which includes the
 * electron shell number and an index into SubshellDescriptions to obtain the 
 * subshell name.
 * 
 * MapConfToSubs
 * Map to find the corresponding ElectronSubshells index from the 
 * ElectronConfigurations index.
 * 
 * MapSubsToConf
 * Map to find the corresponding ElectronConfigurations index from the 
 * ElectronSubshells index.
 * 
 * FinalCounts
 * Maximum number of electrons within each subshell of the electron 
 * configuration.
 * 
 * ElementConfigurations
 * A table containing chemical element descriptions, which includes the 
 * Chemical element symbol, Chemical element name, Atomic Number, Group, 
 * Group32, Period, Atomic Weight, Density, Melting point, Boiling point, 
 * Specific heat capacity, Electronegativity, Abundance, Subcategory, Electron 
 * Configuration generated using Madelung's rule, Observed Electron 
 * Configuration, Electron count within each subshell and Electron count 
 * within each shell for each element.
 */
package phillockett65.CardCreate2.elements;

public class Elements {

	private static final String[] Subcategories = {
			"UNDEFINED",
			"Metalloid",
			"Unknown",
			"Alkali Metal",
			"Alkaline Earth Metal",
			"Lanthanide",
			"Actinide",
			"Transition Metal",
			"Post Transition Metal",
			"Reactive Nonmetal",
			"Noble Gas"
	};

	/**
	 * Get the sub-category as a string using the index from getSubcategory().
	 * 
	 * @param index of the Subcategory.
	 * @return the string name of the Subcategory.
	 */
	public static String getSubcategory(int index) {
		return Subcategories[index];
	}

	/**
	 * Get the number of sub-categories.
	 * 
	 * @return the sub-category count.
	 */
	public static int getSubcategoryCount() {
		return Subcategories.length;
	}

	private static final SubshellDesc[] SubshellDescriptions = {
		new SubshellDesc('s', 1),    // 0
		new SubshellDesc('p', 3),    // 1
		new SubshellDesc('d', 5),    // 2
		new SubshellDesc('f', 7),    // 3
		new SubshellDesc('g', 9),    // 4
		new SubshellDesc('h', 11),   // 5
	};

	/**
	 * Get the subshell description for the indexed subshell.
	 * 
	 * @param index of the subshell description.
	 * @return the subshell description.
	 */
	public static SubshellDesc getSubshellDescription(int index) {
		return SubshellDescriptions[index];
	}

	/**
	 * Get the name for the indexed subshell.
	 * 
	 * @param index of the subshell description.
	 * @return the name of the subshell.
	 */
	public static char getSubshellName(int index) {
		return SubshellDescriptions[index].getName();
	}

	/**
	 * Get the maximum number of Orbitals for the indexed subshell.
	 * 
	 * @param index of the subshell description.
	 * @return the maximum number of Orbitals of the subshell.
	 */
	public static int getSubshellOrbitals(int index) {
		return SubshellDescriptions[index].getOrbitals();
	}

	/**
	 * Get the maximum number of Electrons for the indexed subshell.
	 * 
	 * @param index of the subshell description.
	 * @return the maximum number of Electrons of the subshell.
	 */
	public static int getSubshellElectrons(int index) {
		return SubshellDescriptions[index].getElectrons();
	}


	private static final ElectronDesc[] ElectronConfigurations = {
		new ElectronDesc(1, 0),    // 1s
		new ElectronDesc(2, 0),    // 2s
		new ElectronDesc(2, 1),    // 2p
		new ElectronDesc(3, 0),    // 3s
		new ElectronDesc(3, 1),    // 3p
		new ElectronDesc(4, 0),    // 4s
		new ElectronDesc(3, 2),    // 3d
		new ElectronDesc(4, 1),    // 4p
		new ElectronDesc(5, 0),    // 5s
		new ElectronDesc(4, 2),    // 4d
		new ElectronDesc(5, 1),    // 5p
		new ElectronDesc(6, 0),    // 6s
		new ElectronDesc(4, 3),    // 4f
		new ElectronDesc(5, 2),    // 5d
		new ElectronDesc(6, 1),    // 6p
		new ElectronDesc(7, 0),    // 7s
		new ElectronDesc(5, 3),    // 5f
		new ElectronDesc(6, 2),    // 6d
		new ElectronDesc(7, 1),    // 7p
		new ElectronDesc(8, 0),    // 8s
		new ElectronDesc(5, 4),    // 5g
		new ElectronDesc(6, 3),    // 6f
		new ElectronDesc(7, 2),    // 7d
		new ElectronDesc(8, 1),    // 8p
		new ElectronDesc(9, 0),    // 9s
		new ElectronDesc(6, 4),    // 6g
		new ElectronDesc(7, 3),    // 7f
		new ElectronDesc(8, 2),    // 8d
		new ElectronDesc(9, 1),    // 9p
		new ElectronDesc(10, 0),   // 10s
	};

	/**
	 * Get the electron description for the indexed electron configuration.
	 * 
	 * @param index of the electron configuration.
	 * @return the electron description of the electron configuration.
	 */
	public static ElectronDesc getElectronConfig(int index) {
		return ElectronConfigurations[index];
	}

	/**
	 * Get the electron description String for the indexed electron configuration.
	 * 
	 * @param index of the electron configuration.
	 * @return the electron description String of the electron configuration.
	 */
	public static String getElectronConfigString(int index) {
		return ElectronConfigurations[index].toString();
	}

	/**
	 * Get the electron shell for the indexed electron configuration.
	 * 
	 * @param index of the electron configuration.
	 * @return the electron shell of the electron configuration.
	 */
	public static int getElectronConfigShell(int index) {
		return ElectronConfigurations[index].getShell();
	}

	/**
	 * Get the electron subshell for the indexed electron configuration.
	 * 
	 * @param index of the electron configuration.
	 * @return the electron subshell of the electron configuration.
	 */
	public static int getElectronConfigSubshell(int index) {
		return ElectronConfigurations[index].getSubshell();
	}


	private static final ElectronDesc[] ElectronSubshells = {
		new ElectronDesc(1, 0),    // 1s
		new ElectronDesc(2, 0),    // 2s
		new ElectronDesc(2, 1),    // 2p
		new ElectronDesc(3, 0),    // 3s
		new ElectronDesc(3, 1),    // 3p
		new ElectronDesc(3, 2),    // 3d
		new ElectronDesc(4, 0),    // 4s
		new ElectronDesc(4, 1),    // 4p
		new ElectronDesc(4, 2),    // 4d
		new ElectronDesc(4, 3),    // 4f
		new ElectronDesc(5, 0),    // 5s
		new ElectronDesc(5, 1),    // 5p
		new ElectronDesc(5, 2),    // 5d
		new ElectronDesc(5, 3),    // 5f
		new ElectronDesc(5, 4),    // 5g
		new ElectronDesc(6, 0),    // 6s
		new ElectronDesc(6, 1),    // 6p
		new ElectronDesc(6, 2),    // 6d
		new ElectronDesc(6, 3),    // 6f
		new ElectronDesc(7, 0),    // 7s
		new ElectronDesc(7, 1),    // 7p
		new ElectronDesc(7, 2),    // 7d
		new ElectronDesc(8, 0),    // 8s
		new ElectronDesc(8, 1),    // 8p
		new ElectronDesc(9, 0),    // 9s
		new ElectronDesc(9, 1),    // 9p
		};

	/**
	 * Get the electron description for the indexed electron subshell.
	 * 
	 * @param index of the electron subshell.
	 * @return the electron description of the electron subshell.
	 */
	public static ElectronDesc getElectronSubshell(int index) {
		return ElectronSubshells[index];
	}

	/**
	 * Get the electron description String for the indexed electron subshell.
	 * 
	 * @param index of the electron subshell.
	 * @return the electron description String of the electron subshell.
	 */
	public static String getElectronSubshellString(int index) {
		return ElectronSubshells[index].toString();
	}

	/**
	 * Get the electron shell for the indexed electron subshell.
	 * 
	 * @param index of the electron subshell.
	 * @return the electron shell of the electron subshell.
	 */
	public static int getElectronSubshellShell(int index) {
		return ElectronSubshells[index].getShell();
	}

	/**
	 * Get the electron subshell for the indexed electron subshell.
	 * 
	 * @param index of the electron subshell.
	 * @return the electron subshell of the electron subshell.
	 */
	public static int getElectronSubshellSubshell(int index) {
		return ElectronSubshells[index].getSubshell();
	}


	// Map ElectronConfigurations[] indices to ElectronSubshells[].
	private static final int[] MapConfToSubs = {
		0, 1, 2, 3, 4, 6, 5, 7, 10, 8, 11, 15, 9, 12, 16, 19, 13, 17, 20, 22,
		14, 18, 21, 23, 24, -1, -1, -1, 25, -1
	};
	/**
	 * Get the electron subshell for the given electron configuration.
	 * 
	 * @param config.
	 * @return the electron subshell.
	 */
	public static int getElectronSubshellFromConfig(int config) {
		return MapConfToSubs[config];
	}

	// Map ElectronSubshells[] indices to ElectronConfigurations[].
	private static final int[] MapSubsToConf = {
		0, 1, 2, 3, 4, 6, 5, 7, 9, 12, 8, 10, 13, 16, 20, 11, 14, 17, 21, 15,
		18, 22, 19, 23, 24, 28
	};
	/**
	 * Get the electron configuration for the given electron subshell.
	 * 
	 * @param subshell.
	 * @return the electron configuration.
	 */
	public static int getElectronConfigFromSubshell(int subshell) {
		return MapSubsToConf[subshell];
	}

	// Full Electron Configuration.
	private static final int[] FinalCounts = {
		2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 6, 2, 18,
		14, 10, 6, 2, 18, 14, 10, 6, 2
	};
	/**
	 * Get the electron configuration for the given electron subshell.
	 * 
	 * @param index.
	 * @return the full electron configuration.
	 */
	public static int getFullElectronConfig(int index) {
		return FinalCounts[index];
	}


	private static final ElementConfig[] ElementConfigurations = {
		new ElementConfig(
			"H", "Hydrogen",
			1, 1, 1, 1,
			1.008F, 8.988e-05F,
			14.01F, 20.28F,
			14.304F, 2.2F,
			1400F,
			9,
			new int[]{ 1 },
			new int[]{ 1 },
			new int[]{ 1 },
			new int[]{ 1 }),

		new ElementConfig(
			"He", "Helium",
			2, 18, 32, 1,
			4.0026F, 0.0001785F,
			0F, 4.22F,
			5.193F, 0F,
			0.008F,
			10,
			new int[]{ 2 },
			new int[]{ 2 },
			new int[]{ 2 },
			new int[]{ 2 }),

		new ElementConfig(
			"Li", "Lithium",
			3, 1, 1, 2,
			6.94F, 0.534F,
			453.69F, 1560F,
			3.582F, 0.98F,
			20F,
			3,
			new int[]{ 2, 1 },
			new int[]{ 2, 1 },
			new int[]{ 2, 1 },
			new int[]{ 2, 1 }),

		new ElementConfig(
			"Be", "Beryllium",
			4, 2, 2, 2,
			9.01218F, 1.85F,
			1560F, 2742F,
			1.825F, 1.57F,
			2.8F,
			4,
			new int[]{ 2, 2 },
			new int[]{ 2, 2 },
			new int[]{ 2, 2 },
			new int[]{ 2, 2 }),

		new ElementConfig(
			"B", "Boron",
			5, 13, 27, 2,
			10.81F, 2.34F,
			2349F, 4200F,
			1.026F, 2.04F,
			10F,
			1,
			new int[]{ 2, 2, 1 },
			new int[]{ 2, 2, 1 },
			new int[]{ 2, 2, 1 },
			new int[]{ 2, 3 }),

		new ElementConfig(
			"C", "Carbon",
			6, 14, 28, 2,
			12.011F, 2.267F,
			3800F, 4300F,
			0.709F, 2.55F,
			200F,
			9,
			new int[]{ 2, 2, 2 },
			new int[]{ 2, 2, 2 },
			new int[]{ 2, 2, 2 },
			new int[]{ 2, 4 }),

		new ElementConfig(
			"N", "Nitrogen",
			7, 15, 29, 2,
			14.007F, 0.0012506F,
			63.15F, 77.36F,
			1.04F, 3.04F,
			19F,
			9,
			new int[]{ 2, 2, 3 },
			new int[]{ 2, 2, 3 },
			new int[]{ 2, 2, 3 },
			new int[]{ 2, 5 }),

		new ElementConfig(
			"O", "Oxygen",
			8, 16, 30, 2,
			15.999F, 0.001429F,
			54.36F, 90.2F,
			0.918F, 3.44F,
			461000F,
			9,
			new int[]{ 2, 2, 4 },
			new int[]{ 2, 2, 4 },
			new int[]{ 2, 2, 4 },
			new int[]{ 2, 6 }),

		new ElementConfig(
			"F", "Fluorine",
			9, 17, 31, 2,
			18.9984F, 0.001696F,
			53.53F, 85.03F,
			0.824F, 3.98F,
			585F,
			9,
			new int[]{ 2, 2, 5 },
			new int[]{ 2, 2, 5 },
			new int[]{ 2, 2, 5 },
			new int[]{ 2, 7 }),

		new ElementConfig(
			"Ne", "Neon",
			10, 18, 32, 2,
			20.1797F, 0.0008999F,
			24.56F, 27.07F,
			1.03F, 0F,
			0.005F,
			10,
			new int[]{ 2, 2, 6 },
			new int[]{ 2, 2, 6 },
			new int[]{ 2, 2, 6 },
			new int[]{ 2, 8 }),

		new ElementConfig(
			"Na", "Sodium",
			11, 1, 1, 3,
			22.9898F, 0.971F,
			370.87F, 1156F,
			1.228F, 0.93F,
			23600F,
			3,
			new int[]{ 2, 2, 6, 1 },
			new int[]{ 2, 2, 6, 1 },
			new int[]{ 2, 2, 6, 1 },
			new int[]{ 2, 8, 1 }),

		new ElementConfig(
			"Mg", "Magnesium",
			12, 2, 2, 3,
			24.305F, 1.738F,
			923F, 1363F,
			1.023F, 1.31F,
			23300F,
			4,
			new int[]{ 2, 2, 6, 2 },
			new int[]{ 2, 2, 6, 2 },
			new int[]{ 2, 2, 6, 2 },
			new int[]{ 2, 8, 2 }),

		new ElementConfig(
			"Al", "Aluminium",
			13, 13, 27, 3,
			26.9815F, 2.698F,
			933.47F, 2792F,
			0.897F, 1.61F,
			82300F,
			8,
			new int[]{ 2, 2, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 1 },
			new int[]{ 2, 8, 3 }),

		new ElementConfig(
			"Si", "Silicon",
			14, 14, 28, 3,
			28.085F, 2.3296F,
			1687F, 3538F,
			0.705F, 1.9F,
			282000F,
			1,
			new int[]{ 2, 2, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 2 },
			new int[]{ 2, 8, 4 }),

		new ElementConfig(
			"P", "Phosphorus",
			15, 15, 29, 3,
			30.9738F, 1.82F,
			317.3F, 550F,
			0.769F, 2.19F,
			1050F,
			9,
			new int[]{ 2, 2, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 3 },
			new int[]{ 2, 8, 5 }),

		new ElementConfig(
			"S", "Sulfur",
			16, 16, 30, 3,
			32.06F, 2.067F,
			388.36F, 717.87F,
			0.71F, 2.58F,
			350F,
			9,
			new int[]{ 2, 2, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 4 },
			new int[]{ 2, 8, 6 }),

		new ElementConfig(
			"Cl", "Chlorine",
			17, 17, 31, 3,
			35.45F, 0.003214F,
			171.6F, 239.11F,
			0.479F, 3.16F,
			145F,
			9,
			new int[]{ 2, 2, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 5 },
			new int[]{ 2, 8, 7 }),

		new ElementConfig(
			"Ar", "Argon",
			18, 18, 32, 3,
			39.948F, 0.0017837F,
			83.8F, 87.3F,
			0.52F, 0F,
			3.5F,
			10,
			new int[]{ 2, 2, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6 },
			new int[]{ 2, 8, 8 }),

		new ElementConfig(
			"K", "Potassium",
			19, 1, 1, 4,
			39.0983F, 0.862F,
			336.53F, 1032F,
			0.757F, 0.82F,
			20900F,
			3,
			new int[]{ 2, 2, 6, 2, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 0, 1 },
			new int[]{ 2, 8, 8, 1 }),

		new ElementConfig(
			"Ca", "Calcium",
			20, 2, 2, 4,
			40.078F, 1.54F,
			1115F, 1757F,
			0.647F, 1F,
			41500F,
			4,
			new int[]{ 2, 2, 6, 2, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 0, 2 },
			new int[]{ 2, 8, 8, 2 }),

		new ElementConfig(
			"Sc", "Scandium",
			21, 3, 3, 4,
			44.9559F, 2.989F,
			1814F, 3109F,
			0.568F, 1.36F,
			22F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 1, 2 },
			new int[]{ 2, 8, 9, 2 }),

		new ElementConfig(
			"Ti", "Titanium",
			22, 4, 18, 4,
			47.867F, 4.54F,
			1941F, 3560F,
			0.523F, 1.54F,
			5650F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 2 },
			new int[]{ 2, 8, 10, 2 }),

		new ElementConfig(
			"V", "Vanadium",
			23, 5, 19, 4,
			50.9415F, 6.11F,
			2183F, 3680F,
			0.489F, 1.63F,
			120F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 3, 2 },
			new int[]{ 2, 8, 11, 2 }),

		new ElementConfig(
			"Cr", "Chromium",
			24, 6, 20, 4,
			51.9961F, 7.15F,
			2180F, 2944F,
			0.449F, 1.66F,
			102F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 6, 1, 5 },
			new int[]{ 2, 2, 6, 2, 6, 5, 1 },
			new int[]{ 2, 8, 13, 1 }),

		new ElementConfig(
			"Mn", "Manganese",
			25, 7, 21, 4,
			54.938F, 7.44F,
			1519F, 2334F,
			0.479F, 1.55F,
			950F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 5, 2 },
			new int[]{ 2, 8, 13, 2 }),

		new ElementConfig(
			"Fe", "Iron",
			26, 8, 22, 4,
			55.845F, 7.874F,
			1811F, 3134F,
			0.449F, 1.83F,
			56300F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 6, 2 },
			new int[]{ 2, 8, 14, 2 }),

		new ElementConfig(
			"Co", "Cobalt",
			27, 9, 23, 4,
			58.9332F, 8.86F,
			1768F, 3200F,
			0.421F, 1.88F,
			25F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 7, 2 },
			new int[]{ 2, 8, 15, 2 }),

		new ElementConfig(
			"Ni", "Nickel",
			28, 10, 24, 4,
			58.6934F, 8.912F,
			1728F, 3186F,
			0.444F, 1.91F,
			84F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 8 },
			new int[]{ 2, 2, 6, 2, 6, 8, 2 },
			new int[]{ 2, 8, 16, 2 }),

		new ElementConfig(
			"Cu", "Copper",
			29, 11, 25, 4,
			63.546F, 8.96F,
			1357.77F, 2835F,
			0.385F, 1.9F,
			60F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 1, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 1 },
			new int[]{ 2, 8, 18, 1 }),

		new ElementConfig(
			"Zn", "Zinc",
			30, 12, 26, 4,
			65.38F, 7.134F,
			692.88F, 1180F,
			0.388F, 1.65F,
			70F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2 },
			new int[]{ 2, 8, 18, 2 }),

		new ElementConfig(
			"Ga", "Gallium",
			31, 13, 27, 4,
			69.723F, 5.907F,
			302.915F, 2673F,
			0.371F, 1.81F,
			19F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 1 },
			new int[]{ 2, 8, 18, 3 }),

		new ElementConfig(
			"Ge", "Germanium",
			32, 14, 28, 4,
			72.63F, 5.323F,
			1211.4F, 3106F,
			0.32F, 2.01F,
			1.5F,
			1,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 2 },
			new int[]{ 2, 8, 18, 4 }),

		new ElementConfig(
			"As", "Arsenic",
			33, 15, 29, 4,
			74.9216F, 5.776F,
			1090F, 887F,
			0.329F, 2.18F,
			1.8F,
			1,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 3 },
			new int[]{ 2, 8, 18, 5 }),

		new ElementConfig(
			"Se", "Selenium",
			34, 16, 30, 4,
			78.971F, 4.809F,
			453F, 958F,
			0.321F, 2.55F,
			0.05F,
			9,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 4 },
			new int[]{ 2, 8, 18, 6 }),

		new ElementConfig(
			"Br", "Bromine",
			35, 17, 31, 4,
			79.904F, 3.122F,
			265.8F, 332F,
			0.474F, 2.96F,
			2.4F,
			9,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 5 },
			new int[]{ 2, 8, 18, 7 }),

		new ElementConfig(
			"Kr", "Krypton",
			36, 18, 32, 4,
			83.798F, 0.003733F,
			115.79F, 119.93F,
			0.248F, 3F,
			1F,
			10,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6 },
			new int[]{ 2, 8, 18, 8 }),

		new ElementConfig(
			"Rb", "Rubidium",
			37, 1, 1, 5,
			85.4678F, 1.532F,
			312.46F, 961F,
			0.363F, 0.82F,
			90F,
			3,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 0, 0, 1 },
			new int[]{ 2, 8, 18, 8, 1 }),

		new ElementConfig(
			"Sr", "Strontium",
			38, 2, 2, 5,
			87.62F, 2.64F,
			1050F, 1655F,
			0.301F, 0.95F,
			370F,
			4,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 8, 2 }),

		new ElementConfig(
			"Y", "Yttrium",
			39, 3, 3, 5,
			88.9058F, 4.469F,
			1799F, 3609F,
			0.298F, 1.22F,
			33F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 9, 2 }),

		new ElementConfig(
			"Zr", "Zirconium",
			40, 4, 18, 5,
			91.224F, 6.506F,
			2128F, 4682F,
			0.278F, 1.33F,
			165F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 2, 0, 2 },
			new int[]{ 2, 8, 18, 10, 2 }),

		new ElementConfig(
			"Nb", "Niobium",
			41, 5, 19, 5,
			92.9064F, 8.57F,
			2750F, 5017F,
			0.265F, 1.6F,
			20F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 4, 0, 1 },
			new int[]{ 2, 8, 18, 12, 1 }),

		new ElementConfig(
			"Mo", "Molybdenum",
			42, 6, 20, 5,
			95.95F, 10.22F,
			2896F, 4912F,
			0.251F, 2.16F,
			1.2F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 5, 0, 1 },
			new int[]{ 2, 8, 18, 13, 1 }),

		new ElementConfig(
			"Tc", "Technetium",
			43, 7, 21, 5,
			0F, 11.5F,
			2430F, 4538F,
			0F, 1.9F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 5, 0, 2 },
			new int[]{ 2, 8, 18, 13, 2 }),

		new ElementConfig(
			"Ru", "Ruthenium",
			44, 8, 22, 5,
			101.07F, 12.37F,
			2607F, 4423F,
			0.238F, 2.2F,
			0.001F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1, 7 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 7, 0, 1 },
			new int[]{ 2, 8, 18, 15, 1 }),

		new ElementConfig(
			"Rh", "Rhodium",
			45, 9, 23, 5,
			102.906F, 12.41F,
			2237F, 3968F,
			0.243F, 2.28F,
			0.001F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1, 8 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 8, 0, 1 },
			new int[]{ 2, 8, 18, 16, 1 }),

		new ElementConfig(
			"Pd", "Palladium",
			46, 10, 24, 5,
			106.42F, 12.02F,
			1828.05F, 3236F,
			0.244F, 2.2F,
			0.015F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 0, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10 },
			new int[]{ 2, 8, 18, 18 }),

		new ElementConfig(
			"Ag", "Silver",
			47, 11, 25, 5,
			107.868F, 10.501F,
			1234.93F, 2435F,
			0.235F, 1.93F,
			0.075F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 1, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 1 },
			new int[]{ 2, 8, 18, 18, 1 }),

		new ElementConfig(
			"Cd", "Cadmium",
			48, 12, 26, 5,
			112.414F, 8.69F,
			594.22F, 1040F,
			0.232F, 1.69F,
			0.159F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2 },
			new int[]{ 2, 8, 18, 18, 2 }),

		new ElementConfig(
			"In", "Indium",
			49, 13, 27, 5,
			114.818F, 7.31F,
			429.75F, 2345F,
			0.233F, 1.78F,
			0.25F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 1 },
			new int[]{ 2, 8, 18, 18, 3 }),

		new ElementConfig(
			"Sn", "Tin",
			50, 14, 28, 5,
			118.71F, 7.287F,
			505.08F, 2875F,
			0.228F, 1.96F,
			2.3F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 2 },
			new int[]{ 2, 8, 18, 18, 4 }),

		new ElementConfig(
			"Sb", "Antimony",
			51, 15, 29, 5,
			121.76F, 6.685F,
			903.78F, 1860F,
			0.207F, 2.05F,
			0.2F,
			1,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 3 },
			new int[]{ 2, 8, 18, 18, 5 }),

		new ElementConfig(
			"Te", "Tellurium",
			52, 16, 30, 5,
			127.6F, 6.232F,
			722.66F, 1261F,
			0.202F, 2.1F,
			0.001F,
			1,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 4 },
			new int[]{ 2, 8, 18, 18, 6 }),

		new ElementConfig(
			"I", "Iodine",
			53, 17, 31, 5,
			126.904F, 4.93F,
			386.85F, 457.4F,
			0.214F, 2.66F,
			0.45F,
			9,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 5 },
			new int[]{ 2, 8, 18, 18, 7 }),

		new ElementConfig(
			"Xe", "Xenon",
			54, 18, 32, 5,
			131.293F, 0.005887F,
			161.4F, 165.03F,
			0.158F, 2.6F,
			3F,
			10,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 6 },
			new int[]{ 2, 8, 18, 18, 8 }),

		new ElementConfig(
			"Cs", "Caesium",
			55, 1, 1, 6,
			132.905F, 1.873F,
			301.59F, 944F,
			0.242F, 0.79F,
			3F,
			3,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 6, 0, 0, 0, 1 },
			new int[]{ 2, 8, 18, 18, 8, 1 }),

		new ElementConfig(
			"Ba", "Barium",
			56, 2, 2, 6,
			137.327F, 3.594F,
			1000F, 2170F,
			0.204F, 0.89F,
			425F,
			4,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 18, 8, 2 }),

		new ElementConfig(
			"La", "Lanthanum",
			57, 3, 3, 6,
			138.905F, 6.145F,
			1193F, 3737F,
			0.195F, 1.1F,
			39F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 0, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 0, 2, 6, 1, 0, 0, 2 },
			new int[]{ 2, 8, 18, 18, 9, 2 }),

		new ElementConfig(
			"Ce", "Cerium",
			58, 0, 4, 6,
			140.116F, 6.77F,
			1068F, 3716F,
			0.192F, 1.12F,
			66.5F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 1, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 1, 2, 6, 1, 0, 0, 2 },
			new int[]{ 2, 8, 18, 19, 9, 2 }),

		new ElementConfig(
			"Pr", "Praseodymium",
			59, 0, 5, 6,
			140.908F, 6.773F,
			1208F, 3793F,
			0.193F, 1.13F,
			9.2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 3, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 21, 8, 2 }),

		new ElementConfig(
			"Nd", "Neodymium",
			60, 0, 6, 6,
			144.242F, 7.007F,
			1297F, 3347F,
			0.19F, 1.14F,
			41.5F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 4, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 22, 8, 2 }),

		new ElementConfig(
			"Pm", "Promethium",
			61, 0, 7, 6,
			0F, 7.26F,
			1315F, 3273F,
			0F, 1.13F,
			2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 5, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 23, 8, 2 }),

		new ElementConfig(
			"Sm", "Samarium",
			62, 0, 8, 6,
			150.36F, 7.52F,
			1345F, 2067F,
			0.197F, 1.17F,
			7.05F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 6, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 24, 8, 2 }),

		new ElementConfig(
			"Eu", "Europium",
			63, 0, 9, 6,
			151.964F, 5.243F,
			1099F, 1802F,
			0.182F, 1.2F,
			2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 7, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 25, 8, 2 }),

		new ElementConfig(
			"Gd", "Gadolinium",
			64, 0, 10, 6,
			157.25F, 7.895F,
			1585F, 3546F,
			0.236F, 1.2F,
			6.2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 7, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 7, 2, 6, 1, 0, 0, 2 },
			new int[]{ 2, 8, 18, 25, 9, 2 }),

		new ElementConfig(
			"Tb", "Terbium",
			65, 0, 11, 6,
			158.925F, 8.229F,
			1629F, 3503F,
			0.182F, 1.2F,
			1.2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 9, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 27, 8, 2 }),

		new ElementConfig(
			"Dy", "Dysprosium",
			66, 0, 12, 6,
			162.5F, 8.55F,
			1680F, 2840F,
			0.17F, 1.22F,
			5.2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 10, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 28, 8, 2 }),

		new ElementConfig(
			"Ho", "Holmium",
			67, 0, 13, 6,
			164.93F, 8.795F,
			1734F, 2993F,
			0.165F, 1.23F,
			1.3F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 11 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 11 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 11, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 29, 8, 2 }),

		new ElementConfig(
			"Er", "Erbium",
			68, 0, 14, 6,
			167.259F, 9.066F,
			1802F, 3141F,
			0.168F, 1.24F,
			3.5F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 12 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 12 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 12, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 30, 8, 2 }),

		new ElementConfig(
			"Tm", "Thulium",
			69, 0, 15, 6,
			168.934F, 9.321F,
			1818F, 2223F,
			0.16F, 1.25F,
			0.52F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 13 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 13 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 13, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 31, 8, 2 }),

		new ElementConfig(
			"Yb", "Ytterbium",
			70, 0, 16, 6,
			173.045F, 6.965F,
			1097F, 1469F,
			0.155F, 1.1F,
			3.2F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 0, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 8, 2 }),

		new ElementConfig(
			"Lu", "Lutetium",
			71, 0, 17, 6,
			174.967F, 9.84F,
			1925F, 3675F,
			0.154F, 1.27F,
			0.8F,
			5,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 1, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 9, 2 }),

		new ElementConfig(
			"Hf", "Hafnium",
			72, 4, 18, 6,
			178.49F, 13.31F,
			2506F, 4876F,
			0.144F, 1.3F,
			3F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 2, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 10, 2 }),

		new ElementConfig(
			"Ta", "Tantalum",
			73, 5, 19, 6,
			180.948F, 16.654F,
			3290F, 5731F,
			0.14F, 1.5F,
			2F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 3, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 11, 2 }),

		new ElementConfig(
			"W", "Tungsten",
			74, 6, 20, 6,
			183.84F, 19.25F,
			3695F, 5828F,
			0.132F, 2.36F,
			1.3F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 4, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 12, 2 }),

		new ElementConfig(
			"Re", "Rhenium",
			75, 7, 21, 6,
			186.207F, 21.02F,
			3459F, 5869F,
			0.137F, 1.9F,
			7F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 5, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 13, 2 }),

		new ElementConfig(
			"Os", "Osmium",
			76, 8, 22, 6,
			190.23F, 22.61F,
			3306F, 5285F,
			0.13F, 2.2F,
			0.002F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 14, 2 }),

		new ElementConfig(
			"Ir", "Iridium",
			77, 9, 23, 6,
			192.217F, 22.56F,
			2719F, 4701F,
			0.131F, 2.2F,
			0.001F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 7 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 7, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 15, 2 }),

		new ElementConfig(
			"Pt", "Platinum",
			78, 10, 24, 6,
			195.084F, 21.46F,
			2041.4F, 4098F,
			0.133F, 2.28F,
			0.005F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 1, 14, 9 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 9, 0, 0, 1 },
			new int[]{ 2, 8, 18, 32, 17, 1 }),

		new ElementConfig(
			"Au", "Gold",
			79, 11, 25, 6,
			196.967F, 19.282F,
			1337.33F, 3129F,
			0.129F, 2.54F,
			0.004F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 9 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 1, 14, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 1 },
			new int[]{ 2, 8, 18, 32, 18, 1 }),

		new ElementConfig(
			"Hg", "Mercury",
			80, 12, 26, 6,
			200.592F, 13.5336F,
			234.43F, 629.88F,
			0.14F, 2F,
			0.085F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 18, 2 }),

		new ElementConfig(
			"Tl", "Thallium",
			81, 13, 27, 6,
			204.38F, 11.85F,
			577F, 1746F,
			0.129F, 1.62F,
			0.85F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 1 },
			new int[]{ 2, 8, 18, 32, 18, 3 }),

		new ElementConfig(
			"Pb", "Lead",
			82, 14, 28, 6,
			207.2F, 11.342F,
			600.61F, 2022F,
			0.129F, 1.87F,
			14F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 2 },
			new int[]{ 2, 8, 18, 32, 18, 4 }),

		new ElementConfig(
			"Bi", "Bismuth",
			83, 15, 29, 6,
			208.98F, 9.807F,
			544.7F, 1837F,
			0.122F, 2.02F,
			0.009F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 3 },
			new int[]{ 2, 8, 18, 32, 18, 5 }),

		new ElementConfig(
			"Po", "Polonium",
			84, 16, 30, 6,
			0F, 9.32F,
			527F, 1235F,
			0F, 2F,
			2F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 4 },
			new int[]{ 2, 8, 18, 32, 18, 6 }),

		new ElementConfig(
			"At", "Astatine",
			85, 17, 31, 6,
			0F, 7F,
			575F, 610F,
			0F, 2.2F,
			3F,
			1,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 5 },
			new int[]{ 2, 8, 18, 32, 18, 7 }),

		new ElementConfig(
			"Rn", "Radon",
			86, 18, 32, 6,
			0F, 0.00973F,
			202F, 211.3F,
			0.094F, 2.2F,
			4F,
			10,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 6 },
			new int[]{ 2, 8, 18, 32, 18, 8 }),

		new ElementConfig(
			"Fr", "Francium",
			87, 1, 1, 7,
			0F, 1.87F,
			300F, 950F,
			0F, 0.7F,
			0F,
			3,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 6, 0, 0, 1 },
			new int[]{ 2, 8, 18, 32, 18, 8, 1 }),

		new ElementConfig(
			"Ra", "Radium",
			88, 2, 2, 7,
			0F, 5.5F,
			973F, 2010F,
			0.094F, 0.9F,
			9F,
			4,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 18, 8, 2 }),

		new ElementConfig(
			"Ac", "Actinium",
			89, 3, 3, 7,
			0F, 10.07F,
			1323F, 3471F,
			0.12F, 1.1F,
			5.5F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 0, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 32, 18, 9, 2 }),

		new ElementConfig(
			"Th", "Thorium",
			90, 0, 4, 7,
			232.038F, 11.72F,
			2115F, 5061F,
			0.113F, 1.3F,
			9.6F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 0, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 0, 0, 2, 6, 2, 0, 2 },
			new int[]{ 2, 8, 18, 32, 18, 10, 2 }),

		new ElementConfig(
			"Pa", "Protactinium",
			91, 0, 5, 7,
			231.036F, 15.37F,
			1841F, 4300F,
			0F, 1.5F,
			1.4F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 2, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 2, 0, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 32, 20, 9, 2 }),

		new ElementConfig(
			"U", "Uranium",
			92, 0, 6, 7,
			238.029F, 18.95F,
			1405.3F, 4404F,
			0.116F, 1.38F,
			2.7F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 3, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 3, 0, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 32, 21, 9, 2 }),

		new ElementConfig(
			"Np", "Neptunium",
			93, 0, 7, 7,
			0F, 20.45F,
			917F, 4273F,
			0F, 1.36F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 4, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 4, 0, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 32, 22, 9, 2 }),

		new ElementConfig(
			"Pu", "Plutonium",
			94, 0, 8, 7,
			0F, 19.84F,
			912.5F, 3501F,
			0F, 1.28F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 6, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 24, 8, 2 }),

		new ElementConfig(
			"Am", "Americium",
			95, 0, 9, 7,
			0F, 13.69F,
			1449F, 2880F,
			0F, 1.13F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 7 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 7, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 25, 8, 2 }),

		new ElementConfig(
			"Cm", "Curium",
			96, 0, 10, 7,
			0F, 13.51F,
			1613F, 3383F,
			0F, 1.28F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 7, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 7, 0, 2, 6, 1, 0, 2 },
			new int[]{ 2, 8, 18, 32, 25, 9, 2 }),

		new ElementConfig(
			"Bk", "Berkelium",
			97, 0, 11, 7,
			0F, 14.79F,
			1259F, 2900F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 9 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 9, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 27, 8, 2 }),

		new ElementConfig(
			"Cf", "Californium",
			98, 0, 12, 7,
			0F, 15.1F,
			1173F, 1743F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 10, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 28, 8, 2 }),

		new ElementConfig(
			"Es", "Einsteinium",
			99, 0, 13, 7,
			0F, 8.84F,
			1133F, 1269F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 11 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 11 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 11, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 29, 8, 2 }),

		new ElementConfig(
			"Fm", "Fermium",
			100, 0, 14, 7,
			0F, 0F,
			0F, 0F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 12 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 12 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 12, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 30, 8, 2 }),

		new ElementConfig(
			"Md", "Mendelevium",
			101, 0, 15, 7,
			0F, 0F,
			0F, 0F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 13 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 13 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 13, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 31, 8, 2 }),

		new ElementConfig(
			"No", "Nobelium",
			102, 0, 16, 7,
			0F, 0F,
			0F, 0F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 0, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 8, 2 }),

		new ElementConfig(
			"Lr", "Lawrencium",
			103, 0, 17, 7,
			0F, 0F,
			0F, 0F,
			0F, 1.3F,
			0F,
			6,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 0, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 0, 0, 2, 1 },
			new int[]{ 2, 8, 18, 32, 32, 8, 3 }),

		new ElementConfig(
			"Rf", "Rutherfordium",
			104, 4, 18, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 2, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 10, 2 }),

		new ElementConfig(
			"Db", "Dubnium",
			105, 5, 19, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 3, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 11, 2 }),

		new ElementConfig(
			"Sg", "Seaborgium",
			106, 6, 20, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 4, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 12, 2 }),

		new ElementConfig(
			"Bh", "Bohrium",
			107, 7, 21, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 5, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 13, 2 }),

		new ElementConfig(
			"Hs", "Hassium",
			108, 8, 22, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			7,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 6, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 14, 2 }),

		new ElementConfig(
			"Mt", "Meitnerium",
			109, 9, 23, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 7 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 7 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 7, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 15, 2 }),

		new ElementConfig(
			"Ds", "Darmstadtium",
			110, 10, 24, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 8 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 8 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 8, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 16, 2 }),

		new ElementConfig(
			"Rg", "Roentgenium",
			111, 11, 25, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 9 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 9 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 9, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 17, 2 }),

		new ElementConfig(
			"Cn", "Copernicium",
			112, 12, 26, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			8,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2 },
			new int[]{ 2, 8, 18, 32, 32, 18, 2 }),

		new ElementConfig(
			"Nh", "Nihonium",
			113, 13, 27, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 1 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 1 },
			new int[]{ 2, 8, 18, 32, 32, 18, 3 }),

		new ElementConfig(
			"Fl", "Flerovium",
			114, 14, 28, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 2 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 2 },
			new int[]{ 2, 8, 18, 32, 32, 18, 4 }),

		new ElementConfig(
			"Mc", "Moscovium",
			115, 15, 29, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 3 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 3 },
			new int[]{ 2, 8, 18, 32, 32, 18, 5 }),

		new ElementConfig(
			"Lv", "Livermorium",
			116, 16, 30, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 4 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 4 },
			new int[]{ 2, 8, 18, 32, 32, 18, 6 }),

		new ElementConfig(
			"Ts", "Tennessine",
			117, 17, 31, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 5 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 5 },
			new int[]{ 2, 8, 18, 32, 32, 18, 7 }),

		new ElementConfig(
			"Og", "Oganesson",
			118, 18, 32, 7,
			0F, 0F,
			0F, 0F,
			0F, 0F,
			0F,
			2,
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 6 },
			new int[]{ 2, 2, 6, 2, 6, 10, 2, 6, 10, 14, 2, 6, 10, 14, 0, 2, 6, 10, 0, 2, 6 },
			new int[]{ 2, 8, 18, 32, 32, 18, 8 })

		};


	public static final int UNDEFINED_KEY = -1;
	/**
	 * Get the key of the first element of ElementConfigurations[].
	 * 
	 * @return the key of the first element.
	 */
	public static int firstKey() {
		return 0;
	}

	/**
	 * Get the key of the last element of ElementConfigurations[].
	 * 
	 * @return the key of the last element.
	 */
	public static int lastKey() {
		return ElementConfigurations.length - 1;
	}

	/**
	 * Get the key of the next element. No error checking, so return value
	 * must be checked with isKeyValid().
	 * 
	 * @param key of the current element.
	 * @return the key of the next element.
	 */
	public static int nextKey(int key) {
		return ++key;
	}

	/**
	 * Check if the element key is valid.
	 * 
	 * @param key to be validated.
	 * @return true if the key is valid, false otherwise.
	 */
	public static boolean isKeyValid(int key) {
		if ((key < firstKey()) || (key > lastKey()))
			return false;

		return true;
	}

	/**
	 * Get the ElementConfig of the element with the given key.
	 * 
	 * @param key of the element required.
	 * @return the ElementConfig of the element with the given key or null if
	 *         the key is invalid.
	 */
	public static ElementConfig element(int key) {
		if (!isKeyValid(key))
			return null;

		return ElementConfigurations[key];
	}


}
