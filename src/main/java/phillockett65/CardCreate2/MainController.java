/*  PTDesigner - a simple application to design a periodic table.
 *
 *  Copyright 2020 Philip Lockett.
 *
 *  This file is part of PTDesigner.
 *
 *  PTDesigner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PTDesigner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PTDesigner.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * MainController is a class that is responsible for centralizing control. It
 * creates the Model and PTable window and provides access between all the 
 * tabs via a callback mechanism.
 */
package phillockett65.CardCreate2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import phillockett65.CardCreate2.elements.ElementConfig;
import phillockett65.CardCreate2.elements.Elements;
import phillockett65.CardCreate2.table.Cell;
import phillockett65.CardCreate2.table.PTable;
import phillockett65.CardCreate2.table.Quantities;

public class MainController {

	// Details.
	@FXML private Label datDetailsElementName;
	@FXML private Label datDetailsElementSymbol;
	@FXML private Label datDetailsAtomicNumber;
	@FXML private Label datDetailsGroup;
	@FXML private Label datDetailsGroup32;
	@FXML private Label datDetailsPeriod;
	@FXML private Label datDetailsAtomicWeight;
	@FXML private Label datDetailsDensity;
	@FXML private Label datDetailsMeltingPoint;
	@FXML private Label datDetailsBoilingPoint;
	@FXML private Label lblDetailsState;
	@FXML private Label datDetailsState;
	@FXML private Label datDetailsSpecificHeat;
	@FXML private Label datDetailsElectronegativity;
	@FXML private Label datDetailsSubcategory;
	@FXML private Label datDetailsElectronShell;
	@FXML private Label datDetailsElectronSubshell;
	@FXML private Label datDetailsElectronConfiguration;

	// Layout Settings.
	@FXML private Spinner<Integer> spnLytRows;
	@FXML private Spinner<Integer> spnLytColumns;
	@FXML private Spinner<Integer> spnLytTile;
	@FXML private Spinner<Integer> spnLytBorder;
	@FXML private Spinner<Integer> spnLytTemp;
	@FXML private Button btnLytColFlip;
	@FXML private Button btnLytRowFlip;
	@FXML private Button btnLytSettings;

	// Subcategory Settings.
	private ObservableList<String> SubcategoryList = FXCollections.observableArrayList("UNDEFINED");
	@FXML private TextField txtSubSettings;
	@FXML private Button btnSubSettings;
	@FXML private ChoiceBox<String> chcSubSettings;
	@FXML private ColorPicker colSubSettings;

	// State Settings.
	private ObservableList<String> StatesList = FXCollections.observableArrayList("UNDEFINED");
	@FXML private TextField txtSttSettings;
	@FXML private Button btnSttSettings;
	@FXML private ChoiceBox<String> chcSttSettings;
	@FXML private ColorPicker colSttSettings;

	// Status.
	@FXML private Button btnStatusUpdate;
	@FXML private Label datStatusElementCount;
	@FXML private Label datStatusNeighbourCount;
	@FXML private Label datStatusElectronShell;
	@FXML private Label datStatusElectronSubshell;
	@FXML private Label datStatusElectronConfig;

	// Main
	private Model model;
	private PTable table;


