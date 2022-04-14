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


    private final static int[][] flags = {
        { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
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
     * @param pattern of icons/sprite for this Payload.
     * @param group node to add the ImageViews to.
     * @return the ImageView array.
     */
    private void createImageViewArray(int pattern, Group group) {
        int icons = (pattern > 1) ? 17 : 2;
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
        if ((item == Item.FACE) && (!isLandscape()))
            return 1;

        return views.length;
    }

    /**
     * Indicates whether the indexed ImageView should be visible.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return true if the image should be visible, false otherwise.
     */
    private boolean isImageViewVisible(int imageIndex) {
        if (!display)
            return false;

        return isIconVisible(pattern, imageIndex);
    }

    private boolean isIconVisible(int pattern, int imageIndex) {
        return flags[pattern][imageIndex] == 1;
    }

    protected class Real {
        private final boolean height;
        private double percent = 0;
        
        public Real(boolean height) {
            this.height = height;
        }

        private double getScale() {
            return height ? cardHeightPX : cardWidthPX;
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

    private double getXOriginPX() {
            return (centreX.getPixels()) - (spriteWidth.getPixels()/2);
    }
    private double getYOriginPX() {
            return (centreY.getPixels()) - (spriteHeight.getPixels()/2);
    }


    // "image" refers to the image in the file, 
    // "sprite" refers to the image on screen.
    protected Model model;

    private int pattern = 0;
    private final Item item;
    protected String path;
    private Group group;
    private Image image = null;
    protected double imageWidthPX = 0;
    protected double imageHeightPX = 0;
    protected double cardWidthPX;
    protected double cardHeightPX;

    private boolean display = true;
    protected final Real centreX;
    protected final Real centreY;
    protected final Real spriteHeight;
    private final Real spriteWidth;


    public Payload(Model mainModel, Item it) {
        // System.out.println("Payload()");

        model = mainModel;

        group = model.getGroup();
        cardWidthPX = model.getCalculatedWidth();
        cardHeightPX = model.getHeight();

        item = it;
        syncPattern();

        centreX = new Real(false);
        centreY = new Real(true);
        spriteWidth = new Real(false);
        spriteHeight = new Real(true);

        // Set up default percentages.
        spriteHeight.setPercent(item.getH());
        centreX.setPercent(item.getX());
        centreY.setPercent(item.getY());

        // Set up the image views.
        createImageViewArray(pattern, group);

        // Set up image dependent values.
        syncImageFile();
        initImageViews();
    }


    /**
     * Initialize the Image Views based on item.
     */
    private void initImageViews() {
        // System.out.println("initImageViews() :: " + item);
        
        if (item == Item.FACE)
            return;

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            ImageView view = getImageView(i);
            view.setVisible(visible);

            paintIcon(view, getLocation(i));
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

        setImages(image);

        return true;
    }

    /**
     * Set up the new image file based on the current file path.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        path = model.getImagePath(item);
        // System.out.println("syncImageFile(" + path + ") :: " + item);

        if (path.equals(""))
            return false;

        if (loadNewImageFile()) {
            syncPattern();
            setPatterns();

            return true;
        }

        return false;
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
    private void setPatterns() {
        // System.out.println("setPatterns()");

        if (item == Item.FACE) {
            System.err.println("Payload.setPatterns() called for face image");

            return;
        }

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            ImageView view = getImageView(i);
            view.setVisible(visible);
            // System.out.println(i + " visible = " + visible + " display = " + display);

            if (visible)
                paintIcon(view, getLocation(i));
        }
    }

    /**
     * Change pattern of a standard pip item to match the current card or set
     *  it to 0 if this is not a standard pip card item.
     * 
     * @return true if pattern was changed, false otherwise.
     */
    private boolean syncPattern() {
        final boolean change = (item == Item.STANDARD_PIP);

        pattern = (change) ? model.getCard() : 0;

        // System.out.println("syncPattern(" + pattern + ")");

        return change;
    }

    /**
     * Synchronise to the current card size.
     */
    public void syncCardSize() {
        // System.out.println("syncCardSize(" + item + ") :: " + item);

        cardWidthPX = model.getCalculatedWidth();
        cardHeightPX = model.getHeight();

        setPatterns();
    }

    protected void resizePercentages() {
        if (image == null)
            return;

        spriteWidth.setPixels(spriteHeight.getPixels() * imageWidthPX / imageHeightPX);
    }
    
    public void resizeCard(double w, double h) {
        if (image == null)
            return;

//        System.out.printf("resizeCard() Width = %d,  Height = %d\n", w, h);

        cardWidthPX = w;
        cardHeightPX = h;
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

        // System.out.println("setSize(" + size + ") :: " + item);

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
     * Indicates if the associated image file has a landscape aspect ratio.
     * @return true if the image file is landscape.
     */
    public boolean isLandscape() {
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

        for (int i = 0; i < getImageCount(); ++i) 
            getImageView(i).setVisible(isImageViewVisible(i));
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
    private class Data {
        public final double iconWidthPX;
        public final double iconHeightPX;
        public final double width;
        public final double height;

        public final double pixelsX;
        public final double pixelsY;
        public final double winX;
        public final double winY;

        public final double originX;
        public final double originY;

        public Data(Image iconImage) {
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
