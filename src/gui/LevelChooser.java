package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Lets the user choose which level to load or where to save it.
 */
public final class LevelChooser
{
    private final static JFileChooser FILE_CHOOSER;
    private final static String FILTER_DESCRIPTION;
    private final static String FILTER_EXTENSION;

    static {
	FILE_CHOOSER = new JFileChooser();
	FILTER_DESCRIPTION = "Level";
	FILTER_EXTENSION = "json";
	final FileNameExtensionFilter filter = new FileNameExtensionFilter(FILTER_DESCRIPTION, FILTER_EXTENSION);
	FILE_CHOOSER.setFileFilter(filter);
	FILE_CHOOSER.setCurrentDirectory(new File("resources/levels"));
    }

    private LevelChooser() {

    }

    public static String chooseLevel() {
	final int returnValue = FILE_CHOOSER.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return FILE_CHOOSER.getSelectedFile().getAbsolutePath();
	}
	return null;
    }

    public static String addFileExtension(final String fileName) {
        return (fileName.endsWith(FILTER_EXTENSION)) ? fileName : fileName + "." + FILTER_EXTENSION;
    }

    public static String removeFileExtension(final String fileName) {
        return fileName.replace("." + FILTER_EXTENSION, "");
    }

    public static String saveLevelTo() {
        final int returnValue = FILE_CHOOSER.showSaveDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return addFileExtension(FILE_CHOOSER.getSelectedFile().getAbsolutePath());
	}
	return null;
    }
}
