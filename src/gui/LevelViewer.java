package gui;

import game.BlockType;
import game.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

public class LevelViewer
{
    private Level level;
    private JFrame frame;
    private final static String FRAME_TITLE;

    static {
	FRAME_TITLE = "Dot";
    }

    public LevelViewer(final Level level) {
	this.level = level;

	final Color backgroundColor = new Color(23,16,22);

	final LevelComponent levelComponent = new LevelComponent(level, getBlockColorTable(), backgroundColor);
	level.subscribeListener(levelComponent);

	frame = new JFrame(FRAME_TITLE);
	frame.setLayout(new BorderLayout());
	frame.add(levelComponent, BorderLayout.CENTER);
	frame.pack();

	frame.addWindowListener(new WindowAdapter()
	{
	    @Override public void windowClosing(WindowEvent e) {
		exit();
	    }
	});
	frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	frame.setVisible(true);
    }

    private EnumMap<BlockType, Color> getBlockColorTable() {
	EnumMap<BlockType, Color> polyColorTable = new EnumMap<>(BlockType.class);
	polyColorTable.put(BlockType.EMPTY, new Color(39, 32, 28, 255));
	return polyColorTable;
    }

    private void exit() {
	if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to close this window?", "Close Window?",
					  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
	    System.exit(0);
	}
    }
}