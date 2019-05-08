package util;

/**
 * To be notified when a property has changed one must implement this interface and subscribe to the publisher.
 */
public interface Observer
{
    void observableUpdated();
}