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
 * PrimaryController is a class that is responsible for centralizing control. It
 * creates the Model and CardSample window and provides a callback mechanism.
 */
package phillockett65.CardCreate2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

import phillockett65.CardCreate2.sample.Item;

public class PrimaryController {


    /************************************************************************
     * Support code for "Playing Card Generator" panel. 
     */

    private Model model;


    /**
     * Constructor.
     * 
     * Responsible for creating the Model, called by the FXMLLoader().
     */
    public PrimaryController() {
        // System.out.println("Controller constructed.");
    }

    private void setUpImageButton(Button button, String imageFileName)
    {
        Image image = new Image(getClass().getResourceAsStream(imageFileName));
        ImageView view = new ImageView(image);

        button.setGraphic(view);
        button.setText(null);
    }

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
        // System.out.println("Controller initialized.");

        /**
         * Initialize "Playing Card Generator" panel.
         */

        setUpImageButton(generateButton, "icon-play.png");
        setUpImageButton(previousSuitButton, "icon-up.png");
        setUpImageButton(previousCardButton, "icon-left.png");
        setUpImageButton(nextCardButton, "icon-right.png");
        setUpImageButton(nextSuitButton, "icon-down.png");

        initializeInputDirectories();
        initializeGenerate();
        initializeOutputDirectory();
        initializeSampleNavigation();
        initializeDisplayCardItems();
        initializeSelectCardItem();
    }

    /**
     * Called by Application after the stage has been set. Sets up the base 
     * directory (or aborts) then completes the initialization.
     */
    public void init(Model model) {
        this.model = model;

        initializeCardSize();
        initializeBackgroundColour();
        initializeModifySelectedCardItem();

        setInitialBaseDirectory();
        setCurrentCardItemLabelAndTooltips();
        initInputDirectoryChoiceBoxHandlers();
        setCardItemRadioState();
    }



    /************************************************************************
     * Support code for "Playing Card Generator" Menu. 
     */

    /**
     * Synchronise all controls with the model.
     */
    public void syncUI() {
        // System.out.println("syncUI()");

        loadButton.setDisable(!model.isSettingsFileExist());

        faceChoiceBox.setValue(model.getFaceStyle());
        indexChoiceBox.setValue(model.getIndexStyle());
        pipChoiceBox.setValue(model.getPipStyle());

        outputToggleButton.setSelected(model.isManual());
        syncOutputTextField();

        setCardSizeRadioState();

        colourTextField.setText(model.getBackgroundColourString());
        colourPicker.setValue(model.getBackgroundColour());

        indicesCheckBox.setSelected(model.isDisplayIndex());
        cornerPipCheckBox.setSelected(model.isDisplayCornerPip());
        standardPipCheckBox.setSelected(model.isDisplayStandardPip());
        faceCheckBox.setSelected(model.isDisplayFaceImage());
        facePipCheckBox.setSelected(model.isDisplayFacePip());

        setCardItemRadioState();
    }




    /************************************************************************
     * Support code for "Sample" panel interface. 
     */

    /**
     * Called by Sample when the mouse changes the selected Item.
     */
    public void syncToCurrentCardItem() {
        setSelectCardItemPrompts();
        setSelectedCardItemRadioToCurrent();
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
    private Button loadButton;

    @FXML
    private Button saveButton;

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
        // System.out.println("setInitialBaseDirectory(" + model.getBaseDirectory() + ")");

        baseDirectoryComboBox.setItems(model.getBaseList());
        baseDirectoryComboBox.setValue(model.getBaseDirectory());

        faceChoiceBox.setItems(model.getFaceList());
        faceChoiceBox.setValue(model.getFaceStyle());

        indexChoiceBox.setItems(model.getIndexList());
        indexChoiceBox.setValue(model.getIndexStyle());

        pipChoiceBox.setItems(model.getPipList());
        pipChoiceBox.setValue(model.getPipStyle());

        syncOutputTextField();

        model.getMainController().setInitialBaseDirectory();
    }


    public boolean setBaseDirectory(String base) {
        // System.out.println("setBaseDirectory(" + base + ")");

        baseDirectoryComboBox.setValue(model.getBaseDirectory());
        faceChoiceBox.setValue(model.getFaceStyle());
        indexChoiceBox.setValue(model.getIndexStyle());
        pipChoiceBox.setValue(model.getPipStyle());

        syncOutputTextField();

        return true;
    }




    @FXML
    void baseDirectoryButtonActionPerformed(ActionEvent event) {
        // System.out.println("baseDirectoryButtonActionPerformed()");

        model.getMainController().openBaseDirectory();
    }

    @FXML
    void loadButtonActionPerformed(ActionEvent event) {
        // System.out.println("loadButtonActionPerformed()");
        model.getMainController().loadSettings();
    }

    @FXML
    void saveButtonActionPerformed(ActionEvent event) {
        // System.out.println("saveButtonActionPerformed()");
        model.getMainController().saveSettings();
    }

    @FXML
    void baseDirectoryComboBoxActionPerformed(ActionEvent event) {
        // System.out.println("baseDirectoryComboBoxActionPerformed()" + event.toString());

        if (!model.setBaseDirectory(baseDirectoryComboBox.getValue()))
            return;

        faceChoiceBox.setValue(model.getFaceStyle());
        indexChoiceBox.setValue(model.getIndexStyle());
        pipChoiceBox.setValue(model.getPipStyle());

        syncOutputTextField();
    }

    /**
     * Initialize "Input Directories" panel.
     */
    private void initializeInputDirectories() {

        baseDirectoryLabel.setTooltip(new Tooltip("Working directory that contains faces, indices and pips directories"));
        baseDirectoryComboBox.setTooltip(new Tooltip("Select the Base Directory"));
        baseDirectoryButton.setTooltip(new Tooltip("Browse to the Base Directory"));
        loadButton.setTooltip(new Tooltip("Load previously saved Settings"));
        saveButton.setTooltip(new Tooltip("Save current Settings to the Output Directory"));

        faceLabel.setTooltip(new Tooltip("Subdirectory of face image files to use (default: \"1\")"));
        indexLabel.setTooltip(new Tooltip("Subdirectory of index image files to use (default: \"1\")"));
        pipLabel.setTooltip(new Tooltip("Subdirectory of pip image files to use (default: \"1\")"));
        faceChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
        indexChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
        pipChoiceBox.setTooltip(new Tooltip("Requires the Base Directory to be correctly set"));
    }

    /**
     * This has to be done after the base directory has been set up. This sets 
     * up the change handlers for the Face, Index and Pip style choice boxes.
     */
    private void initInputDirectoryChoiceBoxHandlers() {
        faceChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setFaceStyle(newValue);
            syncOutputTextField();
            setCardItemRadioState();
        });

        indexChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setIndexStyle(newValue);
            setCardItemRadioState();
        });

        pipChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            model.setPipStyle(newValue);
            setCardItemRadioState();
        });
    }



    /************************************************************************
     * Support code for "Generate" panel. 
     */

    @FXML
    private Button generateButton;

    @FXML
    void generateButtonActionPerformed(ActionEvent event) {
        model.getMainController().invokeGenerateTask();
    }

    public void disableGenerateButton(boolean state) {
        generateButton.setDisable(state);
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
        // System.out.println("outputjTextFieldKeyTyped()" + event.toString());
        model.setOutputName(outputTextField.getText());
        model.getMainController().syncFileLoadMenuItem();
        loadButton.setDisable(!model.isSettingsFileExist());
    }

    @FXML
    void outputToggleButtonActionPerformed(ActionEvent event) {
        // System.out.println("outputjToggleButtonActionPerformed()" + event.toString());

        final boolean manual = outputToggleButton.isSelected(); 

        outputTextField.setEditable(manual);
        model.setOutputNameManually(manual);
        syncOutputTextField();
    }

    private void syncOutputTextField() {
        outputTextField.setText(model.getOutputName());
        model.getMainController().syncFileLoadMenuItem();
        loadButton.setDisable(!model.isSettingsFileExist());
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
        setCardItemRadioState();
    }

    @FXML
    void previousSuitButtonActionPerformed(ActionEvent event) {
        model.prevSuit();
        setCardItemRadioState();
    }

    @FXML
    void nextCardButtonActionPerformed(ActionEvent event) {
        model.nextCard();
        setCardItemRadioState();
    }

    @FXML
    void nextSuitButtonActionPerformed(ActionEvent event) {
        model.nextSuit();
        setCardItemRadioState();
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
    private RadioButton mpcRadioButton;

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
            model.setPokerCardSize();
        }
        else
        if (bridgeRadioButton.isSelected()) {
            model.setBridgeCardSize();
        }
        else
        if (freeRadioButton.isSelected()) {
            model.setFreeCardSize();
        }
        else
        if (mpcRadioButton.isSelected()) {
            model.setMpcCardSize();
        }

        // Control whether Card Width and Height are changeble.
        final boolean autoWidth = model.isAutoCardWidth();
        widthLabel.setDisable(autoWidth);
        widthSpinner.setDisable(autoWidth);
        widthButton.setDisable(autoWidth);

        final boolean autoHeight = model.isAutoCardHeight();
        heightLabel.setDisable(autoHeight);
        heightSpinner.setDisable(autoHeight);
        heightButton.setDisable(autoHeight);
    }

    @FXML
    void widthButtonActionPerformed(ActionEvent event) {
        model.resetCardWidthSVF();
    }

    @FXML
    void heightButtonActionPerformed(ActionEvent event) {
        model.resetCardHeightSVF();
    }

    @FXML
    void radiusButtonActionPerformed(ActionEvent event) {
        model.resetRadiusSVF();
    }

    private void setCardSizeRadioState() {
        pokerRadioButton.setSelected(model.isPokerCardSize());
        bridgeRadioButton.setSelected(model.isBridgeCardSize());
        freeRadioButton.setSelected(model.isFreeCardSize());
        mpcRadioButton.setSelected(model.isMpcCardSize());
    }

    /**
     * Initialize "Card Size" panel.
     */
    private void initializeCardSize() {

        pokerRadioButton.setTooltip(new Tooltip("Maintain poker card aspect ratio"));
        bridgeRadioButton.setTooltip(new Tooltip("Maintain bridge card aspect ratio"));
        freeRadioButton.setTooltip(new Tooltip("independently set card width and height"));
        mpcRadioButton.setTooltip(new Tooltip("Generate poker cards suitable for use at makeplayingcards.com"));

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
        });

        heightSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setHeight(newValue);
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
        model.getSample().syncBackgroundColour();
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
    void indicesCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayIndex(indicesCheckBox.isSelected());
        setCardItemRadioState();
    }

    @FXML
    void cornerPipCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayCornerPip(cornerPipCheckBox.isSelected());
        setCardItemRadioState();
    }

    @FXML
    void standardPipCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayStandardPip(standardPipCheckBox.isSelected());
        setCardItemRadioState();
    }

    @FXML
    void faceCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayFaceImage(faceCheckBox.isSelected());
        setCardItemRadioState();
    }

    @FXML
    void facePipCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayFacePip(facePipCheckBox.isSelected());
        setCardItemRadioState();
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
        // System.out.println("cardItemRadioButtonActionPerformed()");

        if (indicesRadioButton.isSelected())
            model.setCurrentCardItemToIndex();
        else
        if (cornerPipRadioButton.isSelected())
            model.setCurrentCardItemToCornerPip();
        else
        if (standardPipRadioButton.isSelected())
            model.setCurrentCardItemToStandardPip();
        else
        if (faceRadioButton.isSelected())
            model.setCurrentCardItemToFace();
        else
        if (facePipRadioButton.isSelected())
            model.setCurrentCardItemToFacePip();

        setSelectCardItemPrompts();
    }

    /**
     * Set the "Select Card Item" radio button to the current card item .
     */
    private void setSelectedCardItemRadioToCurrent() {
        Item item = model.getCurrentItem();
        // System.out.println("setSelectedCardItemRadioToCurrent(" + item + ")");

        if (item == Item.INDEX)
            indicesRadioButton.setSelected(true);
        else
        if (item == Item.CORNER_PIP)
            cornerPipRadioButton.setSelected(true);
        else
        if (item == Item.STANDARD_PIP)
            standardPipRadioButton.setSelected(true);
        else
        if (item == Item.FACE)
            faceRadioButton.setSelected(true);
        else
        if (item == Item.FACE_PIP)
            facePipRadioButton.setSelected(true);
    }

    /**
     * Set the "Select Card Item" radio button Disabled state based on the 
     * current card.
     */
    private void setCardItemRadioState() {
        indicesRadioButton.setDisable(!model.shouldIndexBeDisplayed());
        cornerPipRadioButton.setDisable(!model.shouldCornerPipBeDisplayed());
        standardPipRadioButton.setDisable(!model.shouldStandardPipBeDisplayed());
        faceRadioButton.setDisable(!model.shouldFaceImageBeDisplayed());
        facePipRadioButton.setDisable(!model.shouldFacePipBeDisplayed());

        setSelectCardItemPrompts();
        setSelectedCardItemRadioToCurrent();
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
     * Fix the disabled state of the "Modify Card Item" controls based on the 
     * card items being displayed.
     */
    private void setSelectCardItemPrompts() {
        // System.out.println("setSelectCardItemPrompts()");

        setDisabledStateOfCurrentCardItem();

        setCurrentCardItemLabelAndTooltips();
    }

    /**
     * Fix the disable state of the "Modify Card Item" controls.
     */
    private void setDisabledStateOfCurrentCardItem() {
        // System.out.println("setDisabledStateOfCurrentCardItem()");

        boolean disabled = !model.isCurrentHeightChangable();
        itemHeightButton.setDisable(disabled);
        itemHeightSpinner.setDisable(disabled);
        itemHeightLabel.setDisable(disabled);

        disabled = !model.isCurrentXCentreChangable();
        itemCentreXButton.setDisable(disabled);
        itemCentreXSpinner.setDisable(disabled);
        itemCentreXLabel.setDisable(disabled);

        disabled = !model.isCurrentYCentreChangable();
        itemCentreYButton.setDisable(disabled);
        itemCentreYSpinner.setDisable(disabled);
        itemCentreYLabel.setDisable(disabled);
    }
        
    /**
     * Sets the label text and tool tips for the current card item on the 
     * "Modify Card Item" controls.
     */
    private void setCurrentCardItemLabelAndTooltips() {
        // System.out.println("setCurrentCardItemLabelAndTooltips()");

        itemHeightLabel.setText(model.getCurrentHLabel());
        itemCentreXLabel.setText(model.getCurrentXLabel());
        itemCentreYLabel.setText(model.getCurrentYLabel());

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
        model.setKeepImageAspectRatio(keepAspectRatioCheckBox.isSelected());
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
            model.setCurrentH(newValue);
        });

        itemCentreXSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("itemCentreXSpinner.valueProperty().Listener());");
            model.setCurrentX(newValue);
        });

        itemCentreYSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            // System.out.println("itemCentreYSpinner.valueProperty().Listener());");
            model.setCurrentY(newValue);
        });

    }

}
