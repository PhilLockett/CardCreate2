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
 * ElementConfig is a simple class that captures the attributes of a chemical 
 * element. All attributes are set by the constructor and are finalized.
 * 
 * Element Configurations class contains:
 * 
 * Symbol						Chemical element symbol.
 * Name							Chemical element name.
 * Z							Atomic Number.
 * Group						In chemistry, a group (also known as a family) 
 * 								is a column of elements in the periodic table 
 * 								of the chemical elements. There are 18 
 * 								numbered groups in the periodic table, and the 
 * 								f-block columns (between groups 3 and 4) are 
 * 								not numbered. The elements in a group have 
 * 								similar physical or chemical characteristics 
 * 								of the outermost electron shells of their 
 * 								atoms (i.e., the same core charge), as most 
 * 								chemical properties are dominated by the 
 * 								orbital location of the outermost electron. 
 * 								The modern numbering group 1 to group 18 is 
 * 								recommended by the International Union of Pure 
 * 								and Applied Chemistry (IUPAC).
 * Group32						The column number from the expanded periodic 
 * 								table (1 to 32).
 * Period						A period in the periodic table is a row of 
 * 								chemical elements. All elements in a row have 
 * 								the same number of electron shells. Each next 
 * 								element in a period has one more proton and is 
 * 								less metallic than its predecessor.
 * AtomicWeight					Relative atomic mass or atomic weight is a 
 * 								dimensionless physical quantity defined as the 
 * 								ratio of the average mass of atoms of a 
 * 								chemical element in a given sample to one 
 * 								unified atomic mass unit. The unified atomic 
 * 								mass unit is defined as being being 1/12 of the 
 * 								atomic mass of a carbon-12 atom.
 * Density						Density in g/cm^3.
 * Melt							Melting point in K.
 * Boil							Boiling point in K.
 * C							Specific heat capacity in J/g.K.
 * X							Electronegativity is a chemical property that 
 * 								describes the tendency of an atom to attract 
 * 								a shared pair of electrons towards itself
 * Abundance					Abundance of elements in Earth's crust in PPM.
 * Subcategory					Index into the Subcategories list.
 * GenElectronConfigCounts		Electron Configuration generated using 
 * 								Madelung's rule (see ElectronConfigurations 
 * 								for order).
 * ElectronConfigurationCounts	Observed Electron Configuration.
 * ElectronSubshellCounts		Electron count within each subshell (see  
 * 								ElectronSubshells for order).
 * ElectronShellCounts			Electron count within each shell.
 * 
 */
package phillockett65.CardCreate2.elements;

public class ElementConfig {

	private final String symbol;
	private final String name;
	private final int Z;
	private final int group;
	private final int group32;
	private final int period;
	private final float atomicWeight;
	private final float density;
	private final float melt;
	private final float boil;
	private final float C;
	private final float X;
	private final float abundance;
	private final int subcategory;
	private final int[] genElectronConfigCounts;
	private final int[] electronConfigurationCounts;
	private final int[] electronSubshellCounts;
	private final int[] electronShellCounts;

	/**
	 * Get the Chemical element symbol as a string.
	 * 
	 * @return the Chemical element symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Crop or space pad a String to width.
	 * 
	 * @param string to be cropped or space padded.
	 * @param width  of returned String (negative values for left justified).
	 * @return cropped or space padded version of string.
	 */
	public String cropOrPad(String string, int width) {

		if (width < 0) {
			width *= -1;
			return String.format("%-" + width + "." + width + "s", string);
		}
		return String.format("%" + width + "." + width + "s", string);
	}

	/**
	 * Get the Chemical element symbol as a string.
	 * 
	 * @param width of returned String (negative values for left justified).
	 * @return the Chemical element symbol.
	 */
	public String getSymbol(int width) {
		return cropOrPad(symbol, width);
	}

	/**
	 * Get the Chemical element name as a string.
	 * 
	 * @return the Chemical element name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the Chemical element name as a string.
	 * 
	 * @param width of returned String (negative values for left justified).
	 * @return the Chemical element name.
	 */
	public String getName(int width) {
		return cropOrPad(name, width);
	}

	/**
	 * Get the Atomic Number of element.
	 * 
	 * @return the Atomic Number of element.
	 */
	public int getZ() {
		return Z;
	}

	/**
	 * Get the Group Number of element. In chemistry, a group (also known as a
	 * family) is a column of elements in the periodic table of the chemical
	 * elements. There are 18 numbered groups in the periodic table, and the f-block
	 * columns (between groups 3 and 4) are not numbered. The elements in a group
	 * have similar physical or chemical characteristics of the outermost electron
	 * shells of their atoms (i.e., the same core charge), as most chemical
	 * properties are dominated by the orbital location of the outermost electron.
	 * The modern numbering group 1 to group 18 is recommended by the International
	 * Union of Pure and Applied Chemistry (IUPAC).
	 * 
	 * @return the Group Number of element.
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * Get the column number from the expanded periodic table (1 to 32) of element.
	 * 
	 * @return the Density of element in g/cm^3.
	 */
	public int getGroup32() {
		return group32;
	}

