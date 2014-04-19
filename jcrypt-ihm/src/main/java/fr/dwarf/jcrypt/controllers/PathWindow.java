package fr.dwarf.jcrypt.controllers;

import java.io.File;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TextInput;

import fr.dwarf.jcrypt.models.MainWindowModel;

public class PathWindow extends Sheet implements Bindable {

    /**
     * Bouton de sélection dossier de destination.
     */
    @BXML
    private PushButton pushButton;

    /**
     * Répertoire de destination.
     */
    @BXML
    private TextInput outputDirectory;

    /**
     * Largeur file browser
     */
    private final static Integer FILE_BROWSER_WIDTH = 400;

    /**
     * Hauteur file browser
     */
    private final static Integer FILE_BROWSER_HEIGHT = 372;

    /**
     * Modèle.
     */
    private MainWindowModel model;

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
	initFilePicker();

    }

    /**
     * Initialisation du listener du filepicker.
     */
    private void initFilePicker() {
	pushButton.getButtonPressListeners().add(new ButtonPressListener() {
	    @Override
	    public void buttonPressed(Button button) {

		final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();

		fileBrowserSheet.setMinimumHeight(FILE_BROWSER_HEIGHT);
		fileBrowserSheet.setMinimumWidth(FILE_BROWSER_WIDTH);
		fileBrowserSheet.setMaximumHeight(FILE_BROWSER_HEIGHT);
		fileBrowserSheet.setMaximumWidth(FILE_BROWSER_WIDTH);

		fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_TO);
		fileBrowserSheet.open(PathWindow.this, new SheetCloseListener() {
		    @Override
		    public void sheetClosed(Sheet sheet) {
			if (sheet.getResult()) {

			    // Récupération des fichiers sélectionnés
			    Sequence<File> selectedFiles = fileBrowserSheet.getSelectedFiles();

			    // Affectation du premier fichier
			    // selectionné
			    if (selectedFiles.getLength() == 1) {
				model.setDossierOut(selectedFiles.get(0).toPath());
				outputDirectory.setText(model.getDossierOut().toString());
				PathWindow.this.close();
			    }
			}
		    }
		});
	    }
	});
    }

    /**
     * @return the model
     */
    public MainWindowModel getModel() {
	return model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(MainWindowModel model) {
	this.model = model;
    }

    /**
     * @return the outputDirectory
     */
    public TextInput getOutputDirectory() {
	return outputDirectory;
    }

    /**
     * @param outputDirectory
     *            the outputDirectory to set
     */
    public void setOutputDirectory(TextInput outputDirectory) {
	this.outputDirectory = outputDirectory;
    }

}
