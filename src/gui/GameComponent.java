package gui;

import game.objects.BlockType;
import game.Game;
import game.objects.movables.Movable;
import util.Point2D;

import java.awt.*;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * This class handles the rendering of the game.
 */
public class GameComponent extends LevelComponent
{
    private final Game game;

    private final static String VICTORY_TEXT;
    private final static Font VICTORY_TEXT_FONT;
    private final static Point2D VICTORY_TEXT_POSITION;

    static {
	VICTORY_TEXT = "You Won!";
	VICTORY_TEXT_POSITION = new Point2D(5, 5);
	VICTORY_TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 70);
    }

    /**
     * @param game 		The Game to draw.
     * @param blockColorTable 	The color scheme for the blocks.
     * @param backgroundColor 	The background color of the game.
     */
    public GameComponent(final Game game, final EnumMap<BlockType, Color> blockColorTable, final Color backgroundColor)
    {
	super(game, blockColorTable, backgroundColor);
	this.game = game;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;

	final Iterator<Movable> movingObjects = game.getMovingObjectsIterator();
	while (movingObjects.hasNext()) {
	    final Movable movingObject = movingObjects.next();
	    drawBlock(g2d, blockColorTable.get(movingObject.getBlockType()), movingObject.getX(), movingObject.getY());
	}

	if (game.isLevelCompleted()) {
	    showVictoryText(g2d);
	}
    }

    @Override protected void drawBackWithPadding(final Graphics2D g2d, final BlockType blockType, final int x, final int y) {
	Color color = blockColorTable.get(blockType);
	if (game.isLevelCompleted()) {
	    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
	}
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE + PADDING, BLOCK_SIZE + PADDING);
	drawBlock(g2d, color, x, y);
    }

    /**
     * Show the victory text on the screen.
     *
     * @param g2d The graphics object to draw with.
     */
    private void showVictoryText(final Graphics2D g2d) {
	g2d.setColor(Color.white);
	g2d.setFont(VICTORY_TEXT_FONT);
	g2d.drawString(VICTORY_TEXT, (game.getWidth() * BLOCK_SIZE) / VICTORY_TEXT_POSITION.getX(), (game.getHeight() * BLOCK_SIZE) / VICTORY_TEXT_POSITION.getY());
    }
}
