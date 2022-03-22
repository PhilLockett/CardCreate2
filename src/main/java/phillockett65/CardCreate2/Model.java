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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;

import phillockett65.CardCreate2.sample.Default;

public class Model {

	/**
	 * Support code for "Playing Card Generator" panel.
	 */
	private final String PATHSFILE = "Files.txt";



	/**
	 * Support code for "Input Directories" panel.
	 */

	private boolean validBaseDirectory = false;
	private String baseDirectory = ".";
	private String faceStyle;
	private String indexStyle;
	private String pipStyle;

	ObservableList<String> baseList = FXCollections.observableArrayList();
	ObservableList<String> faceList = FXCollections.observableArrayList();
	ObservableList<String> indexList = FXCollections.observableArrayList();
	ObservableList<String> pipList = FXCollections.observableArrayList();

	public ObservableList<String> getBaseList() {
		return baseList;
	}

	public ObservableList<String> getFaceList() {
		return faceList;
	}

	public ObservableList<String> getIndexList() {
		return indexList;
	}

	public ObservableList<String> getPipList() {
		return pipList;
	}

	
	public void setBaseList(ObservableList<String> baseList) {
		this.baseList = baseList;
	}

	public void setFaceList(ObservableList<String> faceList) {
		this.faceList = faceList;
	}

	public void setIndexList(ObservableList<String> indexList) {
		this.indexList = indexList;
	}

	public void setPipList(ObservableList<String> pipList) {
		this.pipList = pipList;
	}

	public boolean isValidBaseDirectory() {
		return validBaseDirectory;
	}

