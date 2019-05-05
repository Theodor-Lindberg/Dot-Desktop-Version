package gui;

import game.BlockType;
import game.Direction;
import game.Game;
import game.Level;
import gui.LevelEditor.BlockPlacer;
import util.FileHandler;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

/**
 * This class handles the user interface and the game loop.
 */
public class LevelViewer
{
    private Level level;
    private Game game;
    private final JFrame frame;
    private LevelComponent levelComponent;
    private LevelEditor levelEditor = null;

    private final static String FRAME_TITLE;
    private final static String START_LEVEL;
    private final static Color BACKGROUND_COLOR;
    private final static EnumMap<BlockType, Color> BLOCK_COLOR_TABLE;

    static {
	FRAME_TITLE = "Dot";
	START_LEVEL = "level1.json";

	BACKGROUND_COLOR = new Color(23, 16, 22);

	BLOCK_COLOR_TABLE = new EnumMap<>(BlockType.class);
	BLOCK_COLOR_TABLE.put(BlockType.EMPTY, new Color(39, 32, 28, 255));
	BLOCK_COLOR_TABLE.put(BlockType.WALL, new Color(125, 190, 255, 255));
	BLOCK_COLOR_TABLE.put(BlockType.KEY, new Color(222, 0, 235, 255));
	BLOCK_COLOR_TABLE.put(BlockType.WALL1, new Color(131, 77, 235, 255));
	BLOCK_COLOR_TABLE.put(BlockType.WALL2, new Color(224, 197, 20, 255));
	BLOCK_COLOR_TABLE.put(BlockType.END, new Color(40, 242, 134, 255));
	BLOCK_COLOR_TABLE.put(BlockType.PLAYER, new Color(237, 228, 228, 255));
	BLOCK_COLOR_TABLE.put(BlockType.ENEMY, new Color(242, 0, 70, 255));
    }

