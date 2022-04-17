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


    /************************************************************************
     * Support code for the ImagePayload class.
     */

    protected boolean keepAspectRatio = true;

    public ImagePayload(Model mainModel) {
        super(mainModel, Item.FACE);

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    protected void initImageViews() {
        setPath(Item.FACE);
        // System.out.println("initImageViews(" + path + ") :: image");

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages();
            paintImage();
        }
    }

    private void paintImage() {
        // System.out.println("paintImage() :: ImagePayload");

        if (!hasImage())
            return;

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
        setPath(Item.FACE);
        // System.out.println("syncImageFile() :: image");

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setImages();
            paintImage();

            return true;
        }

        return false;
    }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        // System.out.println("syncCardSize(" + item + ") :: image");

        cardWidthPX = model.getCalculatedWidth();
        cardHeightPX = model.getHeight();

        paintImage();
    }

    
    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        if (setSpriteCentreX(value))
            paintImage();
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        if (setSpriteCentreY(value))
            paintImage();
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        if (setSpriteSize(size))
            paintImage();
    }

    /**
     * Increase the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public void incSize() {
        if (incSpriteSize())
            paintImage();
    }

    /**
     * Decrease the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public void decSize() {
        if (decSpriteSize())
            paintImage();
    }

    /**
     * Flag whether the Payload image should maintain it's aspect ratio.
     * @param keepAspectRatio when displaying the image if true.
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;

        getImageView(0).setPreserveRatio(keepAspectRatio);
        getImageView(1).setPreserveRatio(keepAspectRatio);
        paintImage();
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        // System.out.println("setVisible(" + state + ") :: face");
        display = state;

        getImageView(0).setVisible(display);
        if (isLandscape())
            getImageView(1).setVisible(display);
        else
            getImageView(1).setVisible(false);
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Draw icons to a given graphics context using the user specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage) {
        if (iconImage == null)
            return false;

        // System.out.println("drawImage()");
        final double imageWidthPX = iconImage.getWidth();
        final double imageHeightPX = iconImage.getHeight();
        final boolean landscape = imageHeightPX < imageWidthPX;

        final double pixelsX = centreX.getPixels();
        final double pixelsY = centreY.getPixels();
        double winX = cardWidthPX - (2*pixelsX);
        double winY;

        double dX = 0;
        double dY = 0;

        if (landscape) {
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

            winY = cardHeightPX - (2*pixelsY);

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

        return true;
    }

    /**
     * Draw single portrait image to a given graphics context using hard coded 
     * specification.
     * 
     * @param gc graphics context to draw on.
     * @param image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image image) {
        if (image == null)
            return false;

        final double imageWidthPX = image.getWidth();
        final double imageHeightPX = image.getHeight();

        final double pixelsX = cardWidthPX * 0.07;
        final double pixelsY = cardHeightPX * 0.05;
        double winX = cardWidthPX - (2*pixelsX);
        double winY = cardHeightPX - (2*pixelsY);

        double dX = 0;
        double dY = 0;

        double scaleX = winX / imageWidthPX;
        double scaleY = winY / imageHeightPX;
        if (scaleX < scaleY) {
            dY = (winY - (imageHeightPX * scaleX)) / 2;
            winY = imageHeightPX * scaleX;
        } else {
            dX = (winX - (imageWidthPX * scaleY)) / 2;
            winX = imageWidthPX * scaleY;
        }

        gc.drawImage(image, pixelsX + dX, pixelsY + dY, winX, winY);

        return true;
    }

}