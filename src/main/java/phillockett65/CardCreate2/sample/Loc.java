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
 * Loc is an enumeration that captures the icon locations (as a percentage) and 
 * whether it should be up side down and provides access via getters.
 */
package phillockett65.CardCreate2.sample;

/**
 *
 * @author Phil
 */
public enum Loc {
    L_0 (0, 0, false),
    L_1 (0, 0,  true),
    L_2 (2, 2, false),
    L_3 (2, 0,  true),
    L_4 (2, 0, false),
    L_5 (1, 0,  true),
    L_6 (1, 0, false),
    L_7 (0, 2, false),
    L_8 (1, 2, false),
    L_9 (2, 3, false),
    L10 (2, 3,  true),
    L11 (0, 4,  true),
    L12 (1, 4,  true),
    L13 (0, 4, false),
    L14 (1, 4, false),
    L15 (2, 5,  true),
    L16 (2, 5, false);

    private final int       xIndex;
    private final int       yIndex;
    private final boolean   rotate;

    Loc(int ix, int iy, boolean rot) {
        xIndex = ix;
        yIndex = iy;
        rotate = rot;
    }

    public boolean getRotate() { return rotate; }

    private final double[] offsets = { 0D, 1D, 0.5D, 0.25D, 1D / 3, 1D / 6 };

    public double getXOffset() { return rotate ? 1-offsets[xIndex] : offsets[xIndex]; }
    public double getYOffset() { return rotate ? 1-offsets[yIndex] : offsets[yIndex]; }

};
