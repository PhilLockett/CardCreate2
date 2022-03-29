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
import javafx.scene.Group;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import phillockett65.CardCreate2.sample.Default;
import phillockett65.CardCreate2.sample.Handle;
import phillockett65.CardCreate2.sample.Item;
import phillockett65.CardCreate2.sample.Payload;

public class Model {


	/************************************************************************
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
		return baseDirectory + "\\faces\\" + faceStyle;
	}

	public String getIndexDirectory() {
		return baseDirectory + "\\indices\\" + indexStyle;
	}

	public String getPipDirectory() {
		return baseDirectory + "\\pips\\" + pipStyle;
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
        setFaceCardItemPayload();
    }

    public void setIndexStyle(String style) {
        indexStyle = style;
        setIndexCardItemPayload();
    }

    public void setPipStyle(String style) {
        pipStyle = style;
        setPipCardItemPayloads();
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



	/************************************************************************
	 * Support code for "Generate" panel.
	 */

	/**
	 * Initialize"Generate" panel.
	 */
	private void initializeGenerate() {
	}



	/************************************************************************
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



	/************************************************************************
	 * Support code for "Sample Navigation" panel.
	 */

    private int suit = 0;
    private int card = 10;

    private final String[] suits = { "C", "D", "H", "S" };
    private final String[] alts  = { "S", "H", "D", "C" };
    private final String[] cards = { "Joker", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    

	public int getSuit() {
		return suit;
	}

	public int getCard() {
		return card;
	}

	public int nextSuit() {
		if (++suit >= suits.length)
			suit = 0;

        setCardItemPayloads();

        return suit;
	}

	public int nextCard() {
		if (++card >= cards.length)
			card = 1;

        setCardItemPayloads();

        return card;
	}

	public int prevSuit() {
		if (--suit < 0)
			suit = suits.length - 1;

        setCardItemPayloads();

        return suit;
	}

	public int prevCard() {
		if (--card <= 0)
			card = cards.length - 1;

        setCardItemPayloads();

        return card;
	}

	/**
	 * @return true if the current card is a face card (court card), false 
	 * otherwise.
	 */
	public boolean isFaceCard() {
		return card > 10;
	}

	/**
	 * @return true if the current card has an image file, false otherwise.
	 */
	public boolean isImageCard() {
		return face.hasImage();
	}

	/**
	 * Initialize "Sample Navigation" panel.
	 */
	private void initializeSampleNavigation() {
	}



	/************************************************************************
	 * Support code for "Card Size" panel.
	 */

	public enum CardSize { POKER, BRIDGE, FREE };
	
	private CardSize cardSize = CardSize.POKER;

	private double cardWidthPX = Default.WIDTH.getInt();
	private double cardHeightPX = Default.HEIGHT.getInt();

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

	public double getWidth() {
		return cardWidthPX;
	}

	public double getCalculatedWidth() {
		if (cardSize == CardSize.POKER)
			return cardHeightPX * 5 / 7;

		if (cardSize == CardSize.BRIDGE)
			return cardHeightPX * 9 / 14;

		return cardWidthPX;
	}

	public void setWidth(double width) {
		cardWidthPX = width;
	}

	public double getHeight() {
		return cardHeightPX;
	}

	public void setHeight(double height) {
		cardHeightPX = height;
	}

	/**
	 * Convert X coordinate percentage to pixels.
	 * 
	 * @param x coordinate as a percentage of the card width.
	 * @return equivalent pixel count.
	 */
    public long percentageToPX(double x) {
    	return Math.round(x * cardWidthPX / 100); 
    }

	/**
	 * Convert Y coordinate percentage to pixels.
	 * 
	 * @param y coordinate as a percentage of the card height.
	 * @return equivalent pixel count.
	 */
    public long percentageToPY(double y) {
    	return Math.round(y * cardHeightPX / 100); 
    }

	public SpinnerValueFactory<Integer> getWidthSVF() {
		return widthSVF;
	}

	public SpinnerValueFactory<Integer> getHeightSVF() {
		return heightSVF;
	}

	public void defaultWidthSVF() {
		widthSVF.setValue(Default.WIDTH.getInt());
	}

	public void defaultHeightSVF() {
		heightSVF.setValue(Default.HEIGHT.getInt());
	}

	/**
	 * Initialize "Card Size" panel.
	 */
	private void initializeCardSize() {
		final int WIDTH = Default.WIDTH.getInt();
		final int MIN_WIDTH = Default.MIN_WIDTH.getInt();
		final int MAX_WIDTH = Default.MAX_WIDTH.getInt();
		final int HEIGHT = Default.HEIGHT.getInt();
		final int MIN_HEIGHT = Default.MIN_HEIGHT.getInt();
		final int MAX_HEIGHT = Default.MAX_HEIGHT.getInt();

		widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, WIDTH);
	    heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, HEIGHT);
	}



	/************************************************************************
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



	/************************************************************************
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



	/************************************************************************
	 * Support code for "Select Card Item" panel.
	 */

    private Item currentItem = Item.INDEX;

	public Item getCardItem() {
		return currentItem;
	}

	public void setCardItem(Item cardItem) {
		currentItem = cardItem;

		if (currentItem == Item.INDEX) {
			changeCurrentCardItemAndSyncSpinners(index);
		} else if (currentItem == Item.CORNER_PIP) {
			changeCurrentCardItemAndSyncSpinners(cornerPip);
		} else if (currentItem == Item.STANDARD_PIP) {
			changeCurrentCardItemAndSyncSpinners(standardPip);
		} else if (currentItem == Item.FACE) {
			changeCurrentCardItemAndSyncSpinners(face);
		} else if (currentItem == Item.FACE_PIP) {
			changeCurrentCardItemAndSyncSpinners(facePip);
		}

	}

