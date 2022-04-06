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

    private final String PATHSFILE = "Files.txt";

    private boolean validBaseDirectory = false;
    private String baseDirectory = ".";
    private String faceStyle;
    private String indexStyle;
    private String pipStyle;

    ObservableList<String> baseList = FXCollections.observableArrayList();
    ObservableList<String> faceList = FXCollections.observableArrayList();
    ObservableList<String> indexList = FXCollections.observableArrayList();
    ObservableList<String> pipList = FXCollections.observableArrayList();

    public ObservableList<String> getBaseList() { return baseList; }

    public ObservableList<String> getFaceList()  { return faceList; }
    public ObservableList<String> getIndexList() { return indexList; }
    public ObservableList<String> getPipList()   { return pipList; }


    public boolean isValidBaseDirectory() { return validBaseDirectory; }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    private String getFaceDirectory() {
        return baseDirectory + "\\faces\\" + faceStyle;
    }

    private String getIndexDirectory() {
        return baseDirectory + "\\indices\\" + indexStyle;
    }

    private String getPipDirectory() {
        return baseDirectory + "\\pips\\" + pipStyle;
    }

    public String getFaceStyle()  { return faceStyle; }
    public String getIndexStyle() { return indexStyle; }
    public String getPipStyle()   { return pipStyle; }

    /**
     * Set the selected face style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setFaceStyle(String style) {
        faceStyle = style;
        setFaceCardItemPayload();
    }

    /**
     * Set the selected index style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setIndexStyle(String style) {
        indexStyle = style;
        setIndexCardItemPayload();
    }

    /**
     * Set the selected pip style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
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

        updateCardItemDisplayStatus();
        setCardItemPayloads();

        return suit;
    }

    public int nextCard() {
        if (++card >= cards.length)
            card = 1;

        updateCardItemDisplayStatus();
        setCardItemPayloads();

        return card;
    }

    public int prevSuit() {
        if (--suit < 0)
            suit = suits.length - 1;

        updateCardItemDisplayStatus();
        setCardItemPayloads();

        return suit;
    }

    public int prevCard() {
        if (--card <= 0)
            card = cards.length - 1;

        updateCardItemDisplayStatus();
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
        return isFaceImageExists();
    }

    /**
     * Initialize "Sample Navigation" panel.
     */
    private void initializeSampleNavigation() {
    }



    /************************************************************************
     * Support code for "Card Size" panel.
     */

    private enum CardSize { POKER, BRIDGE, FREE };
    
    private CardSize cardSize = CardSize.POKER;

    private double cardWidthPX = Default.WIDTH.getInt();
    private double cardHeightPX = Default.HEIGHT.getInt();

    private SpinnerValueFactory<Integer> widthSVF;
    private SpinnerValueFactory<Integer> heightSVF;


    public void setPokerCardSize() {
        cardSize = CardSize.POKER;

        syncCardItemsWithCardSize();
    }

    public void setBridgeCardSize() {
        cardSize = CardSize.BRIDGE;

        syncCardItemsWithCardSize();
    }

    public void setFreeCardSize() {
        cardSize = CardSize.FREE;

        syncCardItemsWithCardSize();
    }

    public boolean isAutoCardWidth() { return cardSize != CardSize.FREE; }

    /**
     * @return the freely set card width in pixels.
     */
    public double getWidth() {
        return cardWidthPX;
    }

    /**
     * @return the calculated card width in pixels for Poker and Bridge, or 
     * the freely set width.
     */
    public double getCalculatedWidth() {
        if (cardSize == CardSize.POKER)
            return cardHeightPX * 5 / 7;

        if (cardSize == CardSize.BRIDGE)
            return cardHeightPX * 9 / 14;

        return cardWidthPX;
    }

    /**
     * Set the freely set card width in pixels.
     * 
     * @param width value in pixels.
     */
    public void setWidth(double width) {
        cardWidthPX = width;

        syncCardItemsWithCardSize();
    }

    /**
     * @return the card height in pixels.
     */
    public double getHeight() {
        return cardHeightPX;
    }

    /**
     * Set the card height in pixels.
     * 
     * @param height value in pixels.
     */
    public void setHeight(double height) {
        cardHeightPX = height;
        syncCardItemsWithCardSize();
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

    public SpinnerValueFactory<Integer> getWidthSVF()   { return widthSVF; }
    public SpinnerValueFactory<Integer> getHeightSVF()  { return heightSVF; }

    public void resetCardWidthSVF()     { widthSVF.setValue(Default.WIDTH.getInt()); }
    public void resetCardHeightSVF()    { heightSVF.setValue(Default.HEIGHT.getInt()); }

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

    public Color getBackgroundColour() { return backgroundColour; }
    public void setBackgroundColour(Color colour) { backgroundColour = colour; }

    private int colourRealToInt(double comp) {
        return (int)(comp * 256);
    }

    public String getBackgroundColourString() {
        return String.format("rgb(%d, %d, %d)",
                colourRealToInt(backgroundColour.getRed()),
                colourRealToInt(backgroundColour.getGreen()),
                colourRealToInt(backgroundColour.getBlue()));
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

    /**
     * @return true if the index Item should be displayed, false otherwise.
     */
    public boolean shouldIndexBeDisplayed() {
        return displayIndex;
    }

    /**
     * @return true if the corner pip Item should be displayed, false otherwise.
     */
    public boolean shouldCornerPipBeDisplayed() {
        return displayCornerPip;
    }

    /**
     * @return true if the standard pip Item should be displayed, false otherwise.
     */
    public boolean shouldStandardPipBeDisplayed() {
        if (!isImageCard())
            return displayStandardPip;
        
        return false;
    }

    /**
     * @return true if the face image Item should be displayed, false otherwise.
     */
    public boolean shouldFaceImageBeDisplayed() {
        if (isImageCard())
            return displayFaceImage;

        return false;
    }

    /**
     * @return true if the face pip Item should be displayed, false otherwise.
     */
    public boolean shouldFacePipBeDisplayed() {
        if (isFaceCard())
            return displayFacePip;

        return false;
    }

    /**
     * @return true if the specified Item should be displayed for the current 
     * card, false otherwise.
     */
    private boolean shouldItemBeDisplayed(Item item) {
        if (item == Item.INDEX)
            return shouldIndexBeDisplayed();
        if (item == Item.CORNER_PIP)
            return shouldCornerPipBeDisplayed();
        if (item == Item.STANDARD_PIP)
            return shouldStandardPipBeDisplayed();
        if (item == Item.FACE)
            return shouldFaceImageBeDisplayed();
        if (item == Item.FACE_PIP)
            return shouldFacePipBeDisplayed();
 
        return false;
    }

    public void setDisplayIndex(boolean state) {
        displayIndex = state;
        index.setVisible(shouldIndexBeDisplayed());
        handle.syncDisplayState(shouldIndexBeDisplayed());
    }

    public void setDisplayCornerPip(boolean state) {
        displayCornerPip = state;
        cornerPip.setVisible(shouldCornerPipBeDisplayed());
        handle.syncDisplayState(shouldCornerPipBeDisplayed());
    }

    public void setDisplayStandardPip(boolean state) {
        displayStandardPip = state;
        standardPip.setVisible(shouldStandardPipBeDisplayed());
        handle.syncDisplayState(shouldStandardPipBeDisplayed());
    }

    public void setDisplayFaceImage(boolean state) {
        displayFaceImage = state;
        face.setVisible(shouldFaceImageBeDisplayed());
        handle.syncDisplayState(shouldFaceImageBeDisplayed());
    }

    public void setDisplayFacePip(boolean state) {
        displayFacePip = state;
        facePip.setVisible(shouldFacePipBeDisplayed());
        handle.syncDisplayState(shouldFacePipBeDisplayed());
    }

    /**
     * Initialize "Display Card Items" panel.
     */
    private void initializeDisplayCardItems() {
    }



    /************************************************************************
     * Support code for "Select Card Item" panel.
     */

    public void setCurrentCardItemToIndex() {
        // System.out.println("setCurrentCardItemToIndex()");
        changeCurrentCardItemAndSyncSpinners(index);
        handle.syncDisplayState(shouldIndexBeDisplayed());
    }

    public void setCurrentCardItemToCornerPip() {
        // System.out.println("setCurrentCardItemToCornerPip()");
        changeCurrentCardItemAndSyncSpinners(cornerPip);
        handle.syncDisplayState(shouldCornerPipBeDisplayed());
    }

    public void setCurrentCardItemToStandardPip() {
        // System.out.println("setCurrentCardItemToStandardPip()");
        changeCurrentCardItemAndSyncSpinners(standardPip);
        handle.syncDisplayState(shouldStandardPipBeDisplayed());
    }

    public void setCurrentCardItemToFace() {
        // System.out.println("setCurrentCardItemToFace()");
        changeCurrentCardItemAndSyncSpinners(face);
        handle.syncDisplayState(shouldFaceImageBeDisplayed());
    }

    public void setCurrentCardItemToFacePip() {
        // System.out.println("setCurrentCardItemToFacePip()");
        changeCurrentCardItemAndSyncSpinners(facePip);
        handle.syncDisplayState(shouldFacePipBeDisplayed());
    }

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
    private Payload[] payloadSlider;
    
    private SpinnerValueFactory<Double> itemHeightSVF;
    private SpinnerValueFactory<Double> itemCentreXSVF;
    private SpinnerValueFactory<Double> itemCentreYSVF;

    private boolean keepAspectRatio = true;


    /**
     * @return true if the face image file exits.
     */
    private boolean isFaceImageExists() {
        File file = new File(getFaceImagePath());

        return file.exists();
    }

    /**
     * @return the file path for the face image of the current card in the 
     * current style.
     */
    private String getFaceImagePath() {
        return getFaceDirectory() + "\\" + suits[suit] + cards[card] + ".png";
    }

    /**
     * @return the file path for the index image of the current card in the 
     * current style.
     */
    private String getIndexImagePath() {
        String pathToImage = getIndexDirectory() + "\\" + suits[suit] + cards[card] + ".png";
        File file = new File(pathToImage);
        if (!file.exists())
            pathToImage = getIndexDirectory() + "\\" + alts[suit] + cards[card] + ".png";

        return pathToImage;
    }

    /**
     * @return the file path for the standard pip image of the current card in 
     * the current style.
     */
    private String getStandardPipImagePath() {
        return getPipDirectory() + "\\" + suits[suit] + ".png";
    }

    /**
     * @return the file path for the face pip image of the current card in the 
     * current style.
     */
    private String getFacePipImagePath() {
        String pathToImage = getPipDirectory() + "\\" + suits[suit] + "F.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath();
    }

    /**
     * @return the file path for the corner pip image of the current card in 
     * the current style.
     */
    private String getCornerPipImagePath() {
        String pathToImage = getPipDirectory() + "\\" + suits[suit] + "S.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath();
    }

    /**
     * Get the file path for the image for a specified Item.
     * 
     * @param item for which the image file path is required.
     * @return the file path for the image of the given Item.
     */
    public String getImagePath(Item item) {
        if (item == Item.FACE)
            return getFaceImagePath();

        if (item == Item.INDEX)
            return getIndexImagePath();

        if (item == Item.STANDARD_PIP)
            return getStandardPipImagePath();

        if (item == Item.FACE_PIP)
            return getFacePipImagePath();

        if (item == Item.CORNER_PIP)
            return getCornerPipImagePath();

        return "";
    }

    private void setFaceCardItemPayload() {
        face.syncImageFile();
    }

    private void setIndexCardItemPayload() {
        index.syncImageFile();
    }

    private void setPipCardItemPayloads() {
        standardPip.syncImageFile();

        facePip.syncImageFile();

        cornerPip.syncImageFile();
    }

    /**
     * Synchronize the card items with the current selected images.
     */
    private void setCardItemPayloads() {
        setFaceCardItemPayload();
        setIndexCardItemPayload();
        setPipCardItemPayloads();
    }

    /**
     * Set the display status of the card items specifically for the current 
     * card.
     */
    private void updateCardItemDisplayStatus() {
        index.setVisible(shouldIndexBeDisplayed());
        cornerPip.setVisible(shouldCornerPipBeDisplayed());

        face.setVisible(shouldFaceImageBeDisplayed());
        standardPip.setVisible(shouldStandardPipBeDisplayed());

        facePip.setVisible(shouldFacePipBeDisplayed());

        handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));
    }

    /**
     * Find the next displayable card item for the current card and update the 
     * current card item.
     * 
     * @return true if a new displayable card item is found, false otherwise.
     */
    public boolean setNextPayload() {
        int idx = current.getItem().index() + 1;

        while (payloadSlider[idx].getItem() != current.getItem()) {
            if (shouldItemBeDisplayed(payloadSlider[idx].getItem())) {
                changeCurrentCardItemAndSyncSpinners(payloadSlider[idx]);

                return true;
            }

            idx++;
            if (idx > payloadSlider.length)   // Safety check.
                break;
        }

        return false;
    }

    /**
     * Create the card item Payload instances after the base directory has 
     * been selected.
     */
    private void initializeCardItemPayloads() {
        // System.out.println("initializeCardItemPayloads()");

        face		= new Payload(this, Item.FACE);

        index		= new Payload(this, Item.INDEX);

        standardPip	= new Payload(this, Item.STANDARD_PIP);
        facePip		= new Payload(this, Item.FACE_PIP);

        cornerPip	= new Payload(this, Item.CORNER_PIP);

        // Set up payload slider used to determine next item.
        final int CARDITEMCOUNT = 5;
        payloadSlider = new Payload[CARDITEMCOUNT * 2];
        payloadSlider[Item.FACE.index()] = face;
        payloadSlider[Item.FACE.index() + CARDITEMCOUNT] = face;
        payloadSlider[Item.INDEX.index()] = index;
        payloadSlider[Item.INDEX.index() + CARDITEMCOUNT] = index;
        payloadSlider[Item.STANDARD_PIP.index()] = standardPip;
        payloadSlider[Item.STANDARD_PIP.index() + CARDITEMCOUNT] = standardPip;
        payloadSlider[Item.FACE_PIP.index()] = facePip;
        payloadSlider[Item.FACE_PIP.index() + CARDITEMCOUNT] = facePip;
        payloadSlider[Item.CORNER_PIP.index()] = cornerPip;
        payloadSlider[Item.CORNER_PIP.index() + CARDITEMCOUNT] = cornerPip;

        initCurrentCardItemAndSyncSpinners(index);
        updateCardItemDisplayStatus();
    }

    /**
     * Tells the card items to synchronize with the current card size.
     */
    private void syncCardItemsWithCardSize() {
        index.syncCardSize();
        cornerPip.syncCardSize();
        standardPip.syncCardSize();
        face.syncCardSize();
        facePip.syncCardSize();
    }


    public SpinnerValueFactory<Double> getItemHeightSVF()   { return itemHeightSVF; }
    public SpinnerValueFactory<Double> getItemCentreXSVF()  { return itemCentreXSVF; }
    public SpinnerValueFactory<Double> getItemCentreYSVF()  { return itemCentreYSVF; }

    public boolean iskeepImageAspectRatio() { return keepAspectRatio; }

    public void setkeepImageAspectRatio(boolean state) {
        keepAspectRatio = state;
        face.setKeepAspectRatio(keepAspectRatio);
    }


    public Tooltip getCurrentHButtonTip() { return new Tooltip(current.getItem().getHButtonTip()); }
    public Tooltip getCurrentXButtonTip() { return new Tooltip(current.getItem().getXButtonTip()); }
    public Tooltip getCurrentYButtonTip() { return new Tooltip(current.getItem().getYButtonTip()); }

    public Tooltip getCurrentHToolTip() { return new Tooltip(current.getItem().getHToolTip()); }
    public Tooltip getCurrentXToolTip() { return new Tooltip(current.getItem().getXToolTip()); }
    public Tooltip getCurrentYToolTip() { return new Tooltip(current.getItem().getYToolTip()); }

    public String getCurrentHLabel() { return current.getItem().getHLabel(); }
    public String getCurrentXLabel() { return current.getItem().getXLabel(); }
    public String getCurrentYLabel() { return current.getItem().getYLabel(); }

    /**
     * @return true if the height of the current card item should be user
     * changable, false otherwise.
     */
    public boolean isCurrentHeightChangable() {
        if (!shouldItemBeDisplayed(current.getItem()))
            return false;

        return current.getItem().isCentre();
    }

    /**
     * @return true if the x coordinate of the centre of the current card item 
     * should be user changable, false otherwise.
     */
    public boolean isCurrentXCentreChangable() {
        return shouldItemBeDisplayed(current.getItem());
    }

    /**
     * @return true if the y coordinate of the centre of the current card item 
     * should be user changable, false otherwise.
     */
    public boolean isCurrentYCentreChangable() {
        return shouldItemBeDisplayed(current.getItem());
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
    public void resetCurrentHSVF() {
        itemHeightSVF.setValue(roundPercentage(current.getItem().getH()));
    }

    /**
     * Set itemCentreXSVF to the default value of the currently selected card 
     * item.
     */
    public void resetCurrentXSVF() {
        itemCentreXSVF.setValue(roundPercentage(current.getItem().getX()));
    }

    /**
     * Set itemCentreYSVF to the default value of the currently selected card 
     * item.
     */
    public void resetCurrentYSVF() {
        itemCentreYSVF.setValue(roundPercentage(current.getItem().getY()));
    }

    /**
     * Set the height of the currently selected card item.
     * 
     * @param value as a percentage of the card height.
     * @param updateSVF flag indicating if itemHeightSVF should be updated.
     */
    public void setCurrentH(double value, boolean updateSVF) {
        // System.out.println("model.setCurrentH(" + value + ");");

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
        // System.out.println("model.setCurrentX(" + value + ");");

        current.setX(value);
        handle.syncPosition();

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
        // System.out.println("model.setCurrentY(" + value + ");");

        current.setY(value);
        handle.syncPosition();

        if (updateSVF)
            itemCentreYSVF.setValue(roundPercentage(value));
    }


    /**
     * Init the current card item, create the handle and adjust the Card Item 
     * spinners.
     * 
     * @param item currently selected card item Payload.
     */
    private void initCurrentCardItemAndSyncSpinners(Payload item) {
        current = item;
        handle = new Handle(handleImage, current);
        handle.setPayload(current);

        itemHeightSVF.setValue(roundPercentage(current.getSpriteH()));
        itemCentreXSVF.setValue(roundPercentage(current.getSpriteX()));
        itemCentreYSVF.setValue(roundPercentage(current.getSpriteY()));
    }

    /**
     * Change the current card item, adjust the Card Item spinners and update 
     * the handle with the new Payload.
     * 
     * @param item currently selected card item Payload.
     */
    private void changeCurrentCardItemAndSyncSpinners(Payload item) {
        current = item;
        handle.setPayload(current);

        itemHeightSVF.setValue(roundPercentage(current.getSpriteH()));
        itemCentreXSVF.setValue(roundPercentage(current.getSpriteX()));
        itemCentreYSVF.setValue(roundPercentage(current.getSpriteY()));
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
    private Image handleImage;
    private Handle handle;

    /**
     * @return the Group used by the "Sample" panel.
     */
    public Group getGroup() {
        return group;
    }

    /**
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
        handleImage = new Image(getClass().getResourceAsStream("Handle.png"));
    }



    /************************************************************************
     * Support code for "Playing Card Generator" panel.
     */

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
     * Initialization after a base directory has been selected.
     */
    public void init() {
        // System.out.println("init()");

        initializeCardItemPayloads();

        // Add handle to the group last so that it is displayed on top.
        group.getChildren().add(handle);
    }


}
