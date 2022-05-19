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
 * Utils is a library of general purpose methods. 
 */
package phillockett65.CardCreate2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import phillockett65.CardCreate2.sample.Default;

public class Utils {

    /**
     * Load an image file from disc.
     * 
     * @param path to the image file.
     * @return the Image, or null if the file is not found.
     */
    public static Image loadImage(String path) {
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
     * Rotate the given image by 180 degrees.
     * 
     * @param image to rotate.
     * @return the rotated Image.
     */
    public static Image rotateImage(Image input) {

        PixelReader reader = input.getPixelReader();

        int width = (int)input.getWidth();
        int height = (int)input.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();

        // Hand draw rotated image.
        int a = width-1;
        int b = height-1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Color color = reader.getColor(x, y);
                
                writer.setColor(a, b, color);
                a--;
            }
            a = width-1;
            b--;
        }

        return output;
    }


    public static final Color opaque = Color.BLACK;
    public static final Color transparent = Color.WHITE;

    public static WritableImage createMask(double width, double height, double arcWidth, double arcHeight) {

        // Create mask.
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(transparent);
        gc.fillRect(0, 0, width, height);
        gc.setFill(opaque);
        gc.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);

        gc.setStroke(opaque);
        gc.setLineWidth(Default.BORDER_WIDTH.getInt());
        gc.strokeRoundRect(0, 0, width, height, arcWidth, arcHeight);

        WritableImage mask = null;
        SnapshotParameters parameters = new SnapshotParameters();

        try {
            mask = canvas.snapshot(parameters, null);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return mask;
    }

}
