package gui;

import game.BlockType;
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
    private final static int BLOCKSIZE;
    private final static int PADDING;

    static {
        BLOCKSIZE = 18;
	PADDING = 4;
    }

    public LevelComponent(final Level level, final EnumMap<BlockType, Color> blockColorTable, final Color backgroundColor) {
	this.level = level;
	this.blockColorTable = blockColorTable;
	this.backgroundColor = backgroundColor;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D)g;

	for (int y = 0; y < level.getHeight(); y++)
	    for (int x = 0; x < level.getWidth(); x++) {
		drawBackWithPadding(g2d, blockColorTable.get(level.getBlockTypeAt(x, y)), x, y);
	    }

	Iterator<Moveable> movingObjects = level.getMovingObstaclesIterator();
	while (movingObjects.hasNext()) {
	    Moveable block = movingObjects.next();
	    drawBlock(g2d, blockColorTable.get(block.getBlockType()), block.getX(),
	    		  block.getY());
	}
    }

    private void drawBackWithPadding(Graphics2D g2d, Color color, int x, int y) {
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCKSIZE, y * BLOCKSIZE, BLOCKSIZE + PADDING, BLOCKSIZE + PADDING);
	drawBlock(g2d, color, x, y);
    }

    private void drawBlock(Graphics2D g2d, Color color, float x, float y) {
	g2d.setColor(color);
	g2d.fillRect((int)(x * BLOCKSIZE) + PADDING, (int)(y * BLOCKSIZE + PADDING), BLOCKSIZE - PADDING, BLOCKSIZE - PADDING);
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(PADDING + BLOCKSIZE * level.getWidth(), PADDING + BLOCKSIZE * level.getHeight());
    }

    @Override public void levelChanged() {
        this.repaint();
    }
}