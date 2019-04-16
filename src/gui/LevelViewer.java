package gui;

import game.BlockType;
import game.Direction;
import game.Game;
import game.Level;

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
    private Game game;
    private JFrame frame;
    private LevelComponent levelComponent;
    private LevelEditor levelEditor;
    private final static String FRAME_TITLE;

    static {
	FRAME_TITLE = "Dot";
    }

    public LevelViewer(final Game game) {
	this.game = game;

	final Color backgroundColor = new Color(23,16,22);

	levelComponent = new LevelComponent(game, getBlockColorTable(), backgroundColor);
	game.subscribeListener(levelComponent);

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

    private EnumMap<BlockType, Color> getBlockColorTable() {
	EnumMap<BlockType, Color> polyColorTable = new EnumMap<>(BlockType.class);

	final Color emptyColor = new Color(39, 32, 28, 255);
	polyColorTable.put(BlockType.EMPTY, emptyColor);

	final Color wallColor = new Color(125, 190, 255, 255);
	polyColorTable.put(BlockType.WALL, wallColor);

	final Color keyColor = new Color(222, 0, 235, 255);
	polyColorTable.put(BlockType.KEY, keyColor);

	final Color wall1Color = new Color(131, 77, 235, 255);
	polyColorTable.put(BlockType.WALL1, wall1Color);

	final Color wall2Color = new Color(224, 197, 20, 255);
	polyColorTable.put(BlockType.WALL2, wall2Color);

	final Color endColor = new Color(40, 242, 134, 255);
	polyColorTable.put(BlockType.END, endColor);

	final Color playerColor = new Color(237, 228, 228, 255);
	polyColorTable.put(BlockType.PLAYER, playerColor);

	final Color enemyColor = new Color(242, 0, 70, 255);
	polyColorTable.put(BlockType.ENEMY, enemyColor);
	return polyColorTable;
    }

    private void initializeMenuBar() {
	final JMenuBar menuBar = new JMenuBar();

	final String menuText = "Options";
	final JMenu menu = new JMenu(menuText);
	final String helpMenuText = "Help";
	final String gameMenuText = "Load Game";
	final String editorMenuText = "Editor";
	final String exitMenuText = "Exit";


	final JMenuItem helpItem = new JMenuItem(helpMenuText);
	final JMenuItem gameItem = new JMenuItem(gameMenuText);
	final JMenuItem editorItem = new JMenuItem(editorMenuText);
	final JMenuItem exitItem = new JMenuItem(exitMenuText);

	gameItem.addActionListener(ev -> showGame());
	editorItem.addActionListener(ev -> showLevelEditor());
	exitItem.addActionListener(ev -> exit());

	menu.add(helpItem);
	menu.add(gameItem);
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

        private MotionAction(String name, final Direction direction, final boolean onKeyRelease)
        {
            super(name);

            this.direction = direction;
            this.onKeyRelease = onKeyRelease;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!onKeyRelease)
            	game.movePlayer(direction);
            else
                game.removeDirection(direction);
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
	levelEditor = new LevelEditor(game);
	frame.add(levelEditor, BorderLayout.WEST);
	final LevelEditor.BlockPlacer blockPlacer = levelEditor.new BlockPlacer();
	levelComponent.addMouseMotionListener(blockPlacer);
	levelComponent.addMouseListener(blockPlacer);
	frame.pack();
    }

    private void showGame() {
        frame.remove(levelEditor);
        frame.pack();
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
	new LevelViewer(new Game(new Level("level.json")));
    }
}