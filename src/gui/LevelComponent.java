package gui;

import game.Block;
import game.BlockType;
import game.Game;
import game.KeyBlock;
import game.LevelListener;
import game.Movable;
import game.Point2D;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * Handles the rendering of the game.
 */
public class LevelComponent extends JComponent implements LevelListener
{
    private final Game game;
    private final EnumMap<BlockType, Color> blockColorTable;
    private final Color backgroundColor;
    public final static int BLOCK_SIZE;
    private final static int PADDING;

    private final static String VICTORY_TEXT;
    private final static Font VICTORY_TEXT_FONT;
    private final static Point2D VICTORY_TEXT_POSITION;

    static {
	BLOCK_SIZE = 18;
	PADDING = 4;
	VICTORY_TEXT = "You Won!";
	VICTORY_TEXT_POSITION = new Point2D(5, 5);
	VICTORY_TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 70);
    }

    public LevelComponent(final Game game, final EnumMap<BlockType, Color> blockColorTable, final Color backgroundColor) {
	this.game = game;
	this.blockColorTable = blockColorTable;
	this.backgroundColor = backgroundColor;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	for (int y = 0; y < game.getHeight(); y++)
	    for (int x = 0; x < game.getWidth(); x++) {
		final Block block = game.getBlockAt(x, y);
		final BlockType blockType = (block.getBlockType() == BlockType.KEY) ? ((KeyBlock) block).getTargetBlock() : block.getBlockType();
		drawBackWithPadding(g2d, blockColorTable.get(blockType), x, y);

	    }

	Iterator<Movable> movingObjects = game.getMovingObstaclesIterator();
	while (movingObjects.hasNext()) {
	    final Movable movingObject = movingObjects.next();
	    drawBlock(g2d, blockColorTable.get(movingObject.getBlockType()), movingObject.getX(), movingObject.getY());
	}

	if (game.isLevelCompleted()) {
	    showVictoryText(g2d);
	}
    }

    private void drawBackWithPadding(final Graphics2D g2d, Color color, final int x, final int y) {
        if (game.isLevelCompleted()) {
	    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 2);
	}
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE + PADDING, BLOCK_SIZE + PADDING);
	drawBlock(g2d, color, x, y);
    }

    private void drawBlock(final Graphics2D g2d, final Color color, final float x, final float y) {
	g2d.setColor(color);
	g2d.fillRect((int)(x * BLOCK_SIZE) + PADDING, (int)(y * BLOCK_SIZE + PADDING), BLOCK_SIZE - PADDING, BLOCK_SIZE - PADDING);
    }

    private void showVictoryText(final Graphics2D g2d) {
	g2d.setColor(Color.white);
	g2d.setFont(VICTORY_TEXT_FONT);
	g2d.drawString(VICTORY_TEXT, (game.getWidth() * BLOCK_SIZE) / VICTORY_TEXT_POSITION.getX(), (game.getHeight() * BLOCK_SIZE) / VICTORY_TEXT_POSITION.getY());
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(PADDING + BLOCK_SIZE * game.getWidth(), PADDING + BLOCK_SIZE * game.getHeight());
    }

    @Override public void levelChanged() {
        this.repaint();
    }
}