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
 * CardData is a class that is used to serialize the image settings. 
 */
package phillockett65.CardCreate2;


public class CardData {

    private int id;
    private int suit;
    private int card;

    public CardData(int id, int suit, int card) {
        this.id = id;
        this.suit = suit;
        this.card = card;
    }

    public int getId() { return id; }
    public int getSuit() { return suit; }
    public int getCard() { return card; }
    public boolean isJoker() { return card == 0; }

}
