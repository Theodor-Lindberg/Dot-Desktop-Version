package gui;

import game.Block;
import game.BlockType;
import game.KeyBlock;
import game.Level;
import game.Level.LevelChanger;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.BlockType.*;

public class LevelEditor extends JPanel
{
    private final Level level;
    private final FittedComboBox<BlockType> blockTypeBox;
    private final FittedComboBox<BlockType> keyTargetTypeBox;
    private final LevelChanger levelChanger;

    public LevelEditor(final Level level) {
	this.level = level;
	this.levelChanger = level.requestLevelChanger(this);

	blockTypeBox = new FittedComboBox<>(new BlockType[] { EMPTY, WALL, WALL1, WALL2, KEY, ENEMY, START, END });
	keyTargetTypeBox = new FittedComboBox<>(new BlockType[] { EMPTY, WALL, WALL1, WALL2, KEY, ENEMY, START, END });

	setLayout( new BoxLayout(this, BoxLayout.Y_AXIS));
	setupButtons();
	setMaximumSize( this.getPreferredSize() );
    }

    private void setupButtons() {
	final String clearButtonText = "Clear";
	final var clearButton = new JButton(clearButtonText);
	clearButton.addActionListener(e -> clearLevel());
	this.add(clearButton);

	final String saveButtonText = "Save";
	final var saveButton = new JButton(saveButtonText);
	clearButton.addActionListener(e -> saveLevel());
	this.add(saveButton);

	this.add(blockTypeBox);
	this.add(keyTargetTypeBox);
    }

    private void clearLevel() {
	for (int x = 0; x < level.getWidth(); x++) {
	    for (int y = 0; y < level.getHeight(); y++) {
		level.createBlockAt(x, y, new Block(EMPTY));
	    }
	}
    }

    private void saveLevel() {

    }

    private void placeBlock(final int mouseX, final int mouseY) {
        final int x = mouseX / LevelComponent.BLOCK_SIZE;
        final int y = mouseY / LevelComponent.BLOCK_SIZE;
	if (x >= 0 && x < level.getWidth() && y >= 0 && y < level.getHeight()) {
	    final BlockType selectedBlockType = (BlockType)blockTypeBox.getSelectedItem();
	    level.createBlockAt(x, y, new Block(selectedBlockType));
	}
    }

    public class BlockPlacer extends MouseAdapter {
	@Override public void mouseClicked(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY());
	}

	@Override public void mouseDragged(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY());
	}
    }
}