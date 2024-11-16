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
 * CardItemData is a class that is used to serialize the image settings. 
 */
package phillockett65.CardCreate2;

import java.io.Serializable;

public class CardItemData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Double height;
    private Double centreX;
    private Double centreY;

    public CardItemData(Integer id, Double height, Double centreX, Double centreY) {
        this.id = id;
        this.height = height;
        this.centreX = centreX;
        this.centreY = centreY;
    }

    public Integer getId() {
        return id;
    }

    public Double getHeight() {
        return height;
    }

    public Double getCentreX() {
        return centreX;
    }

    public Double getCentreY() {
        return centreY;
    }

}
