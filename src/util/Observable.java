package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Other classes can subscribe to classes that extends this class.
 */
public abstract class Observable
{
    private final List<Observer> observers;

    protected Observable() {
	observers = new ArrayList<>();
    }

    /**
     * Add an observer to notify.
     *
     * @param observer The observer to add.
     */
    public void addObserver(final Observer observer) {
        observers.add(observer);
    }

    /**
     * Notify all observers to update.
     */
    protected void notifyObservers() {
	for (Observer observer : observers) {
	    observer.observableUpdated();
	}
    }
}
