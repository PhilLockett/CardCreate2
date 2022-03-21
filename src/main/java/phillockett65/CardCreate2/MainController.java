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
import java.util.Optional;

import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.sample.CardSample;
import phillockett65.CardCreate2.sample.Default;

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
    private Label statusLabel;


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

		setUpImageButton(generateButton, "icon-play.png");
		setUpImageButton(previousSuitjButton, "icon-up.png");
		setUpImageButton(previousCardjButton, "icon-left.png");
		setUpImageButton(nextCardjButton, "icon-right.png");
		setUpImageButton(nextSuitjButton, "icon-down.png");

	    widthjSpinner.setValueFactory(model.getWidthSVF());
	    heightjSpinner.setValueFactory(model.getHeightSVF());
	    itemHeightjSpinner.setValueFactory(model.getItemHeightSVF());
	    itemCentreXjSpinner.setValueFactory(model.getItemCentreXSVF());
	    itemCentreYjSpinner.setValueFactory(model.getItemCentreYSVF());

	    faceChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
	    	model.setFaceStyle(newValue);
	    	outputTextField.setText(model.getOutputName());
	    });
	    indexChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
	    	model.setIndexStyle(newValue);
	    });
	    pipChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
	    	model.setPipStyle(newValue);
	    });

	    sample = new CardSample(this, model, "Sample");
	}

	public void init(Stage stage) {
        this.stage = stage;

        userGUI.setDisable(true);

		if ((!model.isValidBaseDirectory()) &&
            (!selectValidBaseDirectory())) {
            stage.close();
            sample.close();
        }
		setInitialBaseDirectory();
	}


    /**
     * Support code for "Input Directories" panel. 
     */

	@FXML
    private Label baseDirectoryLabel;

    @FXML
    private ComboBox<String> baseDirectoryComboBox;

    @FXML
    private Button baseDirectoryButton;

    @FXML
    private Label faceLabel;

    @FXML
    private Label indexLabel;

    @FXML
    private Label pipLabel;

    @FXML
    private ChoiceBox<String> faceChoiceBox;

    @FXML
    private ChoiceBox<String> indexChoiceBox;

    @FXML
    private ChoiceBox<String> pipChoiceBox;

	private boolean setInitialBaseDirectory() {
//		System.out.println("setInitialBaseDirectory(" + model.getBaseDirectory() + ")");

		baseDirectoryComboBox.setItems(model.getBaseList());
		baseDirectoryComboBox.setValue(model.getBaseDirectory());

		faceChoiceBox.setItems(model.getFaceList());
		faceChoiceBox.setValue(model.getFaceStyle());

		indexChoiceBox.setItems(model.getIndexList());
		indexChoiceBox.setValue(model.getIndexStyle());

		pipChoiceBox.setItems(model.getPipList());
        pipChoiceBox.setValue(model.getPipStyle());

        outputTextField.setText(model.getOutputName());

        userGUI.setDisable(false);

        return true;
    }


	private boolean setBaseDirectory(String base) {
//		System.out.println("setBaseDirectory(" + base + ")");

    	if (!model.setBaseDirectory(base))
    		return false;

		baseDirectoryComboBox.setValue(model.getBaseDirectory());
    	faceChoiceBox.setValue(model.getFaceStyle());
    	indexChoiceBox.setValue(model.getIndexStyle());
    	pipChoiceBox.setValue(model.getPipStyle());

        outputTextField.setText(model.getOutputName());

        return true;
    }

    private boolean selectBaseDirectory() {
    	DirectoryChooser choice = new DirectoryChooser();
        choice.setInitialDirectory(new File(model.getBaseDirectory()));
        choice.setTitle("Select Base Directory");
        File directory = choice.showDialog(stage);

        if (directory != null) {
//        	System.out.println("Selected: " + directory.getAbsolutePath());

            if (setBaseDirectory(directory.getPath()))
                return true;
        }

        return false;
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


    @FXML
    void baseDirectoryjButtonActionPerformed(ActionEvent event) {
//    	System.out.println("baseDirectoryjButtonActionPerformed()");

        if (!selectBaseDirectory()) {
            if (!selectValidBaseDirectory()) {
                // Put original base directory back.
                setBaseDirectory(model.getBaseDirectory());
            }
        }
    }

    @FXML
    void baseDirectoryjComboBoxActionPerformed(ActionEvent event) {
//    	System.out.println("baseDirectoryjComboBoxActionPerformed()" + event.toString());

    	if (!model.setBaseDirectory(baseDirectoryComboBox.getValue()))
    		return;

    	faceChoiceBox.setValue(model.getFaceStyle());
    	indexChoiceBox.setValue(model.getIndexStyle());
    	pipChoiceBox.setValue(model.getPipStyle());

        outputTextField.setText(model.getOutputName());
    }




    /**
     * Support code for "Generate" panel. 
     */

    @FXML
    private Button generateButton;

    @FXML
    void generateButtonActionPerformed(ActionEvent event) {
    	System.out.println(model.getFaceDirectory());
    	System.out.println(model.getIndexDirectory());
    	System.out.println(model.getPipDirectory());
    	System.out.println(model.getOutputDirectory());

    	statusLabel.setText("Output sent to: " + model.getOutputDirectory());
    }



    /**
     * Support code for "Output Directory" panel. 
     */

    @FXML
    private TextField outputTextField;

    @FXML
    private ToggleButton outputToggleButton;


    @FXML
    void outputTextFieldKeyTyped(KeyEvent event) {
//    	System.out.println("outputjTextFieldKeyTyped()" + event.toString());
    	model.setOutputName(outputTextField.getText());
    }

    @FXML
    void outputToggleButtonActionPerformed(ActionEvent event) {
//    	System.out.println("outputjToggleButtonActionPerformed()" + event.toString());

    	final boolean manual = outputToggleButton.isSelected(); 

    	outputTextField.setEditable(manual);
    	model.setOutputNameManually(manual);
        outputTextField.setText(model.getOutputName());
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

    @FXML
    private Spinner<Integer> heightjSpinner;

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
    	model.getWidthSVF().setValue(Default.WIDTH.intr());
    }

    @FXML
    void heightjButtonActionPerformed(ActionEvent event) {
    	model.getHeightSVF().setValue(Default.HEIGHT.intr());
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

    @FXML
    private Spinner<Integer> itemCentreXjSpinner;

    @FXML
    private Spinner<Integer> itemCentreYjSpinner;

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
    	model.getItemHeightSVF().setValue(10);
//        itemHeightSVF.setValue(Math.round(currentItem.getH() * 10));
    }

    @FXML
    void itemCentreXjButtonActionPerformed(ActionEvent event) {
    	model.getItemCentreXSVF().setValue(10);
//        itemCentreXSVF.setValue(Math.round(currentItem.getX() * 10));
    }

    @FXML
    void itemCentreYjButtonActionPerformed(ActionEvent event) {
    	model.getItemCentreYSVF().setValue(10);
//        itemCentreYSVF.setValue(Math.round(currentItem.getY() * 10));
    }

    @FXML
    void keepAspectRatiojCheckBoxActionPerformed(ActionEvent event) {

    }

}
