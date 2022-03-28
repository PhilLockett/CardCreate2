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
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Payload {

    public static final int PAINT_DISPLAY = -1;
    public static final int PAINT_FILE = -2;

    final Loc[][] patterns = {
        { Loc.L_3, Loc.L_5 },
        { Loc.L_0 },
        { Loc.L_1, Loc.L_2 },
        { Loc.L_1, Loc.L_0, Loc.L_2 },
        { Loc.L_3, Loc.L_4, Loc.L_5, Loc.L_6 },
        { Loc.L_3, Loc.L_4, Loc.L_0, Loc.L_5, Loc.L_6 },
        { Loc.L_3, Loc.L_4, Loc.L_5, Loc.L_6, Loc.L_7, Loc.L_8 },
        { Loc.L_3, Loc.L_4, Loc.L_5, Loc.L_6, Loc.L_7, Loc.L_8, Loc.L_9 },
        { Loc.L_3, Loc.L_4, Loc.L10, Loc.L_5, Loc.L_6, Loc.L_7, Loc.L_8, Loc.L_9 },
        { Loc.L_3, Loc.L_4, Loc.L11, Loc.L12, Loc.L_0, Loc.L_5, Loc.L_6, Loc.L13, Loc.L14 },
        { Loc.L_3, Loc.L_4, Loc.L11, Loc.L12, Loc.L15, Loc.L_5, Loc.L_6, Loc.L13, Loc.L14, Loc.L16 },
        { Loc.L_3, Loc.L_4, Loc.L10, Loc.L11, Loc.L12, Loc.L_0, Loc.L_5, Loc.L_6, Loc.L_9, Loc.L13, Loc.L14 },
        { Loc.L_1, Loc.L_3, Loc.L_4, Loc.L10, Loc.L11, Loc.L12, Loc.L_2, Loc.L_5, Loc.L_6, Loc.L_9, Loc.L13, Loc.L14 },
        { Loc.L_1, Loc.L_3, Loc.L_4, Loc.L10, Loc.L11, Loc.L12, Loc.L_0, Loc.L_2, Loc.L_5, Loc.L_6, Loc.L_9, Loc.L13, Loc.L14 },
     };

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
    private int pattern = 0;
    private final Item item;
    private final String path;
    private Group group;
    private Image image = null;
    private long imageWidthPX = 0;
    private long imageHeightPX = 0;
    private int cardWidthPX = Default.WIDTH.getInt();
    private int cardHeightPX = Default.HEIGHT.getInt();

    private boolean display;
    private boolean keepAspectRatio;
    private final Real centreX;
    private final Real centreY;
    private final Real spriteHeight;
    private final Real spriteWidth;
    private double spriteScale = 1;

    public Payload(Group group, String path, int w, int h, int p, Item it) {
        System.out.println("Payload(" + path + ")");

        this.group = group;
        pattern = p;
        item = it;

        cardWidthPX = w;
        cardHeightPX = h;

        display = true;
        keepAspectRatio = true;
        centreX = new Real(false);
        centreY = new Real(true);
        spriteWidth = new Real(false);
        spriteHeight = new Real(true);

        // Set up default percentages.
        spriteHeight.setPercent(item.getH());
        centreX.setPercent(item.getX());
        centreY.setPercent(item.getY());

        // Set up image dependent values.
        this.path = path;
        if (path.equals(""))
            return;

        loadNewImageFile(path);
    }

    private Image loadImage(String path) {
//    	System.out.println("loadImage(" + path + ")");
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("File does not exist!");

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
    
    public boolean loadNewImageFile(String path) {
    	System.out.println("loadNewImageFile(" + path + ")");

        image = loadImage(path);

        if (image != null) {
            imageWidthPX = Math.round(image.getWidth());
            imageHeightPX = Math.round(image.getHeight());
            spriteWidth.setPixels(spriteHeight.getRealPixels() * imageWidthPX / imageHeightPX);
            spriteScale = spriteHeight.getRealPixels() / imageHeightPX;

            return true;
        }


        return false;
    }

    private void paintImage(Group group, boolean generate) {

        final long pX = centreX.getIntPixels();
        final long pY = centreY.getIntPixels();
        final double winX = (double)cardWidthPX - (2*centreX.getRealPixels());
        final double winY = (double)cardHeightPX - (2*centreY.getRealPixels());

//        System.out.printf("paintImage() pX = %d,  pY = %d generate = %s\n", pX, pY, generate ? "true" : "false");
//        AffineTransform at = AffineTransform.getTranslateInstance(pX, pY);

        double scaleX = winX / imageWidthPX;
        double scaleY = winY / imageHeightPX;
        double scale;
        if (keepAspectRatio) {
            if (!generate) {
                Rectangle box = new Rectangle(pX, pY, Math.round(winX), Math.round(winY));
                box.setFill(Color.LIGHTGREY);
                group.getChildren().add(box);
            }
            if (scaleX < scaleY) {
                scale = scaleX;
                long dY = Math.round((winY - (imageHeightPX * scale)) / 2);
//                at.translate(0, dY);
            }
            else {
                scale = scaleY;
                long dX = Math.round((winX - (imageWidthPX * scale)) / 2);
//                at.translate(dX, 0);
            }
//            at.scale(scale, scale);
        } 
        else {
//            at.scale(scaleX, scaleY);
        }
//        g2d.drawImage(image, at, null);
    }


    private void paintIcon(Loc location) {

        final double winX = (double)cardWidthPX - (2*centreX.getRealPixels());
        final double offX = location.getXOffset() * winX;
        final long pX = Math.round(centreX.getRealOrigin() + offX);

        final double winY = (double)cardHeightPX - (2*centreY.getRealPixels());
        final double offY = location.getYOffset() * winY;
        final long pY = Math.round(centreY.getRealOrigin() + offY);

//        AffineTransform at = AffineTransform.getTranslateInstance(pX, pY);
//
//        if (location.getRotate()) {
//            at.setToTranslation(cardWidthPX-pX, cardHeightPX-pY);
//            at.quadrantRotate(2);
//        }
//        at.scale(spriteScale, spriteScale);
//        g2d.drawImage(image, at, null);
    }

    /**
     * Paint the icons associated with his payload.
     * @param g2d the 2D graphics object.
     */
    public void paintPatterns() {
        if (!display)
            return;
        
//        if (getPattern() == PAINT_DISPLAY) {
//            paintImage(g2d, false);
//            return;
//        }
//        if (getPattern() == PAINT_FILE) {
//            paintImage(g2d, true);
//            return;
//        }
//
//        for (int i = 0; i < patterns[getPattern()].length; ++i) {
//            paintIcon(g2d, patterns[getPattern()][i]);
//        }
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
    
    public void resizeCard(int w, int h) {
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
    public long getWidthPX() {
        return imageWidthPX;
    } 

    /**
     * @return the height of the image on disc.
     */
    public long getHeightPX() {
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
     * Flag whether the Payload image should be drawn.
     * @param display the pattern if true.
     */
    public void setVisible(boolean display) {
        this.display = display;
    }

    /**
     * Indicates whether the Payload image should be drawn.
     * @return true if the image should be drawn, false otherwise.
     */
    public boolean isVisible() {
        return this.display;
    }

    /**
     * Flag whether the Payload image should maintain it's aspect ratio.
     * @param keepAspectRatio when displaying the image if true.
     */
    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }
}