    public LevelViewer(final Level level) {
	this.level = level;
	game = new Game(level);

	levelComponent = new GameComponent(game, BLOCK_COLOR_TABLE, BACKGROUND_COLOR);
	game.addListener(levelComponent);

	frame = new JFrame(FRAME_TITLE);
	initializeMenuBar();
	frame.setLayout(new BorderLayout());

	frame.add(levelComponent, BorderLayout.EAST);
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

    private void initializeMenuBar() {
	final JMenuBar menuBar = new JMenuBar();

	final JMenu menu = new JMenu("Options");

	final JMenuItem editorItem = new JMenuItem("Editor");
	final JMenuItem exitItem = new JMenuItem("Exit");
	final JMenu loadItem = new JMenu("Load Game");
	final JMenu loadIncludedLevelItem = new JMenu("Included Levels");
	final JMenuItem loadCustomLevelItem = new JMenuItem("Custom Level");

	try {
	    for (String defaultFile : FileHandler.getLevelsFromResources()) {
		final JMenuItem defLevelItem = new JMenuItem(defaultFile);
		defLevelItem.addActionListener(ev -> loadGame(defaultFile));
		loadIncludedLevelItem.add(defLevelItem);
	    }
	}
	catch(Exception e) {
	    Logger.log(java.util.logging.Level.SEVERE, getClass().getName(), "Could not list resource files.", e);
	}

	loadCustomLevelItem.addActionListener(ev -> loadGame(LevelChooser.chooseLevel()));
	editorItem.addActionListener(ev -> showLevelEditor());
	exitItem.addActionListener(ev -> exit());

	loadItem.add(loadCustomLevelItem);
	loadItem.add(loadIncludedLevelItem);
	menu.add(loadItem);
	menu.add(editorItem);
	menu.add(exitItem);
	menuBar.add(menu);

	frame.setJMenuBar(menuBar);
    }

    private void addKeyboardEvents() {
	JComponent pane = frame.getRootPane();

	final InputMap in = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	final ActionMap act = pane.getActionMap();

	addMotionEvent(in, act, KeyEvent.VK_LEFT, false, "MoveLeft", Direction.LEFT);
	addMotionEvent(in, act, KeyEvent.VK_LEFT, true, "ReleaseLeft", Direction.LEFT);

	addMotionEvent(in, act, KeyEvent.VK_RIGHT, false, "MoveRight", Direction.RIGHT);
	addMotionEvent(in, act, KeyEvent.VK_RIGHT, true, "ReleaseRight", Direction.RIGHT);

	addMotionEvent(in, act, KeyEvent.VK_UP, false, "MoveUp", Direction.UP);
	addMotionEvent(in, act, KeyEvent.VK_UP, true, "ReleaseUp", Direction.UP);

	addMotionEvent(in, act, KeyEvent.VK_DOWN, false, "MoveDown", Direction.DOWN);
	addMotionEvent(in, act, KeyEvent.VK_DOWN, true, "ReleaseDown", Direction.DOWN);

	in.put(KeyStroke.getKeyStroke("P"), "TogglePause");
	act.put("TogglePause", new ToggleGamePause());
	in.put(KeyStroke.getKeyStroke("R"), "RestartLevel");
	act.put("RestartLevel", new RestartLevel());
    }

    private void addMotionEvent(final InputMap in, final ActionMap act, final int key, final boolean onKeyRelease, final String name, final Direction direction) {
	in.put(KeyStroke.getKeyStroke(key, 0, onKeyRelease), name);
	act.put(name, new MotionAction(name, direction, onKeyRelease));
    }

    private final class MotionAction extends AbstractAction
    {
	private final Direction direction;
	private final boolean onKeyRelease;

	private MotionAction(final String name, final Direction direction, final boolean onKeyRelease)
	{
	    super(name);

	    this.direction = direction;
	    this.onKeyRelease = onKeyRelease;
	}

	public void actionPerformed(ActionEvent e)
	{
	    if (!onKeyRelease) {
		game.movePlayer(direction);
	    } else {
		game.removeDirection(direction);
	    }
	}
    }

    private class ToggleGamePause extends AbstractAction {
	@Override public void actionPerformed(final ActionEvent e) {
	    game.setPaused(!game.isPaused());
	}
    }

    private class RestartLevel extends AbstractAction {
	@Override public void actionPerformed(final ActionEvent e) {
	    game.restartLevel();
	}
    }

    private void showLevelEditor() {
        game.setPaused(true);
	levelEditor = new LevelEditor(level);
	frame.add(levelEditor, BorderLayout.WEST);
	final BlockPlacer blockPlacer = levelEditor.new BlockPlacer();
	frame.remove(levelComponent);
	levelComponent = new LevelComponent(level, BLOCK_COLOR_TABLE, BACKGROUND_COLOR);
	levelComponent.addMouseMotionListener(blockPlacer);
	levelComponent.addMouseListener(blockPlacer);
	frame.add(levelComponent);
	level.addListener(levelComponent);
	frame.pack();
    }

    private void loadGame(final String fileName) {
	if (fileName != null) {
	    try {
		level = new Level(fileName);
		game = new Game(level);
		frame.remove(levelComponent);
		levelComponent = new GameComponent(game, BLOCK_COLOR_TABLE, BACKGROUND_COLOR);
		game.addListener(levelComponent);
		frame.add(levelComponent);

		try {
		    frame.remove(levelEditor);
		} catch (RuntimeException e) {
		    Logger.log(java.util.logging.Level.FINE, this.getClass().getName(), "Tried to remove level editor component.", e);
		}
		frame.pack();
	    } catch (Exception e) {
		Logger.log(java.util.logging.Level.SEVERE, this.getClass().getName(), "Could not load level", e);
		JOptionPane.showMessageDialog(frame, "Could not load level.", "Level load error", JOptionPane.ERROR_MESSAGE);
	    }
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
		game.tick();
	    }
	};

	final Timer gameTimer = new Timer(16, gameTick);
	gameTimer.setCoalesce(true);
	gameTimer.start();
    }

    public static void main(String[] args) {
        try {
	    new LevelViewer(new Level(START_LEVEL));
	}
	catch (Exception e) {
	    Logger.log(java.util.logging.Level.SEVERE, "main", "Could not load level", e);
	    JOptionPane.showMessageDialog(null, "Could not load level.", "Level load error", JOptionPane.ERROR_MESSAGE);
	}
    }
}