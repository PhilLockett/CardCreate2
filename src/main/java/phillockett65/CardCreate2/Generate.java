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
 * Generate is a concurrent task used to generate the card images and save 
 * them to disc. 
 */
package phillockett65.CardCreate2;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import phillockett65.CardCreate2.sample.Default;

public class Generate {

    private final Color opaque = Color.BLACK;
    private final Color transparent = Color.WHITE;

    private Model model;
    
    public Generate(Model model) {
        this.model = model;
    }

    private WritableImage createMask() {

        final double xMax = model.getWidth();
        final double yMax = model.getHeight();
        final double arcWidth = model.getArcWidthPX();
        final double arcHeight = model.getArcHeightPX();

        // Create mask.
        Canvas canvas = new Canvas(xMax, yMax);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(transparent);
        gc.fillRect(0, 0, xMax, yMax);
        gc.setFill(opaque);
        gc.fillRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);

        gc.setStroke(opaque);
        gc.setLineWidth(Default.BORDER_WIDTH.getInt());
        gc.strokeRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);

        // get image from mask
        WritableImage mask = new WritableImage((int)xMax, (int)yMax);
        // mask = canvas.snapshot(null, mask);
        
        try {
            canvas.snapshot(null, mask);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return mask;
    }


    private class CardContext {
        private final double xMax;
        private final double yMax;
        private final double arcWidth;
        private final double arcHeight;
        private Group root;
        private final Canvas canvas;
        private final GraphicsContext gc;
        private Image mask;

        public CardContext(Image mask) {
            this.mask = mask;

            xMax = model.getWidth();
            yMax = model.getHeight();
            arcWidth = model.getArcWidthPX();
            arcHeight = model.getArcHeightPX();

            root = new Group();

            // We need this Scene otherwise the canvas gets default background colour.
            Scene s = new Scene(root, xMax, yMax, Color.TRANSPARENT);
            canvas = new Canvas(xMax, yMax);
            gc = canvas.getGraphicsContext2D();

            // Add the canvas to the root otherwise the snapshot gets default background colour.
            root.getChildren().add(canvas);

            gc.setFill(model.getBackgroundColour());
            gc.setStroke(model.border);
            gc.setLineWidth(Default.BORDER_WIDTH.getInt());

            gc.fillRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
            gc.strokeRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
        }

        public GraphicsContext getGraphicsContext() { return gc; }
        public double getXMax() { return xMax; }
        public double getYMax() { return yMax; }

        private WritableImage applyMask(Image input) {

            PixelReader maskReader = mask.getPixelReader();
            PixelReader reader = input.getPixelReader();
    
            int width = (int)input.getWidth();
            int height = (int)input.getHeight();
    
            WritableImage output = new WritableImage(width, height);
            PixelWriter writer = output.getPixelWriter();
    
            // Blend image and mask.
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    final Color color = reader.getColor(x, y);
                    final boolean show = maskReader.getColor(x, y).equals(opaque);
                    
                    if (show)
                        writer.setColor(x, y, color);
                }
            }
    
            return output;
        }
    
        public boolean write(int s, int c) {
            boolean success = false;
            try {
                final WritableImage snapshot = canvas.snapshot(null, null);
                final BufferedImage image;

                if (mask == null) {
                    image = SwingFXUtils.fromFXImage(snapshot, null);
                } else {
                    Image cropped = applyMask(snapshot);
                    image = SwingFXUtils.fromFXImage(cropped, null);
                }

                final String outputPath = model.getOutputImagePath(s, c);

                ImageIO.write(image, "png", new File(outputPath));
                success = true;
            } catch (Exception e) {
                System.out.println("Failed saving image: " + e);
            }
    
            return success;
        }
    }



    /**
     * Generate the indicated card to disc using the current settings.
     * 
     * @param suit of card to generate.
     * @param card number of card to generate.
     * @param images list of pip Images to use (so they are only read once).
     */
    private void generateCard(int suit, int card, Image[] images, Image mask) {

        // Create blank card.
        CardContext cc = new CardContext(mask);
        GraphicsContext gc = cc.getGraphicsContext();

        // Add the icons using the Payloads.
        if (model.showWatermark(suit, card))
            gc.drawImage(model.getWatermark(), 0, 0, cc.getXMax(), cc.getYMax());

        if (model.shouldIndexBeDisplayed()) {
            Image image = Utils.loadImage(model.getIndexImagePath(suit, card));
            Image rotatedImage = Utils.rotateImage(image);

            model.drawCardIndex(gc, image, rotatedImage);
        }

        if (model.shouldCornerPipBeDisplayed())
            model.drawCardCornerPip(gc, images[2], images[3]);

        if (model.shouldFaceImageBeDisplayed(suit, card)) {
            Image image = Utils.loadImage(model.getFaceImagePath(suit, card));
            Image rotatedImage = Utils.rotateImage(image);

            model.drawCardFace(gc, image, rotatedImage);
        }

        if (model.shouldStandardPipBeDisplayed(suit, card))
            model.drawCardStandardPip(gc, images[0], images[1], card);

        if (model.shouldFacePipBeDisplayed(card))
            model.drawCardFacePip(gc, images[4], images[5]);

        // Write the image to disc.
        cc.write(suit, card);
    }

    /**
     * Generate the indicated joker card to disc using the current settings.
     * 
     * @param suit of joker to generate.
     * @param defaults number of times no joker image file was found, used to 
     * vary default joker generation.
     * @return the number of times no joker image file was found.
     */
    private int generateJoker(int suit, int defaults, Image mask) {

        // Create blank card.
        CardContext cc = new CardContext(mask);
        GraphicsContext gc = cc.getGraphicsContext();

        // Draw Joker indices specific to the suit.
        String pathToImage = model.getJokerIndexImagePath(suit);
        Image indexImage = Utils.loadImage(pathToImage);
        if (indexImage != null) {
            Image rotatedImage = Utils.rotateImage(indexImage);

            model.drawJokerIndex(gc, indexImage, rotatedImage);
        }
    
        // Draw Joker image specific to the suit.
        Image faceImage = Utils.loadImage(model.getFaceImagePath(suit, 0));
        
        if (faceImage == null) {
            defaults++;
            if (defaults % 2 == 1) {
                faceImage = Utils.loadImage(model.getBaseDirectory() + "\\boneyard\\Back.png");
            }
        }
        model.drawJokerFace(gc, faceImage);

        // Write the image to disc.
        cc.write(suit, 0);

        return defaults;
    }

    
    /**
     * Generate the card images and save them to disc.
     */
    public void call() {

        // System.out.println("Generate called");

        // Ensure that the output directory exists.
        model.makeOutputDirectory();

        // Generate the cards.
        final Image mask = model.isCropCorners() ? createMask() : null;
        Image[] images = new Image[6];
        final int suits = model.lastSuit();
        final int cards = model.lastCard();
        for (int suit = 0; suit < suits; ++suit) {
            images[0] = Utils.loadImage(model.getStandardPipImagePath(suit));
            images[1] = Utils.rotateImage(images[0]);
            images[2] = Utils.loadImage(model.getCornerPipImagePath(suit));
            images[3] = Utils.rotateImage(images[2]);
            images[4] = Utils.loadImage(model.getFacePipImagePath(suit));
            images[5] = Utils.rotateImage(images[4]);

            for (int card = 1; card < cards; ++card)
                generateCard(suit, card, images, mask);
        }

        // Generate the jokers.
        int defaults = 0;
        for (int suit = 0; suit < suits; ++suit)
            defaults = generateJoker(suit, defaults, mask);
    }

}
