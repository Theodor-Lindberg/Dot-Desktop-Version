package gui;

import javax.swing.JComboBox;
import java.awt.Dimension;
import java.util.Vector;

/**
 * Works as the normal JComboBox but the maximum height is set to the preferred height because some layouts
 * does not honor the preferred size.
 * @param <E> the type of the elements of this combo box
 */
public class FittedComboBox<E> extends JComboBox
{
    public FittedComboBox(final E[] items) {
	super(items);
    }

    @Override public Dimension getMaximumSize() {
	final Dimension maxDimension = super.getMaximumSize();
	return new Dimension(maxDimension.width, getPreferredSize().height);
    }
}
