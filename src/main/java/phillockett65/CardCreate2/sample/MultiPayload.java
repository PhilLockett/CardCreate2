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
import phillockett65.Debug.Debug;


public class MultiPayload extends Payload {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;


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
     */
    private void createImageViewArray() {
        int icons = 17;
        views = new ImageView[icons];

        for (int i = 0; i < views.length; ++i) {
            views[i] = new ImageView();

            views[i].setPreserveRatio(true);
            if (getLocation(i).getRotate())
                views[i].setRotate(180);
        }
    }

    public void removeFromGroup() {
        final Group group = model.getGroup();

        for (int i = 0; i < views.length; ++i)
            group.getChildren().remove(views[i]);
    }

    public void addToGroup() {
        final Group group = model.getGroup();

        for (int i = 0; i < views.length; ++i)
            group.getChildren().add(views[i]);
    }

    /**
     * Create the ImageView array to hold the image for this Payload and add 
     * the ImageViews to the Group.
     */
    protected void createImageViews() {
        createImageViewArray();
        addToGroup();
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

    public MultiPayload() {
        super(Item.STANDARD_PIP);

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initMultiImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    private void initMultiImageViews() {
        setPath(Item.STANDARD_PIP);
        Debug.trace(DD, "initMultiImageViews(" + path + ") :: number");

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages(getImage());
            pattern = model.getCard();

            final Data data = new Data(getImage());

            final double pX = data.originX;
            final double pY = data.originY;

            for (int i = 0; i < getImageCount(); ++i) {
                ImageView view = getImageView(i);
                view.setVisible(isImageViewVisible(i));

                Loc location = getLocation(i);
                final double offX = location.getXOffset() * data.winX;
                final double offY = location.getYOffset() * data.winY;

                view.relocate(pX + offX, pY + offY);
                view.setFitWidth(data.width);
                view.setFitHeight(data.height);
            }
        }
    }

    /**
     * Paint the icons associated with this payload.
     */
    private void setMultiPatterns() {
        Debug.trace(DD, "setMultiPatterns()");

        if (!hasImage())
            return;

        final Data data = new Data(getImage());

        final double pX = data.originX;
        final double pY = data.originY;

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            ImageView view = getImageView(i);
            view.setVisible(visible);

            if (visible) {
                Loc location = getLocation(i);
                final double offX = location.getXOffset() * data.winX;
                final double offY = location.getYOffset() * data.winY;

                view.relocate(pX + offX, pY + offY);
                view.setFitWidth(data.width);
                view.setFitHeight(data.height);
            }
        }
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        setPath(Item.STANDARD_PIP);
        Debug.trace(DD, "syncImageFile() :: number");

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
        Debug.trace(DD, "syncCardSize() :: number");

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
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     */
    public void setPos(double x, double y) {
        Debug.trace(DD, "setPos(" + x + ", " + y + ") :: ");
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (valid)
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
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     * @param size as a percentage of the card height.
     */
    public void update(double x, double y, double size) {
        Debug.trace(DD, "update(" + x + ", " + y + ", " + size + ") :: number");
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (!setSpriteSize(size))
            valid = false;

        if (valid)
            setMultiPatterns();
    }

    /**
     * Increase the size of the sprite.
     */
    public void incSize() {
        if (incSpriteSize(Default.STEP_COUNT.getInt()))
            setMultiPatterns();
    }

    /**
     * Decrease the size of the sprite.
     */
    public void decSize() {
        if (decSpriteSize(Default.STEP_COUNT.getInt()))
            setMultiPatterns();
    }

    /**
     * Resize of the sprite.
     * @param steps number of Default.STEP_SIZE steps to resize by.
     */
    public void resize(int steps) {
        if (steps > 0) {
            if (incSpriteSize(steps))
                setMultiPatterns();
        } else {
            if (decSpriteSize(-steps))
                setMultiPatterns();
        }
    }

    /**
     * Move the sprite up.
     */
    public void moveUp(int steps) {
        if (moveSpriteUp(steps))
            setMultiPatterns();
    }
    public void moveUp() { moveUp(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite down.
     */
    public void moveDown(int steps) {
        if (moveSpriteDown(steps))
            setMultiPatterns();
    }
    public void moveDown() { moveDown(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite left.
     */
    public void moveLeft(int steps) {
        if (moveSpriteLeft(steps))
            setMultiPatterns();
    }
    public void moveLeft() { moveLeft(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite right.
     */
    public void moveRight(int steps) {
        if (moveSpriteRight(steps))
            setMultiPatterns();
    }
    public void moveRight() { moveRight(Default.STEP_COUNT.getInt()); }


    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        Debug.trace(DD, "setVisible(" + state + ") :: ");
        display = state;

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            getImageView(i).setVisible(visible);

            if (visible)
                setMultiPatterns();
        }
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
                final Loc loc = getLocation(i);

                final double posX = data.originX + (loc.getXOffset() * data.winX);
                final double posY = data.originY + (loc.getYOffset() * data.winY);
                
                if (loc.getRotate())
                    gc.drawImage(rotatedImage, posX, posY, data.width, data.height);
                else
                    gc.drawImage(iconImage, posX, posY, data.width, data.height);
            }
        }

        return true;
    }

}
