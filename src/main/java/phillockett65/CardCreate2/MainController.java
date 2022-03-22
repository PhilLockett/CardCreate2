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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import phillockett65.CardCreate2.Model;
import phillockett65.CardCreate2.sample.CardSample;
import phillockett65.CardCreate2.sample.Item;

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

    private void setStatusMessage(String Message) {
        statusLabel.setText(Message);
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

		/**
		 * Initialize "Playing Card Generator" panel.
		 */

	    sample = new CardSample(this, model, "Sample");

		setUpImageButton(generateButton, "icon-play.png");
		setUpImageButton(previousSuitButton, "icon-up.png");
		setUpImageButton(previousCardButton, "icon-left.png");
		setUpImageButton(nextCardButton, "icon-right.png");
		setUpImageButton(nextSuitButton, "icon-down.png");

		initializeInputDirectories();
		initializeGenerate();
		initializeOutputDirectory();
		initializeSampleNavigation();
		initializeCardSize();
		initializeBackgroundColour();
		initializeDisplayCardItems();
		initializeSelectCardItem();
		initializeModifySelectedCardItem();
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
	 * Initialize "Input Directories" panel.
	 */
	private void initializeInputDirectories() {
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
	}



    /**
     * Support code for "Generate" panel. 
     */

    @FXML
    private Button generateButton;

    @FXML
    void generateButtonActionPerformed(ActionEvent event) {
    	model.displayDiags();
    	setStatusMessage("Output sent to: " + model.getOutputDirectory());
    }

	/**
	 * Initialize"Generate" panel.
	 */
	private void initializeGenerate() {
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
	 * Initialize"Output Directory" panel.
	 */
	private void initializeOutputDirectory() {
	}



    /**
     * Support code for "Sample Navigation" panel. 
     */

    @FXML
    private Button previousCardButton;

    @FXML
    private Button previousSuitButton;

    @FXML
    private Button nextCardButton;

    @FXML
    private Button nextSuitButton;

    @FXML
    void previousCardButtonActionPerformed(ActionEvent event) {
    	model.prevCard();
    }

    @FXML
    void previousSuitButtonActionPerformed(ActionEvent event) {
    	model.prevSuit();
    }

    @FXML
    void nextCardButtonActionPerformed(ActionEvent event) {
    	model.nextCard();
    }

    @FXML
    void nextSuitButtonActionPerformed(ActionEvent event) {
    	model.nextSuit();
    }

	/**
	 * Initialize "Sample Navigation" panel.
	 */
	private void initializeSampleNavigation() {
	}



    /**
     * Support code for "Card Size" panel. 
     */

    @FXML
    private ToggleGroup cardSize;

    @FXML
    private RadioButton pokerRadioButton;

    @FXML
    private RadioButton bridgeRadioButton;

    @FXML
    private RadioButton freeRadioButton;

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
    void cardSizeRadioButtonActionPerformed(ActionEvent event) {
    	if (pokerRadioButton.isSelected())
    		model.setCardSize(Model.CardSize.POKER);
    	else
    	if (bridgeRadioButton.isSelected())
    		model.setCardSize(Model.CardSize.BRIDGE);
    	else
    	if (freeRadioButton.isSelected())
    		model.setCardSize(Model.CardSize.FREE);

    	final boolean autoWidth = model.isAutoCardWidth();
    	widthjSpinner.setDisable(autoWidth);
    	widthjButton.setDisable(autoWidth);
    }

    @FXML
    void widthjButtonActionPerformed(ActionEvent event) {
    	model.defaultWidthSVF();
    }

    @FXML
    void heightjButtonActionPerformed(ActionEvent event) {
    	model.defaultHeightSVF();
    }

	/**
	 * Initialize "Card Size" panel.
	 */
	private void initializeCardSize() {
	    widthjSpinner.setValueFactory(model.getWidthSVF());
	    heightjSpinner.setValueFactory(model.getHeightSVF());
	}



    /**
     * Support code for "Background Colour" panel. 
     */

    @FXML
    private TextField colourTextField;

    @FXML
    private ColorPicker colourPicker;

    @FXML
    void colourPickerActionPerformed(ActionEvent event) {
    	model.setBackgroundColour(colourPicker.getValue());
		colourTextField.setText(model.getBackgroundColourString());
    }

	/**
	 * Initialize "Background Colour" panel.
	 */
	private void initializeBackgroundColour() {
		colourTextField.setText(model.getBackgroundColourString());
    }



    /**
     * Support code for "Display Card Items" panel. 
     */

    @FXML
    private CheckBox indicesCheckBox;

    @FXML
    private CheckBox cornerPipCheckBox;

    @FXML
    private CheckBox standardPipCheckBox;

    @FXML
    private CheckBox faceCheckBox;

    @FXML
    private CheckBox facePipCheckBox;

    @FXML
    void indicesjCheckBoxActionPerformed(ActionEvent event) {
    	model.setDisplayIndex(indicesCheckBox.isSelected());
    }

    @FXML
    void cornerPipjCheckBoxActionPerformed(ActionEvent event) {
    	model.setDisplayCornerPip(cornerPipCheckBox.isSelected());
    }

    @FXML
    void standardPipjCheckBoxActionPerformed(ActionEvent event) {
    	model.setDisplayStandardPip(standardPipCheckBox.isSelected());
    }

    @FXML
    void facejCheckBoxActionPerformed(ActionEvent event) {
    	model.setDisplayFaceImage(faceCheckBox.isSelected());
    }

    @FXML
    void facePipjCheckBoxActionPerformed(ActionEvent event) {
    	model.setDisplayFacePip(facePipCheckBox.isSelected());
    }

    /**
	 * Initialize "Display Card Items" panel.
	 */
	private void initializeDisplayCardItems() {
    }



	/**
     * Support code for "Select Card Item" panel. 
     */

    @FXML
    private ToggleGroup cardItem;

    @FXML
    private RadioButton indicesRadioButton;

    @FXML
    private RadioButton cornerPipRadioButton;

    @FXML
    private RadioButton standardPipRadioButton;

    @FXML
    private RadioButton faceRadioButton;

    @FXML
    private RadioButton facePipRadioButton;

    @FXML
    void cardItemRadioButtonActionPerformed(ActionEvent event) {
    	if (indicesRadioButton.isSelected())
    		model.setCardItem(Item.INDEX);
    	else
    	if (cornerPipRadioButton.isSelected())
    		model.setCardItem(Item.CORNER_PIP);
    	else
    	if (standardPipRadioButton.isSelected())
    		model.setCardItem(Item.STANDARD_PIP);
    	else
    	if (faceRadioButton.isSelected())
    		model.setCardItem(Item.FACE);
    	else
    	if (facePipRadioButton.isSelected())
    		model.setCardItem(Item.FACE_PIP);

//    	if (indicesRadioButton.isSelected())
//    		model.setCardItem(Model.CardItem.INDEX);
//    	else
//    	if (cornerPipRadioButton.isSelected())
//    		model.setCardItem(Model.CardItem.CORNER_PIP);
//    	else
//    	if (standardPipRadioButton.isSelected())
//    		model.setCardItem(Model.CardItem.STANDARD_PIP);
//    	else
//    	if (faceRadioButton.isSelected())
//    		model.setCardItem(Model.CardItem.FACE);
//    	else
//    	if (facePipRadioButton.isSelected())
//    		model.setCardItem(Model.CardItem.FACE_PIP);
    }

    /**
	 * Initialize "Select Card Item" panel.
	 */
	private void initializeSelectCardItem() {
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
    private CheckBox keepAspectRatioCheckBox;

    @FXML
    void itemHeightButtonActionPerformed(ActionEvent event) {
    	model.setCurrentDefaultH();
    }

    @FXML
    void itemCentreXButtonActionPerformed(ActionEvent event) {
    	model.setCurrentDefaultX();
    }

    @FXML
    void itemCentreYButtonActionPerformed(ActionEvent event) {
    	model.setCurrentDefaultY();
    }

    @FXML
    void keepAspectRatioCheckBoxActionPerformed(ActionEvent event) {
    	model.setkeepImageAspectRatio(keepAspectRatioCheckBox.isSelected());
    }

    /**
	 * Initialize "Modify Selected Card Item" panel.
	 */
	private void initializeModifySelectedCardItem() {
	    itemHeightjSpinner.setValueFactory(model.getItemHeightSVF());
	    itemCentreXjSpinner.setValueFactory(model.getItemCentreXSVF());
	    itemCentreYjSpinner.setValueFactory(model.getItemCentreYSVF());

	}


}
