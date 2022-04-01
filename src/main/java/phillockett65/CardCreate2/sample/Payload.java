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
 * Default is an enumeration that captures the static values used by the  
 * application and provides access via integer and real getters.
 */
package phillockett65.CardCreate2.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import phillockett65.CardCreate2.Model;

public class Payload {

    private static enum Loc {
        L_0 (0, 0, false),
        L_1 (0, 0,  true),
        L_2 (2, 2, false),
        L_3 (2, 0,  true),
        L_4 (2, 0, false),
        L_5 (1, 0,  true),
        L_6 (1, 0, false),
        L_7 (0, 2, false),
        L_8 (1, 2, false),
        L_9 (2, 3, false),
        L10 (2, 3,  true),
        L11 (0, 4,  true),
        L12 (1, 4,  true),
        L13 (0, 4, false),
        L14 (1, 4, false),
        L15 (2, 5,  true),
        L16 (2, 5, false);

        private final int       xIndex;
        private final int       yIndex;
        private final boolean   rotate;

        Loc(int ix, int iy, boolean rot) {
            xIndex = ix;
            yIndex = iy;
            rotate = rot;
        }

        public boolean getRotate() { return rotate; }

        private final double[] offsets = { 0D, 1D, 0.5D, 0.25D, 1D / 3, 1D / 6 };

        public double getXOffset() { return rotate ? 1-offsets[xIndex] : offsets[xIndex]; }
        public double getYOffset() { return rotate ? 1-offsets[yIndex] : offsets[yIndex]; }

    };

    private static enum Destination { DISPLAY, DISC };
    private static Destination destination = Destination.DISPLAY;

    public static void setDestinationToDisplay() {
        destination = Destination.DISPLAY;
    }
    
    public static void setDestinationToDisc() {
        destination = Destination.DISC;
    }


