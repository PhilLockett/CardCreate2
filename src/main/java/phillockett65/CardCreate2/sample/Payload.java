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
 * Payload is a class that is responsible for maintaining the image file and 
 * the icon size and position.
 */
package phillockett65.CardCreate2.sample;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import phillockett65.CardCreate2.CardItemData;
import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.Utils;
import phillockett65.Debug.Debug;


public class Payload {

    // Debug delta used to adjust the local logging level.
    private static final int DD = 0;


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

        views = new ImageView[2];

        views[0] = new ImageView();
        views[0].setPreserveRatio(true);

        views[1] = new ImageView();
        views[1].setPreserveRatio(true);
        views[1].setRotate(180);
    }

    public void removeFromGroup() {
        final Group group = model.getGroup();

        group.getChildren().remove(views[0]);
        group.getChildren().remove(views[1]);
    }

    public void addToGroup() {
        final Group group = model.getGroup();

        group.getChildren().add(views[0]);
        group.getChildren().add(views[1]);
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
     * Set the image in all ImageViews.
     * 
     * @param image to be set in each ImageView in views[].
     */
    private void setImages(Image image) {
        views[0].setImage(image);
        views[1].setImage(image);
    }

    /**
     * Set the image in all ImageViews.
     */
    protected void setImages() {
        setImages(image);
    }

    protected class Real {
        private final boolean height;
        private double percent = 0;
        
        public Real(boolean height) {
            this.height = height;
        }

        private double getScale() {
            return height ? model.getHeight() : model.getWidth();
        }

        public void setPercent(double value) {
            percent = value;
        }

        public void setPixels(double value) {
            percent = value * 100 / getScale();
        }
        
        public double getPercent() {
            return percent;
        }

        public double getPixels() {
            return percent * getScale() / 100;
        }

        public long getIntPixels() {
            return Math.round(getPixels());
        }

    }

    public CardItemData getData() {

        CardItemData data = new CardItemData(item.index(), 
            spriteHeight.getPercent(), 
            centreX.getPercent(), 
            centreY.getPercent());

        return data;
    }

    public boolean setData(CardItemData data) {
        if (data.getId() != item.index())
            return false;

        setSize(data.getHeight());
        setX(data.getCentreX());
        setY(data.getCentreY());

        return true;
    }



    /************************************************************************
     * Support code for the Payload class.
     */

    // "image" refers to the image in the file, 
    // "sprite" refers to the image on screen (AKA icon).
    protected Model model;

    private final Item item;
    protected String path;
    private Image image = null;

    protected boolean display = true;
    protected final Real centreX;
    protected final Real centreY;
    protected final Real spriteHeight;


    public Payload(Item it) {
        Debug.trace(DD, "Payload()");

        model = Model.getInstance();

        item = it;

        centreX = new Real(false);
        centreY = new Real(true);
        spriteHeight = new Real(true);

        // Set up default percentages.
        spriteHeight.setPercent(item.getH());
        centreX.setPercent(item.getX());
        centreY.setPercent(item.getY());
    }

    /**
     * Initialize the Image Views based on item.
     */
    protected void initImageViews() {
        setPath(item);
        Debug.trace(DD, "initImageViews(" + path + ") :: " + item);

        if (path.equals(""))
            return;

        if (loadNewImageFile()) {
            setImages();

            final boolean visible = isVisible();

            getImageView(0).setVisible(visible);
            getImageView(1).setVisible(visible);

            paintIcons();
        }
    }


    /**
     * Load an image into this payload and set up the attributes for the image 
     * width and height.
     * 
     * @return true if the image file was found, false otherwise.
     */
    protected boolean loadNewImageFile() {
        Debug.trace(DD, "loadNewImageFile(" + path + ")");

        image = Utils.loadImage(path);

        return image != null;
    }

    protected void setPath(Item item) {
        path = model.getImagePath(item);
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        setPath(item);
        Debug.trace(DD, "syncImageFile(" + path + ") :: " + item);

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setImages();
            setPatterns();

            return true;
        }

        return false;
    }

    /**
     * Paint both icons associated with this payload.
     */
    private void paintIcons() {
        Debug.trace(DD, "paintIcon() :: " + item);

        if (!hasImage())
            return;

        final Data data = new Data(image);

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
    }

    /**
     * Paint the icons associated with this payload if visible.
     */
    protected void setPatterns() {
        Debug.trace(DD, "setPatterns() :: " + item);

        final boolean visible = isVisible();

        getImageView(0).setVisible(visible);
        getImageView(1).setVisible(visible);

        if (visible)
            paintIcons();
    }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        Debug.trace(DD, "syncCardSize() :: " + item);

        setPatterns();
    }

    private boolean isValidPercentage(double value) {
        if ((value < 0D) || (value > 100D))
            return false;

        return true;
    }

    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     * @return true if value is valid, false otherwise.
     */
    protected boolean setSpriteCentreX(double value) {
        if (!isValidPercentage(value))
            return false;

        centreX.setPercent(value);
        
        return true;
    }

    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        Debug.trace(DD, "setX(" + value + ") :: " + item);
        if (setSpriteCentreX(value))
            setPatterns();
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     * @return true if value is valid, false otherwise.
     */
    protected boolean setSpriteCentreY(double value) {
        if (!isValidPercentage(value))
            return false;

        centreY.setPercent(value);

        return true;
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        Debug.trace(DD, "setY(" + value + ") :: " + item);
        if (setSpriteCentreY(value))
            setPatterns();
    }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     */
    public void setPos(double x, double y) {
        Debug.trace(DD, "setPos(" + x + ", " + y + ") :: " + item);
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (valid)
            setPatterns();
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     * @return true if size is valid, false otherwise.
     */
    protected boolean setSpriteSize(double size) {
        if (!isValidPercentage(size))
            return false;

        Debug.trace(DD, "setSpriteSize(" + size + ") :: " + item);

        spriteHeight.setPercent(size);

        return true;
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        Debug.trace(DD, "setSize(" + size + ") :: " + item);
        if (setSpriteSize(size))
            setPatterns();
    }

    /**
     * Set the position of the centre of the sprite and the size.
     * @param x co-ordinate as a percentage of the card width.
     * @param y co-ordinate as a percentage of the card height.
     * @param size as a percentage of the card height.
     */
    public void update(double x, double y, double size) {
        Debug.trace(DD, "update(" + x + ", " + y + ", " + size + ") :: " + item);
        boolean valid = true;

        if (!setSpriteCentreX(x))
            valid = false;

        if (!setSpriteCentreY(y))
            valid = false;

        if (!setSpriteSize(size))
            valid = false;

        if (valid)
            setPatterns();
    }


    /**
     * Increase the size of the sprite.
     * @param steps number of Default.STEP_SIZE steps to increase by.
     * @return true if size is increased, false otherwise.
     */
    protected boolean incSpriteSize(int steps) {
        double size = spriteHeight.getPercent();

        if (size == 100D)
            return false;

        size += Default.STEP_SIZE.getFloat() * steps;
        if (size > 100D)
            size = 100D;

        setSpriteSize(size);

        return true;
    }

    /**
     * Increase the size of the sprite.
     */
    public void incSize() {
        if (incSpriteSize(Default.STEP_COUNT.getInt()))
            setPatterns();
    }

    /**
     * Decrease the size of the sprite.
     * @param steps number of Default.STEP_SIZE steps to decrease by.
     * @return true if size is decreased, false otherwise.
     */
    protected boolean decSpriteSize(int steps) {
        double size = spriteHeight.getPercent();

        if (size == 0D)
            return false;

        size -= Default.STEP_SIZE.getFloat() * steps;
        if (size <= 0D)
            size = Default.STEP_SIZE.getFloat();

        setSpriteSize(size);
        
        return true;
    }

    /**
     * Decrease the size of the sprite.
     */
    public void decSize() {
        if (decSpriteSize(Default.STEP_COUNT.getInt()))
            setPatterns();
    }

    /**
     * Resize of the sprite.
     * @param steps number of Default.STEP_SIZE steps to resize by.
     */
    public void resize(int steps) {
        if (steps > 0) {
            if (incSpriteSize(steps))
                setPatterns();
        } else {
            if (decSpriteSize(-steps))
                setPatterns();
        }
    }

    protected boolean moveSpriteUp(int steps) {
        double value = centreY.getPercent();

        if (value == 0D)
            return false;

        value -= Default.STEP_SIZE.getFloat() * steps;
        if (value < 0D)
            value = 0D;

        setY(value);
        
        return true;
    }

    /**
     * Move the sprite up.
     */
    public void moveUp(int steps) {
        if (moveSpriteUp(steps))
            setPatterns();
    }
    public void moveUp() { moveUp(Default.STEP_COUNT.getInt()); }

    protected boolean moveSpriteDown(int steps) {
        double value = centreY.getPercent();

        if (value == 100D)
            return false;

        value += Default.STEP_SIZE.getFloat() * steps;
        if (value > 100D)
            value = 100D;

        setY(value);

        return true;
    }

    /**
     * Move the sprite down.
     */
    public void moveDown(int steps) {
        if (moveSpriteDown(steps))
            setPatterns();
    }
    public void moveDown() { moveDown(Default.STEP_COUNT.getInt()); }

    protected boolean moveSpriteLeft(int steps) {
        double value = centreX.getPercent();

        if (value == 0D)
            return false;

        value -= Default.STEP_SIZE.getFloat() * steps;
        if (value < 0D)
            value = 0D;

        setX(value);
        
        return true;
    }

    /**
     * Move the sprite left.
     */
    public void moveLeft(int steps) {
        if (moveSpriteLeft(steps))
            setPatterns();
    }
    public void moveLeft() { moveLeft(Default.STEP_COUNT.getInt()); }

    protected boolean moveSpriteRight(int steps) {
        double value = centreX.getPercent();

        if (value == 100D)
            return false;

        value += Default.STEP_SIZE.getFloat() * steps;
        if (value > 100D)
            value = 100D;

        setX(value);

        return true;
    }

    /**
     * Move the sprite right.
     */
    public void moveRight(int steps) {
        if (moveSpriteRight(steps))
            setPatterns();
    }
    public void moveRight() { moveRight(Default.STEP_COUNT.getInt()); }


    public double getCentreX() {
        return centreX.getPixels();
    }

    public double getCentreY() {
        return centreY.getPixels();
    }

    /**
     * @return the centre X co-ordinate of the sprite as a percentage of the card width.
     */
    public double getSpriteX() {
        return centreX.getPercent();
    }

    /**
     * @return the centre Y co-ordinate of the sprite as a percentage of the card height.
     */
    public double getSpriteY() {
        return centreY.getPercent();
    }

    /**
     * @return the height of the sprite as a percentage of the card height.
     */
    public double getSpriteH() {
        return spriteHeight.getPercent();
    }

    /**
     * @return the Item this Payload represents.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Indicates if the payload has an associated image file.
     * @return true if the Payload has an image file.
     */
    public boolean hasImage() {
        return (image != null);
    }

    /**
     * @return the Payload image.
     */
    protected Image getImage() {
        return image;
    }

    /**
     * Indicates if the associated image file has a landscape aspect ratio.
     * @return true if the image file is landscape.
     */
    public boolean isLandscape() {
        Debug.trace(DD, "isLandscape() :: " + item);
        if (image != null)
            return image.getHeight() < image.getWidth();

        return false;
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        Debug.trace(DD, "setVisible(" + state + ") :: " + item);
        display = state;

        getImageView(0).setVisible(display);
        getImageView(1).setVisible(display);

        if (display)
            paintIcons();
    }

    /**
     * Indicates whether the Payload image should be drawn.
     * @return true if the image should be drawn, false otherwise.
     */
    public boolean isVisible() {
        return display;
    }



    /************************************************************************
     * Support code for Playing Card Generation.
     */

    /**
     * Class to build the data needed to render the icons.
     */
    protected class Data {
        public final double cardWidthPX;
        public final double cardHeightPX;
        private final double iconWidthPX;
        private final double iconHeightPX;
        public final double width;
        public final double height;

        private final double pixelsX;
        private final double pixelsY;
        public final double winX;
        public final double winY;

        public final double originX;
        public final double originY;

        public Data(Image iconImage) {
            cardWidthPX = model.getWidth();
            cardHeightPX = model.getHeight();

            iconWidthPX = iconImage.getWidth();
            iconHeightPX = iconImage.getHeight();
            height = spriteHeight.getPixels();
            width = height * iconWidthPX / iconHeightPX;
    
            pixelsX = centreX.getPixels();
            pixelsY = centreY.getPixels();
            winX = cardWidthPX - (2*pixelsX);
            winY = cardHeightPX - (2*pixelsY);
    
            originX = pixelsX - (width/2) + model.getMpcBorderWidth();
            originY = pixelsY - (height/2) + model.getMpcBorderHeight();
        }
     }


    /**
     * Draw top left and rotated bottom right images to a given graphics 
     * context using hard coded specification.
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

        return true;
    }

    /**
     * Draw top left and rotated bottom right images to a given graphics 
     * context for the Joker indices.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image iconImage, Image rotatedImage) {
        if (image == null)
            return false;

        final double cardWidthPX = model.getWidth();
        final double cardHeightPX = model.getHeight();
        final double xOffset = model.getMpcBorderWidth();
        final double yOffset = model.getMpcBorderHeight();

        final double iconWidthPX = iconImage.getWidth();
        final double iconHeightPX = iconImage.getHeight();
        final boolean landscape = iconHeightPX < iconWidthPX;

        double width;
        double height;
        double posX;
        double posY;

        if (landscape) {
            height = cardHeightPX * 0.05;
            width = height * iconWidthPX / iconHeightPX;

            posX = cardHeightPX * 0.02;
            posY = model.getArcWidthPX()/4;
        } else {
            width = cardWidthPX * 0.07;
            height = width * iconHeightPX / iconWidthPX;

            posX = cardWidthPX * 0.02;
            posY = model.getArcHeightPX()/4;
        }
        gc.drawImage(iconImage, posX + xOffset, posY + yOffset, width, height);

        posX = cardWidthPX - posX - width;
        posY = cardHeightPX - posY - height;
        gc.drawImage(rotatedImage, posX + xOffset, posY + yOffset, width, height);

        return true;
    }

}
