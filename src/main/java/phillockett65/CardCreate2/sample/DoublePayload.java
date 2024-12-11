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
 * DoublePayload extends Payload specifically for resolving a sequnecing issue 
 * on initialisation with MultiPayload.
 */
package phillockett65.CardCreate2.sample;


public class DoublePayload extends Payload {


    /************************************************************************
     * Support code for the DoublePayload class.
     */

    /**
     * Basic constructor.
     * @param item card item
     */
    public DoublePayload(Item item) {
        super(item);

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initImageViews();
    }

}
