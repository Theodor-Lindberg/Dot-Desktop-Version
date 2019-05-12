package gui;

import game.GameObjects.Block;
import game.GameObjects.BlockType;
import game.GameObjects.KeyBlock;
import game.LevelGrid;
import util.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

/**
 * Handles the rendering of the level.
 */
public class LevelComponent extends JComponent implements Observer
{
    private LevelGrid levelGrid;
    protected final EnumMap<BlockType, Color> blockColorTable;
    protected final Color backgroundColor;
    public final static int BLOCK_SIZE;
    protected final static int PADDING;

    static {
	BLOCK_SIZE = 18;
	PADDING = 4;
    }

    public LevelComponent(final LevelGrid levelGrid, final EnumMap<BlockType, Color> blockColorTable, final Color backgroundColor) {
	this.levelGrid = levelGrid;
	this.blockColorTable = blockColorTable;
	this.backgroundColor = backgroundColor;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	for (int y = 0; y < levelGrid.getHeight(); y++)
	    for (int x = 0; x < levelGrid.getWidth(); x++) {
		final Block block = levelGrid.getBlockAt(x, y);
		if (block.getBlockType() == BlockType.KEY) {
		    drawBackWithPadding(g2d, ((KeyBlock) block).getTargetBlock(), x, y);
		    g2d.setColor(backgroundColor);
		    g2d.fillRect(x * BLOCK_SIZE + PADDING * 2, y * BLOCK_SIZE + PADDING * 2, BLOCK_SIZE - PADDING * 3, BLOCK_SIZE - PADDING * 3);
		}
		else {
		    drawBackWithPadding(g2d, block.getBlockType(), x, y);
		}

	    }
    }

    protected void drawBackWithPadding(final Graphics2D g2d, final BlockType blockType, final int x, final int y) {
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE + PADDING, BLOCK_SIZE + PADDING);
	drawBlock(g2d, blockColorTable.get(blockType), x, y);
    }

    protected void drawBlock(final Graphics2D g2d, final Color color, final float x, final float y) {
	g2d.setColor(color);
	g2d.fillRect((int)(x * BLOCK_SIZE) + PADDING, (int)(y * BLOCK_SIZE + PADDING), BLOCK_SIZE - PADDING, BLOCK_SIZE - PADDING);
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(PADDING + BLOCK_SIZE * levelGrid.getWidth(), PADDING + BLOCK_SIZE * levelGrid.getHeight());
    }

    @Override public void observableUpdated() {
        this.repaint();
    }
}