package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

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

    public static String saveLevelTo() {
        final int returnValue = FILE_CHOOSER.showSaveDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    String fileName = FILE_CHOOSER.getSelectedFile().getAbsolutePath();
	    if (!fileName.contains("." + FILTER_EXTENSION)){
	        fileName += "." + FILTER_EXTENSION;
	    }
	    return fileName;
	}
	return null;
    }
}
