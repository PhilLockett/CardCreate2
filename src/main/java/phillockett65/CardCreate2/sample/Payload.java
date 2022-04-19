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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import phillockett65.CardCreate2.Model;

public class Payload {


    /************************************************************************
     * Support code for ImageView array management.
     */

    private ImageView[] views;

    /**
     * Create the ImageView array to hold the image for this Payload.
     * 
     * @param group node to add the ImageViews to.
     */
    private void createImageViewArray(Group group) {

        views = new ImageView[2];

        views[0] = new ImageView();
        views[0].setPreserveRatio(true);

        views[1] = new ImageView();
        views[1].setPreserveRatio(true);
        views[1].setRotate(180);

        group.getChildren().add(views[0]);
        group.getChildren().add(views[1]);
    }

    /**
     * Create the ImageView array to hold the image for this Payload.
     */
    protected void createImageViews() {
        createImageViewArray(model.getGroup());
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
            return height ? model.getHeight() : model.getCalculatedWidth();
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

    protected double getXOriginPX() {
        return (centreX.getPixels()) - (spriteWidth.getPixels()/2);
    }
    protected double getYOriginPX() {
        return (centreY.getPixels()) - (spriteHeight.getPixels()/2);
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
    protected double imageWidthPX = 0;
    protected double imageHeightPX = 0;

    protected boolean display = true;
    protected final Real centreX;
    protected final Real centreY;
    protected final Real spriteHeight;
    protected final Real spriteWidth;


    public Payload(Model mainModel, Item it) {
        // System.out.println("Payload()");

        model = mainModel;

        item = it;

        centreX = new Real(false);
        centreY = new Real(true);
        spriteWidth = new Real(false);
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
        // System.out.println("initImageViews(" + path + ") :: " + item);

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
     * Load an image file from disc.
     * 
     * @param path to the image file.
     * @return the Image, or null if the file is not found.
     */
    private Image loadImage(String path) {
    // System.out.println("loadImage(" + path + ")");
        File file = new File(path);

        if (!file.exists()) {
            // System.out.println("File does not exist!");

             return null;
        }

        Image loadedImage = null;
        try {
            loadedImage = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return loadedImage;
    }

    /**
     * Load an image into this payload and set up the attributes for the image 
     * width and height.
     * 
     * @return true if the image file was found, false otherwise.
     */
    protected boolean loadNewImageFile() {
        // System.out.println("loadNewImageFile(" + path + ")");

        image = loadImage(path);

        if (image == null)
            return false;

        imageWidthPX = image.getWidth();
        imageHeightPX = image.getHeight();
        spriteWidth.setPixels(spriteHeight.getPixels() * imageWidthPX / imageHeightPX);
        // System.out.println("image size(" + imageWidthPX + ", " + imageHeightPX+ ")  scale = " + spriteScale);

        return true;
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
        // System.out.println("syncImageFile(" + path + ") :: " + item);

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            setImages();
            setPatterns();

            return true;
        }

        return false;
    }

    private void paintIcons() {
        // System.out.println("paintIcon()");

        final double cardWidthPX = model.getCalculatedWidth();
        final double cardHeightPX = model.getHeight();

        ImageView view = getImageView(0);
        double pX = getXOriginPX();
        double pY = getYOriginPX();

        view.relocate(pX, pY);
        view.setFitWidth(spriteWidth.getPixels());
        view.setFitHeight(spriteHeight.getPixels());

        view = getImageView(1);
        pX += cardWidthPX - (2*centreX.getPixels());
        pY += cardHeightPX - (2*centreY.getPixels());

        view.relocate(pX, pY);
        view.setFitWidth(spriteWidth.getPixels());
        view.setFitHeight(spriteHeight.getPixels());
    }

    /**
     * Paint the icons associated with this payload.
     */
    protected void setPatterns() {
        // System.out.println("setPatterns() :: " + item);

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
        // System.out.println("syncCardSize(" + item + ") :: " + item);

        setPatterns();
    }

    protected void resizePercentages() {
        if (image == null)
            return;

        spriteWidth.setPixels(spriteHeight.getPixels() * imageWidthPX / imageHeightPX);
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
        if (setSpriteCentreY(value))
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

        // System.out.println("setSize(" + size + ") :: " + item);

        spriteHeight.setPercent(size);
        resizePercentages();

        return false;
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        // System.out.println("setSize(" + size + ") :: " + item);
        if (setSpriteSize(size))
            setPatterns();
    }

    /**
     * Increase the size of the sprite.
     * @return true if size is increased, false otherwise.
     */
    protected boolean incSpriteSize() {
        double size = spriteHeight.getPercent();

        if (size == 100D)
            return false;

        size += 0.5D;
        if (size > 100D)
            size = 100D;

        setSpriteSize(size);

        return true;
    }

    /**
     * Increase the size of the sprite.
     */
    public void incSize() {
        if (incSpriteSize())
            setPatterns();
    }

    /**
     * Decrease the size of the sprite.
     * @return true if size is decreased, false otherwise.
     */
    protected boolean decSpriteSize() {
        double size = spriteHeight.getPercent();

        if (size == 0D)
        return false;

        size -= 0.5D;
        if (size < 0D)
            size = 0D;

        setSpriteSize(size);
        
        return true;
    }

    /**
     * Decrease the size of the sprite.
     */
    public void decSize() {
        if (decSpriteSize())
            setPatterns();
    }

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
        // System.out.println("isLandscape(" + (imageHeightPX < imageWidthPX) + ") :: " + item);
        if (image != null) {
            return imageHeightPX < imageWidthPX;
        }

        return false;
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        // System.out.println("setVisible(" + state + ") :: " + item);
        display = state;

        getImageView(0).setVisible(display);
        getImageView(1).setVisible(display);
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
            cardWidthPX = model.getCalculatedWidth();
            cardHeightPX = model.getHeight();

            iconWidthPX = iconImage.getWidth();
            iconHeightPX = iconImage.getHeight();
            height = spriteHeight.getPixels();
            width = height * iconWidthPX / iconHeightPX;
    
            pixelsX = centreX.getPixels();
            pixelsY = centreY.getPixels();
            winX = cardWidthPX - (2*pixelsX);
            winY = cardHeightPX - (2*pixelsY);
    
            originX = pixelsX - (width/2);
            originY = pixelsY - (height/2);
        }
     }


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

        return true;
    }

    /**
     * Draw single portrait image to a given graphics context using hard coded 
     * specification.
     * 
     * @param gc graphics context to draw on.
     * @param iconImage used for the icons.
     * @param rotatedImage rotated version of the image used for the icons.
     * @return true if the icons are drawn, false otherwise.
     */
    public boolean drawJoker(GraphicsContext gc, Image iconImage, Image rotatedImage) {
        if (image == null)
            return false;

        final double cardWidthPX = model.getCalculatedWidth();
        final double cardHeightPX = model.getHeight();
    
        final double iconWidthPX = iconImage.getWidth();
        final double iconHeightPX = iconImage.getHeight();
        final double radius = model.getRadiusPX();

        final double width = cardWidthPX * 0.07;
        final double height = width * iconHeightPX / iconWidthPX;
        
        double posX = cardWidthPX * 0.02;
        double posY = radius/4;
        gc.drawImage(iconImage, posX, posY, width, height);

        posX = cardWidthPX - posX - width;
        posY = cardHeightPX - posY - height;
        gc.drawImage(rotatedImage, posX, posY, width, height);

        return true;
    }

}
