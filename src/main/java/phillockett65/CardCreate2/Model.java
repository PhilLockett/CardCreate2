/*  CardCreate2 - a JavaFX based playing card image generator.
 *
 *  Copyright 2022 Philip Lockett.
 *
 *  This file is part of CardCreate2.
 *
 *  CardCreate2 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CardCreate2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CardCreate2.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Model is a class that captures the dynamic shared data plus some supporting 
 * constants and provides access via getters and setters.
 */
package phillockett65.CardCreate2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.SpinnerValueFactory;
import phillockett65.CardCreate2.sample.Default;

public class Model {

	/**
	 * Support code for "Playing Card Generator" panel.
	 */
	private final String PATHSFILE = "Files.txt";

	/**
	 * Support code for "Input Directories" panel.
	 */

	private String baseDirectory = ".";
	private boolean validBaseDirectory = false;

	ArrayList<String> baseList = new ArrayList<String>();
	ArrayList<String> facesList = new ArrayList<String>();
	ArrayList<String> indexList = new ArrayList<String>();
	ArrayList<String> pipList = new ArrayList<String>();

	public ArrayList<String> getBaseList() {
		return baseList;
	}

	public ArrayList<String> getFacesList() {
		return facesList;
	}

	public ArrayList<String> getIndexList() {
		return indexList;
	}

	public ArrayList<String> getPipList() {
		return pipList;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}


	/**
	 * Formerly baseDirectoryjComboBoxInit()
	 * 
	 * Read base directory file paths from disc and set up Base, Face, Index 
	 * and Pip pull-down lists.
	 * 
	 * @return
	 */
	public boolean readBaseDirectoryFilePathsFromDisc() {
//      System.out.println("baseDirectoryjComboBoxInit()");

		// Check if PATHSFILE exists.
		File file = new File(PATHSFILE);
		if (!file.exists()) {
			file = new File(".");
//          samplejPanel.setIndexDirectory(file.getPath());
			return false;
		}

		// Read path list file into array.
		ArrayList<String> pathList = new ArrayList<String>();
		try (FileReader reader = new FileReader(PATHSFILE); BufferedReader br = new BufferedReader(reader)) {

			String line;
			while ((line = br.readLine()) != null) {
				pathList.add(line);
//              System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
//          e.printStackTrace();
		}

		// If array is not empty use it to fill in baseDirectoryjComboBox.
		if (!pathList.isEmpty()) {
			setBaseDirectory(pathList.get(0));

			if (validBaseDirectory) {
				baseList = pathList;

				return true;
			}
		}

		return false;
	}

	public boolean baseDirectoryjComboBoxSave() {

		try (FileWriter writer = new FileWriter(PATHSFILE); BufferedWriter bw = new BufferedWriter(writer)) {
			for (final String directory : baseList) {
				final String item = directory + System.lineSeparator();
				bw.write(item);
			}
			bw.close();
		} catch (IOException e) {
//          e.printStackTrace();
		}

		return true;
	}

	/**
	 * Formerly baseDirectoryjComboBoxAdd()
	 * 
	 * Re-order base directory list so that baseDirectory is first.
	 * 
	 * @return
	 */
	public boolean reorderBaseDirectoryComboBox() {

        // Build array with path as first item.
        ArrayList<String> pathList = new ArrayList<String>();
        pathList.add(baseDirectory);

        // Add baseDirectoryjComboBox items to array, except for "path".
        for (final String item : baseList) {
            if (!baseDirectory.equals(item)) {
                pathList.add(item);
            }
        }

        // Use array to fill in baseDirectoryjComboBox.
		baseList = pathList;

        baseDirectoryjComboBoxSave();

        return true;
    }


	private boolean fillDirectoryList(ArrayList<String> styleList, String directoryName) {
		final File style = new File(directoryName);

		for (final File styleEntry : style.listFiles()) {
			if (styleEntry.isDirectory()) {
//                System.out.println(directoryName + "\\" + styleEntry.getName());
				styleList.add(styleEntry.getName());
			}
		}

		return !styleList.isEmpty();
	}

	public boolean setBaseDirectory(String base) {

		File directory = new File(base);

		if (!directory.isDirectory()) {
			return false;
		}

		baseDirectory = base;

		boolean faces = false;
		boolean indices = false;
		boolean pips = false;

		facesList.clear();
		indexList.clear();
		pipList.clear();

		for (final File fileEntry : directory.listFiles()) {
			if (fileEntry.isDirectory()) {
				String directoryName = directory.getPath() + "\\" + fileEntry.getName();
//                System.out.println(directoryName);
				switch (fileEntry.getName()) {
				case "faces":
					faces = fillDirectoryList(facesList, directoryName);
					break;
				case "indices":
					indices = fillDirectoryList(indexList, directoryName);
					break;
				case "pips":
					pips = fillDirectoryList(pipList, directoryName);
					break;
				}
			}
		}

		if (faces)
			outputName = facesList.get(0);

		validBaseDirectory = (faces && indices && pips);

		return validBaseDirectory;
	}

	public boolean isValidBaseDirectory() {
		return validBaseDirectory;
	}

	public void setValidBaseDirectory(boolean validBaseDirectory) {
		this.validBaseDirectory = validBaseDirectory;
	}

	public String getFaceDirectory() {
		return baseDirectory + "face";
	}

	public String getIndexDirectory() {
		return baseDirectory + "index";
	}

	public String getPipDirectory() {
		return baseDirectory + "pip";
	}


	/**
	 * Support code for "Generate" panel.
	 */

	/**
	 * Support code for "Output Directory" panel.
	 */

	private String outputName = "";

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}


