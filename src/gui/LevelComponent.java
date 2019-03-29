package gui;

import game.BlockType;
import game.Level;
import game.LevelListener;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

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
	final Graphics2D g2d = (Graphics2D) g;

	for (int y = 0; y < level.getHeight(); y++)
	    for (int x = 0; x < level.getWidth(); x++) {
		drawBackWithPadding(g2d, blockColorTable.get(level.getBlockAt(x, y)), x, y);
	    }
	drawBlock(g2d, blockColorTable.get(BlockType.PLAYER), (int)level.getPlayerPosition().getX(), (int)level.getPlayerPosition().getY());
    }

    private void drawBackWithPadding(Graphics2D g2d, Color color, int x, int y) {
	g2d.setColor(backgroundColor);
	g2d.fillRect(x * BLOCKSIZE, y * BLOCKSIZE, BLOCKSIZE + PADDING, BLOCKSIZE + PADDING);
	drawBlock(g2d, color, x, y);
    }

    private void drawBlock(Graphics2D g2d, Color color, int x, int y) {
	g2d.setColor(color);
	g2d.fillRect(x * BLOCKSIZE + PADDING, y * BLOCKSIZE + PADDING, BLOCKSIZE - PADDING, BLOCKSIZE - PADDING);
    }

    @Override public Dimension getPreferredSize() {

        return new Dimension(BLOCKSIZE * level.getWidth(), BLOCKSIZE * level.getHeight());
    }

    @Override public void levelChanged() {
        this.repaint();
    }
}