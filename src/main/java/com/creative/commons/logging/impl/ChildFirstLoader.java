package com.creative.commons.logging.impl;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * see <a href="http://grepcode.com/file/repo1.maven.org/maven2/org.apache.tomcat/tomcat-catalina/8.0.21/org/apache/catalina/loader/WebappClassLoaderBase.java#WebappClassLoaderBase">
 * 'Tomcat WebappClassLoaderBase' implementation</a>.
 *
 * @author Von Gosling
 */
public class ChildFirstLoader extends URLClassLoader {
    protected ChildFirstLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public URL getResource(String name) {
        URL url = findResource(name);
        if (url == null) {
            url = getParent().getResource(name);
        }
        return url;
    }


    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            //If not loaded,search the local resources
            if (c == null) {
                //long t0 = System.nanoTime();
                try {
                    c = findClass(name);
                } catch (ClassNotFoundException cnfe) {
                    //Ignore
                }

                // If still not found, delegate to parent
                if (c == null) {
                    if (getParent() != null) {
                        c = getParent().loadClass(name);
                    } else {
                        c = getSystemClassLoader().loadClass(name);
                    }
                }
                //long t1 = System.nanoTime();
                // this is the defining class loader; record the stats
                //sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                //sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                //sun.misc.PerfCounter.getFindClasses().increment();
            }
            if (resolve) {
                resolveClass(c);
            }

            return c;
        }
    }
}
