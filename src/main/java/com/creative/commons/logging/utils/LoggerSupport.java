package com.creative.commons.logging.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author Von Gosling
 */
public abstract class LoggerSupport {
    private final static Logger LOG = LoggerFactory.getLogger(LoggerSupport.class);

    private LoggerSupport() {
    }

    private static final String LOG4J_FACTORY = "org.slf4j.impl.Log4jLoggerFactory";
    private static final String LOG4J_CONFIG_XML = "org.apache.log4j.xml.DOMConfigurator";
    private static final String LOG4J_CONFIG_PROP = "org.apache.log4j.PropertyConfigurator";
    private static final String LOGBACK_FACTORY = "ch.qos.logback.classic.LoggerContext";

    public static void resetLogger(String logFile) {
        if (StringUtils.isBlank(logFile)) {
            LOG.error("Null value for storm.client.logger configuration item !");
            return;
        }
        try {
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
            Class<?> classType = iLoggerFactory.getClass();
            if (LOG4J_FACTORY.equals(classType.getName())) {
                //LogManager.resetConfiguration();
                Class<?> logManagerCls = Class.forName("org.apache.log4j.LogManager");
                Method resetMethod = logManagerCls.newInstance().getClass()
                        .getMethod("resetConfiguration");
                resetMethod.invoke(logManagerCls.newInstance());

                //Configurator.configure(“");
                Class<?> configurator;
                String nameSuffix = logFile.substring(logFile.lastIndexOf(".") + 1);

                if ("xml".equals(nameSuffix)) {
                    configurator = Class.forName(LOG4J_CONFIG_XML);
                } else if ("properties".equals(nameSuffix)) {
                    configurator = Class.forName(LOG4J_CONFIG_PROP);
                } else {
                    LOG.error("Bad value->{} for storm.client.logger configuration item !",
                            logFile);
                    return;
                }

                Method configureMethod = configurator.getMethod("configure", URL.class);
                URL url = getContextClassLoader().getResource(logFile);
                configureMethod.invoke(configurator.newInstance(), url);
            } else if (LOGBACK_FACTORY.equals(classType.getName())) {
                //configurator.setContext(context);
                Class<?> context = Class.forName("ch.qos.logback.core.Context");
                Object contextObj = context.newInstance();
                Class<?> joranConfigurator = Class
                        .forName("ch.qos.logback.classic.joran.JoranConfigurator");
                Object joranConfiguratoroObj = joranConfigurator.newInstance();
                Method setContext = joranConfiguratoroObj.getClass().getMethod("setContext",
                        context);
                setContext.invoke(joranConfiguratoroObj, iLoggerFactory);

                //context.reset();
                Method reset = context.getMethod("reset");
                reset.invoke(contextObj);

                //configurator.doConfigure("logback.xml");
                URL url = getContextClassLoader().getResource(logFile);
                Method doConfigure = joranConfiguratoroObj.getClass().getMethod("doConfigure",
                        URL.class);
                doConfigure.invoke(joranConfiguratoroObj, url);
            }
        } catch (Exception e) {
            LOG.error("Initializing  custom log configuration error !", e);
        }
    }

    protected static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;

        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            // ignore
        }

        // Return the selected class loader
        return classLoader;
    }
}
