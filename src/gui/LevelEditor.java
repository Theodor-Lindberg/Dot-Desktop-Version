package gui;

import game.BasicAI;
import game.Block;
import game.BlockType;
import game.Direction;
import game.Enemy;
import game.EnemyFactory;
import game.EnemyFactory.EnemyAI;
import game.Game;
import game.Game.LevelChanger;
import game.KeyBlock;
import game.Movable;
import game.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static game.BlockType.*;
import static game.Movable.*;

public class LevelEditor extends JPanel
{
    private final Game game;
    private final FittedComboBox<BlockType> blockTypeBox;
    private final FittedComboBox<BlockType> keyTargetTypeBox;
    private final FittedComboBox<Speed> speedBox;
    private final FittedComboBox<EnemyAI> aiBox;
    private final FittedComboBox<Direction> directionBox;
    private final LevelChanger levelChanger;

    public LevelEditor(final Game game) {
	this.game = game;
	this.levelChanger = game.requestLevelChanger(this);

	blockTypeBox = new FittedComboBox<>(new BlockType[] { EMPTY, WALL, WALL1, WALL2, KEY, ENEMY, START, END });
	keyTargetTypeBox = new FittedComboBox<>(new BlockType[] { EMPTY, WALL, WALL1, WALL2, KEY, ENEMY, START, END });
	speedBox = new FittedComboBox<>(Speed.values());
	aiBox = new FittedComboBox<>(EnemyAI.values());
	directionBox = new FittedComboBox<>(Direction.values());

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
	this.add(speedBox);
	this.add(directionBox);
	this.add(aiBox);

    }

    private void clearLevel() {
	for (int x = 0; x < game.getWidth(); x++) {
	    for (int y = 0; y < game.getHeight(); y++) {
		game.createBlockAt(x, y, new Block(EMPTY));
	    }
	}
    }

    private void saveLevel() {

    }

    private Point convertToBlockPosition(final int mouseX, final int mouseY) {
        return new Point(mouseX / LevelComponent.BLOCK_SIZE, mouseY / LevelComponent.BLOCK_SIZE);
    }

    private void placeBlock(final int x, final int y, final Boolean remove) {
	final Point blockPosition = convertToBlockPosition(x, y);
	if (blockPosition.x >= 0 && blockPosition.x < game.getWidth() && blockPosition.y >= 0 && blockPosition.y < game.getHeight()) {
	    if (remove) {
		game.createBlockAt(blockPosition.x, blockPosition.y, new Block(BlockType.EMPTY));
	    }
	    else {
	        final BlockType blockType = (BlockType) blockTypeBox.getSelectedItem();
	        final Block block;
	        if (blockType == KEY) {
	            block = new KeyBlock((BlockType) keyTargetTypeBox.getSelectedItem(), null, null);
		    game.createBlockAt(blockPosition.x, blockPosition.y, block);
		}
		else if (blockType == ENEMY) {
		    EnemyFactory ef = new EnemyFactory(game);
		    Enemy e = ef.createEnemy(new Point2D(blockPosition.x,blockPosition.y), (Direction)directionBox.getSelectedItem(), (Speed)speedBox.getSelectedItem(), (EnemyAI)aiBox.getSelectedItem());
		    game.createBlockAt(blockPosition.x, blockPosition.y, (Block)e);
		}
		else {
		    game.createBlockAt(blockPosition.x, blockPosition.y, new Block((BlockType) blockTypeBox.getSelectedItem()));
		}
	    }
	}
    }

    public class BlockPlacer extends MouseAdapter {
	@Override public void mouseClicked(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));
	}

	@Override public void mouseDragged(final MouseEvent e) {
	    placeBlock(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));
	}
    }
}