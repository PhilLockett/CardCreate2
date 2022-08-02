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
 * DataStore is a class that is serializing the settings data for saving and 
 * restoring to and from disc.
 */
package phillockett65.CardCreate2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.paint.Color;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private String faceStyle;
    private String indexStyle;
    private String pipStyle;

    private Boolean manual;
    private String outputName;

    private Integer suit;
    private Integer card;

    private Integer cardSize;
    private Double cardHeightPX;
    private Double cardWidthPX;

    private Double red;
    private Double green;
    private Double blue;

    private Boolean displayIndex;
    private Boolean displayCornerPip;
    private Boolean displayStandardPip;
    private Boolean displayFaceImage;
    private Boolean displayFacePip;

    ArrayList<CardItemData> cardItemDataList; 
    private Integer current;

    private Boolean independentlySetCornerRadii;
    private Double arcWidth;
    private Double arcHeight;
    private Boolean cropCorners;

    private Boolean displayCourtWatermark = true;
    private Boolean displayImageWatermark = true;
    private Boolean displayNumberWatermark = true;

    private Boolean lockX;
    private Boolean lockY;
    private Boolean leftHanded;
    private Boolean showGuideBox;

    private Boolean borderlessJokers;


    public DataStore() {
        
    }

    private boolean pull(Model model) {
        boolean success = true;

        faceStyle   = model.getFaceStyle();
        indexStyle  = model.getIndexStyle();
        pipStyle    = model.getPipStyle();

        manual = model.isManual();
        outputName = model.getManualOutputName();

        suit = model.getSuit();
        card = model.getCard();

        cardSize = getCardSize(model);
        cardHeightPX = model.getHeight();
        cardWidthPX = model.getUserWidth();

        red = model.getBackgroundColour().getRed();
        green = model.getBackgroundColour().getGreen();
        blue = model.getBackgroundColour().getBlue();

        displayIndex = model.isDisplayIndex();
        displayCornerPip = model.isDisplayCornerPip();
        displayStandardPip = model.isDisplayStandardPip();
        displayFaceImage = model.isDisplayFaceImage();
        displayFacePip = model.isDisplayFacePip();

        cardItemDataList = model.buildCardItemDataList();
        current = model.getCurrentItemId();

        independentlySetCornerRadii = model.isSetCornerRadiiIndependently();
        arcWidth = model.getArcWidth();
        arcHeight = model.getArcHeight();
        cropCorners = model.isCropCorners();

        displayCourtWatermark = model.isDisplayCourtWatermark();
        displayImageWatermark = model.isDisplayImageWatermark();
        displayNumberWatermark = model.isDisplayNumberWatermark();

        lockX = model.isLockX();
        lockY = model.isLockY();
        leftHanded = model.isLeftHanded();
        showGuideBox = model.isShowGuideBox();

        borderlessJokers = model.isBorderlessJokers();
 
        return success;
    }

    private boolean push(Model model) {
        boolean success = true;
        
        model.setFaceStyle(faceStyle);
        model.setIndexStyle(indexStyle);
        model.setPipStyle(pipStyle);

        model.setOutputNameManually(manual);
        model.setOutputName(outputName);

        model.setSuit(suit);
        model.setCard(card);

        setCardSize(model);
        model.setHeight(cardHeightPX);
        model.setWidth(cardWidthPX);

        model.setBackgroundColour(Color.color(red, green, blue));

        model.setDisplayIndex(displayIndex);
        model.setDisplayCornerPip(displayCornerPip);
        model.setDisplayStandardPip(displayStandardPip);
        model.setDisplayFaceImage(displayFaceImage);
        model.setDisplayFacePip(displayFacePip);

        model.injectCardItemDataList(cardItemDataList);
        model.setCurrentItemFromId(current);

        model.setCornerRadiiIndependently(independentlySetCornerRadii);
        model.setArcWidth(arcWidth);
        model.setArcHeight(arcHeight);
        model.setCropCorners(cropCorners);

        model.setDisplayCourtWatermark(displayCourtWatermark);
        model.setDisplayImageWatermark(displayImageWatermark);
        model.setDisplayNumberWatermark(displayNumberWatermark);

        model.setLockX(lockX);
        model.setLockY(lockY);
        model.setLeftHanded(leftHanded);
        model.setShowGuideBox(showGuideBox);

        model.setBorderlessJokers(borderlessJokers);

        return success;
    }



    /************************************************************************
     * Support code for static public interface.
     */

    public static boolean writeData(Model model) {
        boolean success = false;

        // Ensure that the output directory exists.
        model.makeOutputDirectory();

        DataStore dataStore = new DataStore();
        dataStore.pull(model);

        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(model.getSettingsFile()));

            objectOutputStream.writeObject(dataStore);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return success;
    }

    public static boolean readData(Model model) {
        boolean success = false;

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(model.getSettingsFile()));

            DataStore dataStore = (DataStore)objectInputStream.readObject();
            success = dataStore.push(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return success;
    }



    /************************************************************************
     * Support code for mapping data.
     */

    private void setCardSize(Model model) {
        if (cardSize == 0)
            model.setPokerCardSize();
        else if (cardSize == 1)
            model.setBridgeCardSize();
        else if (cardSize == 2)
            model.setFreeCardSize();
        else
            model.setMpcCardSize();
    }

    private int getCardSize(Model model) {
        if (model.isPokerCardSize()) return 0;

        if (model.isBridgeCardSize()) return 1;

        if (model.isFreeCardSize()) return 2;

        if (model.isMpcCardSize()) return 3;

        return 0;
    }

}

