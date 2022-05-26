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
 * SettingsController is a class that is responsible for the controls of the 
 * additional settings window.
 */
package phillockett65.CardCreate2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import phillockett65.CardCreate2.sample.CardSample;

public class SettingsController {


    /************************************************************************
     * Support code for "Settings" panel. 
     */

    private Stage stage;
    private Model model;
    private CardSample sample;

    @FXML
    private BorderPane settingsGUI;

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
//		System.out.println("Controller initialized.");
    }

    /**
     * Called by Controller after the stage has been set. Sets up the base 
     * directory (or aborts) then completes the initialization.
     */
    public void init(Stage stage, Model model, CardSample sample) {
        this.stage = stage;
        this.model = model;
        this.sample = sample;
        model.setSettingsWindowLaunched(true);

        initializeCardCorners();
        initializeDisplayWatermark();
        initializeModifySelectedCardItem();
        initializeCardItemPriority();
        initializeJokers();
        initializeStatus();
    }



    /************************************************************************
     * Support code for "Card Corners and Cropping" panel. 
     */

    @FXML
    private CheckBox independentRadiiCheckBox;

    @FXML
    private CheckBox cropCornersCheckBox;

    @FXML
    private Label arcWidthLabel;

    @FXML
    private Label arcHeightLabel;

    @FXML
    private Spinner<Double> arcWidthSpinner;

    @FXML
    private Spinner<Double> arcHeightSpinner;

    @FXML
    private Button arcWidthButton;

    @FXML
    private Button arcHeightButton;

    @FXML
    void independentRadiiCheckBoxActionPerformed(ActionEvent event) {
        final boolean indy = independentRadiiCheckBox.isSelected();

        model.setCornerRadiiIndependently(indy);

        setArcLabelsAndToolsTips(indy);

        sample.syncCardSize();
    }

    @FXML
    void arcWidthButtonActionPerformed(ActionEvent event) {
        model.resetArcWidthSVF();
        sample.syncCardSize();
    }

    @FXML
    void arcHeightButtonActionPerformed(ActionEvent event) {
        model.resetArcHeightSVF();
        sample.syncCardSize();
    }

    @FXML
    void cropCornersCheckBoxActionPerformed(ActionEvent event) {
        final boolean crop = cropCornersCheckBox.isSelected();

        model.setCropCorners(crop);
    }

    private void setArcLabelsAndToolsTips(boolean indy) {

        if (indy) {
            arcHeightLabel.setText("Corner Radius Arc Height (%)");
            arcHeightLabel.setTooltip(new Tooltip("Card arc height radius as a percentage of card height (default: 10)"));
            arcHeightSpinner.setTooltip(new Tooltip("Set card arc height corner radius as a percentage of card height"));
            arcHeightButton.setTooltip(new Tooltip("Reset arc corner radius height to default value of 10 percent"));
        } else {
            arcHeightLabel.setText("Corner Radius (%)");
            arcHeightLabel.setTooltip(new Tooltip("Corner radius as a percentage of card height (default: 10)"));
            arcHeightSpinner.setTooltip(new Tooltip("Set corner radius as a percentage of card height"));
            arcHeightButton.setTooltip(new Tooltip("Reset corner radius to default value of 10 percent"));
        }

        arcWidthLabel.setDisable(!indy);
        arcWidthSpinner.setDisable(!indy);
        arcWidthButton.setDisable(!indy);

    }
    /**
     * Initialize "Card Corners" panel.
     */
    private void initializeCardCorners() {
        final boolean indy = model.isSetCornerRadiiIndependently();
        independentRadiiCheckBox.setSelected(indy);
        independentRadiiCheckBox.setTooltip(new Tooltip("Independently set card corner radius width and height"));

        arcWidthLabel.setTooltip(new Tooltip("Card arc width radius as a percentage of card width (default: 10)"));
        arcWidthSpinner.setTooltip(new Tooltip("Set card arc width corner radius as a percentage of card width"));
        arcWidthButton.setTooltip(new Tooltip("Reset arc width corner radius to default value of 10 percent"));

        arcWidthSpinner.setValueFactory(model.getArcWidthSVF());
        arcHeightSpinner.setValueFactory(model.getArcHeightSVF());

        setArcLabelsAndToolsTips(indy);

        arcWidthSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setArcWidth(newValue);
            sample.syncCardSize();
        });

        arcHeightSpinner.valueProperty().addListener( (v, oldValue, newValue) -> {
            model.setArcHeight(newValue);
            sample.syncCardSize();
        });

        cropCornersCheckBox.setSelected(model.isCropCorners());
        cropCornersCheckBox.setTooltip(new Tooltip("Crop the card corners, useful when the image overruns at the corners"));
    }



    /************************************************************************
     * Support code for "Display Watermark" panel. 
     */

    @FXML
    private CheckBox courtWatermarkCheckBox;

    @FXML
    private CheckBox imageWatermarkCheckBox;

    @FXML
    private CheckBox numberWatermarkCheckBox;

    @FXML
    void courtWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayCourtWatermark(courtWatermarkCheckBox.isSelected());
    }

    @FXML
    void imageWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayImageWatermark(imageWatermarkCheckBox.isSelected());
    }

    @FXML
    void numberWatermarkCheckBoxActionPerformed(ActionEvent event) {
        model.setDisplayNumberWatermark(numberWatermarkCheckBox.isSelected());
    }

    /**
     * Initialize "Display Watermark" panel.
     */
    private void initializeDisplayWatermark() {
        courtWatermarkCheckBox.setSelected(model.isDisplayCourtWatermark());
        imageWatermarkCheckBox.setSelected(model.isDisplayImageWatermark());
        numberWatermarkCheckBox.setSelected(model.isDisplayNumberWatermark());

        courtWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on Court cards"));
        imageWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on cards with images"));
        numberWatermarkCheckBox.setTooltip(new Tooltip("Display Watermark on Aces to 10 cards"));
    }



    /************************************************************************
     * Support code for "Modify Selected Card Item" panel. 
     */

    @FXML
    private CheckBox lockXCheckBox;

    @FXML
    private CheckBox lockYCheckBox;

    @FXML
    private CheckBox leftHandedCheckBox;

    @FXML
    private CheckBox showGuideBoxCheckBox;

    @FXML
    void lockXCheckBoxActionPerformed(ActionEvent event) {
        model.setLockX(lockXCheckBox.isSelected());
    }

    @FXML
    void lockYCheckBoxActionPerformed(ActionEvent event) {
        model.setLockY(lockYCheckBox.isSelected());
    }

    @FXML
    void leftHandedCheckBoxActionPerformed(ActionEvent event) {
        model.setLeftHanded(leftHandedCheckBox.isSelected());
    }

    @FXML
    void showGuideBoxCheckBoxActionPerformed(ActionEvent event) {
        model.setShowGuideBox(showGuideBoxCheckBox.isSelected());
    }

    /**
     * Initialize "Modify Selected Card Item" panel.
     */
    private void initializeModifySelectedCardItem() {
        lockXCheckBox.setSelected(model.isLockX());
        lockYCheckBox.setSelected(model.isLockY());
        leftHandedCheckBox.setSelected(model.isLeftHanded());
        showGuideBoxCheckBox.setSelected(model.isShowGuideBox());

        lockXCheckBox.setTooltip(new Tooltip("Lock X coordinate of Index and Corner pip together"));
        lockYCheckBox.setTooltip(new Tooltip("Lock Y separation of Index and Corner pip"));
        leftHandedCheckBox.setTooltip(new Tooltip("Show Indices and Corner pips in all four corners"));
        showGuideBoxCheckBox.setTooltip(new Tooltip("Display guide box to aid Card Item positioning"));
    }



    /************************************************************************
     * Support code for "Card Item Priority" panel. 
     */

    @FXML
    private ListView<String> cardItemListView;
    
    @FXML
    private Button upItemButton;

    @FXML
    private Button downItemButton;

    @FXML
    private Button priorityResetButton;

    @FXML
    public void upItemButtonActionPerformed(ActionEvent event) {
        int index = model.moveSelectedCardItemUp();
        cardItemListView.getSelectionModel().select(index);
    }

    @FXML
    public void downItemButtonActionPerformed(ActionEvent event) {
        int index = model.moveSelectedCardItemDown();
        cardItemListView.getSelectionModel().select(index);
    }

    @FXML
    public void priorityResetButtonActionPerformed(ActionEvent event) {
        model.resetPriorityList();
        cardItemListView.getSelectionModel().select(-1);
    }

    /**
     * Initialize "Card Item Priority" panel.
     */
    private void initializeCardItemPriority() {
        cardItemListView.setItems(model.getCardItemList());
        cardItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.setSelectedCardItem(newValue);

                upItemButton.setDisable(!model.isUpAvailable());
                downItemButton.setDisable(!model.isDownAvailable());
            }
        } );

        cardItemListView.setTooltip(new Tooltip("Use Up and Down buttons to change order of Card Items"));
        upItemButton.setTooltip(new Tooltip("Increase priority of selected Card Items"));
        downItemButton.setTooltip(new Tooltip("Decrease priority of selected Card Items"));
        priorityResetButton.setTooltip(new Tooltip("Reset selected Card Items priorities"));

        upItemButton.setDisable(true);
        downItemButton.setDisable(true);
    }



    /************************************************************************
     * Support code for "Jokers" panel. 
     */

    @FXML
    private CheckBox borderlessCheckBox;

    @FXML
    void borderlessCheckBoxActionPerformed(ActionEvent event) {
        model.setBorderlessJokers(borderlessCheckBox.isSelected());
    }

    /**
     * Initialize "Jokers" panel.
     */
    private void initializeJokers() {
        borderlessCheckBox.setSelected(model.isBorderlessJokers());

        borderlessCheckBox.setTooltip(new Tooltip("Don't use borders on Joker images"));
    }

    /************************************************************************
     * Support code for "Status" panel. 
     */

    @FXML
    private Button closeSettingsButton;

    @FXML
    void closeSettingsButtonActionPerformed(ActionEvent event) {
        model.setSettingsWindowLaunched(false);
        stage.close();
    }

    /**
     * Initialize "Status" panel.
     */
    private void initializeStatus() {
        closeSettingsButton.setTooltip(new Tooltip("Close Additional Configuraton window"));
    }

}
