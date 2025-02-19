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
import phillockett65.Debug.Debug;

public class Handle extends ImageView {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;

    private Image image;

    private final double size = 24;
    private final double width = size;
    private final double height = size;
    private Payload payload;

    public Handle(Image handleImage, Payload payload) {
        image = handleImage;
        this.setImage(image);

        this.setFitWidth(width);
        this.setFitHeight(height);
        setPayload(payload);
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
        Debug.trace(DD, "handle.setPayload(" + payload.getItem() + ");");
        this.payload = payload;

        syncPosition();
    }

    /**
     * Synchronise the Display State of the handle.
     */
    public void syncDisplayState(boolean display) {
        Debug.trace(DD, "handle.syncDisplayState(" + display + ")");
        setVisible(display);
    }

    /**
     * Synchronise the position of the handle with the payload.
     */
    public void syncPosition() {
        Debug.trace(DD, "handle.syncPosition()");
        double xPos = payload.getCentreX() - (width/2);
        double yPos = payload.getCentreY() - (height/2);
        this.setTranslateX(xPos);
        this.setTranslateY(yPos);
    }

}
