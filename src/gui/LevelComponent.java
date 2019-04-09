package gui;

import game.Block;
import game.BlockType;
import game.KeyBlock;
import game.Level;
import game.LevelListener;
import game.Moveable;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Iterator;

public class LevelComponent extends JComponent implements LevelListener
{
    private final Level level;
    private final EnumMap<BlockType, Color> blockColorTable;
    private final Color backgroundColor;
    private final static int BLOCK_SIZE;
    private final static int PADDING;

    static {
	BLOCK_SIZE = 18;
	PADDING = 4;
    }

    public LevelComponent(final Level level, final EnumMap<BlockType, Color> blockColorTable, final Color backgroundColor) {
	this.level = level;
	this.blockColorTable = blockColorTable;
	this.backgroundColor = backgroundColor;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;

	for (int y = 0; y < level.getHeight(); y++)
	    for (int x = 0; x < level.getWidth(); x++) {
		Block block = level.getBlockAt(x, y);
		BlockType blockType = (block.getBlockType() == BlockType.KEY) ? ((KeyBlock) block).getTargetBlock() : block.getBlockType();
		drawBackWithPadding(g2d, blockColorTable.get(blockType), x, y);

	    }

	Iterator<Moveable> movingObjects = level.getMovingObstaclesIterator();
	while (movingObjects.hasNext()) {
	    Moveable movingObject = movingObjects.next();
	    drawBlock(g2d, blockColorTable.get(movingObject.getBlockType()), movingObject.getX(), movingObject.getY());
	}
    }

    private void drawBackWithPadding(Graphics2D g2d, Color color, int x, int y) {
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE + PADDING, BLOCK_SIZE + PADDING);
	drawBlock(g2d, color, x, y);
    }

    private void drawBlock(Graphics2D g2d, Color color, float x, float y) {
	g2d.setColor(color);
	g2d.fillRect((int)(x * BLOCK_SIZE) + PADDING, (int)(y * BLOCK_SIZE + PADDING), BLOCK_SIZE - PADDING, BLOCK_SIZE - PADDING);
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(PADDING + BLOCK_SIZE * level.getWidth(), PADDING + BLOCK_SIZE * level.getHeight());
    }

    @Override public void levelChanged() {
        this.repaint();
    }
}