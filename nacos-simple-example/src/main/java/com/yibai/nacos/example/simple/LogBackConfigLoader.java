package com.yibai.nacos.example.simple;

/**
 * @author yb
 * @date 2022/5/9 17:05
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Simple Utility class for loading an external config file for logback
 *
 * @author daniel
 */
public class LogBackConfigLoader {

    public static void load(String fileName) throws IOException, JoranException {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ClassLoader classLoader = SimpleNacosExample.class.getClassLoader();
        URL url = classLoader.getResource(fileName);
        if (Objects.isNull(url)) {
            throw new IOException("Logback External Config File Parameter does not reference a file that exists");
        }
        String externalConfigFileLocation = url.getFile();
        File externalConfigFile = new File(externalConfigFileLocation);
        if (!externalConfigFile.exists()) {
        } else {
            if (!externalConfigFile.isFile()) {
                throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
            } else {
                if (!externalConfigFile.canRead()) {
                    throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
                } else {
                    JoranConfigurator configurator = new JoranConfigurator();
                    configurator.setContext(lc);
                    lc.reset();
                    configurator.doConfigure(externalConfigFileLocation);
                    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
                }
            }
        }
    }

}