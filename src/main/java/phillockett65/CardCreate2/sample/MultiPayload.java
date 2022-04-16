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
 * MultiPayload extends Payload specifically for number cards. 
 */
package phillockett65.CardCreate2.sample;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import phillockett65.CardCreate2.Model;

public class MultiPayload extends Payload {


    /************************************************************************
     * Support code for ImageView array management.
     */

    private final static int[][] flags = {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 },
        { 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0 }
    };

    private final static Loc[] locationList = {
        Loc.L_0, Loc.L_1, Loc.L_2, Loc.L_3,
        Loc.L_4, Loc.L_5, Loc.L_6, Loc.L_7,
        Loc.L_8, Loc.L_9, Loc.L10, Loc.L11,
        Loc.L12, Loc.L13, Loc.L14, Loc.L15,
        Loc.L16 
    };

    private ImageView[] views;

    /**
     * Get the corresponding Loc for the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the corresponding Loc for the indicated ImageView.
     */
    private Loc getLocation(int imageIndex) {
        return locationList[imageIndex];
    }

    /**
     * Create the ImageView array to hold the image for this Payload.
     * 
     * @param group node to add the ImageViews to.
     * @return the ImageView array.
     */
    private void createImageViewArray(Group group) {
        int icons = 17;
        views = new ImageView[icons];

        for (int i = 0; i < views.length; ++i) {
            views[i] = new ImageView();

            views[i].setPreserveRatio(true);
            if (getLocation(i).getRotate())
                views[i].setRotate(180);
            
            group.getChildren().add(views[i]);
        }
    }

    /**
     * Get the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the indicated ImageView.
     */
    protected ImageView getImageView(int imageIndex) {
        return views[imageIndex];
    }

    /**
     * set the image in all ImageViews.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the indicated ImageView.
     */
    private void setImages(Image image) {
        for (int i = 0; i < views.length; ++i)
            getImageView(i).setImage(image);
    }

    /**
     * @return the active ImageView count.
     */
    private int getImageCount() {
        return views.length;
    }

    /**
     * Indicates whether the indexed ImageView should be visible.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return true if the image should be visible, false otherwise.
     */
    private boolean isImageViewVisible(int imageIndex) {
        if (!isVisible())
            return false;

        return isIconVisible(pattern, imageIndex);
    }

    private boolean isIconVisible(int pattern, int imageIndex) {
        return flags[pattern][imageIndex] == 1;
    }



    /************************************************************************
     * Support code for the MultiPayload class.
     */

    private int pattern = 0;

    public MultiPayload(Model mainModel) {
        super(mainModel, Item.STANDARD_PIP);

        // Set up the image views.
        createImageViewArray(model.getGroup());

        // Set up image dependent values.
        initMultiImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    private void initMultiImageViews() {
        setPath(Item.STANDARD_PIP);
        // System.out.println("initMultiImageViews(" + path + ") :: number");

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages(getImage());
            pattern = model.getCard();

            for (int i = 0; i < getImageCount(); ++i) {
                ImageView view = getImageView(i);
                view.setVisible(isImageViewVisible(i));
                
                paintIcon(view, getLocation(i));
            }
        }
    }

    private void paintIcon(ImageView view, Loc location) {
        // System.out.println("paintIcon()");

        final double winX = cardWidthPX - (2*centreX.getPixels());
        final double offX = location.getXOffset() * winX;
        double pX = getXOriginPX() + offX;

        final double winY = cardHeightPX - (2*centreY.getPixels());
        final double offY = location.getYOffset() * winY;
        double pY = getYOriginPX() + offY;

        view.relocate(pX, pY);

        view.setFitWidth(spriteWidth.getPixels());
        view.setFitHeight(spriteHeight.getPixels());
    }

    /**
     * Paint the icons associated with this payload.
     */
    private void setMultiPatterns() {
        // System.out.println("setMultiPatterns()");

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            ImageView view = getImageView(i);
            view.setVisible(visible);

            if (visible)
                paintIcon(view, getLocation(i));
        }
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        setPath(Item.STANDARD_PIP);
        // System.out.println("syncImageFile() :: number");

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setImages(getImage());
            pattern = model.getCard();
            setMultiPatterns();

            return true;
        }

        return false;
    }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        // System.out.println("syncCardSize(" + item + ") :: number");

        cardWidthPX = model.getCalculatedWidth();
        cardHeightPX = model.getHeight();

        setMultiPatterns();
    }

    
    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        if (setSpriteCentreX(value))
            setMultiPatterns();
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        if (setSpriteCentreY(value))
            setMultiPatterns();
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        if (setSpriteSize(size))
            setMultiPatterns();
    }

    /**
     * Increase the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public void incSize() {
        if (incSpriteSize())
            setMultiPatterns();
    }

    /**
     * Decrease the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public void decSize() {
        if (decSpriteSize())
            setMultiPatterns();
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        // System.out.println("setVisible(" + state + ") :: " + number);
        display = state;

        for (int i = 0; i < getImageCount(); ++i) 
            getImageView(i).setVisible(isImageViewVisible(i));
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
     * @param pattern indicating the arrangement of icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawCard(GraphicsContext gc, Image iconImage, Image rotatedImage, int pattern) {
        if (iconImage == null)
            return false;

        final Data data = new Data(iconImage);

        for (int i = 0; i < getImageCount(); ++i) {
            if (isIconVisible(pattern, i)) {
                final Loc location = getLocation(i);

                final double posX = data.originX + (location.getXOffset() * data.winX);
                final double posY = data.originY + (location.getYOffset() * data.winY);
                
                if (location.getRotate())
                    gc.drawImage(rotatedImage, posX, posY, data.width, data.height);
                else
                    gc.drawImage(iconImage, posX, posY, data.width, data.height);
            }
        }

        return true;
    }

}
