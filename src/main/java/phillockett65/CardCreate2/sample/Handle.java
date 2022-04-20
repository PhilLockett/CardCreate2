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
 * Handle is a class that is bound to a Payload and is used to manipulate the 
 * image it contains. Handle operates in pixels.
 */
package phillockett65.CardCreate2.sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Handle extends ImageView {

    private Image[] images;

    private final double width = 20;
    private final double height = 20;
    private Payload payload;

    public Handle(Image[] handleImages, Payload payload) {
        images = handleImages;
        this.setImage(images[0]);

        this.setFitWidth(width);
        this.setFitHeight(height);
        setPayload(payload);
    }

    /**
     * Display the indexed handle image.
     * @param index of image to display.
     */
    public void setHandleImage(int index) {
        this.setImage(images[index]);
    }

    /**
     * @return the attached payload.
     */
    public Payload getPayload() {
        return payload;
    }

    /**
     * Attach this handle to the given Payload and reposition the handle.
     * 
     * @param payload to control.
     */
    public void setPayload(Payload payload) {
        // System.out.println("handle.setPayload(" + payload.getItem() + ");");
        this.payload = payload;

        syncPosition();
    }

    /**
     * Synchronise the Display State of the handle.
     */
    public void syncDisplayState(boolean display) {
        // System.out.println("handle.syncDisplayState(" + display + ")");
        setVisible(display);
    }

    /**
     * Synchronise the position of the handle with the payload.
     */
    public void syncPosition() {
        // System.out.println("handle.syncPosition()");
        double xPos = payload.getCentreX() - (width/2);
        double yPos = payload.getCentreY() - (height/2);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
    }

}
