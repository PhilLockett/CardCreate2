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
 * MainController is a class that is responsible for centralizing control. It
 * creates the Model and CardSample window and provides a callback mechanism.
 */
package phillockett65.CardCreate2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.sample.CardSample;

public class MainController {

    @FXML
    private Button baseDirectoryjButton;

    @FXML
    private ComboBox<String> baseDirectoryjComboBox;

    @FXML
    private Label baseDirectoryjLabel;

    @FXML
    private RadioButton bridgejRadioButton;

    @FXML
    private Button colourjButton;

    @FXML
    private TextField colourjTextField;

    @FXML
    private CheckBox cornerPipjCheckBox;

    @FXML
    private RadioButton cornerPipjRadioButton;

    @FXML
    private CheckBox facePipjCheckBox;

    @FXML
    private RadioButton facePipjRadioButton;

    @FXML
    private CheckBox facejCheckBox;

    @FXML
    private ComboBox<String> facejComboBox;

    @FXML
    private Label facejLabel;

    @FXML
    private RadioButton facejRadioButton;

    @FXML
    private RadioButton freejRadioButton;

    @FXML
    private Button generatejButton;

    @FXML
    private Button heightjButton;

    @FXML
    private Label heightjLabel;

    @FXML
    private Spinner<Integer> heightjSpinner;
    private SpinnerValueFactory<Integer> heightSVF;

    @FXML
    private ComboBox<String> indexjComboBox;

    @FXML
    private Label indexjLabel;

    @FXML
    private CheckBox indicesjCheckBox;

    @FXML
    private RadioButton indicesjRadioButton;

    @FXML
    private Button itemCentreXjButton;

    @FXML
    private Label itemCentreXjLabel;

    @FXML
    private Spinner<Integer> itemCentreXjSpinner;
    private SpinnerValueFactory<Integer> itemCentreXSVF;

    @FXML
    private Button itemCentreYjButton;

    @FXML
    private Label itemCentreYjLabel;

    @FXML
    private Spinner<Integer> itemCentreYjSpinner;
    private SpinnerValueFactory<Integer> itemCentreYSVF;

    @FXML
    private Button itemHeightjButton;

    @FXML
    private Label itemHeightjLabel;

    @FXML
    private Spinner<Integer> itemHeightjSpinner;
    private SpinnerValueFactory<Integer> itemHeightSVF;

    @FXML
    private CheckBox keepAspectRatiojCheckBox;

    @FXML
    private Button nextCardjButton;

    @FXML
    private Button nextSuitjButton;

    @FXML
    private TextField outputjTextField;

    @FXML
    private ToggleButton outputjToggleButton;

    @FXML
    private ComboBox<String> pipjComboBox;

    @FXML
    private Label pipjLabel;

    @FXML
    private RadioButton pokerjRadioButton;

    @FXML
    private Button previousCardjButton;

    @FXML
    private Button previousSuitjButton;

    @FXML
    private CheckBox standardPipjCheckBox;

    @FXML
    private RadioButton standardPipjRadioButton;

    @FXML
    private Button widthjButton;

    @FXML
    private Label widthjLabel;

    @FXML
    private Spinner<Integer> widthjSpinner;
    private SpinnerValueFactory<Integer> widthSVF;

    @FXML
    void baseDirectoryjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void baseDirectoryjComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void bridgejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void colourjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void cornerPipjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void cornerPipjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void facePipjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facePipjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void freejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void generatejButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void heightjButtonActionPerformed(ActionEvent event) {
        heightSVF.setValue(532);
    }

    @FXML
    void indexjComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void indicesjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void indicesjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void itemCentreXjButtonActionPerformed(ActionEvent event) {
        itemCentreXSVF.setValue(10);
//        itemCentreXSVF.setValue(Math.round(currentItem.getX() * 10));
    }

    @FXML
    void itemCentreYjButtonActionPerformed(ActionEvent event) {
//        itemCentreYSVF.setValue(Math.round(currentItem.getY() * 10));
    }

    @FXML
    void itemHeightjButtonActionPerformed(ActionEvent event) {
        itemHeightSVF.setValue(10);
//        itemHeightSVF.setValue(Math.round(currentItem.getH() * 10));
    }

    @FXML
    void keepAspectRatiojCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void nextCardjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void nextSuitjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void outputjTextFieldActionPerformed(ActionEvent event) {

    }

    @FXML
    void outputjToggleButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void pipjComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void pokerjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void previousCardjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void previousSuitjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void standardPipjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void standardPipjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void widthjButtonActionPerformed(ActionEvent event) {
        widthSVF.setValue(380);
    }




	// Main

	private Model model;
	private CardSample sample;

	private void setUpImageButton(Button button, String imageFileName)
	{
		Image image = new Image(getClass().getResourceAsStream(imageFileName));
		ImageView view = new ImageView(image);
		
		button.setGraphic(view);
		button.setText(null);
	}
	/**
	 * Called by the FXML mechanism to initialize the controller. Creates a 
	 * callback link for all the tab controllers, creates the PTable window and
	 * update the Status tab display.
	 */
	@FXML public void initialize() {
//		System.out.println("MainController initialized.");

		setUpImageButton(generatejButton, "icon-play.png");
		setUpImageButton(previousSuitjButton, "icon-up.png");
		setUpImageButton(previousCardjButton, "icon-left.png");
		setUpImageButton(nextCardjButton, "icon-right.png");
		setUpImageButton(nextSuitjButton, "icon-down.png");

	    widthSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(38, 3800, 380);
	    widthjSpinner.setValueFactory(widthSVF);

	    heightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(53, 5320, 532);
	    heightjSpinner.setValueFactory(heightSVF);

	    itemHeightSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemHeightjSpinner.setValueFactory(itemHeightSVF);

	    itemCentreXSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreXjSpinner.setValueFactory(itemCentreXSVF);

	    itemCentreYSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
	    itemCentreYjSpinner.setValueFactory(itemCentreYSVF);

		sample = new CardSample(this, model, "Periodic Table");
	}


	/**
	 * Constructor.
	 * 
	 * Responsible for creating the Model.
	 */
	public MainController() {
//		System.out.println("MainController constructed.");
		model = new Model();
		
	}

}