	/**
	 * Support code for "Sample Navigation" panel.
	 */

	/**
	 * Support code for "Card Size" panel.
	 */

	private int width = Default.WIDTH.intr();
	private int height = Default.HEIGHT.intr();

	private SpinnerValueFactory<Integer> widthSVF;
    private SpinnerValueFactory<Integer> heightSVF;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	
	/**
	 * Support code for "Background Colour" panel.
	 */

	/**
	 * Support code for "Display Card Items" panel.
	 */

	/**
	 * Support code for "Select Card Item" panel.
	 */

	public SpinnerValueFactory<Integer> getWidthSVF() {
		return widthSVF;
	}

	public SpinnerValueFactory<Integer> getHeightSVF() {
		return heightSVF;
	}

	/**
	 * Support code for "Modify Selected Card Item" panel.
	 */

    private SpinnerValueFactory<Integer> itemHeightSVF;
    private SpinnerValueFactory<Integer> itemCentreXSVF;
    private SpinnerValueFactory<Integer> itemCentreYSVF;


	public SpinnerValueFactory<Integer> getItemHeightSVF() {
		return itemHeightSVF;
	}

	public SpinnerValueFactory<Integer> getItemCentreXSVF() {
		return itemCentreXSVF;
	}

	public SpinnerValueFactory<Integer> getItemCentreYSVF() {
		return itemCentreYSVF;
	}

	/**
	 * Default Constructor.
	 */
	public Model() {
//		System.out.println("Model constructed.");

		/**
		 * Initialize "Playing Card Generator" panel.
		 */

		/**
		 * Initialize "Input Directories" panel.
		 */

		readBaseDirectoryFilePathsFromDisc();

		/**
		 * Initialize"Generate" panel.
		 */

		/**
		 * Initialize"Output Directory" panel.
		 */

		/**
		 * Initialize "Sample Navigation" panel.
		 */

		/**
		 * Initialize "Card Size" panel.
		 */

		final int WIDTH = Default.WIDTH.intr();
		final int HEIGHT = Default.HEIGHT.intr();
		widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(WIDTH/10, WIDTH*10, WIDTH);
	    heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(HEIGHT/10, HEIGHT*10, HEIGHT);

		/**
		 * Initialize "Background Colour" panel.
		 */

		/**
		 * Initialize "Display Card Items" panel.
		 */

		/**
		 * Initialize "Select Card Item" panel.
		 */

		/**
		 * Initialize "Modify Selected Card Item" panel.
		 */

	    itemHeightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreXSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreYSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);

	}

}
