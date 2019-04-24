package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Other classes can subscribe to classes that extends this class.
 */
public abstract class Publisher
{
    private List<Listener> listeners;

    protected Publisher() {
        listeners = new ArrayList<>();
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    protected void notifyListeners() {
	for (Listener listener : listeners) {
	    listener.publisherUpdated();
	}
    }
}
