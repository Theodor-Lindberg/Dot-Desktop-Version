package gui;

import game.Direction;
import game.Level;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener
{
    Level level;

    public Keyboard(final Level level) {
	this.level = level;
    }

    @Override public void keyTyped(final KeyEvent e) {
    }

    @Override public void keyPressed(final KeyEvent e) {
	switch(e.getKeyCode()) {
	    case KeyEvent.VK_UP:
	    case KeyEvent.VK_W:
	        level.movePlayer(Direction.UP);
	    case KeyEvent.VK_DOWN:
	    case KeyEvent.VK_S:
	        level.movePlayer(Direction.DOWN);
	    case KeyEvent.VK_RIGHT:
	    case KeyEvent.VK_D:
	        level.movePlayer(Direction.RIGHT);
	    case KeyEvent.VK_LEFT:
	    case KeyEvent.VK_A:
	        level.movePlayer(Direction.LEFT);
	    case KeyEvent.VK_SPACE:
	        level.setPaused(!level.isPaused());
	}
    }

    @Override public void keyReleased(final KeyEvent e) {

    }
}
