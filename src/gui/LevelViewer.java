package gui;

import game.BlockType;
import game.Direction;
import game.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

public class LevelViewer
{
    private Level level;
    private JFrame frame;
    private final static String FRAME_TITLE;
    private Timer gameTimer;
    private Keyboard keyboard;

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

	//addKeyboardEvents();
	keyboard = new Keyboard(level);
	frame.addKeyListener(keyboard);

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

    private void exit() {
	if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to close this window?", "Close Window?",
					  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
	    System.exit(0);
	}
    }

    private void addKeyboardEvents() {
 	JComponent pane = frame.getRootPane();

 	final InputMap in = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
 	in.put(KeyStroke.getKeyStroke("LEFT"), "MoveLeft");
 	in.put(KeyStroke.getKeyStroke("RIGHT"), "MoveRight");
 	in.put(KeyStroke.getKeyStroke("UP"), "MoveUp");
 	in.put(KeyStroke.getKeyStroke("DOWN"), "MoveDown");
 	in.put(KeyStroke.getKeyStroke("SPACE"), "TogglePause");

 	final ActionMap act = pane.getActionMap();
 	act.put("MoveLeft", new MoveLeft());
 	act.put("MoveRight", new MoveRight());
 	act.put("MoveUp", new MoveUp());
 	act.put("MoveDown", new MoveDown());
 	act.put("TogglePause", new ToggleGamePause());
     }

     private class MoveLeft extends AbstractAction {
 	@Override public void actionPerformed(final ActionEvent e) {
 	    level.movePlayer(Direction.LEFT);
 	}
     }

     private class MoveRight extends AbstractAction {
             @Override public void actionPerformed(final ActionEvent e) {
		 level.movePlayer(Direction.RIGHT);
             }
         }

     private class MoveUp extends AbstractAction {
 	@Override public void actionPerformed(final ActionEvent e) {
	    level.movePlayer(Direction.UP);
 	}
     }

     private class MoveDown extends AbstractAction {
 	@Override public void actionPerformed(final ActionEvent e) {
	    level.movePlayer(Direction.DOWN);
 	}
     }

     private class ToggleGamePause extends AbstractAction {
 	@Override public void actionPerformed(final ActionEvent e) {
 	    level.setPaused(!level.isPaused());
 	}
     }

    private void initializeGameLoop() {
	final Action gameTick = new AbstractAction()
	{
	    public void actionPerformed(ActionEvent e) {
		level.tick();
		frame.setTitle(String.valueOf((int)level.getP().getY()) + " " + String.valueOf((int)level.getP().getX()));
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
