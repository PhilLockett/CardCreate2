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

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Handle extends ImageView {

	private Image image;

	private long xPos = 150;
    private long yPos = 50;
    private long xMouse = xPos;
    private long yMouse = yPos;
    private long xOff = 0;
    private long yOff = 0;
    private final long width = 16;
    private final long height = 16;
    private Payload payload;

    public Handle(Image handleImage) {
		image = handleImage;
		this.setImage(image);
//		width = Math.round((float)image.getWidth());
//		height = Math.round((float)image.getHeight());
		this.resize(width, height);
//		this.relocate(20, 10);
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
        xPos = payload.getCentreX() - width/2;
        yPos = payload.getCentreY() - height/2;
    }

    /**
     * Synchronize handle position with current payload.
     */
    public void syncToPayload() {
        if (payload != null) {
            xPos = payload.getCentreX() - width/2;
            yPos = payload.getCentreY() - height/2;
        }
    }

    /**
     * Position the handle.
     * 
     * @param x coordinate of the centre of the current Item in pixels.
     * @param y coordinate of the centre of the current Item in pixels.
     */
    public void set(long x, long y) {
        xPos = x - (width/2);
        yPos = y - (height/2);
		this.relocate(xPos, yPos);
    }

    public long getXPos() {
        if (payload != null) {
            return payload.getX();
        }

        return xPos;
    }

    public long getYPos() {
        if (payload != null) {
            return payload.getY();
        }

        return yPos;
    }

    public long getWidth() {
        if (payload != null) {
            return Math.round(payload.getWidthPX());
        }

        return width;
    }

    public long getHeight() {
        if (payload != null) {
            return Math.round(payload.getHeightPX());
        }

        return height;
    }

    public long getXMouse() {
        return xMouse;
    }

    public long getYMouse() {
        return yMouse;
    }

    public void setMouse(int x, int y) {
        if (!isOver(x, y)) {
            return;
        }

        xMouse = x;
        yMouse = y;

        xOff = x - xPos;
        yOff = y - yPos;
    }

    public void setRel(int x, int y) {
        if (payload != null) {
            final long dx = x - xMouse;
            final long dy = y - yMouse;
            payload.moveBy(dx, dy);
        }

        xMouse = x;
        yMouse = y;

        xPos = x - xOff;
        yPos = y - yOff;
    }


    public boolean isOver(int x, int y){
        final int OFFSET = 2;
        x += OFFSET;
        y += OFFSET;

        if (x < this.xPos)
            return false;
        if (x > this.xPos + this.width)
            return false;

        if (y < this.yPos)
            return false;
        if (y > this.yPos + this.height)
            return false;

        return true;
    }

    public void paint(Group group) {
        if (payload != null) {
//            payload.paintPatterns(g2d);
        }

        final Color backCol = Color.DARKGREEN;
        final Color foreCol = Color.WHITE;
        final int lineWidth = 1;

        Rectangle box = new Rectangle(xPos, yPos, width, height);
        box.setFill(backCol);
        box.setStrokeWidth(lineWidth);
        box.setStroke(foreCol);

        Line line1 = new Line(xPos, yPos, xPos+width, yPos+height);
        line1.setStrokeWidth(lineWidth);
        line1.setStroke(foreCol);

        Line line2 = new Line(xPos+width, yPos, xPos, yPos+height);
        line2.setStrokeWidth(lineWidth);
        line2.setStroke(foreCol);

        group.getChildren().add(box);
        group.getChildren().add(line1);
        group.getChildren().add(line2);
    }
}
