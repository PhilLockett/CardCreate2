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
 * Default is an enumeration that captures the static values used by the  
 * application and provides access via integer and real getters.
 */
package phillockett65.CardCreate2.sample;

/**
 *
 * @author Phil
 */
public enum Default {
    POKER_ASPECT ((5F / 7F)),
    BRIDGE_ASPECT ((9F / 14F)),

    STEP_SIZE (0.05F),
    STEP_COUNT (10F),

    GENERATE_STEPS (56*2F + 1),
    CARD_ITEM_COUNT (5F),

    INDEX_HEIGHT (10.5F),
    INDEX_CENTRE_X (8.07F),
    INDEX_CENTRE_Y (9.84F),
    CORNER_PIPHEIGHT (7.5F),
    CORNER_PIPCENTRE_X (8.07F),
    CORNER_PIPCENTRE_Y (20.41F),
    STANDARD_PIPHEIGHT (18.0F),
    STANDARD_PIPCENTRE_X (25.7F),
    STANDARD_PIPCENTRE_Y (18.65F),
    FACE_HEIGHT (79.72F),
    FACE_BOARDER_X (14.54F),
    FACE_BOARDER_Y (10.14F),
    FACE_PIPHEIGHT (14.29F),
    FACE_PIPCENTRE_X (14.54F+12.63F),
    FACE_PIPCENTRE_Y (10.14F+9.77F),

    BORDER_WIDTH (3F),
    WIDTH (380F),
    MIN_WIDTH (38F),
    MAX_WIDTH (3800F),
    HEIGHT (380F * 7 / 5),
    MIN_HEIGHT (38F * 7 / 5),
    MAX_HEIGHT (3800F * 7 / 5),
    RADIUS (10F),
    MIN_RADIUS (0F),
    MAX_RADIUS (100F);

    private final int	iValue;
    private final float	rValue;

    Default(float val) {
        rValue = val;
        iValue = Math.round(val);
    }
    
    public int getInt() { return iValue; }
    public float getFloat() { return rValue; }

}
