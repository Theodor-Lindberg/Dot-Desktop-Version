package gui;

import game.BlockType;
import game.Direction;
import game.Level;
import game.Player;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

public class LevelViewer
{
    private Level level;
    private JFrame frame;
    private final static String FRAME_TITLE;
    private Timer gameTimer;

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

	addKeyboardEvents();

	frame.setVisible(true);

	initializeGameLoop();
    }

    private EnumMap<BlockType, Color> getBlockColorTable() {
	EnumMap<BlockType, Color> polyColorTable = new EnumMap<>(BlockType.class);
	polyColorTable.put(BlockType.EMPTY, new Color(39, 32, 28, 255));
	polyColorTable.put(BlockType.WALL, new Color(125, 228, 190, 255));
	polyColorTable.put(BlockType.PLAYER, new Color(237, 228, 228, 255));
	return polyColorTable;
    }

    private void addKeyboardEvents() {
	JComponent pane = frame.getRootPane();

	final InputMap in = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	final ActionMap act = pane.getActionMap();

	addEvent(in, act, KeyEvent.VK_LEFT, false, "MoveLeft", Direction.LEFT);
	addEvent(in, act, KeyEvent.VK_LEFT, true, "ReleaseLeft", Direction.LEFT);

	addEvent(in, act, KeyEvent.VK_RIGHT, false, "MoveRight", Direction.RIGHT);
	addEvent(in, act, KeyEvent.VK_RIGHT, true, "ReleaseRight", Direction.RIGHT);

	addEvent(in, act, KeyEvent.VK_UP, false, "MoveUp", Direction.UP);
	addEvent(in, act, KeyEvent.VK_UP, true, "ReleaseUp", Direction.UP);

	addEvent(in, act, KeyEvent.VK_DOWN, false, "MoveDown", Direction.DOWN);
	addEvent(in, act, KeyEvent.VK_DOWN, true, "ReleaseDown", Direction.DOWN);
    }

    private void addEvent(final InputMap in, final ActionMap act, final int key, final boolean onKeyRelease, final String name, final Direction direction) {
	in.put(KeyStroke.getKeyStroke(key, 0, onKeyRelease), name);
	act.put(name, new MotionAction(name, direction, onKeyRelease));
    }

    private class MotionAction extends AbstractAction implements ActionListener
    {
        private final Direction direction;
        private final boolean onKeyRelease;

        public MotionAction(String name, final Direction direction, final boolean onKeyRelease)
        {
            super(name);

            this.direction = direction;
            this.onKeyRelease = onKeyRelease;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!onKeyRelease)
            	level.movePlayer(direction);
            else
                level.removeDirection(direction);
        }
    }

    private void exit() {
	if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to close this window?", "Close Window?",
					  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
	    System.exit(0);
	}
    }

    private void initializeGameLoop() {
	final Action gameTick = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e) {
		level.tick();
	    }
	};

	gameTimer = new Timer(16, gameTick);
	gameTimer.setCoalesce(true);
	gameTimer.start();
    }

    public static void main(String[] args) {
	new LevelViewer(new Level(50,50));
    }
}
