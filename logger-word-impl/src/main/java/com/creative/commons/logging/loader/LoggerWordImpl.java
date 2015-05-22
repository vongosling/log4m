package com.creative.commons.logging.loader;

import com.creative.commons.logging.LoggerWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Von Gosling
 */
public class LoggerWordImpl implements LoggerWord {
    @Override
    public void printWord() {
        Log log = LogFactory.getLog(LoggerWordImpl.class);
        log.info("Print word in class " + this.toString());
    }

    @Override
    public String toString() {
        return getClass().getClassLoader().getClass().getName() + " -> " + getClass().getName();
    }
}
