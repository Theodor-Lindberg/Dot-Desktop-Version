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

    /**
     * Let the user choose a level from the local file system.
     *
     * @return The absolute path to the selected file.
     */
    public static String chooseLevel() {
	final int returnValue = FILE_CHOOSER.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return FILE_CHOOSER.getSelectedFile().getAbsolutePath();
	}
	return null;
    }

    /**
     * Get the file name with the file extension for levels.
     *
     * @param fileName 	The name of the file
     *
     * @return	 	The name of the file with the file extension.
     */
    public static String addFileExtension(final String fileName) {
        return (fileName.endsWith(FILTER_EXTENSION)) ? fileName : fileName + "." + FILTER_EXTENSION;
    }

    /**
     * Get the file name without the file extension.
     *
     * @param fileName 	The name of the file.
     *
     * @return 		The file name without the extension.
     */
    public static String removeFileExtension(final String fileName) {
        return fileName.replace("." + FILTER_EXTENSION, "");
    }

    /**
     * Let the user choose where to save the level.
     *
     * @return The absolute path to the selected file.
     */
    public static String saveLevelTo() {
        final int returnValue = FILE_CHOOSER.showSaveDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return addFileExtension(FILE_CHOOSER.getSelectedFile().getAbsolutePath());
	}
	return null;
    }
}
