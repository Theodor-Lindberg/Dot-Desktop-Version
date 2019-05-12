package gui;

import game.GameObjects.Block;
import game.GameObjects.BlockType;
import game.GameObjects.MovingObjects.Direction;
import game.GameObjects.MovingObjects.Enemy;
import game.GameObjects.MovingObjects.EnemyFactory;
import game.GameObjects.MovingObjects.EnemyFactory.EnemyAI;
import game.Game;
import game.GameObjects.KeyBlock;
import game.Level;
import game.GameObjects.MovingObjects.Player;
import util.Point2D;
import util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static game.GameObjects.BlockType.*;
import static game.GameObjects.MovingObjects.Movable.*;

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

    /**
     * @param level The level to edit.
     */
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

    /**
     * Set up the clear and save button.
     */
    private void setupButtons() {
	final JButton clearButton = new JButton("Clear level");
	clearButton.addActionListener(e -> clearLevel());
	addComponent(clearButton);

	final JButton saveButton = new JButton("Save level to file");
	saveButton.addActionListener(e -> saveLevel());
	addComponent(saveButton);
    }

    /**
     * Set the combo boxes for choosing blocks and labels with helping descriptions.
     */
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

    /**
     * Add a child component with left alignment to the LevelEditor.
     *
     * @param component The component to add.
     */
    private void addComponent(final JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(component);
    }

    /**
     * Fill the entire level with empty blocks.
     */
    private void clearLevel() {
	for (int x = 0; x < level.getWidth(); x++) {
	    for (int y = 0; y < level.getHeight(); y++) {
		level.insertBlockAt(x, y, new Block(EMPTY));
	    }
	}
	uniqueBlocksPlaced.clear();
    }

    /**
     * Let user save the level to a file which they choose.
     */
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

    /**
     * Find all the unique blocks exists in the level.
     *
     * @param level 	The level search in.
     *
     * @return 		A list of all the unique blocks that exists in the level.
     */
    private List<BlockType> findUniqueBlocksPlaced(final Level level) {
        final List<BlockType> uniqueBlocksPlaced = new ArrayList<>();
	for (BlockType blockType : BlockType.values()) {
	    if (Game.isBlockTypeUnique(blockType) && isBlockTypePlaced(level, blockType)) {
		uniqueBlocksPlaced.add(blockType);
	    }
	}
	return uniqueBlocksPlaced;
    }

    /**
     * Check if a certain block type exists in the level.
     *
     * @param level 	The level to search in.
     * @param blockType The type of block to search for.
     *
     * @return 		True if the block type exists in the level.
     */
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

    /**
     * Calculate the coordinate of the block that was clicked on.
     *
     * @param mouseX 	The x coordinate of the mouse press.
     * @param mouseY 	The y coordinate of the mouse press.
     *
     * @return 		The coordinate of the block which was clicked on.
     */
    private Point convertToBlockPosition(final int mouseX, final int mouseY) {
        return new Point(mouseX / LevelComponent.BLOCK_SIZE, mouseY / LevelComponent.BLOCK_SIZE);
    }

    /**
     * Place a block in the level.
     *
     * @param x 	The x coordinate of the mouse press relative to the component.
     * @param y 	The y coordinate of the mouse press relative to the component.
     * @param blockType The block type to place.
     */
    private void placeBlock(final int x, final int y, final BlockType blockType) {
	final Point blockPosition = convertToBlockPosition(x, y);
	if (blockPosition.x >= 0 && blockPosition.x < level.getWidth() && blockPosition.y >= 0 && blockPosition.y < level.getHeight()) {
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
		    final Player player =
			    new Player(new Point2D(blockPosition.x, blockPosition.y), (Speed) speedBox.getSelectedItem(), null);
		    level.insertBlockAt(blockPosition.x, blockPosition.y, player);
		} else {
		    level.insertBlockAt(blockPosition.x, blockPosition.y, new Block(blockType));
		}
	    }
	}
    }

    /**
     * Listens for mouse events.
     */
    public class BlockPlacer extends MouseAdapter {
	@Override public void mouseClicked(final MouseEvent e) {
	    if (Boolean.valueOf(SwingUtilities.isRightMouseButton(e))) {
		placeBlock(e.getX(), e.getY(), EMPTY);
	    }
	    else {
		placeBlock(e.getX(), e.getY(), (BlockType) blockTypeBox.getSelectedItem());
	    }
	}

	@Override public void mouseDragged(final MouseEvent e) {
	    if (Boolean.valueOf(SwingUtilities.isRightMouseButton(e))) {
		placeBlock(e.getX(), e.getY(), EMPTY);
	    }
	    else {
		placeBlock(e.getX(), e.getY(), (BlockType) blockTypeBox.getSelectedItem());
	    }
	}
    }
}