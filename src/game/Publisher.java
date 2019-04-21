package game;

import java.util.ArrayList;
import java.util.List;

public abstract class Publisher
{
    private List<Listener> listeners;

    protected Publisher() {
        listeners = new ArrayList<>();
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners() {
	for (Listener listener : listeners) {
	    listener.publisherUpdated();
	}
    }
}
