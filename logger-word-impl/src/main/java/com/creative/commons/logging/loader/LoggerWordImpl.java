package com.creative.commons.logging.loader;

import com.creative.commons.logging.LoggerWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author Von Gosling
 */
public class LoggerWordImpl implements LoggerWord {
    @Override
    public void printWord(String name) {
        Log log = LogFactory.getLog(LoggerWordImpl.class);
        log.info("Print word " + name + " in class " + this.toString());
    }

    @Override
    public String toString() {
        return getClass().getClassLoader() + " -> " + getClass().getName();
    }
}
