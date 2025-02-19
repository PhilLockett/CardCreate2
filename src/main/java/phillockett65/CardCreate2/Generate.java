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
 * Generate is a concurrent task used to generate the card images. 
 */
package phillockett65.CardCreate2;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import phillockett65.CardCreate2.sample.Default;


public class Generate extends Task<Long> {

    private final Model model;
    private Long progress;
    private Canvas canvas;
    private int defaults;

    /**
     * Generate task constructor.
     * 
     * @param progress so far.
     * @param defaults number of times no joker image file was found, used to 
     *     vary default joker generation.
     */
    public Generate(Long progress, int defaults) {
        model = Model.getInstance();
        this.progress = progress;
        this.defaults = defaults;

        // Update progress bar to stop it jittering.
        updateProgress(progress, Default.GENERATE_STEPS.getInt());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getDefaults() {
        return defaults;
    }

    private class CardContext {
        private final double width;
        private final double height;
        private final double xOffset;
        private final double yOffset;
        private final Canvas canvas;
        private final GraphicsContext gc;

        public CardContext() {

            width = model.getWidth();
            height = model.getHeight();
            xOffset = model.getMpcBorderWidth();
            yOffset = model.getMpcBorderHeight();

            final double xMax = width + (xOffset * 2);
            final double yMax = height + (yOffset * 2);

            canvas = new Canvas(xMax, yMax);
            gc = canvas.getGraphicsContext2D();

            gc.setFill(model.getBackgroundColour());
            
            if (!model.isMpcCardSize()) {
                final double arcWidth = model.getArcWidthPX();
                final double arcHeight = model.getArcHeightPX();

                gc.fillRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
                gc.setStroke(model.border);
                gc.setLineWidth(Default.BORDER_WIDTH.getInt());
                gc.strokeRoundRect(0, 0, xMax, yMax, arcWidth, arcHeight);
            } else {
                gc.fillRect(0, 0, xMax, yMax);
            }
        }

        public Canvas getCanvas() { return canvas; }
        public GraphicsContext getGraphicsContext() { return gc; }
        public double getWidth() { return width; }
        public double getHeight() { return height; }
        public double getXOffset() { return xOffset; }
        public double getYOffset() { return yOffset; }

    }


    /**
     * Generate the indicated card using the current settings.
     */
    private void generateCard() {
        final int suit = model.currentSuit();
        final int card = model.currentCard();
        final Image[] images = model.currentImages();

        // Create blank card.
        CardContext cc = new CardContext();
        GraphicsContext gc = cc.getGraphicsContext();

        // Add the icons using the Payloads in priority order.
        if (model.showWatermark(suit, card))
            gc.drawImage(model.getWatermark(), cc.getXOffset(), cc.getYOffset(), cc.getWidth(), cc.getHeight());

        int[] priorities = model.getPriorityList();
        for (int i = priorities.length-1; i >= 0; --i) {

            final int priority = priorities[i];

            switch (priority) {
            case Model.INDEX_ID:
                if (model.shouldIndexBeDisplayed()) {
                    Image image = Utils.loadImage(model.getIndexImagePath(suit, card));
                    Image rotatedImage = Utils.rotateImage(image);
        
                    model.drawCardIndex(gc, image, rotatedImage);
                }
            break;
        
            case Model.CORNER_PIP_ID:
                if (model.shouldCornerPipBeDisplayed())
                    model.drawCardCornerPip(gc, images[2], images[3]);
            break;

            case Model.STANDARD_PIP_ID:
                if (model.shouldStandardPipBeDisplayed(suit, card))
                    model.drawCardStandardPip(gc, images[0], images[1], card);
            break;

            case Model.FACE_PIP_ID:
                if (model.shouldFacePipBeDisplayed(card))
                    model.drawCardFacePip(gc, images[4], images[5]);
            break;

            case Model.FACE_ID:
                if (model.shouldFaceImageBeDisplayed(suit, card)) {
                    Image image = Utils.loadImage(model.getFaceImagePath(suit, card));
                    Image rotatedImage = Utils.rotateImage(image);
        
                    model.drawCardFace(gc, image, rotatedImage);
                }
            break;
            }
        }

        canvas = cc.getCanvas();
    }

    /**
     * Generate the indicated joker using the current settings.
     */
    private void generateJoker() {
        final int suit = model.currentSuit();

        // Create blank card.
        CardContext cc = new CardContext();
        GraphicsContext gc = cc.getGraphicsContext();

        // Draw Joker image specific to the suit.
        Image faceImage = Utils.loadImage(model.getFaceImagePath(suit, 0));
        if (faceImage == null) {
            defaults++;
            if (defaults % 2 == 1) {
                faceImage = Utils.loadImage(model.getBaseDirectory() + "\\boneyard\\Back.png");
            }
        }
        model.drawJokerFace(gc, faceImage);

        // Draw Joker indices specific to the suit.
        Image indexImage = Utils.loadImage(model.getJokerIndexImagePath(suit));
        if (indexImage != null) {
            Image rotatedImage = Utils.rotateImage(indexImage);

            model.drawJokerIndex(gc, indexImage, rotatedImage);
        }

        canvas = cc.getCanvas();
    }


    /**
     * Generate the card images and add them to the observable list.
     */
    @Override
    protected Long call() throws Exception {

        if (isCancelled()) {
            return progress;
        }
        
        // Generate the card.
        if (model.currentIsJoker()) {
            generateJoker();
        } else {
            generateCard();
        }

        updateProgress(++progress, Default.GENERATE_STEPS.getInt());

        return progress;
    }

}
