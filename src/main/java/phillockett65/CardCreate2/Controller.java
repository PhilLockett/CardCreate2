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
 * Controller is a class that is responsible for centralizing control. It
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import phillockett65.CardCreate2.sample.CardSample;
import phillockett65.CardCreate2.sample.Item;

public class Controller {

    /************************************************************************
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
    public Controller() {
//		System.out.println("Controller constructed.");
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
//		System.out.println("Controller initialized.");

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

    /**
     * Called by Application after the stage has been set.
     */
    public void init(Stage stage) {
        this.stage = stage;

        userGUI.setDisable(true);

        if ((!model.isValidBaseDirectory()) &&
            (!selectValidBaseDirectory())) {
            stage.close();
            sample.close();
        }

        setInitialBaseDirectory();
        model.init();
        sample.syncHandle();
        setSelectCardItemTooltips();
        initInputDirectoryChoiceBoxHandlers();
    }



    /************************************************************************
     * Support code for "Sample" panel interface. 
     */

    /**
     * Called by Sample when the mouse changes the height of the current Item.
     * 
     * @param h height of the current Item as a percentage of the card height.
     */
    public void resizeIcon(double h) {
        System.out.println("resizeIcon(" + h + ")");
        model.setCurrentH(h, true);
    }

    /**
     * Called by Sample when the mouse moves the current Item.
     * 
     * @param x coordinate of the centre of the current Item as a percentage of the card width.
     * @param y coordinate of the centre of the current Item as a percentage of the card height.
     */
    public void moveIcon(double x, double y) {
        System.out.println("moveIcon(" + x + ", " + y + ")");
        model.setCurrentX(x, true);
        model.setCurrentY(y, true);
    }

    /**
     * Called by Sample when the mouse changes the selected Item.
     * 
     * @param item now selected.
     */
    public void changeIcon(Item item) {
        System.out.println("changeIcon(" + item.getD() + ")");

        if (item == Item.INDEX) {
            indicesRadioButton.setSelected(true);
        }
        else if (item == Item.CORNER_PIP) {
            cornerPipRadioButton.setSelected(true);
        }
        else if (item == Item.STANDARD_PIP) {
            standardPipRadioButton.setSelected(true);
        }
        else if (item == Item.FACE) {
            faceRadioButton.setSelected(true);
        }
        else if (item == Item.FACE_PIP) {
            facePipRadioButton.setSelected(true);
        }

        setSelectCardItemPrompts();
    }



    /************************************************************************
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


    /**
     * Called by init() after a valid Base Directory has initially been 
     * selected.
     */
    private void setInitialBaseDirectory() {
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

        baseDirectoryLabel.setTooltip(new Tooltip("Working directory that contains faces, indices and pips directories"));
        baseDirectoryComboBox.setTooltip(new Tooltip("Select the Base Directory"));
        baseDirectoryButton.setTooltip(new Tooltip("Browse to the Base Directory"));
        faceLabel.setTooltip(new Tooltip("Subdirectory of face image files to use (default: \"1\")"));
        indexLabel.setTooltip(new Tooltip("Subdirectory of index image files to use (default: \"1\")"));
        pipLabel.setTooltip(new Tooltip("Subdirectory of pip image files to use (default: \"1\")"));
        faceChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
        indexChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
        pipChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
    }

