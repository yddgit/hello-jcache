package com.my.project.jcache;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.Serializable;

/**
 * EntryProcessor allows us to modify Cache entries using atomic operations without having to re-add them to the Cache.
 */
public class SimpleEntryProcessor implements EntryProcessor<String, String, String>, Serializable {
    @Override
    public String process(MutableEntry<String, String> entry, Object... arguments) throws EntryProcessorException {
        if(entry.exists()) {
            String current = entry.getValue();
            entry.setValue(current + " - modified");
            return current;
        }
        return null;
    }
}
