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

import java.io.File;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import phillockett65.CardCreate2.sample.Default;

public class Model {

    /**
     * Support code for "Playing Card Generator" panel. 
     */


    /**
     * Support code for "Input Directories" panel. 
     */

	private String baseDirectory = ".";
	private boolean validBaseDirectory = false;

    ArrayList<String> facesList = new ArrayList<String>();
	ArrayList<String> indexList = new ArrayList<String>();
    ArrayList<String> pipList = new ArrayList<String>();


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

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

    private boolean fillComboBox(ArrayList<String> styleList, String directoryName) {
        final File style = new File(directoryName);

        for (final File styleEntry : style.listFiles()) {
            if (styleEntry.isDirectory()) {
//                System.out.println(directoryName + "\\" + styleEntry.getName());
                styleList.add(styleEntry.getName());
            }
        }

        return !styleList.isEmpty();
    }

	public boolean setBaseDirectory(File directory) {
        if (!directory.isDirectory()) {
            return false;
        }

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
                        faces = fillComboBox(facesList, directoryName);
                        break;
                    case "indices":
                        indices = fillComboBox(indexList, directoryName);
                        break;
                    case "pips":
                        pips = fillComboBox(pipList, directoryName);
                        break;
                }
            }
        }

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


	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

    /**
     * Support code for "Generate" panel. 
     */


    /**
     * Support code for "Output Directory" panel. 
     */

    private String outputName = "";



    /**
     * Support code for "Sample Navigation" panel. 
     */


    /**
     * Support code for "Card Size" panel. 
     */

	private int width = Default.WIDTH.intr();
	private int height = Default.HEIGHT.intr();

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


    /**
     * Support code for "Modify Selected Card Item" panel. 
     */


	/**
	 * Default Constructor.
	 */
	public Model() {
//		System.out.println("Model constructed.");

	}

}