    /**
     * This has to be done after the base directory has been set up.
     */
    private void initInputDirectoryChoiceBoxHandlers() {
        faceChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setFaceStyle(newValue);
            sample.syncCurrentCard();
            outputTextField.setText(model.getOutputName());
        });

        indexChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setIndexStyle(newValue);
            sample.syncCurrentCard();
        });

        pipChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setPipStyle(newValue);
            sample.syncCurrentCard();
        });
    }



    /************************************************************************
     * Support code for "Generate" panel. 
     */
    private double xPos = 8.1, yPos = 9.8;

    @FXML
    private Button generateButton;

    @FXML
    void generateButtonActionPerformed(ActionEvent event) {
        model.displayDiags();

        // Diags
        xPos += 0.5;
        yPos += 0.7;
        sample.setHandle(xPos, yPos);
        model.setCurrentX(xPos, true);
        model.setCurrentY(yPos, true);

        setStatusMessage("Output sent to: " + model.getOutputDirectory());
    }

    /**
     * Initialize"Generate" panel.
     */
    private void initializeGenerate() {
        generateButton.setTooltip(new Tooltip("Generate the card images to the selected output directory"));
    }



    /************************************************************************
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
        outputTextField.setTooltip(new Tooltip("Preferred output directory name"));
        outputToggleButton.setTooltip(new Tooltip("Manually enter the output directory name, otherwise use same name as selected Face"));
    }



    /************************************************************************
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
        sample.syncCurrentCard();
    }

    @FXML
    void previousSuitButtonActionPerformed(ActionEvent event) {
        model.prevSuit();
        sample.syncCurrentCard();
    }

    @FXML
    void nextCardButtonActionPerformed(ActionEvent event) {
        model.nextCard();
        sample.syncCurrentCard();
    }

    @FXML
    void nextSuitButtonActionPerformed(ActionEvent event) {
        model.nextSuit();
        sample.syncCurrentCard();
    }

    /**
     * Initialize "Sample Navigation" panel.
     */
    private void initializeSampleNavigation() {
        previousCardButton.setTooltip(new Tooltip("Display previous card as Sample"));
        previousSuitButton.setTooltip(new Tooltip("Display previous suit as Sample"));
        nextCardButton.setTooltip(new Tooltip("Display next card as Sample"));
        nextSuitButton.setTooltip(new Tooltip("Display next suit as Sample"));
    }



    /************************************************************************
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
    private Label widthLabel;

    @FXML
    private Label heightLabel;

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Button widthButton;

    @FXML
    private Button heightButton;

    @FXML
    void cardSizeRadioButtonActionPerformed(ActionEvent event) {
        if (pokerRadioButton.isSelected()) {
            model.setCardSize(Model.CardSize.POKER);
            sample.syncCardSize();
        }
        else
        if (bridgeRadioButton.isSelected()) {
            model.setCardSize(Model.CardSize.BRIDGE);
            sample.syncCardSize();
        }
        else
        if (freeRadioButton.isSelected()) {
            model.setCardSize(Model.CardSize.FREE);
            sample.syncCardSize();
        }

        final boolean autoWidth = model.isAutoCardWidth();
        widthSpinner.setDisable(autoWidth);
        widthButton.setDisable(autoWidth);
    }

    @FXML
    void widthjButtonActionPerformed(ActionEvent event) {
        model.resetCardWidthSVF();
    }

    @FXML
    void heightjButtonActionPerformed(ActionEvent event) {
        model.resetCardHeightSVF();
    }

    /**
     * Initialize "Card Size" panel.
     */
    private void initializeCardSize() {

        pokerRadioButton.setTooltip(new Tooltip("Maintain poker card aspect ratio"));
        bridgeRadioButton.setTooltip(new Tooltip("Maintain bridge card aspect ratio"));
        freeRadioButton.setTooltip(new Tooltip("independently set card width and height"));
        widthLabel.setTooltip(new Tooltip("Card width in pixels (default: 380)"));
        heightLabel.setTooltip(new Tooltip("Card height in pixels (default: 532)"));
        widthSpinner.setTooltip(new Tooltip("Select card width in pixels"));
        heightSpinner.setTooltip(new Tooltip("Select card height in pixels"));
        widthButton.setTooltip(new Tooltip("Reset Card Width to default value of 380 pixels"));
        heightButton.setTooltip(new Tooltip("Reset Card Height to default value of 532 pixels"));

        widthSpinner.setValueFactory(model.getWidthSVF());
        heightSpinner.setValueFactory(model.getHeightSVF());

        widthSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setWidth(newValue);
            sample.syncCardSize();
        });

        heightSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setHeight(newValue);
            sample.syncCardSize();
        });

    }



    /************************************************************************
     * Support code for "Background Colour" panel. 
     */

    @FXML
    private TextField colourTextField;

    @FXML
    private ColorPicker colourPicker;

    @FXML
    void colourPickerActionPerformed(ActionEvent event) {
        model.setBackgroundColour(colourPicker.getValue());
        sample.syncBackgroundColour();
        colourTextField.setText(model.getBackgroundColourString());
    }

    /**
     * Initialize "Background Colour" panel.
     */
    private void initializeBackgroundColour() {
        colourTextField.setTooltip(new Tooltip("Copy and paste where needed"));
        colourPicker.setTooltip(new Tooltip("Select the background colour for the card"));

        colourTextField.setText(model.getBackgroundColourString());
    }



    /************************************************************************
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
        indicesCheckBox.setTooltip(new Tooltip("Select to display card indices"));
        cornerPipCheckBox.setTooltip(new Tooltip("Select to display corner pips"));
        standardPipCheckBox.setTooltip(new Tooltip("Select to display standard pips"));
        faceCheckBox.setTooltip(new Tooltip("Select to display face images"));
        facePipCheckBox.setTooltip(new Tooltip("Select to display face pips"));
    }



    /************************************************************************
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
//    	System.out.println("cardItemRadioButtonActionPerformed()");

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

        setSelectCardItemPrompts();
    }

    /**
     * Initialize "Select Card Item" panel.
     */
    private void initializeSelectCardItem() {
        indicesRadioButton.setTooltip(new Tooltip("Select to modify card indices"));
        cornerPipRadioButton.setTooltip(new Tooltip("Select to modify corner pips"));
        standardPipRadioButton.setTooltip(new Tooltip("Select to modify standard pips"));
        faceRadioButton.setTooltip(new Tooltip("Select to modify face images"));
        facePipRadioButton.setTooltip(new Tooltip("Select to modify face pips"));
    }



    /************************************************************************
     * Support code for "Modify Selected Card Item" panel. 
     */

    @FXML
    private Label itemHeightLabel;

    @FXML
    private Label itemCentreXLabel;

    @FXML
    private Label itemCentreYLabel;

    @FXML
    private Spinner<Double> itemHeightSpinner;

    @FXML
    private Spinner<Double> itemCentreXSpinner;

    @FXML
    private Spinner<Double> itemCentreYSpinner;

    @FXML
    private Button itemHeightButton;

    @FXML
    private Button itemCentreXButton;

    @FXML
    private Button itemCentreYButton;

    @FXML
    private CheckBox keepAspectRatioCheckBox;

    /**
     * Sets the Payload and fixes the "Modify Card Item" controls.
     */
    private void setSelectCardItemPrompts() {
//    	System.out.println("setSelectCardItemPrompts()");

//        samplejPanel.setPayload(item);
//        currentItem = item;
//        updateModifyCardItemControls();
        final boolean centre = !model.isCurrentCentred();
        itemHeightButton.setDisable(centre);
        itemHeightSpinner.setDisable(centre);
        itemHeightLabel.setDisable(centre);

        itemHeightLabel.setText(model.getCurrentHLabel());
        itemCentreXLabel.setText(model.getCurrentXLabel());
        itemCentreYLabel.setText(model.getCurrentYLabel());

        setSelectCardItemTooltips();
    }

    private void setSelectCardItemTooltips() {
//    	System.out.println("setSelectCardItemTooltips()");

        itemHeightButton.setTooltip(model.getCurrentHButtonTip());
        itemCentreXButton.setTooltip(model.getCurrentXButtonTip());
        itemCentreYButton.setTooltip(model.getCurrentYButtonTip());

        itemHeightLabel.setTooltip(model.getCurrentHToolTip());
        itemCentreXLabel.setTooltip(model.getCurrentXToolTip());
        itemCentreYLabel.setTooltip(model.getCurrentYToolTip());
    }

    @FXML
    void itemHeightButtonActionPerformed(ActionEvent event) {
        model.resetCurrentHSVF();
    }

    @FXML
    void itemCentreXButtonActionPerformed(ActionEvent event) {
        model.resetCurrentXSVF();
    }

    @FXML
    void itemCentreYButtonActionPerformed(ActionEvent event) {
        model.resetCurrentYSVF();
    }

    @FXML
    void keepAspectRatioCheckBoxActionPerformed(ActionEvent event) {
        model.setkeepImageAspectRatio(keepAspectRatioCheckBox.isSelected());
    }

    /**
     * Initialize "Modify Selected Card Item" panel.
     */
    private void initializeModifySelectedCardItem() {

        keepAspectRatioCheckBox.setTooltip(new Tooltip("Keep Aspect Ratio of image's from the faces directory"));

        itemHeightSpinner.setValueFactory(model.getItemHeightSVF());
        itemCentreXSpinner.setValueFactory(model.getItemCentreXSVF());
        itemCentreYSpinner.setValueFactory(model.getItemCentreYSVF());

        itemHeightSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("itemHeightSpinner.valueProperty().Listener());");
            model.setCurrentH(newValue, false);
        });

        itemCentreXSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("itemCentreXSpinner.valueProperty().Listener());");
            model.setCurrentX(newValue, false);
        });

        itemCentreYSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("itemCentreYSpinner.valueProperty().Listener());");
            model.setCurrentY(newValue, false);
        });

    }


}
