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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class MainController {


    /************************************************************************
     * Support code for "Playing Card Generator" panel. 
     */

    private Model model;
    private static final String TOPBARICON = "top-bar-icon";

    @FXML
    private VBox userGUI;

    @FXML
    PrimaryController primaryTabController;

    @FXML
    AdditionalController additionalTabController;

    /**
     * Constructor.
     * 
     * Responsible for creating the Model, called by the FXMLLoader().
     */
    public MainController() {
        // System.out.println("Controller constructed.");
        model = new Model();
    }

    /**
     * Called by the FXML mechanism to initialize the controller. Sets up the 
     * images on the buttons and initialises all the controls.
     */
    @FXML public void initialize() {
        // System.out.println("Controller initialized.");

        model.initialize();

        initializeTopBar();
        initializeMenu();
        initializeGenerate();
        initializeStatus();
    }

    /**
     * Called by Application after the stage has been set. Sets up the base 
     * directory (or aborts) then completes the initialization.
     */
    public void init(Stage stage) {

        userGUI.setDisable(true);
        model.setControllers(stage, this, primaryTabController, additionalTabController);

        if ((!model.isValidBaseDirectory()) &&
            (!selectValidBaseDirectory())) {
            stage.close();
        }

        model.init();

        headingLabel.setText(stage.getTitle());
    }


    /************************************************************************
     * Support code for "Top Bar" panel.
     */

    private double x = 0.0;
    private double y = 0.0;

    @FXML
    private HBox topBar;

    @FXML
    private Label headingLabel;

    @FXML
    void topBarOnMousePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void topBarOnMouseDragged(MouseEvent event) {
        model.getStage().setX(event.getScreenX() - x);
        model.getStage().setY(event.getScreenY() - y);
    }
 
 
    private Pane buildCancel() {
        final double iconSize = 32.0;
        final double cancelPadding = 0.3;

        Pane cancel = new Pane();
        cancel.setPrefWidth(iconSize);
        cancel.setPrefHeight(iconSize);
        cancel.getStyleClass().add(TOPBARICON);

        double a = iconSize * cancelPadding;
        double b = iconSize - a;
        Line line1 = new Line(a, a, b, b);
        line1.setStroke(Color.WHITE);
        line1.setStrokeWidth(4.0);
        line1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line line2 = new Line(a, b, b, a);
        line2.setStroke(Color.WHITE);
        line2.setStrokeWidth(4.0);
        line2.setStrokeLineCap(StrokeLineCap.ROUND);

        cancel.getChildren().addAll(line1, line2);

        cancel.setOnMouseClicked(event -> {
            model.close();
        });

        return cancel;
    }


    /**
     * Initialize "Top Bar" panel.
     */
    private void initializeTopBar() {
        topBar.getChildren().add(buildCancel());
    }
  

    /************************************************************************
     * Support code for Pull-down Menu structure.
     */

    @FXML
    private MenuItem fileLoadMenuItem;

    @FXML
    private void fileOpenOnAction() {
        openBaseDirectory();
    }

    @FXML
    private void fileLoadOnAction() {
        loadSettings();
    }

    @FXML
    private void fileSaveOnAction() {
        saveSettings();
    }

    @FXML
    private void fileCloseOnAction() {
        model.close();
    }


    @FXML
    private void helpAboutOnAction() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About CardCreate");
        alert.setHeaderText("CardCreate 2.0");
        alert.setContentText("CardCreate is a prototype playing card generator.");

        alert.showAndWait();
    }

    @FXML
    private void helpHelpOnAction() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("CardCreate Help");
        alert.setHeaderText("CardCreate User Guide Documentation");
        alert.setContentText("A complete guide to CardCreate is given in:\n 'Card Generator User Guide.pdf'.");

        alert.showAndWait();
    }

    public void openBaseDirectory() {
        if (!selectBaseDirectory()) {
            selectValidBaseDirectory();
        }
    }

    /**
     * Synchronise all controls with the model.
     */
    public void syncUI() {
        // System.out.println("syncUI()");
        syncFileLoadMenuItem();
    }

    public void loadSettings() {
        // System.out.println("loadSettings()");
        DataStore.readData(model);

        model.syncAllUIs();

        setStatusMessage("Settings loaded from: " + model.getSettingsFile());
    }

    public void saveSettings() {
        DataStore.writeData(model);

        model.syncAllUIs();

        setStatusMessage("Settings saved as: " + model.getSettingsFile());
    }

    /**
     * Initialize menu bar.
     */
    private void initializeMenu() {
        fileLoadMenuItem.setDisable(true);
    }



    /************************************************************************
     * Support code for "Input Directories" panel. 
     */

    /**
     * Called by init() after a valid Base Directory has initially been 
     * selected.
     */
    public void setInitialBaseDirectory() {
        // System.out.println("setInitialBaseDirectory(" + model.getBaseDirectory() + ")");
        userGUI.setDisable(false);
    }


    /**
     * Set the base directory in the model, if valid, update the 
     * primaryTabController.
     * @param base path of the directory.
     * @return true if base is a valid base directory, false otherwise.
     */
    private boolean setBaseDirectory(String base) {
        // System.out.println("setBaseDirectory(" + base + ")");

        if (!model.setBaseDirectory(base))
            return false;

        primaryTabController.setBaseDirectory();

        return true;
    }

    /**
     * Use a DirectoryChooser to select a valid base directory.
     * @return true if a valid base directory is selected, false otherwise.
     */
    private boolean selectBaseDirectory() {
        DirectoryChooser choice = new DirectoryChooser();
        choice.setInitialDirectory(new File(model.getBaseDirectory()));
        choice.setTitle("Select Base Directory");
        File directory = choice.showDialog(model.getStage());

        if (directory != null) {
            // System.out.println("Selected: " + directory.getAbsolutePath());

            if (setBaseDirectory(directory.getPath()))
                return true;
        }

        return false;
    }

    /**
     * Query user if they want to select a valid base directory.
     * @return true if a valid base directory is selected, false otherwise.
     */
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



    /************************************************************************
     * Support code for "Generate" panel. 
     */

    private ObservableList<Canvas> canvasses;
    private Generate generateTask;

    private ObservableList<Image> images;
    private Write writeTask;

    private Long progress = 0L;
 
 
    /**
     * Step 1: use the Generate task to draw all the cards onto canvasses. 
     * When completed the valueProperty changed() handler is called.
     */
    public void invokeGenerateTask() {
 
        if (generateTask != null && generateTask.isRunning())
            return;

        model.setGenerating(true);

        progress = 0L;
        showProgress();

        generateTask = new Generate(model, canvasses);
        generateTask.valueProperty().addListener( (v, oldValue, newValue) -> {
            progress = newValue;
            Platform.runLater(() -> invokeSaveTask()); 
        });
        progressBar.progressProperty().bind(generateTask.progressProperty());

        Thread th = new Thread(generateTask);
        th.setDaemon(true);
        th.start();
    }
 
    /**
     * Step 2: take snapshots of all the canvasses and produce a list of 
     * images. Must be run from the Application thread.
     */
    private void takeSnapshots() {
        final int width;
        final int height;

        if (!model.isMpcCardSize()) {
            width = (int)model.getWidth();
            height = (int)model.getHeight();
        } else {
            final double xOffset = model.getMpcBorderWidth();
            final double yOffset = model.getMpcBorderHeight();

            width = (int)(model.getWidth() + (xOffset * 2));
            height = (int)(model.getHeight() + (yOffset * 2));
        }

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        final int count = canvasses.size();
        for (int i = 0; i < count; ++i ) {
            final Canvas canvas = canvasses.remove(0);
            WritableImage snapshot = new WritableImage(width, height);
            try {
                canvas.snapshot(parameters, snapshot);
            } catch (IllegalStateException e) {
                System.out.println("takeSnapshots() - Failed to take snapshot: " + e);
            }

            images.add(snapshot);
        }

        ++progress;
    }
 
    /**
     * Step 3: takeSnapshots() then use the Write task to save all the images 
     * to disc. When completed the valueProperty changed() handler is called.
     */
    private void invokeSaveTask() {
        
        if (writeTask != null && writeTask.isRunning())
            return;

        takeSnapshots();

        Image mask = null;
        if (model.isCropCorners()) {
            final double width = model.getWidth();
            final double height = model.getHeight();
            final double arcWidth = model.getArcWidthPX();
            final double arcHeight = model.getArcHeightPX();
            
            mask = Utils.createMask(width, height, arcWidth, arcHeight);
        }
        writeTask = new Write(model, mask, progress, images);
        writeTask.valueProperty().addListener( (v, oldValue, newValue) -> Platform.runLater(() -> generationFinished()) );
        progressBar.progressProperty().bind(writeTask.progressProperty());

        Thread th = new Thread(writeTask);
        th.setDaemon(true);
        th.start();
    }
 
    /**
     * Step 4: final clean up.
     */
    private void generationFinished() {
        model.setGenerating(false);

        hideProgress();
        setStatusMessage("Output sent to: " + model.getOutputDirectory());
        images.clear();
    }
 
    /**
     * Initialize"Generate" panel.
    */
    private void initializeGenerate() {
        canvasses = FXCollections.observableArrayList();
        images = FXCollections.observableArrayList();
    }
 

    /************************************************************************
     * Support code for "Output Directory" panel. 
     */

    public void syncFileLoadMenuItem() {
        fileLoadMenuItem.setDisable(!model.isSettingsFileExist());
    }

    /************************************************************************
     * Support code for "Status" panel. 
     */

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar progressBar;


    /**
     * Update the status line message.
     * @param Message to display on the status line.
     */
    public void setStatusMessage(String Message) {
        statusLabel.setText(Message);
    }


    /**
     * Show the progress bar, but also hide the status and disable the 
     * generate button.
     */
    private void showProgress() {
        primaryTabController.disableGenerateButton(true);
        statusLabel.setVisible(false);
        progressBar.setVisible(true);
    }

    /**
     * Hide the progress bar, but also show the status and enable the generate 
     * button.
     */
    private void hideProgress() {
        primaryTabController.disableGenerateButton(false);
        statusLabel.setVisible(true);
        progressBar.setVisible(false);
    }

    /**
     * Initialize "Status" panel.
     */
    private void initializeStatus() {
        setStatusMessage("Ready.");

        hideProgress();
    }

}
