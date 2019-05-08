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

    public void addObserver(final Observer observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
	for (Observer observer : observers) {
	    observer.observableUpdated();
	}
    }
}