    final static int[][] flags = {
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

    final static Loc[] locationList = {
        Loc.L_0, Loc.L_1, Loc.L_2, Loc.L_3,
        Loc.L_4, Loc.L_5, Loc.L_6, Loc.L_7,
        Loc.L_8, Loc.L_9, Loc.L10, Loc.L11,
        Loc.L12, Loc.L13, Loc.L14, Loc.L15,
        Loc.L16 
    };

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
     * Get the indexed ImageView.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return the indicated ImageView.
     */
    private ImageView getImageView(int imageIndex) {
        return views[imageIndex];
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
     * Indicates whether the indexed ImageView should be drawn.
     * 
     * @param imageIndex for the ImageView in views[].
     * @return true if the image should be drawn, false otherwise.
     */
    private boolean isImageViewVisible(int imageIndex) {
        if (!display)
            return false;

        return flags[getPattern()][imageIndex] == 1;
    }



    public class Real {
        private final boolean height;
        private final double scale;
        private double decimal = 0;
        private double percent = 0;
        private double pixels = 0;
        
        public Real(boolean height) {
            this.height = height;
            scale = height ? cardHeightPX : cardWidthPX;
        }

        private void calcPixels() {
            pixels = decimal * scale;
        }
        public void setPercent(double value) {
            percent = value;
            decimal = percent / 100;
            calcPixels();
        }

        public void setReal(double value) {
            decimal = value;
            percent = decimal * 100;
            calcPixels();
        }

        public void setPixels(double value) {
            pixels = value;
            decimal = pixels / scale;
            percent = decimal * 100;
        }
        
        public double getPercent() {
            return percent;
        }

        public double getReal() {
            return decimal;
        }
        
        public double getRealPixels() {
            return pixels;
        }

        public long getIntPixels() {
            return Math.round(pixels);
        }

        public double getRealOrigin() {
            if (height)
                return pixels - (spriteHeight.getRealPixels()/2);
            else
                return pixels - (spriteWidth.getRealPixels()/2);
        }

        public long getOrigin() {
            return Math.round(getRealOrigin());
        }

    }


    // "image" refers to the image in the file, 
    // "sprite" refers to the image on screen.
    private Model model;

    private int pattern = 0;
    private final Item item;
    private String path;
    private Group group;
    private Image image = null;
    private ImageView[] views;
    private double imageWidthPX = 0;
    private double imageHeightPX = 0;
    private double cardWidthPX;
    private double cardHeightPX;

    private boolean display = true;
    private boolean keepAspectRatio = true;
    private final Real centreX;
    private final Real centreY;
    private final Real spriteHeight;
    private final Real spriteWidth;
    private double spriteScale = 1;

    public Payload(Model mainModel, int p, Item it) {
        System.out.println("Payload()");

        model = mainModel;

        group = model.getGroup();
        cardWidthPX = model.getWidth();
        cardHeightPX = model.getHeight();

        pattern = p;
        item = it;

        centreX = new Real(false);
        centreY = new Real(true);
        spriteWidth = new Real(false);
        spriteHeight = new Real(true);

        // Set up default percentages.
        spriteHeight.setPercent(item.getH());
        centreX.setPercent(item.getX());
        centreY.setPercent(item.getY());

        // Set up the image views.
        int icons = (pattern > 1) ? 17 : 2;
        views = new ImageView[icons];
        for (int i = 0; i < views.length; ++i) {
            views[i] = new ImageView();

            views[i].setPreserveRatio(true);
            if (getLocation(i).getRotate())
                views[i].setRotate(180);
            
            group.getChildren().add(views[i]);
        }

        // Set up image dependent values.
        syncImageFile();
        initPatterns();
    }
    private void initPatterns() {
        System.out.println("initPatterns()");
        // if (!display) {
        //     System.out.println("initPatterns() ABORTING!");

        //     return;
        // }
        
        if (item == Item.FACE) {
            paintImage();
            return;
        }

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

    private boolean loadNewImageFile() {
        // System.out.println("loadNewImageFile(" + path + ")");

        image = loadImage(path);

        if (image != null) {
            imageWidthPX = image.getWidth();
            imageHeightPX = image.getHeight();
            spriteWidth.setPixels(spriteHeight.getRealPixels() * imageWidthPX / imageHeightPX);
            spriteScale = spriteHeight.getRealPixels() / imageHeightPX;
            // System.out.println("image size(" + imageWidthPX + ", " + imageHeightPX+ ")  scale = " + spriteScale);

            for (int i = 0; i < getImageCount(); ++i)
                getImageView(i).setImage(image);

            return true;
        }

        return false;
    }

    /**
     * @return the file path for the image of the Item for the current card.
     */
    private String getImagePath() {
        if (item == Item.FACE)
            return model.getFaceImagePath();

        if (item == Item.INDEX)
            return model.getIndexImagePath();

        if ((item == Item.STANDARD_PIP) || (item == Item.FACE_PIP))
            return model.getPipImagePath();

        if (item == Item.CORNER_PIP)
            return model.getCornerPipImagePath();

        return "";
    }

    /**
     * Set up the new image file.
     * 
     * @return true if the new file was loaded, false otherwise.
     */
    public boolean syncImageFile() {
        path = getImagePath();
        // System.out.println("syncImageFile(" + path + ")");

        if (path.equals(""))
            return false;

        return loadNewImageFile();
    }

    private void paintImage() {
        final boolean generate = (destination == Destination.DISC);
        System.out.println("paintImage(" + generate + ")");

        final long pX = centreX.getIntPixels();
        final long pY = centreY.getIntPixels();
        final double winX = (double)cardWidthPX - (2*centreX.getRealPixels());
        final double winY = (double)cardHeightPX - (2*centreY.getRealPixels());
        ImageView view = getImageView(0);

        // System.out.println("relocate(" + pX + ", " + pY+ ")  scale = " + spriteScale);
        view.relocate(pX, pY);

        view.setFitWidth(spriteWidth.getRealPixels());
        view.setFitHeight(spriteHeight.getRealPixels());

        // System.out.println("relocate(" + pX + ", " + pY+ ")  scale = " + spriteScale);
        view = getImageView(1);
        if (isLandscape()) {
            System.out.println("landscape");

            view.relocate(pX, pY);  // FIX THIS ??
            
            view.setFitWidth(spriteWidth.getRealPixels());
            view.setFitHeight(spriteHeight.getRealPixels());
        } else {
            System.out.println("portrait");
            view.setVisible(false);
        }

    }


    private void paintIcon(ImageView view, Loc location) {
        System.out.println("paintIcon()");

        final double winX = cardWidthPX - (2*centreX.getRealPixels());
        final double offX = location.getXOffset() * winX;
        double pX = centreX.getRealOrigin() + offX;

        final double winY = cardHeightPX - (2*centreY.getRealPixels());
        final double offY = location.getYOffset() * winY;
        double pY = centreY.getRealOrigin() + offY;

        // System.out.println("relocate(" + pX + ", " + pY+ ")  scale = " + spriteScale);
        view.relocate(pX, pY);

        view.setFitWidth(spriteWidth.getRealPixels());
        view.setFitHeight(spriteHeight.getRealPixels());
    }

    /**
     * Paint the icons associated with this payload.
     */
    private void setPatterns() {
        System.out.println("setPatterns()");
        // if (!display) {
        //     System.out.println("setPatterns() ABORTING!");

        //     return;
        // }
        
        if (item == Item.FACE) {
            if (image != null)
                paintImage();
            return;
        }

        for (int i = 0; i < getImageCount(); ++i) {
            final boolean visible = isImageViewVisible(i);
            ImageView view = getImageView(i);
            view.setVisible(visible);

            if (visible)
                paintIcon(view, getLocation(i));
        }
    }

    /**
     * Change the pattern of standard pips to match the current card.
     * 
     * @return true if the pattern was changed, false otherwise.
     */
    public boolean syncCurrentCard() {
        // System.out.println("syncCurrentCard(" + item + ")");
        final boolean change = (item == Item.STANDARD_PIP);

        if (change)
            pattern = model.getCard();

        System.out.println("syncCurrentCard(" + pattern + ")");

        setPatterns();

        return change;
    }

    /**
     * Copy the attributes from another Payload object.
     * @param p the Payload object to copy.
     * @return true if the attributes were copied, false otherwise.
     */
    public boolean copyPercentages(Payload p) {
        if ((p == this) || (image == null)) {
            return false;
        }

        display = p.display;
        keepAspectRatio = p.keepAspectRatio;
        spriteHeight.setPercent(p.getSpriteH());
        spriteWidth.setPixels(spriteHeight.getRealPixels() * imageWidthPX / imageHeightPX);
        centreX.setPercent(p.getSpriteX());
        centreY.setPercent(p.getSpriteY());
        spriteScale = spriteHeight.getRealPixels() / imageHeightPX;
        
//        System.out.printf("copyPercentages(payload) spriteSize = %f,  spriteScale = %f\n", p.getSpriteH(), spriteScale);
        return true;
    }

    private void resizePercentages() {
        if (image == null) {
            return;
        }

        spriteWidth.setPixels(spriteHeight.getRealPixels() * imageWidthPX / imageHeightPX);
        spriteScale = spriteHeight.getRealPixels() / imageHeightPX;
    }
    
    public void resizeCard(double w, double h) {
        if (image == null) {
            return;
        }
//        System.out.printf("resizeCard() Width = %d,  Height = %d\n", w, h);

        cardWidthPX = w;
        cardHeightPX = h;
    }

    /**
     * Set the X co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card width.
     */
    public void setX(double value) {
        if ((value < 0f) || (value > 100f))
            return;

        centreX.setPercent(value);
    }

    /**
     * Set the Y co-ordinate of the centre of the sprite.
     * @param value as a percentage of the card height.
     */
    public void setY(double value) {
        if ((value < 0f) || (value > 100f))
            return;

        centreY.setPercent(value);
    }

    /**
     * Set the size of the sprite.
     * @param size as a percentage of the card height.
     */
    public void setSize(double size) {
        if ((size < 0f) || (size > 100f))
            return;

//        System.out.printf("setSize(%f)\n", size);
        spriteHeight.setPercent(size);
        resizePercentages();
    }

    /**
     * Increase the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public double incSize() {
        double size = spriteHeight.getPercent();
        if (size != 100f) {
            size += 0.5F;
            if (size > 100f)
                size = 100f;
            spriteHeight.setPercent(size);
            resizePercentages();
        }
        
        return size;
    }

    /**
     * Decrease the size of the sprite.
     * @return the new size as a percentage of the card height.
     */
    public double decSize() {
        double size = spriteHeight.getPercent();
        if (size != 0f) {
            size -= 0.5F;
            if (size < 0f)
                size = 0f;
            spriteHeight.setPercent(size);
            resizePercentages();
        }
        
        return size;
    }

    public void moveBy(long dx, long dy) {
        centreX.setPixels((spriteWidth.getRealPixels()/2) + centreX.getOrigin() + dx);
        centreY.setPixels((spriteHeight.getRealPixels()/2) + centreY.getOrigin() + dy);
    }

    public long getX() {
        return centreX.getOrigin();
    }

    public long getY() {
        return centreY.getOrigin();
    }

    public long getCentreX() {
        return centreX.getIntPixels();
    }

    public long getCentreY() {
        return centreY.getIntPixels();
    }

    /**
     * @return the width of the image on disc.
     */
    public double getWidthPX() {
        return imageWidthPX;
    } 

    /**
     * @return the height of the image on disc.
     */
    public double getHeightPX() {
        return imageHeightPX;
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
     * @return the width of the sprite as a percentage of the card width.
     */
    public double getSpriteW() {
        return spriteWidth.getPercent();
    }

    /**
     * @return the height of the sprite as a percentage of the card height.
     */
    public double getSpriteH() {
        return spriteHeight.getPercent();
    }

    /**
     * @return the width of the sprite in pixels.
     */
    public long getSpriteWidthPX() {
        return spriteWidth.getIntPixels();
    }

    /**
     * @return the height of the sprite in pixels.
     */
    public long getSpriteHeightPX() {
        return spriteHeight.getIntPixels();
    }

    /**
     * @return the pattern being used for this Payload.
     */
    public int getPattern() {
        return pattern;
    }

    /**
     * @return the Item this Payload represents.
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the path to the image file used for this Payload.
     */
    public String getPath() {
        return path;
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
     * Set the arrangement of the standard pips to use for this payload.
     * @param pattern indicating the arrangement to use.
     */
    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    /**
     * Hide/display all locations of icons for this item.
     * @param state if true, display the icons, hide them otherwise.
     */
    public void setVisible(boolean state) {
        // System.out.println("setVisible(" + state + ")");
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

    /**
     * Flag whether the Payload image should maintain it's aspect ratio.
     * @param keepAspectRatio when displaying the image if true.
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }
}
