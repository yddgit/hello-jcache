package com.my.project.jcache;

import javax.cache.event.*;
import java.io.Serializable;

/**
 * Event Listeners allow us to take actions upon triggering any of the event types defined in the EventType enum, which are:
 * <ul>
 *     <li>CREATED - {@link javax.cache.event.CacheEntryCreatedListener}</li>
 *     <li>UPDATED - {@link javax.cache.event.CacheEntryUpdatedListener}</li>
 *     <li>REMOVED - {@link javax.cache.event.CacheEntryRemovedListener}</li>
 *     <li>EXPIRED - {@link javax.cache.event.CacheEntryExpiredListener}</li>
 * </ul>
 */
public class SimpleCacheEntryListener implements
        CacheEntryCreatedListener<String, String>,
        CacheEntryUpdatedListener<String, String>,
        CacheEntryRemovedListener<String, String>,
        CacheEntryExpiredListener<String, String>, Serializable {

    private boolean created;
    private boolean updated;
    private boolean removed;
    private boolean expired;

    public boolean getCreated() {
        return created;
    }

    public boolean getUpdated() {
        return updated;
    }

    public boolean getRemoved() {
        return removed;
    }

    public boolean getExpired() {
        return expired;
    }

    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        this.created = true;
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        this.updated = true;
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        this.removed = true;
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends String, ? extends String>> cacheEntryEvents) throws CacheEntryListenerException {
        this.expired = true;
    }
}