	/**
	 * Get the Period of element. A period in the periodic table is a row of
	 * chemical elements. All elements in a row have the same number of electron
	 * shells. Each next element in a period has one more proton and is less
	 * metallic than its predecessor.
	 * 
	 * @return the Period of element.
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * Get the Atomic Weight of element. Relative atomic mass or atomic weight is a
	 * dimensionless physical quantity defined as the ratio of the average mass of
	 * atoms of a chemical element in a given sample to one unified atomic mass
	 * unit. The unified atomic mass unit is defined as being 1/12 of the atomic
	 * mass of a carbon-12 atom.
	 * 
	 * @return the Atomic Weight of element.
	 */
	public float getAtomicWeight() {
		return atomicWeight;
	}

	/**
	 * Get the Density of element in g/cm^3.
	 * 
	 * @return the Density of element in g/cm^3.
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * Get the Melting point of element in K.
	 * 
	 * @return the Melting point of element in K.
	 */
	public float getMelt() {
		return melt;
	}

	/**
	 * Get the Boiling point of element in K.
	 * 
	 * @return the Boiling point of element in K.
	 */
	public float getBoil() {
		return boil;
	}

	/**
	 * Get the Specific heat capacity of element in J/g.K.
	 * 
	 * @return the Specific heat capacity of element in J/g.K.
	 */
	public float getC() {
		return C;
	}

	/**
	 * Get the Electronegativity of element. Electronegativity is a chemical
	 * property that describes the tendency of an atom to attract a shared pair of
	 * electrons towards itself
	 * 
	 * @return the Electronegativity of element.
	 */
	public float getX() {
		return X;
	}

	/**
	 * Get the Abundance of element in Earth's crust in PPM.
	 * 
	 * @return the Abundance of element in Earth's crust in PPM.
	 */
	public float getAbundance() {
		return abundance;
	}

	/**
	 * Get the Index into the "Subcategories[]" list.
	 * 
	 * @return the Index into the "Subcategories[]" list.
	 */
	public int getSubcategory() {
		return subcategory;
	}

	/**
	 * Get the Electron Configuration generated using Madelung's rule (see
	 * ElectronConfigurations[] for order).
	 * 
	 * @return an array of the Electron Configuration generated using Madelung's
	 *         rule.
	 */
	public int[] getGenElectronConfigCounts() {
		return genElectronConfigCounts;
	}

	/**
	 * Get the observed Electron Configuration.
	 * 
	 * @return an array of the observed Electron Configuration.
	 */
	public int[] getElectronConfigurationCounts() {
		return electronConfigurationCounts;
	}

	/**
	 * Get the electron count within each subshell (see ElectronSubshells[] for
	 * order).
	 * 
	 * @return an array of the electron count within each subshell.
	 */
	public int[] getElectronSubshellCounts() {
		return electronSubshellCounts;
	}

	/**
	 * Get the electron count within each shell.
	 * 
	 * @return an array of the electron count within each shell.
	 */
	public int[] getElectronShellCounts() {
		return electronShellCounts;
	}

	/**
	 * Constructor. See the corresponding getter for a description of the attribute.
	 * 
	 * @param symbol
	 * @param name
	 * @param Z
	 * @param group
	 * @param group32
	 * @param period
	 * @param atomicWeight
	 * @param density
	 * @param melt
	 * @param boil
	 * @param C
	 * @param X
	 * @param abundance
	 * @param subcategory
	 * @param genElectronConfigCounts
	 * @param electronConfigurationCounts
	 * @param electronSubshellCounts
	 * @param electronShellCounts
	 */
	public ElementConfig(String symbol, String name, int Z, int group, int group32, int period, float atomicWeight,
			float density, float melt, float boil, float C, float X, float abundance, int subcategory,
			int[] genElectronConfigCounts, int[] electronConfigurationCounts, int[] electronSubshellCounts,
			int[] electronShellCounts) {

		this.symbol = symbol;
		this.name = name;
		this.Z = Z;
		this.group = group;
		this.group32 = group32;
		this.period = period;
		this.atomicWeight = atomicWeight;
		this.density = density;
		this.melt = melt;
		this.boil = boil;
		this.C = C;
		this.X = X;
		this.abundance = abundance;
		this.subcategory = subcategory;
		this.genElectronConfigCounts = genElectronConfigCounts;
		this.electronConfigurationCounts = electronConfigurationCounts;
		this.electronSubshellCounts = electronSubshellCounts;
		this.electronShellCounts = electronShellCounts;
	}

}
