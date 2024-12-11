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
 * QuadPayload extends Payload specifically to support left-handed cards by 
 * optionally displaying indices and corner pips in all 4 corners.
 */
package phillockett65.CardCreate2.sample;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class QuadPayload extends Payload {


    /************************************************************************
     * Support code for ImageView array management.
     */



    private ImageView[] views;

    /**
     * Create the ImageView array to hold the image for this Payload.
     * 
     * @param group node to add the ImageViews to.
     */
    private void createImageViewArray() {

        views = new ImageView[4];

        views[0] = new ImageView();
        views[0].setPreserveRatio(true);

        views[1] = new ImageView();
        views[1].setPreserveRatio(true);
        views[1].setRotate(180);

        views[2] = new ImageView();
        views[2].setPreserveRatio(true);

        views[3] = new ImageView();
        views[3].setPreserveRatio(true);
        views[3].setRotate(180);
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

        if (imageIndex > 1)
            return model.isLeftHanded();

        return true;
    }

    // private boolean isIconVisible(int pattern, int imageIndex) {
    //     return flags[pattern][imageIndex] == 1;
    // }


    /************************************************************************
     * Support code for the MultiPayload class.
     */

    private final Item item;

    public QuadPayload(Item it) {
        super(it);

        item = it;

        // Set up the image views.
        createImageViews();

        // Set up image dependent values.
        initQuadImageViews();
    }

    /**
     * Initialize the Image Views based on item.
     */
    private void initQuadImageViews() {
        setPath(item);
        // System.out.println("initMultiImageViews(" + path + ") :: " + item);

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages(getImage());

            for (int i = 0; i < getImageCount(); ++i)
                getImageView(i).setVisible(isImageViewVisible(i));

            paintIcons();
        }
    }

    /**
     * Paint both icons associated with this payload.
     */
    private void paintIcons() {
        // System.out.println("paintIcon() :: " + item);

        if (!hasImage())
            return;

        final Data data = new Data(getImage());

        ImageView view = getImageView(0);
        double posX = data.originX;
        double posY = data.originY;

        view.relocate(posX, posY);
        view.setFitWidth(data.width);
        view.setFitHeight(data.height);

        view = getImageView(1);
        posX += data.winX;
        posY += data.winY;

        view.relocate(posX, posY);
        view.setFitWidth(data.width);
        view.setFitHeight(data.height);

        if (!model.isLeftHanded())
            return;

        // Add left-handed indices and corner pips.
        view = getImageView(2);
        posY = data.originY;

        view.relocate(posX, posY);
        view.setFitWidth(data.width);
        view.setFitHeight(data.height);

        view = getImageView(3);
        posX = data.originX;
        posY += data.winY;

        view.relocate(posX, posY);
        view.setFitWidth(data.width);
        view.setFitHeight(data.height);
    }

    /**
     * Paint the icons associated with this payload.
     */
    private void setQuadPatterns() {
        // System.out.println("setMultiPatterns()");

        if (!hasImage())
            return;

        for (int i = 0; i < getImageCount(); ++i)
            getImageView(i).setVisible(isImageViewVisible(i));

        if (isVisible())
            paintIcons();
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        setPath(item);
        // System.out.println("syncImageFile() :: " + item);

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setImages(getImage());
            setQuadPatterns();

            return true;
        }

        return false;
    }

    /**
     * Synchronise to the current left-handed state.
     */
    public void syncQuadState() {
        // System.out.println("syncQuadState() :: " + item);

        setQuadPatterns();
    }

    
    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        // System.out.println("syncCardSize() :: " + item);

        setQuadPatterns();
    }

    
    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        if (setSpriteCentreX(value))
            setQuadPatterns();
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        if (setSpriteCentreY(value))
            setQuadPatterns();
    }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     */
    public void setPos(double x, double y) {
        // System.out.println("setPos(" + x + ", " + y + ") :: " + item);
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (valid)
            setQuadPatterns();
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        if (setSpriteSize(size))
            setQuadPatterns();
    }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     * @param size as a percentage of the card height.
     */
    public void update(double x, double y, double size) {
        // System.out.println("update(" + x + ", " + y + ", " + size + ") :: " + item);
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (!setSpriteSize(size))
            valid = false;

        if (valid)
            setQuadPatterns();
    }

    /**
     * Increase the size of the sprite.
     */
    public void incSize() {
        if (incSpriteSize(Default.STEP_COUNT.getInt()))
            setQuadPatterns();
    }

    /**
     * Decrease the size of the sprite.
     */
    public void decSize() {
        if (decSpriteSize(Default.STEP_COUNT.getInt()))
            setQuadPatterns();
    }

    /**
     * Resize of the sprite.
     * @param steps number of Default.STEP_SIZE steps to resize by.
     */
    public void resize(int steps) {
        if (steps > 0) {
            if (incSpriteSize(steps))
                setQuadPatterns();
        } else {
            if (decSpriteSize(-steps))
                setQuadPatterns();
        }
    }

    /**
     * Move the sprite up.
     */
    public void moveUp(int steps) {
        if (moveSpriteUp(steps))
            setQuadPatterns();
    }
    public void moveUp() { moveUp(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite down.
     */
    public void moveDown(int steps) {
        if (moveSpriteDown(steps))
            setQuadPatterns();
    }
    public void moveDown() { moveDown(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite left.
     */
    public void moveLeft(int steps) {
        if (moveSpriteLeft(steps))
            setQuadPatterns();
    }
    public void moveLeft() { moveLeft(Default.STEP_COUNT.getInt()); }

    /**
     * Move the sprite right.
     */
    public void moveRight(int steps) {
        if (moveSpriteRight(steps))
            setQuadPatterns();
    }
    public void moveRight() { moveRight(Default.STEP_COUNT.getInt()); }


    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        // System.out.println("setVisible(" + state + ") :: " + number);
        display = state;

        for (int i = 0; i < getImageCount(); ++i)
            getImageView(i).setVisible(isImageViewVisible(i));

        if (isVisible())
            paintIcons();
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

        final Data data = new Data(iconImage);

        double posX = data.originX;
        double posY = data.originY;
        gc.drawImage(iconImage, posX, posY, data.width, data.height);

        posX += data.winX;
        posY += data.winY;
        gc.drawImage(rotatedImage, posX, posY, data.width, data.height);

        if (!model.isLeftHanded())
            return true;

        // Add left-handed indices and corner pips.
        posY = data.originY;
        gc.drawImage(iconImage, posX, posY, data.width, data.height);

        posX = data.originX;
        posY += data.winY;
        gc.drawImage(rotatedImage, posX, posY, data.width, data.height);

        return true;
    }

}