	// Details.
	/**
	 * Construct a String to represent Electron Shells of the given Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Shells. 
	 */
	private String genElectronShellsString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronShellCounts().length; ++s)
			ret += String.valueOf(e.getElectronShellCounts()[s]) + ' ';

		return ret;
	}

	/**
	 * Construct a String to represent Electron Subshells of the given Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Subshells. 
	 */
	private String genElectronSubshellsString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronSubshellCounts().length; ++s)
			ret += Elements.getElectronSubshellString(s) + String.valueOf(e.getElectronSubshellCounts()[s]) + ' ';

		return ret;
	}

	/**
	 * Construct a String to represent Electron Configuration of the given 
	 * Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Configuration. 
	 */
	private String genElectronConfigurationString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronConfigurationCounts().length; ++s) {
			final int count = e.getElectronConfigurationCounts()[s];
			if (count != 0)
				ret += Elements.getElectronConfigString(s)+ String.valueOf(count) + ' ';
		}

		return ret;
	}

	/**
	 * Display the chemical elements details associated with the given cell.
	 * 
	 * @param cell details to display.
	 */
	private void updateSelectedDetails(Cell cell) {

		final ElementConfig e = cell.getE();
		datDetailsElementName.setText(e.getName());
		datDetailsElementSymbol.setText(e.getSymbol());
		datDetailsAtomicNumber.setText(String.valueOf(e.getZ()));
		datDetailsGroup.setText(String.valueOf(e.getGroup()));
		datDetailsGroup32.setText(String.valueOf(e.getGroup32()));
		datDetailsPeriod.setText(String.valueOf(e.getPeriod()));
		datDetailsAtomicWeight.setText(String.valueOf(e.getAtomicWeight()));
		datDetailsDensity.setText(String.valueOf(e.getDensity()));
		datDetailsMeltingPoint.setText(String.valueOf(e.getMelt()));
		datDetailsBoilingPoint.setText(String.valueOf(e.getBoil()));

		lblDetailsState.setText("State (at " + String.valueOf(model.getTemp()) + "K):");
		datDetailsState.setText(getStateString(cell.getE()));

		datDetailsSpecificHeat.setText(String.valueOf(e.getC()));
		datDetailsElectronegativity.setText(String.valueOf(e.getX()));
		datDetailsSubcategory.setText(model.getSubcategoryString(e.getSubcategory()));

		datDetailsElectronShell.setText(genElectronShellsString(e));
		datDetailsElectronSubshell.setText(genElectronSubshellsString(e));
		datDetailsElectronConfiguration.setText(genElectronConfigurationString(e));
	}


	/**
	 * Apply the changes selected by the user. Keeps track of which Spinners 
	 * have changed and which haven't.
	 */
	private void updateLayoutSettings() {

		final int rows = Integer.parseInt(spnLytRows.getValue().toString());
		ChangeChecker rowCkr = new ChangeChecker(model.getRows(), rows);
		if (rowCkr.isChanged())
			model.setRows(rowCkr.getNewValue());

		final int cols = Integer.parseInt(spnLytColumns.getValue().toString());
		ChangeChecker colCkr = new ChangeChecker(model.getCols(), cols);
		if (colCkr.isChanged())
			model.setCols(colCkr.getNewValue());

		final int tile = Integer.parseInt(spnLytTile.getValue().toString());
		ChangeChecker tileCkr = new ChangeChecker(model.getTileSize(), tile);
		if (tileCkr.isChanged())
			model.setTileSize(tileCkr.getNewValue());

		final int brdr = Integer.parseInt(spnLytBorder.getValue().toString());
		ChangeChecker brdrCkr = new ChangeChecker(model.getBorderSize(), brdr);
		if (brdrCkr.isChanged())
			model.setBorderSize(brdrCkr.getNewValue());

		final int temp = Integer.parseInt(spnLytTemp.getValue().toString());
		ChangeChecker tempCkr = new ChangeChecker(model.getTemp(), temp);
		if (tempCkr.isChanged()) {
			// Update model with temperature change.
			model.setTemp(tempCkr.getNewValue());

			// Update Details with temperature change.
			Cell cell = table.getCurrentCell();
			if (!cell.isBlank())
				updateSelectedDetails(cell);
		}

		// Update the PTable.
		table.updateLayout(rowCkr, colCkr, tileCkr, brdrCkr, tempCkr);
	}


	/**
	 * Apply the Subcategory changes selected by the user.
	 */
	private void updateSubcategorySettings() {
		String text = txtSubSettings.getText();
		if (text.length() < 1)
			return;

		final int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
		final Color colour = colSubSettings.getValue();
		if (model.setSubcategoryString(selected, text)) {
			model.setSubcategoryColour(selected, colour);
			table.setSubcategoryColour(selected, colour);

			SubcategoryList.set(selected, text);
			chcSubSettings.getSelectionModel().select(selected);
		}
	}


	/**
	 * Apply the State changes selected by the user.
	 */
	private void updateStateSettings() {
		String text = txtSttSettings.getText();
		if (text.length() < 1)
			return;

		final int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
		final Color colour = colSttSettings.getValue();
		if (model.setStateString(selected, text)) {
			model.setStateColour(selected, colour);
			table.setStateColour(selected, colour);

			StatesList.set(selected, text);
			chcSttSettings.getSelectionModel().select(selected);
		}
	}


	/**
	 * Apply the changes selected by the user.
	 */
	private void updateStatusSettings() {
		Quantities quantities = table.getQuantities();
		datStatusElementCount.setText(quantities.getElementCount());
		datStatusNeighbourCount.setText(quantities.getNeighbourCount());
		datStatusElectronShell.setText(quantities.getElectronShellSimilarityPercent());
		datStatusElectronSubshell.setText(quantities.getElectronSubshellSimilarityPercent());
		datStatusElectronConfig.setText(quantities.getElectronConfigSimilarityPercent());
	}


	// Event handlers.

	/**
	 * Event handler for the Layout Settings tab Flip Columns button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsFlipColumns(ActionEvent event) {
//		System.out.println("Flip Columns.");
		table.flipColumns();
	}

	/**
	 * Event handler for the Layout Settings tab Flip Rows button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsFlipRows(ActionEvent event) {
//		System.out.println("Flip Rows.");
		table.flipRows();
	}

	/**
	 * Event handler for the Apply Change button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsApplyClicked(ActionEvent event) {
//		System.out.println("Applying Change.");
		updateLayoutSettings();
		updateSubcategorySettings();
		updateStateSettings();
	}

	/**
	 * Event handler for the Status tab Update button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnStatusUpdateClicked(ActionEvent event) {
//		System.out.println("Update Status.");
		updateStatusSettings();
	}


	// Main

	/**
	 * Initialize the Subcategory ColorPicker and ChoiceBox including adding a 
	 * listener.
	 */
	private void initializeSubcategory() {
//		System.out.println("SubSettingsController initialized.");

		colSubSettings.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
		chcSubSettings.setItems(SubcategoryList);

		chcSubSettings.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
//			System.out.println("chcSettings Change.");
			txtSubSettings.setText((String)newValue);
			int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
			if (selected != -1)
				colSubSettings.setValue(model.getSubcategoryColour(selected));
		});
	}

	/**
	 * Initialize the State ColorPicker and ChoiceBox including adding a 
	 * listener.
	 */
	private void initializeState() {
//		System.out.println("SettingsController initialized.");

		colSttSettings.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
		chcSttSettings.setItems(StatesList);

		chcSttSettings.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
//			System.out.println("chcSettings Change.");
			txtSttSettings.setText((String)newValue);
			int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
			if (selected != -1)
				colSttSettings.setValue(model.getStateColour(selected));
		});
	}


	/**
	 * Initialize all the Layout Spinners.
	 */
	private void initLayoutSpinners() {
		SpinnerValueFactory<Integer> vFRows = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 45, model.getRows());
		spnLytRows.setValueFactory(vFRows);

		SpinnerValueFactory<Integer> vFColumns = new SpinnerValueFactory.IntegerSpinnerValueFactory(18, 45, model.getCols());
		spnLytColumns.setValueFactory(vFColumns);

		SpinnerValueFactory<Integer> vFTile = new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 100, model.getTileSize());
		spnLytTile.setValueFactory(vFTile);

		SpinnerValueFactory<Integer> vFBorder = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, model.getBorderSize());
		spnLytBorder.setValueFactory(vFBorder);

		SpinnerValueFactory<Integer> vFTemp = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, model.getMaxTemp(), model.getTemp());
		spnLytTemp.setValueFactory(vFTemp);
	}

	/**
	 * Initialize the Subcategory ChoiceBox with data from the Model.
	 */
	private void initSubcategoryChoiceBox() {
		int i = 0;
		SubcategoryList.clear();
		for (String item = model.getSubcategoryString(i++); item != null; item = model.getSubcategoryString(i++)) {
			SubcategoryList.add(item);
		}
		chcSubSettings.getSelectionModel().select(1);
	}

	/**
	 * Initialize the Subcategory ColorPicker with data from the Model.
	 */
	private void initSubcategoryColorPicker() {
		int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
		Color item = model.getSubcategoryColour(selected);
		if (item != null)
			colSubSettings.setValue(item);
	}

	/**
	 * Initialize the State ChoiceBox with data from the Model.
	 */
	private void initStateChoiceBox() {
		int i = 0;
		StatesList.clear();
		for (String item = model.getStateString(i++); item != null; item = model.getStateString(i++)) {
			StatesList.add(item);
		}
		chcSttSettings.getSelectionModel().select(1);
	}

	/**
	 * Initialize the State ColorPicker with data from the Model.
	 */
	private void initStateColorPicker() {
		int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
		Color item = model.getStateColour(selected);
		if (item != null)
			colSttSettings.setValue(item);
	}

	/**
	 * Called by the FXML mechanism to initialize the controller. Creates a 
	 * callback link for all the tab controllers, creates the PTable window and
	 * update the Status tab display.
	 */
	@FXML public void initialize() {
//		System.out.println("MainController initialized.");
		initializeSubcategory();
		initializeState();

		initLayoutSpinners();
		initSubcategoryChoiceBox();
		initSubcategoryColorPicker();
		initStateChoiceBox();
		initStateColorPicker();

		table = new PTable(this, "Sample");

		updateStatusSettings();
	}


	public Color getStateColour(ElementConfig e) {
		final float temp = model.getTemp();
		final int index = Model.findState(e, temp);
		Color ret = model.getStateColour(index);
		return ret;
	}

	public Color getStateColour(int index) {
		Color ret = model.getStateColour(index);
		return ret;
	}

	private String getStateString(ElementConfig e) {
		final float temp = model.getTemp();
		final int index = Model.findState(e, temp);
		String ret = model.getStateString(index);
		return ret;
	}

	public Color getSubcategoryColour(int index) {
		Color ret = model.getSubcategoryColour(index);
		return ret;
	}

	public int getRows() {
		return model.getRows();
	}

	public int getCols() {
		return model.getCols();
	}

	public int getBorderSize() {
		return model.getBorderSize();
	}

	public int getStepSize() {
		return model.getStepSize();
	}

	public int getWidth() {
		return model.getWidth();
	}

	public int getHeight() {
		return model.getHeight();
	}


	public void setSelected(Cell cell) {
		updateSelectedDetails(cell);
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