	public void setBaseDirectoryValidity(boolean validity) {
		validBaseDirectory = validity;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public String getFaceDirectory() {
		return baseDirectory + "\\face\\" + faceStyle;
	}

	public String getIndexDirectory() {
		return baseDirectory + "\\index\\" + indexStyle;
	}

	public String getPipDirectory() {
		return baseDirectory + "\\pip\\" + pipStyle;
	}

	public String getFaceStyle() {
		return faceStyle;
	}

	public String getIndexStyle() {
		return indexStyle;
	}

	public String getPipStyle() {
		return pipStyle;
	}

	public void setFaceStyle(String style) {
		faceStyle = style;
	}

	public void setIndexStyle(String style) {
		indexStyle = style;
	}

	public void setPipStyle(String style) {
		pipStyle = style;
	}


	/**
	 * Read base directory file paths from disc and set up Base, Face, Index 
	 * and Pip pull-down lists.
	 * 
	 * @return
	 */
	private boolean readBaseDirectoryFilePathsFromDisc() {
//      System.out.println("readBaseDirectoryFilePathsFromDisc()");

		// Check if PATHSFILE exists.
		File file = new File(PATHSFILE);
		if (!file.exists()) {
			file = new File(".");
//          samplejPanel.setIndexDirectory(file.getPath());
			return false;
		}

		// Read path list file into array.
		try (FileReader reader = new FileReader(PATHSFILE); BufferedReader br = new BufferedReader(reader)) {

			String line;
			while ((line = br.readLine()) != null) {
				baseList.add(line);
//              System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
//          e.printStackTrace();
		}

		// If array is not empty use it to fill in baseDirectoryjComboBox.
		if (!baseList.isEmpty()) {
			setBaseDirectory(baseList.get(0));

			if (validBaseDirectory)
				return true;
		}

		return false;
	}

	/**
	 * Write the list of base directories to disc with current baseDirectory
	 * first.
	 * 
	 * @return
	 */
	private boolean writeBaseDirectoryFilePathsToDisc() {
//		System.out.println("writeBaseDirectoryFilePathsToDisc()");

		try (FileWriter writer = new FileWriter(PATHSFILE); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(baseDirectory + System.lineSeparator());
			for (final String directory : baseList) {
				final String item = directory + System.lineSeparator();
	            if (!baseDirectory.equals(directory))
	            	bw.write(item);
			}
			bw.close();
		} catch (IOException e) {
//          e.printStackTrace();
		}

		return true;
	}


	private boolean fillDirectoryList(ObservableList<String> styleList, String directory, String item) {

		String directoryName = directory + "\\" + item;
		final File style= new File(directoryName);

		styleList.clear();
		for (final File styleEntry : style.listFiles()) {
			if (styleEntry.isDirectory()) {
//                System.out.println(directoryName + "\\" + styleEntry.getName());
				styleList.add(styleEntry.getName());
			}
		}

		return !styleList.isEmpty();
	}

	public boolean setBaseDirectory(String base) {
//		System.out.println("model.setBaseDirectory(" + base + ")");

		if (base.equals(""))
			return false;

		File directory = new File(base);

		if (!directory.isDirectory())
			return false;

		baseDirectory = base;

		boolean faces = false;
		boolean indices = false;
		boolean pips = false;

		for (final File fileEntry : directory.listFiles()) {
			if (fileEntry.isDirectory()) {
				final String directoryName = directory.getPath();
				final String item = fileEntry.getName();
//                System.out.println(directoryName);
				switch (fileEntry.getName()) {
				case "faces":
					faces = fillDirectoryList(faceList, directoryName, item);
					break;
				case "indices":
					indices = fillDirectoryList(indexList, directoryName, item);
					break;
				case "pips":
					pips = fillDirectoryList(pipList, directoryName, item);
					break;
				}
			}
		}

		validBaseDirectory = (faces && indices && pips);
		if (validBaseDirectory) {
			if (!baseList.contains(baseDirectory))
				baseList.add(baseDirectory);
			writeBaseDirectoryFilePathsToDisc();
			faceStyle = faceList.get(0);
			indexStyle = indexList.get(0);
			pipStyle = pipList.get(0);
		}

		return validBaseDirectory;
	}

	/**
	 * Initialize "Input Directories" panel.
	 */
	private void initializeInputDirectories() {
		readBaseDirectoryFilePathsFromDisc();
	}



	/**
	 * Support code for "Generate" panel.
	 */

	/**
	 * Initialize"Generate" panel.
	 */
	private void initializeGenerate() {
	}



	/**
	 * Support code for "Output Directory" panel.
	 */

	private boolean manual = false;
	private String outputName = "";

	public void setOutputNameManually(boolean state) {
		manual = state;
	}

	public String getOutputName() {
		if (manual)
			return outputName.equals("") ? "anon" : outputName;

		return faceStyle;
	}

	public void setOutputName(String name) {
		outputName = name;
	}

	public String getOutputDirectory() {
		return baseDirectory + "\\cards\\" + getOutputName();
	}

	/**
	 * Initialize"Output Directory" panel.
	 */
	private void initializeOutputDirectory() {
	}



	/**
	 * Support code for "Sample Navigation" panel.
	 */

	/**
	 * Initialize "Sample Navigation" panel.
	 */
	private void initializeSampleNavigation() {
	}



	/**
	 * Support code for "Card Size" panel.
	 */

	public enum CardSize { POKER, BRIDGE, FREE };
	
	private CardSize cardSize = CardSize.POKER;

	private int width = Default.WIDTH.intr();
	private int height = Default.HEIGHT.intr();

	private SpinnerValueFactory<Integer> widthSVF;
	private SpinnerValueFactory<Integer> heightSVF;

	public CardSize getCardSize() {
		return cardSize;
	}

	public void setCardSize(CardSize cardSize) {
		this.cardSize = cardSize;
	}

	public boolean isAutoCardWidth() {
		return cardSize != CardSize.FREE;
	}

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

	public SpinnerValueFactory<Integer> getWidthSVF() {
		return widthSVF;
	}

	public SpinnerValueFactory<Integer> getHeightSVF() {
		return heightSVF;
	}

	public void defaultWidthSVF() {
		widthSVF.setValue(Default.WIDTH.intr());
	}

	public void defaultHeightSVF() {
		heightSVF.setValue(Default.HEIGHT.intr());
	}

	/**
	 * Initialize "Card Size" panel.
	 */
	private void initializeCardSize() {
		final int WIDTH = Default.WIDTH.intr();
		final int HEIGHT = Default.HEIGHT.intr();
		widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(WIDTH/10, WIDTH*10, WIDTH);
	    heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(HEIGHT/10, HEIGHT*10, HEIGHT);
	}



	/**
	 * Support code for "Background Colour" panel.
	 */

	private Color backgroundColour = Color.WHITE;

	public Color getBackgroundColour() {
        return backgroundColour;
    }

	private int colourRealToInt(double comp) {
		return (int)(comp * 256);
	}
	public String getBackgroundColourString() {
        return String.format("rgb(%d, %d, %d)",
        		colourRealToInt(backgroundColour.getRed()),
        		colourRealToInt(backgroundColour.getGreen()),
        		colourRealToInt(backgroundColour.getBlue()));
    }

	public void setBackgroundColour(Color colour) {
		backgroundColour = colour;
	}

	/**
	 * Initialize "Background Colour" panel.
	 */
	private void initializeBackgroundColour() {
    }



	/**
	 * Support code for "Display Card Items" panel.
	 */

	private boolean displayIndex = true;
	private boolean displayCornerPip = true;
	private boolean displayStandardPip = true;
	private boolean displayFaceImage = true;
	private boolean displayFacePip = true;

	public boolean isDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(boolean state) {
		displayIndex = state;
	}

	public boolean isDisplayCornerPip() {
		return displayCornerPip;
	}

	public void setDisplayCornerPip(boolean state) {
		displayCornerPip = state;
	}

	public boolean isDisplayStandardPip() {
		return displayStandardPip;
	}

	public void setDisplayStandardPip(boolean state) {
		displayStandardPip = state;
	}

	public boolean isDisplayFaceImage() {
		return displayFaceImage;
	}

	public void setDisplayFaceImage(boolean state) {
		displayFaceImage = state;
	}

	public boolean isDisplayFacePip() {
		return displayFacePip;
	}

	public void setDisplayFacePip(boolean state) {
		displayFacePip = state;
	}

	/**
	 * Initialize "Display Card Items" panel.
	 */
	private void initializeDisplayCardItems() {
    }



	/**
	 * Support code for "Select Card Item" panel.
	 */

	public enum CardItem { INDEX, CORNER_PIP, STANDARD_PIP, FACE_IMAGE, FACE_PIP };
	
	private CardItem cardItem = CardItem.INDEX;

	public CardItem getCardItem() {
		return cardItem;
	}

	public void setCardItem(CardItem cardItem) {
		this.cardItem = cardItem;
	}

    /**
	 * Initialize "Select Card Item" panel.
	 */
	private void initializeSelectCardItem() {
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
	 * Initialize "Modify Selected Card Item" panel.
	 */

	private boolean keepImageAspectRatio = true;

	public boolean iskeepImageAspectRatio() {
		return keepImageAspectRatio;
	}

	public void setkeepImageAspectRatio(boolean state) {
		keepImageAspectRatio = state;
	}


	private void initializeModifySelectedCardItem() {
	    itemHeightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreXSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreYSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	}



	/**
	 * Default Constructor.
	 */
	public Model() {
//		System.out.println("Model constructed.");

		/**
		 * Initialize "Playing Card Generator" panel.
		 */

		initializeInputDirectories();
		initializeGenerate();
		initializeOutputDirectory();
		initializeSampleNavigation();
		initializeCardSize();
		initializeBackgroundColour();
		initializeDisplayCardItems();
		initializeSelectCardItem();
		initializeModifySelectedCardItem();
	}

}
