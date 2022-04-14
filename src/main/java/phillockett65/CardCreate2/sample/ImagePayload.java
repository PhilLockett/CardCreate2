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
 * ImagePayload extends Payload specifically for image cards. 
 */
package phillockett65.CardCreate2.sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import phillockett65.CardCreate2.Model;

public class ImagePayload extends Payload {

    protected boolean keepAspectRatio = true;

    public ImagePayload(Model mainModel) {
		super(mainModel, Item.FACE);

		paintImage();
	}

    private void paintImage() {
        // System.out.println("paintImage() :: ImagePayload");

        final double pX = centreX.getPixels();
        final double pY = centreY.getPixels();
        final double winX = cardWidthPX - (2*pX);

        double dX = 0;
        double dY = 0;

        if (isLandscape()) {
            final double winY = (cardHeightPX / 2) - pY;

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX));
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                }
            }

            ImageView view = getImageView(0);
            view.relocate(pX + dX, pY + dY);
            view.setFitWidth(winX);
            view.setFitHeight(winY);
    
            view = getImageView(1);
            view.relocate(pX + dX, cardHeightPX/2);
            view.setFitWidth(winX);
            view.setFitHeight(winY);
        } else {
            final double winY = cardHeightPX - (2*pY);

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX)) / 2;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                }
            } 

            ImageView view = getImageView(0);
            view.relocate(pX + dX, pY + dY);
            view.setFitWidth(winX);
            view.setFitHeight(winY);

            view = getImageView(1);
            view.setVisible(false);
        }

    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        path = model.getImagePath(Item.FACE);
        // System.out.println("syncImageFile(" + path + ") :: " + item);

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setPatterns();

            return true;
        }

        return false;
    }

    /**
     * Paint the icons associated with this payload.
     */
    private void setPatterns() {
        // System.out.println("setPatterns()");

        if (hasImage())
            paintImage();
    }

    
    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        // System.out.println("syncCardSize(" + item + ") :: image");

        cardWidthPX = model.getCalculatedWidth();
        cardHeightPX = model.getHeight();

        setPatterns();
    }

    
    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        if ((value < 0D) || (value > 100D))
            return;

        centreX.setPercent(value);
        setPatterns();
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        if ((value < 0D) || (value > 100D))
            return;

        centreY.setPercent(value);
        setPatterns();
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        if ((size < 0D) || (size > 100D))
            return;

        // System.out.println("setSize(" + size + ") :: image");

        spriteHeight.setPercent(size);
        resizePercentages();
        setPatterns();
    }

    /**
     * Increase the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public double incSize() {
        double size = spriteHeight.getPercent();
        if (size != 100D) {
            size += 0.5D;
            if (size > 100D)
                size = 100D;
            spriteHeight.setPercent(size);
            resizePercentages();
            setPatterns();
        }
        
        return size;
    }

    /**
     * Decrease the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public double decSize() {
        double size = spriteHeight.getPercent();
        if (size != 0D) {
            size -= 0.5D;
            if (size < 0D)
                size = 0D;
            spriteHeight.setPercent(size);
            resizePercentages();
            setPatterns();
        }
        
        return size;
    }

    /**
     * Flag whether the Payload image should maintain it's aspect ratio.
     * @param keepAspectRatio when displaying the image if true.
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;

        setPatterns();
        getImageView(0).setPreserveRatio(keepAspectRatio);
        getImageView(1).setPreserveRatio(keepAspectRatio);
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

     /**
      * Draw the image card image -- work in progress.
      * @param gc
      * @param image
      */
      private void drawImage(GraphicsContext gc, Image iconImage, Image rotatedImage) {
        // System.out.println("drawImage()");
        final double imageWidthPX = iconImage.getWidth();
        final double imageHeightPX = iconImage.getHeight();

        final double pixelsX = centreX.getPixels();
        final double pixelsY = centreY.getPixels();
        double winX = cardWidthPX - (2*pixelsX);
        double winY = cardHeightPX - (2*pixelsY);

        double dX = 0;
        double dY = 0;

        if (imageHeightPX < imageWidthPX) {
            // System.out.println("landscape");

            winY = (cardHeightPX / 2) - pixelsY;

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX));
                    winY = imageHeightPX * scaleX;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                    winX = imageWidthPX * scaleY;
                }
            }

            gc.drawImage(rotatedImage, pixelsX + dX, cardHeightPX/2, winX, winY);
            gc.drawImage(iconImage, pixelsX + dX, pixelsY + dY, winX, winY);
        } else {
            // System.out.println("portrait");

            if (keepAspectRatio) {
                double scaleX = winX / imageWidthPX;
                double scaleY = winY / imageHeightPX;
                if (scaleX < scaleY) {
                    dY = (winY - (imageHeightPX * scaleX)) / 2;
                    winY = imageHeightPX * scaleX;
                } else {
                    dX = (winX - (imageWidthPX * scaleY)) / 2;
                    winX = imageWidthPX * scaleY;
                }
            }

            gc.drawImage(iconImage, pixelsX + dX, pixelsY + dY, winX, winY);
        }
    }


    /**
     * Draw icons to a given graphics context using the user specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @param pattern indicating the arrangement of icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage, int pattern) {
        if (iconImage == null)
            return false;

        drawImage(gc, iconImage, rotatedImage);

        return true;
    }

}
