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
 * Write is a concurrent task used to save the card images to disc. 
 */
package phillockett65.CardCreate2;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import phillockett65.CardCreate2.sample.Default;

public class Write extends Task<Long> {

    private final Model model;
    private final Image mask;
    private Long progress;
    private Image snapshot;


    /**
     * Write task constructor.
     * 
     * @param model containing run-time data..
     * @param progress so far.
     * @param mask to apply to final image, may be null.
     * @param image to to save to disc.
     */
    public Write(Model model, Long progress, Image mask, Image image) {
        this.model = model;
        this.progress = progress;
        this.mask = mask;
        this.snapshot = image;

        // Update progress bar to stop it jittering.
        updateProgress(progress, Default.GENERATE_STEPS.getInt());
    }

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
                final boolean show = maskReader.getColor(x, y).equals(Utils.opaque);

                if (show)
                    writer.setColor(x, y, color);
            }
        }

        return output;
    }

    private boolean save() {
        boolean success = false;

        try {
            final BufferedImage image;
            if (mask == null) {
                image = SwingFXUtils.fromFXImage(snapshot, null);
            } else {
                Image cropped = applyMask(snapshot);
                image = SwingFXUtils.fromFXImage(cropped, null);
            }

            final String outputPath = model.currentOutputImagePath();

            success = ImageIO.write(image, "png", new File(outputPath));
        } catch (Exception e) {
            System.out.println("write() - Failed saving image: " + e);
        }

        return success;
    }


    /**
     * Save the card images to disc.
     */
    @Override
    protected Long call() throws Exception {

        if (isCancelled()) {
            return progress;
        }

        save();

        updateProgress(++progress, Default.GENERATE_STEPS.getInt());

        return progress;
    }

}
