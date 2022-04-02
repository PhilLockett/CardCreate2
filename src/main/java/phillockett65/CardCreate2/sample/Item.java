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
 * Item is an enumeration that captures the attributes of the 5 card items 
 * (index, corner pip, standard pip, face image & face pip) used by the 
 * application and provides access via getters.
 */
package phillockett65.CardCreate2.sample;

import java.text.DecimalFormat;

/**
 *
 * @author Phil
 */
public enum Item {
    INDEX (Default.INDEX_HEIGHT.getFloat(), Default.INDEX_CENTRE_X.getFloat(), Default.INDEX_CENTRE_Y.getFloat(), true, "Index"),
    CORNER_PIP (Default.CORNER_PIPHEIGHT.getFloat(), Default.CORNER_PIPCENTRE_X.getFloat(), Default.CORNER_PIPCENTRE_Y.getFloat(), true, "Corner Pip"),
    STANDARD_PIP (Default.STANDARD_PIPHEIGHT.getFloat(), Default.STANDARD_PIPCENTRE_X.getFloat(), Default.STANDARD_PIPCENTRE_Y.getFloat(), true, "Standard Pip"),
    FACE (Default.FACE_HEIGHT.getFloat(), Default.FACE_BOARDER_X.getFloat(), Default.FACE_BOARDER_Y.getFloat(), false, "Face"),
    FACE_PIP (Default.FACE_PIPHEIGHT.getFloat(), Default.FACE_PIPCENTRE_X.getFloat(), Default.FACE_PIPCENTRE_Y.getFloat(), true, "Face Pip");

    private final float height;
    private final float centreX;
    private final float centreY;
    private final boolean centre;
    private final String desc;

    Item(float h, float x, float y, boolean c, String d) {
        height = h;
        centreX = x;
        centreY = y;
        centre = c;
        desc = d;
    }

    public int index() {
        if (this == INDEX)
            return 0;
        if (this == CORNER_PIP)
            return 1;
        if (this == STANDARD_PIP)
            return 2;
        if (this == FACE)
            return 3;

        return 4;
    }

    private final DecimalFormat df = new DecimalFormat("#.#");

    /**
     * @return the default height for the item as a percentage of the card height.
     */
    public float getH() { return height; }

    /**
     * @return the default X coordinate of the centre of the item as a percentage of the card width.
     */
    public float getX() { return centreX; }

    /**
     * @return the default Y coordinate of the centre of the item as a percentage of the card height.
     */
    public float getY() { return centreY; }

    /**
     * @return the description of the item.
     */
    public String getD() { return desc; }

    /**
     * @return the reset button tool tip for the height of the item.
     */
    public String getHButtonTip() {
        if (centre)
            return "Reset the " + desc + " Height to " + df.format(height) + " % of card height";

        return "Not applicable";
    }

    /**
     * @return the reset button tool tip for the X coordinate of the centre of the item.
     */
    public String getXButtonTip() {
        if (centre)
            return "Reset X coordinate of the centre of the " + desc + " to " + df.format(centreX) + " % of card width";

        return "Reset X Boarder of the " + desc + " to " + df.format(centreX) + " % of card width";
    }

    /**
     * @return the reset button tool tip for the Y coordinate of the centre of the item.
     */
    public String getYButtonTip() {
        if (centre)
            return "Reset Y coordinate of the centre of the " + desc + " to " + df.format(centreY) + " % of card height";

        return "Reset Y Boarder of the " + desc + " to " + df.format(centreY) + " % of card height";
    }

    /**
     * @return the tool tip for the height of the item.
     */
    public String getHToolTip() {
        if (centre)
            return "Height of the " + desc + " as a % of card height";

        return "Height of the " + desc + " as a % of card height";
    }

    /**
     * @return the tool tip for the X coordinate of the centre of the item.
     */
    public String getXToolTip() {
        if (centre)
            return "X coordinate of the centre of the " + desc + " as a % of card width";

        return "X Boarder of the " + desc + " as a % of card width";
    }

    /**
     * @return the tool tip for the Y coordinate of the centre of the item.
     */
    public String getYToolTip() {
        if (centre)
            return "Y coordinate of the centre of the " + desc + " as a % of card height";

        return "Y Boarder of the " + desc + " as a % of card height";
    }

    /**
     * @return the label for the height of the item.
     */
    public String getHLabel() {
        if (centre)
            return desc + " Height (%):";

        return "Not applicable:";
    }

    /**
     * @return the label for the X coordinate of the centre of the item.
     */
    public String getXLabel() {
        if (centre)
            return desc + " X Centre (%):";

        return "Image X Boarder (%):";
    }

    /**
     * @return the label for the Y coordinate of the centre of the item.
     */
    public String getYLabel() {
        if (centre)
            return desc + " Y Centre (%):";

        return "Image Y Boarder (%):";
    }

    /**
     * @return true if the card item is positioned by it's centre, false otherwise.
     */
    public boolean isCentre() {
        return centre;
    }
}
