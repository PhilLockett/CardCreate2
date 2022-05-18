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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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
	public static Image rotateImage(Image image) {
        // System.out.println("rotateImage()");

        ImageView view = new ImageView(image);
        view.setRotate(180);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return view.snapshot(params, null);
    }

}
