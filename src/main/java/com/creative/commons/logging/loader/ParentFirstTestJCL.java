package com.creative.commons.logging.loader;

import com.creative.commons.logging.LoggerWord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.spi.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Von Gosling
 */
public class ParentFirstTestJCL {
    public static void main(String[] args) throws Exception {

        URLClassLoader childLoader = new URLClassLoader(new URL[]{LoggerWordImpl.class.getResource(LoggerWordImpl.class.getSimpleName() + ".class"),
                Log.class.getProtectionDomain().getCodeSource().getLocation(),
                LoggerFactory.class.getProtectionDomain().getCodeSource().getLocation()}, ParentFirstTestJCL.class.getClassLoader());
        System.out.println(LoggerWordImpl.class.getResource(LoggerWordImpl.class.getSimpleName() + ".class"));
        System.out.println(Log.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println(LoggerFactory.class.getProtectionDomain().getCodeSource().getLocation());

        System.out.println(Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(childLoader);
        System.out.println(Thread.currentThread().getContextClassLoader());

        Log log = LogFactory.getLog(ParentFirstTestJCL.class);
        log.info("Hello, " + ParentFirstTestJCL.class.getName());

        Class<?> clazz = childLoader.loadClass(LoggerWordImpl.class.getName());
        LoggerWord lw = (LoggerWord) clazz.newInstance();
        lw.printWord("messi");
    }
}