//	public enum CardItem { INDEX, CORNER_PIP, STANDARD_PIP, FACE, FACE_PIP };
//	
//	private CardItem cardItem = CardItem.INDEX;
//
//	public CardItem getCardItem() {
//		return cardItem;
//	}
//
//	public void setCardItem(CardItem cardItem) {
//		this.cardItem = cardItem;
//	}

    /**
	 * Initialize "Select Card Item" panel.
	 */
	private void initializeSelectCardItem() {
    }



	/************************************************************************
	 * Support code for "Modify Selected Card Item" panel.
	 */

    private Payload index = null;
    private Payload cornerPip = null;
    private Payload standardPip = null;
    private Payload face = null;
    private Payload facePip = null;
    private Payload current = null;
    
    private SpinnerValueFactory<Double> itemHeightSVF;
    private SpinnerValueFactory<Double> itemCentreXSVF;
    private SpinnerValueFactory<Double> itemCentreYSVF;

	private boolean keepAspectRatio = true;


	/**
	 * @return the file path for the face image of the current card in the 
	 * current style.
	 */
    public String getFaceImagePath() {
        return getFaceDirectory() + "\\" + suits[suit] + cards[card] + ".png";
    }

	/**
	 * @return the file path for the index image of the current card in the 
	 * current style.
	 */
    public String getIndexImagePath() {
        String pathToImage = getIndexDirectory() + "\\" + suits[suit] + cards[card] + ".png";
        File file = new File(pathToImage);
        if (!file.exists())
            pathToImage = getIndexDirectory() + "\\" + alts[suit] + cards[card] + ".png";

        return pathToImage;
    }

	/**
	 * @return the file path for the pip image of the current card in the 
	 * current style.
	 */
    public String getPipImagePath() {
        return getPipDirectory() + "\\" + suits[suit] + ".png";
    }

	/**
	 * @return the file path for the corner pip image of the current card in 
	 * the current style.
	 */
    public String getCornerPipImagePath() {
        String pathToImage = getPipDirectory() + "\\" + suits[suit] + "S.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getPipImagePath();
    }

    private void setFaceCardItemPayload() {
        face.syncImageFile();
    }

    private void setIndexCardItemPayload() {
        index.syncImageFile();
    }

    private void setPipCardItemPayloads() {
        standardPip.syncImageFile();
		standardPip.syncCurrentCard();

		facePip.syncImageFile();

        cornerPip.syncImageFile();
    }
    private void setCardItemPayloads() {
        setFaceCardItemPayload();
        setIndexCardItemPayload();
        setPipCardItemPayloads();
	}

	/**
	 * Set the sample display status of the card items for the current card.
	 */
    private void updateCardItemDisplayStatus() {
		index.setVisible(displayIndex);
		cornerPip.setVisible(displayCornerPip);

		if (isImageCard()) {
			face.setVisible(displayFaceImage);
			standardPip.setVisible(false);
		} else {
			standardPip.setVisible(displayStandardPip);
			face.setVisible(false);
		}
		if (isFaceCard()) {
			facePip.setVisible(displayFacePip);
		} else {
			facePip.setVisible(false);
		}
    }


	/**
	 * Create the card item Payload instances after the base directory has 
	 * been selected.
	 */
    private void initializeCardItemPayloads() {
		System.out.println("initializeCardItemPayloads()");

        face		= new Payload(this, Payload.PAINT_FILE, Item.FACE);

        index		= new Payload(this, 0, Item.INDEX);

        standardPip	= new Payload(this, card, Item.STANDARD_PIP);
        facePip		= new Payload(this, 0, Item.FACE_PIP);

        cornerPip	= new Payload(this, 0, Item.CORNER_PIP);

        changeCurrentCardItemAndSyncSpinners(index);
		updateCardItemDisplayStatus();
    }

    public SpinnerValueFactory<Double> getItemHeightSVF() {
		return itemHeightSVF;
	}

	public SpinnerValueFactory<Double> getItemCentreXSVF() {
		return itemCentreXSVF;
	}

	public SpinnerValueFactory<Double> getItemCentreYSVF() {
		return itemCentreYSVF;
	}

	public boolean iskeepImageAspectRatio() {
		return keepAspectRatio;
	}

	public void setkeepImageAspectRatio(boolean state) {
		keepAspectRatio = state;
	}


	public Tooltip getCurrentHButtonTip() {
		return new Tooltip(current.getItem().getHButtonTip());
	}

	public Tooltip getCurrentXButtonTip() {
		return new Tooltip(current.getItem().getXButtonTip());
	}

	public Tooltip getCurrentYButtonTip() {
		return new Tooltip(current.getItem().getYButtonTip());
	}

	public Tooltip getCurrentHToolTip() {
		return new Tooltip(current.getItem().getHToolTip());
	}

	public Tooltip getCurrentXToolTip() {
		return new Tooltip(current.getItem().getXToolTip());
	}

	public Tooltip getCurrentYToolTip() {
		return new Tooltip(current.getItem().getYToolTip());
	}

	public String getCurrentHLabel() {
		return current.getItem().getHLabel();
	}

	public String getCurrentXLabel() {
		return current.getItem().getXLabel();
	}

	public String getCurrentYLabel() {
		return current.getItem().getYLabel();
	}

	public boolean isCurrentCentred() {
		return current.getItem().isCentre();
	}


	public double getCurrentDefaultH() {
		return current.getItem().getH();
	}

	public double getCurrentDefaultX() {
		return current.getItem().getX();
	}

	public double getCurrentDefaultY() {
		return current.getItem().getY();
	}

	public double getCurrentH() {
		return current.getSpriteH();
	}

	public double getCurrentX() {
		return current.getSpriteX();
	}

	public double getCurrentY() {
		return current.getSpriteY();
	}

	/**
	 * Round a value to the nearest 0.05.
	 * 
	 * @param value to be rounded.
	 * @return rounded value.
	 */
	private double roundPercentage(double value) {
		long round = Math.round(value * 20);
		value = (double)round / 20;

		return value;
	}

	/**
	 * Set itemHeightSVF to the default value of the currently selected card 
	 * item.
	 */
	public void resetCurrentH() {
		itemHeightSVF.setValue(roundPercentage(current.getItem().getH()));
	}

	/**
	 * Set itemCentreXSVF to the default value of the currently selected card 
	 * item.
	 */
	public void resetCurrentX() {
		itemCentreXSVF.setValue(roundPercentage(current.getItem().getX()));
	}

	/**
	 * Set itemCentreYSVF to the default value of the currently selected card 
	 * item.
	 */
	public void resetCurrentY() {
		itemCentreYSVF.setValue(roundPercentage(current.getItem().getY()));
	}

	/**
	 * Set the height of the currently selected card item.
	 * 
	 * @param value as a percentage of the card height.
	 * @param updateSVF flag indicating if itemHeightSVF should be updated.
	 */
	public void setCurrentH(double value, boolean updateSVF) {
    	System.out.println("model.setCurrentH(" + value + ");");

        current.setSize(value);
        if (updateSVF)
        	itemHeightSVF.setValue(roundPercentage(value));
	}

	/**
	 * Set the X co-ordinate of the centre of the currently selected card item.
	 * 
	 * @param value as a percentage of the card width.
	 * @param updateSVF flag indicating if itemCentreXSVF should be updated.
	 */
	public void setCurrentX(double value, boolean updateSVF) {
    	System.out.println("model.setCurrentX(" + value + ");");

        current.setX(value);
        if (updateSVF)
        	itemCentreXSVF.setValue(roundPercentage(value));
	}

	/**
	 * Set the Y co-ordinate of the centre of the currently selected card item.
	 * 
	 * @param value as a percentage of the card height.
	 * @param updateSVF flag indicating if itemCentreYSVF should be updated.
	 */
	public void setCurrentY(double value, boolean updateSVF) {
    	System.out.println("model.setCurrentY(" + value + ");");

        current.setY(value);
        if (updateSVF)
        	itemCentreYSVF.setValue(roundPercentage(value));
	}



    /************************************************************************
     * Update the "Modify Card Item" controls.
     */

	/**
	 * Set the current card item and adjust the Card Item spinners.
	 * @param item currently selected card item Payload.
	 */
	private void changeCurrentCardItemAndSyncSpinners(Payload item) {
        current = item;
	    itemHeightSVF.setValue(roundPercentage(getCurrentH()));
	    itemCentreXSVF.setValue(roundPercentage(getCurrentX()));
	    itemCentreYSVF.setValue(roundPercentage(getCurrentY()));
	}

	/**
	 * Initialize "Modify Selected Card Item" panel.
	 */
    private void initializeModifySelectedCardItem() {
        itemHeightSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, 0.05);
        itemCentreXSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, 0.05);
        itemCentreYSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 10, 0.05);
	}




	/************************************************************************
	 * Support code for "Sample" panel.
	 */

	private Group group;
    private Image image;
	private Handle handle;

	/**
	 * @return the Group used by the "Sample" panel.
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @return the Handle Image used by the "Sample" panel.
	 */
	public Image getHandleImage() {
		return image;
	}

	/**
	 * @return the Handle used by the "Sample" panel.
	 */
	public Handle getHandle() {
		return handle;
	}

    /**
	 * Initialize "Sample" panel.
	 */
	private void initializeSample() {
		group = new Group();
		image = new Image(getClass().getResourceAsStream("Handle.png"));
		handle = new Handle(image);
		group.getChildren().add(handle);
    }



	/************************************************************************
	 * Support code for "Playing Card Generator" panel.
	 */
	private final String PATHSFILE = "Files.txt";


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
		initializeSample();
	}

	/**
	 * Initialization for the "Playing Card Generator" panel after a base 
	 * directory has been selected.
	 */
	public void init() {
		System.out.println("init()");

		initializeCardItemPayloads();
	}


	/************************************************************************
	 * Debug.
	 */
	public void displayDiags() {
		System.out.println(getFaceDirectory());
		System.out.println(getIndexDirectory());
		System.out.println(getPipDirectory());
		System.out.println(getOutputDirectory());
		System.out.println();
	
		if (cardSize == CardSize.POKER)
	    	System.out.println("Poker size");
		else
		if (cardSize == CardSize.BRIDGE)
	    	System.out.println("Bridge size");
		else
		if (cardSize == CardSize.FREE)
	    	System.out.println("Free size");
		System.out.println();
	
		System.out.println("Index " + (displayIndex ? "displayed" : "NOT displayed"));
		System.out.println("Corner pip " + (displayCornerPip ? "displayed" : "NOT displayed"));
		System.out.println("Standard pip " + (displayStandardPip ? "displayed" : "NOT displayed"));
		System.out.println("Face image " + (displayFaceImage ? "displayed" : "NOT displayed"));
		System.out.println("Face pip " + (displayFacePip ? "displayed" : "NOT displayed"));
		System.out.println();
	
		if (currentItem == Item.INDEX)
	    	System.out.println("Change Index " + currentItem.getH());
		else
		if (currentItem == Item.CORNER_PIP)
	    	System.out.println("Change Corner pip " + currentItem.getH());
		else
		if (currentItem == Item.STANDARD_PIP)
	    	System.out.println("Change Standard pip " + currentItem.getH());
		else
		if (currentItem == Item.FACE)
	    	System.out.println("Change Face image " + currentItem.getH());
		else
		if (currentItem == Item.FACE_PIP)
	    	System.out.println("Change Face pip " + currentItem.getH());
	
	//	if (cardItem == CardItem.INDEX)
	//    	System.out.println("Change Index");
	//	else
	//	if (cardItem == CardItem.CORNER_PIP)
	//    	System.out.println("Change Corner pip");
	//	else
	//	if (cardItem == CardItem.STANDARD_PIP)
	//    	System.out.println("Change Standard pip");
	//	else
	//	if (cardItem == CardItem.FACE)
	//    	System.out.println("Change Face image");
	//	else
	//	if (cardItem == CardItem.FACE_PIP)
	//    	System.out.println("Change Face pip");
	
		System.out.println((keepAspectRatio ? "Keeping" : "NOT keeping") + " image aspect ratio");
	
		System.out.println("Current card: " + suits[suit] + cards[card]);
	
		System.out.println("Index path: " + index.getPath());
		System.out.println("Corner Pip path: " + cornerPip.getPath());
		System.out.println("Standard Pip path: " + standardPip.getPath());
		System.out.println("Face path: " + face.getPath());
		System.out.println("FacePip path: " + facePip.getPath());
		System.out.println("Current path: " + current.getPath());
	}

}


