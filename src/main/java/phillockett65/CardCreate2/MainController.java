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

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.sample.CardSample;

public class MainController {

    /**
     * Support code for "Playing Card Generator" panel. 
     */

	private Stage stage;
	private Model model;
	private CardSample sample;

    @FXML
    private BorderPane userGUI;

    @FXML
    private Label statusjLabel;

    public void setStage(Stage stage) {
    	this.stage = stage;
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

		sample = new CardSample(this, model, "Sample");
	}

	public void init() {
        if (!model.isValidBaseDirectory()) {
            if (selectValidBaseDirectory() == false) {
                stage.close();
                sample.close();
            }
        }
	}


    /**
     * Support code for "Input Directories" panel. 
     */
    @FXML
    private Label baseDirectoryjLabel;

    @FXML
    private ComboBox<String> baseDirectoryjComboBox;

    @FXML
    private Button baseDirectoryjButton;

    @FXML
    private Label facejLabel;

    @FXML
    private Label indexjLabel;

    @FXML
    private Label pipjLabel;

    @FXML
    private ChoiceBox<String> facejComboBox;

    @FXML
    private ChoiceBox<String> indexjComboBox;

    @FXML
    private ChoiceBox<String> pipjComboBox;


    private boolean setjComboBoxModelFromArrayList(ChoiceBox<String> choiceBox, ArrayList<String> list) {
        if (list.isEmpty())
            return false;

        choiceBox.getItems().clear();
        for (String s : list)
            choiceBox.getItems().add(s);

    	choiceBox.setValue(list.get(0));

        return true;
    }

    private boolean selectValidBaseDirectory() {

        do {
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Do you wish to continue?");
        	alert.setHeaderText("Continue by selecting a valid directory?");
        	alert.setContentText("You need to select a valid directory that contains 'faces',\n'indices' and 'pips' directories.");

        	Optional<ButtonType> result = alert.showAndWait();

        	if (!result.isPresent() || result.get() != ButtonType.OK)
        		return false;

            selectBaseDirectory();
        } while (model.isValidBaseDirectory() == false);

        return true;
    }

    private boolean filljComboBox(ChoiceBox<String> choiceBox, ArrayList<String> styleList) {
        return setjComboBoxModelFromArrayList(choiceBox, styleList);
    }

    private boolean setBaseDirectory(File directory) {

    	model.setBaseDirectory(directory);

    	setjComboBoxModelFromArrayList(facejComboBox, model.getFacesList());
    	setjComboBoxModelFromArrayList(indexjComboBox, model.getIndexList());
    	setjComboBoxModelFromArrayList(pipjComboBox, model.getPipList());

    	setGenerationEnabled(directory);

        return model.isValidBaseDirectory();
    }

    // Control widgets until a valid base directory is provided.
    private boolean setGenerationEnabled(File directory) {

        if (model.isValidBaseDirectory()) {
        	model.setBaseDirectory(directory.getPath() + "\\");
            baseDirectoryjComboBox.getItems().add(directory.getPath());	// TEMP!
            baseDirectoryjComboBox.setValue(directory.getPath()); // TEMP
//            baseDirectoryjComboBoxAdd(baseDirectory);
            outputjTextField.setText("");
        }

        final boolean validBaseDirectory = model.isValidBaseDirectory();
        baseDirectoryjComboBox.setDisable(!validBaseDirectory);
        generatejButton.setDisable(!validBaseDirectory);
        outputjToggleButton.setDisable(!validBaseDirectory);
        facejComboBox.setDisable(!validBaseDirectory);
        indexjComboBox.setDisable(!validBaseDirectory);
        pipjComboBox.setDisable(!validBaseDirectory);

        return validBaseDirectory;
    }

    private boolean selectBaseDirectory() {
    	DirectoryChooser choice = new DirectoryChooser();
        choice.setInitialDirectory(new File(model.getBaseDirectory()));
        choice.setTitle("Select Base Directory");
    	Stage stage = (Stage) userGUI.getScene().getWindow();
        File directory = choice.showDialog(stage);

        if (directory != null) {
//        	System.out.println("Selected: " + directory.getAbsolutePath());
            setBaseDirectory(directory);

            if (model.isValidBaseDirectory()) {
                return true;
            }
        }

        return false;
    }

    @FXML
    void baseDirectoryjButtonActionPerformed(ActionEvent event) {
//    	System.out.println("baseDirectoryjButtonActionPerformed()");
        selectBaseDirectory();
        if (!model.isValidBaseDirectory()) {
            if (!selectValidBaseDirectory()) {
                // Put original base directory back.
                File directory = new File(model.getBaseDirectory());
                setBaseDirectory(directory);
            }
        }
    }

