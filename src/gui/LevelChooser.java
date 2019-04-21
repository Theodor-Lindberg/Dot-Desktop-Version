package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LevelChooser
{
    private final static JFileChooser levelChooser;
    private final static String filterDescription;
    private final static String filterExtension;

    static {
	levelChooser = new JFileChooser();
	filterDescription = "Level";
	filterExtension = "json";
	final FileNameExtensionFilter filter = new FileNameExtensionFilter(filterDescription, filterExtension);
	levelChooser.setFileFilter(filter);
    }

    private LevelChooser() {

    }

    public static String chooseLevel() {
	final int returnValue = levelChooser.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    return levelChooser.getSelectedFile().getAbsolutePath();
	}
	return null;
    }

    public static String saveLevelTo() {
        final int returnValue = levelChooser.showSaveDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    String fileName = levelChooser.getSelectedFile().getAbsolutePath();
	    if (!fileName.contains("." + filterExtension)){
	        fileName += "." + filterExtension;
	    }
	    return fileName;
	}
	return null;
    }
}
