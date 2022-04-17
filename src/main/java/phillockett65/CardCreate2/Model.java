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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import phillockett65.CardCreate2.sample.Default;
import phillockett65.CardCreate2.sample.DoublePayload;
import phillockett65.CardCreate2.sample.Handle;
import phillockett65.CardCreate2.sample.ImagePayload;
import phillockett65.CardCreate2.sample.Item;
import phillockett65.CardCreate2.sample.MultiPayload;
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
        updateCardItemDisplayStatus();
        updateHandleState();
    }

    /**
     * Set the selected index style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setIndexStyle(String style) {
        indexStyle = style;
        setIndexCardItemPayload();
        updateCardItemDisplayStatus();
        updateHandleState();
    }

    /**
     * Set the selected pip style and update the necessary card item Payloads.
     * 
     * @param style selected.
     */
    public void setPipStyle(String style) {
        pipStyle = style;
        setPipCardItemPayloads();
        updateCardItemDisplayStatus();
        updateHandleState();
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

    /**
     * Set up the base directory, check if it is valid and set up the style 
     * lists. 
     * 
     * @param base path of the directory.
     * @return true if base is a valid base directory, false otherwise.
     */
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

            makeCardsDirectory();
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

    private boolean makeOutputDirectory() {
        File dir = new File(getOutputDirectory());
        if (dir.exists())
            return true;

        return dir.mkdir();
    }

    private boolean makeCardsDirectory() {
        File dir = new File(baseDirectory + "\\cards");
        if (dir.exists())
            return true;

        return dir.mkdir();
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
        updateHandleState();

        return suit;
    }

    public int nextCard() {
        if (++card >= cards.length)
            card = 1;

        updateCardItemDisplayStatus();
        setCardItemPayloads();
        updateHandleState();

        return card;
    }

    public int prevSuit() {
        if (--suit < 0)
            suit = suits.length - 1;

        updateCardItemDisplayStatus();
        setCardItemPayloads();
        updateHandleState();

        return suit;
    }

    public int prevCard() {
        if (--card <= 0)
            card = cards.length - 1;

        updateCardItemDisplayStatus();
        setCardItemPayloads();
        updateHandleState();

        return card;
    }

    /**
     * @return true if the specified card is a face card (court card), false 
     * otherwise.
     */
    private boolean isFaceCard(int c) {
        return c > 10;
    }

    /**
     * @return true if the specified card has an image file, false otherwise.
     */
    private boolean isImageCard(int s, int c) {
        return isFaceImageExists(s, c);
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
    private double cornerRadius = Default.RADIUS.getFloat();

    private SpinnerValueFactory<Integer> widthSVF;
    private SpinnerValueFactory<Integer> heightSVF;
    private SpinnerValueFactory<Double>  radiusSVF;


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
        // System.out.println("setWidth(" + width + ")");
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
        // System.out.println("setHeight(" + height + ")");
        cardHeightPX = height;

        syncCardItemsWithCardSize();
    }

    /**
     * @return the corner radius in pixels.
     */
    public double getRadiusPX() {
        return cardHeightPX * cornerRadius / 100;
    }

    /**
     * Set the corner radius as a percentage of the card height.
     * 
     * @param radius value as a percentage of the card height.
     */
    public void setRadius(double radius) {
        cornerRadius = radius;

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
    public SpinnerValueFactory<Double>  getRadiusSVF()  { return radiusSVF; }

    public void resetCardWidthSVF()     { widthSVF.setValue(Default.WIDTH.getInt()); }
    public void resetCardHeightSVF()    { heightSVF.setValue(Default.HEIGHT.getInt()); }
    public void resetRadiusSVF()        { radiusSVF.setValue((double)Default.RADIUS.getFloat()); }

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
        final double RADIUS = Default.RADIUS.getFloat();
        final double MIN_RADIUS = Default.MIN_RADIUS.getFloat();
        final double MAX_RADIUS = Default.MAX_RADIUS.getFloat();

        widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, WIDTH);
        heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, HEIGHT);
        radiusSVF = new SpinnerValueFactory.DoubleSpinnerValueFactory(MIN_RADIUS, MAX_RADIUS, RADIUS, 0.2);
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
     * @return true if the index Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldIndexBeDisplayed() {
        return displayIndex;
    }

    /**
     * @return true if the corner pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldCornerPipBeDisplayed() {
        return displayCornerPip;
    }

    /**
     * @return true if the standard pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldStandardPipBeDisplayed() {
        return shouldStandardPipBeDisplayed(suit, card);
    }

    /**
     * @return true if the face image Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldFaceImageBeDisplayed() {
        return shouldFaceImageBeDisplayed(suit, card);
    }

    /**
     * @return true if the face pip Item for the current card should be 
     * displayed, false otherwise.
     */
    public boolean shouldFacePipBeDisplayed() {
        return shouldFacePipBeDisplayed(card);
    }

    /**
     * @return true if the standard pip Item should be displayed for the given 
     * card, false otherwise.
     */
    private boolean shouldStandardPipBeDisplayed(int s, int c) {
        if (!isImageCard(s, c))
            return displayStandardPip;
        
        return false;
    }

    /**
     * @return true if the face image Item should be displayed for the given 
     * card, false otherwise.
     */
    private boolean shouldFaceImageBeDisplayed(int s, int c) {
        if (isImageCard(s, c))
            return displayFaceImage;

        return false;
    }

    /**
     * @return true if the face pip Item should be displayed for the given 
     * card, false otherwise.
     */
    private boolean shouldFacePipBeDisplayed(int c) {
        if (isFaceCard(c))
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

    /**
     * Update the state of the handle based on a change to a Card Item check 
     * box. If the current card item is disabled find the next one available, 
     * otherwise set the state for the current.
     */
    private void updateHandleState() {
        if (!current.isVisible())
            setNextPayload();
        else
            handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));
    }

    public void setDisplayIndex(boolean state) {
        displayIndex = state;
        index.setVisible(shouldIndexBeDisplayed());
        updateHandleState();
    }

    public void setDisplayCornerPip(boolean state) {
        displayCornerPip = state;
        cornerPip.setVisible(shouldCornerPipBeDisplayed());
        updateHandleState();
    }

    public void setDisplayStandardPip(boolean state) {
        displayStandardPip = state;
        standardPip.setVisible(shouldStandardPipBeDisplayed());
        updateHandleState();
    }

    public void setDisplayFaceImage(boolean state) {
        displayFaceImage = state;
        face.setVisible(shouldFaceImageBeDisplayed());
        updateHandleState();
    }

    public void setDisplayFacePip(boolean state) {
        displayFacePip = state;
        facePip.setVisible(shouldFacePipBeDisplayed());
        updateHandleState();
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

    private DoublePayload index = null;
    private DoublePayload cornerPip = null;
    private MultiPayload standardPip = null;
    private ImagePayload face = null;
    private DoublePayload facePip = null;
    private Payload current = null;
    private Payload[] payloadSlider;
    
    private SpinnerValueFactory<Double> itemHeightSVF;
    private SpinnerValueFactory<Double> itemCentreXSVF;
    private SpinnerValueFactory<Double> itemCentreYSVF;

    private boolean keepAspectRatio = true;
    private boolean showGuideBox = false;


    /**
     * @return the current Item.
     */
    public Item getCurrentItem() {
        return current.getItem();
    }

    /**
     * Check if the face image file exists.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return true if the face image file exists.
     */
    private boolean isFaceImageExists(int s, int c) {
        File file = new File(getFaceImagePath(s, c));

        return file.exists();
    }

    /**
     * Generate the file path for the face image.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return the file path for the face image of the specified card in the 
     * current style.
     */
    private String getFaceImagePath(int s, int c) {
        return getFaceDirectory() + "\\" + suits[s] + cards[c] + ".png";
    }

    /**
     * Generate the file path for the index image.
     * 
     * @param s suit of card to generate.
     * @param c card number of card to generate.
     * @return the file path for the index image of the specified card in the 
     * current style.
     */
    private String getIndexImagePath(int s, int c) {
        String pathToImage = getIndexDirectory() + "\\" + suits[s] + cards[c] + ".png";
        File file = new File(pathToImage);
        if (!file.exists())
            pathToImage = getIndexDirectory() + "\\" + alts[s] + cards[c] + ".png";

        return pathToImage;
    }

    /**
     * Generate the file path for the standard pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the standard pip image of the specified card 
     * in the current style.
     */
    private String getStandardPipImagePath(int s) {
        return getPipDirectory() + "\\" + suits[s] + ".png";
    }

    /**
     * Generate the file path for the face pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the face pip image of the specified card in 
     * the current style.
     */
    private String getFacePipImagePath(int s) {
        String pathToImage = getPipDirectory() + "\\" + suits[s] + "F.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath(s);
    }

    /**
     * Generate the file path for the corner pip image.
     * 
     * @param s suit of card to generate.
     * @return the file path for the corner pip image of the specified card in 
     * the current style.
     */
    private String getCornerPipImagePath(int s) {
        String pathToImage = getPipDirectory() + "\\" + suits[s] + "S.png";
        File file = new File(pathToImage);
        if (file.exists())
            return pathToImage;

        return getStandardPipImagePath(s);
    }


    /**
     * @return the file path for the face image of the current card in the 
     * current style.
     */
    private String getFaceImagePath() {
        return getFaceImagePath(suit, card);
    }

    /**
     * @return the file path for the index image of the current card in the 
     * current style.
     */
    private String getIndexImagePath() {
        return getIndexImagePath(suit, card);
    }

    /**
     * @return the file path for the standard pip image of the current card in 
     * the current style.
     */
    private String getStandardPipImagePath() {
        return getStandardPipImagePath(suit);
    }

    /**
     * @return the file path for the face pip image of the current card in the 
     * current style.
     */
    private String getFacePipImagePath() {
        return getFacePipImagePath(suit);
    }

    /**
     * @return the file path for the corner pip image of the current card in 
     * the current style.
     */
    private String getCornerPipImagePath() {
        return getCornerPipImagePath(suit);
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

        showImageBox();
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

                handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));

                return true;
            }

            idx++;
            if (idx > payloadSlider.length)   // Safety check.
                break;
        }

        handle.syncDisplayState(shouldItemBeDisplayed(current.getItem()));

        return false;
    }

    /**
     * Create the card item Payload instances after the base directory has 
     * been selected.
     */
    private void initializeCardItemPayloads() {
        // System.out.println("initializeCardItemPayloads()");

        face		= new ImagePayload(this);

        index		= new DoublePayload(this, Item.INDEX);

        standardPip	= new MultiPayload(this);
        facePip		= new DoublePayload(this, Item.FACE_PIP);

        cornerPip	= new DoublePayload(this, Item.CORNER_PIP);

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
     * Tells the card items to synchronize with the current card size and the 
     * handle to reposition.
     */
    private void syncCardItemsWithCardSize() {
        // System.out.println("syncCardItemsWithCardSize()");

        index.syncCardSize();
        cornerPip.syncCardSize();
        standardPip.syncCardSize();
        face.syncCardSize();
        facePip.syncCardSize();

        showImageBox();
        handle.syncPosition();
    }


    public SpinnerValueFactory<Double> getItemHeightSVF()   { return itemHeightSVF; }
    public SpinnerValueFactory<Double> getItemCentreXSVF()  { return itemCentreXSVF; }
    public SpinnerValueFactory<Double> getItemCentreYSVF()  { return itemCentreYSVF; }

    public void setKeepImageAspectRatio(boolean state) {
        keepAspectRatio = state;
        face.setKeepAspectRatio(keepAspectRatio);
    }

    public void setShowGuideBox(boolean state) {
        showGuideBox = state;
        showImageBox();
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
        showImageBox();
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
        showImageBox();
        handle.syncPosition();

        if (updateSVF)
            itemCentreYSVF.setValue(roundPercentage(value));
    }

    /**
     * Get the X co-ordinate of the centre of the currently selected card item.
     */
    public double getCurrentX() {
        return current.getCentreX();
    }

    /**
     * Get the Y co-ordinate of the centre of the currently selected card item.
     */
    public double getCurrentY() {
        return current.getCentreY();
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
        SyncSpinners();
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
        SyncSpinners();
    }

    /**
     * Sync the Card Item spinners to the current payload.
     */
    private void SyncSpinners() {
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
    private Rectangle box;

    private void buildImageBox() {
        box = new Rectangle();
        box.setFill(null);
        box.setStrokeWidth(2);
        box.setStroke(Color.GREY);
        box.setVisible(false);
    }

    private void showImageBox() {
        // System.out.println("showImageBox(" + display + "); keepAspectRatio = " + keepAspectRatio);
        if (!showGuideBox) {
            box.setVisible(false);

            return;
        }

        final double pX = current.getCentreX();
        final double pY = current.getCentreY();
        final double winX = getCalculatedWidth() - (2*pX);
        final double winY = cardHeightPX - (2*pY);

        box.setX(pX);
        box.setY(pY);
        box.setWidth(winX);
        box.setHeight(winY);
        box.setVisible(true);
    }

    /**
     * @return the Group used by the "Sample" panel.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @return the Handle used by the "Sample" panel.
     */
    public Handle getHandle() {
        return handle;
    }

    /**
     * Increase the size of the current card item.
     */
    public void incCurrent() {
        current.incSize();
        SyncSpinners();
    }

    /**
     * Decrease the size of the current card item.
     */
    public void decCurrent() {
        current.decSize();
        SyncSpinners();
    }

    /**
     * Initialize "Sample" panel.
     */
    private void initializeSample() {
        group = new Group();
        handleImage = new Image(getClass().getResourceAsStream("Handle.png"));
        buildImageBox();
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Load an image file from disc.
     * 
     * @param path to the image file.
     * @return the Image, or null if the file is not found.
     */
    private Image loadImage(String path) {
        // System.out.println("loadImage(" + path + ")");
        File file = new File(path);

        if (!file.exists()) {
            // System.out.println("File does not exist!");

                return null;
        }

        Image loadedImage = null;
        try {
            loadedImage = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return loadedImage;
    }

    /**
     * Rotate the given image by 189 degrees.
     * 
     * @param image to rotate.
     * @return the rotated Image.
     */
    private Image rotateImage(Image image) {
        // System.out.println("rotateImage()");

        ImageView view = new ImageView(image);
        view.setRotate(180);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return view.snapshot(params, null);
    }


    /**
     * Draw a blank card, add the icons using the Payloads then write the 
     * image to disc.
     * 
     * @param suit of card to generate.
     * @param card number of card to generate.
     * @param images list of pip Images to use (so they are only read once).
     */
    private void save(int suit, int card, Image[] images) {

        final double xMax = getCalculatedWidth();
        final double yMax = getHeight();
        final double radius = getRadiusPX();

        Group root = new Group();
        // We need this Scene otherwise the canvas gets default background colour.
        Scene s = new Scene(root, xMax, yMax, Color.TRANSPARENT);
        final Canvas canvas = new Canvas(xMax, yMax);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // We add the canvas to the root otherwise the snapshot gets default background colour.
        root.getChildren().add(canvas);

        gc.setFill(backgroundColour);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.fillRoundRect(0, 0, xMax, yMax, radius, radius);
        gc.strokeRoundRect(0, 0, xMax, yMax, radius, radius);

        if (shouldIndexBeDisplayed()) {
            Image image = loadImage(getIndexImagePath(suit, card));
            Image rotatedImage = rotateImage(image);

            index.drawCard(gc, image, rotatedImage);
        }

        if (shouldCornerPipBeDisplayed())
            cornerPip.drawCard(gc, images[2], images[3]);

        if (shouldFaceImageBeDisplayed(suit, card)) {
            Image image = loadImage(getFaceImagePath(suit, card));
            Image rotatedImage = rotateImage(image);

            face.drawCard(gc, image, rotatedImage);
        }

        if (shouldStandardPipBeDisplayed(suit, card))
            standardPip.drawCard(gc, images[0], images[1], card);

        if (shouldFacePipBeDisplayed(card))
            facePip.drawCard(gc, images[4], images[5]);

        String outputPath = getOutputDirectory() + "\\" + suits[suit] + cards[card] + ".png";
        // System.out.println(outputPath);

        try {
            Image snapshot = canvas.snapshot(null, null);
            BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
            
            ImageIO.write(image, "png", new File(outputPath));
        } catch (Exception e) {
            System.out.println("Failed saving image: " + e);
        }

    }

    /**
     * Draw a blank card, add the joker images then write to disc.
     * 
     * @param suit of joker to generate.
     */
    private int joker(int suit, int errors) {

        final double xMax = getCalculatedWidth();
        final double yMax = getHeight();
        final double radius = getRadiusPX();

        Group root = new Group();
        // We need this Scene otherwise the canvas gets default background colour.
        Scene s = new Scene(root, xMax, yMax, Color.TRANSPARENT);
        final Canvas canvas = new Canvas(xMax, yMax);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // We add the canvas to the root otherwise the snapshot gets default background colour.
        root.getChildren().add(canvas);

        gc.setFill(backgroundColour);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.fillRoundRect(0, 0, xMax, yMax, radius, radius);
        gc.strokeRoundRect(0, 0, xMax, yMax, radius, radius);

        // Draw indices specific to the suit.
        String pathToImage = getIndexDirectory() + "\\" + suits[suit] + cards[0] + ".png";
        Image indexImage = loadImage(pathToImage);
        if (indexImage != null) {
            Image rotatedImage = rotateImage(indexImage);

            index.drawJoker(gc, indexImage, rotatedImage);
        }

        // Draw face image specific to the suit.
        Image faceImage = loadImage(getFaceImagePath(suit, 0));
        
        if (faceImage == null) {
            errors++;
            if (errors % 2 == 1) {
                faceImage = loadImage(baseDirectory + "\\boneyard\\Back.png");
            }
        }
        face.drawJoker(gc, faceImage);

        String outputPath = getOutputDirectory() + "\\" + suits[suit] + cards[0] + ".png";
        // System.out.println(outputPath);

        try {
            Image snapshot = canvas.snapshot(null, null);
            BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
            
            ImageIO.write(image, "png", new File(outputPath));
        } catch (Exception e) {
            System.out.println("Failed saving image: " + e);
        }

        return errors;
    }

    /**
     * Generate the card images and save them to disc.
     */
    public void generate() {

        // Ensure that the output directory exists.
        makeOutputDirectory();

        // Generate the cards.
        for (int suit = 0; suit < suits.length; ++suit) {
            Image[] images = new Image[6];
            images[0] = loadImage(getStandardPipImagePath(suit));
            images[1] = rotateImage(images[0]);
            images[2] = loadImage(getCornerPipImagePath(suit));
            images[3] = rotateImage(images[2]);
            images[4] = loadImage(getFacePipImagePath(suit));
            images[5] = rotateImage(images[4]);

            for (int card = 1; card < cards.length; ++card) {
                save(suit, card, images);
            }
        }

        // Generate the jokers.
        int errors = 0;
        for (int suit = 0; suit < suits.length; ++suit)
            errors = joker(suit, errors);

    }



    /************************************************************************
     * Support code for the Initialization of the Model.
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
        makeCardsDirectory();

        // Add handle to the group last so that it is displayed on top.
        group.getChildren().add(box);
        group.getChildren().add(handle);
    }


}