    @FXML
    void baseDirectoryjComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void indexjComboBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void pipjComboBoxActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Generate" panel. 
     */

    @FXML
    private Button generatejButton;

    @FXML
    void generatejButtonActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Output Directory" panel. 
     */

    @FXML
    private TextField outputjTextField;

    @FXML
    private ToggleButton outputjToggleButton;

    @FXML
    void outputjTextFieldActionPerformed(ActionEvent event) {

    }

    @FXML
    void outputjToggleButtonActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Sample Navigation" panel. 
     */

    @FXML
    private Button previousCardjButton;

    @FXML
    private Button previousSuitjButton;

    @FXML
    private Button nextCardjButton;

    @FXML
    private Button nextSuitjButton;

    @FXML
    void previousCardjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void previousSuitjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void nextCardjButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void nextSuitjButtonActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Card Size" panel. 
     */

    @FXML
    private RadioButton pokerjRadioButton;

    @FXML
    private RadioButton bridgejRadioButton;

    @FXML
    private RadioButton freejRadioButton;

    @FXML
    private Label widthjLabel;

    @FXML
    private Label heightjLabel;

    @FXML
    private Spinner<Integer> widthjSpinner;
    private SpinnerValueFactory<Integer> widthSVF;

    @FXML
    private Spinner<Integer> heightjSpinner;
    private SpinnerValueFactory<Integer> heightSVF;

    @FXML
    private Button widthjButton;

    @FXML
    private Button heightjButton;

    @FXML
    void pokerjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void bridgejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void freejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void widthjButtonActionPerformed(ActionEvent event) {
        widthSVF.setValue(380);
    }

    @FXML
    void heightjButtonActionPerformed(ActionEvent event) {
        heightSVF.setValue(532);
    }



    /**
     * Support code for "Background Colour" panel. 
     */

    @FXML
    private TextField colourjTextField;

    @FXML
    private Button colourjButton;

    @FXML
    void colourjButtonActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Display Card Items" panel. 
     */

    @FXML
    private CheckBox indicesjCheckBox;

    @FXML
    private CheckBox cornerPipjCheckBox;

    @FXML
    private CheckBox standardPipjCheckBox;

    @FXML
    private CheckBox facejCheckBox;

    @FXML
    private CheckBox facePipjCheckBox;

    @FXML
    void indicesjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void cornerPipjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void standardPipjCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejCheckBoxActionPerformed(ActionEvent event) {

    }

    @FXML
    void facePipjCheckBoxActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Select Card Item" panel. 
     */

    @FXML
    private RadioButton indicesjRadioButton;

    @FXML
    private RadioButton cornerPipjRadioButton;

    @FXML
    private RadioButton standardPipjRadioButton;

    @FXML
    private RadioButton facejRadioButton;

    @FXML
    private RadioButton facePipjRadioButton;

    @FXML
    void indicesjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void cornerPipjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void standardPipjRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void facejRadioButtonActionPerformed(ActionEvent event) {

    }

    @FXML
    void facePipjRadioButtonActionPerformed(ActionEvent event) {

    }



    /**
     * Support code for "Modify Selected Card Item" panel. 
     */

    @FXML
    private Label itemHeightjLabel;

    @FXML
    private Label itemCentreXjLabel;

    @FXML
    private Label itemCentreYjLabel;

    @FXML
    private Spinner<Integer> itemHeightjSpinner;
    private SpinnerValueFactory<Integer> itemHeightSVF;

    @FXML
    private Spinner<Integer> itemCentreXjSpinner;
    private SpinnerValueFactory<Integer> itemCentreXSVF;

    @FXML
    private Spinner<Integer> itemCentreYjSpinner;
    private SpinnerValueFactory<Integer> itemCentreYSVF;

    @FXML
    private Button itemHeightjButton;

    @FXML
    private Button itemCentreXjButton;

    @FXML
    private Button itemCentreYjButton;

    @FXML
    private CheckBox keepAspectRatiojCheckBox;

    @FXML
    void itemHeightjButtonActionPerformed(ActionEvent event) {
        itemHeightSVF.setValue(10);
//        itemHeightSVF.setValue(Math.round(currentItem.getH() * 10));
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
    void keepAspectRatiojCheckBoxActionPerformed(ActionEvent event) {

    }

}
