package gui;

import game.Block;
import game.BlockType;
import game.Direction;
import game.Enemy;
import game.EnemyFactory;
import game.EnemyFactory.EnemyAI;
import game.Game;
import game.KeyBlock;
import game.Level;
import game.Player;
import util.Point2D;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static game.BlockType.*;
import static game.Movable.*;

/**
 * This is a component which lets the user edit a level with a graphical user interface.
 */
public class LevelEditor extends JPanel
{
    private final Level level;
    private final FittedComboBox<BlockType> blockTypeBox;
    private final FittedComboBox<BlockType> keyTargetTypeBox;
    private final FittedComboBox<Speed> speedBox;
    private final FittedComboBox<EnemyAI> aiBox;
    private final FittedComboBox<Direction> directionBox;
    private final List<BlockType> uniqueBlocksPlaced;

    public LevelEditor(final Level level) {
	this.level = level;
	uniqueBlocksPlaced = findUniqueBlocksPlaced(level);

	blockTypeBox = new FittedComboBox<>(new BlockType[] { EMPTY, WALL, WALL1, WALL2, KEY, ENEMY, PLAYER, END });
	keyTargetTypeBox = new FittedComboBox<>(new BlockType[] { WALL1, WALL2 });
	speedBox = new FittedComboBox<>(Speed.values());
	aiBox = new FittedComboBox<>(EnemyAI.values());
	directionBox = new FittedComboBox<>(Direction.values());

	setLayout( new BoxLayout(this, BoxLayout.PAGE_AXIS));
	setupButtons();
	addComboBoxes();
	setMaximumSize( this.getPreferredSize() );
    }

    private void setupButtons() {
	final JButton clearButton = new JButton("Clear level");
	clearButton.addActionListener(e -> clearLevel());
	addComponent(clearButton);

	final JButton saveButton = new JButton("Save level to file");
	saveButton.addActionListener(e -> saveLevel());
	addComponent(saveButton);
    }

    private void addComboBoxes() {
        addComponent(new JLabel("Block Type:"));
	addComponent(blockTypeBox);
	addComponent(new JLabel("Target block for key blocks:"));
	addComponent(keyTargetTypeBox);
	addComponent(new JLabel("Speed for enemies and player:"));
	addComponent(speedBox);
	addComponent(new JLabel("AI type for enemies:"));
	addComponent(aiBox);
	addComponent(new JLabel("Start direction for enemies:"));
	addComponent(directionBox);
    }

    private void addComponent(final JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(component);
    }

    private void clearLevel() {
	for (int x = 0; x < level.getWidth(); x++) {
	    for (int y = 0; y < level.getHeight(); y++) {
		level.insertBlockAt(x, y, new Block(EMPTY));
	    }
	}
    }

    private void saveLevel() {
	final String fileName = LevelChooser.saveLevelTo();
	if (fileName != null) {
	    try {
		level.saveToFile(fileName);
	    } catch (FileNotFoundException e) {
		Logger.log(java.util.logging.Level.SEVERE, this.getClass().getName(),
			   "Could not save level because the file was not found", e);
		JOptionPane.showMessageDialog(this, "Could not find file.", "Level save error", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception e) {
		Logger.log(java.util.logging.Level.SEVERE, this.getClass().getName(), "Could not save level", e);
		JOptionPane.showMessageDialog(this, "Could not save level.", "Level save error", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }

    private List<BlockType> findUniqueBlocksPlaced(final Level level) {
        final List<BlockType> uniqueBlocksPlaced = new ArrayList<>();
	for (BlockType blockType : BlockType.values()) {
	    if (Game.isBlockTypeUnique(blockType) && isBlockTypePlaced(level, blockType)) {
		uniqueBlocksPlaced.add(blockType);
	    }
	}
	return uniqueBlocksPlaced;
    }

    private boolean isBlockTypePlaced(final Level level, final BlockType blockType) {
	for (int y = 0; y < level.getHeight(); y++) {
	    for (int x = 0; x < level.getHeight(); x++) {
		if (level.getBlockAt(x, y).getBlockType() == blockType) {
		    return true;
		}
	    }
	}
	return false;
    }

    private Point convertToBlockPosition(final int mouseX, final int mouseY) {
        return new Point(mouseX / LevelComponent.BLOCK_SIZE, mouseY / LevelComponent.BLOCK_SIZE);
    }

    private void placeBlock(final int x, final int y, final Boolean remove) {
	final Point blockPosition = convertToBlockPosition(x, y);
	if (blockPosition.x >= 0 && blockPosition.x < level.getWidth() && blockPosition.y >= 0 && blockPosition.y < level.getHeight()) {
	    if (remove) {
		uniqueBlocksPlaced.remove(level.getBlockAt(blockPosition.x, blockPosition.y).getBlockType());
		level.insertBlockAt(blockPosition.x, blockPosition.y, new Block(EMPTY));
	    } else {
		final BlockType blockType = (BlockType) blockTypeBox.getSelectedItem();
		if (!uniqueBlocksPlaced.contains(blockType)) {
		    if (Game.isBlockTypeUnique(blockType)) {
		        uniqueBlocksPlaced.add(blockType);
		    }

		    if (blockType == KEY) {
			final KeyBlock block = new KeyBlock((BlockType) keyTargetTypeBox.getSelectedItem(), null, null);
			level.insertBlockAt(blockPosition.x, blockPosition.y, block);
		    } else if (blockType == ENEMY) {
			final EnemyFactory enemyFactory = new EnemyFactory(null);
			final Enemy enemy = enemyFactory.createEnemy(new Point2D(blockPosition.x, blockPosition.y), (Direction) directionBox.getSelectedItem(),
								     (Speed) speedBox.getSelectedItem(), (EnemyAI) aiBox.getSelectedItem());
			level.insertBlockAt(blockPosition.x, blockPosition.y, enemy);
		    } else if (blockType == PLAYER) {
			final Player player = new Player(new Point2D(blockPosition.x, blockPosition.y), (Speed) speedBox.getSelectedItem(),
							 null, null);
			level.insertBlockAt(blockPosition.x, blockPosition.y, player);
		    } else {
			level.insertBlockAt(blockPosition.x, blockPosition.y, new Block((BlockType) blockTypeBox.getSelectedItem()));
		    }
		}
	    }
	}
    }

    public class BlockPlacer extends MouseAdapter {
	@Override public void mouseClicked(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY(), Boolean.valueOf(SwingUtilities.isRightMouseButton(e)));
	}

	@Override public void mouseDragged(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY(), Boolean.valueOf(SwingUtilities.isRightMouseButton(e)));
	}
    }
}